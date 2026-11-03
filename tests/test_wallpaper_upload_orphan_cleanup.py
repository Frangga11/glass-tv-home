"""Recovery guard for interrupted custom-wallpaper QR upload files."""
from pathlib import Path
import unittest


ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome"
MAIN = JAVA / "MainActivity.java"
SERVER = JAVA / "WallpaperUploadServer.java"


class WallpaperUploadOrphanCleanupTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.main = MAIN.read_text(encoding="utf-8")
        cls.server = SERVER.read_text(encoding="utf-8")

    def test_launcher_cleans_abandoned_wallpaper_uploads_before_starting_ui(self):
        on_create = self.main.split("protected void onCreate(Bundle savedInstanceState)", 1)[1]
        cleanup = on_create.index("WallpaperUploadServer.cleanupOrphanedCustomWallpaperUploads(this)")
        content = on_create.index("setContentView(R.layout.activity_main)")
        self.assertLess(cleanup, content)

    def test_cleanup_only_targets_stale_private_custom_wallpaper_upload_parts(self):
        self.assertIn("static void cleanupOrphanedCustomWallpaperUploads(Context context)", self.server)
        method = self.server.split(
            "static void cleanupOrphanedCustomWallpaperUploads(Context context)", 1
        )[1].split(
            "private String localized(", 1
        )[0]
        self.assertIn('new File(context.getFilesDir(), "custom_wallpapers")', method)
        self.assertIn('name.startsWith("custom_") && name.endsWith(".upload")', method)
        self.assertIn("10L * 60L * 1000L", method)
        self.assertIn("partial.lastModified() >= cutoff", method)
        self.assertIn("canonicalPartial.getParentFile()", method)
        self.assertIn("canonicalDirectory", method)
        self.assertIn("canonicalPartial.delete()", method)


if __name__ == "__main__":
    unittest.main()
