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
        self.assertIn("new GlassDrawable(panel, 18 * density, density, glider, 0.5f, true)", self.popup)

    def test_existing_prayer_schedule_source_and_six_cells_are_preserved(self):
        self.assertIn("PrayerTimes.getFullSchedule(this)", self.popup)
        self.assertIn("private String prayerPopup24Hour(String value)", MAIN)
        self.assertIn('"HH:mm"', MAIN)
        self.assertIn("R.drawable.ic_header_location", self.popup)
        self.assertIn("R.drawable.ic_header_mountain", self.popup)
        self.assertIn('"800 mdpl"', self.popup)
        self.assertNotIn("±800", self.popup)
        self.assertIn("item.isNext", self.popup)
        self.assertIn("for (int pair = 0; pair < schedule.size(); pair += 2)", self.popup)
        self.assertIn("scheduleFocus.add(card)", self.popup)
        self.assertIn("focusOrder.addAll(scheduleFocus)", self.popup)
        self.assertNotIn("focusOrder.add(header)", self.popup)
        self.assertIn("focusOrder.get(0)", self.popup)
        self.assertIn("prayerPopup24Hour(item.timeFormatted)", self.popup)

    def test_azan_preferences_schedule_and_existing_test_activity_are_reused(self):
        for token in (
            "Azan.isMasterEnabled(this)",
            "masterStatus.setText(enabled ? \"● Aktif\" : \"● Nonaktif\")",
            "masterStatus.setTextColor(enabled ? 0xFF58C77A : 0xFFB7BEC7)",
            "masterStatus.setBackground(prayerPopupStatusCapsuleBackground(enabled))",
            "masterStatus.setBackground(prayerPopupStatusCapsuleBackground(\n                Azan.isMasterEnabled(this)))",
            "private Drawable prayerPopupStatusCapsuleBackground(boolean enabled)",
            "capsule.setCornerRadius(999 * density)",
            "headingLp.topMargin = (int) (2 * density)",
            "headingLp.bottomMargin = (int) (2 * density)",
            "Azan.setMasterEnabled(MainActivity.this, enabled)",
            "Azan.schedule(MainActivity.this, PrayerTimes.getActiveZone(MainActivity.this))",
            "Azan.setSoundChoice(MainActivity.this, next)",
            "Azan.fetch(MainActivity.this, next)",
            "new Intent(MainActivity.this, AzanActivity.class)",
            'azanIntent.putExtra("prayer", 4)',
        ):
            self.assertIn(token, self.popup)

    def test_schedule_cells_are_read_only_and_azan_has_only_the_three_requested_controls(self):
        self.assertIn("card.addView(createPrayerPopupText(item.name", self.popup)
        self.assertIn("prayerPopup24Hour(item.timeFormatted)", self.popup)
        self.assertNotIn("Azan.on(this, prayerIndex)", self.popup)
        self.assertNotIn("Azan.set(MainActivity.this, selectedPrayer, enabled)", self.popup)
        self.assertIn("Azan.isMasterEnabled(this)", self.popup)
        self.assertIn("Suara Azan", self.popup)
        self.assertIn("Tes Suara", self.popup)

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
        self.assertIn("View.MeasureSpec.UNSPECIFIED", self.popup)
        self.assertIn("naturalContentHeight = panel.getMeasuredHeight()", self.popup)
        self.assertIn("Content exceeds Wi-Fi-to-Dock lane", self.popup)
        self.assertIn("Content fits Wi-Fi-to-Dock lane", self.popup)
        self.assertIn("panel.getMeasuredHeight()", self.popup)
        self.assertIn("showPrayerPopupOverlay(panel, rows, anchor, width, height, focusOrder, safeX, safeY)", self.popup)

    def test_popup_is_right_aligned_to_glass_dock_and_does_not_scroll_or_glide_content(self):
        self.assertIn("Gravity.TOP | Gravity.RIGHT", self.popup)
        self.assertIn("View prayerScheduleAnchor = findViewById(R.id.prayerTable)", self.popup)
        self.assertIn("prayerScheduleAnchor.getLocationOnScreen(scheduleOnScreen)", self.popup)
        self.assertIn("scheduleRightInRoot", self.popup)
        self.assertIn("root.getWidth() - scheduleRightInRoot", self.popup)
        self.assertIn("lp.rightMargin = Math.max(0, root.getWidth() - scheduleRightInRoot);", self.popup)
        self.assertNotIn("Math.max(safeX, root.getWidth() - scheduleRightInRoot)", self.popup)
        self.assertNotIn("wifiRightInRoot", self.popup)
        self.assertNotIn("glideToMenuRow(", self.popup)
        self.assertNotIn("ScrollView(", self.popup)

    def test_popup_top_uses_wifi_capsule_only_as_vertical_reference(self):
        self.assertIn("View topReference = findViewById(R.id.btnWifi)", self.popup)
        self.assertIn("topReference.getLocationOnScreen(topReferenceOnScreen)", self.popup)
        self.assertIn("topReference.getHeight()", self.popup)
        self.assertIn("referenceTop = topReferenceOnScreen[1]", self.popup)
        self.assertIn("Math.round(10 * density)", self.popup)
        self.assertIn("lp.topMargin = Math.max(safeY, referenceTop)", self.popup)
        self.assertIn("dockTopInRoot - Math.round(10 * density)", self.popup)
        self.assertIn("lp.height = Math.min(height, maxHeightBeforeDock)", self.popup)
        self.assertIn("bottomLimit - desiredTop", self.popup)
        self.assertNotIn("topReference.getWidth()", self.popup)

    def test_owner_refinement_header_next_prayer_schedule_cells_and_azan_status(self):
        self.assertIn("(int) (26 * density)", self.popup)
        self.assertIn("titleDivider", self.popup)
        self.assertIn("locationStack.setOrientation(LinearLayout.VERTICAL)", self.popup)
        self.assertIn("createPrayerPopupText(\"800 mdpl\", 9f", self.popup)
        self.assertIn("(int) (54 * density)", self.popup)
        self.assertIn('createPrayerPopupText("Waktu Sholat Berikutnya", 9f', self.popup)
        self.assertIn('"JADWAL HARI INI", 10.5f', self.popup)
        self.assertIn("addPrayerPopupDivider(rows)", self.popup)
        self.assertIn('"OK Pilih . BACK Kembali.", 8f', self.popup)
        self.assertIn("Math.round(29 * density)", self.popup)
        self.assertIn("\"● Aktif\" : \"● Nonaktif\"", self.popup)
        self.assertIn("0xFF58C77A", self.popup)
        self.assertIn("0xFFB7BEC7", self.popup)

    def test_owner_typography_and_proportional_capsule_dimensions_are_exact(self):
        for token in (
            'createPrayerPopupText("WAKTU SHOLAT", 13f',
            'PrayerTimes.getShortZoneName(this), 11f',
            'createPrayerPopupText("800 mdpl", 9f',
            '"Waktu Sholat Berikutnya", 9f',
            'nextPrayer.name : "Jadwal diperbarui", 15f',
            'prayerPopup24Hour(nextPrayer.timeFormatted) : "--:--", 14f',
            '"JADWAL HARI INI", 10.5f',
            'createPrayerPopupText(item.name, 10f',
            'prayerPopup24Hour(item.timeFormatted), 12f',
            'Azan.isMasterEnabled(this) ? "● Aktif" : "● Nonaktif", 8f',
            'createPrayerPopupText(title, 11f',
            'createPrayerPopupText(description, 8f',
            '(int) (28 * density)',
            '"OK Pilih . BACK Kembali.", 8f',
            '((TextView) testRow.getChildAt(1)).setTextSize(10f)',
            '(int) (28 * density)',
            'ViewGroup.LayoutParams.MATCH_PARENT, (int) (54 * density)',
            'ViewGroup.LayoutParams.MATCH_PARENT, (int) (28 * density)',
            'headingLp.topMargin = (int) (2 * density)',
            'headingLp.bottomMargin = (int) (2 * density)',
            'private Drawable prayerPopupStatusCapsuleBackground(boolean enabled)',
            'capsule.setCornerRadius(999 * density)',
            'masterStatus.setBackground(prayerPopupStatusCapsuleBackground(enabled))',
            'view.setIncludeFontPadding(false)',
            'view.setLineSpacing(0f, 1f)',
            '(int) (12 * density));',
        ):
            self.assertIn(token, self.popup)


    def test_owner_width_contract_uses_115dp_cells_as_the_single_source_of_truth(self):
        self.assertIn("final int scheduleCapsuleWidthPx = Math.round(115 * density);", self.popup)
        self.assertIn("final int scheduleGapPx = Math.round(6 * density);", self.popup)
        self.assertIn("final int panelPaddingPx = Math.round(12 * density);", self.popup)
        self.assertIn("final int innerCapsuleWidthPx = 2 * scheduleCapsuleWidthPx + scheduleGapPx;", self.popup)
        self.assertIn("final int targetPopupWidthPx = innerCapsuleWidthPx + 2 * panelPaddingPx;", self.popup)
        self.assertIn("int width = Math.min(targetPopupWidthPx, safeWidth);", self.popup)
        self.assertIn("scheduleCapsuleWidthPx, Math.round(29 * density)", self.popup)
        self.assertIn("ViewGroup.LayoutParams.MATCH_PARENT, (int) (54 * density)", self.popup)
        self.assertIn("ViewGroup.LayoutParams.MATCH_PARENT, (int) (28 * density)", self.popup)

    def test_owner_liquid_glass_is_popup_local_and_preserves_global_silver_tokens(self):
        self.assertIn("panel.setBackground(prayerPopupGlassBackground(panel, 18 * density))", self.popup)
        self.assertIn("private Drawable prayerPopupGlassBackground(View owner, float radius)", MAIN)
        self.assertIn("veil.setColor(0x36131926)", MAIN)
        for color in ("0xD9FFFFFF", "0xCC67E8F9", "0xB3C084FC", "0x80FDE047", "0x59818CF8", "0x8038BDF8"):
            self.assertIn(color, MAIN)
        self.assertIn("new Backdrop.Fill()", MAIN)
        self.assertIn("backdrop.draw(canvas, owner, bounds, radius, fade)", MAIN)
        self.assertIn("windowLight.getShader().setLocalMatrix(lightMatrix)", MAIN)
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
            "double crossLimit = horizontal",
            "if (cross > crossLimit) continue;",
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
        self.assertIn("returnAnchor.requestFocus()", MAIN)
        self.assertIn("activityResumed && openMenu == null", MAIN)
        self.assertIn("prayerPopupReturnAnchor = anchor;", self.popup)
        self.assertIn("prayerPopupRestoreFocusPending = false;", self.popup)


    def test_owner_visual_refinement_compacts_full_content_and_status_capsule(self):
        for token in (
            "(int) (4 * density)",
            "(int) (26 * density)",
            "(int) (54 * density)",
            "(int) (16 * density)",
            "headingLp.topMargin = (int) (2 * density)",
            "headingLp.bottomMargin = (int) (2 * density)",
            "Math.round(29 * density)",
            "(int) (31 * density)",
            "(int) (30 * density)",
            "(int) (18 * density)",
            "ViewGroup.LayoutParams.WRAP_CONTENT, (int) (18 * density)",
            "masterStatus.setPadding((int) (4 * density), 0,",
            "createPrayerPopupText(title, 11f",
            "createPrayerPopupText(description, 8f",
            "lp.setMargins((int) (7 * density), 0",
            "naturalContentHeight > maxContentHeight",
            "View.MeasureSpec.UNSPECIFIED",
        ):
            self.assertIn(token, self.popup)
        self.assertNotIn("View.MeasureSpec.AT_MOST", self.popup)


    def test_owner_focus_capsules_and_indonesian_copy_are_restored(self):
        for token in (
            'createPrayerPopupText("Waktu Sholat Berikutnya", 9f',
            '"Tes Suara", "Dengarkan contoh suara azan", "▶  UJI"',
            "nextCard.setBackground(prayerPopupCardBackground(false))",
            "masterRow.setBackground(prayerPopupCardBackground(false))",
            "soundRow.setBackground(prayerPopupCardBackground(false))",
            "testRow.setBackground(prayerPopupCardBackground(false))",
            "v.setBackground(prayerPopupFocusedBackground())",
            "v.setBackground((Drawable) v.getTag())",
        ):
            self.assertIn(token, self.popup)
        self.assertNotIn('createPrayerPopupText("Waktu Solat Berikutnya"', self.popup)
        self.assertNotIn('"Test Sound"', self.popup)



if __name__ == "__main__":
    unittest.main()
