"""Guard against using an unrelated AccessibilityService action ID as reboot."""
from pathlib import Path
import unittest


ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome" / "HomeKeyService.java"


class HomeKeyPowerActionContractTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.source = SOURCE.read_text(encoding="utf-8")

    def test_restart_uses_power_dialog_selection_not_action_id_12(self):
        method = self.source.split("public static boolean restartTv(Context context)", 1)[1]
        method = method.split("public static boolean turnOffTv(Context context)", 1)[0]
        self.assertIn("openPowerDialogAndSelect(POWER_ACTION_RESTART)", method)
        self.assertNotIn("performGlobalAction(12)", method)

    def test_power_dialog_timeout_remains_scoped_to_power_action(self):
        self.assertIn("homeHandler.postDelayed(powerActionTimeout, 12000);", self.source)
        self.assertIn("homeHandler.removeCallbacks(powerActionTimeout);", self.source)


if __name__ == "__main__":
    unittest.main()
