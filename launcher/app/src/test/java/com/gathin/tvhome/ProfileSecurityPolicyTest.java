package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ProfileSecurityPolicyTest {

    @Test
    public void leavingChildProfileRequiresParentPin() {
        assertTrue(ChildModeManager.requiresParentPinToSwitch(true));
    }

    @Test
    public void switchingBetweenChildProfilesAlsoRequiresParentPin() {
        // Different child profiles can have different app allow-lists.
        // Requiring the PIN prevents switching to a less restricted child profile.
        assertTrue(ChildModeManager.requiresParentPinToSwitch(true));
    }

    @Test
    public void switchingFromAdultProfileDoesNotRequireParentPin() {
        assertFalse(ChildModeManager.requiresParentPinToSwitch(false));
    }

    @Test
    public void preferenceKeysAreIsolatedByProfileId() {
        String childLocked = ChildModeManager.profileKey("child_1", "locked_apps");
        String adultLocked = ChildModeManager.profileKey("adult_1", "locked_apps");
        String childHidden = ChildModeManager.profileKey("child_1", "hidden_apps");

        assertFalse(childLocked.equals(adultLocked));
        assertFalse(childLocked.equals(childHidden));
        assertEquals(childLocked, ChildModeManager.profileKey("child_1", "locked_apps"));
    }
}
