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


}
