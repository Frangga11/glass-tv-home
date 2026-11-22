package com.gathin.tvhome;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.view.PixelCopy;
import android.view.View;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.RejectedExecutionException;

/**
 * Captures and blurs the actual Home composition underneath the Quick Settings panel.
 *
 * <p>The shared Backdrop cache contains only wallpaper pixels. It cannot blur foreground Home
 * labels, prayer times or other Views, so those elements remain sharp through a translucent
 * Dialog window. PixelCopy samples the Activity Window crop before the Dialog is shown; the
 * existing Backdrop blur routine then softens that complete composition off the UI thread.
 */
final class QuickSettingsBackdropCapture {
    interface Listener {
        void onReady(Bitmap blurredPanel);
        void onFailure(String reason);
    }

    private QuickSettingsBackdropCapture() { }

    static Rect panelCropInWindow(int hostWindowX, int hostWindowY, int panelLeftInHost,
                                  int panelTopInHost, int panelWidth, int panelHeight) {
        return new Rect(hostWindowX + panelLeftInHost, hostWindowY + panelTopInHost,
                hostWindowX + panelLeftInHost + panelWidth,
                hostWindowY + panelTopInHost + panelHeight);
    }

    static void capture(Activity activity, Rect sourceRect, Executor worker,
                        Handler callbackHandler, AtomicBoolean active, Listener listener) {
        if (activity == null || sourceRect == null || worker == null
                || callbackHandler == null || active == null || listener == null) {
            notifyFailure(callbackHandler, listener, "Missing capture dependency");
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notifyFailure(callbackHandler, listener, "PixelCopy requires API 26");
            return;
        }
        if (!active.get() || activity.isFinishing() || activity.isDestroyed()) {
            notifyFailure(callbackHandler, listener, "Activity is no longer active");
            return;
        }

        View decor = activity.getWindow().getDecorView();
        if (!decor.isAttachedToWindow() || !decor.isLaidOut()
                || decor.getWidth() <= 0 || decor.getHeight() <= 0) {
            notifyFailure(callbackHandler, listener, "Activity window has no laid-out surface");
            return;
        }
        Rect windowBounds = new Rect(0, 0, decor.getWidth(), decor.getHeight());
        Rect crop = new Rect(sourceRect);
        if (!windowBounds.contains(crop) || crop.width() <= 0 || crop.height() <= 0) {
            notifyFailure(callbackHandler, listener, "Panel crop is outside the Activity window: " + crop);
            return;
        }

        final Bitmap captured;
        try {
            // Capture at native pixel dimensions for this panel only (normally about 0.3 MP).
            captured = Bitmap.createBitmap(crop.width(), crop.height(), Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError | IllegalArgumentException e) {
            notifyFailure(callbackHandler, listener, "Unable to allocate panel capture bitmap");
            return;
        }

        try {
            PixelCopy.request(activity.getWindow(), crop, captured, result -> {
                if (!active.get()) {
                    recycle(captured);
                    return;
                }
                if (result != PixelCopy.SUCCESS) {
                    recycle(captured);
                    listener.onFailure("PixelCopy returned " + result);
                    return;
                }

                try {
                    worker.execute(() -> {
                        Bitmap blurred = null;
                        try {
                            if (active.get() && !Thread.currentThread().isInterrupted()) {
                                // The source crop is native-size; Backdrop.blur downsamples once
                                // and applies its established two-pass box blur at half resolution.
                                blurred = Backdrop.blur(captured, crop.width(), crop.height(), 0);
                            }
                        } finally {
                            recycle(captured);
                        }

                        final Bitmap ready = blurred;
                        if (!active.get()) {
                            recycle(ready);
                            return;
                        }
                        boolean posted = callbackHandler.post(() -> {
                            if (!active.get()) {
                                recycle(ready);
                                return;
                            }
                            if (ready == null || ready.isRecycled()) {
                                listener.onFailure("Unable to blur captured Activity window");
                            } else {
                                listener.onReady(ready);
                            }
                        });
                        if (!posted) recycle(ready);
                    });
                } catch (RejectedExecutionException e) {
                    recycle(captured);
                    listener.onFailure("Backdrop worker is shutting down");
                }
            }, callbackHandler);
        } catch (RuntimeException e) {
            recycle(captured);
            listener.onFailure("PixelCopy request failed: " + e.getClass().getSimpleName());
        }
    }

    private static void notifyFailure(Handler handler, Listener listener, String reason) {
        if (listener == null) return;
        if (handler == null) {
            listener.onFailure(reason);
        } else {
            handler.post(() -> listener.onFailure(reason));
        }
    }

    private static void recycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
    }
}
