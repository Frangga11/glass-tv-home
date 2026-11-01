package com.gathin.tvhome;

/** Pure policy for restoring AlarmManager work after a full Android boot. */
final class AzanBootRecoveryPolicy {
    static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

    interface Scheduler {
        void schedule(String zoneCode);
    }

    private AzanBootRecoveryPolicy() { }

    static boolean restore(String action, String zoneCode, Scheduler scheduler) {
        if (!ACTION_BOOT_COMPLETED.equals(action)
                || zoneCode == null
                || zoneCode.trim().isEmpty()
                || scheduler == null) {
            return false;
        }
        scheduler.schedule(zoneCode.trim());
        return true;
    }
}
