package com.gathin.tvhome;

/**
 * Single source of truth for the horizontal Dock's visual geometry.
 *
 * Horizontal Dock/Grid geometry shares the P2 safe content frame, a five-column
 * slot model, 6/12dp vertical decoration offsets, 14dp tray inset, 8dp Grid-to-tray
 * breathing room, and a 36dp shelf-to-tray gap. The Dock and Grid remain independent
 * RecyclerViews but derive their card and slot sizes from this class.
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

    /**
     * The Dock host is already inside activity_main's 56dp Home content inset.
     * Applying the old extra 48dp inset here double-inset the Dock and made its tray
     * visibly narrower than the left weather/clock and right prayer/ayah shelves.
     */
    static int horizontalContentWidthPx(int viewportWidthPx, float density) {
        return Math.max(0, viewportWidthPx);
    }

    /** One visible app row plus the RecyclerView's existing top breathing inset. */
    static int horizontalGridViewportHeightPx(int tileWidthPx, float density, int topPaddingPx) {
        int itemHeight = itemHeightPx(tileHeightPx(tileWidthPx), density);
        return Math.max(0, itemHeight + Math.max(0, topPaddingPx));
    }

    /**
     * Center Dock slots whose widths match the five-column Grid cells.
     * The offset is measured inside the safe content viewport, not the full screen.
     */
    static int horizontalCenterOffsetPx(int safeContentWidthPx, int slotWidthPx, int itemCount) {
        if (safeContentWidthPx <= 0 || slotWidthPx <= 0 || itemCount <= 0) return 0;
        return Math.max(0, (safeContentWidthPx - slotWidthPx * itemCount) / 2);
    }

    /**
     * Keeps the top-right controls inside the Home safe frame and clear of the clock/date block.
     * The Dock may contain fewer items than its configured capacity, so its last card is not
     * always a safe horizontal anchor for the controls.
     */
    static float clampTopRowTranslationPx(float requestedShiftPx, int topRowLeftPx, int topRowRightPx,
                                          int screenWidthPx, int safeLeftPx, int safeRightPx,
                                          int leftBlockRightPx, int minimumGapPx) {
        int minLeft = Math.max(Math.max(0, safeLeftPx), leftBlockRightPx + Math.max(0, minimumGapPx));
        int maxRight = Math.max(0, screenWidthPx - Math.max(0, safeRightPx));
        float minShift = minLeft - topRowLeftPx;
        float maxShift = maxRight - topRowRightPx;
        // If the controls cannot fit between the clock and the right safe edge, preserve the
        // right edge rather than allowing the buttons to be clipped off-screen.
        if (minShift > maxShift) return maxShift;
        return Math.max(minShift, Math.min(maxShift, requestedShiftPx));
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
