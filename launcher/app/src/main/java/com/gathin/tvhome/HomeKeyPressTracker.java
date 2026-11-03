package com.gathin.tvhome;

/** Android-free timing state for the Home-key double-press gesture. */
final class HomeKeyPressTracker {
    static final long DOUBLE_PRESS_WINDOW_MS = 350L;
    static final long SINGLE_PRESS_DELAY_MS = 400L;

    enum ReleaseResult {
        FIRST_PENDING,
        DOUBLE_PRESS,
        EXPIRED_SINGLE_AND_PENDING
    }

    private long firstReleaseAt = -1L;

    boolean hasPendingPress() {
        return firstReleaseAt >= 0L;
    }

    boolean isSecondPressCandidate(long eventTimeMs) {
        if (!hasPendingPress() || eventTimeMs < firstReleaseAt) return false;
        return eventTimeMs - firstReleaseAt <= DOUBLE_PRESS_WINDOW_MS;
    }

    long remainingSingleDelay(long nowMs) {
        if (!hasPendingPress()) return 0L;
        long elapsed = Math.max(0L, nowMs - firstReleaseAt);
        return Math.max(1L, SINGLE_PRESS_DELAY_MS - elapsed);
    }

    ReleaseResult registerShortRelease(long eventTimeMs) {
        if (hasPendingPress()) {
            long elapsed = eventTimeMs - firstReleaseAt;
            if (elapsed >= 0L && elapsed <= DOUBLE_PRESS_WINDOW_MS) {
                cancel();
                return ReleaseResult.DOUBLE_PRESS;
            }
            firstReleaseAt = eventTimeMs;
            return ReleaseResult.EXPIRED_SINGLE_AND_PENDING;
        }
        firstReleaseAt = eventTimeMs;
        return ReleaseResult.FIRST_PENDING;
    }

    boolean consumeExpiredSingle(long nowMs) {
        if (!hasPendingPress() || nowMs - firstReleaseAt <= DOUBLE_PRESS_WINDOW_MS) {
            return false;
        }
        cancel();
        return true;
    }

    void cancel() {
        firstReleaseAt = -1L;
    }
}
