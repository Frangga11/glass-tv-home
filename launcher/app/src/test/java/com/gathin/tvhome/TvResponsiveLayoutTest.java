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

    @Test
    public void appliesConservativeSafeAreaAtCommonTvResolution() {
        assertEquals(77, TvResponsiveLayout.safeInsetPx(1920));
        assertEquals(43, TvResponsiveLayout.safeInsetPx(1080));
        assertEquals(1766, TvResponsiveLayout.safeContentPx(1920));
        assertEquals(994, TvResponsiveLayout.safeContentPx(1080));
    }

    @Test
    public void safeAreaNeverConsumesTinyViewport() {
        assertEquals(1, TvResponsiveLayout.safeContentPx(1));
        assertEquals(2, TvResponsiveLayout.safeContentPx(2));
        assertEquals(0, TvResponsiveLayout.safeContentPx(0));
    }

    @Test
    public void clampsPreferredWidthToSafeArea() {
        assertEquals(1178, Math.min(1500, TvResponsiveLayout.safeContentPx(1280)));
        assertEquals(800, Math.min(800, TvResponsiveLayout.safeContentPx(1280)));
    }

    @Test
    public void popupHorizontalPositionStaysInsideSafeArea() {
        assertEquals(1343, TvResponsiveLayout.popupLeftPx(1840, 500, 1920));
        assertEquals(77, TvResponsiveLayout.popupLeftPx(-20, 500, 1920));
        assertEquals(0, TvResponsiveLayout.popupLeftPx(20, 300, 0));
    }

    @Test
    public void popupPrefersRequestedSideWhenItFitsAndOtherwiseUsesTheRoomierSide() {
        assertTrue(TvResponsiveLayout.shouldPlacePopupAbove(500, 700, 400, 900, true));
        assertTrue(!TvResponsiveLayout.shouldPlacePopupAbove(200, 700, 400, 900, true));
        assertTrue(TvResponsiveLayout.shouldPlacePopupAbove(300, 200, 500, 900, false));
        assertTrue(!TvResponsiveLayout.shouldPlacePopupAbove(0, 0, 400, 900, false));
    }

    @Test
    public void honorsLargeUserFontScaleWithoutClamping() {
        assertEquals(27f, TvResponsiveLayout.scaledTextSizeSp(18f, 1.5f), 0.001f);
        assertEquals(36f, TvResponsiveLayout.scaledTextSizeSp(18f, 2f), 0.001f);
        assertEquals(18f, TvResponsiveLayout.scaledTextSizeSp(18f, Float.NaN), 0.001f);
    }

    @Test
    public void suppliesComfortableDpadTargetSize() {
        assertEquals(112, TvResponsiveLayout.minDpadTargetPx(2f));
        assertEquals(56, TvResponsiveLayout.minDpadTargetPx(Float.NaN));
    }
}
