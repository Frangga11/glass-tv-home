package com.gathin.tvhome;

import android.graphics.Canvas;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Repaints the shared Glider above Grid children while the Grid owns focus. The root Glider
 * remains behind the Home content tree; drawing in onDrawOver prevents opaque app cards or
 * artwork from covering the second-row focus indicator.
 */
final class GridFocusDecoration extends RecyclerView.ItemDecoration {
    private final Glider glider;
    private final int[] gridAt = new int[2];
    private final int[] gliderAt = new int[2];
    private final RectF bleed = new RectF();
    private final float bleedPx;

    GridFocusDecoration(@NonNull Glider glider) {
        this.glider = glider;
        this.bleedPx = 36f * glider.getResources().getDisplayMetrics().density;
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent,
                           @NonNull RecyclerView.State state) {
        // Dock focus is painted by DockDecoration. Only the active Grid region owns this path.
        if (!parent.hasFocus() || !glider.isShowing()) return;
        parent.getLocationInWindow(gridAt);
        glider.getLocationInWindow(gliderAt);
        canvas.save();
        bleed.set(0f, 0f, parent.getWidth(), parent.getHeight());
        bleed.inset(-bleedPx, -bleedPx);
        canvas.clipRect(bleed);
        glider.drawShape(canvas, gliderAt[0] - gridAt[0], gliderAt[1] - gridAt[1], parent);
        canvas.restore();
    }
}
