"""Ensure Recent Apps and its destructive action are not externally launchable."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET


ROOT = Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "launcher" / "app" / "src" / "main" / "AndroidManifest.xml"
HOME_SERVICE = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome" / "HomeKeyService.java"
ANDROID_NS = "{http://schemas.android.com/apk/res/android}"


class RecentAppsManifestSecurityTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.root = ET.parse(MANIFEST).getroot()
        cls.app = cls.root.find("application")

    def test_recent_apps_activity_is_internal_only(self):
        matches = [
            node for node in self.app.findall("activity")
            if node.get(ANDROID_NS + "name") == ".RecentAppsActivity"
        ]
        self.assertEqual(1, len(matches), "expected exactly one Recent Apps activity")
        activity = matches[0]
        self.assertEqual("false", activity.get(ANDROID_NS + "exported"))
        self.assertIsNone(activity.find("intent-filter"))

    def test_recent_apps_remains_reachable_through_home_service_explicit_intent(self):
        source = HOME_SERVICE.read_text(encoding="utf-8")
        self.assertIn("new Intent(this, RecentAppsActivity.class)", source)
        self.assertIn("RecentAppsActivity.triggerCloseAllFromGlobal()", source)


if __name__ == "__main__":
    unittest.main()
