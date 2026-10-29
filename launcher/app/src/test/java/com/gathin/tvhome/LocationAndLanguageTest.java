package com.gathin.tvhome;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class LocationAndLanguageTest {

    @Test
    public void testSingaporeLocation() {
        Location.Zone sg = Location.byCode("SGP01");
        assertNotNull("Singapore zone should exist", sg);
        assertEquals("Singapura", sg.town);
        assertEquals(1.3521, sg.lat, 0.001);
        assertEquals(103.8198, sg.lon, 0.001);
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
    public void testStatesIncludeNewCountries() {
        List<String> states = Location.states();
        assertTrue("States should contain Singapore", states.contains("Singapura (Singapore)"));
        assertTrue("States should contain DKI Jakarta", states.contains("Indonesia: DKI Jakarta"));
        assertTrue("States should contain Jawa Barat", states.contains("Indonesia: Jawa Barat"));
        assertTrue("States should contain Bali", states.contains("Indonesia: Bali"));
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
