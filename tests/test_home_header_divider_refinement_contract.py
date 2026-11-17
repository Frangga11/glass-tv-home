"""Regression checks for the Home header divider and unified navigation spacing."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
LAYOUT = ROOT / "launcher/app/src/main/res/layout/activity_main.xml"
WEATHER = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/WeatherAirQuality.java"
ANDROID = "{http://schemas.android.com/apk/res/android}"


class HomeHeaderDividerRefinementContractTest(unittest.TestCase):
    def test_clock_and_date_are_separated_by_a_centered_vertical_divider(self):
        root = ET.parse(LAYOUT).getroot()
        nodes = list(root.iter())
        divider = next(n for n in nodes if n.get(ANDROID + "id") == "@+id/clockDateDivider")
        date = next(n for n in nodes if n.get(ANDROID + "id") == "@+id/dateBlock")
        left = next(n for n in nodes if n.get(ANDROID + "id") == "@+id/leftBlock")
        self.assertEqual(divider.get(ANDROID + "layout_width"), "1dp")
        self.assertEqual(divider.get(ANDROID + "layout_height"), "22dp")
        self.assertEqual(divider.get(ANDROID + "background"), "#66FFFFFF")
        self.assertEqual(date.get(ANDROID + "layout_marginStart"), "0dp")
        self.assertLess(list(left).index(divider), list(left).index(date))

    def test_navigation_icon_label_spacing_is_uniform(self):
        root = ET.parse(LAYOUT).getroot()
        nodes = {n.get(ANDROID + "id"): n for n in root.iter() if n.get(ANDROID + "id")}
        for view_id in ("@+id/btnHome", "@+id/btnCandyBox", "@+id/btnSettings"):
            node = nodes[view_id]
            with self.subTest(view=view_id):
                self.assertEqual(node.get(ANDROID + "drawablePadding"), "1dp")
                self.assertEqual(node.get(ANDROID + "paddingStart"), "4dp")
                self.assertEqual(node.get(ANDROID + "paddingEnd"), "4dp")
                self.assertEqual(node.get(ANDROID + "textSize"), "14sp")
                self.assertEqual(node.get(ANDROID + "fontVariationSettings"), "'wght' 600")

    def test_weather_aqi_separator_is_a_centered_vertical_divider(self):
        source = WEATHER.read_text(encoding="utf-8")
        self.assertIn("class WeatherDividerSpan extends ReplacementSpan", source)
        self.assertIn("canvas.drawLine(centerX, centerY - halfHeight, centerX, centerY + halfHeight, paint)", source)
        self.assertIn("new WeatherDividerSpan(context.getResources().getDisplayMetrics().density)", source)
        self.assertIn('sb.append("\\uFFFC")', source)
        self.assertNotIn('sb.append("  •  ")', source)


if __name__ == "__main__":
    unittest.main()
