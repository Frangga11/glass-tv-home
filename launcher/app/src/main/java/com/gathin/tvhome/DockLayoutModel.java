package com.gathin.tvhome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Immutable boundary between the authoritative app inventory and the two Home presentation regions.
 *
 * Dock membership is selected by stable app identity, never by the first N positions in the app list.
 * Dock order comes from the explicit pinned-key list. Non-pinned apps retain their source order in
 * the main grid. Pinned apps beyond capacity remain in the main grid; nothing is silently dropped.
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

        int dockCount() { return dockItems.size(); }
        int gridCount() { return gridItems.size(); }
        int totalCount() { return dockItems.size() + gridItems.size(); }
    }

    private DockLayoutModel() { }

    static int normalizeCapacity(int capacity) {
        return capacity == 4 || capacity == 5 || capacity == 6
                ? capacity : DockPreferences.DEFAULT_CAPACITY;
    }


    /**
     * Resolves package-configured Dock membership to stable component keys while honoring the
     * user's persisted tile order. Arranged identities are considered first; newly installed or
     * previously unarranged favourite components are appended in configured package order.
     */
    static <T> List<String> orderedPinnedKeys(List<T> inventory, List<String> favouritePackages,
                                               List<String> arrangedKeys,
                                               Function<T, String> stableKey,
                                               Function<T, String> packageKey) {
        if (stableKey == null || packageKey == null) {
            throw new IllegalArgumentException("stableKey and packageKey are required");
        }
        Set<String> favourites = new HashSet<>();
        if (favouritePackages != null) {
            for (String pkg : favouritePackages) {
                if (pkg != null && !pkg.trim().isEmpty()) favourites.add(pkg);
            }
        }
        Map<String, T> byKey = new LinkedHashMap<>();
        if (inventory != null) {
            for (T item : inventory) {
                if (item == null) continue;
                String key = stableKey.apply(item);
                if (key != null && !key.trim().isEmpty() && !byKey.containsKey(key)) {
                    byKey.put(key, item);
                }
            }
        }

        List<String> result = new ArrayList<>();
        Set<String> added = new HashSet<>();
        if (arrangedKeys != null) {
            for (String key : arrangedKeys) {
                T item = key == null ? null : byKey.get(key);
                if (item == null || !favourites.contains(packageKey.apply(item))) continue;
                if (added.add(key)) result.add(key);
            }
        }
        if (favouritePackages != null) {
            for (String pkg : favouritePackages) {
                if (pkg == null || pkg.trim().isEmpty()) continue;
                for (Map.Entry<String, T> entry : byKey.entrySet()) {
                    if (pkg.equals(packageKey.apply(entry.getValue())) && added.add(entry.getKey())) {
                        result.add(entry.getKey());
                    }
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    static <T> Snapshot<T> split(List<T> orderedItems, List<String> pinnedKeys,
                                 int requestedCapacity, Function<T, String> stableKey) {
        if (stableKey == null) throw new IllegalArgumentException("stableKey is required");
        int capacity = normalizeCapacity(requestedCapacity);
        Map<String, T> byKey = new LinkedHashMap<>();
        if (orderedItems != null) {
            for (T item : orderedItems) {
                if (item == null) continue;
                String key = stableKey.apply(item);
                if (key != null && !key.trim().isEmpty() && !byKey.containsKey(key)) {
                    byKey.put(key, item);
                }
            }
        }

        List<T> dock = new ArrayList<>();
        Set<String> selectedKeys = new HashSet<>();
        if (pinnedKeys != null) {
            for (String key : pinnedKeys) {
                if (key == null || key.trim().isEmpty() || !selectedKeys.add(key)) continue;
                T item = byKey.get(key);
                if (item == null) {
                    selectedKeys.remove(key); // missing apps are not reserved Dock slots
                    continue;
                }
                if (dock.size() < capacity) {
                    dock.add(item);
                } else {
                    selectedKeys.remove(key); // overflow remains reachable in the main grid
                }
            }
        }

        List<T> grid = new ArrayList<>();
        for (Map.Entry<String, T> entry : byKey.entrySet()) {
            if (!selectedKeys.contains(entry.getKey())) grid.add(entry.getValue());
        }
        return new Snapshot<>(dock, grid, capacity);
    }
}
