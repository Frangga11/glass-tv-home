import pathlib
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
COORDINATOR = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/StandbyCoordinator.java"
MAIN = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java"
CLOCK = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/ClockDream.java"
MANIFEST = ROOT / "launcher/app/src/main/AndroidManifest.xml"


class StandbyLifecycleCoordinatorContractTest(unittest.TestCase):
    def test_idle_timeout_is_owned_by_one_coordinator(self):
        source = COORDINATOR.read_text(encoding="utf-8")
        self.assertIn("15L * 60L * 1000L", source)
        self.assertIn("WeakReference<MainActivity>", source)
        self.assertNotIn("Runnable showStandby", source)
        self.assertIn("activity.showNightstandScreensaver()", source)
        self.assertIn("void onUserInteraction()", source)
        self.assertIn("void onHomePaused()", source)
        self.assertIn("void onManualStandbyStarted()", source)
        self.assertIn("void onDreamStarted()", source)
        self.assertIn("void onDreamStopped()", source)

    def test_home_and_native_dream_report_lifecycle_to_coordinator(self):
        main = MAIN.read_text(encoding="utf-8")
        clock = CLOCK.read_text(encoding="utf-8")
        self.assertIn("standbyCoordinator.onUserInteraction()", main)
        self.assertIn("standbyCoordinator.onHomeResumed()", main)
        self.assertIn("standbyCoordinator.onHomePaused()", main)
        self.assertIn("standbyCoordinator.onManualStandbyStarted()", main)
        self.assertIn("standbyCoordinator.onManualStandbyDismissed()", main)
        self.assertIn("StandbyCoordinator.get(this).onDreamStarted()", clock)
        self.assertIn("StandbyCoordinator.get(this).onDreamStopped()", clock)

    def test_android_remains_owner_of_native_dream_registration(self):
        manifest = MANIFEST.read_text(encoding="utf-8")
        self.assertIn('android.permission.BIND_DREAM_SERVICE', manifest)
        self.assertIn('android.service.dreams.DreamService', manifest)


if __name__ == "__main__":
    unittest.main()
