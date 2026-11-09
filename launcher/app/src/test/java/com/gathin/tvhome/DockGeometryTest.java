package com.gathin.tvhome;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DockGeometryTest {
    @Test
    public void homeFrameUsesEqualHorizontalAndVerticalSafeMargins() {
        assertEquals(30, DockGeometry.safeHorizontalMarginDp());
        assertEquals(30, DockGeometry.safeVerticalMarginDp());
        assertEquals(58, DockGeometry.safeTopInsetPx(2f));
    }

    @Test
    public void safeInsetsConvertUsingTheSameDisplayDensity() {
        assertEquals(60, DockGeometry.safeHorizontalInsetPx(2f));
        assertEquals(60, DockGeometry.safeBottomInsetPx(2f));
    }
}
