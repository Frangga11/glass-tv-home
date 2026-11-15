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
        self.assertIn("dockRecycler.setTranslationY(-getHorizontalDockPitch() - delta)", listener)
        self.assertIn("!horizontalDockCollapseInProgress", listener)

    def test_expansion_captures_scroll_baseline_and_collapse_resets_it(self):
        self.assertIn("horizontalGridScrollPixels = 0;", MAIN)
        self.assertIn("horizontalGridScrollPixels = 0;", MAIN)
        self.assertIn("horizontalGridExpanded = false;", MAIN)

    def test_normal_up_from_first_grid_row_collapses_to_home_header(self):
        start = MAIN.index("public boolean onTileVerticalKey")
        end = MAIN.index("private int pendingFocus;", start)
        method = MAIN[start:end]
        self.assertIn("collapseHorizontalOverflowToHomeHeader(dockTarget)", method)
        self.assertIn("pendingDockCollapseDirection = KeyEvent.KEYCODE_DPAD_UP", MAIN)

    def test_move_mode_keeps_row_one_up_as_a_hard_boundary(self):
        start = MAIN.index("private void stepMove(int keyCode)")
        end = MAIN.index("public void onUserInteraction()", start)
        method = MAIN[start:end]
        self.assertIn("if (!dockVertical && keyCode == KeyEvent.KEYCODE_DPAD_UP && movingPos < COLUMNS)", method)
        self.assertIn("// unpin/promote the selected app into the Dock or end the active move.", method)
        row_one_up = method[method.index("if (!dockVertical && keyCode == KeyEvent.KEYCODE_DPAD_UP"):method.index("if (dockVertical && keyCode == KeyEvent.KEYCODE_DPAD_LEFT")]
        self.assertNotIn("moveTileAcrossRegion", row_one_up)

    def test_moved_grid_identity_is_revealed_without_ending_move_mode(self):
        start = MAIN.index("private void restoreMovingTile")
        end = MAIN.index("private void stepMove(int keyCode)", start)
        method = MAIN[start:end]
        self.assertIn("recycler.scrollToPosition(position)", method)
        self.assertIn("restoreMovingTile(inDock, key, position, previousCard, attemptsRemaining - 1)", method)
        timeout = method[method.index("if (holder == null)"):method.index("View nextCard", method.index("if (holder == null)"))]
        self.assertIn("adapter.positionOf(key)", timeout)
        self.assertIn("restoreMovingTile(false, key, resolved, previousCard, 8)", timeout)
        persistent = timeout[timeout.index("else if (!inDock"):timeout.index("} else {", timeout.index("else if (!inDock"))]
        self.assertNotIn("finishMove();", persistent)


if __name__ == "__main__":
    unittest.main()
