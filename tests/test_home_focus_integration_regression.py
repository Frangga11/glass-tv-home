"""Cross-regression contracts for shared app focus and horizontal Dock collapse ownership."""
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
        self.assertIn("pendingDockCollapseDirections.add(keyCode)", key_method)
        self.assertLess(key_method.index("if (horizontalDockCollapseInProgress)"),
                        key_method.index("int count = adapter.getItemCount();"))

    def test_collapse_commits_one_translation_and_restores_same_dock_position(self):
        start = MAIN.index("private void collapseHorizontalOverflowToDock")
        end = MAIN.index("private void animateHorizontalDockTransition", start)
        collapse = MAIN[start:end]
        self.assertIn("dockRecycler.setTranslationY(0f);", collapse)
        self.assertIn("grid.setTranslationY(0f);", collapse)
        self.assertIn("applyDockLayout();", collapse)
        self.assertIn("restoreDockFocusAfterCollapse(", collapse)
        self.assertIn("dockPosition, generation, 32", collapse)
        self.assertLess(collapse.index("dockRecycler.setTranslationY(0f);"),
                        collapse.index("applyDockLayout();"))

    def test_grid_has_no_competing_static_focus_overlay(self):
        self.assertNotIn("grid.addItemDecoration(new GridFocusDecoration(glider));", MAIN)
        self.assertNotIn("glider.addCompanion(grid);", MAIN)


if __name__ == "__main__":
    unittest.main()
