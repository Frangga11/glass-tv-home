package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DockPreferencesStateTest {
    @Test public void capacityIsRestrictedToFourFiveOrSix() {
        assertEquals(4,DockPreferences.normalizeCapacity(4));
        assertEquals(5,DockPreferences.normalizeCapacity(5));
        assertEquals(6,DockPreferences.normalizeCapacity(6));
        assertEquals(5,DockPreferences.normalizeCapacity(0));
        assertEquals(5,DockPreferences.normalizeCapacity(7));
        assertEquals(5,DockPreferences.normalizeCapacity(-1));
    }
    @Test public void orientationFallsBackSafely() {
        assertEquals(DockPreferences.Orientation.VERTICAL,
                DockPreferences.normalizeOrientation("VERTICAL"));
        assertEquals(DockPreferences.Orientation.HORIZONTAL,
                DockPreferences.normalizeOrientation("HORIZONTAL"));
        assertEquals(DockPreferences.Orientation.HORIZONTAL,
                DockPreferences.normalizeOrientation("sideways"));
        assertEquals(DockPreferences.Orientation.HORIZONTAL,
                DockPreferences.normalizeOrientation(null));
    }
    @Test public void dockMembershipIsBoundedAndNeverIncludesNegativePositions() {
        assertEquals(false,DockPreferences.isDockPosition(-1,5));
        assertEquals(true,DockPreferences.isDockPosition(0,5));
        assertEquals(true,DockPreferences.isDockPosition(4,5));
        assertEquals(false,DockPreferences.isDockPosition(5,5));
    }
}
