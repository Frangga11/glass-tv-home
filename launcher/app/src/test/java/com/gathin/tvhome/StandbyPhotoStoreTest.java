package com.gathin.tvhome;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StandbyPhotoStoreTest {

    @Test
    public void downsamplesFourKPhotoToTvResolution() {
        assertEquals(2, StandbyPhotoStore.calculateInSampleSize(3840, 2160, 1920, 1080));
    }

    @Test
    public void downsamplesPortraitPhotoWithoutKeepingFullPhoneResolution() {
        assertEquals(2, StandbyPhotoStore.calculateInSampleSize(3000, 4000, 1920, 1080));
    }

    @Test
    public void keepsSmallPhotoAtOriginalResolution() {
        assertEquals(1, StandbyPhotoStore.calculateInSampleSize(1280, 720, 1920, 1080));
    }

    @Test
    public void downsamplesEightKPhotoByFour() {
        assertEquals(4, StandbyPhotoStore.calculateInSampleSize(7680, 4320, 1920, 1080));
    }

    @Test
    public void invalidDimensionsFallBackToSafeSampleSize() {
        assertEquals(1, StandbyPhotoStore.calculateInSampleSize(0, 4000, 1920, 1080));
        assertEquals(1, StandbyPhotoStore.calculateInSampleSize(4000, 3000, 0, 1080));
    }
}
