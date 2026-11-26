package com.gathin.tvhome;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@org.robolectric.annotation.Config(sdk = 34)
public class GliderFocusHandoffTest {
    @Test
    public void absoluteHeaderHandoffClearsPreviousRecyclerScrollCompensation() throws Exception {
        Context context = RuntimeEnvironment.getApplication();
        Glider glider = new Glider(context, false);

        // Simulate the legacy RecyclerView scroll owner having moved the Glider.
        glider.scrolled(96f);
        assertEquals(-96f, scrollShift(glider), 0.001f);

        // Header coordinates are absolute window/root coordinates, not scroll-relative.
        glider.appearAt(120f, 40f, 260f, 100f, 20f);

        assertEquals(0f, scrollShift(glider), 0.001f);
    }

    private static float scrollShift(Glider glider) throws Exception {
        Field field = Glider.class.getDeclaredField("yShift");
        field.setAccessible(true);
        return field.getFloat(glider);
    }
}
