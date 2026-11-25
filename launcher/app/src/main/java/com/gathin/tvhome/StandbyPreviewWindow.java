package com.gathin.tvhome;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/** Full-screen topmost host for the ordinary standby preview opened from Settings. */
final class StandbyPreviewWindow {
    private StandbyPreviewWindow() { }

    static void show(Activity activity, View preview, Runnable onClose) {
        if (activity == null || preview == null || !(preview.getParent() instanceof ViewGroup)) return;
        ViewGroup originalParent = (ViewGroup) preview.getParent();
        int originalIndex = originalParent.indexOfChild(preview);
        ViewGroup.LayoutParams originalParams = preview.getLayoutParams();

        Dialog dialog = new Dialog(activity);
        FrameLayout windowRoot = new FrameLayout(activity);
        windowRoot.setFocusable(true);
        windowRoot.setFocusableInTouchMode(true);
        originalParent.removeView(preview);
        windowRoot.addView(preview, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.setContentView(windowRoot);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(ignored -> {
            if (preview.getParent() instanceof ViewGroup) {
                ((ViewGroup) preview.getParent()).removeView(preview);
            }
            if (preview.getParent() == null) {
                int index = Math.max(0, Math.min(originalIndex, originalParent.getChildCount()));
                originalParent.addView(preview, index, originalParams);
            }
            if (onClose != null) onClose.run();
        });
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
        }

        View.OnKeyListener exitOnNavigation = (v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN) return true;
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                    || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                    || keyCode == KeyEvent.KEYCODE_DPAD_UP
                    || keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                    || keyCode == KeyEvent.KEYCODE_BACK
                    || keyCode == KeyEvent.KEYCODE_ESCAPE) {
                dialog.dismiss();
                return true;
            }
            return true;
        };
        windowRoot.setOnKeyListener(exitOnNavigation);
        preview.setFocusable(true);
        preview.setFocusableInTouchMode(true);
        preview.setOnKeyListener(exitOnNavigation);
        preview.setOnClickListener(v -> dialog.dismiss());
        windowRoot.post(preview::requestFocus);
    }
}
