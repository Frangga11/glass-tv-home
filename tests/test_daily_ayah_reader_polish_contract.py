from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
START = MAIN.index("private void showDailyAyahDialog()")
END = MAIN.index("private void updateDateAndPeriod()", START)
DIALOG = MAIN[START:END]


class DailyAyahReaderPolishContractTest(unittest.TestCase):
    def test_reader_uses_prayer_popup_glass_material(self):
        self.assertIn("new GlassDrawable(panel, 20 * density, density, null, 0.5f)", DIALOG)
        self.assertIn("Backdrop.watch(panel);", DIALOG)
        self.assertIn("TvDialogStyle.applyCustom(dialog, this, panelWidthDp, panelHeightDp, 0.48f)", DIALOG)

    def test_panel_height_is_capped_at_forty_percent_of_display(self):
        self.assertIn("screenHeightPx * 0.40f / density", DIALOG)
        self.assertIn("panelHeightDp", DIALOG)
        self.assertNotIn("0.68f", DIALOG)

    def test_arabic_is_gold_right_aligned_and_adaptive(self):
        self.assertIn("arabic.setTextColor(TvUiTokens.ISLAMIC_GOLD);", DIALOG)
        self.assertIn("arabic.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);", DIALOG)
        self.assertIn("arabic.setAutoSizeTextTypeUniformWithConfiguration(", DIALOG)
        self.assertIn("R.font.amiri_quran", DIALOG)
        self.assertIn("arabic.setMaxLines(2);", DIALOG)

    def test_translation_is_centered_and_adaptive(self):
        self.assertIn("translation.setGravity(Gravity.CENTER);", DIALOG)
        self.assertIn("translation.setAutoSizeTextTypeUniformWithConfiguration(", DIALOG)
        self.assertIn("translation.setMaxLines(2);", DIALOG)

    def test_reader_has_no_vertical_scroll_container_or_scroll_navigation(self):
        self.assertNotIn("new ScrollView(", DIALOG)
        self.assertNotIn("scroll.canScrollVertically", DIALOG)
        self.assertNotIn("smoothScrollBy", DIALOG)

    def test_header_body_and_footer_have_separate_fixed_regions(self):
        self.assertIn("panel.addView(header", DIALOG)
        self.assertIn("panel.addView(body", DIALOG)
        self.assertIn("panel.addView(footer", DIALOG)
        self.assertIn("Math.round(36 * density)", DIALOG)
        self.assertIn("Math.round(34 * density)", DIALOG)
        self.assertIn("closeAction.requestFocus()", DIALOG)

    def test_close_controls_and_origin_focus_restore_are_preserved(self):
        self.assertIn("close.setOnClickListener", DIALOG)
        self.assertIn("closeAction.setOnClickListener", DIALOG)
        self.assertIn("restoreFocusTo(originFocus)", DIALOG)
        self.assertIn("KEYCODE_BACK", DIALOG)


if __name__ == "__main__":
    unittest.main()
