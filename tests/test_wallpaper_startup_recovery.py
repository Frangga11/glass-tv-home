"""Startup recovery guards for persisted custom-wallpaper selection.

These source-contract tests cover recovery after abrupt process death or
filesystem changes; physical Android TV behavior remains a Project G gate.
"""
from pathlib import Path
import unittest


ROOT = Path(__file__).resolve().parents[1]
MAIN = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome" / "MainActivity.java"


class WallpaperStartupRecoveryTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.source = MAIN.read_text(encoding="utf-8")

    def test_startup_reconciles_custom_wallpaper_state_before_loading_collection(self):
        on_create = self.source.split("protected void onCreate(Bundle savedInstanceState)", 1)[1]
        reconcile = on_create.index("reconcileActiveWallpaperCategoryForStartup()")
        load = on_create.index('if ("Default".equalsIgnoreCase(activeCat))')
        self.assertLess(reconcile, load)

    def test_missing_or_unsafe_pinned_wallpaper_clears_stale_single_mode(self):
        method = self.source.split(
            "private String reconcileActiveWallpaperCategoryForStartup()", 1
        )[1].split(
            "private boolean isSupportedWallpaperCategory(", 1
        )[0]
        self.assertIn("UploadFilePolicy.isSafeStoredImageName(singleFile)", method)
        self.assertIn("selected.isFile()", method)
        self.assertIn("selected.length() > 0", method)
        self.assertIn('.putBoolean("custom_single_mode", false)', method)
        self.assertIn('.remove("custom_single_file")', method)

    def test_empty_custom_collection_recovers_to_natural_category(self):
        method = self.source.split(
            "private String reconcileActiveWallpaperCategoryForStartup()", 1
        )[1].split(
            "private boolean isSupportedWallpaperCategory(", 1
        )[0]
        self.assertIn('prefs.edit().putString("active_category", "Natural")', method)
        self.assertIn('return "Natural";', method)

    def test_unreadable_existing_directory_does_not_destroy_saved_selection(self):
        method = self.source.split(
            "private String reconcileActiveWallpaperCategoryForStartup()", 1
        )[1].split(
            "private boolean isSupportedWallpaperCategory(", 1
        )[0]
        self.assertIn("customDir.exists() && files == null", method)
        self.assertIn("return category;", method)


if __name__ == "__main__":
    unittest.main()
