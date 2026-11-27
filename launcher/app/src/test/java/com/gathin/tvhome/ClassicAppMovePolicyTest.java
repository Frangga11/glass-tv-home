package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** Contract tests ported from Home Universal Classic Move Mode policy. */
public class ClassicAppMovePolicyTest {
    @Test public void horizontalMovementStopsAtRowEdges() {
        assertEquals(4, ClassicAppMovePolicy.target(4, 10, 4, -1, 0));
        assertEquals(5, ClassicAppMovePolicy.target(4, 10, 4, 1, 0));
        assertEquals(7, ClassicAppMovePolicy.target(7, 10, 4, 1, 0));
    }

    @Test public void verticalMovementPreservesColumnAndHandlesIncompleteRows() {
        assertEquals(1, ClassicAppMovePolicy.target(5, 10, 4, 0, -1));
        assertEquals(9, ClassicAppMovePolicy.target(5, 10, 4, 0, 1));
        assertEquals(9, ClassicAppMovePolicy.target(9, 10, 4, 0, 1));
    }

    @Test public void invalidPositionsAndColumnsAreStable() {
        assertEquals(-1, ClassicAppMovePolicy.target(-1, 10, 4, 1, 0));
        assertEquals(3, ClassicAppMovePolicy.target(3, 10, 0, 0, 1));
    }
    @Test public void bumpOnlyPlaysWhenValidMoveTargetsCurrentSlot() {
        org.junit.Assert.assertTrue(ClassicAppMovePolicy.isBoundaryBump(4, 4));
        org.junit.Assert.assertFalse(ClassicAppMovePolicy.isBoundaryBump(4, 5));
        org.junit.Assert.assertFalse(ClassicAppMovePolicy.isBoundaryBump(-1, -1));
    }

    @Test public void pauseCancelsClassicPreviewOnlyWhenNoInternalOverlayIsShowing() {
        org.junit.Assert.assertTrue(ClassicAppMovePolicy.shouldCancelOnPause(true, false));
        org.junit.Assert.assertFalse(ClassicAppMovePolicy.shouldCancelOnPause(true, true));
        org.junit.Assert.assertFalse(ClassicAppMovePolicy.shouldCancelOnPause(false, false));
    }

}
