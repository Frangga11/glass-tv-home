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
    private static final String KEY_IS_DEFAULT = "security_pin_is_default_v1";
    private static final String DEFAULT_PIN = "0000";
    private static final int SALT_BYTES = 16;
    private static final int HASH_BITS = 256;
    private static final int ITERATIONS = 120_000;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PinCredentialStore() {}

    static synchronized boolean isDefaultPin(Context context) {
        if (context == null) return true;
        SharedPreferences prefs = prefs(context);
        try {
            if (prefs.contains(LEGACY_KEY_PIN)) {
                return DEFAULT_PIN.equals(prefs.getString(LEGACY_KEY_PIN, DEFAULT_PIN));
            }
            if (!prefs.contains(KEY_SALT) && !prefs.contains(KEY_HASH)) {
                // A metadata marker with no credential pair means stored data is corrupt,
                // not a fresh install. Never silently fall back to the default PIN.
                return !prefs.contains(KEY_IS_DEFAULT);
            }
            return prefs.getBoolean(KEY_IS_DEFAULT, false);
        } catch (ClassCastException exception) {
            // Corrupt preference types must not crash Settings or imply the default PIN.
            return false;
        }
    }

    static synchronized boolean verifyPin(Context context, String entered) {
        if (context == null || entered == null) return false;
        SharedPreferences prefs = prefs(context);
        try {
            if (prefs.contains(LEGACY_KEY_PIN)) {
                String legacyPin = prefs.getString(LEGACY_KEY_PIN, DEFAULT_PIN);
                if (legacyPin == null || !legacyPin.equals(entered)) return false;
                // Migrate only after a successful legacy verification. A failed disk write
                // leaves the old credential intact and usable on the next attempt.
                migrateLegacyPin(prefs);
                return true;
            }
            if (!prefs.contains(KEY_SALT) && !prefs.contains(KEY_HASH)) {
                // Fresh installs have no credential metadata. Once a PIN has been
                // explicitly stored, missing hash/salt must fail closed.
                return !prefs.contains(KEY_IS_DEFAULT) && DEFAULT_PIN.equals(entered);
            }
            return verifyStoredPin(prefs, entered);
        } catch (ClassCastException exception) {
            // Fail closed when persisted credential values have unexpected types.
            return false;
        }
    }

    static synchronized boolean setPin(Context context, String pin) {
        if (context == null || pin == null || !pin.matches("[0-9]{4}")) return false;
        SharedPreferences prefs = prefs(context);
        return writeCredential(prefs, pin);
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

    private static boolean writeCredential(SharedPreferences prefs, String pin) {
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] hash;
        try {
            hash = derive(pin, salt);
        } catch (GeneralSecurityException exception) {
            // Preserve the previous credential if the platform crypto provider fails.
            return false;
        }

        return prefs.edit()
                .putString(KEY_SALT, Base64.encodeToString(salt, Base64.NO_WRAP))
                .putString(KEY_HASH, Base64.encodeToString(hash, Base64.NO_WRAP))
                .putBoolean(KEY_IS_DEFAULT, DEFAULT_PIN.equals(pin))
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
