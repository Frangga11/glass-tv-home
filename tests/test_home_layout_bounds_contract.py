"""Regression contract for Home controls sharing a balanced safe frame."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET


ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
LAYOUT = ROOT / "launcher/app/src/main/res/layout/activity_main.xml"
GEOMETRY = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java").read_text(encoding="utf-8")
ANDROID = "{http://schemas.android.com/apk/res/android}"


class HomeLayoutBoundsContractTest(unittest.TestCase):
    def test_home_content_uses_equal_34dp_top_and_side_safe_margins(self):
        root = ET.parse(LAYOUT).getroot()
        main_content = next((node for node in root.iter()
                             if node.get(ANDROID + "id") == "@+id/mainContent"), None)
        self.assertIsNotNone(main_content)
        self.assertEqual(main_content.get(ANDROID + "paddingStart"), "34dp")
        self.assertEqual(main_content.get(ANDROID + "paddingEnd"), "34dp")
        self.assertEqual(main_content.get(ANDROID + "paddingTop"), "34dp")

        self.assertIn("SAFE_HORIZONTAL_MARGIN_DP = 34f", GEOMETRY)
        self.assertIn("SAFE_VERTICAL_MARGIN_DP = 34f", GEOMETRY)
        self.assertIn("safeBottomInsetPx", GEOMETRY)

    def test_right_prayer_shelf_uses_the_same_top_and_right_safe_inset(self):
        start = MAIN.index("private void applyHomeShelfSafeMargins()")
        end = MAIN.index("private void applyDockLayout()", start)
        shelf = MAIN[start:end]
        self.assertIn("params.setMarginEnd(inset)", shelf)
        self.assertIn("params.topMargin = inset", shelf)
        self.assertIn("params.setMarginStart(inset)", shelf)

    def test_bottom_dock_tray_uses_the_same_safe_frame_as_top_and_sides(self):
        start = MAIN.index("private void applyDockLayout()")
        end = MAIN.index("private void updateHomeTabIndicator()", start)
        dock_layout = MAIN[start:end]
        self.assertIn(
            "dockParams.bottomMargin = DockGeometry.horizontalDockBottomMarginPx(tileHeight, density)",
            dock_layout,
        )
        self.assertIn("return safeBottomInsetPx(density) - trayBottomInset", GEOMETRY)

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


if __name__ == "__main__":
    unittest.main()
