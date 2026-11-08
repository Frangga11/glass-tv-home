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

    def test_both_shelves_share_a_measured_five_millimetre_downward_anchor(self):
        self.assertIn("HOME_SHELF_DROP_MM = 5f", MAIN)
        self.assertIn("HOME_SHELF_DROP_MM * (160f / 25.4f) * density", MAIN)
        self.assertIn("Math.min(requestedDropPx, maximumSafeDropPx)", MAIN)
        self.assertIn("islamicShelf.setY(iy)", MAIN)
        self.assertIn("float y = heroBottom - heroShelf.getHeight();", MAIN)
        self.assertIn("MIN_HOME_SHELF_TRAY_GAP_DP = 12f", MAIN)

    def test_prayer_names_and_times_receive_subtle_gradients_without_data_changes(self):
        self.assertIn("applyPrayerTextGradient(labels[i], items[i].isNext, false)", MAIN)
        self.assertIn("applyPrayerTextGradient(times[i], items[i].isNext, true)", MAIN)
        self.assertIn("new android.graphics.LinearGradient(", MAIN)
        self.assertIn("Shader.TileMode.CLAMP", MAIN)
        self.assertIn("PrayerTimes.getPrayerItems(this)", MAIN)

    def test_daily_ayah_has_no_idle_frame_but_keeps_remote_focus_feedback(self):
        self.assertIn("focusSurface.setStroke(hasFocus ? Math.max(1, Math.round(density)) : 0,", MAIN)
        self.assertIn("hasFocus ? 0x997DD3FC : 0x00000000", MAIN)
        self.assertIn("hintBg.setStroke(0, 0x00000000)", MAIN)
        self.assertIn("ayahBlock.setOnKeyListener", MAIN)
        self.assertIn("showDailyAyahDialog();", MAIN)


if __name__ == "__main__":
    unittest.main()
