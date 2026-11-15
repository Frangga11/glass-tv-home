from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
AZAN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/AzanActivity.java").read_text(encoding="utf-8")
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")


class AzanTestOverlayCompactGlassContractTest(unittest.TestCase):
    def test_overlay_uses_the_approved_prayer_popup_glass_material(self):
        self.assertIn(
            "card.setBackground(new GlassDrawable(card, 20 * density, density, null, 0.5f));",
            AZAN,
        )
        self.assertIn("Backdrop.watch(card)", AZAN)
        self.assertIn(
            "panel.setBackground(new GlassDrawable(panel, 20 * density, density, null, 0.5f));",
            MAIN,
        )
        self.assertNotIn("glass.setColor(0xD936414B)", AZAN)
        self.assertNotIn("setBackgroundBlurRadius", AZAN)

    def test_overlay_is_reduced_proportionally_to_approximately_fifteen_percent(self):
        self.assertIn("private static final float CARD_SCALE = 0.806f;", AZAN)
        self.assertIn("t.setTextSize(sp * CARD_SCALE)", AZAN)
        self.assertIn("Math.round(v * density * CARD_SCALE)", AZAN)
        for token in (
            "card.setPadding(cardDp(30), cardDp(20), cardDp(34), cardDp(20))",
            "cardDp(70)",
            "llp.leftMargin = cardDp(26)",
            "llp.rightMargin = cardDp(26)",
            "nameRow.setPadding(0, cardDp(2), 0, cardDp(2))",
            "time.setPadding(cardDp(14), 0, 0, cardDp(3))",
        ):
            self.assertIn(token, AZAN)

    def test_popup_position_and_content_order_are_preserved(self):
        self.assertIn("w.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)", AZAN)
        self.assertIn("lp.y = dp(40)", AZAN)
        self.assertIn('text("ٱللَّهُ أَكْبَرُ", amiri, 40', AZAN)
        self.assertIn("card.addView(takbir)", AZAN)
        self.assertIn("card.addView(line, llp)", AZAN)
        self.assertIn("card.addView(words)", AZAN)
        self.assertLess(AZAN.index("card.addView(takbir)"), AZAN.index("card.addView(line, llp)"))
        self.assertLess(AZAN.index("card.addView(line, llp)"), AZAN.index("card.addView(words)"))
        self.assertIn("TextView name = text(pNames[validPrayerIdx], outfit, 28", AZAN)
        self.assertIn("TextView time = text(when, outfit, 20", AZAN)
        self.assertIn("hint = text(AppLanguage.strAzanPopupHint(this), outfit, 12", AZAN)

    def test_focus_dismissal_and_audio_playback_lifecycle_are_unchanged(self):
        for token in (
            "card.setFocusable(true)",
            "card.setClickable(true)",
            "card.setOnClickListener(v -> finish())",
            "card.requestFocus()",
            "focusOutline.setStroke",
            "playChime()",
            "play(file, soundChoice)",
            "player.setOnCompletionListener(mp -> finish())",
            "KEYCODE_DPAD_CENTER",
            "KEYCODE_BACK",
            "audio.abandonAudioFocusRequest(focus)",
        ):
            self.assertIn(token, AZAN)


if __name__ == "__main__":
    unittest.main()
