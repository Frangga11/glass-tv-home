package com.gathin.tvhome;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/** Short-lived, single-purpose local HTTP endpoint for replacing one profile avatar. */
public final class AvatarUploadServer {
    private static final String TAG = "TvHome.AvatarUpload";
    private static final long MAX_BYTES = 15L * 1024L * 1024L;
    private static final long SESSION_MS = 5L * 60L * 1000L;
    private static final int MAX_ACTIVE_CLIENTS = 4;

    public interface Listener { boolean onAvatarUploaded(File file); }

    private final Context context;
    private final Listener listener;
    private final String profileId;
    private final String token = UUID.randomUUID().toString().replace("-", "");
    private final long expiresAt = System.currentTimeMillis() + SESSION_MS;
    private ServerSocket server;
    private Thread thread;
    private volatile boolean running;
    private int port;
    private String boundIp;
    private final AtomicBoolean uploadClaimed = new AtomicBoolean(false);
    private final Set<Socket> activeClients = ConcurrentHashMap.newKeySet();
    private final AtomicInteger activeClientCount = new AtomicInteger(0);

    public AvatarUploadServer(Context context, String profileId, Listener listener) {
        this.context = context.getApplicationContext();
        this.profileId = profileId;
        this.listener = listener;
    }

    /** Reuse the existing Wi-Fi/Ethernet-first interface selection used by other QR uploads. */
    public static String getLocalIpAddress() {
        return WallpaperUploadServer.getLocalIpAddress();
    }

    public String getUrl() {
        return boundIp == null || port <= 0 ? null : "http://" + boundIp + ":" + port + "/avatar/" + token;
    }

