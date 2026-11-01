package com.gathin.tvhome;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
    static final int MAX_PHOTO_COUNT = 100;
    static final long MAX_UPLOADED_BYTES = 512L * 1024L * 1024L;
    private static final long MIN_FREE_BYTES = 64L * 1024L * 1024L;

    private static final ExecutorService DECODE_EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "standby-photo-decoder");
        thread.setDaemon(true);
        return thread;
    });

    interface BitmapCallback {
        void onLoaded(Bitmap bitmap);
    }

    private StandbyPhotoStore() {}

    static synchronized List<Uri> getUris(Context context) {
        List<Uri> result = new ArrayList<>();
        if (context == null) return result;
        String raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_URIS, "");
        if (TextUtils.isEmpty(raw)) return result;
        Set<String> unique = new HashSet<>();
        for (String value : raw.split("\\n")) {
            if (value == null || value.isEmpty() || !unique.add(value)) continue;
            try {
                Uri uri = Uri.parse(value);
                if (uri != null) result.add(uri);
            } catch (Exception ignored) {
            }
        }
        return result;
    }

    static synchronized boolean saveUris(Context context, List<Uri> uris) {
        if (context == null) return false;
        List<String> values = new ArrayList<>();
        Set<String> unique = new HashSet<>();
        if (uris != null) {
            for (Uri uri : uris) {
                if (uri != null && unique.add(uri.toString())) values.add(uri.toString());
            }
        }
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putString(KEY_URIS, TextUtils.join("\n", values))
                .commit();
    }

    private static long getUploadedBytes(Context context) {
        long total = 0;
        for (Uri uri : getUris(context)) {
            if (isManagedFile(context, uri)) {
                try {
                    total += new File(uri.getPath()).length();
                } catch (Exception ignored) {
                }
            }
        }
        return total;
    }

    static synchronized boolean canAcceptUpload(Context context, long incomingBytes) {
        if (context == null || incomingBytes <= 0) return false;
        if (getPhotoCount(context) >= MAX_PHOTO_COUNT) return false;
        long used = getUploadedBytes(context);
        if (used + incomingBytes > MAX_UPLOADED_BYTES) return false;
        return context.getFilesDir().getUsableSpace() - incomingBytes >= MIN_FREE_BYTES;
    }

    enum UploadResult {
        ADDED,
        ALREADY_PRESENT,
        REJECTED,
        PERSISTENCE_FAILED
    }

    static synchronized UploadResult addUploadedFile(Context context, File imageFile) {
        if (context == null || imageFile == null || !imageFile.isFile()) return UploadResult.REJECTED;
        if (!isManagedFile(context, Uri.fromFile(imageFile))) return UploadResult.REJECTED;
        List<Uri> uris = getUris(context);
        Uri uploaded = Uri.fromFile(imageFile);
        SharedPreferences preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        if (uris.contains(uploaded)) {
            String existingUris = TextUtils.join("\n", toUriStrings(uris));
            return commitUploadedState(preferences, existingUris)
                    ? UploadResult.ALREADY_PRESENT : UploadResult.PERSISTENCE_FAILED;
        }
        if (uris.size() >= MAX_PHOTO_COUNT) return UploadResult.REJECTED;
        if (getUploadedBytes(context) + imageFile.length() > MAX_UPLOADED_BYTES) return UploadResult.REJECTED;
        if (context.getFilesDir().getUsableSpace() < MIN_FREE_BYTES) return UploadResult.REJECTED;
        uris.add(uploaded);

        // Collection membership and the mode that consumes it are one durable state transition.
        String serializedUris = TextUtils.join("\n", toUriStrings(uris));
        return commitUploadedState(preferences, serializedUris)
                ? UploadResult.ADDED : UploadResult.PERSISTENCE_FAILED;
    }

    static final class UriMergeResult {
        final List<String> values;
        final int addedCount;
        final int skippedCount;
        final boolean persisted;

        UriMergeResult(List<String> values, int addedCount, int skippedCount) {
            this(values, addedCount, skippedCount, true);
        }

        UriMergeResult(List<String> values, int addedCount, int skippedCount, boolean persisted) {
            this.values = values;
            this.addedCount = addedCount;
            this.skippedCount = skippedCount;
            this.persisted = persisted;
        }
    }

    /** Merges a picker result against the latest collection, not a stale UI snapshot. */
    static synchronized UriMergeResult mergeUriValues(List<String> current, List<String> additions, int limit) {
        List<String> merged = new ArrayList<>();
        Set<String> unique = new HashSet<>();
        if (current != null) {
            for (String value : current) {
                if (value != null && !value.isEmpty() && unique.add(value)) merged.add(value);
            }
        }
        int added = 0;
        int skipped = 0;
        if (additions != null) {
            for (String value : additions) {
                if (TextUtils.isEmpty(value) || !unique.add(value)) continue;
                if (merged.size() >= Math.max(0, limit)) {
                    skipped++;
                    continue;
                }
                merged.add(value);
                added++;
            }
        }
        return new UriMergeResult(merged, added, skipped);
    }

    static synchronized UriMergeResult appendUris(Context context, List<Uri> additions) {
        if (context == null) return new UriMergeResult(new ArrayList<>(), 0, 0, false);
        List<String> current = toUriStrings(getUris(context));
        List<String> incoming = toUriStrings(additions);
        UriMergeResult merged = mergeUriValues(current, incoming, MAX_PHOTO_COUNT);
        if (merged.addedCount == 0) return merged;
        SharedPreferences preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String serialized = TextUtils.join("\n", merged.values);
        if (!commitUploadedState(preferences, serialized)) {
            return new UriMergeResult(current, 0, merged.skippedCount, false);
        }
        return merged;
    }

    static boolean commitClearedState(SharedPreferences preferences) {
        return preferences != null && preferences.edit()
                .putString(KEY_URIS, "")
                .putBoolean(KEY_MODE, false)
                .commit();
    }

    /** Clears collection membership and photo mode as one durable transition. */
    static synchronized List<Uri> clearCollectionAndMode(Context context) {
        if (context == null) return null;
        List<Uri> previousUris = getUris(context);
        SharedPreferences preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        boolean saved = commitClearedState(preferences);
        if (!saved) return null;
        clearUploadedFiles(context);
        return previousUris;
    }

    private static List<String> toUriStrings(List<Uri> uris) {
        List<String> values = new ArrayList<>();
        Set<String> unique = new HashSet<>();
        if (uris != null) {
            for (Uri uri : uris) {
                if (uri != null && unique.add(uri.toString())) values.add(uri.toString());
            }
        }
        return values;
    }

    /** Commits the photo collection and its enabling mode through one SharedPreferences write. */
    static boolean commitUploadedState(SharedPreferences preferences, String serializedUris) {
        if (preferences == null || serializedUris == null) return false;
        return preferences.edit()
                .putString(KEY_URIS, serializedUris)
                .putBoolean(KEY_MODE, true)
                .commit();
    }

    static boolean isManagedFile(Context context, Uri uri) {
        if (context == null || uri == null || !"file".equalsIgnoreCase(uri.getScheme())) return false;
        try {
            File directory = new File(context.getFilesDir(), "standby_photos").getCanonicalFile();
            File candidate = new File(uri.getPath()).getCanonicalFile();
            return directory.equals(candidate.getParentFile())
                    && UploadFilePolicy.isSafeStoredImageName(candidate.getName())
                    && candidate.isFile();
        } catch (Exception ignored) {
            return false;
        }
    }

    /** Remove interrupted or orphaned QR uploads that are not referenced by the saved collection. */
    static void cleanupOrphanedUploads(Context context) {
        if (context == null) return;
        File directory = new File(context.getFilesDir(), "standby_photos");
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) return;
        Set<String> referenced = new HashSet<>();
        for (Uri uri : getUris(context)) {
            if (isManagedFile(context, uri)) {
                try {
                    referenced.add(new File(uri.getPath()).getCanonicalPath());
                } catch (Exception ignored) {
                }
            }
        }
        for (File file : files) {
            try {
                if (file.isFile() && !referenced.contains(file.getCanonicalPath())) file.delete();
            } catch (Exception ignored) {
            }
        }
    }

    static synchronized void clearUploadedFiles(Context context) {
        if (context == null) return;
        File directory = new File(context.getFilesDir(), "standby_photos");
        File[] files = directory.listFiles();
        if (files == null) return;
        for (File file : files) {
            try {
                if (file.isFile()) file.delete();
            } catch (Exception ignored) {
            }
        }
    }

    static boolean isPhotoMode(Context context) {
        return context != null && context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getBoolean(KEY_MODE, false);
    }

    static synchronized boolean setPhotoMode(Context context, boolean enabled) {
        if (context == null) return false;
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putBoolean(KEY_MODE, enabled).commit();
    }

    static int getPhotoCount(Context context) {
        return getUris(context).size();
    }

    /** Shared slide index so the in-app overlay and Android DreamService show the same photo. */
    static int getCurrentPhotoIndex(int photoCount) {
        return getPhotoIndexAtElapsedTime(photoCount, SystemClock.elapsedRealtime());
    }

    static int getPhotoIndexAtElapsedTime(int photoCount, long elapsedRealtimeMs) {
        if (photoCount <= 0 || elapsedRealtimeMs < 0) return -1;
        return (int) ((elapsedRealtimeMs / ROTATION_INTERVAL_MS) % photoCount);
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
            new Handler(Looper.getMainLooper()).post(() -> callback.onLoaded(bitmap));
        });
    }

    private static Bitmap applyExifOrientation(Context context, Uri uri, Bitmap bitmap) {
        try (InputStream in = openInputStream(context, uri)) {
            if (in == null) return bitmap;
            ExifInterface exif = new ExifInterface(in);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    break;
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.setScale(-1f, 1f);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.setScale(1f, -1f);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    matrix.setRotate(90);
                    matrix.postScale(-1f, 1f);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.setRotate(270);
                    matrix.postScale(-1f, 1f);
                    break;
                default:
                    return bitmap;
            }
            Bitmap corrected = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (corrected != bitmap) bitmap.recycle();
            return corrected;
        } catch (Exception ignored) {
            return bitmap;
        }
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
            Bitmap bitmap;
            try (InputStream in = openInputStream(context, uri)) {
                bitmap = in == null ? null : BitmapFactory.decodeStream(in, null, decode);
            }
            return bitmap == null ? null : applyExifOrientation(context, uri, bitmap);
        } catch (OutOfMemoryError e) {
            android.util.Log.e("TvHome.StandBy", "Not enough memory to decode StandBy photo", e);
            return null;
        } catch (Exception e) {
            android.util.Log.w("TvHome.StandBy", "Unable to load StandBy photo: " + uri, e);
            return null;
        }
    }
}
