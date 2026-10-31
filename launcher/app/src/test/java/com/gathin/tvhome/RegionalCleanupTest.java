package com.gathin.tvhome;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RegionalCleanupTest {
    @Test
    public void defaultLocationIsPagarAlamIndonesia() {
        assertTrue("Indonesia".equals(AppLocationManager.DEFAULT_REGION));
        assertTrue("ID_PGA".equals(AppLocationManager.DEFAULT_STATION_ID));
        assertTrue("Pagar Alam".equals(AppLocationManager.DEFAULT_LOCATION_NAME));
    }

    @Test
    public void retiredMalaysiaRegionIsNotSelectable() {
        assertFalse(AppLocationManager.getAvailableRegions().contains("Malaysia"));
        assertNotNull(AppLocationManager.getStationsForRegion(AppLocationManager.DEFAULT_REGION));
        assertFalse(AppLocationManager.getStationsForRegion(AppLocationManager.DEFAULT_REGION).isEmpty());
    }

    @Test
    public void retiredMalayWallpaperCategoryDoesNotMatch() {
        assertFalse(OnlineWallpapers.matchesCategory("Malay", "Malay"));
        assertTrue(OnlineWallpapers.matchesCategory("Nature", "Natural"));
        assertTrue(OnlineWallpapers.matchesCategory("Ocean", "Ocean"));
        assertTrue(OnlineWallpapers.matchesCategory("Space", "Space"));
    }
}
