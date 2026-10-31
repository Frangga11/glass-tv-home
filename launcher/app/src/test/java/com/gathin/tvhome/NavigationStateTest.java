package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void clampsInvalidResolvedPosition() {
        assertEquals(0, NavigationState.resolveHomePosition(-1, -4, 5));
        assertEquals(4, NavigationState.resolveHomePosition(99, 1, 5));
    }

    @Test
    public void acceptsOnlyKnownSemanticTopPillKeys() {
        assertEquals("inputs", NavigationState.normalizeTopPillKey("inputs"));
        assertEquals("wifi", NavigationState.normalizeTopPillKey("wifi"));
        assertEquals("assistant", NavigationState.normalizeTopPillKey("assistant"));
        assertEquals("candy_box", NavigationState.normalizeTopPillKey("candy_box"));
        assertEquals("settings", NavigationState.normalizeTopPillKey("settings"));
        assertNull(NavigationState.normalizeTopPillKey(null));
        assertNull(NavigationState.normalizeTopPillKey("btnSettings"));
        assertNull(NavigationState.normalizeTopPillKey("unknown"));
    }

    @Test
    public void acceptsAllCurrentSettingsTabsAndRejectsUnknownValues() {
        for (int tab = 0; tab <= 5; tab++) {
            assertEquals(tab, NavigationState.normalizeSettingsTab(tab));
        }
        assertEquals(7, NavigationState.normalizeSettingsTab(7));
        assertEquals(NavigationState.DEFAULT_SETTINGS_TAB, NavigationState.normalizeSettingsTab(-1));
        assertEquals(NavigationState.DEFAULT_SETTINGS_TAB, NavigationState.normalizeSettingsTab(6));
        assertEquals(NavigationState.DEFAULT_SETTINGS_TAB, NavigationState.normalizeSettingsTab(8));
    }

}
