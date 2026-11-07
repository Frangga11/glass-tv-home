from pathlib import Path
import unittest


ROOT = Path(__file__).resolve().parents[1]


class GlobalSettingsSourceContractTest(unittest.TestCase):
    def test_v06_seven_category_taxonomy_is_present(self):
        language = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/AppLanguage.java").read_text()
        expected_labels = [
            'case LANG_ID: return "Profil";',
            'case LANG_ID: return "Layar Utama";',
            'case LANG_ID: return "Tampilan";',
            'case LANG_ID: return "Widget & Informasi";',
            'case LANG_ID: default: return "Animasi & Efek";',
            'case LANG_ID: return "Layar Siaga";',
            'case LANG_ID: default: return "Pengaturan Perangkat";',
        ]
        for label in expected_labels:
            with self.subTest(label=label):
                self.assertIn(label, language)

    def test_global_settings_has_header_status_and_split_panes(self):
        source = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java").read_text()
        self.assertIn('brandTitle.setText("GLASS TV HOME")', source)
        self.assertIn("activity.getGlobalSettingsNetworkSummary()", source)
        self.assertIn("body.addView(leftCol, leftLp)", source)
        self.assertIn("body.addView(rightCol, rightLp)", source)

    def test_search_routes_to_control_and_focuses_it(self):
        source = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java").read_text()
        self.assertIn("buildSearchEntries(activity)", source)
        self.assertIn("findControlByDescription(rightContent, selected.title)", source)
        self.assertIn("target.setTag(selected.id)", source)
        self.assertIn("target.requestFocus()", source)
        self.assertIn("R.string.search_keyword_required", source)
        self.assertNotIn("String[] keywords =", source)

    def test_search_catalog_uses_existing_control_labels_and_summaries(self):
        source = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java").read_text()
        for control_id in (
            "profile.child-mode", "profile.avatar.builtin.", "profile.profile.",
            "home.dock-orientation", "appearance.wallpaper-collection",
            "widgets.weather", "effects.glass", "standby.upload", "device.android-settings",
        ):
            with self.subTest(control_id=control_id):
                self.assertIn('"' + control_id + '"', source)

    def test_english_category_name_matches_v06_taxonomy(self):
        source = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/AppLanguage.java").read_text()
        self.assertIn('case LANG_EN: return "Widgets & Information";', source)

    def test_external_android_settings_return_refreshes_without_new_network_owner(self):
        main = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text()
        settings = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java").read_text()
        self.assertIn("ModernSettingsDialog.onHostResumed(this)", main)
        self.assertIn("ModernSettingsDialog.onHostDestroyed(this)", main)
        self.assertIn("ModernSettingsDialog.refreshNetworkStatus(this)", main)
        self.assertIn("WeakReference<MainActivity> sSettingsHost", settings)
        self.assertIn("findControlByDescription(content, focusedDescription)", settings)
        self.assertIn("legacyFocusSignature(current)", settings)
        self.assertIn("resolveLegacySidebarFocusPath(leftCol, searchBox", settings)
        self.assertIn("resolveLegacyRightFocusPath(rightContent, activeTab[0]", settings)


if __name__ == "__main__":
    unittest.main()
