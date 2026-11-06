package com.gathin.tvhome;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * The authoritative, ordered launcher inventory owned by AppAdapter.
 *
 * Entries are unique by stable launcher identity (normally ComponentName.flattenToShortString()).
 * Rebuilds replace the inventory as one operation; ordering and lookup are kept independent from
 * any RecyclerView position or Dock capacity. The mutable List surface is retained temporarily for
 * the legacy adapter's move/rebuild call sites while the two-region renderer is migrated.
 */
final class AppInventory<T> extends AbstractList<T> {
    private final Function<T, String> stableKey;
    private final List<T> entries = new ArrayList<>();
    private final Set<String> keys = new HashSet<>();

    AppInventory(Function<T, String> stableKey) {
        if (stableKey == null) throw new IllegalArgumentException("stableKey is required");
        this.stableKey = stableKey;
    }

    @Override
    public T get(int index) {
        return entries.get(index);
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public void add(int index, T item) {
        String key = keyOf(item);
        if (key == null || keys.contains(key)) return;
        entries.add(index, item);
        keys.add(key);
        modCount++;
    }

    @Override
    public T remove(int index) {
        T removed = entries.remove(index);
        keys.remove(keyOf(removed));
        modCount++;
        return removed;
    }

    @Override
    public void clear() {
        if (entries.isEmpty()) return;
        entries.clear();
        keys.clear();
        modCount++;
    }

    /** Replaces the complete inventory, retaining first occurrence and source order per identity. */
    void replace(Collection<? extends T> source) {
        clear();
        if (source == null) return;
        for (T item : source) add(size(), item);
    }

    /** Moves an existing identity without changing inventory membership. */
    boolean move(int from, int to) {
        if (from < 0 || from >= size() || to < 0 || to >= size() || from == to) return false;
        T item = entries.remove(from);
        entries.add(to, item);
        modCount++;
        return true;
    }

    /** Stable-key lookup; returns -1 when the identity is not currently installed/visible. */
    int indexOfKey(String key) {
        if (key == null || key.trim().isEmpty()) return -1;
        for (int i = 0; i < entries.size(); i++) {
            if (key.equals(keyOf(entries.get(i)))) return i;
        }
        return -1;
    }

    /** Defensive immutable snapshot for presentation models and tests. */
    List<T> snapshot() {
        return Collections.unmodifiableList(new ArrayList<>(entries));
    }

    private String keyOf(T item) {
        if (item == null) return null;
        String key = stableKey.apply(item);
        return key == null || key.trim().isEmpty() ? null : key;
    }
}
