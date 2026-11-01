package com.gathin.tvhome;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UploadSessionPolicyTest {
    @Test
    public void sessionExpiryUsesAnExclusiveDeadline() {
        assertFalse(UploadSessionPolicy.isExpired(99L, 100L));
        assertTrue(UploadSessionPolicy.isExpired(100L, 100L));
        assertTrue(UploadSessionPolicy.isExpired(101L, 100L));
    }

    @Test
    public void commitRequiresLiveServerConnectedClientAndUnexpiredSession() {
        assertTrue(UploadSessionPolicy.canCommit(true, true, 99L, 100L));
        assertFalse(UploadSessionPolicy.canCommit(false, true, 99L, 100L));
        assertFalse(UploadSessionPolicy.canCommit(true, false, 99L, 100L));
        assertFalse(UploadSessionPolicy.canCommit(true, true, 100L, 100L));
    }

    @Test
    public void transportFailureCanRetryOnlyWhileSessionIsLiveAndNotCancelled() {
        assertTrue(UploadSessionPolicy.shouldRetryAfterTransportFailure(false, false));
        assertFalse(UploadSessionPolicy.shouldRetryAfterTransportFailure(true, false));
        assertFalse(UploadSessionPolicy.shouldRetryAfterTransportFailure(false, true));
    }
}
