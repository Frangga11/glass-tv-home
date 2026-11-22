package com.gathin.tvhome;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.Gravity;
import android.os.Looper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class WifiSettingsDialogTest {
    @Test public void popupUsesCompactTopRightGeometryAndLowBackdropDim() {
        Activity activity = Robolectric.buildActivity(Activity.class).setup().get();
        AlertDialog dialog = WifiSettingsDialog.show(activity, () -> { }, () -> { });
        assertNotNull(dialog);
        android.view.Window window = dialog.getWindow();
        assertNotNull(window);
        android.view.WindowManager.LayoutParams params = window.getAttributes();
        assertEquals(Gravity.TOP | Gravity.RIGHT, WifiSettingsDialog.anchoredWindowGravity());
        assertTrue(WifiSettingsDialog.popupDimAmount() <= 0.10f);
        View networkList = window.getDecorView().findViewWithTag("wifi_network_list");
        assertNotNull(networkList);
        assertTrue(networkList instanceof android.widget.ScrollView);
        assertTrue(((android.widget.ScrollView) networkList).isVerticalScrollBarEnabled());
        assertEquals(View.SCROLLBARS_INSIDE_INSET, ((android.widget.ScrollView) networkList).getScrollBarStyle());
        // INSIDE_INSET reserves scrollbar width in addition to the explicit 4dp content gutter.
        assertTrue(networkList.getPaddingRight() >= Math.round(4 * activity.getResources().getDisplayMetrics().density));
        assertEquals(Math.round(WifiSettingsDialog.networkViewportHeightDp()
                        * activity.getResources().getDisplayMetrics().density),
                networkList.getLayoutParams().height);
        assertEquals(0f, ((android.widget.LinearLayout.LayoutParams) networkList.getLayoutParams()).weight, 0.001f);
        View root = window.getDecorView().findViewWithTag("wifi_popup_root");
        View header = window.getDecorView().findViewWithTag("wifi_popup_header");
        View footer = window.getDecorView().findViewWithTag("wifi_popup_footer");
        assertNotNull(root);
        assertNotNull(header);
        assertEquals(Math.round(12 * activity.getResources().getDisplayMetrics().density), root.getPaddingLeft());
        assertEquals(Math.round(12 * activity.getResources().getDisplayMetrics().density), root.getPaddingRight());
        assertEquals(Math.round(5 * activity.getResources().getDisplayMetrics().density), root.getPaddingTop());
        assertEquals(Math.round(10 * activity.getResources().getDisplayMetrics().density), root.getPaddingBottom());
        assertEquals(Math.round(28 * activity.getResources().getDisplayMetrics().density), header.getLayoutParams().height);
        assertNotNull(footer);
        assertEquals(android.widget.LinearLayout.VERTICAL, ((android.widget.LinearLayout) footer).getOrientation());
        View rescan = window.getDecorView().findViewWithTag("wifi_rescan");
        View settings = window.getDecorView().findViewWithTag("wifi_open_settings");
        assertNotNull(rescan);
        assertNotNull(settings);
        assertEquals(rescan.getLayoutParams().height, settings.getLayoutParams().height);
        assertEquals(android.view.ViewGroup.LayoutParams.MATCH_PARENT, rescan.getLayoutParams().width);
        assertEquals(android.view.ViewGroup.LayoutParams.MATCH_PARENT, settings.getLayoutParams().width);
        dialog.dismiss();
        Shadows.shadowOf(Looper.getMainLooper()).idle();
    }

    @Test public void popupUsesConstrainedWidthAndVerticalFirstLayout() {
        assertEquals(180, WifiSettingsDialog.popupWidthDp());
        assertEquals(12f, WifiSettingsDialog.networkTextSizeSp(), 0.001f);
        assertEquals(16, WifiSettingsDialog.networkStatusIconSizeDp());
        assertTrue(WifiSettingsDialog.networkStatusIconColor() != 0);
        assertEquals(14f, WifiSettingsDialog.popupTitleSizeSp(), 0.001f);
        assertEquals(12f, WifiSettingsDialog.popupSectionSizeSp(), 0.001f);
        assertEquals(10f, WifiSettingsDialog.popupMetaSizeSp(), 0.001f);
        assertEquals(12f, WifiSettingsDialog.popupFooterSizeSp(), 0.001f);
        assertEquals(12f, WifiSettingsDialog.permissionTextSizeSp(), 0.001f);
        assertEquals(2, WifiSettingsDialog.visibleNetworkRowCount());
        assertEquals(26, WifiSettingsDialog.networkRowHeightDp());
        assertEquals(4, WifiSettingsDialog.networkRowGapDp());
        assertEquals(56, WifiSettingsDialog.networkViewportHeightDp());
        Activity activity = Robolectric.buildActivity(Activity.class).setup().get();
        AlertDialog dialog = WifiSettingsDialog.show(activity, () -> { }, () -> { });
        View toggle = dialog.getWindow().getDecorView().findViewWithTag("wifi_toggle");
        assertNotNull(toggle);
        assertEquals(14f, ((android.widget.TextView) toggle).getTextSize()
                / activity.getResources().getDisplayMetrics().scaledDensity, 0.1f);
        assertEquals(Math.round(42 * activity.getResources().getDisplayMetrics().density),
                toggle.getLayoutParams().width);
        assertEquals(Math.round(21 * activity.getResources().getDisplayMetrics().density),
                toggle.getLayoutParams().height);
        View permission = dialog.getWindow().getDecorView().findViewWithTag("wifi_permission");
        assertNotNull(permission);
        assertEquals(Math.round(26 * activity.getResources().getDisplayMetrics().density),
                permission.getLayoutParams().height);
        View settings = dialog.getWindow().getDecorView().findViewWithTag("wifi_open_settings");
        assertNotNull(settings);
        assertEquals(permission.getParent(), settings.getParent());
        assertEquals(Math.round(4 * activity.getResources().getDisplayMetrics().density),
                ((android.widget.LinearLayout.LayoutParams) permission.getLayoutParams()).bottomMargin);
        dialog.dismiss();
        Shadows.shadowOf(Looper.getMainLooper()).idle();
    }

    @Test public void dialogProvidesPermissionGuidanceAndDpadControls() {
        Activity activity = Robolectric.buildActivity(Activity.class).setup().get();
        final int[] returns = {0};
        AlertDialog dialog = WifiSettingsDialog.show(activity, () -> { }, () -> returns[0]++);
        assertNotNull(dialog);
        assertFalse(activity.isFinishing());
        assertFalse(activity.isDestroyed());
        View permission = dialog.getWindow().getDecorView().findViewWithTag("wifi_permission");
        View rescan = dialog.getWindow().getDecorView().findViewWithTag("wifi_rescan");
        View settings = dialog.getWindow().getDecorView().findViewWithTag("wifi_open_settings");
        View close = dialog.getWindow().getDecorView().findViewWithTag("wifi_close");
        assertNotNull(permission);
        assertNotNull(rescan);
        assertNotNull(settings);
        assertNotNull(close);
        assertTrue(rescan.isFocusable());
        assertTrue(settings.isClickable());
        assertNotEquals(View.NO_ID, rescan.getId());
        assertNotEquals(View.NO_ID, settings.getId());
        assertEquals(settings.getId(), permission.getNextFocusDownId());
        assertTrue(settings.getNextFocusDownId() != 0);
        // Permission state hides rescan; no DPAD edge may target that hidden control.
        assertEquals(settings.getId(), settings.getNextFocusLeftId());
        assertEquals(settings.getId(), settings.getNextFocusRightId());
        assertEquals(settings.getId(), settings.getNextFocusUpId());
        assertEquals(settings.getId(), settings.getNextFocusDownId());
        dialog.cancel();
        Shadows.shadowOf(Looper.getMainLooper()).idle();
        assertFalse(dialog.isShowing());
        assertTrue(returns[0] == 1);
    }
    @Test public void androidSettingsHandoffRestoresQuickSettingsAfterHostResume() {
        Activity activity = Robolectric.buildActivity(Activity.class).setup().get();
        final int[] launched = {0};
        final int[] returned = {0};
        AlertDialog dialog = WifiSettingsDialog.show(activity,
                () -> launched[0]++, () -> returned[0]++);
        assertNotNull(dialog);
        View settings = dialog.getWindow().getDecorView().findViewWithTag("wifi_open_settings");
        assertNotNull(settings);
        assertTrue(settings.performClick());
        Shadows.shadowOf(Looper.getMainLooper()).idle();
        assertFalse(dialog.isShowing());
        assertEquals(1, launched[0]);
        assertEquals(0, returned[0]);
        WifiSettingsDialog.onHostResumed();
        assertEquals(1, returned[0]);
    }

    @Test public void unavailableAndroidSettingsRestoresParentWithoutWaitingForResume() {
        Activity activity = Robolectric.buildActivity(Activity.class).setup().get();
        final int[] returned = {0};
        AlertDialog dialog = WifiSettingsDialog.show(activity,
                WifiSettingsDialog::onExternalSettingsUnavailable, () -> returned[0]++);
        assertNotNull(dialog);
        View settings = dialog.getWindow().getDecorView().findViewWithTag("wifi_open_settings");
        assertNotNull(settings);
        assertTrue(settings.performClick());
        Shadows.shadowOf(Looper.getMainLooper()).idle();
        assertFalse(dialog.isShowing());
        assertEquals(1, returned[0]);
    }

}
