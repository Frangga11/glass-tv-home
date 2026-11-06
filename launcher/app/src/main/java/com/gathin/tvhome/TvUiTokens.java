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
    static final int SHELL = 0xFF0B2A5B;
    static final int SURFACE = 0xFF0C2E61;
    static final int SURFACE_FOCUSED = 0xFF0E3567;
    static final int SURFACE_SELECTED = 0xFF1A4D8B;
    static final int SURFACE_IDLE = 0xFF0A244E;
    static final int INPUT_SURFACE = 0xFF0D315F;
    static final int POPUP_SURFACE = 0xFF0B2A5B;
    static final int DOCK_SURFACE = 0xFF071E46;
    static final int BORDER = 0xFF426BAA;

    // Text and semantic accents
    static final int TEXT_PRIMARY = 0xFFFFFFFF;
    static final int TEXT_SECONDARY = 0xFFB6C6FF;
    static final int ACCENT_SECTION = 0xFFB6C6FF;
    static final int ACCENT_BLUE = 0xFF1E6BFF;
    static final int ACCENT_CYAN = 0xFF00E5FF;
    static final int FOCUS_BORDER = ACCENT_CYAN;
    static final int FOCUS_HALO = 0xFF0B2A5B;
    static final int GLASS_VEIL_COLOR = 0x990B2A5B;
    static final int NAV_PROFILE_SELECTED = 0xFF1E6BFF;
    static final int NAV_PROFILE_IDLE = 0xFF0A244E;
    static final int NAV_ITEM_SELECTED = 0xFF1E6BFF;
    static final int NAV_ITEM_TEXT = 0xFFFFFFFF;
    static final int NAV_SELECTION_BORDER = 0x9900E5FF;
    static final int NAV_ICON_TINT = TEXT_PRIMARY;
    static final int SUCCESS = 0xFF22C55E;
    static final int WARNING = 0xFFF59E0B;
    static final int DANGER = 0xFFEF4444;

    // TV typography scale (sp); content containers must wrap/scroll at larger user font scales.
    static final float PAGE_TITLE_SP = 36f;
    static final float PAGE_HEADER_SP = 28f;
    static final float SECTION_TITLE_SP = 22f;
    static final float PRIMARY_VALUE_SP = 22f;
    static final float BODY_SP = 18f;
    static final float NAV_ITEM_TEXT_SP = 18f;
    static final float CARD_TITLE_SP = 18f;
    static final float CARD_SUBTITLE_SP = 16f;
    static final float SECTION_LABEL_SP = 14f;
    static final float METADATA_SP = 12f;

    // Geometry and focus
    static final float POPUP_RADIUS_DP = 24f;
    static final float CARD_RADIUS_DP = 18f;
    static final float ROW_RADIUS_DP = 14f;
    static final float NAV_ROW_RADIUS_DP = 14f;
    static final float NAV_ICON_RADIUS_DP = 10f;
    static final float NAV_ROW_HEIGHT_DP = 56f;
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
