package com.gathin.tvhome;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Live weather forecast and air-quality information for the selected location.
 * Open-Meteo supplies the forecast and modelled US AQI for every supported location.
 */
final class WeatherAirQuality {
    private static final String TAG = "TvHome.Weather";
    private static final String PREFS_NAME = "weather_aqi_cache";
    private static final String KEY_WEATHER_JSON = "weather_json";
    private static final String KEY_AQI_JSON = "aqi_json";
    private static final String KEY_TEMP = "temp";
    private static final String KEY_CODE = "weather_code";
    private static final String KEY_AQI = "aqi";
    private static final String KEY_AQI_STATUS = "aqi_status";
    private static final String KEY_AQI_PARAM = "aqi_param";
    private static final String KEY_AQI_STATION_PLACE = "aqi_place";
    private static final String KEY_AQI_DATETIME = "aqi_datetime";
    private static final String KEY_AQI_IS_JAS = "aqi_is_jas";
    private static final String KEY_LAST_UPDATE = "last_update";
    private static final String KEY_WEATHER_LAST_SUCCESS = "weather_last_success";
    private static final String KEY_AQI_LAST_SUCCESS = "aqi_last_success";
    private static final String KEY_WEATHER_LAT = "weather_cache_lat";
    private static final String KEY_WEATHER_LON = "weather_cache_lon";
    private static final String KEY_AQI_LAT = "aqi_cache_lat";
    private static final String KEY_AQI_LON = "aqi_cache_lon";
    private static final String KEY_REQUEST_GENERATION = "request_generation";
    private static final long REFRESH_INTERVAL_MS = 30L * 60 * 1000; // 30 minutes

    static class HourlyItem {
        final String time;
        final String icon;
        final int temp;
        final int rainChance;

        HourlyItem(String time, String icon, int temp, int rainChance) {
            this.time = time;
            this.icon = icon;
            this.temp = temp;
            this.rainChance = rainChance;
        }
    }

    static class DailyItem {
        final String day;
        final String icon;
        final int maxTemp;
        final int minTemp;
        final int rainChance;

        DailyItem(String day, String icon, int maxTemp, int minTemp, int rainChance) {
            this.day = day;
            this.icon = icon;
            this.maxTemp = maxTemp;
            this.minTemp = minTemp;
            this.rainChance = rainChance;
        }
    }

    static class FullWeather {
        final String location;
        final String stationId;
        final String stationPlace;
        final boolean isJasSource;
        final int temp;
        final int apparentTemp;
        final String condition;
        final String icon;
        final int highTemp;
        final int lowTemp;
        final int humidity;
        final int windSpeed;
        final float windDirection;
        final int windGust;
        final double precipitationMm;
        final double visibilityKm;
        final int pressure;
        final int uvIndex;
        final int rainChanceToday;
        final String sunrise;
        final String sunset;
        final int aqi;
        final String aqiStatus;
        final int aqiColor;
        final String aqiParam;
        final double pm25;
        final double pm10;
        final String updatedTime;
        final List<HourlyItem> hourly;
        final List<DailyItem> daily;

        FullWeather(String location, String stationId, String stationPlace, boolean isJasSource,
                    int temp, int apparentTemp, String condition, String icon,
                    int highTemp, int lowTemp, int humidity, int windSpeed, float windDirection, int windGust,
                    double precipitationMm, double visibilityKm, int pressure, int uvIndex,
                    int rainChanceToday, String sunrise, String sunset, int aqi,
                    String aqiStatus, int aqiColor, String aqiParam, double pm25, double pm10,
                    String updatedTime, List<HourlyItem> hourly, List<DailyItem> daily) {
            this.location = location;
            this.stationId = stationId;
            this.stationPlace = stationPlace;
            this.isJasSource = isJasSource;
            this.temp = temp;
            this.apparentTemp = apparentTemp;
            this.condition = condition;
            this.icon = icon;
            this.highTemp = highTemp;
            this.lowTemp = lowTemp;
            this.humidity = humidity;
            this.windSpeed = windSpeed;
            this.windDirection = windDirection;
            this.windGust = windGust;
            this.precipitationMm = precipitationMm;
            this.visibilityKm = visibilityKm;
            this.pressure = pressure;
            this.uvIndex = uvIndex;
            this.rainChanceToday = rainChanceToday;
            this.sunrise = sunrise;
            this.sunset = sunset;
            this.aqi = aqi;
            this.aqiStatus = aqiStatus;
            this.aqiColor = aqiColor;
            this.aqiParam = aqiParam;
            this.pm25 = pm25;
            this.pm10 = pm10;
            this.updatedTime = updatedTime;
            this.hourly = hourly;
            this.daily = daily;
        }
    }

