package com.gathin.tvhome;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.AlertDialog;
import android.os.Looper;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlertDialog;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30, qualifiers = "w960dp-h540dp-xhdpi")
public class MainActivityQuickSettingsTest {
    @Test
    public void settingsButtonClickOpensVisibleQuickSettingsDialogOnAndroid11() {
        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class);
        AlertDialog dialog = null;
        try {
            MainActivity activity = controller.setup().get();
            View settings = activity.findViewById(R.id.btnSettings);

            assertNotNull("Settings navigation button must exist", settings);
            assertTrue("Settings click listener must be invoked", settings.performClick());
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            dialog = ShadowAlertDialog.getLatestAlertDialog();
            assertNotNull("Settings click must create a Quick Settings AlertDialog", dialog);
            assertTrue("Quick Settings dialog must remain visible after the click", dialog.isShowing());
            assertNotNull("Quick Settings content must be attached to the dialog window",
                    dialog.getWindow().getDecorView().findViewWithTag("quick_settings_root"));
        } finally {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            controller.pause().stop().destroy();
            Shadows.shadowOf(Looper.getMainLooper()).idle();
        }
    }
}
