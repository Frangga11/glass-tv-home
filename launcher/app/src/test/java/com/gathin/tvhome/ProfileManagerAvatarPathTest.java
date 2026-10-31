package com.gathin.tvhome;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import org.junit.Test;

public class ProfileManagerAvatarPathTest {
    @Test
    public void acceptsOnlyDirectChildrenOfPrivateAvatarDirectory() {
        File avatarDirectory = new File("/data/user/0/com.gathin.tvhome/files/profile_avatars");
        assertTrue(ProfileManager.isPrivateAvatarFile(avatarDirectory, new File(avatarDirectory, "avatar_1.upload")));
        assertFalse(ProfileManager.isPrivateAvatarFile(avatarDirectory, new File(avatarDirectory, "../shared/photo.jpg")));
        assertFalse(ProfileManager.isPrivateAvatarFile(avatarDirectory, new File("/data/user/0/com.gathin.tvhome/files/photo.jpg")));
        assertFalse(ProfileManager.isPrivateAvatarFile(avatarDirectory, null));
    }
}
