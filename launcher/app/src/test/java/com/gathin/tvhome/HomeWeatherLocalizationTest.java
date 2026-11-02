package com.gathin.tvhome;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/** Ensures the Home weather summary changes language without stale Indonesian labels. */
public class HomeWeatherLocalizationTest {
    @Test
    public void weatherConditionsAreLocalized() {
        assertEquals("Cerah", HomeWeather.describe(0, false));
        assertEquals("Clear sky", HomeWeather.describe(0, true));
        assertEquals("Cerah berawan", HomeWeather.describe(2, false));
        assertEquals("Partly cloudy", HomeWeather.describe(2, true));
        assertEquals("Hujan lebat", HomeWeather.describe(65, false));
        assertEquals("Heavy rain", HomeWeather.describe(65, true));
        assertEquals("Badai petir", HomeWeather.describe(95, false));
        assertEquals("Thunderstorm", HomeWeather.describe(95, true));
    }

    @Test
    public void airQualityBandsAreLocalized() {
        assertEquals("Baik", HomeWeather.aqiName(0, false));
        assertEquals("Good", HomeWeather.aqiName(0, true));
        assertEquals("Tidak sehat bagi kelompok sensitif", HomeWeather.aqiName(2, false));
        assertEquals("Unhealthy for sensitive groups", HomeWeather.aqiName(2, true));
        assertEquals("Berbahaya", HomeWeather.aqiName(5, false));
        assertEquals("Hazardous", HomeWeather.aqiName(5, true));
    }
}
