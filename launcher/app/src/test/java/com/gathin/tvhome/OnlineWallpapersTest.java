package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class OnlineWallpapersTest {

    @Test
    public void prunesOldestImagesUntilAggregateByteBudgetIsMet() throws Exception {
        File dir = java.nio.file.Files.createTempDirectory("online-wallpaper-budget").toFile();
        File oldest = new File(dir, "oldest.jpg");
        File middle = new File(dir, "middle.jpg");
        File newest = new File(dir, "newest.jpg");
        try (FileOutputStream out = new FileOutputStream(oldest)) { out.write(new byte[4]); }
        try (FileOutputStream out = new FileOutputStream(middle)) { out.write(new byte[4]); }
        try (FileOutputStream out = new FileOutputStream(newest)) { out.write(new byte[4]); }
        java.nio.file.Files.write(new File(dir, "oldest.txt").toPath(), new byte[]{1});
        oldest.setLastModified(1_000L);
        middle.setLastModified(2_000L);
        newest.setLastModified(3_000L);

        OnlineWallpapers.pruneCache(dir, 10, 8L);

        assertTrue(!oldest.exists());
        assertTrue(!new File(dir, "oldest.txt").exists());
        assertTrue(middle.exists());
        assertTrue(newest.exists());
        assertEquals(8L, middle.length() + newest.length());

        middle.delete();
        newest.delete();
        dir.delete();
    }

    @Test
    public void pruneCacheHonorsFileCountAndAggregateBudgetsTogether() throws Exception {
        File dir = java.nio.file.Files.createTempDirectory("online-wallpaper-count").toFile();
        File a = new File(dir, "a.jpg");
        File b = new File(dir, "b.jpg");
        File c = new File(dir, "c.jpg");
        try (FileOutputStream out = new FileOutputStream(a)) { out.write(new byte[2]); }
        try (FileOutputStream out = new FileOutputStream(b)) { out.write(new byte[2]); }
        try (FileOutputStream out = new FileOutputStream(c)) { out.write(new byte[2]); }
        a.setLastModified(1_000L);
        b.setLastModified(2_000L);
        c.setLastModified(3_000L);

        OnlineWallpapers.pruneCache(dir, 2, 100L);

        assertTrue(!a.exists());
        assertTrue(b.exists());
        assertTrue(c.exists());
        b.delete();
        c.delete();
        dir.delete();
    }

    @Test
    public void describeReadsCompanionTextFile() throws Exception {
        File dir = File.createTempFile("online_test", "");
        dir.delete();
        dir.mkdirs();

        File jpg = new File(dir, "photo1.jpg");
        jpg.createNewFile();

        File txt = new File(dir, "photo1.txt");
        try (FileOutputStream os = new FileOutputStream(txt)) {
            os.write("Moody Alpine Mountains\nDark 4K Landscape\nUnsplash\n".getBytes(StandardCharsets.UTF_8));
        }

        String[] about = OnlineWallpapers.describe(jpg);
        assertNotNull(about);
        assertEquals(3, about.length);
        assertEquals("Moody Alpine Mountains", about[0]);
        assertEquals("Dark 4K Landscape", about[1]);
        assertEquals("Unsplash", about[2]);

        jpg.delete();
        txt.delete();
        dir.delete();
    }
}
