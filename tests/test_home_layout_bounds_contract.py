"""Regression contract for Home top controls staying inside the XML safe content frame."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET


ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
LAYOUT = ROOT / "launcher/app/src/main/res/layout/activity_main.xml"
ANDROID = "{http://schemas.android.com/apk/res/android}"


class HomeLayoutBoundsContractTest(unittest.TestCase):
    def test_top_controls_keep_their_declared_safe_end_alignment(self):
        root = ET.parse(LAYOUT).getroot()
        main_content = next((node for node in root.iter()
                             if node.get(ANDROID + "id") == "@+id/mainContent"), None)
        self.assertIsNotNone(main_content)
        self.assertEqual(main_content.get(ANDROID + "paddingStart"), "56dp")
        self.assertEqual(main_content.get(ANDROID + "paddingEnd"), "56dp")

        top_buttons = next((node for node in root.iter()
                            if node.get(ANDROID + "id") == "@+id/topButtons"), None)
        self.assertIsNotNone(top_buttons)
        self.assertEqual(top_buttons.get(ANDROID + "layout_alignParentEnd"), "true")

        ids = {node.get(ANDROID + "id") for node in top_buttons.iter()}
        for view_id in ("btnWifi", "btnAssistant", "btnCandyBox", "btnSettingsContainer"):
            self.assertIn(f"@+id/{view_id}", ids)

    def test_top_controls_are_not_repositioned_from_dock_item_geometry(self):
        self.assertNotIn("alignTopButtons", MAIN)
        self.assertNotIn("topRow.setTranslationX", MAIN)
        self.assertNotIn("root.offsetDescendantRectToMyCoords(topSettings", MAIN)

    def test_home_dock_and_grid_still_share_the_existing_geometry_owner(self):
        geometry = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java").read_text(encoding="utf-8")
        self.assertIn("horizontalGridViewportHeightPx", geometry)
        self.assertIn("horizontalTileWidthPx", geometry)
        self.assertIn("horizontalGridWidthPx", geometry)


if __name__ == "__main__":
    unittest.main()
