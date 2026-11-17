"""Regression contracts for restoring cached Home wallpaper weather effects."""

from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")


class HomeWeatherOverlayCacheRestoreContractTest(unittest.TestCase):
    def test_cached_overlay_is_reapplied_before_network_refresh_gate(self):
        start = MAIN.index("private void updateHomeWeather()")
        end = MAIN.index("private void showHomeWeather(", start)
        method = MAIN[start:end]

        self.assertIn("HomeWeather.Line cached = HomeWeather.last(this);", method)
        self.assertIn("if (cached != null) updateWeatherOverlay(cached);", method)
        self.assertLess(method.index("updateWeatherOverlay(cached)"),
                        method.index("if (!HomeWeather.due(this)) return;"))

    def test_refresh_cadence_still_gates_network_fetch_only(self):
        start = MAIN.index("private void updateHomeWeather()")
        end = MAIN.index("private void showHomeWeather(", start)
        method = MAIN[start:end]

        self.assertIn("homeWeatherRunning) return;", method)
        self.assertIn("if (!HomeWeather.due(this)) return;", method)
        self.assertLess(method.index("if (!HomeWeather.due(this)) return;"),
                        method.index("homeWeatherRunning = true;"))
        self.assertIn('new Thread(() -> {', method)

    def test_cached_restore_respects_disabled_setting_and_running_guard(self):
        start = MAIN.index("private void updateHomeWeather()")
        end = MAIN.index("private void showHomeWeather(", start)
        method = MAIN[start:end]
        guard = method[:method.index("HomeWeather.Line cached")]

        self.assertIn("weatherAirQualityView == null", guard)
        self.assertIn("!isDynamicWeatherEffectsEnabled()", guard)
        self.assertIn("homeWeatherRunning", guard)
