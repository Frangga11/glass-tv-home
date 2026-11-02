package com.gathin.tvhome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * B01-owned visual contract for app-owned modal dialogs.
 *
 * Keep modal surface, dimming, centering and viewport bounds in one place. The main Settings
 * shell and anchored glass PopupWindows have their own distinct presentation contracts.
 */
final class TvDialogStyle {
    private static final float DIM_AMOUNT = 0.68f;
    private static final float VIEWPORT_SHARE = 0.88f;
    private static final int MIN_WIDTH_DP = 360;

    private TvDialogStyle() { }

    static void apply(AlertDialog dialog, Context context, int maxWidthDp, int maxHeightDp) {
        if (dialog == null || context == null) return;
        Window window = dialog.getWindow();
        if (window == null) return;

        float density = context.getResources().getDisplayMetrics().density;
        int availableWidth = context.getResources().getDisplayMetrics().widthPixels;
        int availableHeight = context.getResources().getDisplayMetrics().heightPixels;

        window.setBackgroundDrawableResource(R.drawable.bg_glass_dialog);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setDimAmount(DIM_AMOUNT);
        window.setLayout(
                resolveWidthPx(availableWidth, density, maxWidthDp),
                resolveHeightPx(availableHeight, density, maxHeightDp));
        window.setGravity(Gravity.CENTER);
    }

    static void applyCustom(Dialog dialog, Context context, int maxWidthDp, int maxHeightDp,
                            float dimAmount) {
        if (dialog == null || context == null) return;
        Window window = dialog.getWindow();
        if (window == null) return;
        float density = context.getResources().getDisplayMetrics().density;
        int availableWidth = context.getResources().getDisplayMetrics().widthPixels;
        int availableHeight = context.getResources().getDisplayMetrics().heightPixels;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setDimAmount(Math.max(0f, Math.min(1f, dimAmount)));
        window.setLayout(resolveWidthPx(availableWidth, density, maxWidthDp),
                resolveHeightPx(availableHeight, density, maxHeightDp));
        window.setGravity(Gravity.CENTER);
    }

    static int resolveWidthPx(int availableWidthPx, float density, int maxWidthDp) {
        if (availableWidthPx <= 0 || density <= 0f) return 0;
        int requested = Math.max(MIN_WIDTH_DP, maxWidthDp);
        return Math.min(Math.round(requested * density),
                Math.round(availableWidthPx * VIEWPORT_SHARE));
    }

    static int resolveHeightPx(int availableHeightPx, float density, int maxHeightDp) {
        if (maxHeightDp <= 0) return ViewGroup.LayoutParams.WRAP_CONTENT;
        if (availableHeightPx <= 0 || density <= 0f) return ViewGroup.LayoutParams.WRAP_CONTENT;
        return Math.min(Math.round(maxHeightDp * density),
                Math.round(availableHeightPx * VIEWPORT_SHARE));
    }
}
