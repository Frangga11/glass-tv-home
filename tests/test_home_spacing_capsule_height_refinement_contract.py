"""Regression contract for the scoped Home spacing and capsule-height refinement."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
HOME_XML = ROOT / "launcher/app/src/main/res/layout/activity_main.xml"
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
GEOMETRY = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java").read_text(encoding="utf-8")
ANDROID = "{http://schemas.android.com/apk/res/android}"


class HomeSpacingCapsuleHeightRefinementContractTest(unittest.TestCase):
    def test_home_content_safe_frame_is_30dp_left_right_and_29dp_top(self):
        root = ET.parse(HOME_XML).getroot()
        content = next(node for node in root.iter()
                       if node.get(ANDROID + "id") == "@+id/mainContent")
        self.assertEqual(content.get(ANDROID + "paddingStart"), "30dp")
        self.assertEqual(content.get(ANDROID + "paddingEnd"), "30dp")
        self.assertEqual(content.get(ANDROID + "paddingTop"), "29dp")
        self.assertIn("SAFE_HORIZONTAL_MARGIN_DP = 30f", GEOMETRY)
        self.assertIn("SAFE_VERTICAL_MARGIN_DP = 30f", GEOMETRY)
        self.assertIn("SAFE_TOP_MARGIN_DP = 29f", GEOMETRY)
        self.assertIn("params.topMargin = DockGeometry.safeTopInsetPx(density);", MAIN)

    def test_target_capsules_have_30dp_outer_height_without_changing_inputs(self):
        root = ET.parse(HOME_XML).getroot()
        by_id = {node.get(ANDROID + "id"): node for node in root.iter()
                 if node.get(ANDROID + "id")}
        self.assertEqual(by_id["@+id/btnWifi"].get(ANDROID + "layout_height"), "30dp")
        self.assertEqual(by_id["@+id/btnAssistant"].get(ANDROID + "layout_height"), "30dp")
        self.assertEqual(by_id["@+id/navPill"].get(ANDROID + "layout_height"), "30dp")
        # Inputs is intentionally excluded from the five requested capsules.
        self.assertIsNone(by_id["@+id/btnInputs"].get(ANDROID + "layout_height"))

    def test_home_and_profile_icon_label_spacing_is_compact_but_settings_is_untouched(self):
        root = ET.parse(HOME_XML).getroot()
        by_id = {node.get(ANDROID + "id"): node for node in root.iter()
                 if node.get(ANDROID + "id")}
        self.assertEqual(by_id["@+id/btnHome"].get(ANDROID + "drawablePadding"), "2dp")
        self.assertEqual(by_id["@+id/btnCandyBox"].get(ANDROID + "drawablePadding"), "2dp")
        self.assertEqual(by_id["@+id/btnSettings"].get(ANDROID + "drawablePadding"), "4dp")

    def test_bottom_safe_margin_and_shelf_gap_use_density_aware_30dp(self):
        self.assertIn("return Math.round(SAFE_VERTICAL_MARGIN_DP * density);", GEOMETRY)
        self.assertIn("return Math.round(30f * density);", GEOMETRY)
        # At the device's 320dpi density (2px per dp), 30dp is 60px.
        self.assertEqual(round(30 * (320 / 160)), 60)

    def test_wallpaper_and_prayer_visible_edges_anchor_to_actual_tray_boundary(self):
        self.assertIn("int trayTop = horizontalDockTrayTopInRoot();", MAIN)
        self.assertIn("heroBottom = trayTop - shelfGapPx;", MAIN)
        self.assertIn("islamicShelfBottomAnchor = heroBottom + islamicShelfVisualBottomOffsetPx();", MAIN)
        self.assertIn("return Math.max(0, -Math.round(prayerTable.getTranslationY()));", MAIN)
        self.assertIn("int shelfGapPx = DockGeometry.islamicShelfDockGapPx(density);", MAIN)

    def test_existing_prayer_translation_and_navigation_behavior_are_not_rewritten(self):
        root = ET.parse(HOME_XML).getroot()
        by_id = {node.get(ANDROID + "id"): node for node in root.iter()
                 if node.get(ANDROID + "id")}
        self.assertEqual(by_id["@+id/prayerHeader"].get(ANDROID + "translationY"), "-6dp")
        self.assertEqual(by_id["@+id/prayerTable"].get(ANDROID + "translationY"), "-6dp")
        self.assertEqual(by_id["@+id/btnHome"].get(ANDROID + "drawableStart"), "@drawable/ic_header_home")
        self.assertEqual(by_id["@+id/btnCandyBox"].get(ANDROID + "drawableStart"), "@drawable/ic_shield_child")
        self.assertEqual(by_id["@+id/btnSettings"].get(ANDROID + "drawableStart"), "@drawable/ic_settings")
        self.assertIn("btnCandyBox.setOnClickListener(v -> showCandyBoxPopup());", MAIN)
        self.assertIn("btnSettings.setOnClickListener(v -> showQuickControlPopup());", MAIN)


if __name__ == "__main__":
    unittest.main()
