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
        self.assertIn("icon.setScaleX(vertical ? 1f : 1.045f)", DOCK)
        root = ET.parse(LAYOUT).getroot()
        icon = next(node for node in root.iter()
                    if node.get(ANDROID + "id") == "@+id/icon")
        self.assertEqual(icon.get(ANDROID + "layout_width"), "48dp")
        self.assertEqual(icon.get(ANDROID + "layout_height"), "48dp")

    def test_dock_host_height_uses_refined_extra_height_and_keeps_layout_helpers(self):
        self.assertIn("DOCK_EXTRA_HEIGHT_DP = 12f", GEOMETRY)
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
        # Expanded overflow now uses the Dock's measured idle anchor plus one Grid viewport
        # and an explicit inter-row gap; the legacy standalone Grid-bottom helper is not
        # the active owner of this two-row transition.
        self.assertIn("horizontalDockBottomMarginPx(dockTileHeight, density)", sizing)
        self.assertIn("horizontalGridViewportHeightPx(gridTileWidth", sizing)
        self.assertIn("int rowGap = Math.round(8f * density);", sizing)

    def test_home_information_edges_use_measured_tray_edge_and_exact_30dp_gap(self):
        start = MAIN.index("private void updateHomeShelfAnchor()")
        end = MAIN.index("private void showHero(int position)", start)
        anchor = MAIN[start:end]
        self.assertIn("horizontalDockTrayTopInRoot()", anchor)
        self.assertIn("DockGeometry.islamicShelfDockGapPx(density)", anchor)
        self.assertIn("heroBottom = trayTop - shelfGapPx", anchor)
        self.assertIn("islamicShelfBottomAnchor = heroBottom + islamicShelfVisualBottomOffsetPx()", anchor)
        self.assertIn("private int islamicShelfVisualBottomOffsetPx()", anchor)
        self.assertIn("private int horizontalDockTrayTopInRoot()", MAIN)
        self.assertIn("root.offsetDescendantRectToMyCoords(item, cardRect)", MAIN)
        self.assertIn("DockGeometry.trayPaddingPx(density)", MAIN)
        self.assertIn("static int islamicShelfDockGapPx(float density)", GEOMETRY)
        self.assertIn("30f * density", GEOMETRY)
        self.assertNotIn("showHero(position, r.top)", MAIN)

    def test_islamic_shelf_has_one_bottom_anchor_for_all_layout_changes_and_focus(self):
        # One listener owns content-height changes; it delegates to the same measured
        # Dock-relative anchor used by showHero. A legacy heroBottom writer caused a
        # 10dp vertical jump whenever layout and focus callbacks interleaved.
        self.assertEqual(MAIN.count("islamicShelf.addOnLayoutChangeListener"), 1)
        self.assertIn("applyIslamicShelfPosition();", MAIN)
        self.assertIn("private void applyIslamicShelfPosition()", MAIN)
        shelf_listener_start = MAIN.index("islamicShelf.addOnLayoutChangeListener")
        shelf_listener_end = MAIN.index("GradientDrawable islamicScrim", shelf_listener_start)
        shelf_listener = MAIN[shelf_listener_start:shelf_listener_end]
        self.assertIn("applyIslamicShelfPosition();", shelf_listener)
        self.assertNotIn("heroBottom", shelf_listener)
        self.assertIn("applyIslamicShelfPosition();", MAIN[MAIN.index("private void showHero(int position)"):])
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
        self.assertIn("private String pendingHorizontalGridKey;", MAIN)
        self.assertIn("pendingHorizontalGridKey = adapter.keyAt(targetPosition)", MAIN)
        self.assertIn("adapter.positionOf(pendingHorizontalGridKey)", MAIN)
        self.assertIn("grid.stopScroll()", MAIN)
        self.assertNotIn("grid.smoothScrollBy(0, dy", MAIN)
        self.assertIn("focusHorizontalGridRowTargetWhenLaidOut(targetKey, target, focusGeneration, 8)", MAIN)
        self.assertIn("Math.abs(firstWindowBounds.top - grid.getPaddingTop()) > 1", MAIN)
        self.assertIn("adapter.positionOf(key)", MAIN)
        self.assertNotIn("horizontalGridScrollAwaitingIdle", MAIN)
        self.assertIn("grid.setClipToPadding(!horizontalCarouselMode && horizontalGridExpanded)", MAIN)
        start = MAIN.index("private void startHorizontalGridRowTransition")
        end = MAIN.index("private void settleHorizontalGridRowTransition", start)
        transition = MAIN[start:end]
        self.assertNotIn("scrollToPositionWithOffset", transition)
        focus_start = MAIN.index("private void focusHorizontalGridRowTargetWhenLaidOut")
        focus_end = MAIN.index("private void alignHorizontalGridRowToGlass", focus_start)
        self.assertNotIn("scrollToPositionWithOffset", MAIN[focus_start:focus_end])
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
        self.assertIn("pendingHorizontalGridPosition", keys)
        self.assertIn("DockLayoutModel.gridNeighborPosition(basePosition, direction, COLUMNS, count)", keys)
        self.assertIn("requestHorizontalGridRow(target)", keys)
        self.assertIn("collapseHorizontalOverflowToDock(dockTarget)", keys)
        self.assertIn("cancelHorizontalGridRowTransition()", keys)

    def test_grid_programmatic_restore_keeps_existing_alignment_fallback(self):
        self.assertIn("private void alignHorizontalGridRowToGlass(int position)", MAIN)
        self.assertIn("windowStartRow * COLUMNS, grid.getPaddingTop()", MAIN)
        self.assertIn("if (dy != 0) grid.scrollBy(0, dy);", MAIN)


