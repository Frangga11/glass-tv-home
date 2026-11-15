from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
START = MAIN.index("private void showDailyAyahDialog()")
END = MAIN.index("private void updateDateAndPeriod()", START)
DIALOG = MAIN[START:END]


class DailyAyahReaderPolishContractTest(unittest.TestCase):
    def test_glass_surface_is_more_legible_without_removing_glass_identity(self):
        self.assertIn("new int[]{0xF02B3B50, 0xE91B2638, 0xF0222D40}", DIALOG)
        self.assertIn("0x99FFFFFF", DIALOG)
        self.assertIn("TvDialogStyle.applyCustom(dialog, this, widthDp, heightDp, 0.48f)", DIALOG)

    def test_reading_typography_has_more_room(self):
        self.assertIn("? 38f : 33f", DIALOG)
        self.assertIn("translation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)", DIALOG)
        self.assertIn("meaningLabelLp.topMargin = Math.round(22 * density)", DIALOG)

    def test_navigation_hint_is_fixed_outside_scroll_content(self):
        scroll_add = DIALOG.index("panel.addView(scroll")
        hint_add = DIALOG.index("panel.addView(footerHint")
        close_add = DIALOG.index("panel.addView(closeAction")
        self.assertLess(scroll_add, hint_add)
        self.assertLess(hint_add, close_add)
        self.assertNotIn("body.addView(footerHint", DIALOG)

    def test_reader_keeps_bounded_scroll_and_dpad_footer_navigation(self):
        self.assertIn("scroll.canScrollVertically(1)", DIALOG)
        self.assertIn("scroll.canScrollVertically(-1)", DIALOG)
        self.assertIn("closeAction.requestFocus()", DIALOG)
        self.assertIn("close.requestFocus()", DIALOG)
        self.assertIn("scroll.requestFocus()", DIALOG)


if __name__ == "__main__":
    unittest.main()
