""""Cross-regression contracts for shared app focus and horizontal Dock collapse ownership."""
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
ADAPTER = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/AppAdapter.java").read_text(encoding="utf-8")
BORDER = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockBreathingBorderView.java").read_text(encoding="utf-8")


class HomeFocusIntegrationRegressionTest(unittest.TestCase):
    def test_grid_and_dock_share_the_same_cyan_focus_view(self):
        self.assertIn("if (focused) {", ADAPTER)
        self.assertNotIn("focused && dockTile", ADAPTER)
        self.assertIn("breathingBorder.setVisibility(View.VISIBLE);", ADAPTER)
        self.assertIn("breathingBorder.setVisibility(View.GONE);", ADAPTER)
        self.assertIn("breathAnimator.setDuration(1600)", BORDER)
        self.assertIn("TvUiTokens.ACCENT_CYAN", BORDER)

    def test_collapse_owns_focus_until_idle_geometry_is_committed(self):
        focus_start = MAIN.index("private void focusDockTile(int position)")
        focus_end = MAIN.index("private void focusDockTileWhenLaidOut", focus_start)
        focus_method = MAIN[focus_start:focus_end]
        self.assertIn("if (horizontalDockCollapseInProgress) return;", focus_method)

        key_start = MAIN.index("public boolean onTileVerticalKey(int position, int keyCode)")
        key_end = MAIN.index("private int pendingFocus;", key_start)
        key_method = MAIN[key_start:key_end]
        self.assertIn("if (horizontalDockCollapseInProgress)", key_method)
        self.assertIn("if (horizontalDockCollapseInProgress) return true;", key_method)
        self.assertNotIn("pendingDockCollapseDirection =", key_method)
        self.assertLess(key_method.index("if (horizontalDockCollapseInProgress)"),
                        key_method.index("int count = adapter.getItemCount();"))

    def test_focus_is_precommitted_before_grid_is_hidden(self):
        start = MAIN.index("private void collapseHorizontalOverflowToDock")
        end = MAIN.index("private void animateHorizontalDockTransition", start)
        collapse = MAIN[start:end]
        self.assertIn("prepareDockFocusBeforeCollapse", collapse)
        prepare = collapse[collapse.index("private void prepareDockFocusBeforeCollapse"):
                           collapse.index("private void startHorizontalDockCollapse")]
        self.assertIn("card.requestFocus()", prepare)
        self.assertIn("card.isFocused()", prepare)
        animation = collapse[collapse.index("private void startHorizontalDockCollapse"):
                             collapse.index("private void abortHorizontalDockCollapse")]
        self.assertIn("grid.setVisibility(View.GONE)", animation)
        self.assertIn("dispatchPendingDockCollapseDirection(dockPosition)", animation)
        self.assertNotIn("restoreDockFocusAfterCollapse", MAIN)
        # Header fallback is forbidden while the Grid-to-Dock focus transaction is being
        # prepared or animated. A separately captured second UP may legitimately navigate
        # from the now-focused Dock to the top shelf after the transaction commits.
        self.assertNotIn("focusTopBarFallback()", prepare + animation)

    def test_captured_second_up_is_dispatched_only_after_dock_focus_commit(self):
        start = MAIN.index("private void startHorizontalDockCollapse")
        end = MAIN.index("private void abortHorizontalDockCollapse", start)
        animation = MAIN[start:end]
        commit = animation.index("grid.setVisibility(View.GONE)")
        settled = animation.index("horizontalDockCollapseInProgress = false;", commit)
        dispatch = animation.index("dispatchPendingDockCollapseDirection(dockPosition)", settled)
        self.assertLess(commit, settled)
        self.assertLess(settled, dispatch)
        pending_start = MAIN.index("private void dispatchPendingDockCollapseDirection")
        pending_end = MAIN.index("private void animateHorizontalDockTransition", pending_start)
        pending = MAIN[pending_start:pending_end]
        self.assertIn("direction == KeyEvent.KEYCODE_DPAD_UP", pending)
        self.assertIn("focusTopBarFallback()", pending)

    def test_grid_has_no_competing_static_focus_overlay(self):
        self.assertNotIn("grid.addItemDecoration(new GridFocusDecoration(glider));", MAIN)
        self.assertNotIn("glider.addCompanion(grid);", MAIN)


if __name__ == "__main__":
    unittest.main()
