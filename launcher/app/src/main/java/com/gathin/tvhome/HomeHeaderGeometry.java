package com.gathin.tvhome;

/** Pure geometry for the centered Home navigation between the real left/right header controls. */
final class HomeHeaderGeometry {
    private HomeHeaderGeometry() { }

    /**
     * Returns a centered width that cannot overlap either side control.
     * leftContentEndPx and rightGroupStartPx are coordinates in the same parent.
     */
    static int centeredWidthPx(int parentWidthPx, int leftContentEndPx,
                               int rightGroupStartPx, int desiredWidthPx) {
        if (parentWidthPx <= 0 || desiredWidthPx <= 0) return 0;
        int center = parentWidthPx / 2;
        int leftRoom = center - Math.max(0, leftContentEndPx);
        int rightRoom = Math.min(parentWidthPx, rightGroupStartPx) - center;
        if (leftRoom <= 0 || rightRoom <= 0) return 0;
        int available = 2 * Math.min(leftRoom, rightRoom);
        return Math.min(desiredWidthPx, available);
    }
}
