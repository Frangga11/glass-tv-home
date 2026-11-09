"""Regression contracts for the Home UI proportion, manual OTA, glass and focus refinement."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
UPDATE = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/AppUpdateManager.java").read_text(encoding="utf-8")
SETTINGS = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java").read_text(encoding="utf-8")
DOCK = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockAdapter.java").read_text(encoding="utf-8")
GEOMETRY = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java").read_text(encoding="utf-8")
HOME_XML = ROOT / "launcher/app/src/main/res/layout/activity_main.xml"
OTA_XML = ROOT / "launcher/app/src/main/res/layout/dialog_ota_coffee.xml"
NAV_BG = ROOT / "launcher/app/src/main/res/drawable/header_nav_tab_bg.xml"
GLASS_BG = ROOT / "launcher/app/src/main/res/drawable/header_glass_pill_bg.xml"
PILL_BG = ROOT / "launcher/app/src/main/res/drawable/pill_bg.xml"
ANDROID = "{http://schemas.android.com/apk/res/android}"


class HomeUiProportionGlassFocusContractTest(unittest.TestCase):
    def test_updates_are_manual_only_and_auto_prompt_paths_are_removed(self):
        self.assertNotIn("checkForUpdates(this, false)", MAIN)
        self.assertNotIn("badgeSettingsUpdate", MAIN)
        self.assertNotIn("updateSettingsBadge", MAIN)
        self.assertNotIn("showAutoUpdateDialog", MAIN)
        self.assertNotIn("checkAutoUpdateIfDue", UPDATE)
        self.assertNotIn("KEY_AUTO_UPDATE", UPDATE)
        self.assertNotIn("KEY_SKIPPED_VERSION_CODE", UPDATE)
        self.assertIn("if (manualCheck) {\n                            showUpdateDialog", UPDATE)
        self.assertIn("AppUpdateManager.checkForUpdates(MainActivity.this, true)", MAIN)
        self.assertIn("AppUpdateManager.checkForUpdates(activity, true)", SETTINGS)
        self.assertIn("activity.showManualUpdateDialog()", SETTINGS)

    def test_manual_update_installation_pipeline_is_preserved(self):
        self.assertIn("downloadAndInstallApk(activity, apkUrl, newVersionName)", UPDATE)
        self.assertIn("private static void downloadAndInstallApk", UPDATE)
        self.assertIn("private static void installApk", UPDATE)
        self.assertIn("FileProvider.getUriForFile", UPDATE)
        self.assertIn("Intent.ACTION_VIEW", UPDATE)
        self.assertIn("application/vnd.android.package-archive", UPDATE)
        self.assertIn("canRequestPackageInstalls()", UPDATE)
        self.assertIn("R.id.btnCheckNow", MAIN)

    def test_update_red_badge_and_auto_controls_are_absent(self):
        home = ET.parse(HOME_XML).getroot()
        self.assertFalse(any(node.get(ANDROID + "id") == "@+id/badgeSettingsUpdate"
                             for node in home.iter()))
        ota = ET.parse(OTA_XML).getroot()
        ids = {node.get(ANDROID + "id") for node in ota.iter()}
        self.assertIn("@+id/btnCheckNow", ids)
        self.assertIn("@+id/btnClose", ids)
        self.assertIn("@+id/tvManualStatus", ids)
        self.assertNotIn("@+id/btnChangeTime", ids)
        self.assertNotIn("@+id/btnToggleAuto", ids)
        self.assertNotIn("@+id/tvAutoStatus", ids)
        for path in (
            ROOT / "launcher/app/src/main/res/values/strings.xml",
            ROOT / "launcher/app/src/main/res/values-en/strings.xml",
            ROOT / "launcher/app/src/main/res/values-id/strings.xml",
        ):
            resource = ET.parse(path).getroot()
            names = {node.get("name") for node in resource.findall("string")}
            self.assertNotIn("ota_auto_update_active", names)
            self.assertNotIn("auto_update_schedule_title", names)
            self.assertIn("ota_manual_update_status", names)

    def test_navigation_capsule_is_slimmer_and_segment_spacing_is_uniform(self):
        root = ET.parse(HOME_XML).getroot()
        nav = next(node for node in root.iter()
                   if node.get(ANDROID + "id") == "@+id/navPill")
        self.assertEqual(nav.get(ANDROID + "layout_height"), "30dp")
        self.assertEqual(nav.get(ANDROID + "paddingStart"), "2dp")
        self.assertEqual(nav.get(ANDROID + "paddingEnd"), "2dp")
        self.assertEqual(nav.get(ANDROID + "paddingTop"), "0dp")
        self.assertEqual(nav.get(ANDROID + "paddingBottom"), "0dp")
        segments = {}
        for node in nav.iter():
            node_id = node.get(ANDROID + "id")
            if node_id in ("@+id/btnHome", "@+id/btnCandyBox", "@+id/btnSettings"):
                segments[node_id] = node
        self.assertEqual(set(segments), {"@+id/btnHome", "@+id/btnCandyBox", "@+id/btnSettings"})
        for node_id, node in segments.items():
            self.assertEqual(node.get(ANDROID + "gravity"), "center")
            self.assertEqual(node.get(ANDROID + "drawablePadding"),
                             "1dp" if node_id in ("@+id/btnHome", "@+id/btnCandyBox") else "4dp")
            self.assertEqual(node.get(ANDROID + "layout_height"), "match_parent")
        settings_container = next(node for node in nav.iter()
                                  if node.get(ANDROID + "id") == "@+id/btnSettingsContainer")
        self.assertEqual(settings_container.get(ANDROID + "layout_weight"), "1")

    def test_dock_icons_expand_optically_without_changing_grid_icon_geometry(self):
        self.assertIn("int iconSize = Math.round(44 * density)", DOCK)
        self.assertIn("icon.setScaleX(vertical ? 1f : 1.045f)", DOCK)
        self.assertIn("DOCK_EXTRA_HEIGHT_DP = 12f", GEOMETRY)
        item = ET.parse(ROOT / "launcher/app/src/main/res/layout/item_app.xml").getroot()
        icon = next(node for node in item.iter()
                    if node.get(ANDROID + "id") == "@+id/icon")
        self.assertEqual(icon.get(ANDROID + "layout_width"), "48dp")
        self.assertEqual(icon.get(ANDROID + "layout_height"), "48dp")

    def test_prayer_and_daily_doa_have_a_subtle_content_scoped_divider(self):
        root = ET.parse(HOME_XML).getroot()
        ayah = next(node for node in root.iter()
                    if node.get(ANDROID + "id") == "@+id/ayahBlock")
        divider = next(node for node in ayah
                       if node.get(ANDROID + "id") == "@+id/prayerSectionDivider")
        self.assertEqual(divider.get(ANDROID + "layout_width"), "180dp")
        self.assertEqual(divider.get(ANDROID + "layout_height"), "1dp")
        self.assertEqual(divider.get(ANDROID + "layout_gravity"), "end")
        self.assertIn("0x12000000, 0x26000000", MAIN)
        self.assertNotIn("0x44000000", MAIN)

    def test_all_header_pills_share_one_glider_focus_and_glass_material(self):
        self.assertIn("new GlassDrawable(navCapsule, 20f * density, density, glider)", MAIN)
        self.assertIn("new GlassDrawable(pill, 20f * density, density, glider)", MAIN)
        self.assertIn("glider.addCompanion(navCapsule)", MAIN)
        self.assertIn("glider.addCompanion(pill)", MAIN)
        self.assertIn("glider.setStyle(Glider.DARK)", MAIN)
        self.assertIn("Math.min(20f * density, pill.getHeight() / 2f)", MAIN)
        nav_bg = ET.parse(NAV_BG).getroot()
        self.assertTrue(any("state_focused" in key for node in nav_bg.iter()
                            for key in node.attrib))
        glass_bg = ET.parse(GLASS_BG).getroot()
        pill_bg = ET.parse(PILL_BG).getroot()
        for drawable in (glass_bg, pill_bg):
            self.assertTrue(any(node.get(ANDROID + "radius") == "20dp"
                                for node in drawable.iter()))
            self.assertFalse(any("state_focused" in key for node in drawable.iter()
                                 for key in node.attrib))


if __name__ == "__main__":
    unittest.main()
