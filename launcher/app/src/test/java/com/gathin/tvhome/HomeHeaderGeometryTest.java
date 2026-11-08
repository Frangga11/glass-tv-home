package com.gathin.tvhome;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HomeHeaderGeometryTest {
    @Test
    public void centersWithinMeasuredLeftAndRightContent() {
        assertEquals(700, HomeHeaderGeometry.centeredWidthPx(1920, 500, 1420, 700));
    }

    @Test
    public void preservesCenteredNavigationAt4kResolution() {
        assertEquals(1400, HomeHeaderGeometry.centeredWidthPx(3840, 1000, 2840, 1400));
    }

    @Test
    public void constrainsToNarrowerSideWithoutOverlappingControls() {
        assertEquals(280, HomeHeaderGeometry.centeredWidthPx(1280, 440, 780, 500));
    }

    @Test
    public void refusesImpossibleOrUnmeasuredGeometry() {
        assertEquals(0, HomeHeaderGeometry.centeredWidthPx(0, 100, 900, 500));
        assertEquals(0, HomeHeaderGeometry.centeredWidthPx(1280, 660, 620, 500));
    }
}
