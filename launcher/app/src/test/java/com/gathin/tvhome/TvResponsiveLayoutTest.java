package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TvResponsiveLayoutTest {
    @Test
    public void preservesDesignCardSizeOnWideViewport() {
        assertEquals(630, TvResponsiveLayout.recentCardWidthPx(1920, 1.5f));
        assertEquals(375, TvResponsiveLayout.recentCardHeightPx(630));
        assertEquals(180, TvResponsiveLayout.recentCardOverlapPx(630));
    }

    @Test
    public void scalesRecentCardToFitNarrowViewport() {
        int width = TvResponsiveLayout.recentCardWidthPx(1280, 2.5f);
        assertEquals(960, width);
        assertEquals(571, TvResponsiveLayout.recentCardHeightPx(width));
        assertTrue(width + Math.round(128f * 2.5f) <= 1280);
    }

    @Test
    public void keepsCardWidthPositiveOnVeryNarrowViewport() {
        assertEquals(1, TvResponsiveLayout.recentCardWidthPx(200, 2.0f));
    }

    @Test
    public void retainsLargeClockOnNormalTvAndShrinksOnNarrowViewport() {
        assertEquals(110f, TvResponsiveLayout.nightstandClockTextSizeSp(1920, 1.5f), 0.01f);
        assertEquals(74.67f, TvResponsiveLayout.nightstandClockTextSizeSp(800, 2.5f), 0.02f);
        assertTrue(TvResponsiveLayout.nightstandClockTextSizeSp(800, 2.5f) < 110f);
    }

    @Test
    public void boundsNightstandContentAndClockWidth() {
        assertEquals(1152, TvResponsiveLayout.nightstandContentWidthPx(1280));
        assertEquals(992, TvResponsiveLayout.nightstandClockMaxWidthPx(1280, 2.5f));
    }
}