"""Regression guard for the global MainActivity modal styling entry point."""
from pathlib import Path
import re
import unittest


ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome" / "MainActivity.java"


class MainActivityModalContractTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.source = SOURCE.read_text(encoding="utf-8")

    def test_default_dialog_styler_delegates_to_global_contract(self):
        self.assertRegex(
            self.source,
            re.compile(
                r"void styleGlassDialog\(AlertDialog dialog\)\s*\{"
                r"\s*styleGlassDialog\(dialog,\s*520,\s*0\);",
            ),
        )

    def test_sized_dialog_styler_applies_global_contract(self):
        self.assertRegex(
            self.source,
            re.compile(
                r"void styleGlassDialog\(AlertDialog dialog,\s*int maxWidthDp,\s*int maxHeightDp\)\s*\{"
                r"[\s\S]*?TvDialogStyle\.apply\(dialog,\s*this,\s*maxWidthDp,\s*maxHeightDp\);",
            ),
        )

    def test_device_settings_popup_uses_sized_global_styler_when_present(self):
        if "void styleDeviceSettingsPopup(" not in self.source:
            self.skipTest("Device Settings wrapper is added by the integrated UI branch")
        self.assertRegex(
            self.source,
            re.compile(
                r"void styleDeviceSettingsPopup\(AlertDialog dialog,\s*int maxWidthDp,\s*int maxHeightDp\)\s*\{"
                r"\s*styleGlassDialog\(dialog,\s*maxWidthDp,\s*maxHeightDp\);",
            ),
        )

    def test_now_playing_custom_dialog_uses_global_modal_contract(self):
        self.assertRegex(
            self.source,
            re.compile(
                r"private void showNowPlayingDialog\(\)\s*\{[\s\S]*?"
                r"dialog\.show\(\);\s*"
                r"TvDialogStyle\.applyCustom\(dialog,\s*this,\s*480,\s*0,\s*0\.65f\);",
            ),
        )

    def test_card_prompt_custom_dialog_uses_global_modal_contract(self):
        self.assertRegex(
            self.source,
            re.compile(
                r"private void showCardPromptNow\(\)\s*\{[\s\S]*?"
                r"dialog\.show\(\);\s*"
                r"TvDialogStyle\.applyCustom\(dialog,\s*this,\s*440,\s*0,\s*0\.6f\);",
            ),
        )


if __name__ == "__main__":
    unittest.main()
