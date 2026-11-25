import pathlib
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
MAIN = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java"
SETTINGS = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java"
EDITOR = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/StandbyWidgetEditor.java"


class StandbyWidgetEditorContractTest(unittest.TestCase):
    def test_editor_is_reached_from_layout_settings(self):
        self.assertIn("activity::showStandbyWidgetEditor", SETTINGS.read_text(encoding="utf-8"))
        self.assertIn("StandbyWidgetEditor.show", MAIN.read_text(encoding="utf-8"))

    def test_editor_supports_move_save_cancel_and_reset(self):
        editor = EDITOR.read_text(encoding="utf-8")
        for token in ("KEYCODE_DPAD_LEFT", "KEYCODE_DPAD_RIGHT", "KEYCODE_DPAD_UP",
                      "KEYCODE_DPAD_DOWN", '"widget_x"', '"widget_y"', "Simpan", "Batal", "Reset",
                      "dispatchKeyEvent", "selectedAction", "perform[0].run()"):
            self.assertIn(token, editor)
        self.assertNotIn("button.setOnKeyListener", editor)
        self.assertNotIn("overlay.setOnKeyListener", editor)

    def test_editor_uses_topmost_window_and_restores_preview_host(self):
        editor = EDITOR.read_text(encoding="utf-8")
        self.assertIn("new Dialog(activity)", editor)
        self.assertIn("originalParent.removeView(host)", editor)
        self.assertIn("originalParent.addView(host, index, originalParams)", editor)
        self.assertIn("FLAG_LAYOUT_IN_SCREEN", editor)

    def test_preview_dpad_exits_and_refresh_does_not_reset_widget(self):
        main = MAIN.read_text(encoding="utf-8")
        self.assertIn("StandbyPreviewWindow.show", main)
        self.assertIn("standbyPhotoUriLoading", main)
        self.assertIn("boolean stillCurrent", main)
        photo_loader = main.split("    private void updateNightstandPhoto() {", 1)[1].split("    private void dismissScreensaver()", 1)[0]
        self.assertNotIn("!screensaverActive", photo_loader)
        preview = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/StandbyPreviewWindow.java").read_text(encoding="utf-8")
        self.assertIn("KEYCODE_DPAD_LEFT", preview)
        self.assertIn("KEYCODE_DPAD_RIGHT", preview)
        self.assertIn("KEYCODE_DPAD_UP", preview)
        self.assertIn("KEYCODE_DPAD_DOWN", preview)
        self.assertIn("dispatchKeyEvent", preview)
        self.assertNotIn("preview.setOnKeyListener", preview)
        self.assertNotIn("content.setTranslationX(0f)", main)
        self.assertNotIn("content.setTranslationY(0f)", main)

    def test_android_sync_tab_reads_live_system_settings(self):
        settings = SETTINGS.read_text(encoding="utf-8")
        self.assertIn("buildStandbyDiagnosticsCard", settings)
        self.assertIn('"screensaver_enabled"', settings)
        self.assertIn('"screensaver_components"', settings)
        self.assertIn("SCREEN_OFF_TIMEOUT", settings)
        self.assertIn('"sleep_timeout"', settings)
        self.assertIn("Tidak dapat dibaca", settings)

    def test_panel_uses_shared_silver_cyan_material(self):
        main = MAIN.read_text(encoding="utf-8")
        self.assertIn("UniversalGlassMaterial.homeCapsule(", main)
        self.assertNotIn("new int[]{0xEAF8FFFF, 0xDDEAF7FF, 0xD3DCEBFA}", main)

    def test_four_standby_widget_layouts_are_selectable_and_compact_is_default(self):
        settings = SETTINGS.read_text(encoding="utf-8")
        main = MAIN.read_text(encoding="utf-8")
        controller_path = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/StandbyWidgetLayoutController.java"
        controller = controller_path.read_text(encoding="utf-8")
        for token in ('"Widget Linear"', '"Widget Kolom"', '"Widget Bentang"', '"Widget Compact"',
                      '"linear", "column", "bentang", "compact"', '"Bentuk Widget"'):
            self.assertIn(token, settings)
        self.assertIn('getString("widget_layout", StandbyWidgetLayoutController.COMPACT)', main)
        self.assertIn("StandbyWidgetLayoutController.apply(", main)
        for token in ("buildLinear(", "buildColumn(", "buildBentang(", "restoreCompact(",
                      "refreshPalette(", "nightstandPrayerSchedule"):
            self.assertIn(token, controller)
        self.assertIn("Widget Compact", settings)
        self.assertNotIn('putString("widget_shape"', settings)

    def test_widget_layout_controller_reuses_live_views_and_restores_compact_tree(self):
        controller = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/StandbyWidgetLayoutController.java").read_text(encoding="utf-8")
        self.assertIn("WeakHashMap<LinearLayout, Snapshot>", controller)
        self.assertIn("root.findViewById(R.id.nightstandClock)", controller)
        self.assertIn("root.findViewById(R.id.nightstandPrayerSchedule)", controller)
        self.assertIn("detach(label)", controller)
        self.assertIn("content.addView(child, s.childParams[i])", controller)


if __name__ == "__main__":
    unittest.main()
