package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Looper;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class QuickSettingsDialogTest {
    private static final class RecordingActions implements QuickSettingsDialog.Actions {
        String lastAction;
        @Override public String wifiSummary() { return "Connected"; }
        @Override public String languageSummary() { return "ID"; }
        @Override public String powerTitle() { return "Power Off"; }
        @Override public String cacheTitle() { return "Clean Cache"; }
        @Override public String wallpaperIntervalSummary() { return "Every 10 minutes"; }
        @Override public void openWifi() { lastAction = "wifi"; }
        @Override public void restart() { lastAction = "restart"; }
        @Override public void chooseLanguage() { lastAction = "language"; }
        @Override public void openPowerMenu() { lastAction = "power"; }
        @Override public void cleanCache() { lastAction = "cache"; }
        @Override public void nextPhoto() { lastAction = "next_photo"; }
        @Override public void chooseWallpaperInterval() { lastAction = "interval"; }
        @Override public void manageWallpaper() { lastAction = "manage_wallpaper"; }
        @Override public void openGlassSettings() { lastAction = "glass_settings"; }
        @Override public void openAndroidSettings() { lastAction = "android_settings"; }
    }

    private Activity newActivity() {
        return Robolectric.buildActivity(Activity.class).setup().get();
    }

    private View find(AlertDialog dialog, String tag) {
        return dialog.getWindow().getDecorView().findViewWithTag(tag);
    }

    private void assertAction(Activity activity, RecordingActions actions, String tag, String expected) {
        final int[] dismissed = {0};
        actions.lastAction = null;
        AlertDialog dialog = QuickSettingsDialog.show(activity, actions,
                QuickSettingsFocusGraph.WIFI, new View(activity), () -> dismissed[0]++);
        assertNotNull(dialog);
        View control = find(dialog, tag);
        assertNotNull("missing control " + tag, control);
        assertTrue(control.isFocusable());
        assertTrue(control.isClickable());
        assertTrue(control.performClick());
        Shadows.shadowOf(Looper.getMainLooper()).idle();
        assertEquals(expected, actions.lastAction);
        assertEquals(1, dismissed[0]);
        assertFalse(dialog.isShowing());
    }

    @Test
    public void everyQuickActionDispatchesToItsOwnerAndDismissesTheModal() {
        Activity activity = newActivity();
        RecordingActions actions = new RecordingActions();
        assertAction(activity, actions, "quick_settings_wifi", "wifi");
        assertAction(activity, actions, "quick_settings_restart", "restart");
        assertAction(activity, actions, "quick_settings_language", "language");
        assertAction(activity, actions, "quick_settings_power", "power");
        assertAction(activity, actions, "quick_settings_cache", "cache");
        assertAction(activity, actions, "quick_settings_next_photo", "next_photo");
        assertAction(activity, actions, "quick_settings_interval", "interval");
        assertAction(activity, actions, "quick_settings_manage_wallpaper", "manage_wallpaper");
        assertAction(activity, actions, "quick_settings_glass_settings", "glass_settings");
        assertAction(activity, actions, "quick_settings_android_settings", "android_settings");
    }

    @Test
    public void dpadFocusLinksAreAppliedToActualDialogViews() {
        Activity activity = newActivity();
        AlertDialog dialog = QuickSettingsDialog.show(activity, new RecordingActions(),
                QuickSettingsFocusGraph.WIFI, new View(activity), null);
        View wifi = find(dialog, "quick_settings_wifi");
        View restart = find(dialog, "quick_settings_restart");
        View language = find(dialog, "quick_settings_language");
        View power = find(dialog, "quick_settings_power");
        View cache = find(dialog, "quick_settings_cache");
        View manageWallpaper = find(dialog, "quick_settings_manage_wallpaper");
        assertNotNull(wifi);
        assertNotNull(restart);
        assertNotNull(language);
        assertNotNull(power);
        assertNotNull(cache);
        assertNotNull(manageWallpaper);
        assertEquals(restart.getId(), wifi.getNextFocusRightId());
        assertEquals(wifi.getId(), wifi.getNextFocusLeftId());
        assertEquals(language.getId(), wifi.getNextFocusDownId());
        assertEquals(power.getId(), language.getNextFocusRightId());
        assertEquals(manageWallpaper.getId(), power.getNextFocusDownId());
        assertEquals(manageWallpaper.getId(), cache.getNextFocusDownId());
        View glass = find(dialog, "quick_settings_glass_settings");
        View android = find(dialog, "quick_settings_android_settings");
        View close = find(dialog, "quick_settings_close");
        assertNotNull(glass);
        assertNotNull(android);
        assertNotNull(close);
        assertEquals(android.getId(), glass.getNextFocusDownId());
        assertEquals(glass.getId(), android.getNextFocusUpId());
        assertFalse(close.isFocusable());
        dialog.cancel();
    }

    @Test
    public void backCancellationDismissesModalWithoutDispatchingAction() {
        Activity activity = newActivity();
        RecordingActions actions = new RecordingActions();
        final int[] dismissed = {0};
        AlertDialog dialog = QuickSettingsDialog.show(activity, actions,
                QuickSettingsFocusGraph.WIFI, () -> dismissed[0]++);
        assertNotNull(dialog);
        dialog.cancel();
        Shadows.shadowOf(Looper.getMainLooper()).idle();
        assertEquals(1, dismissed[0]);
        assertEquals(null, actions.lastAction);
        assertFalse(dialog.isShowing());
    }
}
