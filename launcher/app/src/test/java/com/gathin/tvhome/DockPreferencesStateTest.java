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
        assertEquals(DockPreferences.Orientation.VERTICAL,
                DockPreferences.normalizeOrientation("sideways"));
        assertEquals(DockPreferences.Orientation.VERTICAL,
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

    @Test
    public void carouselTailIsZeroWhenEveryAppFitsInTheVisibleCapacity() {
        // Five 160px slots plus two 100px centered insets fill the 1000px viewport.
        assertEquals(0, DockGeometry.horizontalCarouselTailInsetPx(1000, 160, 5, 5, 100));
        assertEquals(0, DockGeometry.horizontalCarouselTailInsetPx(1000, 160, 3, 5, 260));
    }

    @Test
    public void carouselTailAllowsTheLastAppToSettleAtTheSecondVisibleSlot() {
        int viewportWidth = 1000;
        int slotWidth = 160;
        int itemCount = 10;
        int capacity = 5;
        int edgeInset = 100;
        int tail = DockGeometry.horizontalCarouselTailInsetPx(
                viewportWidth, slotWidth, itemCount, capacity, edgeInset);
        assertEquals(480, tail);
        int maximumScroll = itemCount * slotWidth + 2 * edgeInset + tail - viewportWidth;
        assertEquals((itemCount - 2) * slotWidth, maximumScroll);
    }
}
