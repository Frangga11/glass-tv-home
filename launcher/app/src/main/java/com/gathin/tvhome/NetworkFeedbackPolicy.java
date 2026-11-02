package com.gathin.tvhome;

/** Shared transport policy for user-facing network operations. Service/session lifetimes stay separate. */
final class NetworkFeedbackPolicy {
    static final int AVATAR_UPLOAD_CLIENT_IDLE_TIMEOUT_MS = 90_000;
    static final int WALLPAPER_UPLOAD_CLIENT_IDLE_TIMEOUT_MS = 30_000;
    static final int AVATAR_BROWSER_UPLOAD_TIMEOUT_MS = 240_000;
    static final int WALLPAPER_BROWSER_UPLOAD_TIMEOUT_MS = 600_000;
    static final int STANDBY_BROWSER_UPLOAD_TIMEOUT_MS = 600_000;

    private NetworkFeedbackPolicy() {}

    static boolean isCompleteDownload(long receivedBytes, long declaredBytes) {
        return receivedBytes > 0 && (declaredBytes < 0 || receivedBytes == declaredBytes);
    }
}
