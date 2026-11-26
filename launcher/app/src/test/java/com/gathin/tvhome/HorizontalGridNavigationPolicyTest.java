package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.view.KeyEvent;
import org.junit.Test;

public class HorizontalGridNavigationPolicyTest {
    @Test public void dockDownEntersGridAtMatchingHorizontalSlot() {
        HorizontalGridNavigationPolicy.Target target =
                HorizontalGridNavigationPolicy.fromDock(2, KeyEvent.KEYCODE_DPAD_DOWN, 5, 8, 4, true);
        assertFalse(target.dock);
        assertEquals(2, target.position);
    }

    @Test public void dockUpExitsToHeaderWithoutClassicVerticalSemantics() {
        HorizontalGridNavigationPolicy.Target target =
                HorizontalGridNavigationPolicy.fromDock(2, KeyEvent.KEYCODE_DPAD_UP, 5, 8, 4, true);
        assertTrue(target.dock);
        assertEquals(-1, target.position);
    }

    @Test public void firstGridRowUpReturnsToMatchingDockSlot() {
        HorizontalGridNavigationPolicy.Target target =
                HorizontalGridNavigationPolicy.fromGrid(2, KeyEvent.KEYCODE_DPAD_UP, 8, 4, 5);
        assertTrue(target.dock);
        assertEquals(2, target.position);
    }

    @Test public void gridMovementUsesHorizontalGridBoundaries() {
        HorizontalGridNavigationPolicy.Target left =
                HorizontalGridNavigationPolicy.fromGrid(4, KeyEvent.KEYCODE_DPAD_LEFT, 9, 4, 5);
        HorizontalGridNavigationPolicy.Target down =
                HorizontalGridNavigationPolicy.fromGrid(5, KeyEvent.KEYCODE_DPAD_DOWN, 9, 4, 5);
        assertFalse(left.dock);
        assertEquals(4, left.position);
        assertEquals(8, down.position);
    }
}
