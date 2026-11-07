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
    public void overflowPinnedAppCanBePromotedIntoVisibleDockSlot() {
        List<String> pinned = Arrays.asList("a", "b", "c", "d", "overflow");
        List<String> updated = DockLayoutModel.pinKey(pinned, "overflow", 1, 4);
        assertEquals(Arrays.asList("a", "overflow", "b", "c", "d"), updated);
        DockLayoutModel.Snapshot<String> snapshot = DockLayoutModel.split(updated, updated, 4, value -> value);
        assertEquals(Arrays.asList("a", "overflow", "b", "c"), snapshot.dockItems);
        assertEquals(Collections.singletonList("d"), snapshot.gridItems);
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
    public void patch41DockFillsSlotsFromInventoryWhenFavoritesAreMissing() {
        List<String> inventory = Arrays.asList("browse", "disney", "miracast", "netflix", "store", "prime", "vidio");
        List<String> pinned = DockLayoutModel.fillDockSlots(
                Collections.emptyList(), inventory, Collections.emptyList(), 5);
        assertEquals(Arrays.asList("browse", "disney", "miracast", "netflix", "store"), pinned);
        DockLayoutModel.Snapshot<String> snapshot = DockLayoutModel.split(inventory, pinned, 5, value -> value);
        assertEquals(pinned, snapshot.dockItems);
        assertEquals(Arrays.asList("prime", "vidio"), snapshot.gridItems);
    }

    @Test
    public void patch41DockFillHonorsExplicitUnpins() {
        List<String> pinned = DockLayoutModel.fillDockSlots(
                Collections.singletonList("favorite"), Arrays.asList("favorite", "excluded", "next", "last"),
                Collections.singletonList("excluded"), 4);
        assertEquals(Arrays.asList("favorite", "next", "last"), pinned);
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
    public void dockAndGridDirectionalNavigationMapsToNearestAlignedSlot() {
        assertEquals(2, DockLayoutModel.gridPositionForDock(2, false, 4, 12));
        assertEquals(8, DockLayoutModel.gridPositionForDock(2, true, 4, 12));
        assertEquals(1, DockLayoutModel.gridPositionForDock(5, false, 4, 12));
        assertEquals(11, DockLayoutModel.gridPositionForDock(5, true, 4, 12));
        assertEquals(1, DockLayoutModel.dockPositionForGrid(6, true, 4, 6));
        assertEquals(2, DockLayoutModel.dockPositionForGrid(6, false, 4, 6));
        assertEquals(0, DockLayoutModel.dockPositionForGrid(0, false, 4, 6));
        assertEquals(-1, DockLayoutModel.gridPositionForDock(0, false, 4, 0));
        assertEquals(-1, DockLayoutModel.dockPositionForGrid(0, false, 4, 0));
    }

    @Test
    public void bottomDockFocusTargetsTheFirstGridRowAndRowsAdvanceDownward() {
        for (int dockPosition = 0; dockPosition < 5; dockPosition++) {
            assertEquals(dockPosition, DockLayoutModel.gridPositionForDock(dockPosition, false, 5, 20));
            assertEquals(dockPosition, DockLayoutModel.dockPositionForGrid(dockPosition, false, 5, 5));
        }
        assertEquals(5, DockLayoutModel.gridPositionForDock(0, false, 5, 20) + 5);
        assertEquals(19, Math.min(15 + 5, 19));
    }

    @Test
    public void bottomDockFocusTargetsTheAdjacentLastGridRowLegacyHelper() {
        assertEquals(10, DockLayoutModel.gridPositionAboveDock(2, 4, 12));
        assertEquals(9, DockLayoutModel.gridPositionAboveDock(3, 4, 10));
        assertEquals(0, DockLayoutModel.gridPositionAboveDock(5, 4, 1));
        assertEquals(-1, DockLayoutModel.gridPositionAboveDock(0, 4, 0));
        assertEquals(-1, DockLayoutModel.gridPositionAboveDock(-1, 4, 5));
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
    @Test
    public void dockGeometryPreservesLegacyFourFiveSixSlotRatiosAndSpacing() {
        for (float density : new float[]{1f, 1.5f, 1.75f, 2f}) {
            int innerWidth = 1920;
            int largestTileWidth = DockGeometry.horizontalTileWidthPx(innerWidth, 4, density);
            int largestTileHeight = DockGeometry.tileHeightPx(largestTileWidth);
            int gridReserve = DockGeometry.horizontalGridBottomReservePx(largestTileWidth, density);
            int legacyGridReserve = DockGeometry.itemHeightPx(largestTileHeight, density)
                    + Math.round(34f * density);
            assertEquals(legacyGridReserve, gridReserve);

            for (int capacity : new int[]{4, 5, 6}) {
                int tileWidth = DockGeometry.horizontalTileWidthPx(innerWidth, capacity, density);
                int tileHeight = DockGeometry.tileHeightPx(tileWidth);
                int itemHeight = DockGeometry.itemHeightPx(tileHeight, density);
                int dockHeight = DockGeometry.horizontalDockHeightPx(tileHeight, density);
                float trayHeight = tileHeight + 28f * density;

                assertEquals(tileHeight + Math.round(45f * density), itemHeight);
                assertEquals(itemHeight + Math.round(18f * density), dockHeight);
                assertEquals(14f * density, DockGeometry.trayPaddingPx(density), 0.001f);
                assertEquals(35f * density, dockHeight - trayHeight, 1.1f);
                // The horizontal host sits 16dp above the gridWrap edge, matching Patch 4.1.
                assertEquals(51f * density,
                        dockHeight - trayHeight + Math.round(16f * density), 1.1f);
            }
        }
    }

    @Test
    public void dockGeometrySharesFiveColumnTileScaleAndFitsSixSlotCapacity() {
        float density = 2f;
        int four = DockGeometry.horizontalTileWidthPx(1920, 4, density);
        int five = DockGeometry.horizontalTileWidthPx(1920, 5, density);
        int six = DockGeometry.horizontalTileWidthPx(1920, 6, density);

        assertTrue(four > 0);
        assertEquals(five, four);
        assertTrue(six < five);
        assertEquals(Math.round(five * 101f / 180f), DockGeometry.tileHeightPx(five));
    }


    @Test
    public void horizontalDockDownAndOverflowUpPreserveTheAlignedColumn() {
        List<String> grid = Arrays.asList("overflow-a", "overflow-b", "overflow-c", "overflow-d",
                "overflow-e", "overflow-f", "overflow-g");
        for (int dockPosition = 0; dockPosition < 5; dockPosition++) {
            int gridPosition = DockLayoutModel.gridPositionForDock(
                    dockPosition, false, 5, grid.size());
            assertEquals(dockPosition, gridPosition);
            assertEquals(dockPosition, DockLayoutModel.dockPositionForGrid(
                    gridPosition, false, 5, 5));
        }
    }

    @Test
    public void horizontalDockCentersFourFiveAndSixVisibleItems() {
        for (float density : new float[]{1f, 1.5f, 1.75f, 2f}) {
            int innerWidth = 1920;
            for (int capacity : new int[]{4, 5, 6}) {
                int slotWidth = DockGeometry.horizontalSlotWidthPx(innerWidth, capacity);
                int count = capacity;
                int offset = DockGeometry.horizontalCenterOffsetPx(innerWidth, slotWidth, count);
                int right = offset + slotWidth * count;
                int rightMargin = innerWidth - right;
                assertTrue(offset >= 0);
                assertTrue(rightMargin >= 0);
                assertTrue(Math.abs(offset - rightMargin) <= 1);
            }
        }
    }

    @Test
    public void horizontalDockCentersActualItemCountAndHandlesEmptyOrOversizedGroups() {
        assertEquals(0, DockGeometry.horizontalCenterOffsetPx(1920, 320, 0));
        assertEquals(0, DockGeometry.horizontalCenterOffsetPx(1920, 320, 8));
        assertEquals(480, DockGeometry.horizontalCenterOffsetPx(1920, 320, 3));
    }


    @Test
    public void horizontalGridViewportShowsExactlyOneItemRow() {
        for (float density : new float[]{1f, 1.5f, 1.75f, 2f}) {
            for (int capacity : new int[]{4, 5, 6}) {
                int width = DockGeometry.horizontalTileWidthPx(1920, capacity, density);
                int height = DockGeometry.horizontalGridViewportHeightPx(width, density,
                        Math.round(10f * density));
                assertEquals(DockGeometry.itemHeightPx(DockGeometry.tileHeightPx(width), density)
                        + Math.round(10f * density), height);
            }
        }
    }

    @Test
    public void p2SafeDockGeometryIsSymmetricAcrossDensitiesCapacitiesAndViewports() {
        for (float density : new float[]{1f, 1.5f, 1.75f, 2f}) {
            for (int viewport : new int[]{1280, 1920}) {
                int safeWidth = DockGeometry.horizontalContentWidthPx(viewport, density);
                assertEquals(5, DockGeometry.horizontalGridColumns());
                assertEquals(viewport, safeWidth);
                for (int capacity : new int[]{4, 5, 6}) {
                    int slotWidth = DockGeometry.horizontalSlotWidthPx(safeWidth, capacity);
                    int offset = DockGeometry.horizontalCenterOffsetPx(safeWidth, slotWidth, capacity);
                    int groupWidth = capacity * slotWidth;
                    int rightMargin = safeWidth - offset - groupWidth;
                    assertTrue(Math.abs(offset - rightMargin) <= 1);
                }
            }
        }
    }

    @Test
    public void p2DockTrayBottomAndShelfBaselineUseSafeMarginsAndSharedGeometry() {
        for (float density : new float[]{1f, 1.5f, 1.75f, 2f}) {
            int tileHeight = DockGeometry.tileHeightPx(Math.round(180f * density));
            int dockHeight = DockGeometry.horizontalDockHeightPx(tileHeight, density);
            int bottomMargin = DockGeometry.horizontalDockBottomMarginPx(tileHeight, density);
            int trayBottomInset = dockHeight - tileHeight
                    - DockGeometry.dockItemTopOffsetPx(density)
                    - Math.round(DockGeometry.trayPaddingPx(density));
            assertEquals(DockGeometry.safeBottomInsetPx(density), bottomMargin + trayBottomInset);
            assertEquals(Math.round(50f * density),
                    DockGeometry.shelfBottomOffsetFromTileTopPx(density));
            int gridBottomMargin = DockGeometry.horizontalGridBottomMarginPx(tileHeight, density);
            int trayTopOffset = DockGeometry.dockItemTopOffsetPx(density)
                    - Math.round(DockGeometry.trayPaddingPx(density));
            int trayTopFromParentBottom = -dockHeight - bottomMargin + trayTopOffset;
            int gridBottomFromParentBottom = -gridBottomMargin;
            assertEquals(Math.round(8f * density), trayTopFromParentBottom - gridBottomFromParentBottom);
            assertEquals(dockHeight, gridBottomMargin);
        }
    }


    @Test
    public void dockAndFiveColumnGridShareTileAndCellGeometryAtAllCapacities() {
        for (float density : new float[]{1f, 1.5f, 1.75f, 2f}) {
            for (int viewport : new int[]{1280, 1920}) {
                int safeWidth = DockGeometry.horizontalContentWidthPx(viewport, density);
                for (int capacity : new int[]{4, 5, 6}) {
                    int gridWidth = DockGeometry.horizontalGridWidthPx(safeWidth, capacity);
                    int gridTileWidth = AppAdapter.tileWidthPx(gridWidth, 5, density);
                    int dockTileWidth = DockGeometry.horizontalTileWidthPx(safeWidth, capacity, density);
                    int slotWidth = DockGeometry.horizontalSlotWidthPx(safeWidth, capacity);
                    assertEquals(gridTileWidth, dockTileWidth);
                    assertTrue(slotWidth >= dockTileWidth);
                    int offset = DockGeometry.horizontalCenterOffsetPx(safeWidth, slotWidth, capacity);
                    int rightMargin = safeWidth - offset - slotWidth * capacity;
                    assertTrue(Math.abs(offset - rightMargin) <= 1);
                }
            }
        }
    }

    @Test
    public void horizontalDockPaddingCentersIconsWithoutMovingTheTrayHost() {
        for (int viewport : new int[]{1280, 1920}) {
            for (int capacity : new int[]{4, 5, 6}) {
                int slot = DockGeometry.horizontalSlotWidthPx(viewport, capacity);
                for (int count = 1; count <= capacity; count++) {
                    int inset = DockGeometry.horizontalCenterOffsetPx(viewport, slot, count);
                    int rightInset = viewport - inset - slot * count;
                    assertTrue(inset >= 0);
                    assertTrue(Math.abs(inset - rightInset) <= 1);
                    assertEquals(viewport, DockGeometry.horizontalContentWidthPx(viewport, 2f));
                }
            }
        }
    }


    @Test
    public void measuredDockViewportGeometryIsStableAcrossInitialZeroAndResize() {
        for (int viewport : new int[]{1280, 1696, 1712, 1920}) {
            for (int capacity : new int[]{4, 5, 6}) {
                int slot = DockGeometry.horizontalSlotWidthPx(viewport, capacity);
                assertTrue(slot > 0);
                int tile = DockGeometry.horizontalTileWidthPx(viewport, capacity, 2f);
                assertTrue(tile > 0);
                assertTrue(tile <= slot);
                for (int count = 1; count <= capacity; count++) {
                    int left = DockGeometry.horizontalCenterOffsetPx(viewport, slot, count);
                    int right = viewport - left - slot * count;
                    assertTrue(left >= 0);
                    assertTrue(right >= 0);
                    assertTrue(Math.abs(left - right) <= 1);
                }
            }
        }
    }

}
