"""Source-level contracts for Universal Home material adoption and protected focus behavior."""
from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher/app/src/main/java/com/gathin/tvhome"


class PopupFamilyGlassMaterialContractTest(unittest.TestCase):
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

    def test_home_navigation_and_header_capsules_share_silver_cyan_windowlight_surface(self):
        main = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        material = (JAVA / "UniversalGlassMaterial.java").read_text(encoding="utf-8")
        drawable = (JAVA / "UniversalGlassDrawable.java").read_text(encoding="utf-8")

        # Beranda, Profil and Pengaturan sit on one shared navPill surface.
        self.assertIn("View navCapsule = findViewById(R.id.navPill)", main)
        self.assertIn("UniversalGlassMaterial.surface(this, density, 20f,", main)
        self.assertIn("glider.addCompanion(navCapsule)", main)

        # Location/altitude, Weather/AQI, Wi-Fi and microphone receive the same
        # UniversalGlassDrawable surface, with the existing Glider overlay kept above it.
        for token in ("R.id.locationPill", "R.id.weatherAirQuality", "R.id.btnWifi",
                      "R.id.btnAssistant"):
            with self.subTest(token=token):
                self.assertIn(token, main[main.index("for (int id : new int[]{R.id.locationPill"):
                                         main.index("if (prayerTimesView != null)", main.index("for (int id : new int[]{R.id.locationPill"))])
        self.assertIn("UniversalGlassMaterial.surface(this, density, 20f,", main)
        self.assertIn("new Drawable[]{glassBackground, new GliderFocusDrawable(pill, glider)}", main)
        self.assertIn("return new UniversalGlassDrawable(density, radiusDp)", material)
        self.assertIn("0xA650DDF2", drawable)
        self.assertIn("windowLight.setAlpha(124)", drawable)
        self.assertNotIn("Backdrop.watch(navCapsule)", main)

    def test_wifi_actions_keep_existing_focus_state_contract_and_use_universal_idle_surface(self):
        wifi = (JAVA / "WifiSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("TvFocusIndicator.applyRoundedTextFocus(view, density, TvUiTokens.ROW_RADIUS_DP", wifi)
        self.assertIn("UniversalGlassMaterial.controlStateBackground(activity, density,", wifi)

    def test_ported_universal_material_keeps_source_gradient_layers(self):
        glass = (JAVA / "UniversalGlassDrawable.java").read_text(encoding="utf-8")
        material = (JAVA / "UniversalGlassMaterial.java").read_text(encoding="utf-8")
        # Universal surface/reflection layers remain intact; shared rim and Window Light
        # carry the restrained pre-adoption Silver-Cyan identity across the Dock and
        # the explicitly selected Home header capsules.
        for token in ("0x36FFFFFF", "0x16FFFFFF", "0x0DFFFFFF", "0x30232323",
                      "0x22FFFFFF", "0x08FFFFFF", "0x30FFFFFF",
                      "0xD9E2E8EE", "0xA650DDF2", "0x667D8D9B",
                      "0x80DCE5ED", "0x8067E8F9"):
            with self.subTest(token=token):
                self.assertIn(token, glass)
        self.assertIn("rim.setStrokeWidth(density)", glass)
        self.assertIn("inner.setStrokeWidth(density * 0.7f)", glass)
        self.assertIn("windowLight.setAlpha(124)", glass)
        self.assertIn("windowLight.setAlpha(Math.round(124f * alpha / 255f))", glass)
        self.assertIn("0xB31D1D1D", material)
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
