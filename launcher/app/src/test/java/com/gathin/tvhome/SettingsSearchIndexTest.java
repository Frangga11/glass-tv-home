package com.gathin.tvhome;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SettingsSearchIndexTest {

    @Test
    public void matchesLocalizedControlTitleCaseInsensitively() {
        assertTrue(SettingsSearchIndex.matches("WALLPAPER", "Kelola Wallpaper", ""));
    }

    @Test
    public void matchesAllQueryTermsAcrossTitleAndSummary() {
        assertTrue(SettingsSearchIndex.matches("foto interval", "Wallpaper", "Atur interval pergantian foto"));
    }

    @Test
    public void rejectsWhenAnyQueryTermIsMissing() {
        assertFalse(SettingsSearchIndex.matches("foto remote", "Wallpaper", "Atur interval pergantian foto"));
    }

    @Test
    public void collapsesWhitespaceAndTrimsQuery() {
        assertTrue(SettingsSearchIndex.matches("  pengaturan   android  ",
                "Pengaturan Android", "Buka pengaturan sistem perangkat"));
    }

    @Test
    public void rejectsEmptyQueryAndNullLabels() {
        assertFalse(SettingsSearchIndex.matches("   ", "Wallpaper", "Foto"));
        assertFalse(SettingsSearchIndex.matches("weather", null, null));
    }

    @Test
    public void matchesEnglishAndIndonesianLabelsWithoutLocaleSensitiveCase() {
        assertTrue(SettingsSearchIndex.matches("widgets information", "Widgets & Information", "Weather and location"));
        assertTrue(SettingsSearchIndex.matches("cuaca lokasi", "Widget & Informasi", "Atur lokasi cuaca"));
    }
}
