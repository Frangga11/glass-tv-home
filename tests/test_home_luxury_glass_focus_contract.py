"""Regression contracts for the luxury realistic-glass Home focus surfaces."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher/app/src/main/java/com/gathin/tvhome"
RES = ROOT / "launcher/app/src/main/res"
ANDROID = "{http://schemas.android.com/apk/res/android}"


class HomeLuxuryGlassFocusContractTest(unittest.TestCase):
    def test_glass_remains_visible_while_shared_dpad_focus_moves(self):
        source = (JAVA / "GlassDrawable.java").read_text(encoding="utf-8")
        self.assertIn("private static final int VEIL_ALPHA = 0x70;", source)
        self.assertIn("float glass = fade;", source)
        self.assertNotIn("highlight.coverage(", source)

    def test_silver_cyan_rim_has_visible_reflection_and_window_glint(self):
        source = (JAVA / "GlassRim.java").read_text(encoding="utf-8")
        self.assertIn("0xA650DDF2", source)
        self.assertIn("0xD9FFFFFF", source)
        self.assertIn("0x8067E8F9", source)

    def test_header_icon_tint_is_consistent_and_focus_aware(self):
        tint = ET.parse(RES / "color/header_icon_tint.xml").getroot()
        items = tint.findall("item")
        self.assertEqual(items[0].get(ANDROID + "state_focused"), "true")
        self.assertEqual(items[0].get(ANDROID + "color"), "#FFF8FBFF")
        layout = ET.parse(RES / "layout/activity_main.xml").getroot()
        ids = {
            node.get(ANDROID + "id"): node
            for node in layout.iter()
            if node.get(ANDROID + "id")
        }
        for view_id in ("@+id/btnHome", "@+id/btnCandyBox", "@+id/btnSettings",
                        "@+id/btnWifi", "@+id/btnAssistant"):
            with self.subTest(view_id=view_id):
                self.assertEqual(ids[view_id].get(ANDROID + "drawableTint"),
                                 "@color/header_icon_tint")
        self.assertEqual(ids["@+id/locationMountainIcon"].get(ANDROID + "tint"),
                         "@color/header_icon_tint")

    def test_header_and_islamic_focus_surfaces_share_runtime_glass(self):
        main = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        for view_id in ("R.id.locationPill", "R.id.weatherAirQuality", "R.id.btnWifi",
                        "R.id.btnAssistant", "R.id.btnHome", "R.id.btnCandyBox",
                        "R.id.btnSettings"):
            with self.subTest(view_id=view_id):
                self.assertIn(view_id, main)
        self.assertIn("R.id.islamicShelf", main)
        self.assertIn("islamicGlassPanel.setBackground(new GlassDrawable(", main)
        self.assertIn("Backdrop.watch(islamicGlassPanel)", main)

    def test_all_dpad_cursor_states_use_shared_frosted_silver_not_islamic_gold(self):
        tokens = (JAVA / "TvUiTokens.java").read_text(encoding="utf-8")
        glider = (JAVA / "Glider.java").read_text(encoding="utf-8")
        colors = ET.parse(RES / "values/colors.xml").getroot()
        color_map = {node.get("name"): (node.text or "").strip() for node in colors.findall("color")}
        self.assertIn("FOCUS_BORDER = 0xFFDCE5ED", tokens)
        self.assertIn("FOCUS_HALO = 0x35DCE5ED", tokens)
        self.assertIn("focusStroke.setColor(TvUiTokens.FOCUS_BORDER)", glider)
        self.assertIn("paint.setColor(TvUiTokens.FOCUS_HALO)", glider)
        self.assertEqual(color_map["tv_ui_focus_cyan"], "#FFDCE5ED")
        self.assertEqual(color_map["tv_ui_focus_halo"], "#35DCE5ED")
        prayer = (RES / "drawable/prayer_pill_bg.xml").read_text(encoding="utf-8")
        self.assertIn("@color/tv_ui_focus_halo", prayer)

    def test_islamic_gold_and_black_shadow_remain_independent_of_global_glass(self):
        tokens = (JAVA / "TvUiTokens.java").read_text(encoding="utf-8")
        main = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        self.assertIn("ISLAMIC_GOLD = 0xFFFFC45A", tokens)
        self.assertIn("ISLAMIC_TEXT_SHADOW = 0xCC000000", tokens)
        self.assertIn("labels[i].setTextColor(TvUiTokens.ISLAMIC_GOLD)", main)
        self.assertIn("ayahArabic.setTextColor(TvUiTokens.ISLAMIC_GOLD)", main)
        self.assertIn("ayahArabic.setShadowLayer", main)
        self.assertIn("TvUiTokens.ISLAMIC_TEXT_SHADOW", main)


if __name__ == "__main__":
    unittest.main()
