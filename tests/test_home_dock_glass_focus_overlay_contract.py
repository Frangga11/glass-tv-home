from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
GRID_DECORATION = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/GridFocusDecoration.java").read_text(encoding="utf-8")
GLIDER = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/Glider.java").read_text(encoding="utf-8")


class HomeDockGlassFocusOverlayContractTest(unittest.TestCase):
    def test_grid_uses_shared_glider_companion_rendering(self):
        self.assertIn("grid.addItemDecoration(new GridFocusDecoration(glider));", MAIN)
        self.assertIn("glider.addCompanion(grid);", MAIN)

    def test_grid_focus_is_drawn_over_children_only_when_grid_owns_focus(self):
        self.assertIn("void onDrawOver(", GRID_DECORATION)
        self.assertIn("View focused = parent.findFocus();", GRID_DECORATION)
        self.assertIn("focused.findViewById(R.id.card)", GRID_DECORATION)
        self.assertIn("focusRim.setStyle(Paint.Style.STROKE);", GRID_DECORATION)
        self.assertIn("canvas.drawRoundRect(outline", GRID_DECORATION)
        self.assertNotIn("drawShape(", GRID_DECORATION)
        self.assertNotIn("drawFocusOutline(", GRID_DECORATION)
        self.assertNotIn("void onDraw(", GRID_DECORATION)

    def test_up_collapse_commits_idle_geometry_without_double_translation(self):
        start = MAIN.index("private void collapseHorizontalOverflowToDock")
        end = MAIN.index("private void animateHorizontalDockTransition", start)
        collapse = MAIN[start:end]
        self.assertIn("horizontalGridExpanded = false;", collapse)
        self.assertIn("dockRecycler.setTranslationY(0f);", collapse)
        self.assertIn("grid.setTranslationY(0f);", collapse)
        self.assertIn("applyDockLayout();", collapse)
        self.assertLess(collapse.index("dockRecycler.setTranslationY(0f);"),
                        collapse.index("applyDockLayout();"))
        self.assertNotIn("setTranslationY(pitch)", collapse)

    def test_row_transition_does_not_animate_grid_alpha(self):
        start = MAIN.index("private void expandHorizontalDockForOverflow")
        end = MAIN.index("private void focusTile(int position)", start)
        transition = MAIN[start:end]
        self.assertNotIn("grid.setAlpha(0.72f)", transition)
        self.assertNotIn("grid.setAlpha(1f - 0.28f", transition)
        self.assertNotIn("grid.setAlpha(0.72f + 0.28f", transition)


if __name__ == "__main__":
    unittest.main()
