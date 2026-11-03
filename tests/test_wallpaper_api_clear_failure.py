"""C07 regression guards for partial Wallpaper API clear failures."""
from pathlib import Path
import unittest


ROOT = Path(__file__).resolve().parents[1]
SOURCE = (
    ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome"
    / "WallpaperUploadServer.java"
)


class WallpaperApiClearFailureTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.source = SOURCE.read_text(encoding="utf-8")

    def test_upload_listener_exposes_non_success_clear_callback(self):
        listener = self.source.split("public interface UploadListener", 1)[1].split(
            "public interface OnWallpaperUploadedListener", 1
        )[0]
        self.assertIn("default void onClearFailed() {}", listener)

    def test_partial_api_clear_notifies_owner_and_returns_failure(self):
        method = self.source.split(
            "private synchronized void handleApiClear(OutputStream out)", 1
        )[1].split(
            "private void sendSimpleJsonResponse(", 1
        )[0]
        self.assertIn('new File(context.getFilesDir(), "custom_wallpapers")', method)
        self.assertIn("boolean deletionFailed = false", method)
        self.assertIn("if (file.isFile() && !file.delete()) deletionFailed = true", method)
        self.assertIn("if (deletionFailed)", method)
        self.assertIn("listener.onClearFailed()", method)
        self.assertIn('delete_failed', method)
        self.assertIn("listener.onCleared()", method)
        self.assertLess(method.index("listener.onClearFailed()"), method.index("listener.onCleared()"))

    def test_api_clear_never_targets_avatar_or_standby_storage(self):
        method = self.source.split(
            "private synchronized void handleApiClear(OutputStream out)", 1
        )[1].split(
            "private void sendSimpleJsonResponse(", 1
        )[0]
        self.assertNotIn('"profile_avatars"', method)
        self.assertNotIn('"standby_photos"', method)


if __name__ == "__main__":
    unittest.main()
