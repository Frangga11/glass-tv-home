"""Regression contracts for the Home Dock, Islamic shelf anchor, and DPAD row motion."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
MAIN = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java").read_text(encoding="utf-8")
GEOMETRY = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java").read_text(encoding="utf-8")
DOCK = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockAdapter.java").read_text(encoding="utf-8")
DECORATION = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockDecoration.java").read_text(encoding="utf-8")
APP = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/AppAdapter.java").read_text(encoding="utf-8")
LAYOUT = ROOT / "launcher/app/src/main/res/layout/item_app.xml"
HOME_LAYOUT = ROOT / "launcher/app/src/main/res/layout/activity_main.xml"
ANDROID = "{http://schemas.android.com/apk/res/android}"


class HomeDockIslamicShelfNavigationContractTest(unittest.TestCase):
    def test_dock_has_independent_proportional_tile_geometry(self):
        self.assertIn("DOCK_TILE_SCALE = 0.95f", GEOMETRY)
        self.assertIn("horizontalDockTileWidthPx", GEOMETRY)
        self.assertIn("verticalDockTileWidthPx", GEOMETRY)
        self.assertIn("horizontalDockSlotWidthPx", GEOMETRY)
        self.assertIn("DOCK_SLOT_GAP_DP = 32f", GEOMETRY)
        self.assertIn("horizontalDockTileWidthPx(horizontalWidth, capacity, density)", DOCK)
        self.assertIn("horizontalDockSlotWidthPx(horizontalWidth, capacity, density)", DOCK)
        self.assertIn("verticalDockTileWidthPx(inner, density)", DOCK)
        self.assertIn("static int tileWidthPx(int innerWidthPx, int columns, float density)", APP)
        self.assertIn("DESIGN_W_DP = 180f, DESIGN_H_DP = 101f", APP)

    def test_dock_icon_is_44dp_but_grid_icon_stays_48dp(self):
        self.assertIn("Math.round(44 * density)", DOCK)
        root = ET.parse(LAYOUT).getroot()
        icon = next(node for node in root.iter()
                    if node.get(ANDROID + "id") == "@+id/icon")
        self.assertEqual(icon.get(ANDROID + "layout_width"), "48dp")
        self.assertEqual(icon.get(ANDROID + "layout_height"), "48dp")

    def test_dock_host_total_height_reduces_by_about_eight_dp(self):
        self.assertIn("DOCK_EXTRA_HEIGHT_DP = 14f", GEOMETRY)
        self.assertIn("return itemHeightPx(tileHeightPx, density) + Math.round(DOCK_EXTRA_HEIGHT_DP * density);", GEOMETRY)
        self.assertIn("horizontalShelfAnchorHeightPx", GEOMETRY)
        self.assertIn("horizontalDockBottomMarginPx", GEOMETRY)
        self.assertIn("horizontalGridBottomMarginPx", GEOMETRY)
        self.assertIn("HORIZONTAL_DOCK_ITEM_BOTTOM_OFFSET_DP = 8f", GEOMETRY)
        self.assertIn("DOCK_ITEM_BOTTOM_OFFSET_DP = 12f", GEOMETRY)
        self.assertIn("horizontalDockItemBottomOffsetPx", DECORATION)

    def test_dock_capacity_uses_separate_dock_and_grid_widths(self):
        start = MAIN.index("int gridTileWidth = DockGeometry.horizontalTileWidthPx")
        end = MAIN.index("dockParams.height = dockHeight;", start)
        sizing = MAIN[start:end]
        self.assertIn("horizontalDockTileWidthPx", sizing)
        self.assertIn("horizontalDockSlotWidthPx", sizing)
        self.assertIn("horizontalGridViewportHeightPx(gridTileWidth", sizing)
        self.assertIn("horizontalGridBottomMarginPx(dockTileHeight", sizing)

    def test_islamic_shelf_anchor_uses_measured_tray_edge_and_exact_27_5dp_gap(self):
        start = MAIN.index("private void updateHomeShelfAnchor()")
        end = MAIN.index("private void showHero(int position)", start)
        anchor = MAIN[start:end]
        self.assertIn("horizontalDockTrayTopInRoot()", anchor)
        self.assertIn("DockGeometry.islamicShelfDockGapPx(density)", anchor)
        self.assertIn("islamicShelfBottomAnchor = trayTop -", anchor)
        self.assertIn("heroBottom = islamicShelfBottomAnchor", anchor)
        self.assertIn("private int horizontalDockTrayTopInRoot()", MAIN)
        self.assertIn("root.offsetDescendantRectToMyCoords(item, cardRect)", MAIN)
        self.assertIn("DockGeometry.trayPaddingPx(density)", MAIN)
        self.assertIn("static int islamicShelfDockGapPx(float density)", GEOMETRY)
        self.assertIn("27.5f * density", GEOMETRY)
        self.assertNotIn("showHero(position, r.top)", MAIN)

    def test_islamic_shelf_content_height_changes_keep_the_bottom_anchor(self):
        self.assertIn("islamicShelf.addOnLayoutChangeListener", MAIN)
        self.assertIn("applyIslamicShelfPosition();", MAIN)
        self.assertIn("private void applyIslamicShelfPosition()", MAIN)
        self.assertIn("heroShelf.addOnLayoutChangeListener", MAIN)
        start = MAIN.index("private void hideShelf()")
        end = MAIN.index("private boolean isIconGlowEnabled()", start)
        hide = MAIN[start:end]
        self.assertIn("animate().alpha(0f)", hide)
        self.assertNotIn("setY(", hide)
        self.assertNotIn("translationY(", hide)

    def test_row_navigation_uses_decorated_geometry_and_real_scroll(self):
        self.assertIn("manager.getDecoratedBoundsWithMargins(firstView, rowBounds)", MAIN)
        self.assertIn("manager.getDecoratedBoundsWithMargins(targetView, targetBounds)", MAIN)
        self.assertIn("ValueAnimator.ofInt(0, dy)", MAIN)
        self.assertIn("grid.scrollBy(0, step)", MAIN)
        self.assertIn("horizontalGridTransitionGeneration", MAIN)
        self.assertIn("horizontalGridTransitionRetries", MAIN)
        self.assertIn("pendingHorizontalGridPosition", MAIN)
        self.assertIn("pendingHorizontalGridKey = adapter.keyAt(targetPosition)", MAIN)
        self.assertIn("adapter.positionOf(pendingHorizontalGridKey)", MAIN)
        self.assertIn("grid.stopScroll()", MAIN)
        self.assertNotIn("grid.smoothScrollBy(0, dy", MAIN)
        self.assertIn("focusHorizontalGridRowTargetWhenLaidOut(targetKey, target, focusGeneration, 8)", MAIN)
        self.assertNotIn("horizontalGridScrollAwaitingIdle", MAIN)
        self.assertIn("grid.setClipToPadding(!dockVertical && horizontalGridExpanded)", MAIN)
        start = MAIN.index("private void startHorizontalGridRowTransition")
        end = MAIN.index("private void settleHorizontalGridRowTransition", start)
        transition = MAIN[start:end]
        self.assertNotIn("scrollToPositionWithOffset", transition)
        self.assertIn("DockGeometry.gridRowScrollDeltaPx(", transition)
        pause_start = MAIN.index("protected void onPause()")
        pause_end = MAIN.index("public void onWindowFocusChanged", pause_start)
        self.assertIn("cancelHorizontalGridRowTransition();", MAIN[pause_start:pause_end])

    def test_islamic_headings_and_prayer_names_have_requested_typography(self):
        root = ET.parse(HOME_LAYOUT).getroot()
        by_id = {node.get(ANDROID + "id"): node for node in root.iter()
                 if node.get(ANDROID + "id")}
        for view_id in ("@+id/prayerHeader", "@+id/ayahLabel"):
            self.assertEqual(by_id[view_id].get(ANDROID + "fontVariationSettings"), "'wght' 700")
        for view_id in ("@+id/lblSubuh", "@+id/lblSyuruk", "@+id/lblZohor",
                        "@+id/lblAsar", "@+id/lblMaghrib", "@+id/lblIsyak"):
            label = by_id[view_id]
            self.assertEqual(label.get(ANDROID + "textAllCaps"), "true")
            self.assertEqual(label.get(ANDROID + "fontVariationSettings"), "'wght' 650")
            self.assertEqual(label.get(ANDROID + "textSize"), "13sp")
            self.assertEqual(label.get(ANDROID + "fontFamily"), "@font/inter")

    def test_dpad_down_and_up_use_pending_row_and_clamp_incomplete_last_row(self):
        start = MAIN.index("public boolean onTileVerticalKey")
        end = MAIN.index("private int pendingFocus", start)
        keys = MAIN[start:end]
        self.assertIn("basePosition + COLUMNS", keys)
        self.assertIn("basePosition - COLUMNS", keys)
        self.assertIn("Math.min(basePosition + COLUMNS, count - 1)", keys)
        self.assertIn("focusDockTile(DockLayoutModel.dockPositionForGrid(", keys)

    def test_grid_programmatic_restore_keeps_existing_alignment_fallback(self):
        self.assertIn("private void alignHorizontalGridRowToGlass(int position)", MAIN)
        self.assertIn("scrollToPositionWithOffset(position, grid.getPaddingTop())", MAIN)


if __name__ == "__main__":
    unittest.main()
