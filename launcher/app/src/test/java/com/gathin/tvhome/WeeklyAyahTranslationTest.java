package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WeeklyAyahTranslationTest {
    @Test
    public void IndonesianFallsBackToMalayWhenItsTranslationIsUnavailable() {
        WeeklyAyah.Ayah ayah = new WeeklyAyah.Ayah("arabic", "Malay meaning", "English meaning", "", "2:152");
        assertEquals("Malay meaning", ayah.translation(AppLanguage.LANG_ID));
    }

    @Test
    public void EnglishUsesEnglishTranslationWhenAvailable() {
        WeeklyAyah.Ayah ayah = new WeeklyAyah.Ayah("arabic", "Malay meaning", "English meaning", "", "2:152");
        assertEquals("English meaning", ayah.translation(AppLanguage.LANG_EN));
    }

    @Test
    public void IndonesianFallsBackToEnglishWhenMalayIsAlsoUnavailable() {
        WeeklyAyah.Ayah ayah = new WeeklyAyah.Ayah("arabic", "", "English meaning", "", "2:152");
        assertEquals("English meaning", ayah.translation(AppLanguage.LANG_ID));
    }

    @Test
    public void MalayRemainsTheDefaultTranslation() {
        WeeklyAyah.Ayah ayah = new WeeklyAyah.Ayah("arabic", "Malay meaning", "English meaning", "", "2:152");
        assertEquals("Malay meaning", ayah.translation(AppLanguage.LANG_MS));
    }

    @Test
    public void MalayFallsBackToEnglishWhenMalayIsUnavailable() {
        WeeklyAyah.Ayah ayah = new WeeklyAyah.Ayah("arabic", "", "English meaning", "", "2:152");
        assertEquals("English meaning", ayah.translation(AppLanguage.LANG_MS));
    }
    @Test
    public void retryCooldownSuppressesRepeatedSameDayRequests() {
        long now = 10L * 60L * 60L * 1000L;
        assertEquals(false, WeeklyAyah.isRetryDue(100, now - 10L * 60L * 1000L, 100, now));
        assertEquals(true, WeeklyAyah.isRetryDue(100, now - 31L * 60L * 1000L, 100, now));
    }

    @Test
    public void newDayCanRefreshImmediatelyEvenWhenPreviousDayWasRecent() {
        long now = 10L * 60L * 60L * 1000L;
        assertEquals(true, WeeklyAyah.isRetryDue(100, now - 1L * 60L * 1000L, 101, now));
    }

    @Test
    public void missingAttemptCanRefreshImmediately() {
        assertEquals(true, WeeklyAyah.isRetryDue(Integer.MIN_VALUE, 0L, 100, 1000L));
    }
}
