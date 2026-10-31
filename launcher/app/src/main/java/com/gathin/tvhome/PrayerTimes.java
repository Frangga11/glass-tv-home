package com.gathin.tvhome;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Indonesian prayer timetable support with a validated Kemenag data mirror and a local
 * astronomical estimate as an offline fallback.
 */
final class PrayerTimes {
    private static final String TAG = "TvHome.Prayer";
    private static final String PREFS_NAME = "prayer_times_cache";
    private static final String KEY_JSON = "takwim_indonesia_annual";
    private static final String KEY_MONTH = "takwim_month";
    private static final String KEY_YEAR = "takwim_year";

    // Default coordinates and timezone for Kota Pagar Alam.
    private static final double LATITUDE = -4.0212;
    private static final double LONGITUDE = 103.2517;
    private static final double TIMEZONE = 7.0;

    static final String[] NAMES = {"Subuh", "Terbit", "Dzuhur", "Ashar", "Maghrib", "Isya"};
    private static final String[] PRAYER_NAMES = NAMES;
    private static final String[] PRAYER_KEYS = {"fajr", "syuruk", "dhuhr", "asr", "maghrib", "isha"};

    static final class Day {
        final int[] minutes;
        final String hijri;

        Day(int[] minutes, String hijri) {
            this.minutes = minutes;
            this.hijri = hijri;
        }
    }

    static Day day(Context ctx, String zone, Calendar date) {
        if (zone == null || zone.isEmpty() || !zone.startsWith("ID_")) zone = getJakimZone(ctx);
        long[] times = getTimesForDate(ctx, date);
        if (times == null || times.length < 6) {
            times = calculateFallback(ctx, date);
        }
        if (times == null || times.length < 6) return null;
        int[] minutes = new int[6];
        for (int i = 0; i < 6; i++) {
            Calendar c = Calendar.getInstance(getPrayerTimeZone(ctx));
            c.setTimeInMillis(times[i]);
            minutes[i] = c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
        }
        return new Day(minutes, getHijriDate());
    }

    static boolean due(Context ctx, String zone) {
        return day(ctx, zone, calendar(ctx)) == null;
    }

    static boolean refresh(Context ctx, String zone) {
        sync(ctx, null);
        return true;
    }

    private PrayerTimes() {
    }

    /** Forces an immediate background sync for the active location/zone. */
    static void sync(Context context, Runnable onUpdated) {
        syncInternal(context, onUpdated, true);
    }

    /** Refresh timetable in background if needed (once a month / day). */
    static void syncIfDue(Context context, Runnable onUpdated) {
        syncInternal(context, onUpdated, false);
    }

