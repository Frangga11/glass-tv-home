package com.gathin.tvhome;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AvatarUploadServerPolicyTest {
    private static final String TOKEN = "0123456789abcdef0123456789abcdef";

    @Test
    public void acceptsOnlyExactPageAndUploadSessionPaths() {
        assertTrue(AvatarUploadServer.isAvatarPagePath("/avatar/" + TOKEN, TOKEN));
        assertTrue(AvatarUploadServer.isAvatarUploadPath("/avatar-upload/" + TOKEN, TOKEN));

        assertFalse(AvatarUploadServer.isAvatarPagePath("/avatar/" + TOKEN + "/extra", TOKEN));
        assertFalse(AvatarUploadServer.isAvatarPagePath("/avatar/ffffffffffffffffffffffffffffffff", TOKEN));
        assertFalse(AvatarUploadServer.isAvatarUploadPath("/avatar-upload/" + TOKEN + "?x=1", TOKEN));
        assertFalse(AvatarUploadServer.isAvatarUploadPath("/upload", TOKEN));
        assertFalse(AvatarUploadServer.isAvatarUploadPath(null, TOKEN));
    }

    @Test
    public void enforcesUploadByteLimitAtExactBoundary() {
        assertFalse(AvatarUploadServer.isValidUploadLength(0));
        assertFalse(AvatarUploadServer.isValidUploadLength(-1));
        assertTrue(AvatarUploadServer.isValidUploadLength(1));
        assertTrue(AvatarUploadServer.isValidUploadLength(15L * 1024L * 1024L));
        assertFalse(AvatarUploadServer.isValidUploadLength(15L * 1024L * 1024L + 1));
    }

    @Test
    public void rejectsInvalidOrExcessiveImageDimensions() {
        assertFalse(AvatarUploadServer.isValidImageDimensions(0, 100));
        assertFalse(AvatarUploadServer.isValidImageDimensions(100, -1));
        assertFalse(AvatarUploadServer.isValidImageDimensions(12001, 100));
        assertFalse(AvatarUploadServer.isValidImageDimensions(100, 12001));
        assertFalse(AvatarUploadServer.isValidImageDimensions(10000, 8001));
        assertTrue(AvatarUploadServer.isValidImageDimensions(10000, 8000));
        assertTrue(AvatarUploadServer.isValidImageDimensions(12000, 1));
    }
}
