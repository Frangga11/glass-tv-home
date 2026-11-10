package com.gathin.tvhome;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Draws the shared Glider in the Grid's background pass, matching DockDecoration's
 * companion path. The root Glider intentionally sits behind app content; this companion
 * restores the focus pane above the RecyclerView background but below translucent tile cards.
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
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent,
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
