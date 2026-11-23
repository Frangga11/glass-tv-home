package com.gathin.tvhome;

import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;

/**
 * Process-wide owner of Glass TV Home's idle-to-manual-standby transition.
 *
 * Android remains responsible for scheduling its native DreamService and device power policy.
 * This coordinator only schedules the launcher's own in-window standby presentation.
 */
final class StandbyCoordinator {
    static final long IDLE_TIMEOUT_MS = 15L * 60L * 1000L;

    private static volatile StandbyCoordinator instance;

    static StandbyCoordinator get(android.content.Context context) {
        StandbyCoordinator current = instance;
        if (current == null) {
            synchronized (StandbyCoordinator.class) {
                current = instance;
                if (current == null) {
                    current = new StandbyCoordinator();
                    instance = current;
                }
            }
        }
        return current;
    }

    private final Handler handler = new Handler(Looper.getMainLooper());
    private WeakReference<MainActivity> home = new WeakReference<>(null);
    private boolean homeResumed;
    private boolean manualStandbyActive;
    private boolean nativeDreamActive;
    private Runnable pendingIdleTransition;

    private final Runnable idleTransition = new Runnable() {
        @Override public void run() {
            pendingIdleTransition = null;
            if (!homeResumed || manualStandbyActive || nativeDreamActive) return;
            MainActivity activity = home.get();
            if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;
            manualStandbyActive = true;
            activity.showNightstandScreensaver();
        }
    };

    private StandbyCoordinator() {
        // The coordinator stores only a weak reference to the Home Activity.
    }

    void bindHome(MainActivity activity) {
        if (activity == null) return;
        home = new WeakReference<>(activity);
    }

    void unbindHome(MainActivity activity) {
        if (home.get() != activity) return;
        onHomePaused();
        home.clear();
    }

    void onHomeResumed() {
        homeResumed = true;
        scheduleIfEligible();
    }

    void onHomePaused() {
        homeResumed = false;
        stopTimer();
    }

    void onUserInteraction() {
        if (!manualStandbyActive) scheduleIfEligible();
    }

    void onManualStandbyStarted() {
        manualStandbyActive = true;
        stopTimer();
    }

    void onManualStandbyDismissed() {
        manualStandbyActive = false;
        scheduleIfEligible();
    }

    void onDreamStarted() {
        nativeDreamActive = true;
        stopTimer();
    }

    void onDreamStopped() {
        nativeDreamActive = false;
        scheduleIfEligible();
    }

    private void scheduleIfEligible() {
        if (!homeResumed || manualStandbyActive || nativeDreamActive
                || home.get() == null) return;
        stopTimer();
        pendingIdleTransition = idleTransition;
        handler.postDelayed(pendingIdleTransition, IDLE_TIMEOUT_MS);
    }

    private void stopTimer() {
        if (pendingIdleTransition != null) {
            handler.removeCallbacks(pendingIdleTransition);
            pendingIdleTransition = null;
        }
    }
}
