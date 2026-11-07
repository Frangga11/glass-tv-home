package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class PinCredentialStoreTest {
    private Context context;
    private SharedPreferences prefs;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.getApplication();
        prefs = context.getSharedPreferences("child_mode_prefs", Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
    }

    @Test
    public void freshInstallKeepsDefaultPin0000WithoutForcingChange() {
        assertTrue(PinCredentialStore.isDefaultPin(context));
        assertTrue(PinCredentialStore.verifyPin(context, "0000"));
        assertFalse(PinCredentialStore.verifyPin(context, "1234"));
        assertTrue(PinCredentialStore.isDefaultPin(context));
    }

    @Test
    public void customLegacyPlaintextPinMigratesWithoutChangingItsValue() {
        prefs.edit().putString("security_pin", "2468").commit();

        assertTrue(PinCredentialStore.verifyPin(context, "2468"));
        assertFalse(PinCredentialStore.verifyPin(context, "0000"));
        assertFalse(prefs.contains("security_pin"));
        assertTrue(prefs.contains("security_pin_salt_v1"));
        assertTrue(prefs.contains("security_pin_hash_v1"));
        assertFalse(PinCredentialStore.isDefaultPin(context));
    }

    @Test
    public void legacyDefaultPinMigratesWithoutRequiringOwnerToChangeIt() {
        prefs.edit().putString("security_pin", "0000").commit();

        assertTrue(PinCredentialStore.verifyPin(context, "0000"));
        assertTrue(PinCredentialStore.isDefaultPin(context));
        assertFalse(prefs.contains("security_pin"));
        assertFalse(prefs.contains("security_pin_hash_v1"));
    }

    @Test
    public void changedPinPersistsAsSaltedHashAndSurvivesRepeatedFailures() {
        PinCredentialStore.setPin(context, "7391");

        assertTrue(PinCredentialStore.verifyPin(context, "7391"));
        assertFalse(PinCredentialStore.verifyPin(context, "0000"));
        assertFalse(PinCredentialStore.isDefaultPin(context));
        assertFalse(prefs.contains("security_pin"));

        // Failed attempts are intentionally consequence-free and never lock the PIN.
        for (int i = 0; i < 25; i++) {
            assertFalse(PinCredentialStore.verifyPin(context, "1111"));
        }
        assertTrue(PinCredentialStore.verifyPin(context, "7391"));
        assertEquals(3, prefs.getAll().size());
    }

    @Test
    public void eachPinChangeUsesANewSalt() {
        PinCredentialStore.setPin(context, "1357");
        String firstSalt = prefs.getString("security_pin_salt_v1", "");
        PinCredentialStore.setPin(context, "1357");
        String secondSalt = prefs.getString("security_pin_salt_v1", "");

        assertFalse(firstSalt.equals(secondSalt));
        assertTrue(PinCredentialStore.verifyPin(context, "1357"));
    }
}
