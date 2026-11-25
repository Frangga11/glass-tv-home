package com.gathin.tvhome;

import android.content.ComponentName;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@org.robolectric.annotation.Config(sdk = 34)
public class ClassicModeContractTest {
    private Context context;
    private AppAdapter adapter;
    private String one;
    private String two;
    private String three;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.getApplication();
        context.getSharedPreferences("tiles", Context.MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences(DockPreferences.PREFS, Context.MODE_PRIVATE).edit().clear().commit();

        adapter = new AppAdapter(context, null, 5, Config.load(context));
        AppAdapter.Entry first = entry("One");
        AppAdapter.Entry second = entry("Two");
        AppAdapter.Entry third = entry("Three");
        one = first.component.flattenToShortString();
        two = second.component.flattenToShortString();
        three = third.component.flattenToShortString();

        inventory(adapter).replace(Arrays.asList(first, second, third));
        assertTrue(DockPreferences.savePinnedState(context,
                Arrays.asList(one), Arrays.asList(two, three)));
    }

    @Test
    public void classicGridDpadMappingHasHardEdgesAndClampsIncompleteRows() {
        assertEquals(-1, DockLayoutModel.gridNeighborPosition(0, -1, 5, 12));
        assertEquals(1, DockLayoutModel.gridNeighborPosition(0, 1, 5, 12));
        assertEquals(0, DockLayoutModel.gridNeighborPosition(5, -2, 5, 12));
        assertEquals(11, DockLayoutModel.gridNeighborPosition(7, 2, 5, 12));
        assertEquals(-1, DockLayoutModel.gridNeighborPosition(11, 1, 5, 12));
    }

    @Test
    public void focusScaleMatchesClassicDockAndGridWithoutChangingLayoutReservation() {
        assertEquals(1.12f, AppAdapter.focusScale(true), 0.001f);
        assertEquals(1.10f, AppAdapter.focusScale(false), 0.001f);
        assertEquals(1.07f, AppAdapter.LAYOUT_FOCUS_RESERVATION_SCALE, 0.001f);
    }

    @Test
    public void classicGridOwnsFocusScrollingAndSuppressesRecyclerViewAutoScroll() {
        FocusControlledGridLayoutManager layout =
                new FocusControlledGridLayoutManager(context, 5);
        assertTrue(layout.onRequestChildFocus(null, null, null, null));
    }

    @Test
    public void backCancelsPreviewWithoutPersistingGridOrderOrDockPins() {
        assertTrue(adapter.beginMoveTransaction());
        assertTrue(adapter.move(0, 1));

        assertEquals(three, adapter.keyAt(0));
        assertEquals("", context.getSharedPreferences("tiles", Context.MODE_PRIVATE)
                .getString("order", ""));
        assertEquals(Arrays.asList(one), DockPreferences.loadPinnedKeys(context));

        adapter.cancelMoveTransaction();

        assertEquals(two, adapter.keyAt(0));
        assertEquals(three, adapter.keyAt(1));
        assertEquals("", context.getSharedPreferences("tiles", Context.MODE_PRIVATE)
                .getString("order", ""));
        assertEquals(Arrays.asList(one), DockPreferences.loadPinnedKeys(context));
        assertFalse(adapter.isMoveTransactionActive());
    }

    @Test
    public void classicDockReorderAlsoUpdatesTheSingleGlobalAppOrder() {
        assertTrue(DockPreferences.savePinnedState(context,
                Arrays.asList(one, two), Arrays.asList(three)));
        assertTrue(adapter.beginMoveTransaction());
        assertTrue(adapter.moveDock(0, 1));

        assertEquals(two, adapter.dockSnapshot().dockItems.get(0).component.flattenToShortString());
        assertEquals("", context.getSharedPreferences("tiles", Context.MODE_PRIVATE)
                .getString("order", ""));

        assertTrue(adapter.commitMoveTransaction());

        assertEquals(two + "\n" + one + "\n" + three,
                context.getSharedPreferences("tiles", Context.MODE_PRIVATE).getString("order", ""));
        assertEquals(Arrays.asList(two, one), DockPreferences.loadPinnedKeys(context));
    }

