package com.gathin.tvhome;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Universal Home's DockGlassDrawable material, ported under its MIT license.
 * Focus is intentionally not implemented here: Glass TV Home keeps its existing Glider.
 */
final class UniversalGlassDrawable extends Drawable {
    private final float density;
    private final float radiusDp;
    private final Paint surface = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint rim = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint reflection = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint inner = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF area = new RectF();
    private final RectF lastSourceBounds = new RectF();
    private final RectF innerArea = new RectF();
    private final Path clip = new Path();
    private final Path sweep = new Path();
    private float radius;
    private boolean prepared;

    UniversalGlassDrawable(float density, float radiusDp) {
        this.density = density;
        this.radiusDp = radiusDp;
        rim.setStyle(Paint.Style.STROKE);
        rim.setStrokeWidth(density);
        inner.setStyle(Paint.Style.STROKE);
        inner.setStrokeWidth(density * 0.7f);
    }

    private void prepare(RectF sourceBounds) {
        if (prepared && lastSourceBounds.equals(sourceBounds)) return;
        lastSourceBounds.set(sourceBounds);
        area.set(sourceBounds);
        area.inset(density, density);
        prepared = true;
        if (area.width() <= 0f || area.height() <= 0f) {
            clip.reset();
            sweep.reset();
            return;
        }
        radius = Math.min(radiusDp * density, area.height() / 2f);
        surface.setShader(new LinearGradient(0f, area.top, 0f, area.bottom,
                new int[]{0x36FFFFFF, 0x16FFFFFF, 0x0DFFFFFF, 0x30232323},
                new float[]{0f, 0.18f, 0.6f, 1f}, Shader.TileMode.CLAMP));
        rim.setShader(new LinearGradient(area.left, area.top, area.right, area.bottom,
                new int[]{0xA6FFFFFF, 0x38FFFFFF, 0x14FFFFFF, 0x66FFFFFF},
                new float[]{0f, 0.3f, 0.65f, 1f}, Shader.TileMode.CLAMP));
        reflection.setShader(new LinearGradient(area.left, area.top, area.right, area.bottom,
                new int[]{0x22FFFFFF, 0x08FFFFFF, 0x00FFFFFF},
                new float[]{0f, 0.35f, 1f}, Shader.TileMode.CLAMP));
        inner.setShader(new LinearGradient(0f, area.top, 0f, area.bottom,
                new int[]{0x30FFFFFF, 0x00FFFFFF, 0x20FFFFFF}, null, Shader.TileMode.CLAMP));
        clip.reset();
        clip.addRoundRect(area, radius, radius, Path.Direction.CW);
        sweep.reset();
        sweep.moveTo(area.left, area.top);
        sweep.lineTo(area.right, area.top);
        sweep.cubicTo(area.right - area.width() * 0.12f, area.top + area.height() * 0.23f,
                area.left + area.width() * 0.25f, area.top + area.height() * 0.06f,
                area.left, area.top + area.height() * 0.44f);
        sweep.close();
    }

    void drawSurface(Canvas canvas, RectF sourceBounds) {
        prepare(sourceBounds);
        if (!area.isEmpty()) canvas.drawRoundRect(area, radius, radius, surface);
    }

    void drawReflection(Canvas canvas, RectF sourceBounds) {
        prepare(sourceBounds);
        if (area.isEmpty()) return;
        int saved = canvas.save();
        canvas.clipPath(clip);
        canvas.drawPath(sweep, reflection);
        canvas.restoreToCount(saved);
    }

    void drawRim(Canvas canvas, RectF sourceBounds) {
        prepare(sourceBounds);
        if (area.isEmpty()) return;
        canvas.drawRoundRect(area, radius, radius, rim);
        innerArea.set(area);
        innerArea.inset(density * 2f, density * 2f);
        canvas.drawRoundRect(innerArea,
                Math.max(0f, radius - density * 2f),
                Math.max(0f, radius - density * 2f), inner);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        prepare(new RectF(bounds));
    }

    @Override
    public void getOutline(android.graphics.Outline outline) {
        if (area.isEmpty()) {
            outline.setEmpty();
            return;
        }
        outline.setRoundRect((int) Math.ceil(area.left), (int) Math.ceil(area.top),
                (int) Math.floor(area.right), (int) Math.floor(area.bottom), radius);
        outline.setAlpha(1f);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        RectF sourceBounds = new RectF(bounds);
        drawSurface(canvas, sourceBounds);
        drawReflection(canvas, sourceBounds);
        drawRim(canvas, sourceBounds);
    }

    @Override
    public void setAlpha(int alpha) {
        surface.setAlpha(alpha);
        rim.setAlpha(alpha);
        reflection.setAlpha(alpha);
        inner.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter filter) {
        surface.setColorFilter(filter);
        rim.setColorFilter(filter);
        reflection.setColorFilter(filter);
        inner.setColorFilter(filter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
