package com.gathin.tvhome;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Regression tests for OTA download completion and the shared QR upload timeout policy. */
public class NetworkFeedbackPolicyTest {
    @Test
    public void acceptsCompleteDownloadWithKnownLength() {
        assertTrue(NetworkFeedbackPolicy.isCompleteDownload(1024, 1024));
    }

    @Test
    public void rejectsEmptyOrTruncatedDownload() {
        assertFalse(NetworkFeedbackPolicy.isCompleteDownload(0, 0));
        assertFalse(NetworkFeedbackPolicy.isCompleteDownload(512, 1024));
        assertFalse(NetworkFeedbackPolicy.isCompleteDownload(0, 1024));
        assertFalse(NetworkFeedbackPolicy.isCompleteDownload(1, 0));
    }

    @Test
    public void acceptsNonEmptyDownloadWhenServerOmitsLength() {
        assertTrue(NetworkFeedbackPolicy.isCompleteDownload(512, -1));
    }

    @Test
    public void uploadStorageReservationPreservesMinimumFreeSpace() {
        long mb = 1024L * 1024L;
        assertTrue(NetworkFeedbackPolicy.canReserveUploadStorage(200 * mb, 60 * mb, 76 * mb, 64 * mb));
        assertFalse(NetworkFeedbackPolicy.canReserveUploadStorage(200 * mb, 60 * mb, 76 * mb + 1, 64 * mb));
        assertFalse(NetworkFeedbackPolicy.canReserveUploadStorage(100 * mb, 40 * mb, 1 * mb, 64 * mb));
    }

    @Test
    public void uploadStorageReservationRejectsInvalidOrOvercommittedValues() {
        assertFalse(NetworkFeedbackPolicy.canReserveUploadStorage(100, 101, 1, 0));
        assertFalse(NetworkFeedbackPolicy.canReserveUploadStorage(100, 0, 0, 0));
        assertFalse(NetworkFeedbackPolicy.canReserveUploadStorage(100, -1, 1, 0));
        assertFalse(NetworkFeedbackPolicy.canReserveUploadStorage(100, 0, 1, -1));
    }

    @Test
    public void uploadTimeoutsRemainSpecificToTheirOperations() {
        assertTrue(NetworkFeedbackPolicy.AVATAR_UPLOAD_CLIENT_IDLE_TIMEOUT_MS == 90_000);
        assertTrue(NetworkFeedbackPolicy.WALLPAPER_UPLOAD_CLIENT_IDLE_TIMEOUT_MS == 30_000);
        assertTrue(NetworkFeedbackPolicy.AVATAR_UPLOAD_CLIENT_IDLE_TIMEOUT_MS
                != NetworkFeedbackPolicy.WALLPAPER_UPLOAD_CLIENT_IDLE_TIMEOUT_MS);
        assertTrue(NetworkFeedbackPolicy.AVATAR_BROWSER_UPLOAD_TIMEOUT_MS == 240_000);
        assertTrue(NetworkFeedbackPolicy.WALLPAPER_BROWSER_UPLOAD_TIMEOUT_MS == 600_000);
        assertTrue(NetworkFeedbackPolicy.STANDBY_BROWSER_UPLOAD_TIMEOUT_MS == 600_000);
        assertTrue(NetworkFeedbackPolicy.AVATAR_BROWSER_UPLOAD_TIMEOUT_MS
                < NetworkFeedbackPolicy.STANDBY_BROWSER_UPLOAD_TIMEOUT_MS);
    }
}
