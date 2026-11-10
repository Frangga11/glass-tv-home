"""Regression contracts for pre-committed Dock focus during collapse."""
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text()


class HomeDpadFocusHandoffTest(unittest.TestCase):
    def test_vertical_input_is_captured_once_before_android_focus_search(self):
        start = MAIN.index("public boolean dispatchKeyEvent(KeyEvent event)")
        end = MAIN.index("@Override\\n    public boolean onTileVerticalKey", start)
        dispatch = MAIN[start:end]
        capture = dispatch.index("if (horizontalDockCollapseInProgress")
        fallback = dispatch.index("return super.dispatchKeyEvent(event);")
        self.assertLess(capture, fallback)
        self.assertIn("event.getRepeatCount() == 0", dispatch)
        self.assertIn("pendingDockCollapseDirection = code", dispatch)

    def test_dock_focus_is_committed_before_collapse_animation(self):
        start = MAIN.index("private void collapseHorizontalOverflowToDock")
        end = MAIN.index("private void animateHorizontalDockTransition", start)
        collapse = MAIN[start:end]
        self.assertIn("prepareDockFocusBeforeCollapse(dockPosition, transition, 12)", collapse)
        prepare_start = collapse.index("private void prepareDockFocusBeforeCollapse")
        animate_start = collapse.index("private void startHorizontalDockCollapse")
        prepare = collapse[prepare_start:animate_start]
        self.assertIn("card.requestFocus()", prepare)
        self.assertIn("card.isFocused()", prepare)
        self.assertIn("startHorizontalDockCollapse(target, transition, gridFocusRequestGeneration)", prepare)
        self.assertNotIn("focusTopBarFallback()", collapse)

    def test_grid_is_hidden_only_after_dock_owns_focus(self):
        start = MAIN.index("private void startHorizontalDockCollapse")
        end = MAIN.index("private void abortHorizontalDockCollapse", start)
        collapse = MAIN[start:end]
        self.assertLess(collapse.index("card"), len(collapse)) if "card" in collapse else None
        self.assertIn("focusGeneration != gridFocusRequestGeneration", collapse)
        self.assertIn("grid.setVisibility(View.GONE)", collapse)
        self.assertIn("horizontalDockCollapseInProgress = false", collapse)
        self.assertNotIn("restoreDockFocusAfterCollapse", MAIN)

    def test_failed_precommit_aborts_without_losing_grid_focus(self):
        start = MAIN.index("private void abortHorizontalDockCollapse")
        end = MAIN.index("private int getHorizontalDockPitch", start)
        abort = MAIN[start:end]
        self.assertIn("horizontalDockCollapseInProgress = false", abort)
        self.assertIn("grid.setVisibility(View.VISIBLE)", abort)
        self.assertIn("dockRecycler.setTranslationY(-getHorizontalDockPitch())", abort)
        self.assertNotIn("focusTopBarFallback()", abort)

    def test_pending_direction_uses_semantic_navigation_not_synthetic_key_replay(self):
        start = MAIN.index("private void dispatchPendingDockCollapseDirection")
        end = MAIN.index("private void animateHorizontalDockTransition", start)
        method = MAIN[start:end]
        self.assertIn("DockLayoutModel.gridPositionForDock", method)
        self.assertIn("focusTile(target)", method)
        self.assertIn("focusTopBarFallback()", method)
        self.assertNotIn("super.dispatchKeyEvent(new KeyEvent", MAIN)
        self.assertNotIn("new KeyEvent(KeyEvent.ACTION_DOWN", MAIN)
        self.assertNotIn("pendingDockCollapseDirections", MAIN)

    def test_grid_listener_does_not_duplicate_transition_input(self):
        start = MAIN.index("public boolean onTileVerticalKey(int position, int keyCode)")
        end = MAIN.index("private int pendingFocus;", start)
        method = MAIN[start:end]
        self.assertIn("if (horizontalDockCollapseInProgress) return true;", method)
        self.assertNotIn("pendingDockCollapseDirection =", method)


if __name__ == "__main__":
    unittest.main()
