package com.gathin.tvhome;

import android.view.View;

/** Explicit two-dimensional D-pad focus graph for the V04 Quick Settings dialog. */
final class QuickSettingsFocusGraph {
    static final int WIFI = 0;
    static final int RESTART = 1;
    static final int LANGUAGE = 2;
    static final int POWER = 3;
    static final int CACHE = 4;
    static final int NEXT_PHOTO = 5;
    static final int INTERVAL = 6;
    static final int WALLPAPER_MANAGE = 7;
    static final int GLASS_SETTINGS = 8;
    static final int ANDROID_SETTINGS = 9;
    static final int CLOSE = 10;
    static final int COUNT = 11;

    static final int LEFT = 0;
    static final int RIGHT = 1;
    static final int UP = 2;
    static final int DOWN = 3;
    static final int NO_TARGET = -1;

    private QuickSettingsFocusGraph() { }

    static int target(int node, int direction) {
        switch (node) {
            case WIFI:
                if (direction == RIGHT) return RESTART;
                if (direction == UP) return CLOSE;
                if (direction == DOWN) return LANGUAGE;
                break;
            case RESTART:
                if (direction == LEFT) return WIFI;
                if (direction == UP) return CLOSE;
                if (direction == DOWN) return POWER;
                break;
            case LANGUAGE:
                if (direction == RIGHT) return POWER;
                if (direction == UP) return WIFI;
                if (direction == DOWN) return CACHE;
                break;
            case POWER:
                if (direction == LEFT) return LANGUAGE;
                if (direction == UP) return RESTART;
                if (direction == DOWN) return CACHE;
                break;
            case CACHE:
                if (direction == UP) return LANGUAGE;
                if (direction == DOWN) return WALLPAPER_MANAGE;
                break;
            case NEXT_PHOTO:
                if (direction == RIGHT) return INTERVAL;
                if (direction == UP) return WALLPAPER_MANAGE;
                if (direction == DOWN) return GLASS_SETTINGS;
                break;
            case INTERVAL:
                if (direction == LEFT) return NEXT_PHOTO;
                if (direction == UP) return WALLPAPER_MANAGE;
                if (direction == DOWN) return ANDROID_SETTINGS;
                break;
            case WALLPAPER_MANAGE:
                if (direction == UP) return CACHE;
                if (direction == DOWN) return NEXT_PHOTO;
                break;
            case GLASS_SETTINGS:
                if (direction == RIGHT) return ANDROID_SETTINGS;
                if (direction == UP) return NEXT_PHOTO;
                if (direction == DOWN) return CLOSE;
                break;
            case ANDROID_SETTINGS:
                if (direction == LEFT) return GLASS_SETTINGS;
                if (direction == UP) return INTERVAL;
                if (direction == DOWN) return CLOSE;
                break;
            case CLOSE:
                if (direction == UP) return ANDROID_SETTINGS;
                if (direction == DOWN) return WIFI;
                break;
            default:
                break;
        }
        return NO_TARGET;
    }

    static void connect(View[] controls) {
        if (controls == null || controls.length < COUNT) return;
        for (int node = 0; node < COUNT; node++) {
            View view = controls[node];
            if (view == null) continue;
            view.setNextFocusLeftId(resolveId(controls, target(node, LEFT), view));
            view.setNextFocusRightId(resolveId(controls, target(node, RIGHT), view));
            view.setNextFocusUpId(resolveId(controls, target(node, UP), view));
            view.setNextFocusDownId(resolveId(controls, target(node, DOWN), view));
        }
    }

    private static int resolveId(View[] controls, int target, View fallback) {
        if (target >= 0 && target < controls.length && controls[target] != null) {
            return controls[target].getId();
        }
        return fallback.getId();
    }
}
