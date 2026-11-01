package com.gathin.tvhome;

/** Shared transport policy for user-facing network operations. Service/session lifetimes stay separate. */
final class NetworkFeedbackPolicy {
    static final int AVATAR_UPLOAD_CLIENT_IDLE_TIMEOUT_MS = 90_000;
    static final int WALLPAPER_UPLOAD_CLIENT_IDLE_TIMEOUT_MS = 30_000;
    static final int AVATAR_BROWSER_UPLOAD_TIMEOUT_MS = 240_000;
    static final int WALLPAPER_BROWSER_UPLOAD_TIMEOUT_MS = 600_000;
    static final int STANDBY_BROWSER_UPLOAD_TIMEOUT_MS = 600_000;
    static final int WALLPAPER_SESSION_LIFETIME_MS = 30 * 60 * 1000;
    static final int WALLPAPER_UPLOAD_REQUEST_DEADLINE_MS = 10 * 60 * 1000;
    static final int AVATAR_UPLOAD_REQUEST_DEADLINE_MS = 4 * 60 * 1000;

    private NetworkFeedbackPolicy() {}

    static int boundedReadTimeoutMs(long deadlineAtMs, int idleTimeoutMs, long nowMs) {
        if (idleTimeoutMs <= 0) return 0;
        long remaining = deadlineAtMs - nowMs;
        if (remaining <= 0) return 0;
        return (int) Math.max(1L, Math.min((long) idleTimeoutMs, remaining));
    }

    static boolean isSessionActive(boolean running, long expiresAtMs, long nowMs) {
        return running && expiresAtMs > 0L && nowMs < expiresAtMs;
    }

    static boolean isCompleteDownload(long receivedBytes, long declaredBytes) {
        return receivedBytes > 0 && (declaredBytes < 0 || receivedBytes == declaredBytes);
    }
}
