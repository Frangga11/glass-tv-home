package com.gathin.tvhome;

/** Android prerequisites for a Wi-Fi scan; never implies that a scan is guaranteed to run. */
final class WifiScanPolicy {
    enum BlockReason { NONE, PERMISSION, LOCATION, WIFI_DISABLED }

    private WifiScanPolicy() { }

    static BlockReason blockReason(boolean fineLocationGranted, boolean locationEnabled, boolean wifiEnabled) {
        if (!fineLocationGranted) return BlockReason.PERMISSION;
        if (!locationEnabled) return BlockReason.LOCATION;
        if (!wifiEnabled) return BlockReason.WIFI_DISABLED;
        return BlockReason.NONE;
    }

    static boolean mayRequestScan(boolean fineLocationGranted, boolean locationEnabled, boolean wifiEnabled) {
        return blockReason(fineLocationGranted, locationEnabled, wifiEnabled) == BlockReason.NONE;
    }
}
