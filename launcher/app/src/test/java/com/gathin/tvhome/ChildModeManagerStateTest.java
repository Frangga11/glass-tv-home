package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ChildModeManagerStateTest {

    @Test
    public void concurrentPackageMutationsCanBeAppliedWithoutDroppingEarlierChanges() {
        Set<String> first = ChildModeManager.updatedPackageSet(
                Collections.<String>emptySet(), "com.example.one", true);
        Set<String> second = ChildModeManager.updatedPackageSet(
                first, "com.example.two", true);

        assertEquals(new HashSet<>(java.util.Arrays.asList(
                "com.example.one", "com.example.two")), second);
        assertFalse(first.contains("com.example.two"));
    }

    @Test
    public void packageMutationsAreScopedToTheRequestedProfileKey() {
        String firstProfileKey = ChildModeManager.profileKey("profile_one", "locked_apps");
        String secondProfileKey = ChildModeManager.profileKey("profile_two", "locked_apps");

        assertFalse(firstProfileKey.equals(secondProfileKey));
        Set<String> first = ChildModeManager.updatedPackageSet(
                Collections.<String>emptySet(), "com.example.tv", true);
        Set<String> second = ChildModeManager.updatedPackageSet(
                Collections.<String>emptySet(), "com.example.video", true);

        assertTrue(first.contains("com.example.tv"));
        assertFalse(first.contains("com.example.video"));
        assertTrue(second.contains("com.example.video"));
        assertFalse(second.contains("com.example.tv"));
    }

    @Test
    public void removingOnePackagePreservesOtherProfileEntries() {
        Set<String> current = new HashSet<>(java.util.Arrays.asList(
                "com.example.one", "com.example.two"));
        Set<String> updated = ChildModeManager.updatedPackageSet(current, "com.example.one", false);

        assertFalse(updated.contains("com.example.one"));
        assertTrue(updated.contains("com.example.two"));
        assertEquals(2, current.size());
    }
}
