"""Ensure elapsed upload/session deadlines are immune to wall-clock changes."""
from pathlib import Path
import unittest


ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome"


class UploadMonotonicDeadlineClockTest(unittest.TestCase):
    def test_all_upload_session_and_read_deadlines_use_elapsed_realtime(self):
        for name in (
            "AvatarUploadServer.java",
            "WallpaperUploadServer.java",
            "UploadDeadlineInputStream.java",
        ):
            source = (JAVA / name).read_text(encoding="utf-8")
            with self.subTest(source=name):
                self.assertIn("SystemClock.elapsedRealtime()", source)
                self.assertNotIn("System.currentTimeMillis()", source)
                self.assertIn("import android.os.SystemClock;", source)


if __name__ == "__main__":
    unittest.main()
