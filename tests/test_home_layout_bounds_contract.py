"""Regression contract for Home controls sharing a balanced safe frame."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET


ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
LAYOUT = ROOT / "launcher/app/src/main/res/layout/activity_main.xml"
GEOMETRY = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java").read_text(encoding="utf-8")
ANDROID = "{http://schemas.android.com/apk/res/android}"


class HomeLayoutBoundsContractTest(unittest.TestCase):
    def test_home_content_uses_equal_34dp_top_and_side_safe_margins(self):
        root = ET.parse(LAYOUT).getroot()
        main_content = next((node for node in root.iter()
                             if node.get(ANDROID + "id") == "@+id/mainContent"), None)
        self.assertIsNotNone(main_content)
        self.assertEqual(main_content.get(ANDROID + "paddingStart"), "34dp")
        self.assertEqual(main_content.get(ANDROID + "paddingEnd"), "34dp")
        self.assertEqual(main_content.get(ANDROID + "paddingTop"), "34dp")

        self.assertIn("SAFE_HORIZONTAL_MARGIN_DP = 34f", GEOMETRY)
        self.assertIn("SAFE_VERTICAL_MARGIN_DP = 34f", GEOMETRY)
        self.assertIn("safeBottomInsetPx", GEOMETRY)

    def test_right_prayer_shelf_uses_the_same_top_and_right_safe_inset(self):
        start = MAIN.index("private void applyHomeShelfSafeMargins()")
        end = MAIN.index("private void applyDockLayout()", start)
        shelf = MAIN[start:end]
        self.assertIn("params.setMarginEnd(inset)", shelf)
        self.assertIn("params.topMargin = inset", shelf)
        self.assertIn("params.setMarginStart(inset)", shelf)

    def test_header_navigation_keeps_the_owner_approved_order(self):
        root = ET.parse(LAYOUT).getroot()
        nav = next((node for node in root.iter()
                    if node.get(ANDROID + "id") == "@+id/navPill"), None)
        self.assertIsNotNone(nav)
        children = [node.get(ANDROID + "id") for node in list(nav)]
        self.assertEqual(children[:3], [
            "@+id/btnHome", "@+id/btnCandyBox", "@+id/btnSettingsContainer"
        ])

    def test_right_controls_remain_right_aligned_and_outside_center_tabs(self):
        root = ET.parse(LAYOUT).getroot()
        top_buttons = next((node for node in root.iter()
                            if node.get(ANDROID + "id") == "@+id/topButtons"), None)
        self.assertIsNotNone(top_buttons)
        self.assertEqual(top_buttons.get(ANDROID + "layout_alignParentEnd"), "true")
        ids = {node.get(ANDROID + "id") for node in top_buttons.iter()}
        for view_id in ("btnInputs", "btnWifi", "btnAssistant"):
            self.assertIn(f"@+id/{view_id}", ids)
        self.assertNotIn("@+id/btnCandyBox", ids)
        self.assertNotIn("@+id/btnSettingsContainer", ids)

    def test_header_labels_truncate_inside_their_own_slots(self):
        root = ET.parse(LAYOUT).getroot()
        expected = ("locationName", "locationAltitude", "weatherAirQuality",
                    "btnHome", "btnCandyBox", "btnSettings")
        for view_id in expected:
            node = next((item for item in root.iter()
                         if item.get(ANDROID + "id") == f"@+id/{view_id}"), None)
            self.assertIsNotNone(node, view_id)
            self.assertEqual(node.get(ANDROID + "singleLine"), "true", view_id)
            self.assertEqual(node.get(ANDROID + "ellipsize"), "end", view_id)

    def test_center_navigation_is_sized_from_measured_side_controls(self):
        self.assertIn("HomeHeaderGeometry.centeredWidthPx", MAIN)
        self.assertIn("date.getRight()", MAIN)
        self.assertIn("right.getLeft()", MAIN)

    def test_header_controls_keep_the_established_liquid_glass_surface(self):
        start = MAIN.index("// Preserve the established GlassDrawable/Backdrop treatment")
        end = MAIN.index("// Hide the glass when focus leaves the tiles.", start)
        header = MAIN[start:end]
        for view_id in ("locationPill", "weatherAirQuality", "btnInputs", "btnWifi",
                        "btnAssistant", "btnHome", "btnCandyBox", "btnSettings"):
            self.assertIn(f"R.id.{view_id}", header)
        self.assertIn("new GlassDrawable(pill", header)
        self.assertIn("pill.setForeground(getDrawable(R.drawable.header_nav_tab_bg))", header)

    def test_top_controls_are_not_repositioned_from_dock_item_geometry(self):
        self.assertNotIn("alignTopButtons", MAIN)
        self.assertNotIn("topRow.setTranslationX", MAIN)
        self.assertNotIn("root.offsetDescendantRectToMyCoords(topSettings", MAIN)

    def test_home_dock_and_grid_still_share_the_existing_geometry_owner(self):
        self.assertIn("horizontalGridViewportHeightPx", GEOMETRY)
        self.assertIn("horizontalTileWidthPx", GEOMETRY)
        self.assertIn("horizontalGridWidthPx", GEOMETRY)


if __name__ == "__main__":
    unittest.main()
