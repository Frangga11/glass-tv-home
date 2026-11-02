package com.gathin.tvhome;

import java.io.File;

/**
 * Metadata for bundled wallpapers that do not carry their own descriptive text.
 * Custom and downloaded wallpaper metadata remains owned by their existing providers.
 */
final class WallpaperMetadata {
    private static final String DEFAULT_DEMPO_FILE = "kabut-pagi-dempo-default.jpg";

    private WallpaperMetadata() { }

    /**
     * Returns [title, description, attribution/country] for a known bundled wallpaper,
     * or null when the file is not managed by this metadata registry.
     */
    static String[] describe(File picture) {
        if (picture == null || !DEFAULT_DEMPO_FILE.equals(picture.getName())) return null;
        return new String[] {
                "Kabut Pagi Dempo",
                "Udara sejuk turun perlahan, menyelimuti lereng dan kebun teh Pagar Alam.",
                "Pagar Alam, Sumatera Selatan"
        };
    }

    static boolean isManagedDefault(File picture) {
        return picture != null && DEFAULT_DEMPO_FILE.equals(picture.getName());
    }
}
