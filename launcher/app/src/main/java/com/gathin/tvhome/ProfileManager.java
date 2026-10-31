package com.gathin.tvhome;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.media.ExifInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * Manages multiple user profiles, active profile selection, and animal avatars for Glass TV.
 */
public final class ProfileManager {
    private static final String PREFS_NAME = "profile_prefs";
    private static final String KEY_LEGACY_NAME = "profile_name";
    private static final String KEY_LEGACY_AVATAR = "avatar_index";
    private static final String KEY_PROFILES_JSON = "profiles_list_json";
    private static final String KEY_ACTIVE_PROFILE_ID = "active_profile_id";

    public static final String[] AVATAR_NAMES = {
            "Fox", "Panda", "Tiger", "Lion", "Frog", "Koala", "Octopus"
    };

    public static final int[] AVATAR_RES_IDS = {
            R.drawable.avatar_fox,
            R.drawable.avatar_panda,
            R.drawable.avatar_tiger,
            R.drawable.avatar_lion,
            R.drawable.avatar_frog,
            R.drawable.avatar_koala,
            R.drawable.avatar_octopus
    };

    public static class UserProfile {
        public String id;
        public String name;
        public int avatarIndex;
        public boolean isChild;
        public String customAvatarPath;

        public UserProfile(String id, String name, int avatarIndex, boolean isChild) {
            this(id, name, avatarIndex, isChild, null);
        }

        public UserProfile(String id, String name, int avatarIndex, boolean isChild, String customAvatarPath) {
            this.id = id != null ? id : "user_" + System.currentTimeMillis();
            this.name = name != null && !name.trim().isEmpty() ? name.trim() : "User";
            this.avatarIndex = (avatarIndex >= 0 && avatarIndex < AVATAR_RES_IDS.length) ? avatarIndex : 1;
            this.isChild = isChild;
            this.customAvatarPath = customAvatarPath;
        }

        public JSONObject toJson() {
            JSONObject obj = new JSONObject();
            try {
                obj.put("id", id);
                obj.put("name", name);
                obj.put("avatarIndex", avatarIndex);
                obj.put("isChild", isChild);
                if (customAvatarPath != null && !customAvatarPath.trim().isEmpty()) obj.put("customAvatarPath", customAvatarPath);
            } catch (JSONException ignored) {}
            return obj;
        }

        public static UserProfile fromJson(JSONObject obj) {
            if (obj == null) return new UserProfile("default", "User", 1, false);
            String id = obj.optString("id", "user_" + System.currentTimeMillis());
            String name = obj.optString("name", "User");
            int avatarIndex = obj.optInt("avatarIndex", 1);
            boolean isChild = obj.optBoolean("isChild", false);
            String customAvatarPath = obj.optString("customAvatarPath", null);
            if (customAvatarPath != null && customAvatarPath.trim().isEmpty()) customAvatarPath = null;
            return new UserProfile(id, name, avatarIndex, isChild, customAvatarPath);
        }
    }

    private ProfileManager() {}

    public static Drawable getCircularAvatarDrawable(Context context, int avatarIndex, int sizePx, boolean isChild) {
        if (context == null || sizePx <= 0) return null;
        try {
            int resId = getAvatarResId(avatarIndex);
            Bitmap src = BitmapFactory.decodeResource(context.getResources(), resId);
            if (src == null) return null;
            Bitmap output = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

            float radius = sizePx / 2f;
            float inset = 2f * context.getResources().getDisplayMetrics().density;
            Rect srcRect = new Rect(0, 0, src.getWidth(), src.getHeight());
            RectF dstRect = new RectF(inset, inset, sizePx - inset, sizePx - inset);

            // Clip to circle
            Path path = new Path();
            path.addCircle(radius, radius, radius - inset, Path.Direction.CW);
            canvas.save();
            canvas.clipPath(path);
            canvas.drawBitmap(src, srcRect, dstRect, paint);
            canvas.restore();

            // Border ring: Amber for Child Mode, Cyan/White for Adult
            Paint ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ringPaint.setStyle(Paint.Style.STROKE);
            ringPaint.setStrokeWidth(inset);
            ringPaint.setColor(isChild ? 0xFFF59E0B : 0xFF38BDF8);
            canvas.drawCircle(radius, radius, radius - inset / 2f, ringPaint);

            return new BitmapDrawable(context.getResources(), output);
        } catch (Exception e) {
            return null;
        }
    }

