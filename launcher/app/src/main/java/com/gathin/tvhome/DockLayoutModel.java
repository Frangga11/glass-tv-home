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

    /** Unique holder type per orientation/capacity so changed geometry cannot reuse stale tile views. */
    static int gridPositionForDock(int dockPosition, boolean vertical, int gridColumns, int gridCount) {
        if (dockPosition < 0 || gridColumns <= 0 || gridCount <= 0) return -1;
        int target = vertical ? dockPosition * gridColumns : dockPosition % gridColumns;
        return Math.min(target, gridCount - 1);
    }

    static int dockPositionForGrid(int gridPosition, boolean vertical, int gridColumns, int dockCount) {
        if (gridPosition < 0 || gridColumns <= 0 || dockCount <= 0) return -1;
        int target = vertical ? gridPosition / gridColumns : gridPosition % gridColumns;
        return Math.min(target, dockCount - 1);
    }

    static int dockViewType(boolean vertical, int capacity) {
        return (vertical ? 100 : 0) + normalizeCapacity(capacity);
    }

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

    /** Resolves a persisted component-key list against the current inventory, dropping missing and duplicate identities. */
    static <T> List<String> orderedExplicitPinnedKeys(List<T> inventory, List<String> explicitKeys,
                                                       Function<T, String> stableKey) {
        if (stableKey == null) throw new IllegalArgumentException("stableKey is required");
        Set<String> available = new HashSet<>();
        if (inventory != null) {
            for (T item : inventory) {
                if (item == null) continue;
                String key = stableKey.apply(item);
                if (key != null && !key.trim().isEmpty()) available.add(key);
            }
        }
        List<String> result = new ArrayList<>();
        Set<String> added = new HashSet<>();
        if (explicitKeys != null) {
            for (String key : explicitKeys) {
                if (key != null && available.contains(key) && added.add(key)) result.add(key);
            }
        }
        return Collections.unmodifiableList(result);
    }

    static List<String> mergePinnedKeys(List<String> explicitKeys, List<String> defaultKeys, List<String> excludedKeys) {
        Set<String> excluded = new HashSet<>();
        if (excludedKeys != null) excluded.addAll(excludedKeys);
        List<String> result = new ArrayList<>();
        Set<String> added = new HashSet<>();
        if (explicitKeys != null) {
            for (String key : explicitKeys) {
                if (key != null && !key.trim().isEmpty() && !excluded.contains(key) && added.add(key)) result.add(key);
            }
        }
        if (defaultKeys != null) {
            for (String key : defaultKeys) {
                if (key != null && !key.trim().isEmpty() && !excluded.contains(key) && added.add(key)) result.add(key);
            }
        }
        return Collections.unmodifiableList(result);
    }

    static List<String> movePinnedKey(List<String> keys, int from, int to, int visibleCount) {
        List<String> result = new ArrayList<>(keys == null ? Collections.emptyList() : keys);
        if (from < 0 || to < 0 || from >= visibleCount || to >= visibleCount
                || from >= result.size() || to >= result.size() || from == to) {
            return Collections.unmodifiableList(result);
        }
        String moved = result.remove(from);
        result.add(to, moved);
        return Collections.unmodifiableList(result);
    }

    static List<String> pinKey(List<String> keys, String key, int requestedPosition, int capacity) {
        List<String> result = new ArrayList<>(keys == null ? Collections.emptyList() : keys);
        if (key == null || key.trim().isEmpty()) return Collections.unmodifiableList(result);
        // An overflow-pinned app is in the Grid but remains in the pin order. Moving it upward
        // promotes it into a visible Dock slot instead of treating it as an already-pinned no-op.
        result.remove(key);
        int normalizedCapacity = normalizeCapacity(capacity);
        int position = Math.max(0, Math.min(requestedPosition, Math.min(normalizedCapacity - 1, result.size())));
        result.add(position, key);
        return Collections.unmodifiableList(result);
    }

    static List<String> unpinKey(List<String> keys, String key) {
        List<String> result = new ArrayList<>(keys == null ? Collections.emptyList() : keys);
        if (key != null) result.remove(key);
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
