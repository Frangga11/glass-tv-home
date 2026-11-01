package com.gathin.tvhome.installer;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User-started, temporary HTTP server for downloading the installer APK over the
 * active phone LAN. It binds only to that LAN IPv4 address.
 */
public final class LocalApkServer {
    private static final String TAG = "LocalApkServer";
    private static final int MAX_ACTIVE_CLIENTS = 4;
    private static final int MAX_HEADER_BYTES = 8192;
    private static final int CLIENT_TIMEOUT_MS = 10000;
    private static final int[] PORT_CANDIDATES = {8080, 8888, 8081, 9090, 0};
    private static volatile ServerState activeState;

    private LocalApkServer() {}

    public interface ServerStateCallback {
        void onStarted(String url);
        void onStopped();
    }

    private static final class ServerState {
        final Context context;
        final ServerStateCallback callback;
        final ServerSocket listeningSocket;
        final String url;
        final Set<Socket> clients = Collections.synchronizedSet(new HashSet<>());
        final AtomicBoolean running = new AtomicBoolean(true);
        final AtomicBoolean stoppedCallbackSent = new AtomicBoolean(false);
        final Thread thread;

        ServerState(Context context, ServerStateCallback callback, ServerSocket socket, String url) {
            this.context = context;
            this.callback = callback;
            this.listeningSocket = socket;
            this.url = url;
            this.thread = new Thread(this::serve, "installer-apk-http-server");
            this.thread.setDaemon(true);
        }

        void start() {
            thread.start();
        }

        void stop() {
            running.set(false);
            try { listeningSocket.close(); } catch (Exception ignored) {}
            synchronized (clients) {
                for (Socket client : clients) {
                    try { client.close(); } catch (Exception ignored) {}
                }
                clients.clear();
            }
            thread.interrupt();
        }

        void serve() {
            try {
                boolean announce;
                synchronized (LocalApkServer.class) {
                    announce = activeState == this && running.get();
                }
                if (callback != null && announce) callback.onStarted(url);
                while (running.get() && !listeningSocket.isClosed()) {
                    Socket client;
                    try {
                        client = listeningSocket.accept();
                    } catch (Exception e) {
                        if (running.get()) Log.w(TAG, "HTTP accept failed", e);
                        break;
                    }
                    if (!running.get()) {
                        closeQuietly(client);
                        break;
                    }
                    boolean accepted;
                    synchronized (clients) {
                        accepted = running.get() && clients.size() < MAX_ACTIVE_CLIENTS;
                        if (accepted) clients.add(client);
                    }
                    if (!accepted) {
                        if (running.get()) writeError(client, 503, "Service Unavailable", "Server sedang sibuk. Coba lagi.");
                        closeQuietly(client);
                        continue;
                    }
                    try {
                        Thread worker = new Thread(() -> handleClient(this, client), "installer-apk-client");
                        worker.setDaemon(true);
                        worker.start();
                    } catch (Exception e) {
                        clients.remove(client);
                        closeQuietly(client);
                    }
                }
            } catch (Exception e) {
                if (running.get()) Log.w(TAG, "HTTP server stopped unexpectedly", e);
            } finally {
                stop();
                finish(this);
            }
        }
    }

    public static synchronized void start(Context context, int requestedPort, ServerStateCallback callback) {
        if (activeState != null && activeState.running.get()) return;
        if (context == null) {
            notifyStopped(callback);
            return;
        }

        String localIp = TvScanner.getLocalIpAddress(context);
        if (localIp == null || localIp.trim().isEmpty()) {
            Log.w(TAG, "No active Wi-Fi/Ethernet IPv4 address; APK server not started");
            notifyStopped(callback);
            return;
        }

        ServerSocket socket = bind(localIp, requestedPort);
        if (socket == null) {
            Log.e(TAG, "Could not bind installer server to active LAN interface");
            notifyStopped(callback);
            return;
        }

        String url = "http://" + localIp + ":" + socket.getLocalPort() + "/glass-tv.apk";
        ServerState state = new ServerState(context.getApplicationContext(), callback, socket, url);
        activeState = state;
        try {
            state.start();
        } catch (Exception e) {
            activeState = null;
            state.stop();
            Log.e(TAG, "Could not start installer server thread", e);
            notifyStopped(callback);
        }
    }

