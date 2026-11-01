package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class UploadBodyReaderTest {
    @Test
    public void acceptsOneStrictPositiveContentLength() {
        assertEquals(3L, UploadBodyReader.parseContentLength(
                new String[]{"POST /upload HTTP/1.1", "Content-Length: 3"}, 10L));
        assertEquals(10L, UploadBodyReader.parseContentLength(
                new String[]{"Content-Length: 10"}, 10L));
    }

    @Test
    public void rejectsMissingDuplicateMalformedTransferEncodedAndOverflowLengths() {
        assertEquals(-1L, UploadBodyReader.parseContentLength(
                new String[]{"POST /upload HTTP/1.1"}, 10L));
        assertEquals(-1L, UploadBodyReader.parseContentLength(
                new String[]{"Content-Length: 1", "content-length: 1"}, 10L));
        assertEquals(-1L, UploadBodyReader.parseContentLength(
                new String[]{"Content-Length: +1"}, 10L));
        assertEquals(-1L, UploadBodyReader.parseContentLength(
                new String[]{"Content-Length: 9223372036854775808"}, Long.MAX_VALUE));
        assertEquals(-1L, UploadBodyReader.parseContentLength(
                new String[]{"Content-Length: 1", "Transfer-Encoding: chunked"}, 10L));
        assertEquals(-2L, UploadBodyReader.parseContentLength(
                new String[]{"Content-Length: 11"}, 10L));
    }

    @Test
    public void receivesExactlyDeclaredBytesAndLeavesFollowingBytesUnread() throws Exception {
        File target = File.createTempFile("upload-body-", ".test");
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(
                    "bodyEXTRA".getBytes(StandardCharsets.UTF_8));
            assertTrue(UploadBodyReader.receiveExactly(input, target, 4L, true));
            assertEquals("body", new String(readFile(target), StandardCharsets.UTF_8));
            assertEquals('E', input.read());
        } finally {
            target.delete();
        }
    }

    @Test
    public void deletesPartialFileWhenBodyIsTruncated() throws Exception {
        File target = File.createTempFile("upload-body-", ".test");
        try {
            assertFalse(UploadBodyReader.receiveExactly(
                    new ByteArrayInputStream(new byte[]{1, 2}), target, 3L, false));
            assertFalse(target.exists());
        } finally {
            target.delete();
        }
    }

    @Test
    public void makesProgressWhenAnInputStreamReturnsZeroLengthReads() throws Exception {
        File target = File.createTempFile("upload-body-", ".test");
        try {
            InputStream input = new ByteArrayInputStream(new byte[]{7, 8, 9}) {
                private boolean returnZero = true;
                @Override public synchronized int read(byte[] bytes, int offset, int length) {
                    if (returnZero) {
                        returnZero = false;
                        return 0;
                    }
                    return super.read(bytes, offset, length);
                }
            };
            assertTrue(UploadBodyReader.receiveExactly(input, target, 3L, false));
            assertEquals(3L, target.length());
        } finally {
            target.delete();
        }
    }

    private static byte[] readFile(File file) throws IOException {
        java.io.FileInputStream in = new java.io.FileInputStream(file);
        try {
            byte[] bytes = new byte[(int) file.length()];
            int offset = 0;
            while (offset < bytes.length) {
                int count = in.read(bytes, offset, bytes.length - offset);
                if (count < 0) break;
                offset += count;
            }
            return bytes;
        } finally {
            in.close();
        }
    }
}
