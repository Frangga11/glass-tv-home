package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClassicAppOrderTest {
    @Test public void mergeRetainsSavedOrderAppendsDefaultsThenNewlyInstalledApps() {
        List<String> result = ClassicAppOrder.merge(
                Arrays.asList("app.b", "removed.app", "app.a", "app.b"),
                Arrays.asList("app.c", "app.a"),
                Arrays.asList("app.a", "app.b", "app.c", "app.d"));
        assertEquals(Arrays.asList("app.b", "app.a", "app.c", "app.d"), result);
    }

    @Test public void mergeSeedsDefaultsWhenNoSavedAppRemains() {
        assertEquals(Arrays.asList("app.c", "app.a", "app.b"),
                ClassicAppOrder.merge(Collections.singletonList("removed.app"),
                        Arrays.asList("app.c", "app.a"),
                        Arrays.asList("app.a", "app.b", "app.c")));
    }

    @Test public void moveClampsDestinationAndRetainsUniquePackageOrder() {
        java.util.ArrayList<String> order = new java.util.ArrayList<>(Arrays.asList("a", "b", "c"));
        ClassicAppOrder.move(order, "a", 99);
        assertEquals(Arrays.asList("b", "c", "a"), order);
    }
}
