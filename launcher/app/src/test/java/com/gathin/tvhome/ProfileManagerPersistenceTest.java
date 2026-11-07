package com.gathin.tvhome;

import static org.junit.Assert.*;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class ProfileManagerPersistenceTest {
    private Context context() { return RuntimeEnvironment.getApplication(); }

    @Test
    public void addRenameSwitchAndDeleteAreDurableAndProfileScoped() {
        Context context = context();
        ProfileManager.UserProfile initial = ProfileManager.getActiveProfile(context);
        ProfileManager.UserProfile child = ProfileManager.addProfile(context, "Child", 2, true);
        assertNotNull(child);
        assertTrue(ProfileManager.setActiveProfile(context, child.id));
        assertEquals(child.id, ProfileManager.getActiveProfile(context).id);
        assertTrue(ProfileManager.setProfileName(context, "Child Room"));
        assertEquals("Child Room", ProfileManager.getActiveProfile(context).name);
        assertTrue(ChildModeManager.setChildModeEnabled(context, child.id, true));
        assertTrue(ChildModeManager.replaceAppPolicyPackages(context, child.id, false,
                Collections.singleton("com.example.locked")));
        assertTrue(ChildModeManager.replaceAppPolicyPackages(context, child.id, true,
                Collections.singleton("com.example.hidden")));
        assertTrue(ProfileManager.deleteProfile(context, child.id));
        assertEquals(initial.id, ProfileManager.getActiveProfile(context).id);
        assertTrue(ChildModeManager.getLockedApps(context, child.id).isEmpty());
        assertTrue(ChildModeManager.getHiddenApps(context, child.id).isEmpty());
        assertFalse(ChildModeManager.isChildModeEnabled(context, child.id));
        assertFalse(ProfileManager.deleteProfile(context, initial.id));
    }

    @Test
    public void duplicatePersistedProfileIdsAreRepairedWithoutSharingIdentity() {
        Context context = context();
        SharedPreferences prefs = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE);
        String duplicate = "[{\"id\":\"same\",\"name\":\"One\",\"avatarIndex\":1},{\"id\":\"same\",\"name\":\"Two\",\"avatarIndex\":2}]";
        assertTrue(prefs.edit().putString("profiles_list_json", duplicate).commit());
        List<ProfileManager.UserProfile> profiles = ProfileManager.getAllProfiles(context);
        assertEquals(2, profiles.size());
        assertNotEquals(profiles.get(0).id, profiles.get(1).id);
        assertEquals(1, ProfileManager.indexOfProfile(profiles, profiles.get(1).id));
    }

    @Test
    public void unreadableProfileJsonIsPreservedBeforeFallbackRecovery() {
        Context context = context();
        SharedPreferences prefs = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE);
        String damaged = "{not-valid-json";
        assertTrue(prefs.edit().putString("profiles_list_json", damaged).commit());
        ProfileManager.UserProfile fallback = ProfileManager.getActiveProfile(context);
        assertNotNull(fallback);
        assertEquals(damaged, prefs.getString("profiles_list_json_corrupt_backup", null));
        assertTrue(prefs.getString("profiles_list_json", "").startsWith("["));
    }

    @Test
    public void customAvatarCommitAndBuiltInResetRemainProfileScoped() throws Exception {
        Context context = context();
        ProfileManager.UserProfile profile = ProfileManager.addProfile(context, "Photo", 0, false);
        assertNotNull(profile);
        File directory = new File(context.getFilesDir(), "profile_avatars");
        assertTrue(directory.mkdirs() || directory.isDirectory());
        File photo = new File(directory, "avatar_test.jpg");
        try (FileOutputStream output = new FileOutputStream(photo)) {
            output.write(new byte[]{1, 2, 3, 4});
        }
        assertTrue(ProfileManager.replaceCustomAvatar(context, profile.id, photo));
        ProfileManager.UserProfile saved = find(context, profile.id);
        assertEquals(photo.getCanonicalPath(), saved.customAvatarPath);
        assertTrue(ProfileManager.selectBuiltInAvatar(context, profile.id, 3));
        ProfileManager.UserProfile reset = find(context, profile.id);
        assertNull(reset.customAvatarPath);
        assertEquals(3, reset.avatarIndex);
    }

    @Test
    public void legacyChildPoliciesMigrateToActiveProfileWhileParentPinStaysGlobal() {
        Context context = context();
        ProfileManager.UserProfile active = ProfileManager.getActiveProfile(context);
        SharedPreferences prefs = context.getSharedPreferences("child_mode_prefs", Context.MODE_PRIVATE);
        assertTrue(prefs.edit()
                .putBoolean("child_mode_enabled", true)
                .putStringSet("locked_apps", Collections.singleton("com.example.legacy"))
                .putString("security_pin", "2468")
                .commit());

        assertTrue(ChildModeManager.isChildModeEnabled(context, active.id));
        assertTrue(ChildModeManager.getLockedApps(context, active.id).contains("com.example.legacy"));
        assertEquals("2468", ChildModeManager.getPin(context));
        assertTrue(prefs.getBoolean("profile_settings_migration_v1", false));
    }

    private ProfileManager.UserProfile find(Context context, String id) {
        for (ProfileManager.UserProfile profile : ProfileManager.getAllProfiles(context)) {
            if (id.equals(profile.id)) return profile;
        }
        return null;
    }
}
