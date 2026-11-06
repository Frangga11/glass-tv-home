package com.gathin.tvhome;

/**
 * Pure responsive rules shared by TV surfaces.
 *
 * The 4% edge inset is a conservative software safe-area default, not a claim of final OEM
 * overscan calibration. Physical-device validation may increase the inset for a specific panel.
 */
final class TvResponsiveLayout {
    private static final float SAFE_INSET_FRACTION = 0.04f;
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

    static int safeInsetPx(int viewportDimensionPx) {
        if (viewportDimensionPx <= 1) return 0;
        return Math.min(Math.round(viewportDimensionPx * SAFE_INSET_FRACTION),
                (viewportDimensionPx - 1) / 2);
    }

    static int safeContentPx(int dimensionPx) {
        return dimensionPx <= 0 ? 0 : Math.max(1, dimensionPx - 2 * safeInsetPx(dimensionPx));
    }

    static int minDpadTargetPx(float density) {
        if (!(density > 0f) || Float.isInfinite(density) || Float.isNaN(density)) {
            return TvUiTokens.MIN_DPAD_TARGET_DP;
        }
        return Math.max(1, Math.round(TvUiTokens.MIN_DPAD_TARGET_DP * density));
    }

    /** Applies the user's font scale without imposing a ceiling that would defeat accessibility. */
    static float scaledTextSizeSp(float baseSp, float fontScale) {
        if (!(baseSp > 0f) || Float.isInfinite(baseSp) || Float.isNaN(baseSp)) return 0f;
        float scale = fontScale > 0f && !Float.isInfinite(fontScale) && !Float.isNaN(fontScale)
                ? fontScale : 1f;
        return baseSp * scale;
    }

    static int recentCardWidthPx(int viewportWidthPx, float density) {
        if (!(density > 0f) || Float.isInfinite(density) || Float.isNaN(density)) {
            return viewportWidthPx > 0 ? Math.max(1, Math.min(420, viewportWidthPx)) : 420;
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
