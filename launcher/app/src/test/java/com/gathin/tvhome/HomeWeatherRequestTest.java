package com.gathin.tvhome;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HomeWeatherRequestTest {

    @Test
    public void currentGenerationMayCommit() {
        assertTrue(HomeWeather.isCurrentRequest(7L, 7L, true, true));
    }

    @Test
    public void supersededGenerationCannotOverwriteNewerCache() {
        assertFalse(HomeWeather.isCurrentRequest(6L, 7L, true, true));
    }

    @Test
    public void changedLocationCannotCommitOldWeather() {
        assertFalse(HomeWeather.isCurrentRequest(7L, 7L, false, true));
    }

    @Test
    public void changedLanguageCannotCommitOldLocalizedWeather() {
        assertFalse(HomeWeather.isCurrentRequest(7L, 7L, true, false));
    }



    @Test
    public void detailedWeatherCurrentGenerationMayCommit() {
        assertTrue(WeatherAirQuality.isCurrentRequest(11L, 11L, true, true));
    }

    @Test
    public void detailedWeatherRejectsOlderGeneration() {
        assertFalse(WeatherAirQuality.isCurrentRequest(10L, 11L, true, true));
    }

    @Test
    public void detailedWeatherRejectsChangedLocation() {
        assertFalse(WeatherAirQuality.isCurrentRequest(11L, 11L, false, true));
    }

    @Test
    public void detailedWeatherRejectsChangedLanguage() {
        assertFalse(WeatherAirQuality.isCurrentRequest(11L, 11L, true, false));
    }

    @Test
    public void detailedWeatherCacheAcceptsExactSelectedCoordinates() {
        assertTrue(WeatherAirQuality.isSameLocation(-6.2088, 106.8456, -6.2088, 106.8456));
    }

    @Test
    public void detailedWeatherCacheRejectsDifferentSelectedCoordinates() {
        assertFalse(WeatherAirQuality.isSameLocation(-6.2088, 106.8456, -7.2575, 112.7521));
    }

    @Test
    public void detailedWeatherCacheRejectsNonFiniteCoordinates() {
        assertFalse(WeatherAirQuality.isSameLocation(Double.NaN, 106.8456, -6.2088, 106.8456));
    }
}
