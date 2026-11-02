package com.gathin.tvhome;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Single delivery path for short, non-blocking launcher feedback.
 *
 * Resolves localized text at the call site, posts display work to the main thread,
 * and avoids showing a Toast for an Activity that has already left the foreground.
 */
public final class TvFeedback {
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    private TvFeedback() {}

    public static void show(Context context, CharSequence message, int duration) {
        if (context == null || !FeedbackPolicy.hasMessage(message)) return;

        Runnable display = () -> {
            Activity activity = findActivity(context);
            if (activity != null && !FeedbackPolicy.isActivityUsable(activity.isFinishing(), activity.isDestroyed())) return;

            Context toastContext = context.getApplicationContext();
            if (toastContext == null) toastContext = context;
            Toast.makeText(toastContext, message, FeedbackPolicy.normalizeToastDuration(duration)).show();
        };

        if (Looper.myLooper() == Looper.getMainLooper()) display.run();
        else MAIN.post(display);
    }

    public static void show(Context context, int messageResId, int duration) {
        if (context == null) return;
        show(context, context.getText(messageResId), duration);
    }

    private static Activity findActivity(Context context) {
        Context current = context;
        while (current != null) {
            if (current instanceof Activity) return (Activity) current;
            if (!(current instanceof ContextWrapper)) return null;
            Context next = ((ContextWrapper) current).getBaseContext();
            if (next == current) return null;
            current = next;
        }
        return null;
    }
}