    private static ServerSocket bind(String ip, int requestedPort) {
        final InetAddress address;
        try {
            address = InetAddress.getByName(ip);
        } catch (Exception e) {
            return null;
        }
        int[] candidates = new int[PORT_CANDIDATES.length + 1];
        candidates[0] = requestedPort > 0 && requestedPort <= 65535 ? requestedPort : 8080;
        System.arraycopy(PORT_CANDIDATES, 0, candidates, 1, PORT_CANDIDATES.length);
        Set<Integer> attempted = new HashSet<>();
        for (int port : candidates) {
            if (port < 0 || port > 65535 || !attempted.add(port)) continue;
            ServerSocket candidate = null;
            try {
                candidate = new ServerSocket();
                candidate.setReuseAddress(true);
                candidate.bind(new InetSocketAddress(address, port), MAX_ACTIVE_CLIENTS);
                return candidate;
            } catch (Exception e) {
                closeQuietly(candidate);
            }
        }
        return null;
    }

    public static synchronized void stop() {
        ServerState state = activeState;
        activeState = null;
        if (state != null) state.stop();
    }

    public static boolean isRunning() {
        ServerState state = activeState;
        return state != null && state.running.get() && !state.listeningSocket.isClosed();
    }

    private static void finish(ServerState state) {
        boolean notify = false;
        synchronized (LocalApkServer.class) {
            if (activeState == state) {
                activeState = null;
                notify = true;
            }
        }
        if (notify) notifyStopped(state.callback);
    }

    private static void notifyStopped(ServerStateCallback callback) {
        if (callback != null) callback.onStopped();
    }

    private static void handleClient(ServerState state, Socket client) {
        try (Socket socket = client) {
            socket.setSoTimeout(CLIENT_TIMEOUT_MS);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            String requestLine = readLine(in, MAX_HEADER_BYTES);
            if (requestLine == null || requestLine.isEmpty()) {
                writeError(out, 400, "Bad Request", "Permintaan tidak valid.");
                return;
            }
            int total = requestLine.length() + 2;
            String header;
            while ((header = readLine(in, MAX_HEADER_BYTES - total)) != null && !header.isEmpty()) {
                total += header.length() + 2;
                if (total > MAX_HEADER_BYTES) {
                    writeError(out, 431, "Request Header Fields Too Large", "Header terlalu besar.");
                    return;
                }
            }
            if (header == null) {
                writeError(out, 400, "Bad Request", "Header permintaan tidak lengkap.");
                return;
            }

            String[] parts = requestLine.split(" ", -1);
            if (parts.length != 3 || !"HTTP/1.1".equals(parts[2]) && !"HTTP/1.0".equals(parts[2])) {
                writeError(out, 400, "Bad Request", "Format permintaan tidak valid.");
                return;
            }
            String path = parts[1];
            int query = path.indexOf('?');
            if (query >= 0) path = path.substring(0, query);

            if (isDownloadRequest(parts[0], path)) {
                sendApk(state.context, out);
            } else if (isLandingRequest(parts[0], path)) {
                sendLandingPage(out);
            } else if (!"GET".equals(parts[0])) {
                writeError(out, 405, "Method Not Allowed", "Metode tidak didukung.");
            } else {
                writeError(out, 404, "Not Found", "Halaman tidak ditemukan.");
            }
        } catch (Exception e) {
            if (state.running.get()) Log.d(TAG, "Client request ended: " + e.getMessage());
        } finally {
            state.clients.remove(client);
            closeQuietly(client);
        }
    }

    static boolean isDownloadRequest(String method, String path) {
        return "GET".equals(method) && "/glass-tv.apk".equals(path);
    }

