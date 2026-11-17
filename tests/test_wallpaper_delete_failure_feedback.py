"""E08 regression guards for truthful Wallpaper deletion feedback."""
from pathlib import Path
import unittest


ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome"


class WallpaperDeleteFailureFeedbackTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.main = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        cls.server = (JAVA / "WallpaperUploadServer.java").read_text(encoding="utf-8")

    def test_single_delete_only_reports_deleted_after_success_or_prior_absence(self):
        action = self.main.split("case 1: // Delete this", 1)[1].split(
            "case 2: // Keep this, delete others", 1
        )[0]
        self.assertIn("if (!file.exists() || file.delete())", action)
        self.assertIn("R.string.wallpaper_delete_failed", action)

    def test_delete_all_checks_every_file_and_reconciles_partial_failure(self):
        action = self.main.split(
            ".setPositiveButton(R.string.delete_all_action, (d, w) -> {", 1
        )[1].split(
            ".setNegativeButton(R.string.cancel_action, null)", 1
        )[0]
        self.assertIn("f.exists() && !f.delete()", action)
        self.assertIn("if (allDeleted) onCustomWallpaperCleared()", action)
        self.assertIn("else onCustomWallpaperDeleteFailed()", action)

    def test_keep_one_checks_other_file_deletions_before_success_feedback(self):
        action = self.main.split("case 2: // Keep this, delete others", 1)[1].split(
            "case 3: // Back", 1
        )[0]
        self.assertIn("f.exists() && !f.delete()", action)
        self.assertIn("allDeleted", action)
        self.assertIn("R.string.wallpaper_delete_failed", action)

    def test_partial_failure_clears_stale_pin_and_refreshes_remaining_collection(self):
        method = self.main.split("private void onCustomWallpaperDeleteFailed()", 1)[1].split(
            "private void onCustomWallpaperCleared()", 1
        )[0]
        self.assertIn('"custom_wallpapers"', method)
        self.assertIn('putBoolean("custom_single_mode", false)', method)
        self.assertIn('remove("custom_single_file")', method)
        self.assertIn("wallpapers = new ArrayList<>(Arrays.asList(remaining))", method)
        self.assertIn("R.string.wallpaper_delete_failed", method)

    def test_qr_api_clear_failure_callback_is_wired_to_same_reconciliation(self):
        listener = self.main.split("private WallpaperUploadServer.UploadListener createUploadListener()", 1)[1].split(
            "private void onStandbyPhotoUploaded(File file)", 1
        )[0]
        self.assertIn("onClearFailed()", listener)
        self.assertIn("onCustomWallpaperDeleteFailed()", listener)
        self.assertIn("default void onClearFailed() {}", self.server)


if __name__ == "__main__":
    unittest.main()
