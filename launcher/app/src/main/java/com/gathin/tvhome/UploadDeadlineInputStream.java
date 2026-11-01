package com.gathin.tvhome;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Applies both an idle-read timeout and an absolute request deadline to every socket read.
 * A peer cannot keep a session alive indefinitely by sending one byte just before each idle timeout.
 */
final class UploadDeadlineInputStream extends FilterInputStream {
    private final Socket socket;
    private final long deadlineAtMs;
    private final int idleTimeoutMs;

    UploadDeadlineInputStream(InputStream input, Socket socket, long deadlineAtMs, int idleTimeoutMs) {
        super(input);
        if (socket == null) throw new IllegalArgumentException("socket is required");
        if (idleTimeoutMs <= 0) throw new IllegalArgumentException("idle timeout must be positive");
        this.socket = socket;
        this.deadlineAtMs = deadlineAtMs;
        this.idleTimeoutMs = idleTimeoutMs;
    }

    private void applyReadDeadline() throws IOException {
        int timeout = NetworkFeedbackPolicy.boundedReadTimeoutMs(
                deadlineAtMs, idleTimeoutMs, System.currentTimeMillis());
        if (timeout <= 0) throw new SocketTimeoutException("Upload request deadline exceeded");
        socket.setSoTimeout(timeout);
    }

    @Override
    public int read() throws IOException {
        applyReadDeadline();
        return super.read();
    }

    @Override
    public int read(byte[] bytes, int offset, int length) throws IOException {
        if (length == 0) return 0;
        applyReadDeadline();
        return super.read(bytes, offset, length);
    }
}
