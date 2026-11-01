package com.gathin.tvhome;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Weather and modelled US AQI for the selected Indonesian location.
 * Weather and air quality are both supplied by Open-Meteo; AQI is a model estimate, not an
 * official Indonesian ISPU station observation. The last line is cached for an instant Home render.
 */
final class HomeWeather {
    private static final String TAG = "TvHome.Weather";
    static final long REFRESH_MS = 15 * 60_000L;
    /** US EPA AQI breakpoints, shown as a model estimate rather than an official ISPU reading. */
    private static final int[] AQI_TOP = {50, 100, 150, 200, 300, 500};
    static final int[] AQI_COLOR = {0xFF34C759, 0xFFFFD60A, 0xFFFF9F0A, 0xFFFF453A, 0xFFAF52DE, 0xFF7A0019};
    private static final String[] AQI_NAME = {"Baik", "Sedang", "Tidak sehat bagi kelompok sensitif", "Tidak sehat", "Sangat tidak sehat", "Berbahaya"};

    /** What the line says: the weather part, the AQI part and its colour; either part may be empty. */
    static final class Line {
        final String weather, aqi;
        final int aqiColor, icon;

        Line(String weather, String aqi, int aqiColor, int icon) {
            this.weather = weather;
            this.aqi = aqi;
            this.aqiColor = aqiColor;
            this.icon = icon;
        }
    }

    private HomeWeather() {}

    /** Forgets the line, so a new location is fetched at once instead of the old place's weather showing. */
    static void forget(Context ctx) {
        synchronized (HomeWeather.class) {
            SharedPreferences preferences = prefs(ctx);
            long nextGeneration = preferences.getLong("request_generation", 0L) + 1L;
            preferences.edit().clear().putLong("request_generation", nextGeneration).apply();
        }
    }

    static boolean isCurrentRequest(long requestGeneration, long currentGeneration) {
        return requestGeneration == currentGeneration;
    }

    private static boolean isCurrentLocation(Context ctx, double lat, double lon) {
        Location.Zone zone = Location.chosen(ctx);
        double currentLat = zone != null ? zone.lat : -4.0212;
        double currentLon = zone != null ? zone.lon : 103.2517;
        return Double.compare(lat, currentLat) == 0 && Double.compare(lon, currentLon) == 0;
    }

    static boolean due(Context ctx) {
        return Math.abs(System.currentTimeMillis() - prefs(ctx).getLong("at", 0)) >= REFRESH_MS;
    }

    /** The last line fetched, or null before the first. Reads storage only. */
    static Line last(Context ctx) {
        SharedPreferences p = prefs(ctx);
        String w = p.getString("weather", null), aqi = p.getString("api", "");
        String selectedLanguage = AppLanguage.getLanguage(ctx);
        String cachedLanguage = p.getString("language", "");
        if (!selectedLanguage.equals(cachedLanguage)) {
            // Cached weather/AQI labels are localized. Never show stale Indonesian text in English mode (or vice versa).
            p.edit().putLong("at", 0).putString("weather", "").putString("api", "")
                    .putString("language", selectedLanguage).apply();
            return null;
        }
        // Drop cached Malaysian APIMS labels from older builds; this launcher uses Open-Meteo US AQI.
        if (!aqi.isEmpty() && !aqi.startsWith("AQI ")) {
            p.edit().putString("api", "").putInt("api_color", 0).putLong("at", 0).apply();
            aqi = "";
        }
        return w == null && aqi.isEmpty() ? null : new Line(w == null ? "" : w, aqi, p.getInt("api_color", 0),
                p.getInt("icon_code", -1) < 0 ? 0 : icon(p.getInt("icon_code", 3), p.getBoolean("icon_day", true)));
    }

