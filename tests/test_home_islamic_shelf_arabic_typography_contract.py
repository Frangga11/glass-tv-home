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

    def test_arabic_text_uses_warm_yellow_orange(self):
        arabic = self.views["ayahArabic"]
        color = arabic.attrib["{http://schemas.android.com/apk/res/android}textColor"]
        self.assertEqual(color.upper(), "#FFFFC45A")

    def test_arabic_two_line_spacing_is_tight_but_not_collapsed(self):
        arabic = self.views["ayahArabic"]
        extra = arabic.attrib["{http://schemas.android.com/apk/res/android}lineSpacingExtra"]
        multiplier = arabic.attrib["{http://schemas.android.com/apk/res/android}lineSpacingMultiplier"]
        self.assertEqual(extra, "-1dp")
        self.assertEqual(multiplier, "0.94")
        self.assertEqual(arabic.attrib["{http://schemas.android.com/apk/res/android}maxLines"], "2")
        self.assertEqual(arabic.attrib["{http://schemas.android.com/apk/res/android}includeFontPadding"], "true")

    def test_arabic_text_has_soft_depth_shadow(self):
        arabic = self.views["ayahArabic"]
        ns = "{http://schemas.android.com/apk/res/android}"
        self.assertEqual(arabic.attrib[ns + "shadowColor"].upper(), "#CC3A1800")
        self.assertEqual(arabic.attrib[ns + "shadowDx"], "0")
        self.assertEqual(arabic.attrib[ns + "shadowDy"], "2")
        self.assertEqual(arabic.attrib[ns + "shadowRadius"], "3")

    def test_translation_and_other_shelf_layout_are_not_restyled(self):
        arabic_start = self.source.index('android:id="@+id/ayahArabic"')
        translation_start = self.source.index('android:id="@+id/ayahText"', arabic_start)
        arabic_block = self.source[arabic_start:translation_start]
        self.assertIn('android:textColor="#FFFFC45A"', arabic_block)
        self.assertNotIn('android:textColor="#FFFFC45A"', self.source[translation_start:])


if __name__ == "__main__":
    unittest.main()
