package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.junit.Test;

public class UploadDeadlineInputStreamTest {
    private static final class TimeoutRecordingSocket extends Socket {
        int lastTimeout;

        @Override
        public void setSoTimeout(int timeout) {
            lastTimeout = timeout;
        }
    }

    private static final class FakeMonotonicClock implements UploadDeadlineInputStream.MonotonicClock {
        long nowMs;

        FakeMonotonicClock(long nowMs) {
            this.nowMs = nowMs;
        }

        @Override
        public long elapsedRealtime() {
            return nowMs;
        }
    }

    @Test
    public void appliesRemainingDeadlineToEachRead() throws Exception {
        TimeoutRecordingSocket socket = new TimeoutRecordingSocket();
        FakeMonotonicClock clock = new FakeMonotonicClock(1000L);
        UploadDeadlineInputStream input = new UploadDeadlineInputStream(
                new ByteArrayInputStream(new byte[]{10, 20}), socket,
                6000L, 30000, clock);

        assertEquals(10, input.read());
        assertEquals(5000, socket.lastTimeout);
        clock.nowMs = 2500L;
        assertEquals(20, input.read());
        assertEquals(3000, socket.lastTimeout);
        assertEquals(-1, input.read());
    }

    @Test(expected = SocketTimeoutException.class)
    public void rejectsReadsAfterAbsoluteDeadline() throws Exception {
        TimeoutRecordingSocket socket = new TimeoutRecordingSocket();
        FakeMonotonicClock clock = new FakeMonotonicClock(2000L);
        UploadDeadlineInputStream input = new UploadDeadlineInputStream(
                new ByteArrayInputStream(new byte[]{10}), socket,
                1999L, 30000, clock);
        input.read();
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsMissingMonotonicClock() {
        new UploadDeadlineInputStream(
                new ByteArrayInputStream(new byte[]{10}), new TimeoutRecordingSocket(),
                5000L, 30000, null);
    }
}
