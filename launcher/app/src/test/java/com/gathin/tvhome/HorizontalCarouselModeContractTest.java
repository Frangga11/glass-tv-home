package com.gathin.tvhome;

import android.content.ComponentName;
import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@org.robolectric.annotation.Config(sdk = 34)
public class HorizontalCarouselModeContractTest {
    @Test
    public void horizontalCarouselUsesEntireInventoryWhileClassicDockUsesCapacity() throws Exception {
        Context context = RuntimeEnvironment.getApplication();
        context.getSharedPreferences("tiles", Context.MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences(DockPreferences.PREFS, Context.MODE_PRIVATE).edit().clear().commit();

        AppAdapter source = new AppAdapter(context, null, 5, Config.load(context));
        List<AppAdapter.Entry> inventory = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            String pkg = "com.example.carousel.app" + i;
            ComponentName component = new ComponentName(pkg, pkg + ".MainActivity");
            inventory.add(new AppAdapter.Entry("App " + i, component, null, null));
        }
        Field items = AppAdapter.class.getDeclaredField("items");
        items.setAccessible(true);
        ((AppInventory<AppAdapter.Entry>) items.get(source)).replace(inventory);

        source.setClassicVerticalMode(false);
        DockAdapter dock = new DockAdapter(context, source, null, false);
        dock.setCarouselMode(false);
        assertEquals(5, dock.getItemCount());

        dock.setCarouselMode(true);
        assertEquals(8, dock.getItemCount());
    }
}
