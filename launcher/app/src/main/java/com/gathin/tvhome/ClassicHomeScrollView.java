package com.gathin.tvhome;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Home Universal Classic scroll host.
 *
 * Focus does not trigger Android's automatic rectangle scroll; MainActivity applies the
 * Classic row-centering policy after resolving the focused app.
 */
final class ClassicHomeScrollView extends ScrollView {
    ClassicHomeScrollView(Context context) {
        super(context);
        initialize();
    }

    ClassicHomeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        setFillViewport(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setClipChildren(false);
        setClipToPadding(false);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}
