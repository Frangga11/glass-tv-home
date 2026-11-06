package com.gathin.tvhome;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * B01-owned visual contract for app-owned modal dialogs.
 *
 * Centered modal dialogs, transparent custom-content dialogs, and anchored PopupWindows are
 * separate surface types. This class owns only modal window bounds, dimming and material fallback.
 */
final class TvDialogStyle {
    private static final float DIM_AMOUNT = 0.68f;
    private static final int MIN_WIDTH_DP = 360;

    private TvDialogStyle() { }

    static void apply(Dialog dialog, Context context, int maxWidthDp, int maxHeightDp) {
        if (dialog == null || context == null) return;
        Window window = dialog.getWindow();
        if (window == null) return;

        float density = context.getResources().getDisplayMetrics().density;
        int availableWidth = context.getResources().getDisplayMetrics().widthPixels;
        int availableHeight = context.getResources().getDisplayMetrics().heightPixels;

        window.setBackgroundDrawable(createDialogSurface(context));
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setDimAmount(DIM_AMOUNT);
        window.setLayout(
                resolveWidthPx(availableWidth, density, maxWidthDp),
                resolveHeightPx(availableHeight, density, maxHeightDp));
        window.setGravity(Gravity.CENTER);
    }

    /** Use the same material policy as GlassDrawable: glass when enabled, solid surface otherwise. */
    static android.graphics.drawable.Drawable createDialogSurface(Context context) {
        boolean glassEnabled = context.getSharedPreferences("wallpaper_prefs", Context.MODE_PRIVATE)
                .getBoolean("glass_effects_enabled", true);
        if (glassEnabled) return context.getDrawable(R.drawable.bg_glass_dialog);
        GradientDrawable solid = new GradientDrawable();
        solid.setShape(GradientDrawable.RECTANGLE);
        solid.setColor(TvUiTokens.POPUP_SURFACE);
        solid.setCornerRadius(TvUiTokens.POPUP_RADIUS_DP
                * context.getResources().getDisplayMetrics().density);
        solid.setStroke(Math.max(1, Math.round(context.getResources().getDisplayMetrics().density)),
                TvUiTokens.BORDER);
        return solid;
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
        if (availableWidthPx <= 0 || !(density > 0f) || Float.isInfinite(density) || Float.isNaN(density)) return 0;
        int requested = Math.max(MIN_WIDTH_DP, maxWidthDp);
        return Math.min(Math.round(requested * density), TvResponsiveLayout.safeContentPx(availableWidthPx));
    }

    static int resolveHeightPx(int availableHeightPx, float density, int maxHeightDp) {
        if (maxHeightDp <= 0) return ViewGroup.LayoutParams.WRAP_CONTENT;
        if (availableHeightPx <= 0 || !(density > 0f) || Float.isInfinite(density) || Float.isNaN(density)) {
            return ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        return Math.min(Math.round(maxHeightDp * density),
                TvResponsiveLayout.safeContentPx(availableHeightPx));
    }
}
