package com.gathin.tvhome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Builds a shuffled wallpaper cycle in which every item appears once before the next cycle. */
final class WallpaperShuffle {
    private WallpaperShuffle() {}

    static List<String> newCycle(List<String> fileNames, String previousFile, Random random) {
        List<String> cycle = new ArrayList<>(fileNames);
        Collections.shuffle(cycle, random);
        if (cycle.size() > 1 && cycle.get(0).equals(previousFile)) {
            int replacement = 1 + random.nextInt(cycle.size() - 1);
            Collections.swap(cycle, 0, replacement);
        }
        return cycle;
    }
}
