"""Regression contracts for popup-only translucent glass and protected UI behavior."""
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher/app/src/main/java/com/gathin/tvhome"


class PopupFamilyGlassMaterialContractTest(unittest.TestCase):
    def test_settings_shell_changes_material_only_and_keeps_white_text(self):
        settings = (JAVA / "ModernSettingsDialog.java").read_text(encoding="utf-8")
        tokens = (JAVA / "TvUiTokens.java").read_text(encoding="utf-8")
        self.assertIn("UniversalGlassMaterial.panel(activity, root, density, 24f", settings)
        self.assertIn("TvUiTokens.TEXT_PRIMARY", settings)
        self.assertIn("static final int TEXT_PRIMARY = 0xFFFFFFFF", tokens)

    def test_quick_settings_and_wifi_keep_original_white_typography(self):
        quick = (JAVA / "QuickSettingsDialog.java").read_text(encoding="utf-8")
        wifi = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("PANEL_TEXT_PRIMARY = TvUiTokens.TEXT_PRIMARY", quick)
        self.assertIn("label.setTextColor(PANEL_TEXT_PRIMARY)", quick)
        self.assertIn("view.setTextColor(TvUiTokens.TEXT_PRIMARY)", wifi)
        self.assertNotIn("TvFocusIndicator", wifi)
        self.assertIn("view.setBackground(UniversalGlassMaterial.controlIdle(activity, density,", wifi)
        self.assertIn("focusGlider.setGlassRimStrokeWidth(2f * density)", wifi)
        self.assertIn("focusGlider.setSingleGlassRim(true)", wifi)
        self.assertIn("new GliderFocusDrawable(view, focusGlider, 10f * density)", wifi)
        self.assertIn("view.setSelected(hasFocus)", wifi)

    def test_popup_material_is_translucent_dark_silver_glass_not_opaque_matte(self):
        material = (JAVA / "UniversalGlassMaterial.java").read_text(encoding="utf-8")
        drawable = (JAVA / "UniversalGlassDrawable.java").read_text(encoding="utf-8")
        self.assertIn("PANEL_SHADE = 0x78151D25", material)
        self.assertIn("new UniversalGlassDrawable(owner, density, radiusDp, false, true)", material)
        for token in ("0x28FFFFFF", "0x14FFFFFF", "0x0AFFFFFF", "0x1E111A22",
                      "0xDFFFFFFF", "0x55FFFFFF", "0x38FFFFFF"):
            self.assertIn(token, drawable)
        self.assertIn("silverCyanIdentity || brightSilverPopup", drawable)
        self.assertIn("backdropFill.draw(canvas, owner, area, radius, 1f)", drawable)
        self.assertIn("Backdrop.watch(owner)", material)
        self.assertIn("Backdrop.watch(pointer)", (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8"))
        self.assertIn("getBoolean(\"glass_effects_enabled\", true)", material)

    def test_wifi_and_quick_settings_share_panel_material_without_layout_or_focus_rewire(self):
        wifi = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        quick = (JAVA / "QuickSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("UniversalGlassMaterial.panel(activity, content, density, TvUiTokens.POPUP_RADIUS_DP,", wifi)
        self.assertIn("UniversalGlassMaterial.panel(activity, content, density, TvUiTokens.POPUP_RADIUS_DP,", quick)
        self.assertIn("new GliderFocusDrawable(control, glider, cornerRadiusDp * density)", quick)
        self.assertIn("QuickSettingsFocusGraph.connect(controls)", quick)
        self.assertIn("QuickSettingsFocusGraph", quick)
        self.assertIn("UniversalGlassMaterial.drawPointer(activity, pointer, canvas, triangle, pointerEdges", wifi)

    def test_settings_sidebar_uses_one_bounded_focus_cursor_only(self):
        settings = (JAVA / "ModernSettingsDialog.java").read_text(encoding="utf-8")
        cursor = (JAVA / "SettingsFocusCursorDrawable.java").read_text(encoding="utf-8")
        tokens = (JAVA / "TvUiTokens.java").read_text(encoding="utf-8")
        self.assertIn("new SettingsFocusCursorDrawable(density, TvUiTokens.NAV_ROW_RADIUS_DP)", settings)
        self.assertIn("new SettingsFocusCursorDrawable(density, TvUiTokens.CARD_RADIUS_DP)", settings)
        selection_block = settings[settings.index("Runnable updateSidebarSelection"):settings.index("View[] activeSidebarView")]
        self.assertNotIn("pcBg.setStroke", selection_block)
        self.assertNotIn("rbg.setStroke", selection_block)
        self.assertNotIn("SettingsNavFocusDrawable", settings)
        self.assertNotIn("bg_tv_focus_outline", settings)
        self.assertFalse((JAVA / "SettingsNavFocusDrawable.java").exists())
        self.assertFalse((ROOT / "launcher/app/src/main/res/drawable/bg_tv_focus_outline.xml").exists())
        self.assertIn("NAV_ROW_RADIUS_DP = 14f", tokens)
        self.assertIn("NAV_ROW_HEIGHT_DP = 56f", tokens)
        self.assertIn("NAV_ROW_BOTTOM_MARGIN_DP = 4f", tokens)
        self.assertIn("new GlassRim(1f * density", cursor)
        self.assertIn("silverCyanRim.paint.setStrokeWidth(1f * density)", cursor)
        self.assertIn("b.left + halfStroke", cursor)
        self.assertIn("radiusPx - halfStroke", cursor)
        self.assertIn("innerShape.inset(1.35f * density", cursor)


    def test_wifi_and_home_use_one_glider_cursor_without_legacy_overlap(self):
        wifi = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        main = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        self.assertIn("installFocusCursor();", wifi[wifi.index("dialog.setOnShowListener"):wifi.index("private void applyAnchoredPopup")])
        self.assertNotIn("screen.installFocusCursor()", wifi)
        self.assertEqual(wifi.count("installFocusCursor();"), 1)
        self.assertIn("private void installFocusCursor()", wifi)
        self.assertIn("private boolean isWifiFocusTarget(View view)", wifi)
        self.assertIn("private void moveHomeCapsuleCursor(View capsule)", main)
        self.assertIn("ayahOpenHint, homeCapsuleGlider, 6f * density)", main)
        self.assertIn("prayerHeaderView, homeCapsuleGlider, 8f * density)", main)
        self.assertIn("homeCapsuleGlider.setFocusStrokeWidth(2f * density)", main)
        self.assertIn("homeCapsuleGlider.setCompanionOnly(true)", main)
        self.assertIn("new GliderFocusDrawable(pill, homeCapsuleGlider, 20f * density)", main)
        self.assertNotIn("glider.addCompanion(pill)", main)
        self.assertNotIn("TvFocusIndicator.createRoundedStateBackground(\\n                        density, 6f", main)
        self.assertNotIn("TvFocusIndicator.createRoundedStateBackground(\\n                    density, 8f", main)
        self.assertNotIn("glider.addCompanion(navCapsule)", main)
        self.assertIn("private void ensureHomeCapsuleGlider()", main)
        self.assertIn("private boolean isHomeCapsuleCursorTarget(View view)", main)

    def test_home_dock_and_capsule_material_remain_separate(self):
        dock = (JAVA / "DockDecoration.java").read_text(encoding="utf-8")
        drawable = (JAVA / "UniversalGlassDrawable.java").read_text(encoding="utf-8")
        self.assertIn("new UniversalGlassDrawable(density, 26f, true)", dock)
        self.assertIn("if (silverCyanIdentity)", drawable)
        self.assertIn("0xA650DDF2", drawable)


if __name__ == "__main__":
    unittest.main()
