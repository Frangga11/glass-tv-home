package com.gathin.tvhome;

import android.content.Context;
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
 * The existing glass_effects_enabled preference and opaque fallbacks remain authoritative.
 */
final class UniversalGlassMaterial {

    private UniversalGlassMaterial() { }

    static boolean isGlassEnabled(Context context) {
        return context.getSharedPreferences("wallpaper_prefs", Context.MODE_PRIVATE)
                .getBoolean("glass_effects_enabled", true);
    }

    /**
     * Shared Home header capsule material: Silver-Cyan rim and restrained Window Light
     * are supplied by UniversalGlassDrawable; existing Glider focus remains a separate overlay.
     * Used only for the navigation capsule and the selected Home information/action pills.
     */
    static Drawable homeCapsule(Context context, float density, float radiusDp,
                                int fallbackSurfaceColor, int fallbackBorderColor) {
        if (!isGlassEnabled(context)) {
            return solidSurface(density, radiusDp, fallbackSurfaceColor, fallbackBorderColor);
        }
        return new UniversalGlassDrawable(density, radiusDp, true);
    }

    /** A single neutral Universal Home surface, or GTVH's opaque fallback. */
    static Drawable surface(Context context, float density, float radiusDp,
                            int fallbackSurfaceColor, int fallbackBorderColor) {
        if (!isGlassEnabled(context)) {
            return solidSurface(density, radiusDp, fallbackSurfaceColor, fallbackBorderColor);
        }
        return new UniversalGlassDrawable(density, radiusDp);
    }

    /** The same dark shade + DockGlassDrawable layer order used by Universal Home GlassMenu. */
    static Drawable panel(Context context, float density, float radiusDp,
                          int fallbackSurfaceColor, int fallbackBorderColor) {
        if (!isGlassEnabled(context)) {
            return solidSurface(density, radiusDp, fallbackSurfaceColor, fallbackBorderColor);
        }
        // Popup shell uses the bright pearl-silver variant; no charcoal underlay.
        return new UniversalGlassDrawable(density, radiusDp, false, true);
    }

    private static GradientDrawable solidSurface(float density, float radiusDp,
                                                 int fillColor, int borderColor) {
        GradientDrawable solid = new GradientDrawable();
        solid.setCornerRadius(radiusDp * density);
        solid.setColor(fillColor);
        solid.setStroke(Math.max(1, Math.round(1.25f * density)), borderColor);
        return solid;
    }

    /** Idle control surface from Universal Home GlassControls.surface(), with GTVH radius retained. */
    static GradientDrawable controlIdle(Context context, float density, float radiusDp,
                                        int fallbackFill, int fallbackBorder) {
        if (!isGlassEnabled(context)) {
            GradientDrawable solid = new GradientDrawable();
            solid.setColor(fallbackFill);
            solid.setCornerRadius(radiusDp * density);
            solid.setStroke(Math.max(1, Math.round(density)), fallbackBorder);
            return solid;
        }
        GradientDrawable glass = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{0xCFEFF2F4, 0xA8DCE2E6});
        glass.setCornerRadius(radiusDp * density);
        glass.setStroke(Math.max(1, Math.round(density)), 0x99FFFFFF);
        return glass;
    }

    /**
     * Keep GTVH's focus listeners and disabled behavior. When glass is enabled, use Universal
     * GlassControls surfaces for idle/focused/pressed states; when disabled, restore GTVH's
     * original solid focus fills. Focus routing and animation remain owned by GTVH.
     */
    static Drawable controlStateBackground(Context context, float density, float radiusDp,
                                           int fallbackFill, int fallbackBorder) {
        StateListDrawable states = new StateListDrawable();

        GradientDrawable disabled = new GradientDrawable();
        disabled.setColor(TvUiTokens.POPUP_DISABLED_SURFACE);
        disabled.setCornerRadius(radiusDp * density);
        disabled.setStroke(Math.max(1, Math.round(density)), TvUiTokens.POPUP_DISABLED_BORDER);

        // Match Universal Home GlassControls when glass is enabled; retain GTVH focus fills otherwise.
        boolean glassEnabled = isGlassEnabled(context);
        GradientDrawable pressed;
        GradientDrawable focused;
        if (glassEnabled) {
            pressed = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{0xF0FFFFFF, 0xD2E0E6EA});
            pressed.setStroke(Math.max(1, Math.round(density)), 0xFF71838D);
            focused = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{0xEAFBFCFC, 0xC9DDE4E9});
            focused.setStroke(Math.max(1, Math.round(2f * density)), 0xFF657984);
        } else {
            pressed = new GradientDrawable();
            pressed.setColor(TvUiTokens.POPUP_CARD_SELECTED);
            pressed.setStroke(Math.max(1, Math.round(density)), TvUiTokens.POPUP_FOCUS_BORDER);
            focused = new GradientDrawable();
            focused.setColor(TvUiTokens.POPUP_CONTROL_FOCUSED);
            focused.setStroke(Math.max(1, Math.round(2f * density)), TvUiTokens.POPUP_FOCUS_BORDER);
        }
        pressed.setCornerRadius(radiusDp * density);
        focused.setCornerRadius(radiusDp * density);

        states.addState(new int[]{-android.R.attr.state_enabled}, disabled);
        states.addState(new int[]{android.R.attr.state_pressed}, pressed);
        states.addState(new int[]{android.R.attr.state_focused}, focused);
        states.addState(new int[]{}, controlIdle(context, density, radiusDp, fallbackFill, fallbackBorder));
        return states;
    }

    /** Render the Wi-Fi pointer with the same material as its shell, retaining a solid fallback. */
    static void drawPointer(Context context, Canvas canvas, Path triangle, Path edges,
                            RectF bounds, float density, int fallbackFill, int fallbackBorder) {
        if (!isGlassEnabled(context)) {
            Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
            fill.setColor(fallbackFill);
            canvas.drawPath(triangle, fill);
            Paint border = new Paint(Paint.ANTI_ALIAS_FLAG);
            border.setStyle(Paint.Style.STROKE);
            border.setStrokeWidth(Math.max(1f, 1.25f * density));
            border.setColor(fallbackBorder);
            canvas.drawPath(edges, border);
            return;
        }

        Paint shade = new Paint(Paint.ANTI_ALIAS_FLAG);
        shade.setColor(0xB8E4E9EC);
        canvas.drawPath(triangle, shade);

        int saved = canvas.save();
        canvas.clipPath(triangle);

        Paint surface = new Paint(Paint.ANTI_ALIAS_FLAG);
        surface.setShader(new LinearGradient(0f, bounds.top, 0f, bounds.bottom,
                new int[]{0xE6F9FAFB, 0xDCEFF2F4, 0xC9E2E6E9, 0xB8D2D8DD},
                new float[]{0f, 0.22f, 0.68f, 1f}, Shader.TileMode.CLAMP));
        canvas.drawRect(bounds, surface);

        Paint reflection = new Paint(Paint.ANTI_ALIAS_FLAG);
        reflection.setShader(new LinearGradient(bounds.left, bounds.top, bounds.right, bounds.bottom,
                new int[]{0x66FFFFFF, 0x22FFFFFF, 0x00FFFFFF},
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
                new int[]{0xF0FFFFFF, 0xBBDDE3E8, 0x667F8D96, 0xD9F4F6F7},
                new float[]{0f, 0.28f, 0.68f, 1f}, Shader.TileMode.CLAMP));
        canvas.drawPath(edges, rim);
    }
}
