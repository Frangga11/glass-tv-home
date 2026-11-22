package com.gathin.tvhome;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Independent DPAD focus overlay for the Settings sidebar capsules.
 *
 * The outline is drawn inside the owner's exact bounds and uses the capsule's own corner radius.
 * The cool glow and raised shadow are visual-only layers; they never affect measurement or focus
 * navigation, and the one-pixel silver/cyan edge remains a distinct 1dp stroke.
 */
final class SettingsNavFocusDrawable extends Drawable {
    private final float density;
    private final float radiusPx;
    private final RectF shape = new RectF();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean focused;

    SettingsNavFocusDrawable(float density, float radiusDp) {
        this.density = density;
        this.radiusPx = radiusDp * density;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    protected boolean onStateChange(int[] state) {
        boolean next = false;
        for (int value : state) {
            if (value == android.R.attr.state_focused
                    || value == android.R.attr.state_pressed) {
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

        android.graphics.Rect bounds = getBounds();
        if (bounds.isEmpty()) return;

        // Keep the outline's outer edge pixel-aligned with the capsule's measured bounds.
        float half = 0.5f * density;
        shape.set(bounds.left + half, bounds.top + half,
                bounds.right - half, bounds.bottom - half);
        float radius = Math.max(0f, radiusPx - half);
        canvas.save();

        // Soft dark lift under the cursor, restrained so the capsule remains legible.
        paint.setStrokeWidth(3.5f * density);
        paint.setColor(0x8A07131B);
        paint.setShadowLayer(3.5f * density, 0f, 1.25f * density, 0xA8000000);
        canvas.drawRoundRect(shape, radius, radius, paint);
        paint.clearShadowLayer();

        // Cool cyan reflection around the silver edge, kept as a separate soft visual layer.
        paint.setStrokeWidth(2.5f * density);
        paint.setColor(0x5038DDF4);
        paint.setShadowLayer(2.5f * density, 0f, 0f, 0x6038DDF4);
        canvas.drawRoundRect(shape, radius, radius, paint);
        paint.clearShadowLayer();

        // Crisp 1dp white-silver edge with a restrained cyan tint.
        paint.setStrokeWidth(1f * density);
        paint.setColor(0xF0EAFBFF);
        canvas.drawRoundRect(shape, radius, radius, paint);

        // Fine inner cyan reflection gives the outline its raised, glass-like finish.
        shape.inset(1f * density, 1f * density);
        paint.setStrokeWidth(Math.max(0.5f, 0.5f * density));
        paint.setColor(0x704ADDF2);
        canvas.drawRoundRect(shape, Math.max(0f, radius - density),
                Math.max(0f, radius - density), paint);

        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        // Focus strength is defined by the restrained palette above.
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // The focus palette is intentionally fixed to the shared silver/cyan system.
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
