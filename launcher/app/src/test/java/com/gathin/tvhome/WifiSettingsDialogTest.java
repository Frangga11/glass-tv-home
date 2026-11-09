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
        assertEquals(0, networkList.getLayoutParams().height);
        assertEquals(1f, ((android.widget.LinearLayout.LayoutParams) networkList.getLayoutParams()).weight, 0.001f);
        View root = window.getDecorView().findViewWithTag("wifi_popup_root");
        View header = window.getDecorView().findViewWithTag("wifi_popup_header");
        View footer = window.getDecorView().findViewWithTag("wifi_popup_footer");
        assertNotNull(root);
        assertNotNull(header);
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
        assertEquals(0.225f, WifiSettingsDialog.popupWidthFraction(), 0.0001f);
        assertEquals(0.69f, WifiSettingsDialog.popupHeightFraction(), 0.0001f);
        assertEquals(25f, WifiSettingsDialog.networkTextSizePx(), 0.001f);
        assertEquals(22, WifiSettingsDialog.networkStatusIconSizeDp());
        assertTrue(WifiSettingsDialog.networkStatusIconColor() != 0);
        assertEquals(17f, WifiSettingsDialog.popupTitleSizeSp(), 0.001f);
        assertEquals(13f, WifiSettingsDialog.popupSectionSizeSp(), 0.001f);
        assertEquals(11f, WifiSettingsDialog.popupMetaSizeSp(), 0.001f);
        assertEquals(13f, WifiSettingsDialog.popupFooterSizeSp(), 0.001f);
        assertEquals(4, WifiSettingsDialog.visibleNetworkRowCount());
        assertEquals(35, WifiSettingsDialog.networkRowHeightDp());
        assertEquals(35, WifiSettingsDialog.popupHeightReductionDp());
        assertEquals(4, WifiSettingsDialog.networkRowGapDp());
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
        assertTrue(permission.getNextFocusDownId() != 0);
        assertTrue(settings.getNextFocusDownId() != 0);
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
