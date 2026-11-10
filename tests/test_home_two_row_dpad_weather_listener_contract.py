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
        self.assertIn("dockParams.bottomMargin = horizontalGridExpanded", layout)
        self.assertIn("? dockBottomMargin + gridViewportHeight + rowGap", layout)
        self.assertIn(": dockBottomMargin;", layout)
        self.assertIn("gridParams.bottomMargin = horizontalGridExpanded ? dockBottomMargin : 0", layout)

    def test_dpad_transition_moves_both_rows_and_commits_focus_after_motion(self):
        expand_start = MAIN.index("private void expandHorizontalDockForOverflow")
        collapse_start = MAIN.index("private void collapseHorizontalOverflowToDock")
        animate_start = MAIN.index("private void animateHorizontalDockTransition")
        expand = MAIN[expand_start:collapse_start]
        collapse = MAIN[collapse_start:animate_start]
        self.assertIn("dockRecycler.setTranslationY(pitch)", expand)
        self.assertIn("grid.setTranslationY(pitch)", expand)
        self.assertIn("focusTile(targetPosition)", expand)
        self.assertIn("dockRecycler.setTranslationY(pitch * progress)", collapse)
        self.assertIn("grid.setTranslationY(pitch * progress)", collapse)
        self.assertIn("restoreDockFocusAfterCollapse(", collapse)
        self.assertIn("dockPosition, generation, 32", collapse)

    def test_geometry_uses_one_measured_overflow_viewport(self):
        self.assertIn("horizontalGridViewportHeightPx", GEOMETRY)
        self.assertIn("horizontalDockBottomMarginPx", GEOMETRY)


if __name__ == "__main__":
    unittest.main()
