package com.gathin.tvhome;

/**
 * Single semantic token source for the global Frosted Silver Glass system.
 * Structural fallbacks remain opaque; translucent wallpaper glass is a separate presentation layer.
 */
final class TvUiTokens {
    // Cool silver structural surfaces and opaque fallbacks.
    static final int SHELL = 0xFF2C333B;
    static final int SURFACE = 0xFF35404A;
    static final int SURFACE_FOCUSED = 0xFF3F4B56;
    static final int SURFACE_SELECTED = 0xFF4C5C68;
    static final int SURFACE_IDLE = 0xFF2D3741;
    static final int INPUT_SURFACE = 0xFF333E48;
    static final int POPUP_SURFACE = 0xFF303A44;
    static final int DOCK_SURFACE = 0xFF242E38;
    static final int BORDER = 0xFF9EABB8;
    static final int DISABLED_SURFACE = 0x223F4A55;
    static final int DISABLED_BORDER = 0x559EABB8;
    static final int DISABLED_TEXT = 0x668B98A5;

    // Typography and accents.
    static final int TEXT_PRIMARY = 0xFFFFFFFF;
    static final int TEXT_SECONDARY = 0xFFE2E8EE;
    static final int ACCENT_SECTION = 0xFFD6E0E8;
    static final int ACCENT_BLUE = 0xFF4CA9C4;
    static final int ACCENT_CYAN = 0xFF00E5FF;
    // Warm Islamic accent is intentionally independent from the global silver/cyan theme.
    static final int ISLAMIC_GOLD = 0xFFFFC45A;
    static final int ISLAMIC_TEXT_SHADOW = 0xCC000000;
    static final int FOCUS_BORDER = ACCENT_CYAN;
    static final int FOCUS_HALO = 0x4000E5FF;
    static final int GLASS_VEIL_COLOR = 0x88343E48;

    // Persistent selected/active state is silver, never the same treatment as DPAD focus.
    static final int NAV_PROFILE_SELECTED = 0xFF46535F;
    static final int NAV_PROFILE_IDLE = 0xFF2B3540;
    static final int NAV_ITEM_SELECTED = 0xFF46535F;
    static final int NAV_ITEM_TEXT = 0xFFFFFFFF;
    static final int NAV_SELECTION_BORDER = 0x99DCE5ED;
    static final int NAV_ICON_TINT = TEXT_PRIMARY;

    static final int SUCCESS = 0xFF22C55E;
    static final int WARNING = 0xFFF59E0B;
    static final int DANGER = 0xFFEF4444;

    // TV typography scale (sp).
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

    // Geometry tokens remain unchanged; focus scale is deliberately neutral (no geometry change).
    static final float POPUP_RADIUS_DP = 24f;
    static final float CARD_RADIUS_DP = 18f;
    static final float ROW_RADIUS_DP = 14f;
    static final float NAV_ROW_RADIUS_DP = 14f;
    static final float NAV_ICON_RADIUS_DP = 10f;
    static final float NAV_ROW_HEIGHT_DP = 56f;
    static final float NAV_ROW_BOTTOM_MARGIN_DP = 4f;
    static final float NAV_FOCUS_SCALE = 1.0f;
    static final float CARD_FOCUS_SCALE = 1.0f;
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
