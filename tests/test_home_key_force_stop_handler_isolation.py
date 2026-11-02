"""Ensure Force Stop queue timeouts cannot cancel global Home-key gestures."""
from pathlib import Path
import re
import unittest


ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome" / "HomeKeyService.java"


class HomeKeyForceStopHandlerIsolationTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.source = SOURCE.read_text(encoding="utf-8")

    def test_force_stop_uses_a_dedicated_handler(self):
        self.assertIn(
            "private final Handler forceStopHandler = new Handler(Looper.getMainLooper());",
            self.source,
        )
        self.assertNotIn("homeHandler.removeCallbacksAndMessages(null)", self.source)
        self.assertGreaterEqual(
            self.source.count("forceStopHandler.removeCallbacksAndMessages(null)"),
            4,
        )

    def test_force_stop_timeouts_and_queue_continuations_use_dedicated_handler(self):
        self.assertRegex(
            self.source,
            re.compile(
                r"forceStopHandler\.postDelayed\(\(\) -> \{[\s\S]*?\}, 400\);"
            ),
        )
        self.assertGreaterEqual(
            self.source.count("forceStopHandler.postDelayed(this::processNextForceStop, 80)"),
            2,
        )

    def test_home_gesture_callbacks_remain_on_home_handler(self):
        self.assertIn("homeHandler.postDelayed(longPressHomeRunnable, 450);", self.source)
        self.assertIn("homeHandler.postDelayed(pendingSingleHomeAction", self.source)

    def test_service_destruction_cleans_both_handler_queues(self):
        destroy = self.source.split("public void onDestroy()", 1)[1].split("@Override", 1)[0]
        self.assertIn("forceStopHandler.removeCallbacksAndMessages(null);", destroy)
        self.assertIn("homeHandler.removeCallbacks(longPressHomeRunnable);", destroy)


if __name__ == "__main__":
    unittest.main()
