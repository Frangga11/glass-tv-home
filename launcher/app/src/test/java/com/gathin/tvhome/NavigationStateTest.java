package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NavigationStateTest {

    @Test
    public void usesResolvedComponentPositionBeforeSavedSlot() {
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
    public void normalizesInvalidFocusKeys() {
        assertEquals(null, NavigationState.normalizeFocusKey(null));
        assertEquals(null, NavigationState.normalizeFocusKey("  "));
        assertEquals("com.example/.Main", NavigationState.normalizeFocusKey(" com.example/.Main "));
    }

    @Test
    public void rejectsUnknownSettingsTab() {
        assertEquals(NavigationState.DEFAULT_SETTINGS_TAB, NavigationState.normalizeSettingsTab(6));
        assertEquals(7, NavigationState.normalizeSettingsTab(7));
    }

}
