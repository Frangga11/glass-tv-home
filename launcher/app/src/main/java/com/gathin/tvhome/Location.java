package com.gathin.tvhome;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Where the TV is: an Indonesian city or region selected on the TV. The selected location controls
 * prayer times and supplies coordinates for the weather line and Weather app.
 */
final class Location {
    static final class Zone {
        final String code, state, town, districts;
        final double lat, lon;

        Zone(String[] r) {
            code = r[0];
            state = r[1];
            town = r[2];
            districts = r[3];
            lat = Double.parseDouble(r[4]);
            lon = Double.parseDouble(r[5]);
        }
    }

    /** Code, state, main town, districts, latitude, longitude. */
    private static final String[][] ZONES = {


        {"ID_JKT", "Indonesia: DKI Jakarta", "Jakarta", "Jakarta Pusat, Selatan, Barat, Timur, Utara", "-6.2088", "106.8456"},
        {"ID_BDG", "Indonesia: Jawa Barat", "Bandung", "Kota Bandung, Cimahi, Kab. Bandung", "-6.9175", "107.6191"},
        {"ID_BKR", "Indonesia: Jawa Barat", "Bekasi & Bogor", "Kota Bekasi, Kab. Bekasi, Kota Bogor, Depok", "-6.2383", "106.9756"},
        {"ID_SBY", "Indonesia: Jawa Timur", "Surabaya", "Kota Surabaya, Sidoarjo, Gresik", "-7.2575", "112.7521"},
        {"ID_MLG", "Indonesia: Jawa Timur", "Malang", "Kota Malang, Kota Batu, Kab. Malang", "-7.9797", "112.6304"},
        {"ID_SMG", "Indonesia: Jawa Tengah", "Semarang", "Kota Semarang, Salatiga, Kendal", "-6.9667", "110.4167"},
        {"ID_SOC", "Indonesia: Jawa Tengah", "Surakarta (Solo)", "Kota Surakarta, Sukoharjo, Klaten, Boyolali", "-7.5755", "110.8243"},
        {"ID_JOG", "Indonesia: D.I. Yogyakarta", "Yogyakarta", "Kota Yogyakarta, Sleman, Bantul, Gunungkidul", "-7.7956", "110.3695"},
        {"ID_MDN", "Indonesia: Sumatera Utara", "Medan", "Kota Medan, Binjai, Deli Serdang", "3.5952", "98.6722"},
        {"ID_BTJ", "Indonesia: Aceh", "Banda Aceh", "Kota Banda Aceh, Aceh Besar, Sabang", "5.5483", "95.3238"},
        {"ID_PDG", "Indonesia: Sumatera Barat", "Padang", "Kota Padang, Pariaman, Bukittinggi", "-0.9471", "100.4172"},
        {"ID_PKU", "Indonesia: Riau", "Pekanbaru", "Kota Pekanbaru, Kampar, Siak, Dumai", "0.5071", "101.4478"},
        {"ID_BTH", "Indonesia: Kepulauan Riau", "Batam", "Kota Batam, Tanjung Pinang, Bintan, Karimun", "1.1301", "104.0529"},
        {"ID_PLM", "Indonesia: Sumatera Selatan", "Palembang", "Kota Palembang, Banyuasin, Ogan Ilir", "-2.9761", "104.7754"},
        {"ID_PGA", "Indonesia: Sumatera Selatan", "Pagar Alam", "Kota Pagar Alam, Dempo Tengah, Dempo Utara, Dempo Selatan, Pagar Alam Utara, Pagar Alam Selatan", "-4.0212", "103.2517"},
        {"ID_BDJ", "Indonesia: Kalimantan Selatan", "Banjarmasin", "Kota Banjarmasin, Banjarbaru, Kab. Banjar", "-3.3194", "114.5908"},
        {"ID_BPN", "Indonesia: Kalimantan Timur", "Balikpapan & IKN", "Kota Balikpapan, Penajam Paser Utara (IKN)", "-1.2379", "116.8529"},
        {"ID_SMD", "Indonesia: Kalimantan Timur", "Samarinda", "Kota Samarinda, Kutai Kartanegara", "-0.5022", "117.1536"},
        {"ID_PNK", "Indonesia: Kalimantan Barat", "Pontianak", "Kota Pontianak, Kubu Raya", "-0.0263", "109.3425"},
        {"ID_DPS", "Indonesia: Bali", "Denpasar", "Kota Denpasar, Badung, Gianyar, Tabanan", "-8.6705", "115.2126"},
        {"ID_MTR", "Indonesia: Nusa Tenggara Barat", "Mataram", "Kota Mataram, Lombok Barat, Lombok Tengah", "-8.5833", "116.1167"},
        {"ID_UPG", "Indonesia: Sulawesi Selatan", "Makassar", "Kota Makassar, Gowa, Maros", "-5.1477", "119.4327"},
        {"ID_MDC", "Indonesia: Sulawesi Utara", "Manado", "Kota Manado, Minahasa, Bitung", "1.4748", "124.8421"},
    };

    private static List<Zone> all;

    private Location() {
    }

    static synchronized List<Zone> all() {
        if (all == null) {
            all = new ArrayList<>();
            for (String[] r : ZONES) all.add(new Zone(r));
        }
        return all;
    }

    /** The states, in the list's order. */
    static List<String> states() {
        List<String> s = new ArrayList<>();
        for (Zone z : all()) if (z.code.startsWith("ID_") && !s.contains(z.state)) s.add(z.state);
        return s;
    }

    static List<Zone> inState(String state) {
        List<Zone> s = new ArrayList<>();
        for (Zone z : all()) if (z.code.startsWith("ID_") && z.state.equals(state)) s.add(z);
        return s;
    }

    static Zone byCode(String code) {
        if (code == null) return null;
        for (Zone z : all()) if (z.code.equalsIgnoreCase(code)) return z;
        return null;
    }

    /** The zone chosen on the TV, or null when none has been chosen there. */
    static Zone chosen(Context ctx) {
        Zone selected = byCode(prefs(ctx).getString("zone", null));
        // This build is localized for Indonesia; migrate an unset or former non-Indonesian zone.
        if (selected == null || !selected.code.startsWith("ID_")) return byCode("ID_PGA");
        return selected;
    }

    static void choose(Context ctx, Zone z) {
        prefs(ctx).edit().putString("zone", z.code).apply();
    }

    /** Whether the Location screen has opened by itself once already (first start with no zone). */
    static boolean asked(Context ctx) {
        return prefs(ctx).getBoolean("asked", false);
    }

    static void setAsked(Context ctx) {
        prefs(ctx).edit().putBoolean("asked", true).apply();
    }

    /** What Home uses now, kept for the Weather app to read (LocationProvider). */
    static void publish(Context ctx, String code, String town, String state, double lat, double lon) {
        prefs(ctx).edit().putString("now_code", code).putString("now_town", town).putString("now_state", state)
                .putString("now_lat", String.valueOf(lat)).putString("now_lon", String.valueOf(lon)).apply();
    }

    static SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences("location", Context.MODE_PRIVATE);
    }
}
