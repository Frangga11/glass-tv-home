package com.gathin.tvhome;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Device-wide parent PIN credential storage.
 *
 * The default remains 0000 until the owner changes it. Verification is deliberately
 * stateless: failed attempts never increment counters, sleep, lock, or disable access.
 */
final class PinCredentialStore {
    private static final String PREFS_NAME = "child_mode_prefs";
    private static final String LEGACY_KEY_PIN = "security_pin";
    private static final String KEY_SALT = "security_pin_salt_v1";
    private static final String KEY_HASH = "security_pin_hash_v1";
    private static final String DEFAULT_PIN = "0000";
    private static final int SALT_BYTES = 16;
    private static final int HASH_BITS = 256;
    private static final int ITERATIONS = 120_000;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PinCredentialStore() {}

    static synchronized boolean isDefaultPin(Context context) {
        if (context == null) return true;
        SharedPreferences prefs = prefs(context);
        migrateLegacyPin(prefs);
        if (prefs.contains(LEGACY_KEY_PIN)) {
            return DEFAULT_PIN.equals(prefs.getString(LEGACY_KEY_PIN, DEFAULT_PIN));
        }
        if (!prefs.contains(KEY_SALT) && !prefs.contains(KEY_HASH)) return true;
        return verifyStoredPin(prefs, DEFAULT_PIN);
    }

    static synchronized boolean verifyPin(Context context, String entered) {
        if (context == null || entered == null) return false;
        SharedPreferences prefs = prefs(context);
        migrateLegacyPin(prefs);
        if (!prefs.contains(KEY_SALT) && !prefs.contains(KEY_HASH)) {
            String legacyPin = prefs.contains(LEGACY_KEY_PIN)
                    ? prefs.getString(LEGACY_KEY_PIN, DEFAULT_PIN) : DEFAULT_PIN;
            return legacyPin != null && legacyPin.equals(entered);
        }
        return verifyStoredPin(prefs, entered);
    }

    static synchronized void setPin(Context context, String pin) {
        if (context == null || pin == null || pin.length() != 4) return;
        SharedPreferences prefs = prefs(context);
        writeCredential(prefs, pin);
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * One-time, lossless migration of the former plaintext preference. The default
     * value is represented by the implicit default and is not forced to change.
     */
    private static void migrateLegacyPin(SharedPreferences prefs) {
        if (!prefs.contains(LEGACY_KEY_PIN)) return;
        String legacyPin = prefs.getString(LEGACY_KEY_PIN, DEFAULT_PIN);
        if (legacyPin == null || legacyPin.length() != 4) return;

        if (DEFAULT_PIN.equals(legacyPin)) {
            prefs.edit().remove(LEGACY_KEY_PIN).commit();
        } else {
            writeCredential(prefs, legacyPin);
        }
    }

    private static void writeCredential(SharedPreferences prefs, String pin) {
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] hash;
        try {
            hash = derive(pin, salt);
        } catch (GeneralSecurityException exception) {
            // Preserve the previous credential if the platform crypto provider fails.
            return;
        }

        prefs.edit()
                .putString(KEY_SALT, Base64.encodeToString(salt, Base64.NO_WRAP))
                .putString(KEY_HASH, Base64.encodeToString(hash, Base64.NO_WRAP))
                .remove(LEGACY_KEY_PIN)
                .commit();
    }

    private static boolean verifyStoredPin(SharedPreferences prefs, String entered) {
        if (!prefs.contains(KEY_SALT) || !prefs.contains(KEY_HASH)) return false;
        try {
            byte[] salt = Base64.decode(prefs.getString(KEY_SALT, ""), Base64.NO_WRAP);
            byte[] expected = Base64.decode(prefs.getString(KEY_HASH, ""), Base64.NO_WRAP);
            if (salt.length != SALT_BYTES || expected.length != HASH_BITS / 8) return false;
            byte[] actual = derive(entered, salt);
            return MessageDigest.isEqual(expected, actual);
        } catch (IllegalArgumentException | GeneralSecurityException exception) {
            return false;
        }
    }

    private static byte[] derive(String pin, byte[] salt) throws GeneralSecurityException {
        PBEKeySpec spec = new PBEKeySpec(pin.toCharArray(), salt, ITERATIONS, HASH_BITS);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).getEncoded();
        } finally {
            spec.clearPassword();
        }
    }
}
