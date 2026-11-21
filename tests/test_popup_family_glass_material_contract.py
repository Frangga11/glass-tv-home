"""Source-level contracts for the shared popup and Dock glass material."""
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher/app/src/main/java/com/gathin/tvhome"


class PopupFamilyGlassMaterialContractTest(unittest.TestCase):
    def test_wifi_popup_uses_live_shared_glass_backdrop(self):
        wifi = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("new GlassDrawable(content", wifi)
        self.assertIn("Backdrop.watch(content)", wifi)
        self.assertIn("window.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT))", wifi)

    def test_wifi_pointer_keeps_the_underlying_glass_visible(self):
        wifi = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        pointer = wifi[wifi.index("pointer = new View(activity)"):wifi.index("content.addView(pointer")]
        self.assertIn("TvUiTokens.GLASS_VEIL_COLOR", pointer)
        self.assertNotIn("paint.setColor(TvUiTokens.POPUP_SURFACE)", pointer)

    def test_dock_and_popup_share_the_backdrop_and_rim_primitives(self):
        glass = (JAVA / "GlassDrawable.java").read_text(encoding="utf-8")
        dock = (JAVA / "DockDecoration.java").read_text(encoding="utf-8")
        self.assertIn("new Backdrop.Fill()", glass)
        self.assertIn("fill.draw(canvas, owner, rect, radius, glass)", glass)
        self.assertIn("new Backdrop.Fill()", dock)
        self.assertIn("blur.draw(c, parent, rect, radius, 1f)", dock)
        self.assertIn("new GlassRim(", glass)
        self.assertIn("new GlassRim(", dock)

    def test_centered_modal_dialogs_use_the_same_live_glass_material(self):
        style = (JAVA / "TvDialogStyle.java").read_text(encoding="utf-8")
        self.assertIn("new GlassDrawable(decor", style)
        self.assertIn("Backdrop.watch(decor)", style)
        self.assertIn("window.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT))", style)

    def test_top_edge_specular_reflection_is_scoped_to_dock_and_quick_settings(self):
        rim = (JAVA / "GlassRim.java").read_text(encoding="utf-8")
        self.assertIn("void drawTopReflection(", rim)
        self.assertIn("topReflectionGlowPaint", rim)
        self.assertIn("topReflectionPaint", rim)
        self.assertIn("canvas.drawPath(topReflectionPath, topReflectionGlowPaint)", rim)
        self.assertIn("canvas.drawPath(topReflectionPath, topReflectionPaint)", rim)

        dock = (JAVA / "DockDecoration.java").read_text(encoding="utf-8")
        self.assertIn("edge.drawTopReflection(c, rect, radius - half, 1f)", dock)

        glass = (JAVA / "GlassDrawable.java").read_text(encoding="utf-8")
        self.assertIn("private final boolean topReflection", glass)
        self.assertIn("if (topReflection)", glass)
        self.assertIn("rim.drawTopReflection(canvas, rect, radius - half, glass)", glass)
        self.assertIn("fallbackSurfaceColor, fallbackBorderColor, false", glass)

        quick = (JAVA / "QuickSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("PANEL_FALLBACK_SURFACE, PANEL_FALLBACK_BORDER, true)", quick)

        # Other popup surfaces keep the default material unless explicitly opted in.
        wifi = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("new GlassDrawable(content,", wifi)
        self.assertNotIn("PANEL_FALLBACK_BORDER, true)", wifi)

    def test_glass_disabled_mode_keeps_an_opaque_structural_surface(self):
        glass = (JAVA / "GlassDrawable.java").read_text(encoding="utf-8")
        self.assertIn('getBoolean("glass_effects_enabled", true)', glass)
        self.assertIn("if (!glassEnabled)", glass)
        self.assertIn("TvUiTokens.POPUP_SURFACE, TvUiTokens.BORDER", glass)
        self.assertIn("solidSurface.setColor(fallbackSurfaceColor)", glass)
        self.assertIn("canvas.drawRoundRect(rect, radius, radius, solidSurface)", glass)


if __name__ == "__main__":
    unittest.main()
