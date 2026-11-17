package com.gathin.tvhome;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AzanBootRecoveryPolicyTest {

    @Test
    public void bootCompletedRestoresScheduleForPersistedZone() {
        String[] scheduledZone = {null};

        boolean restored = AzanBootRecoveryPolicy.restore(
                AzanBootRecoveryPolicy.ACTION_BOOT_COMPLETED,
                "ID_PGA",
                zone -> scheduledZone[0] = zone);

        assertTrue(restored);
        assertEquals("ID_PGA", scheduledZone[0]);
    }

    @Test
    public void unrelatedBroadcastDoesNotScheduleAlarm() {
        boolean[] called = {false};

        boolean restored = AzanBootRecoveryPolicy.restore(
                "android.intent.action.TIME_CHANGED",
                "ID_PGA",
                zone -> called[0] = true);

        assertFalse(restored);
        assertFalse(called[0]);
    }

    @Test
    public void missingZoneDoesNotScheduleAlarm() {
        boolean[] called = {false};

        boolean restored = AzanBootRecoveryPolicy.restore(
                AzanBootRecoveryPolicy.ACTION_BOOT_COMPLETED,
                "  ",
                zone -> called[0] = true);

        assertFalse(restored);
        assertFalse(called[0]);
    }

    @Test
    public void missingSchedulerDoesNotClaimRecovery() {
        assertFalse(AzanBootRecoveryPolicy.restore(
                AzanBootRecoveryPolicy.ACTION_BOOT_COMPLETED,
                "ID_PGA",
                null));
    }
}
