package com.gathin.tvhome;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Draws a non-opaque focus rim around the actual focused app tile. It deliberately does not
 * reuse Glider's full glass pane or paint a filled rectangle over the app artwork.
 */
final class GridFocusDecoration extends RecyclerView.ItemDecoration {
    private final Rect focusedBounds = new Rect();
    private final RectF outline = new RectF();
    private final Paint focusRim = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final float density;

    GridFocusDecoration(@NonNull Glider glider) {
        density = glider.getResources().getDisplayMetrics().density;
        focusRim.setStyle(Paint.Style.STROKE);
        focusRim.setStrokeWidth(2.25f * density);
        focusRim.setColor(TvUiTokens.FOCUS_BORDER);
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent,
                           @NonNull RecyclerView.State state) {
        // Resolve the focused descendant on every draw. Never reuse stale Glider coordinates:
        // after UP/DOWN or a cancelled animation, an old rectangle must not remain on wallpaper.
        View focused = parent.findFocus();
        if (focused == null) return;
        View card = focused.getId() == R.id.card ? focused : focused.findViewById(R.id.card);
        if (card == null || !card.isShown() || !card.hasFocus()) return;

        focusedBounds.set(0, 0, card.getWidth(), card.getHeight());
        parent.offsetDescendantRectToMyCoords(card, focusedBounds);
        float inset = focusRim.getStrokeWidth() / 2f + density;
        outline.set(focusedBounds);
        outline.inset(inset, inset);
        if (outline.width() <= 0f || outline.height() <= 0f) return;

        float radius = AppAdapter.cornerRadius(card.getWidth(), density);
        canvas.save();
        canvas.clipRect(0, 0, parent.getWidth(), parent.getHeight());
        // Stroke only: no fill, veil, blur, or opaque pane can cover the app's banner/icon/label.
        canvas.drawRoundRect(outline, Math.max(0f, radius - inset),
                Math.max(0f, radius - inset), focusRim);
        canvas.restore();
    }
}
