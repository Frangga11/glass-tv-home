package com.gathin.tvhome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView host for the Universal-style horizontal carousel.
 *
 * The selected card drives scrolling. RecyclerView must not independently scroll a focused
 * off-screen child into view, otherwise its automatic jump competes with the carousel pan.
 */
final class CarouselRecyclerView extends RecyclerView {
    private boolean manualHorizontalFocus;
    private boolean horizontalViewportClipEnabled;
    private int horizontalViewportEdgeInsetPx;

    CarouselRecyclerView(Context context) {
        super(context);
    }

    void setManualHorizontalFocus(boolean enabled) {
        manualHorizontalFocus = enabled;
    }

    /**
     * Clips carousel children to the visible lane, independently of RecyclerView padding.
     * In Horizontal mode the right padding is deliberately a large scroll-tail spacer so the
     * final app can reach the Universal second-slot anchor; it must not become the clip edge.
     */
    void setHorizontalViewportClip(boolean enabled, int edgeInsetPx) {
        int inset = Math.max(0, edgeInsetPx);
        if (horizontalViewportClipEnabled == enabled
                && horizontalViewportEdgeInsetPx == inset) return;
        horizontalViewportClipEnabled = enabled;
        horizontalViewportEdgeInsetPx = inset;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (!horizontalViewportClipEnabled) {
            super.dispatchDraw(canvas);
            return;
        }
        int inset = Math.max(0, Math.min(horizontalViewportEdgeInsetPx, getWidth() / 2));
        int left = inset;
        int right = Math.max(left, getWidth() - inset);
        if (right <= left) return;
        int save = canvas.save();
        canvas.clipRect(left, 0, right, getHeight());
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        if (manualHorizontalFocus) return false;
        return super.requestChildRectangleOnScreen(child, rect, immediate);
    }
}

/**
 * Keeps the one-row carousel inventory attached, just like the original HorizontalScrollView
 * implementation in TV Home Universal. This lets a DPAD press move focus immediately to the
 * adjacent card; the focus callback then starts the single controlled carousel pan.
 */
final class CarouselLinearLayoutManager extends LinearLayoutManager {
    private boolean keepAllCarouselItems;
    private int carouselExtraLayoutSpacePx;

    CarouselLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    void setKeepAllCarouselItems(boolean keepAll, int extraLayoutSpacePx) {
        int nextExtent = Math.max(0, extraLayoutSpacePx);
        if (keepAllCarouselItems == keepAll && carouselExtraLayoutSpacePx == nextExtent) return;
        keepAllCarouselItems = keepAll;
        carouselExtraLayoutSpacePx = nextExtent;
        requestLayout();
    }

    @Override
    protected void calculateExtraLayoutSpace(RecyclerView.State state, int[] extraLayoutSpace) {
        if (!keepAllCarouselItems) {
            super.calculateExtraLayoutSpace(state, extraLayoutSpace);
            return;
        }
        // The horizontal viewport is one row, so a full content-width pre-layout is bounded
        // and avoids the virtualized-focus delay at the edge of the visible cards.
        int extent = Math.max(carouselExtraLayoutSpacePx, 0);
        extraLayoutSpace[0] = extent;
        extraLayoutSpace[1] = extent;
    }
}
