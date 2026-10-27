package com.gathin.tvhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends Activity {

    private WeatherBackgroundView weatherBgView;
    private TextView weatherLocation;
    private View btnChangeLocation;
    private TextView weatherMainTemp;
    private TextView weatherMainCondition;
    private TextView weatherMainSub;

    private TextView hourlySummary;
    private LinearLayout hourlyContainer;
    private LinearLayout dailyContainer;

    private TextView aqiValue;
    private TextView aqiStatus;
    private AqiSpectrumBarView aqiBar;
    private TextView aqiAdvisory;
    private TextView aqiStation;

    private TextView windSpeedText;
    private TextView windDescText;
    private TextView uvValText;
    private TextView uvDescText;
    private TextView humidityValText;
    private TextView sunsetText;

    private TextView weatherFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherBgView = findViewById(R.id.weatherBgView);
        weatherLocation = findViewById(R.id.weatherLocation);
        btnChangeLocation = findViewById(R.id.btnChangeLocation);
        weatherMainTemp = findViewById(R.id.weatherMainTemp);
        weatherMainCondition = findViewById(R.id.weatherMainCondition);
        weatherMainSub = findViewById(R.id.weatherMainSub);

        hourlySummary = findViewById(R.id.hourlySummary);
        hourlyContainer = findViewById(R.id.hourlyContainer);
        dailyContainer = findViewById(R.id.dailyContainer);

        aqiValue = findViewById(R.id.aqiValue);
        aqiStatus = findViewById(R.id.aqiStatus);
        aqiBar = findViewById(R.id.aqiBar);
        aqiAdvisory = findViewById(R.id.aqiAdvisory);
        aqiStation = findViewById(R.id.aqiStation);

        windSpeedText = findViewById(R.id.windSpeedText);
        windDescText = findViewById(R.id.windDescText);
        uvValText = findViewById(R.id.uvValText);
        uvDescText = findViewById(R.id.uvDescText);
        humidityValText = findViewById(R.id.humidityValText);
        sunsetText = findViewById(R.id.sunsetText);

        weatherFooter = findViewById(R.id.weatherFooter);

        if (btnChangeLocation != null) {
            btnChangeLocation.setOnClickListener(v -> showLocationPickerDialog());
            setupFocusAnimation(btnChangeLocation, 1.08f);
        }

        // Setup Card Focus Animators for TV Remote Navigation
        setupCardFocus(findViewById(R.id.cardHourly));
        setupCardFocus(findViewById(R.id.cardDaily));
        setupCardFocus(findViewById(R.id.cardAqi));
        setupCardFocus(findViewById(R.id.cardWind));
        setupCardFocus(findViewById(R.id.cardMetrics));

        populateUi();

        // Background sync to ensure freshest data
        WeatherAirQuality.syncIfDue(this, this::populateUi);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (weatherBgView != null) {
            weatherBgView.startAnimation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (weatherBgView != null) {
            weatherBgView.stopAnimation();
        }
    }

    private void showLocationPickerDialog() {
        String activeRegion = AppLocationManager.getActiveRegion(this);
        CharSequence[] topOptions = new CharSequence[]{
                "📍 Pilih Bandar / Stesen (" + activeRegion + ")",
                "🌏 Tukar Negara / Region Asia (Default: Malaysia)",
                "✨ Uji Animasi Latar Belakang (Cerah, Hujan, Ribut, Malam)"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        builder.setTitle("🌏 Lokasi & Suasana Cuaca");
        builder.setItems(topOptions, (dialog, which) -> {
            dialog.dismiss();
            if (which == 0) {
                showStationListDialog(activeRegion);
            } else if (which == 1) {
                showAsiaCountryDialog();
            } else {
                showWeatherAnimationPicker();
            }
        });
        builder.setNegativeButton("Tutup / Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showWeatherAnimationPicker() {
        CharSequence[] conditions = new CharSequence[]{
                "☀️ Cerah / Sunny (Pantulan suria, lens flare & cahaya emas)",
                "🌧️ Hujan / Rain (Titisan hujan bersudut & percikan air)",
                "⛈️ Ribut Petir / Thunderstorm (Kilat menyambar realistik & hujan lebat)",
                "⛅ Mendung / Cloudy (Awan bertingkat berarak)",
                "🌙 Malam / Clear Night (Bintang berkelip & sinaran bulan)",
                "🔄 Pulihkan kepada Cuaca Semasa Sebenar"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        builder.setTitle("✨ Uji Animasi Latar Cuaca");
        builder.setItems(conditions, (dialog, which) -> {
            dialog.dismiss();
            if (which == 0) {
                if (weatherBgView != null) weatherBgView.setCondition(WeatherBackgroundView.ConditionType.SUNNY);
                weatherMainCondition.setText("Sunny");
                hourlySummary.setText("Cuaca cerah dan terang sepanjang hari. Tiupan angin sekitar 6 km/h.");
                Toast.makeText(this, "Suasana: Cerah (Sunny)", Toast.LENGTH_SHORT).show();
            } else if (which == 1) {
                if (weatherBgView != null) weatherBgView.setCondition(WeatherBackgroundView.ConditionType.RAIN);
                weatherMainCondition.setText("Light drizzle");
                hourlySummary.setText("Keadaan hujan renyai dijangka berterusan tengah hari ini.");
                Toast.makeText(this, "Suasana: Hujan (Rain)", Toast.LENGTH_SHORT).show();
            } else if (which == 2) {
                if (weatherBgView != null) weatherBgView.setCondition(WeatherBackgroundView.ConditionType.THUNDERSTORM);
                weatherMainCondition.setText("Thunderstorm");
                hourlySummary.setText("Amaran ribut petir berserta hujan lebat dan kilat menyambar.");
                Toast.makeText(this, "Suasana: Ribut Petir (Thunderstorm)", Toast.LENGTH_SHORT).show();
            } else if (which == 3) {
                if (weatherBgView != null) weatherBgView.setCondition(WeatherBackgroundView.ConditionType.CLOUDY);
                weatherMainCondition.setText("Overcast");
                hourlySummary.setText("Keadaan mendung berterusan dengan litupan awan tebal.");
                Toast.makeText(this, "Suasana: Mendung (Cloudy)", Toast.LENGTH_SHORT).show();
            } else if (which == 4) {
                if (weatherBgView != null) weatherBgView.setCondition(WeatherBackgroundView.ConditionType.NIGHT);
                weatherMainCondition.setText("Clear night");
                hourlySummary.setText("Langit malam cerah dan tenang dengan pemandangan bintang.");
                Toast.makeText(this, "Suasana: Malam (Night)", Toast.LENGTH_SHORT).show();
            } else {
                populateUi();
                Toast.makeText(this, "Pulih kepada data cuaca sebenar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Kembali", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showAsiaCountryDialog() {
        final List<String> regions = AppLocationManager.getAvailableRegions();
        String activeRegion = AppLocationManager.getActiveRegion(this);
        int selectedIndex = 0;
        CharSequence[] items = new CharSequence[regions.size()];
        for (int i = 0; i < regions.size(); i++) {
            String r = regions.get(i);
            items[i] = r;
            if (r.equalsIgnoreCase(activeRegion)) {
                selectedIndex = i;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        builder.setTitle("🌏 Pilih Negara / Region Asia");
        builder.setSingleChoiceItems(items, selectedIndex, (dialog, which) -> {
            dialog.dismiss();
            String chosenRegion = regions.get(which);
            AppLocationManager.setActiveRegion(WeatherActivity.this, chosenRegion);
            Toast.makeText(WeatherActivity.this, "Region dipilih: " + chosenRegion, Toast.LENGTH_SHORT).show();
            showStationListDialog(chosenRegion);
        });
        builder.setNegativeButton("Kembali", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showStationListDialog(String region) {
        final List<AppLocationManager.Station> stations = AppLocationManager.getStationsForRegion(region);
        if (stations.isEmpty()) {
            Toast.makeText(this, "Tiada stesen dijumpai bagi " + region, Toast.LENGTH_SHORT).show();
            return;
        }

        String currentId = AppLocationManager.getStationId(this);
        int selectedIndex = 0;
        CharSequence[] items = new CharSequence[stations.size()];
        for (int i = 0; i < stations.size(); i++) {
            AppLocationManager.Station s = stations.get(i);
            items[i] = s.getDisplayName();
            if (s.id.equalsIgnoreCase(currentId)) {
                selectedIndex = i;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        builder.setTitle("📍 Pilih Bandar / Stesen (" + region + ")");
        builder.setSingleChoiceItems(items, selectedIndex, (dialog, which) -> {
            dialog.dismiss();
            AppLocationManager.Station picked = stations.get(which);
            AppLocationManager.setStation(WeatherActivity.this, picked);
            Toast.makeText(WeatherActivity.this, "Menyelaras cuaca " + picked.name + "...", Toast.LENGTH_SHORT).show();
            WeatherAirQuality.sync(WeatherActivity.this, WeatherActivity.this::populateUi);
        });
        builder.setNegativeButton("Kembali", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void populateUi() {
        WeatherAirQuality.FullWeather w = WeatherAirQuality.getFullWeather(this);

        // 1. Determine Condition and update animated background
        WeatherBackgroundView.ConditionType conditionType = determineCondition(w);
        if (weatherBgView != null) {
            weatherBgView.setCondition(conditionType);
        }

        // 2. Header
        String locDisplay = w.location;
        if (locDisplay != null && !locDisplay.isEmpty()) {
            // Capitalize neatly: e.g. "Putrajaya"
            String neatLoc = locDisplay.substring(0, 1).toUpperCase(Locale.ROOT) + locDisplay.substring(1).toLowerCase(Locale.ROOT);
            weatherLocation.setText(neatLoc);
        } else {
            weatherLocation.setText("Malaysia");
        }

        weatherMainTemp.setText(w.temp + "°");
        weatherMainCondition.setText(w.condition);
        weatherMainSub.setText(String.format(Locale.ROOT, "H: %d°   L: %d°   •   Rasa seperti %d°",
                w.highTemp, w.lowTemp, w.apparentTemp));

        // 3. Apple Weather Dynamic Summary
        if (hourlySummary != null) {
            String summary;
            if (w.rainChanceToday >= 70) {
                summary = "Keadaan hujan dijangka berterusan hari ini. Angin bertiup sehingga " + w.windSpeed + " km/h.";
            } else if (w.rainChanceToday >= 35) {
                summary = "Kemungkinan hujan dengan kebarangkalian " + w.rainChanceToday + "%. Suhu tertinggi " + w.highTemp + "°.";
            } else if (conditionType == WeatherBackgroundView.ConditionType.SUNNY) {
                summary = "Cuaca cerah dan terang sepanjang hari. Tiupan angin sekitar " + w.windSpeed + " km/h.";
            } else if (conditionType == WeatherBackgroundView.ConditionType.NIGHT) {
                summary = "Langit malam tenang. Suhu semasa " + w.temp + "° dengan kelembapan " + w.humidity + "%.";
            } else {
                summary = "Keadaan mendung dengan suhu selesa " + w.temp + "° dan tiupan angin " + w.windSpeed + " km/h.";
            }
            hourlySummary.setText(summary);
        }

        // 4. Hourly Strip (Apple Weather Horizontal Card)
        hourlyContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < w.hourly.size(); i++) {
            WeatherAirQuality.HourlyItem item = w.hourly.get(i);
            View v = inflater.inflate(R.layout.item_weather_hourly, hourlyContainer, false);
            TextView timeView = v.findViewById(R.id.hourlyTime);
            ImageView iconView = v.findViewById(R.id.hourlyIcon);
            TextView rainView = v.findViewById(R.id.hourlyRain);
            TextView tempView = v.findViewById(R.id.hourlyTemp);

            timeView.setText(item.time);
            iconView.setImageResource(getIconDrawable(item.icon, item.time));
            tempView.setText(item.temp + "°");

            if (item.rainChance > 0) {
                rainView.setVisibility(View.VISIBLE);
                rainView.setText("↑ " + item.rainChance + "%");
            } else {
                rainView.setVisibility(View.GONE);
            }

            setupFocusAnimation(v, 1.10f);
            hourlyContainer.addView(v);

            // Default focus on first hourly tile
            if (i == 0) {
                v.requestFocus();
            }
        }

        // 5. 7-Day Forecast (Apple Weather Horizontal Rows with Temp Spectrum Bar)
        dailyContainer.removeAllViews();
        int weekMin = 999;
        int weekMax = -999;
        for (WeatherAirQuality.DailyItem d : w.daily) {
            if (d.minTemp < weekMin) weekMin = d.minTemp;
            if (d.maxTemp > weekMax) weekMax = d.maxTemp;
        }
        if (weekMin == 999) {
            weekMin = 20;
            weekMax = 35;
        }

        for (int i = 0; i < w.daily.size(); i++) {
            WeatherAirQuality.DailyItem item = w.daily.get(i);
            View v = inflater.inflate(R.layout.item_weather_daily_row, dailyContainer, false);
            TextView dayView = v.findViewById(R.id.dailyDay);
            ImageView iconView = v.findViewById(R.id.dailyIcon);
            TextView minTempView = v.findViewById(R.id.dailyMinTemp);
            TempRangeBarView barView = v.findViewById(R.id.dailyTempBar);
            TextView maxTempView = v.findViewById(R.id.dailyMaxTemp);
            View divider = v.findViewById(R.id.dailyDivider);

            dayView.setText(item.day);
            iconView.setImageResource(getIconDrawable(item.icon, "12:00"));
            minTempView.setText(item.minTemp + "°");
            maxTempView.setText(item.maxTemp + "°");

            boolean isToday = (i == 0 || "Today".equalsIgnoreCase(item.day) || "Hari ini".equalsIgnoreCase(item.day));
            barView.setRange(weekMin, weekMax, item.minTemp, item.maxTemp, isToday ? w.temp : -999);

            if (i == w.daily.size() - 1 && divider != null) {
                divider.setVisibility(View.GONE);
            }

            setupFocusAnimation(v, 1.02f);
            dailyContainer.addView(v);
        }

        // 6. Air Quality Card
        aqiValue.setText(String.valueOf(w.aqi));
        aqiStatus.setText(w.aqiStatus);
        aqiBar.setAqi(w.aqi);

        if (w.stationPlace != null && !w.stationPlace.isEmpty()) {
            aqiStation.setText("Stesen JAS: " + w.stationPlace);
        } else {
            aqiStation.setText("Sumber: Jabatan Alam Sekitar (APIMS)");
        }

        // Advisory
        boolean isMalay = Locale.getDefault().getLanguage().equalsIgnoreCase("ms");
        if (w.aqi <= 50) {
            aqiAdvisory.setText(isMalay
                    ? "Kualiti udara baik. Aktiviti riadah luar boleh dijalankan seperti biasa."
                    : "Air quality is satisfactory. Normal outdoor activities can be enjoyed.");
        } else if (w.aqi <= 100) {
            aqiAdvisory.setText(isMalay
                    ? "Kualiti udara sederhana. Tiada sekatan aktiviti bagi kebanyakan orang."
                    : "Moderate air quality. People with respiratory conditions should limit exertion.");
        } else if (w.aqi <= 200) {
            aqiAdvisory.setText(isMalay
                    ? "Kualiti udara TIDAK SIHAT. Golongan berisiko kurangkan aktiviti luar."
                    : "Unhealthy. Sensitive groups should reduce prolonged outdoor exertion.");
        } else {
            aqiAdvisory.setText(isMalay
                    ? "SANGAT TIDAK SIHAT / BERBAHAYA. Elakkan sebarang aktiviti luar."
                    : "Very Unhealthy. Everyone should avoid outdoor exertion.");
        }

        // 7. Wind & Details Cards
        windSpeedText.setText(w.windSpeed + " km/h");
        String windDesc;
        if (w.windSpeed < 10) {
            windDesc = "Angin tenang sepoi-sepoi bahasa • Tiupan sehingga 12 km/h";
        } else if (w.windSpeed < 25) {
            windDesc = "Angin sederhana • Selesa untuk aktiviti luar";
        } else {
            windDesc = "Angin kencang • Berwaspada dengan tiupan angin ribut";
        }
        windDescText.setText(windDesc);

        uvValText.setText(String.valueOf(w.uvIndex));
        if (w.uvIndex <= 2) {
            uvDescText.setText("Rendah");
        } else if (w.uvIndex <= 5) {
            uvDescText.setText("Sederhana");
        } else if (w.uvIndex <= 7) {
            uvDescText.setText("Tinggi");
        } else {
            uvDescText.setText("Sangat Tinggi");
        }

        humidityValText.setText(w.humidity + "%");
        if (w.sunset != null && !w.sunset.isEmpty()) {
            sunsetText.setText("Terbenam: " + w.sunset);
        } else {
            sunsetText.setText("Terbit: " + w.sunrise);
        }

        // 8. Footer
        weatherFooter.setText(String.format(Locale.ROOT,
                "Updated %s  •  API: Jabatan Alam Sekitar (APIMS)  •  Open-Meteo Atmospheric Model",
                w.updatedTime));
    }

    private WeatherBackgroundView.ConditionType determineCondition(WeatherAirQuality.FullWeather w) {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        boolean isNight = (hour < 7 || hour >= 19);

        String cond = (w.condition != null) ? w.condition.toLowerCase(Locale.ROOT) : "";
        String icon = (w.icon != null) ? w.icon : "";

        if (cond.contains("thunder") || cond.contains("storm") || icon.contains("⛈")) {
            return WeatherBackgroundView.ConditionType.THUNDERSTORM;
        }
        if (cond.contains("rain") || cond.contains("drizzle") || cond.contains("shower") || icon.contains("🌧")) {
            return WeatherBackgroundView.ConditionType.RAIN;
        }
        if (isNight) {
            return WeatherBackgroundView.ConditionType.NIGHT;
        }
        if (cond.contains("sun") || cond.contains("clear") || icon.contains("☀")) {
            return WeatherBackgroundView.ConditionType.SUNNY;
        }
        return WeatherBackgroundView.ConditionType.CLOUDY;
    }

    private int getIconDrawable(String icon, String timeText) {
        boolean isNight = false;
        if (timeText != null) {
            String lower = timeText.toLowerCase(Locale.ROOT);
            if (lower.contains("pm") && (lower.contains("8") || lower.contains("9") || lower.contains("10") || lower.contains("11"))) {
                isNight = true;
            } else if (lower.contains("am") && (lower.contains("12") || lower.contains("1") || lower.contains("2") || lower.contains("3") || lower.contains("4") || lower.contains("5") || lower.contains("6"))) {
                isNight = true;
            }
        }

        if (icon == null) return isNight ? R.drawable.wx_moon : R.drawable.wx_partly;
        if (icon.contains("☀")) return isNight ? R.drawable.wx_moon : R.drawable.wx_sun;
        if (icon.contains("⛅")) return isNight ? R.drawable.wx_partly_night : R.drawable.wx_partly;
        if (icon.contains("🌧")) return R.drawable.wx_rain;
        if (icon.contains("⛈")) return R.drawable.wx_storm;
        if (icon.contains("☁")) return R.drawable.wx_cloud;
        if (icon.contains("🌫")) return R.drawable.wx_fog;
        return isNight ? R.drawable.wx_moon : R.drawable.wx_partly;
    }

    private void setupFocusAnimation(View v, float scale) {
        v.setOnFocusChangeListener((view, hasFocus) -> {
            float s = hasFocus ? scale : 1.0f;
            float elev = hasFocus ? 6f : 0f;
            view.animate().scaleX(s).scaleY(s).translationZ(elev).setDuration(140).start();
        });
    }

    private void setupCardFocus(View card) {
        if (card == null) return;
        card.setFocusable(true);
        card.setClickable(true);
        card.setOnFocusChangeListener((view, hasFocus) -> {
            float scale = hasFocus ? 1.025f : 1.0f;
            float elev = hasFocus ? 8f : 0f;
            view.animate().scaleX(scale).scaleY(scale).translationZ(elev).setDuration(150).start();
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
