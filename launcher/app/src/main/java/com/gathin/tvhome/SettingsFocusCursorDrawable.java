package com.gathin.tvhome;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * The single DPAD cursor for the Settings navigation capsules.
 *
 * Geometry is locked to the owning capsule: the 1dp perimeter is inset by half its stroke,
 * and its corner radius is derived from the capsule radius. The raised dark/cyan treatment
 * is painted as inner visual layers, so it cannot enlarge, clip, or move the focus bounds.
 */
final class SettingsFocusCursorDrawable extends Drawable {
    private final float density;
    private final float radiusPx;
    private final RectF boundsShape = new RectF();
    private final RectF innerShape = new RectF();
    private final Paint surface = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint darkLift = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint cyanReflection = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final GlassRim silverCyanRim;
    private boolean focused;

    SettingsFocusCursorDrawable(float density, float radiusDp) {
        this.density = density;
        this.radiusPx = radiusDp * density;
        surface.setStyle(Paint.Style.FILL);
        darkLift.setStyle(Paint.Style.STROKE);
        cyanReflection.setStyle(Paint.Style.STROKE);
        silverCyanRim = new GlassRim(1f * density, 0xD9FFFFFF, 0x1AFFFFFF);
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    protected boolean onStateChange(int[] state) {
        boolean next = false;
        for (int value : state) {
            if (value == android.R.attr.state_focused) {
                next = true;
                break;
            }
        }
        if (focused == next) return false;
        focused = next;
        invalidateSelf();
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!focused) return;
        Rect b = getBounds();
        if (b.isEmpty()) return;

        // Exact capsule envelope; the outer edge of the 1dp stroke lands on its boundary.
        float halfStroke = 0.5f * density;
        boundsShape.set(b.left + halfStroke, b.top + halfStroke,
                b.right - halfStroke, b.bottom - halfStroke);
        float radius = Math.max(0f, radiusPx - halfStroke);
        canvas.save();

        // Dark raised body: one restrained fill, contained inside the capsule perimeter.
        surface.setColor(0x18202C35);
        canvas.drawRoundRect(boundsShape, radius, radius, surface);

        // A dark lower bevel and a cyan inner reflection form the raised focus treatment.
        // Both are inset, never painted beyond the measured capsule bounds.
        innerShape.set(boundsShape);
        innerShape.inset(1f * density, 1f * density);
        float innerRadius = Math.max(0f, radius - density);
        darkLift.setStrokeWidth(1.5f * density);
        darkLift.setColor(0xA807111A);
        darkLift.setShadowLayer(1.2f * density, 0f, 0.45f * density, 0x70000000);
        canvas.drawRoundRect(innerShape, innerRadius, innerRadius, darkLift);
        darkLift.clearShadowLayer();

        innerShape.set(boundsShape);
        innerShape.inset(1.35f * density, 1.35f * density);
        cyanReflection.setStrokeWidth(1f * density);
        cyanReflection.setColor(0x8067E8F9);
        cyanReflection.setShadowLayer(1f * density, 0f, 0f, 0x5038DDF4);
        canvas.drawRoundRect(innerShape, Math.max(0f, radius - 1.35f * density),
                Math.max(0f, radius - 1.35f * density), cyanReflection);
        cyanReflection.clearShadowLayer();

        // One and only one perimeter: 1dp silver/cyan gradient with the white window glint.
        silverCyanRim.fit(boundsShape);
        silverCyanRim.paint.setStrokeWidth(1f * density);
        silverCyanRim.paint.setAlpha(255);
        canvas.drawRoundRect(boundsShape, radius, radius, silverCyanRim.paint);
        silverCyanRim.drawWindowLight(canvas, boundsShape, radius, 0.82f);

        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        // Cursor opacity is governed by focused state; capsule geometry stays fixed.
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // The silver/cyan palette is intentionally fixed.
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
