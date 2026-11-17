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
    def test_dialog_is_single_frame_glass_and_anchored_to_capsule_and_dock(self):
        self.assertNotIn("ScrollView", DIALOG)
        self.assertIn("new GlassDrawable(content, TvUiTokens.POPUP_RADIUS_DP * density, density, null, 0.5f, false,", DIALOG)
        self.assertIn("PANEL_GLASS_VEIL, 0x48, PANEL_FALLBACK_SURFACE, PANEL_FALLBACK_BORDER", DIALOG)
        self.assertIn("Backdrop.watch(content)", DIALOG)
        self.assertIn("settingsAnchor.getHeight() - hostAt[1] + dp(density, 5)", DIALOG)
        self.assertIn("dockTop - dp(density, 8) - top", DIALOG)
        self.assertIn("grid.setColumnCount(2)", DIALOG)
        self.assertIn("grid.setRowCount(3)", DIALOG)
        self.assertNotIn("quick_settings_close", DIALOG)
        self.assertNotIn("QuickSettingsFocusGraph.CLOSE", DIALOG)
        self.assertIn("dialog.setOnKeyListener", DIALOG)
        self.assertIn("KeyEvent.KEYCODE_BACK", DIALOG)
        self.assertIn("settingsActions.setOrientation(LinearLayout.VERTICAL)", DIALOG)
        self.assertIn("hostWidth * 0.29f", DIALOG)
        self.assertIn("private static final int TILE_HEIGHT_DP = 32;", DIALOG)
        self.assertIn("int tileHeightDp = TILE_HEIGHT_DP;", DIALOG)
        self.assertIn("lp.height = dp(density, tileHeightDp);", DIALOG)
        self.assertIn("params.x = finalPanelLeft;", DIALOG)
        self.assertIn("settingsIconCenterX(settingsAnchor, anchorAt[0], density)", DIALOG)
        self.assertIn("Gravity.TOP | Gravity.LEFT", DIALOG)
        self.assertNotIn("addDivider", DIALOG)
        self.assertIn("PANEL_TEXT_PRIMARY = TvUiTokens.TEXT_PRIMARY", DIALOG)
        self.assertIn("PANEL_TEXT_SECONDARY = TvUiTokens.TEXT_SECONDARY", DIALOG)
        self.assertIn("PANEL_GLASS_VEIL = TvUiTokens.GLASS_VEIL_COLOR", DIALOG)
        self.assertIn("PANEL_FALLBACK_SURFACE = TvUiTokens.POPUP_SURFACE", DIALOG)
        self.assertIn("PANEL_FALLBACK_BORDER = TvUiTokens.BORDER", DIALOG)
        self.assertNotIn("0xB3FFFFFF", DIALOG)
        self.assertNotIn("0xEAF8FC", DIALOG)
        self.assertIn("gridLp.topMargin = dp(density, 4);", DIALOG)
        self.assertIn("wallpaperHeaderLp.topMargin = dp(density, 6);", DIALOG)
        self.assertIn("settingsActionsLp.topMargin = dp(density, 5);", DIALOG)
        
        
        self.assertIn("R.drawable.ic_appearance", DIALOG)
        self.assertIn("R.drawable.ic_tv_display", DIALOG)
        self.assertIn("Character.OTHER_SYMBOL", DIALOG)
        self.assertIn("R.string.quick_settings_language_title", DIALOG)
        self.assertIn("tile.setMaxLines(summary == null || summary.isEmpty() ? 1 : 2);", DIALOG)
        self.assertIn("null, tileHeightDp, density);", DIALOG)
        self.assertIn("tile.setTextSize(12f);", DIALOG)
        self.assertIn("int size = dp(density, 16);", DIALOG)
        self.assertNotIn("scaleVertical(", DIALOG)
        self.assertIn("TvFocusIndicator.applyRoundedTextFocus", DIALOG)
        self.assertIn("CAPSULE_IDLE_FILL, CAPSULE_FOCUS_FILL", DIALOG)
        self.assertIn("CAPSULE_IDLE_BORDER, CAPSULE_FOCUS_BORDER", DIALOG)
        self.assertIn("setCanceledOnTouchOutside(false)", DIALOG)
        self.assertIn("dialog.setOnDismissListener", DIALOG)
        self.assertIn("dialog.setCancelable(true)", DIALOG)
        self.assertIn("params.dimAmount = 0f", DIALOG)

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

    def test_quick_settings_input_routes_to_existing_tv_source_selector(self):
        self.assertIn("R.drawable.ic_input, R.string.quick_settings_input_title", DIALOG)
        self.assertIn('setTag("quick_settings_input")', DIALOG)
        self.assertIn("actions::openInputs", DIALOG)
        self.assertIn("@Override public void openInputs() {", MAIN)
        self.assertIn("showInputs();", MAIN)
        self.assertIn("private void showInputs()", MAIN)
        self.assertIn("TvInputManager tim = (TvInputManager) getSystemService(TV_INPUT_SERVICE);", MAIN)
        self.assertIn("TvContract.buildChannelUriForPassthroughInput(inputId)", MAIN)
        self.assertIn("private void openInput(String inputId)", MAIN)
        self.assertIn("private void openSourcePanel()", MAIN)
        self.assertIn("QuickSettingsFocusGraph.INPUT", DIALOG)
        self.assertIn("getString(R.string.input_external_tv_title)", MAIN)
        self.assertIn("R.string.input_external_tv_hint", MAIN)
        self.assertIn("KeyEvent.KEYCODE_TV_INPUT", MAIN)
        self.assertIn("KeyEvent.KEYCODE_TV_INPUT", DIALOG)
        self.assertIn("actions.openInputs();", DIALOG)

    def test_wifi_child_surface_still_restores_its_parent_focus(self):
        self.assertIn("WifiSettingsDialog.show(this", MAIN)
        self.assertIn("returnToParent", MAIN)
        self.assertIn("returnToParent.run()", (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8"))
        wifi_dialog = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("applyAnchoredPopup();", wifi_dialog)
        self.assertIn("params.gravity = anchoredWindowGravity();", wifi_dialog)
        self.assertIn("return Gravity.TOP | Gravity.RIGHT;", wifi_dialog)
        self.assertIn("params.dimAmount = popupDimAmount();", wifi_dialog)
        self.assertIn("return 0.08f;", wifi_dialog)

    def test_wifi_popup_refinement_preserves_compact_width_and_balanced_type(self):
        wifi_dialog = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("return 0.225f;", wifi_dialog)
        self.assertIn("return 0.69f;", wifi_dialog)
        self.assertIn("static float networkTextSizePx() { return 25f; }", wifi_dialog)
        self.assertIn("TypedValue.COMPLEX_UNIT_PX, networkTextSizePx()", wifi_dialog)
        self.assertIn("TextUtils.TruncateAt.MARQUEE", wifi_dialog)
        self.assertIn("row.setOnFocusChangeListener", wifi_dialog)
        self.assertIn("new ConnectedCheckDrawable(activity, networkStatusIconColor(), networkStatusIconSizeDp())", wifi_dialog)
        self.assertIn("static int networkStatusIconSizeDp() { return 22; }", wifi_dialog)
        self.assertIn("static int networkStatusIconColor()", wifi_dialog)
        self.assertIn("static int visibleNetworkRowCount() { return 4; }", wifi_dialog)
        self.assertIn("static int networkRowHeightDp() { return 35; }", wifi_dialog)
        self.assertIn("static int popupHeightReductionDp() { return 35; }", wifi_dialog)
        self.assertIn("static int networkRowGapDp() { return 4; }", wifi_dialog)
        self.assertIn("static float popupTitleSizeSp() { return 17f; }", wifi_dialog)
        self.assertIn("static float popupSectionSizeSp() { return 13f; }", wifi_dialog)
        self.assertIn("static float popupMetaSizeSp() { return 11f; }", wifi_dialog)
        self.assertIn("static float popupFooterSizeSp() { return 13f; }", wifi_dialog)
        self.assertIn('connectedRow.setMaxLines(1);', wifi_dialog)
        self.assertIn('TextView connectedRow = action(connectedSsid,', wifi_dialog)
        self.assertNotIn('connectedLabel', wifi_dialog)
        self.assertIn('row.setMaxLines(1);', wifi_dialog)
        self.assertIn("networkRowLayoutParams(", wifi_dialog)
        self.assertIn("networksHeading.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);", wifi_dialog)
        self.assertIn("scanState.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);", wifi_dialog)
        self.assertIn('content.setTag("wifi_popup_root");', wifi_dialog)
        self.assertIn('header.setTag("wifi_popup_header");', wifi_dialog)
        self.assertIn('actions.setTag("wifi_popup_footer");', wifi_dialog)
        self.assertIn("new LinearLayout.LayoutParams(\n                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)", wifi_dialog)
        self.assertIn("actions.setOrientation(LinearLayout.VERTICAL);", wifi_dialog)
        self.assertIn("ViewGroup.LayoutParams.MATCH_PARENT, dp(density, 35)", wifi_dialog)
        self.assertIn("wifiToggle.setMinHeight(dp(density, 35));", wifi_dialog)
        self.assertIn("wifiToggle.setGravity(Gravity.CENTER);", wifi_dialog)
        self.assertIn("rescan.setMinHeight(dp(density, 35));", wifi_dialog)
        self.assertIn("androidSettings.setMinHeight(dp(density, 35));", wifi_dialog)
        self.assertIn("scanState = text(R.string.wifi_scan_idle, popupMetaSizeSp()", wifi_dialog)
        self.assertIn("view.setMaxLines(1);", wifi_dialog)
        self.assertNotIn('label = "⌁  " + label', wifi_dialog)
        for resource in ("values/strings.xml", "values-id/strings.xml"):
            content = (RES / resource).read_text(encoding="utf-8")
            self.assertIn('name="wifi_open_android_settings">Pengaturan Wi-Fi &gt;', content)

    def test_two_column_focus_graph_has_explicit_dpad_edges(self):
        for setter in (
            "setNextFocusLeftId",
            "setNextFocusRightId",
            "setNextFocusUpId",
            "setNextFocusDownId",
        ):
            self.assertIn(setter, GRAPH)
        self.assertIn("QuickSettingsFocusGraph.connect(controls)", DIALOG)
        self.assertIn("QuickSettingsDialog.show(this, root, anchor, dockRecycler", MAIN)
        cache_graph = GRAPH[GRAPH.index("case CACHE:"):GRAPH.index("case WALLPAPER_MANAGE:")]
        photo_graph = GRAPH[GRAPH.index("case NEXT_PHOTO:"):GRAPH.index("case INTERVAL:")]
        interval_graph = GRAPH[GRAPH.index("case INTERVAL:"):GRAPH.index("case GLASS_SETTINGS:")]
        self.assertNotIn("WALLPAPER_MANAGE", cache_graph)
        self.assertNotIn("WALLPAPER_MANAGE", photo_graph)
        self.assertIn("if (direction == UP) return WALLPAPER_MANAGE;", interval_graph)

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
            "quick_settings_language_title",
            "quick_settings_wifi_summary",
            "quick_settings_input_title",
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
            "input_external_tv_title",
            "input_external_tv_hint",
        }
        contents = [p.read_text(encoding="utf-8") for p in files]
        for content in contents:
            found = set(re.findall(r'<string name="([^"]+)"', content))
            self.assertTrue(keys.issubset(found), f"missing keys: {sorted(keys - found)}")
        for key in keys:
            self.assertEqual([content.count(f'name="{key}"') for content in contents], [1, 1, 1], key)
        self.assertIn('<string name="quick_settings_title">Pengaturan</string>', contents[1])
        self.assertIn('<string name="quick_settings_title">Quick Settings</string>', contents[2])
        self.assertIn('<string name="quick_settings_android_settings">Pengaturan Android TV</string>', contents[1])
        self.assertIn('<string name="quick_settings_android_settings">Android TV Settings</string>', contents[2])
        self.assertIn('<string name="quick_settings_language_title">Bahasa</string>', contents[1])
        self.assertIn('<string name="quick_settings_language_title">Language</string>', contents[2])
        self.assertIn('<string name="quick_settings_input_title">Input</string>', contents[1])
        self.assertIn('<string name="quick_settings_input_title">Input</string>', contents[2])


if __name__ == "__main__":
    unittest.main()
