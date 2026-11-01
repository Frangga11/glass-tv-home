package com.gathin.tvhome;

/** Shared transport policy for user-facing network operations. Service/session lifetimes stay separate. */
final class NetworkFeedbackPolicy {
    static final int UPLOAD_CLIENT_IDLE_TIMEOUT_MS = 90_000;

    private NetworkFeedbackPolicy() {}

    static boolean isCompleteDownload(long receivedBytes, long declaredBytes) {
        return receivedBytes > 0 && (declaredBytes <= 0 || receivedBytes == declaredBytes);
    }
}
