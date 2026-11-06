package com.gathin.tvhome;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
    private static final class ForegroundState {
        final Drawable original;
        final Drawable focused;
        ForegroundState(Drawable original, Drawable focused) {
            this.original = original;
            this.focused = focused;
        }
    }

    private TvFocusIndicator() { }

    static void updateFocusOutline(View view, boolean hasFocus, float density, float radiusDp) {
        if (view == null) return;
        Object tagged = view.getTag(R.id.tv_focus_outline_state);
        ForegroundState state;
        if (tagged instanceof ForegroundState) {
            state = (ForegroundState) tagged;
        } else {
            float safeDensity = density > 0f && !Float.isInfinite(density) && !Float.isNaN(density)
                    ? density : 1f;
            GradientDrawable outline = new GradientDrawable();
            outline.setShape(GradientDrawable.RECTANGLE);
            outline.setColor(Color.TRANSPARENT);
            outline.setCornerRadius(Math.max(0f, radiusDp) * safeDensity);
            outline.setStroke(Math.max(1, Math.round(TvUiTokens.FOCUS_STROKE_DP * safeDensity)),
                    TvUiTokens.FOCUS_BORDER);
            state = new ForegroundState(view.getForeground(), outline);
            view.setTag(R.id.tv_focus_outline_state, state);
        }
        view.setForeground(hasFocus ? state.focused : state.original);
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
            view.animate().scaleX(hasFocus ? TvUiTokens.CARD_FOCUS_SCALE : 1.0f)
                    .scaleY(hasFocus ? TvUiTokens.CARD_FOCUS_SCALE : 1.0f)
                    .setDuration(TvUiTokens.FOCUS_ANIMATION_MS).start();
        });
        return focusRing;
    }

    static void applyRoundedTextFocus(TextView view, float density, float radiusDp,
                                      int normalFill, int focusedFill,
                                      int normalStroke, int focusedStroke,
                                      int normalText, int focusedText,
                                      float focusedScale) {
        if (view == null) return;
        view.setOnFocusChangeListener((target, hasFocus) -> {
            GradientDrawable background = new GradientDrawable();
            background.setColor(hasFocus ? focusedFill : normalFill);
            background.setCornerRadius(radiusDp * density);
            background.setStroke(
                    Math.max(1, Math.round((hasFocus ? 2f : 1f) * density)),
                    hasFocus ? focusedStroke : normalStroke);
            view.setBackground(background);
            view.setTextColor(hasFocus ? focusedText : normalText);
            target.animate().cancel();
            target.animate().scaleX(hasFocus ? focusedScale : 1.0f)
                    .scaleY(hasFocus ? focusedScale : 1.0f)
                    .setDuration(TvUiTokens.FOCUS_ANIMATION_MS).start();
        });
    }
}
