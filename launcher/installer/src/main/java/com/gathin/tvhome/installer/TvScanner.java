package com.gathin.tvhome.installer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.os.Looper;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Discovers the active Wi-Fi/Ethernet IPv4 network and scans its bounded subnet
 * for legacy Android Debug Bridge TCP endpoints (port 5555).
 */
public final class TvScanner {
    private static final int ADB_PORT = 5555;
    private static final int MAX_SCAN_HOSTS = 512;
    private static final int CONNECT_TIMEOUT_MS = 450;
    private static final long SCAN_DEADLINE_SECONDS = 8L;
    private static final class MainThread {
        static final Handler HANDLER = new Handler(Looper.getMainLooper());
    }

    private TvScanner() {}

    public interface ScanCallback {
        void onDeviceFound(String ip);
        void onScanFinished(List<String> devices);
    }

    public static final class ScanHandle {
        private final AtomicBoolean cancelled = new AtomicBoolean(false);
        private volatile ExecutorService executor;
        private volatile Thread coordinator;

        private ScanHandle() {}

        public void cancel() {
            if (cancelled.compareAndSet(false, true)) {
                ExecutorService current = executor;
                if (current != null) current.shutdownNow();
                Thread worker = coordinator;
                if (worker != null) worker.interrupt();
            }
        }

        private boolean isCancelled() {
            return cancelled.get();
        }
    }

    /** Active default network information; null means no safe LAN IPv4 was found. */
    static final class LocalNetwork {
        final String ip;
        final int prefixLength;
        final long networkAddress;
        final long broadcastAddress;

        LocalNetwork(String ip, int prefixLength, long networkAddress, long broadcastAddress) {
            this.ip = ip;
            this.prefixLength = prefixLength;
            this.networkAddress = networkAddress;
            this.broadcastAddress = broadcastAddress;
        }

        int hostCount() {
            long count = broadcastAddress - networkAddress - 1L;
            return count > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) Math.max(0L, count);
        }
    }

    public static String getLocalIpAddress(Context context) {
        LocalNetwork network = getLocalNetwork(context);
        return network == null ? null : network.ip;
    }

    static LocalNetwork getLocalNetwork(Context context) {
        if (context == null) return null;
        try {
            ConnectivityManager manager = (ConnectivityManager)
                    context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager == null) return null;
            Network active = manager.getActiveNetwork();
            if (active == null) return null;
            NetworkCapabilities caps = manager.getNetworkCapabilities(active);
            if (caps == null || caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                    || !(caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))) return null;

            LinkProperties links = manager.getLinkProperties(active);
            if (links == null) return null;
            for (LinkAddress link : links.getLinkAddresses()) {
                InetAddress address = link.getAddress();
                int prefix = link.getPrefixLength();
                if (!(address instanceof Inet4Address) || address.isLoopbackAddress()
                        || address.isLinkLocalAddress() || prefix < 1 || prefix > 30) continue;
                LocalNetwork candidate = createLocalNetwork(address.getHostAddress(), prefix);
                if (candidate != null) return candidate;
            }
        } catch (Exception ignored) {
            // A missing/inaccessible active LAN is reported as unavailable, never as a fabricated IP.
        }
        return null;
    }

    static LocalNetwork createLocalNetwork(String ipAddress, int prefixLength) {
        if (ipAddress == null || !ipAddress.matches("[0-9]{1,3}(\\.[0-9]{1,3}){3}")
                || prefixLength < 1 || prefixLength > 30) return null;
        try {
            String[] octets = ipAddress.split("\\.");
            byte[] bytes = new byte[4];
            for (int i = 0; i < 4; i++) {
                int value = Integer.parseInt(octets[i]);
                if (value < 0 || value > 255) return null;
                bytes[i] = (byte) value;
            }
            long ip = ipv4ToLong(bytes);
            long mask = (0xffffffffL << (32 - prefixLength)) & 0xffffffffL;
            long network = ip & mask;
            long broadcast = network | (~mask & 0xffffffffL);
            if (ip == network || ip == broadcast) return null;
            return new LocalNetwork(ipAddress, prefixLength, network, broadcast);
        } catch (Exception ignored) {
            return null;
        }
    }

    static long ipv4ToLong(byte[] bytes) {
        if (bytes == null || bytes.length != 4) throw new IllegalArgumentException("IPv4 requires 4 bytes");
        return ((bytes[0] & 0xffL) << 24) | ((bytes[1] & 0xffL) << 16)
                | ((bytes[2] & 0xffL) << 8) | (bytes[3] & 0xffL);
    }

    static String longToIpv4(long value) {
        return ((value >>> 24) & 0xff) + "." + ((value >>> 16) & 0xff) + "."
                + ((value >>> 8) & 0xff) + "." + (value & 0xff);
    }

    static int getScanHostCount(Context context) {
        LocalNetwork network = getLocalNetwork(context);
        return network == null ? -1 : network.hostCount();
    }

    public static ScanHandle scan(Context context, ScanCallback callback) {
        final ScanHandle handle = new ScanHandle();
        if (callback == null) return handle;
        final LocalNetwork local = getLocalNetwork(context);
        if (local == null || local.hostCount() < 1 || local.hostCount() > MAX_SCAN_HOSTS) {
            MainThread.HANDLER.post(() -> {
                if (!handle.isCancelled()) callback.onScanFinished(Collections.emptyList());
            });
            return handle;
        }

        Thread coordinator = new Thread(() -> {
            List<String> found = Collections.synchronizedList(new ArrayList<>());
            AtomicBoolean completed = new AtomicBoolean(false);
            Object callbackLock = new Object();
            int hosts = local.hostCount();
            ExecutorService pool = Executors.newFixedThreadPool(Math.min(32, hosts));
            handle.executor = pool;
            CountDownLatch latch = new CountDownLatch(hosts);
            List<Future<?>> futures = new ArrayList<>(hosts);
            try {
                for (long candidate = local.networkAddress + 1;
                     candidate < local.broadcastAddress && !handle.isCancelled(); candidate++) {
                    final String target = longToIpv4(candidate);
                    if (target.equals(local.ip)) {
                        latch.countDown();
                        continue;
                    }
                    futures.add(pool.submit(() -> {
                        try (Socket socket = new Socket()) {
                            if (!handle.isCancelled()) {
                                socket.connect(new InetSocketAddress(target, ADB_PORT), CONNECT_TIMEOUT_MS);
                                synchronized (callbackLock) {
                                    if (!handle.isCancelled() && !completed.get()) {
                                        found.add(target);
                                        MainThread.HANDLER.post(() -> {
                                            if (!handle.isCancelled()) callback.onDeviceFound(target);
                                        });
                                    }
                                }
                            }
                        } catch (Exception ignored) {
                        } finally {
                            latch.countDown();
                        }
                    }));
                }
                latch.await(SCAN_DEADLINE_SECONDS, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            } finally {
                for (Future<?> future : futures) {
                    if (!future.isDone()) future.cancel(true);
                }
                pool.shutdownNow();
                if (!handle.isCancelled()) {
                    synchronized (callbackLock) {
                        completed.set(true);
                        List<String> result;
                        synchronized (found) {
                            result = new ArrayList<>(found);
                        }
                        Collections.sort(result);
                        MainThread.HANDLER.post(() -> {
                            if (!handle.isCancelled()) callback.onScanFinished(result);
                        });
                    }
                }
            }
        }, "tv-network-scan");
        handle.coordinator = coordinator;
        coordinator.setDaemon(true);
        coordinator.start();
        return handle;
    }
}
