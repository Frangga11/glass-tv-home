"""Source-level contracts for Universal Home material adoption and protected focus behavior."""
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher/app/src/main/java/com/gathin/tvhome"


class PopupFamilyGlassMaterialContractTest(unittest.TestCase):
    def test_navigation_surface_uses_a_restrained_underlay_without_replacing_universal_glass(self):
        material = (JAVA / "UniversalGlassMaterial.java").read_text(encoding="utf-8")
        main = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        glider = (JAVA / "Glider.java").read_text(encoding="utf-8")
        self.assertIn("static Drawable navigationSurface(", material)
        self.assertIn("tint.setColor(0x24212A33)", material)
        self.assertIn("new UniversalGlassDrawable(density, radiusDp)", material)
        self.assertIn("UniversalGlassMaterial.navigationSurface(this, density, 20f,", main)
        self.assertIn("setShadowLayer(1.0f * density", main)
        self.assertIn("FOCUS_HALO = 0x35DCE5ED", (JAVA / "TvUiTokens.java").read_text(encoding="utf-8"))
        self.assertIn("paint.setColor(TvUiTokens.FOCUS_HALO)", glider)
        self.assertNotIn("NAVIGATION_DARK", glider)

    def test_popup_uses_a_subtle_smoke_density_increase_and_quick_settings_scrim(self):
        material = (JAVA / "UniversalGlassMaterial.java").read_text(encoding="utf-8")
        quick = (JAVA / "QuickSettingsDialog.java").read_text(encoding="utf-8")
        wifi = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("PANEL_SHADE = 0xC21D1D1D", material)
        self.assertIn("window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)", quick)
        self.assertIn("params.dimAmount = 0.08f", quick)
        self.assertIn("return 0.08f;", wifi)

    def test_wifi_popup_uses_universal_panel_material_without_changing_window_contract(self):
        wifi = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("UniversalGlassMaterial.panel(activity, density, TvUiTokens.POPUP_RADIUS_DP,", wifi)
        self.assertNotIn("Backdrop.watch(content)", wifi)
        self.assertIn("window.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT))", wifi)

    def test_wifi_pointer_uses_the_same_material_and_keeps_its_open_base(self):
        wifi = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        pointer = wifi[wifi.index("pointer = new View(activity)"):wifi.index("content.addView(pointer")]
        self.assertIn("UniversalGlassMaterial.drawPointer(activity, canvas, triangle, pointerEdges", pointer)
        self.assertIn("pointerEdges.lineTo(center + h, h)", pointer)
        self.assertIn("float materialHeight = parent != null && parent.getHeight() > 0", pointer)
        self.assertIn("new RectF(density, density,", pointer)
        self.assertIn("materialBounds, density, TvUiTokens.POPUP_SURFACE", pointer)
        self.assertNotIn("new RectF(0f, 0f, w, h)", pointer)
        self.assertNotIn("TvUiTokens.GLASS_VEIL_COLOR", pointer)

    def test_dock_uses_universal_material_while_glider_stays_between_surface_and_reflection(self):
        dock = (JAVA / "DockDecoration.java").read_text(encoding="utf-8")
        self.assertIn("private final UniversalGlassDrawable universalGlass;", dock)
        self.assertIn("universalGlass.drawSurface(c, rect)", dock)
        self.assertIn("universalGlass.drawReflection(c, rect)", dock)
        self.assertIn("universalGlass.drawRim(c, rect)", dock)
        self.assertNotIn("blur.draw(c, parent, rect, radius, 1f)", dock)
        self.assertNotIn("new GlassRim(", dock)
        surface = dock.index("universalGlass.drawSurface(c, rect)")
        focus = dock.index("glider.drawShape(c, gliderAt[0] - at[0], gliderAt[1] - at[1], parent)", surface)
        reflection = dock.index("universalGlass.drawReflection(c, rect)", focus)
        rim = dock.index("universalGlass.drawRim(c, rect)", reflection)
        self.assertLess(surface, focus)
        self.assertLess(focus, reflection)
        self.assertLess(reflection, rim)

    def test_quick_settings_uses_universal_panel_and_control_material_beneath_existing_glider(self):
        quick = (JAVA / "QuickSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("UniversalGlassMaterial.panel(activity, density, TvUiTokens.POPUP_RADIUS_DP,", quick)
        self.assertIn("UniversalGlassMaterial.controlIdle(activity, density, cornerRadiusDp,", quick)
        self.assertIn("new GliderFocusDrawable(control, glider)", quick)
        self.assertNotIn("Backdrop.watch(content)", quick)

    def test_home_navigation_capsule_uses_universal_surface_but_keeps_shared_glider(self):
        main = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        self.assertIn("UniversalGlassMaterial.navigationSurface(this, density, 20f,", main)
        self.assertIn("glider.addCompanion(navCapsule)", main)
        self.assertIn("new GliderFocusDrawable(pill, glider)", main)
        self.assertNotIn("Backdrop.watch(navCapsule)", main)

    def test_wifi_actions_keep_existing_focus_state_contract_and_use_universal_idle_surface(self):
        wifi = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("TvFocusIndicator.applyRoundedTextFocus(view, density, TvUiTokens.ROW_RADIUS_DP", wifi)
        self.assertIn("UniversalGlassMaterial.controlStateBackground(activity, density,", wifi)

    def test_ported_universal_material_keeps_source_gradient_layers(self):
        glass = (JAVA / "UniversalGlassDrawable.java").read_text(encoding="utf-8")
        material = (JAVA / "UniversalGlassMaterial.java").read_text(encoding="utf-8")
        for token in ("0x36FFFFFF", "0x16FFFFFF", "0x0DFFFFFF", "0x30232323",
                      "0xA6FFFFFF", "0x38FFFFFF", "0x14FFFFFF", "0x66FFFFFF",
                      "0x22FFFFFF", "0x08FFFFFF", "0x30FFFFFF"):
            with self.subTest(token=token):
                self.assertIn(token, glass)
        self.assertIn("PANEL_SHADE = 0xC21D1D1D", material)
        self.assertIn('getBoolean("glass_effects_enabled", true)', material)
        self.assertIn("if (!isGlassEnabled(context))", material)
        self.assertIn("0x16FFFFFF", material)
        self.assertIn("0x08FFFFFF", material)
        self.assertIn("0x28FFFFFF", material)
        self.assertIn("0x48FFFFFF", material)
        self.assertIn("0x22FFFFFF", material)
        self.assertIn("0xBBFFFFFF", material)
        self.assertIn("if (glassEnabled)", material)
        self.assertIn("pressed.setColor(TvUiTokens.SURFACE_SELECTED)", material)
        self.assertIn("focused.setColor(TvUiTokens.SURFACE_FOCUSED)", material)

    def test_other_shared_glass_surfaces_keep_their_existing_backdrop_contract(self):
        glass = (JAVA / "GlassDrawable.java").read_text(encoding="utf-8")
        style = (JAVA / "TvDialogStyle.java").read_text(encoding="utf-8")
        self.assertIn("new Backdrop.Fill()", glass)
        self.assertIn("fill.draw(canvas, owner, rect, radius, glass)", glass)
        self.assertIn("new GlassDrawable(decor", style)
        self.assertIn("Backdrop.watch(decor)", style)
        self.assertIn("window.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT))", style)

    def test_glass_disabled_mode_keeps_opaque_fallbacks(self):
        glass = (JAVA / "GlassDrawable.java").read_text(encoding="utf-8")
        dock = (JAVA / "DockDecoration.java").read_text(encoding="utf-8")
        self.assertIn('getBoolean("glass_effects_enabled", true)', glass)
        self.assertIn("if (!glassEnabled)", glass)
        self.assertIn("TvUiTokens.POPUP_SURFACE, TvUiTokens.BORDER", glass)
        self.assertIn("solidSurface.setColor(fallbackSurfaceColor)", glass)
        self.assertIn("canvas.drawRoundRect(rect, radius, radius, solidSurface)", glass)
        self.assertIn("solidTrayPaint", dock)
        self.assertIn("c.drawRoundRect(rect, radius, radius, solidTrayPaint)", dock)


if __name__ == "__main__":
    unittest.main()
