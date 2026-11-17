"""Regression guard for the Settings search modal shared presentation contract."""
from pathlib import Path
import re
import unittest


ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome" / "ModernSettingsDialog.java"


class SettingsSearchModalContractTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.source = SOURCE.read_text(encoding="utf-8")

    def test_search_results_dialog_uses_shared_modal_contract(self):
        self.assertRegex(
            self.source,
            re.compile(
                r"AlertDialog\s+resultsDialog\s*=.*?\.create\(\);"
                r"\s*resultsDialog\.show\(\);"
                r"\s*TvDialogStyle\.apply\(resultsDialog,\s*activity,\s*620,\s*0\);",
                re.DOTALL,
            ),
        )

    def test_search_input_dialog_uses_shared_modal_contract(self):
        self.assertRegex(
            self.source,
            re.compile(
                r"searchDialog\.show\(\);"
                r"\s*TvDialogStyle\.apply\(searchDialog,\s*activity,\s*620,\s*0\);",
            ),
        )


if __name__ == "__main__":
    unittest.main()
