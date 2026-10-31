package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WallpaperShuffleTest {
    @Test
    public void eachWallpaperAppearsExactlyOncePerCycle() {
        List<String> source = Arrays.asList("a.jpg", "b.jpg", "c.jpg", "d.jpg", "e.jpg");
        List<String> cycle = WallpaperShuffle.newCycle(source, "c.jpg", new Random(42));
        assertEquals(source.size(), cycle.size());
        assertEquals(new HashSet<>(source), new HashSet<>(cycle));
        assertEquals(source.size(), new HashSet<>(cycle).size());
        assertNotEquals("The next cycle must not begin with the previous cycle's last image",
                "c.jpg", cycle.get(0));
    }

    @Test
    public void singleWallpaperRemainsValid() {
        List<String> cycle = WallpaperShuffle.newCycle(Arrays.asList("only.jpg"), "only.jpg", new Random(1));
        assertEquals(Arrays.asList("only.jpg"), cycle);
    }

    @Test
    public void emptyCatalogReturnsEmptyCycle() {
        assertTrue(WallpaperShuffle.newCycle(Arrays.asList(), null, new Random(1)).isEmpty());
    }
}
