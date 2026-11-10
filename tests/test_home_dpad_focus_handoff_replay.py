"""Regression contracts for reliable Dock focus hand-off and DPAD replay during collapse."""
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text()


class HomeDpadFocusHandoffReplayTest(unittest.TestCase):
    def test_vertical_dpad_is_captured_before_android_focus_search(self):
        dispatch_start = MAIN.index("public boolean dispatchKeyEvent(KeyEvent event)")
        dispatch_end = MAIN.index("@Override\n    public boolean onTileVerticalKey", dispatch_start)
        dispatch = MAIN[dispatch_start:dispatch_end]
        capture = dispatch.index("if (horizontalDockCollapseInProgress")
        super_dispatch = dispatch.index("return super.dispatchKeyEvent(event);")
        self.assertLess(capture, super_dispatch)
        self.assertIn("pendingDockCollapseDirections.add(code)", dispatch)
        self.assertIn("MAX_PENDING_DOCK_COLLAPSE_DIRECTIONS", dispatch)

    def test_dock_focus_is_not_committed_until_a_visible_card_owns_focus(self):
        start = MAIN.index("private void restoreDockFocusAfterCollapse")
        end = MAIN.index("private void replayPendingDockCollapseDirections", start)
        restore = MAIN[start:end]
        self.assertIn("card.isShown() && card.isFocusable()", restore)
        self.assertIn("card.requestFocus()", restore)
        self.assertIn("attemptsRemaining > 0", restore)
        self.assertIn("postOnAnimation", restore)
        self.assertIn("32", MAIN[MAIN.index("private void collapseHorizontalOverflowToDock"):start])
        self.assertNotIn("focusTopBarFallback();", restore[:restore.index("if (attemptsRemaining > 0)")])

    def test_queued_directions_replay_only_after_focus_acquisition(self):
        start = MAIN.index("private void restoreDockFocusAfterCollapse")
        end = MAIN.index("private void animateHorizontalDockTransition", start)
        collapse_flow = MAIN[start:end]
        self.assertLess(collapse_flow.index("replayPendingDockCollapseDirections();"),
                        collapse_flow.index("private void replayPendingDockCollapseDirections"))
        replay_start = MAIN.index("private void replayPendingDockCollapseDirections")
        replay_end = MAIN.index("private void animateHorizontalDockTransition", replay_start)
        replay = MAIN[replay_start:replay_end]
        self.assertIn("super.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyCode))", replay)
        self.assertIn("super.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, keyCode))", replay)
        self.assertIn("pendingDockCollapseDirections.clear()", replay)

    def test_no_silent_drop_guard_remains_for_grid_vertical_keys(self):
        start = MAIN.index("public boolean onTileVerticalKey(int position, int keyCode)")
        end = MAIN.index("private int pendingFocus;", start)
        method = MAIN[start:end]
        self.assertIn("pendingDockCollapseDirections.add(keyCode)", method)
        self.assertNotIn("if (horizontalDockCollapseInProgress) return true;", method)


if __name__ == "__main__":
    unittest.main()
