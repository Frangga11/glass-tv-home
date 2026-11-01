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
    }

    @Test
    public void acceptsNonEmptyDownloadWhenServerOmitsLength() {
        assertTrue(NetworkFeedbackPolicy.isCompleteDownload(512, -1));
    }

    @Test
    public void uploadTimeoutsRemainSpecificToTheirOperations() {
        assertTrue(NetworkFeedbackPolicy.AVATAR_UPLOAD_CLIENT_IDLE_TIMEOUT_MS == 90_000);
        assertTrue(NetworkFeedbackPolicy.WALLPAPER_UPLOAD_CLIENT_IDLE_TIMEOUT_MS == 30_000);
        assertTrue(NetworkFeedbackPolicy.AVATAR_UPLOAD_CLIENT_IDLE_TIMEOUT_MS
                != NetworkFeedbackPolicy.WALLPAPER_UPLOAD_CLIENT_IDLE_TIMEOUT_MS);
    }
}
