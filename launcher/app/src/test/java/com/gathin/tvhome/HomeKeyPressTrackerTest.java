package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HomeKeyPressTrackerTest {
    @Test
    public void firstShortReleaseWaitsBeforeResolvingSinglePress() {
        HomeKeyPressTracker tracker = new HomeKeyPressTracker();

        assertEquals(HomeKeyPressTracker.ReleaseResult.FIRST_PENDING, tracker.registerShortRelease(1000L));
        assertTrue(tracker.hasPendingPress());
        assertFalse(tracker.consumeExpiredSingle(1350L));
        assertTrue(tracker.consumeExpiredSingle(1351L));
        assertFalse(tracker.hasPendingPress());
    }

    @Test
    public void secondShortReleaseInsideWindowIsDoublePress() {
        HomeKeyPressTracker tracker = new HomeKeyPressTracker();

        tracker.registerShortRelease(1000L);
        assertTrue(tracker.isSecondPressCandidate(1300L));
        assertEquals(HomeKeyPressTracker.ReleaseResult.DOUBLE_PRESS, tracker.registerShortRelease(1350L));
        assertFalse(tracker.hasPendingPress());
    }

    @Test
    public void lateSecondReleaseResolvesFirstAndStartsNewPendingPress() {
        HomeKeyPressTracker tracker = new HomeKeyPressTracker();

        tracker.registerShortRelease(1000L);
        assertEquals(
                HomeKeyPressTracker.ReleaseResult.EXPIRED_SINGLE_AND_PENDING,
                tracker.registerShortRelease(1351L)
        );
        assertTrue(tracker.hasPendingPress());
        assertEquals(400L, tracker.remainingSingleDelay(1351L));
        assertTrue(tracker.consumeExpiredSingle(1702L));
    }

    @Test
    public void canceledSecondPressCanResumeRemainingSingleDelay() {
        HomeKeyPressTracker tracker = new HomeKeyPressTracker();

        tracker.registerShortRelease(1000L);
        assertTrue(tracker.isSecondPressCandidate(1200L));
        assertEquals(200L, tracker.remainingSingleDelay(1200L));
        assertTrue(tracker.hasPendingPress());
        assertTrue(tracker.consumeExpiredSingle(1351L));
    }

    @Test
    public void cancelClearsPendingGesture() {
        HomeKeyPressTracker tracker = new HomeKeyPressTracker();

        tracker.registerShortRelease(1000L);
        tracker.cancel();

        assertFalse(tracker.hasPendingPress());
        assertFalse(tracker.isSecondPressCandidate(1100L));
        assertFalse(tracker.consumeExpiredSingle(1500L));
    }
}
