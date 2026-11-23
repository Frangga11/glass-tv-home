package com.gathin.tvhome;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.service.dreams.DreamService;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

/** Compact family standby dashboard; prayer/weather data remain owned by their existing modules. */
public class ClockDream extends DreamService {
    private static final String TAG = "TvHome.ClockDream";
    private static final long MOVE_MS = 60_000L;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Random random = new Random();
    private View content;
    private TextView nextPrayer, hijri, weatherIcon, weatherTemp, weatherRange, aqiValue, aqiStatus, hint;
    private TextView[] prayerLabels, prayerTimes;
    private ImageView photoView;
    private View photoScrim;
    private String photoUriShown = "";
    private boolean dreamActive;

    private final Runnable photoTick = new Runnable() {
        @Override public void run() {
            if (dreamActive) {
                updatePhoto();
                handler.postDelayed(this, StandbyPhotoStore.ROTATION_INTERVAL_MS);
            }
        }
    };

    private final Runnable tick = new Runnable() {
        @Override public void run() {
            if (!dreamActive) return;
            updateDashboard();
            PrayerTimes.syncIfDue(ClockDream.this, ClockDream.this::updatePrayerData);
            WeatherAirQuality.syncIfDue(ClockDream.this, ClockDream.this::updateWeather);
            moveClock();
            handler.postDelayed(this, MOVE_MS);
        }
    };

    @Override protected void attachBaseContext(android.content.Context newBase) {
        super.attachBaseContext(LocaleContext.wrap(newBase));
    }

    @Override public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setInteractive(false);
        setFullscreen(true);
        setScreenBright(false);
        setContentView(R.layout.view_nightstand);

