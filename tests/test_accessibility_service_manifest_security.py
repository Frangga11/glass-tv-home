import unittest
import xml.etree.ElementTree as ET
from pathlib import Path


ANDROID = "{http://schemas.android.com/apk/res/android}"
ACCESSIBILITY_ACTION = "android.accessibilityservice.AccessibilityService"
BIND_PERMISSION = "android.permission.BIND_ACCESSIBILITY_SERVICE"
MANIFEST = Path("launcher/app/src/main/AndroidManifest.xml")


class AccessibilityServiceManifestSecurityTest(unittest.TestCase):
    def test_accessibility_services_are_exported_and_system_permission_protected(self):
        root = ET.parse(MANIFEST).getroot()
        application = root.find("application")
        self.assertIsNotNone(application)

        services = {}
        for service in application.findall("service"):
            actions = {
                action.get(ANDROID + "name")
                for intent_filter in service.findall("intent-filter")
                for action in intent_filter.findall("action")
            }
            if ACCESSIBILITY_ACTION in actions:
                name = service.get(ANDROID + "name")
                services[name] = service
                self.assertEqual(
                    "true",
                    service.get(ANDROID + "exported"),
                    f"{name} must be discoverable/bindable by Android system services",
                )
                self.assertEqual(
                    BIND_PERMISSION,
                    service.get(ANDROID + "permission"),
                    f"{name} must restrict binding to the Android system",
                )

        self.assertIn(".HomeKeyService", services)
        self.assertIn(".InputKeyService", services)


if __name__ == "__main__":
    unittest.main()
