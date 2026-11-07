package com.gathin.tvhome;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class WifiSettingsDialogTest {
    @Test public void dialogProvidesPermissionGuidanceAndDpadControls() {
        Activity activity = Robolectric.buildActivity(Activity.class).setup().get();
        final int[] returns = {0};
        AlertDialog dialog = WifiSettingsDialog.show(activity, () -> { }, () -> returns[0]++);
        assertNotNull(dialog);
        View rescan = dialog.getWindow().getDecorView().findViewWithTag("wifi_rescan");
        View settings = dialog.getWindow().getDecorView().findViewWithTag("wifi_open_settings");
        View close = dialog.getWindow().getDecorView().findViewWithTag("wifi_close");
        assertNotNull(rescan);
        assertNotNull(settings);
        assertNotNull(close);
        assertTrue(rescan.isFocusable());
        assertTrue(settings.isClickable());
        assertTrue(rescan.getNextFocusDownId() != 0);
        dialog.cancel();
        assertTrue(returns[0] == 1);
    }
}
