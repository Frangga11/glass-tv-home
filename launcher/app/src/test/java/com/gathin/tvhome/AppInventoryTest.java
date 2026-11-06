package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AppInventoryTest {

    @Test
    public void replaceKeepsFirstOccurrenceAndOriginalInventoryOrder() {
        AppInventory<String> inventory = new AppInventory<>(value -> value);
        inventory.replace(Arrays.asList("app-a", "app-b", "app-a", "app-c", "", null, "app-d"));

        assertEquals(Arrays.asList("app-a", "app-b", "app-c", "app-d"), inventory.snapshot());
        assertEquals(4, inventory.size());
    }

    @Test
    public void identityLookupDoesNotDependOnCurrentPosition() {
        AppInventory<String> inventory = new AppInventory<>(value -> value);
        inventory.replace(Arrays.asList("app-a", "app-b", "app-c"));

        assertEquals(1, inventory.indexOfKey("app-b"));
        assertTrue(inventory.move(1, 2));
        assertEquals(2, inventory.indexOfKey("app-b"));
        assertEquals("app-b", inventory.get(2));
    }

    @Test
    public void invalidOrUnknownIdentityCannotMoveInventory() {
        AppInventory<String> inventory = new AppInventory<>(value -> value);
        inventory.replace(Arrays.asList("app-a", "app-b"));

        assertFalse(inventory.move(-1, 1));
        assertFalse(inventory.move(0, 2));
        assertFalse(inventory.move(1, 1));
        assertEquals(Arrays.asList("app-a", "app-b"), inventory.snapshot());
        assertEquals(-1, inventory.indexOfKey("missing"));
        assertEquals(-1, inventory.indexOfKey(" "));
    }

    @Test
    public void snapshotIsImmutableAndDetachedFromFutureChanges() {
        AppInventory<String> inventory = new AppInventory<>(value -> value);
        inventory.replace(Arrays.asList("app-a", "app-b"));
        List<String> snapshot = inventory.snapshot();

        inventory.replace(Collections.singletonList("app-c"));

        assertEquals(Arrays.asList("app-a", "app-b"), snapshot);
        assertEquals(Collections.singletonList("app-c"), inventory.snapshot());
        assertFalse(snapshot.contains("app-c"));
    }

    @Test
    public void nullSourceClearsInventoryAndNullKeyFunctionIsRejected() {
        AppInventory<String> inventory = new AppInventory<>(value -> value);
        inventory.replace(Collections.singletonList("app-a"));
        inventory.replace(null);

        assertTrue(inventory.isEmpty());
    }
}
