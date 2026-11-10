"""Regression contracts for identical cyan breathing focus on Dock and Grid app rows."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher/app/src/main/java/com/gathin/tvhome"
RES = ROOT / "launcher/app/src/main/res"


class HomeSharedCyanBreathingFocusRowsTest(unittest.TestCase):
    def test_both_rows_use_the_same_breathing_border_view_from_shared_item_layout(self):
        layout = ET.parse(RES / "layout/item_app.xml").getroot()
        ns = "{http://schemas.android.com/apk/res/android}"
        border = next(node for node in layout.iter()
                      if node.attrib.get(ns + "id") == "@+id/dockBreathingBorder")
        self.assertEqual(border.tag, "com.gathin.tvhome.DockBreathingBorderView")
        self.assertEqual(border.attrib[ns + "visibility"], "gone")
        adapter = (JAVA / "AppAdapter.java").read_text(encoding="utf-8")
        self.assertIn("if (focused) {", adapter)
        self.assertIn("breathingBorder.setVisibility(View.VISIBLE);", adapter)
        self.assertIn("breathingBorder.setVisibility(View.GONE);", adapter)
        self.assertNotIn("focused && dockTile", adapter)

    def test_canonical_cyan_breathing_animation_is_preserved(self):
        source = (JAVA / "DockBreathingBorderView.java").read_text(encoding="utf-8")
        self.assertIn("ValueAnimator.ofFloat(0f, 1f)", source)
        self.assertIn("breathAnimator.setDuration(1600)", source)
        self.assertIn("breathAnimator.setRepeatMode(ValueAnimator.REVERSE)", source)
        self.assertIn("breathAnimator.setRepeatCount(ValueAnimator.INFINITE)", source)
        self.assertIn("TvUiTokens.ACCENT_CYAN", source)
        self.assertIn("new int[]{topColor, bottomColor}", source)
        self.assertIn("canvas.drawRoundRect(glowBounds", source)
        self.assertIn("canvas.drawLine(", source)

    def test_grid_does_not_draw_a_second_static_focus_outline(self):
        main = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        self.assertNotIn("grid.addItemDecoration(new GridFocusDecoration(glider))", main)
        self.assertNotIn("glider.addCompanion(grid)", main)

    def test_focus_animation_does_not_change_tile_geometry(self):
        adapter = (JAVA / "AppAdapter.java").read_text(encoding="utf-8")
        self.assertIn("static final float FOCUS_SCALE = 1.0f", adapter)
        listener = adapter.split("card.setOnFocusChangeListener", 1)[1].split(
            "void setIconAnimationsEnabled", 1
        )[0]
        self.assertIn("view.animate().scaleX(s).scaleY(s)", listener)
        # The breathing rim itself only animates its paint/alpha/width; it never scales the card.
        border = (JAVA / "DockBreathingBorderView.java").read_text(encoding="utf-8")
        self.assertNotIn("setScaleX", border)
        self.assertNotIn("setScaleY", border)
        self.assertNotIn("setTranslationX", border)
        self.assertNotIn("setTranslationY", border)


if __name__ == "__main__":
    unittest.main()
