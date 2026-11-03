"""Regression guards for F06 bilingual coverage and shared UI contracts."""
from pathlib import Path
import re
import unittest
import xml.etree.ElementTree as ET


ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome"
RES = ROOT / "launcher" / "app" / "src" / "main" / "res"


class F06LocalizationUiContractTest(unittest.TestCase):
    def test_update_schedule_labels_use_localized_resources(self):
        source = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        method = source.split("private void showUpdateTimePickerDialog()", 1)[1].split(
            "\n    private ", 1
        )[0]
        for key in (
            "auto_update_time_0330_option",
            "auto_update_time_0400_option",
            "auto_update_time_0500_option",
            "auto_update_time_1200_option",
            "auto_update_time_0200_option",
        ):
            self.assertIn(f"R.string.{key}", method)
        self.assertNotRegex(method, r"(Cadangan Terbaik|Awal Pagi|Tengah Hari|Tengah Malam)")

    def test_weather_high_low_labels_use_localized_resource(self):
        source = (JAVA / "WeatherActivity.java").read_text(encoding="utf-8")
        self.assertIn(
            "getString(R.string.weather_high_low_summary, w.highTemp, w.lowTemp)",
            source,
        )
        self.assertNotIn('"H:%d°  L:%d°"', source)

    def test_only_indonesian_and_english_are_advertised(self):
        locale_config = ET.parse(RES / "xml" / "locales_config.xml").getroot()
        android_name = "{http://schemas.android.com/apk/res/android}name"
        self.assertEqual(
            {"id", "en"},
            {node.attrib[android_name] for node in locale_config.findall("locale")},
        )

    def test_every_visible_activity_and_standby_service_wraps_locale_context(self):
        manifest = ET.parse(ROOT / "launcher" / "app" / "src" / "main" / "AndroidManifest.xml").getroot()
        app = manifest.find("application")
        android_name = "{http://schemas.android.com/apk/res/android}name"
        for activity in app.findall("activity"):
            name = activity.attrib[android_name]
            if name.endswith(".HomeSettingsActivity"):
                continue  # NoDisplay trampoline; it renders no user-facing UI.
            source_name = name.rsplit(".", 1)[-1] + ".java"
            source = (JAVA / source_name).read_text(encoding="utf-8")
            self.assertIn("LocaleContext.wrap(newBase)", source, name)
        dream = (JAVA / "ClockDream.java").read_text(encoding="utf-8")
        self.assertIn("LocaleContext.wrap(newBase)", dream)


if __name__ == "__main__":
    unittest.main()
