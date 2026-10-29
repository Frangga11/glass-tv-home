package com.gathin.tvhome.installer;

import org.junit.Test;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import javax.crypto.Cipher;

import static org.junit.Assert.*;

public class AdbCryptoTest {

    @Test
    public void testGenerateAdbPublicKeyFormat() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        RSAPublicKey pubKey = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey privKey = (RSAPrivateKey) kp.getPrivate();

        byte[] adbWire = AdbCrypto.convertToAdbPublicKey(pubKey);
        assertEquals(524, adbWire.length);

        ByteBuffer buf = ByteBuffer.wrap(adbWire).order(ByteOrder.LITTLE_ENDIAN);
        int words = buf.getInt();
        assertEquals(64, words);

        int n0inv = buf.getInt();
        assertNotEquals(0, n0inv);

        byte[] modBytes = new byte[256];
        buf.get(modBytes);

        byte[] rrBytes = new byte[256];
        buf.get(rrBytes);

        int exp = buf.getInt();
        assertEquals(65537, exp);

        // Verify signing
        byte[] token = new byte[20];
        token[0] = 0x12; token[1] = 0x34;

        byte[] asn1Token = new byte[35];
        byte[] prefix = new byte[]{0x30, 0x21, 0x30, 0x09, 0x06, 0x05, 0x2b, 0x0e, 0x03, 0x02, 0x1a, 0x05, 0x00, 0x04, 0x14};
        System.arraycopy(prefix, 0, asn1Token, 0, prefix.length);
        System.arraycopy(token, 0, asn1Token, prefix.length, 20);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, privKey);
        byte[] signature = cipher.doFinal(asn1Token);

        assertNotNull(signature);
        assertEquals(256, signature.length);
    }
}
