"""Security regression checks for the externally callable location configuration receiver."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET


ROOT = Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "launcher" / "app" / "src" / "main" / "AndroidManifest.xml"
ANDROID_NS = "{http://schemas.android.com/apk/res/android}"


class LocationBroadcastManifestSecurityTest(unittest.TestCase):
    def test_exported_receiver_requires_shell_system_permission(self):
        root = ET.parse(MANIFEST).getroot()
        app = root.find("application")
        matches = [
            node for node in app.findall("receiver")
            if node.get(ANDROID_NS + "name") == ".LocationBroadcastReceiver"
        ]
        self.assertEqual(1, len(matches), "expected exactly one location receiver")
        receiver = matches[0]

        # Keep the documented adb-shell workflow while blocking ordinary app UIDs.
        self.assertEqual("true", receiver.get(ANDROID_NS + "exported"))
        self.assertEqual(
            "android.permission.DUMP",
            receiver.get(ANDROID_NS + "permission"),
            "external location/weather broadcasts must be limited to shell/system callers",
        )

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
            "authorization must not silently remove the existing supported actions",
        )


if __name__ == "__main__":
    unittest.main()
