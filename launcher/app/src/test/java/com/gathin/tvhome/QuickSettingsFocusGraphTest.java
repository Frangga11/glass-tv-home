package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

public class QuickSettingsFocusGraphTest {
    @Test
    public void horizontalAndVerticalPrimaryActionsHaveDeterministicNeighbors() {
        assertEquals(QuickSettingsFocusGraph.RESTART,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.WIFI, QuickSettingsFocusGraph.RIGHT));
        assertEquals(QuickSettingsFocusGraph.LANGUAGE,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.WIFI, QuickSettingsFocusGraph.DOWN));
        assertEquals(QuickSettingsFocusGraph.POWER,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.LANGUAGE, QuickSettingsFocusGraph.RIGHT));
        assertEquals(QuickSettingsFocusGraph.CACHE,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.POWER, QuickSettingsFocusGraph.DOWN));
    }

    @Test
    public void wallpaperAndSettingsSectionsRemainReachableByDpad() {
        assertEquals(QuickSettingsFocusGraph.INTERVAL,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.NEXT_PHOTO, QuickSettingsFocusGraph.RIGHT));
        assertEquals(QuickSettingsFocusGraph.NEXT_PHOTO,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.WALLPAPER_MANAGE, QuickSettingsFocusGraph.DOWN));
        assertEquals(QuickSettingsFocusGraph.GLASS_SETTINGS,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.NEXT_PHOTO, QuickSettingsFocusGraph.DOWN));
        assertEquals(QuickSettingsFocusGraph.ANDROID_SETTINGS,
                QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.GLASS_SETTINGS, QuickSettingsFocusGraph.RIGHT));
    }

    @Test
    public void everyVisibleControlIsReachableFromWifiAndBackCanReturnToRoot() {
        Set<Integer> visited = new HashSet<>();
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.add(QuickSettingsFocusGraph.WIFI);
        while (!queue.isEmpty()) {
            int current = queue.removeFirst();
            if (!visited.add(current)) continue;
            for (int direction = QuickSettingsFocusGraph.LEFT;
                 direction <= QuickSettingsFocusGraph.DOWN; direction++) {
                int next = QuickSettingsFocusGraph.target(current, direction);
                if (next != QuickSettingsFocusGraph.NO_TARGET && !visited.contains(next)) queue.addLast(next);
            }
        }
        assertEquals(QuickSettingsFocusGraph.COUNT, visited.size());
        assertTrue(QuickSettingsFocusGraph.target(QuickSettingsFocusGraph.CLOSE,
                QuickSettingsFocusGraph.DOWN) == QuickSettingsFocusGraph.WIFI);
    }
}
