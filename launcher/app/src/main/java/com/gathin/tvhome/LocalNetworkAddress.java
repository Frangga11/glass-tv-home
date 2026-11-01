package com.gathin.tvhome;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;

import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * Resolves the IPv4 address on Android's active default Wi-Fi/Ethernet network.
 * QR upload URLs must use the route Android actually selects, not an arbitrary
 * interface discovered by enumerating network adapters.
 */
public final class LocalNetworkAddress {
    private LocalNetworkAddress() {}

    public static String getActiveLanIpv4(Context context) {
        if (context == null) return null;
        try {
            Context app = context.getApplicationContext();
            ConnectivityManager manager = (ConnectivityManager)
                    app.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager == null) return null;

            Network active = manager.getActiveNetwork();
            if (active == null) return null;

            NetworkCapabilities capabilities = manager.getNetworkCapabilities(active);
            if (capabilities == null
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                    || !(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))) {
                return null;
            }

            LinkProperties properties = manager.getLinkProperties(active);
            if (properties == null) return null;

            for (LinkAddress link : properties.getLinkAddresses()) {
                InetAddress address = link.getAddress();
                if (isUsableLanIpv4(address, link.getPrefixLength())) {
                    return address.getHostAddress();
                }
            }
        } catch (SecurityException | RuntimeException ignored) {
            // A missing/inaccessible active LAN must never produce a fabricated QR address.
        }
        return null;
    }

    static boolean isUsableLanIpv4(InetAddress address, int prefixLength) {
        return address instanceof Inet4Address
                && !address.isAnyLocalAddress()
                && !address.isLoopbackAddress()
                && !address.isLinkLocalAddress()
                && !address.isMulticastAddress()
                && prefixLength >= 1
                && prefixLength <= 30;
    }
}
