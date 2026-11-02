package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class UploadDeadlineInputStreamTest {
    private static final class TimeoutRecordingController
            implements UploadDeadlineInputStream.SocketTimeoutController {
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
        TimeoutRecordingController controller = new TimeoutRecordingController();
        FakeMonotonicClock clock = new FakeMonotonicClock(1000L);
        UploadDeadlineInputStream input = new UploadDeadlineInputStream(
                new ByteArrayInputStream(new byte[]{10, 20}), controller,
                6000L, 30000, clock);

        assertEquals(10, input.read());
        assertEquals(5000, controller.lastTimeout);
        clock.nowMs = 2500L;
        assertEquals(20, input.read());
        assertEquals(3500, controller.lastTimeout);
        assertEquals(-1, input.read());
    }

    @Test(expected = java.net.SocketTimeoutException.class)
    public void rejectsReadsAfterAbsoluteDeadline() throws Exception {
        TimeoutRecordingController controller = new TimeoutRecordingController();
        FakeMonotonicClock clock = new FakeMonotonicClock(2000L);
        UploadDeadlineInputStream input = new UploadDeadlineInputStream(
                new ByteArrayInputStream(new byte[]{10}), controller,
                1999L, 30000, clock);
        input.read();
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsMissingMonotonicClock() {
        new UploadDeadlineInputStream(
                new ByteArrayInputStream(new byte[]{10}),
                (UploadDeadlineInputStream.SocketTimeoutController) timeout -> {},
                5000L, 30000, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsMissingTimeoutController() {
        new UploadDeadlineInputStream(
                new ByteArrayInputStream(new byte[]{10}),
                (UploadDeadlineInputStream.SocketTimeoutController) null,
                5000L, 30000, () -> 1000L);
    }
}
