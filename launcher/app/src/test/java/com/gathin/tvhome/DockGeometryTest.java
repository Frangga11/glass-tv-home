package com.gathin.tvhome;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DockGeometryTest {
    @Test
    public void homeFrameUsesEqualHorizontalAndVerticalSafeMargins() {
        assertEquals(34, DockGeometry.safeHorizontalMarginDp());
        assertEquals(34, DockGeometry.safeVerticalMarginDp());
    }

    @Test
    public void safeInsetsConvertUsingTheSameDisplayDensity() {
        assertEquals(68, DockGeometry.safeHorizontalInsetPx(2f));
        assertEquals(68, DockGeometry.safeBottomInsetPx(2f));
    }
}
