package com.gathin.tvhome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/** Data-driven lunar phase illustration. The phase value comes from the selected location's forecast. */
public class MoonPhaseView extends View {

    private final Paint moonGlow = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint moonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint craterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private double phase = Double.NaN;
    private boolean southernHemisphere;

    public MoonPhaseView(Context context) {
        this(context, null);
    }

    public MoonPhaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        moonPaint.setStyle(Paint.Style.FILL);
        moonPaint.setColor(0xFFF1F5F9);
        craterPaint.setStyle(Paint.Style.FILL);
        craterPaint.setColor(0x2894A3B8);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setColor(0xCC0B1220);
        moonGlow.setStyle(Paint.Style.FILL);
    }

    public void setMoonData(double phase, boolean southernHemisphere) {
        this.phase = Double.isFinite(phase) ? phase - Math.floor(phase) : Double.NaN;
        this.southernHemisphere = southernHemisphere;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) return;

        float cx = w / 2f;
        float cy = h / 2f;
        float radius = Math.min(w * 0.22f, h * 0.42f);
        if (radius <= 0f) return;

        RadialGradient glow = new RadialGradient(
                cx, cy, radius * 1.45f,
                new int[]{0x24FFFFFF, 0x00000000},
                new float[]{0.5f, 1f},
                Shader.TileMode.CLAMP);
        moonGlow.setShader(glow);
        canvas.drawCircle(cx, cy, radius * 1.45f, moonGlow);

        if (Double.isNaN(phase)) {
            moonPaint.setColor(0x667E8A98);
            canvas.drawCircle(cx, cy, radius, moonPaint);
            moonPaint.setColor(0xFFF1F5F9);
            return;
        }

        moonPaint.setColor(0xFFF1F5F9);
        canvas.drawCircle(cx, cy, radius, moonPaint);
        canvas.drawCircle(cx - radius * 0.30f, cy - radius * 0.20f, radius * 0.18f, craterPaint);
        canvas.drawCircle(cx + radius * 0.25f, cy + radius * 0.20f, radius * 0.22f, craterPaint);
        canvas.drawCircle(cx - radius * 0.10f, cy + radius * 0.40f, radius * 0.14f, craterPaint);

        float terminator = (float) Math.cos(phase * Math.PI * 2.0) * radius;
        float left = phase < 0.5 ? cx - radius : cx - terminator;
        float right = phase < 0.5 ? cx + terminator : cx + radius;
        if (southernHemisphere) {
            canvas.save();
            canvas.scale(-1f, 1f, cx, cy);
        }
        if (right > left) {
            canvas.drawOval(left, cy - radius, right, cy + radius, shadowPaint);
        }
        if (southernHemisphere) canvas.restore();
    }
}
