package com.gathin.tvhome;

/**
 * Stable keys and validation helpers for restoring TV navigation after Activity/process recreation.
 * Keep this class free of Android view dependencies so its rules can be covered by JVM tests.
 */
final class NavigationState {
    static final String PREFS = "navigation_state";
    static final String HOME_FOCUS_KEY = "home_focus_component";
    static final String HOME_FOCUS_POSITION = "home_focus_position";
    static final String HOME_FOCUS_TOP_PILL = "home_focus_top_pill";
    static final String HOME_FOCUS_TOP_PILL_KEY = "home_focus_top_pill_key";
    static final String SETTINGS_TAB = "settings_tab";

    static final int TAB_PROFILE = 0;
    static final int TAB_HOME_SCREEN = 1;
    static final int TAB_APPEARANCE = 2;
    static final int TAB_STATUS_WEATHER = 3;
    static final int TAB_EFFECTS = 4;
    static final int TAB_STANDBY = 5;
    static final int TAB_DEVICE_SETTINGS = 7;
    static final int DEFAULT_SETTINGS_TAB = TAB_PROFILE;

    private NavigationState() { }

    static String normalizeFocusKey(String key) {
        if (key == null) return null;
        String value = key.trim();
        return value.isEmpty() || value.length() > 512 ? null : value;
    }

    static int resolveHomePosition(int keyPosition, int savedPosition, int itemCount) {
        if (itemCount <= 0) return -1;
        int candidate = keyPosition >= 0 ? keyPosition : savedPosition;
        return Math.max(0, Math.min(candidate, itemCount - 1));
    }

    static String normalizeTopPillKey(String key) {
        if ("inputs".equals(key) || "wifi".equals(key) || "assistant".equals(key)
                || "candy_box".equals(key) || "settings".equals(key)) return key;
        return null;
    }

    static int normalizeSettingsTab(int tab) {
        switch (tab) {
            case TAB_PROFILE:
            case TAB_HOME_SCREEN:
            case TAB_APPEARANCE:
            case TAB_STATUS_WEATHER:
            case TAB_EFFECTS:
            case TAB_STANDBY:
            case TAB_DEVICE_SETTINGS:
                return tab;
            default:
                return DEFAULT_SETTINGS_TAB;
        }
    }

}
