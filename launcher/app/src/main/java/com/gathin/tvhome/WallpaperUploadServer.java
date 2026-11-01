package com.gathin.tvhome;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class WallpaperUploadServer {
    private static final String TAG = "TvHome.UploadServer";
    static final long MAX_UPLOAD_BYTES = 60L * 1024L * 1024L;
    private static final int MAX_ACTIVE_CLIENTS = 8; // Allows normal parallel gallery thumbnail requests.

    public interface UploadListener {
        void onUploaded(File imageFile);
        default void onDeleted(File file) {}
        default void onSelected(File file) {}
        default void onSlideshowMode() {}
        default void onCleared() {}
        default void onStandbyUploaded(File imageFile) {}
    }

    public interface OnWallpaperUploadedListener extends UploadListener {}

    private final Context context;
    private int port;
    private final UploadListener listener;
    private final boolean standbyOnly;
    private final String sessionToken = UUID.randomUUID().toString().replace("-", "");
    private ServerSocket serverSocket;
    private String boundIpAddress;
    private volatile boolean running = false;
    private final Set<Socket> activeClients = Collections.synchronizedSet(new java.util.HashSet<>());
    private Thread serverThread;

    public WallpaperUploadServer(Context context, int port, UploadListener listener) {
        this(context, port, listener, false);
    }

    public WallpaperUploadServer(Context context, int port, UploadListener listener, boolean standbyOnly) {
        this.context = context.getApplicationContext();
        this.port = port;
        this.listener = listener;
        this.standbyOnly = standbyOnly;
    }

    private String localized(int resourceId) {
        return LocaleContext.wrap(context).getString(resourceId);
    }

    private String htmlText(int resourceId) {
        return localized(resourceId).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private String jsText(int resourceId) {
        return localized(resourceId).replace("\\", "\\\\").replace("'", "\\'").replace("\n", " ");
    }

    /** Active default Wi-Fi/Ethernet IPv4 used by the QR upload route. */
    public static String getLocalIpAddress(Context context) {
        return LocalNetworkAddress.getActiveLanIpv4(context);
    }

    /** Session-specific route; the StandBy QR never points at the wallpaper uploader. */
    public String getStandbyPath() {
        return "/standby/" + sessionToken;
    }

    private String getStandbyUploadPath() {
        return "/standby-upload/" + sessionToken;
    }

    /** Wallpaper QR routes are scoped to this unpredictable per-server session path. */
    public String getWallpaperPath() {
        return "/wallpaper/" + sessionToken;
    }

    private String getWallpaperUploadPath() {
        return getWallpaperPath() + "/upload";
    }

    /**
     * Accept one unambiguous Content-Length only. Returns -1 for invalid/missing values and
     * -2 for a valid numeric length above the endpoint limit. Chunked transfer is not supported.
     */
    static long parseContentLength(String[] headerLines, long maxBytes) {
        return UploadBodyReader.parseContentLength(headerLines, maxBytes);
    }

    /** Matches a fully parsed request against one exact path, ignoring only its query string. */
    static boolean isRequestToPath(String requestLine, String method, String path) {
        UploadHttpRequest request = UploadHttpRequest.parse(requestLine);
        return request != null && method != null && method.equals(request.method)
                && request.isPath(path);
    }

    static String wallpaperAllowedMethods(String requestPath, String sessionPath) {
        if (requestPath == null || sessionPath == null) return null;
        if (requestPath.equals(sessionPath)
                || requestPath.equals(sessionPath + "/thumb")
                || requestPath.equals(sessionPath + "/api/list")) return "GET, OPTIONS";
        if (requestPath.equals(sessionPath + "/upload")
                || requestPath.equals(sessionPath + "/api/select")
                || requestPath.equals(sessionPath + "/api/delete")
                || requestPath.equals(sessionPath + "/api/slideshow")
                || requestPath.equals(sessionPath + "/api/clear")) return "POST, OPTIONS";
        return null;
    }

    static boolean isWallpaperSessionRequest(String requestLine, String sessionPath) {
        UploadHttpRequest request = UploadHttpRequest.parse(requestLine);
        if (request == null) return false;
        String allowed = wallpaperAllowedMethods(request.path, sessionPath);
        return allowed != null && (request.method.equals("OPTIONS")
                || (allowed.startsWith(request.method + ",") || allowed.equals(request.method)));
    }

    public int getPort() {
        if (serverSocket != null && serverSocket.isBound() && !serverSocket.isClosed()) {
            return serverSocket.getLocalPort();
        }
        return port;
    }

    /** The exact interface address this server is listening on; null when it is not started. */
    public String getBoundIpAddress() {
        return boundIpAddress;
    }

    public synchronized boolean start() {
        if (running) return true;
        String selectedIp = getLocalIpAddress(context);
        if (selectedIp == null || selectedIp.trim().isEmpty() || "127.0.0.1".equals(selectedIp)) {
            Log.e(TAG, "No usable local IPv4 interface for upload server");
            return false;
        }
        final InetAddress bindAddress;
        try {
            bindAddress = InetAddress.getByName(selectedIp);
        } catch (Exception e) {
            Log.e(TAG, "Cannot resolve local upload interface", e);
            return false;
        }

        int[] candidatePorts = new int[]{port, 8080, 8888, 8081, 9090, 0};
        for (int p : candidatePorts) {
            try {
                serverSocket = new ServerSocket();
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(bindAddress, p));
                boundIpAddress = selectedIp;
                this.port = serverSocket.getLocalPort();
                running = true;
                break;
            } catch (Exception e) {
                try {
                    if (serverSocket != null) serverSocket.close();
                } catch (Exception ignored) {}
                serverSocket = null;
            }
        }

        if (!running || serverSocket == null) {
            Log.e(TAG, "Failed to bind wallpaper server on any port");
            return false;
        }

        try {
            final ServerSocket listeningSocket = serverSocket;
            serverThread = new Thread(() -> {
                Log.i(TAG, "Server started successfully on port " + this.port);
                while (running && !listeningSocket.isClosed()) {
                    try {
                        Socket client = listeningSocket.accept();
                        if (!running) {
                            client.close();
                            break;
                        }
                        boolean accepted;
                        synchronized (activeClients) {
                            accepted = running && activeClients.size() < MAX_ACTIVE_CLIENTS;
                            if (accepted) activeClients.add(client);
                        }
                        if (!accepted) {
                            try {
                                byte[] body = localized(R.string.local_upload_error_busy).getBytes("UTF-8");
                                OutputStream rejectOut = client.getOutputStream();
                                rejectOut.write(("HTTP/1.1 503 Service Unavailable\r\nContent-Type: text/plain; charset=UTF-8\r\n"
                                        + "Content-Length: " + body.length + "\r\nCache-Control: no-store\r\nConnection: close\r\n\r\n").getBytes("UTF-8"));
                                rejectOut.write(body);
                                rejectOut.flush();
                            } catch (Exception ignored) {}
                            try { client.close(); } catch (Exception ignored) {}
                            if (!running) break;
                            continue;
                        }
                        try {
                            Thread worker = new Thread(() -> handleClient(client), "upload-client");
                            worker.setDaemon(true);
                            worker.start();
                        } catch (Exception e) {
                            activeClients.remove(client);
                            try { client.close(); } catch (Exception ignored) {}
                            Log.w(TAG, "Cannot start upload client worker", e);
                        }
                    } catch (Exception e) {
                        if (!running) break;
                        Log.w(TAG, "Accept error: " + e.getMessage());
                    }
                }
            }, "wallpaper-http-server");
            serverThread.setDaemon(true);
            serverThread.start();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to launch server thread: " + e.getMessage(), e);
            running = false;
            try { if (serverSocket != null) serverSocket.close(); } catch (Exception ignored) {}
            boundIpAddress = null;
            return false;
        }
    }

    public synchronized void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (Exception ignored) {}
        if (serverThread != null) {
            serverThread.interrupt();
            serverThread = null;
        }
        boundIpAddress = null;
        synchronized (activeClients) {
            for (Socket client : activeClients) {
                try {
                    client.close();
                } catch (Exception ignored) {
                }
            }
            activeClients.clear();
        }
        Log.i(TAG, "Server stopped");
    }

    private void sendTextResponse(OutputStream out, int status, String reason, String message, String extraHeaders) throws Exception {
        byte[] body = (message == null ? "" : message).getBytes("UTF-8");
        String response = "HTTP/1.1 " + status + " " + reason + "\r\n"
                + "Content-Type: text/plain; charset=UTF-8\r\n"
                + "Content-Length: " + body.length + "\r\n"
                + "Cache-Control: no-store\r\n"
                + (extraHeaders == null ? "" : extraHeaders)
                + "Connection: close\r\n\r\n";
        out.write(response.getBytes("UTF-8"));
        out.write(body);
        out.flush();
    }

    private void handleClient(Socket client) {
        try {
            client.setSoTimeout(NetworkFeedbackPolicy.WALLPAPER_UPLOAD_CLIENT_IDLE_TIMEOUT_MS); // Session expiry remains independently enforced.
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();

            ByteArrayOutputStream headerBuf = new ByteArrayOutputStream();
            int prev3 = 0, prev2 = 0, prev1 = 0, b;
            boolean headerComplete = false;
            while ((b = in.read()) != -1) {
                headerBuf.write(b);
                if (headerBuf.size() > 16 * 1024) {
                    sendTextResponse(out, 431, "Request Header Fields Too Large", "Request Header Fields Too Large", null);
                    return;
                }
                if (prev3 == '\r' && prev2 == '\n' && prev1 == '\r' && b == '\n') {
                    headerComplete = true;
                    break;
                }
                prev3 = prev2;
                prev2 = prev1;
                prev1 = b;
            }

            if (!headerComplete) {
                sendTextResponse(out, 400, "Bad Request", "Incomplete HTTP headers", null);
                return;
            }
            String headerText = headerBuf.toString("UTF-8");
            String[] lines = headerText.split("\r\n");
            if (lines.length == 0 || lines[0].isEmpty() || !UploadHttpRequest.hasValidHeaders(lines)) {
                sendTextResponse(out, 400, "Bad Request", "Invalid HTTP request", null);
                return;
            }

            String requestLine = lines[0];
            UploadHttpRequest request = UploadHttpRequest.parse(requestLine);
            if (request == null) {
                sendTextResponse(out, 400, "Bad Request", "Invalid HTTP request line", null);
                return;
            }
            Log.d(TAG, "Request: " + requestLine);

            boolean standbyPagePath = request.isPath(getStandbyPath());
            boolean standbyUploadPath = request.isPath(getStandbyUploadPath());
            boolean standbySessionPath = standbyPagePath || standbyUploadPath;
            boolean standbyRoute = (standbyPagePath && "GET".equals(request.method))
                    || (standbyUploadPath && "POST".equals(request.method));
            String wallpaperAllowed = wallpaperAllowedMethods(request.path, getWallpaperPath());
            boolean wallpaperSessionPath = request.isWithinPath(getWallpaperPath());

            if (standbyOnly && !standbySessionPath) {
                sendTextResponse(out, 404, "Not Found", "Not Found", null);
            } else if (!standbyOnly && !wallpaperSessionPath) {
                sendTextResponse(out, 404, "Not Found", "Not Found", null);
            } else if (standbyOnly && !standbyRoute) {
                String allow = standbyPagePath ? "GET" : "POST";
                sendTextResponse(out, 405, "Method Not Allowed", "Method Not Allowed", "Allow: " + allow + "\r\n");
            } else if (!standbyOnly && wallpaperAllowed == null) {
                sendTextResponse(out, 404, "Not Found", "Not Found", null);
            } else if (!standbyOnly && "OPTIONS".equals(request.method)) {
                String response = "HTTP/1.1 204 No Content\r\n" +
                        "Access-Control-Allow-Origin: *\r\n" +
                        "Access-Control-Allow-Methods: " + wallpaperAllowed + "\r\n" +
                        "Access-Control-Allow-Headers: Content-Type\r\n" +
                        "Cache-Control: no-store\r\n" +
                        "Connection: close\r\n\r\n";
                out.write(response.getBytes("UTF-8"));
                out.flush();
            } else if (!standbyOnly && !wallpaperAllowed.startsWith(request.method + ",")
                    && !wallpaperAllowed.equals(request.method)) {
                sendTextResponse(out, 405, "Method Not Allowed", "Method Not Allowed", "Allow: " + wallpaperAllowed.replace(", OPTIONS", "") + "\r\n");
            } else if (isRequestToPath(requestLine, "GET", getWallpaperPath() + "/thumb")) {
                String fileName = getQueryParam(requestLine, "file");
                if (fileName == null || fileName.trim().isEmpty()) {
                    sendTextResponse(out, 400, "Bad Request", "Missing or ambiguous file parameter", null);
                } else {
                    serveThumbnail(fileName, out);
                }
            } else if (isRequestToPath(requestLine, "GET", getWallpaperPath() + "/api/list")) {
                serveApiList(out);
            } else if (isRequestToPath(requestLine, "POST", getWallpaperPath() + "/api/select")) {
                String fileName = getQueryParam(requestLine, "file");
                handleApiSelect(fileName, out);
            } else if (isRequestToPath(requestLine, "POST", getWallpaperPath() + "/api/delete")) {
                String fileName = getQueryParam(requestLine, "file");
                handleApiDelete(fileName, out);
            } else if (isRequestToPath(requestLine, "POST", getWallpaperPath() + "/api/slideshow")) {
                handleApiSlideshow(out);
            } else if (isRequestToPath(requestLine, "POST", getWallpaperPath() + "/api/clear")) {
                handleApiClear(out);
            } else if (standbyOnly && isRequestToPath(requestLine, "GET", getStandbyPath())) {
                byte[] pageBytes = getStandbyHtmlPage().getBytes("UTF-8");
                String response = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html; charset=UTF-8\r\n"
                        + "Content-Length: " + pageBytes.length + "\r\n"
                        + "Cache-Control: no-store\r\n"
                        + "Connection: close\r\n\r\n";
                out.write(response.getBytes("UTF-8"));
                out.write(pageBytes);
                out.flush();
            } else if (standbyOnly && isRequestToPath(requestLine, "POST", getStandbyUploadPath())) {
                handleStandbyUpload(client, in, out, lines);
            } else if (isRequestToPath(requestLine, "GET", getWallpaperPath())) {
                byte[] pageBytes = getHtmlPage().getBytes("UTF-8");
                String response = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html; charset=UTF-8\r\n" +
                        "Content-Length: " + pageBytes.length + "\r\n" +
                        "Cache-Control: no-store\r\n" +
                        "Access-Control-Allow-Origin: *\r\n" +
                        "Connection: close\r\n\r\n";
                out.write(response.getBytes("UTF-8"));
                out.write(pageBytes);
                out.flush();
            } else if (isRequestToPath(requestLine, "POST", getWallpaperUploadPath())) {
                long contentLength = parseContentLength(lines, MAX_UPLOAD_BYTES);

                if (contentLength == -2L) {
                    sendTextResponse(out, 413, "Payload Too Large", "Upload exceeds the allowed size", null);
                } else if (contentLength > 0) {
                    File customDir = new File(context.getFilesDir(), "custom_wallpapers");
                    if (!customDir.exists() && !customDir.mkdirs()) {
                        throw new java.io.IOException("Cannot create wallpaper storage");
                    }

                    File targetFile = File.createTempFile("custom_", ".upload", customDir);
                    boolean complete = UploadBodyReader.receiveExactly(in, targetFile, contentLength, true);
                    UploadFilePolicy.ImageFormat imageFormat = complete
                            ? validateImageFile(targetFile, lines) : null;

                    if (!complete || imageFormat == null) {
                        targetFile.delete();
                        String message = !complete ? localized(R.string.wallpaper_error_incomplete) : localized(R.string.wallpaper_error_unsupported_image);
                        sendTextResponse(out, 400, "Bad Request", message, null);
                    } else {
                        File storedFile = new File(customDir,
                                targetFile.getName().substring(0, targetFile.getName().length() - ".upload".length())
                                        + "." + imageFormat.extension);
                        if (!targetFile.renameTo(storedFile)) {
                            targetFile.delete();
                            sendTextResponse(out, 500, "Internal Server Error", "Unable to store uploaded image", null);
                            return;
                        }
                        String json = "{\"status\":\"ok\",\"file\":\"" + storedFile.getName() + "\"}";
                        byte[] jsonBytes = json.getBytes("UTF-8");
                        String response = "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: application/json; charset=UTF-8\r\n" +
                                "Content-Length: " + jsonBytes.length + "\r\n" +
                                "Access-Control-Allow-Origin: *\r\n" +
                                "Connection: close\r\n\r\n";
                        out.write(response.getBytes("UTF-8"));
                        out.write(jsonBytes);
                        out.flush();
                        if (listener != null) listener.onUploaded(storedFile);
                    }
                } else {
                    sendTextResponse(out, 400, "Bad Request", localized(R.string.wallpaper_error_invalid_size), null);
                }
            } else {
                sendTextResponse(out, 404, "Not Found", "Not Found", null);
            }

            out.close();
            in.close();
            client.close();
        } catch (Exception e) {
            Log.w(TAG, "Client handle exception: " + e.getMessage());
            try { client.close(); } catch (Exception ignored) {}
        } finally {
            activeClients.remove(client);
            try {
                if (!client.isClosed()) client.close();
            } catch (Exception ignored) {
            }
        }
    }

    /** Rejects unsupported signatures/MIME, malformed images and excessive dimensions. */
    private static UploadFilePolicy.ImageFormat validateImageFile(File file, String[] headers) {
        try {
            UploadFilePolicy.ImageFormat format = UploadFilePolicy.detectImageFormat(file);
            if (format == null || !UploadFilePolicy.hasCompatibleContentType(headers, format)) return null;
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(), bounds);
            if (bounds.outWidth <= 0 || bounds.outHeight <= 0
                    || bounds.outWidth > 16384 || bounds.outHeight > 16384
                    || (long) bounds.outWidth * (long) bounds.outHeight > 100_000_000L) return null;
            BitmapFactory.Options verify = new BitmapFactory.Options();
            int sample = 1;
            while (bounds.outWidth / sample > 512 || bounds.outHeight / sample > 512) sample *= 2;
            verify.inSampleSize = sample;
            verify.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap decoded = BitmapFactory.decodeFile(file.getAbsolutePath(), verify);
            if (decoded == null) return null;
            decoded.recycle();
            return format;
        } catch (Exception ignored) {
            return null;
        }
    }

    static File resolveWallpaperFile(File customDir, String fileName) {
        return UploadFilePolicy.resolveDirectChild(customDir, fileName);
    }

    private void handleStandbyUpload(Socket client, InputStream in, OutputStream out, String[] lines) throws Exception {
        long contentLength = parseContentLength(lines, MAX_UPLOAD_BYTES);
        if (contentLength == -2L) {
            sendUploadError(out, 413, localized(R.string.standby_error_photo_size));
            return;
        }
        if (contentLength <= 0) {
            sendUploadError(out, 400, localized(R.string.standby_error_photo_size));
            return;
        }
        if (!StandbyPhotoStore.canAcceptUpload(context, contentLength)) {
            sendUploadError(out, 400, localized(R.string.standby_error_collection_full));
            return;
        }

        File standbyDir = new File(context.getFilesDir(), "standby_photos");
        if (!standbyDir.exists() && !standbyDir.mkdirs()) {
            sendUploadError(out, 500, localized(R.string.standby_error_storage));
            return;
        }

        File targetFile = File.createTempFile("standby_", ".upload", standbyDir);
        boolean complete = UploadBodyReader.receiveExactly(in, targetFile, contentLength, true);
        UploadFilePolicy.ImageFormat imageFormat = complete
                ? validateImageFile(targetFile, lines) : null;

        if (!complete || imageFormat == null) {
            targetFile.delete();
            sendUploadError(out, 400, !complete
                    ? localized(R.string.wallpaper_error_incomplete_retry)
                    : localized(R.string.wallpaper_error_unsupported_image));
            return;
        }
        File storedFile = new File(standbyDir,
                targetFile.getName().substring(0, targetFile.getName().length() - ".upload".length())
                        + "." + imageFormat.extension);
        if (!targetFile.renameTo(storedFile)) {
            targetFile.delete();
            sendUploadError(out, 500, localized(R.string.standby_error_storage));
            return;
        }

        synchronized (activeClients) {
            if (!running || client.isClosed()) {
                storedFile.delete();
                return;
            }
            if (!StandbyPhotoStore.addUploadedFile(context, storedFile)) {
                storedFile.delete();
                sendUploadError(out, 400, localized(R.string.standby_error_collection_storage));
                return;
            }
        }

        String json = "{\"status\":\"ok\",\"file\":\"" + targetFile.getName() + "\"}";
        byte[] bytes = json.getBytes("UTF-8");
        String response = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/json; charset=UTF-8\r\n"
                + "Content-Length: " + bytes.length + "\r\n"
                + "Cache-Control: no-store\r\n"
                + "Connection: close\r\n\r\n";
        out.write(response.getBytes("UTF-8"));
        out.write(bytes);
        out.flush();
        if (listener != null) listener.onStandbyUploaded(storedFile);
    }

    private void sendUploadError(OutputStream out, int status, String message) throws Exception {
        byte[] bytes = message.getBytes("UTF-8");
        String label = status == 500 ? "Internal Server Error" : status == 413 ? "Payload Too Large" : "Bad Request";
        String response = "HTTP/1.1 " + status + " " + label + "\r\n"
                + "Content-Type: text/plain; charset=UTF-8\r\n"
                + "Content-Length: " + bytes.length + "\r\n"
                + "Connection: close\r\n\r\n";
        out.write(response.getBytes("UTF-8"));
        out.write(bytes);
        out.flush();
    }

    private String getStandbyHtmlPage() {
        String endpoint = getStandbyUploadPath();
        return "<!doctype html><html lang='" + AppLanguage.getLanguage(context) + "'><head><meta charset='utf-8'>"
                + "<meta name='viewport' content='width=device-width,initial-scale=1'>"
                + "<meta name='theme-color' content='#101827'><title>" + htmlText(R.string.standby_page_title) + "</title>"
                + "<style>"
                + "*{box-sizing:border-box}body{margin:0;padding:24px 16px 40px;background:#0b1220;color:#f8fafc;"
                + "font-family:system-ui,-apple-system,'Segoe UI',sans-serif;min-height:100vh}"
                + ".card{max-width:520px;margin:0 auto;padding:24px;border:1px solid #334155;border-radius:24px;"
                + "background:#172235;box-shadow:0 18px 50px #0008}h1{font-size:24px;margin:0 0 8px}"
                + "p{color:#cbd5e1;line-height:1.55;font-size:14px}.drop{display:block;margin:22px 0;padding:22px;"
                + "border:2px dashed #64748b;border-radius:18px;text-align:center;background:#0f1a2b}"
                + "input{width:100%;color:#e2e8f0}button{width:100%;border:0;border-radius:14px;padding:15px;"
                + "font-size:16px;font-weight:700;color:#07111f;background:#7dd3fc;margin-top:14px}"
                + "button:disabled{opacity:.45}#status{margin-top:16px;padding:12px;border-radius:12px;background:#0f1a2b;"
                + "font-size:14px}#bar{height:7px;background:#334155;border-radius:9px;overflow:hidden;margin-top:10px}"
                + "#fill{height:100%;width:0;background:#38bdf8;transition:width .15s}small{display:block;color:#94a3b8;"
                + "margin-top:18px;line-height:1.5}"
                + "</style></head><body><main class='card'><h1>" + htmlText(R.string.standby_page_heading) + "</h1>"
                + "<p>" + htmlText(R.string.standby_page_intro) + "</p>"
                + "<label class='drop'>" + htmlText(R.string.standby_page_choose_photos) + "<br><br><input id='files' type='file' accept='image/*' multiple></label>"
                + "<button id='send' disabled>" + htmlText(R.string.standby_page_upload_button) + "</button><div id='status'>" + htmlText(R.string.standby_page_no_photo) + "</div>"
                + "<div id='bar'><div id='fill'></div></div>"
                + "<small>" + htmlText(R.string.standby_page_storage_note) + " "
                + htmlText(R.string.standby_page_limit_note) + "</small></main><script>"
                + "const endpoint='" + endpoint + "';const input=document.getElementById('files');"
                + "const send=document.getElementById('send');const status=document.getElementById('status');"
                + "const fill=document.getElementById('fill');let files=[];"
                + "input.addEventListener('change',()=>{files=Array.from(input.files||[]);"
                + "send.disabled=!files.length;status.textContent=files.length+'" + jsText(R.string.standby_page_selected_suffix) + "';fill.style.width='0%';});"
                + "send.addEventListener('click',()=>{if(!files.length)return;send.disabled=true;let i=0,uploaded=0,failed=0;"
                + "const next=()=>{if(i>=files.length){status.textContent='" + jsText(R.string.standby_page_done_prefix) + "'+uploaded+'" + jsText(R.string.standby_page_done_middle) + "'+failed+'" + jsText(R.string.standby_page_done_suffix) + "';"
                + "fill.style.width='100%';input.value='';files=[];send.disabled=true;return;}const file=files[i];"
                + "if(file.size>60*1024*1024){failed++;i++;next();return;}"
                + "status.textContent='" + jsText(R.string.standby_page_uploading_prefix) + "'+(i+1)+'/'+files.length+': '+file.name;const xhr=new XMLHttpRequest();"
                + "xhr.open('POST',endpoint,true);xhr.timeout=" + NetworkFeedbackPolicy.STANDBY_BROWSER_UPLOAD_TIMEOUT_MS + ";xhr.setRequestHeader('Content-Type',file.type||'application/octet-stream');"
                + "xhr.upload.onprogress=e=>{if(e.lengthComputable){const part=e.loaded/e.total;"
                + "fill.style.width=Math.round(((i+part)/files.length)*100)+'%';}};"
                + "xhr.onload=()=>{if(xhr.status>=200&&xhr.status<300)uploaded++;else failed++;i++;next();};"
                + "xhr.onerror=()=>{status.textContent='" + jsText(R.string.standby_page_connection_lost_prefix) + "'+uploaded+'" + jsText(R.string.standby_page_connection_lost_suffix) + "';send.disabled=false;};"
                + "xhr.ontimeout=()=>{status.textContent='" + jsText(R.string.standby_page_timeout_prefix) + "'+uploaded+'" + jsText(R.string.standby_page_timeout_suffix) + "';send.disabled=false;};"
                + "xhr.send(file);};next();});</script></body></html>";
    }

    private String getQueryParam(String requestLine, String param) {
        UploadHttpRequest request = UploadHttpRequest.parse(requestLine);
        if (request == null || param == null || request.query.isEmpty()) return null;
        try {
            String found = null;
            for (String pair : request.query.split("&", -1)) {
                int eq = pair.indexOf('=');
                if (eq <= 0) continue;
                String name = URLDecoder.decode(pair.substring(0, eq), "UTF-8");
                if (!param.equals(name)) continue;
                if (found != null) return null; // Ambiguous duplicate parameter.
                found = URLDecoder.decode(pair.substring(eq + 1), "UTF-8");
            }
            return found;
        } catch (Exception ignored) {
            return null;
        }
    }

    private void serveThumbnail(String fileName, OutputStream out) {
        try {
            File customDir = new File(context.getFilesDir(), "custom_wallpapers");
            File target = resolveWallpaperFile(customDir, fileName);
            if (target == null) {
                sendTextResponse(out, 404, "Not Found", "Not Found", null);
                return;
            }

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(target.getAbsolutePath(), opts);

            int sample = 1;
            while (opts.outWidth / (sample * 2) >= 200 && opts.outHeight / (sample * 2) >= 150) sample *= 2;
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = sample;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;

            Bitmap bmp = BitmapFactory.decodeFile(target.getAbsolutePath(), opts);
            if (bmp == null) {
                sendTextResponse(out, 500, "Internal Server Error", "Thumbnail decode failed", null);
                return;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            boolean compressed = bmp.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            bmp.recycle();
            if (!compressed) {
                sendTextResponse(out, 500, "Internal Server Error", "Thumbnail encoding failed", null);
                return;
            }
            byte[] bytes = baos.toByteArray();

            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: image/jpeg\r\n" +
                    "Content-Length: " + bytes.length + "\r\n" +
                    "Cache-Control: max-age=3600\r\n" +
                    "Access-Control-Allow-Origin: *\r\n" +
                    "Connection: close\r\n\r\n";
            out.write(response.getBytes("UTF-8"));
            out.write(bytes);
            out.flush();
        } catch (Exception e) {
            Log.w(TAG, "Thumbnail error: " + e.getMessage());
            try { sendTextResponse(out, 500, "Internal Server Error", "Thumbnail request failed", null); } catch (Exception ignored) {}
        }
    }

    private void serveApiList(OutputStream out) {
        try {
            File customDir = new File(context.getFilesDir(), "custom_wallpapers");
            File[] files = customDir.exists()
                    ? customDir.listFiles((dir, name) -> resolveWallpaperFile(dir, name) != null)
                    : new File[0];
            if (files == null) throw new java.io.IOException("Cannot list wallpaper storage");
            JSONArray arr = new JSONArray();
            String activeSingle = context.getSharedPreferences("wallpaper_prefs", Context.MODE_PRIVATE)
                    .getString("custom_single_file", "");
            boolean isSingleMode = context.getSharedPreferences("wallpaper_prefs", Context.MODE_PRIVATE)
                    .getBoolean("custom_single_mode", false);

            if (files != null && files.length > 0) {
                Arrays.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
                SimpleDateFormat sdf = new SimpleDateFormat("d MMM, HH:mm", Locale.getDefault());
                for (File f : files) {
                    JSONObject obj = new JSONObject();
                    obj.put("name", f.getName());
                    double kb = f.length() / 1024.0;
                    if (kb > 1024) {
                        obj.put("size", String.format(Locale.ROOT, "%.1f MB", kb / 1024.0));
                    } else {
                        obj.put("size", String.format(Locale.ROOT, "%.0f KB", kb));
                    }
                    obj.put("date", sdf.format(new Date(f.lastModified())));
                    obj.put("active", isSingleMode && f.getName().equals(activeSingle));
                    arr.put(obj);
                }
            }

            byte[] jsonBytes = arr.toString().getBytes("UTF-8");
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json; charset=UTF-8\r\n" +
                    "Content-Length: " + jsonBytes.length + "\r\n" +
                    "Cache-Control: no-store\r\n" +
                    "Access-Control-Allow-Origin: *\r\n" +
                    "Connection: close\r\n\r\n";
            out.write(response.getBytes("UTF-8"));
            out.write(jsonBytes);
            out.flush();
        } catch (Exception e) {
            Log.w(TAG, "List API error: " + e.getMessage());
            try { sendSimpleJsonResponse(out, 500, "{\"status\":\"error\",\"error\":\"internal_error\"}"); } catch (Exception ignored) {}
        }
    }

    private void handleApiSelect(String fileName, OutputStream out) {
        try {
            if (fileName == null || fileName.trim().isEmpty()) {
                sendSimpleJsonResponse(out, 400, "{\"status\":\"error\",\"error\":\"missing_file\"}");
                return;
            }
            File customDir = new File(context.getFilesDir(), "custom_wallpapers");
            File target = resolveWallpaperFile(customDir, fileName);
            if (target == null) {
                sendSimpleJsonResponse(out, 400, "{\\"status\\":\\"error\\",\\"error\\":\\"invalid_file\\"}");
                return;
            }
            if (!target.isFile()) {
                sendSimpleJsonResponse(out, 404, "{\"status\":\"error\",\"error\":\"not_found\"}");
                return;
            }
            if (listener == null) {
                sendSimpleJsonResponse(out, 503, "{\"status\":\"error\",\"error\":\"unavailable\"}");
                return;
            }
            listener.onSelected(target);
            sendSimpleJsonResponse(out, 200, "{\"status\":\"ok\"}");
        } catch (Exception e) {
            Log.w(TAG, "Select API failed", e);
            try { sendSimpleJsonResponse(out, 500, "{\"status\":\"error\",\"error\":\"internal_error\"}"); } catch (Exception ignored) {}
        }
    }

    private void handleApiDelete(String fileName, OutputStream out) {
        try {
            if (fileName == null || fileName.trim().isEmpty()) {
                sendSimpleJsonResponse(out, 400, "{\"status\":\"error\",\"error\":\"missing_file\"}");
                return;
            }
            File customDir = new File(context.getFilesDir(), "custom_wallpapers");
            File target = resolveWallpaperFile(customDir, fileName);
            if (target == null) {
                sendSimpleJsonResponse(out, 400, "{\\"status\\":\\"error\\",\\"error\\":\\"invalid_file\\"}");
                return;
            }
            if (!target.isFile()) {
                sendSimpleJsonResponse(out, 404, "{\"status\":\"error\",\"error\":\"not_found\"}");
                return;
            }
            if (!target.delete()) {
                sendSimpleJsonResponse(out, 500, "{\"status\":\"error\",\"error\":\"delete_failed\"}");
                return;
            }
            if (listener != null) listener.onDeleted(target);
            sendSimpleJsonResponse(out, 200, "{\"status\":\"ok\"}");
        } catch (Exception e) {
            Log.w(TAG, "Delete API failed", e);
            try { sendSimpleJsonResponse(out, 500, "{\"status\":\"error\",\"error\":\"internal_error\"}"); } catch (Exception ignored) {}
        }
    }

    private void handleApiSlideshow(OutputStream out) {
        try {
            if (listener == null) {
                sendSimpleJsonResponse(out, 503, "{\"status\":\"error\",\"error\":\"unavailable\"}");
                return;
            }
            listener.onSlideshowMode();
            sendSimpleJsonResponse(out, 200, "{\"status\":\"ok\"}");
        } catch (Exception e) {
            Log.w(TAG, "Slideshow API failed", e);
            try { sendSimpleJsonResponse(out, 500, "{\"status\":\"error\",\"error\":\"internal_error\"}"); } catch (Exception ignored) {}
        }
    }

    private void handleApiClear(OutputStream out) {
        try {
            if (listener == null) {
                sendSimpleJsonResponse(out, 503, "{\"status\":\"error\",\"error\":\"unavailable\"}");
                return;
            }
            File customDir = new File(context.getFilesDir(), "custom_wallpapers");
            File[] files = customDir.exists()
                    ? customDir.listFiles((dir, name) -> resolveWallpaperFile(dir, name) != null)
                    : new File[0];
            if (files == null) {
                sendSimpleJsonResponse(out, 500, "{\"status\":\"error\",\"error\":\"storage_unavailable\"}");
                return;
            }
            for (File file : files) {
                if (file.isFile() && !file.delete()) {
                    sendSimpleJsonResponse(out, 500, "{\"status\":\"error\",\"error\":\"delete_failed\"}");
                    return;
                }
            }
            listener.onCleared();
            sendSimpleJsonResponse(out, 200, "{\"status\":\"ok\"}");
        } catch (Exception e) {
            Log.w(TAG, "Clear API failed", e);
            try { sendSimpleJsonResponse(out, 500, "{\"status\":\"error\",\"error\":\"internal_error\"}"); } catch (Exception ignored) {}
        }
    }

    private void sendSimpleJsonResponse(OutputStream out, String json) throws Exception {
        sendSimpleJsonResponse(out, 200, json);
    }

    private void sendSimpleJsonResponse(OutputStream out, int status, String json) throws Exception {
        String reason = status == 200 ? "OK" : status == 400 ? "Bad Request"
                : status == 404 ? "Not Found" : status == 503 ? "Service Unavailable" : "Internal Server Error";
        byte[] bytes = json.getBytes("UTF-8");
        String response = "HTTP/1.1 " + status + " " + reason + "\r\n" +
                "Content-Type: application/json; charset=UTF-8\r\n" +
                "Content-Length: " + bytes.length + "\r\n" +
                "Cache-Control: no-store\r\n" +
                "Access-Control-Allow-Origin: *\r\n" +
                "Connection: close\r\n\r\n";
        out.write(response.getBytes("UTF-8"));
        out.write(bytes);
        out.flush();
    }

    private String getHtmlPage() {
        String base = getWallpaperPath();
        return "<!DOCTYPE html>\n" +
                "<html lang=\"" + AppLanguage.getLanguage(context) + "\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\">\n" +
                "  <title>" + htmlText(R.string.wallpaper_page_title) + "</title>\n" +
                "  <style>\n" +
                "    * { box-sizing: border-box; margin: 0; padding: 0; }\n" +
                "    body {\n" +
                "      background: #0b0f19;\n" +
                "      color: #f1f5f9;\n" +
                "      font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, sans-serif;\n" +
                "      min-height: 100vh;\n" +
                "      display: flex;\n" +
                "      flex-direction: column;\n" +
                "      align-items: center;\n" +
                "      padding: 24px 16px 40px;\n" +
                "    }\n" +
                "    .card {\n" +
                "      width: 100%;\n" +
                "      max-width: 440px;\n" +
                "      background: rgba(255, 255, 255, 0.05);\n" +
                "      border: 1px solid rgba(255, 255, 255, 0.12);\n" +
                "      border-radius: 28px;\n" +
                "      padding: 26px 20px;\n" +
                "      box-shadow: 0 20px 40px rgba(0, 0, 0, 0.6), inset 0 1px 0 rgba(255, 255, 255, 0.2);\n" +
                "      backdrop-filter: blur(24px);\n" +
                "      -webkit-backdrop-filter: blur(24px);\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "    .badge {\n" +
                "      display: inline-block;\n" +
                "      padding: 6px 14px;\n" +
                "      background: rgba(56, 189, 248, 0.15);\n" +
                "      color: #38bdf8;\n" +
                "      border: 1px solid rgba(56, 189, 248, 0.3);\n" +
                "      border-radius: 9999px;\n" +
                "      font-size: 12px;\n" +
                "      font-weight: 600;\n" +
                "      letter-spacing: 0.5px;\n" +
                "      margin-bottom: 14px;\n" +
                "      text-transform: uppercase;\n" +
                "    }\n" +
                "    .watermark {\n" +
                "      margin-top: 18px;\n" +
                "      font-size: 13px;\n" +
                "      font-weight: 500;\n" +
                "      color: #94a3b8;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      gap: 6px;\n" +
                "      letter-spacing: 0.3px;\n" +
                "    }\n" +
                "    .watermark strong {\n" +
                "      color: #38bdf8;\n" +
                "      font-weight: 700;\n" +
                "    }\n" +
                "    h1 {\n" +
                "      font-size: 23px;\n" +
                "      font-weight: 700;\n" +
                "      margin-bottom: 8px;\n" +
                "      color: #ffffff;\n" +
                "    }\n" +
                "    p.subtitle {\n" +
                "      font-size: 13.5px;\n" +
                "      color: #94a3b8;\n" +
                "      margin-bottom: 20px;\n" +
                "      line-height: 1.5;\n" +
                "    }\n" +
                "    .dropzone {\n" +
                "      border: 2px dashed rgba(255, 255, 255, 0.2);\n" +
                "      border-radius: 20px;\n" +
                "      padding: 24px 16px;\n" +
                "      cursor: pointer;\n" +
                "      transition: all 0.2s ease;\n" +
                "      background: rgba(0, 0, 0, 0.2);\n" +
                "      margin-bottom: 18px;\n" +
                "    }\n" +
                "    .dropzone:active {\n" +
                "      border-color: #38bdf8;\n" +
                "      background: rgba(56, 189, 248, 0.08);\n" +
                "    }\n" +
                "    .dropzone-icon {\n" +
                "      font-size: 38px;\n" +
                "      margin-bottom: 8px;\n" +
                "    }\n" +
                "    .dropzone-text {\n" +
                "      font-size: 15px;\n" +
                "      font-weight: 600;\n" +
                "      color: #e2e8f0;\n" +
                "      margin-bottom: 4px;\n" +
                "    }\n" +
                "    .dropzone-hint {\n" +
                "      font-size: 12px;\n" +
                "      color: #64748b;\n" +
                "    }\n" +
                "    #preview-container {\n" +
                "      display: none;\n" +
                "      margin-bottom: 18px;\n" +
                "      position: relative;\n" +
                "      border-radius: 18px;\n" +
                "      overflow: hidden;\n" +
                "      border: 1px solid rgba(255, 255, 255, 0.15);\n" +
                "      background: #000;\n" +
                "      max-height: 220px;\n" +
                "    }\n" +
                "    #preview {\n" +
                "      width: 100%;\n" +
                "      height: 220px;\n" +
                "      object-fit: cover;\n" +
                "      display: block;\n" +
                "    }\n" +
                "    .change-btn {\n" +
                "      position: absolute;\n" +
                "      bottom: 10px;\n" +
                "      right: 10px;\n" +
                "      background: rgba(0, 0, 0, 0.7);\n" +
                "      backdrop-filter: blur(8px);\n" +
                "      color: #fff;\n" +
                "      border: 1px solid rgba(255, 255, 255, 0.3);\n" +
                "      padding: 6px 12px;\n" +
                "      border-radius: 12px;\n" +
                "      font-size: 12px;\n" +
                "      cursor: pointer;\n" +
                "    }\n" +
                "    input[type=\"file\"] { display: none; }\n" +
                "    .btn-submit {\n" +
                "      width: 100%;\n" +
                "      background: linear-gradient(135deg, #0ea5e9 0%, #2563eb 100%);\n" +
                "      color: #ffffff;\n" +
                "      border: none;\n" +
                "      border-radius: 16px;\n" +
                "      padding: 15px;\n" +
                "      font-size: 15px;\n" +
                "      font-weight: 600;\n" +
                "      cursor: pointer;\n" +
                "      box-shadow: 0 8px 20px rgba(14, 165, 233, 0.35);\n" +
                "      transition: all 0.2s ease;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "      gap: 8px;\n" +
                "    }\n" +
                "    .btn-submit:disabled {\n" +
                "      opacity: 0.5;\n" +
                "      cursor: not-allowed;\n" +
                "      box-shadow: none;\n" +
                "    }\n" +
                "    .btn-submit:active:not(:disabled) {\n" +
                "      transform: scale(0.98);\n" +
                "    }\n" +
                "    .progress-wrap {\n" +
                "      display: none;\n" +
                "      width: 100%;\n" +
                "      height: 8px;\n" +
                "      background: rgba(255, 255, 255, 0.1);\n" +
                "      border-radius: 9999px;\n" +
                "      margin-top: 16px;\n" +
                "      overflow: hidden;\n" +
                "    }\n" +
                "    .progress-bar {\n" +
                "      width: 0%;\n" +
                "      height: 100%;\n" +
                "      background: linear-gradient(90deg, #38bdf8, #4ade80);\n" +
                "      border-radius: 9999px;\n" +
                "      transition: width 0.2s ease;\n" +
                "    }\n" +
                "    .status-msg {\n" +
                "      margin-top: 14px;\n" +
                "      font-size: 13.5px;\n" +
                "      font-weight: 500;\n" +
                "      display: none;\n" +
                "    }\n" +
                "    .status-msg.success { color: #4ade80; }\n" +
                "    .status-msg.error { color: #f87171; }\n" +
                "    \n" +
                "    /* Manager Gallery Section */\n" +
                "    .gallery-card {\n" +
                "      width: 100%;\n" +
                "      max-width: 440px;\n" +
                "      margin-top: 22px;\n" +
                "      background: rgba(255, 255, 255, 0.05);\n" +
                "      border: 1px solid rgba(255, 255, 255, 0.12);\n" +
                "      border-radius: 28px;\n" +
                "      padding: 22px 18px;\n" +
                "      box-shadow: 0 20px 40px rgba(0, 0, 0, 0.6);\n" +
                "      backdrop-filter: blur(24px);\n" +
                "      -webkit-backdrop-filter: blur(24px);\n" +
                "    }\n" +
                "    .gallery-header {\n" +
                "      display: flex;\n" +
                "      justify-content: space-between;\n" +
                "      align-items: center;\n" +
                "      margin-bottom: 8px;\n" +
                "    }\n" +
                "    .gallery-title {\n" +
                "      font-size: 17px;\n" +
                "      font-weight: 700;\n" +
                "      color: #ffffff;\n" +
                "    }\n" +
                "    .gallery-desc {\n" +
                "      font-size: 12.5px;\n" +
                "      color: #94a3b8;\n" +
                "      text-align: left;\n" +
                "      margin-bottom: 14px;\n" +
                "      line-height: 1.4;\n" +
                "    }\n" +
                "    .btn-slideshow {\n" +
                "      background: rgba(56, 189, 248, 0.18);\n" +
                "      border: 1px solid rgba(56, 189, 248, 0.35);\n" +
                "      color: #38bdf8;\n" +
                "      padding: 6px 12px;\n" +
                "      border-radius: 12px;\n" +
                "      font-size: 12px;\n" +
                "      font-weight: 600;\n" +
                "      cursor: pointer;\n" +
                "      transition: all 0.2s ease;\n" +
                "    }\n" +
                "    .btn-slideshow:active { transform: scale(0.96); }\n" +
                "    .gallery-list {\n" +
                "      display: flex;\n" +
                "      flex-direction: column;\n" +
                "      gap: 10px;\n" +
                "    }\n" +
                "    .photo-row {\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      gap: 10px;\n" +
                "      background: rgba(0, 0, 0, 0.35);\n" +
                "      border: 1px solid rgba(255, 255, 255, 0.08);\n" +
                "      border-radius: 16px;\n" +
                "      padding: 8px 10px;\n" +
                "      transition: all 0.2s ease;\n" +
                "    }\n" +
                "    .photo-row.active {\n" +
                "      border-color: rgba(56, 189, 248, 0.4);\n" +
                "      background: rgba(56, 189, 248, 0.08);\n" +
                "    }\n" +
                "    .photo-thumb {\n" +
                "      width: 60px;\n" +
                "      height: 44px;\n" +
                "      border-radius: 10px;\n" +
                "      object-fit: cover;\n" +
                "      background: #1e293b;\n" +
                "      flex-shrink: 0;\n" +
                "    }\n" +
                "    .photo-meta {\n" +
                "      flex: 1;\n" +
                "      text-align: left;\n" +
                "      min-width: 0;\n" +
                "    }\n" +
                "    .photo-name {\n" +
                "      font-size: 13px;\n" +
                "      font-weight: 600;\n" +
                "      color: #f1f5f9;\n" +
                "      white-space: nowrap;\n" +
                "      overflow: hidden;\n" +
                "      text-overflow: ellipsis;\n" +
                "    }\n" +
                "    .photo-sub {\n" +
                "      font-size: 11px;\n" +
                "      color: #94a3b8;\n" +
                "      margin-top: 2px;\n" +
                "    }\n" +
                "    .photo-btns {\n" +
                "      display: flex;\n" +
                "      gap: 6px;\n" +
                "      flex-shrink: 0;\n" +
                "    }\n" +
                "    .btn-act {\n" +
                "      background: rgba(255, 255, 255, 0.08);\n" +
                "      border: 1px solid rgba(255, 255, 255, 0.15);\n" +
                "      color: #cbd5e1;\n" +
                "      padding: 6px 9px;\n" +
                "      border-radius: 10px;\n" +
                "      font-size: 11.5px;\n" +
                "      font-weight: 500;\n" +
                "      cursor: pointer;\n" +
                "    }\n" +
                "    .btn-act:active { transform: scale(0.95); }\n" +
                "    .btn-act.active {\n" +
                "      background: #0284c7;\n" +
                "      border-color: #38bdf8;\n" +
                "      color: #ffffff;\n" +
                "      font-weight: 700;\n" +
                "    }\n" +
                "    .btn-act.del {\n" +
                "      color: #f87171;\n" +
                "      border-color: rgba(248, 113, 113, 0.3);\n" +
                "      background: rgba(248, 113, 113, 0.1);\n" +
                "    }\n" +
                "    footer {\n" +
                "      margin-top: 24px;\n" +
                "      font-size: 12px;\n" +
                "      color: #475569;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"card\">\n" +
                "    <div class=\"badge\">Glass TV OS • by @imsomangge</div>\n" +
                "    <h1>" + htmlText(R.string.wallpaper_page_heading) + "</h1>\n" +
                "    <p class=\"subtitle\">" + htmlText(R.string.wallpaper_page_intro) + "</p>\n" +
                "\n" +
                "    <div class=\"dropzone\" id=\"dropzone\" onclick=\"document.getElementById('file-input').click()\">\n" +
                "      <div class=\"dropzone-icon\">🖼️</div>\n" +
                "      <div class=\"dropzone-text\">" + htmlText(R.string.wallpaper_page_choose_photo) + "</div>\n" +
                "      <div class=\"dropzone-hint\">" + htmlText(R.string.wallpaper_page_format_hint) + "</div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div id=\"preview-container\">\n" +
                "      <img id=\"preview\" alt=\"Preview\">\n" +
                "      <button class=\"change-btn\" onclick=\"document.getElementById('file-input').click()\">" + htmlText(R.string.wallpaper_page_change_photo) + "</button>\n" +
                "    </div>\n" +
                "\n" +
                "    <input type=\"file\" id=\"file-input\" accept=\"image/*\">\n" +
                "\n" +
                "    <button class=\"btn-submit\" id=\"btn-submit\" disabled onclick=\"uploadImage()\">\n" +
                "      <span>" + htmlText(R.string.wallpaper_page_upload_button) + "</span>\n" +
                "    </button>\n" +
                "\n" +
                "    <div class=\"progress-wrap\" id=\"prog-wrap\">\n" +
                "      <div class=\"progress-bar\" id=\"prog-bar\"></div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"status-msg\" id=\"status-msg\"></div>\n" +
                "    <div class=\"watermark\">✨ Powered by <strong>@imsomangge</strong></div>\n" +
                "  </div>\n" +
                "\n" +
                "  <div class=\"gallery-card\">\n" +
                "    <div class=\"gallery-header\">\n" +
                "      <div class=\"gallery-title\">" + htmlText(R.string.wallpaper_page_gallery_title) + " (<span id=\"photo-count\">0</span>)</div>\n" +
                "      <button class=\"btn-slideshow\" onclick=\"setSlideshow()\">" + htmlText(R.string.wallpaper_page_slideshow) + "</button>\n" +
                "    </div>\n" +
                "    <div class=\"gallery-desc\">" + htmlText(R.string.wallpaper_page_gallery_description) + "</div>\n" +
                "    <div class=\"gallery-list\" id=\"gallery-list\">\n" +
                "      <div style=\"color:#64748b; font-size:12.5px; padding:12px 0;\">" + htmlText(R.string.wallpaper_page_loading_gallery) + "</div>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "\n" +
                "  <footer>\n" +
                "    <div>" + htmlText(R.string.wallpaper_page_connected) + "</div>\n" +
                "    <div style=\"margin-top: 6px; font-weight: 600; color: #64748b;\">Powered by @imsomangge</div>\n" +
                "  </footer>\n" +
                "\n" +
                "  <script>\n" +
                "    const fileInput = document.getElementById('file-input');\n" +
                "    const dropzone = document.getElementById('dropzone');\n" +
                "    const previewContainer = document.getElementById('preview-container');\n" +
                "    const preview = document.getElementById('preview');\n" +
                "    const btnSubmit = document.getElementById('btn-submit');\n" +
                "    const progWrap = document.getElementById('prog-wrap');\n" +
                "    const progBar = document.getElementById('prog-bar');\n" +
                "    const statusMsg = document.getElementById('status-msg');\n" +
                "\n" +
                "    let selectedFile = null;\n" +
                "\n" +
                "    fileInput.addEventListener('change', (e) => {\n" +
                "      const file = e.target.files[0];\n" +
                "      if (!file) return;\n" +
                "      selectedFile = file;\n" +
                "\n" +
                "      const reader = new FileReader();\n" +
                "      reader.onload = (evt) => {\n" +
                "        preview.src = evt.target.result;\n" +
                "        dropzone.style.display = 'none';\n" +
                "        previewContainer.style.display = 'block';\n" +
                "        btnSubmit.disabled = false;\n" +
                "        btnSubmit.innerHTML = '<span>✨ " + jsText(R.string.wallpaper_page_set_wallpaper) + "</span>';\n" +
                "        statusMsg.style.display = 'none';\n" +
                "      };\n" +
                "      reader.readAsDataURL(file);\n" +
                "    });\n" +
                "\n" +
                "    function uploadImage() {\n" +
                "      if (!selectedFile) return;\n" +
                "\n" +
                "      btnSubmit.disabled = true;\n" +
                "      btnSubmit.innerText = '" + jsText(R.string.wallpaper_page_sending) + "';\n" +
                "      progWrap.style.display = 'block';\n" +
                "      progBar.style.width = '15%';\n" +
                "      statusMsg.style.display = 'none';\n" +
                "\n" +
                "      const xhr = new XMLHttpRequest();\n" +
                "      xhr.open('POST', '" + getWallpaperUploadPath() + "?name=' + encodeURIComponent(selectedFile.name), true);\n" +
                "      xhr.timeout = " + NetworkFeedbackPolicy.WALLPAPER_BROWSER_UPLOAD_TIMEOUT_MS + ";\n" +
                "      xhr.setRequestHeader('Content-Type', selectedFile.type || 'image/jpeg');\n" +
                "\n" +
                "      xhr.upload.onprogress = (e) => {\n" +
                "        if (e.lengthComputable) {\n" +
                "          const percent = Math.round((e.loaded / e.total) * 100);\n" +
                "          progBar.style.width = percent + '%';\n" +
                "        }\n" +
                "      };\n" +
                "\n" +
                "      xhr.onload = () => {\n" +
                "        if (xhr.status >= 200 && xhr.status < 300) {\n" +
                "          progBar.style.width = '100%';\n" +
                "          btnSubmit.innerText = '✅ " + jsText(R.string.wallpaper_page_uploaded) + "';\n" +
                "          statusMsg.className = 'status-msg success';\n" +
                "          statusMsg.innerHTML = '🎉 " + jsText(R.string.wallpaper_page_success) + "';\n" +
                "          statusMsg.style.display = 'block';\n" +
                "          loadGallery();\n" +
                "        } else {\n" +
                "          const detail = (xhr.responseText || xhr.statusText || '').trim();\n" +
                "          showError('" + jsText(R.string.wallpaper_page_upload_failed) + "' + detail);\n" +
                "        }\n" +
                "      };\n" +
                "\n" +
                "      xhr.onerror = () => {\n" +
                "        showError('" + jsText(R.string.wallpaper_page_connection_error) + "');\n" +
                "      };\n" +
                "\n" +
                "      xhr.ontimeout = () => { showError('" + jsText(R.string.wallpaper_page_upload_timeout) + "'); };\n" +
                "      xhr.send(selectedFile);\n" +
                "    }\n" +
                "\n" +
                "    function showError(msg) {\n" +
                "      btnSubmit.disabled = false;\n" +
                "      btnSubmit.innerText = '" + jsText(R.string.wallpaper_page_retry) + "';\n" +
                "      statusMsg.className = 'status-msg error';\n" +
                "      statusMsg.innerText = '❌ ' + msg;\n" +
                "      statusMsg.style.display = 'block';\n" +
                "      progWrap.style.display = 'none';\n" +
                "    }\n" +
                "\n" +
                "    function loadGallery() {\n" +
                "      fetch('" + base + "/api/list')\n" +
                "        .then(r => r.json())\n" +
                "        .then(items => {\n" +
                "          const listEl = document.getElementById('gallery-list');\n" +
                "          const countEl = document.getElementById('photo-count');\n" +
                "          if (countEl) countEl.innerText = items.length;\n" +
                "          if (!items || items.length === 0) {\n" +
                "            listEl.innerHTML = '<div style=\"color:#64748b; font-size:12.5px; padding:12px 0;\">" + jsText(R.string.wallpaper_page_gallery_empty) + "</div>';\n" +
                "            return;\n" +
                "          }\n" +
                "          let html = '';\n" +
                "          for (const item of items) {\n" +
                "            const enc = encodeURIComponent(item.name);\n" +
                "            html += '<div class=\"photo-row' + (item.active ? ' active' : '') + '\">' +\n" +
                "              '<img class=\"photo-thumb\" src=\"" + base + "/thumb?file=' + enc + '\" alt=\"thumb\">' +\n" +
                "              '<div class=\"photo-meta\">' +\n" +
                "                '<div class=\"photo-name\">' + (item.active ? '✨ ' : '') + item.name + '</div>' +\n" +
                "                '<div class=\"photo-sub\">' + item.date + ' • ' + item.size + '</div>' +\n" +
                "              '</div>' +\n" +
                "              '<div class=\"photo-btns\">' +\n" +
                "                '<button class=\"btn-act' + (item.active ? ' active' : '') + '\" onclick=\"selectSingle(\\'' + enc + '\\')\">' +\n" +
                "                  (item.active ? '" + jsText(R.string.wallpaper_page_active) + " 📌' : '" + jsText(R.string.wallpaper_page_set) + " 📌') +\n" +
                "                '</button>' +\n" +
                "                '<button class=\"btn-act del\" onclick=\"deletePhoto(\\'' + enc + '\\')\">🗑️</button>' +\n" +
                "              '</div>' +\n" +
                "            '</div>';\n" +
                "          }\n" +
                "          listEl.innerHTML = html;\n" +
                "        })\n" +
                "        .catch(() => {});\n" +
                "    }\n" +
                "\n" +
                "    function postAction(url, onSuccess) {\n" +
                "      fetch(url, { method: 'POST' })\n" +
                "        .then(r => { if (!r.ok) throw new Error('HTTP ' + r.status); return r.json(); })\n" +
                "        .then(() => { loadGallery(); if (onSuccess) onSuccess(); })\n" +
                "        .catch(() => alert('" + jsText(R.string.wallpaper_page_action_failed) + "'));\n" +
                "    }\n" +
                "\n" +
                "    function selectSingle(fileName) {\n" +
                "      postAction('" + base + "/api/select?file=' + fileName);\n" +
                "    }\n" +
                "\n" +
                "    function deletePhoto(fileName) {\n" +
                "      if (!confirm('" + jsText(R.string.wallpaper_page_confirm_delete) + "')) return;\n" +
                "      postAction('" + base + "/api/delete?file=' + fileName);\n" +
                "    }\n" +
                "\n" +
                "    function setSlideshow() {\n" +
                "      postAction('" + base + "/api/slideshow', () => alert('" + jsText(R.string.wallpaper_page_slideshow_active) + "'));\n" +
                "    }\n" +
                "\n" +
                "    // Muat galeri foto saat halaman dibuka\n" +
                "    loadGallery();\n" +
                "  </script>\n" +
                "</body>\n" +
                "</html>";
    }
}
