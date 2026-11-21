package com.gathin.tvhome;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Universal Home's GlassMenu / GlassControls material recipes, adapted to existing GTVH bounds.
 * Copyright and MIT license notice are recorded in THIRD_PARTY_NOTICES.md.
 */
final class UniversalGlassMaterial {
    private static final int PANEL_SHADE = 0xB31D1D1D;

    private UniversalGlassMaterial() { }

    /** The same dark shade + DockGlassDrawable layer order used by Universal Home GlassMenu. */
    static Drawable panel(float density, float radiusDp) {
        GradientDrawable shade = new GradientDrawable();
        shade.setCornerRadius(radiusDp * density);
        shade.setColor(PANEL_SHADE);
        return new LayerDrawable(new Drawable[]{
                shade, new UniversalGlassDrawable(density, radiusDp)
        });
    }

    /** Idle control surface from Universal Home GlassControls.surface(), with GTVH radius retained. */
    static GradientDrawable controlIdle(float density, float radiusDp) {
        GradientDrawable glass = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{0x16FFFFFF, 0x08FFFFFF});
        glass.setCornerRadius(radiusDp * density);
        glass.setStroke(Math.max(1, Math.round(density)), 0x28FFFFFF);
        return glass;
    }

    /**
     * Preserve GTVH's existing disabled/pressed/focused states and use Universal's glass only
     * for the idle state. This prevents the material adoption from replacing focus behavior.
     */
    static Drawable controlStateBackground(float density, float radiusDp) {
        StateListDrawable states = new StateListDrawable();

        GradientDrawable disabled = new GradientDrawable();
        disabled.setColor(TvUiTokens.DISABLED_SURFACE);
        disabled.setCornerRadius(radiusDp * density);
        disabled.setStroke(Math.max(1, Math.round(density)), TvUiTokens.DISABLED_BORDER);

        GradientDrawable pressed = new GradientDrawable();
        pressed.setColor(TvUiTokens.SURFACE_SELECTED);
        pressed.setCornerRadius(radiusDp * density);
        pressed.setStroke(Math.max(1, Math.round(density)), TvUiTokens.FOCUS_BORDER);

        GradientDrawable focused = new GradientDrawable();
        focused.setColor(TvUiTokens.SURFACE_FOCUSED);
        focused.setCornerRadius(radiusDp * density);
        focused.setStroke(Math.max(1, Math.round(2f * density)), TvUiTokens.FOCUS_BORDER);

        states.addState(new int[]{-android.R.attr.state_enabled}, disabled);
        states.addState(new int[]{android.R.attr.state_pressed}, pressed);
        states.addState(new int[]{android.R.attr.state_focused}, focused);
        states.addState(new int[]{}, controlIdle(density, radiusDp));
        return states;
    }

    /** Render the Wi-Fi pointer using the same shade, surface, sweep and rim as the popup shell. */
    static void drawPointer(Canvas canvas, Path triangle, Path edges, RectF bounds, float density) {
        Paint shade = new Paint(Paint.ANTI_ALIAS_FLAG);
        shade.setColor(PANEL_SHADE);
        canvas.drawPath(triangle, shade);

        int saved = canvas.save();
        canvas.clipPath(triangle);

        Paint surface = new Paint(Paint.ANTI_ALIAS_FLAG);
        surface.setShader(new LinearGradient(0f, bounds.top, 0f, bounds.bottom,
                new int[]{0x36FFFFFF, 0x16FFFFFF, 0x0DFFFFFF, 0x30232323},
                new float[]{0f, 0.18f, 0.6f, 1f}, Shader.TileMode.CLAMP));
        canvas.drawRect(bounds, surface);

        Paint reflection = new Paint(Paint.ANTI_ALIAS_FLAG);
        reflection.setShader(new LinearGradient(bounds.left, bounds.top, bounds.right, bounds.bottom,
                new int[]{0x22FFFFFF, 0x08FFFFFF, 0x00FFFFFF},
                new float[]{0f, 0.35f, 1f}, Shader.TileMode.CLAMP));
        Path sweep = new Path();
        sweep.moveTo(bounds.left, bounds.top);
        sweep.lineTo(bounds.right, bounds.top);
        sweep.cubicTo(bounds.right - bounds.width() * 0.12f, bounds.top + bounds.height() * 0.23f,
                bounds.left + bounds.width() * 0.25f, bounds.top + bounds.height() * 0.06f,
                bounds.left, bounds.top + bounds.height() * 0.44f);
        sweep.close();
        canvas.drawPath(sweep, reflection);
        canvas.restoreToCount(saved);

        Paint rim = new Paint(Paint.ANTI_ALIAS_FLAG);
        rim.setStyle(Paint.Style.STROKE);
        rim.setStrokeWidth(density);
        rim.setShader(new LinearGradient(bounds.left, bounds.top, bounds.right, bounds.bottom,
                new int[]{0xA6FFFFFF, 0x38FFFFFF, 0x14FFFFFF, 0x66FFFFFF},
                new float[]{0f, 0.3f, 0.65f, 1f}, Shader.TileMode.CLAMP));
        canvas.drawPath(edges, rim);
    }
}
