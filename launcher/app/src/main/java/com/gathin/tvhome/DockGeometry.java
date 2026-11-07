package com.gathin.tvhome;

/**
 * Single source of truth for the horizontal Dock's visual geometry.
 *
 * Dimensions preserve the proven v4.0/v4.1 tile contract: 180x101dp design
 * tiles, 45dp item height beyond the card, 6/12dp item decoration offsets,
 * 14dp tray inset, and 16dp Grid-to-tray breathing room. The independent Dock
 * adapter and Home layout must use these same values.
 */
final class DockGeometry {
    private static final float DOCK_ITEM_TOP_OFFSET_DP = 6f;
    private static final float DOCK_ITEM_BOTTOM_OFFSET_DP = 12f;
    private static final float TRAY_PADDING_DP = 14f;
    private static final float DOCK_EXTRA_HEIGHT_DP = 18f;
    private static final float GRID_TO_TRAY_GAP_DP = 8f;
    private static final float ITEM_EXTRA_HEIGHT_DP = 45f;
    private static final float SAFE_HORIZONTAL_MARGIN_DP = 48f;
    private static final float SAFE_VERTICAL_MARGIN_DP = 27f;
    private static final float HORIZONTAL_ITEM_GAP_DP = 8f;
    private static final float SHELF_TO_TRAY_GAP_DP = 36f;

    private DockGeometry() { }

    static int horizontalGridColumns() {
        return 5;
    }

    static int horizontalSlotWidthPx(int safeContentWidthPx, int capacity) {
        int columns = Math.max(horizontalGridColumns(), DockPreferences.normalizeCapacity(capacity));
        return Math.max(0, safeContentWidthPx / columns);
    }

    /** Five-column Grid viewport width chosen so its cells match the Dock slots at capacities 4/5/6. */
    static int horizontalGridWidthPx(int safeContentWidthPx, int capacity) {
        int columns = Math.max(horizontalGridColumns(), DockPreferences.normalizeCapacity(capacity));
        return Math.max(0, safeContentWidthPx * horizontalGridColumns() / columns);
    }

    static int horizontalTileWidthPx(int safeContentWidthPx, int capacity, float density) {
        int gridWidth = horizontalGridWidthPx(safeContentWidthPx, capacity);
        return AppAdapter.tileWidthPx(gridWidth, horizontalGridColumns(), density);
    }

    static int safeHorizontalInsetPx(float density) {
        return Math.round(SAFE_HORIZONTAL_MARGIN_DP * density);
    }

    static int safeBottomInsetPx(float density) {
        return Math.round(SAFE_VERTICAL_MARGIN_DP * density);
    }

    static int horizontalContentWidthPx(int viewportWidthPx, float density) {
        return Math.max(0, viewportWidthPx - 2 * safeHorizontalInsetPx(density));
    }

    /** Center Dock slots whose widths match the five-column Grid cells. */
    static int horizontalCenterOffsetPx(int safeContentWidthPx, int slotWidthPx, int itemCount) {
        if (safeContentWidthPx <= 0 || slotWidthPx <= 0 || itemCount <= 0) return 0;
        return Math.max(0, (safeContentWidthPx - slotWidthPx * itemCount) / 2);
    }

    /** Symmetric inset between a centered card and its Dock/Grid slot. */
    static int dockItemSideOffsetPx(int safeContentWidthPx, int tileWidthPx, int capacity) {
        int slotWidth = horizontalSlotWidthPx(safeContentWidthPx, capacity);
        return Math.max(0, (slotWidth - tileWidthPx) / 2);
    }

    /** Compatibility overload for existing geometry callers. */
    static int horizontalCenterOffsetPx(int innerWidthPx, int tileWidthPx, int itemCount) {
        if (innerWidthPx <= 0 || tileWidthPx <= 0 || itemCount <= 0) return 0;
        return Math.max(0, (innerWidthPx - tileWidthPx * itemCount) / 2);
    }

    static int tileHeightPx(int tileWidthPx) {
        return AppAdapter.tileHeightPx(tileWidthPx);
    }

    static int itemHeightPx(int tileHeightPx, float density) {
        return AppAdapter.itemHeightPx(tileHeightPx, density);
    }

    static int horizontalDockHeightPx(int tileHeightPx, float density) {
        return itemHeightPx(tileHeightPx, density) + Math.round(DOCK_EXTRA_HEIGHT_DP * density);
    }

    /** Dock host extends below its glass tray so the tray itself lands on the P2 safe margin. */
    static int horizontalDockBottomMarginPx(int tileHeightPx, float density) {
        int dockHeight = horizontalDockHeightPx(tileHeightPx, density);
        int trayBottomInset = dockHeight - tileHeightPx
                - dockItemTopOffsetPx(density) - Math.round(TRAY_PADDING_DP * density);
        return safeBottomInsetPx(density) - trayBottomInset;
    }

    /** The Grid viewport ends one 8dp rhythm above the tray's top edge. */
    static int horizontalGridBottomMarginPx(int tileHeightPx, float density) {
        int dockHeight = horizontalDockHeightPx(tileHeightPx, density);
        int dockBottomMargin = horizontalDockBottomMarginPx(tileHeightPx, density);
        int trayTopOffset = dockItemTopOffsetPx(density)
                - Math.round(TRAY_PADDING_DP * density);
        int gridToTrayGap = Math.round(GRID_TO_TRAY_GAP_DP * density);
        return Math.max(0, dockHeight + dockBottomMargin - trayTopOffset + gridToTrayGap);
    }

    /** Shelf baseline sits 36dp above the tray; tray starts 14dp above the tile card. */
    static int shelfBottomOffsetFromTileTopPx(float density) {
        return Math.round((TRAY_PADDING_DP + SHELF_TO_TRAY_GAP_DP) * density);
    }

    static int horizontalGridBottomReservePx(int largestTileWidthPx, float density) {
        int largestTileHeight = tileHeightPx(largestTileWidthPx);
        return itemHeightPx(largestTileHeight, density) + Math.round(34f * density);
    }

    static int dockItemTopOffsetPx(float density) {
        return (int) (DOCK_ITEM_TOP_OFFSET_DP * density);
    }

    static int dockItemBottomOffsetPx(float density) {
        return (int) (DOCK_ITEM_BOTTOM_OFFSET_DP * density);
    }

    static float trayPaddingPx(float density) {
        return TRAY_PADDING_DP * density;
    }
}
