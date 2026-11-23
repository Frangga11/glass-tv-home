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
import java.util.Random;

/** Compact family standby dashboard; prayer/weather data remain owned by their existing modules. */
public class ClockDream extends DreamService {
    private static final String TAG = "TvHome.ClockDream";
    private static final long MOVE_MS = 60_000L;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Random random = new Random();
    private View content;
    private TextView nextPrayer, hijri, hint;
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
            PrayerTimes.syncIfDue(ClockDream.this, ClockDream.this::updateDashboard);
            WeatherAirQuality.syncIfDue(ClockDream.this, ClockDream.this::updateDashboard);
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
        PrayerTimes.syncIfDue(this, this::updateDashboard);
        WeatherAirQuality.syncIfDue(this, this::updateDashboard);
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
        StandbyDashboardBinder.updateMetrics(this, content, isWindDown());
        applyNightMode();
    }

    private void updatePrayerData() {
        if (nextPrayer != null) nextPrayer.setText("🕌  " + PrayerTimes.getNextPrayerSummary(this));
        if (hijri != null) hijri.setText(PrayerTimes.getHijriDate());
    }

    private boolean isWindDown() {
        int hour = PrayerTimes.calendar(this).get(Calendar.HOUR_OF_DAY);
        return hour >= 22 || hour < 6;
    }

    private void applyNightMode() {
        boolean windDown = isWindDown();
        int primary = windDown ? 0xFFFF453A : 0xFF172334;
        int secondary = windDown ? 0xCCFF453A : 0xFF475569;
        if (nextPrayer != null) nextPrayer.setTextColor(primary);
        if (hijri != null) hijri.setTextColor(secondary);
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
