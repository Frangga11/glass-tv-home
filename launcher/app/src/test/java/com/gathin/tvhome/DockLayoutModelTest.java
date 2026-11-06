package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class DockLayoutModelTest {
    @Test
    public void partitionsInSourceOrderAndKeepsGridItemsAfterDock() {
        List<String> ordered = Arrays.asList("pinned-a", "pinned-b", "app-c", "app-d", "app-e");
        DockLayoutModel.Snapshot<String> result =
                DockLayoutModel.split(ordered, 4, value -> value);

        assertEquals(Arrays.asList("pinned-a", "pinned-b", "app-c", "app-d"), result.dockItems);
        assertEquals(Collections.singletonList("app-e"), result.gridItems);
        assertEquals(5, result.totalCount());
    }

    @Test
    public void supportsFourFiveAndSixSlotsWithoutChangingSourceOrder() {
        List<String> ordered = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
        assertEquals(4, DockLayoutModel.split(ordered, 4, value -> value).dockCount());
        assertEquals(5, DockLayoutModel.split(ordered, 5, value -> value).dockCount());
        assertEquals(6, DockLayoutModel.split(ordered, 6, value -> value).dockCount());
        assertEquals(Collections.singletonList("g"),
                DockLayoutModel.split(ordered, 6, value -> value).gridItems);
    }

    @Test
    public void invalidCapacityFallsBackToDefaultFive() {
        DockLayoutModel.Snapshot<String> result =
                DockLayoutModel.split(Arrays.asList("a", "b", "c", "d", "e", "f", "g"), 99, value -> value);
        assertEquals(5, result.capacity);
        assertEquals(5, result.dockCount());
        assertEquals(2, result.gridCount());
    }

    @Test
    public void duplicateAndInvalidKeysDoNotCreateDuplicateTiles() {
        List<String> ordered = Arrays.asList("a", "b", "a", "", "c", "d", "e", "f");
        DockLayoutModel.Snapshot<String> result =
                DockLayoutModel.split(ordered, 4, value -> value);
        assertEquals(Arrays.asList("a", "b", "c", "d"), result.dockItems);
        assertEquals(Arrays.asList("e", "f"), result.gridItems);
    }

    @Test
    public void snapshotsAreImmutable() {
        DockLayoutModel.Snapshot<String> result =
                DockLayoutModel.split(Arrays.asList("a", "b"), 4, value -> value);
        assertThrows(UnsupportedOperationException.class, () -> result.dockItems.add("c"));
        assertThrows(UnsupportedOperationException.class, () -> result.gridItems.clear());
    }

    @Test
    public void nullInventoryProducesAnEmptySnapshot() {
        DockLayoutModel.Snapshot<String> result =
                DockLayoutModel.split(null, 4, value -> value);
        assertTrue(result.dockItems.isEmpty());
        assertTrue(result.gridItems.isEmpty());
        assertEquals(0, result.totalCount());
    }

    @Test
    public void stableKeyFunctionIsRequired() {
        assertThrows(IllegalArgumentException.class,
                () -> DockLayoutModel.split(Collections.singletonList("a"), 4, null));
    }
}
