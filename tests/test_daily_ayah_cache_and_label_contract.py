from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
WEEKLY = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/WeeklyAyah.java"
LANGUAGE = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/AppLanguage.java"
MAIN = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java"
LAYOUT = ROOT / "launcher/app/src/main/res/layout/activity_main.xml"
ID_STRINGS = ROOT / "launcher/app/src/main/res/values-id/strings.xml"
EN_STRINGS = ROOT / "launcher/app/src/main/res/values-en/strings.xml"

class DailyAyahCacheAndLabelContract(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.weekly = WEEKLY.read_text(encoding="utf-8")
        cls.language = LANGUAGE.read_text(encoding="utf-8")
        cls.main = MAIN.read_text(encoding="utf-8")
        cls.layout = LAYOUT.read_text(encoding="utf-8")
        cls.id_strings = ID_STRINGS.read_text(encoding="utf-8")
        cls.en_strings = EN_STRINGS.read_text(encoding="utf-8")

    def test_current_verse_cache_does_not_require_every_optional_translation(self):
        stored = self.weekly[self.weekly.index("static Ayah stored"):self.weekly.index("static void clearCache")]
        self.assertIn("hasUsableContent(p)", stored)
        self.assertNotIn("text_indonesian", stored)
        self.assertIn("boolean hasTranslation", self.weekly)

    def test_stale_or_local_fallback_state_is_visible(self):
        self.assertIn("static boolean hasCached(Context ctx)", self.weekly)
        self.assertIn("WeeklyAyah.stored(this) != null, WeeklyAyah.hasCached(this)", self.main)
        self.assertIn("SAVED COPY", self.language)
        self.assertIn("TERSIMPAN", self.language)
        self.assertIn("LOCAL FALLBACK", self.language)
        self.assertIn("DATA LOKAL", self.language)

    def test_ayah_label_describes_verse_not_daily_dua(self):
        self.assertIn("AYAT PILIHAN", self.language)
        self.assertIn("DAILY VERSE", self.language)
        self.assertNotIn("DOA HARIAN", self.language)
        self.assertIn("AYAT PILIHAN HARIAN", self.id_strings)
        self.assertIn("DAILY SELECTED VERSE", self.en_strings)

    def test_runtime_does_not_override_xml_arabic_line_spacing(self):
        self.assertNotIn("ayahArabic.setLineSpacing(", self.main)
        self.assertIn("android:lineSpacingExtra=\"-1dp\"", self.layout)
        self.assertIn("android:lineSpacingMultiplier=\"0.94\"", self.layout)

    def test_failed_incomplete_payload_is_not_written_to_cache(self):
        self.assertIn("Incomplete Quran verse payload", self.weekly)
        self.assertIn("if (arabic.isEmpty()", self.weekly)

if __name__ == "__main__":
    unittest.main()
