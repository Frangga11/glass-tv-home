package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import android.view.ViewGroup;

public class TvDialogStyleTest {
    @Test
    public void boundsDialogWidthToRequestedSizeAndViewport() {
        assertEquals(1040, TvDialogStyle.resolveWidthPx(1920, 2f, 520));
        assertEquals(1126, TvDialogStyle.resolveWidthPx(1280, 2.5f, 620));
    }

    @Test
    public void boundsDialogHeightToViewport() {
        assertEquals(960, TvDialogStyle.resolveHeightPx(1200, 2f, 480));
        assertEquals(1056, TvDialogStyle.resolveHeightPx(1200, 2f, 700));
    }

    @Test
    public void allowsContentSizedDialogHeight() {
        assertEquals(ViewGroup.LayoutParams.WRAP_CONTENT,
                TvDialogStyle.resolveHeightPx(1080, 2f, 0));
    }
}
