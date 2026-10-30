package com.gathin.tvhome;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

        public UserProfile(String id, String name, int avatarIndex, boolean isChild) {
            this.id = id != null ? id : "user_" + System.currentTimeMillis();
            this.name = name != null && !name.trim().isEmpty() ? name.trim() : "User";
            this.avatarIndex = (avatarIndex >= 0 && avatarIndex < AVATAR_RES_IDS.length) ? avatarIndex : 1;
            this.isChild = isChild;
        }

        public JSONObject toJson() {
            JSONObject obj = new JSONObject();
            try {
                obj.put("id", id);
                obj.put("name", name);
                obj.put("avatarIndex", avatarIndex);
                obj.put("isChild", isChild);
            } catch (JSONException ignored) {}
            return obj;
        }

        public static UserProfile fromJson(JSONObject obj) {
            if (obj == null) return new UserProfile("default", "User", 1, false);
            String id = obj.optString("id", "user_" + System.currentTimeMillis());
            String name = obj.optString("name", "User");
            int avatarIndex = obj.optInt("avatarIndex", 1);
            boolean isChild = obj.optBoolean("isChild", false);
            return new UserProfile(id, name, avatarIndex, isChild);
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
