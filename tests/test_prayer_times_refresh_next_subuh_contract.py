from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
PRAYER = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/PrayerTimes.java"
MAIN = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java

class PrayerTimesRefreshAndNextSubuhContract(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.prayer = PRAYER.read_text(encoding="utf-8")
        cls.main = MAIN.read_text(encoding="utf-8")

    def test_sync_has_single_flight_and_retry_cooldown(self):
        self.assertIn("SYNC_RUNNING.compareAndSet(false, true)", self.prayer)
        self.assertIn("SYNC_RUNNING.set(false)", self.prayer)
        self.assertIn("SYNC_RETRY_COOLDOWN_MS", self.prayer)
        self.assertIn("KEY_LAST_SYNC_ATTEMPT", self.prayer)

    def test_active_home_checks_for_monthly_or_annual_refresh(self):
        self.assertIn("PrayerTimes.syncIfDue(MainActivity.this, MainActivity.this::updatePrayerTimes);", self.main)
        self.assertIn("PrayerTimes.syncIfDue(this, this::updatePrayerTimes);", self.main)

    def test_all_prayer_views_use_shared_next_prayer_selection(self):
        self.assertGreaterEqual(self.prayer.count("findNextPrayerIndex(times, nowMs)"), 4)

    def test_after_isya_uses_tomorrow_subuh_in_prayer_rows(self):
        start = self.prayer.index("static PrayerItem[] getPrayerItems")
        end = self.prayer.index("static CharSequence getFormatted", start)
        block = self.prayer[start:end]
        self.assertIn("nextSubuhTime(times, getTomorrowTimes(context, now, times))", block)
        self.assertIn("displayTimes[0]", block)

if __name__ == "__main__":
    unittest.main()
