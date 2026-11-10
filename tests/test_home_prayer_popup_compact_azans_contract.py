from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")


def method_body(source, signature, next_signature):
    start = source.index(signature)
    end = source.index(next_signature, start)
    return source[start:end]


class HomePrayerPopupCompactAzansContractTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.popup = method_body(
            MAIN,
            "private void showPrayerPopup()",
            "private TextView createPrayerPopupText("
        )
        cls.overlay = method_body(
            MAIN,
            "private void showPrayerPopupOverlay(",
            "/** Refreshes only the manually opened in-launcher nightstand"
        )

    def test_popup_does_not_reuse_shared_inputs_or_item_layouts(self):
        self.assertNotIn("R.layout.popup_inputs", self.popup)
        self.assertNotIn("R.layout.item_input", self.popup)
        self.assertNotIn("showAzanSettingsDialog()", self.popup)
        self.assertNotIn("ScrollView", self.popup)

    def test_schedule_is_read_only_source_and_next_prayer_uses_existing_flag(self):
        self.assertIn("PrayerTimes.getFullSchedule(this)", self.popup)
        self.assertIn("item.isNext", self.popup)
        self.assertIn("TvUiTokens.ISLAMIC_GOLD", self.popup)
        self.assertIn("final int prayerIndex = (i == 1) ? -1 : i;", self.popup)

    def test_per_prayer_and_master_switches_reuse_existing_azan_preferences(self):
        self.assertIn("Azan.on(this, prayerIndex)", self.popup)
        self.assertIn("Azan.set(MainActivity.this, selectedPrayer, enabled)", self.popup)
        self.assertIn("Azan.isMasterEnabled(this)", self.popup)
        self.assertIn("Azan.setMasterEnabled(MainActivity.this, enabled)", self.popup)
        self.assertIn("Azan.schedule(MainActivity.this, zoneCode)", self.popup)

    def test_all_existing_sound_choices_are_available_inline(self):
        for key in (
            "Azan.SOUND_MAKKAH", "Azan.SOUND_MADINAH", "Azan.SOUND_KLCC",
            "Azan.SOUND_CHIME", "Azan.SOUND_SILENT"
        ):
            self.assertIn(key, self.popup)
        self.assertIn("Azan.setSoundChoice(MainActivity.this, next)", self.popup)
        self.assertIn("Azan.fetch(MainActivity.this, next)", self.popup)

    def test_existing_azan_test_activity_is_reused(self):
        self.assertIn("new Intent(MainActivity.this, AzanActivity.class)", self.popup)
        self.assertIn('azanIntent.putExtra("prayer", 4)', self.popup)

    def test_home_popup_keeps_existing_glass_material_and_centered_single_frame(self):
        self.assertIn("new GlassDrawable(panel, 20 * density, density, glider, 0.5f, true)", self.overlay)
        self.assertIn("Backdrop.watch(panel)", self.overlay)
        self.assertIn("glider.addCompanion(panel)", self.overlay)
        self.assertIn("new FrameLayout.LayoutParams(width, height, Gravity.CENTER)", self.overlay)
        self.assertNotIn("R.layout.popup_inputs", self.overlay)
        self.assertNotIn("ScrollView", self.overlay)

    def test_existing_shared_menu_and_glass_implementations_are_not_replaced(self):
        show_menu = MAIN[MAIN.index("private void showMenu("):MAIN.index("private void glideToMenuRow(")]
        self.assertIn("new GlassDrawable(panel, 20 * density, density, glider, 0.5f, true)", show_menu)
        self.assertIn("private void showPrayerPopupOverlay(", MAIN)
        self.assertIn("private void showInputs()", MAIN)


if __name__ == "__main__":
    unittest.main()
