package com.gathin.tvhome;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AvatarUploadSessionPolicyTest {
    @Test
    public void uploadCommitRequiresLiveSessionAndStrictlyPreExpiryTime() {
        assertTrue(AvatarUploadServer.isSessionOpen(true, 100L, 101L));
        assertFalse(AvatarUploadServer.isSessionOpen(false, 100L, 101L));
        assertFalse(AvatarUploadServer.isSessionOpen(true, 101L, 101L));
        assertFalse(AvatarUploadServer.isSessionOpen(true, 102L, 101L));
    }
}
