package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PrayerTimesNextPrayerTest {
    private static final long HOUR = 60L * 60L * 1000L;

    @Test
    public void selectsNextPrayerAndSkipsSunrise() {
        long[] times = {5 * HOUR, 6 * HOUR, 12 * HOUR, 15 * HOUR, 18 * HOUR, 20 * HOUR};
        assertEquals(2, PrayerTimes.findNextPrayerIndex(times, 7 * HOUR));
        assertEquals(5, PrayerTimes.findNextPrayerIndex(times, 19 * HOUR));
    }

    @Test
    public void returnsNoSameDayPrayerAfterIsya() {
        long[] times = {5 * HOUR, 6 * HOUR, 12 * HOUR, 15 * HOUR, 18 * HOUR, 20 * HOUR};
        assertEquals(-1, PrayerTimes.findNextPrayerIndex(times, 21 * HOUR));
    }

    @Test
    public void usesTomorrowSubuhAfterIsya() {
        long[] today = {5 * HOUR, 6 * HOUR, 12 * HOUR, 15 * HOUR, 18 * HOUR, 20 * HOUR};
        long[] tomorrow = {29 * HOUR + 10 * 60 * 1000L, 30 * HOUR, 36 * HOUR, 39 * HOUR, 42 * HOUR, 44 * HOUR};
        assertEquals(tomorrow[0], PrayerTimes.nextSubuhTime(today, tomorrow));
    }

    @Test
    public void fallsBackToNextDayWhenTomorrowDataIsUnavailable() {
        long[] today = {5 * HOUR, 6 * HOUR, 12 * HOUR, 15 * HOUR, 18 * HOUR, 20 * HOUR};
        assertEquals(today[0] + 24 * HOUR, PrayerTimes.nextSubuhTime(today, null));
    }
}