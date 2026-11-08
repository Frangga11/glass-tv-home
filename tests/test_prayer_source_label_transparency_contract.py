from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
MAIN = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java"
LANGUAGE = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/AppLanguage.java"
PRAYER = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/PrayerTimes.java"

class PrayerSourceLabelTransparencyContract(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.main = MAIN.read_text(encoding="utf-8")
        cls.language = LANGUAGE.read_text(encoding="utf-8")
        cls.prayer = PRAYER.read_text(encoding="utf-8")

    def test_islamic_shelf_header_shows_source_for_indonesian_zones(self):
        self.assertIn("activePrayerZone.startsWith(\"ID_\")", self.main)
        self.assertIn("AppLanguage.getPrayerDataSourceLabel(", self.main)
        self.assertIn("PrayerTimes.isUsingOfficialKemenagSchedule(this)", self.main)

    def test_source_label_is_localized_and_distinguishes_official_from_estimate(self):
        self.assertIn("KEMENAG RI", self.language)
        self.assertIn("LOCAL ESTIMATE", self.language)
        self.assertIn("PERKIRAAN LOKAL", self.language)
        self.assertIn("ANGGARAN SETEMPAT", self.language)

    def test_compact_prayer_summary_uses_same_source_mode(self):
        self.assertIn("activeZone.startsWith(\"ID_\")", self.prayer)
        self.assertIn("isUsingOfficialKemenagSchedule(context) ? \" • KEMENAG RI\" : \" • PERKIRAAN\"", self.prayer)

if __name__ == "__main__":
    unittest.main()
