package com.gathin.tvhome.installer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TvScannerTest {
    @Test
    public void convertsIpv4AddressWithoutSignedByteErrors() {
        byte[] address = new byte[]{(byte) 192, (byte) 168, 1, 10};
        assertEquals(0xC0A8010AL, TvScanner.ipv4ToLong(address));
        assertEquals("192.168.1.10", TvScanner.longToIpv4(0xC0A8010AL));
    }

    @Test
    public void calculatesActualSubnetBoundariesForDifferentPrefixes() {
        TvScanner.LocalNetwork slash23 = TvScanner.createLocalNetwork("192.168.1.10", 23);
        TvScanner.LocalNetwork slash25 = TvScanner.createLocalNetwork("192.168.1.10", 25);
        assertEquals(0xC0A80000L, slash23.networkAddress);
        assertEquals(0xC0A801FFL, slash23.broadcastAddress);
        assertEquals(510, slash23.hostCount());
        assertEquals(0xC0A80100L, slash25.networkAddress);
        assertEquals(0xC0A8017FL, slash25.broadcastAddress);
        assertEquals(126, slash25.hostCount());
    }

    @Test
    public void rejectsInvalidAndOversizedSubnetInputs() {
        assertEquals(null, TvScanner.createLocalNetwork("192.168.1.10", 0));
        assertEquals(null, TvScanner.createLocalNetwork("192.168.1.10", 31));
        assertEquals(null, TvScanner.createLocalNetwork("192.168.999.10", 24));
        TvScanner.LocalNetwork slash22 = TvScanner.createLocalNetwork("192.168.1.10", 22);
        assertTrue(slash22.hostCount() > 512);
    }
}
