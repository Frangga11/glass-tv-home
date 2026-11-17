from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
MAIN = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java"
LANGUAGE = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/AppLanguage.java"
PRAYER = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/PrayerTimes.java"

class PrayerSourceLabelRemovalContract(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.main = MAIN.read_text(encoding="utf-8")
        cls.language = LANGUAGE.read_text(encoding="utf-8")
        cls.prayer = PRAYER.read_text(encoding="utf-8")

    def test_islamic_shelf_header_is_restored_without_source_label(self):
        start = self.main.index("if (prayerHeader != null)", self.main.index("Location.Zone currentZone"))
        end = self.main.index("PrayerTimes.PrayerItem[] items", start)
        header = self.main[start:end]
        self.assertIn("AppLanguage.getPrayerHeaderPrefix(this)", header)
        self.assertIn("PrayerTimes.getHijriDate(this)", header)
        self.assertNotIn("getPrayerDataSourceLabel", header)
        self.assertNotIn("activePrayerZone", header)

    def test_compact_prayer_summary_keeps_location_and_next_prayer_only(self):
        start = self.prayer.index("String zoneHeader = getShortZoneName(context)")
        end = self.prayer.index("int l1End = sb.length()", start)
        summary_header = self.prayer[start:end]
        self.assertIn('sb.append(zoneHeader).append("  •  ").append(nextWord)', summary_header)
        self.assertNotIn("KEMENAG RI", summary_header)
        self.assertNotIn("PERKIRAAN", summary_header)
        self.assertNotIn("LOCAL ESTIMATE", summary_header)

    def test_prayer_source_label_helper_is_not_exposed_in_home_ui_language_api(self):
        self.assertNotIn("getPrayerDataSourceLabel", self.language)

if __name__ == "__main__":
    unittest.main()
