"""Regression guard for the Home-key Recent Apps entry and close-all routing."""
from pathlib import Path
import unittest


ROOT = Path(__file__).resolve().parents[1]
HOME_SERVICE = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome" / "HomeKeyService.java"
RECENT_APPS = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome" / "RecentAppsActivity.java"


class HomeKeyRecentAppsContractTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.home_service = HOME_SERVICE.read_text(encoding="utf-8")
        cls.recent_apps = RECENT_APPS.read_text(encoding="utf-8")

    def test_double_home_opens_recent_apps_from_other_activities(self):
        self.assertIn("new Intent(this, RecentAppsActivity.class)", self.home_service)
        self.assertIn("HomeKeyPressTracker.ReleaseResult.DOUBLE_PRESS", self.home_service)

    def test_double_home_inside_recent_apps_triggers_close_all(self):
        self.assertIn("RecentAppsActivity.triggerCloseAllFromGlobal()", self.home_service)
        self.assertIn("void triggerCloseAllFromGlobal()", self.recent_apps)

    def test_single_home_still_returns_home_or_dismisses_recent_apps(self):
        self.assertIn("RecentAppsActivity.dismissFromGlobal()", self.home_service)
        self.assertIn("Intent.CATEGORY_HOME", self.home_service)
        self.assertIn("HomeKeyPressTracker.DOUBLE_PRESS_WINDOW_MS", self.recent_apps)

    def test_app_switch_key_remains_disabled_by_existing_policy(self):
        self.assertIn("KeyEvent.KEYCODE_APP_SWITCH", self.home_service)
        self.assertIn("Live apps disabled per request", self.home_service)

    def test_settings_key_cancels_pending_home_navigation(self):
        settings_branch = self.home_service.split("if (isSettingsKey(event))", 1)[1].split("int keyCode", 1)[0]
        self.assertIn("cancelPendingSingleHomeAction();", settings_branch)
        self.assertIn("homePressTracker.cancel();", settings_branch)

    def test_long_home_still_opens_settings_after_existing_hold_threshold(self):
        self.assertIn("homeHandler.postDelayed(longPressHomeRunnable, 450);", self.home_service)
        self.assertIn("openSettingsPopupGlobal();", self.home_service)


if __name__ == "__main__":
    unittest.main()
