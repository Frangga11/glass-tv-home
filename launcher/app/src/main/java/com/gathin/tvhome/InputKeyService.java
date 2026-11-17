package com.gathin.tvhome;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import java.util.List;

/**
 * Routes recognized TV remote Input keys to Glass TV Home's own source popup from any foreground app.
 * Standard Android TV remotes use KEYCODE_TV_INPUT; some Chromecast remotes use KEYCODE_TV, and one
 * Xiaomi TV remote family reports KEYCODE_MEDIA_RECORD. The accepted list is configurable in config.xml
 * or a theme's config.json so an unrelated key is never swallowed just because a vendor uses another map.
 *
 * Opt-in: INPUT_KEY_SERVICE=1 build-and-install.sh (or -InputKeyService on Windows) enables global capture.
 */
public class InputKeyService extends AccessibilityService {

    private List<Integer> inputKeycodes = java.util.Collections.emptyList();

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        inputKeycodes = Config.load(this).inputKeyKeycodes;
    }

    static boolean isInputKeyCode(int keyCode, List<Integer> configuredKeycodes) {
        return configuredKeycodes != null && configuredKeycodes.contains(keyCode);
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        if (!isInputKeyCode(event.getKeyCode(), inputKeycodes)) return false;
        if (event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled()) {
            try {
                Intent openInputs = new Intent(this, MainActivity.class)
                        .putExtra(MainActivity.EXTRA_OPEN_INPUTS, true)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(openInputs);
            } catch (RuntimeException ignored) {
                // A remote shortcut must never crash the accessibility service.
            }
        }
        return true;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Which app is in front, for the azan: it stays quiet over Live TV and the HDMI inputs.
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || event.getPackageName() == null) return;
        String pkg = event.getPackageName().toString();
        if (pkg.equals("com.android.systemui") || pkg.startsWith("com.google.android.inputmethod")) return;
        front = pkg;
    }

    /** The app last brought to the front (as this service saw it); null before the first change. */
    private static volatile String front;
    /** Apps that show Live TV or an HDMI input on Android TV sets. */
    private static final java.util.Set<String> LIVE_TV = new java.util.HashSet<>(java.util.Arrays.asList(
            "com.android.tv", "com.google.android.tv", "com.mediatek.wwtv.tvcenter", "com.sony.dtv.tvx",
            "com.sony.dtv.tvinput", "com.realtek.tv", "com.tcl.tv", "com.xiaomi.mitv.tvplayer"));

    static String front() {
        return front;
    }

    static boolean liveTvInFront() {
        String f = front;
        return f != null && LIVE_TV.contains(f);
    }

    @Override
    public void onInterrupt() {
    }
}