        View root = findViewById(R.id.nightstandRoot);
        if (root != null) root.setOnClickListener(v -> finish());
        content = findViewById(R.id.nightstandContent);
        nextPrayer = findViewById(R.id.nightstandPrayer);
        hijri = findViewById(R.id.nightstandHijri);
        weatherIcon = findViewById(R.id.nightstandWeatherIcon);
        weatherTemp = findViewById(R.id.nightstandWeatherTemp);
        weatherRange = findViewById(R.id.nightstandWeatherRange);
        aqiValue = findViewById(R.id.nightstandAqiValue);
        aqiStatus = findViewById(R.id.nightstandAqiStatus);
        prayerLabels = new TextView[] {
                findViewById(R.id.nightstandFajrLabel), findViewById(R.id.nightstandDhuhrLabel),
                findViewById(R.id.nightstandAsrLabel), findViewById(R.id.nightstandMaghribLabel),
                findViewById(R.id.nightstandIshaLabel)
        };
        prayerTimes = new TextView[] {
                findViewById(R.id.nightstandFajrTime), findViewById(R.id.nightstandDhuhrTime),
                findViewById(R.id.nightstandAsrTime), findViewById(R.id.nightstandMaghribTime),
                findViewById(R.id.nightstandIshaTime)
        };
        hint = findViewById(R.id.nightstandHint);
        photoView = findViewById(R.id.nightstandPhoto);
        photoScrim = findViewById(R.id.nightstandPhotoScrim);
        applyResponsiveBounds();
        updateDashboard();
    }

    @Override public void onDreamingStarted() {
        super.onDreamingStarted();
        dreamActive = true;
        updatePhoto();
        updateDashboard();
        PrayerTimes.syncIfDue(this, this::updatePrayerData);
        WeatherAirQuality.syncIfDue(this, this::updateWeather);
        if (content != null) {
            content.setAlpha(0f);
            content.post(() -> {
                if (!dreamActive || content == null) return;
                updateDashboard();
                placeClock();
                content.animate().alpha(1f).setDuration(850).start();
            });
        }
        handler.removeCallbacks(tick);
        handler.postDelayed(tick, MOVE_MS);
        handler.removeCallbacks(photoTick);
        handler.postDelayed(photoTick, StandbyPhotoStore.ROTATION_INTERVAL_MS);
    }

    @Override public void onDreamingStopped() {
        dreamActive = false;
        handler.removeCallbacks(tick);
        handler.removeCallbacks(photoTick);
        if (photoView != null) photoView.setImageDrawable(null);
        photoUriShown = "";
        super.onDreamingStopped();
    }

    @Override public void onDetachedFromWindow() {
        dreamActive = false;
        handler.removeCallbacks(tick);
        handler.removeCallbacks(photoTick);
        if (photoView != null) photoView.setImageDrawable(null);
        photoUriShown = "";
        super.onDetachedFromWindow();
    }

    private void updatePhoto() {
        if (photoView == null || photoScrim == null) return;
        java.util.List<Uri> uris = StandbyPhotoStore.getUris(this);
        if (!StandbyPhotoStore.isPhotoMode(this) || uris.isEmpty()) {
            photoView.setImageDrawable(null);
            photoView.setVisibility(View.GONE);
            photoScrim.setVisibility(View.GONE);
            photoUriShown = "";
            return;
        }
        int index = StandbyPhotoStore.getCurrentPhotoIndex(uris.size());
        if (index < 0 || index >= uris.size()) return;
        Uri uri = uris.get(index);
        photoView.setVisibility(View.VISIBLE);
        photoScrim.setVisibility(View.VISIBLE);
        if (!uri.toString().equals(photoUriShown)) {
            photoUriShown = uri.toString();
            StandbyPhotoStore.loadBitmapAsync(this, uri, bitmap -> {
                if (bitmap == null) {
                    if (dreamActive && uri.toString().equals(photoUriShown)) {
                        photoView.setImageDrawable(null);
                        photoView.setVisibility(View.GONE);
                        photoScrim.setVisibility(View.GONE);
                        Log.w(TAG, "Skipping unreadable StandBy photo: " + uri);
                    }
                    return;
                }
                if (!dreamActive || photoView == null || !StandbyPhotoStore.isPhotoMode(this)
                        || !uri.toString().equals(photoUriShown)) {
                    bitmap.recycle();
                    return;
                }
                photoView.setImageBitmap(bitmap);
            });
        }
    }

    private void applyResponsiveBounds() {
        if (content == null) return;
        float density = getResources().getDisplayMetrics().density;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int width = Math.min(Math.round(280 * density), Math.max(Math.round(200 * density),
                screenWidth - Math.round(48 * density)));
        ViewGroup.LayoutParams params = content.getLayoutParams();
        if (params != null) {
            params.width = width;
            content.setLayoutParams(params);
        }
    }

    private void updateDashboard() {
        if (content == null) return;
        updatePrayerData();
        updateWeather();
        applyNightMode();
    }

    private void updatePrayerData() {
        if (nextPrayer != null) nextPrayer.setText("🕌  " + PrayerTimes.getNextPrayerSummary(this));
        if (hijri != null) hijri.setText(PrayerTimes.getHijriDate());
        if (prayerTimes == null || prayerLabels == null) return;

        PrayerTimes.Day day = PrayerTimes.day(this, null, PrayerTimes.calendar(this));
        int[] indices = {0, 2, 3, 4, 5}; // Fajr, Dhuhr, Asr, Maghrib, Isha; omit sunrise.
        boolean english = AppLanguage.LANG_EN.equals(AppLanguage.getLanguage(this));
        String[] names = english
                ? new String[] {"Fajr", "Dhuhr", "Asr", "Maghrib", "Isha"}
                : new String[] {"Subuh", "Dzuhur", "Ashar", "Maghrib", "Isya"};
        for (int i = 0; i < indices.length; i++) {
            prayerLabels[i].setText(names[i]);
            prayerTimes[i].setText(day == null ? "—" : formatPrayerTime(day.minutes[indices[i]]));
        }
    }

    private static String formatPrayerTime(int minuteOfDay) {
        if (minuteOfDay < 0 || minuteOfDay >= 1440) return "—";
        return String.format(Locale.ROOT, "%02d:%02d", minuteOfDay / 60, minuteOfDay % 60);
    }

    private void updateWeather() {
        if (weatherTemp == null || aqiValue == null) return;
        WeatherAirQuality.FullWeather weather = WeatherAirQuality.getFullWeather(this);
        if (weather == null) {
            weatherIcon.setText("—");
            weatherTemp.setText("—");
            weatherRange.setText("");
            aqiValue.setText("AQI —");
            aqiStatus.setText("—");
            aqiStatus.setTag(0xFF64748B);
            return;
        }
        weatherIcon.setText(weather.weatherAvailable && weather.icon != null && !weather.icon.isEmpty()
                ? weather.icon : "—");
        weatherTemp.setText(weather.weatherAvailable ? weather.temp + "°" : "—");
        weatherRange.setText(weather.weatherAvailable && weather.highTemp >= 0 && weather.lowTemp >= 0
                ? "H:" + weather.highTemp + "°  L:" + weather.lowTemp + "°" : "");
        aqiValue.setText(weather.aqiAvailable ? "AQI " + weather.aqi : "AQI —");
        aqiStatus.setText(weather.aqiAvailable ? weather.aqiStatus : "—");
        aqiStatus.setTag(weather.aqiAvailable ? weather.aqiColor : 0xFF64748B);
    }

    private void applyNightMode() {
        int hour = PrayerTimes.calendar(this).get(Calendar.HOUR_OF_DAY);
        boolean windDown = hour >= 22 || hour < 6;
        int primary = windDown ? 0xFFFF453A : 0xFF172334;
        int secondary = windDown ? 0xCCFF453A : 0xFF475569;
        if (nextPrayer != null) nextPrayer.setTextColor(primary);
        if (hijri != null) hijri.setTextColor(secondary);
        if (weatherIcon != null) weatherIcon.setTextColor(primary);
        if (weatherTemp != null) weatherTemp.setTextColor(primary);
        if (weatherRange != null) weatherRange.setTextColor(secondary);
        if (aqiValue != null) aqiValue.setTextColor(primary);
        if (aqiStatus != null) {
            Object color = aqiStatus.getTag();
            aqiStatus.setTextColor(windDown || !(color instanceof Integer) ? primary : (Integer) color);
        }
        if (prayerLabels != null) for (TextView label : prayerLabels) if (label != null) label.setTextColor(secondary);
        if (prayerTimes != null) for (TextView time : prayerTimes) if (time != null) time.setTextColor(primary);
        TextView clock = findViewById(R.id.nightstandClock);
        TextView date = findViewById(R.id.nightstandDate);
        if (clock != null) clock.setTextColor(primary);
        if (date != null) date.setTextColor(secondary);
        if (hint != null) hint.setTextColor(windDown ? 0x66FF453A : 0x66475569);
    }

    private void moveClock() {
        if (content == null) return;
        content.animate().alpha(0f).setDuration(500).withEndAction(() -> {
            if (!dreamActive || content == null) return;
            placeClock();
            content.animate().alpha(1f).setDuration(650).start();
        }).start();
    }

    /** Subtle OLED burn-in drift; the panel's internal center alignment is unchanged. */
    private void placeClock() {
        if (content == null) return;
        View parent = (View) content.getParent();
        if (parent == null) return;
        int width = parent.getWidth(), height = parent.getHeight();
        if (width <= 0 || height <= 0) return;
        content.setTranslationX(width * 0.012f * (2f * random.nextFloat() - 1f));
        content.setTranslationY(height * 0.012f * (2f * random.nextFloat() - 1f));
    }
}
