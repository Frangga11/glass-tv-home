package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30)
public class WeatherAirQualityFreshnessTest {
    private static final long INTERVAL = 30L * 60L * 1000L;

    @Test
    public void cacheIsFreshOnlyForMatchingCoordinatesWithinRefreshWindow() {
        long now = 2_000_000L;
        assertTrue(WeatherAirQuality.isCacheFresh(true, now, now - 5 * 60_000L,
                "-6.2088", "106.8456", -6.2088, 106.8456, INTERVAL));
    }

    @Test
    public void cacheFromPreviousLocationCannotSuppressRefresh() {
        long now = 2_000_000L;
        assertFalse(WeatherAirQuality.isCacheFresh(true, now, now - 5 * 60_000L,
                "-6.2088", "106.8456", -7.2575, 112.7521, INTERVAL));
    }

    @Test
    public void legacyJasCacheCannotSuppressCamsRefresh() {
        long now = 2_000_000L;
        assertFalse(WeatherAirQuality.isAqiCacheFresh(true, true, now, now - 1_000L,
                "-6.2088", "106.8456", -6.2088, 106.8456, INTERVAL));
        assertTrue(WeatherAirQuality.isAqiCacheFresh(true, false, now, now - 1_000L,
                "-6.2088", "106.8456", -6.2088, 106.8456, INTERVAL));
    }

    @Test
    public void cacheAtRefreshBoundaryIsStale() {
        long now = 2_000_000L;
        assertFalse(WeatherAirQuality.isCacheFresh(true, now, now - INTERVAL,
                "-6.2088", "106.8456", -6.2088, 106.8456, INTERVAL));
    }

    @Test
    public void missingOrInvalidCacheCoordinatesAreNeverFresh() {
        long now = 2_000_000L;
        assertFalse(WeatherAirQuality.isCacheFresh(true, now, now - 1_000L,
                null, "106.8456", -6.2088, 106.8456, INTERVAL));
        assertFalse(WeatherAirQuality.isCacheFresh(true, now, now - 1_000L,
                "invalid", "106.8456", -6.2088, 106.8456, INTERVAL));
    }

    @Test
    public void futureTimestampAndMissingCacheAreNeverFresh() {
        long now = 2_000_000L;
        assertFalse(WeatherAirQuality.isCacheFresh(true, now, now + 1_000L,
                "-6.2088", "106.8456", -6.2088, 106.8456, INTERVAL));
        assertFalse(WeatherAirQuality.isCacheFresh(false, now, now - 1_000L,
                "-6.2088", "106.8456", -6.2088, 106.8456, INTERVAL));
    }

    @Test
    public void missingOrInvalidPollutantsRemainUnavailableInsteadOfZero() throws Exception {
        JSONObject current = new JSONObject("{\"pm2_5\":12.4,\"pm10\":null,\"carbon_monoxide\":\"invalid\"}");
        assertEquals(12.4, WeatherAirQuality.doubleValue(current, "pm2_5"), 0.001);
        assertTrue(Double.isNaN(WeatherAirQuality.doubleValue(current, "pm10")));
        assertTrue(Double.isNaN(WeatherAirQuality.doubleValue(current, "carbon_monoxide")));
        assertTrue(Double.isNaN(WeatherAirQuality.doubleValue(current, "nitrogen_dioxide")));
    }
}
