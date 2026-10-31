package com.gathin.tvhome;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/** Persistent, local-only photo selection for the StandBy screensaver. */
final class StandbyPhotoStore {
    private static final String PREFS = "standby_prefs";
    private static final String KEY_URIS = "photo_uris";
    private static final String KEY_MODE = "display_mode";

    private StandbyPhotoStore() {}

    static List<Uri> getUris(Context context) {
        List<Uri> result = new ArrayList<>();
        if (context == null) return result;
        String raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_URIS, "");
        if (TextUtils.isEmpty(raw)) return result;
        for (String value : raw.split("\\n")) {
            if (!TextUtils.isEmpty(value)) {
                try {
                    result.add(Uri.parse(value));
                } catch (Exception ignored) {
                }
            }
        }
        return result;
    }

    static void saveUris(Context context, List<Uri> uris) {
        if (context == null) return;
        List<String> values = new ArrayList<>();
        if (uris != null) {
            for (Uri uri : uris) {
                if (uri != null) values.add(uri.toString());
            }
        }
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putString(KEY_URIS, TextUtils.join("\n", values))
                .apply();
    }

    static boolean isPhotoMode(Context context) {
        return context != null && context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getBoolean(KEY_MODE, false);
    }

    static void setPhotoMode(Context context, boolean enabled) {
        if (context == null) return;
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putBoolean(KEY_MODE, enabled).apply();
    }

    static int getPhotoCount(Context context) {
        return getUris(context).size();
    }

    /** Decode to screen size, avoiding full-resolution phone photos in the TV's limited RAM. */
    static Bitmap loadBitmap(Context context, Uri uri) {
        if (context == null || uri == null) return null;
        try {
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            try (InputStream in = context.getContentResolver().openInputStream(uri)) {
                if (in == null) return null;
                BitmapFactory.decodeStream(in, null, bounds);
            }
            if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null;

            int targetW = Math.max(1, context.getResources().getDisplayMetrics().widthPixels);
            int targetH = Math.max(1, context.getResources().getDisplayMetrics().heightPixels);
            int sample = 1;
            while (bounds.outWidth / (sample * 2) >= targetW
                    && bounds.outHeight / (sample * 2) >= targetH) {
                sample *= 2;
            }

            BitmapFactory.Options decode = new BitmapFactory.Options();
            decode.inSampleSize = Math.max(1, sample);
            decode.inPreferredConfig = Bitmap.Config.RGB_565;
            decode.inDither = true;
            try (InputStream in = context.getContentResolver().openInputStream(uri)) {
                return in == null ? null : BitmapFactory.decodeStream(in, null, decode);
            }
        } catch (OutOfMemoryError e) {
            android.util.Log.e("TvHome.StandBy", "Not enough memory to decode StandBy photo", e);
            return null;
        } catch (Exception e) {
            android.util.Log.w("TvHome.StandBy", "Unable to load StandBy photo: " + uri, e);
            return null;
        }
    }
}
