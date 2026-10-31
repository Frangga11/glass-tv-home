package com.gathin.tvhome;

import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.service.dreams.DreamService;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Random;

/**
 * Screensaver (DreamService): StandBy OLED Nightstand Screensaver with large 110sp digits,
 * Gregorian date, Islamic Hijri date, next prayer countdown capsule, and subtle
 * drift to prevent OLED burn-in over long sessions.
 *
 * <p>Between 10:00 PM and 6:00 AM, automatically shifts to iPhone StandBy Wind Down
 * Mode (soft monochrome red on pure black). Dismisses on any remote button press.
 */
public class ClockDream extends DreamService {

    private static final String TAG = "TvHome.ClockDream";
    private static final long MOVE_MS = 60_000L;
    private static final long DREAM_SLEEP_TIMEOUT_MS = 2L * 60 * 60 * 1000L; // 2 hours

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Random random = new Random();
    private View content;
    private TextView clock;
    private TextView ampm;
    private TextView date;
    private TextView hijri;
    private TextView prayer;
    private TextView hint;
    private ImageView photoView;
    private View photoScrim;
    private String photoUriShown = "";
    private long dreamStartTime = 0;

    private final Runnable photoTick = new Runnable() {
        @Override public void run() {
            if (dreamStartTime > 0) {
                updatePhoto();
                handler.postDelayed(this, 20_000L);
            }
        }
    };

    private final Runnable tick = new Runnable() {
        @Override
        public void run() {
            if (dreamStartTime > 0 && android.os.SystemClock.elapsedRealtime() - dreamStartTime >= DREAM_SLEEP_TIMEOUT_MS) {
                Log.i(TAG, "Screensaver ran for 2 hours: turning off TV display");
                HomeKeyService.sleepScreen();
                try {
                    Runtime.getRuntime().exec("input keyevent 223");
                } catch (Exception ignored) {
                }
                finish();
                return;
            }
            updateNightstand();
            moveClock();
            handler.postDelayed(this, MOVE_MS);
        }
    };

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setInteractive(false);   // Any key wakes the TV back to where it was
        setFullscreen(true);
        setScreenBright(false);
        setContentView(R.layout.view_nightstand);

        View root = findViewById(R.id.nightstandRoot);
        if (root != null) {
            root.setOnClickListener(v -> finish());
        }

        content = findViewById(R.id.nightstandContent);
        clock = findViewById(R.id.nightstandClock);
        ampm = findViewById(R.id.nightstandAmPm);
        date = findViewById(R.id.nightstandDate);
        hijri = findViewById(R.id.nightstandHijri);
        prayer = findViewById(R.id.nightstandPrayer);
        hint = findViewById(R.id.nightstandHint);
        photoView = findViewById(R.id.nightstandPhoto);
        photoScrim = findViewById(R.id.nightstandPhotoScrim);

        updateNightstand();
        updatePhoto();
    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
        dreamStartTime = android.os.SystemClock.elapsedRealtime();
        if (content != null) {
            content.setAlpha(0f);
            content.post(() -> {
                updateNightstand();
                placeClock();
                content.animate().alpha(1f).setDuration(1000).start();
            });
        }
        handler.postDelayed(tick, MOVE_MS);
        handler.removeCallbacks(photoTick);
        handler.postDelayed(photoTick, 20_000L);
    }

    @Override
    public void onDreamingStopped() {
        dreamStartTime = 0;
        handler.removeCallbacks(tick);
        handler.removeCallbacks(photoTick);
        if (photoView != null) photoView.setImageDrawable(null);
        photoUriShown = "";
        super.onDreamingStopped();
    }

    @Override
    public void onDetachedFromWindow() {
        dreamStartTime = 0;
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

        int index = (int) ((android.os.SystemClock.elapsedRealtime() / 20_000L) % uris.size());
        Uri uri = uris.get(index);
        photoView.setVisibility(View.VISIBLE);
        photoScrim.setVisibility(View.VISIBLE);
        if (!uri.toString().equals(photoUriShown)) {
            android.graphics.Bitmap bitmap = StandbyPhotoStore.loadBitmap(this, uri);
            if (bitmap != null) {
                photoView.setImageBitmap(bitmap);
                photoUriShown = uri.toString();
            } else {
                photoView.setImageDrawable(null);
                photoView.setVisibility(View.GONE);
                photoScrim.setVisibility(View.GONE);
                photoUriShown = "";
                Log.w(TAG, "Skipping unreadable StandBy photo: " + uri);
            }
        }
    }

    private void updateNightstand() {
        if (content == null) return;

        if (hijri != null) {
            hijri.setText("🌙 " + PrayerTimes.getHijriDate());
        }

        if (prayer != null) {
            prayer.setText("🕌 " + PrayerTimes.getNextPrayerSummary(this));
        }

        float density = getResources().getDisplayMetrics().density;

        // StandBy Wind Down Mode: 10:00 PM (22:00) to 6:00 AM (06:00) -> Red Monochrome
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        boolean isNightMode = (hour >= 22 || hour < 6);

        if (isNightMode) {
            int red = 0xFFFF453A;
            if (clock != null) clock.setTextColor(red);
            if (ampm != null) ampm.setTextColor(0xCCFF453A);
            if (date != null) date.setTextColor(0xE6FF453A);
            if (hijri != null) hijri.setTextColor(0xE6FF453A);
            if (prayer != null) {
                prayer.setTextColor(red);
                GradientDrawable redPill = new GradientDrawable();
                redPill.setShape(GradientDrawable.RECTANGLE);
                redPill.setColor(0x26FF453A);
                redPill.setStroke((int) (1 * density), 0x59FF453A);
                redPill.setCornerRadius(21 * density);
                prayer.setBackground(redPill);
            }
            if (hint != null) hint.setTextColor(0x55FF453A);
        } else {
            if (clock != null) clock.setTextColor(0xFFFFFFFF);
            if (ampm != null) ampm.setTextColor(0xB3FFFFFF);
            if (date != null) date.setTextColor(0xE6FFFFFF);
            if (hijri != null) hijri.setTextColor(0xD9E6E0D2);
            if (prayer != null) {
                prayer.setTextColor(0xFFFFFFFF);
                prayer.setBackgroundResource(R.drawable.pill_bg);
            }
            if (hint != null) hint.setTextColor(0x4DFFFFFF);
        }
    }

    private void moveClock() {
        if (content == null) return;
        content.animate().alpha(0f).setDuration(600).withEndAction(() -> {
            placeClock();
            content.animate().alpha(1f).setDuration(800).start();
        }).start();
    }

    /** Subtle drift to prevent OLED burn-in: nudged up to 1.5% around the center. */
    private void placeClock() {
        if (content == null) return;
        View parent = (View) content.getParent();
        if (parent == null) return;
        int pw = parent.getWidth();
        int ph = parent.getHeight();
        if (pw <= 0 || ph <= 0) return;

        float nudgeX = pw * 0.015f * (2f * random.nextFloat() - 1f);
        float nudgeY = ph * 0.015f * (2f * random.nextFloat() - 1f);
        content.setTranslationX(nudgeX);
        content.setTranslationY(nudgeY);
    }
}