    static boolean isLandingRequest(String method, String path) {
        return "GET".equals(method) && "/".equals(path);
    }

    private static String readLine(InputStream in, int maxBytes) throws Exception {
        if (maxBytes <= 0) throw new IllegalArgumentException("Header limit exceeded");
        ByteArrayOutputStream line = new ByteArrayOutputStream();
        int previous = -1;
        while (line.size() <= maxBytes) {
            int value = in.read();
            if (value == -1) return line.size() == 0 ? null : line.toString("UTF-8");
            if (previous == '\r' && value == '\n') {
                byte[] bytes = line.toByteArray();
                return new String(bytes, 0, Math.max(0, bytes.length - 1), StandardCharsets.UTF_8);
            }
            line.write(value);
            previous = value;
        }
        throw new IllegalArgumentException("HTTP header too large");
    }

    private static void sendApk(Context context, OutputStream out) throws Exception {
        File apk = AdbInstaller.getOrExtractApk(context);
        long size = apk.length();
        if (!apk.isFile() || size <= 0) {
            writeError(out, 500, "Internal Server Error", "Fail APK tidak tersedia.");
            return;
        }
        String header = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/vnd.android.package-archive\r\n"
                + "Content-Length: " + size + "\r\n"
                + "Content-Disposition: attachment; filename=\"glass-tv-home.apk\"\r\n"
                + "Cache-Control: no-store\r\nConnection: close\r\n\r\n";
        out.write(header.getBytes(StandardCharsets.UTF_8));
        try (InputStream apkIn = new FileInputStream(apk)) {
            byte[] buffer = new byte[16 * 1024];
            int read;
            while ((read = apkIn.read(buffer)) != -1) out.write(buffer, 0, read);
        }
        out.flush();
    }

    private static void sendLandingPage(OutputStream out) throws Exception {
        String html = "<!DOCTYPE html><html><head><meta charset='utf-8'><meta name='viewport' content='width=device-width,initial-scale=1'>"
                + "<title>Glass TV Home Installer</title>"
                + "<style>body{background:#0c0e14;color:#fff;font-family:sans-serif;text-align:center;padding:40px 20px}"
                + "h1{font-size:28px;margin-bottom:10px}p{color:#aaa;margin-bottom:30px}"
                + ".btn{background:#fff;color:#000;padding:16px 32px;font-size:18px;font-weight:bold;text-decoration:none;border-radius:12px;display:inline-block;box-shadow:0 4px 15px rgba(255,255,255,0.2)}"
                + "</style></head><body><h1>📺 Glass TV Home</h1>"
                + "<p>Muat turun fail pemasangan APK terus ke Android TV anda.</p>"
                + "<a href='/glass-tv.apk' class='btn'>📥 Muat Turun APK (v3.1.4)</a>"
                + "</body></html>";
        byte[] body = html.getBytes(StandardCharsets.UTF_8);
        String header = "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\n"
                + "Content-Length: " + body.length + "\r\nCache-Control: no-store\r\nConnection: close\r\n\r\n";
        out.write(header.getBytes(StandardCharsets.UTF_8));
        out.write(body);
        out.flush();
    }

    private static void writeError(Socket socket, int code, String reason, String message) {
        try { writeError(socket.getOutputStream(), code, reason, message); } catch (Exception ignored) {}
    }

    private static void writeError(OutputStream out, int code, String reason, String message) throws Exception {
        byte[] body = message.getBytes(StandardCharsets.UTF_8);
        String header = "HTTP/1.1 " + code + " " + reason + "\r\n"
                + "Content-Type: text/plain; charset=UTF-8\r\nContent-Length: " + body.length
                + "\r\nCache-Control: no-store\r\nConnection: close\r\n\r\n";
        out.write(header.getBytes(StandardCharsets.UTF_8));
        out.write(body);
        out.flush();
    }

    private static void closeQuietly(Socket socket) {
        if (socket == null) return;
        try { socket.close(); } catch (Exception ignored) {}
    }
}
