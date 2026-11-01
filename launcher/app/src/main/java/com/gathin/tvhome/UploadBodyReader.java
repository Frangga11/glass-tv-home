package com.gathin.tvhome;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * Shared bounded HTTP-body receiver for the local QR upload endpoints.
 * It never reads beyond the declared Content-Length and removes partial files.
 */
final class UploadBodyReader {
    private UploadBodyReader() {}

    /**
     * Returns -1 for missing/invalid/ambiguous length, -2 when over maxBytes,
     * or the one valid positive length.
     */
    static long parseContentLength(String[] headerLines, long maxBytes) {
        if (headerLines == null || maxBytes <= 0) return -1L;
        int count = 0;
        long length = -1L;
        for (String line : headerLines) {
            if (line == null) continue;
            int separator = line.indexOf(':');
            if (separator <= 0) continue;
            String name = line.substring(0, separator).toLowerCase(Locale.ROOT);
            String value = line.substring(separator + 1).trim();
            if ("transfer-encoding".equals(name)) return -1L;
            if (!"content-length".equals(name)) continue;
            count++;
            if (count != 1 || value.isEmpty()) return -1L;
            for (int i = 0; i < value.length(); i++) {
                char ch = value.charAt(i);
                if (ch < '0' || ch > '9') return -1L;
            }
            try {
                length = Long.parseLong(value);
            } catch (NumberFormatException e) {
                return -1L;
            }
        }
        if (count != 1 || length <= 0) return -1L;
        return length > maxBytes ? -2L : length;
    }

    /**
     * Writes exactly expectedBytes to an existing temporary file. Bytes after the
     * declared body remain unread for the caller/socket close. On truncation or
     * I/O failure the partial file is deleted. Optionally fsyncs before success.
     */
    static boolean receiveExactly(InputStream in, File target, long expectedBytes,
                                  boolean syncToDisk) throws IOException {
        if (in == null || target == null || expectedBytes <= 0) {
            if (target != null) target.delete();
            return false;
        }
        long remaining = expectedBytes;
        try (FileOutputStream out = new FileOutputStream(target, false)) {
            byte[] buffer = new byte[8192];
            while (remaining > 0) {
                int count = in.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                if (count < 0) break;
                if (count == 0) {
                    int one = in.read();
                    if (one < 0) break;
                    out.write(one);
                    remaining--;
                } else {
                    out.write(buffer, 0, count);
                    remaining -= count;
                }
            }
            out.flush();
            if (remaining == 0 && target.length() == expectedBytes) {
                if (syncToDisk) out.getFD().sync();
                return true;
            }
        } catch (IOException | RuntimeException e) {
            target.delete();
            throw e;
        }
        target.delete();
        return false;
    }
}
