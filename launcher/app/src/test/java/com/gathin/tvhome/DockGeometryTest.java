package com.gathin.tvhome;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DockGeometryTest {
    @Test
    public void homeFrameUsesEqualHorizontalAndVerticalSafeMargins() {
        assertEquals(48, DockGeometry.safeHorizontalMarginDp());
        assertEquals(48, DockGeometry.safeVerticalMarginDp());
    }

    @Test
    public void safeInsetsConvertUsingTheSameDisplayDensity() {
        assertEquals(96, DockGeometry.safeHorizontalInsetPx(2f));
        assertEquals(96, DockGeometry.safeBottomInsetPx(2f));
    }
}
