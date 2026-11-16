"""Regression contracts for progressive horizontal Grid viewport and persistent app Move Mode."""
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")


class ProgressiveGridExpansionContractTest(unittest.TestCase):
    def test_dock_tracks_grid_scroll_by_the_same_pixel_delta(self):
        start = MAIN.index("grid.addOnScrollListener")
        end = MAIN.index("// Liquid feel:", start)
        listener = MAIN[start:end]
        self.assertIn("horizontalGridScrollPixels += dy;", listener)
        self.assertIn("dockRecycler.setTranslationY(dockRaisedTranslationY())", listener)
        self.assertIn("horizontalGridViewportAnimator == null", listener)
        helper = MAIN[MAIN.index("private float dockRaisedTranslationY"):MAIN.index("private void setGridViewportRowsImmediate")]
        self.assertIn("horizontalGridScrollPixels", helper)
        self.assertIn("(horizontalGridViewportRows - 1) * gridRowPitchPx()", helper)
        self.assertIn("!horizontalDockCollapseInProgress", listener)

    def test_expansion_captures_scroll_baseline_and_collapse_resets_it(self):
        self.assertIn("horizontalGridScrollPixels = 0;", MAIN)
        self.assertIn("horizontalGridScrollPixels = 0;", MAIN)
        self.assertIn("horizontalGridExpanded = false;", MAIN)

    def test_normal_up_from_first_grid_row_collapses_and_keeps_dock_focus(self):
        start = MAIN.index("public boolean onTileVerticalKey")
        end = MAIN.index("private int pendingFocus;", start)
        method = MAIN[start:end]
        self.assertIn("collapseHorizontalOverflowToDock(dockTarget)", method)

    def test_move_mode_keeps_row_one_up_as_a_hard_boundary(self):
        start = MAIN.index("private void stepMove(int keyCode)")
        end = MAIN.index("public void onUserInteraction()", start)
        method = MAIN[start:end]
        self.assertIn("if (!dockVertical && keyCode == KeyEvent.KEYCODE_DPAD_UP && movingPos < COLUMNS)", method)
        self.assertIn("It never ends Move Mode and never moves focus to the Home header.", method)
        row_one_up = method[method.index("if (!dockVertical && keyCode == KeyEvent.KEYCODE_DPAD_UP"):method.index("if (dockVertical && keyCode == KeyEvent.KEYCODE_DPAD_LEFT")]
        # UP from Grid row one hands the app to Dock row one; a full Dock keeps Move Mode alive.
        self.assertIn("moveTileAcrossRegion(movingKey, true", row_one_up)
        survive = MAIN[MAIN.index("private void moveTileAcrossRegion"):MAIN.index("private void restoreMovingTile")]
        self.assertIn("startMove(key, !toDock)", survive)

    def test_three_row_window_keeps_target_at_bottom_and_preserves_rows_above(self):
        self.assertIn("return Math.max(0, targetRow - 2);", MAIN)
        self.assertIn("int windowStartRow = gridWindowStartRow(targetRow);", MAIN)
        self.assertIn("return targetRow >= 2 ? 3 : 1;", MAIN)
        self.assertIn("currentRow, windowStartRow, rowPitch, currentTopOffset", MAIN)
        self.assertIn("int expectedWindowStartRow = gridWindowStartRow(position / COLUMNS);", MAIN)

    def test_three_row_mode_hides_home_chrome_but_keeps_clock_and_date(self):
        start = MAIN.index("private void setThreeRowFrameChromeHidden")
        end = MAIN.index("private void hideGlass()", start)
        method = MAIN[start:end]
        for view_id in ("R.id.heroShelf", "R.id.islamicShelf", "R.id.locationPill",
                        "R.id.weatherAirQuality", "R.id.navPill", "R.id.topButtons",
                        "R.id.nowPlayingCard", "R.id.prayerTimes"):
            self.assertIn(view_id, method)
        self.assertIn("View.INVISIBLE", method)
        self.assertIn("threeRowOriginalVisibility.get(i)", method)
        self.assertNotIn("R.id.leftBlock", method)
        self.assertIn("setThreeRowFrameChromeHidden(false)", MAIN)

    def test_three_row_navigation_triggers_chrome_hide_only_at_row_three(self):
        start = MAIN.index("private void startHorizontalGridRowTransition")
        end = MAIN.index("private void animateHorizontalGridRowScroll", start)
        method = MAIN[start:end]
        # Row three joins the frame through animateGridViewportRows(true, ...), which hides the chrome.
        self.assertIn("gridViewportRowsFor(target / COLUMNS) > horizontalGridViewportRows", method)
        self.assertIn("animateGridViewportRows(true", method)
        self.assertNotIn("targetRow >= 2", method)
        grow = MAIN[MAIN.index("private void animateGridViewportRows"):MAIN.index("private void animateHorizontalDockTransition")]
        self.assertIn("setThreeRowFrameChromeHidden(grow)", grow)
        self.assertIn("horizontalGridViewportRows = 3;", grow)

    def test_moved_grid_identity_is_revealed_without_ending_move_mode(self):
        start = MAIN.index("private void restoreMovingTile")
        end = MAIN.index("private void stepMove(int keyCode)", start)
        method = MAIN[start:end]
        self.assertIn("alignHorizontalGridRowToGlass(position)", method)
        self.assertIn("restoreMovingTile(inDock, key, position, previousCard, attemptsRemaining - 1)", method)
        timeout = method[method.index("if (holder == null)"):method.index("View nextCard", method.index("if (holder == null)"))]
        self.assertIn("adapter.positionOf(key)", timeout)
        self.assertIn("restoreMovingTile(false, key, resolved, previousCard, 8)", timeout)
        persistent = timeout[timeout.index("else if (!inDock"):timeout.index("} else {", timeout.index("else if (!inDock"))]
        self.assertNotIn("finishMove();", persistent)

    def test_third_row_grows_stable_three_row_viewport_without_shrinking_on_up(self):
        layout = MAIN[MAIN.index("private void applyDockLayout"):MAIN.index("private boolean sameLayoutParams")]
        self.assertIn("(horizontalGridViewportRows - 1) * gridRowPitchPx()", layout)
        settle = MAIN[MAIN.index("private void settleHorizontalGridRowTransition"):MAIN.index("private void focusHorizontalGridRowTargetWhenLaidOut")]
        self.assertNotIn("animateGridViewportRows(false", settle)
        self.assertIn("gridWindowStartRow(targetRow)", settle)
        self.assertIn("gridViewportRowsFor(target / COLUMNS) > horizontalGridViewportRows", MAIN)

    def test_collapse_lowers_the_whole_frame_and_resets_rows(self):
        collapse = MAIN[MAIN.index("private void startHorizontalDockCollapse"):MAIN.index("private void abortHorizontalDockCollapse")]
        self.assertIn("final float gridDrop = pitch + (horizontalGridViewportRows - 1) * gridRowPitchPx();", collapse)
        self.assertIn("horizontalGridViewportRows = 1;", collapse)
        self.assertIn("setThreeRowFrameChromeHidden(false)", collapse)

    def test_header_never_receives_focus_while_frame_or_move_mode_is_open(self):
        method = MAIN[MAIN.index("private boolean focusTopBarFallback"):MAIN.index("private void focusLastTile")]
        self.assertIn("movingPos >= 0", method)
        self.assertIn("horizontalGridExpanded", method)

    def test_raised_dock_up_collapses_frame_instead_of_leaving_for_header(self):
        keys = MAIN[MAIN.index("public boolean onDockTileKey("):MAIN.index("public boolean onDockTileHeld(")]
        self.assertIn("if (horizontalGridExpanded) {\n                    collapseHorizontalOverflowToDock(position);", keys)


if __name__ == "__main__":
    unittest.main()
