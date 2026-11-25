package com.gathin.tvhome;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/** DPAD-first live editor shown in its own topmost window above Settings. */
final class StandbyWidgetEditor {
    private StandbyWidgetEditor() { }

    static void show(Activity activity, FrameLayout host, View widget, float density,
                     Runnable reapply) {
        if (activity == null || host == null || widget == null) return;
        ViewGroup originalParent = host.getParent() instanceof ViewGroup
                ? (ViewGroup) host.getParent() : null;
        if (originalParent == null) return;
        final int originalIndex = originalParent.indexOfChild(host);
        final ViewGroup.LayoutParams originalParams = host.getLayoutParams();
        final float startX = widget.getTranslationX();
        final float startY = widget.getTranslationY();
        final float step = Math.max(4f, 12f * density);
        final SharedPreferences prefs = activity.getSharedPreferences(
                "standby_prefs", Activity.MODE_PRIVATE);

        Dialog windowDialog = new Dialog(activity);
        FrameLayout windowRoot = new FrameLayout(activity);
        windowRoot.setFocusable(true);
        windowRoot.setFocusableInTouchMode(true);
        windowRoot.setBackgroundColor(Color.TRANSPARENT);

        // Move the real standby view into a new application window. This places the
        // preview above the Settings Dialog without cloning or altering dashboard data.
        originalParent.removeView(host);
        windowRoot.addView(host, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        windowDialog.setContentView(windowRoot);
        windowDialog.setCancelable(false);
        windowDialog.setCanceledOnTouchOutside(false);
        windowDialog.setOnDismissListener(d -> {
            if (host.getParent() instanceof ViewGroup) {
                ((ViewGroup) host.getParent()).removeView(host);
            }
            if (host.getParent() == null && originalParent.getChildCount() >= 0) {
                int index = Math.max(0, Math.min(originalIndex, originalParent.getChildCount()));
                originalParent.addView(host, index, originalParams);
            }
        });
        windowDialog.show();
        Window window = windowDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
        }

        FrameLayout overlay = new FrameLayout(activity);
        overlay.setTag("standby_widget_editor");
        overlay.setFocusable(true);
        overlay.setFocusableInTouchMode(true);
        overlay.setClickable(true);
        overlay.setBackgroundColor(0x12000000);

        TextView hint = new TextView(activity);
        hint.setText("ATUR POSISI WIDGET  •  ARAH = GESER  •  OK = SELESAI  •  BACK = BATAL");
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
        final boolean[] finished = {false};
        Runnable restoreAndClose = () -> {
            if (finished[0]) return;
            finished[0] = true;
            widget.setTranslationX(startX);
            widget.setTranslationY(startY);
            windowDialog.dismiss();
        };
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
            button.setMinHeight(Math.round(42*density));
            button.setPadding(Math.round(10*density), 0, Math.round(10*density), 0);
            button.setFocusable(true);
            button.setClickable(true);
            button.setId(View.generateViewId());
            button.setBackground(UniversalGlassMaterial.controlStateBackground(
                    activity, density, 9f, 0x332B3B4A, 0x66FFFFFF));
            button.setOnClickListener(v -> {
                if (finished[0]) return;
                if (action == 0) {
                    float maxX = Math.max(0f, host.getWidth() - widget.getWidth());
                    float maxY = Math.max(0f, host.getHeight() - widget.getHeight());
                    float x = maxX == 0f ? 0f : clamp(widget.getTranslationX() / maxX);
                    float y = maxY == 0f ? 0f : clamp(widget.getTranslationY() / maxY);
                    if (!prefs.edit().putFloat("widget_x", x).putFloat("widget_y", y).commit()) return;
                    finished[0] = true;
                    windowDialog.dismiss();
                    if (reapply != null) reapply.run();
                } else if (action == 1) {
                    restoreAndClose.run();
                } else {
                    float maxX = Math.max(0f, host.getWidth() - widget.getWidth());
                    float maxY = Math.max(0f, host.getHeight() - widget.getHeight());
                    widget.setTranslationX(Math.min(maxX, 42f*density));
                    widget.setTranslationY(maxY * 0.5f);
                    overlay.requestFocus();
                }
            });
            button.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() != KeyEvent.ACTION_DOWN) return true;
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
                    restoreAndClose.run();
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    overlay.requestFocus();
                    return true;
                }
                return false;
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
            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
                restoreAndClose.run();
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP
                    || keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                    || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                    || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
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
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                buttons[0].requestFocus();
                return true;
            }
            return true;
        });
        windowRoot.post(overlay::requestFocus);
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
