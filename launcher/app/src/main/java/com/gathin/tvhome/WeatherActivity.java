package com.gathin.tvhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends Activity {

    private WeatherBackgroundView weatherBgView;
    private boolean reducedMotion;
    private TextView weatherLocation;
    private View btnChangeLocation;
    private TextView weatherMainTemp;
    private TextView weatherMainCondition;
    private TextView weatherMainSub;

    private LinearLayout hourlyContainer;
    private LinearLayout dailyContainer;

    // Custom Tiles
    private WindCompassView windCompassView;
    private MoonPhaseView moonPhaseView;
    private SunArcView sunArcView;
    private RadarMapView radarMapView;
    private PressureGaugeView pressureGaugeView;

    private TextView sunsetText;
    private TextView uvValText;
    private TextView uvDescText;
    private TextView uvAdviceText;
    private TextView feelsLikeVal;

    private TextView aqiValue;
    private TextView aqiStatus;
    private AqiSpectrumBarView aqiBar;
    private TextView aqiStation;
    private TextView aqiPollutantsText;
    private TextView dewPointValText;
    private TextView moonPhaseName;
    private TextView moonriseMoonsetText;
    private boolean destroyed;

    private TextView humidityValText;
    private TextView precipitationValue;
    private TextView precipitationSub;
    private TextView visibilityValue;
    private TextView visibilitySub;
    private TextView weatherFooter;

    @Override
    protected void attachBaseContext(android.content.Context newBase) {
        super.attachBaseContext(LocaleContext.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reducedMotion = Config.isReducedMotion(this);
        setContentView(R.layout.activity_weather);

        weatherBgView = findViewById(R.id.weatherBgView);
        if (weatherBgView != null) {
            weatherBgView.setReducedMotion(reducedMotion);
            try {
                android.app.ActivityManager am = (android.app.ActivityManager) getSystemService(ACTIVITY_SERVICE);
                android.app.ActivityManager.MemoryInfo mi = new android.app.ActivityManager.MemoryInfo();
                if (am != null) {
                    am.getMemoryInfo(mi);
                    if (mi.totalMem <= 1610612736L) weatherBgView.setLowSpecMode(true);
                }
            } catch (Exception ignored) {
                // Keep the normal visual mode when device memory information is unavailable.
            }
        }
        weatherLocation = findViewById(R.id.weatherLocation);
        btnChangeLocation = findViewById(R.id.btnChangeLocation);
        weatherMainTemp = findViewById(R.id.weatherMainTemp);
        weatherMainCondition = findViewById(R.id.weatherMainCondition);
        weatherMainSub = findViewById(R.id.weatherMainSub);

        hourlyContainer = findViewById(R.id.hourlyContainer);
        dailyContainer = findViewById(R.id.dailyContainer);

        windCompassView = findViewById(R.id.windCompassView);
        moonPhaseView = findViewById(R.id.moonPhaseView);
        moonPhaseName = findViewById(R.id.moonPhaseName);
        moonriseMoonsetText = findViewById(R.id.moonriseMoonsetText);
        sunArcView = findViewById(R.id.sunArcView);
        radarMapView = findViewById(R.id.radarMapView);
        pressureGaugeView = findViewById(R.id.pressureGaugeView);

        sunsetText = findViewById(R.id.sunsetText);
        uvValText = findViewById(R.id.uvValText);
        uvDescText = findViewById(R.id.uvDescText);
        uvAdviceText = findViewById(R.id.uvAdviceText);
        feelsLikeVal = findViewById(R.id.feelsLikeVal);

        aqiValue = findViewById(R.id.aqiValue);
        aqiStatus = findViewById(R.id.aqiStatus);
        aqiBar = findViewById(R.id.aqiBar);
        aqiStation = findViewById(R.id.aqiStation);
        aqiPollutantsText = findViewById(R.id.aqiPollutantsText);
        dewPointValText = findViewById(R.id.dewPointValText);

        humidityValText = findViewById(R.id.humidityValText);
        precipitationValue = findViewById(R.id.precipValue);
        precipitationSub = findViewById(R.id.precipSub);
        visibilityValue = findViewById(R.id.visibilityValue);
        visibilitySub = findViewById(R.id.visibilitySub);
        weatherFooter = findViewById(R.id.weatherFooter);

        if (btnChangeLocation != null) {
            btnChangeLocation.setVisibility(View.GONE);
        }

        // Setup Card Focus Animators for TV Remote Navigation on Leaf Cards
        setupCardFocus(findViewById(R.id.tileWind));
        setupCardFocus(findViewById(R.id.tileMoon));
        setupCardFocus(findViewById(R.id.tileSunset));
        setupCardFocus(findViewById(R.id.tileUv));
        setupCardFocus(findViewById(R.id.tileFeelsLike));
        setupCardFocus(findViewById(R.id.tileAqi));
        setupCardFocus(findViewById(R.id.tilePrecip));
        setupCardFocus(findViewById(R.id.tileVisibility));
        setupCardFocus(findViewById(R.id.tileDewPoint));
        setupCardFocus(findViewById(R.id.cardRadar));
        setupCardFocus(findViewById(R.id.tileHumidity));
        setupCardFocus(findViewById(R.id.tilePressure));

        populateUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (weatherBgView != null) {
            weatherBgView.startAnimation();
        }
        refreshUiIfActive();
        WeatherAirQuality.syncIfDue(this, this::refreshUiIfActive);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (weatherBgView != null) {
            weatherBgView.stopAnimation();
        }
    }

    @Override
    protected void onDestroy() {
        destroyed = true;
        super.onDestroy();
    }

    private void refreshUiIfActive() {
        if (destroyed || isFinishing() || isDestroyed()) return;
        populateUi();
    }

    private void showLocationPickerDialog() {
        String activeRegion = AppLocationManager.getActiveRegion(this);
        CharSequence[] topOptions = new CharSequence[]{
                getString(R.string.weather_pick_city_station, activeRegion),
                getString(R.string.weather_change_asia_region),
                getString(R.string.weather_set_background_mood)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setTitle(R.string.weather_location_dialog_title);
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
        builder.setNegativeButton(R.string.close_label, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        TvDialogStyle.apply(dialog, this, 520, 0);
    }

    private void showWeatherAnimationPicker() {
        SharedPreferences p = getSharedPreferences("app_weather_prefs", MODE_PRIVATE);

        CharSequence[] conditions = new CharSequence[]{
                getString(R.string.weather_set_sunny),
                getString(R.string.weather_set_rain),
                getString(R.string.weather_set_storm),
                getString(R.string.weather_set_cloudy),
                getString(R.string.weather_set_night),
                getString(R.string.weather_restore_live)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setTitle(R.string.weather_mood_dialog_title);
        builder.setItems(conditions, (dialog, which) -> {
            dialog.dismiss();
            SharedPreferences.Editor ed = p.edit();
            if (which == 0) {
                ed.putString("atmosphere_mode", "SUNNY").apply();
                TvFeedback.show(this, getString(R.string.weather_mood_selected, getString(R.string.weather_condition_clear)), Toast.LENGTH_SHORT);
            } else if (which == 1) {
                ed.putString("atmosphere_mode", "RAIN").apply();
                TvFeedback.show(this, getString(R.string.weather_mood_selected, getString(R.string.weather_condition_rain)), Toast.LENGTH_SHORT);
            } else if (which == 2) {
                ed.putString("atmosphere_mode", "THUNDERSTORM").apply();
                TvFeedback.show(this, getString(R.string.weather_mood_selected, getString(R.string.weather_condition_storm)), Toast.LENGTH_SHORT);
            } else if (which == 3) {
                ed.putString("atmosphere_mode", "CLOUDY").apply();
                TvFeedback.show(this, getString(R.string.weather_mood_selected, getString(R.string.weather_condition_cloudy)), Toast.LENGTH_SHORT);
            } else if (which == 4) {
                ed.putString("atmosphere_mode", "NIGHT").apply();
                TvFeedback.show(this, getString(R.string.weather_mood_selected, getString(R.string.weather_condition_night)), Toast.LENGTH_SHORT);
            } else {
                ed.putString("atmosphere_mode", "AUTO").apply();
                TvFeedback.show(this, R.string.weather_mood_auto, Toast.LENGTH_SHORT);
            }
            populateUi();
        });
        builder.setNegativeButton(R.string.back_action, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        TvDialogStyle.apply(dialog, this, 520, 0);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setTitle(R.string.weather_region_picker_title);
        builder.setSingleChoiceItems(items, selectedIndex, (dialog, which) -> {
            dialog.dismiss();
            String chosenRegion = regions.get(which);
            AppLocationManager.setActiveRegion(WeatherActivity.this, chosenRegion);
            TvFeedback.show(WeatherActivity.this, getString(R.string.weather_region_selected, chosenRegion), Toast.LENGTH_SHORT);
            showStationListDialog(chosenRegion);
        });
        builder.setNegativeButton(R.string.back_action, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        TvDialogStyle.apply(dialog, this, 520, 0);
    }

    private void showStationListDialog(String region) {
        final List<AppLocationManager.Station> stations = AppLocationManager.getStationsForRegion(region);
        if (stations.isEmpty()) {
            TvFeedback.show(this, getString(R.string.weather_no_stations, region), Toast.LENGTH_SHORT);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_TvHome_GlassDialog);
        builder.setTitle(getString(R.string.weather_station_picker_title, region));
        builder.setSingleChoiceItems(items, selectedIndex, (dialog, which) -> {
            dialog.dismiss();
            AppLocationManager.Station picked = stations.get(which);
            AppLocationManager.setStation(WeatherActivity.this, picked);
            TvFeedback.show(WeatherActivity.this, getString(R.string.weather_syncing, picked.name), Toast.LENGTH_SHORT);
            WeatherAirQuality.sync(WeatherActivity.this, WeatherActivity.this::refreshUiIfActive);
        });
        builder.setNegativeButton(R.string.back_action, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        TvDialogStyle.apply(dialog, this, 520, 0);
    }

    private void populateUi() {
        View previousFocus = getCurrentFocus();
        boolean hadFocus = previousFocus != null && previousFocus.hasFocus();
        String focusType = null;
        String focusKey = null;
        if (isInside(previousFocus, hourlyContainer)) {
            TextView label = previousFocus.findViewById(R.id.hourlyTime);
            focusType = "hourly";
            focusKey = label == null ? null : label.getText().toString();
        } else if (isInside(previousFocus, dailyContainer)) {
            TextView label = previousFocus.findViewById(R.id.dailyDay);
            focusType = "daily";
            focusKey = label == null ? null : label.getText().toString();
        }

        WeatherAirQuality.FullWeather w = WeatherAirQuality.getFullWeather(this);

        // 1. Determine Condition and update animated background
        WeatherBackgroundView.ConditionType conditionType = determineCondition(w);
        if (weatherBgView != null) {
            weatherBgView.setCondition(conditionType);
        }

        // 2. Header
        Location.Zone chosen = Location.chosen(this);
        String neatLoc = AppLocationManager.getLocationName(this);
        if (neatLoc == null || neatLoc.trim().isEmpty()) neatLoc = (chosen != null) ? chosen.town : w.location;
        if (neatLoc == null || neatLoc.trim().isEmpty()) neatLoc = "Pagar Alam";
        neatLoc = neatLoc.substring(0, 1).toUpperCase(Locale.ROOT) + neatLoc.substring(1);
        weatherLocation.setText(neatLoc);

        weatherMainTemp.setText(w.temp >= -100 && w.temp <= 100 ? w.temp + "°" : "—");
        
        // The selected visual mood may be manual; weather text always reflects API data.
        weatherMainCondition.setText(w.condition == null || w.condition.isEmpty()
                ? getString(R.string.weather_data_unavailable) : w.condition);

        if (w.highTemp >= -100 && w.lowTemp >= -100) {
            weatherMainSub.setText(getString(R.string.weather_high_low_summary, w.highTemp, w.lowTemp));
        } else {
            weatherMainSub.setText(getString(R.string.weather_data_unavailable));
        }

        // 3. Hourly Strip (Apple Weather Conditions Card)
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
            if (item.icon == null || item.icon.isEmpty()) iconView.setVisibility(View.INVISIBLE);
            else iconView.setImageResource(getIconDrawable(item.icon, item.time));
            tempView.setText(item.temp >= -100 && item.temp <= 100 ? item.temp + "°" : "—");

            if (item.rainChance > 0) {
                rainView.setVisibility(View.VISIBLE);
                rainView.setText(item.rainChance + "%");
            } else {
                rainView.setVisibility(View.INVISIBLE); // Keep layout height stable and aligned
            }

            setupFocusAnimation(v, 1.0f);
            hourlyContainer.addView(v);

        }

        // 4. 10-Day Forecast (Apple Weather Vertical List with Temp Spectrum Bar)
        dailyContainer.removeAllViews();
        int weekMin = Integer.MAX_VALUE;
        int weekMax = Integer.MIN_VALUE;
        for (WeatherAirQuality.DailyItem d : w.daily) {
            if (d.minTemp >= -100 && d.minTemp <= 100) weekMin = Math.min(weekMin, d.minTemp);
            if (d.maxTemp >= -100 && d.maxTemp <= 100) weekMax = Math.max(weekMax, d.maxTemp);
        }
        boolean hasWeekRange = weekMin != Integer.MAX_VALUE && weekMax != Integer.MIN_VALUE;
        if (hasWeekRange && weekMax <= weekMin) weekMax = weekMin + 1;

        for (int i = 0; i < w.daily.size(); i++) {
            WeatherAirQuality.DailyItem item = w.daily.get(i);
            View v = inflater.inflate(R.layout.item_weather_daily_row, dailyContainer, false);
            TextView dayView = v.findViewById(R.id.dailyDay);
            ImageView iconView = v.findViewById(R.id.dailyIcon);
            TextView rainView = v.findViewById(R.id.dailyRain);
            TextView minTempView = v.findViewById(R.id.dailyMinTemp);
            TempRangeBarView barView = v.findViewById(R.id.dailyTempBar);
            TextView maxTempView = v.findViewById(R.id.dailyMaxTemp);
            View divider = v.findViewById(R.id.dailyDivider);

            dayView.setText(item.day);
            if (item.icon == null || item.icon.isEmpty()) iconView.setVisibility(View.INVISIBLE);
            else iconView.setImageResource(getIconDrawable(item.icon, "12:00"));
            if (rainView != null) {
                if (item.rainChance > 0) {
                    rainView.setVisibility(View.VISIBLE);
                    rainView.setText(item.rainChance + "%");
                } else {
                    rainView.setVisibility(View.INVISIBLE);
                }
            }

            boolean validDayRange = item.minTemp >= -100 && item.maxTemp <= 100
                    && item.maxTemp >= item.minTemp;
            minTempView.setText(validDayRange ? item.minTemp + "°" : "—");
            maxTempView.setText(validDayRange ? item.maxTemp + "°" : "—");

            boolean isToday = (i == 0 || "Today".equalsIgnoreCase(item.day) || "Hari ini".equalsIgnoreCase(item.day));
            if (hasWeekRange && validDayRange) {
                barView.setVisibility(View.VISIBLE);
                barView.setRange(weekMin, weekMax, item.minTemp, item.maxTemp,
                        isToday && w.temp >= -100 && w.temp <= 100 ? w.temp : -999);
            } else {
                barView.setVisibility(View.INVISIBLE);
            }

            if (i == w.daily.size() - 1 && divider != null) {
                divider.setVisibility(View.GONE);
            }

            setupFocusAnimation(v, 1.0f);
            dailyContainer.addView(v);
        }

        // 5. Populate Modular Metric Tiles
        View focusTarget = null;
        if ("hourly".equals(focusType)) focusTarget = findRowByText(hourlyContainer, R.id.hourlyTime, focusKey);
        else if ("daily".equals(focusType)) focusTarget = findRowByText(dailyContainer, R.id.dailyDay, focusKey);
        if (focusTarget == null && "hourly".equals(focusType) && hourlyContainer.getChildCount() > 0) {
            focusTarget = hourlyContainer.getChildAt(0);
        } else if (focusTarget == null && "daily".equals(focusType) && dailyContainer.getChildCount() > 0) {
            focusTarget = dailyContainer.getChildAt(0);
        }
        if (focusTarget != null) focusTarget.requestFocus();
        else if (!hadFocus && hourlyContainer.getChildCount() > 0) hourlyContainer.getChildAt(0).requestFocus();

        if (windCompassView != null) {
            windCompassView.setVisibility(w.windSpeed >= 0 ? View.VISIBLE : View.INVISIBLE);
            if (w.windSpeed >= 0) windCompassView.setWind(w.windSpeed, w.windGust, w.windDirection);
        }

        if (sunArcView != null) {
            sunArcView.setTimes(w.sunrise, w.sunset, w.currentTimeLocal, getString(R.string.weather_sunrise_label));
        }
        if (moonPhaseView != null) {
            moonPhaseView.setMoonData(w.moonPhase, AppLocationManager.getLatitude(this) < 0.0);
        }
        if (moonPhaseName != null) moonPhaseName.setText(moonPhaseName(w.moonPhase));
        if (moonriseMoonsetText != null) {
            moonriseMoonsetText.setText(("—".equals(w.moonrise) && "—".equals(w.moonset)) ? "—"
                    : getString(R.string.weather_moonrise_set_format, w.moonrise, w.moonset));
        }
        if (sunsetText != null) {
            sunsetText.setText(w.sunset != null ? w.sunset : "—");
        }

        if (uvValText != null) {
            uvValText.setText(w.uvIndex >= 0 ? String.valueOf(w.uvIndex) : "—");
        }
        if (uvDescText != null) uvDescText.setText(uvCategoryLabel(w.uvIndex));
        if (uvAdviceText != null) uvAdviceText.setText(uvRecommendation(w.uvIndex));

        if (feelsLikeVal != null) {
            feelsLikeVal.setText(w.apparentTemp >= -100 && w.apparentTemp <= 100 ? w.apparentTemp + "°" : "—");
        }

        // Air Quality
        if (aqiValue != null) aqiValue.setText(w.aqi >= 0 ? String.valueOf(w.aqi) : "—");
        if (aqiStatus != null) aqiStatus.setText(w.aqiStatus);
        if (aqiBar != null) {
            if (w.aqi >= 0) {
                aqiBar.setVisibility(View.VISIBLE);
                aqiBar.setAqi(w.aqi);
            } else {
                aqiBar.setVisibility(View.INVISIBLE);
            }
        }
        if (aqiStation != null) {
            String place = (w.stationPlace != null && !w.stationPlace.isEmpty())
                    ? w.stationPlace : AppLocationManager.getStationPlace(this);
            aqiStation.setText(getString(R.string.weather_model_reference_detail, place));
        }
        if (aqiPollutantsText != null) {
            String pollutantText = getString(R.string.weather_pollutants_format,
                    pollutantValue(w.pm25), pollutantValue(w.pm10), pollutantValue(w.carbonMonoxide / 1000.0),
                    pollutantValue(w.nitrogenDioxide), pollutantValue(w.sulphurDioxide));
            boolean missingPollutant = Double.isNaN(w.pm25) || Double.isInfinite(w.pm25)
                    || Double.isNaN(w.pm10) || Double.isInfinite(w.pm10)
                    || Double.isNaN(w.carbonMonoxide) || Double.isInfinite(w.carbonMonoxide)
                    || Double.isNaN(w.nitrogenDioxide) || Double.isInfinite(w.nitrogenDioxide)
                    || Double.isNaN(w.sulphurDioxide) || Double.isInfinite(w.sulphurDioxide);
            if (missingPollutant) pollutantText += "\\n" + getString(R.string.weather_pollutant_missing_note);
            aqiPollutantsText.setText(pollutantText);
        }

        // Radar Map
        if (radarMapView != null) {
            radarMapView.setDetails(w.temp, neatLoc);
        }

        // Humidity, precipitation, visibility & pressure
        if (humidityValText != null) {
            humidityValText.setText(w.humidity >= 0 ? w.humidity + "%" : "—");
        }
        boolean englishMetrics = AppLanguage.LANG_EN.equals(AppLanguage.getLanguage(this));
        if (precipitationValue != null) {
            precipitationValue.setText(Double.isNaN(w.precipitationMm) ? "—" : String.format(Locale.ROOT, "%.1f mm", w.precipitationMm));
        }
        if (dewPointValText != null) {
            dewPointValText.setText(Double.isNaN(w.dewPoint) ? "—" : String.format(Locale.ROOT, "%.1f°", w.dewPoint));
        }
        if (precipitationSub != null) {
            precipitationSub.setText(w.rainChanceToday >= 0 ? getString(R.string.weather_precipitation_summary, w.rainChanceToday) : "—");
        }
        if (visibilityValue != null) {
            visibilityValue.setText(Double.isNaN(w.visibilityKm) ? "—" : String.format(Locale.ROOT, "%.1f km", w.visibilityKm));
        }
        if (visibilitySub != null) {
            visibilitySub.setText(Double.isNaN(w.visibilityKm) ? "—" : (w.visibilityKm < 1.0
                    ? getString(R.string.limited_visibility)
                    : getString(R.string.good_visibility)));
        }
        if (pressureGaugeView != null) {
            pressureGaugeView.setVisibility(w.pressure >= 0 ? View.VISIBLE : View.INVISIBLE);
            if (w.pressure >= 0) pressureGaugeView.setPressure(w.pressure);
        }

        // 6. Footer
        if (weatherFooter != null) {
            String weatherState = sourceState(w.weatherAvailable, w.weatherStale,
                    w.weatherRefreshFailed, w.networkAvailable, w.updatedTime);
            String aqiState = sourceState(w.aqiAvailable, w.aqiStale,
                    w.aqiRefreshFailed, w.networkAvailable, w.aqiUpdatedTime);
            weatherFooter.setText(getString(R.string.weather_footer_format,
                    weatherState, aqiState, getString(R.string.weather_data_source)));
        }
    }

    private boolean isInside(View candidate, ViewGroup parent) {
        if (candidate == null || parent == null) return false;
        View current = candidate;
        while (current != null) {
            if (current == parent) return true;
            ViewParent next = current.getParent();
            current = next instanceof View ? (View) next : null;
        }
        return false;
    }

    static View findRowByText(LinearLayout container, int textViewId, String value) {
        if (container == null || value == null) return null;
        for (int i = 0; i < container.getChildCount(); i++) {
            View row = container.getChildAt(i);
            TextView label = row.findViewById(textViewId);
            if (label != null && value.contentEquals(label.getText())) return row;
        }
        return null;
    }

    private String pollutantValue(double value) {
        return Double.isNaN(value) || Double.isInfinite(value) ? "—"
                : String.format(Locale.ROOT, "%.1f", value);
    }

    private String moonPhaseName(double phase) {
        if (Double.isNaN(phase) || Double.isInfinite(phase)) return getString(R.string.weather_data_unavailable);
        double p = phase - Math.floor(phase);
        if (p < 0.0625 || p >= 0.9375) return getString(R.string.weather_moon_new);
        if (p < 0.1875) return getString(R.string.weather_moon_waxing_crescent);
        if (p < 0.3125) return getString(R.string.weather_moon_first_quarter);
        if (p < 0.4375) return getString(R.string.weather_moon_waxing_gibbous);
        if (p < 0.5625) return getString(R.string.weather_moon_full);
        if (p < 0.6875) return getString(R.string.weather_moon_waning_gibbous);
        if (p < 0.8125) return getString(R.string.weather_moon_last_quarter);
        return getString(R.string.weather_moon_waning_crescent);
    }

    private String uvCategoryLabel(int uv) {
        if (uv < 0) return getString(R.string.weather_data_unavailable);
        if (uv <= 2) return getString(R.string.uv_low);
        if (uv <= 5) return getString(R.string.uv_moderate);
        if (uv <= 7) return getString(R.string.uv_high);
        if (uv <= 10) return getString(R.string.uv_very_high);
        return getString(R.string.uv_extreme);
    }

    private String uvRecommendation(int uv) {
        if (uv < 0) return getString(R.string.weather_data_unavailable);
        if (uv <= 2) return getString(R.string.uv_advice_low);
        if (uv <= 5) return getString(R.string.uv_advice_moderate);
        if (uv <= 7) return getString(R.string.uv_advice_high);
        if (uv <= 10) return getString(R.string.uv_advice_very_high);
        return getString(R.string.uv_advice_extreme);
    }

    private String sourceState(boolean available, boolean stale, boolean failed,
                               boolean networkAvailable, String updatedTime) {
        String state;
        if (!available) {
            if (!networkAvailable) state = getString(R.string.weather_state_offline_unavailable);
            else if (failed) state = getString(R.string.weather_state_refresh_failed);
            else state = getString(R.string.weather_state_unavailable);
        } else if (!networkAvailable) {
            state = stale ? getString(R.string.weather_state_offline_stale)
                    : getString(R.string.weather_state_offline);
        } else if (stale && failed) {
            state = getString(R.string.weather_state_stale_failed);
        } else if (stale) {
            state = getString(R.string.weather_state_stale);
        } else if (failed) {
            state = getString(R.string.weather_state_refresh_failed);
        } else {
            state = getString(R.string.weather_state_current);
        }
        return updatedTime == null || updatedTime.isEmpty() || "—".equals(updatedTime)
                ? state : state + " · " + updatedTime;
    }

    private WeatherBackgroundView.ConditionType determineCondition(WeatherAirQuality.FullWeather w) {
        SharedPreferences p = getSharedPreferences("app_weather_prefs", MODE_PRIVATE);
        String mode = p.getString("atmosphere_mode", "AUTO");
        if ("SUNNY".equalsIgnoreCase(mode)) return WeatherBackgroundView.ConditionType.SUNNY;
        if ("RAIN".equalsIgnoreCase(mode)) return WeatherBackgroundView.ConditionType.RAIN;
        if ("THUNDERSTORM".equalsIgnoreCase(mode)) return WeatherBackgroundView.ConditionType.THUNDERSTORM;
        if ("CLOUDY".equalsIgnoreCase(mode)) return WeatherBackgroundView.ConditionType.CLOUDY;
        if ("NIGHT".equalsIgnoreCase(mode)) return WeatherBackgroundView.ConditionType.NIGHT;

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        boolean isNight = w.isDayAvailable ? !w.isDay : (hour < 7 || hour >= 19);

        String cond = (w.condition != null) ? w.condition.toLowerCase(Locale.ROOT) : "";
        String icon = (w.icon != null) ? w.icon : "";
        if (w.temp < -100 || cond.isEmpty() || cond.contains("unavailable")
                || cond.contains("tidak tersedia")) {
            return WeatherBackgroundView.ConditionType.CLOUDY;
        }

        // Thunderstorm codes: 95, 96, 99
        if (cond.contains("thunder") || cond.contains("storm") || cond.contains("ribut") || icon.contains("⛈")) {
            return WeatherBackgroundView.ConditionType.THUNDERSTORM;
        }

        // Real Rain: Heavy rain or showers (codes 61, 63, 65, 80, 81, 82)
        if (cond.contains("heavy") || cond.contains("lebat") || 
            (cond.contains("hujan") && !cond.contains("renyai")) || 
            (cond.contains("rain") && !cond.contains("drizzle") && !cond.contains("light"))) {
            return WeatherBackgroundView.ConditionType.RAIN;
        }

        // Night
        if (isNight) {
            return WeatherBackgroundView.ConditionType.NIGHT;
        }

        // Daytime: In Malaysia, if condition is trace drizzle or sunny or partly cloudy or clear
        // default to Sunny with radiant sky and sun lens flare!
        return WeatherBackgroundView.ConditionType.SUNNY;
    }

    private int getIconDrawable(String icon, String timeText) {
        boolean isNight = false;
        if (timeText != null) {
            String lower = timeText.trim().toLowerCase(Locale.ROOT);
            if ("now".equals(lower)) {
                int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                isNight = (h < 7 || h >= 19);
            } else {
                try {
                    boolean isPm = lower.contains("pm");
                    boolean isAm = lower.contains("am");
                    String digits = lower.replaceAll("[^0-9]", "");
                    if (!digits.isEmpty()) {
                        int hourNum = Integer.parseInt(digits);
                        int hour24;
                        if (isPm) {
                            hour24 = (hourNum == 12) ? 12 : hourNum + 12;
                        } else if (isAm) {
                            hour24 = (hourNum == 12) ? 0 : hourNum;
                        } else {
                            hour24 = hourNum;
                        }
                        isNight = (hour24 < 7 || hour24 >= 19);
                    }
                } catch (Exception ignored) {
                }
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
            if (reducedMotion) {
                view.animate().cancel();
                view.setScaleX(s);
                view.setScaleY(s);
                view.setTranslationZ(elev);
            } else {
                view.animate().scaleX(s).scaleY(s).translationZ(elev).setDuration(TvUiTokens.FOCUS_ANIMATION_MS).start();
            }
        });
    }

    private void setupCardFocus(View card) {
        if (card == null) return;
        card.setFocusable(true);
        card.setClickable(true);
        card.setOnFocusChangeListener((view, hasFocus) -> {
            float elev = hasFocus ? 6f : 0f;
            if (reducedMotion) {
                view.animate().cancel();
                view.setScaleX(1f);
                view.setScaleY(1f);
                view.setTranslationZ(elev);
            } else {
                view.animate().scaleX(1.0f).scaleY(1.0f).translationZ(elev).setDuration(120).start();
            }
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
