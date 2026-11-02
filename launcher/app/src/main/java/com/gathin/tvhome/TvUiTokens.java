package com.gathin.tvhome;

/**
 * B01-owned visual tokens for the shared Settings shell, tabs, subtabs and reusable card rows.
 * Keep values stable across project branches; intentional component-specific treatments should
 * reference these tokens rather than introducing near-duplicate colors or dimensions.
 */
final class TvUiTokens {
    static final int SHELL = 0xFF1E2026;
    static final int SURFACE = 0xFF272A33;
    static final int SURFACE_FOCUSED = 0xFF353844;
    static final int INPUT_SURFACE = 0xFF282A33;
    static final int TEXT_PRIMARY = 0xFFFFFFFF;
    static final int TEXT_SECONDARY = 0xFF8E929E;
    static final int ACCENT_SECTION = 0xFF7DD3FC;
    static final int BORDER = 0xFF2F323B;
    static final int FOCUS_BORDER = 0xFFFFFFFF;

    static final float CARD_RADIUS_DP = 12f;
    static final float CARD_TITLE_SP = 14f;
    static final float CARD_SUBTITLE_SP = 12f;
    static final float SECTION_LABEL_SP = 11.5f;
    static final float PAGE_HEADER_SP = 24f;
    static final float CARD_FOCUS_SCALE = 1.015f;
    static final int CARD_PADDING_HORIZONTAL_DP = 16;
    static final int CARD_PADDING_VERTICAL_DP = 10;
    static final int CARD_BOTTOM_MARGIN_DP = 8;

    private TvUiTokens() { }
}
