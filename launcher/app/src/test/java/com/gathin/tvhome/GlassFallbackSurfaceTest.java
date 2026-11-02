package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/** Regression guard: disabling a decorative effect must not make app-owned surfaces transparent. */
public class GlassFallbackSurfaceTest {

    @Test
    public void popupFallbackIsOpaqueAndUsesSharedBorder() {
        assertEquals(255, (TvUiTokens.POPUP_SURFACE >>> 24));
        assertEquals(255, (TvUiTokens.BORDER >>> 24));
    }

    @Test
    public void dockFallbackIsOpaqueAndUsesSharedBorder() {
        assertEquals(255, (TvUiTokens.DOCK_SURFACE >>> 24));
        assertEquals(255, Color.alpha(TvUiTokens.BORDER));
    }

    @Test
    public void popupAndDockFallbacksRemainDistinctSurfaces() {
        org.junit.Assert.assertNotEquals(TvUiTokens.POPUP_SURFACE, TvUiTokens.DOCK_SURFACE);
    }
}