class HomeAppModeContractTest(unittest.TestCase):
    def test_vertical_mode_keeps_the_existing_progressive_grid(self):
        self.assertIn("dockVertical = false;", MAIN)
        self.assertIn("horizontalCarouselMode = preferences.orientation == DockPreferences.Orientation.HORIZONTAL;", MAIN)
        self.assertIn("!horizontalCarouselMode && horizontalGridExpanded ? View.VISIBLE : View.GONE", MAIN)
        keys = MAIN[MAIN.index("public boolean onDockTileKey"):MAIN.index("public boolean onDockTileHeld")]
        self.assertIn("DockLayoutModel.gridPositionForDock", keys)
        self.assertIn("if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)", keys)
        self.assertIn("focusTile(target)", keys)

    def test_horizontal_mode_uses_the_full_inventory_in_one_dock_row(self):
        dock = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockAdapter.java").read_text(encoding="utf-8")
        adapter = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/AppAdapter.java").read_text(encoding="utf-8")
        self.assertIn("entries = carouselMode ? source.allEntriesSnapshot()", dock)
        self.assertIn("source.moveAllEntries(from, to)", dock)
        self.assertIn("List<Entry> allEntriesSnapshot()", adapter)
        self.assertIn("boolean moveAllEntries(int from, int to)", adapter)
        dock_focus = MAIN[MAIN.index("public void onDockTileFocused"):MAIN.index("private void handleTileFocused")]
        self.assertIn("dockRecycler.post(() ->", dock_focus)
        self.assertIn("if (focusedPosition >= 0) alignHorizontalCarousel(focusedPosition, true);", dock_focus)
        restore = MAIN[MAIN.index("private void restoreMovingTile"):MAIN.index("private void stepMove(int keyCode)")]
        self.assertIn("if (inDock && horizontalCarouselMode) alignHorizontalCarousel(position, false);", restore)

    def test_carousel_matches_universal_focus_scroll_contract(self):
        carousel = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/CarouselRecyclerView.java").read_text(encoding="utf-8")
        self.assertIn("private CarouselRecyclerView dockRecycler;", MAIN)
        self.assertIn("dockRecycler = new CarouselRecyclerView(this);", MAIN)
        self.assertIn("new CarouselLinearLayoutManager(this, orientation, false)", MAIN)
        self.assertIn("setKeepAllCarouselItems(horizontalCarouselMode, carouselPrelayoutExtent)", MAIN)
        self.assertIn("setManualHorizontalFocus(horizontalCarouselMode)", MAIN)
        self.assertIn("if (manualHorizontalFocus) return false;", carousel)
        self.assertIn("calculateExtraLayoutSpace(RecyclerView.State state, int[] extraLayoutSpace)", carousel)
        self.assertIn("extraLayoutSpace[0] = extent;", carousel)
        self.assertIn("extraLayoutSpace[1] = extent;", carousel)

    def test_horizontal_carousel_is_one_dimensional_and_keeps_dock_bottom(self):
        keys = MAIN[MAIN.index("public boolean onDockTileKey"):MAIN.index("public boolean onDockTileHeld")]
        self.assertIn("if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)", keys)
        self.assertIn("if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)", keys)
        self.assertIn("// The carousel is one-dimensional; DOWN never opens the progressive Grid.", keys)
        layout = MAIN[MAIN.index("private void applyDockLayout"):MAIN.index("private boolean sameLayoutParams")]
        self.assertIn("dockParams.gravity = dockVertical ? Gravity.START : Gravity.BOTTOM;", layout)
        self.assertIn("dockVertical = false;", layout)


    def test_horizontal_carousel_matches_universal_clipping_motion_and_capacity(self):
        dock = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockAdapter.java").read_text(encoding="utf-8")
        geometry = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java").read_text(encoding="utf-8")
        self.assertIn("dockRecycler.setClipChildren(horizontalCarouselMode)", MAIN)
        self.assertIn("ValueAnimator.ofInt(currentOffset, targetOffset)", MAIN)
        self.assertIn("setDuration(240)", MAIN)
        self.assertIn("DecelerateInterpolator(1.5f)", MAIN)
        self.assertIn("horizontalCarouselCardWidthPx(viewportWidthPx, capacity, density)", dock)
        self.assertIn("horizontalCarouselSlotWidthPx(viewportWidthPx, capacity, density)", dock)
        self.assertIn("CAROUSEL_GAP_DP = 20f", geometry)
        self.assertNotIn("LinearSmoothScroller", MAIN)
        self.assertIn("horizontalCarouselTailInsetPx", MAIN)

    def test_latest_quick_settings_master_is_preserved_on_this_feature_branch(self):
        self.assertIn("QuickSettingsDialog.show(this, root, anchor, dockRecycler", MAIN)
        quick_settings = (ROOT / "launcher/app/src/main/java/com/gathin/tvhome/QuickSettingsDialog.java").read_text(encoding="utf-8")
        self.assertIn("QuickSettingsFocusGraph", quick_settings)


if __name__ == "__main__":
    unittest.main()
