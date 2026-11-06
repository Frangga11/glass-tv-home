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
    public void arrangedOrderIsPreservedForPinnedComponents() {
        List<String> inventory = Arrays.asList("pkg.a/.Main", "pkg.b/.Main", "pkg.c/.Main");
        List<String> pinned = DockLayoutModel.orderedPinnedKeys(
                inventory, Arrays.asList("pkg.a", "pkg.b"),
                Arrays.asList("pkg.b/.Main", "pkg.c/.Main", "pkg.a/.Main"),
                value -> value, value -> value.substring(0, value.indexOf('/')));

        assertEquals(Arrays.asList("pkg.b/.Main", "pkg.a/.Main"), pinned);
    }

    @Test
    public void newlyInstalledFavouriteComponentsAppendInConfiguredPackageOrder() {
        List<String> inventory = Arrays.asList(
                "pkg.a/.Second", "pkg.b/.Main", "pkg.a/.First", "pkg.c/.Main");
        List<String> pinned = DockLayoutModel.orderedPinnedKeys(
                inventory, Arrays.asList("pkg.a", "pkg.b"),
                Collections.singletonList("pkg.b/.Main"),
                value -> value, value -> value.substring(0, value.indexOf('/')));

        assertEquals(Arrays.asList("pkg.b/.Main", "pkg.a/.Second", "pkg.a/.First"), pinned);
    }

    @Test
    public void explicitPinnedKeysAreResolvedByStableIdentityAndDeduplicated() {
        List<String> inventory = Arrays.asList("grid-a", "dock-b", "grid-c", "dock-a");
        List<String> pinned = DockLayoutModel.orderedExplicitPinnedKeys(
                inventory, Arrays.asList("dock-a", "missing", "dock-b", "dock-a"), value -> value);
        assertEquals(Arrays.asList("dock-a", "dock-b"), pinned);
    }

    @Test
    public void customDockOrderKeepsNewDefaultsButHonorsExplicitUnpins() {
        assertEquals(Arrays.asList("custom", "favorite-a", "favorite-c"),
                DockLayoutModel.mergePinnedKeys(
                        Arrays.asList("custom", "favorite-a"),
                        Arrays.asList("favorite-a", "favorite-b", "favorite-c"),
                        Collections.singletonList("favorite-b")));
    }

    @Test
    public void movingDockKeyOnlyReordersVisiblePinnedSlots() {
        List<String> pinned = Arrays.asList("a", "b", "c", "d", "overflow");
        assertEquals(Arrays.asList("c", "a", "b", "d", "overflow"),
                DockLayoutModel.movePinnedKey(pinned, 2, 0, 4));
        assertEquals(pinned, DockLayoutModel.movePinnedKey(pinned, 4, 0, 4));
    }

    @Test
    public void pinningInsertsAtRequestedVisibleSlotAndPushesOverflowToGrid() {
        List<String> pinned = Arrays.asList("a", "b", "c", "d");
        List<String> updated = DockLayoutModel.pinKey(pinned, "new", 1, 4);
        assertEquals(Arrays.asList("a", "new", "b", "c", "d"), updated);
        assertEquals(Arrays.asList("a", "new", "b", "c"),
                DockLayoutModel.split(updated, updated, 4, value -> value).dockItems);
        assertEquals(Collections.singletonList("d"),
                DockLayoutModel.split(updated, updated, 4, value -> value).gridItems);
    }

    @Test
    public void unpinningMovesIdentityOutOfDockWithoutDroppingIt() {
        List<String> pinned = Arrays.asList("a", "b", "c");
        List<String> updated = DockLayoutModel.unpinKey(pinned, "b");
        assertEquals(Arrays.asList("a", "c"), updated);
        DockLayoutModel.Snapshot<String> snapshot = DockLayoutModel.split(
                Arrays.asList("a", "b", "c"), updated, 4, value -> value);
        assertEquals(Arrays.asList("a", "c"), snapshot.dockItems);
        assertEquals(Collections.singletonList("b"), snapshot.gridItems);
    }

    @Test
    public void membershipUsesExplicitStableKeysRatherThanFirstInventoryPositions() {
        List<String> inventory = Arrays.asList("grid-first", "pinned-b", "grid-second", "pinned-a");
        DockLayoutModel.Snapshot<String> result = DockLayoutModel.split(
                inventory, Arrays.asList("pinned-a", "pinned-b"), 4, value -> value);

        assertEquals(Arrays.asList("pinned-a", "pinned-b"), result.dockItems);
        assertEquals(Arrays.asList("grid-first", "grid-second"), result.gridItems);
    }

    @Test
    public void distinctLauncherComponentsFromSamePackageRemainDistinctTiles() {
        // Package-level Dock configuration can match more than one launcher activity.
        // The partition identity must therefore be the component key, not package name.
        List<String> inventory = Arrays.asList("pkg/.ActivityA", "other/.Main", "pkg/.ActivityB");
        DockLayoutModel.Snapshot<String> result = DockLayoutModel.split(
                inventory, Arrays.asList("pkg/.ActivityA", "pkg/.ActivityB"), 4, value -> value);

        assertEquals(Arrays.asList("pkg/.ActivityA", "pkg/.ActivityB"), result.dockItems);
        assertEquals(Collections.singletonList("other/.Main"), result.gridItems);
        assertEquals(inventory.size(), result.totalCount());
    }

    @Test
    public void dockOrderFollowsPinnedIdentityOrderAndGridKeepsInventoryOrder() {
        List<String> inventory = Arrays.asList("app-a", "app-b", "app-c", "app-d");
        DockLayoutModel.Snapshot<String> result = DockLayoutModel.split(
                inventory, Arrays.asList("app-d", "app-b"), 4, value -> value);

        assertEquals(Arrays.asList("app-d", "app-b"), result.dockItems);
        assertEquals(Arrays.asList("app-a", "app-c"), result.gridItems);
    }

    @Test
    public void pinnedAppsBeyondCapacityRemainInGridAndAreNeverDropped() {
        List<String> inventory = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
        List<String> pinned = Arrays.asList("g", "f", "e", "d", "c", "b", "a");
        DockLayoutModel.Snapshot<String> result = DockLayoutModel.split(
                inventory, pinned, 4, value -> value);

        assertEquals(Arrays.asList("g", "f", "e", "d"), result.dockItems);
        assertEquals(Arrays.asList("a", "b", "c"), result.gridItems);
        assertEquals(inventory.size(), result.totalCount());
    }

    @Test
    public void supportsFourFiveAndSixSlots() {
        List<String> inventory = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
        assertEquals(4, DockLayoutModel.split(inventory, inventory, 4, value -> value).dockCount());
        assertEquals(5, DockLayoutModel.split(inventory, inventory, 5, value -> value).dockCount());
        assertEquals(6, DockLayoutModel.split(inventory, inventory, 6, value -> value).dockCount());
    }

    @Test
    public void emptyDockKeepsEveryInstalledAppInTheGrid() {
        List<String> inventory = Arrays.asList("app-a", "app-b", "app-c");
        DockLayoutModel.Snapshot<String> snapshot = DockLayoutModel.split(
                inventory, Collections.emptyList(), 4, value -> value);
        assertTrue(snapshot.dockItems.isEmpty());
        assertEquals(inventory, snapshot.gridItems);
        assertEquals(inventory.size(), snapshot.totalCount());
    }

    @Test
    public void allPinnedAppsCanFillTheDockWithoutInventingGridItems() {
        List<String> inventory = Arrays.asList("app-a", "app-b", "app-c", "app-d");
        DockLayoutModel.Snapshot<String> snapshot = DockLayoutModel.split(
                inventory, inventory, 6, value -> value);
        assertEquals(inventory, snapshot.dockItems);
        assertTrue(snapshot.gridItems.isEmpty());
        assertEquals(inventory.size(), snapshot.totalCount());
    }

    @Test
    public void pinningAtEverySupportedCapacityPushesThePreviousLastSlotIntoGrid() {
        List<String> source = Arrays.asList("a", "b", "c", "d", "e", "f");
        for (int capacity : new int[]{4, 5, 6}) {
            List<String> updated = DockLayoutModel.pinKey(
                    source.subList(0, capacity), "new", 1, capacity);
            DockLayoutModel.Snapshot<String> snapshot = DockLayoutModel.split(
                    updated, updated, capacity, value -> value);
            assertEquals(capacity, snapshot.dockCount());
            assertEquals(1, snapshot.gridCount());
            assertEquals(source.get(capacity - 1), snapshot.gridItems.get(0));
        }
    }

    @Test
    public void allSixDockConfigurationsHaveDistinctHolderTypes() {
        assertEquals(4, DockLayoutModel.dockViewType(false, 4));
        assertEquals(5, DockLayoutModel.dockViewType(false, 5));
        assertEquals(6, DockLayoutModel.dockViewType(false, 6));
        assertEquals(104, DockLayoutModel.dockViewType(true, 4));
        assertEquals(105, DockLayoutModel.dockViewType(true, 5));
        assertEquals(106, DockLayoutModel.dockViewType(true, 6));
    }

    @Test
    public void invalidCapacityFallsBackToDefaultFive() {
        List<String> inventory = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
        DockLayoutModel.Snapshot<String> result =
                DockLayoutModel.split(inventory, inventory, 99, value -> value);
        assertEquals(5, result.capacity);
        assertEquals(5, result.dockCount());
        assertEquals(2, result.gridCount());
    }

    @Test
    public void missingAndDuplicatePinnedKeysDoNotReserveSlotsOrDuplicateTiles() {
        List<String> inventory = Arrays.asList("a", "b", "c", "d", "e");
        List<String> pinned = Arrays.asList("missing", "b", "b", "", "d");
        DockLayoutModel.Snapshot<String> result =
                DockLayoutModel.split(inventory, pinned, 4, value -> value);
        assertEquals(Arrays.asList("b", "d"), result.dockItems);
        assertEquals(Arrays.asList("a", "c", "e"), result.gridItems);
    }

    @Test
    public void duplicateInventoryKeysKeepOnlyFirstEntry() {
        List<String> inventory = Arrays.asList("a", "b", "a", "c");
        DockLayoutModel.Snapshot<String> result =
                DockLayoutModel.split(inventory, Collections.singletonList("a"), 4, value -> value);
        assertEquals(Collections.singletonList("a"), result.dockItems);
        assertEquals(Arrays.asList("b", "c"), result.gridItems);
    }

    @Test
    public void snapshotsAreImmutable() {
        DockLayoutModel.Snapshot<String> result =
                DockLayoutModel.split(Arrays.asList("a", "b"), Collections.singletonList("a"), 4, value -> value);
        assertThrows(UnsupportedOperationException.class, () -> result.dockItems.add("c"));
        assertThrows(UnsupportedOperationException.class, () -> result.gridItems.clear());
    }

    @Test
    public void nullInventoryOrPinnedListProducesSafeSnapshot() {
        DockLayoutModel.Snapshot<String> empty =
                DockLayoutModel.split(null, null, 4, value -> value);
        assertTrue(empty.dockItems.isEmpty());
        assertTrue(empty.gridItems.isEmpty());
        assertEquals(0, empty.totalCount());
    }

    @Test
    public void stableKeyFunctionIsRequired() {
        assertThrows(IllegalArgumentException.class,
                () -> DockLayoutModel.split(Collections.singletonList("a"),
                        Collections.singletonList("a"), 4, null));
    }
}
