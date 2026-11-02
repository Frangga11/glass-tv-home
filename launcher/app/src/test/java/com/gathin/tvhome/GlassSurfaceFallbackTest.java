package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/** Regression guard for the shared global material policy when decorative glass is disabled. */
public class GlassSurfaceFallbackTest {
    @Test
    public void popupSurfaceAndBorderAreOpaque() {
        assertEquals(255, TvUiTokens.POPUP_SURFACE >>> 24);
        assertEquals(255, TvUiTokens.BORDER >>> 24);
    }

    @Test
    public void dockSurfaceAndBorderAreOpaque() {
        assertEquals(255, TvUiTokens.DOCK_SURFACE >>> 24);
        assertEquals(255, TvUiTokens.BORDER >>> 24);
    }

    @Test
    public void dockAndPopupUseDistinctSharedMaterials() {
        assertNotEquals(TvUiTokens.POPUP_SURFACE, TvUiTokens.DOCK_SURFACE);
    }
}
