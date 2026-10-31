package com.gathin.tvhome;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class LocationAndLanguageTest {

    @Test
    public void testNonIndonesianLocationsAreRemoved() {
        assertNull("Legacy Malaysia locations must not be available", Location.byCode("WLY01"));
        assertNull("Singapore locations must not be available", Location.byCode("SGP01"));
    }

    @Test
    public void testIndonesiaLocations() {
        Location.Zone jkt = Location.byCode("ID_JKT");
        assertNotNull("Jakarta zone should exist", jkt);
        assertEquals("Jakarta", jkt.town);
        assertEquals(-6.2088, jkt.lat, 0.001);
        assertEquals(106.8456, jkt.lon, 0.001);

        Location.Zone dps = Location.byCode("ID_DPS");
        assertNotNull("Bali zone should exist", dps);
        assertEquals("Denpasar", dps.town);
        assertEquals(115.2126, dps.lon, 0.001); // WITA

        Location.Zone upg = Location.byCode("ID_UPG");
        assertNotNull("Makassar zone should exist", upg);
        assertEquals("Makassar", upg.town);

        Location.Zone mdc = Location.byCode("ID_MDC");
        assertNotNull("Manado zone should exist", mdc);
        assertEquals("Manado", mdc.town);
        assertEquals(124.8421, mdc.lon, 0.001);
    }

    @Test
    public void testLocationPickerShowsIndonesiaOnly() {
        List<String> states = Location.states();
        assertFalse("Singapore must not appear in the Indonesia-only picker", states.contains("Singapura (Singapore)"));
        assertFalse("Malaysia regions must not appear in the Indonesia-only picker", states.contains("Wilayah Persekutuan"));
        assertTrue("States should contain DKI Jakarta", states.contains("Indonesia: DKI Jakarta"));
        assertTrue("States should contain Sumatera Selatan", states.contains("Indonesia: Sumatera Selatan"));
        assertTrue("States should contain Jawa Barat", states.contains("Indonesia: Jawa Barat"));
        assertTrue("States should contain Bali", states.contains("Indonesia: Bali"));
        assertNotNull("Pagar Alam must be available", Location.byCode("ID_PGA"));
    }

    @Test
    public void testWeeklyAyahTranslations() {
        WeeklyAyah.Ayah a = new WeeklyAyah.Ayah(
                "فَاذْكُرُونِي أَذْكُرْكُمْ",
                "Oleh itu ingatlah kamu kepada-Ku",
                "So remember Me",
                "Maka ingatlah kepada-Ku",
                "2:152"
        );

        assertEquals("Oleh itu ingatlah kamu kepada-Ku", a.translation("ms"));
        assertEquals("So remember Me", a.translation("en"));
        assertEquals("Maka ingatlah kepada-Ku", a.translation("id"));
        assertEquals("So remember Me", a.translation(true));
        assertEquals("Oleh itu ingatlah kamu kepada-Ku", a.translation(false));
    }
}
