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
import android.graphics.drawable.StateListDrawable;
import android.view.View;

/**
 * Universal Home's GlassMenu / GlassControls material recipes, adapted to existing GTVH bounds.
 * The existing glass_effects_enabled preference and opaque fallbacks remain authoritative.
 */
final class UniversalGlassMaterial {
    private static final Backdrop.Fill POINTER_BACKDROP = new Backdrop.Fill();
    private static final int PANEL_SHADE = 0x78151D25;

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

    /**
     * Clean, brighter frosted-silver shell dedicated to Quick Settings. Other popups keep
     * the established darker Universal panel material until separately accepted.
     */
    static Drawable quickSettingsPanel(Context context, View owner, float density, float radiusDp,
                                       int fallbackSurfaceColor, int fallbackBorderColor) {
        if (!isGlassEnabled(context)) {
            return solidSurface(density, radiusDp, fallbackSurfaceColor, fallbackBorderColor);
        }
        // Reuse the existing low-radius wallpaper blur, then add a defined slate-silver face.
        // The panel stays distinct from Home without changing the global backdrop pipeline.
        if (owner != null) Backdrop.watch(owner);
        return new UniversalGlassDrawable(owner, density, radiusDp, false, true, true);
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
    static Drawable panel(Context context, View owner, float density, float radiusDp,
                          int fallbackSurfaceColor, int fallbackBorderColor) {
        if (!isGlassEnabled(context)) {
            return solidSurface(density, radiusDp, fallbackSurfaceColor, fallbackBorderColor);
        }
        if (owner != null) Backdrop.watch(owner);
        return new UniversalGlassDrawable(owner, density, radiusDp, false, true);
    }

    private static GradientDrawable solidSurface(float density, float radiusDp,
                                                 int fillColor, int borderColor) {
        GradientDrawable solid = new GradientDrawable();
        solid.setCornerRadius(radiusDp * density);
        solid.setColor(fillColor);
        solid.setStroke(Math.max(1, Math.round(1.25f * density)), borderColor);
        return solid;
    }

    /**
     * Smoked-silver idle capsules for Quick Settings. The neutral edge stays static so
     * the existing DPAD Glider remains the only moving focus indicator.
     */
    static Drawable quickSettingsControlIdle(Context context, float density, float radiusDp,
                                               int fallbackFill, int fallbackBorder) {
        if (!isGlassEnabled(context)) {
            return solidSurface(density, radiusDp, fallbackFill, fallbackBorder);
        }
        // Keep the existing slate RGB values, reduce their opacity, then add one fine
        // top-edge reflection. The base remains visible through the shine: no colour swap.
        GradientDrawable glass = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{0x88343E48, 0x78303A44});
        glass.setCornerRadius(radiusDp * density);
        glass.setStroke(Math.max(1, Math.round(density)), 0x889EABB8);

        GradientDrawable specular = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{0x30FFFFFF, 0x00FFFFFF});
        specular.setCornerRadius(radiusDp * density);
        return new android.graphics.drawable.LayerDrawable(new Drawable[]{glass, specular});
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
                new int[]{0x16FFFFFF, 0x08FFFFFF});
        glass.setCornerRadius(radiusDp * density);
        glass.setStroke(Math.max(1, Math.round(density)), 0x28FFFFFF);
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
        disabled.setColor(TvUiTokens.DISABLED_SURFACE);
        disabled.setCornerRadius(radiusDp * density);
        disabled.setStroke(Math.max(1, Math.round(density)), TvUiTokens.DISABLED_BORDER);

        // Match Universal Home GlassControls when glass is enabled; retain GTVH focus fills otherwise.
        boolean glassEnabled = isGlassEnabled(context);
        GradientDrawable pressed;
        GradientDrawable focused;
        if (glassEnabled) {
            pressed = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{0x48FFFFFF, 0x22FFFFFF});
            pressed.setStroke(Math.max(1, Math.round(density)), 0xBBFFFFFF);
            focused = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{0x48FFFFFF, 0x22FFFFFF});
            focused.setStroke(Math.max(1, Math.round(density)), 0xBBFFFFFF);
        } else {
            pressed = new GradientDrawable();
            pressed.setColor(TvUiTokens.SURFACE_SELECTED);
            pressed.setStroke(Math.max(1, Math.round(density)), TvUiTokens.FOCUS_BORDER);
            focused = new GradientDrawable();
            focused.setColor(TvUiTokens.SURFACE_FOCUSED);
            focused.setStroke(Math.max(1, Math.round(2f * density)), TvUiTokens.FOCUS_BORDER);
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
    static void drawPointer(Context context, View owner, Canvas canvas, Path triangle, Path edges,
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

        if (owner != null) {
            POINTER_BACKDROP.draw(canvas, owner, bounds, Math.min(bounds.height() / 2f, 12f * density), 1f);
        }
        Paint shade = new Paint(Paint.ANTI_ALIAS_FLAG);
        shade.setColor(PANEL_SHADE);
        canvas.drawPath(triangle, shade);

        int saved = canvas.save();
        canvas.clipPath(triangle);

        Paint surface = new Paint(Paint.ANTI_ALIAS_FLAG);
        surface.setShader(new LinearGradient(0f, bounds.top, 0f, bounds.bottom,
                new int[]{0x28FFFFFF, 0x14FFFFFF, 0x0AFFFFFF, 0x1E111A22},
                new float[]{0f, 0.18f, 0.6f, 1f}, Shader.TileMode.CLAMP));
        canvas.drawRect(bounds, surface);

        Paint reflection = new Paint(Paint.ANTI_ALIAS_FLAG);
        reflection.setShader(new LinearGradient(bounds.left, bounds.top, bounds.right, bounds.bottom,
                new int[]{0x38FFFFFF, 0x14FFFFFF, 0x00FFFFFF},
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
                new int[]{0xDFFFFFFF, 0x55FFFFFF, 0x20FFFFFF, 0x99FFFFFF},
                new float[]{0f, 0.3f, 0.65f, 1f}, Shader.TileMode.CLAMP));
        canvas.drawPath(edges, rim);
    }
}
