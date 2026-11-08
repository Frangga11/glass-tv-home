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
        stored = self.weekly[self.weekly.index("static Ayah stored"):self.weekly.index("/** True when any valid verse", self.weekly.index("static Ayah stored"))]
        self.assertIn("hasUsableContent(p)", stored)
        self.assertNotIn("text_indonesian", stored)
        self.assertIn("boolean hasTranslation", self.weekly)

    def test_refresh_uses_single_flight_and_persisted_retry_cooldown(self):
        self.assertIn("REFRESH_RUNNING.compareAndSet(false, true)", self.weekly)
        self.assertIn("REFRESH_RUNNING.set(false)", self.weekly)
        self.assertIn("RETRY_COOLDOWN_MS = 30L * 60L * 1000L", self.weekly)
        self.assertIn("KEY_LAST_ATTEMPT_DAY", self.weekly)
        self.assertIn("KEY_LAST_ATTEMPT_MS", self.weekly)
        self.assertIn("WeeklyAyah.shouldAttemptRefresh(this)", self.main)

    def test_cache_uses_exact_day_index_not_repeating_verse_key_alone(self):
        self.assertIn("KEY_DAY_INDEX = \"day_index\"", self.weekly)
        self.assertIn("p.getInt(KEY_DAY_INDEX, Integer.MIN_VALUE) != day()", self.weekly)
        self.assertIn(".putInt(KEY_DAY_INDEX, dayIndex)", self.weekly)
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

    def test_arabic_ayah_is_rtl_right_aligned_and_translation_is_ltr_left_aligned(self):
        shelf_arabic = self.layout[self.layout.index('android:id="@+id/ayahArabic"'):self.layout.index('android:id="@+id/ayahText"')]
        shelf_translation = self.layout[self.layout.index('android:id="@+id/ayahText"'):self.layout.index('</LinearLayout>', self.layout.index('android:id="@+id/ayahText"'))]
        self.assertIn('android:gravity="end"', shelf_arabic)
        self.assertIn('android:textDirection="rtl"', shelf_arabic)
        self.assertIn('android:gravity="start"', shelf_translation)
        self.assertIn('android:textDirection="ltr"', shelf_translation)
        self.assertIn('ayahArabic.setTextDirection(View.TEXT_DIRECTION_RTL)', self.main)
        self.assertIn('ayahArabic.setGravity(Gravity.END)', self.main)
        self.assertIn('ayahText.setTextDirection(View.TEXT_DIRECTION_LTR)', self.main)
        self.assertIn('ayahText.setGravity(Gravity.START)', self.main)
        self.assertIn('arabic.setTextDirection(View.TEXT_DIRECTION_RTL)', self.main)
        self.assertIn('arabic.setGravity(Gravity.END)', self.main)
        self.assertIn('translation.setTextDirection(View.TEXT_DIRECTION_LTR)', self.main)
        self.assertIn('translation.setGravity(Gravity.START)', self.main)

    def test_failed_incomplete_payload_is_not_written_to_cache(self):
        self.assertIn("Incomplete Quran verse payload", self.weekly)
        self.assertIn("if (arabic.isEmpty()", self.weekly)

if __name__ == "__main__":
    unittest.main()
