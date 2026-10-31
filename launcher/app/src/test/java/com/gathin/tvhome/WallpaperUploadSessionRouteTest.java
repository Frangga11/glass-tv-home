package com.gathin.tvhome;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WallpaperUploadSessionRouteTest {
    private static final String SESSION = "/wallpaper/0123456789abcdef0123456789abcdef";

    @Test
    public void acceptsOnlyRequestsScopedToTheCurrentSession() {
        assertTrue(WallpaperUploadServer.isWallpaperSessionRequest(
                "GET " + SESSION + " HTTP/1.1", SESSION));
        assertTrue(WallpaperUploadServer.isWallpaperSessionRequest(
                "POST " + SESSION + "/upload HTTP/1.1", SESSION));
        assertTrue(WallpaperUploadServer.isWallpaperSessionRequest(
                "GET " + SESSION + "/api/list HTTP/1.1", SESSION));
        assertTrue(WallpaperUploadServer.isWallpaperSessionRequest(
                "POST " + SESSION + "/api/delete?file=a.jpg HTTP/1.1", SESSION));
        assertTrue(WallpaperUploadServer.isWallpaperSessionRequest(
                "OPTIONS " + SESSION + "/upload HTTP/1.1", SESSION));
    }

    @Test
    public void rejectsLegacyAndForeignSessionRoutes() {
        assertFalse(WallpaperUploadServer.isWallpaperSessionRequest(
                "GET / HTTP/1.1", SESSION));
        assertFalse(WallpaperUploadServer.isWallpaperSessionRequest(
                "POST /upload HTTP/1.1", SESSION));
        assertFalse(WallpaperUploadServer.isWallpaperSessionRequest(
                "POST /api/clear HTTP/1.1", SESSION));
        assertFalse(WallpaperUploadServer.isWallpaperSessionRequest(
                "GET /wallpaper/ffffffffffffffffffffffffffffffff HTTP/1.1", SESSION));
        assertFalse(WallpaperUploadServer.isWallpaperSessionRequest(
                "POST " + SESSION + "suffix/upload HTTP/1.1", SESSION));
    }

    @Test
    public void rejectsUnsupportedMethodsAndUnscopedOptions() {
        assertFalse(WallpaperUploadServer.isWallpaperSessionRequest(
                "DELETE " + SESSION + "/upload HTTP/1.1", SESSION));
        assertFalse(WallpaperUploadServer.isWallpaperSessionRequest(
                "OPTIONS /upload HTTP/1.1", SESSION));
    }
}
