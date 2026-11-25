package com.gathin.tvhome;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Classic Home Universal app-order policy, adapted only to the Glass TV Home package.
 *
 * Source: https://github.com/destradess/tv-home-universal
 * Original file: launcher/src/io/github/tvhome/universal/AppOrder.java
 * Copyright (c) 2026 Mihir Gupta. Licensed under the MIT License.
 */
final class ClassicAppOrder {
    private ClassicAppOrder() {}

    static List<String> merge(List<String> saved, List<String> defaults, List<String> installed) {
        Set<String> available = new HashSet<>(installed);
        Set<String> unique = new LinkedHashSet<>();
        if (saved != null) {
            for (String pkg : saved) if (available.contains(pkg)) unique.add(pkg);
        }
        if (unique.isEmpty() && defaults != null) {
            for (String pkg : defaults) if (available.contains(pkg)) unique.add(pkg);
        }
        if (installed != null) unique.addAll(installed);
        return new ArrayList<>(unique);
    }

    static void move(List<String> order, String pkg, int position) {
        if (order == null || pkg == null || !order.remove(pkg)) return;
        order.add(Math.max(0, Math.min(position, order.size())), pkg);
    }
}
