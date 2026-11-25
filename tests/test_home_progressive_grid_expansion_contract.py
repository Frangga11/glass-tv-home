"""Regression contracts for progressive horizontal Grid viewport and persistent app Move Mode."""
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
APP_ADAPTER = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/AppAdapter.java").read_text(encoding="utf-8")
GLIDER = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/Glider.java").read_text(encoding="utf-8")


class ProgressiveGridExpansionContractTest(unittest.TestCase):
    def test_dock_tracks_grid_scroll_by_the_same_pixel_delta(self):
        start = MAIN.index("grid.addOnScrollListener")
        end = MAIN.index("btnInputs = findViewById", start)
        listener = MAIN[start:end]
        self.assertIn("measuredGridScrollPixels(horizontalGridScrollPixels + dy)", listener)
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
        self.assertIn("return Math.max(0, targetRow - 1);", MAIN)
        self.assertIn("int windowStartRow = gridWindowStartRow(targetRow);", MAIN)
        self.assertIn("return targetRow >= 1 ? 2 : 1;", MAIN)
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
        self.assertIn("private void reassertThreeRowChrome()", MAIN)

    def test_three_row_navigation_triggers_chrome_hide_only_at_row_three(self):
        start = MAIN.index("private void startHorizontalGridRowTransition")
        end = MAIN.index("private void animateHorizontalGridRowScroll", start)
        method = MAIN[start:end]
        # Row three joins the frame through animateGridViewportRows(true, ...), which hides the chrome.
        self.assertIn("gridViewportRowsFor(target / COLUMNS) > horizontalGridViewportRows", method)
        self.assertIn("animateGridViewportRows(true", method)
        self.assertNotIn("targetRow >= 2", method)
        grow = MAIN[MAIN.index("private void animateGridViewportRows"):MAIN.index("private void animateHorizontalDockTransition")]
        self.assertIn("if (grow) setThreeRowFrameChromeHidden(true);", grow)
        self.assertIn("horizontalGridViewportRows = 2;", grow)

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


