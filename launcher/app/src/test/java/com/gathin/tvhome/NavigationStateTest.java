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
        assertNull(NavigationState.normalizeFocusKey(null));
        assertNull(NavigationState.normalizeFocusKey("  "));
        assertNull(NavigationState.normalizeFocusKey(new String(new char[513]).replace('\0', 'x')));
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
        int[] validTabs = {
                NavigationState.TAB_PROFILE,
                NavigationState.TAB_HOME_SCREEN,
                NavigationState.TAB_APPEARANCE,
                NavigationState.TAB_STATUS_WEATHER,
                NavigationState.TAB_EFFECTS,
                NavigationState.TAB_STANDBY,
                NavigationState.TAB_DEVICE_SETTINGS
        };
        for (int tab : validTabs) {
            assertEquals(tab, NavigationState.normalizeSettingsTab(tab));
        }
        assertEquals(NavigationState.DEFAULT_SETTINGS_TAB, NavigationState.normalizeSettingsTab(-1));
        assertEquals(NavigationState.DEFAULT_SETTINGS_TAB, NavigationState.normalizeSettingsTab(6));
        assertEquals(NavigationState.DEFAULT_SETTINGS_TAB, NavigationState.normalizeSettingsTab(8));
    }

}
