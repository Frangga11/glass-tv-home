"""Regression contracts for deterministic Dock focus hand-off during collapse."""
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

    def test_focus_restore_requires_a_visible_focusable_dock_card(self):
        start = MAIN.index("private void restoreDockFocusAfterCollapse")
        end = MAIN.index("private void dispatchPendingDockCollapseDirection", start)
        restore = MAIN[start:end]
        self.assertIn("card.isShown() && card.isFocusable()", restore)
        self.assertIn("card.requestFocus()", restore)
        self.assertIn("card.isFocused()", restore)
        self.assertIn("postOnAnimation", restore)
        self.assertIn("attemptsRemaining > 0", restore)

    def test_pending_direction_uses_semantic_navigation_not_synthetic_key_replay(self):
        start = MAIN.index("private void dispatchPendingDockCollapseDirection")
        end = MAIN.index("private void animateHorizontalDockTransition", start)
        method = MAIN[start:end]
        self.assertIn("DockLayoutModel.gridPositionForDock", method)
        self.assertIn("focusTile(target)", method)
        self.assertIn("focusTopBarFallback()", method)
        self.assertNotIn("super.dispatchKeyEvent", MAIN)
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
