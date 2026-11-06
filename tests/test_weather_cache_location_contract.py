"""G01 regression guards for location-scoped weather/AQI cache state."""
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome" / "WeatherAirQuality.java"


class WeatherCacheLocationContractTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.source = JAVA.read_text(encoding="utf-8")

    def test_weather_and_aqi_keep_independent_success_and_location_metadata(self):
        for key in (
            "KEY_WEATHER_LAST_SUCCESS", "KEY_AQI_LAST_SUCCESS",
            "KEY_WEATHER_LAT", "KEY_WEATHER_LON", "KEY_AQI_LAT", "KEY_AQI_LON",
        ):
            self.assertIn(key, self.source)

    def test_home_summary_rejects_cached_data_from_a_different_location(self):
        method = self.source.split("static CharSequence getFormatted", 1)[1].split(
            "static FullWeather getFullWeather", 1
        )[0]
        self.assertIn("cacheMatchesLocation(prefs, KEY_WEATHER_LAT, KEY_WEATHER_LON", method)
        self.assertIn("cacheMatchesLocation(prefs, KEY_AQI_LAT, KEY_AQI_LON", method)

    def test_weather_dashboard_rejects_cached_data_from_a_different_location(self):
        method = self.source.split("static FullWeather getFullWeather", 1)[1]
        self.assertIn("cacheMatchesLocation(prefs, KEY_WEATHER_LAT, KEY_WEATHER_LON", method)
        self.assertIn("cacheMatchesLocation(prefs, KEY_AQI_LAT, KEY_AQI_LON", method)

    def test_only_successful_source_responses_advance_their_own_cache_metadata(self):
        method = self.source.split("private static void syncInternal", 1)[1].split(
            "static boolean isCurrentRequest", 1
        )[0]
        self.assertIn("if (wJson != null)", method)
        self.assertIn("if (aqiJson != null)", method)
        self.assertIn("putLong(KEY_WEATHER_LAST_SUCCESS, completedAt)", method)
        self.assertIn("putLong(KEY_AQI_LAST_SUCCESS, completedAt)", method)
        self.assertIn("putString(KEY_WEATHER_LAT, Double.toString(lat))", method)
        self.assertIn("putString(KEY_AQI_LAT, Double.toString(lat))", method)


    def test_refresh_freshness_is_scoped_to_the_selected_coordinates(self):
        method = self.source.split("private static void syncInternal", 1)[1].split(
            "static boolean isCurrentRequest", 1
        )[0]
        self.assertIn("isCacheFresh(prefs.contains(KEY_WEATHER_JSON)", method)
        self.assertNotIn("isCacheFresh(prefs.contains(KEY_AQI_JSON)", method)
        self.assertIn("isAqiCacheFresh(prefs.contains(KEY_AQI_JSON)", method)
        self.assertIn("prefs.getBoolean(KEY_AQI_IS_JAS, false)", method)
        self.assertIn("lat, lon, REFRESH_INTERVAL_MS", method)

    def test_weather_and_aqi_requests_cover_the_approved_v4_metrics(self):
        for parameter in (
            "dew_point_2m", "moon_phase", "moonrise", "moonset", "precipitation_sum",
            "carbon_monoxide", "nitrogen_dioxide", "sulphur_dioxide",
        ):
            self.assertIn(parameter, self.source)

    def test_location_comparison_rejects_non_finite_coordinates(self):
        method = self.source.split("static boolean isSameLocation", 1)[1].split(
            "private static boolean cacheMatchesLocation", 1
        )[0]
        self.assertIn("Double.isFinite(cachedLat)", method)
        self.assertIn("Double.isFinite(cachedLon)", method)


if __name__ == "__main__":
    unittest.main()
