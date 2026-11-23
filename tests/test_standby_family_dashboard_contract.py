import pathlib
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
CLOCK = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/ClockDream.java"
LAYOUT = ROOT / "launcher/app/src/main/res/layout/view_nightstand.xml"


class StandbyFamilyDashboardContractTest(unittest.TestCase):
    def test_dashboard_reuses_existing_weather_and_prayer_owners(self):
        source = CLOCK.read_text(encoding="utf-8")
        self.assertIn("WeatherAirQuality.getFullWeather(this)", source)
        self.assertIn("WeatherAirQuality.syncIfDue(this", source)
        self.assertIn("PrayerTimes.day(this, null, PrayerTimes.calendar(this))", source)
        self.assertIn("PrayerTimes.getNextPrayerSummary(this)", source)
        self.assertNotIn("https://api.open-meteo.com", source)

    def test_compact_panel_and_five_prayer_columns_are_present(self):
        layout = LAYOUT.read_text(encoding="utf-8")
        self.assertIn('android:layout_width="280dp"', layout)
        self.assertIn('android:gravity="center_horizontal"', layout)
        for prayer in ("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha"):
            self.assertIn(f"nightstand{prayer}Time", layout)

    def test_custom_standby_photo_overlay_remains_available(self):
        layout = LAYOUT.read_text(encoding="utf-8")
        source = CLOCK.read_text(encoding="utf-8")
        self.assertIn('android:id="@+id/nightstandPhoto"', layout)
        self.assertIn("StandbyPhotoStore.loadBitmapAsync", source)
        self.assertIn("StandbyPhotoStore.ROTATION_INTERVAL_MS", source)


if __name__ == "__main__":
    unittest.main()
