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
    public void countsUsableHostsForDifferentPrefixLengths() {
        TvScanner.LocalNetwork slash23 =
                new TvScanner.LocalNetwork("192.168.1.10", 23, 0xC0A80000L, 0xC0A801FFL);
        TvScanner.LocalNetwork slash25 =
                new TvScanner.LocalNetwork("192.168.1.10", 25, 0xC0A80100L, 0xC0A8017FL);
        assertEquals(510, slash23.hostCount());
        assertEquals(126, slash25.hostCount());
    }

    @Test
    public void rejectsOversizedAndHostlessScanRangesByCount() {
        TvScanner.LocalNetwork slash22 =
                new TvScanner.LocalNetwork("192.168.1.10", 22, 0xC0A80000L, 0xC0A803FFL);
        TvScanner.LocalNetwork slash30 =
                new TvScanner.LocalNetwork("192.168.1.10", 30, 0xC0A80108L, 0xC0A8010BL);
        assertTrue(slash22.hostCount() > 512);
        assertEquals(2, slash30.hostCount());
    }
}
