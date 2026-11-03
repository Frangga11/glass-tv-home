"""Cross-feature storage and route isolation guards for Project F04.

These are source-contract regression tests. They do not replace runtime LAN,
process-death, Android TV, or remote-control acceptance in Project G.
"""
from pathlib import Path
import unittest


ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome"


class MediaFeatureIsolationTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.avatar = (JAVA / "AvatarUploadServer.java").read_text(encoding="utf-8")
        cls.profiles = (JAVA / "ProfileManager.java").read_text(encoding="utf-8")
        cls.wallpaper = (JAVA / "WallpaperUploadServer.java").read_text(encoding="utf-8")
        cls.standby = (JAVA / "StandbyPhotoStore.java").read_text(encoding="utf-8")
        cls.metadata = (JAVA / "WallpaperMetadata.java").read_text(encoding="utf-8")
        cls.main = (JAVA / "MainActivity.java").read_text(encoding="utf-8")

    def test_each_feature_uses_a_distinct_private_storage_root(self):
        self.assertIn('new File(context.getFilesDir(), "profile_avatars")', self.avatar)
        self.assertIn('new File(context.getFilesDir(), "profile_avatars")', self.profiles)
        self.assertIn('new File(context.getFilesDir(), "custom_wallpapers")', self.wallpaper)
        self.assertIn('new File(context.getFilesDir(), "standby_photos")', self.standby)
        self.assertNotEqual("profile_avatars", "custom_wallpapers")
        self.assertNotEqual("profile_avatars", "standby_photos")
        self.assertNotEqual("custom_wallpapers", "standby_photos")

    def test_avatar_upload_is_bound_to_the_requested_profile_and_private_directory(self):
        self.assertIn("private final String profileId;", self.avatar)
        self.assertIn('String safeId = profileId == null ? "" : profileId.replaceAll', self.avatar)
        self.assertIn('File dir = new File(context.getFilesDir(), "profile_avatars")', self.avatar)
        self.assertIn("listener.onAvatarUploaded(delivered)", self.avatar)
        self.assertIn("public static synchronized boolean replaceCustomAvatar(Context context, String profileId, File newPhoto)", self.profiles)
        self.assertIn("if (!isPrivateAvatarFile(avatarDir, candidate)) return false;", self.profiles)
        self.assertIn("for (UserProfile profile : list) if (profileId.equals(profile.id))", self.profiles)

    def test_avatar_replacement_commits_profile_reference_before_old_file_cleanup(self):
        method = self.profiles.split(
            "public static synchronized boolean replaceCustomAvatar(", 1
        )[1].split(
            "public static synchronized boolean selectBuiltInAvatar(", 1
        )[0]
        self.assertLess(method.index(".commit()"), method.index("deleteAvatarIfUnreferenced"))
        cleanup = self.profiles.split(
            "private static void deleteAvatarIfUnreferenced(", 1
        )[1].split(
            "public static synchronized void cleanupOrphanedAvatarUploads(", 1
        )[0]
        self.assertIn("profile.customAvatarPath", cleanup)
        self.assertIn("candidate.delete()", cleanup)

    def test_standby_owns_its_preferences_and_only_its_photo_directory(self):
        self.assertIn('private static final String PREFS = "standby_prefs";', self.standby)
        self.assertIn('new File(context.getFilesDir(), "standby_photos")', self.standby)
        self.assertIn('new File(context.getFilesDir(), "standby_photos").getCanonicalFile()', self.standby)
        clear = self.standby.split(
            "static synchronized void clearUploadedFiles(Context context)", 1
        )[1].split(
            "static boolean isPhotoMode(", 1
        )[0]
        self.assertIn('"standby_photos"', clear)
        self.assertNotIn('"custom_wallpapers"', clear)
        self.assertNotIn('"profile_avatars"', clear)

    def test_standby_upload_commits_collection_and_mode_as_one_durable_transition(self):
        commit = self.standby.split(
            "static boolean commitUploadedState(", 1
        )[1].split(
            "static boolean isManagedFile(", 1
        )[0]
        self.assertIn('.putString(KEY_URIS, serializedUris)', commit)
        self.assertIn('.putBoolean(KEY_MODE, true)', commit)
        self.assertIn(".commit()", commit)
        self.assertIn("StandbyPhotoStore.addUploadedFile(context, storedFile)", self.wallpaper)
        self.assertIn("if (responseStatus != 200)", self.wallpaper)
        self.assertIn("listener.onStandbyUploaded(storedFile)", self.wallpaper)

    def test_wallpaper_and_standby_qr_routes_are_mode_scoped(self):
        self.assertIn('return "/standby/" + sessionToken;', self.wallpaper)
        self.assertIn('return "/wallpaper/" + sessionToken;', self.wallpaper)
        self.assertIn("private final boolean standbyOnly;", self.wallpaper)
        self.assertIn("standbyOnly && !standbyRoute", self.wallpaper)
        self.assertIn("!standbyOnly && wallpaperAllowed == null", self.wallpaper)
        self.assertIn("standbyOnly && isRequestToPath(requestLine, \"POST\", getStandbyUploadPath())", self.wallpaper)
        self.assertIn('isRequestToPath(requestLine, "POST", getWallpaperUploadPath())', self.wallpaper)

    def test_wallpaper_clear_is_scoped_to_custom_wallpapers(self):
        clear = self.wallpaper.split(
            "private synchronized void handleApiClear(OutputStream out)", 1
        )[1].split(
            "private void sendSimpleJsonResponse(", 1
        )[0]
        self.assertIn('new File(context.getFilesDir(), "custom_wallpapers")', clear)
        self.assertIn("listener.onCleared()", clear)
        self.assertNotIn('"standby_photos"', clear)
        self.assertNotIn('"profile_avatars"', clear)

    def test_bundled_default_metadata_does_not_capture_custom_wallpapers(self):
        self.assertIn('private static final String DEFAULT_DEMPO_FILE = "kabut-pagi-dempo-default.jpg";', self.metadata)
        self.assertIn('if (picture == null || !DEFAULT_DEMPO_FILE.equals(picture.getName())) return null;', self.metadata)


    def test_avatar_qr_callback_targets_the_captured_profile_id(self):
        method = self.main.split(
            "void showAvatarUploadQrDialog(String profileId, Runnable onUpdated)", 1
        )[1].split(
            "void showWallpaperUploadQrDialog()", 1
        )[0]
        self.assertIn("new AvatarUploadServer(this, profileId, file ->", method)
        self.assertIn("ProfileManager.replaceCustomAvatar(this, profileId, file)", method)
        self.assertNotIn("ProfileManager.getActiveProfile(this)", method)

    def test_wallpaper_and_standby_qr_sessions_use_separate_server_modes(self):
        wallpaper = self.main.split(
            "void showWallpaperUploadQrDialog()", 1
        )[1].split(
            "void showStandbyUploadQrDialog()", 1
        )[0]
        standby = self.main.split(
            "void showStandbyUploadQrDialog()", 1
        )[1].split(
            "void showPhoneUploadQrDialog(", 1
        )[0]
        self.assertIn("new WallpaperUploadServer(this, 8080, createUploadListener())", wallpaper)
        self.assertIn("uploadServer.getWallpaperPath()", wallpaper)
        self.assertIn("new WallpaperUploadServer(this, 8080, listener, true)", standby)
        self.assertIn("uploadServer.getStandbyPath()", standby)
        self.assertIn("onStandbyUploaded(File imageFile)", standby)
        self.assertIn("onStandbyPhotoUploaded(imageFile)", standby)

    def test_shared_qr_dialog_dismissal_stops_both_server_types(self):
        method = self.main.split(
            "void showPhoneUploadQrDialog(", 1
        )[1].split(
            "private WallpaperUploadServer.UploadListener createUploadListener()", 1
        )[0]
        dismiss = method.split("setOnDismissListener(", 1)[1]
        self.assertIn("uploadServer.stop()", dismiss)
        self.assertIn("avatarUploadServer.stop()", dismiss)
        self.assertIn("uploadServer = null", dismiss)
        self.assertIn("avatarUploadServer = null", dismiss)

    def test_standby_ui_refresh_requires_durable_collection_membership(self):
        method = self.main.split(
            "private void onStandbyPhotoUploaded(File file)", 1
        )[1].split(
            "private void onCustomWallpaperUploaded(File file)", 1
        )[0]
        self.assertIn("StandbyPhotoStore.getUris(this).contains(Uri.fromFile(file))", method)
        self.assertIn("if (screensaverActive) updateNightstandScreensaver()", method)


    def test_bulk_wallpaper_cleanup_checks_delete_failures_before_success_feedback(self):
        delete_all = self.main.split(
            ".setPositiveButton(R.string.delete_all_action, (d, w) -> {", 1
        )[1].split(
            ".setNegativeButton(R.string.cancel_action, null)", 1
        )[0]
        keep_one = self.main.split(
            "case 2: // Keep this, delete others", 1
        )[1].split(
            "case 3: // Back", 1
        )[0]
        self.assertIn("if (!f.delete())", delete_all)
        self.assertIn("onCustomWallpaperCleared()", delete_all)
        self.assertIn("if (!f.delete())", keep_one)
        self.assertIn("onCustomWallpaperSelected(file)", keep_one)

    def test_both_standby_renderers_share_the_same_private_photo_store(self):
        dream = (JAVA / "ClockDream.java").read_text(encoding="utf-8")
        self.assertIn("StandbyPhotoStore.getUris(this)", dream)
        self.assertIn("StandbyPhotoStore.isPhotoMode(this)", dream)
        self.assertIn("StandbyPhotoStore.getCurrentPhotoIndex(uris.size())", dream)
        overlay = self.main.split(
            "private void updateNightstandPhoto()", 1
        )[1].split(
            "private void dismissScreensaver()", 1
        )[0]
        self.assertIn("StandbyPhotoStore.getUris(this)", overlay)
        self.assertIn("StandbyPhotoStore.isPhotoMode(this)", overlay)
        self.assertIn("StandbyPhotoStore.getCurrentPhotoIndex(uris.size())", overlay)


if __name__ == "__main__":
    unittest.main()
