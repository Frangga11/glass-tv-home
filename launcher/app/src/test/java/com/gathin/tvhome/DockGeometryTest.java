package com.gathin.tvhome;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DockGeometryTest {
    @Test
    public void homeFrameUsesEqualHorizontalAndVerticalSafeMargins() {
        assertEquals(29, DockGeometry.safeHorizontalMarginDp());
        assertEquals(29, DockGeometry.safeVerticalMarginDp());
    }

    @Test
    public void safeInsetsConvertUsingTheSameDisplayDensity() {
        assertEquals(58, DockGeometry.safeHorizontalInsetPx(2f));
        assertEquals(58, DockGeometry.safeBottomInsetPx(2f));
    }
}
