package com.gathin.tvhome;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Persisted Dock presentation preferences.
 *
 * These values describe only the Dock. They must never be used as the
 * app-grid span count: MainActivity.COLUMNS remains the independent grid
 * geometry until the Dock renderer and focus model are migrated.
 */
final class DockPreferences {
    enum Orientation { VERTICAL, HORIZONTAL }

    static final int DEFAULT_CAPACITY = 5;
    static final String PREFS = "dock_presentation";
    private static final String KEY_ORIENTATION = "orientation";
    private static final String KEY_CAPACITY = "capacity";

    final Orientation orientation;
    final int capacity;

    private DockPreferences(Orientation orientation, int capacity) {
        this.orientation = orientation;
        this.capacity = capacity;
    }

    static DockPreferences load(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        Orientation orientation = normalizeOrientation(
                preferences.getString(KEY_ORIENTATION, Orientation.HORIZONTAL.name()));
        int capacity = normalizeCapacity(
                preferences.getInt(KEY_CAPACITY, DEFAULT_CAPACITY));
        return new DockPreferences(orientation, capacity);
    }

    static Orientation normalizeOrientation(String raw) {
        try {
            return Orientation.valueOf(raw);
        } catch (IllegalArgumentException | NullPointerException ignored) {
            return Orientation.HORIZONTAL;
        }
    }

    static int normalizeCapacity(int value) {
        return value == 4 || value == 5 || value == 6 ? value : DEFAULT_CAPACITY;
    }

    static boolean save(Context context, Orientation orientation, int capacity) {
        if (context == null || orientation == null
                || (capacity != 4 && capacity != 5 && capacity != 6)) {
            return false;
        }
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putString(KEY_ORIENTATION, orientation.name())
                .putInt(KEY_CAPACITY, capacity)
                .commit();
    }

    static boolean isDockPosition(int position, int capacity) {
        return position >= 0 && position < normalizeCapacity(capacity);
    }
}