    public synchronized boolean start() {
        if (running) return true;
        try {
            boundIp = getLocalIpAddress();
            if (boundIp == null || boundIp.trim().isEmpty() || "127.0.0.1".equals(boundIp)) return false;
            InetAddress bindAddress = InetAddress.getByName(boundIp);
            int[] ports = new int[]{8080, 8888, 8081, 9090, 0};
            for (int candidate : ports) {
                try {
                    server = new ServerSocket();
                    server.setReuseAddress(true);
                    server.bind(new InetSocketAddress(bindAddress, candidate));
                    port = server.getLocalPort();
                    break;
                } catch (Exception e) {
                    try { if (server != null) server.close(); } catch (Exception ignored) {}
                    server = null;
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Cannot bind avatar service to local network interface", e);
            server = null;
        }
        if (server == null) return false;
        running = true;
        final ServerSocket listeningSocket = server;
        thread = new Thread(() -> {
            while (running && !listeningSocket.isClosed()) {
                try {
                    Socket client = listeningSocket.accept();
                    if (!running) { client.close(); break; }
                    int active = activeClientCount.incrementAndGet();
                    if (active > MAX_ACTIVE_CLIENTS) {
                        activeClientCount.decrementAndGet();
                        try { respond(client.getOutputStream(), 503, "Layanan sedang sibuk. Coba lagi."); } catch (Exception ignored) {}
                        try { client.close(); } catch (Exception ignored) {}
                        continue;
                    }
                    activeClients.add(client);
                    Thread worker = new Thread(() -> handle(client), "avatar-upload-client");
                    worker.setDaemon(true);
                    worker.start();
                } catch (Exception e) {
                    if (running) Log.w(TAG, "Accept failed", e);
                }
            }
        }, "avatar-upload-server");
        thread.setDaemon(true);
        thread.start();
        return true;
    }

    public synchronized void stop() {
        running = false;
        try { if (server != null) server.close(); } catch (Exception ignored) {}
        if (thread != null) thread.interrupt();
        thread = null;
        for (Socket client : activeClients) {
            try { client.close(); } catch (Exception ignored) {}
        }
        activeClients.clear();
        server = null;
        boundIp = null;
        port = 0;
    }

    private void handle(Socket socket) {
        File temp = null;
        boolean claimed = false;
        boolean uploadSucceeded = false;
        try (Socket client = socket) {
            client.setSoTimeout(90000);
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            ByteArrayOutputStream header = new ByteArrayOutputStream();
            int state = 0, value;
            while ((value = in.read()) != -1) {
                header.write(value);
                if (header.size() > 8192) { respond(out, 431, "Header terlalu besar."); return; }
                if ((state == 0 && value == 13) || (state == 2 && value == 13)) state++;
                else if ((state == 1 && value == 10) || (state == 3 && value == 10)) state++;
                else state = value == 13 ? 1 : 0;
                if (state == 4) break;
            }
            String[] lines = header.toString("UTF-8").split("\\r\\n");
            if (lines.length == 0 || System.currentTimeMillis() >= expiresAt || !running) {
                respond(out, 410, "Sesi upload sudah berakhir. Buat QR baru dari TV."); return;
            }
            String[] request = lines[0].split(" ");
            if (request.length < 2) { respond(out, 400, "Permintaan tidak valid."); return; }
            String expectedUpload = "/avatar-upload/" + token;
            if ("GET".equals(request[0]) && isAvatarPagePath(request[1], token)) {
                byte[] page = page(expectedUpload).getBytes(StandardCharsets.UTF_8);
                out.write(("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\nContent-Length: " + page.length
                        + "\r\nCache-Control: no-store\r\nX-Content-Type-Options: nosniff\r\nReferrer-Policy: no-referrer\r\n"
                        + "Content-Security-Policy: default-src 'none'; style-src 'unsafe-inline'; script-src 'unsafe-inline'; base-uri 'none'; form-action 'none'\r\n"
                        + "Connection: close\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                out.write(page); out.flush(); return;
            }
            if (!"POST".equals(request[0]) || !isAvatarUploadPath(request[1], token)) {
                respond(out, 404, "Tidak ditemukan."); return;
            }
            if (!uploadClaimed.compareAndSet(false, true)) {
                respond(out, 409, "QR ini sudah digunakan. Tutup dialog dan buat QR baru jika ingin mengganti foto lagi.");
                return;
            }
            claimed = true;
            long length = -1;
            int lengthHeaders = 0;
            for (String line : lines) {
                String lower = line.toLowerCase(Locale.ROOT);
                if (lower.startsWith("transfer-encoding:")) {
                    respond(out, 400, "Format unggahan tidak didukung."); return;
                }
                if (lower.startsWith("content-length:")) {
                    lengthHeaders++;
                    try { length = Long.parseLong(line.substring(15).trim()); }
                    catch (Exception ignored) { length = -1; }
                }
            }
            if (lengthHeaders != 1 || length <= 0) { respond(out, 400, "Ukuran unggahan tidak valid."); return; }
            if (!isValidUploadLength(length)) { respond(out, 413, "Foto maksimal 15 MB."); return; }
            File dir = new File(context.getFilesDir(), "profile_avatars");
            if (!dir.exists() && !dir.mkdirs()) { respond(out, 500, "Penyimpanan avatar tidak tersedia."); return; }
            String safeId = profileId == null ? "" : profileId.replaceAll("[^a-zA-Z0-9_-]", "_");
            if (safeId.isEmpty()) { respond(out, 400, "Profil tidak valid."); return; }
            temp = File.createTempFile("avatar_" + safeId + "_", ".upload", dir);
            try (FileOutputStream fos = new FileOutputStream(temp)) {
                byte[] buffer = new byte[8192]; long remaining = length;
                while (remaining > 0) {
                    int read = in.read(buffer, 0, (int)Math.min(buffer.length, remaining));
                    if (read < 0) break;
                    fos.write(buffer, 0, read); remaining -= read;
                }
                fos.flush();
                fos.getFD().sync(); // Make the photo durable before committing its path into profile preferences.
                if (remaining != 0 || temp.length() != length) { respond(out, 400, "Unggahan tidak lengkap."); return; }
            }
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(temp.getAbsolutePath(), bounds);
            if (!isValidImageDimensions(bounds.outWidth, bounds.outHeight)) {
                respond(out, 415, "Berkas bukan gambar yang didukung atau resolusi terlalu besar."); return;
            }
            // Bounds alone can accept a truncated or malformed image. Decode a small sample to
            // verify that the platform decoder can actually read the photo before replacing anything.
            BitmapFactory.Options verify = new BitmapFactory.Options();
            verify.inSampleSize = 1;
            while (bounds.outWidth / (verify.inSampleSize * 2) > 512
                    || bounds.outHeight / (verify.inSampleSize * 2) > 512) verify.inSampleSize *= 2;
            verify.inPreferredConfig = android.graphics.Bitmap.Config.RGB_565;
            android.graphics.Bitmap verifiedBitmap = BitmapFactory.decodeFile(temp.getAbsolutePath(), verify);
            if (verifiedBitmap == null) {
                respond(out, 415, "Foto rusak atau format gambar tidak didukung."); return;
            }
            verifiedBitmap.recycle();
            if (!running || System.currentTimeMillis() > expiresAt) { respond(out, 410, "Sesi upload berakhir."); return; }
            File delivered = temp;
            boolean accepted;
            synchronized (this) {
                // stop() uses the same monitor: a closed/revoked QR session cannot commit afterwards.
                if (!running || System.currentTimeMillis() >= expiresAt) {
                    respond(out, 410, "Sesi upload berakhir atau telah ditutup.");
                    return;
                }
                accepted = listener != null && listener.onAvatarUploaded(delivered);
            }
            if (!accepted) {
                respond(out, 500, "Foto diterima tetapi profil gagal diperbarui. Foto lama tetap dipertahankan.");
                return;
            }
            uploadSucceeded = true;
            temp = null;
            byte[] ok = "{\"status\":\"ok\"}".getBytes(StandardCharsets.UTF_8);
            out.write(("HTTP/1.1 200 OK\r\nContent-Type: application/json; charset=UTF-8\r\nContent-Length: " + ok.length
                    + "\r\nCache-Control: no-store\r\nConnection: close\r\n\r\n").getBytes(StandardCharsets.UTF_8));
            out.write(ok); out.flush();
        } catch (Exception e) {
            Log.w(TAG, "Avatar upload failed", e);
        } finally {
            if (temp != null && temp.exists()) temp.delete();
            if (claimed && !uploadSucceeded) uploadClaimed.set(false);
            activeClients.remove(socket);
            activeClientCount.decrementAndGet();
        }
    }

    static boolean isAvatarPagePath(String path, String token) {
        return path != null && token != null && path.equals("/avatar/" + token);
    }

    static boolean isAvatarUploadPath(String path, String token) {
        return path != null && token != null && path.equals("/avatar-upload/" + token);
    }

    static boolean isValidUploadLength(long length) {
        return length > 0 && length <= MAX_BYTES;
    }

    static boolean isValidImageDimensions(int width, int height) {
        return width > 0 && height > 0 && width <= 12000 && height <= 12000
                && (long) width * height <= 80000000L;
    }

    private String page(String endpoint) {
        return "<!doctype html><html lang='id'><meta charset='utf-8'><meta name='viewport' content='width=device-width,initial-scale=1'>"
                + "<title>Ganti Avatar Glass TV</title><style>body{font:16px system-ui;background:#0b1220;color:#fff;padding:24px}"
                + "main{max-width:460px;margin:auto;background:#172235;padding:24px;border-radius:20px}button,input{width:100%;padding:14px;margin:12px 0}"
                + "button{background:#7dd3fc;border:0;border-radius:12px;font-weight:700}#s{color:#bae6fd}</style>"
                + "<main><h2>📷 Ganti Foto Avatar</h2><p>Pilih satu foto. Foto ini hanya menggantikan avatar profil yang sedang dipilih di TV.</p>"
                + "<input id='f' type='file' accept='image/*'><button id='b' disabled>Ganti Avatar di TV</button><p id='s'>Maksimal 15 MB. Sesi berlaku 5 menit.</p></main>"
                + "<script>const f=document.getElementById('f'),b=document.getElementById('b'),s=document.getElementById('s');"
                + "f.onchange=()=>b.disabled=!f.files.length;b.onclick=()=>{const x=f.files[0];if(!x)return;b.disabled=true;s.textContent='Mengirim foto...';"
                + "const r=new XMLHttpRequest();r.open('POST','" + endpoint + "');r.setRequestHeader('Content-Type',x.type||'application/octet-stream');"
                + "r.onload=()=>{s.textContent=r.status===200?'Berhasil! Avatar di TV sudah diperbarui.':'Gagal: '+r.responseText;b.disabled=false;};"
                + "r.onerror=()=>{s.textContent='Koneksi gagal. Pastikan HP dan TV satu jaringan.';b.disabled=false;};r.send(x);};</script></html>";
    }

    private void respond(OutputStream out, int status, String message) throws Exception {
        String label = status == 200 ? "OK" : status == 400 ? "Bad Request" : status == 404 ? "Not Found"
                : status == 409 ? "Conflict" : status == 410 ? "Gone" : status == 413 ? "Payload Too Large"
                : status == 415 ? "Unsupported Media Type" : status == 431 ? "Request Header Fields Too Large"
                : status == 503 ? "Service Unavailable" : "Internal Server Error";
        byte[] body = message.getBytes(StandardCharsets.UTF_8);
        out.write(("HTTP/1.1 " + status + " " + label + "\r\nContent-Type: text/plain; charset=UTF-8\r\nContent-Length: " + body.length
                + "\r\nCache-Control: no-store\r\nConnection: close\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        out.write(body); out.flush();
    }
}
