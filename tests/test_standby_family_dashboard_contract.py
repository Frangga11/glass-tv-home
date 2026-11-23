import pathlib
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
CLOCK = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/ClockDream.java"
BINDER = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/StandbyDashboardBinder.java"
MAIN = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java"
LAYOUT = ROOT / "launcher/app/src/main/res/layout/view_nightstand.xml"


class StandbyFamilyDashboardContractTest(unittest.TestCase):
    def test_both_standby_paths_share_existing_weather_and_prayer_owners(self):
        binder = BINDER.read_text(encoding="utf-8")
        clock = CLOCK.read_text(encoding="utf-8")
        main = MAIN.read_text(encoding="utf-8")
        self.assertIn("WeatherAirQuality.getFullWeather(context)", binder)
        self.assertIn("PrayerTimes.day(context, null, PrayerTimes.calendar(context))", binder)
        self.assertIn("WeatherAirQuality.syncIfDue(this", clock)
        self.assertIn("PrayerTimes.syncIfDue(this", clock)
        self.assertIn("StandbyDashboardBinder.updateMetrics(this, content", clock)
        self.assertIn("StandbyDashboardBinder.updateMetrics(this, screensaverView", main)
        self.assertNotIn("https://api.open-meteo.com", binder)

    def test_compact_centered_panel_and_five_prayer_columns_are_present(self):
        layout = LAYOUT.read_text(encoding="utf-8")
        self.assertIn('android:layout_width="280dp"', layout)
        self.assertIn('android:gravity="center_horizontal"', layout)
        self.assertIn('android:id="@+id/nightstandAmPm"', layout)
        self.assertIn('android:visibility="gone"', layout)
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
