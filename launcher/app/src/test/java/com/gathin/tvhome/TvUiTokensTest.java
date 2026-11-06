package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TvUiTokensTest {
    @Test
    public void primaryAndSecondaryTextMeetHighContrastTargetOnSharedSurfaces() {
        int[] surfaces = {
                TvUiTokens.SHELL, TvUiTokens.SURFACE,
                TvUiTokens.POPUP_SURFACE, TvUiTokens.SURFACE_FOCUSED
        };
        for (int surface : surfaces) {
            assertTrue("primary text contrast on " + Integer.toHexString(surface),
                    contrast(TvUiTokens.TEXT_PRIMARY, surface) >= 7.0);
            assertTrue("secondary text contrast on " + Integer.toHexString(surface),
                    contrast(TvUiTokens.TEXT_SECONDARY, surface) >= 7.0);
        }
    }

    @Test
    public void focusAccentRemainsDistinctFromEverySharedSurface() {
        int[] surfaces = {
                TvUiTokens.SHELL, TvUiTokens.SURFACE,
                TvUiTokens.POPUP_SURFACE, TvUiTokens.SURFACE_FOCUSED
        };
        for (int surface : surfaces) {
            assertTrue("focus contrast on " + Integer.toHexString(surface),
                    contrast(TvUiTokens.FOCUS_BORDER, surface) >= 3.0);
        }
    }

    private static double contrast(int foreground, int background) {
        double first = luminance(foreground);
        double second = luminance(background);
        double lighter = Math.max(first, second);
        double darker = Math.min(first, second);
        return (lighter + 0.05) / (darker + 0.05);
    }

    private static double luminance(int color) {
        double red = channel((color >>> 16) & 0xFF);
        double green = channel((color >>> 8) & 0xFF);
        double blue = channel(color & 0xFF);
        return 0.2126 * red + 0.7152 * green + 0.0722 * blue;
    }

    private static double channel(int value) {
        double normalized = value / 255.0;
        return normalized <= 0.04045 ? normalized / 12.92
                : Math.pow((normalized + 0.055) / 1.055, 2.4);
    }
}
