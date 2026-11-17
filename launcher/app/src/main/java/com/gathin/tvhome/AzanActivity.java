package com.gathin.tvhome;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

/**
 * The azan, as a floating glass card at the top of the screen over whatever was on (the app or Home stays
 * in view behind it, blurred through the card where the TV can blur). Video and music pause because the
 * azan takes the sound (audio focus). When it ends, or on Back, it closes and the app
 * underneath is where it was.
 */
public class AzanActivity extends Activity {
    // 0.843 total linear scale targets approximately 12% of screen area from the prior photo-based area estimate.
    // Keep the window anchor and top offset independent so the popup does not move.
    private static final float CARD_SCALE = 0.843f;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private MediaPlayer player;
    private AudioManager audio;
    private AudioFocusRequest focus;
    private float density;
    private TextView hint;
    /** True once the card has been on screen: until then a stop is the screensaver going, not the owner. */
    private boolean shown;

    @Override
    protected void attachBaseContext(android.content.Context newBase) {
        super.attachBaseContext(LocaleContext.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Over the screensaver too: turn the screen on and come in front of it.
        setShowWhenLocked(true);
        setTurnScreenOn(true);
        density = getResources().getDisplayMetrics().density;
        int prayer = getIntent().getIntExtra("prayer", 4);
        Typeface outfit = getResources().getFont(R.font.inter);
        Typeface amiri = getResources().getFont(R.font.amiri_quran);

        // A floating card at the top of the screen; the app or Home behind it stays in view.
        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        w.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.y = dp(40);
        w.setAttributes(lp);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(cardDp(30), cardDp(20), cardDp(34), cardDp(20));
        // Match the approved Waktu Sholat popup material exactly: shared blurred Backdrop fill,
        // the same veil, and the same 20dp glass rim. No popup-specific opaque tint.
        card.setBackground(new GlassDrawable(card, 20 * density * CARD_SCALE, density, null, 0.5f));
        GradientDrawable focusOutline = new GradientDrawable();
        focusOutline.setColor(0x00000000);
        focusOutline.setCornerRadius(cardDp(20));
        focusOutline.setStroke(Math.max(1, cardDp(1.5f)), 0x80DCE5ED);
        card.setForeground(focusOutline);
        card.setClipToOutline(true);
        card.setElevation(cardDp(16));

        TextView takbir = text("ٱللَّهُ أَكْبَرُ", amiri, 40, 0xFFF5D489, 400);
        card.addView(takbir);
        View line = new View(this);
        line.setBackgroundColor(0x33FFFFFF);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(Math.max(1, cardDp(1)), cardDp(70));
        llp.leftMargin = cardDp(26);
        llp.rightMargin = cardDp(26);
        card.addView(line, llp);

        LinearLayout words = new LinearLayout(this);
        words.setOrientation(LinearLayout.VERTICAL);
        String town = Location.prefs(this).getString("now_town", "");
        String azanWord = AppLanguage.strAzanPopupTitle(this);
        TextView label = text(town.isEmpty() ? azanWord : azanWord + "  ·  " + town.toUpperCase(java.util.Locale.ROOT), outfit, 12, 0xB3FFFFFF, 600);
        label.setLetterSpacing(0.22f);
        label.setGravity(Gravity.START);
        words.addView(label);
        String when = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
        LinearLayout nameRow = new LinearLayout(this);
        nameRow.setOrientation(LinearLayout.HORIZONTAL);
        nameRow.setGravity(Gravity.BOTTOM);
        nameRow.setPadding(0, cardDp(2), 0, cardDp(2));

        String[] pNames = AppLanguage.getPrayerNames(this);
        int validPrayerIdx = Math.max(0, Math.min(prayer, pNames.length - 1));
        TextView name = text(pNames[validPrayerIdx], outfit, 28, 0xFFFFFFFF, 500);
        name.setSingleLine(true);
        nameRow.addView(name);
        TextView time = text(when, outfit, 20, 0xCCFFFFFF, 300);
        time.setSingleLine(true);
        time.setPadding(cardDp(14), 0, 0, cardDp(3));
        nameRow.addView(time);
        words.addView(nameRow);
        hint = text(AppLanguage.strAzanPopupHint(this), outfit, 12, 0x99FFFFFF, 400);
        hint.setGravity(Gravity.START);
        words.addView(hint);
        card.addView(words);
        card.setFocusable(true);
        card.setClickable(true);
        card.setOnClickListener(v -> finish());
        card.setOnFocusChangeListener((view, hasFocus) -> {
            focusOutline.setStroke(Math.max(1, cardDp(hasFocus ? 2f : 1.5f)),
                    hasFocus ? TvUiTokens.FOCUS_BORDER : 0x80DCE5ED);
            focusOutline.invalidateSelf();
        });

        // Attach the card first, then register it to redraw with the shared Home backdrop.
        setContentView(card);
        // Backdrop maps the same blurred Home wallpaper into this floating Activity window.
        Backdrop.watch(card);
        card.requestFocus();

        String soundChoice = Azan.getSoundChoice(this);
        if (Azan.SOUND_SILENT.equals(soundChoice)) {
            hint.setText(AppLanguage.strAzanPopupSilentHint(this));
            handler.postDelayed(this::finish, 15_000);
        } else if (Azan.SOUND_CHIME.equals(soundChoice)) {
            hint.setText(AppLanguage.strAzanPopupChimeHint(this));
            playChime();
        } else {
            File file = Azan.recording(this, soundChoice);
            if (file == null) {
                hint.setText(AppLanguage.strAzanPopupDownloadingHint(this, Azan.soundLabel(soundChoice)));
                Azan.fetch(this, soundChoice);
                playChime();
                handler.postDelayed(this::finish, 8000);
                return;
            }
            play(file, soundChoice);
        }
    }

    private void playChime() {
        audio = getSystemService(AudioManager.class);
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        focus = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                .setAudioAttributes(attrs)
                .setOnAudioFocusChangeListener(change -> { })
                .build();
        audio.requestAudioFocus(focus);
        try {
            player = MediaPlayer.create(this, R.raw.ps5_select);
            if (player != null) {
                player.setOnCompletionListener(mp -> handler.postDelayed(this::finish, 5000));
                player.start();
            } else {
                handler.postDelayed(this::finish, 8000);
            }
        } catch (Exception e) {
            handler.postDelayed(this::finish, 8000);
        }
    }

    private void play(File file, String soundChoice) {
        audio = getSystemService(AudioManager.class);
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        // Transient focus: Spotify and video players pause now and carry on when the azan is over.
        focus = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                .setAudioAttributes(attrs)
                .setOnAudioFocusChangeListener(change -> { })
                .build();
        audio.requestAudioFocus(focus);
        try {
            player = new MediaPlayer();
            player.setAudioAttributes(attrs);
            player.setDataSource(file.getPath());
            player.setOnCompletionListener(mp -> finish());
            player.setOnErrorListener((mp, what, extra) -> {
                finish();
                return true;
            });
            player.prepare();
            if (Azan.SOUND_KLCC.equals(soundChoice) && file.getName().contains("klcc")) {
                player.seekTo(Azan.KLCC_START_MS);
            }
            player.start();
        } catch (Exception e) {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE
                || keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        shown = true;
    }

    /**
     * Left in any way (Home, another app): the azan stops with it. Not before it has been shown, though:
     * started while the screensaver was ending, it is stopped once on the way in, and must keep playing.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (shown && !isChangingConfigurations()) finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (player != null) {
            player.release();
            player = null;
        }
        if (audio != null && focus != null) audio.abandonAudioFocusRequest(focus);
    }

    private TextView text(String s, Typeface font, float sp, int color, int weight) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTypeface(font);
        t.setTextSize(sp * CARD_SCALE);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER);
        if (font != null) t.setFontVariationSettings("'wght' " + weight);
        return t;
    }

    private int cardDp(float v) {
        return Math.max(1, Math.round(v * density * CARD_SCALE));
    }

    private int dp(float v) {
        return Math.round(v * density);
    }
}