    /** Returns the active profile image, preferring its private locally stored photo. */
    public static Drawable getAvatarDrawable(Context context, UserProfile profile, int sizePx, boolean isChild) {
        if (context == null || profile == null || sizePx <= 0) return null;
        try {
            if (profile.customAvatarPath != null) {
                File avatarDir = new File(context.getFilesDir(), "profile_avatars").getCanonicalFile();
                File photo = new File(profile.customAvatarPath).getCanonicalFile();
                if (photo.getParentFile().equals(avatarDir) && photo.isFile() && photo.length() > 0) {
                    BitmapFactory.Options bounds = new BitmapFactory.Options();
                    bounds.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(photo.getAbsolutePath(), bounds);
                    if (bounds.outWidth > 0 && bounds.outHeight > 0) {
                        int sample = 1;
                        while (bounds.outWidth / (sample * 2) > sizePx * 2
                                || bounds.outHeight / (sample * 2) > sizePx * 2) sample *= 2;
                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        opts.inSampleSize = sample;
                        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap src = BitmapFactory.decodeFile(photo.getAbsolutePath(), opts);
                        if (src != null) {
                            int orientation = ExifInterface.ORIENTATION_NORMAL;
                            try {
                                ExifInterface exif = new ExifInterface(photo.getAbsolutePath());
                                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                            } catch (Exception ignored) {}
                            Matrix matrix = new Matrix();
                            switch (orientation) {
                                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL: matrix.setScale(-1, 1); break;
                                case ExifInterface.ORIENTATION_ROTATE_180: matrix.setRotate(180); break;
                                case ExifInterface.ORIENTATION_FLIP_VERTICAL: matrix.setRotate(180); matrix.postScale(-1, 1); break;
                                case ExifInterface.ORIENTATION_TRANSPOSE: matrix.setRotate(90); matrix.postScale(-1, 1); break;
                                case ExifInterface.ORIENTATION_ROTATE_90: matrix.setRotate(90); break;
                                case ExifInterface.ORIENTATION_TRANSVERSE: matrix.setRotate(-90); matrix.postScale(-1, 1); break;
                                case ExifInterface.ORIENTATION_ROTATE_270: matrix.setRotate(-90); break;
                                default: break;
                            }
                            Bitmap oriented = matrix.isIdentity() ? src
                                    : Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
                            if (oriented != src) src.recycle();
                            Bitmap output = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(output);
                            float inset = 2f * context.getResources().getDisplayMetrics().density;
                            float radius = sizePx / 2f;
                            Path clip = new Path();
                            clip.addCircle(radius, radius, radius - inset, Path.Direction.CW);
                            canvas.save();
                            canvas.clipPath(clip);
                            float scale = Math.max((sizePx - 2 * inset) / (float) oriented.getWidth(),
                                    (sizePx - 2 * inset) / (float) oriented.getHeight());
                            float w = oriented.getWidth() * scale, h = oriented.getHeight() * scale;
                            RectF dst = new RectF((sizePx - w) / 2f, (sizePx - h) / 2f,
                                    (sizePx + w) / 2f, (sizePx + h) / 2f);
                            canvas.drawBitmap(oriented, null, dst, new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
                            canvas.restore();
                            oriented.recycle();
                            Paint ring = new Paint(Paint.ANTI_ALIAS_FLAG);
                            ring.setStyle(Paint.Style.STROKE);
                            ring.setStrokeWidth(inset);
                            ring.setColor(isChild ? 0xFFF59E0B : 0xFF38BDF8);
                            canvas.drawCircle(radius, radius, radius - inset / 2f, ring);
                            return new BitmapDrawable(context.getResources(), output);
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return getCircularAvatarDrawable(context, profile.avatarIndex, sizePx, isChild);
    }

    /** Atomically switches the profile reference, then deletes the superseded private photo. */
    public static synchronized boolean replaceCustomAvatar(Context context, String profileId, File newPhoto) {
        if (context == null || profileId == null || newPhoto == null || !newPhoto.isFile() || newPhoto.length() <= 0) return false;
        try {
            File avatarDir = new File(context.getFilesDir(), "profile_avatars").getCanonicalFile();
            File candidate = newPhoto.getCanonicalFile();
            if (!candidate.getParentFile().equals(avatarDir)) return false;
            List<UserProfile> list = getAllProfiles(context);
            UserProfile target = null;
            for (UserProfile profile : list) if (profileId.equals(profile.id)) { target = profile; break; }
            if (target == null) return false;
            String oldPath = target.customAvatarPath;
            target.customAvatarPath = candidate.getAbsolutePath();
            JSONArray arr = new JSONArray();
            for (UserProfile profile : list) arr.put(profile.toJson());
            boolean saved = getPrefs(context).edit().putString(KEY_PROFILES_JSON, arr.toString()).commit();
            if (!saved) {
                target.customAvatarPath = oldPath;
                return false;
            }
            if (oldPath != null && !oldPath.equals(candidate.getAbsolutePath())) {
                // Do not remove a photo if another profile still references the same file.
                deleteAvatarIfUnreferenced(context, list, profileId, oldPath);
            }
            cleanupOrphanedAvatarUploads(context);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static synchronized boolean clearCustomAvatar(Context context, String profileId) {
        if (context == null || profileId == null) return false;
        List<UserProfile> list = getAllProfiles(context);
        for (UserProfile profile : list) {
            if (profileId.equals(profile.id)) {
                String oldPath = profile.customAvatarPath;
                profile.customAvatarPath = null;
                JSONArray arr = new JSONArray();
                for (UserProfile item : list) arr.put(item.toJson());
                boolean saved = getPrefs(context).edit().putString(KEY_PROFILES_JSON, arr.toString()).commit();
                if (!saved) { profile.customAvatarPath = oldPath; return false; }
                if (oldPath != null) deleteAvatarIfUnreferenced(context, list, profileId, oldPath);
                cleanupOrphanedAvatarUploads(context);
                return true;
            }
        }
        return false;
    }

    private static void deleteAvatarIfUnreferenced(Context context, List<UserProfile> profiles,
                                                    String excludedProfileId, String path) {
        if (context == null || profiles == null || path == null) return;
        try {
            File avatarDir = new File(context.getFilesDir(), "profile_avatars").getCanonicalFile();
            File candidate = new File(path).getCanonicalFile();
            if (!candidate.getParentFile().equals(avatarDir)) return;
            for (UserProfile profile : profiles) {
                if (profile == null || profile.id == null || profile.id.equals(excludedProfileId)
                        || profile.customAvatarPath == null) continue;
                if (candidate.equals(new File(profile.customAvatarPath).getCanonicalFile())) return;
            }
            if (candidate.isFile()) candidate.delete();
        } catch (Exception ignored) {}
    }

    /** Removes unreferenced avatar files and abandoned uploads left by a force-stop or power loss. */
    public static synchronized void cleanupOrphanedAvatarUploads(Context context) {
        if (context == null) return;
        File dir = new File(context.getFilesDir(), "profile_avatars");
        File[] files = dir.listFiles();
        if (files == null) return;
        Set<String> referenced = new HashSet<>();
        for (UserProfile profile : getAllProfiles(context)) {
            if (profile.customAvatarPath == null) continue;
            try { referenced.add(new File(profile.customAvatarPath).getCanonicalPath()); }
            catch (Exception ignored) {}
        }
        long cutoff = System.currentTimeMillis() - 10L * 60L * 1000L;
        for (File file : files) {
            if (!file.isFile()) continue;
            try {
                String path = file.getCanonicalPath();
                if (referenced.contains(path)) continue;
                String name = file.getName().toLowerCase(java.util.Locale.ROOT);
                if (name.endsWith(".upload") && file.lastModified() >= cutoff) continue;
                // This directory is dedicated to profile avatars: unreferenced images and stale
                // .tmp/.bak/.upload files are safe to remove.
                file.delete();
            } catch (Exception ignored) {}
        }
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized List<UserProfile> getAllProfiles(Context context) {
        List<UserProfile> list = new ArrayList<>();
        if (context == null) {
            list.add(new UserProfile("default", "Xiaomi TV", 1, false));
            return list;
        }

        SharedPreferences sp = getPrefs(context);
        String json = sp.getString(KEY_PROFILES_JSON, null);

        if (json != null && !json.trim().isEmpty()) {
            try {
                JSONArray arr = new JSONArray(json);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    list.add(UserProfile.fromJson(obj));
                }
            } catch (JSONException e) {
                list.clear();
            }
        }

        if (list.isEmpty()) {
            // Seed initial default profile from legacy prefs or TV model
            String legacyName = sp.getString(KEY_LEGACY_NAME, null);
            if (legacyName == null || legacyName.trim().isEmpty()) {
                String model = Build.MODEL;
                if (model != null && !model.isEmpty()) {
                    if (model.contains("MiTV") || model.contains("MIBOX")) legacyName = "Xiaomi TV";
                    else if (model.contains("Chromecast")) legacyName = "Chromecast TV";
                    else if (model.contains("BRAVIA")) legacyName = "Sony BRAVIA TV";
                    else legacyName = model + "'s TV";
                } else {
                    legacyName = "Glass TV";
                }
            }
            int legacyAvatar = sp.getInt(KEY_LEGACY_AVATAR, 1);
            UserProfile defaultProfile = new UserProfile("profile_default", legacyName, legacyAvatar, false);
            list.add(defaultProfile);
            saveProfiles(context, list);
            sp.edit().putString(KEY_ACTIVE_PROFILE_ID, defaultProfile.id).apply();
        }

        return list;
    }

    private static synchronized void saveProfiles(Context context, List<UserProfile> list) {
        if (context == null || list == null) return;
        JSONArray arr = new JSONArray();
        for (UserProfile p : list) {
            arr.put(p.toJson());
        }
        getPrefs(context).edit().putString(KEY_PROFILES_JSON, arr.toString()).apply();
    }

    public static synchronized UserProfile getActiveProfile(Context context) {
        List<UserProfile> list = getAllProfiles(context);
        if (context == null || list.isEmpty()) {
            return new UserProfile("default", "Xiaomi TV", 1, false);
        }

        String activeId = getPrefs(context).getString(KEY_ACTIVE_PROFILE_ID, null);
        if (activeId != null) {
            for (UserProfile p : list) {
                if (activeId.equals(p.id)) return p;
            }
        }

        UserProfile first = list.get(0);
        getPrefs(context).edit().putString(KEY_ACTIVE_PROFILE_ID, first.id).apply();
        return first;
    }

    public static synchronized void setActiveProfile(Context context, String profileId) {
        if (context == null || profileId == null) return;
        List<UserProfile> list = getAllProfiles(context);
        for (UserProfile p : list) {
            if (profileId.equals(p.id)) {
                getPrefs(context).edit()
                        .putString(KEY_ACTIVE_PROFILE_ID, p.id)
                        .putString(KEY_LEGACY_NAME, p.name)
                        .putInt(KEY_LEGACY_AVATAR, p.avatarIndex)
                        .apply();
                return;
            }
        }
    }

    public static synchronized UserProfile addProfile(Context context, String name, int avatarIndex, boolean isChild) {
        if (context == null) return null;
        List<UserProfile> list = getAllProfiles(context);
        String id = "user_" + System.currentTimeMillis();
        UserProfile newProf = new UserProfile(id, name, avatarIndex, isChild);
        list.add(newProf);
        saveProfiles(context, list);
        return newProf;
    }

    public static synchronized boolean deleteProfile(Context context, String profileId) {
        if (context == null || profileId == null) return false;
        List<UserProfile> list = getAllProfiles(context);
        if (list.size() <= 1) return false; // Cannot delete the last remaining profile

        UserProfile toRemove = null;
        for (UserProfile p : list) {
            if (profileId.equals(p.id)) {
                toRemove = p;
                break;
            }
        }

        if (toRemove == null) return false;
        list.remove(toRemove);
        saveProfiles(context, list);
        // Remove this profile's parental-control keys but preserve the device-wide parent PIN.
        ChildModeManager.clearProfileSettings(context, profileId);
        // The cleanup routine deletes only unreferenced files inside the private avatar directory.
        cleanupOrphanedAvatarUploads(context);

        String activeId = getPrefs(context).getString(KEY_ACTIVE_PROFILE_ID, "");
        if (profileId.equals(activeId)) {
            // Fall back to first remaining profile
            setActiveProfile(context, list.get(0).id);
        }
        return true;
    }

    public static synchronized void updateProfile(Context context, String profileId, String newName, int newAvatarIndex, boolean isChild) {
        if (context == null || profileId == null) return;
        List<UserProfile> list = getAllProfiles(context);
        for (UserProfile p : list) {
            if (profileId.equals(p.id)) {
                if (newName != null && !newName.trim().isEmpty()) p.name = newName.trim();
                if (newAvatarIndex >= 0 && newAvatarIndex < AVATAR_RES_IDS.length) p.avatarIndex = newAvatarIndex;
                p.isChild = isChild;
                saveProfiles(context, list);

                String activeId = getPrefs(context).getString(KEY_ACTIVE_PROFILE_ID, "");
                if (profileId.equals(activeId)) {
                    getPrefs(context).edit()
                            .putString(KEY_LEGACY_NAME, p.name)
                            .putInt(KEY_LEGACY_AVATAR, p.avatarIndex)
                            .apply();
                }
                return;
            }
        }
    }

    // --- Backward-compatible convenience methods ---
    public static String getProfileName(Context context) {
        return getActiveProfile(context).name;
    }

    public static void setProfileName(Context context, String name) {
        if (context == null) return;
        UserProfile active = getActiveProfile(context);
        updateProfile(context, active.id, name, active.avatarIndex, active.isChild);
    }

    public static int getAvatarIndex(Context context) {
        return getActiveProfile(context).avatarIndex;
    }

    public static void setAvatarIndex(Context context, int index) {
        if (context == null) return;
        UserProfile active = getActiveProfile(context);
        updateProfile(context, active.id, active.name, index, active.isChild);
    }

    public static int getAvatarResId(int index) {
        if (index < 0 || index >= AVATAR_RES_IDS.length) return R.drawable.avatar_panda;
        return AVATAR_RES_IDS[index];
    }
}
