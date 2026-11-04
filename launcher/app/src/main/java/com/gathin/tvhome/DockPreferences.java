package com.gathin.tvhome;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Persisted Dock presentation settings, independent from the main app-grid span count.
 * Layout and focus controllers must consume this model without changing MainActivity.COLUMNS.
 */
final class DockPreferences {
    enum Orientation { VERTICAL, HORIZONTAL }
    static final int DEFAULT_CAPACITY=5;
    static final String PREFS="dock_presentation";
    private static final String KEY_ORIENTATION="orientation", KEY_CAPACITY="capacity";
    final Orientation orientation;
    final int capacity;
    private DockPreferences(Orientation orientation,int capacity) { this.orientation=orientation; this.capacity=capacity; }

    static DockPreferences load(Context context) {
        SharedPreferences p=context.getSharedPreferences(PREFS,Context.MODE_PRIVATE);
        Orientation o;
        try { o=Orientation.valueOf(p.getString(KEY_ORIENTATION,Orientation.HORIZONTAL.name())); }
        catch(Exception ignored) { o=Orientation.HORIZONTAL; }
        int n=p.getInt(KEY_CAPACITY,DEFAULT_CAPACITY);
        if(n!=4 && n!=5 && n!=6) n=DEFAULT_CAPACITY;
        return new DockPreferences(o,n);
    }
    static boolean save(Context context,Orientation orientation,int capacity) {
        if(context==null || orientation==null || (capacity!=4 && capacity!=5 && capacity!=6)) return false;
        return context.getSharedPreferences(PREFS,Context.MODE_PRIVATE).edit()
            .putString(KEY_ORIENTATION,orientation.name()).putInt(KEY_CAPACITY,capacity).commit();
    }
    static boolean isDockPosition(int position,int capacity) { return position>=0 && position<capacity; }
}
