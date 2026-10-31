package com.gathin.tvhome;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/** Short-lived, single-purpose local HTTP endpoint for replacing one profile avatar. */
public final class AvatarUploadServer {
    private static final String TAG = "TvHome.AvatarUpload";
    private static final long MAX_BYTES = 15L * 1024L * 1024L;
    private static final long SESSION_MS = 5L * 60L * 1000L;

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

    public AvatarUploadServer(Context context, String profileId, Listener listener) {
        this.context = context.getApplicationContext();
        this.profileId = profileId;
        this.listener = listener;
    }

    public static String getLocalIpAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.isLoopback() || !nif.isUp()) continue;
                for (InetAddress addr : Collections.list(nif.getInetAddresses())) {
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        String ip = addr.getHostAddress();
                        if (ip != null && !ip.startsWith("169.254.")) return ip;
                    }
                }
            }
        } catch (Exception e) { Log.w(TAG, "IP lookup failed", e); }
        return "127.0.0.1";
    }

    public String getUrl() { return "http://" + getLocalIpAddress() + ":" + port + "/avatar/" + token; }

    public synchronized boolean start() {
        if (running) return true;
        int[] ports = new int[]{8080, 8888, 8081, 9090, 0};
        for (int candidate : ports) {
            try {
                server = new ServerSocket();
                server.setReuseAddress(true);
                server.bind(new InetSocketAddress(candidate));
                port = server.getLocalPort();
                break;
            } catch (Exception e) {
                try { if (server != null) server.close(); } catch (Exception ignored) {}
                server = null;
            }
        }
        if (server == null) return false;
        running = true;
        thread = new Thread(() -> {
            while (running && server != null && !server.isClosed()) {
                try {
                    Socket client = server.accept();
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
        server = null;
    }

    private void handle(Socket socket) {
        File temp = null;
        try (Socket client = socket) {
            client.setSoTimeout(20000);
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
            if (lines.length == 0 || System.currentTimeMillis() > expiresAt || !running) {
                respond(out, 410, "Sesi upload sudah berakhir. Buat QR baru dari TV."); return;
            }
            String[] request = lines[0].split(" ");
            if (request.length < 2) { respond(out, 400, "Permintaan tidak valid."); return; }
            String expectedPage = "/avatar/" + token;
            String expectedUpload = "/avatar-upload/" + token;
            if ("GET".equals(request[0]) && expectedPage.equals(request[1])) {
                byte[] page = page(expectedUpload).getBytes(StandardCharsets.UTF_8);
                out.write(("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\nContent-Length: " + page.length
                        + "\r\nCache-Control: no-store\r\nConnection: close\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                out.write(page); out.flush(); return;
            }
            if (!"POST".equals(request[0]) || !expectedUpload.equals(request[1])) {
                respond(out, 404, "Tidak ditemukan."); return;
            }
            long length = -1;
            for (String line : lines) if (line.toLowerCase(Locale.ROOT).startsWith("content-length:")) {
                try { length = Long.parseLong(line.substring(15).trim()); } catch (Exception ignored) {}
            }
            if (length <= 0 || length > MAX_BYTES) { respond(out, 413, "Foto maksimal 15 MB."); return; }
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
                if (remaining != 0 || temp.length() != length) { respond(out, 400, "Unggahan tidak lengkap."); return; }
            }
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(temp.getAbsolutePath(), bounds);
            if (bounds.outWidth <= 0 || bounds.outHeight <= 0 || bounds.outWidth > 12000 || bounds.outHeight > 12000
                    || (long) bounds.outWidth * bounds.outHeight > 80000000L) {
                respond(out, 415, "Berkas bukan gambar yang didukung atau resolusi terlalu besar."); return;
            }
            if (!running || System.currentTimeMillis() > expiresAt) { respond(out, 410, "Sesi upload berakhir."); return; }
            File delivered = temp;
            if (listener == null || !listener.onAvatarUploaded(delivered)) {
                respond(out, 500, "Foto diterima tetapi profil gagal diperbarui. Foto lama tetap dipertahankan.");
                return;
            }
            temp = null;
            byte[] ok = "{\\"status\\":\\"ok\\"}".getBytes(StandardCharsets.UTF_8);
            out.write(("HTTP/1.1 200 OK\r\nContent-Type: application/json; charset=UTF-8\r\nContent-Length: " + ok.length
                    + "\r\nCache-Control: no-store\r\nConnection: close\r\n\r\n").getBytes(StandardCharsets.UTF_8));
            out.write(ok); out.flush();
        } catch (Exception e) {
            Log.w(TAG, "Avatar upload failed", e);
        } finally {
            if (temp != null && temp.exists()) temp.delete();
        }
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
        String label = status == 200 ? "OK" : status == 404 ? "Not Found" : status == 410 ? "Gone" : status == 413 ? "Payload Too Large" : "Bad Request";
        byte[] body = message.getBytes(StandardCharsets.UTF_8);
        out.write(("HTTP/1.1 " + status + " " + label + "\r\nContent-Type: text/plain; charset=UTF-8\r\nContent-Length: " + body.length
                + "\r\nCache-Control: no-store\r\nConnection: close\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        out.write(body); out.flush();
    }
}
