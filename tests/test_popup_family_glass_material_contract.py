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

    def test_quick_settings_preserves_bright_white_typography_while_wifi_is_unchanged(self):
        quick = (JAVA / "QuickSettingsDialog.java").read_text(encoding="utf-8")
        wifi = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("PANEL_TEXT_PRIMARY = TvUiTokens.TEXT_PRIMARY", quick)
        self.assertIn("PANEL_TEXT_SECONDARY = TvUiTokens.TEXT_SECONDARY", quick)
        self.assertIn("label.setTextColor(PANEL_TEXT_PRIMARY)", quick)
        self.assertIn("label.setShadowLayer(0.85f * density, 0f, 0.35f * density, 0x55000000)", quick)
        self.assertIn("view.setTextColor(TvUiTokens.TEXT_PRIMARY)", wifi)
        self.assertIn("TvUiTokens.TEXT_PRIMARY, TvUiTokens.TEXT_PRIMARY", wifi)

    def test_popup_material_is_translucent_dark_silver_glass_not_opaque_matte(self):
        material = (JAVA / "UniversalGlassMaterial.java").read_text(encoding="utf-8")
        drawable = (JAVA / "UniversalGlassDrawable.java").read_text(encoding="utf-8")
        self.assertIn("PANEL_SHADE = 0x78151D25", material)
        self.assertIn("new UniversalGlassDrawable(owner, density, radiusDp, false, true)", material)
        self.assertIn("quickSettingsPanel(Context context", material)
        self.assertIn("new int[]{0x55343E48, 0x45303A44}", material)
        self.assertIn("new int[]{0x30FFFFFF, 0x00FFFFFF}", material)
        self.assertIn("new android.graphics.drawable.LayerDrawable(new Drawable[]{glass, specular})", material)
        self.assertIn("0x889EABB8", material)
        self.assertIn("new UniversalGlassDrawable(owner, density, radiusDp, false, true, true)", material)
        self.assertIn("popupVeil.setColor(cleanQuickSettings ? 0x24343E48 : 0x68151D25)", drawable)
        self.assertIn("new int[]{0x18FFFFFF, 0x0CFFFFFF, 0x04343E48, 0x18343E48}", drawable)
        self.assertIn("new int[]{0x00FFFFFF, 0x00FFFFFF, 0x00FFFFFF}", drawable)
        self.assertIn("new int[]{0xE8FFFFFF, 0x70FFFFFF, 0x25FFFFFF, 0xA8FFFFFF}", drawable)
        for token in ("0x28FFFFFF", "0x14FFFFFF", "0x0AFFFFFF", "0x1E111A22",
                      "0xDFFFFFFF", "0x55FFFFFF", "0x38FFFFFF"):
            self.assertIn(token, drawable)
        self.assertIn("silverCyanIdentity || brightSilverPopup", drawable)
        self.assertIn("if (!cleanQuickSettings)", drawable)
        self.assertIn("backdropFill.draw(canvas, owner, area, radius, 1f)", drawable)
        self.assertIn("if (cleanQuickSettings) return; // No broad white wash across the clear glass face.", drawable)
        self.assertIn("Backdrop.watch(owner)", material)
        self.assertIn("Backdrop.watch(pointer)", (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8"))
        self.assertIn("getBoolean(\"glass_effects_enabled\", true)", material)

    def test_quick_settings_gets_scoped_clean_material_without_rewiring_wifi_or_focus(self):
        wifi = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        quick = (JAVA / "QuickSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("UniversalGlassMaterial.panel(activity, content, density, TvUiTokens.POPUP_RADIUS_DP,", wifi)
        self.assertIn("UniversalGlassMaterial.quickSettingsPanel(activity, content, density, TvUiTokens.POPUP_RADIUS_DP,", quick)
        self.assertIn("new GliderFocusDrawable(control, glider, cornerRadiusDp * density)", quick)
        self.assertIn("QuickSettingsFocusGraph.connect(controls)", quick)
        self.assertIn("QuickSettingsFocusGraph", quick)
        self.assertIn("glider.setGlassRimStrokeWidth(1.25f * density)", quick)
        self.assertIn("glider.setPopupShadowStyle(14f * density, 3f * density, 0x58)", quick)
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

    def test_quick_settings_focus_tuning_is_instance_scoped_and_keeps_glider_defaults(self):
        glider = (JAVA / "Glider.java").read_text(encoding="utf-8")
        quick = (JAVA / "QuickSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("private float shadowBlur, shadowDrop;", glider)
        self.assertIn("private int shadowAlpha = 0x9A;", glider)
        self.assertIn("shadowBlur = 22f * density;", glider)
        self.assertIn("shadowDrop = 8f * density;", glider)
        self.assertIn("void setPopupShadowStyle(float blurPx, float dropPx, int alpha)", glider)
        self.assertIn("shadowPaint.setAlpha(Math.round(shadowAlpha * opacity * shadowMix));", glider)
        self.assertIn("glider.setPopupShadowStyle(14f * density, 3f * density, 0x58)", quick)
        self.assertIn("glider.setSingleGlassRim(true)", quick)
        self.assertIn("glider.setCompanionOnly(true)", quick)

    def test_home_dock_and_capsule_material_remain_separate(self):
        dock = (JAVA / "DockDecoration.java").read_text(encoding="utf-8")
        drawable = (JAVA / "UniversalGlassDrawable.java").read_text(encoding="utf-8")
        self.assertIn("new UniversalGlassDrawable(density, 26f, true)", dock)
        self.assertIn("if (silverCyanIdentity)", drawable)
        self.assertIn("0xA650DDF2", drawable)


if __name__ == "__main__":
    unittest.main()
