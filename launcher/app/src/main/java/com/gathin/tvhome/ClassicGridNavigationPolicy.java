package com.gathin.tvhome;

/**
 * Focus-neighbour rules from Home Universal's Classic vertical Dock/Grid layout.
 * Indices are local to their respective adapter; a negative dock index means Grid.
 *
 * Source: https://github.com/destradess/tv-home-universal
 * Original focus graph: MainActivity.wireFocus().
 * Copyright (c) 2026 Mihir Gupta. Licensed under the MIT License.
 */
final class ClassicGridNavigationPolicy {
    static final class Target {
        final boolean dock;
        final int position;
        Target(boolean dock, int position) { this.dock = dock; this.position = position; }
    }

    private ClassicGridNavigationPolicy() {}

    static Target fromGrid(int position, int keyCode, int columns, int gridCount, int dockCount) {
        if (position < 0 || position >= gridCount || columns < 1) return new Target(false, position);
        int row = position / columns;
        int column = position % columns;
        int lastRow = (gridCount - 1) / columns;
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_DPAD_LEFT:
                return new Target(false, column == 0 ? position : position - 1);
            case android.view.KeyEvent.KEYCODE_DPAD_RIGHT:
                return new Target(false, column == columns - 1 || position == gridCount - 1
                        ? position : position + 1);
            case android.view.KeyEvent.KEYCODE_DPAD_UP:
                if (row == 0) return dockCount > 0
                        ? new Target(true, Math.min(column, dockCount - 1))
                        : new Target(false, position);
                return new Target(false, position - columns);
            case android.view.KeyEvent.KEYCODE_DPAD_DOWN:
                if (position + columns < gridCount) return new Target(false, position + columns);
                if (row < lastRow) return new Target(false, gridCount - 1);
                return new Target(false, position);
            default:
                return new Target(false, position);
        }
    }

    /** Adapter positions are local: every Grid target changes region even when its index matches Dock. */
    static boolean transfersOutOfDock(Target target, int currentDockPosition) {
        return target != null && (!target.dock || target.position != currentDockPosition);
    }

    static Target fromDock(int position, int keyCode, int dockCount, int gridCount, int topTarget) {
        if (position < 0 || position >= dockCount) return new Target(true, position);
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_DPAD_LEFT:
                return new Target(true, position == 0 ? position : position - 1);
            case android.view.KeyEvent.KEYCODE_DPAD_RIGHT:
                return new Target(true, position == dockCount - 1 ? position : position + 1);
            case android.view.KeyEvent.KEYCODE_DPAD_UP:
                return new Target(true, topTarget);
            case android.view.KeyEvent.KEYCODE_DPAD_DOWN:
                return gridCount > 0 ? new Target(false, Math.min(position, gridCount - 1))
                        : new Target(true, position);
            default:
                return new Target(true, position);
        }
    }
}
