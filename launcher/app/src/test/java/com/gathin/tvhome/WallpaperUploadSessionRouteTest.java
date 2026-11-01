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

    @Test
    public void acceptsOneValidContentLengthWithinLimit() {
        assertTrue(WallpaperUploadServer.parseContentLength(
                new String[]{"POST /upload HTTP/1.1", "Content-Length: 1"}, 60L) == 1L);
        assertTrue(WallpaperUploadServer.parseContentLength(
                new String[]{"Content-Length: 62914560"}, WallpaperUploadServer.MAX_UPLOAD_BYTES)
                == WallpaperUploadServer.MAX_UPLOAD_BYTES);
    }

    @Test
    public void rejectsMissingDuplicateMalformedAndOversizedContentLength() {
        assertTrue(WallpaperUploadServer.parseContentLength(
                new String[]{"POST /upload HTTP/1.1"}, 60L) < 0);
        assertTrue(WallpaperUploadServer.parseContentLength(
                new String[]{"Content-Length: 1", "content-length: 1"}, 60L) < 0);
        assertTrue(WallpaperUploadServer.parseContentLength(
                new String[]{"Content-Length: +1"}, 60L) < 0);
        assertTrue(WallpaperUploadServer.parseContentLength(
                new String[]{"Content-Length: 61"}, 60L) < 0);
        assertTrue(WallpaperUploadServer.parseContentLength(
                new String[]{"Content-Length: 0"}, 60L) < 0);
    }

    @Test
    public void rejectsTransferEncodingBecauseChunkedBodiesAreNotSupported() {
        assertTrue(WallpaperUploadServer.parseContentLength(
                new String[]{"Content-Length: 1", "Transfer-Encoding: chunked"}, 60L) < 0);
    }

    @Test
    public void matchesExactUploadRouteAndAllowsFilenameQueryParameter() {
        String uploadPath = SESSION + "/upload";
        assertTrue(WallpaperUploadServer.isRequestToPath(
                "POST " + uploadPath + "?name=photo.jpg HTTP/1.1", "POST", uploadPath));
        assertTrue(WallpaperUploadServer.isRequestToPath(
                "POST " + uploadPath + " HTTP/1.1", "POST", uploadPath));
        assertFalse(WallpaperUploadServer.isRequestToPath(
                "POST " + uploadPath + "evil HTTP/1.1", "POST", uploadPath));
        assertFalse(WallpaperUploadServer.isRequestToPath(
                "GET " + uploadPath + "?name=photo.jpg HTTP/1.1", "POST", uploadPath));
        assertFalse(WallpaperUploadServer.isRequestToPath(
                "POST " + uploadPath + "/extra HTTP/1.1", "POST", uploadPath));
    }
}
