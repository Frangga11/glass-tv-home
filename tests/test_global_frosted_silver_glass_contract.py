"""Regression contracts for the global Frosted Silver Glass visual system."""
from pathlib import Path
import re
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher/app/src/main/java/com/gathin/tvhome"
RES = ROOT / "launcher/app/src/main/res"


def java_int(name):
    source = (JAVA / "TvUiTokens.java").read_text(encoding="utf-8")
    match = re.search(rf"static final int {name} = (0x[0-9A-Fa-f]+);", source)
    if not match:
        raise AssertionError(f"Missing Java token: {name}")
    return "#" + match.group(1)[2:].upper()


def java_float(name):
    source = (JAVA / "TvUiTokens.java").read_text(encoding="utf-8")
    match = re.search(rf"static final float {name} = ([0-9.]+)f;", source)
    if not match:
        raise AssertionError(f"Missing Java float token: {name}")
    return float(match.group(1))


class GlobalFrostedSilverGlassContractTest(unittest.TestCase):
    def test_java_and_xml_semantic_palette_stay_synchronized(self):
        xml = ET.parse(RES / "values/colors.xml").getroot()
        values = {node.attrib["name"]: node.text.strip().upper()
                  for node in xml.findall("color")}
        mapping = {
            "SHELL": "tv_ui_shell",
            "SURFACE": "tv_ui_surface",
            "SURFACE_FOCUSED": "tv_ui_surface_focused",
            "SURFACE_SELECTED": "tv_ui_surface_selected",
            "POPUP_SURFACE": "tv_ui_popup_surface",
            "DOCK_SURFACE": "tv_ui_dock_surface",
            "TEXT_PRIMARY": "tv_ui_text_primary",
            "TEXT_SECONDARY": "tv_ui_text_secondary",
            "ACCENT_BLUE": "tv_ui_accent_blue",
            "ACCENT_CYAN": "tv_ui_focus_cyan",
            "BORDER": "tv_ui_border",
            "FOCUS_HALO": "tv_ui_focus_halo",
            "GLASS_VEIL_COLOR": "tv_ui_glass_veil",
            "NAV_SELECTION_BORDER": "tv_ui_selected_border",
        }
        for java_name, xml_name in mapping.items():
            with self.subTest(token=java_name):
                self.assertEqual(values[xml_name], java_int(java_name))

    def test_focused_selected_and_pressed_are_distinct_states(self):
        self.assertNotEqual(java_int("ACCENT_CYAN"), java_int("NAV_SELECTION_BORDER"))
        selector = ET.parse(RES / "drawable/selector_glass_dialog_list_item_bg.xml").getroot()
        ns = "{http://schemas.android.com/apk/res/android}"
        states = {item.attrib.get(ns + "state_focused"): item for item in selector.findall("item")}
        self.assertIn("true", states)
        focused = states["true"].find("shape/stroke")
        activated = next(item for item in selector.findall("item")
                         if item.attrib.get(ns + "state_activated") == "true")
        selected = activated.find("shape/stroke")
        self.assertEqual(focused.attrib[ns + "color"], "@color/tv_ui_focus_cyan")
        self.assertEqual(selected.attrib[ns + "color"], "@color/tv_ui_selected_border")

    def test_recent_apps_focus_does_not_scale_cards_and_keeps_readable_label(self):
        source = (JAVA / "RecentAppsAdapter.java").read_text(encoding="utf-8")
        self.assertNotIn("scaleX(1.06f)", source)
        self.assertNotIn("scaleX(0.94f)", source)
        self.assertIn("tvAppLabel.setTextColor(TvUiTokens.TEXT_PRIMARY)", source)
        layout = ET.parse(RES / "layout/item_recent_app.xml").getroot()
        ns = "{http://schemas.android.com/apk/res/android}"
        card = next(node for node in layout.iter()
                    if node.attrib.get("{http://schemas.android.com/apk/res/android}id") == "@+id/cardContainer")
        self.assertEqual(card.attrib.get(ns + "foreground"), "@drawable/bg_recent_card_focus")
        ring = ET.parse(RES / "drawable/bg_recent_card_focus.xml").getroot()
        focused = next(item for item in ring.findall("item")
                       if item.attrib.get(ns + "state_focused") == "true")
        self.assertEqual(focused.find("shape/stroke").attrib[ns + "color"], "@color/tv_ui_focus_cyan")

    def test_weather_and_shared_focus_helpers_never_scale_controls(self):
        weather = (JAVA / "WeatherActivity.java").read_text(encoding="utf-8")
        helper = weather.split("private void setupFocusAnimation", 1)[1].split("private void setupCardFocus", 1)[0]
        self.assertNotIn("scaleX(", helper)
        self.assertNotIn("scaleY(", helper)
        indicator = (JAVA / "TvFocusIndicator.java").read_text(encoding="utf-8")
        self.assertNotIn(".scaleX(hasFocus", indicator)
        self.assertNotIn(".scaleY(hasFocus", indicator)

    def test_pressed_state_precedes_focused_and_selected_selector_states(self):
        ns = "{http://schemas.android.com/apk/res/android}"
        button = ET.parse(RES / "drawable/selector_glass_dialog_button_bg.xml").getroot()
        button_states = [item.attrib.get(ns + "state_pressed") == "true" for item in button.findall("item")]
        self.assertTrue(button_states[0], "Pressed must be evaluated before focused")
        rows = ET.parse(RES / "drawable/selector_glass_dialog_list_item_bg.xml").getroot()
        row_states = [item.attrib.get(ns + "state_pressed") == "true" for item in rows.findall("item")]
        self.assertTrue(row_states[0], "Pressed must be evaluated before focused/activated")

    def test_dpad_focus_does_not_scale_or_translate_tiles(self):
        adapter = (JAVA / "AppAdapter.java").read_text(encoding="utf-8")
        self.assertEqual(java_float("CARD_FOCUS_SCALE"), 1.0)
        self.assertEqual(java_float("NAV_FOCUS_SCALE"), 1.0)
        self.assertIn("static final float FOCUS_SCALE = 1.0f;", adapter)
        self.assertIn("static final float LAYOUT_FOCUS_RESERVATION_SCALE = 1.07f;", adapter)
        self.assertIn("/ LAYOUT_FOCUS_RESERVATION_SCALE", adapter)
        self.assertNotIn(".translationY(focused ? density * 4f : -density * 6f)", adapter)
        self.assertIn(".translationY(0f)", adapter)

    def test_top_navigation_glider_uses_cyan_outline_not_dark_focus_fill(self):
        source = (JAVA / "Glider.java").read_text(encoding="utf-8")
        self.assertIn("paint.setColor(TvUiTokens.FOCUS_HALO)", source)
        self.assertIn("focusStroke.setColor(TvUiTokens.FOCUS_BORDER)", source)
        self.assertIn("focusStroke.setAlpha(Math.round(220 * opacity))", source)
        self.assertNotIn("FOCUS_ALPHA", source)
        grid_focus = (JAVA / "GlassView.java").read_text(encoding="utf-8")
        self.assertIn("focusStroke.setColor(TvUiTokens.FOCUS_BORDER)", grid_focus)

    def test_glass_rim_uses_neutral_silver_and_restrained_cyan(self):
        source = (JAVA / "GlassRim.java").read_text(encoding="utf-8")
        self.assertIn("0xD9E2E8EE", source)
        self.assertIn("0x8050DDF2", source)
        self.assertNotIn("0xC084FC", source)
        self.assertNotIn("0xFDE047", source)

    def test_core_surfaces_keep_opaque_fallback_and_silver_identity(self):
        fallback = (JAVA / "GlassDrawable.java").read_text(encoding="utf-8")
        tokens = (JAVA / "TvUiTokens.java").read_text(encoding="utf-8")
        self.assertIn("solidSurface.setColor(TvUiTokens.POPUP_SURFACE)", fallback)
        self.assertIn("solidBorder.setColor(TvUiTokens.BORDER)", fallback)
        self.assertEqual(int(java_int("POPUP_SURFACE")[1:3], 16), 255)
        self.assertEqual(int(java_int("DOCK_SURFACE")[1:3], 16), 255)
        self.assertIn("0x88343E48", tokens)

    def test_islamic_arabic_visual_uses_silver_and_preserves_rtl(self):
        ns = "{http://schemas.android.com/apk/res/android}"
        home = ET.parse(RES / "layout/activity_main.xml").getroot()
        ayah = next(node for node in home.iter()
                    if node.attrib.get(ns + "id") == "@+id/ayahArabic")
        self.assertEqual(ayah.attrib[ns + "textDirection"], "rtl")
        self.assertEqual(ayah.attrib[ns + "layoutDirection"], "ltr")
        self.assertEqual(ayah.attrib[ns + "gravity"], "end")
        self.assertEqual(ayah.attrib[ns + "textColor"], "@color/tv_ui_text_primary")
        nightstand = ET.parse(RES / "layout/view_nightstand.xml").getroot()
        hijri = next(node for node in nightstand.iter()
                     if node.attrib.get(ns + "id") == "@+id/nightstandHijri")
        self.assertEqual(hijri.attrib[ns + "textColor"], "@color/tv_ui_text_secondary")

    def test_remaining_location_and_recent_surfaces_keep_silver_fallbacks(self):
        ns = "{http://schemas.android.com/apk/res/android}"
        zone = ET.parse(RES / "drawable/bg_location_zone_card.xml").getroot()
        zone_focused = next(item for item in zone.findall("item")
                            if item.attrib.get(ns + "state_focused") == "true")
        self.assertEqual(zone_focused.find("shape/solid").attrib[ns + "color"], "@color/tv_ui_focus_halo")
        self.assertEqual(zone_focused.find("shape/stroke").attrib[ns + "color"], "@color/tv_ui_focus_cyan")
        recent = ET.parse(RES / "drawable/bg_recent_card.xml").getroot()
        self.assertEqual(recent.find("solid").attrib[ns + "color"], "#C83B4651")
        self.assertEqual(recent.find("stroke").attrib[ns + "color"], "#99DCE5ED")

    def test_dynamic_dialog_focus_surfaces_use_shared_cyan_tokens(self):
        main = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        self.assertIn("on.setColor(TvUiTokens.SURFACE_FOCUSED)", main)
        self.assertIn("on.setStroke(Math.max(1, (int) (2 * density)), TvUiTokens.FOCUS_BORDER)", main)
        self.assertNotIn("new int[]{0xFF000000", main)
        settings = (JAVA / "ModernSettingsDialog.java").read_text(encoding="utf-8")
        self.assertNotIn("0xFF176B87", settings)
        self.assertNotIn("0xFF4A4E5C", settings)

    def test_changed_drawable_resources_are_valid_xml(self):
        names = [
            "bg_glass_dialog.xml", "glass_panel.xml", "bg_floating_dock.xml",
            "bg_location_pill.xml", "bg_tv_focus_outline.xml",
            "bg_app_pill.xml", "bg_app_pill_focused.xml", "bg_app_pill_unfocused.xml",
            "bg_recent_card_focus.xml",
            "selector_glass_dialog_button_bg.xml", "selector_glass_dialog_list_item_bg.xml",
            "header_nav_tab_bg.xml", "bg_recent_apps_glass.xml", "bg_media_app_pill.xml",
            "bg_np_round_btn.xml", "input_row_bg.xml", "bg_dialog_btn.xml",
            "bg_location_state_pill.xml", "bg_location_zone_card.xml", "prayer_pill_bg.xml",
            "bg_track_info_selector.xml", "tile_bg.xml", "bg_ram_pill.xml",
            "bg_circle_close_all.xml", "header_glass_pill_bg.xml", "bg_recent_card.xml",
            "bg_app_squircle.xml", "dialog_btn_text.xml", "pill_text.xml",
            "pill_text_dim.xml", "pill_text_mid.xml", "top_text.xml", "top_text_dim.xml",
            "weather_card_bg.xml", "weather_pill_bg.xml", "bg_weather_tab_active.xml",
            "selector_daily_row_bg.xml",
        ]
        for name in names:
            with self.subTest(resource=name):
                ET.parse(RES / "drawable" / name)


if __name__ == "__main__":
    unittest.main()
