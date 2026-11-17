"""Manifest security regression checks for app-owned prayer alert components."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET


ROOT = Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "launcher" / "app" / "src" / "main" / "AndroidManifest.xml"
ANDROID_NS = "{http://schemas.android.com/apk/res/android}"


class PrayerComponentManifestSecurityTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.root = ET.parse(MANIFEST).getroot()
        cls.application = cls.root.find("application")

    def component(self, tag, name):
        matches = [
            node for node in self.application.findall(tag)
            if node.get(ANDROID_NS + "name") == name
        ]
        self.assertEqual(1, len(matches), f"expected exactly one {tag} declaration for {name}")
        return matches[0]

    def test_azan_activity_is_not_externally_launchable(self):
        activity = self.component("activity", ".AzanActivity")
        self.assertEqual(
            "false",
            activity.get(ANDROID_NS + "exported"),
            "AzanActivity is app-internal and must not be exported",
        )

    def test_alarm_receivers_remain_app_internal(self):
        for name in (".AzanReceiver", ".AzanBootReceiver"):
            with self.subTest(component=name):
                receiver = self.component("receiver", name)
                self.assertEqual("false", receiver.get(ANDROID_NS + "exported"))


if __name__ == "__main__":
    unittest.main()
