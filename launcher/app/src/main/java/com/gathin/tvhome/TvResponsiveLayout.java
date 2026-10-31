package com.gathin.tvhome;

/** Pure sizing rules shared by TV surfaces that must adapt to the available viewport. */
final class TvResponsiveLayout {
    private static final float RECENT_CARD_WIDTH_DP = 420f;
    private static final float RECENT_CARD_HEIGHT_DP = 250f;
    private static final float RECENT_SIDE_PADDING_DP = 64f;
    private static final float RECENT_CARD_OVERLAP_DP = 120f;
    private static final float NIGHTSTAND_CONTENT_WIDTH_FRACTION = 0.90f;
    private static final float NIGHTSTAND_RESERVED_WIDTH_DP = 64f;
    private static final float CLOCK_GLYPH_WIDTH_EM = 3f;
    private static final float CLOCK_MIN_SP = 36f;
    private static final float CLOCK_MAX_SP = 110f;

    private TvResponsiveLayout() { }

    static int recentCardWidthPx(int viewportWidthPx, float density) {
        if (!(density > 0f) || Float.isInfinite(density) || Float.isNaN(density)) {
            return Math.max(1, viewportWidthPx);
        }
        int designWidth = Math.max(1, Math.round(RECENT_CARD_WIDTH_DP * density));
        if (viewportWidthPx <= 0) return designWidth;
        int sidePadding = Math.round(2f * RECENT_SIDE_PADDING_DP * density);
        int availableWidth = viewportWidthPx - sidePadding;
        return Math.max(1, Math.min(designWidth, availableWidth));
    }

    static int recentCardHeightPx(int cardWidthPx) {
        return Math.max(1, Math.round(cardWidthPx * RECENT_CARD_HEIGHT_DP / RECENT_CARD_WIDTH_DP));
    }

    static int recentCardOverlapPx(int cardWidthPx) {
        return Math.max(0, Math.round(cardWidthPx * RECENT_CARD_OVERLAP_DP / RECENT_CARD_WIDTH_DP));
    }

    static int nightstandContentWidthPx(int viewportWidthPx) {
        return Math.max(1, Math.round(viewportWidthPx * NIGHTSTAND_CONTENT_WIDTH_FRACTION));
    }

    static int nightstandClockMaxWidthPx(int viewportWidthPx, float density) {
        int reserved = density > 0f ? Math.round(NIGHTSTAND_RESERVED_WIDTH_DP * density) : 64;
        return Math.max(1, nightstandContentWidthPx(viewportWidthPx) - reserved);
    }

    static float nightstandClockTextSizeSp(int viewportWidthPx, float density) {
        if (viewportWidthPx <= 0 || !(density > 0f) || Float.isInfinite(density) || Float.isNaN(density)) {
            return CLOCK_MAX_SP;
        }
        float viewportWidthDp = viewportWidthPx / density;
        float availableWidthDp = Math.max(CLOCK_MIN_SP * CLOCK_GLYPH_WIDTH_EM,
                viewportWidthDp * NIGHTSTAND_CONTENT_WIDTH_FRACTION - NIGHTSTAND_RESERVED_WIDTH_DP);
        return Math.max(CLOCK_MIN_SP, Math.min(CLOCK_MAX_SP, availableWidthDp / CLOCK_GLYPH_WIDTH_EM));
    }
}