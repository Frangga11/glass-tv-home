"""Regression contract for the floating Ayat and prayer-time presentation."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher/app/src/main/java/com/gathin/tvhome"
LAYOUT = ROOT / "launcher/app/src/main/res/layout/activity_main.xml"
ANDROID = "{http://schemas.android.com/apk/res/android}"


class HomeFloatingContentRightGradientContractTest(unittest.TestCase):
    def test_islamic_shelf_uses_a_local_right_fade_instead_of_glass_panel(self):
        source = (JAVA / "GlassDrawable.java").read_text(encoding="utf-8")
        branch = source.index("if (owner.getId() == R.id.islamicShelf)")
        glass_preferences = source.index('getSharedPreferences("wallpaper_prefs"')
        self.assertLess(branch, glass_preferences)
        self.assertIn("new RadialGradient(", source)
        self.assertIn("private LinearGradient floatingGradientShader;", source)
        self.assertIn("floatingGradientBounds.equals(rect)", source)
        self.assertIn("floatingGradient.setShader(floatingGradientShader)", source)
        self.assertIn("new int[]{0x1A000000, 0x10000000, 0x06000000, 0x00000000}", source)
        self.assertIn("new float[]{0f, 0.32f, 0.68f, 1f}", source)
        self.assertIn("ovalMatrix.setScale(1f, 0.58f, gradientCenterX, gradientCenterY)", source)
        floating_branch = source[branch:glass_preferences]
        self.assertIn("canvas.drawRect(rect, floatingGradient)", floating_branch)
        self.assertNotIn("fill.draw(", floating_branch)
        self.assertNotIn("drawRoundRect", floating_branch)
        self.assertIn("setLocalMatrix(ovalMatrix)", floating_branch)
        self.assertNotIn("rim.", floating_branch)

    def test_prayer_section_rule_is_visible_between_ayah_and_prayer_without_layout_shift(self):
        root = ET.parse(LAYOUT).getroot()
        divider = next(node for node in root.iter()
                       if node.get(ANDROID + "id") == "@+id/prayerSectionDivider")
        self.assertEqual(divider.get(ANDROID + "alpha"), "0.65")
        self.assertEqual(divider.get(ANDROID + "background"), "#66FFFFFF")
        self.assertEqual(divider.get(ANDROID + "layout_width"), "180dp")
        self.assertEqual(divider.get(ANDROID + "layout_height"), "1dp")
        self.assertEqual(divider.get(ANDROID + "layout_marginTop"), "6dp")
        self.assertEqual(divider.get(ANDROID + "layout_marginBottom"), "2dp")

    def test_target_text_typography_and_existing_soft_shadows_remain_locked(self):
        root = ET.parse(LAYOUT).getroot()
        nodes = {node.get(ANDROID + "id"): node for node in root.iter()
                 if node.get(ANDROID + "id")}
        expected = {
            "@+id/ayahLabel": ("@font/inter", "12sp", "#CCFFFFFF", "3"),
            "@+id/ayahArabic": ("@font/amiri_quran", "20sp", "@color/tv_ui_islamic_gold", "3"),
            "@+id/ayahText": ("@font/inter", "14sp", "#F2FFFFFF", "1.5"),
            "@+id/prayerHeader": ("@font/inter", "12sp", "#B3FFFFFF", "4"),
            "@+id/lblSubuh": ("@font/inter", "13sp", "#B3FFFFFF", "1.5"),
            "@+id/timeSubuh": ("@font/inter", "17sp", "#FFFFFFFF", "2"),
        }
        for view_id, (font, size, color, shadow) in expected.items():
            with self.subTest(view_id=view_id):
                node = nodes[view_id]
                self.assertEqual(node.get(ANDROID + "fontFamily"), font)
                self.assertEqual(node.get(ANDROID + "textSize"), size)
                self.assertEqual(node.get(ANDROID + "textColor"), color)
                self.assertEqual(node.get(ANDROID + "shadowRadius"), shadow)

    def test_all_six_prayer_labels_and_times_remain_present(self):
        root = ET.parse(LAYOUT).getroot()
        ids = {node.get(ANDROID + "id") for node in root.iter()}
        for name in ("Subuh", "Syuruk", "Zohor", "Asar", "Maghrib", "Isyak"):
            with self.subTest(prayer=name):
                self.assertIn(f"@+id/lbl{name}", ids)
                self.assertIn(f"@+id/time{name}", ids)

    def test_ayah_focus_target_and_prayer_interaction_views_remain_present(self):
        root = ET.parse(LAYOUT).getroot()
        ids = {node.get(ANDROID + "id") for node in root.iter()}
        self.assertIn("@+id/ayahBlock", ids)
        self.assertIn("@+id/ayahOpenHint", ids)
        self.assertIn("@+id/prayerTable", ids)
        main = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        focus = main[main.index("prayerTableView.setBackground("):main.index("prayerTableView.setOnFocusChangeListener(")]
        self.assertIn("0x00000000, TvUiTokens.FOCUS_BORDER", focus)
        self.assertIn("@+id/islamicShelf", ids)
        hint = next(node for node in root.iter() if node.get(ANDROID + "id") == "@+id/ayahOpenHint")
        self.assertEqual(hint.get(ANDROID + "textSize"), "9sp")
        self.assertEqual(hint.get(ANDROID + "paddingStart"), "8dp")
        self.assertEqual(hint.get(ANDROID + "paddingEnd"), "8dp")
        self.assertEqual(hint.get(ANDROID + "paddingTop"), "4dp")
        self.assertEqual(hint.get(ANDROID + "paddingBottom"), "4dp")


if __name__ == "__main__":
    unittest.main()
