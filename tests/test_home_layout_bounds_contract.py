"""Regression contract for Home controls sharing a balanced safe frame."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET


ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
LAYOUT = ROOT / "launcher/app/src/main/res/layout/activity_main.xml"
GEOMETRY = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java").read_text(encoding="utf-8")
NOW_PLAYING = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/NowPlayingManager.java").read_text(encoding="utf-8")
ANDROID = "{http://schemas.android.com/apk/res/android}"


class HomeLayoutBoundsContractTest(unittest.TestCase):
    def test_home_content_uses_30dp_horizontal_and_bottom_29dp_top_safe_margins(self):
        root = ET.parse(LAYOUT).getroot()
        main_content = next((node for node in root.iter()
                             if node.get(ANDROID + "id") == "@+id/mainContent"), None)
        self.assertIsNotNone(main_content)
        self.assertEqual(main_content.get(ANDROID + "paddingStart"), "30dp")
        self.assertEqual(main_content.get(ANDROID + "paddingEnd"), "30dp")
        self.assertEqual(main_content.get(ANDROID + "paddingTop"), "29dp")

        self.assertIn("SAFE_HORIZONTAL_MARGIN_DP = 30f", GEOMETRY)
        self.assertIn("SAFE_VERTICAL_MARGIN_DP = 30f", GEOMETRY)
        self.assertIn("SAFE_TOP_MARGIN_DP = 29f", GEOMETRY)
        self.assertIn("safeBottomInsetPx", GEOMETRY)

    def test_right_prayer_shelf_uses_the_same_top_and_right_safe_inset(self):
        start = MAIN.index("private void applyHomeShelfSafeMargins()")
        end = MAIN.index("private void applyDockLayout()", start)
        shelf = MAIN[start:end]
        self.assertIn("params.setMarginEnd(inset)", shelf)
        self.assertIn("params.topMargin = DockGeometry.safeTopInsetPx(density)", shelf)
        self.assertIn("params.setMarginStart(inset)", shelf)

    def test_bottom_dock_tray_uses_the_same_safe_frame_as_top_and_sides(self):
        start = MAIN.index("private void applyDockLayout()")
        end = MAIN.index("private void updateHomeTabIndicator()", start)
        dock_layout = MAIN[start:end]
        self.assertIn(
            "dockParams.bottomMargin = DockGeometry.horizontalDockBottomMarginPx(dockTileHeight, density)",
            dock_layout,
        )
        self.assertIn("return safeBottomInsetPx(density) - trayBottomInset", GEOMETRY)
        self.assertIn("dockParams.bottomMargin = DockGeometry.safeBottomInsetPx(density)", dock_layout)

    def test_legacy_home_weather_refresh_cannot_overwrite_shared_weather_aqi_text(self):
        start = MAIN.index("private void showHomeWeather(HomeWeather.Line line)")
        end = MAIN.index("public void ensureWeatherOverlayView()", start)
        method = MAIN[start:end]
        self.assertIn("updateWeatherAqi()", method)
        self.assertIn("updateWeatherOverlay(line)", method)
        self.assertNotIn("line.aqi", method)
        self.assertNotIn("weatherAirQualityView.setText", method)

    def test_header_navigation_keeps_the_owner_approved_order(self):
        root = ET.parse(LAYOUT).getroot()
        nav = next((node for node in root.iter()
                    if node.get(ANDROID + "id") == "@+id/navPill"), None)
        self.assertIsNotNone(nav)
        children = [node.get(ANDROID + "id") for node in list(nav)]
        self.assertEqual(children[:3], [
            "@+id/btnHome", "@+id/btnCandyBox", "@+id/btnSettingsContainer"
        ])

    def test_right_controls_remain_right_aligned_and_outside_center_tabs(self):
        root = ET.parse(LAYOUT).getroot()
        top_buttons = next((node for node in root.iter()
                            if node.get(ANDROID + "id") == "@+id/topButtons"), None)
        self.assertIsNotNone(top_buttons)
        self.assertEqual(top_buttons.get(ANDROID + "layout_alignParentEnd"), "true")
        ids = {node.get(ANDROID + "id") for node in top_buttons.iter()}
        for view_id in ("btnInputs", "btnWifi", "btnAssistant"):
            self.assertIn(f"@+id/{view_id}", ids)
        self.assertNotIn("@+id/btnCandyBox", ids)
        self.assertNotIn("@+id/btnSettingsContainer", ids)

    def test_header_labels_truncate_inside_their_own_slots(self):
        root = ET.parse(LAYOUT).getroot()
        expected = ("locationName", "locationAltitude", "weatherAirQuality",
                    "btnHome", "btnCandyBox", "btnSettings")
        for view_id in expected:
            node = next((item for item in root.iter()
                         if item.get(ANDROID + "id") == f"@+id/{view_id}"), None)
            self.assertIsNotNone(node, view_id)
            self.assertEqual(node.get(ANDROID + "singleLine"), "true", view_id)
            self.assertEqual(node.get(ANDROID + "ellipsize"), "end", view_id)

    def test_center_navigation_is_sized_from_measured_side_controls(self):
        self.assertIn("HomeHeaderGeometry.centeredWidthPx", MAIN)
        self.assertIn("date.getRight()", MAIN)
        self.assertIn("right.getLeft()", MAIN)

    def test_header_controls_keep_the_established_liquid_glass_surface(self):
        start = MAIN.index("// Preserve the established GlassDrawable/Backdrop treatment")
        end = MAIN.index("// Hide the glass when focus leaves the tiles.", start)
        header = MAIN[start:end]
        for view_id in ("locationPill", "weatherAirQuality", "btnInputs", "btnWifi",
                        "btnAssistant", "btnHome", "btnCandyBox", "btnSettings"):
            self.assertIn(f"R.id.{view_id}", header)
        self.assertIn("new GlassDrawable(pill", header)
        self.assertIn("pill.setForeground(getDrawable(R.drawable.header_nav_tab_bg))", header)

    def test_deferred_grid_focus_is_cancelled_by_newer_focus_destinations(self):
        dock_start = MAIN.index("public void onDockTileFocused(String key, View card)")
        dock_end = MAIN.index("private void handleTileFocused", dock_start)
        dock_focus = MAIN[dock_start:dock_end]
        self.assertIn("gridFocusRequestGeneration++;", dock_focus)

        header_start = MAIN.index("pill.setOnFocusChangeListener((v, has) -> {")
        header_end = MAIN.index("if (prayerTimesView != null)", header_start)
        header_focus = MAIN[header_start:header_end]
        self.assertIn("gridFocusRequestGeneration++;", header_focus)

        pause_start = MAIN.index("protected void onPause()")
        pause_end = MAIN.index("public void onWindowFocusChanged", pause_start)
        lifecycle = MAIN[pause_start:pause_end]
        self.assertIn("gridFocusRequestGeneration++;", lifecycle)

        focus_start = MAIN.index("private void focusTile(int position)")
        focus_end = MAIN.index("private void alignHorizontalGridRowToGlass", focus_start)
        focus = MAIN[focus_start:focus_end]
        self.assertIn("if (requestGeneration != gridFocusRequestGeneration) return;", focus)
        self.assertIn("grid.post(() -> {", focus)

    def test_resume_focus_restore_does_not_steal_a_newer_user_focus_target(self):
        start = MAIN.index("private void restoreFocus()")
        end = MAIN.index("protected void onPause()", start)
        restore = MAIN[start:end]
        self.assertIn("final int restoreGeneration = gridFocusRequestGeneration;", restore)
        self.assertIn("restoreGeneration != gridFocusRequestGeneration", restore)
        self.assertIn("if (!supersededByNewFocus)", restore)
        # Lifecycle completion must still happen when the stale restore is skipped.
        self.assertIn("tilesReady = true;", restore)
        self.assertIn("resuming = false;", restore)
        global_focus_start = MAIN.index("addOnGlobalFocusChangeListener((oldF, newF) -> {")
        global_focus_end = MAIN.index("packageFilter.addAction", global_focus_start)
        global_focus = MAIN[global_focus_start:global_focus_end]
        self.assertIn("if (newF != null && !onTile) gridFocusRequestGeneration++;", global_focus)

    def test_horizontal_grid_focus_restore_reapplies_glass_for_already_focused_card(self):
        start = MAIN.index("private void focusTileWhenLaidOut")
        end = MAIN.index("// ---- top bar", start)
        focus_restore = MAIN[start:end]
        self.assertIn("if (card.isFocused()) onTileFocused(position, card);", focus_restore)
        self.assertIn("else card.requestFocus();", focus_restore)

        dock_start = MAIN.index("private void focusDockTileWhenLaidOut")
        dock_end = MAIN.index("private void focusTile(int position)", dock_start)
        dock_restore = MAIN[dock_start:dock_end]
        self.assertIn(
            "if (card.isFocused()) onDockTileFocused(dockAdapter.keyAt(position), card);",
            dock_restore,
        )

    def test_location_altitude_uses_bilingual_resources_without_approximation_symbol(self):
        self.assertIn(
            "getString(R.string.location_altitude_format, zone.altitudeMeters)",
            MAIN,
        )
        self.assertNotIn('altitude.setText("± "', MAIN)
        resource_paths = (
            ROOT / "launcher/app/src/main/res/values/strings.xml",
            ROOT / "launcher/app/src/main/res/values-id/strings.xml",
            ROOT / "launcher/app/src/main/res/values-en/strings.xml",
        )
        expected = ("%1$d mdpl", "%1$d mdpl", "%1$d m ASL")
        for path, value in zip(resource_paths, expected):
            resources = ET.parse(path).getroot()
            entry = next((item for item in resources
                          if item.get("name") == "location_altitude_format"), None)
            self.assertIsNotNone(entry, str(path))
            self.assertEqual(entry.text, value, str(path))

    def test_top_controls_are_not_repositioned_from_dock_item_geometry(self):
        self.assertNotIn("alignTopButtons", MAIN)
        self.assertNotIn("topRow.setTranslationX", MAIN)
        self.assertNotIn("root.offsetDescendantRectToMyCoords(topSettings", MAIN)

    def test_home_dock_and_grid_still_share_the_existing_geometry_owner(self):
        self.assertIn("horizontalGridViewportHeightPx", GEOMETRY)
        self.assertIn("horizontalTileWidthPx", GEOMETRY)
        self.assertIn("horizontalGridWidthPx", GEOMETRY)


    def test_clock_and_date_hierarchy_uses_compact_readable_sizes(self):
        root = ET.parse(LAYOUT).getroot()
        expected = {"clock": "44sp", "ampm": "11sp", "date": "11sp",
                    "shamsiDate": "11sp"}
        for view_id, size in expected.items():
            node = next((item for item in root.iter()
                         if item.get(ANDROID + "id") == f"@+id/{view_id}"), None)
            self.assertIsNotNone(node, view_id)
            self.assertEqual(node.get(ANDROID + "textSize"), size, view_id)

    def test_navigation_is_one_glass_capsule_with_independent_transparent_tabs(self):
        root = ET.parse(LAYOUT).getroot()
        nav = next((node for node in root.iter()
                    if node.get(ANDROID + "id") == "@+id/navPill"), None)
        self.assertIsNotNone(nav)
        self.assertEqual(nav.get(ANDROID + "background"), "@drawable/header_glass_pill_bg")
        self.assertIn("navCapsule.setBackground(new GlassDrawable(navCapsule", MAIN)
        self.assertIn("pill.setBackgroundColor(android.graphics.Color.TRANSPARENT)", MAIN)
        self.assertIn("pill.setForeground(getDrawable(R.drawable.header_nav_tab_bg))", MAIN)
        self.assertIn("Keep each tab's measured segment geometry stable", MAIN)


    def test_home_atmospheric_weather_overlay_refreshes_on_resume_when_due(self):
        start = MAIN.index("protected void onResume()")
        end = MAIN.index("protected void onPause()", start)
        resume = MAIN[start:end]
        self.assertIn("updateWeatherAqi();", resume)
        self.assertIn("WeatherAirQuality.syncIfDue(this, this::updateWeatherAqi);", resume)
        self.assertIn("updateHomeWeather();", resume)
        start = MAIN.index("private void showHomeWeather(HomeWeather.Line line)")
        end = MAIN.index("public void ensureWeatherOverlayView()", start)
        display = MAIN[start:end]
        self.assertIn("updateWeatherAqi();", display)
        self.assertIn("updateWeatherOverlay(line);", display)
        self.assertNotIn("weatherAirQualityView.setText", display)

    def test_weather_pill_focus_scale_and_header_highlight_share_one_listener(self):
        self.assertNotIn("weatherAirQualityView.setOnFocusChangeListener", MAIN)
        start = MAIN.index("for (int id : new int[]{R.id.locationPill, R.id.weatherAirQuality")
        end = MAIN.index("if (prayerTimesView != null)", start)
        header = MAIN[start:end]
        self.assertIn("pill.setOnFocusChangeListener((v, has) -> {", header)
        self.assertIn("if (id == R.id.weatherAirQuality)", header)
        self.assertIn("TvUiTokens.CARD_FOCUS_SCALE", header)
        self.assertIn("TvUiTokens.FOCUS_ANIMATION_MS", header)
        self.assertIn("moveTopHighlight(v);", header)

    def test_deferred_dock_focus_retries_are_cancelled_by_newer_focus_requests(self):
        start = MAIN.index("private void focusDockTile(int position)")
        end = MAIN.index("private void focusTile(int position)", start)
        dock_focus = MAIN[start:end]
        self.assertIn("final int requestGeneration = ++gridFocusRequestGeneration;", dock_focus)
        self.assertIn("focusDockTileWhenLaidOut(targetPosition, 3, requestGeneration);", dock_focus)
        self.assertIn(
            "private void focusDockTileWhenLaidOut(int position, int attemptsRemaining, int requestGeneration)",
            dock_focus,
        )
        self.assertIn("if (requestGeneration != gridFocusRequestGeneration) return;", dock_focus)
        self.assertIn(
            "focusDockTileWhenLaidOut(position, attemptsRemaining - 1, requestGeneration)",
            dock_focus,
        )

    def test_visible_prayer_timetable_opens_popup_and_supports_dpad_activation(self):
        start = MAIN.index("View prayerTableView = findViewById(R.id.prayerTable);")
        end = MAIN.index("updatePrayerTimes();", start)
        binding = MAIN[start:end]
        self.assertIn("prayerTableView.setFocusable(true);", binding)
        self.assertIn("prayerTableView.setClickable(true);", binding)
        self.assertIn("prayerTableView.setOnClickListener(v -> showPrayerPopup());", binding)
        self.assertIn("KEYCODE_DPAD_CENTER", binding)
        self.assertIn("KEYCODE_ENTER", binding)
        self.assertIn("KEYCODE_DPAD_DOWN", binding)
        self.assertIn("KEYCODE_DPAD_UP", binding)
        self.assertIn("R.id.btnAssistant", binding)
        self.assertIn("setOnFocusChangeListener", binding)
        self.assertIn("showPrayerPopup();", binding)

        start = MAIN.index("View.OnKeyListener pillKeys")
        end = MAIN.index("if (locationPill != null) locationPill.setOnKeyListener", start)
        header_keys = MAIN[start:end]
        self.assertIn("v.getId() == R.id.btnAssistant", header_keys)
        self.assertIn("findViewById(R.id.prayerTable)", header_keys)

    def test_supported_paused_media_session_remains_available_on_home(self):
        start = NOW_PLAYING.index("public boolean hasMusic()")
        end = NOW_PLAYING.index("public String getDisplayText()", start)
        has_music = NOW_PLAYING[start:end]
        self.assertIn("return isAllowedMusicApp(packageName) && isActiveSession;", has_music)
        self.assertNotIn("return isPlaying &&", has_music)
        self.assertIn("public final boolean isActiveSession;", NOW_PLAYING)
        self.assertIn("this.isActiveSession = playbackState == PlaybackState.STATE_PLAYING", NOW_PLAYING)
        for state in ("STATE_PLAYING", "STATE_PAUSED", "STATE_BUFFERING", "STATE_CONNECTING"):
            self.assertIn("PlaybackState." + state, NOW_PLAYING)
        self.assertIn("boolean hasActiveMusic = info != null && info.hasMusic();", MAIN)
        self.assertIn("info.isPlaying ? R.drawable.ic_pause : R.drawable.ic_play", MAIN)

    def test_home_weather_overlay_refreshes_on_resume_and_active_minute_tick(self):
        tick_start = MAIN.index("private final Runnable minuteTick")
        tick_end = MAIN.index("private final Runnable nowPlayingPoll", tick_start)
        minute_tick = MAIN[tick_start:tick_end]
        self.assertIn("updateHomeWeather();", minute_tick)
        self.assertIn("WeatherAirQuality.syncIfDue", minute_tick)

        resume_start = MAIN.index("protected void onResume()")
        resume_end = MAIN.index("private void reloadApps", resume_start)
        resume = MAIN[resume_start:resume_end]
        self.assertIn("updateHomeWeather();", resume)

        start = MAIN.index("private void updateHomeWeather()")
        end = MAIN.index("private void showHomeWeather", start)
        refresh = MAIN[start:end]
        self.assertIn("HomeWeather.due(this)", refresh)
        self.assertIn("!isDynamicWeatherEffectsEnabled()", refresh)
        self.assertIn("homeWeatherRunning", refresh)
        self.assertIn('new Thread(() ->', refresh)

        toggle_start = MAIN.index("void setDynamicWeatherEffectsEnabled(boolean enabled)")
        toggle_end = MAIN.index("private void applyIconGlowPreference()", toggle_start)
        toggle = MAIN[toggle_start:toggle_end]
        self.assertIn("updateWeatherOverlay(HomeWeather.last(this));", toggle)
        self.assertIn("if (enabled) updateHomeWeather();", toggle)


    def test_location_pill_opens_prayer_zone_dialog_and_routes_to_weather(self):
        self.assertIn(
            "if (locationPill != null) locationPill.setOnClickListener(v -> showPrayerZoneDialog());",
            MAIN,
        )
        start = MAIN.index("View.OnKeyListener pillKeys")
        end = MAIN.index("if (locationPill != null) locationPill.setOnKeyListener", start)
        keys = MAIN[start:end]
        for key in ("KEYCODE_DPAD_CENTER", "KEYCODE_ENTER", "KEYCODE_NUMPAD_ENTER",
                    "KEYCODE_DPAD_UP", "KEYCODE_DPAD_DOWN", "KEYCODE_DPAD_LEFT",
                    "KEYCODE_DPAD_RIGHT"):
            self.assertIn(key, keys)
        self.assertIn("v.performClick();", keys)
        self.assertIn("v.getId() == R.id.locationPill", keys)
        self.assertIn("weatherAirQualityView.requestFocus();", keys)

        start = MAIN.index("if (weatherAirQualityView != null) {")
        end = MAIN.index("// Measure the actual clock/date content edge", start)
        weather_keys = MAIN[start:end]
        self.assertIn("locationPill.requestFocus();", weather_keys)
        self.assertIn("btnHome.requestFocus()", weather_keys)

    def test_home_profile_and_settings_tabs_keep_their_real_actions(self):
        self.assertIn(
            "if (btnHome != null) btnHome.setOnClickListener(v -> updateHomeTabIndicator());",
            MAIN,
        )
        self.assertIn(
            "btnCandyBox.setOnClickListener(v -> showCandyBoxPopup());",
            MAIN,
        )
        self.assertIn(
            "btnSettings.setOnClickListener(v -> showQuickControlPopup());",
            MAIN,
        )
        self.assertIn("setText(indonesian ? \"Beranda\" : \"Home\")", MAIN)
        self.assertIn("setText(indonesian ? \"Profil\" : \"Profile\")", MAIN)
        self.assertIn("setText(indonesian ? \"Pengaturan\" : \"Settings\")", MAIN)

        start = MAIN.index("View.OnKeyListener pillKeys")
        end = MAIN.index("if (locationPill != null) locationPill.setOnKeyListener", start)
        keys = MAIN[start:end]
        self.assertIn("id == R.id.btnHome) target = btnCandyBox", keys)
        self.assertIn("id == R.id.btnCandyBox) target = btnSettings", keys)
        self.assertIn("id == R.id.btnSettings)", keys)
        self.assertIn("target = btnCandyBox;", keys)

    def test_header_widths_recompute_after_each_measured_group_changes(self):
        start = MAIN.index("View.OnLayoutChangeListener headerMetricsListener")
        end = MAIN.index("// Hide the glass when focus leaves the tiles.", start)
        metrics = MAIN[start:end]
        for view in ("headerRow", "leftHeader", "dateHeader", "rightHeader", "navHeader"):
            self.assertIn(f"{view}.addOnLayoutChangeListener(headerMetricsListener)", metrics)
        self.assertIn("root.post(this::applyHeaderWidths);", metrics)
        self.assertIn("HomeHeaderGeometry.centeredWidthPx", MAIN)
        self.assertIn("No measured space for centered Home navigation", MAIN)


    def test_weather_aqi_pill_matches_location_pill_width_and_left_anchor(self):
        start = MAIN.index("private void applyHeaderWidths()")
        end = MAIN.index("private void updatePrayerTimes()", start)
        sizing = MAIN[start:end]
        self.assertIn("int locationWidth = Math.round(width * 0.30f);", sizing)
        self.assertEqual(sizing.count("params.width = locationWidth;"), 2)
        self.assertNotIn("0.44f", sizing)

        root = ET.parse(LAYOUT).getroot()
        by_id = {node.get(ANDROID + "id"): node for node in root.iter()
                 if node.get(ANDROID + "id")}
        location = by_id["@+id/locationPill"]
        weather = by_id["@+id/weatherAirQuality"]
        self.assertEqual(location.get(ANDROID + "layout_alignParentStart"), "true")
        self.assertEqual(weather.get(ANDROID + "layout_alignParentStart"), "true")
        # Location/altitude is the physical-size reference for both capsules.
        self.assertEqual(location.get(ANDROID + "layout_height"), "30dp")
        self.assertEqual(weather.get(ANDROID + "layout_height"),
                         location.get(ANDROID + "layout_height"))
        self.assertEqual(weather.get(ANDROID + "paddingStart"),
                         location.get(ANDROID + "paddingStart"))
        self.assertEqual(weather.get(ANDROID + "paddingEnd"),
                         location.get(ANDROID + "paddingEnd"))
        self.assertEqual(weather.get(ANDROID + "paddingTop"), "0dp")
        self.assertEqual(weather.get(ANDROID + "paddingBottom"), "0dp")
        self.assertEqual(weather.get(ANDROID + "gravity"), "center_vertical")
        self.assertEqual(location.get(ANDROID + "gravity"), "center_vertical")
        self.assertEqual(weather.get(ANDROID + "singleLine"), "true")
        self.assertEqual(weather.get(ANDROID + "ellipsize"), "end")

        weather_update_start = MAIN.index("private void updateWeatherAqi()")
        weather_update_end = MAIN.index("private void updateHomeWeather()", weather_update_start)
        weather_update = MAIN[weather_update_start:weather_update_end]
        self.assertIn("weatherAirQualityView.setText(WeatherAirQuality.getFormatted(this));", weather_update)
        self.assertNotIn("setLayoutParams", weather_update)


if __name__ == "__main__":
    unittest.main()
