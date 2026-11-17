package com.gathin.tvhome;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;

public class WallpaperMetadataTest {
    @Test
    public void defaultDempoUsesExternalMetadata() {
        String[] metadata = WallpaperMetadata.describe(new File("/data/user/0/com.gathin.tvhome/files/kabut-pagi-dempo-default.jpg"));
        assertNotNull(metadata);
        assertEquals("Kabut Pagi Dempo", metadata[0]);
        assertEquals("Udara sejuk turun perlahan, menyelimuti lereng dan kebun teh Pagar Alam.", metadata[1]);
        assertEquals("Pagar Alam, Sumatera Selatan", metadata[2]);
    }

    @Test
    public void customAndOtherWallpapersDoNotReceiveDefaultMetadata() {
        assertNull(WallpaperMetadata.describe(new File("/data/user/0/com.gathin.tvhome/files/custom_wallpapers/foto.jpg")));
        assertNull(WallpaperMetadata.describe(new File("/data/user/0/com.gathin.tvhome/files/wallpaper-nature.jpg")));
        assertNull(WallpaperMetadata.describe(null));
    }

    @Test
    public void defaultDetectionUsesExactFilename() {
        assertTrue(WallpaperMetadata.isManagedDefault(new File("kabut-pagi-dempo-default.jpg")));
        assertFalse(WallpaperMetadata.isManagedDefault(new File("kabut-pagi-dempo-default.jpg.bak")));
        assertFalse(WallpaperMetadata.isManagedDefault(new File("kabut-pagi-dempo.jpg")));
    }
}
