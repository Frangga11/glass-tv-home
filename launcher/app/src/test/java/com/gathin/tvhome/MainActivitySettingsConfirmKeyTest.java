package com.gathin.tvhome;

import android.view.KeyEvent;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainActivitySettingsConfirmKeyTest {
    @Test
    public void acceptsAndroidTvRemoteConfirmKeys() {
        assertTrue(MainActivity.isSettingsConfirmKey(KeyEvent.KEYCODE_DPAD_CENTER));
        assertTrue(MainActivity.isSettingsConfirmKey(KeyEvent.KEYCODE_ENTER));
        assertTrue(MainActivity.isSettingsConfirmKey(KeyEvent.KEYCODE_NUMPAD_ENTER));
        assertTrue(MainActivity.isSettingsConfirmKey(KeyEvent.KEYCODE_BUTTON_A));
    }

    @Test
    public void rejectsDirectionalAndBackKeys() {
        assertFalse(MainActivity.isSettingsConfirmKey(KeyEvent.KEYCODE_DPAD_UP));
        assertFalse(MainActivity.isSettingsConfirmKey(KeyEvent.KEYCODE_DPAD_DOWN));
        assertFalse(MainActivity.isSettingsConfirmKey(KeyEvent.KEYCODE_BACK));
    }
}
