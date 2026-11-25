package com.gathin.tvhome;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/** DPAD-first live editor for the actual standby dashboard panel. */
final class StandbyWidgetEditor {
    private StandbyWidgetEditor() { }

    static void show(Activity activity, FrameLayout host, View widget, float density,
                     Runnable reapply) {
        if (activity == null || host == null || widget == null) return;
        View existing = host.findViewWithTag("standby_widget_editor");
        if (existing != null) host.removeView(existing);

        SharedPreferences prefs = activity.getSharedPreferences("standby_prefs", Activity.MODE_PRIVATE);
        final float startX = widget.getTranslationX();
        final float startY = widget.getTranslationY();
        final float step = Math.max(4f, 12f * density);

        FrameLayout overlay = new FrameLayout(activity);
        overlay.setTag("standby_widget_editor");
        overlay.setFocusable(true);
        overlay.setFocusableInTouchMode(true);
        overlay.setClickable(true);
        overlay.setBackgroundColor(0x12000000);

        TextView hint = new TextView(activity);
        hint.setText("ATUR POSISI WIDGET   •   Arahkan remote untuk menggeser");
        hint.setTextColor(Color.WHITE);
        hint.setTextSize(12f);
        hint.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        hint.setGravity(Gravity.CENTER);
        hint.setPadding(Math.round(18*density), Math.round(10*density),
                Math.round(18*density), Math.round(10*density));
        hint.setBackground(plate(density));
        FrameLayout.LayoutParams hintLp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        hintLp.topMargin = Math.round(22*density);
        overlay.addView(hint, hintLp);

        LinearLayout actions = new LinearLayout(activity);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setGravity(Gravity.CENTER);
        actions.setPadding(Math.round(8*density), Math.round(6*density),
                Math.round(8*density), Math.round(6*density));
        actions.setBackground(plate(density));
        String[] labels = {"Simpan", "Batal", "Reset"};
        TextView[] buttons = new TextView[3];
        for (int i = 0; i < labels.length; i++) {
            final int action = i;
            TextView button = new TextView(activity);
            buttons[i] = button;
            button.setText(labels[i]);
            button.setTextColor(Color.WHITE);
            button.setTextSize(12f);
            button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            button.setGravity(Gravity.CENTER);
            button.setMinWidth(Math.round(76*density));
            button.setMinHeight(Math.round(40*density));
            button.setPadding(Math.round(10*density), 0, Math.round(10*density), 0);
            button.setFocusable(true);
            button.setClickable(true);
            button.setId(View.generateViewId());
            button.setBackground(UniversalGlassMaterial.controlStateBackground(
                    activity, density, 9f, 0x332B3B4A, 0x66FFFFFF));
            button.setOnClickListener(v -> {
                if (action == 0) {
                    float maxX = Math.max(0f, host.getWidth() - widget.getWidth());
                    float maxY = Math.max(0f, host.getHeight() - widget.getHeight());
                    float x = maxX == 0f ? 0f : clamp(widget.getTranslationX() / maxX);
                    float y = maxY == 0f ? 0f : clamp(widget.getTranslationY() / maxY);
                    prefs.edit().putFloat("widget_x", x).putFloat("widget_y", y).commit();
                    host.removeView(overlay);
                    if (reapply != null) reapply.run();
                } else if (action == 1) {
                    widget.setTranslationX(startX);
                    widget.setTranslationY(startY);
                    host.removeView(overlay);
                } else {
                    float maxX = Math.max(0f, host.getWidth() - widget.getWidth());
                    float maxY = Math.max(0f, host.getHeight() - widget.getHeight());
                    widget.setTranslationX(Math.min(maxX, 42f*density));
                    widget.setTranslationY(maxY * 0.5f);
                }
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, Math.round(42*density));
            if (i > 0) lp.leftMargin = Math.round(6*density);
            actions.addView(button, lp);
        }
        FrameLayout.LayoutParams actionsLp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        actionsLp.bottomMargin = Math.round(24*density);
        overlay.addView(actions, actionsLp);
        host.addView(overlay, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        overlay.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN) return true;
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP
                    || keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                    || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                    || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (buttons[0].hasFocus() || buttons[1].hasFocus() || buttons[2].hasFocus()) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) overlay.requestFocus();
                    return true;
                }
                float maxX = Math.max(0f, host.getWidth() - widget.getWidth());
                float maxY = Math.max(0f, host.getHeight() - widget.getHeight());
                float x = widget.getTranslationX();
                float y = widget.getTranslationY();
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) x -= step;
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) x += step;
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) y -= step;
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) y += step;
                widget.setTranslationX(Math.max(0f, Math.min(maxX, x)));
                widget.setTranslationY(Math.max(0f, Math.min(maxY, y)));
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
                widget.setTranslationX(startX);
                widget.setTranslationY(startY);
                host.removeView(overlay);
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                buttons[0].requestFocus();
                return true;
            }
            return true;
        });
        overlay.requestFocus();
    }

    private static float clamp(float value) {
        return Math.max(0f, Math.min(1f, value));
    }

    private static GradientDrawable plate(float density) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(0xD91B2733);
        drawable.setCornerRadius(12f*density);
        drawable.setStroke(Math.max(1, Math.round(density)), 0x99FFFFFF);
        return drawable;
    }
}
