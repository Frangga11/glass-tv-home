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
import android.view.ViewGroup;
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
@Config(sdk = 34, qualifiers = "w960dp-h540dp-xhdpi")
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
        return host(activity, DockPreferences.DEFAULT_CAPACITY);
    }

    private FrameLayout[] host(Activity activity, int capacity) {
        int[] production = productionDockMetricsPx(activity, capacity);
        return hostAtPx(activity, production[4], capacity);
    }

    private FrameLayout[] hostAtDp(Activity activity, int dockTopDp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return hostAtPx(activity, Math.round(dockTopDp * density));
    }

    private int minimumDockTopForFixedPanelPx(Activity activity) {
        float density = activity.getResources().getDisplayMetrics().density;
        int panelTopPx = Math.round((29f + 30f + 5f) * density);
        return panelTopPx + Math.round(330f * density) + Math.round(5f * density);
    }

    /** Derive the baseline Dock dimensions from the same geometry functions used by MainActivity. */
    private int[] productionDockMetricsPx(Activity activity) {
        return productionDockMetricsPx(activity, DockPreferences.DEFAULT_CAPACITY);
    }

    private int[] productionDockMetricsPx(Activity activity, int capacity) {
        float density = activity.getResources().getDisplayMetrics().density;
        int screenWidthPx = Math.round(960f * density);
        int screenHeightPx = Math.round(540f * density);
        // gridWrap is match-parent inside mainContent, whose horizontal padding is 30dp per side.
        int viewportWidthPx = screenWidthPx - 2 * DockGeometry.safeHorizontalInsetPx(density);
        int innerWidthPx = DockGeometry.horizontalContentWidthPx(viewportWidthPx, density);
        int dockTileWidthPx = DockGeometry.horizontalDockTileWidthPx(
                innerWidthPx, capacity, density);
        int dockTileHeightPx = DockGeometry.tileHeightPx(dockTileWidthPx);
        int dockHeightPx = DockGeometry.horizontalDockHeightPx(dockTileHeightPx, density);
        int dockBottomMarginPx = DockGeometry.horizontalDockBottomMarginPx(dockTileHeightPx, density);
        int dockTopPx = screenHeightPx - dockHeightPx - dockBottomMarginPx;
        return new int[]{screenWidthPx, screenHeightPx, dockHeightPx, dockBottomMarginPx, dockTopPx};
    }

    private FrameLayout[] hostWithVerticalDock(Activity activity) {
        android.util.DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        float density = metrics.density;
        int screenWidthPx = metrics.widthPixels;
        int screenHeightPx = metrics.heightPixels;
        FrameLayout root = new FrameLayout(activity);
        View settings = new View(activity);
        FrameLayout.LayoutParams settingsLp = new FrameLayout.LayoutParams(
                Math.round(320f * density), Math.round(30f * density),
                Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        settingsLp.topMargin = Math.round(29f * density);
        root.addView(settings, settingsLp);

        View dock = new View(activity);
        FrameLayout.LayoutParams dockLp = new FrameLayout.LayoutParams(
                Math.round(202f * density), ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.START);
        dockLp.bottomMargin = DockGeometry.safeBottomInsetPx(density);
        root.addView(dock, dockLp);

        activity.setContentView(root);
        root.measure(View.MeasureSpec.makeMeasureSpec(screenWidthPx, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(screenHeightPx, View.MeasureSpec.EXACTLY));
        root.layout(0, 0, screenWidthPx, screenHeightPx);
        assertTrue("Vertical Dock fixture must be visible", dock.isShown());
        assertEquals(0, dock.getTop());
        assertTrue("Vertical Dock must span almost the full host height",
                dock.getHeight() > screenHeightPx / 2);
        return new FrameLayout[]{root, root, root};
    }

    private FrameLayout[] hostAtPx(Activity activity, int dockTopPx) {
        return hostAtPx(activity, dockTopPx, DockPreferences.DEFAULT_CAPACITY);
    }

    private FrameLayout[] hostAtPx(Activity activity, int dockTopPx, int capacity) {
        android.util.DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        assertEquals(2f, metrics.density, 0.01f);
        assertEquals(1920, metrics.widthPixels);
        assertEquals(1080, metrics.heightPixels);
        float density = metrics.density;
        int[] production = productionDockMetricsPx(activity, capacity);
        int screenWidthPx = production[0];
        int screenHeightPx = production[1];
        int dockHeightPx = production[2];
        int dockBottomMarginPx = screenHeightPx - dockTopPx - dockHeightPx;

        FrameLayout root = new FrameLayout(activity);
        View settings = new View(activity);
        FrameLayout.LayoutParams settingsLp = new FrameLayout.LayoutParams(
                Math.round(320f * density), Math.round(30f * density),
                Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        settingsLp.topMargin = Math.round(29f * density);
        root.addView(settings, settingsLp);

        View dock = new View(activity);
        FrameLayout.LayoutParams dockLp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, dockHeightPx,
                Gravity.BOTTOM | Gravity.START);
        dockLp.bottomMargin = dockBottomMarginPx;
        root.addView(dock, dockLp);

        activity.setContentView(root);
        root.measure(View.MeasureSpec.makeMeasureSpec(screenWidthPx, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(screenHeightPx, View.MeasureSpec.EXACTLY));
        root.layout(0, 0, screenWidthPx, screenHeightPx);
        assertEquals(screenWidthPx, root.getWidth());
        assertEquals(screenHeightPx, root.getHeight());
        assertEquals(Math.round(29f * density), settings.getTop());
        assertEquals(Math.round(30f * density), settings.getHeight());
        assertEquals(dockTopPx, dock.getTop());
        assertEquals(dockHeightPx, dock.getHeight());
        assertTrue("Dock fixture must be visible to QuickSettingsDialog", dock.isShown());
        int[] rootAt = new int[2], settingsAt = new int[2], dockAt = new int[2];
        root.getLocationOnScreen(rootAt);
        settings.getLocationOnScreen(settingsAt);
        dock.getLocationOnScreen(dockAt);
        assertEquals(Math.round(29f * density), settingsAt[1] - rootAt[1]);
        assertEquals(dockTopPx, dockAt[1] - rootAt[1]);
        return new FrameLayout[]{root, root, root};
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
        FrameLayout[] views = host(activity);
        AlertDialog dialog = QuickSettingsDialog.show(activity, views[0], views[0].getChildAt(0),
                views[0].getChildAt(1), new RecordingActions(), QuickSettingsFocusGraph.INPUT, null);
        assertNotNull(dialog);
        int[] hostAt = new int[2], anchorAt = new int[2], dockAt = new int[2];
        views[0].getLocationOnScreen(hostAt);
        views[0].getChildAt(0).getLocationOnScreen(anchorAt);
        views[0].getChildAt(1).getLocationOnScreen(dockAt);
        float density = activity.getResources().getDisplayMetrics().density;
        int fiveDp = Math.round(5f * density);
        int panelTop = QuickSettingsDialog.panelTopInHost(anchorAt[1],
                views[0].getChildAt(0).getHeight(), hostAt[1], fiveDp);
        int dockTop = dockAt[1] - hostAt[1];
        int availableHeight = QuickSettingsDialog.availablePanelHeight(dockTop, panelTop, fiveDp);
        View content = find(dialog, "quick_settings_root");
        assertNotNull(content);
        assertEquals(Math.round(340.5f * density), availableHeight);
        assertEquals(Math.round(330f * density), content.getMeasuredHeight());
        // Production Dock geometry leaves 340.5dp available; the fixed 330dp panel
        // preserves the 5dp minimum clearance and leaves the remaining space untouched.
        assertEquals(Math.round(15.5f * density), dockTop - (panelTop + content.getMeasuredHeight()));
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
    public void popupOpensForEverySupportedDockCapacityWithoutCompressingCapsules() {
        for (int capacity : new int[]{4, 5, 6}) {
            Activity activity = newActivity();
            FrameLayout[] views = host(activity, capacity);
            View anchor = views[0].getChildAt(0);
            View dock = views[0].getChildAt(1);

            AlertDialog dialog = QuickSettingsDialog.show(activity, views[0], anchor, dock,
                    new RecordingActions(), QuickSettingsFocusGraph.INPUT, null);

            assertNotNull("Quick Settings must open with Dock capacity " + capacity, dialog);
            assertTrue("Quick Settings must be visible with Dock capacity " + capacity,
                    dialog.isShowing());
            View content = find(dialog, "quick_settings_root");
            assertNotNull(content);
            float density = activity.getResources().getDisplayMetrics().density;
            int[] hostAt = new int[2], anchorAt = new int[2], dockAt = new int[2];
            views[0].getLocationOnScreen(hostAt);
            anchor.getLocationOnScreen(anchorAt);
            dock.getLocationOnScreen(dockAt);
            int top = QuickSettingsDialog.panelTopInHost(anchorAt[1], anchor.getHeight(),
                    hostAt[1], Math.round(5f * density));
            int available = QuickSettingsDialog.availablePanelHeight(
                    dockAt[1] - hostAt[1], top, Math.round(5f * density));
            assertEquals("Panel may use only the unused trailing space when needed",
                    Math.min(Math.round(330f * density), available), content.getMeasuredHeight());
            assertTrue("All action content must remain inside the measured panel",
                    content.getMeasuredHeight() >= Math.round(320f * density));
            dialog.dismiss();
            activity.finish();
        }
    }

    @Test
    public void reducedTrailingClearanceKeepsPopupContentAndCapsulesAtApprovedSize() {
        Activity activity = newActivity();
        FrameLayout[] views = hostAtPx(activity, minimumDockTopForFixedPanelPx(activity) - 1);
        AlertDialog dialog = QuickSettingsDialog.show(activity, views[0], views[0].getChildAt(0),
                views[0].getChildAt(1), new RecordingActions(), QuickSettingsFocusGraph.INPUT, null);
        assertNotNull("The dialog should use available trailing space instead of rejecting the request", dialog);
        assertTrue(dialog.isShowing());
        View content = find(dialog, "quick_settings_root");
        assertNotNull(content);
        float density = activity.getResources().getDisplayMetrics().density;
        assertEquals(Math.round(329.5f * density), content.getMeasuredHeight());
        assertEquals(Math.round(36f * density), find(dialog, "quick_settings_input").getMeasuredHeight());
        dialog.dismiss();
    }

    @Test
    @Config(sdk = 30, qualifiers = "w960dp-h540dp-xhdpi")
    public void quickSettingsDialogIsActuallyShownOnAndroid11Api30() {
        Activity activity = newActivity();
        FrameLayout[] views = host(activity);
        AlertDialog dialog = QuickSettingsDialog.show(activity, views[0], views[0].getChildAt(0),
                views[0].getChildAt(1), new RecordingActions(), QuickSettingsFocusGraph.INPUT, null);
        assertNotNull("Android 11 must create the Quick Settings dialog", dialog);
        assertTrue("Android 11 must display the Quick Settings dialog", dialog.isShowing());
        assertNotNull(find(dialog, "quick_settings_root"));
        assertEquals(Math.round(330f * activity.getResources().getDisplayMetrics().density),
                find(dialog, "quick_settings_root").getMeasuredHeight());
        dialog.dismiss();
    }

    @Test
    public void verticalDockUsesBottomSafeEdgeInsteadOfItsFullHeightTopEdge() {
        Activity activity = newActivity();
        FrameLayout[] views = hostWithVerticalDock(activity);
        View anchor = views[0].getChildAt(0);
        View verticalDock = views[0].getChildAt(1);

        AlertDialog dialog = QuickSettingsDialog.show(activity, views[0], anchor, verticalDock,
                new RecordingActions(), QuickSettingsFocusGraph.INPUT, null);

        assertNotNull("Vertical Dock must not be mistaken for a bottom Dock boundary", dialog);
        assertTrue("Quick Settings dialog must actually be shown", dialog.isShowing());
        float density = activity.getResources().getDisplayMetrics().density;
        int safeBottom = DockGeometry.safeBottomInsetPx(density);
        int panelTop = dialog.getWindow().getAttributes().y;
        int panelHeight = find(dialog, "quick_settings_root").getMeasuredHeight();
        assertTrue("Fixed panel must remain above the Home bottom safe edge",
                panelTop + panelHeight <= views[0].getHeight() - safeBottom);
        dialog.dismiss();
    }

    @Test
    public void fixedCapsuleGeometryIsPreserved() {
        Activity activity = newActivity();
        FrameLayout[] views = hostAtDp(activity, 420);
        AlertDialog dialog = QuickSettingsDialog.show(activity, views[0], views[0].getChildAt(0),
                views[0].getChildAt(1), new RecordingActions(), QuickSettingsFocusGraph.INPUT, null);
        assertNotNull(dialog);
        assertEquals(Math.round(330f * activity.getResources().getDisplayMetrics().density),
                find(dialog, "quick_settings_root").getMeasuredHeight());
        assertEquals(Math.round(245f * activity.getResources().getDisplayMetrics().density),
                find(dialog, "quick_settings_root").getMeasuredWidth());
        float density = activity.getResources().getDisplayMetrics().density;
        int halfWidth = Math.round(110f * density);
        int wideWidth = Math.round(225f * density);
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
        FrameLayout[] views = hostAtDp(activity, 420);
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
}
