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
    private static final float GRID_TO_TRAY_GAP_DP = 16f;
    private static final float ITEM_EXTRA_HEIGHT_DP = 45f;

    private DockGeometry() { }

    static int horizontalTileWidthPx(int innerWidthPx, int capacity, float density) {
        return AppAdapter.tileWidthPx(innerWidthPx, DockPreferences.normalizeCapacity(capacity), density);
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
