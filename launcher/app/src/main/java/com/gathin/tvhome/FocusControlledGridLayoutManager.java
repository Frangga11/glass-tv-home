package com.gathin.tvhome;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The Classic Grid has one explicit scroll owner: MainActivity's row coordinator.
 * This mirrors Home Universal's HomeScrollView, which suppresses platform auto-scroll
 * when focus changes so native scrolling cannot compete with the controlled row transition.
 */
final class FocusControlledGridLayoutManager extends GridLayoutManager {
    FocusControlledGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    @Override
    public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state,
                                       View child, View focused) {
        // Focus is handled by RecyclerView; scrolling is handled only by MainActivity.
        return true;
    }
}
