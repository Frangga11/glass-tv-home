"""Regression contract for the Installer launch-window surface."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
ANDROID = "{http://schemas.android.com/apk/res/android}"
INSTALLER_THEME = ROOT / "launcher/installer/src/main/res/values/styles.xml"
INSTALLER_LAYOUT = ROOT / "launcher/installer/src/main/res/layout/activity_main.xml"


class InstallerWindowSurfaceContractTest(unittest.TestCase):
    def test_window_background_matches_first_content_root(self):
        theme = ET.parse(INSTALLER_THEME).getroot()
        layout = ET.parse(INSTALLER_LAYOUT).getroot()
        installer_theme = next(node for node in theme.findall("style")
                               if node.get("name") == "Theme.Installer")
        window_background = next(node for node in installer_theme.findall("item")
                                  if node.get("name") == "android:windowBackground")
        self.assertEqual(window_background.text.strip(), "#2C333B")
        self.assertEqual(layout.get(ANDROID + "background"), "#2C333B")

    def test_system_bar_colors_remain_explicit_and_unchanged(self):
        theme = ET.parse(INSTALLER_THEME).getroot()
        installer_theme = next(node for node in theme.findall("style")
                               if node.get("name") == "Theme.Installer")
        items = {node.get("name"): node.text.strip()
                 for node in installer_theme.findall("item")}
        self.assertEqual(items["android:statusBarColor"], "#0C0E14")
        self.assertEqual(items["android:navigationBarColor"], "#0C0E14")


if __name__ == "__main__":
    unittest.main()
