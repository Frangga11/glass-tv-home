package com.gathin.tvhome;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
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
    private final Paint windowLight = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Matrix rimMatrix = new Matrix();
    private final Matrix windowLightMatrix = new Matrix();
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
        windowLight.setStyle(Paint.Style.STROKE);
        windowLight.setStrokeWidth(Math.max(1f, density * 0.9f));
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
        // Restore the original Frosted Silver-Cyan edge identity while retaining
        // Universal Home's 1dp outer rim geometry and current draw order.
        float rimAlpha = 0xD9 / 255f;
        int[] rimColors = new int[]{
                modulateAlpha(0xFFFFFFFF, rimAlpha),
                modulateAlpha(0xD9E2E8EE, rimAlpha),
                modulateAlpha(0xA650DDF2, rimAlpha),
                modulateAlpha(0xD9E2E8EE, rimAlpha),
                modulateAlpha(0x1AFFFFFF, rimAlpha),
                modulateAlpha(0x1AFFFFFF, rimAlpha),
                modulateAlpha(0x667D8D9B, rimAlpha),
                modulateAlpha(0x80DCE5ED, rimAlpha),
                modulateAlpha(0xD9FFFFFF, rimAlpha)
        };
        float[] rimStops = new float[]{
                0.00f, 0.04f, 0.09f, 0.15f, 0.46f, 0.54f, 0.85f, 0.925f, 1.00f
        };
        rim.setShader(new LinearGradient(area.left, area.top, area.right, area.bottom,
                rimColors, rimStops, Shader.TileMode.CLAMP));
        // A subdued overhead window reflection: original white-to-cyan profile,
        // deliberately reduced to 62% intensity so it reads as material, not focus.
        windowLight.setShader(new LinearGradient(area.left, area.top, area.right, area.top,
                new int[]{0x00FFFFFF, 0x00FFFFFF, 0xD9FFFFFF, 0x8067E8F9, 0x00FFFFFF, 0x00FFFFFF},
                new float[]{0.00f, 0.15f, 0.32f, 0.46f, 0.68f, 1.00f},
                Shader.TileMode.CLAMP));
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
        windowLight.setAlpha(Math.round(200f * 0.62f));
        canvas.drawRoundRect(area, radius, radius, windowLight);
    }

    private static int modulateAlpha(int color, float factor) {
        int alpha = Math.round(((color >>> 24) & 0xFF) * factor);
        return (color & 0x00FFFFFF) | (Math.max(0, Math.min(255, alpha)) << 24);
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
        windowLight.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter filter) {
        surface.setColorFilter(filter);
        rim.setColorFilter(filter);
        reflection.setColorFilter(filter);
        inner.setColorFilter(filter);
        windowLight.setColorFilter(filter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
