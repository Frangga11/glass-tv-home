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

    @Test
    public void appliesRemainingDeadlineToEachRead() throws Exception {
        TimeoutRecordingSocket socket = new TimeoutRecordingSocket();
        UploadDeadlineInputStream input = new UploadDeadlineInputStream(
                new ByteArrayInputStream(new byte[]{10, 20}), socket,
                System.currentTimeMillis() + 5000L, 30000);
        assertEquals(10, input.read());
        assertEquals(20, input.read());
        assertEquals(-1, input.read());
        assertTrueBounded(socket.lastTimeout);
    }

    @Test(expected = SocketTimeoutException.class)
    public void rejectsReadsAfterAbsoluteDeadline() throws Exception {
        TimeoutRecordingSocket socket = new TimeoutRecordingSocket();
        UploadDeadlineInputStream input = new UploadDeadlineInputStream(
                new ByteArrayInputStream(new byte[]{10}), socket,
                System.currentTimeMillis() - 1L, 30000);
        input.read();
    }

    private static void assertTrueBounded(int timeout) {
        if (timeout <= 0 || timeout > 30000) {
            throw new AssertionError("Read timeout was not bounded: " + timeout);
        }
    }
}
