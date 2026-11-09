"""Regression contracts for Home header typography and navigation focus layering."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
LAYOUT = ROOT / "launcher/app/src/main/res/layout/activity_main.xml"
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
FOCUS = (ROOT / "launcher/app/src/main/res/drawable/header_nav_tab_bg.xml").read_text(encoding="utf-8")
ANDROID = "{http://schemas.android.com/apk/res/android}"


class HomeHeaderTypographyFocusContractTest(unittest.TestCase):
    def setUp(self):
        self.root = ET.parse(LAYOUT).getroot()
        self.views = {
            node.get(ANDROID + "id"): node
            for node in self.root.iter()
            if node.get(ANDROID + "id")
        }

    def test_requested_header_labels_use_inter_semibold_14sp(self):
        ids = (
            "@+id/ampm", "@+id/date", "@+id/shamsiDate",
            "@+id/locationName", "@+id/locationAltitude",
            "@+id/weatherAirQuality", "@+id/btnHome", "@+id/btnCandyBox",
            "@+id/btnSettings", "@+id/btnWifi",
        )
        for view_id in ids:
            with self.subTest(view_id=view_id):
                view = self.views[view_id]
                self.assertEqual(view.get(ANDROID + "textSize"), "14sp")
                self.assertEqual(view.get(ANDROID + "fontVariationSettings"), "'wght' 600")
                if view_id not in ("@+id/btnCandyBox", "@+id/btnSettings", "@+id/btnWifi"):
                    self.assertEqual(view.get(ANDROID + "fontFamily"), "@font/inter")

    def test_header_capsules_remain_30dp(self):
        for view_id in ("@+id/locationPill", "@+id/weatherAirQuality",
                        "@+id/navPill", "@+id/btnWifi", "@+id/btnAssistant"):
            with self.subTest(view_id=view_id):
                self.assertEqual(self.views[view_id].get(ANDROID + "layout_height"), "30dp")

    def test_navigation_icon_spacing_is_tight_and_settings_spacing_is_preserved(self):
        self.assertEqual(self.views["@+id/btnHome"].get(ANDROID + "drawablePadding"), "1dp")
        self.assertEqual(self.views["@+id/btnCandyBox"].get(ANDROID + "drawablePadding"), "1dp")
        self.assertEqual(self.views["@+id/btnSettings"].get(ANDROID + "drawablePadding"), "4dp")
        for view_id in ("@+id/btnHome", "@+id/btnCandyBox", "@+id/btnSettings"):
            self.assertEqual(self.views[view_id].get(ANDROID + "gravity"), "center")

    def test_navigation_focus_is_painted_on_tab_surface_above_shared_capsule(self):
        self.assertIn('android:state_focused="true"', FOCUS)
        self.assertIn('<solid android:color="#35DCE5ED" />', FOCUS)
        self.assertIn('<stroke android:width="1dp" android:color="@color/tv_ui_focus_cyan" />', FOCUS)
        self.assertIn('<stroke android:width="1dp"', FOCUS)
        self.assertIn("pill.setBackground(getDrawable(R.drawable.header_nav_tab_bg));", MAIN)
        self.assertNotIn("pill.setForeground(getDrawable(R.drawable.header_nav_tab_bg));", MAIN)

    def test_wifi_keeps_its_existing_glass_focus_path(self):
        self.assertIn("new GlassDrawable(pill, 20f * density, density, glider)", MAIN)
        self.assertIn("moveTopHighlight(v)", MAIN)
        self.assertIn("btnWifi.setOnClickListener(v -> showWifiPopup())", MAIN)
        self.assertIn("private void showWifiPopup(Runnable returnToParent)", MAIN)

    def test_shamsi_date_runtime_override_does_not_reduce_date_size(self):
        self.assertIn("date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);", MAIN)
        self.assertNotIn("date.setTextSize(11f);", MAIN)


if __name__ == "__main__":
    unittest.main()
