package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NavigationStateTest {

    @Test
    public void resolvesStableComponentBeforeSavedSlot() {
        assertEquals(4, NavigationState.resolveHomePosition(4, 1, 8));
    }

    @Test
    public void fallsBackToSavedSlotWhenComponentDisappears() {
        assertEquals(3, NavigationState.resolveHomePosition(-1, 3, 8));
    }

    @Test
    public void clampsSavedSlotAfterAppListShrinks() {
        assertEquals(2, NavigationState.resolveHomePosition(-1, 9, 3));
    }

    @Test
    public void emptyAppListHasNoTilePosition() {
        assertEquals(-1, NavigationState.resolveHomePosition(-1, 4, 0));
    }

    @Test
    public void rejectsUnknownSettingsTab() {
        assertEquals(NavigationState.DEFAULT_SETTINGS_TAB, NavigationState.normalizeSettingsTab(6));
        assertEquals(7, NavigationState.normalizeSettingsTab(7));
    }

    @Test
    public void rejectsMalformedFocusPath() {
        assertNull(NavigationState.normalizeFocusPath("1/a/3"));
        assertNull(NavigationState.normalizeFocusPath("1//3"));
        assertEquals("1/2/3", NavigationState.normalizeFocusPath("1/2/3"));
    }
}
