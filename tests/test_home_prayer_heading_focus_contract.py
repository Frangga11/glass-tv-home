from pathlib import Path
import unittest

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")


class HomePrayerHeadingFocusContractTest(unittest.TestCase):
    def test_ayah_down_moves_to_prayer_heading(self):
        block = MAIN[MAIN.index("ayahOpenHint.setOnKeyListener"):MAIN.index("ayahOpenHint.setBackground", MAIN.index("ayahOpenHint.setOnKeyListener"))]
        self.assertIn("R.id.prayerHeader", block)
        self.assertNotIn("R.id.prayerTable", block)

    def test_heading_is_the_only_focusable_prayer_entry_point(self):
        block = MAIN[MAIN.index("// The Waktu Sholat heading is the single Home DPAD/click target."):MAIN.index("updatePrayerTimes();", MAIN.index("// The Waktu Sholat heading is the single Home DPAD/click target."))]
        self.assertIn("prayerHeaderView.setFocusable(true)", block)
        self.assertIn("prayerHeaderView.setClickable(true)", block)
        self.assertIn("prayerHeaderView.setOnClickListener(v -> showPrayerPopup())", block)
        self.assertIn("glider, 8f * density)", block)
        self.assertIn("KEYCODE_DPAD_DOWN", block)
        self.assertIn("KEYCODE_DPAD_UP", block)
        self.assertIn("KEYCODE_DPAD_RIGHT", block)
        self.assertIn("KEYCODE_DPAD_CENTER", block)
        self.assertIn("KEYCODE_BUTTON_A", block)

    def test_six_prayer_cells_are_display_only(self):
        block = MAIN[MAIN.index("View prayerTableView = findViewById(R.id.prayerTable);"):MAIN.index("updatePrayerTimes();", MAIN.index("View prayerTableView = findViewById(R.id.prayerTable);"))]
        self.assertIn("prayerTableView.setFocusable(false)", block)
        self.assertIn("prayerTableView.setClickable(false)", block)
        self.assertIn("prayerTableView.setBackground(null)", block)
        self.assertIn("prayerTableView.setOnKeyListener(null)", block)


if __name__ == "__main__":
    unittest.main()
