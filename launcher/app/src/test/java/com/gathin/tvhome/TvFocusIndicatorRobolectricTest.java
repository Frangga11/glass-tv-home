package com.gathin.tvhome;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30)
public class TvFocusIndicatorRobolectricTest {
    public static class TestActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setTheme(android.R.style.Theme_Material);
            super.onCreate(savedInstanceState);
        }
    }

    @Test
    public void focusOutlinePreservesAndRestoresExistingForeground() {
        Activity activity = Robolectric.buildActivity(TestActivity.class).setup().get();
        View view = new View(activity);
        Drawable original = new GradientDrawable();
        view.setForeground(original);

        TvFocusIndicator.updateFocusOutline(view, true, 2f, 14f);
        Drawable focused = view.getForeground();
        assertNotNull(focused);
        assertNotSame(original, focused);

        TvFocusIndicator.updateFocusOutline(view, false, 2f, 14f);
        assertSame(original, view.getForeground());
    }

    @Test
    public void focusOutlineRestoresNullForegroundWithoutChangingBackground() {
        Activity activity = Robolectric.buildActivity(TestActivity.class).setup().get();
        View view = new View(activity);
        Drawable originalBackground = new GradientDrawable();
        view.setBackground(originalBackground);

        TvFocusIndicator.updateFocusOutline(view, true, 2f, 12f);
        assertNotNull(view.getForeground());
        TvFocusIndicator.updateFocusOutline(view, false, 2f, 12f);

        assertNull(view.getForeground());
        assertSame(originalBackground, view.getBackground());
    }
}
