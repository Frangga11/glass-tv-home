package com.gathin.tvhome.installer;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * Manages clean, device-specific RSA keys for Wireless ADB installation.
 * Keys are dynamically generated on the user's phone and stored privately in internal app storage.
 * No developer laptop names, hostnames, or pre-shared keys are ever bundled or exposed.
 */
public final class AdbCrypto {
    private static final String KEY_FILE_NAME = "glass_tv_adb.key";
    private static final String PUB_FILE_NAME = "glass_tv_adb.pub";

    private AdbCrypto() {}

    /**
     * Retrieves or dynamically generates the RSA PrivateKey in the app's private internal storage.
     */
    public static synchronized PrivateKey getPrivateKey(Context context) {
        File keyFile = new File(context.getFilesDir(), KEY_FILE_NAME);
        if (!keyFile.exists()) {
            generateAndSaveKeyPair(context);
        }
        return loadPrivateKeyFromFile(keyFile);
    }

    /**
     * Retrieves or dynamically generates the ADB Public Key string in the app's private internal storage.
     * Formatted as: "<base64_adb_struct> GlassTV@<DeviceModel>"
     */
    public static synchronized String getPublicKeyAdbString(Context context) {
        File pubFile = new File(context.getFilesDir(), PUB_FILE_NAME);
        if (!pubFile.exists()) {
            generateAndSaveKeyPair(context);
        }
        return loadPublicKeyFromFile(pubFile);
    }

    /**
     * Generates a new 2048-bit RSA key pair and stores it in app internal storage.
     */
    private static void generateAndSaveKeyPair(Context context) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();

            RSAPublicKey pubKey = (RSAPublicKey) kp.getPublic();
            RSAPrivateKey privKey = (RSAPrivateKey) kp.getPrivate();

            // Save private key (PKCS#8 DER)
            File keyFile = new File(context.getFilesDir(), KEY_FILE_NAME);
            try (FileOutputStream fos = new FileOutputStream(keyFile)) {
                fos.write(privKey.getEncoded());
                fos.flush();
            }

            // Convert public key to ADB struct & save
            byte[] adbWire = convertToAdbPublicKey(pubKey);
            String b64 = Base64.getEncoder().encodeToString(adbWire);
            String identifier = getCleanDeviceIdentifier();
            String fullPubString = b64 + " GlassTV@" + identifier;

            File pubFile = new File(context.getFilesDir(), PUB_FILE_NAME);
            try (FileOutputStream fos = new FileOutputStream(pubFile)) {
                fos.write(fullPubString.getBytes("UTF-8"));
                fos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PrivateKey loadPrivateKeyFromFile(File keyFile) {
        try (FileInputStream fis = new FileInputStream(keyFile)) {
            byte[] data = new byte[(int) keyFile.length()];
            int read = 0;
            while (read < data.length) {
                int r = fis.read(data, read, data.length - read);
                if (r < 0) break;
                read += r;
            }
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(data));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String loadPublicKeyFromFile(File pubFile) {
        try (FileInputStream fis = new FileInputStream(pubFile)) {
            byte[] data = new byte[(int) pubFile.length()];
            int read = 0;
            while (read < data.length) {
                int r = fis.read(data, read, data.length - read);
                if (r < 0) break;
                read += r;
            }
            return new String(data, "UTF-8").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a Java RSAPublicKey into standard 524-byte ADB wire format.
     */
    public static byte[] convertToAdbPublicKey(RSAPublicKey pubKey) {
        BigInteger n = pubKey.getModulus();
        BigInteger e = pubKey.getPublicExponent();

        // 1. Calculate n0inv = -1 / N[0] mod 2^32
        BigInteger r32 = BigInteger.ONE.shiftLeft(32);
        BigInteger n0 = n.remainder(r32);
        BigInteger n0inv = r32.subtract(n0.modInverse(r32)).remainder(r32);

        // 2. Calculate RR = (2^2048)^2 mod N
        BigInteger r = BigInteger.ONE.shiftLeft(2048);
        BigInteger rr = r.multiply(r).remainder(n);

        // 3. Assemble 524-byte structure:
        // uint32_t words (64)
        // uint32_t n0inv
        // uint8_t modulus[256] (little-endian)
        // uint8_t rr[256] (little-endian)
        // uint32_t exponent (little-endian)
        ByteBuffer buf = ByteBuffer.allocate(524).order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(64); // words
        buf.putInt((int) n0inv.longValue());
        buf.put(toLittleEndian256(n));
        buf.put(toLittleEndian256(rr));
        buf.putInt(e.intValue());

        return buf.array();
    }

    private static byte[] toLittleEndian256(BigInteger b) {
        byte[] src = b.toByteArray();
        byte[] dest = new byte[256];
        int len = Math.min(src.length, 256);
        for (int i = 0; i < len; i++) {
            dest[i] = src[src.length - 1 - i];
        }
        return dest;
    }

    private static String getCleanDeviceIdentifier() {
        try {
            String model = Build.MODEL;
            if (model != null && !model.trim().isEmpty()) {
                return model.replaceAll("[^a-zA-Z0-9_-]", "_");
            }
        } catch (Throwable ignored) {
        }
        return "Android";
    }
}
