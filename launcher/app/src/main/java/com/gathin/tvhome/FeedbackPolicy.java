package com.gathin.tvhome;

/** Pure policy helpers shared by the TV feedback layer and its JVM regression tests. */
final class FeedbackPolicy {
    private FeedbackPolicy() {}

    static int normalizeToastDuration(int duration) {
        return duration == 1 ? 1 : 0;
    }

    static boolean hasMessage(CharSequence message) {
        return message != null && message.toString().trim().length() > 0;
    }

    static boolean isActivityUsable(boolean finishing, boolean destroyed) {
        return !finishing && !destroyed;
    }
}
