package com.gathin.tvhome;

import android.os.SystemClock;

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
    interface MonotonicClock {
        long elapsedRealtime();
    }

    private final Socket socket;
    private final long deadlineAtMs;
    private final int idleTimeoutMs;
    private final MonotonicClock clock;

    UploadDeadlineInputStream(InputStream input, Socket socket, long deadlineAtMs, int idleTimeoutMs) {
        this(input, socket, deadlineAtMs, idleTimeoutMs, () -> SystemClock.elapsedRealtime());
    }

    UploadDeadlineInputStream(InputStream input, Socket socket, long deadlineAtMs,
                              int idleTimeoutMs, MonotonicClock clock) {
        super(input);
        if (socket == null) throw new IllegalArgumentException("socket is required");
        if (idleTimeoutMs <= 0) throw new IllegalArgumentException("idle timeout must be positive");
        if (clock == null) throw new IllegalArgumentException("monotonic clock is required");
        this.socket = socket;
        this.deadlineAtMs = deadlineAtMs;
        this.idleTimeoutMs = idleTimeoutMs;
        this.clock = clock;
    }

    private void applyReadDeadline() throws IOException {
        int timeout = NetworkFeedbackPolicy.boundedReadTimeoutMs(
                deadlineAtMs, idleTimeoutMs, clock.elapsedRealtime());
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
