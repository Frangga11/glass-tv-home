package com.gathin.tvhome;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Shared, high-contrast focus treatment for TV remote controls.
 *
 * Selection state and keyboard focus are deliberately separate: callers may keep their own
 * selected marker while this class adds a second, transient focus marker.
 */
final class TvFocusIndicator {
    private TvFocusIndicator() { }

    static View attachAvatarFocusRing(FrameLayout frame, int frameSizePx, float density) {
        if (frame == null) return null;

        View focusRing = new View(frame.getContext());
        GradientDrawable ring = new GradientDrawable();
        ring.setShape(GradientDrawable.OVAL);
        ring.setColor(Color.TRANSPARENT);
        ring.setStroke(Math.max(1, Math.round(2f * density)), 0xFF00E5FF);
        focusRing.setBackground(ring);
        focusRing.setVisibility(View.GONE);

        int inset = Math.max(2, Math.round(4f * density));
        int ringSize = Math.max(1, frameSizePx - 2 * inset);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ringSize, ringSize, Gravity.CENTER);
        frame.addView(focusRing, lp);

        frame.setOnFocusChangeListener((view, hasFocus) -> {
            focusRing.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            view.animate().cancel();
            view.animate().scaleX(hasFocus ? 1.10f : 1.0f)
                    .scaleY(hasFocus ? 1.10f : 1.0f)
                    .setDuration(120).start();
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
                    .setDuration(120).start();
        });
    }
}
