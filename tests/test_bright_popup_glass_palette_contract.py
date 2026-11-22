"""Contracts for the popup-only Bright Frosted Silver-Cyan palette."""
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher/app/src/main/java/com/gathin/tvhome"


class BrightPopupGlassPaletteContractTest(unittest.TestCase):
    def test_palette_is_popup_scoped_and_keeps_global_tokens_untouched(self):
        palette = (JAVA / "PopupGlassTokens.java").read_text(encoding="utf-8")
        global_tokens = (JAVA / "TvUiTokens.java").read_text(encoding="utf-8")
        self.assertIn("0xFFE8EFF4", palette)
        self.assertIn("0xFF263746", palette)
        self.assertIn("0xFF526675", palette)
        self.assertIn("0xFFDDF5F8", palette)
        self.assertIn("0xFF42BCD2", palette)
        self.assertIn("private PopupGlassTokens()", palette)
        self.assertIn("static final int POPUP", global_tokens) if "static final int POPUP" in global_tokens else self.assertIn("POPUP_SURFACE", global_tokens)

    def test_settings_popup_uses_bright_panel_and_capsule_tokens(self):
        settings = (JAVA / "QuickSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("PopupGlassTokens.PANEL_VEIL", settings)
        self.assertIn("PopupGlassTokens.PANEL_VEIL_ALPHA", settings)
        self.assertIn("PopupGlassTokens.PANEL_FALLBACK", settings)
        self.assertIn("PopupGlassTokens.TEXT_PRIMARY", settings)
        self.assertIn("PopupGlassTokens.CAPSULE_IDLE", settings)
        self.assertIn("PopupGlassTokens.CAPSULE_FOCUS", settings)

    def test_wifi_popup_uses_same_panel_capsule_and_text_palette(self):
        wifi = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("PopupGlassTokens.PANEL_VEIL", wifi)
        self.assertIn("PopupGlassTokens.POINTER_VEIL", wifi)
        self.assertIn("PopupGlassTokens.CONNECTED_SURFACE", wifi)
        self.assertIn("PopupGlassTokens.CAPSULE_FOCUS_BORDER", wifi)
        self.assertIn("PopupGlassTokens.TEXT_PRIMARY", wifi)
        self.assertIn("Color.rgb(80, 255, 145)", wifi)  # Connected status semantics remain green.

    def test_popup_geometry_and_glass_focus_engine_remain_owned_by_existing_code(self):
        settings = (JAVA / "QuickSettingsDialog.java").read_text(encoding="utf-8")
        wifi = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        for contract in ("PANEL_HEIGHT_DP = 320", "int width = dp(density, 255)",
                         "HALF_TILE_WIDTH_DP = 115", "CAPSULE_GAP_DP = 5",
                         "new GliderFocusDrawable(control, glider)", "glider.setStyle(Glider.GLASS)"):
            self.assertIn(contract, settings)
        self.assertIn("return 0.225f;", wifi)
        self.assertIn("return 0.69f;", wifi)
        self.assertIn("TvFocusIndicator.applyRoundedTextFocus", wifi)


if __name__ == "__main__":
    unittest.main()
