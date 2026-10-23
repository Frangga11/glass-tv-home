package com.gathin.tvhome;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

/**
 * Sends the remote's Home button to this launcher on remotes that emit KEYCODE_ALL_APPS for Home
 * (Xiaomi sticks and boxes). Android TV 9 routes that key only to the system-image launcher, so once
 * the stock launcher is disabled the button does nothing. Every other key passes through untouched.
 *
 * Enable over adb:
 *   settings put secure enabled_accessibility_services com.gathin.tvhome/.HomeKeyService
 *   settings put secure accessibility_enabled 1
 */
public class HomeKeyService extends AccessibilityService {

    private static final String TAG = "TvHome.HomeKey";
    private static HomeKeyService instance;
    private static final long DOUBLE_HOME_INTERVAL_MS = 400;

    private long lastHomePressTime = 0;
    private Runnable singleHomeRunnable = null;
    private final android.os.Handler homeHandler = new android.os.Handler(android.os.Looper.getMainLooper());

    /** Scancodes of remote buttons that should open Settings (config.xml, settings_key_scancodes). */
    private int[] settingsScancodes = new int[0];

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
        settingsScancodes = getResources().getIntArray(R.array.settings_key_scancodes);
        Log.i(TAG, "HomeKeyService connected");
    }

    @Override
    public void onDestroy() {
        if (instance == this) instance = null;
        super.onDestroy();
    }

    /** Put the TV display into sleep standby mode. */
    public static boolean sleepScreen() {
        if (instance != null) {
            Log.i(TAG, "Executing GLOBAL_ACTION_LOCK_SCREEN to turn off TV display");
            return instance.performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN);
        }
        return false;
    }

    private boolean isSettingsKey(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_SETTINGS) return true;
        for (int code : settingsScancodes) if (event.getScanCode() == code) return true;
        return false;
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.d(TAG, "onKeyEvent: code=" + event.getKeyCode() + " action=" + event.getAction());
        // The stock launcher handled the remote's Settings button; with it disabled the button does nothing.
        if (isSettingsKey(event)) {
            if (event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled()) {
                startActivity(new Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
            return true;
        }
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
            if (event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled()) {
                Intent recent = new Intent(this, RecentAppsActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(recent);
            }
            return true;
        }

        if (keyCode != KeyEvent.KEYCODE_ALL_APPS && keyCode != KeyEvent.KEYCODE_HOME) return false;
        if (event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled()) {
            long now = SystemClock.uptimeMillis();
            if (now - lastHomePressTime < DOUBLE_HOME_INTERVAL_MS) {
                lastHomePressTime = 0;
                if (singleHomeRunnable != null) {
                    homeHandler.removeCallbacks(singleHomeRunnable);
                    singleHomeRunnable = null;
                }
                Intent recent = new Intent(this, RecentAppsActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(recent);
                return true;
            } else {
                lastHomePressTime = now;
                singleHomeRunnable = () -> {
                    singleHomeRunnable = null;
                    Intent home = new Intent(Intent.ACTION_MAIN)
                            .addCategory(Intent.CATEGORY_HOME)
                            .setPackage(getPackageName())
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    try {
                        startActivity(home);
                    } catch (Exception ignored) {}
                };
                homeHandler.postDelayed(singleHomeRunnable, DOUBLE_HOME_INTERVAL_MS);
                return true;
            }
        }
        return true;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }
}
