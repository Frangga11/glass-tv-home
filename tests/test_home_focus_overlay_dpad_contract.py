"""Regression contracts for shared Home capsule focus layering and the owner DPAD graph."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher/app/src/main/java/com/gathin/tvhome"
MAIN = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
LAYOUT = ROOT / "launcher/app/src/main/res/layout/activity_main.xml"
ANDROID = "{http://schemas.android.com/apk/res/android}"


class HomeFocusOverlayDpadContractTest(unittest.TestCase):
    def test_focus_is_composited_after_capsule_glass_and_before_text_content(self):
        drawable = (JAVA / "GliderFocusDrawable.java").read_text(encoding="utf-8")
        self.assertIn("glider.drawShape(canvas", drawable)
        self.assertIn("canvas.clipRect(getBounds())", drawable)
        self.assertIn("new GliderFocusDrawable(pill, glider)", MAIN)
        self.assertIn("new android.graphics.drawable.LayerDrawable(", MAIN)
        self.assertIn("glider.addCompanion(pill)", MAIN)

    def test_navigation_tabs_have_equal_weights_and_one_dp_icon_label_gap(self):
        root = ET.parse(LAYOUT).getroot()
        views = {node.get(ANDROID + "id"): node for node in root.iter()
                 if node.get(ANDROID + "id")}
        for view_id in ("@+id/btnHome", "@+id/btnCandyBox", "@+id/btnSettings"):
            with self.subTest(view_id=view_id):
                self.assertEqual(views[view_id].get(ANDROID + "drawablePadding"), "1dp")
                self.assertEqual(views[view_id].get(ANDROID + "layout_weight"), "1")
                self.assertEqual(views[view_id].get(ANDROID + "gravity"), "center")

    def test_home_has_no_permanent_selected_or_elevated_state(self):
        start = MAIN.index("private void updateHomeTabIndicator()")
        end = MAIN.index("private void applyHeaderWidths()", start)
        method = MAIN[start:end]
        self.assertIn("btnHome.setSelected(false);", method)
        self.assertIn("btnHome.setElevation(0f);", method)
        self.assertNotIn("setSelected(active)", method)
        self.assertNotIn("8f * density", method)

    def test_owner_header_horizontal_and_vertical_routes_are_explicit(self):
        start = MAIN.index("View.OnKeyListener pillKeys")
        end = MAIN.index("// Measure the actual clock/date content edge", start)
        keys = MAIN[start:end]
        # Left branch from Home and the vertical information pair.
        self.assertIn("if (id == R.id.btnHome) target = locationPill;", keys)
        self.assertIn("id == R.id.locationPill && weatherAirQualityView", keys)
        self.assertIn("weatherAirQualityView.requestFocus()", keys)
        # Primary horizontal route skips the optional Inputs control.
        self.assertIn("else if (id == R.id.btnHome) target = btnCandyBox;", keys)
        self.assertIn("else if (id == R.id.btnCandyBox) target = btnSettings;", keys)
        self.assertIn("else if (id == R.id.btnSettings)", keys)
        self.assertIn("target = btnWifi != null && btnWifi.isShown() ? btnWifi : btnAssistant;", keys)
        self.assertIn("else if (id == R.id.btnWifi) target = btnAssistant;", keys)
        # Header controls go straight to app row one, while Mic opens the Ayah path.
        self.assertIn("focusTile(0);", keys)
        self.assertIn("if (id == R.id.btnAssistant)", keys)
        self.assertIn("ayahBlock.requestFocus()", keys)

    def test_ayah_prayer_and_grid_form_one_vertical_path(self):
        ayah_start = MAIN.index("ayahBlock.setOnKeyListener")
        ayah_end = MAIN.index("ayahBlock.setBackground", ayah_start)
        ayah = MAIN[ayah_start:ayah_end]
        self.assertIn("KEYCODE_DPAD_UP", ayah)
        self.assertIn("btnAssistant.requestFocus()", ayah)
        self.assertIn("KEYCODE_DPAD_DOWN", ayah)
        self.assertIn("prayerTable.requestFocus()", ayah)

        prayer_start = MAIN.index("View prayerTableView = findViewById(R.id.prayerTable);")
        prayer_end = MAIN.index("updatePrayerTimes();", prayer_start)
        prayer = MAIN[prayer_start:prayer_end]
        self.assertIn("ayahBlock.requestFocus()", prayer)
        self.assertIn("focusTile(0);", prayer)

    def test_first_app_row_up_returns_to_home_and_row_two_up_keeps_row_transition(self):
        start = MAIN.index("public boolean onTileVerticalKey")
        end = MAIN.index("private int pendingFocus;", start)
        method = MAIN[start:end]
        self.assertIn("btnHome.requestFocus()", method)
        self.assertIn("requestHorizontalGridRow(basePosition - COLUMNS)", method)
        self.assertIn("target = position - COLUMNS;", method)

    def test_profile_and_wifi_popups_restore_their_trigger_focus(self):
        self.assertIn("UserSwitchDialog.show(this, () -> restoreFocusTo(originFocus));", MAIN)
        self.assertIn("Runnable onReturn = returnToParent != null ? returnToParent : () -> restoreFocusTo(originFocus);", MAIN)
        self.assertIn("private void restoreFocusTo(View origin)", MAIN)
        profile = (JAVA / "UserSwitchDialog.java").read_text(encoding="utf-8")
        self.assertIn("public static void show(MainActivity activity, Runnable onDismiss)", profile)
        self.assertIn("dialog.setOnDismissListener", profile)
        quick = (JAVA / "QuickSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("if (onDismiss != null) onDismiss.run();", quick)


if __name__ == "__main__":
    unittest.main()
