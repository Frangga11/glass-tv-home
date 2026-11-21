package com.gathin.tvhome;

import android.content.Context;
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

    CarouselRecyclerView(Context context) {
        super(context);
    }

    void setManualHorizontalFocus(boolean enabled) {
        manualHorizontalFocus = enabled;
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
