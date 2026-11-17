package com.gathin.tvhome;

import java.util.Locale;

/** Locale-neutral matching for the canonical Settings control labels and summaries. */
final class SettingsSearchIndex {
    private SettingsSearchIndex() { }

    static boolean matches(String query, String title, String summary) {
        String normalizedQuery = normalize(query);
        if (normalizedQuery.isEmpty()) return false;
        String searchable = normalize((title == null ? "" : title) + " "
                + (summary == null ? "" : summary));
        for (String term : normalizedQuery.split("\\s+")) {
            if (!term.isEmpty() && !searchable.contains(term)) return false;
        }
        return true;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
    }
}
