"""Cross-feature persistence and recovery contracts for Project F05.

These host-side source guards do not simulate Android force-stop or physical reboot.
"""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET


ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome"
MAIN = JAVA / "MainActivity.java"
PROFILE = JAVA / "ProfileManager.java"
STANDBY = JAVA / "StandbyPhotoStore.java"
CLOCK = JAVA / "ClockDream.java"
NAV = JAVA / "NavigationState.java"
SETTINGS = JAVA / "ModernSettingsDialog.java"
MANIFEST = ROOT / "launcher" / "app" / "src" / "main" / "AndroidManifest.xml"
ANDROID_NS = "{http://schemas.android.com/apk/res/android}"


class PersistenceRecoveryContractTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.main = MAIN.read_text(encoding="utf-8")
        cls.profile = PROFILE.read_text(encoding="utf-8")
        cls.standby = STANDBY.read_text(encoding="utf-8")
        cls.clock = CLOCK.read_text(encoding="utf-8")
        cls.nav = NAV.read_text(encoding="utf-8")
        cls.settings = SETTINGS.read_text(encoding="utf-8")
        cls.manifest = ET.parse(MANIFEST).getroot()
        cls.application = cls.manifest.find("application")

    def test_avatar_profile_reference_is_serialized_and_restored(self):
        self.assertIn('obj.put("customAvatarPath", customAvatarPath)', self.profile)
        self.assertIn('obj.optString("customAvatarPath", null)', self.profile)
        self.assertIn('sp.getString(KEY_PROFILES_JSON, null)', self.profile)
        self.assertIn('getPrefs(context).getString(KEY_ACTIVE_PROFILE_ID, null)', self.profile)

    def test_avatar_replacement_persists_new_reference_before_old_file_cleanup(self):
        method = self.profile.split(
            "public static synchronized boolean replaceCustomAvatar(", 1
        )[1].split(
            "public static synchronized boolean selectBuiltInAvatar(", 1
        )[0]
        self.assertLess(method.index(".commit()"), method.index("deleteAvatarIfUnreferenced"))
        self.assertIn("if (!saved)", method)
        self.assertIn("target.customAvatarPath = oldPath", method)

    def test_avatar_orphan_cleanup_is_invoked_on_launcher_startup(self):
        on_create = self.main.split("protected void onCreate(Bundle savedInstanceState)", 1)[1]
        self.assertIn("ProfileManager.cleanupOrphanedAvatarUploads(this)", on_create)
        cleanup = self.profile.split(
            "public static synchronized void cleanupOrphanedAvatarUploads(", 1
        )[1].split(
            "private static SharedPreferences getPrefs(", 1
        )[0]
        self.assertIn("profile.customAvatarPath", cleanup)
        self.assertIn('name.endsWith(".upload")', cleanup)
        self.assertIn("10L * 60L * 1000L", cleanup)

    def test_standby_collection_and_mode_are_one_durable_commit(self):
        method = self.standby.split(
            "static boolean commitUploadedState(", 1
        )[1].split(
            "static boolean isManagedFile(", 1
        )[0]
        self.assertIn(".putString(KEY_URIS, serializedUris)", method)
        self.assertIn(".putBoolean(KEY_MODE, true)", method)
        self.assertIn(".commit()", method)

    def test_standby_clear_persists_empty_state_before_deleting_photo_files(self):
        method = self.standby.split(
            "static synchronized List<Uri> clearCollectionAndMode(", 1
        )[1].split(
            "private static List<String> toUriStrings(", 1
        )[0]
        self.assertLess(method.index("commitClearedState(preferences)"), method.index("clearUploadedFiles(context)"))
        self.assertIn("if (!saved) return null", method)

    def test_standby_orphan_cleanup_and_both_renderers_use_the_same_store(self):
        on_create = self.main.split("protected void onCreate(Bundle savedInstanceState)", 1)[1]
        self.assertIn("StandbyPhotoStore.cleanupOrphanedUploads(this)", on_create)
        cleanup = self.standby.split(
            "static void cleanupOrphanedUploads(Context context)", 1
        )[1].split(
            "static synchronized void clearUploadedFiles(", 1
        )[0]
        self.assertIn("getUris(context)", cleanup)
        self.assertIn("referenced.contains", cleanup)
        self.assertIn("file.delete()", cleanup)
        self.assertIn("StandbyPhotoStore.getUris(this)", self.clock)
        self.assertIn("StandbyPhotoStore.getUris(this)", self.main)

    def test_home_focus_and_settings_tab_are_persisted_using_stable_keys(self):
        self.assertIn('static final String PREFS = "navigation_state"', self.nav)
        self.assertIn("NavigationState.HOME_FOCUS_KEY", self.main)
        self.assertIn("NavigationState.HOME_FOCUS_POSITION", self.main)
        self.assertIn("NavigationState.SETTINGS_TAB", self.settings)
        self.assertIn("navigationPrefs.getInt(NavigationState.SETTINGS_TAB", self.settings)
        self.assertIn("putInt(NavigationState.SETTINGS_TAB", self.settings)
        focus = self.main.split("public void onTileFocused(", 1)[1].split(
            "// Tile rect in root coordinates", 1
        )[0]
        self.assertIn(".remove(NavigationState.HOME_FOCUS_TOP_PILL_KEY).apply()", focus)

    def test_normal_boot_restores_prayer_alarm_without_enabling_backup(self):
        permissions = {
            node.get(ANDROID_NS + "name")
            for node in self.manifest.findall("uses-permission")
        }
        self.assertIn("android.permission.RECEIVE_BOOT_COMPLETED", permissions)
        self.assertEqual("false", self.application.get(ANDROID_NS + "allowBackup"))
        receivers = [
            node for node in self.application.findall("receiver")
            if node.get(ANDROID_NS + "name") == ".AzanBootReceiver"
        ]
        self.assertEqual(1, len(receivers))
        self.assertEqual("false", receivers[0].get(ANDROID_NS + "exported"))
        actions = {
            node.get(ANDROID_NS + "name")
            for node in receivers[0].findall("intent-filter/action")
        }
        self.assertIn("android.intent.action.BOOT_COMPLETED", actions)
        receiver = (JAVA / "AzanBootReceiver.java").read_text(encoding="utf-8")
        policy = (JAVA / "AzanBootRecoveryPolicy.java").read_text(encoding="utf-8")
        self.assertIn("Location.chosen(context)", receiver)
        self.assertIn("Azan.schedule(context, selectedZone)", receiver)
        self.assertIn("ACTION_BOOT_COMPLETED.equals(action)", policy)

    def test_wallpaper_custom_selection_recovery_is_part_of_startup_path(self):
        on_create = self.main.split("protected void onCreate(Bundle savedInstanceState)", 1)[1]
        self.assertIn("reconcileActiveWallpaperCategoryForStartup()", on_create)
        helper = self.main.split(
            "private String reconcileActiveWallpaperCategoryForStartup()", 1
        )[1].split(
            "private boolean isSupportedWallpaperCategory(", 1
        )[0]
        self.assertIn("UploadFilePolicy.isSafeStoredImageName(singleFile)", helper)
        self.assertIn('putString("active_category", "Natural")', helper)


if __name__ == "__main__":
    unittest.main()
