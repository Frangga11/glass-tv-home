package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

public class CacheCleanerTest {
    @Test
    public void requiresExactSuccessMarkerLine() {
        assertTrue(CacheCleaner.isTrimSuccessOutput("__GLASS_TV_SYSTEM_TRIM_OK__\n"));
        assertTrue(CacheCleaner.isTrimSuccessOutput("Success\r\n__GLASS_TV_SYSTEM_TRIM_OK__\r\n"));
        assertFalse(CacheCleaner.isTrimSuccessOutput("Failure: permission denied\n"));
        assertFalse(CacheCleaner.isTrimSuccessOutput("command finished\n"));
        assertFalse(CacheCleaner.isTrimSuccessOutput(null));
    }
    @Test
    public void countsOnlyBytesFromFilesActuallyDeleted() throws Exception {
        File root = Files.createTempDirectory("glass-tv-cache-cleaner").toFile();
        File nested = new File(root, "nested");
        assertTrue(nested.mkdir());
        File cacheFile = new File(nested, "cache.tmp");
        Files.write(cacheFile.toPath(), new byte[]{1, 2, 3, 4, 5});

        assertEquals(5L, CacheCleaner.deleteDirContents(root));
        assertFalse(cacheFile.exists());
        assertTrue(root.exists());

        nested.delete();
        root.delete();
    }

}
