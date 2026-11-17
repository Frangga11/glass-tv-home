package com.gathin.tvhome.installer;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LocalApkServerTest {
    @Test
    public void acceptsOnlyExactGetDownloadRoute() {
        assertTrue(LocalApkServer.isDownloadRequest("GET", "/glass-tv.apk"));
        assertFalse(LocalApkServer.isDownloadRequest("POST", "/glass-tv.apk"));
        assertFalse(LocalApkServer.isDownloadRequest("GET", "/glass-tv.apk/extra"));
        assertFalse(LocalApkServer.isDownloadRequest("GET", "/glass-tv.apk-malicious"));
        assertFalse(LocalApkServer.isDownloadRequest("GETTING", "/glass-tv.apk"));
    }

    @Test
    public void acceptsOnlyExactLandingRoute() {
        assertTrue(LocalApkServer.isLandingRequest("GET", "/"));
        assertFalse(LocalApkServer.isLandingRequest("POST", "/"));
        assertFalse(LocalApkServer.isLandingRequest("GET", "/other"));
    }
}
