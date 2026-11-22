package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.graphics.Rect;
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
        return host(activity, 420);
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
    public void fixedPanelUsesExactGeometryAndKeepsFiveDpAnchorClearance() {
        Activity activity = newActivity();
        FrameLayout[] views = host(activity, 420);
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
        int availableHeight = QuickSettingsDialog.availablePanelHeight(dockTop, panelTop, 5);
        View content = find(dialog, "quick_settings_root");
        assertNotNull(content);
        assertEquals(320, availableHeight);
        assertEquals(Math.round(320f * activity.getResources().getDisplayMetrics().density),
                content.getMeasuredHeight());
        assertEquals(5, dockTop - (panelTop + content.getMeasuredHeight()));
        for (String tag : new String[] {
                "quick_settings_gap_header_grid",
                "quick_settings_gap_grid_wallpaper",
                "quick_settings_gap_wallpaper_header_actions",
                "quick_settings_gap_wallpaper_to_settings"
        }) {
            assertEquals(Math.round(5f * activity.getResources().getDisplayMetrics().density),
                    find(dialog, tag).getMeasuredHeight());
        }
        dialog.dismiss();
    }

    @Test
    public void insufficientHeightRejectsPopupWithoutCompressingFixedGeometry() {
        Activity activity = newActivity();
        FrameLayout[] views = host(activity, 410);
        AlertDialog dialog = QuickSettingsDialog.show(activity, views[0], views[0].getChildAt(0),
                views[0].getChildAt(1), new RecordingActions(), QuickSettingsFocusGraph.INPUT, null);
        assertEquals(null, dialog);
    }

    @Test
    public void fixedCapsuleGeometryIsPreserved() {
        Activity activity = newActivity();
        FrameLayout[] views = host(activity, 420);
        AlertDialog dialog = QuickSettingsDialog.show(activity, views[0], views[0].getChildAt(0),
                views[0].getChildAt(1), new RecordingActions(), QuickSettingsFocusGraph.INPUT, null);
        assertNotNull(dialog);
        assertEquals(320, find(dialog, "quick_settings_root").getMeasuredHeight());
        float density = activity.getResources().getDisplayMetrics().density;
        int halfWidth = Math.round(115f * density);
        int wideWidth = Math.round(235f * density);
        int mainHeight = Math.round(36f * density);
        int manageHeight = Math.round(20f * density);
        assertEquals(mainHeight, find(dialog, "quick_settings_input").getMeasuredHeight());
        assertEquals(mainHeight, find(dialog, "quick_settings_restart").getMeasuredHeight());
        assertEquals(mainHeight, find(dialog, "quick_settings_language").getMeasuredHeight());
        assertEquals(mainHeight, find(dialog, "quick_settings_power").getMeasuredHeight());
        assertEquals(mainHeight, find(dialog, "quick_settings_cache").getMeasuredHeight());
        assertEquals(mainHeight, find(dialog, "quick_settings_next_photo").getMeasuredHeight());
        assertEquals(manageHeight, find(dialog, "quick_settings_manage_wallpaper").getMeasuredHeight());
        assertEquals(halfWidth, find(dialog, "quick_settings_input").getMeasuredWidth());
        assertEquals(halfWidth, find(dialog, "quick_settings_restart").getMeasuredWidth());
        assertEquals(halfWidth, find(dialog, "quick_settings_next_photo").getMeasuredWidth());
        View manageCapsule = find(dialog, "quick_settings_manage_wallpaper");
        View intervalCapsule = find(dialog, "quick_settings_interval");
        int[] manageAt = new int[2], intervalAt = new int[2];
        manageCapsule.getLocationOnScreen(manageAt);
        intervalCapsule.getLocationOnScreen(intervalAt);
        assertEquals("Manage capsule right edge must align with the other capsule column",
                intervalAt[0] + intervalCapsule.getWidth(), manageAt[0] + manageCapsule.getWidth());
        assertEquals(halfWidth, find(dialog, "quick_settings_interval").getMeasuredWidth());
        assertEquals(wideWidth, find(dialog, "quick_settings_cache").getMeasuredWidth());
        assertEquals(wideWidth, find(dialog, "quick_settings_glass_settings").getMeasuredWidth());
        assertEquals(wideWidth, find(dialog, "quick_settings_android_settings").getMeasuredWidth());
        assertEquals(Math.round(5f * density), find(dialog, "quick_settings_gap_wallpaper_to_settings").getMeasuredHeight());
        dialog.dismiss();
    }

    @Test
    public void wallpaperHeadingPaintedLeftEdgeMatchesNextPhotoCapsuleLeftEdge() {
        Activity activity = newActivity();
        FrameLayout[] views = host(activity, 420);
        AlertDialog dialog = QuickSettingsDialog.show(activity, views[0], views[0].getChildAt(0),
                views[0].getChildAt(1), new RecordingActions(), QuickSettingsFocusGraph.INPUT, null);
        assertNotNull(dialog);
        Shadows.shadowOf(Looper.getMainLooper()).idle();

        TextView wallpaperTitle = (TextView) find(dialog, "quick_settings_wallpaper_title");
        View nextPhoto = find(dialog, "quick_settings_next_photo");
        assertNotNull(wallpaperTitle);
        assertNotNull(nextPhoto);
        assertEquals(0, wallpaperTitle.getPaddingStart());
        assertEquals(0, wallpaperTitle.getCompoundPaddingStart());

        String heading = wallpaperTitle.getText().toString();
        Rect inkBounds = new Rect();
        wallpaperTitle.getPaint().getTextBounds(heading, 0, heading.length(), inkBounds);
        int[] headingAt = new int[2];
        int[] capsuleAt = new int[2];
        wallpaperTitle.getLocationOnScreen(headingAt);
        nextPhoto.getLocationOnScreen(capsuleAt);
        float baseHeadingLeft = headingAt[0] - wallpaperTitle.getTranslationX();
        float expectedCorrection = capsuleAt[0] - baseHeadingLeft
                - wallpaperTitle.getCompoundPaddingLeft() - inkBounds.left;
        assertEquals("Runtime alignment must compensate for the actual layout/font bounds plus the approved 2dp right offset",
                expectedCorrection + 2f * activity.getResources().getDisplayMetrics().density,
                wallpaperTitle.getTranslationX(), 0.01f);
        assertEquals("Painted Wallpaper glyph must start exactly 2dp right of the capsule's left edge",
                capsuleAt[0] + Math.round(2f * activity.getResources().getDisplayMetrics().density),
                headingAt[0] + wallpaperTitle.getCompoundPaddingLeft() + inkBounds.left);
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
        assertEquals(cache.getId(), manage.getNextFocusUpId());
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
    @Test
    public void popupDiminishesHomeBehindGlassWithoutChangingPanelGeometry() {
        Activity activity = newActivity();
        FrameLayout[] views = host(activity);
        AlertDialog dialog = QuickSettingsDialog.show(activity, views[0], views[0].getChildAt(0),
                views[0].getChildAt(1), new RecordingActions(), QuickSettingsFocusGraph.INPUT, null);
        assertNotNull(dialog);
        android.view.WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        assertEquals(TvUiTokens.POPUP_DIM_AMOUNT, QuickSettingsDialog.popupDimAmount(), 0.001f);
        assertEquals(QuickSettingsDialog.popupDimAmount(), params.dimAmount, 0.001f);
        assertTrue((params.flags & android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND) != 0);
        View content = find(dialog, "quick_settings_root");
        assertNotNull(content);
        float density = activity.getResources().getDisplayMetrics().density;
        assertEquals(Math.round(320f * density), content.getMeasuredHeight());
        dialog.dismiss();
        Shadows.shadowOf(Looper.getMainLooper()).idle();
    }

}
