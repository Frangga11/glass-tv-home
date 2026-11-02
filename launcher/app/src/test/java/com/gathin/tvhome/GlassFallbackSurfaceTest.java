package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import android.graphics.Color;

/** Regression guard: disabling a decorative effect must not make app-owned surfaces transparent. */
public class GlassFallbackSurfaceTest {

    @Test
    public void popupFallbackIsOpaqueAndUsesSharedBorder() {
        assertEquals(255, Color.alpha(TvUiTokens.POPUP_SURFACE));
        assertEquals(255, Color.alpha(TvUiTokens.BORDER));
    }

    @Test
    public void dockFallbackIsOpaqueAndUsesSharedBorder() {
        assertEquals(255, Color.alpha(TvUiTokens.DOCK_SURFACE));
        assertEquals(255, Color.alpha(TvUiTokens.BORDER));
    }

    @Test
    public void popupAndDockFallbacksRemainDistinctSurfaces() {
        org.junit.Assert.assertNotEquals(TvUiTokens.POPUP_SURFACE, TvUiTokens.DOCK_SURFACE);
    }
}
