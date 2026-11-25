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

/** Single-owner DPAD state machine for the live standby widget editor. */
final class StandbyWidgetEditor {
    private StandbyWidgetEditor() { }

    static void show(Activity activity, FrameLayout host, View widget, float density,
                     Runnable reapply, Runnable onClose) {
        if (activity == null || host == null || widget == null
                || !(host.getParent() instanceof ViewGroup)) return;

        final ViewGroup originalParent = (ViewGroup) host.getParent();
        final int originalIndex = originalParent.indexOfChild(host);
        final ViewGroup.LayoutParams originalParams = host.getLayoutParams();
        final float startX = widget.getTranslationX();
        final float startY = widget.getTranslationY();
        final float step = Math.max(4f, 12f * density);
        final SharedPreferences prefs = activity.getSharedPreferences(
                "standby_prefs", Activity.MODE_PRIVATE);

        final boolean[] finished = {false};
        final int[] mode = {0}; // 0 = move widget, 1 = choose action
        final int[] selectedAction = {0};
        final TextView[] actionViews = new TextView[3];
        final Runnable[] cancel = {null};
        final Runnable[] perform = {null};

        Dialog dialog = new Dialog(activity) {
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (finished[0]) return true;
                if (event.getAction() != KeyEvent.ACTION_DOWN) return true;
                int key = event.getKeyCode();
                if (key == KeyEvent.KEYCODE_BACK || key == KeyEvent.KEYCODE_ESCAPE) {
                    if (cancel[0] != null) cancel[0].run();
                    return true;
                }
                if (mode[0] == 0) {
                    float maxX = Math.max(0f, host.getWidth() - widget.getWidth());
                    float maxY = Math.max(0f, host.getHeight() - widget.getHeight());
                    float x = widget.getTranslationX();
                    float y = widget.getTranslationY();
                    if (key == KeyEvent.KEYCODE_DPAD_LEFT) x -= step;
                    else if (key == KeyEvent.KEYCODE_DPAD_RIGHT) x += step;
                    else if (key == KeyEvent.KEYCODE_DPAD_UP) y -= step;
                    else if (key == KeyEvent.KEYCODE_DPAD_DOWN) y += step;
                    else if (key == KeyEvent.KEYCODE_DPAD_CENTER || key == KeyEvent.KEYCODE_ENTER) {
                        mode[0] = 1;
                        renderActionSelection(actionViews, selectedAction[0], density);
                    }
                    widget.setTranslationX(Math.max(0f, Math.min(maxX, x)));
                    widget.setTranslationY(Math.max(0f, Math.min(maxY, y)));
                    return true;
                }
                if (key == KeyEvent.KEYCODE_DPAD_LEFT || key == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    int delta = key == KeyEvent.KEYCODE_DPAD_LEFT ? -1 : 1;
                    selectedAction[0] = (selectedAction[0] + delta + actionViews.length)
                            % actionViews.length;
                    renderActionSelection(actionViews, selectedAction[0], density);
                } else if (key == KeyEvent.KEYCODE_DPAD_UP || key == KeyEvent.KEYCODE_DPAD_DOWN) {
                    mode[0] = 0;
                    renderActionSelection(actionViews, -1, density);
                } else if (key == KeyEvent.KEYCODE_DPAD_CENTER || key == KeyEvent.KEYCODE_ENTER) {
                    if (perform[0] != null) perform[0].run();
                }
                return true;
            }
        };

        FrameLayout windowRoot = new FrameLayout(activity);
        windowRoot.setFocusable(true);
        windowRoot.setFocusableInTouchMode(true);
        windowRoot.setBackgroundColor(Color.TRANSPARENT);
        dialog.setContentView(windowRoot);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        FrameLayout overlay = new FrameLayout(activity);
        overlay.setTag("standby_widget_editor");
        overlay.setFocusable(false);
        overlay.setClickable(false);
        overlay.setBackgroundColor(0x12000000);

        TextView hint = new TextView(activity);
        hint.setText("ATUR POSISI WIDGET  •  ARAH = GESER  •  OK = PILIH  •  BACK = BATAL");
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
        for (int i = 0; i < labels.length; i++) {
            TextView button = new TextView(activity);
            actionViews[i] = button;
            button.setText(labels[i]);
            button.setTextColor(Color.WHITE);
            button.setTextSize(12f);
            button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            button.setGravity(Gravity.CENTER);
            button.setMinWidth(Math.round(76*density));
            button.setMinHeight(Math.round(42*density));
            button.setPadding(Math.round(10*density), 0, Math.round(10*density), 0);
            button.setFocusable(false);
            button.setClickable(false);
            button.setBackground(UniversalGlassMaterial.controlStateBackground(
                    activity, density, 9f, 0x332B3B4A, 0x66FFFFFF));
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

        cancel[0] = () -> {
            if (finished[0]) return;
            finished[0] = true;
            widget.setTranslationX(startX);
            widget.setTranslationY(startY);
            dialog.dismiss();
        };
        perform[0] = () -> {
            if (finished[0]) return;
            if (selectedAction[0] == 0) {
                float maxX = Math.max(0f, host.getWidth() - widget.getWidth());
                float maxY = Math.max(0f, host.getHeight() - widget.getHeight());
                float x = maxX <= 0f ? 0f : clamp(widget.getTranslationX() / maxX);
                float y = maxY <= 0f ? 0f : clamp(widget.getTranslationY() / maxY);
                if (!prefs.edit().putFloat("widget_x", x).putFloat("widget_y", y).commit()) return;
                finished[0] = true;
                dialog.dismiss();
                if (reapply != null) reapply.run();
            } else if (selectedAction[0] == 1) {
                cancel[0].run();
            } else {
                float maxX = Math.max(0f, host.getWidth() - widget.getWidth());
                float maxY = Math.max(0f, host.getHeight() - widget.getHeight());
                float defaultX = Math.min(maxX, 42f * density);
                widget.setTranslationX(defaultX);
                widget.setTranslationY(maxY * 0.5f);
                mode[0] = 0;
                renderActionSelection(actionViews, -1, density);
            }
        };

        dialog.setOnDismissListener(ignored -> {
            if (overlay.getParent() instanceof ViewGroup) {
                ((ViewGroup) overlay.getParent()).removeView(overlay);
            }
            if (host.getParent() instanceof ViewGroup) {
                ((ViewGroup) host.getParent()).removeView(host);
            }
            int index = Math.max(0, Math.min(originalIndex, originalParent.getChildCount()));
            originalParent.addView(host, index, originalParams);
            if (onClose != null) onClose.run();
        });

        // Reparent the actual standby view, preserving its photo, data bindings and child state.
        originalParent.removeView(host);
        windowRoot.addView(host, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        host.addView(overlay, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
        }
        windowRoot.post(windowRoot::requestFocus);
    }

    private static void renderActionSelection(TextView[] actions, int selected, float density) {
        for (int i = 0; i < actions.length; i++) {
            TextView action = actions[i];
            if (action == null) continue;
            boolean active = i == selected;
            action.setTextColor(active ? 0xFF071827 : Color.WHITE);
            action.setBackground(UniversalGlassMaterial.controlStateBackground(
                    action.getContext(), density, 9f,
                    active ? 0xD9BDEEFF : 0x332B3B4A,
                    active ? 0xFFFFFFFF : 0x66FFFFFF));
            action.setElevation(active ? 4f * density : 0f);
        }
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
