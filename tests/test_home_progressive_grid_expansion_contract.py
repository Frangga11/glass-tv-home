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
        self.assertIn("rv.computeVerticalScrollOffset() - horizontalGridScrollBaseOffset", listener)
        self.assertIn("dockRecycler.setTranslationY(-getHorizontalDockPitch() - delta)", listener)
        self.assertIn("!horizontalDockCollapseInProgress", listener)

    def test_expansion_captures_scroll_baseline_and_collapse_resets_it(self):
        self.assertIn("horizontalGridScrollBaseOffset = grid.computeVerticalScrollOffset();", MAIN)
        self.assertIn("horizontalGridScrollBaseOffset = 0;", MAIN)
        self.assertIn("horizontalGridExpanded = false;", MAIN)

    def test_normal_up_from_first_grid_row_collapses_to_home_header(self):
        start = MAIN.index("public boolean onTileVerticalKey")
        end = MAIN.index("private int pendingFocus;", start)
        method = MAIN[start:end]
        self.assertIn("collapseHorizontalOverflowToDock(dockTarget)", method)
        self.assertIn("pendingDockCollapseDirection = KeyEvent.KEYCODE_DPAD_UP", method)

    def test_move_mode_keeps_row_one_up_as_a_hard_boundary(self):
        start = MAIN.index("private void stepMove(int keyCode)")
        end = MAIN.index("public void onUserInteraction()", start)
        method = MAIN[start:end]
        self.assertIn("if (!dockVertical && keyCode == KeyEvent.KEYCODE_DPAD_UP && movingPos < COLUMNS)", method)
        self.assertIn("// unpin/promote the selected app into the Dock or end the active move.", method)
        self.assertNotIn("moveTileAcrossRegion(movingKey, true, targetDockPosition)", method)

    def test_moved_grid_identity_is_revealed_without_ending_move_mode(self):
        start = MAIN.index("private void restoreMovingTile")
        end = MAIN.index("private void stepMove(int keyCode)", start)
        method = MAIN[start:end]
        self.assertIn("recycler.scrollToPosition(position)", method)
        self.assertIn("restoreMovingTile(inDock, key, position, previousCard, attemptsRemaining - 1)", method)
        self.assertNotIn("finishMove();", method)


if __name__ == "__main__":
    unittest.main()