    private WeatherAirQuality() {
    }

    /** Forces an immediate background sync regardless of cache freshness. */
    static void sync(Context context, Runnable onUpdated) {
        syncInternal(context, onUpdated, true);
    }

    /** Fetches weather and air quality in background if due (30-min cache). */
    static void syncIfDue(Context context, Runnable onUpdated) {
        syncInternal(context, onUpdated, false);
    }

    private static void syncInternal(Context context, Runnable onUpdated, boolean force) {
        final Context appContext = context.getApplicationContext();
        final SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        final double lat = AppLocationManager.getLatitude(appContext);
        final double lon = AppLocationManager.getLongitude(appContext);
        final String stationId = AppLocationManager.getStationId(appContext);
        final String defaultPlace = AppLocationManager.getStationPlace(appContext);
        final String requestLanguage = AppLanguage.getLanguage(appContext);
        final long requestGeneration;
        synchronized (WeatherAirQuality.class) {
            requestGeneration = prefs.getLong(KEY_REQUEST_GENERATION, 0L) + 1L;
            prefs.edit().putLong(KEY_REQUEST_GENERATION, requestGeneration).apply();
        }

        new Thread(() -> {
            try {
                long now = System.currentTimeMillis();
                long weatherLast = prefs.getLong(KEY_WEATHER_LAST_SUCCESS, 0L);
                long aqiLast = prefs.getLong(KEY_AQI_LAST_SUCCESS, 0L);
                boolean weatherFresh = prefs.contains(KEY_WEATHER_JSON) && now - weatherLast < REFRESH_INTERVAL_MS;
                boolean aqiFresh = prefs.contains(KEY_AQI_JSON) && now - aqiLast < REFRESH_INTERVAL_MS;
                if (!force && weatherFresh && aqiFresh) return;

                String weatherUrl = String.format(Locale.ROOT,
                        "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m,wind_direction_10m,wind_gusts_10m,precipitation,visibility,pressure_msl&hourly=temperature_2m,precipitation_probability,weather_code&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max,sunrise,sunset,uv_index_max&timezone=auto&forecast_days=7",
                        lat, lon);
                JSONObject wJson = fetchJson(weatherUrl);

                String aqiUrl = String.format(Locale.ROOT,
                        "https://air-quality-api.open-meteo.com/v1/air-quality?latitude=%.4f&longitude=%.4f&current=pm2_5,pm10,us_aqi&timezone=auto",
                        lat, lon);
                JSONObject aqiJson = fetchJson(aqiUrl);

                // A response may commit only while its generation, coordinates and language remain current.
                synchronized (WeatherAirQuality.class) {
                    long currentGeneration = prefs.getLong(KEY_REQUEST_GENERATION, 0L);
                    boolean sameLocation = Double.compare(lat, AppLocationManager.getLatitude(appContext)) == 0
                            && Double.compare(lon, AppLocationManager.getLongitude(appContext)) == 0;
                    boolean sameLanguage = requestLanguage.equals(AppLanguage.getLanguage(appContext));
                    if (!isCurrentRequest(requestGeneration, currentGeneration, sameLocation, sameLanguage)) {
                        Log.i(TAG, "Discarded superseded weather/AQI response");
                        return;
                    }
                    if (wJson == null && aqiJson == null) {
                        Log.w(TAG, "Weather and AQI refresh failed; keeping previous cache and timestamps");
                        return;
                    }

                    SharedPreferences.Editor edit = prefs.edit();
                    if (wJson != null) {
                        edit.putString(KEY_WEATHER_JSON, wJson.toString())
                                .putLong(KEY_WEATHER_LAST_SUCCESS, now)
                                .putString(KEY_WEATHER_LAT, Double.toString(lat))
                                .putString(KEY_WEATHER_LON, Double.toString(lon));
                    }
                    if (aqiJson != null) {
                        edit.putString(KEY_AQI_JSON, aqiJson.toString())
                                .putLong(KEY_AQI_LAST_SUCCESS, now)
                                .putString(KEY_AQI_LAT, Double.toString(lat))
                                .putString(KEY_AQI_LON, Double.toString(lon));
                    }
                    edit.putString(KEY_AQI_STATION_PLACE, defaultPlace)
                            .putBoolean(KEY_AQI_IS_JAS, false)
                            .putLong(KEY_LAST_UPDATE, now)
                            .apply();
                }

                Log.i(TAG, "Weather/AQI response committed for " + stationId);
                if (onUpdated != null) {
                    new Handler(Looper.getMainLooper()).post(onUpdated);
                }
            } catch (Exception e) {
                Log.w(TAG, "Weather/AQI sync error: " + e.getMessage());
            }
        }, "weather-aqi-sync").start();
    }

