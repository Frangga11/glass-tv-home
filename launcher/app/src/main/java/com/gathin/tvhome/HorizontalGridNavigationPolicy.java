package com.gathin.tvhome;

import android.view.KeyEvent;

/** Horizontal-mode DPAD graph. Positions are local to Dock and Grid adapters. */
final class HorizontalGridNavigationPolicy {
    static final class Target {
        final boolean dock;
        final int position;
        Target(boolean dock, int position) { this.dock = dock; this.position = position; }
    }

    private HorizontalGridNavigationPolicy() {}

    static Target fromDock(int position, int keyCode, int dockCount, int gridCount,
                           int columns, boolean gridExpanded) {
        if (position < 0 || position >= dockCount || columns < 1) return new Target(true, position);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                return new Target(true, Math.max(0, position - 1));
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                return new Target(true, Math.min(dockCount - 1, position + 1));
            case KeyEvent.KEYCODE_DPAD_UP:
                return new Target(true, -1);
            case KeyEvent.KEYCODE_DPAD_DOWN:
                return gridCount > 0
                        ? new Target(false, DockLayoutModel.gridPositionForDock(
                                position, false, columns, gridCount))
                        : new Target(true, position);
            default:
                return new Target(true, position);
        }
    }

    static Target fromGrid(int position, int keyCode, int gridCount, int columns, int dockCount) {
        if (position < 0 || position >= gridCount || columns < 1) return new Target(false, position);
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP && position < columns && dockCount > 0) {
            return new Target(true, Math.min(position % columns, dockCount - 1));
        }
        int direction;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT: direction = -1; break;
            case KeyEvent.KEYCODE_DPAD_RIGHT: direction = 1; break;
            case KeyEvent.KEYCODE_DPAD_UP: direction = -2; break;
            case KeyEvent.KEYCODE_DPAD_DOWN: direction = 2; break;
            default: return new Target(false, position);
        }
        int next = DockLayoutModel.gridNeighborPosition(position, direction, columns, gridCount);
        return new Target(false, next < 0 ? position : next);
    }
}
