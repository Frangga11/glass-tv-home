package com.gathin.tvhome;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.InetAddress;

import org.junit.Test;

public class LocalNetworkAddressTest {
    private InetAddress address(String value) throws Exception {
        return InetAddress.getByName(value);
    }

    @Test
    public void acceptsUsableIpv4AddressesOnNormalLanPrefixes() throws Exception {
        assertTrue(LocalNetworkAddress.isUsableLanIpv4(address("192.168.1.20"), 24));
        assertTrue(LocalNetworkAddress.isUsableLanIpv4(address("10.10.4.8"), 23));
        assertTrue(LocalNetworkAddress.isUsableLanIpv4(address("172.16.0.9"), 16));
    }

    @Test
    public void rejectsLoopbackLinkLocalUnspecifiedAndMulticastAddresses() throws Exception {
        assertFalse(LocalNetworkAddress.isUsableLanIpv4(address("127.0.0.1"), 8));
        assertFalse(LocalNetworkAddress.isUsableLanIpv4(address("169.254.10.20"), 16));
        assertFalse(LocalNetworkAddress.isUsableLanIpv4(address("0.0.0.0"), 0));
        assertFalse(LocalNetworkAddress.isUsableLanIpv4(address("224.0.0.1"), 24));
    }

    @Test
    public void rejectsIpv6AndNonLanPrefixLengths() throws Exception {
        assertFalse(LocalNetworkAddress.isUsableLanIpv4(address("::1"), 64));
        assertFalse(LocalNetworkAddress.isUsableLanIpv4(address("192.168.1.20"), 0));
        assertFalse(LocalNetworkAddress.isUsableLanIpv4(address("192.168.1.20"), 31));
        assertFalse(LocalNetworkAddress.isUsableLanIpv4(address("192.168.1.20"), 32));
    }
}