    static boolean isCurrentRequest(long requestGeneration, long currentGeneration,
                                    boolean sameLocation, boolean sameLanguage) {
        return requestGeneration == currentGeneration && sameLocation && sameLanguage;
    }

    /** Formats a clean Spannable UI string for the Home screen: "⛅ 29° Cerah  •  IPU 112 Tidak Sihat" */
    static boolean isSameLocation(double cachedLat, double cachedLon,
                                   double selectedLat, double selectedLon) {
        return Double.isFinite(cachedLat) && Double.isFinite(cachedLon)
                && Double.compare(cachedLat, selectedLat) == 0
                && Double.compare(cachedLon, selectedLon) == 0;
    }

    private static boolean cacheMatchesLocation(SharedPreferences prefs, String latKey, String lonKey,
                                                double selectedLat, double selectedLon) {
        try {
            String cachedLat = prefs.getString(latKey, null);
            String cachedLon = prefs.getString(lonKey, null);
            if (cachedLat == null || cachedLon == null) return false;
            return isSameLocation(Double.parseDouble(cachedLat), Double.parseDouble(cachedLon),
                    selectedLat, selectedLon);
        } catch (RuntimeException e) {
            return false;
        }
    }

    static CharSequence getFormatted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isEnglish = AppLanguage.LANG_EN.equals(AppLanguage.getLanguage(context));
        String weatherJson = prefs.getString(KEY_WEATHER_JSON, null);
        String aqiJson = prefs.getString(KEY_AQI_JSON, null);
        double selectedLat = AppLocationManager.getLatitude(context);
        double selectedLon = AppLocationManager.getLongitude(context);
        if (!cacheMatchesLocation(prefs, KEY_WEATHER_LAT, KEY_WEATHER_LON, selectedLat, selectedLon)) weatherJson = null;
        if (!cacheMatchesLocation(prefs, KEY_AQI_LAT, KEY_AQI_LON, selectedLat, selectedLon)) aqiJson = null;
        String weatherText = isEnglish ? "Weather unavailable" : "Cuaca tidak tersedia";
        String aqiText = isEnglish ? "US AQI unavailable" : "US AQI tidak tersedia";
        int aqi = -1;
        try {
            if (weatherJson != null) {
                JSONObject current = new JSONObject(weatherJson).optJSONObject("current");
                if (current != null) {
                    int temp = roundedValue(current, "temperature_2m");
                    int code = intValue(current, "weather_code");
                    if (temp >= -100 && temp <= 100) {
                        weatherText = getWeatherIcon(code) + " " + temp + "° " + getWeatherCondition(code, isEnglish);
                    }
                }
            }
            if (aqiJson != null) {
                JSONObject current = new JSONObject(aqiJson).optJSONObject("current");
                if (current != null) aqi = roundedValue(current, "us_aqi");
            }
        } catch (Exception e) {
            Log.w(TAG, "Unable to format cached weather/AQI: " + e.getMessage());
        }
        if (aqi >= 0) aqiText = "US AQI " + aqi + " " + getAqiStatus(aqi, isEnglish);

