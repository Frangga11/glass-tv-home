package com.gathin.tvhome;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CacheCleanerTest {
    @Test
    public void requiresExactSuccessMarkerLine() {
        assertTrue(CacheCleaner.isTrimSuccessOutput("__GLASS_TV_SYSTEM_TRIM_OK__\n"));
        assertTrue(CacheCleaner.isTrimSuccessOutput("Success\r\n__GLASS_TV_SYSTEM_TRIM_OK__\r\n"));
        assertFalse(CacheCleaner.isTrimSuccessOutput("Failure: permission denied\n"));
        assertFalse(CacheCleaner.isTrimSuccessOutput("command finished\n"));
        assertFalse(CacheCleaner.isTrimSuccessOutput(null));
    }
}
