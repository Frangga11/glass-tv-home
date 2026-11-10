package com.gathin.tvhome;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * Paints the shared DPAD Glider inside a focusable capsule's own background pass.
 *
 * The capsule surface is therefore drawn first, this moving focus layer second, and the
 * TextView's icon/label last. The focus indicator stays visible without covering its content
 * or changing the capsule's measured geometry.
 */
final class GliderFocusDrawable extends Drawable {
    private final View owner;
    private final Glider glider;
    private final int[] ownerAt = new int[2];
    private final int[] gliderAt = new int[2];

    GliderFocusDrawable(@NonNull View owner, @NonNull Glider glider) {
        this.owner = owner;
        this.glider = glider;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (!glider.isShowing() || owner.getWidth() <= 0 || owner.getHeight() <= 0) return;
        owner.getLocationInWindow(ownerAt);
        glider.getLocationInWindow(gliderAt);
        canvas.save();
        canvas.clipRect(getBounds());
        glider.drawShape(canvas, gliderAt[0] - ownerAt[0], gliderAt[1] - ownerAt[1], owner);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        // Opacity is owned by the shared Glider animation, not by the capsule background.
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // The shared Glider owns its silver/cyan focus palette.
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