    private static void syncInternal(Context context, Runnable onUpdated, boolean force) {
        new Thread(() -> {
            try {
                SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                Calendar now = calendar(context);
                int currentMonth = now.get(Calendar.YEAR) * 100 + (now.get(Calendar.MONTH) + 1);
                String zone = getJakimZone(context);
                String cachedZone = prefs.getString("cached_zone", "");

                if (zone != null && zone.startsWith("ID_")) {
                    if (!"ID_PGA".equals(zone)) {
                        Log.w(TAG, "No verified Kemenag city timetable configured for " + zone + "; using local fallback only");
                        if (onUpdated != null) new Handler(Looper.getMainLooper()).post(onUpdated);
                        return;
                    }
                    int year = now.get(Calendar.YEAR);
                    String cachedJson = prefs.getString(KEY_JSON, null);
                    boolean validCache = false;
                    if (!force && zone.equals(cachedZone) && prefs.getInt(KEY_YEAR, 0) == year
                            && prefs.getInt(KEY_MONTH, 0) == currentMonth && cachedJson != null) {
                        try {
                            validCache = isValidIndonesiaTimetable(new JSONObject(cachedJson), year);
                        } catch (Exception ignored) {
                            // A damaged cache must trigger a fresh download, never block recovery.
                        }
                    }
                    if (validCache) return;

                    String yearUrl = "https://sajadah.org/api/v1/times/id/kota-pagar-alam/" + year + ".json";
                    HttpURLConnection idConn = (HttpURLConnection) new URL(yearUrl).openConnection();
                    idConn.setConnectTimeout(10000);
                    idConn.setReadTimeout(10000);
                    idConn.setRequestMethod("GET");
                    try {
                        if (idConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                            throw new IllegalStateException("Timetable HTTP " + idConn.getResponseCode());
                        StringBuilder idJson = new StringBuilder();
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                                idConn.getInputStream(), java.nio.charset.StandardCharsets.UTF_8))) {
                            String line;
                            while ((line = reader.readLine()) != null) idJson.append(line);
                        }
                        JSONObject timetable = new JSONObject(idJson.toString());
                        if (!isValidIndonesiaTimetable(timetable, year))
                            throw new IllegalStateException("Invalid Kemenag timetable for Kota Pagar Alam");
                        prefs.edit().putString(KEY_JSON, idJson.toString()).putInt(KEY_YEAR, year).putInt(KEY_MONTH, currentMonth)
                                .putString("cached_zone", zone).apply();
                        Log.i(TAG, "Loaded validated Kemenag RI timetable for Kota Pagar Alam, " + year);
                        if (onUpdated != null) new Handler(Looper.getMainLooper()).post(onUpdated);
                    } finally { idConn.disconnect(); }
                    return;
                }
                if (zone != null && zone.startsWith("SGP")) {
                    if (onUpdated != null) new Handler(Looper.getMainLooper()).post(onUpdated);
                    return;
                }

                if (!force && zone.equals(cachedZone) && prefs.getInt(KEY_MONTH, 0) == currentMonth && prefs.contains(KEY_JSON)) {
                    return; // already have this month's takwim
                }

                String apiZone = (zone != null && zone.contains("_")) ? zone.split("_")[0] : zone;
                URL url = new URL("https://api.waktusolat.app/v2/solat/" + apiZone);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) sb.append(line);
                    reader.close();

                    prefs.edit()
                            .putString(KEY_JSON, sb.toString())
                            .putInt(KEY_MONTH, currentMonth)
                            .putString("cached_zone", zone)
                            .apply();

                    if (onUpdated != null) {
                        new Handler(Looper.getMainLooper()).post(onUpdated);
                    }
                    Log.i(TAG, "Synced prayer times for zone " + zone + " successfully");
                }
            } catch (Exception e) {
                Log.w(TAG, "Prayer sync offline/failed: " + e.getMessage());
            }
        }, "prayer-sync").start();
    }

    /** Validate the full annual timetable before trusting or caching the Kemenag mirror. */
    private static boolean isValidIndonesiaTimetable(JSONObject obj, int expectedYear) {
        try {
            JSONObject authority = obj.getJSONObject("authority");
            JSONObject region = obj.getJSONObject("region");
            if (obj.optInt("schema", -1) != 1 || !"ID".equals(obj.optString("country"))
                    || obj.optInt("year", -1) != expectedYear || !obj.optBoolean("official", false)
                    || !"kemenag".equalsIgnoreCase(authority.optString("id"))
                    || !"kota-pagar-alam".equalsIgnoreCase(region.optString("id"))
                    || !"Asia/Jakarta".equals(region.optString("tz"))) return false;
            JSONArray fields = obj.getJSONArray("fields");
            String[] required = {"date", "fajr", "sunrise", "dhuhr", "asr", "maghrib", "isha"};
            int[] indexes = new int[required.length];
            for (int i = 0; i < required.length; i++) {
                indexes[i] = -1;
                for (int j = 0; j < fields.length(); j++)
                    if (required[i].equals(fields.optString(j))) { indexes[i] = j; break; }
                if (indexes[i] < 0) return false;
            }
            JSONArray days = obj.getJSONArray("days");
            Calendar expected = new java.util.GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"), Locale.ROOT);
            expected.clear();
            expected.set(expectedYear, Calendar.JANUARY, 1, 0, 0, 0);
            if (days.length() != expected.getActualMaximum(Calendar.DAY_OF_YEAR)) return false;
            for (int i = 0; i < days.length(); i++) {
                JSONArray row = days.getJSONArray(i);
                if (row.length() < fields.length()) return false;
                String date = String.format(Locale.ROOT, "%04d-%02d-%02d",
                        expected.get(Calendar.YEAR), expected.get(Calendar.MONTH) + 1, expected.get(Calendar.DAY_OF_MONTH));
                if (!date.equals(row.optString(indexes[0]))) return false;
                int previous = -1;
                for (int k = 1; k < indexes.length; k++) {
                    String value = row.optString(indexes[k]);
                    if (!value.matches("(?:[01]\\d|2[0-3]):[0-5]\\d")) return false;
                    int minute = Integer.parseInt(value.substring(0, 2)) * 60 + Integer.parseInt(value.substring(3, 5));
                    if (minute <= previous) return false;
                    previous = minute;
                }
                expected.add(Calendar.DAY_OF_MONTH, 1);
            }
            return true;
        } catch (Exception e) {
            Log.w(TAG, "Kemenag timetable validation failed: " + e.getMessage());
            return false;
        }
    }

    static boolean isUsingOfficialKemenagSchedule(Context context) {
        if (!"ID_PGA".equals(getActiveZone(context))) return false;
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String json = prefs.getString(KEY_JSON, null);
            return json != null && isValidIndonesiaTimetable(new JSONObject(json), calendar(context).get(Calendar.YEAR));
        } catch (Exception e) {
            return false;
        }
    }

    private static TimeZone getPrayerTimeZone(Context context) {
        String zone = getActiveZone(context);
        if (zone != null && zone.startsWith("ID_")) {
            double longitude = AppLocationManager.getLongitude(context);
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            if (prefs.contains("manual_lon")) {
                try { longitude = Double.parseDouble(prefs.getString("manual_lon", String.valueOf(longitude))); }
                catch (NumberFormatException ignored) { }
            }
            if (longitude < 114.0) return TimeZone.getTimeZone("Asia/Jakarta");
            if (longitude < 125.0) return TimeZone.getTimeZone("Asia/Makassar");
            return TimeZone.getTimeZone("Asia/Jayapura");
        }
        return TimeZone.getDefault();
    }

    static Calendar calendar(Context context) {
        return Calendar.getInstance(getPrayerTimeZone(context));
    }

    private static SimpleDateFormat prayerTimeFormat(Context context, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        format.setTimeZone(getPrayerTimeZone(context));
        return format;
    }

    public static class PrayerZone {
        public final String code;
        public final String state;
        public final String name;
        public final double lat;
        public final double lon;

        public PrayerZone(String code, String state, String name, double lat, double lon) {
            this.code = code;
            this.state = state;
            this.name = name;
            this.lat = lat;
            this.lon = lon;
        }

        public String getDisplayName() {
            return name + " (" + state + " • " + code + ")";
        }
    }

    private static final List<PrayerZone> PRESET_ZONES = new ArrayList<>();

    static {
        // W.P. Kuala Lumpur & Putrajaya (Separated)
        PRESET_ZONES.add(new PrayerZone("WLY01", "W.P. Kuala Lumpur", "Kuala Lumpur", 3.1412, 101.6865));
        PRESET_ZONES.add(new PrayerZone("WLY01_PJ", "W.P. Putrajaya", "Putrajaya", 2.9158, 101.6909));
        PRESET_ZONES.add(new PrayerZone("WLY02", "W.P.", "Labuan", 5.3347, 115.2289));

        // Selangor
        PRESET_ZONES.add(new PrayerZone("SGR01", "Selangor", "Shah Alam, Petaling, Gombak, Sepang", 3.0683, 101.5058));
        PRESET_ZONES.add(new PrayerZone("SGR02", "Selangor", "Kuala Selangor, Sabak Bernam", 3.3500, 101.2500));
        PRESET_ZONES.add(new PrayerZone("SGR03", "Selangor", "Klang, Kuala Langat, Banting", 3.0108, 101.4081));

        // Melaka
        PRESET_ZONES.add(new PrayerZone("MLK01", "Melaka", "Bandaraya Melaka, Alor Gajah, Jasin", 2.1909, 102.2571));

        // Negeri Sembilan
        PRESET_ZONES.add(new PrayerZone("NGS02", "Negeri Sembilan", "Seremban, Port Dickson, Nilai", 2.6953, 101.9056));
        PRESET_ZONES.add(new PrayerZone("NGS01", "Negeri Sembilan", "Tampin, Jempol", 2.4700, 102.2300));

        // Johor
        PRESET_ZONES.add(new PrayerZone("JHR02", "Johor", "Johor Bahru, Pasir Gudang, Kulai", 1.4947, 103.7419));
        PRESET_ZONES.add(new PrayerZone("JHR03", "Johor", "Batu Pahat, Muar, Tangkak, Segamat", 1.9191, 102.8658));
        PRESET_ZONES.add(new PrayerZone("JHR04", "Johor", "Kluang, Mersing", 2.0306, 103.3214));

        // Pulau Pinang
        PRESET_ZONES.add(new PrayerZone("PNG01", "Pulau Pinang", "Seluruh Negeri Pulau Pinang", 5.3942, 100.3983));

        // Perak
        PRESET_ZONES.add(new PrayerZone("PRK02", "Perak", "Ipoh, Batu Gajah, Kampar", 4.6397, 101.1083));
        PRESET_ZONES.add(new PrayerZone("PRK03", "Perak", "Taiping, Bagan Serai, Parit Buntar", 4.8569, 100.7417));
        PRESET_ZONES.add(new PrayerZone("PRK04", "Perak", "Lumut, Manjung, Pangkor, Sitiawan", 4.1953, 100.6728));
        PRESET_ZONES.add(new PrayerZone("PRK01", "Perak", "Tapah, Slim River, Tanjung Malim", 3.6800, 101.5200));

        // Kedah & Perlis
        PRESET_ZONES.add(new PrayerZone("KDH01", "Kedah", "Kota Setar, Pokok Sena, Kubang Pasu", 6.1371, 100.3469));
        PRESET_ZONES.add(new PrayerZone("KDH02", "Kedah", "Kuala Muda, Yan, Pendang, Sungai Petani", 5.6297, 100.4677));
        PRESET_ZONES.add(new PrayerZone("KDH07", "Kedah", "Pulau Langkawi", 6.3316, 99.8585));
        PRESET_ZONES.add(new PrayerZone("PLS01", "Perlis", "Kangar, Padang Besar, Arau", 6.4299, 100.2109));

        // Pahang
        PRESET_ZONES.add(new PrayerZone("PHG02", "Pahang", "Kuantan, Pekan, Muadzam Shah", 3.8192, 103.2966));
        PRESET_ZONES.add(new PrayerZone("PHG03", "Pahang", "Jerantut, Temerloh, Maran, Bera", 3.4478, 102.4178));
        PRESET_ZONES.add(new PrayerZone("PHG04", "Pahang", "Bentong, Raub, Lipis", 3.5200, 101.9000));
        PRESET_ZONES.add(new PrayerZone("PHG06", "Pahang", "Cameron Highlands", 4.4700, 101.3800));

        // Terengganu & Kelantan
        PRESET_ZONES.add(new PrayerZone("TRG01", "Terengganu", "Kuala Terengganu, Marang, Kuala Nerus", 5.3117, 103.1325));
        PRESET_ZONES.add(new PrayerZone("TRG04", "Terengganu", "Kemaman, Dungun", 4.2622, 103.4258));
        PRESET_ZONES.add(new PrayerZone("KTN01", "Kelantan", "Kota Bharu, Bachok, Pasir Puteh, Tumpat", 6.1256, 102.2386));
        PRESET_ZONES.add(new PrayerZone("KTN02", "Kelantan", "Machang, Pasir Mas, Tanah Merah", 5.7600, 102.2100));

        // Sabah
        PRESET_ZONES.add(new PrayerZone("SBH07", "Sabah", "Kota Kinabalu, Penampang, Tuaran, Papar", 6.0971, 116.1665));
        PRESET_ZONES.add(new PrayerZone("SBH03", "Sabah", "Tawau, Balung, Merotai, Kalabakan", 4.2564, 117.8931));
        PRESET_ZONES.add(new PrayerZone("SBH05", "Sabah", "Kudat, Kota Marudu, Pitas, Sandakan", 5.8456, 118.0678));

        // Sarawak
        PRESET_ZONES.add(new PrayerZone("SWK08", "Sarawak", "Kuching, Bau, Lundu, Samarahan, Serian", 1.5622, 110.3889));
        PRESET_ZONES.add(new PrayerZone("SWK04", "Sarawak", "Bintulu, Tatau, Sebauh", 3.1772, 113.0412));
        PRESET_ZONES.add(new PrayerZone("SWK02", "Sarawak", "Miri, Niah, Bekenu, Sibuti", 4.4947, 114.0434));
        PRESET_ZONES.add(new PrayerZone("SWK05", "Sarawak", "Sibu, Mukah, Kanowit, Song", 2.2981, 111.8344));
    }

    public static List<PrayerZone> getPresetZones() {
        return Collections.unmodifiableList(PRESET_ZONES);
    }

    public static String getActiveZone(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (prefs.contains("manual_prayer_zone")) {
            return getJakimZone(context);
        }
        return getJakimZone(context);
    }

    public static String getActiveZoneName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (prefs.contains("manual_prayer_zone_name")) {
            return prefs.getString("manual_prayer_zone_name", "Pagar Alam");
        }
        String zone = getActiveZone(context);
        for (PrayerZone pz : PRESET_ZONES) {
            if (pz.code.equalsIgnoreCase(zone)) return pz.name;
        }
        return AppLocationManager.getLocationName(context);
    }

    public static String getShortZoneName(Context context) {
        String name = getActiveZoneName(context);
        if (name == null || name.trim().isEmpty()) return "Pagar Alam";
        int cut = name.indexOf(',');
        if (cut > 0) name = name.substring(0, cut);
        cut = name.indexOf('&');
        if (cut > 0) name = name.substring(0, cut);
        cut = name.indexOf('(');
        if (cut > 0) name = name.substring(0, cut);
        name = name.trim();
        return name.isEmpty() ? "Pagar Alam" : name;
    }

    public static void clearManualZone(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .remove("manual_prayer_zone")
                .remove("manual_prayer_zone_name")
                .remove("manual_lat")
                .remove("manual_lon")
                .apply();
    }

    public static void setZone(Context context, String zoneCode, String name, double lat, double lon) {
        if (zoneCode == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putString("manual_prayer_zone", zoneCode)
                .putString("manual_prayer_zone_name", name != null ? name : zoneCode)
                .putString("manual_lat", String.valueOf(lat))
                .putString("manual_lon", String.valueOf(lon))
                .remove(KEY_JSON)
                .remove("cached_zone")
                .apply();
        sync(context, null);
    }

    public static void setZone(Context context, String zoneCode) {
        if (zoneCode == null) return;
        Location.Zone z = Location.byCode(zoneCode);
        if (z != null) {
            setZone(context, z.code, z.town, z.lat, z.lon);
        } else {
            setZone(context, zoneCode, zoneCode, 0, 0);
        }
    }

    public static void setPrayerZone(Context context, PrayerZone zone) {
        if (zone == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putString("manual_prayer_zone", zone.code)
                .putString("manual_prayer_zone_name", zone.name)
                .putString("manual_lat", String.valueOf(zone.lat))
                .putString("manual_lon", String.valueOf(zone.lon))
                .apply();
        sync(context, null);
    }

    private static String getJakimZone(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String selected = prefs.getString("manual_prayer_zone", "ID_PGA");
        if (selected == null || !selected.startsWith("ID_")) {
            // Migrate any former non-Indonesian default to the user's Pagar Alam location.
            prefs.edit().putString("manual_prayer_zone", "ID_PGA")
                    .putString("manual_prayer_zone_name", "Pagar Alam")
                    .putString("manual_lat", "-4.0212")
                    .putString("manual_lon", "103.2517")
                    .apply();
            return "ID_PGA";
        }
        return selected;
    }

    static class PrayerItem {
        final String name;
        final String timeStr;
        final boolean isNext;
        final boolean isPast;

        PrayerItem(String name, String timeStr, boolean isNext, boolean isPast) {
            this.name = name;
            this.timeStr = timeStr;
            this.isNext = isNext;
            this.isPast = isPast;
        }
    }

    /** Returns all 6 prayers for today with formatted times and status. */
    static PrayerItem[] getPrayerItems(Context context) {
        Calendar now = calendar(context);
        long[] times = getTimesForDate(context, now);
        if (times == null || times.length < 6) {
            times = calculateFallback(context, now);
        }
        long nowMs = now.getTimeInMillis();
        int nextIdx = -1;
        for (int i = 0; i < times.length; i++) {
            if (i == 1) continue;
            if (times[i] > nowMs) {
                nextIdx = i;
                break;
            }
        }
        if (nextIdx == -1) nextIdx = 0;

        SimpleDateFormat timeFmt = prayerTimeFormat(context, "HH:mm");
        String[] prayerNames = AppLanguage.getPrayerNames(context);
        PrayerItem[] items = new PrayerItem[6];
        for (int i = 0; i < 6; i++) {
            items[i] = new PrayerItem(
                    prayerNames[i],
                    timeFmt.format(new Date(times[i])),
                    i == nextIdx,
                    times[i] < nowMs
            );
        }
        return items;
    }

    /** Returns formatted clean UI CharSequence with highlighted next prayer, bold and legible. */
    static CharSequence getFormatted(Context context) {
        Calendar now = calendar(context);
        long[] times = getTimesForDate(context, now);

        if (times == null || times.length < 6) {
            times = calculateFallback(context, now);
        }

        long nowMs = now.getTimeInMillis();
        int nextIdx = -1;

        // Find next prayer (skip Syuruk idx 1)
        for (int i = 0; i < times.length; i++) {
            if (i == 1) continue;
            if (times[i] > nowMs) {
                nextIdx = i;
                break;
            }
        }
        if (nextIdx == -1) nextIdx = 0; // After Isyak -> Subuh next morning

        SimpleDateFormat timeFmt = prayerTimeFormat(context, "HH:mm");
        String[] prayerNames = AppLanguage.getPrayerNames(context);
        String nextWord = AppLanguage.getNextPrayerPrefix(context);

        SpannableStringBuilder sb = new SpannableStringBuilder();

        // LINE 1: LOCATION  •  NEXT / SETERUSNYA: ASAR 16:18
        String nextName = prayerNames[nextIdx].toUpperCase(Locale.ROOT);
        long displayTimeMs = times[nextIdx];
        if (nextIdx == 0 && times[0] < nowMs) {
            Calendar tomorrow = (Calendar) now.clone();
            tomorrow.add(Calendar.DAY_OF_MONTH, 1);
            long[] tomTimes = getTimesForDate(context, tomorrow);
            if (tomTimes == null || tomTimes.length < 6) tomTimes = calculateFallback(context, tomorrow);
            if (tomTimes != null && tomTimes.length > 0) displayTimeMs = tomTimes[0];
        }
        String nextTime = timeFmt.format(new Date(displayTimeMs));

        int l1Start = sb.length();
        String zoneHeader = getShortZoneName(context).toUpperCase(Locale.ROOT);
        if ("ID_PGA".equals(getActiveZone(context))) {
            zoneHeader += isUsingOfficialKemenagSchedule(context) ? " • KEMENAG RI" : " • PERKIRAAN";
        }
        sb.append(zoneHeader).append("  •  ").append(nextWord).append(": ").append(nextName).append(" ").append(nextTime);
        int l1End = sb.length();
        sb.setSpan(new ForegroundColorSpan(0xFFFFFFFF), l1Start, l1End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new StyleSpan(Typeface.BOLD), l1Start, l1End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        sb.append("\n");

        // LINE 2: SUBUH 05:49  •  SYURUK 06:55  •  ZOHOR 13:01  •  ASAR 16:18  •  MAGHRIB 19:00  •  ISYAK 20:10
        int[] displayIndices = {0, 1, 2, 3, 4, 5}; // Subuh, Syuruk, Zohor, Asar, Maghrib, Isyak
        for (int i = 0; i < displayIndices.length; i++) {
            int idx = displayIndices[i];
            boolean isNext = (idx == nextIdx);
            int start = sb.length();

            String name = prayerNames[idx].toUpperCase(Locale.ROOT);
            String timeStr = timeFmt.format(new Date(times[idx]));

            sb.append(name).append(" ").append(timeStr);
            if (isNext) {
                sb.setSpan(new ForegroundColorSpan(0xFF4ADE80), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                sb.setSpan(new ForegroundColorSpan(0xE6FFFFFF), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (i < displayIndices.length - 1) {
                int dotStart = sb.length();
                sb.append("  •  ");
                sb.setSpan(new ForegroundColorSpan(0x80FFFFFF), dotStart, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return sb;
    }

    private static long[] getTimesForDate(Context context, Calendar requestedDate) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String jsonStr = prefs.getString(KEY_JSON, null);
            if (jsonStr == null || requestedDate == null) return null;
            JSONObject obj = new JSONObject(jsonStr);
            String activeZone = getActiveZone(context);
            if ("ID_PGA".equals(activeZone)) {
                TimeZone tz = getPrayerTimeZone(context);
                Calendar target = (Calendar) requestedDate.clone();
                target.setTimeZone(tz);
                int year = target.get(Calendar.YEAR);
                if (!isValidIndonesiaTimetable(obj, year)) return null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
                dateFormat.setTimeZone(tz);
                String dateKey = dateFormat.format(target.getTime());
                JSONArray fields = obj.getJSONArray("fields"), days = obj.getJSONArray("days");
                int dateIndex = -1;
                String[] wanted = {"fajr", "sunrise", "dhuhr", "asr", "maghrib", "isha"};
                int[] indexes = new int[wanted.length];
                for (int j = 0; j < fields.length(); j++) {
                    String field = fields.optString(j);
                    if ("date".equals(field)) dateIndex = j;
                    for (int k = 0; k < wanted.length; k++) if (wanted[k].equals(field)) indexes[k] = j;
                }
                if (dateIndex < 0) return null;
                for (int i = 0; i < days.length(); i++) {
                    JSONArray row = days.getJSONArray(i);
                    if (!dateKey.equals(row.optString(dateIndex))) continue;
                    long[] result = new long[6];
                    for (int k = 0; k < 6; k++) {
                        String value = row.optString(indexes[k]);
                        if (!value.matches("(?:[01]\\d|2[0-3]):[0-5]\\d")) return null;
                        Calendar local = (Calendar) target.clone();
                        local.set(Calendar.HOUR_OF_DAY, Integer.parseInt(value.substring(0, 2)));
                        local.set(Calendar.MINUTE, Integer.parseInt(value.substring(3, 5)));
                        local.set(Calendar.SECOND, 0);
                        local.set(Calendar.MILLISECOND, 0);
                        result[k] = local.getTimeInMillis();
                    }
                    return result;
                }
                return null;
            }
            int day = requestedDate.get(Calendar.DAY_OF_MONTH);
            JSONArray prayers = obj.getJSONArray("prayers");
            for (int i = 0; i < prayers.length(); i++) {
                JSONObject prayer = prayers.getJSONObject(i);
                if (prayer.optInt("day") == day) {
                    long[] result = new long[6];
                    for (int k = 0; k < 6; k++) result[k] = prayer.getLong(PRAYER_KEYS[k]) * 1000L;
                    return result;
                }
            }
        } catch (Exception e) { Log.w(TAG, "Unable to read cached prayer timetable: " + e.getMessage()); }
        return null;
    }

    /** Astronomical calculation fallback based on the selected Indonesian location. */
    private static long[] calculateFallback(Context context, Calendar cal) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        double latitude = prefs.contains("manual_lat")
                ? Double.parseDouble(prefs.getString("manual_lat", String.valueOf(AppLocationManager.getLatitude(context))))
                : AppLocationManager.getLatitude(context);
        double longitude = prefs.contains("manual_lon")
                ? Double.parseDouble(prefs.getString("manual_lon", String.valueOf(AppLocationManager.getLongitude(context))))
                : AppLocationManager.getLongitude(context);

        String zone = getActiveZone(context);
        double tz = 7.0;
        if (zone != null && zone.startsWith("ID_")) {
            // Indonesia timezones: WIB (UTC+7), WITA (UTC+8), WIT (UTC+9)
            if (longitude < 114.0) {
                tz = 7.0; // WIB
            } else if (longitude < 125.0) {
                tz = 8.0; // WITA
            } else {
                tz = 9.0; // WIT
            }
        } else {
            tz = 7.0; // Indonesia fallback (WIB)
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        double d = 367.0 * year - Math.floor((7.0 * (year + Math.floor((month + 9.0) / 12.0))) / 4.0)
                + Math.floor((275.0 * month) / 9.0) + day - 730531.5;

        double L = (280.461 + 0.9856474 * d) % 360.0;
        if (L < 0) L += 360.0;
        double M = (357.528 + 0.9856003 * d) % 360.0;
        if (M < 0) M += 360.0;

        double lambda = L + 1.915 * Math.sin(Math.toRadians(M)) + 0.020 * Math.sin(Math.toRadians(2 * M));
        double epsilon = 23.439 - 0.0000004 * d;
        double alpha = Math.toDegrees(Math.atan2(Math.cos(Math.toRadians(epsilon)) * Math.sin(Math.toRadians(lambda)), Math.cos(Math.toRadians(lambda)))) / 15.0;
        double delta = Math.toDegrees(Math.asin(Math.sin(Math.toRadians(epsilon)) * Math.sin(Math.toRadians(lambda))));

        double noon = 12.0 + tz - (longitude / 15.0) - (L / 15.0 - alpha);
        if (noon < 0) noon += 24.0;
        if (noon >= 24) noon -= 24.0;

        double fajrHour = noon - sunAngleHour(-20.0, latitude, delta);
        double sunriseHour = noon - sunAngleHour(-0.833, latitude, delta);
        double asrHour = noon + asrHour(1, latitude, delta);
        double sunsetHour = noon + sunAngleHour(-0.833, latitude, delta);
        double ishaHour = noon + sunAngleHour(-18.0, latitude, delta);

        // Conservative minute rounding and small ihtiyat for the offline estimate.
        double[] hours = {fajrHour + 0.05, sunriseHour, noon + 0.04, asrHour + 0.04, sunsetHour + 0.05, ishaHour + 0.05};
        long[] res = new long[6];
        Calendar base = (Calendar) cal.clone();
        base.set(Calendar.SECOND, 0);
        base.set(Calendar.MILLISECOND, 0);

        for (int i = 0; i < 6; i++) {
            int h = (int) hours[i];
            int m = (int) ((hours[i] - h) * 60.0);
            base.set(Calendar.HOUR_OF_DAY, h);
            base.set(Calendar.MINUTE, m);
            res[i] = base.getTimeInMillis();
        }
        return res;
    }

    private static double sunAngleHour(double angle, double lat, double delta) {
        double cosH = (Math.sin(Math.toRadians(angle)) - Math.sin(Math.toRadians(lat)) * Math.sin(Math.toRadians(delta)))
                / (Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(delta)));
        cosH = Math.max(-1.0, Math.min(1.0, cosH));
        return Math.toDegrees(Math.acos(cosH)) / 15.0;
    }

    private static double asrHour(int shadowFactor, double lat, double delta) {
        double phi = Math.abs(lat - delta);
        double cotA = shadowFactor + Math.tan(Math.toRadians(phi));
        double angle = Math.toDegrees(Math.atan(1.0 / cotA));
        return sunAngleHour(angle, lat, delta);
    }

    static class ScheduleItem {
        final String name;
        final String timeFormatted;
        final boolean isNext;
        final boolean isPast;

        ScheduleItem(String name, String timeFormatted, boolean isNext, boolean isPast) {
            this.name = name;
            this.timeFormatted = timeFormatted;
            this.isNext = isNext;
            this.isPast = isPast;
        }
    }

    /** Returns full day prayer list for popup display. */
    static java.util.List<ScheduleItem> getFullSchedule(Context context) {
        java.util.List<ScheduleItem> list = new java.util.ArrayList<>();
        Calendar now = calendar(context);
        long[] times = getTimesForDate(context, now);
        if (times == null || times.length < 6) times = calculateFallback(context, now);

        long nowMs = now.getTimeInMillis();
        int nextIdx = -1;
        for (int i = 0; i < times.length; i++) {
            if (i == 1) continue; // Skip Syuruk (sunrise)
            if (times[i] > nowMs) {
                nextIdx = i;
                break;
            }
        }
        if (nextIdx == -1) nextIdx = 0;

        SimpleDateFormat sdf = prayerTimeFormat(context, "h:mm a");
        String[] prayerNames = AppLanguage.getPrayerNames(context);
        for (int i = 0; i < times.length; i++) {
            boolean isNext = (i == nextIdx);
            boolean isPast = (times[i] < nowMs);
            list.add(new ScheduleItem(prayerNames[i], sdf.format(new Date(times[i])), isNext, isPast));
        }
        return list;
    }

    /** Returns short summary of next prayer (e.g. for screensaver). */
    static String getNextPrayerSummary(Context context) {
        Calendar now = calendar(context);
        long[] times = getTimesForDate(context, now);
        if (times == null || times.length < 6) times = calculateFallback(context, now);

        long nowMs = now.getTimeInMillis();
        int nextIdx = -1;
        for (int i = 0; i < times.length; i++) {
            if (i == 1) continue; // Skip Syuruk (sunrise)
            if (times[i] > nowMs) {
                nextIdx = i;
                break;
            }
        }

        String[] prayerNames = AppLanguage.getPrayerNames(context);
        long nextTimeMs;
        String prayerName;
        if (nextIdx != -1) {
            prayerName = prayerNames[nextIdx];
            nextTimeMs = times[nextIdx];
        } else {
            // All prayers today have passed (after Isyak) -> Next is tomorrow's Subuh
            prayerName = prayerNames[0];
            Calendar tomorrow = (Calendar) now.clone();
            tomorrow.add(Calendar.DAY_OF_MONTH, 1);
            long[] tomTimes = getTimesForDate(context, tomorrow);
            if (tomTimes == null || tomTimes.length < 6) {
                tomTimes = calculateFallback(context, tomorrow);
            }
            nextTimeMs = (tomTimes != null && tomTimes.length > 0) ? tomTimes[0] : (times[0] + 86400000L);
        }

        SimpleDateFormat sdf = prayerTimeFormat(context, "h:mm a");
        String zone = getShortZoneName(context);
        return prayerName + " • " + sdf.format(new Date(nextTimeMs)) + " (" + zone + ")";
    }

    /** Returns Islamic Hijrah date localized according to active language (e.g. "11 Jamadilawal 1448H"). */
    static String getHijriDate(Context context) {
        try {
            java.time.chrono.HijrahDate h = java.time.chrono.HijrahDate.now(
                    java.time.Clock.system(getPrayerTimeZone(context).toZoneId()));
            int day = h.get(java.time.temporal.ChronoField.DAY_OF_MONTH);
            int month = h.get(java.time.temporal.ChronoField.MONTH_OF_YEAR);
            int year = h.get(java.time.temporal.ChronoField.YEAR);
            String lang = context != null ? AppLanguage.getLanguage(context) : AppLanguage.LANG_MS;
            String[] months;
            switch (lang) {
                case AppLanguage.LANG_EN:
                    months = new String[]{
                        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
                        "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
                        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
                    };
                    break;
                case AppLanguage.LANG_ID:
                    months = new String[]{
                        "Muharram", "Safar", "Rabiul Awal", "Rabiul Akhir",
                        "Jumadil Awal", "Jumadil Akhir", "Rajab", "Sya'ban",
                        "Ramadhan", "Syawal", "Zulkaidah", "Zulhijjah"
                    };
                    break;
                case AppLanguage.LANG_MS:
                default:
                    months = new String[]{
                        "Muharram", "Safar", "Rabiulawal", "Rabiulakhir",
                        "Jamadilawal", "Jamadilakhir", "Rejab", "Syaaban",
                        "Ramadan", "Syawal", "Zulkaedah", "Zulhijjah"
                    };
                    break;
            }
            String monthName = (month >= 1 && month <= 12) ? months[month - 1] : "Hijrah";
            return day + " " + monthName + " " + year + "H";
        } catch (Exception e) {
            return "";
        }
    }

    static String getHijriDate() {
        return getHijriDate(null);
    }
}
