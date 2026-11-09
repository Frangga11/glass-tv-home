from pathlib import Path
import re
import unittest
import xml.etree.ElementTree as ET


ROOT = Path(__file__).resolve().parents[1]
LAYOUT = ROOT / "launcher/app/src/main/res/layout/activity_main.xml"


class HomeIslamicShelfArabicTypographyContract(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.source = LAYOUT.read_text(encoding="utf-8")
        cls.root = ET.fromstring(cls.source)
        cls.views = {
            node.attrib.get("{http://schemas.android.com/apk/res/android}id", "").replace("@+id/", ""): node
            for node in cls.root.iter("TextView")
        }

    def test_arabic_text_uses_protected_islamic_gold(self):
        arabic = self.views["ayahArabic"]
        color = arabic.attrib["{http://schemas.android.com/apk/res/android}textColor"]
        self.assertEqual(color, "@color/tv_ui_islamic_gold")

    def test_arabic_two_line_spacing_is_tight_but_not_collapsed(self):
        arabic = self.views["ayahArabic"]
        extra = arabic.attrib["{http://schemas.android.com/apk/res/android}lineSpacingExtra"]
        multiplier = arabic.attrib["{http://schemas.android.com/apk/res/android}lineSpacingMultiplier"]
        self.assertEqual(extra, "-2dp")
        self.assertEqual(multiplier, "0.92")
        self.assertEqual(arabic.attrib["{http://schemas.android.com/apk/res/android}maxLines"], "2")
        self.assertEqual(arabic.attrib["{http://schemas.android.com/apk/res/android}includeFontPadding"], "true")

    def test_home_ayah_views_use_a_shared_fixed_line_box_at_runtime(self):
        main = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
        self.assertIn("arabicParams.width = islamicMaxWidth", main)
        self.assertIn("ayahArabic.setLayoutParams(arabicParams)", main)
        self.assertIn("translationParams.width = islamicMaxWidth", main)
        self.assertIn("ayahText.setLayoutParams(translationParams)", main)
        self.assertIn("ayahArabic.setGravity(Gravity.RIGHT | Gravity.TOP)", main)
        self.assertIn("ayahArabic.setTextDirection(View.TEXT_DIRECTION_RTL)", main)

    def test_arabic_alignment_separates_rtl_text_from_physical_right_edge(self):
        arabic = self.views["ayahArabic"]
        ns = "{http://schemas.android.com/apk/res/android}"
        self.assertEqual(arabic.attrib[ns + "layoutDirection"], "ltr")
        self.assertEqual(arabic.attrib[ns + "gravity"], "end")
        main = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
        self.assertIn("ayahArabic.setLayoutDirection(View.LAYOUT_DIRECTION_LTR)", main)
        self.assertIn("ayahArabic.setTextDirection(View.TEXT_DIRECTION_RTL)", main)
        self.assertIn("ayahArabic.setGravity(Gravity.RIGHT | Gravity.TOP)", main)
        self.assertIn("ayahArabic.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY)", main)
        self.assertNotIn("ayahArabic.setGravity(Gravity.END)", main)

    def test_arabic_text_has_soft_depth_shadow(self):
        arabic = self.views["ayahArabic"]
        ns = "{http://schemas.android.com/apk/res/android}"
        self.assertEqual(arabic.attrib[ns + "shadowColor"].upper(), "#CC000000")
        self.assertEqual(arabic.attrib[ns + "shadowDx"], "0")
        self.assertEqual(arabic.attrib[ns + "shadowDy"], "2")
        self.assertEqual(arabic.attrib[ns + "shadowRadius"], "3")

    def test_translation_and_other_shelf_layout_are_not_restyled(self):
        arabic_start = self.source.index('android:id="@+id/ayahArabic"')
        translation_start = self.source.index('android:id="@+id/ayahText"', arabic_start)
        arabic_block = self.source[arabic_start:translation_start]
        self.assertIn('android:textColor="@color/tv_ui_islamic_gold"', arabic_block)
        self.assertNotIn('android:textColor="@color/tv_ui_islamic_gold"', self.source[translation_start:])


if __name__ == "__main__":
    unittest.main()
