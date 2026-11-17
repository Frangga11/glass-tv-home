package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class UploadFilePolicyTest {
    @Test
    public void detectsOnlySupportedImageSignatures() throws Exception {
        assertEquals(UploadFilePolicy.ImageFormat.JPEG, detect(
                new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, 0}));
        assertEquals(UploadFilePolicy.ImageFormat.PNG, detect(
                new byte[]{(byte) 0x89, 'P', 'N', 'G', 13, 10, 26, 10}));
        assertEquals(UploadFilePolicy.ImageFormat.WEBP, detect(
                "RIFF0000WEBP".getBytes(StandardCharsets.US_ASCII)));
        assertNull(detect("not an image".getBytes(StandardCharsets.US_ASCII)));
        assertNull(detect(new byte[]{1, 2}));
    }

    @Test
    public void requiresDeclaredMimeToMatchDetectedFormat() {
        UploadFilePolicy.ImageFormat jpeg = UploadFilePolicy.ImageFormat.JPEG;
        assertTrue(UploadFilePolicy.hasCompatibleContentType(null, jpeg));
        assertTrue(UploadFilePolicy.hasCompatibleContentType(
                new String[]{"Content-Type: application/octet-stream"}, jpeg));
        assertTrue(UploadFilePolicy.hasCompatibleContentType(
                new String[]{"Content-Type: image/jpeg; charset=binary"}, jpeg));
        assertTrue(UploadFilePolicy.hasCompatibleContentType(
                new String[]{"Content-Type: image/jpg"}, jpeg));
        assertFalse(UploadFilePolicy.hasCompatibleContentType(
                new String[]{"Content-Type: image/png"}, jpeg));
        assertFalse(UploadFilePolicy.hasCompatibleContentType(
                new String[]{"Content-Type: text/plain"}, jpeg));
        assertFalse(UploadFilePolicy.hasCompatibleContentType(
                new String[]{"Content-Type: "}, jpeg));
        assertFalse(UploadFilePolicy.hasCompatibleContentType(
                new String[]{"Content-Type: image/jpeg", "Content-Type: image/jpeg"}, jpeg));
        assertFalse(UploadFilePolicy.hasCompatibleContentType(null, null));
    }

    @Test
    public void enforcesWallpaperAndStandbyDimensionLimitsWithoutOverflow() {
        assertFalse(UploadFilePolicy.isValidWallpaperImageDimensions(0, 100));
        assertFalse(UploadFilePolicy.isValidWallpaperImageDimensions(100, -1));
        assertFalse(UploadFilePolicy.isValidWallpaperImageDimensions(16385, 1));
        assertFalse(UploadFilePolicy.isValidWallpaperImageDimensions(10000, 10001));
        assertTrue(UploadFilePolicy.isValidWallpaperImageDimensions(10000, 10000));
        assertTrue(UploadFilePolicy.isValidWallpaperImageDimensions(16384, 1));
    }

    @Test
    public void acceptsOnlySafeGeneratedImageFileNames() {
        assertTrue(UploadFilePolicy.isSafeStoredImageName("custom_123.jpg"));
        assertTrue(UploadFilePolicy.isSafeStoredImageName("standby_123.webp"));
        assertTrue(UploadFilePolicy.isSafeStoredImageName("avatar-1.png"));
        assertFalse(UploadFilePolicy.isSafeStoredImageName(null));
        assertFalse(UploadFilePolicy.isSafeStoredImageName("../custom_123.jpg"));
        assertFalse(UploadFilePolicy.isSafeStoredImageName("folder/custom_123.jpg"));
        assertFalse(UploadFilePolicy.isSafeStoredImageName("folder\\\\custom_123.jpg"));
        assertFalse(UploadFilePolicy.isSafeStoredImageName("..jpg"));
        assertFalse(UploadFilePolicy.isSafeStoredImageName(".hidden.jpg"));
        assertFalse(UploadFilePolicy.isSafeStoredImageName("custom_123.exe"));
        assertFalse(UploadFilePolicy.isSafeStoredImageName("custom name.jpg"));
    }

    @Test
    public void resolvesOnlyExistingDirectChildrenOfManagedDirectory() throws Exception {
        File root = createTempDirectory();
        File image = new File(root, "custom_123.jpg");
        try (FileOutputStream output = new FileOutputStream(image)) {
            output.write(new byte[]{1});
        }
        try {
            assertNotNull(UploadFilePolicy.resolveDirectChild(root, image.getName()));
            assertNull(UploadFilePolicy.resolveDirectChild(root, "../outside.jpg"));
            assertNull(UploadFilePolicy.resolveDirectChild(root, "missing.jpg"));
        } finally {
            image.delete();
            root.delete();
        }
    }

    private static UploadFilePolicy.ImageFormat detect(byte[] bytes) throws Exception {
        File file = File.createTempFile("upload-format-", ".bin");
        try (FileOutputStream output = new FileOutputStream(file)) {
            output.write(bytes);
        }
        try {
            return UploadFilePolicy.detectImageFormat(file);
        } finally {
            file.delete();
        }
    }

    private static File createTempDirectory() throws Exception {
        File marker = File.createTempFile("upload-policy-", ".tmp");
        if (!marker.delete() || !marker.mkdir()) throw new IllegalStateException("Cannot create test directory");
        return marker;
    }
}
