package com.gathin.tvhome;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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
    public static String getLocalIpAddress(Context context) {
        return LocalNetworkAddress.getActiveLanIpv4(context);
    }

    public String getUrl() {
        return boundIp == null || port <= 0 ? null : "http://" + boundIp + ":" + port + "/avatar/" + token;
    }

    public synchronized boolean start() {
        if (running) return true;
        if (System.currentTimeMillis() >= expiresAt) return false;
        try {
            boundIp = getLocalIpAddress(context);
            if (boundIp == null || boundIp.trim().isEmpty() || "127.0.0.1".equals(boundIp)) return false;
            InetAddress bindAddress = InetAddress.getByName(boundIp);
            int[] ports = new int[]{8080, 8888, 8081, 9090, 0};
            for (int candidate : ports) {
                try {
                    server = new ServerSocket();
                    server.setReuseAddress(true);
                    server.bind(new InetSocketAddress(bindAddress, candidate));
                    server.setSoTimeout(10000);
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
                        try { respond(client.getOutputStream(), 503, localized(R.string.avatar_error_busy)); } catch (Exception ignored) {}
                        try { client.close(); } catch (Exception ignored) {}
                        continue;
                    }
                    activeClients.add(client);
                    Thread worker = new Thread(() -> handle(client), "avatar-upload-client");
                    worker.setDaemon(true);
                    worker.start();
                } catch (Exception e) {
                    if (e instanceof SocketTimeoutException) {
                        if (System.currentTimeMillis() >= expiresAt) {
                            stop();
                            break;
                        }
                        continue;
                    }
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
            client.setSoTimeout(NetworkFeedbackPolicy.AVATAR_UPLOAD_CLIENT_IDLE_TIMEOUT_MS);
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            ByteArrayOutputStream header = new ByteArrayOutputStream();
            int state = 0, value;
            boolean headerComplete = false;
            while ((value = in.read()) != -1) {
                header.write(value);
                if (header.size() > 8192) { respond(out, 431, localized(R.string.avatar_error_header_too_large)); return; }
                if ((state == 0 && value == 13) || (state == 2 && value == 13)) state++;
                else if ((state == 1 && value == 10) || (state == 3 && value == 10)) state++;
                else state = value == 13 ? 1 : 0;
                if (state == 4) { headerComplete = true; break; }
            }
            if (!headerComplete) { respond(out, 400, localized(R.string.avatar_error_invalid_request)); return; }
            String[] lines = header.toString("UTF-8").split("\\r\\n");
            if (lines.length == 0 || !UploadHttpRequest.hasValidHeaders(lines)) {
                respond(out, 400, localized(R.string.avatar_error_invalid_request)); return;
            }
            if (System.currentTimeMillis() >= expiresAt || !running) {
                respond(out, 410, localized(R.string.avatar_error_session_expired)); return;
            }
            UploadHttpRequest request = UploadHttpRequest.parse(lines[0]);
            if (request == null) { respond(out, 400, localized(R.string.avatar_error_invalid_request)); return; }
            boolean pagePath = isAvatarPagePath(request.path, token);
            boolean uploadPath = isAvatarUploadPath(request.path, token);
            if (!pagePath && !uploadPath) {
                respond(out, 404, localized(R.string.avatar_error_not_found)); return;
            }
            if (pagePath && !"GET".equals(request.method)) {
                respondMethodNotAllowed(out, "GET"); return;
            }
            if (uploadPath && !"POST".equals(request.method)) {
                respondMethodNotAllowed(out, "POST"); return;
            }
            String expectedUpload = "/avatar-upload/" + token;
            if ("GET".equals(request.method) && pagePath) {
                byte[] page = page(expectedUpload).getBytes(StandardCharsets.UTF_8);
                out.write(("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\nContent-Length: " + page.length
                        + "\r\nCache-Control: no-store\r\nX-Content-Type-Options: nosniff\r\nReferrer-Policy: no-referrer\r\n"
                        + "Content-Security-Policy: default-src 'none'; style-src 'unsafe-inline'; script-src 'unsafe-inline'; base-uri 'none'; form-action 'none'\r\n"
                        + "Connection: close\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                out.write(page); out.flush(); return;
            }
            if (!"POST".equals(request.method) || !uploadPath) {
                respond(out, 405, localized(R.string.avatar_error_not_found)); return;
            }
            if (!uploadClaimed.compareAndSet(false, true)) {
                respond(out, 409, localized(R.string.avatar_error_qr_used));
                return;
            }
            claimed = true;
            if (UploadBodyReader.hasTransferEncoding(lines)) {
                respond(out, 400, localized(R.string.avatar_error_upload_format));
                return;
            }
            long length = UploadBodyReader.parseContentLength(lines, MAX_BYTES);
            if (length == -2L) { respond(out, 413, localized(R.string.avatar_error_max_size)); return; }
            if (length <= 0) { respond(out, 400, localized(R.string.avatar_error_upload_size)); return; }
            File dir = new File(context.getFilesDir(), "profile_avatars");
            if (!dir.exists() && !dir.mkdirs()) { respond(out, 500, localized(R.string.avatar_error_storage)); return; }
            String safeId = profileId == null ? "" : profileId.replaceAll("[^a-zA-Z0-9_-]", "_");
            if (safeId.isEmpty()) { respond(out, 400, localized(R.string.avatar_error_profile)); return; }
            temp = File.createTempFile("avatar_" + safeId + "_", ".upload", dir);
            if (!UploadBodyReader.receiveExactly(in, temp, length, true)) {
                respond(out, 400, localized(R.string.avatar_error_incomplete));
                return;
            }
            UploadFilePolicy.ImageFormat imageFormat = UploadFilePolicy.detectImageFormat(temp);
            if (imageFormat == null || !UploadFilePolicy.hasCompatibleContentType(lines, imageFormat)) {
                respond(out, 415, localized(R.string.avatar_error_unsupported_image));
                return;
            }
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(temp.getAbsolutePath(), bounds);
            if (!isValidImageDimensions(bounds.outWidth, bounds.outHeight)) {
                respond(out, 415, localized(R.string.avatar_error_unsupported_image)); return;
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
                respond(out, 415, localized(R.string.avatar_error_corrupt_image)); return;
            }
            verifiedBitmap.recycle();
            File storedAvatar = new File(dir,
                    temp.getName().substring(0, temp.getName().length() - ".upload".length())
                            + "." + imageFormat.extension);
            if (!temp.renameTo(storedAvatar)) {
                respond(out, 500, localized(R.string.avatar_error_storage));
                return;
            }
            temp = storedAvatar;
            if (!running || System.currentTimeMillis() > expiresAt) { respond(out, 410, localized(R.string.avatar_error_session_expired_short)); return; }
            File delivered = temp;
            boolean accepted;
            synchronized (this) {
                // stop() uses the same monitor: a closed/revoked QR session cannot commit afterwards.
                if (!running || System.currentTimeMillis() >= expiresAt) {
                    respond(out, 410, localized(R.string.avatar_error_session_closed));
                    return;
                }
                accepted = listener != null && listener.onAvatarUploaded(delivered);
            }
            if (!accepted) {
                respond(out, 500, localized(R.string.avatar_error_profile_update));
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

    private String localized(int resourceId) {
        return LocaleContext.wrap(context).getString(resourceId);
    }

    private String page(String endpoint) {
        return "<!doctype html><html lang='" + AppLanguage.getLanguage(context) + "'><meta charset='utf-8'><meta name='viewport' content='width=device-width,initial-scale=1'>"
                + "<title>" + localized(R.string.avatar_upload_page_title) + "</title><style>body{font:16px system-ui;background:#0b1220;color:#fff;padding:24px}"
                + "main{max-width:460px;margin:auto;background:#172235;padding:24px;border-radius:20px}button,input{width:100%;padding:14px;margin:12px 0}"
                + "button{background:#7dd3fc;border:0;border-radius:12px;font-weight:700}#s{color:#bae6fd}</style>"
                + "<main><h2>📷 " + localized(R.string.avatar_upload_heading) + "</h2><p>" + localized(R.string.avatar_upload_intro) + "</p>"
                + "<input id='f' type='file' accept='image/jpeg,image/png,image/webp'><button id='b' disabled>" + localized(R.string.avatar_upload_button) + "</button><p id='s'>" + localized(R.string.avatar_upload_limit) + "</p></main>"
                + "<script>const f=document.getElementById('f'),b=document.getElementById('b'),s=document.getElementById('s');"
                + "f.onchange=()=>b.disabled=!f.files.length;b.onclick=()=>{const x=f.files[0];if(!x)return;b.disabled=true;s.textContent='" + localized(R.string.avatar_upload_sending) + "';"
                + "const r=new XMLHttpRequest();r.open('POST','" + endpoint + "');r.timeout=" + NetworkFeedbackPolicy.AVATAR_BROWSER_UPLOAD_TIMEOUT_MS + ";r.setRequestHeader('Content-Type',x.type||'application/octet-stream');"
                + "r.upload.onprogress=e=>{if(e.lengthComputable)s.textContent='" + localized(R.string.avatar_upload_sending) + " '+Math.round(e.loaded/e.total*100)+'%';};"
                + "r.onload=()=>{s.textContent=r.status===200?'" + localized(R.string.avatar_upload_success) + "':'" + localized(R.string.avatar_upload_failed_prefix) + " '+r.responseText;b.disabled=false;};"
                + "r.onerror=()=>{s.textContent='" + localized(R.string.avatar_upload_connection_error) + "';b.disabled=false;};"
                + "r.ontimeout=()=>{s.textContent='" + localized(R.string.avatar_upload_timeout) + "';b.disabled=false;};r.send(x);};</script></html>";
    }

    private void respondMethodNotAllowed(OutputStream out, String allowed) throws Exception {
        byte[] body = "Method Not Allowed".getBytes(StandardCharsets.UTF_8);
        out.write(("HTTP/1.1 405 Method Not Allowed\r\n"
                + "Content-Type: text/plain; charset=UTF-8\r\n"
                + "Content-Length: " + body.length + "\r\n"
                + "Allow: " + allowed + "\r\n"
                + "Cache-Control: no-store\r\n"
                + "Connection: close\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        out.write(body);
        out.flush();
    }

    private void respond(OutputStream out, int status, String message) throws Exception {
        String label = status == 200 ? "OK" : status == 400 ? "Bad Request" : status == 404 ? "Not Found"
                : status == 409 ? "Conflict" : status == 410 ? "Gone" : status == 413 ? "Payload Too Large"
                : status == 415 ? "Unsupported Media Type" : status == 431 ? "Request Header Fields Too Large"
                : status == 405 ? "Method Not Allowed" : status == 503 ? "Service Unavailable" : "Internal Server Error";
        byte[] body = message.getBytes(StandardCharsets.UTF_8);
        out.write(("HTTP/1.1 " + status + " " + label + "\r\nContent-Type: text/plain; charset=UTF-8\r\nContent-Length: " + body.length
                + "\r\nCache-Control: no-store\r\nConnection: close\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        out.write(body); out.flush();
    }
}
