import pathlib
import unittest

ROOT = pathlib.Path(__file__).resolve().parents[1]
SETTINGS = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java"
MAIN = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java"
COORDINATOR = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/StandbyCoordinator.java"
UPLOAD = ROOT / "launcher/app/src/main/java/com/gathin/tvhome/WallpaperUploadServer.java"


class StandbySettingsSubtabsContractTest(unittest.TestCase):
    def test_four_subtabs_and_qr_upload_are_wired(self):
        settings = SETTINGS.read_text(encoding="utf-8")
        for label in ('"Umum"', '"Foto"', '"Tata Letak"', '"Sinkron."'):
            self.assertIn(label, settings)
        self.assertIn("activity::showStandbyUploadQrDialog", settings)
        self.assertNotIn("activity::pickStandbyPhotos", settings)

    def test_enable_switch_controls_the_single_idle_coordinator(self):
        settings = SETTINGS.read_text(encoding="utf-8")
        coordinator = COORDINATOR.read_text(encoding="utf-8")
        main = MAIN.read_text(encoding="utf-8")
        self.assertIn('"standby_enabled"', settings)
        self.assertIn("activity.onStandbyEnabledChanged()", settings)
        self.assertIn("onStandbySettingChanged()", main)
        self.assertIn("isStandbyEnabled()", coordinator)

    def test_layout_preferences_are_persisted_and_applied(self):
        settings = SETTINGS.read_text(encoding="utf-8")
        main = MAIN.read_text(encoding="utf-8")
        editor = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/StandbyWidgetEditor.java").read_text(encoding="utf-8")
        self.assertIn('"widget_x"', editor)
        self.assertIn('"widget_y"', editor)
        self.assertIn('"widget_shape"', settings)
        self.assertIn("applyStandbyPanelPreferences()", main)
        self.assertIn("UniversalGlassMaterial.homeCapsule(", main)
        self.assertIn("Gravity.TOP | Gravity.START", main)

    def test_subtabs_are_compact_and_dpad_navigation_is_explicit(self):
        settings = SETTINGS.read_text(encoding="utf-8")
        self.assertIn('setMinHeight((int) (34 * density))', settings)
        self.assertIn('setTextSize(10.5f)', settings)
        self.assertIn('KEYCODE_DPAD_LEFT && target > 0', settings)
        self.assertIn('KEYCODE_DPAD_RIGHT && target < subtabViews.length - 1', settings)
        self.assertIn('KEYCODE_DPAD_DOWN', settings)
        self.assertIn('KEYCODE_DPAD_UP', settings)
        self.assertIn('findFirstFocusableAfter(rightContent, tabStrip)', settings)
        self.assertIn('control.setNextFocusUpId', settings)
        self.assertIn('subtabViews[selectedIndex].setNextFocusDownId', settings)
        self.assertIn('collectFocusableAfter(rightContent, tabStrip, standbyControls)', settings)

    def test_qr_server_stores_photo_before_reporting_success(self):
        upload = UPLOAD.read_text(encoding="utf-8")
        self.assertIn("handleStandbyUpload(client, in, out, lines)", upload)
        self.assertIn("StandbyPhotoStore.addUploadedFile(context, storedFile)", upload)
        self.assertIn("listener.onStandbyUploaded(storedFile)", upload)
        self.assertIn("standbyUploadResponseStatus(uploadResult)", upload)


if __name__ == "__main__":
    unittest.main()
