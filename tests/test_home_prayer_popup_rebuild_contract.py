from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")


def method_body(source, start, end):
    a = source.index(start)
    b = source.index(end, a)
    return source[a:b]


class HomePrayerPopupRebuildContractTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.popup = method_body(
            MAIN,
            "private void showPrayerPopup()",
            "/** Refreshes only the manually opened in-launcher nightstand"
        )
        cls.overlay = method_body(
            cls.popup,
            "private void showPrayerPopupOverlay(",
            "private void requestPrayerPopupInitialFocus("
        )

    def test_popup_is_dedicated_and_does_not_rewire_shared_inputs_or_menu(self):
        self.assertNotIn("R.layout.popup_inputs", self.popup)
        self.assertNotIn("R.layout.item_input", self.popup)
        self.assertNotIn("showMenu(", self.popup)
        self.assertNotIn("ScrollView(", self.popup)
        self.assertIn("new GlassDrawable(panel, 20 * density, density, glider, 0.5f, true)", self.popup)

    def test_existing_prayer_schedule_source_and_six_cells_are_preserved(self):
        self.assertIn("PrayerTimes.getFullSchedule(this)", self.popup)
        self.assertIn("item.isNext", self.popup)
        self.assertIn("for (int pair = 0; pair < schedule.size(); pair += 2)", self.popup)
        self.assertIn("scheduleFocus.add(card)", self.popup)
        self.assertIn("focusOrder.addAll(scheduleFocus)", self.popup)

    def test_azan_preferences_schedule_and_existing_test_activity_are_reused(self):
        for token in (
            "Azan.isMasterEnabled(this)",
            "Azan.setMasterEnabled(MainActivity.this, enabled)",
            "Azan.schedule(MainActivity.this, PrayerTimes.getActiveZone(MainActivity.this))",
            "Azan.setSoundChoice(MainActivity.this, next)",
            "Azan.fetch(MainActivity.this, next)",
            "new Intent(MainActivity.this, AzanActivity.class)",
            'azanIntent.putExtra("prayer", 4)',
        ):
            self.assertIn(token, self.popup)

    def test_schedule_cells_are_read_only_and_azan_has_only_the_three_requested_controls(self):
        self.assertIn("Timetable cells are read-only", self.popup)
        self.assertIn("card.addView(name)", self.popup)
        self.assertIn("card.addView(time)", self.popup)
        self.assertNotIn("Azan.on(this, prayerIndex)", self.popup)
        self.assertNotIn("Azan.set(MainActivity.this, selectedPrayer, enabled)", self.popup)
        self.assertIn("Azan.isMasterEnabled(this)", self.popup)
        self.assertIn("Suara Azan", self.popup)
        self.assertIn("Test Sound", self.popup)

    def test_all_five_existing_sound_choices_remain_available(self):
        for token in (
            "Azan.SOUND_MAKKAH", "Azan.SOUND_MADINAH", "Azan.SOUND_KLCC",
            "Azan.SOUND_CHIME", "Azan.SOUND_SILENT"
        ):
            self.assertIn(token, self.popup)

    def test_compact_layout_is_measured_against_tv_safe_area_before_placement(self):
        self.assertIn("TvResponsiveLayout.safeInsetPx(root.getWidth())", self.popup)
        self.assertIn("TvResponsiveLayout.safeInsetPx(root.getHeight())", self.popup)
        self.assertIn("panel.measure(", self.popup)
        self.assertLess(
            self.popup.index("panel.setBackground(new GlassDrawable"),
            self.popup.index("panel.measure("),
        )
        self.assertIn("View.MeasureSpec.AT_MOST", self.popup)
        self.assertIn("panel.getMeasuredHeight()", self.popup)
        self.assertIn("showPrayerPopupOverlay(panel, rows, anchor, width, height, focusOrder, safeX, safeY)", self.popup)

    def test_popup_is_right_centered_and_does_not_scroll_or_glide_content(self):
        self.assertIn("Gravity.RIGHT | Gravity.CENTER_VERTICAL", self.popup)
        self.assertIn("lp.rightMargin = safeX + (int) (26 * density)", self.popup)
        self.assertNotIn("glideToMenuRow(", self.popup)
        self.assertNotIn("ScrollView(", self.popup)

    def test_silver_glass_is_preserved_without_cyan_content_tokens(self):
        self.assertIn("TvUiTokens.TEXT_PRIMARY", self.popup)
        self.assertIn("TvUiTokens.TEXT_SECONDARY", self.popup)
        self.assertIn("TvUiTokens.ISLAMIC_GOLD", self.popup)
        self.assertNotIn("TvUiTokens.ACCENT_CYAN", self.popup)
        self.assertIn("shape.setColor(selected ? 0x25FFFFFF : 0x13FFFFFF)", self.popup)

    def test_focus_is_owned_by_popup_and_initial_focus_is_bounded(self):
        for token in (
            "KEYCODE_DPAD_UP", "KEYCODE_DPAD_DOWN", "KEYCODE_DPAD_LEFT", "KEYCODE_DPAD_RIGHT",
            "KEYCODE_DPAD_CENTER", "KEYCODE_ENTER", "KEYCODE_BACK", "KEYCODE_ESCAPE",
            "findPrayerPopupDirectionalFocus(v, focusOrder, keyCode)",
            "closeMenu(true)",
            "requestPrayerPopupInitialFocus(panel, focusOrder, 8)",
            "Initial focus could not be committed; refusing Home-header fallback",
        ):
            self.assertIn(token, self.popup)
        self.assertNotIn("focusTopBarFallback()", self.popup)
        self.assertIn("case KeyEvent.KEYCODE_ESCAPE:", self.popup)
        self.assertIn("default:\n                            return false;", self.popup)

    def test_backdrop_and_glider_companion_are_popup_local(self):
        self.assertIn("Backdrop.watch(panel)", self.popup)
        self.assertIn("glider.addCompanion(panel)", self.popup)
        self.assertIn("glider.removeCompanion(panel)", MAIN)

    def test_azan_overlay_closes_popup_and_restores_its_original_home_anchor(self):
        self.assertIn("private View prayerPopupReturnAnchor;", MAIN)
        self.assertIn("private boolean prayerPopupRestoreFocusPending;", MAIN)
        self.assertIn("if (openMenu != null && prayerPopupReturnAnchor != null)", MAIN)
        self.assertIn("prayerPopupRestoreFocusPending = true;", MAIN)
        self.assertIn("returnAnchor.requestFocus();", MAIN)
        self.assertIn("prayerPopupReturnAnchor = anchor;", self.popup)
        self.assertIn("prayerPopupRestoreFocusPending = false;", self.popup)


if __name__ == "__main__":
    unittest.main()
