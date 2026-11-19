package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Looper;
import android.view.View;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

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
        @Override public String languageSummary() { return "ID"; }
        @Override public String powerTitle() { return "Power Off"; }
        @Override public String cacheTitle() { return "Clean Cache"; }
        @Override public String wallpaperIntervalSummary() { return "Every 10 minutes"; }
        @Override public void openInputs() { lastAction = "inputs"; }
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

    private FrameLayout[] host(Activity activity) {
        return host(activity, 400);
    }

    private FrameLayout[] host(Activity activity, int dockTop) {
        FrameLayout root = new FrameLayout(activity);
        activity.setContentView(root);
        // A 1920x1080 screen at 2x density is a 960x540dp layout canvas.
        root.layout(0, 0, 960, 540);
        View settings = new View(activity);
        root.addView(settings, new FrameLayout.LayoutParams(320, 30, Gravity.TOP | Gravity.CENTER_HORIZONTAL));
        settings.layout(320, 60, 640, 90);
        View dock = new View(activity);
        root.addView(dock, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, 60, Gravity.BOTTOM));
        dock.layout(0, dockTop, 960, dockTop + 60);
        return new FrameLayout[]{root, (FrameLayout) settings.getParent(), (FrameLayout) dock.getParent()};
    }

    private View find(AlertDialog dialog, String tag) {
        return dialog.getWindow().getDecorView().findViewWithTag(tag);
    }

    private void assertAction(Activity activity, RecordingActions actions, String tag, String expected) {
        final int[] dismissed = {0};
        actions.lastAction = null;
        FrameLayout[] views = host(activity);
        View anchor = views[1].getChildAt(0);
        View dock = views[2].getChildAt(1);
        AlertDialog dialog = QuickSettingsDialog.show(activity, views[0], anchor, dock, actions,
                QuickSettingsFocusGraph.INPUT, () -> dismissed[0]++);
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
    public void panelFillsAvailableHeightWithExactFiveDpClearanceAtBothAnchors() {
        Activity activity = newActivity();
        FrameLayout[] views = host(activity, 400);
        AlertDialog dialog = QuickSettingsDialog.show(activity, views[0], views[0].getChildAt(0),
                views[0].getChildAt(1), new RecordingActions(), QuickSettingsFocusGraph.INPUT, null);
        assertNotNull(dialog);

        int[] hostAt = new int[2], anchorAt = new int[2], dockAt = new int[2];
        views[0].getLocationOnScreen(hostAt);
        views[0].getChildAt(0).getLocationOnScreen(anchorAt);
        views[0].getChildAt(1).getLocationOnScreen(dockAt);
        int panelTop = QuickSettingsDialog.panelTopInHost(anchorAt[1],
                views[0].getChildAt(0).getHeight(), hostAt[1], 5);
        int dockTop = dockAt[1] - hostAt[1];
        int expectedHeight = QuickSettingsDialog.availablePanelHeight(dockTop, panelTop, 5);
        View content = find(dialog, "quick_settings_root");
        assertNotNull(content);
        assertEquals(300, expectedHeight);
        assertEquals(expectedHeight, content.getMeasuredHeight());

        int[] spacerHeights = {
                find(dialog, "quick_settings_flex_header").getMeasuredHeight(),
                find(dialog, "quick_settings_flex_grid").getMeasuredHeight(),
                find(dialog, "quick_settings_flex_wallpaper_header").getMeasuredHeight()
        };
        assertEquals(Math.round(activity.getResources().getDisplayMetrics().density * 5f),\n                find(dialog, "quick_settings_gap_wallpaper_to_settings").getMeasuredHeight());
        int min = spacerHeights[0], max = spacerHeights[0];
        for (int height : spacerHeights) {
            min = Math.min(min, height);
            max = Math.max(max, height);
        }
        assertTrue("Flexible vertical gaps must distribute remaining height evenly", max - min <= 1);
        dialog.dismiss();
    }

    @Test
    public void constrainedPanelTightensOnlyVerticalRhythmAndKeepsCapsuleGeometry() {
        Activity activity = newActivity();
        FrameLayout[] views = host(activity, 400);
        AlertDialog dialog = QuickSettingsDialog.show(activity, views[0], views[0].getChildAt(0),
                views[0].getChildAt(1), new RecordingActions(), QuickSettingsFocusGraph.INPUT, null);
        assertNotNull(dialog);

        int[] hostAt = new int[2], anchorAt = new int[2], dockAt = new int[2];
        views[0].getLocationOnScreen(hostAt);
        views[0].getChildAt(0).getLocationOnScreen(anchorAt);
        views[0].getChildAt(1).getLocationOnScreen(dockAt);
        int panelTop = QuickSettingsDialog.panelTopInHost(anchorAt[1],
                views[0].getChildAt(0).getHeight(), hostAt[1], 5);
        int dockTop = dockAt[1] - hostAt[1];
        int expectedHeight = QuickSettingsDialog.availablePanelHeight(dockTop, panelTop, 5);
        assertEquals(300, expectedHeight);
        assertEquals(expectedHeight, find(dialog, "quick_settings_root").getMeasuredHeight());
        assertEquals(34, find(dialog, "quick_settings_input").getMeasuredHeight());
        assertEquals(34, find(dialog, "quick_settings_restart").getMeasuredHeight());
        assertEquals(5, dockTop - (panelTop + expectedHeight));
        dialog.dismiss();
    }

    @Test
    public void everyQuickActionDispatchesToItsOwnerAndDismissesTheModal() {
        Activity activity = newActivity();
        RecordingActions actions = new RecordingActions();
        assertAction(activity, actions, "quick_settings_input", "inputs");
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
        FrameLayout[] views = host(activity);
        AlertDialog dialog = QuickSettingsDialog.show(activity, views[0], views[0].getChildAt(0),
                views[0].getChildAt(1), new RecordingActions(), QuickSettingsFocusGraph.INPUT, null);
        View input = find(dialog, "quick_settings_input");
        View restart = find(dialog, "quick_settings_restart");
        View language = find(dialog, "quick_settings_language");
        View power = find(dialog, "quick_settings_power");
        View cache = find(dialog, "quick_settings_cache");
        View nextPhoto = find(dialog, "quick_settings_next_photo");
        View interval = find(dialog, "quick_settings_interval");
        View manage = find(dialog, "quick_settings_manage_wallpaper");
        View androidSettings = find(dialog, "quick_settings_android_settings");
        assertNotNull(input);
        assertFalse(((TextView) input).getText().toString().contains("\n"));
        assertEquals(activity.getString(R.string.quick_settings_input_title), ((TextView) input).getText().toString());
        assertNotNull(restart);
        assertNotNull(language);
        assertNotNull(power);
        assertNotNull(cache);
        assertNotNull(nextPhoto);
        assertNotNull(interval);
        assertNotNull(manage);
        assertNotNull(androidSettings);
        assertEquals(restart.getId(), input.getNextFocusRightId());
        assertEquals(input.getId(), input.getNextFocusLeftId());
        assertEquals(language.getId(), input.getNextFocusDownId());
        assertEquals(input.getId(), restart.getNextFocusLeftId());
        assertEquals(power.getId(), language.getNextFocusRightId());
        assertEquals(cache.getId(), power.getNextFocusDownId());
        assertEquals(nextPhoto.getId(), cache.getNextFocusDownId());
        assertEquals(cache.getId(), nextPhoto.getNextFocusUpId());
        assertEquals(manage.getId(), interval.getNextFocusUpId());
        assertEquals(interval.getId(), manage.getNextFocusDownId());
        assertFalse(((TextView) restart).getText().toString().contains("\n"));
        assertFalse(((TextView) find(dialog, "quick_settings_power")).getText().toString().contains("\n"));
        assertFalse(((TextView) nextPhoto).getText().toString().contains("\n"));
        assertTrue(((TextView) androidSettings).getText().toString().contains(activity.getString(R.string.quick_settings_android_settings)));
        assertEquals("Restart TV", ((TextView) restart).getText().toString());
        assertEquals(activity.getString(R.string.quick_settings_next_photo), ((TextView) nextPhoto).getText().toString());
        dialog.cancel();
    }

    @Test
    public void backCancellationDismissesModalWithoutDispatchingAction() {
        Activity activity = newActivity();
        RecordingActions actions = new RecordingActions();
        final int[] dismissed = {0};
        FrameLayout[] views = host(activity);
        AlertDialog dialog = QuickSettingsDialog.show(activity, views[0], views[0].getChildAt(0),
                views[0].getChildAt(1), actions, QuickSettingsFocusGraph.INPUT, () -> dismissed[0]++);
        assertNotNull(dialog);
        dialog.cancel();
        Shadows.shadowOf(Looper.getMainLooper()).idle();
        assertEquals(1, dismissed[0]);
        assertEquals(null, actions.lastAction);
        assertFalse(dialog.isShowing());
    }
}
