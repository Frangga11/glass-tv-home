package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class UploadHttpRequestTest {
    @Test
    public void parsesOriginFormPathAndQuery() {
        UploadHttpRequest request = UploadHttpRequest.parse(
                "POST /wallpaper/token/upload?name=photo.jpg HTTP/1.1");
        assertNotNull(request);
        assertEquals("POST", request.method);
        assertEquals("/wallpaper/token/upload", request.path);
        assertEquals("name=photo.jpg", request.query);
        assertEquals("HTTP/1.1", request.version);
    }

    @Test
    public void acceptsHttp10AndHttp11Only() {
        assertNotNull(UploadHttpRequest.parse("GET /avatar/token HTTP/1.0"));
        assertNotNull(UploadHttpRequest.parse("GET /avatar/token HTTP/1.1"));
        assertNull(UploadHttpRequest.parse("GET /avatar/token HTTP/2"));
    }

    @Test
    public void rejectsMalformedRequestLinesAndExtraTokens() {
        assertNull(UploadHttpRequest.parse(null));
        assertNull(UploadHttpRequest.parse(""));
        assertNull(UploadHttpRequest.parse("GET"));
        assertNull(UploadHttpRequest.parse("GET /avatar/token"));
        assertNull(UploadHttpRequest.parse("GET /avatar/token HTTP/1.1 EXTRA"));
        assertNull(UploadHttpRequest.parse("GET  /avatar/token HTTP/1.1"));
        assertNull(UploadHttpRequest.parse("GET /avatar/token  HTTP/1.1"));
        assertNull(UploadHttpRequest.parse("GET http://tv/avatar HTTP/1.1"));
        assertNull(UploadHttpRequest.parse("GET /avatar/token#fragment HTTP/1.1"));
        assertNull(UploadHttpRequest.parse("get /avatar/token HTTP/1.1"));
    }

    @Test
    public void matchesSessionPathBySegmentBoundary() {
        UploadHttpRequest request = UploadHttpRequest.parse(
                "POST /wallpaper/token/api/delete?file=a.jpg HTTP/1.1");
        assertTrue(request.isWithinPath("/wallpaper/token"));
        assertFalse(request.isWithinPath("/wallpaper/toke"));
        assertFalse(UploadHttpRequest.parse("GET /wallpaper/token-suffix HTTP/1.1")
                .isWithinPath("/wallpaper/token"));
    }
}
