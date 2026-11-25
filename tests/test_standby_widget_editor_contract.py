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
                      "KEYCODE_DPAD_DOWN", '"widget_x"', '"widget_y"', "Simpan", "Batal", "Reset"):
            self.assertIn(token, editor)

    def test_panel_uses_shared_silver_cyan_material(self):
        main = MAIN.read_text(encoding="utf-8")
        self.assertIn("UniversalGlassMaterial.homeCapsule(", main)
        self.assertNotIn("new int[]{0xEAF8FFFF, 0xDDEAF7FF, 0xD3DCEBFA}", main)


if __name__ == "__main__":
    unittest.main()
