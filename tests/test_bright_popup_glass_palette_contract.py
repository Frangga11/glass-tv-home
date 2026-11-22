"""Contracts for bright popup-only material on top of PR #166."""
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher/app/src/main/java/com/gathin/tvhome"


class BrightPopupGlassPaletteContractTest(unittest.TestCase):
    def test_popup_palette_is_scoped_and_has_readable_silver_cyan_roles(self):
        tokens = (JAVA / "PopupGlassTokens.java").read_text(encoding="utf-8")
        self.assertIn("0xB8E8EFF4", tokens)
        self.assertIn("0xFFE8EFF4", tokens)
        self.assertIn("0xFF263746", tokens)
        self.assertIn("0xFF526675", tokens)
        self.assertIn("0xFFDCE7EF", tokens)
        self.assertIn("0xFFDDF5F8", tokens)
        self.assertIn("0xFF42BCD2", tokens)

    def test_quick_settings_uses_bright_panel_and_capsules_without_changing_glider(self):
        source = (JAVA / "QuickSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("PopupGlassTokens.PANEL_SHADE", source)
        self.assertIn("PopupGlassTokens.TEXT_PRIMARY", source)
        self.assertIn("PopupGlassTokens.CAPSULE_IDLE", source)
        self.assertIn("PopupGlassTokens.CAPSULE_FOCUS", source)
        self.assertIn("new GliderFocusDrawable(control, glider)", source)
        self.assertIn("glider.setStyle(Glider.GLASS)", source)
        self.assertIn("PANEL_HEIGHT_DP = 320", source)
        self.assertIn("HALF_TILE_WIDTH_DP = 115", source)
        self.assertIn("CAPSULE_GAP_DP = 5", source)

    def test_wifi_shell_pointer_and_controls_share_bright_popup_material(self):
        source = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("PopupGlassTokens.PANEL_SHADE", source)
        self.assertIn("PopupGlassTokens.POINTER", source) if "PopupGlassTokens.POINTER" in source else self.assertIn("PopupGlassTokens.PANEL_SHADE", source)
        self.assertIn("PopupGlassTokens.CAPSULE_IDLE", source)
        self.assertIn("PopupGlassTokens.CAPSULE_FOCUS_BORDER", source)
        self.assertIn("PopupGlassTokens.TEXT_PRIMARY", source)
        self.assertIn("return 0.225f;", source)
        self.assertIn("return 0.69f;", source)
        self.assertIn("Color.rgb(80, 255, 145)", source)

    def test_universal_default_panel_material_is_preserved_and_popup_variant_is_opt_in(self):
        material = (JAVA / "UniversalGlassMaterial.java").read_text(encoding="utf-8")
        self.assertIn("return panel(context, density, radiusDp, fallbackSurfaceColor, fallbackBorderColor, PANEL_SHADE);", material)
        self.assertIn("int panelShadeColor", material)
        self.assertIn("int panelShadeColor)", material)
        self.assertIn("boolean popupBright", material)
        self.assertIn("shade.setColor(panelShadeColor)", material)


if __name__ == "__main__":
    unittest.main()
