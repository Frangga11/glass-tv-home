package com.gathin.tvhome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/** Sun path uses the API's selected-location sunrise, sunset and current local timestamp. */
public class SunArcView extends View {

    private final Paint arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint horizonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint sunDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint sunGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private String sunsetTime = "—";
    private String sunriseTime = "—";
    private String currentTimeLocal = "";
    private String sunriseLabel = "Sunrise";

    public SunArcView(Context context) {
        this(context, null);
    }

    public SunArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(1.6f);
        arcPaint.setColor(0x88FFFFFF);
        horizonPaint.setStyle(Paint.Style.STROKE);
        horizonPaint.setStrokeWidth(1f);
        horizonPaint.setColor(0x40FFFFFF);
        horizonPaint.setPathEffect(new DashPathEffect(new float[]{3f, 3f}, 0));
        sunDotPaint.setStyle(Paint.Style.FILL);
        sunDotPaint.setColor(0xFFFFFFFF);
        sunGlowPaint.setStyle(Paint.Style.FILL);
        sunGlowPaint.setColor(0x44FBBF24);
        labelPaint.setStyle(Paint.Style.FILL);
        labelPaint.setColor(0xCCFFFFFF);
    }

    public void setTimes(String sunrise, String sunset) {
        setTimes(sunrise, sunset, "");
    }

    public void setTimes(String sunrise, String sunset, String currentTimeLocal) {
        setTimes(sunrise, sunset, currentTimeLocal, "Sunrise");
    }

    public void setTimes(String sunrise, String sunset, String currentTimeLocal, String sunriseLabel) {
        this.sunriseTime = sunrise == null ? "—" : sunrise;
        this.sunsetTime = sunset == null ? "—" : sunset;
        this.currentTimeLocal = currentTimeLocal == null ? "" : currentTimeLocal;
        this.sunriseLabel = sunriseLabel == null ? "Sunrise" : sunriseLabel;
        invalidate();
    }

    private int parseMinutes(String value) {
        if (value == null || value.isEmpty() || "—".equals(value)) return -1;
        String time = value.trim();
        if (time.contains("T")) time = time.substring(time.indexOf('T') + 1);
        try {
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
            int minute = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
            String lower = time.toLowerCase(java.util.Locale.ROOT);
            if (lower.contains("pm") && hour < 12) hour += 12;
            if (lower.contains("am") && hour == 12) hour = 0;
            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) return -1;
            return hour * 60 + minute;
        } catch (Exception ignored) {
            return -1;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) return;

        float density = getResources().getDisplayMetrics().density;
        float horizonY = h * 0.62f;
        float startX = 8f * density;
        float endX = w - (8f * density);
        float midX = (startX + endX) / 2f;
        float peakY = h * 0.20f;

        canvas.drawLine(6f * density, horizonY, w - (6f * density), horizonY, horizonPaint);
        Path path = new Path();
        path.moveTo(startX, horizonY);
        path.quadTo(midX, peakY - (15f * density), endX, horizonY);
        canvas.drawPath(path, arcPaint);

        int sunrise = parseMinutes(sunriseTime);
        int sunset = parseMinutes(sunsetTime);
        int current = parseMinutes(currentTimeLocal);
        if (sunrise >= 0 && sunset > sunrise && current >= sunrise && current <= sunset) {
            float progress = (current - sunrise) / (float) (sunset - sunrise);
            float sunX = startX + progress * (endX - startX);
            float t = progress;
            float sunY = (1 - t) * (1 - t) * horizonY
                    + 2 * (1 - t) * t * (peakY - (15f * density))
                    + t * t * horizonY;
            canvas.drawCircle(sunX, sunY, 6f * density, sunGlowPaint);
            canvas.drawCircle(sunX, sunY, 2.5f * density, sunDotPaint);
        }

        labelPaint.setTextSize(8f * density);
        labelPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(sunriseLabel + ": " + sunriseTime, startX, h - (2f * density), labelPaint);
    }
}
