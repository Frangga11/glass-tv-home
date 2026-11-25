"""Regression contracts for identical cyan breathing focus on Dock and Grid app rows."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher/app/src/main/java/com/gathin/tvhome"
RES = ROOT / "launcher/app/src/main/res"


class HomeSharedCyanBreathingFocusRowsTest(unittest.TestCase):
    def test_glider_is_the_only_dock_and_grid_focus_cursor(self):
        layout = ET.parse(RES / "layout/item_app.xml").getroot()
        ns = "{http://schemas.android.com/apk/res/android}"
        self.assertFalse(any(node.attrib.get(ns + "id") == "@+id/dockBreathingBorder"
                             for node in layout.iter()))
        adapter = (JAVA / "AppAdapter.java").read_text(encoding="utf-8")
        self.assertNotIn("DockBreathingBorderView", adapter)
        self.assertNotIn("breathingBorder", adapter)
        main = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        self.assertIn("Glider is the sole app/Dock focus cursor", main)
        self.assertIn("dockRecycler, tray.area", main)
        self.assertIn("syncFocusedAppGlider();", main)

    def test_grid_does_not_draw_a_second_static_focus_outline(self):
        main = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        self.assertNotIn("grid.addItemDecoration(new GridFocusDecoration(glider))", main)
        self.assertNotIn("glider.addCompanion(grid)", main)

    def test_focus_scale_changes_visual_transform_without_changing_layout_geometry(self):
        adapter = (JAVA / "AppAdapter.java").read_text(encoding="utf-8")
        self.assertIn("static final float DOCK_FOCUS_SCALE = 1.12f", adapter)
        self.assertIn("static final float GRID_FOCUS_SCALE = 1.10f", adapter)
        self.assertIn("static final float LAYOUT_FOCUS_RESERVATION_SCALE = 1.07f", adapter)
        self.assertIn("/ LAYOUT_FOCUS_RESERVATION_SCALE", adapter)
        listener = adapter.split("card.setOnFocusChangeListener", 1)[1].split(
            "void setIconAnimationsEnabled", 1
        )[0]
        self.assertIn("view.animate().scaleX(s).scaleY(s)", listener)
        self.assertIn(".setDuration(focused ? 320 : 200)", listener)
        self.assertIn("new OvershootInterpolator(1.8f)", adapter)
        self.assertNotIn("setElevation(", adapter)
        self.assertNotIn("translationZ(", adapter)
        border = (JAVA / "DockBreathingBorderView.java").read_text(encoding="utf-8")
        self.assertNotIn("setScaleX", border)
        self.assertNotIn("setScaleY", border)
        self.assertNotIn("setTranslationX", border)
        self.assertNotIn("setTranslationY", border)


if __name__ == "__main__":
    unittest.main()
