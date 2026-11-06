package com.gathin.tvhome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Immutable boundary between the ordered Home app inventory and the Dock renderer.
 *
 * The source order is authoritative (pinned apps first, followed by user ordering and
 * the alphabetical remainder). This class partitions that order without mutating it.
 * A dedicated Dock view can consume dockItems while the main app grid consumes gridItems.
 */
final class DockLayoutModel {
    static final class Snapshot<T> {
        final List<T> dockItems;
        final List<T> gridItems;
        final int capacity;

        private Snapshot(List<T> dockItems, List<T> gridItems, int capacity) {
            this.dockItems = Collections.unmodifiableList(new ArrayList<>(dockItems));
            this.gridItems = Collections.unmodifiableList(new ArrayList<>(gridItems));
            this.capacity = capacity;
        }

        int dockCount() {
            return dockItems.size();
        }

        int gridCount() {
            return gridItems.size();
        }

        int totalCount() {
            return dockItems.size() + gridItems.size();
        }
    }

    private DockLayoutModel() { }

    static int normalizeCapacity(int capacity) {
        return capacity == 4 || capacity == 5 || capacity == 6
                ? capacity : DockPreferences.DEFAULT_CAPACITY;
    }

    static <T> Snapshot<T> split(List<T> orderedItems, int requestedCapacity,
                                 Function<T, String> stableKey) {
        if (stableKey == null) throw new IllegalArgumentException("stableKey is required");
        int capacity = normalizeCapacity(requestedCapacity);
        List<T> dock = new ArrayList<>();
        List<T> grid = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        if (orderedItems != null) {
            for (T item : orderedItems) {
                if (item == null) continue;
                String key = stableKey.apply(item);
                if (key == null || key.trim().isEmpty() || !seen.add(key)) continue;
                if (dock.size() < capacity) dock.add(item);
                else grid.add(item);
            }
        }
        return new Snapshot<>(dock, grid, capacity);
    }
}
