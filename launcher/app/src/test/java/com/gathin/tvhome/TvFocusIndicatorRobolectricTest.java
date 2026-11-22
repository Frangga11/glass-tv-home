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
    public void sharedRoundedBackgroundSeparatesDisabledPressedFocusedAndNormalStates() {
        Activity activity = Robolectric.buildActivity(TestActivity.class).setup().get();
        StateListDrawable states = TvFocusIndicator.createRoundedStateBackground(
                1f, 10f, android.graphics.Color.TRANSPARENT, TvUiTokens.FOCUS_HALO,
                android.graphics.Color.TRANSPARENT, TvUiTokens.FOCUS_BORDER);
        states.setState(new int[]{-android.R.attr.state_enabled});
        Drawable disabled = states.getCurrent();
        assertNotNull(disabled);
        states.setState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed});
        Drawable pressed = states.getCurrent();
        assertNotNull(pressed);
        assertNotSame(disabled, pressed);
        states.setState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused});
        Drawable focused = states.getCurrent();
        assertNotNull(focused);
        assertNotSame(pressed, focused);
        states.setState(new int[]{android.R.attr.state_enabled});
        Drawable normal = states.getCurrent();
        assertNotNull(normal);
        assertNotSame(focused, normal);
    }

    @Test
    public void settingsCursorUsesOneStatefulForegroundWithoutReplacingBackground() {
        Activity activity = Robolectric.buildActivity(TestActivity.class).setup().get();
        android.view.View view = new android.view.View(activity);
        Drawable background = new GradientDrawable();
        view.setBackground(background);

        SettingsFocusCursorDrawable cursor = new SettingsFocusCursorDrawable(1f, 12f);
        assertNotNull(cursor);
        assertTrue(cursor.isStateful());
        cursor.setBounds(0, 0, 120, 48);
        cursor.setState(new int[]{android.R.attr.state_focused});
        view.setForeground(cursor);
        assertTrue(view.getForeground() == cursor);
        assertTrue(view.getBackground() == background);

        cursor.setState(new int[]{});
        assertTrue(view.getForeground() == cursor);
    }
}
