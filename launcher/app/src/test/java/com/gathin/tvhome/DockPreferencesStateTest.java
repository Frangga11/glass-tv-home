package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DockPreferencesStateTest {
    @Test
    public void capacityIsRestrictedToFourFiveOrSix() {
        assertEquals(4, DockPreferences.normalizeCapacity(4));
        assertEquals(5, DockPreferences.normalizeCapacity(5));
        assertEquals(6, DockPreferences.normalizeCapacity(6));
        assertEquals(5, DockPreferences.normalizeCapacity(0));
        assertEquals(5, DockPreferences.normalizeCapacity(7));
        assertEquals(5, DockPreferences.normalizeCapacity(-1));
    }

    @Test
    public void allSixOrientationAndCapacityCombinationsAreSupported() {
        for (DockPreferences.Orientation orientation : DockPreferences.Orientation.values()) {
            assertEquals(orientation, DockPreferences.normalizeOrientation(orientation.name()));
            for (int capacity : new int[]{4, 5, 6}) {
                assertEquals(capacity, DockPreferences.normalizeCapacity(capacity));
            }
        }
    }

    @Test
    public void orientationFallsBackSafely() {
        assertEquals(DockPreferences.Orientation.VERTICAL,
                DockPreferences.normalizeOrientation("VERTICAL"));
        assertEquals(DockPreferences.Orientation.HORIZONTAL,
                DockPreferences.normalizeOrientation("HORIZONTAL"));
        assertEquals(DockPreferences.Orientation.HORIZONTAL,
                DockPreferences.normalizeOrientation("sideways"));
        assertEquals(DockPreferences.Orientation.HORIZONTAL,
                DockPreferences.normalizeOrientation(null));
    }

    @Test
    public void pinnedKeysAreTrimmedDeduplicatedAndEmptyOverrideIsPreserved() {
        assertEquals(java.util.Arrays.asList("pkg/.A", "pkg/.B"),
                DockPreferences.normalizePinnedKeys(java.util.Arrays.asList(
                        "pkg/.A", " ", "pkg/.A", null, "pkg/.B")));
        assertTrue(DockPreferences.normalizePinnedKeys(java.util.Collections.emptyList()).isEmpty());
    }

    @Test
    public void dockMembershipIsBoundedAndNeverIncludesNegativePositions() {
        assertFalse(DockPreferences.isDockPosition(-1, 5));
        assertTrue(DockPreferences.isDockPosition(0, 5));
        assertTrue(DockPreferences.isDockPosition(4, 5));
        assertFalse(DockPreferences.isDockPosition(5, 5));
    }
}
