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

    static StateListDrawable createRoundedStateBackground(float density, float radiusDp,
            int normalFill, int focusedFill, int normalStroke, int focusedStroke) {
        return createStateBackground(density, radiusDp, GradientDrawable.RECTANGLE,
                normalFill, focusedFill, normalStroke, focusedStroke);
    }

    static StateListDrawable createOvalStateBackground(float density, int normalFill,
            int focusedFill, int normalStroke, int focusedStroke) {
        return createStateBackground(density, 0f, GradientDrawable.OVAL,
                normalFill, focusedFill, normalStroke, focusedStroke);
    }

    static StateListDrawable createSelectableRoundedStateBackground(float density, float radiusDp,
            int normalFill, int selectedFill, int focusedFill,
            int normalStroke, int selectedStroke, int focusedStroke) {
        StateListDrawable states = new StateListDrawable();
        GradientDrawable disabled = rounded(density, radiusDp, TvUiTokens.DISABLED_SURFACE,
                TvUiTokens.DISABLED_BORDER, 1f);
        GradientDrawable pressed = rounded(density, radiusDp, TvUiTokens.SURFACE_SELECTED,
                TvUiTokens.FOCUS_BORDER, 1f);
        GradientDrawable focused = rounded(density, radiusDp, focusedFill, focusedStroke, 2f);
        GradientDrawable selected = rounded(density, radiusDp, selectedFill, selectedStroke, 1f);
        GradientDrawable normal = rounded(density, radiusDp, normalFill, normalStroke, 1f);
        states.addState(new int[]{-android.R.attr.state_enabled}, disabled);
        states.addState(new int[]{android.R.attr.state_pressed}, pressed);
        states.addState(new int[]{android.R.attr.state_focused}, focused);
        states.addState(new int[]{android.R.attr.state_selected}, selected);
        states.addState(new int[]{}, normal);
        return states;
    }

    private static GradientDrawable rounded(float density, float radiusDp, int fill, int stroke, float strokeDp) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(fill);
        drawable.setCornerRadius(radiusDp * density);
        drawable.setStroke(Math.max(1, Math.round(strokeDp * density)), stroke);
        return drawable;
    }

    private static StateListDrawable createStateBackground(float density, float radiusDp, int shape,
            int normalFill, int focusedFill, int normalStroke, int focusedStroke) {
        StateListDrawable states = new StateListDrawable();
        GradientDrawable disabled = new GradientDrawable();
        disabled.setShape(shape);
        if (shape == GradientDrawable.RECTANGLE) disabled.setCornerRadius(radiusDp * density);
        disabled.setColor(TvUiTokens.DISABLED_SURFACE);
        disabled.setStroke(Math.max(1, Math.round(density)), TvUiTokens.DISABLED_BORDER);

        GradientDrawable pressed = new GradientDrawable();
        pressed.setShape(shape);
        if (shape == GradientDrawable.RECTANGLE) pressed.setCornerRadius(radiusDp * density);
        pressed.setColor(TvUiTokens.SURFACE_SELECTED);
        pressed.setStroke(Math.max(1, Math.round(density)), TvUiTokens.FOCUS_BORDER);

        GradientDrawable focused = new GradientDrawable();
        focused.setShape(shape);
        if (shape == GradientDrawable.RECTANGLE) focused.setCornerRadius(radiusDp * density);
        focused.setColor(focusedFill);
        focused.setStroke(Math.max(1, Math.round(2f * density)), focusedStroke);

        GradientDrawable normal = new GradientDrawable();
        normal.setShape(shape);
        if (shape == GradientDrawable.RECTANGLE) normal.setCornerRadius(radiusDp * density);
        normal.setColor(normalFill);
        normal.setStroke(Math.max(1, Math.round(density)), normalStroke);

        states.addState(new int[]{-android.R.attr.state_enabled}, disabled);
        states.addState(new int[]{android.R.attr.state_pressed}, pressed);
        states.addState(new int[]{android.R.attr.state_focused}, focused);
        states.addState(new int[]{}, normal);
        return states;
    }

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
