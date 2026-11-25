package com.gathin.tvhome;

import android.app.Activity;
import android.app.Dialog;
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
        if (activity == null || preview == null
                || !(preview.getParent() instanceof ViewGroup)) return;

        final ViewGroup originalParent = (ViewGroup) preview.getParent();
        final int originalIndex = originalParent.indexOfChild(preview);
        final ViewGroup.LayoutParams originalParams = preview.getLayoutParams();

        Dialog dialog = new Dialog(activity) {
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN) return true;
                int key = event.getKeyCode();
                if (key == KeyEvent.KEYCODE_DPAD_LEFT
                        || key == KeyEvent.KEYCODE_DPAD_RIGHT
                        || key == KeyEvent.KEYCODE_DPAD_UP
                        || key == KeyEvent.KEYCODE_DPAD_DOWN
                        || key == KeyEvent.KEYCODE_BACK
                        || key == KeyEvent.KEYCODE_ESCAPE
                        || key == KeyEvent.KEYCODE_DPAD_CENTER
                        || key == KeyEvent.KEYCODE_ENTER) {
                    dismiss();
                }
                return true;
            }
        };
        FrameLayout windowRoot = new FrameLayout(activity);
        windowRoot.setFocusable(true);
        windowRoot.setFocusableInTouchMode(true);
        dialog.setContentView(windowRoot);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(ignored -> {
            if (preview.getParent() instanceof ViewGroup) {
                ((ViewGroup) preview.getParent()).removeView(preview);
            }
            int index = Math.max(0, Math.min(originalIndex, originalParent.getChildCount()));
            originalParent.addView(preview, index, originalParams);
            if (onClose != null) onClose.run();
        });

        originalParent.removeView(preview);
        windowRoot.addView(preview, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
        preview.setOnClickListener(v -> dialog.dismiss());
        windowRoot.post(windowRoot::requestFocus);
    }
}
