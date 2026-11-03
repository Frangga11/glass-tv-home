package com.gathin.tvhome;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AvatarUploadRouteTest {
    private static final String TOKEN = "0123456789abcdef0123456789abcdef";

    @Test
    public void matchesOnlyExactAvatarPageAndUploadPaths() {
        UploadHttpRequest page = UploadHttpRequest.parse(
                "GET /avatar/" + TOKEN + "?source=qr HTTP/1.1");
        assertTrue(page != null && AvatarUploadServer.isAvatarPagePath(page.path, TOKEN));

        UploadHttpRequest upload = UploadHttpRequest.parse(
                "POST /avatar-upload/" + TOKEN + " HTTP/1.1");
        assertTrue(upload != null && AvatarUploadServer.isAvatarUploadPath(upload.path, TOKEN));

        assertFalse(AvatarUploadServer.isAvatarPagePath("/avatar/" + TOKEN + "suffix", TOKEN));
        assertFalse(AvatarUploadServer.isAvatarUploadPath("/avatar-upload/" + TOKEN + "/extra", TOKEN));
        assertFalse(AvatarUploadServer.isAvatarUploadPath("/avatar/" + TOKEN, TOKEN));
    }

    @Test
    public void queryStringDoesNotChangeTheMatchedRoute() {
        UploadHttpRequest request = UploadHttpRequest.parse(
                "GET /avatar/" + TOKEN + "?unused=1 HTTP/1.1");
        assertTrue(request != null && AvatarUploadServer.isAvatarPagePath(request.path, TOKEN));
    }
}