    /** Fetches both parts. Network; never on the UI thread. Returns null when neither could be read. */
    static Line refresh(Context ctx, double lat, double lon) {
        final long requestGeneration;
        final String requestLanguage = AppLanguage.getLanguage(ctx);
        synchronized (HomeWeather.class) {
            SharedPreferences preferences = prefs(ctx);
            requestGeneration = preferences.getLong("request_generation", 0L) + 1L;
            preferences.edit().putLong("request_generation", requestGeneration).apply();
        }
        String weather = "", aqi = "";
        int color = 0, code = -1;
        boolean day = true;
        try {
            JSONObject now = new JSONObject(get("https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon
                    + "&current=temperature_2m,weather_code,is_day&timezone=Asia%2FJakarta")).getJSONObject("current");
            code = now.optInt("weather_code");
            day = now.optInt("is_day", 1) == 1;
            boolean isEnglish = AppLanguage.LANG_EN.equals(AppLanguage.getLanguage(ctx));
            weather = Math.round(now.optDouble("temperature_2m")) + "°  " + describe(code, isEnglish);
        } catch (Exception e) {
            Log.w(TAG, "weather failed: " + e.getMessage());
        }
        try {
            String airUrl = "https://air-quality-api.open-meteo.com/v1/air-quality?latitude=" + lat
                    + "&longitude=" + lon + "&current=us_aqi&timezone=Asia%2FJakarta";
            JSONObject current = new JSONObject(get(airUrl)).getJSONObject("current");
            int value = current.optInt("us_aqi", -1);
            if (value >= 0) {
                int band = 0;
                while (band < AQI_TOP.length - 1 && value > AQI_TOP[band]) band++;
                aqi = "AQI " + value + " · " + aqiName(band, AppLanguage.LANG_EN.equals(AppLanguage.getLanguage(ctx)));
                color = AQI_COLOR[band];
            }
        } catch (Exception e) {
            Log.w(TAG, "Open-Meteo air quality failed: " + e.getMessage());
        }
        // Discard a response if a newer refresh or a location/language change superseded it.
        synchronized (HomeWeather.class) {
            SharedPreferences preferences = prefs(ctx);
            long currentGeneration = preferences.getLong("request_generation", 0L);
            if (!isCurrentRequest(requestGeneration, currentGeneration)
                    || !requestLanguage.equals(AppLanguage.getLanguage(ctx))
                    || !isCurrentLocation(ctx, lat, lon)) {
                return null;
            }
            // A failed try waits for the next round too, rather than asking again every minute.
            SharedPreferences.Editor ed = preferences.edit().putLong("at", System.currentTimeMillis())
                    .putString("language", requestLanguage);
            if (weather.isEmpty() && aqi.isEmpty()) {
                ed.apply();
                return null;
            }
            ed.putString("weather", weather).putString("api", aqi).putInt("api_color", color)
                    .putInt("icon_code", code).putBoolean("icon_day", day).apply();
        }
        return new Line(weather, aqi, color, code < 0 ? 0 : icon(code, day));
    }

    static String describe(int code, boolean isEnglish) {
        if (code == 0) return isEnglish ? "Clear sky" : "Cerah";
        if (code == 1) return isEnglish ? "Mainly clear" : "Umumnya cerah";
        if (code == 2) return isEnglish ? "Partly cloudy" : "Cerah berawan";
        if (code == 3) return isEnglish ? "Overcast" : "Berawan";
        if (code == 45 || code == 48) return isEnglish ? "Fog" : "Berkabut";
        if (code >= 51 && code <= 57) return isEnglish ? "Drizzle" : "Gerimis";
        if (code >= 61 && code <= 67) return isEnglish ? (code >= 65 ? "Heavy rain" : "Rain") : (code >= 65 ? "Hujan lebat" : "Hujan");
        if (code >= 80 && code <= 82) return isEnglish ? "Rain showers" : "Hujan lokal";
        if (code >= 95) return isEnglish ? "Thunderstorm" : "Badai petir";
        return "";
    }

    static String aqiName(int band, boolean isEnglish) {
        if (isEnglish) {
            String[] names = {"Good", "Moderate", "Unhealthy for sensitive groups", "Unhealthy", "Very unhealthy", "Hazardous"};
            return names[Math.max(0, Math.min(names.length - 1, band))];
        }
        return AQI_NAME[Math.max(0, Math.min(AQI_NAME.length - 1, band))];
    }

    /** The sky as a small vector symbol (not an emoji: the colour emoji font alone costs Home megabytes). */
    static int icon(int code, boolean day) {
        if (code == 0) return day ? R.drawable.wx_sun : R.drawable.wx_moon;
        if (code == 1 || code == 2) return day ? R.drawable.wx_partly : R.drawable.wx_partly_night;
        if (code == 3) return R.drawable.wx_cloud;
        if (code == 45 || code == 48) return R.drawable.wx_fog;
        if (code >= 51 && code <= 57) return R.drawable.wx_drizzle;
        if (code >= 61 && code <= 67) return R.drawable.wx_rain;
        if (code >= 80 && code <= 82) return R.drawable.wx_rain;
        if (code >= 95) return R.drawable.wx_storm;
        return R.drawable.wx_cloud;
    }

    private static String get(String url) throws Exception {
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setConnectTimeout(15000);
        c.setReadTimeout(20000);
        c.setRequestProperty("User-Agent", "GlassTV/1.0 (Android TV home screen)");
        try {
            if (c.getResponseCode() != 200) throw new IllegalStateException("HTTP " + c.getResponseCode());
            try (InputStream in = c.getInputStream(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buf = new byte[16 * 1024];
                int r;
                while ((r = in.read(buf)) > 0) {
                    out.write(buf, 0, r);
                    if (out.size() > 512 * 1024) throw new IllegalStateException("response too large");
                }
                return new String(out.toByteArray(), StandardCharsets.UTF_8);
            }
        } finally {
            c.disconnect();
        }
    }

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences("home_weather", Context.MODE_PRIVATE);
    }
}
