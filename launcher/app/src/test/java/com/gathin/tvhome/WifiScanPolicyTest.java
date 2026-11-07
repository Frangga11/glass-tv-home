package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WifiScanPolicyTest {
    @Test public void permissionIsRequiredBeforeScanning() {
        assertEquals(WifiScanPolicy.BlockReason.PERMISSION,
                WifiScanPolicy.blockReason(false, true, true));
        assertFalse(WifiScanPolicy.mayRequestScan(false, true, true));
    }

    @Test public void locationMustBeEnabled() {
        assertEquals(WifiScanPolicy.BlockReason.LOCATION,
                WifiScanPolicy.blockReason(true, false, true));
        assertFalse(WifiScanPolicy.mayRequestScan(true, false, true));
    }

    @Test public void wifiMustBeEnabledWithoutAppOwnedRadioMutation() {
        assertEquals(WifiScanPolicy.BlockReason.WIFI_DISABLED,
                WifiScanPolicy.blockReason(true, true, false));
        assertFalse(WifiScanPolicy.mayRequestScan(true, true, false));
    }

    @Test public void allPrerequisitesOnlyAuthorizeAnAttemptNotGuaranteeResults() {
        assertEquals(WifiScanPolicy.BlockReason.NONE,
                WifiScanPolicy.blockReason(true, true, true));
        assertTrue(WifiScanPolicy.mayRequestScan(true, true, true));
    }
}
