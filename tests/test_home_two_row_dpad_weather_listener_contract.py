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

    def test_primary_dock_is_above_overflow_grid_in_horizontal_layout(self):
        start = MAIN.index("private void applyDockLayout()")
        end = MAIN.index("private boolean sameLayoutParams", start)
        layout = MAIN[start:end]
        self.assertIn("gridParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;", layout)
        self.assertIn("dockParams.gravity = Gravity.TOP | Gravity.START;", layout)
        self.assertIn("horizontalPrimaryDockTopMarginPx", layout)
        self.assertIn("horizontalOverflowGridTopMarginPx", layout)

    def test_geometry_anchors_preserve_a_single_lower_overflow_viewport(self):
        self.assertIn("horizontalGridViewportHeightPx", GEOMETRY)
        self.assertIn("horizontalPrimaryDockTopMarginPx", GEOMETRY)
        self.assertIn("horizontalOverflowGridTopMarginPx", GEOMETRY)


if __name__ == "__main__":
    unittest.main()
