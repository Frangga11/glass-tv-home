package com.gathin.tvhome;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

/**
 * Applies the launcher's persisted language to Android resource lookup.
 * Required on API 26-32, where per-app language settings are not provided by the platform.
 */
final class LocaleContext {
    private LocaleContext() { }

    static Context wrap(Context base) {
        if (base == null) return null;
        String language = AppLanguage.getLanguage(base);
        Locale locale = Locale.forLanguageTag(language);
        Configuration configuration = new Configuration(base.getResources().getConfiguration());
        configuration.setLocale(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLayoutDirection(locale);
        }
        return base.createConfigurationContext(configuration);
    }
}
