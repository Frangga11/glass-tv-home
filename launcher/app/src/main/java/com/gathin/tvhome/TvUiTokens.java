package com.gathin.tvhome;

/**
 * Single semantic token source for the shared Glass TV Home UI.
 *
 * The palette follows the approved blue-tinted glass direction. Opaque surfaces are intentional
 * fallbacks for dense content, daylight glare, and devices where backdrop glass is unavailable.
 * Feature-specific success/warning/error colors remain owned by their feature contracts.
 */
final class TvUiTokens {
    // Structural surfaces
    static final int SHELL = 0xFF071A2B;
    static final int SURFACE = 0xFF0C2942;
    static final int SURFACE_FOCUSED = 0xFF123F60;
    static final int SURFACE_SELECTED = 0xFF0E3552;
    static final int SURFACE_IDLE = 0xFF09243A;
    static final int INPUT_SURFACE = 0xFF0A263E;
    static final int POPUP_SURFACE = 0xFF0B2B46;
    static final int DOCK_SURFACE = 0xFF061D32;
    static final int BORDER = 0xFF416987;

    // Text and semantic accents
    static final int TEXT_PRIMARY = 0xFFF4FAFF;
    static final int TEXT_SECONDARY = 0xFFD0E0EE;
    static final int ACCENT_SECTION = 0xFF8ADFFF;
    static final int ACCENT_CYAN = 0xFF56E2FF;
    static final int FOCUS_BORDER = ACCENT_CYAN;
    static final int FOCUS_HALO = 0xFF071A2B;
    static final int NAV_PROFILE_SELECTED = 0xFF124465;
    static final int NAV_PROFILE_IDLE = 0xFF0A263D;
    static final int NAV_ITEM_SELECTED = 0xFF123B59;
    static final int NAV_ITEM_TEXT = 0xFFEAF5FF;
    static final int NAV_SELECTION_BORDER = 0x995BE6FF;
    static final int NAV_ICON_TINT = TEXT_PRIMARY;

    // TV typography scale (sp); content containers must wrap/scroll at larger user font scales.
    static final float PAGE_TITLE_SP = 32f;
    static final float PAGE_HEADER_SP = 24f;
    static final float SECTION_TITLE_SP = 22f;
    static final float PRIMARY_VALUE_SP = 22f;
    static final float BODY_SP = 18f;
    static final float NAV_ITEM_TEXT_SP = 16f;
    static final float CARD_TITLE_SP = 18f;
    static final float CARD_SUBTITLE_SP = 16f;
    static final float SECTION_LABEL_SP = 14f;
    static final float METADATA_SP = 14f;

    // Geometry and focus
    static final float POPUP_RADIUS_DP = 24f;
    static final float CARD_RADIUS_DP = 18f;
    static final float ROW_RADIUS_DP = 14f;
    static final float NAV_ROW_RADIUS_DP = 14f;
    static final float NAV_ICON_RADIUS_DP = 10f;
    static final float NAV_ROW_HEIGHT_DP = 52f;
    static final float NAV_ROW_BOTTOM_MARGIN_DP = 4f;
    static final float NAV_FOCUS_SCALE = 1.015f;
    static final float CARD_FOCUS_SCALE = 1.015f;
    static final float FOCUS_STROKE_DP = 3f;
    static final int FOCUS_ANIMATION_MS = 120;
    static final int NAV_ROW_PADDING_HORIZONTAL_DP = 16;
    static final int NAV_ROW_PADDING_VERTICAL_DP = 8;
    static final int NAV_ICON_CONTAINER_DP = 32;
    static final int NAV_ICON_GLYPH_DP = 20;
    static final int NAV_ICON_MARGIN_END_DP = 12;
    static final int CARD_PADDING_HORIZONTAL_DP = 20;
    static final int CARD_PADDING_VERTICAL_DP = 14;
    static final int CARD_BOTTOM_MARGIN_DP = 10;
    static final int MIN_DPAD_TARGET_DP = 56;

    private TvUiTokens() { }
}
