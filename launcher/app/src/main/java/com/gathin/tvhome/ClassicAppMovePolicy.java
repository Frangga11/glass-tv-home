package com.gathin.tvhome;

/**
 * Classic Home Universal grid move policy.
 *
 * Source: https://github.com/destradess/tv-home-universal
 * Original file: launcher/src/io/github/tvhome/universal/AppMovePolicy.java
 * Copyright (c) 2026 Mihir Gupta. Licensed under the MIT License.
 */
final class ClassicAppMovePolicy {
    private ClassicAppMovePolicy() {}

    static int target(int index, int count, int columns, int dx, int dy) {
        if (index < 0 || index >= count || columns < 1) return index;
        if (dx < 0) return index % columns > 0 ? index - 1 : index;
        if (dx > 0) return index % columns < columns - 1 && index + 1 < count ? index + 1 : index;
        if (dy < 0) return index >= columns ? index - columns : index;
        if (dy > 0) {
            int nextRow = (index / columns + 1) * columns;
            return nextRow < count ? Math.min(index + columns, count - 1) : index;
        }
        return index;
    }

    /** A bump is emitted only for a valid selection whose requested target is its current slot. */
    static boolean isBoundaryBump(int currentIndex, int targetIndex) {
        return currentIndex >= 0 && targetIndex == currentIndex;
    }

    /** Classic chrome follows normal Grid/Dock focus, but stays steady during a move hand-off. */
    static boolean shouldHideClassicChrome(boolean classicHostActive, boolean gridFocused,
                                            boolean moveActive, boolean wasHidden) {
        if (!classicHostActive) return false;
        return moveActive ? wasHidden : gridFocused;
    }

    /** Focus may transfer only after the Classic ScrollView reaches its measured target. */
    static boolean isScrollSettled(int currentY, int targetY) {
        return Math.abs(currentY - targetY) <= 2;
    }

    /** Internal launcher overlays must not cancel an in-progress Classic preview on pause. */
    static boolean shouldCancelOnPause(boolean moveTransactionActive, boolean internalOverlayShowing) {
        return moveTransactionActive && !internalOverlayShowing;
    }
}
