package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClassicGridNavigationPolicyTest {
    @Test public void firstGridRowUpReturnsToDockAtSameColumn() {
        ClassicGridNavigationPolicy.Target target =
                ClassicGridNavigationPolicy.fromGrid(2, android.view.KeyEvent.KEYCODE_DPAD_UP, 4, 9, 3);
        assertTrue(target.dock);
        assertEquals(2, target.position);
    }

    @Test public void gridHorizontalEdgesDoNotWrap() {
        ClassicGridNavigationPolicy.Target left =
                ClassicGridNavigationPolicy.fromGrid(4, android.view.KeyEvent.KEYCODE_DPAD_LEFT, 4, 9, 4);
        ClassicGridNavigationPolicy.Target right =
                ClassicGridNavigationPolicy.fromGrid(8, android.view.KeyEvent.KEYCODE_DPAD_RIGHT, 4, 9, 4);
        assertFalse(left.dock);
        assertEquals(4, left.position);
        assertEquals(8, right.position);
    }

    @Test public void gridDownPreservesColumnAndUsesClassicIncompleteRowFallback() {
        ClassicGridNavigationPolicy.Target normal =
                ClassicGridNavigationPolicy.fromGrid(5, android.view.KeyEvent.KEYCODE_DPAD_DOWN, 4, 10, 4);
        ClassicGridNavigationPolicy.Target incomplete =
                ClassicGridNavigationPolicy.fromGrid(3, android.view.KeyEvent.KEYCODE_DPAD_DOWN, 4, 6, 4);
        assertEquals(9, normal.position);
        assertEquals(5, incomplete.position);
    }

    @Test public void finalGridRowDownIsAStableBoundary() {
        ClassicGridNavigationPolicy.Target target =
                ClassicGridNavigationPolicy.fromGrid(8, android.view.KeyEvent.KEYCODE_DPAD_DOWN, 4, 9, 4);
        assertEquals(8, target.position);
    }

    @Test public void dockDownMapsToGridAtSameSlot() {
        ClassicGridNavigationPolicy.Target target =
                ClassicGridNavigationPolicy.fromDock(2, android.view.KeyEvent.KEYCODE_DPAD_DOWN, 4, 7, 0);
        assertFalse(target.dock);
        assertEquals(2, target.position);
    }
}
