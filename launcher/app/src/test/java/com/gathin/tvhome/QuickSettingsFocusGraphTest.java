package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

public class QuickSettingsFocusGraphTest {
    @Test
    public void primaryRowsFollowReferenceOrderWithoutCloseFocus() {
        assertEquals(QuickSettingsFocusGraph.RESTART,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.WIFI, QuickSettingsFocusGraph.RIGHT));
        assertEquals(QuickSettingsFocusGraph.LANGUAGE,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.WIFI, QuickSettingsFocusGraph.DOWN));
        assertEquals(QuickSettingsFocusGraph.POWER,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.LANGUAGE, QuickSettingsFocusGraph.RIGHT));
        assertEquals(QuickSettingsFocusGraph.CACHE,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.POWER, QuickSettingsFocusGraph.DOWN));
        assertEquals(QuickSettingsFocusGraph.NO_TARGET,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.CLOSE, QuickSettingsFocusGraph.DOWN));
    }

    @Test
    public void wallpaperAndFullWidthSettingsAreVerticallyStacked() {
        assertEquals(QuickSettingsFocusGraph.WALLPAPER_MANAGE,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.CACHE, QuickSettingsFocusGraph.DOWN));
        assertEquals(QuickSettingsFocusGraph.NEXT_PHOTO,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.WALLPAPER_MANAGE, QuickSettingsFocusGraph.DOWN));
        assertEquals(QuickSettingsFocusGraph.INTERVAL,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.NEXT_PHOTO, QuickSettingsFocusGraph.RIGHT));
        assertEquals(QuickSettingsFocusGraph.GLASS_SETTINGS,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.INTERVAL, QuickSettingsFocusGraph.DOWN));
        assertEquals(QuickSettingsFocusGraph.ANDROID_SETTINGS,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.GLASS_SETTINGS, QuickSettingsFocusGraph.DOWN));
    }

    @Test
    public void dpadStopsAtTheTopAndBottomWithoutWrappingToClose() {
        assertEquals(QuickSettingsFocusGraph.NO_TARGET,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.WIFI, QuickSettingsFocusGraph.UP));
        assertEquals(QuickSettingsFocusGraph.NO_TARGET,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.ANDROID_SETTINGS, QuickSettingsFocusGraph.DOWN));
        assertEquals(QuickSettingsFocusGraph.NO_TARGET,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.CLOSE, QuickSettingsFocusGraph.UP));
    }

    @Test
    public void everyActionRemainsReachableFromWifi() {
        Set<Integer> visited = new HashSet<>();
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.add(QuickSettingsFocusGraph.WIFI);
        while (!queue.isEmpty()) {
            int current = queue.removeFirst();
            if (!visited.add(current)) continue;
            for (int direction = QuickSettingsFocusGraph.LEFT;
                 direction <= QuickSettingsFocusGraph.DOWN; direction++) {
                int next = QuickSettingsFocusGraph.target(current, direction);
                if (next >= 0 && next < QuickSettingsFocusGraph.ACTION_COUNT && !visited.contains(next)) {
                    queue.addLast(next);
                }
            }
        }
        assertEquals(QuickSettingsFocusGraph.ACTION_COUNT, visited.size());
    }
}
