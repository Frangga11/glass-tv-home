package com.gathin.tvhome;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.provider.Settings;

import org.junit.Test;

public class SystemSettingsAccessPolicyTest {

    @Test
    public void requiresPinForOnlyTheApprovedSystemPages() {
        assertTrue(SystemSettingsAccess.requiresParentPin(Settings.ACTION_SETTINGS));
        assertTrue(SystemSettingsAccess.requiresParentPin(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        assertTrue(SystemSettingsAccess.requiresParentPin(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        assertTrue(SystemSettingsAccess.requiresParentPin(Settings.ACTION_HOME_SETTINGS));
        assertTrue(SystemSettingsAccess.requiresParentPin(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS));
    }

    @Test
    public void doesNotRequirePinForApprovedExemptPages() {
        assertFalse(SystemSettingsAccess.requiresParentPin(Settings.ACTION_WIFI_SETTINGS));
        assertFalse(SystemSettingsAccess.requiresParentPin(Settings.ACTION_DISPLAY_SETTINGS));
        assertFalse(SystemSettingsAccess.requiresParentPin(Settings.ACTION_DREAM_SETTINGS));
        assertFalse(SystemSettingsAccess.requiresParentPin(null));
    }
}
