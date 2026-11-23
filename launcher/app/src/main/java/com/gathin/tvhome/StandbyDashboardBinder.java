package com.gathin.tvhome;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

/**
 * Shared data binder for the Home preview and Android DreamService standby surfaces.
 * Reads only from the existing PrayerTimes and WeatherAirQuality owners.
 */
final class StandbyDashboardBinder {
    private StandbyDashboardBinder() {}

    static void updateMetrics(Context context, View root, boolean windDown) {
        if (context == null || root == null) return;
        int primary = windDown ? 0xFFFF453A : 0xFF172334;
        int secondary = windDown ? 0xCCFF453A : 0xFF475569;
        updateWeather(context, root, primary, secondary, windDown);
        updatePrayerSchedule(context, root, primary, secondary);
    }

    private static void updateWeather(Context context, View root, int primary,
                                      int secondary, boolean windDown) {
        TextView icon = root.findViewById(R.id.nightstandWeatherIcon);
        TextView temp = root.findViewById(R.id.nightstandWeatherTemp);
        TextView range = root.findViewById(R.id.nightstandWeatherRange);
        TextView aqi = root.findViewById(R.id.nightstandAqiValue);
        TextView status = root.findViewById(R.id.nightstandAqiStatus);
        if (icon == null || temp == null || range == null || aqi == null || status == null) return;

        WeatherAirQuality.FullWeather weather = WeatherAirQuality.getFullWeather(context);
        if (weather == null) {
            icon.setText("—");
            temp.setText("—");
            range.setText("");
            aqi.setText("AQI —");
            status.setText("—");
            status.setTag(0xFF64748B);
        } else {
            icon.setText(weather.weatherAvailable && weather.icon != null && !weather.icon.isEmpty()
                    ? weather.icon : "—");
            temp.setText(weather.weatherAvailable ? weather.temp + "°" : "—");
            range.setText(weather.weatherAvailable && weather.highTemp != -1 && weather.lowTemp != -1
                    ? "H:" + weather.highTemp + "°  L:" + weather.lowTemp + "°" : "");
            aqi.setText(weather.aqiAvailable ? "AQI " + weather.aqi : "AQI —");
            status.setText(weather.aqiAvailable ? weather.aqiStatus : "—");
            status.setTag(weather.aqiAvailable ? weather.aqiColor : 0xFF64748B);
        }

        icon.setTextColor(primary);
        temp.setTextColor(primary);
        range.setTextColor(secondary);
        aqi.setTextColor(primary);
        Object aqiColor = status.getTag();
        status.setTextColor(windDown || !(aqiColor instanceof Integer)
                ? primary : (Integer) aqiColor);
    }

    private static void updatePrayerSchedule(Context context, View root, int primary, int secondary) {
        TextView[] labels = {
                root.findViewById(R.id.nightstandFajrLabel),
                root.findViewById(R.id.nightstandDhuhrLabel),
                root.findViewById(R.id.nightstandAsrLabel),
                root.findViewById(R.id.nightstandMaghribLabel),
                root.findViewById(R.id.nightstandIshaLabel)
        };
        TextView[] times = {
                root.findViewById(R.id.nightstandFajrTime),
                root.findViewById(R.id.nightstandDhuhrTime),
                root.findViewById(R.id.nightstandAsrTime),
                root.findViewById(R.id.nightstandMaghribTime),
                root.findViewById(R.id.nightstandIshaTime)
        };
        PrayerTimes.Day day = PrayerTimes.day(context, null, PrayerTimes.calendar(context));
        int[] indices = {0, 2, 3, 4, 5};
        boolean english = AppLanguage.LANG_EN.equals(AppLanguage.getLanguage(context));
        String[] names = english
                ? new String[] {"Fajr", "Dhuhr", "Asr", "Maghrib", "Isha"}
                : new String[] {"Subuh", "Dzuhur", "Ashar", "Maghrib", "Isya"};
        for (int i = 0; i < indices.length; i++) {
            if (labels[i] != null) {
                labels[i].setText(names[i]);
                labels[i].setTextColor(secondary);
            }
            if (times[i] != null) {
                times[i].setText(day == null ? "—" : formatPrayerTime(day.minutes[indices[i]]));
                times[i].setTextColor(primary);
            }
        }
        TextView title = root.findViewById(R.id.nightstandPrayerScheduleTitle);
        if (title != null) title.setTextColor(primary);
    }

    private static String formatPrayerTime(int minuteOfDay) {
        if (minuteOfDay < 0 || minuteOfDay >= 1440) return "—";
        return String.format(Locale.ROOT, "%02d:%02d", minuteOfDay / 60, minuteOfDay % 60);
    }
}
