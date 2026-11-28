package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ClassicGliderBlendTest {
    @Test public void progressStartsAtSourceAndEndsAtDestination() {
        assertEquals(0f, ClassicGliderBlend.progress(100, 100, 300), 0.0001f);
        assertEquals(1f, ClassicGliderBlend.progress(300, 100, 300), 0.0001f);
    }

    @Test public void progressInterpolatesHalfwayInBothScrollDirections() {
        assertEquals(0.5f, ClassicGliderBlend.progress(200, 100, 300), 0.0001f);
        assertEquals(0.5f, ClassicGliderBlend.progress(200, 300, 100), 0.0001f);
    }

    @Test public void progressClampsOverscrollAndZeroDistance() {
        assertEquals(0f, ClassicGliderBlend.progress(50, 100, 300), 0.0001f);
        assertEquals(1f, ClassicGliderBlend.progress(350, 100, 300), 0.0001f);
        assertEquals(1f, ClassicGliderBlend.progress(120, 120, 120), 0.0001f);
    }

    @Test public void interpolationClampsAndUsesMidpoint() {
        assertEquals(10f, ClassicGliderBlend.interpolate(10f, 30f, 0f), 0.0001f);
        assertEquals(20f, ClassicGliderBlend.interpolate(10f, 30f, 0.5f), 0.0001f);
        assertEquals(30f, ClassicGliderBlend.interpolate(10f, 30f, 1f), 0.0001f);
        assertEquals(10f, ClassicGliderBlend.interpolate(10f, 30f, -1f), 0.0001f);
        assertEquals(30f, ClassicGliderBlend.interpolate(10f, 30f, 2f), 0.0001f);
    }
}
