from pathlib import Path
import unittest


ROOT = Path(__file__).resolve().parents[1]


class GlobalSettingsSourceContractTest(unittest.TestCase):
    def test_v06_seven_category_taxonomy_is_present(self):
        prototype = (ROOT / "docs/project-management/prototypes/UI_REFRESH_G1_GLOBAL_UI_PROTOTYPE_V6.html").read_text()
        categories = [
            "Profil", "Layar Utama", "Tampilan", "Widget &amp; Informasi",
            "Animasi &amp; Efek", "Layar Siaga", "Pengaturan Perangkat",
        ]
        for category in categories:
            with self.subTest(category=category):
                self.assertIn(category, prototype)

    def test_global_settings_has_header_status_and_split_panes(self):
        source = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java").read_text()
        self.assertIn('brandTitle.setText("GLASS TV HOME")', source)
        self.assertIn("activity.getQuickSettingsNetworkSummary()", source)
        self.assertIn("body.addView(leftCol, leftLp)", source)
        self.assertIn("body.addView(rightCol, rightLp)", source)

    def test_search_routes_to_control_and_focuses_it(self):
        source = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java").read_text()
        self.assertIn("buildSearchEntries(activity)", source)
        self.assertIn("findControlByDescription(rightContent, selected.title)", source)
        self.assertIn("target.requestFocus()", source)
        self.assertNotIn("String[] keywords =", source)

    def test_search_catalog_uses_existing_control_labels_and_summaries(self):
        source = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java").read_text()
        for control_id in (
            "profile.child-mode", "home.dock-orientation", "appearance.wallpaper-collection",
            "widgets.weather", "effects.glass", "standby.upload", "device.android-settings",
        ):
            with self.subTest(control_id=control_id):
                self.assertIn('"' + control_id + '"', source)

    def test_english_category_name_matches_v06_taxonomy(self):
        source = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/AppLanguage.java").read_text()
        self.assertIn('case LANG_EN: return "Widgets & Information";', source)


if __name__ == "__main__":
    unittest.main()
