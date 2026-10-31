package com.gathin.tvhome;

/**
 * Stable keys and validation helpers for restoring TV navigation after Activity/process recreation.
 * Keep this class free of Android view dependencies so its rules can be covered by JVM tests.
 */
final class NavigationState {
    static final String PREFS = "navigation_state";
    static final String HOME_FOCUS_KEY = "home_focus_component";
    static final String HOME_FOCUS_POSITION = "home_focus_position";
    static final String SETTINGS_TAB = "settings_tab";

    static final int DEFAULT_SETTINGS_TAB = 0;

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

    static int normalizeSettingsTab(int tab) {
        switch (tab) {
            case 0: case 1: case 2: case 3: case 4: case 5: case 7:
                return tab;
            default:
                return DEFAULT_SETTINGS_TAB;
        }
    }

}
