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
    public void MalayRemainsTheDefaultTranslation() {
        WeeklyAyah.Ayah ayah = new WeeklyAyah.Ayah("arabic", "Malay meaning", "English meaning", "", "2:152");
        assertEquals("Malay meaning", ayah.translation(AppLanguage.LANG_MS));
    }
}
