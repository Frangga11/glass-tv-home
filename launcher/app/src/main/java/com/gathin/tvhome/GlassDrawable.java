package com.gathin.tvhome;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * A top-bar pill or popup panel in the glass style: the blurred wallpaper behind it, an even veil, and an edge
 * that shines at the top-left and bottom-right (GlassRim), with no brighter top. The glass remains present beneath the shared gliding focus layer, so focus never exposes the wallpaper.
 */
final class GlassDrawable extends Drawable {

    private static final int VEIL_ALPHA = 0x70;
    private final View owner;
    private final float radius;
    private final Backdrop.Fill fill = new Backdrop.Fill();
    private final Paint veil = new Paint(Paint.ANTI_ALIAS_FLAG);
    /** Opaque fallback surface: disabling decoration must never remove the component surface. */
    private final Paint solidSurface = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint solidBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
    /** Wallpaper-preserving readability veil used only by the Home Islamic floating content. */
    private final Paint floatingGradient = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF floatingGradientBounds = new RectF();
    private LinearGradient floatingGradientShader;
    private final GlassRim rim;
    private final Glider highlight;
    /** Paints the focus shape over the glass (a menu), rather than fading the glass for it (a pill). */
    private final boolean paintsShape;
    /** How far the glass is shown, 0 to 1, for a panel fading in or out. */
    private float fade = 1f;
    private final int[] at = new int[2], highlightAt = new int[2];
    private final RectF rect = new RectF();

    GlassDrawable(View owner, float radius, float density, Glider highlight) {
        this(owner, radius, density, highlight, 1f);
    }

    /** {@code reach} as in GlassRim: larger panels keep the shine to the corners. */
    GlassDrawable(View owner, float radius, float density, Glider highlight, float reach) {
        this(owner, radius, density, highlight, reach, false);
    }

    /**
     * With {@code paintsShape}, the focus shape is painted over this glass and under what the panel
     * holds, as the dock's tray paints it: the shape glides in from outside the panel and the glass
     * never hides it.
     */
    GlassDrawable(View owner, float radius, float density, Glider highlight, float reach, boolean paintsShape) {
        this.paintsShape = paintsShape;
        this.owner = owner;
        this.highlight = highlight;
        this.radius = radius;
        veil.setColor(TvUiTokens.GLASS_VEIL_COLOR);
        solidSurface.setColor(TvUiTokens.POPUP_SURFACE);
        solidBorder.setColor(TvUiTokens.BORDER);
        solidBorder.setStyle(Paint.Style.STROKE);
        solidBorder.setStrokeWidth(1.25f * density);
        rim = new GlassRim(1.5f * density, 0xD9FFFFFF, 0x1AFFFFFF, reach);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        rect.set(getBounds());
        // Ayat Pilihan and Waktu Salat are floating wallpaper content, not a glass panel.
        // Keep the gradient local to this right-side shelf and fade it in only toward the right edge.
        // Existing TextView shadows provide the second readability layer without changing typography.
        if (owner.getId() == R.id.islamicShelf) {
            if (fade > 0f && rect.width() > 0f && rect.height() > 0f) {
                // Cache the shader; rebuild only when the shelf's local bounds actually change.
                if (floatingGradientShader == null || !floatingGradientBounds.equals(rect)) {
                    floatingGradientBounds.set(rect);
                    floatingGradientShader = new LinearGradient(
                            rect.left, rect.top, rect.right, rect.top,
                            new int[]{0x00000000, 0x06000000, 0x10000000, 0x1A000000},
                            new float[]{0f, 0.48f, 0.78f, 1f}, Shader.TileMode.CLAMP);
                    floatingGradient.setShader(floatingGradientShader);
                }
                floatingGradient.setAlpha(Math.round(255 * fade));
                canvas.drawRect(rect, floatingGradient);
            }
            return;
        }
        boolean glassEnabled = owner.getContext().getSharedPreferences("wallpaper_prefs", android.content.Context.MODE_PRIVATE)
                .getBoolean("glass_effects_enabled", true);
        if (!glassEnabled) {
            // Glass is optional decoration, not the panel's structural background.
            int surfaceAlpha = Math.round(255 * fade);
            solidSurface.setAlpha(surfaceAlpha);
            solidBorder.setAlpha(surfaceAlpha);
            canvas.drawRoundRect(rect, radius, radius, solidSurface);
            canvas.drawRoundRect(rect, radius, radius, solidBorder);
            if (paintsShape && highlight != null && highlight.isShowing()) {
                owner.getLocationInWindow(at);
                highlight.getLocationInWindow(highlightAt);
                canvas.save();
                canvas.clipRect(getBounds());
                highlight.drawShape(canvas, highlightAt[0] - at[0], highlightAt[1] - at[1], owner);
                canvas.restore();
            }
            return;
        }
        // The glass gives way to the focus fill as it arrives: the more of this pill the gliding
        // shape covers, the fainter the glass, until a focused pill is the fill alone. As the shape
        // leaves, the glass comes back the same way.
        // Keep the glass capsule fully rendered while DPAD focus glides above it.
        // Focus is a separate layer; it must never fade the shared glass surface away.
        float glass = fade;
        if (glass > 0f) {
            fill.draw(canvas, owner, rect, radius, glass);
            veil.setAlpha(Math.round(VEIL_ALPHA * glass));
            canvas.drawRoundRect(rect, radius, radius, veil);
            if (paintsShape && highlight != null && highlight.isShowing()) {
                owner.getLocationInWindow(at);
                highlight.getLocationInWindow(highlightAt);
                canvas.save();
                canvas.clipRect(getBounds());
                highlight.drawShape(canvas, highlightAt[0] - at[0], highlightAt[1] - at[1], owner);
                canvas.restore();
            }
            float half = rim.paint.getStrokeWidth() / 2f;
            rect.inset(half, half);
            rim.fit(rect);
            rim.paint.setAlpha(Math.round(255 * glass));
            canvas.drawRoundRect(rect, radius - half, radius - half, rim.paint);
            rim.drawWindowLight(canvas, rect, radius - half, glass);
        }
        // The fill itself is the highlight view behind the pills, showing through as the glass clears;
        // painting it here as well would darken a focused pill twice over.
    }

    @Override
    public void setAlpha(int alpha) {
        fade = alpha / 255f;
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
