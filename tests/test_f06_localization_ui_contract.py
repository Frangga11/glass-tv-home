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

    def test_known_locale_copy_is_correctly_assigned(self):
        def read_value(locale, key):
            root = ET.parse(RES / locale / "strings.xml").getroot()
            for node in root.findall("string"):
                if node.attrib.get("name") == key:
                    return node.text or ""
            self.fail(f"Missing {key} in {locale}")

        self.assertEqual(
            "Gunakan tombol arah untuk menggulir bacaan.",
            read_value("values-id", "daily_ayah_navigation_hint"),
        )
        self.assertEqual(
            "Use the directional buttons to scroll the reading.",
            read_value("values-en", "daily_ayah_navigation_hint"),
        )
        self.assertEqual("Tutup", read_value("values-id", "daily_ayah_close"))
        self.assertEqual("Close", read_value("values-en", "daily_ayah_close"))
        self.assertEqual("🪟 Efek Kaca", read_value("values-id", "glass_effect_setting_title"))

    def test_app_language_selector_and_platform_locale_are_bilingual(self):
        main = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        language = (JAVA / "AppLanguage.java").read_text(encoding="utf-8")
        self.assertIn("AppLanguage.LANG_ID", main)
        self.assertIn("AppLanguage.LANG_EN", main)
        self.assertIn("localeManager.getApplicationLocales()", language)
        self.assertIn("localeManager.setApplicationLocales", language)
        self.assertRegex(
            language,
            r"static String normalizeLanguage\(String language\)\s*\{\s*"
            r"return LANG_EN\.equalsIgnoreCase\(language\) \? LANG_EN : LANG_ID;",
        )

    def test_qr_upload_error_and_fallback_copy_is_locale_backed(self):
        wallpaper = (JAVA / "WallpaperUploadServer.java").read_text(encoding="utf-8")
        avatar = (JAVA / "AvatarUploadServer.java").read_text(encoding="utf-8")
        for key in (
            "upload_error_session_expired",
            "wallpaper_error_low_storage",
            "wallpaper_error_size_limit",
            "wallpaper_error_store_failed",
            "wallpaper_error_thumbnail_decode",
            "wallpaper_error_thumbnail_encode",
            "wallpaper_error_thumbnail_request",
            "standby_page_session_expired",
            "standby_page_server_unavailable",
            "standby_page_cancelled_retry",
            "wallpaper_page_server_error_fallback",
        ):
            self.assertIn(f"R.string.{key}", wallpaper)
        self.assertNotIn("Upload session expired or closed", wallpaper)
        self.assertNotIn("Server unavailable. Retry this photo.", wallpaper)
        self.assertNotIn("file saat ini dapat dicoba lagi", wallpaper)
        self.assertNotIn("xhr.statusText", wallpaper)
        self.assertIn("localized(R.string.upload_error_method_not_allowed)", avatar)
        self.assertNotIn('byte[] body = "Method Not Allowed"', avatar)

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
