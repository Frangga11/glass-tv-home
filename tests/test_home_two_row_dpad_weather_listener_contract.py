from pathlib import Path
import unittest


ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
GEOMETRY = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java").read_text(encoding="utf-8")


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

    def test_dpad_transition_precommits_focus_then_moves_both_rows(self):
        expand_start = MAIN.index("private void expandHorizontalDockForOverflow")
        collapse_start = MAIN.index("private void collapseHorizontalOverflowToDock")
        animate_start = MAIN.index("private void animateHorizontalDockTransition")
        expand = MAIN[expand_start:collapse_start]
        collapse = MAIN[collapse_start:animate_start]
        self.assertIn("dockRecycler.setTranslationY(0f)", expand)
        self.assertIn("grid.setTranslationY(pitch)", expand)
        self.assertIn("focusTile(targetPosition)", expand)
        self.assertIn("prepareDockFocusBeforeCollapse", collapse)
        self.assertIn("card.requestFocus()", collapse)
        self.assertIn("dockRecycler.setTranslationY(-pitch * (1f - progress))", collapse)
        self.assertIn("grid.setTranslationY(pitch * progress)", collapse)
        self.assertIn("grid.setVisibility(View.GONE)", collapse)
        self.assertNotIn("restoreDockFocusAfterCollapse", MAIN)

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

    def test_row_scroll_coalesces_latest_target_without_restarting_active_motion(self):
        start = MAIN.index("private void requestHorizontalGridRow(")
        end = MAIN.index("private int resolvePendingHorizontalGridPosition()", start)
        request = MAIN[start:end]
        self.assertIn("pendingHorizontalGridKey = adapter.keyAt(targetPosition);", request)
        self.assertIn("if (horizontalGridTransitionRunning) return;", request)
        self.assertLess(request.index("pendingHorizontalGridKey = adapter.keyAt(targetPosition);"),
                        request.index("if (horizontalGridTransitionRunning) return;"))
        self.assertLess(request.index("if (horizontalGridTransitionRunning) return;"),
                        request.index("horizontalGridTransitionRunning = true;"))
        self.assertNotIn("previous.cancel()", request)
        settle_start = MAIN.index("private void settleHorizontalGridRowTransition(")
        settle_end = MAIN.index("private void focusHorizontalGridRowTargetWhenLaidOut(", settle_start)
        settle = MAIN[settle_start:settle_end]
        self.assertIn("int target = resolvePendingHorizontalGridPosition();", settle)
        self.assertIn("startHorizontalGridRowTransition(", settle)

    def test_geometry_uses_one_measured_overflow_viewport(self):
        self.assertIn("horizontalGridViewportHeightPx", GEOMETRY)
        self.assertIn("horizontalDockBottomMarginPx", GEOMETRY)


if __name__ == "__main__":
    unittest.main()
