package com.gathin.tvhome;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Persistent, private photo collection used only by the StandBy screensaver. */
final class StandbyPhotoStore {
    private static final String PREFS = "standby_prefs";
    private static final String KEY_URIS = "photo_uris";
    private static final String KEY_MODE = "display_mode";
    static final long ROTATION_INTERVAL_MS = 20_000L;

    private static final ExecutorService DECODE_EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "standby-photo-decoder");
        thread.setDaemon(true);
        return thread;
    });
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    interface BitmapCallback {
        void onLoaded(Bitmap bitmap);
    }

    private StandbyPhotoStore() {}

    static List<Uri> getUris(Context context) {
        List<Uri> result = new ArrayList<>();
        if (context == null) return result;
        String raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_URIS, "");
        if (TextUtils.isEmpty(raw)) return result;
        Set<String> unique = new HashSet<>();
        for (String value : raw.split("\\n")) {
            if (TextUtils.isEmpty(value) || !unique.add(value)) continue;
            try {
                Uri uri = Uri.parse(value);
                if (uri != null) result.add(uri);
            } catch (Exception ignored) {
            }
        }
        return result;
    }

    static void saveUris(Context context, List<Uri> uris) {
        if (context == null) return;
        List<String> values = new ArrayList<>();
        Set<String> unique = new HashSet<>();
        if (uris != null) {
            for (Uri uri : uris) {
                if (uri != null && unique.add(uri.toString())) values.add(uri.toString());
            }
        }
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putString(KEY_URIS, TextUtils.join("\n", values))
                .apply();
    }

    static boolean addUploadedFile(Context context, File imageFile) {
        if (context == null || imageFile == null || !imageFile.isFile()) return false;
        if (!isManagedFile(context, Uri.fromFile(imageFile))) return false;
        List<Uri> uris = getUris(context);
        Uri uploaded = Uri.fromFile(imageFile);
        if (!uris.contains(uploaded)) uris.add(uploaded);
        saveUris(context, uris);
        return true;
    }

    static boolean isManagedFile(Context context, Uri uri) {
        if (context == null || uri == null || !"file".equalsIgnoreCase(uri.getScheme())) return false;
        try {
            File directory = new File(context.getFilesDir(), "standby_photos").getCanonicalFile();
            File candidate = new File(uri.getPath()).getCanonicalFile();
            String prefix = directory.getPath() + File.separator;
            return candidate.getPath().startsWith(prefix) && candidate.isFile();
        } catch (Exception ignored) {
            return false;
        }
    }

    /** Delete only app-owned QR uploads that are no longer selected; never delete user originals. */
    static void deleteRemovedUploadedFiles(Context context, List<Uri> oldUris, List<Uri> retainedUris) {
        if (context == null || oldUris == null) return;
        Set<String> retained = new HashSet<>();
        if (retainedUris != null) {
            for (Uri uri : retainedUris) if (uri != null) retained.add(uri.toString());
        }
        for (Uri uri : oldUris) {
            if (uri != null && !retained.contains(uri.toString()) && isManagedFile(context, uri)) {
                try {
                    new File(uri.getPath()).delete();
                } catch (Exception ignored) {
                }
            }
        }
    }

    static void clearUploadedFiles(Context context) {
        if (context == null) return;
        deleteRemovedUploadedFiles(context, getUris(context), new ArrayList<>());
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

    /** Shared slide index so the in-app overlay and Android DreamService show the same photo. */
    static int getCurrentPhotoIndex(Context context) {
        int count = getPhotoCount(context);
        if (count <= 0) return -1;
        return (int) ((SystemClock.elapsedRealtime() / ROTATION_INTERVAL_MS) % count);
    }

    static int calculateInSampleSize(int width, int height, int targetWidth, int targetHeight) {
        if (width <= 0 || height <= 0 || targetWidth <= 0 || targetHeight <= 0) return 1;
        int sourceMax = Math.max(width, height);
        int targetMax = Math.max(targetWidth, targetHeight);
        int sample = 1;
        while (sourceMax / (sample * 2) >= targetMax) sample *= 2;
        return Math.max(1, sample);
    }

    private static InputStream openInputStream(Context context, Uri uri) throws Exception {
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            return new FileInputStream(new File(uri.getPath()));
        }
        return context.getContentResolver().openInputStream(uri);
    }

    /** Decode away from the UI thread; a phone photo must not freeze TV navigation. */
    static void loadBitmapAsync(Context context, Uri uri, BitmapCallback callback) {
        if (context == null || uri == null || callback == null) return;
        Context appContext = context.getApplicationContext();
        DECODE_EXECUTOR.execute(() -> {
            Bitmap bitmap = loadBitmap(appContext, uri);
            MAIN_HANDLER.post(() -> callback.onLoaded(bitmap));
        });
    }

    /** Decode near screen resolution using RGB_565 to limit memory on Android TV sticks. */
    static Bitmap loadBitmap(Context context, Uri uri) {
        if (context == null || uri == null) return null;
        try {
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            try (InputStream in = openInputStream(context, uri)) {
                if (in == null) return null;
                BitmapFactory.decodeStream(in, null, bounds);
            }
            if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null;

            int targetW = Math.max(1, context.getResources().getDisplayMetrics().widthPixels);
            int targetH = Math.max(1, context.getResources().getDisplayMetrics().heightPixels);
            BitmapFactory.Options decode = new BitmapFactory.Options();
            decode.inSampleSize = calculateInSampleSize(bounds.outWidth, bounds.outHeight, targetW, targetH);
            decode.inPreferredConfig = Bitmap.Config.RGB_565;
            decode.inDither = true;
            try (InputStream in = openInputStream(context, uri)) {
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