class ThreeRowFramePopupContractTest(unittest.TestCase):
    def test_up_from_grid_row_two_focuses_raised_dock_without_collapsing(self):
        method = MAIN[MAIN.index("public boolean onTileVerticalKey"):MAIN.index("private int pendingFocus;")]
        self.assertIn("horizontalGridViewportRows >= 2", method)
        self.assertIn("focusRaisedDockTile(dockTarget)", method)
        self.assertLess(method.index("focusRaisedDockTile(dockTarget)"), method.index("collapseHorizontalOverflowToDock(dockTarget)"))
        raised = MAIN[MAIN.index("private void focusRaisedDockTile"):MAIN.index("private void focusDockTile(int position)")]
        self.assertNotIn("collapseHorizontalOverflowToDock", raised)

    def test_dock_move_down_opens_grid_and_restarts_move_mode(self):
        method = MAIN[MAIN.index("private void moveTileAcrossRegion"):MAIN.index("private void restoreMovingTile")]
        self.assertIn("moveHandoffPending = true;", method)
        self.assertIn("expandHorizontalDockForOverflow(Math.max(0, adapter.positionOf(key)), generation, key)", method)
        expand = MAIN[MAIN.index("private void expandHorizontalDockForOverflow(int targetPosition, int requestGeneration, String moveKey)"):MAIN.index("private void collapseHorizontalOverflowToHomeHeader")]
        self.assertIn("startMoveWhenLaidOut(moveKey, false, resolvedMovePosition, 8)", expand)
        dispatch = MAIN[MAIN.index("public boolean dispatchKeyEvent"):MAIN.index("public boolean onTileVerticalKey")]
        self.assertIn("moveHandoffPending && movingPos < 0", dispatch)

    def test_raised_dock_left_right_keeps_three_row_frame_open(self):
        start = MAIN.index("public boolean onDockTileKey")
        end = MAIN.index("private int pendingFocus;", start)
        method = MAIN[start:end]
        self.assertIn("horizontalGridViewportRows >= 2", method)
        self.assertIn("focusRaisedDockTile(target)", method)
        self.assertIn("if (horizontalGridExpanded) {\n                    collapseHorizontalOverflowToDock(position);", method)
        raised = MAIN[MAIN.index("private void focusRaisedDockTile"):MAIN.index("private void focusDockTile(int position)")]
        self.assertIn("focusRaisedDockTileWhenLaidOut", raised)
        self.assertIn("card.requestFocus() && card.isFocused()", raised)

    def test_move_mode_dock_down_inserts_into_latest_dock_column(self):
        across = MAIN[MAIN.index("private void moveTileAcrossRegion"):MAIN.index("private void restoreMovingTile")]
        self.assertIn("dockAdapter.positionOf(key)", across)
        self.assertIn("gridPositionForDock(liveDockPosition, false, COLUMNS", across)
        self.assertIn("unpinFromDockAtGridPosition(key, Math.max(0, liveGridPosition))", across)
        step = MAIN[MAIN.index("private void stepMove(int keyCode)"):MAIN.index("public void onUserInteraction()")]
        self.assertIn("moveTileAcrossRegion(movingKey, false, currentPosition)", step)
        adapter = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/AppAdapter.java").read_text(encoding="utf-8")
        self.assertIn("DockLayoutModel.insertAtPosition(finalGrid, selected, gridPosition)", adapter)

    def test_move_mode_does_not_leave_recycler_item_animation_ghosts(self):
        self.assertNotIn("DefaultItemAnimator moveSlide", MAIN)
        self.assertNotIn("recycler.setItemAnimator(moveSlide)", MAIN)
        start = MAIN[MAIN.index("private void startMove(String key"):MAIN.index("private void startMoveWhenLaidOut")]
        self.assertIn("recycler.setItemAnimator(null)", start)
        finish = MAIN[MAIN.index("private void finishMoveState"):MAIN.index("private void moveTileAcrossRegion")]
        self.assertIn("movingCard.animate().cancel()", finish)
        self.assertIn("movingCard.setRotation(0f)", finish)
        self.assertIn("finishMoveForHandoff();", MAIN[MAIN.index("private void moveTileAcrossRegion"):MAIN.index("private void restoreMovingTile")])
        across = MAIN[MAIN.index("private void moveTileAcrossRegion"):MAIN.index("private void restoreMovingTile")]
        transfer = across[across.index("finishMoveForHandoff();"):across.index("int liveDockPosition")]
        self.assertNotIn("glider.hideNow()", transfer)
        self.assertIn("moveHandoffPending = true;", MAIN[MAIN.index("private void moveTileAcrossRegion"):MAIN.index("private void restoreMovingTile")])

    def test_scroll_offset_uses_measured_grid_geometry(self):
        self.assertIn("private int measuredGridScrollPixels", MAIN)
        self.assertIn("horizontalGridScrollBaselinePixels", MAIN)
        self.assertIn("measuredGridAbsoluteScrollPixels(horizontalGridScrollPixels)", MAIN)
        grow = MAIN[MAIN.index("private void animateGridViewportRows"):MAIN.index("private void animateHorizontalDockTransition")]
        self.assertIn("dockRecycler.setAlpha(1f);", grow)

    def test_hidden_chrome_is_reasserted_after_focus_changes(self):
        self.assertIn("if (horizontalThreeRowFrameActive) root.post(this::reassertThreeRowChrome);", MAIN)


    def test_move_mode_uses_latest_selected_identity_and_vertical_column(self):
        start = MAIN.index("private void stepMove(int keyCode)")
        end = MAIN.index("public void onUserInteraction()", start)
        method = MAIN[start:end]
        self.assertIn("movingKey = movingDock", method)
        self.assertIn("adapter.positionOf(movingKey)", method)
        self.assertIn("DockLayoutModel.gridNeighborPosition(", method)
        self.assertIn("currentPosition, direction, COLUMNS, last + 1", method)
        self.assertIn("movingPos = resolvedPosition;", method)
        self.assertIn("restoreMovingTile(false, movingKey, resolvedPosition", method)

    def test_third_overall_row_expands_two_grid_rows_at_second_grid_row(self):
        helper = MAIN[MAIN.index("private static int gridViewportRowsFor"):MAIN.index("private int gridRowPitchPx")]
        self.assertIn("return targetRow >= 1 ? 2 : 1;", helper)
        transition = MAIN[MAIN.index("private void startHorizontalGridRowTransition"):MAIN.index("private void animateHorizontalGridRowScroll")]
        self.assertIn("gridViewportRowsFor(target / COLUMNS) > horizontalGridViewportRows", transition)



