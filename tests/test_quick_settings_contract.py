"""Source contracts for the G3D Quick Settings surface and its existing action owners."""
from pathlib import Path
import re
import unittest


ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher/app/src/main/java/com/gathin/tvhome"
RES = ROOT / "launcher/app/src/main/res"
MAIN = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
DIALOG = (JAVA / "QuickSettingsDialog.java").read_text(encoding="utf-8")
GRAPH = (JAVA / "QuickSettingsFocusGraph.java").read_text(encoding="utf-8")
MANIFEST = (ROOT / "launcher/app/src/main/AndroidManifest.xml").read_text(encoding="utf-8")


class QuickSettingsContractTest(unittest.TestCase):
    def test_dialog_uses_shared_modal_and_focus_contracts(self):
        self.assertIn("TvDialogStyle.apply(dialog, activity, 560, 420)", DIALOG)
        self.assertIn("TvUiTokens.", DIALOG)
        self.assertIn("TvFocusIndicator.applyRoundedTextFocus", DIALOG)
        self.assertIn("setCanceledOnTouchOutside(false)", DIALOG)
        self.assertIn("dialog.setOnDismissListener", DIALOG)
        self.assertIn("dialog.setCancelable(true)", DIALOG)

    def test_all_approved_quick_actions_route_to_existing_owners(self):
        for source in (
            "showRestartDialog()",
            "showTurnOffDialog()",
            "showLanguageDialog()",
            "showCleanTvCacheDialog()",
            "showNextWallpaperFromSettings()",
            "showWallpaperIntervalDialog(false)",
            "showWallpaperDialog()",
            "openSystemSettings(systemIntent(Settings.ACTION_SETTINGS))",
        ):
            self.assertIn(source, MAIN)

    def test_wifi_child_route_restores_quick_settings_and_focus(self):
        self.assertIn("showWifiPopup(() -> showQuickControlPopup(QuickSettingsFocusGraph.WIFI))", MAIN)
        self.assertIn("WifiSettingsDialog.show(this", MAIN)
        self.assertIn("returnToParent", MAIN)
        self.assertIn("returnToParent.run()", (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8"))
        self.assertIn("applyAnchoredPopup();", (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8"))

    def test_two_column_focus_graph_has_explicit_dpad_edges(self):
        for setter in (
            "setNextFocusLeftId",
            "setNextFocusRightId",
            "setNextFocusUpId",
            "setNextFocusDownId",
        ):
            self.assertIn(setter, GRAPH)
        self.assertIn("QuickSettingsFocusGraph.connect(controls)", DIALOG)

    def test_cache_result_callback_does_not_retain_or_use_destroyed_activity(self):
        self.assertIn("new java.lang.ref.WeakReference<>(MainActivity.this)", MAIN)
        self.assertIn("activity == null || activity.isFinishing() || activity.isDestroyed() || !activity.hasWindowFocus()", MAIN)

    def test_quick_settings_network_summary_is_independent_of_optional_top_bar_pill(self):
        self.assertIn("private String getQuickSettingsNetworkSummary()", MAIN)
        self.assertIn("return getQuickSettingsNetworkSummary();", MAIN)
        self.assertIn("cm.getAllNetworks()", MAIN)
        self.assertIn("NetworkCapabilities.TRANSPORT_WIFI", MAIN)
        self.assertIn("NetworkCapabilities.TRANSPORT_ETHERNET", MAIN)
        self.assertNotIn("btnWifi.getText()", MAIN[MAIN.index("private String getQuickSettingsNetworkSummary()"):MAIN.index("private void updateWifiLabel()")])

    def test_quick_settings_does_not_fake_system_radio_controls(self):
        for forbidden in ("setWifiEnabled(", "BluetoothAdapter", "BluetoothManager", "ACTION_AIRPLANE_MODE_CHANGED"):
            self.assertNotIn(forbidden, DIALOG)
            self.assertNotIn(forbidden, MAIN)

    def test_manifest_does_not_gain_new_system_toggle_permissions(self):
        for forbidden in (
            "android.permission.BLUETOOTH_CONNECT",
            "android.permission.BLUETOOTH_ADMIN",
            "android.permission.WRITE_SETTINGS",
        ):
            self.assertNotIn(forbidden, MANIFEST)

    def test_localized_quick_settings_resources_are_present_and_parity_safe(self):
        files = [
            RES / "values/strings.xml",
            RES / "values-id/strings.xml",
            RES / "values-en/strings.xml",
        ]
        keys = {
            "quick_settings_title",
            "quick_settings_wifi_summary",
            "quick_settings_restart_summary",
            "quick_settings_power_summary",
            "quick_settings_cache_summary",
            "quick_settings_wallpaper_title",
            "quick_settings_next_photo",
            "quick_settings_next_photo_summary",
            "quick_settings_interval_title",
            "quick_settings_interval_summary",
            "quick_settings_manage_wallpaper",
            "quick_settings_glass_settings",
            "quick_settings_glass_settings_summary",
            "quick_settings_android_settings",
            "quick_settings_android_settings_summary",
        }
        contents = [p.read_text(encoding="utf-8") for p in files]
        for content in contents:
            found = set(re.findall(r'<string name="([^"]+)"', content))
            self.assertTrue(keys.issubset(found), f"missing keys: {sorted(keys - found)}")
        for key in keys:
            self.assertEqual([content.count(f'name="{key}"') for content in contents], [1, 1, 1], key)


if __name__ == "__main__":
    unittest.main()
