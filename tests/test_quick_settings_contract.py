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
    def test_focus_keeps_white_text_and_uses_dark_halo_on_glass(self):
        focus_block = DIALOG[DIALOG.index("if (view instanceof TextView)"):DIALOG.index("\\n                }", DIALOG.index("if (view instanceof TextView)")) + 18]
        self.assertIn("label.setTextColor(PANEL_TEXT_PRIMARY);", focus_block)
        self.assertNotIn("0xFF17232B", focus_block)
        self.assertIn("0.8f * density", focus_block)
        self.assertIn("0x60000000", focus_block)
        self.assertIn("0.35f * density", focus_block)

    def test_wallpaper_heading_and_capsule_share_the_same_left_layout_lane(self):
        self.assertIn("wallpaperHeader.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);", DIALOG)
        self.assertIn("wallpaperHeader.setPaddingRelative(0, 0, 0, 0);", DIALOG)
        self.assertIn("wallpaperActions.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);", DIALOG)
        self.assertIn("wallpaperActions.setPaddingRelative(0, 0, 0, 0);", DIALOG)

    def test_dialog_is_single_frame_glass_and_anchored_to_capsule_and_dock(self):
        self.assertNotIn("ScrollView", DIALOG)
        self.assertIn("UniversalGlassMaterial.panel(activity, content, density, TvUiTokens.POPUP_RADIUS_DP,", DIALOG)
        self.assertIn("PANEL_FALLBACK_SURFACE, PANEL_FALLBACK_BORDER", DIALOG)
        self.assertNotIn("Backdrop.watch(content)", DIALOG)
        self.assertIn("panelTopInHost(anchorAt[1], settingsAnchor.getHeight(), hostAt[1], topClearancePx)", DIALOG)
        self.assertIn("availablePanelHeight(dockTop, top, bottomClearancePx)", DIALOG)
        self.assertIn("int width = dp(density, 245);", DIALOG)
        self.assertIn("private static final int PANEL_HEIGHT_DP = 330;", DIALOG)
        self.assertIn("int panelHeight = Math.min(fixedPanelHeight, availableHeight);", DIALOG)
        self.assertIn("int panelHeight = Math.min(fixedPanelHeight, availableHeight);", DIALOG)
        self.assertIn("if (naturalHeight > panelHeight)", DIALOG)
        self.assertNotIn("addVerticalFlexGap", DIALOG)
        self.assertNotIn("1f)", DIALOG)
        self.assertIn("int panelHeight = Math.min(fixedPanelHeight, availableHeight);", DIALOG)
        self.assertIn("addFixedVerticalGap(activity, content, CAPSULE_GAP_DP, \"quick_settings_gap_header_grid\")", DIALOG)
        self.assertIn("int panelHeight = Math.min(fixedPanelHeight, availableHeight);", DIALOG)
        self.assertIn("addFixedVerticalGap(activity, content, CAPSULE_GAP_DP, \"quick_settings_gap_wallpaper_to_settings\")", DIALOG)
        self.assertIn("private static final int PANEL_VERTICAL_INSET_DP = 10;", DIALOG)
        self.assertIn("private static int measureNaturalHeight(View content, int widthPx)", DIALOG)
        self.assertIn("grid.setColumnCount(3)", DIALOG)
        self.assertIn("grid.setRowCount(3)", DIALOG)
        self.assertIn("GridLayout.Spec columnSpec = GridLayout.spec(column, span);", DIALOG)
        self.assertNotIn("GridLayout.spec(column, span, 1f)", DIALOG)
        self.assertNotIn("quick_settings_close", DIALOG)
        self.assertNotIn("QuickSettingsFocusGraph.CLOSE", DIALOG)
        self.assertIn("dialog.setOnKeyListener", DIALOG)
        self.assertIn("KeyEvent.KEYCODE_BACK", DIALOG)
        self.assertIn("settingsActions.setOrientation(LinearLayout.VERTICAL)", DIALOG)
        self.assertIn("int width = dp(density, 245);", DIALOG)
        self.assertIn("private static final int HALF_TILE_WIDTH_DP = 110;", DIALOG)
        self.assertIn("private static final int WIDE_TILE_WIDTH_DP = 225;", DIALOG)
        self.assertIn("private static final int CAPSULE_GAP_DP = 5;", DIALOG)
        self.assertIn("private static final int PANEL_SIDE_INSET_DP = 10;", DIALOG)
        self.assertIn("addGridColumnGap(activity, grid, 0, density, tileHeightDp);", DIALOG)
        self.assertIn("addGridColumnGap(activity, grid, 1, density, tileHeightDp);", DIALOG)
        self.assertIn("private static final int TILE_HEIGHT_DP = 36;", DIALOG)
        self.assertIn("createLink(activity, R.string.quick_settings_manage_wallpaper, 20, density)", DIALOG)
        self.assertIn("dp(density, 64), dp(density, 20)", DIALOG)
        # Verify actual layout declarations rather than relying on a prose comment.
        self.assertIn("wallpaperTitle.setLayoutParams(new LinearLayout.LayoutParams(dp(density, 156)", DIALOG)
        self.assertIn("new LinearLayout.LayoutParams(dp(density, 5), 1)", DIALOG)
        self.assertIn("dp(density, 64), dp(density, 20)", DIALOG)
        self.assertIn("private static final int PANEL_SIDE_INSET_DP = 10;", DIALOG)
        self.assertIn("private static final int WIDE_TILE_WIDTH_DP = 225;", DIALOG)
        self.assertEqual(156 + 5 + 64, 225)
        self.assertIn("int tileHeightDp = TILE_HEIGHT_DP;", DIALOG)
        self.assertIn("static int panelTopInHost(", DIALOG)
        self.assertIn("static int availablePanelHeight(", DIALOG)
        self.assertIn("params.y = panelTop;", DIALOG)
        self.assertIn("final int desiredContentTopOnScreen = hostAt[1] + panelTop;", DIALOG)
        self.assertIn("PRE-relayout", DIALOG)
        self.assertIn("decorView.getViewTreeObserver().addOnPreDrawListener(positionListener[0]);", DIALOG)
        self.assertIn("int windowYCorrection = desiredContentTopOnScreen - contentAt[1];", DIALOG)
        self.assertIn("corrected.y += windowYCorrection;", DIALOG)
        self.assertIn('Dialog position (post-layout)', DIALOG)
        self.assertIn("dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));", DIALOG)
        self.assertIn("Remove the Material floating-dialog frame before DecorView is installed.", DIALOG)
        self.assertIn("internal custom-panel FrameLayout whose height is WRAP_CONTENT", DIALOG)
        self.assertIn("wrapperParams.height = panelHeight;", DIALOG)
        self.assertIn("content.setLayoutParams(new android.widget.FrameLayout.LayoutParams(panelWidth, panelHeight));", DIALOG)
        self.assertIn("content.setLayoutParams(new android.widget.FrameLayout.LayoutParams(panelWidth, panelHeight));", DIALOG)
        test_source = (ROOT / "launcher/app/src/test/java/com/gathin/tvhome/QuickSettingsDialogTest.java").read_text(encoding="utf-8")
        self.assertIn('Popup lower edge must remain exactly 5dp above the Dock', test_source)
        self.assertIn('content.getLocationOnScreen(contentAt);', test_source)
        self.assertIn('int actualBottomGap = dockAt[1] - (contentAt[1] + content.getMeasuredHeight());', test_source)
        self.assertIn('assertEquals("The 10.5dp surplus must move the popup down from its minimum top",', test_source)
        self.assertIn("QuickSettingsDialog.panelTopForBottomClearance(dockTop, content.getMeasuredHeight(), fiveDp)", test_source)
        self.assertIn("assertEquals(Math.round(330f * activity.getResources().getDisplayMetrics().density),", test_source)
        self.assertIn("lp.height = dp(density, tileHeightDp);", DIALOG)
        self.assertIn("params.x = finalPanelLeft;", DIALOG)
        self.assertIn("settingsIconCenterX(settingsAnchor, anchorAt[0], density)", DIALOG)
        self.assertIn("Gravity.TOP | Gravity.LEFT", DIALOG)
        self.assertNotIn("addDivider", DIALOG)
        self.assertIn("PANEL_TEXT_PRIMARY = TvUiTokens.TEXT_PRIMARY", DIALOG)
        self.assertIn("PANEL_TEXT_SECONDARY = TvUiTokens.TEXT_SECONDARY", DIALOG)
        self.assertIn("PANEL_FALLBACK_SURFACE = TvUiTokens.POPUP_SURFACE", DIALOG)
        self.assertIn("PANEL_FALLBACK_BORDER = TvUiTokens.BORDER", DIALOG)
        self.assertNotIn("0xB3FFFFFF", DIALOG)
        self.assertNotIn("0xEAF8FC", DIALOG)
        self.assertNotIn("gridLp.topMargin", DIALOG)
        self.assertNotIn("wallpaperHeaderLp.topMargin", DIALOG)
        self.assertNotIn("wallpaperActionsLp.topMargin", DIALOG)
        self.assertNotIn("settingsActionsLp.topMargin", DIALOG)
        self.assertIn("row < 2 ? dp(density, CAPSULE_GAP_DP) : 0", DIALOG)
        
        
        self.assertIn("R.drawable.ic_appearance", DIALOG)
        self.assertIn("R.drawable.ic_tv_display", DIALOG)
        self.assertIn("Character.OTHER_SYMBOL", DIALOG)
        self.assertIn("R.string.quick_settings_language_title", DIALOG)
        self.assertIn("tile.setMaxLines(summary == null || summary.isEmpty() ? 1 : 2);", DIALOG)
        self.assertIn("null, tileHeightDp, density);", DIALOG)
        self.assertIn("tile.setTextSize(13f);", DIALOG)
        self.assertIn("int size = dp(density, 16);", DIALOG)
        self.assertIn("title.setTextSize(15f);", DIALOG)
        self.assertIn("wallpaperTitle.setTextSize(12f);", DIALOG)
        self.assertIn("alignWallpaperHeadingToCapsule(wallpaperTitle, nextPhoto, density)", DIALOG)
        self.assertIn("capsuleAt[0] - paintedLeft + dp(density, 2)", DIALOG)
        self.assertIn("link.setTextSize(12f);", DIALOG)
        self.assertIn("private static void addGridColumnGap(Activity activity, GridLayout grid, int row, float density, int tileHeightDp)", DIALOG)
        self.assertNotIn("scaleVertical(", DIALOG)
        self.assertIn("new GliderFocusDrawable(control, glider, cornerRadiusDp * density)", DIALOG)
        self.assertIn("glider.moveTo(left, top,", DIALOG)
        self.assertIn("glider.setStyle(Glider.GLASS)", DIALOG)
        self.assertIn("glider.setCompanionOnly(true)", DIALOG)
        self.assertIn("glider.setSingleGlassRim(true)", DIALOG)
        self.assertIn("glider.setGlassRimStrokeWidth(2.0f * density)", DIALOG)
        self.assertIn("moveDialogGliderTo(target, dialogGlider, density, radiusDp)", DIALOG)
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
        input_service = (JAVA / "InputKeyService.java").read_text(encoding="utf-8")
        config = (JAVA / "Config.java").read_text(encoding="utf-8")
        self.assertIn("isInputKeyCode(event.getKeyCode(), inputKeycodes)", input_service)
        self.assertIn("MainActivity.EXTRA_OPEN_INPUTS", input_service)
        self.assertIn("inputKeyKeycodes", config)
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
        self.assertIn("static int popupWidthDp() { return 190; }", wifi_dialog)
        self.assertIn("static float networkTextSizeSp() { return 12f; }", wifi_dialog)
        self.assertIn("TypedValue.COMPLEX_UNIT_SP, networkTextSizeSp()", wifi_dialog)
        self.assertIn("TextUtils.TruncateAt.MARQUEE", wifi_dialog)
        self.assertIn("row.setOnFocusChangeListener", wifi_dialog)
        self.assertIn("new ConnectedCheckDrawable(activity, networkStatusIconColor(), networkStatusIconSizeDp())", wifi_dialog)
        self.assertIn("static int networkStatusIconSizeDp() { return 16; }", wifi_dialog)
        self.assertIn("static int networkStatusIconColor()", wifi_dialog)
        self.assertIn("static int visibleNetworkRowCount() { return 3; }", wifi_dialog)
        self.assertIn("static int networkRowHeightDp() { return 28; }", wifi_dialog)
        self.assertIn("static int networkViewportHeightDp()", wifi_dialog)
        self.assertIn("static int networkRowGapDp() { return 4; }", wifi_dialog)
        self.assertIn("static float popupTitleSizeSp() { return 14f; }", wifi_dialog)
        self.assertIn("static float popupSectionSizeSp() { return 12f; }", wifi_dialog)
        self.assertIn("static float popupMetaSizeSp() { return 10f; }", wifi_dialog)
        self.assertIn("static float popupFooterSizeSp() { return 12f; }", wifi_dialog)
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
        self.assertIn("dp(density, networkViewportHeightDp())", wifi_dialog)
        self.assertIn("actions.setOrientation(LinearLayout.VERTICAL);", wifi_dialog)
        self.assertIn("dp(density, 28)", wifi_dialog)
        self.assertIn("wifiToggle.setMinHeight(dp(density, 23));", wifi_dialog)
        self.assertIn("wifiToggle.setGravity(Gravity.CENTER);", wifi_dialog)
        self.assertIn("new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(density, 30))", wifi_dialog)
        self.assertIn("wifiToggle.setTextSize(14f);", wifi_dialog)
        self.assertIn("wifiToggle.setTranslationY(dp(density, 5));", wifi_dialog)
        self.assertIn("title.setTranslationY(dp(density, 5));", wifi_dialog)
        self.assertIn("networkViewport.topMargin = dp(density, 10);", wifi_dialog)
        self.assertIn("networkViewport.bottomMargin = dp(density, 10);", wifi_dialog)
        self.assertIn("ViewGroup.LayoutParams.MATCH_PARENT, dp(density, 34)", wifi_dialog)
        self.assertIn("rescan.setMinHeight(dp(density, 28));", wifi_dialog)
        self.assertIn("androidSettings.setMinHeight(dp(density, 28));", wifi_dialog)
        self.assertIn("scanState = text(R.string.wifi_scan_idle, popupMetaSizeSp()", wifi_dialog)
        self.assertIn("view.setMaxLines(1);", wifi_dialog)
        self.assertNotIn('label = "⌁  " + label', wifi_dialog)
        for resource in ("values/strings.xml", "values-id/strings.xml"):
            content = (RES / resource).read_text(encoding="utf-8")
            self.assertIn('name="wifi_open_android_settings">Pengaturan Wi-Fi &gt;', content)

    def test_quick_settings_waits_for_safe_horizontal_dock_collapse(self):
        opener = MAIN[MAIN.index("private void showQuickControlPopup(int initialFocus)"):
                      MAIN.index("private void showQuickSettingsDialog(int initialFocus)")]
        self.assertIn("if (!dockVertical && horizontalGridExpanded)", opener)
        self.assertIn("pendingQuickSettingsOpen = () -> showQuickSettingsDialog(initialFocus);", opener)
        self.assertIn("collapseHorizontalOverflowToDock(dockPosition);", opener)
        collapse = MAIN[MAIN.index("private void startHorizontalDockCollapse("):
                        MAIN.index("private void revealHomeChromeAfterDockCollapse(")]
        self.assertIn("Runnable deferredQuickSettings = pendingQuickSettingsOpen;", collapse)
        self.assertIn("dockRecycler.post(deferredQuickSettings);", collapse)
        abort = MAIN[MAIN.index("private void abortHorizontalDockCollapse("):
                     MAIN.index("private int getHorizontalDockPitch(")]
        self.assertIn("pendingQuickSettingsOpen = null;", abort)

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
            "quick_settings_restart_title",
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