class FocusShapeAndMoveModeSyncContractTest(unittest.TestCase):
    def test_dock_focus_coordinates_include_visual_ancestor_translation(self):
        self.assertIn("private void offsetByAncestorTranslation(View card, Rect bounds)", MAIN)
        self.assertIn("offsetByAncestorTranslation(card, r);", MAIN)
        self.assertIn("if (inDock) glider.moveToScreen(", MAIN)

    def test_grid_scroll_does_not_apply_to_dock_owned_glider(self):
        listener = MAIN[MAIN.index("grid.addOnScrollListener"):MAIN.index("btnInputs = findViewById")]
        self.assertIn("isCurrentFocusInside(dockRecycler)", listener)
        self.assertIn("syncFocusedDockGlider();", listener)
        self.assertIn("glider.scrolled(dy);", listener)

    def test_dock_glider_tracks_each_frame_of_dock_transitions(self):
        self.assertIn("glider.moveToScreenImmediately(", MAIN)
        viewport = MAIN[MAIN.index("private void animateGridViewportRows"):MAIN.index("private void animateHorizontalDockTransition")]
        dock = MAIN[MAIN.index("private void animateHorizontalDockTransition"):MAIN.index("private void focusTile(int position)")]
        collapse = MAIN[MAIN.index("private void startHorizontalDockCollapse"):MAIN.index("private void abortHorizontalDockCollapse")]
        self.assertIn("if (!moveHandoffPending) syncFocusedAppGlider();", viewport)
        self.assertIn("if (!moveHandoffPending) syncFocusedAppGlider();", dock)
        self.assertIn("syncFocusedDockGlider();", collapse)

    def test_home_chrome_restores_only_after_three_row_shrink_finishes(self):
        method = MAIN[MAIN.index("private void animateGridViewportRows"):MAIN.index("private void animateHorizontalDockTransition")]
        # Growing hides chrome before motion; shrinking must not restore it until final geometry
        # has committed in the animator end callback.
        self.assertIn("if (grow) setThreeRowFrameChromeHidden(true);", method)
        self.assertNotIn("setThreeRowFrameChromeHidden(grow);", method)
        end = method[method.index("@Override public void onAnimationEnd"):method.index("animator.start();")]
        self.assertLess(end.index("horizontalGridViewportRows = 1;"), end.index("setThreeRowFrameChromeHidden(false);"))
        self.assertIn("if (!grow) setThreeRowFrameChromeHidden(false);", end)

    def test_move_mode_dock_down_uses_live_dock_slot(self):
        step = MAIN[MAIN.index("private void stepMove(int keyCode)"):MAIN.index("public void onUserInteraction()")]
        self.assertIn("int currentPosition = dockAdapter.positionOf(movingKey);", step)
        self.assertIn("moveTileAcrossRegion(movingKey, false, currentPosition)", step)
        across = MAIN[MAIN.index("private void moveTileAcrossRegion"):MAIN.index("private void restoreMovingTile")]
        self.assertIn("dockAdapter.positionOf(key)", across)
        self.assertIn("gridPositionForDock(liveDockPosition, false, COLUMNS", across)
        self.assertIn("unpinFromDockAtGridPosition(key, Math.max(0, liveGridPosition))", across)

    def test_move_mode_dock_down_repartitions_before_preserving_column(self):
        method = APP_ADAPTER[APP_ADAPTER.index("boolean unpinFromDockAtGridPosition"):
                             APP_ADAPTER.index("private void replacePresentationOrder")]
        self.assertIn("presentationSnapshot = null;", method)
        self.assertIn("DockLayoutModel.Snapshot<Entry> updatedSnapshot = dockSnapshot();", method)
        self.assertIn("new ArrayList<>(updatedSnapshot.gridItems)", method)
        self.assertIn("new ArrayList<>(updatedSnapshot.dockItems)", method)
        self.assertIn("insertAtPosition(finalGrid, selected, gridPosition)", method)

    def test_row_scroll_holds_glider_until_destination_focus_settles(self):
        listener = MAIN[MAIN.index("grid.addOnScrollListener"):MAIN.index("btnInputs = findViewById")]
        self.assertIn("else if (!horizontalGridTransitionRunning)", listener)
        self.assertIn("glider.scrolled(dy);", listener)
        self.assertIn("private void syncFocusedAppGlider()", MAIN)
        transition = MAIN[MAIN.index("private void animateHorizontalDockTransition"):
                          MAIN.index("private void focusTile(int position)")]
        self.assertIn("if (!moveHandoffPending) syncFocusedAppGlider();", transition)
        viewport = MAIN[MAIN.index("private void animateGridViewportRows"):
                         MAIN.index("private void animateHorizontalDockTransition")]
        self.assertIn("if (!moveHandoffPending) syncFocusedAppGlider();", viewport)

    def test_grid_focus_is_acquired_after_reveal_so_glider_starts_at_dock_icon(self):
        expand = MAIN[MAIN.index("private void expandHorizontalDockForOverflow(int targetPosition, int requestGeneration, String moveKey)"):
                      MAIN.index("private void collapseHorizontalOverflowToHomeHeader")]
        self.assertNotIn("focusGridCardImmediately", expand)
        self.assertNotIn("focusedBeforeMotion", expand)
        self.assertLess(expand.index("animateHorizontalDockTransition(pitch, true"),
                        expand.index("focusTile(targetPosition)"))

    def test_three_row_and_move_mode_focus_suppress_home_shelf_copy(self):
        focused = MAIN[MAIN.index("private void handleTileFocused"):MAIN.index("public boolean onDockTileKey")]
        self.assertIn("horizontalThreeRowFrameActive || movingPos >= 0 || moveHandoffPending", focused)
        self.assertIn("if (horizontalThreeRowFrameActive) reassertThreeRowChrome();", focused)
        start = MAIN[MAIN.index("private void startMove(String key"):MAIN.index("private void startMoveWhenLaidOut")]
        self.assertIn("hideShelfImmediately();", start)


    def test_move_mode_uses_one_glider_and_removes_black_shadow(self):
        start = MAIN[MAIN.index("private void startMove(String key"):MAIN.index("private void startMoveWhenLaidOut")]
        restore = MAIN[MAIN.index("private void restoreMovingTile"):MAIN.index("private void stepMove")]
        finish = MAIN[MAIN.index("private void finishMoveState(boolean preserveMoveGlider)"):MAIN.index("private void moveTileAcrossRegion")]
        self.assertIn("glider.setMoveModeActive(true, isGlassEffectsEnabled());", start)
        self.assertIn("glider.setMoveModeActive(true, isGlassEffectsEnabled());", restore)
        self.assertIn("if (!preserveMoveGlider) {", finish)
        self.assertIn("glider.setMoveModeActive(false, isGlassEffectsEnabled());", finish)
        mode = GLIDER[GLIDER.index("void setMoveModeActive"):GLIDER.index("void setStyle")]
        self.assertIn("style = active || glassEnabled ? GLASS : DARK;", mode)
        self.assertIn("paneHidden = false;", mode)
        self.assertNotIn("shadowMix", GLIDER)
        self.assertNotIn("drawShadow", GLIDER)
        self.assertNotIn("Bitmap shadow", GLIDER)

    def test_grid_focus_glide_is_spring_retargeted_without_snapping_each_frame(self):
        sync = MAIN[MAIN.index("private void syncFocusedAppGlider()"):
                    MAIN.index("@Override\n    public void onTileFocused")]
        self.assertIn("glider.moveTo(left + pad, top + pad,", sync)
        self.assertNotIn("glider.moveToVisualImmediately(", sync)
        self.assertIn("glider.moveToScreenImmediately(", sync)
        self.assertIn("if (!moveHandoffPending) syncFocusedAppGlider();", MAIN)
        self.assertIn("Math.max(240, 240 * Math.abs(targetRow - currentRow))", MAIN)

    def test_cross_region_move_keeps_one_glider_and_rim_springs_from_source_to_target(self):
        across = MAIN[MAIN.index("private void moveTileAcrossRegion"):MAIN.index("private void restoreMovingTile")]
        self.assertIn("finishMoveForHandoff();", across)
        self.assertIn("moveHandoffPending = true;", across)
        transfer = across[across.index("finishMoveForHandoff();"):across.index("int liveDockPosition")]
        self.assertNotIn("glider.hideNow()", transfer)
        finish = MAIN[MAIN.index("private void finishMoveForHandoff"):MAIN.index("private void moveTileAcrossRegion")]
        self.assertIn("finishMoveState(true);", finish)
        self.assertIn("if (!preserveMoveGlider) {", finish)
        self.assertIn("glider.setMoveModeActive(false, isGlassEffectsEnabled());", finish)
        self.assertIn("if (!moveHandoffPending) syncFocusedAppGlider();", MAIN)

    def test_normal_dock_to_grid_glide_starts_from_dock_after_reveal(self):
        expand = MAIN[MAIN.index("private void expandHorizontalDockForOverflow(int targetPosition, int requestGeneration, String moveKey)"):
                      MAIN.index("private void collapseHorizontalOverflowToHomeHeader")]
        self.assertIn("focusTile(targetPosition);", expand)
        self.assertIn("Keep focus on the source Dock tile during the reveal.", expand)
        self.assertNotIn("focusGridCardImmediately", MAIN)

    def test_up_collapse_restores_home_shelves_without_a_followup_focus_key(self):
        collapse = MAIN[MAIN.index("private void startHorizontalDockCollapse"):
                        MAIN.index("private void revealHomeChromeAfterDockCollapse")]
        self.assertIn("setThreeRowFrameChromeHidden(false);", collapse)
        self.assertIn("if (!horizontalGridExpanded) revealHomeChromeAfterDockCollapse(dockPosition);", collapse)
        restore = MAIN[MAIN.index("private void revealHomeChromeAfterDockCollapse"):
                       MAIN.index("private void abortHorizontalDockCollapse")]
        self.assertIn("showHero(position);", restore)
        self.assertIn("heroShelf.setAlpha(1f);", restore)
        self.assertIn("islamicShelf.setAlpha(1f);", restore)



if __name__ == "__main__":
    unittest.main()
