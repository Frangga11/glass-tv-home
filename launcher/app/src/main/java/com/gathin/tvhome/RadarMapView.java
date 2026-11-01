package com.gathin.tvhome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Decorative local-atmosphere illustration for the weather dashboard.
 * This is intentionally not presented as a live precipitation radar.
 */
public class RadarMapView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int temp = 29;
    private String locationName = "Pagar Alam";

    public RadarMapView(Context context) {
        this(context, null);
    }

    public RadarMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setDetails(int temp, String locationName) {
        this.temp = temp;
        this.locationName = (locationName == null || locationName.trim().isEmpty())
                ? "Pagar Alam" : locationName;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) return;

        float d = getResources().getDisplayMetrics().density;
        RectF bounds = new RectF(0, 0, w, h);
        Path clip = new Path();
        clip.addRoundRect(bounds, 12f * d, 12f * d, Path.Direction.CW);
        canvas.save();
        canvas.clipPath(clip);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFF102334);
        canvas.drawRect(bounds, paint);

        // Soft atmospheric glow.
        paint.setColor(0x443B82A0);
        canvas.drawCircle(w * 0.76f, h * 0.18f, w * 0.34f, paint);
        paint.setColor(0x2238BDF8);
        canvas.drawCircle(w * 0.22f, h * 0.34f, w * 0.28f, paint);

        // Distant mountain silhouettes inspired by the Dempo landscape.
        Path distant = new Path();
        distant.moveTo(0, h * 0.72f);
        distant.lineTo(w * 0.18f, h * 0.35f);
        distant.lineTo(w * 0.32f, h * 0.57f);
        distant.lineTo(w * 0.53f, h * 0.23f);
        distant.lineTo(w * 0.75f, h * 0.58f);
        distant.lineTo(w * 0.89f, h * 0.42f);
        distant.lineTo(w, h * 0.60f);
        distant.lineTo(w, h);
        distant.lineTo(0, h);
        distant.close();
        paint.setColor(0xFF27465A);
        canvas.drawPath(distant, paint);

        Path foreground = new Path();
        foreground.moveTo(0, h * 0.82f);
        foreground.lineTo(w * 0.20f, h * 0.64f);
        foreground.lineTo(w * 0.39f, h * 0.76f);
        foreground.lineTo(w * 0.61f, h * 0.54f);
        foreground.lineTo(w * 0.82f, h * 0.75f);
        foreground.lineTo(w, h * 0.61f);
        foreground.lineTo(w, h);
        foreground.lineTo(0, h);
        foreground.close();
        paint.setColor(0xFF183747);
        canvas.drawPath(foreground, paint);

        // Location and temperature badge.
        float cx = w * 0.50f;
        float cy = h * 0.48f;
        paint.setColor(0xCC0B1724);
        canvas.drawRoundRect(new RectF(cx - 52f * d, cy - 22f * d,
                cx + 52f * d, cy + 22f * d), 14f * d, 14f * d, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1f * d);
        paint.setColor(0x887DD3FC);
        canvas.drawRoundRect(new RectF(cx - 52f * d, cy - 22f * d,
                cx + 52f * d, cy + 22f * d), 14f * d, 14f * d, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setFakeBoldText(true);
        paint.setTextSize(15f * d);
        paint.setColor(0xFFFFFFFF);
        canvas.drawText(temp + "°", cx, cy + 5f * d, paint);

        paint.setFakeBoldText(true);
        paint.setTextSize(8f * d);
        paint.setColor(0xFFE0F2FE);
        canvas.drawText(locationName.toUpperCase(java.util.Locale.ROOT), cx, h - 21f * d, paint);
        paint.setFakeBoldText(false);
        paint.setTextSize(6.5f * d);
        paint.setColor(0xFFB6C9D8);
        canvas.drawText(getContext().getString(R.string.weather_radar_illustration_label), cx, h - 8f * d, paint);

        canvas.restore();
    }
}
