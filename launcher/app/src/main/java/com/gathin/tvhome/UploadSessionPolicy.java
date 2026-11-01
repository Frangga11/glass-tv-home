package com.gathin.tvhome;

/** Deterministic lifecycle rules shared by the local QR upload sessions. */
final class UploadSessionPolicy {
    static final long WALLPAPER_SESSION_DURATION_MS = 30L * 60L * 1000L;
    static final int WALLPAPER_ACCEPT_POLL_TIMEOUT_MS = 10_000;
    static final int WALLPAPER_CLIENT_IDLE_TIMEOUT_MS = 30_000;

    private UploadSessionPolicy() {}

    static boolean isExpired(long nowMillis, long expiresAtMillis) {
        return nowMillis >= expiresAtMillis;
    }

    static boolean canCommit(boolean serverRunning, boolean clientConnected,
                             long nowMillis, long expiresAtMillis) {
        return serverRunning && clientConnected && !isExpired(nowMillis, expiresAtMillis);
    }

    static boolean shouldRetryAfterTransportFailure(boolean sessionExpired, boolean cancelled) {
        return !sessionExpired && !cancelled;
    }
}
