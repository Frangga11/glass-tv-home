"""Security regression checks for location configuration component boundaries."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET


ROOT = Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "launcher" / "app" / "src" / "main" / "AndroidManifest.xml"
MAIN_ACTIVITY = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome" / "MainActivity.java"
ANDROID_NS = "{http://schemas.android.com/apk/res/android}"


class LocationBroadcastManifestSecurityTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.root = ET.parse(MANIFEST).getroot()
        cls.app = cls.root.find("application")

    def test_exported_receiver_requires_shell_system_permission(self):
        matches = [
            node for node in self.app.findall("receiver")
            if node.get(ANDROID_NS + "name") == ".LocationBroadcastReceiver"
        ]
        self.assertEqual(1, len(matches), "expected exactly one location receiver")
        receiver = matches[0]
        self.assertEqual("true", receiver.get(ANDROID_NS + "exported"))
        self.assertEqual("android.permission.DUMP", receiver.get(ANDROID_NS + "permission"))

        actions = {
            node.get(ANDROID_NS + "name")
            for node in receiver.findall("intent-filter/action")
        }
        self.assertEqual(
            {
                "com.gathin.tvhome.SET_LOCATION",
                "com.gathin.tvhome.SET_WEATHER",
            },
            actions,
            "authorization must not silently remove supported actions",
        )

    def test_location_picker_activity_is_internal_only(self):
        matches = [
            node for node in self.app.findall("activity")
            if node.get(ANDROID_NS + "name") == ".LocationActivity"
        ]
        self.assertEqual(1, len(matches), "expected exactly one location picker activity")
        activity = matches[0]
        self.assertEqual("false", activity.get(ANDROID_NS + "exported"))
        self.assertIsNone(activity.find("intent-filter"))

        # The feature remains reachable through explicit app-owned intents.
        main_source = MAIN_ACTIVITY.read_text(encoding="utf-8")
        self.assertIn("LocationActivity.class", main_source)


if __name__ == "__main__":
    unittest.main()
