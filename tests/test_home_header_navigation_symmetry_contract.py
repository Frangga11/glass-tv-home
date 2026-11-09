"""Regression contracts for Home header separators and symmetric navigation."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher/app/src/main/java/com/gathin/tvhome"
RES = ROOT / "launcher/app/src/main/res"
ANDROID = "{http://schemas.android.com/apk/res/android}"


class HomeHeaderNavigationSymmetryContractTest(unittest.TestCase):
    def setUp(self):
        self.home = ET.parse(RES / "layout/activity_main.xml").getroot()
        self.main = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        self.weather = (JAVA / "WeatherAirQuality.java").read_text(encoding="utf-8")

    def find_id(self, resource_id):
        return next(node for node in self.home.iter()
                    if node.get(ANDROID + "id") == resource_id)

    def test_clock_date_divider_is_vertical_and_center_height(self):
        divider = self.find_id("@+id/clockDateDivider")
        self.assertEqual(divider.get(ANDROID + "layout_width"), "1dp")
        self.assertEqual(divider.get(ANDROID + "layout_height"), "22dp")
        self.assertEqual(divider.get(ANDROID + "background"), "#66FFFFFF")
        left = self.find_id("@+id/leftBlock")
        self.assertLess(list(left).index(divider), list(left).index(self.find_id("@+id/dateBlock")))

    def test_weather_aqi_uses_centered_vertical_replacement_span(self):
        self.assertIn("class WeatherDividerSpan extends ReplacementSpan", self.weather)
        self.assertIn("canvas.drawLine(centerX, centerY - halfHeight, centerX, centerY + halfHeight, paint)", self.weather)
        self.assertIn("new WeatherDividerSpan(context.getResources().getDisplayMetrics().density)", self.weather)
        self.assertIn('sb.append("\\uFFFC")', self.weather)
        self.assertNotIn('sb.append("  •  ")', self.weather)
        self.assertNotIn("StyleSpan(Typeface.BOLD)", self.weather)

    def test_header_information_typography_is_14sp_inter_semibold(self):
        ids = ("@+id/ampm", "@+id/date", "@+id/locationName",
               "@+id/locationAltitude", "@+id/weatherAirQuality",
               "@+id/btnHome", "@+id/btnCandyBox", "@+id/btnSettings", "@+id/btnWifi")
        for resource_id in ids:
            node = self.find_id(resource_id)
            with self.subTest(view=resource_id):
                self.assertEqual(node.get(ANDROID + "textSize"), "14sp")
                self.assertEqual(node.get(ANDROID + "fontFamily"), "@font/inter")
                self.assertEqual(node.get(ANDROID + "fontVariationSettings"), "'wght' 600")

    def test_navigation_segments_have_equal_weights_and_shared_typography(self):
        nav = self.find_id("@+id/navPill")
        segments = {node.get(ANDROID + "id"): node for node in nav.iter()
                    if node.get(ANDROID + "id") in (
                        "@+id/btnHome", "@+id/btnCandyBox", "@+id/btnSettings")}
        self.assertEqual(set(segments), {"@+id/btnHome", "@+id/btnCandyBox", "@+id/btnSettings"})
        for resource_id, node in segments.items():
            with self.subTest(view=resource_id):
                if resource_id == "@+id/btnSettings":
                    self.assertEqual(node.get(ANDROID + "layout_width"), "match_parent")
                    parent = next(parent for parent in nav.iter() if node in list(parent))
                    self.assertEqual(parent.get(ANDROID + "layout_weight"), "1")
                else:
                    self.assertEqual(node.get(ANDROID + "layout_width"), "0dp")
                    self.assertEqual(node.get(ANDROID + "layout_weight"), "1")
                self.assertEqual(node.get(ANDROID + "textSize"), "14sp")
                self.assertEqual(node.get(ANDROID + "fontVariationSettings"), "'wght' 600")
                self.assertEqual(node.get(ANDROID + "gravity"), "center")
                self.assertEqual(node.get(ANDROID + "drawablePadding"), "4dp")
                self.assertEqual(node.get(ANDROID + "paddingStart"), "4dp")
                self.assertEqual(node.get(ANDROID + "paddingEnd"), "4dp")

    def test_home_has_no_exclusive_active_navigation_indicator(self):
        self.assertNotIn("updateHomeTabIndicator", self.main)
        self.assertNotIn("btnHome.setSelected", self.main)
        self.assertNotIn("btnHome.setElevation", self.main)
        selector = ET.parse(RES / "drawable/header_nav_tab_bg.xml").getroot()
        self.assertFalse(any(item.get(ANDROID + "state_selected") == "true"
                             for item in selector.findall("item")))

    def test_navigation_glass_remains_visible_under_shared_focus_glider(self):
        self.assertIn("new GlassDrawable(navCapsule, 20f * density, density, glider, 1f, true)", self.main)
        self.assertIn("glider.addCompanion(navCapsule)", self.main)
        self.assertIn("glider.setStyle(Glider.DARK)", self.main)
        self.assertIn("int iconSize = Math.round(20f * density)", self.main)
        self.assertIn("tab.setCompoundDrawablesRelative(icon, icons[1], icons[2], icons[3])", self.main)
        drawable = (JAVA / "GlassDrawable.java").read_text(encoding="utf-8")
        self.assertIn("if (highlight != null && highlight.isShowing() && !paintsShape)", drawable)
        self.assertIn("if (paintsShape && highlight != null && highlight.isShowing())", drawable)


if __name__ == "__main__":
    unittest.main()
