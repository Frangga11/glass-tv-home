package com.gathin.tvhome;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;

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
    public void sharedFocusOutlineUsesStatefulForegroundWithoutReplacingBackground() {
        Activity activity = Robolectric.buildActivity(TestActivity.class).setup().get();
        android.view.View view = new android.view.View(activity);
        Drawable background = new GradientDrawable();
        view.setBackground(background);
        Drawable outline = activity.getDrawable(R.drawable.bg_tv_focus_outline);
        assertNotNull(outline);
        assertTrue(outline instanceof StateListDrawable);

        outline.setState(new int[]{android.R.attr.state_focused});
        assertTrue(((StateListDrawable) outline).getCurrent() instanceof GradientDrawable);
        Drawable focused = ((StateListDrawable) outline).getCurrent();
        outline.setState(new int[0]);
        assertNotSame(focused, ((StateListDrawable) outline).getCurrent());
        view.setForeground(outline);
        assertTrue(view.getBackground() == background);
    }
}