    @Test
    public void verticalClassicModeDerivesDockAndGridFromOneCommittedOrder() throws Exception {
        AppAdapter.Entry fourth = entry("Four");
        AppAdapter.Entry fifth = entry("Five");
        AppAdapter.Entry sixth = entry("Six");
        AppAdapter.Entry seventh = entry("Seven");
        List<AppAdapter.Entry> sevenApps = new ArrayList<>(adapter.allEntriesSnapshot());
        sevenApps.add(fourth);
        sevenApps.add(fifth);
        sevenApps.add(sixth);
        sevenApps.add(seventh);
        inventory(adapter).replace(sevenApps);

        adapter.setClassicVerticalMode(true);
        assertEquals(5, adapter.dockSnapshot().dockItems.size());
        assertEquals(2, adapter.dockSnapshot().gridItems.size());
        assertTrue(adapter.beginMoveTransaction());
        assertTrue(adapter.moveAllEntries(0, 5));

        assertEquals(5, adapter.dockSnapshot().dockItems.size());
        assertEquals(sixth.component.flattenToShortString(),
                adapter.dockSnapshot().dockItems.get(4).component.flattenToShortString());
        assertEquals(one, adapter.dockSnapshot().gridItems.get(0).component.flattenToShortString());
        assertEquals("", context.getSharedPreferences("tiles", Context.MODE_PRIVATE)
                .getString("order", ""));

        assertTrue(adapter.commitMoveTransaction());
        assertEquals(pkg(two) + "\n" + pkg(three) + "\n" + fourth.pkg
                        + "\n" + fifth.pkg + "\n" + sixth.pkg + "\n" + pkg(one)
                        + "\n" + seventh.pkg,
                context.getSharedPreferences("tiles", Context.MODE_PRIVATE).getString("order", ""));
        assertEquals(Arrays.asList(one), DockPreferences.loadPinnedKeys(context));
    }

    @Test
    public void okCommitsPreviewedGridOrderAndDockMembership() {
        assertTrue(adapter.beginMoveTransaction());
        assertTrue(adapter.move(0, 1));
        assertTrue(adapter.pinToDock(two, 1));

        // The preview is visible in the adapters but remains absent from persisted preferences.
        assertEquals(2, adapter.dockSnapshot().dockItems.size());
        assertEquals(Arrays.asList(one), DockPreferences.loadPinnedKeys(context));
        assertEquals(Arrays.asList(two, three), DockPreferences.loadUnpinnedKeys(context));

        assertTrue(adapter.commitMoveTransaction());

        assertEquals(one + "\n" + two + "\n" + three,
                context.getSharedPreferences("tiles", Context.MODE_PRIVATE).getString("order", ""));
        assertEquals(Arrays.asList(one, two), DockPreferences.loadPinnedKeys(context));
        assertEquals(Arrays.asList(three), DockPreferences.loadUnpinnedKeys(context));
        assertFalse(adapter.isMoveTransactionActive());
    }

    private AppAdapter.Entry entry(String name) {
        String pkg = "com.example.classic." + name.toLowerCase(java.util.Locale.ROOT);
        ComponentName component = new ComponentName(pkg, pkg + "." + name + "Activity");
        return new AppAdapter.Entry(name, component, null, null);
    }

    private String pkg(String componentKey) {
        return componentKey.substring(0, componentKey.indexOf('/'));
    }

    @SuppressWarnings("unchecked")
    private static AppInventory<AppAdapter.Entry> inventory(AppAdapter adapter) throws Exception {
        Field field = AppAdapter.class.getDeclaredField("items");
        field.setAccessible(true);
        return (AppInventory<AppAdapter.Entry>) field.get(adapter);
    }
}
