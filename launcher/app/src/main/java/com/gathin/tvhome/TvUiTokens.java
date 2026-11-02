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
    /** Opaque material fallbacks used when decorative glass is disabled. */
    static final int POPUP_SURFACE = 0xFF272A33;
    static final int DOCK_SURFACE = 0xFF1E2026;
    static final int FOCUS_BORDER = 0xFFFFFFFF;
    static final int NAV_PROFILE_SELECTED = 0xFF333742;
    static final int NAV_PROFILE_IDLE = 0xFF22242D;
    static final int NAV_ITEM_SELECTED = 0xFF30333D;
    static final int NAV_ITEM_TEXT = 0xFFE2E8F0;
    static final int NAV_SELECTION_BORDER = 0x44FFFFFF;
    static final int NAV_ICON_TINT = 0xFFFFFFFF;

    static final float NAV_ITEM_TEXT_SP = 13f;
    static final float NAV_ROW_RADIUS_DP = 10f;
    static final float NAV_ICON_RADIUS_DP = 8f;
    static final float NAV_ROW_HEIGHT_DP = 36f;
    static final float NAV_ROW_BOTTOM_MARGIN_DP = 2f;
    static final float NAV_FOCUS_SCALE = 1.015f;
    static final int NAV_ROW_PADDING_HORIZONTAL_DP = 10;
    static final int NAV_ROW_PADDING_VERTICAL_DP = 4;
    static final int NAV_ICON_CONTAINER_DP = 26;
    static final int NAV_ICON_GLYPH_DP = 15;
    static final int NAV_ICON_MARGIN_END_DP = 10;

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
