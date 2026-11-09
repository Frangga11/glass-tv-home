package com.gathin.tvhome;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Shared high-contrast focus treatment for TV remote controls.
 *
 * The focus outline is a foreground layer: it never replaces the component background, so
 * selected/active state remains independently visible. Call updateFocusOutline from the component's
 * existing focus listener to preserve any component-specific state updates.
 */
final class TvFocusIndicator {
    private TvFocusIndicator() { }

    static View attachAvatarFocusRing(FrameLayout frame, int frameSizePx, float density) {
        if (frame == null) return null;

        View focusRing = new View(frame.getContext());
        GradientDrawable ring = new GradientDrawable();
        ring.setShape(GradientDrawable.OVAL);
        ring.setColor(Color.TRANSPARENT);
        ring.setStroke(Math.max(1, Math.round(TvUiTokens.FOCUS_STROKE_DP * density)),
                TvUiTokens.FOCUS_BORDER);
        focusRing.setBackground(ring);
        focusRing.setVisibility(View.GONE);

        int inset = Math.max(2, Math.round(4f * density));
        int ringSize = Math.max(1, frameSizePx - 2 * inset);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ringSize, ringSize, Gravity.CENTER);
        frame.addView(focusRing, lp);

        frame.setOnFocusChangeListener((view, hasFocus) -> {
            focusRing.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            view.animate().cancel();
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
        });
        return focusRing;
    }

    static void applyRoundedTextFocus(TextView view, float density, float radiusDp,
                                      int normalFill, int focusedFill,
                                      int normalStroke, int focusedStroke,
                                      int normalText, int focusedText,
                                      float focusedScale) {
        if (view == null) return;
        StateListDrawable background = new StateListDrawable();
        GradientDrawable disabled = new GradientDrawable();
        disabled.setColor(TvUiTokens.DISABLED_SURFACE);
        disabled.setCornerRadius(radiusDp * density);
        disabled.setStroke(Math.max(1, Math.round(density)), TvUiTokens.DISABLED_BORDER);
        GradientDrawable pressed = new GradientDrawable();
        pressed.setColor(TvUiTokens.SURFACE_SELECTED);
        pressed.setCornerRadius(radiusDp * density);
        pressed.setStroke(Math.max(1, Math.round(density)), focusedStroke);
        GradientDrawable focused = new GradientDrawable();
        focused.setColor(focusedFill);
        focused.setCornerRadius(radiusDp * density);
        focused.setStroke(Math.max(1, Math.round(2f * density)), focusedStroke);
        GradientDrawable normal = new GradientDrawable();
        normal.setColor(normalFill);
        normal.setCornerRadius(radiusDp * density);
        normal.setStroke(Math.max(1, Math.round(density)), normalStroke);
        background.addState(new int[]{-android.R.attr.state_enabled}, disabled);
        background.addState(new int[]{android.R.attr.state_pressed}, pressed);
        background.addState(new int[]{android.R.attr.state_focused}, focused);
        background.addState(new int[]{}, normal);
        view.setBackground(background);
        view.setTextColor(new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{}
                },
                new int[]{TvUiTokens.DISABLED_TEXT, focusedText, focusedText, normalText}
        ));
        // Keep focusedScale in the signature for existing callers; focus must not alter geometry.
        view.setOnFocusChangeListener((target, hasFocus) -> {
            target.animate().cancel();
            target.setScaleX(1.0f);
            target.setScaleY(1.0f);
        });
    }
}