        SpannableStringBuilder sb = new SpannableStringBuilder();
        int weatherStart = sb.length();
        sb.append(weatherText);
        sb.setSpan(new ForegroundColorSpan(0xFFFFFFFF), weatherStart, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new StyleSpan(Typeface.BOLD), weatherStart, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int separatorStart = sb.length();
        sb.append("  •  ");
        sb.setSpan(new ForegroundColorSpan(0x80FFFFFF), separatorStart, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int aqiStart = sb.length();
        sb.append(aqiText);
        sb.setSpan(new ForegroundColorSpan(aqi < 0 ? 0xFF9E9E9E : getAqiColor(aqi)), aqiStart, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new StyleSpan(Typeface.BOLD), aqiStart, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    /** Returns full weather model for WeatherActivity dashboard. */
    static FullWeather getFullWeather(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String wStr = prefs.getString(KEY_WEATHER_JSON, null);
        String aStr = prefs.getString(KEY_AQI_JSON, null);
        double selectedLat = AppLocationManager.getLatitude(context);
        double selectedLon = AppLocationManager.getLongitude(context);
        if (!cacheMatchesLocation(prefs, KEY_WEATHER_LAT, KEY_WEATHER_LON, selectedLat, selectedLon)) wStr = null;
        if (!cacheMatchesLocation(prefs, KEY_AQI_LAT, KEY_AQI_LON, selectedLat, selectedLon)) aStr = null;

        int temp = -1, apparentTemp = -1, code = -1, highTemp = -1, lowTemp = -1;
        int humidity = -1, windSpeed = -1, pressure = -1, windGust = -1, uvIndex = -1, rainChanceToday = -1;
        float windDirection = -1f;
        double precipitationMm = Double.NaN, visibilityKm = Double.NaN;
        String sunrise = "—", sunset = "—", updated = "—";
        List<HourlyItem> hourly = new ArrayList<>();
        List<DailyItem> daily = new ArrayList<>();

        if (wStr != null) {
            try {
                JSONObject wJson = new JSONObject(wStr);
                JSONObject curr = wJson.optJSONObject("current");
                if (curr != null) {
                    temp = roundedValue(curr, "temperature_2m");
                    apparentTemp = roundedValue(curr, "apparent_temperature");
                    code = intValue(curr, "weather_code");
                    humidity = roundedValue(curr, "relative_humidity_2m");
                    windSpeed = roundedValue(curr, "wind_speed_10m");
                    pressure = roundedValue(curr, "pressure_msl");
                    windDirection = (float) doubleValue(curr, "wind_direction_10m");
                    windGust = roundedValue(curr, "wind_gusts_10m");
                    precipitationMm = doubleValue(curr, "precipitation");
                    double visibilityMeters = doubleValue(curr, "visibility");
                    visibilityKm = Double.isNaN(visibilityMeters) ? Double.NaN : visibilityMeters / 1000.0;
                    String currentTime = curr.optString("time", "");
                    if (!currentTime.isEmpty()) updated = formatApiTime(currentTime);
                }

                JSONObject d = wJson.optJSONObject("daily");
                if (d != null) {
                    JSONArray maxTemps = d.optJSONArray("temperature_2m_max");
                    JSONArray minTemps = d.optJSONArray("temperature_2m_min");
                    JSONArray rains = d.optJSONArray("precipitation_probability_max");
                    JSONArray uvs = d.optJSONArray("uv_index_max");
                    JSONArray sunrises = d.optJSONArray("sunrise");
                    JSONArray sunsets = d.optJSONArray("sunset");
                    JSONArray days = d.optJSONArray("time");
                    JSONArray dailyCodes = d.optJSONArray("weather_code");
                    highTemp = arrayRounded(maxTemps, 0);
                    lowTemp = arrayRounded(minTemps, 0);
                    rainChanceToday = arrayInt(rains, 0);
                    uvIndex = arrayRounded(uvs, 0);
                    if (sunrises != null && sunrises.length() > 0) sunrise = formatSunTime(sunrises.optString(0, ""));
                    if (sunsets != null && sunsets.length() > 0) sunset = formatSunTime(sunsets.optString(0, ""));

                    if (days != null) {
                        int len = Math.min(7, days.length());
                        for (int i = 0; i < len; i++) {
                            String dayStr = days.optString(i, "");
                            int c = arrayInt(dailyCodes, i);
                            int mx = arrayRounded(maxTemps, i);
                            int mn = arrayRounded(minTemps, i);
                            int rain = arrayInt(rains, i);
                            String label = i == 0 ? context.getString(R.string.today_label)
                                    : formatDayName(dayStr, AppLanguage.getLanguage(context));
                            daily.add(new DailyItem(label, c < 0 ? "" : getWeatherIcon(c), mx, mn, rain));
                        }
                    }
                }

                JSONObject h = wJson.optJSONObject("hourly");
                if (h != null) {
                    JSONArray times = h.optJSONArray("time");
                    JSONArray temps = h.optJSONArray("temperature_2m");
                    JSONArray rains = h.optJSONArray("precipitation_probability");
                    JSONArray codes = h.optJSONArray("weather_code");
                    if (times != null && temps != null) {
                        String currentTime = curr == null ? "" : curr.optString("time", "");
                        int firstIndex = 0;
                        if (!currentTime.isEmpty()) {
                            for (int i = 0; i < times.length(); i++) {
                                if (times.optString(i, "").compareTo(currentTime) >= 0) {
                                    firstIndex = i;
                                    break;
                                }
                            }
                        }
                        for (int i = firstIndex, count = 0; i < times.length() && count < 12; i++) {
                            String tStr = times.optString(i, "");
                            if (!tStr.contains("T")) continue;
                            int hour;
                            try {
                                hour = Integer.parseInt(tStr.substring(tStr.indexOf('T') + 1, tStr.indexOf(':')));
                            } catch (Exception ignored) {
                                continue;
                            }
                            int t = arrayRounded(temps, i);
                            int c = arrayInt(codes, i);
                            int rain = arrayInt(rains, i);
                            hourly.add(new HourlyItem(count == 0 ? context.getString(R.string.now_label) : formatHourLabel(hour),
                                    c < 0 ? "" : getWeatherIcon(c), t, rain));
                            count++;
                        }
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "Failed parsing weather json: " + e.getMessage());
            }
        }

        JSONObject aq = null;
        try { if (aStr != null) aq = new JSONObject(aStr); } catch (Exception e) {
            Log.w(TAG, "Failed parsing AQI json: " + e.getMessage());
        }
        JSONObject aqCurrent = aq == null ? null : aq.optJSONObject("current");
        int aqi = aqCurrent == null ? -1 : roundedValue(aqCurrent, "us_aqi");
        double pm25 = aqCurrent == null ? Double.NaN : doubleValue(aqCurrent, "pm2_5");
        double pm10 = aqCurrent == null ? Double.NaN : doubleValue(aqCurrent, "pm10");
        boolean isJas = false;
        String aqiStatus = aqi < 0 ? "—" : getAqiStatus(aqi, AppLanguage.LANG_EN.equals(AppLanguage.getLanguage(context)));
        String aqiParam = "US AQI (CAMS)";
        String stationPlace = prefs.getString(KEY_AQI_STATION_PLACE, AppLocationManager.getStationPlace(context));
        String stationId = AppLocationManager.getStationId(context);
        String locationName = AppLocationManager.getLocationName(context).toUpperCase(Locale.ROOT);

        boolean isEnglish = AppLanguage.LANG_EN.equals(AppLanguage.getLanguage(context));
        if (!isEnglish && aqi >= 0) aqiStatus = translateAqiStatus(aqiStatus);
        int aqiColor = aqi < 0 ? 0xFF9E9E9E : getAqiColor(aqi);

        return new FullWeather(locationName, stationId, stationPlace, isJas, temp, apparentTemp,
                code < 0 ? (isEnglish ? "Unavailable" : "Tidak tersedia") : getWeatherCondition(code, isEnglish),
                code < 0 ? "" : getWeatherIcon(code), highTemp, lowTemp, humidity, windSpeed,
                windDirection, windGust, precipitationMm, visibilityKm, pressure, uvIndex,
                rainChanceToday, sunrise, sunset, aqi, aqiStatus, aqiColor, aqiParam,
                pm25, pm10, updated, hourly, daily);
    }

    private static double doubleValue(JSONObject object, String key) {
        if (object == null || !object.has(key) || object.isNull(key)) return Double.NaN;
        double value = object.optDouble(key, Double.NaN);
        return Double.isFinite(value) ? value : Double.NaN;
    }

    private static int roundedValue(JSONObject object, String key) {
        double value = doubleValue(object, key);
        return Double.isNaN(value) ? -1 : (int) Math.round(value);
    }

    private static int intValue(JSONObject object, String key) {
        double value = doubleValue(object, key);
        return Double.isNaN(value) ? -1 : (int) value;
    }

    private static int arrayRounded(JSONArray array, int index) {
        if (array == null || index < 0 || index >= array.length() || array.isNull(index)) return -1;
        double value = array.optDouble(index, Double.NaN);
        return Double.isFinite(value) ? (int) Math.round(value) : -1;
    }

    private static int arrayInt(JSONArray array, int index) {
        if (array == null || index < 0 || index >= array.length() || array.isNull(index)) return -1;
        double value = array.optDouble(index, Double.NaN);
        return Double.isFinite(value) ? (int) value : -1;
    }

    private static String formatApiTime(String value) {
        try {
            String time = value.contains("T") ? value.substring(value.indexOf('T') + 1) : value;
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            return String.format(Locale.ROOT, "%d:%02d %s", hour % 12 == 0 ? 12 : hour % 12,
                    minute, hour < 12 ? "AM" : "PM");
        } catch (Exception ignored) {
            return value;
        }
    }

    private static String translateAqiStatus(String status) {
        if (status == null) return "Sedang";
        String s = status.toLowerCase(Locale.ROOT);
        if (s.contains("good") || s.contains("baik")) return "Baik";
        if (s.contains("moderate") || s.contains("sederhana") || s.contains("sedang")) return "Sedang";
        if (s.contains("very unhealthy") || s.contains("sangat")) return "Sangat Tidak Sehat";
        if (s.contains("sensitive") || s.contains("kelompok sensitif")) return "Tidak Sehat bagi Kelompok Sensitif";
        if (s.contains("unhealthy") || s.contains("tidak sihat") || s.contains("tidak sehat")) return "Tidak Sehat";
        if (s.contains("hazardous") || s.contains("bahaya")) return "Berbahaya";
        return status;
    }

    private static String formatSunTime(String isoTime) {
        try {
            if (isoTime.contains("T")) {
                String[] parts = isoTime.substring(isoTime.indexOf('T') + 1).split(":");
                int h = Integer.parseInt(parts[0]);
                int m = Integer.parseInt(parts[1]);
                String ampm = h < 12 ? "AM" : "PM";
                int hh = h % 12 == 0 ? 12 : h % 12;
                return String.format(Locale.ROOT, "%d:%02d %s", hh, m, ampm);
            }
        } catch (Exception ignored) {
        }
        return "—";
    }

    private static String formatDayName(String ymd, String language) {
        try {
            SimpleDateFormat inFmt = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
            Date d = inFmt.parse(ymd);
            if (d != null) {
                SimpleDateFormat outFmt = new SimpleDateFormat("EEE", Locale.forLanguageTag(language));
                return outFmt.format(d);
            }
        } catch (Exception ignored) {
        }
        return ymd;
    }

    private static String formatHourLabel(int hour24) {
        String ampm = hour24 < 12 ? "AM" : "PM";
        int hh = hour24 % 12 == 0 ? 12 : hour24 % 12;
        return hh + " " + ampm;
    }

    private static String getWeatherCondition(int code, boolean isEnglish) {
        switch (code) {
            case 0:
                return isEnglish ? "Clear sky" : "Cerah";
            case 1:
            case 2:
                return isEnglish ? "Partly cloudy" : "Cerah berawan";
            case 3:
                return isEnglish ? "Overcast" : "Mendung";
            case 45:
            case 48:
                return isEnglish ? "Fog / Haze" : "Kabut / Jerebu";
            case 51:
            case 53:
            case 55:
            case 56:
            case 57:
                return isEnglish ? "Drizzle / Freezing drizzle" : "Gerimis / gerimis beku";
            case 61:
            case 63:
            case 65:
            case 66:
            case 67:
                return isEnglish ? "Rain / Freezing rain" : "Hujan / hujan beku";
            case 71:
            case 73:
            case 75:
            case 77:
                return isEnglish ? "Snow / Snow grains" : "Salju";
            case 80:
            case 81:
            case 82:
            case 85:
            case 86:
                return isEnglish ? "Heavy showers / Snow showers" : "Hujan lebat / hujan salju";
            case 95:
            case 96:
            case 99:
                return isEnglish ? "Thunderstorm" : "Badai petir";
            default:
                return isEnglish ? "Unavailable" : "Tidak tersedia";
        }
    }

    private static String getWeatherIcon(int code) {
        switch (code) {
            case 0:
                return "☀️";
            case 1:
            case 2:
                return "⛅";
            case 3:
                return "☁️";
            case 45:
            case 48:
                return "🌫️";
            case 51:
            case 53:
            case 55:
            case 56:
            case 57:
                return "🌦️";
            case 61:
            case 63:
            case 65:
            case 66:
            case 67:
                return "🌧️";
            case 71:
            case 73:
            case 75:
            case 77:
                return "❄️";
            case 80:
            case 81:
            case 82:
            case 85:
            case 86:
                return "⛈️";
            case 95:
            case 96:
            case 99:
                return "⚡";
            default:
                return "";
        }
    }

    private static String getAqiStatus(int aqi, boolean isEnglish) {
        if (aqi <= 50) return isEnglish ? "Good" : "Baik";
        if (aqi <= 100) return isEnglish ? "Moderate" : "Sedang";
        if (aqi <= 150) return isEnglish ? "Unhealthy for Sensitive Groups" : "Tidak Sehat bagi Kelompok Sensitif";
        if (aqi <= 200) return isEnglish ? "Unhealthy" : "Tidak Sehat";
        if (aqi <= 300) return isEnglish ? "Very Unhealthy" : "Sangat Tidak Sehat";
        return isEnglish ? "Hazardous" : "Berbahaya";
    }

    private static int getAqiColor(int aqi) {
        if (aqi <= 50) return 0xFF34C759;
        if (aqi <= 100) return 0xFFFFD60A;
        if (aqi <= 150) return 0xFFFF9F0A;
        if (aqi <= 200) return 0xFFFF453A;
        if (aqi <= 300) return 0xFFAF52DE;
        return 0xFF7A0019;
    }

    private static JSONObject fetchJson(String urlStr) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            conn.setRequestProperty("Accept", "application/json, text/plain, */*");
            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();
                return new JSONObject(sb.toString());
            }
        } catch (Exception e) {
            Log.w(TAG, "fetchJson failed for " + urlStr + ": " + e.getMessage());
        } finally {
            if (conn != null) conn.disconnect();
        }
        return null;
    }
}
