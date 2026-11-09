"""Regression contracts for the tightly scoped Home wallpaper text refinement."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
LAYOUT = ROOT / "launcher/app/src/main/res/layout/activity_main.xml"
METADATA = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/WallpaperMetadata.java").read_text(encoding="utf-8")
ANDROID = "{http://schemas.android.com/apk/res/android}"


class HomeWallpaperTextRefinementContractTest(unittest.TestCase):
    def test_wallpaper_shelf_order_is_location_title_description(self):
        root = ET.parse(LAYOUT).getroot()
        shelf = next(node for node in root.iter() if node.get(ANDROID + "id") == "@+id/heroShelf")
        self.assertEqual([node.get(ANDROID + "id") for node in list(shelf)], [
            "@+id/heroWhere", "@+id/heroName", "@+id/heroDesc"
        ])
        self.assertEqual(list(shelf)[0].get(ANDROID + "textAllCaps"), "false")

    def test_default_dempo_wallpaper_metadata_remains_unchanged(self):
        self.assertIn('"Kabut Pagi Dempo"', METADATA)
        self.assertIn('"Udara sejuk turun perlahan, menyelimuti lereng dan kebun teh Pagar Alam."', METADATA)
        self.assertIn('"Pagar Alam, Sumatera Selatan"', METADATA)

    def test_home_shelves_clear_the_dock_and_daily_ayah_prayer_offsets_are_distinct(self):
        self.assertIn("HOME_SHELF_BASELINE_OFFSET_DP = 3.5f", MAIN)
        self.assertIn("MIN_HOME_SHELF_TRAY_GAP_DP = 32f", MAIN)
        self.assertIn("ISLAMIC_SHELF_BOTTOM_OFFSET_DP = 10f", MAIN)
        self.assertIn("heroBottom = trayTop - shelfGapPx", MAIN)
        self.assertIn("islamicShelfBottomAnchor = heroBottom + islamicShelfVisualBottomOffsetPx()", MAIN)
        self.assertIn("islamicShelfDockGapPx(float density)", (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java").read_text(encoding="utf-8"))
        self.assertIn("heroBottom = shelfDockTop - shelfGapPx + safeOffsetPx", MAIN)
        self.assertIn("Math.round(prayerTable.getTranslationY())", MAIN)
        root = ET.parse(LAYOUT).getroot()
        prayer_header = next(node for node in root.iter() if node.get(ANDROID + "id") == "@+id/prayerHeader")
        prayer_table = next(node for node in root.iter() if node.get(ANDROID + "id") == "@+id/prayerTable")
        self.assertEqual(prayer_header.get(ANDROID + "translationY"), "-6dp")
        self.assertEqual(prayer_table.get(ANDROID + "translationY"), "-6dp")

    def test_prayer_text_uses_solid_high_contrast_colors_and_keeps_native_font(self):
        self.assertIn("prayerHeader.setTextColor(0xFFF4F7FC)", MAIN)
        self.assertIn("labels[i].setTextColor(TvUiTokens.ISLAMIC_GOLD)", MAIN)
        self.assertIn("times[i].setTextColor(TvUiTokens.ISLAMIC_GOLD)", MAIN)
        self.assertIn("labels[i].setTextColor(TvUiTokens.TEXT_SECONDARY)", MAIN)
        self.assertIn("times[i].setTextColor(TvUiTokens.TEXT_PRIMARY)", MAIN)
        self.assertNotIn("0xFFFFD27A", MAIN)
        self.assertNotIn("applyPrayerTextGradient", MAIN)
        self.assertNotIn("labels[i].setTypeface(Typeface.DEFAULT", MAIN)
        self.assertNotIn("times[i].setTypeface(Typeface.DEFAULT", MAIN)
        root = ET.parse(LAYOUT).getroot()
        table = next(node for node in root.iter() if node.get(ANDROID + "id") == "@+id/prayerTable")
        prayer_times = [node for node in table.iter()
                        if (node.get(ANDROID + "id") or "").startswith("@+id/time")]
        prayer_names = [node for node in table.iter()
                        if (node.get(ANDROID + "id") or "").startswith("@+id/lbl")]
        self.assertEqual(len(prayer_times), 6)
        self.assertEqual(len(prayer_names), 6)
        self.assertTrue(all(node.get(ANDROID + "textSize") == "17sp" for node in prayer_times))
        self.assertTrue(all(node.get(ANDROID + "textSize") == "13sp" for node in prayer_names))

    def test_daily_ayah_uses_larger_native_arabic_and_translation_text(self):
        self.assertIn("ayahArabic.setTextDirection(View.TEXT_DIRECTION_RTL)", MAIN)
        self.assertIn("ayahArabic.setBreakStrategy(android.text.Layout.BREAK_STRATEGY_HIGH_QUALITY)", MAIN)
        self.assertIn("is4K ? 22f : 20f", MAIN)
        self.assertIn("is4K ? 15f : 14f", MAIN)
        root = ET.parse(LAYOUT).getroot()
        arabic = next(node for node in root.iter() if node.get(ANDROID + "id") == "@+id/ayahArabic")
        translation = next(node for node in root.iter() if node.get(ANDROID + "id") == "@+id/ayahText")
        self.assertEqual(arabic.get(ANDROID + "textSize"), "20sp")
        self.assertEqual(translation.get(ANDROID + "textSize"), "14sp")
        self.assertEqual(arabic.get(ANDROID + "fontFamily"), "@font/amiri_quran")

    def test_daily_ayah_scrim_is_subtle_and_has_no_idle_outline(self):
        self.assertIn("new int[]{0x00000000, 0x12000000, 0x26000000}", MAIN)
        self.assertIn("TvFocusIndicator.createRoundedStateBackground(", MAIN)
        self.assertIn("TvUiTokens.FOCUS_HALO", MAIN)
        self.assertIn("TvUiTokens.FOCUS_BORDER", MAIN)
        self.assertIn("hintBg.setStroke(Math.max(1, Math.round(density)), 0x55DCE5ED)", MAIN)
        self.assertIn("ayahBlock.setOnKeyListener", MAIN)
        self.assertIn("showDailyAyahDialog();", MAIN)


if __name__ == "__main__":
    unittest.main()
