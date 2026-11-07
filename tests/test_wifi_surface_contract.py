"""Source contracts for the V05 Wi-Fi status and Android-owned operation boundary."""
from pathlib import Path
import re
import unittest

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher/app/src/main/java/com/gathin/tvhome"
RES = ROOT / "launcher/app/src/main/res"
DIALOG = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
POLICY = (JAVA / "WifiScanPolicy.java").read_text(encoding="utf-8")
MAIN = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
MANIFEST = (ROOT / "launcher/app/src/main/AndroidManifest.xml").read_text(encoding="utf-8")


class WifiSurfaceContractTest(unittest.TestCase):
    def test_android_owns_radio_authentication_and_connection_operations(self):
        for forbidden in ("setWifiEnabled(", "disconnect()", "enableNetwork(", "addNetwork("):
            self.assertNotIn(forbidden, DIALOG)
            self.assertNotIn(forbidden, MAIN)
        self.assertIn("Settings.ACTION_WIFI_SETTINGS", MAIN)

    def test_scan_requires_location_permission_location_service_and_wifi(self):
        self.assertIn("ACCESS_FINE_LOCATION", DIALOG)
        self.assertIn("isLocationEnabled()", DIALOG)
        self.assertIn("WifiScanPolicy", POLICY)
        self.assertIn("WifiScanPolicy.mayRequestScan", DIALOG)
        self.assertIn("manager.startScan()", DIALOG)
        self.assertIn("manager.getScanResults()", DIALOG)
        self.assertIn("BlockReason.PERMISSION", POLICY)

    def test_scan_receiver_and_timeout_are_lifecycle_scoped(self):
        self.assertIn("registerReceiver(scanReceiver", DIALOG)
        self.assertIn("unregisterReceiver(scanReceiver)", DIALOG)
        self.assertNotIn("ConnectivityManager.NetworkCallback", DIALOG)
        self.assertIn("WifiSettingsDialog.onNetworkStateChanged();", MAIN)
        self.assertIn("handler.removeCallbacksAndMessages(null)", DIALOG)
        self.assertIn("SCAN_TIMEOUT_MS", DIALOG)

    def test_network_selection_hands_off_to_android_settings(self):
        self.assertIn("() -> dismissThen(openSystemWifiSettings)", DIALOG)
        self.assertIn("Settings.ACTION_WIFI_SETTINGS", MAIN)
        self.assertIn("tryOpen(systemIntent(Settings.ACTION_WIFI_SETTINGS))", MAIN)
        self.assertIn("onExternalSettingsUnavailable()", MAIN)
        self.assertIn("deferParentReturn = action != null", DIALOG)
        self.assertIn("pendingReturn = this", DIALOG)
        self.assertIn("waiting.notifyReturnToParent()", DIALOG)

    def test_focus_graph_and_back_dismissal_are_explicit(self):
        for edge in ("setNextFocusUpId", "setNextFocusDownId",
                     "setNextFocusLeftId", "setNextFocusRightId"):
            self.assertIn(edge, DIALOG)
        self.assertIn("dialog.setCancelable(true)", DIALOG)
        self.assertIn("dialog.setOnDismissListener", DIALOG)

    def test_no_new_privileged_wifi_permissions_or_dependencies(self):
        self.assertIn("android.permission.CHANGE_WIFI_STATE", MANIFEST)
        self.assertNotIn("android.permission.WRITE_SETTINGS", MANIFEST)
        self.assertIn("android.permission.ACCESS_WIFI_STATE", MANIFEST)
        self.assertIn("android.permission.ACCESS_FINE_LOCATION", MANIFEST)

    def test_localized_wifi_resources_have_parity(self):
        files = [RES / "values/strings.xml", RES / "values-id/strings.xml",
                 RES / "values-en/strings.xml"]
        keys = {
            "wifi_status_loading", "wifi_status_not_connected", "wifi_status_off",
            "wifi_status_connected", "wifi_status_connected_to", "wifi_available_networks",
            "wifi_scan_idle", "wifi_scan_loading", "wifi_scan_updated", "wifi_scan_limited",
            "wifi_scan_timeout", "wifi_scan_unavailable", "wifi_scan_empty",
            "wifi_scan_permission_required", "wifi_scan_location_required",
            "wifi_scan_radio_off", "wifi_grant_location_permission", "wifi_enable_location",
            "wifi_rescan", "wifi_open_android_settings", "wifi_network_row",
            "wifi_security_wpa3", "wifi_security_wep", "wifi_security_secured",
            "wifi_security_open", "wifi_signal_bars"
        }
        contents = [p.read_text(encoding="utf-8") for p in files]
        for content in contents:
            found = set(re.findall(r'<string name="([^"]+)"', content))
            self.assertTrue(keys.issubset(found), f"missing keys: {sorted(keys - found)}")
        for key in keys:
            self.assertEqual([content.count(f'name="{key}"') for content in contents],
                             [1, 1, 1], key)


if __name__ == "__main__":
    unittest.main()
