from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
LAYOUT = (ROOT / "launcher/app/src/main/res/layout/activity_main.xml").read_text(encoding="utf-8")


class HomeAyahPillFocusAlignContractTest(unittest.TestCase):
    def test_full_ayah_preview_is_not_a_focus_target(self):
        self.assertIn("ayahBlock.setFocusable(false);", MAIN)
        self.assertIn("ayahBlock.setClickable(false);", MAIN)
        self.assertNotIn("ayahBlock.requestFocus()", MAIN)

    def test_complete_ayah_capsule_is_the_focus_and_open_target(self):
        self.assertIn("ayahOpenHint.setFocusable(true);", MAIN)
        self.assertIn("ayahOpenHint.setOnClickListener(v -> showDailyAyahDialog());", MAIN)
        self.assertIn("v.performClick();", MAIN)
        self.assertIn("ayahOpenHint != null && ayahOpenHint.isShown() && ayahOpenHint.requestFocus()", MAIN)

    def test_translation_is_right_aligned(self):
        self.assertIn("ayahText.setGravity(Gravity.END);", MAIN)
        self.assertIn('android:gravity="end"', LAYOUT)

    def test_capsule_to_prayer_heading_gap_is_ten_dp(self):
        self.assertIn('android:id="@+id/prayerSectionDivider"', LAYOUT)
        self.assertIn('android:layout_marginTop="1dp"', LAYOUT)
        self.assertIn('android:translationY="-6dp"', LAYOUT)
