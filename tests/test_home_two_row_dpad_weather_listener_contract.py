from pathlib import Path
import unittest


ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
GEOMETRY = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java").read_text(encoding="utf-8")
ADAPTER = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/AppAdapter.java").read_text(encoding="utf-8")


class HomeTwoRowDpadWeatherListenerContractTest(unittest.TestCase):
    def test_header_down_enters_primary_dock_not_overflow_grid(self):
        start = MAIN.index("View.OnKeyListener pillKeys")
        end = MAIN.index("if (locationPill != null) locationPill.setOnKeyListener", start)
        keys = MAIN[start:end]
        self.assertIn("focusDockTile(0)", keys)
        self.assertIn("The Dock is the primary visible app row", keys)

    def test_weather_aqi_has_one_authoritative_key_listener(self):
        self.assertEqual(1, MAIN.count("weatherAirQualityView.setOnKeyListener("))
        start = MAIN.index("weatherAirQualityView.setOnKeyListener(")
        end = MAIN.index("});", start)
        weather_keys = MAIN[start:end]
        self.assertIn("locationPill.requestFocus()", weather_keys)
        self.assertIn("btnHome.requestFocus()", weather_keys)
        self.assertIn("focusDockTile(0)", weather_keys)

    def test_overflow_first_row_up_returns_to_same_dock_column(self):
        start = MAIN.index("public boolean onTileVerticalKey(int position, int keyCode)")
        end = MAIN.index("private void cancelHorizontalGridRowTransition()", start)
        navigation = MAIN[start:end]
        self.assertIn("basePosition % COLUMNS", navigation)
        self.assertIn("focusDockTile(dockTarget)", navigation)

    def test_idle_dock_restores_legacy_bottom_anchor_and_expanded_rows_share_bottom_frame(self):
        start = MAIN.index("private void applyDockLayout()")
        end = MAIN.index("private boolean sameLayoutParams", start)
        layout = MAIN[start:end]
        self.assertIn("gridParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;", layout)
        self.assertIn("dockParams.gravity = Gravity.BOTTOM | Gravity.START;", layout)
        self.assertIn("dockParams.bottomMargin = dockBottomMargin;", layout)
        self.assertNotIn("dockParams.bottomMargin = horizontalGridExpanded", layout)
        self.assertIn("gridParams.bottomMargin = horizontalGridExpanded ? dockBottomMargin : 0", layout)

    def test_dpad_transition_moves_both_rows_and_commits_focus_after_motion(self):
        expand_start = MAIN.index("private void expandHorizontalDockForOverflow")
        collapse_start = MAIN.index("private void collapseHorizontalOverflowToDock")
        animate_start = MAIN.index("private void animateHorizontalDockTransition")
        expand = MAIN[expand_start:collapse_start]
        collapse = MAIN[collapse_start:animate_start]
        self.assertIn("dockRecycler.setTranslationY(0f)", expand)
        self.assertIn("grid.setTranslationY(pitch)", expand)
        self.assertIn("dockRecycler.setTranslationY(-pitch * progress)", MAIN)
        self.assertIn("grid.setTranslationY(pitch * progress)", collapse)
        self.assertIn("dockRecycler.setTranslationY(0f);", collapse)
        self.assertIn("focusTile(targetPosition)", expand)
        self.assertIn("focusDockTile(dockPosition)", collapse)

    def test_dock_layout_anchor_is_constant_and_up_resets_transform_without_predraw_handoff(self):
        layout_start = MAIN.index("private void applyDockLayout()")
        layout_end = MAIN.index("private boolean sameLayoutParams", layout_start)
        layout = MAIN[layout_start:layout_end]
        self.assertIn("dockParams.bottomMargin = dockBottomMargin;", layout)
        collapse_start = MAIN.index("private void collapseHorizontalOverflowToDock")
        collapse_end = MAIN.index("private void animateHorizontalDockTransition", collapse_start)
        collapse = MAIN[collapse_start:collapse_end]
        self.assertIn("dockRecycler.setTranslationY(0f);", collapse)
        self.assertIn("grid.setTranslationY(0f);", collapse)
        self.assertIn("grid.setVisibility(View.GONE);", collapse)
        self.assertNotIn("ViewTreeObserver.OnPreDrawListener", collapse)

    def test_two_row_transition_uses_responsive_motion_duration(self):
        collapse_start = MAIN.index("private void collapseHorizontalOverflowToDock")
        focus_start = MAIN.index("private void focusTile(", collapse_start)
        transitions = MAIN[collapse_start:focus_start]
        self.assertEqual(2, transitions.count("animator.setDuration(reducedMotion ? 180 : 300);"))
        self.assertNotIn("animator.setDuration(reducedMotion ? 120 : 190);", transitions)

    def test_two_row_geometry_uses_fixed_150dp_pitch_and_182_by_100_design_tile(self):
        expand_start = MAIN.index("private void expandHorizontalDockForOverflow")
        collapse_start = MAIN.index("private void collapseHorizontalOverflowToDock")
        expand = MAIN[expand_start:collapse_start]
        self.assertIn("int pitch = Math.round(150f * density);", expand)
        self.assertIn("DESIGN_W_DP = 182f, DESIGN_H_DP = 100f", ADAPTER)
        self.assertIn("Math.round(42 * density)", ADAPTER)
        self.assertIn("horizontalGridViewportHeightPx", GEOMETRY)
        self.assertIn("horizontalDockBottomMarginPx", GEOMETRY)

    def test_expand_and_collapse_share_fixed_pitch_and_natural_interpolator(self):
        expand_start = MAIN.index("private void expandHorizontalDockForOverflow")
        collapse_start = MAIN.index("private void collapseHorizontalOverflowToDock")
        transition_start = MAIN.index("private void animateHorizontalDockTransition")
        focus_start = MAIN.index("private void focusTile(", transition_start)
        expand = MAIN[expand_start:collapse_start]
        collapse = MAIN[collapse_start:transition_start]
        motion = MAIN[transition_start:focus_start]
        self.assertIn("int pitch = Math.round(150f * density);", expand)
        self.assertIn("final int pitch = Math.round(150f * density);", collapse)
        self.assertIn("PathInterpolator(0.16f, 1f, 0.30f, 1f)", collapse)
        self.assertIn("PathInterpolator(0.16f, 1f, 0.30f, 1f)", motion)
        self.assertNotIn("horizontalGridViewportHeightPx(", collapse)


    def test_glass_focus_keeps_material_and_reduces_blur_dominance(self):
        glider = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/Glider.java").read_text(encoding="utf-8")
        self.assertIn("setStiffness(420f)", glider)
        self.assertIn("opacity * 0.58f", glider)
        self.assertIn("0x2A * opacity", glider)
        self.assertIn("rim.drawWindowLight(canvas, shape, r - half, opacity)", glider)

    def test_dock_reveal_serializes_repeated_vertical_keys(self):
        start = MAIN.index("public boolean onDockTileKey(")
        end = MAIN.index("public boolean onDockTileHeld(", start)
        dock_keys = MAIN[start:end]
        self.assertIn("if (horizontalDockExpansionInProgress) return true;", dock_keys)
        expand_start = MAIN.index("private void expandHorizontalDockForOverflow")
        expand_end = MAIN.index("private void collapseHorizontalOverflowToDock", expand_start)
        expand = MAIN[expand_start:expand_end]
        self.assertIn("horizontalDockExpansionInProgress = true;", expand)
        self.assertIn("horizontalDockExpansionInProgress = false;", expand)
        self.assertLess(expand.index("horizontalDockExpansionInProgress = false;"),
                        expand.index("focusTile(targetPosition)"))

    def test_row_scroll_coalesces_burst_without_restarting_active_animator(self):
        start = MAIN.index("private void requestHorizontalGridRow(")
        end = MAIN.index("private int resolvePendingHorizontalGridPosition()", start)
        request = MAIN[start:end]
        self.assertIn("if (horizontalGridTransitionRunning) return;", request)
        self.assertLess(request.index("pendingHorizontalGridKey = adapter.keyAt(targetPosition);"),
                        request.index("if (horizontalGridTransitionRunning) return;"))
        self.assertNotIn("previous.cancel()", request)

    def test_geometry_uses_one_measured_overflow_viewport(self):
        self.assertIn("horizontalGridViewportHeightPx", GEOMETRY)
        self.assertIn("horizontalDockBottomMarginPx", GEOMETRY)


if __name__ == "__main__":
    unittest.main()
