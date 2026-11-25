package com.gathin.tvhome;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.RobolectricTestRunner;

/**
 * Exercises the Quick Settings surface in isolation.
 *
 * Do not launch MainActivity here: its onCreate intentionally starts the Home's weather,
 * prayer, media and wallpaper lifecycle. Those are integration/runtime concerns and make a
 * popup-layout unit test allocate unrelated network workers, decoders and polling callbacks.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30, qualifiers = "w960dp-h540dp-xhdpi")
public class MainActivityQuickSettingsTest {
    public static class QuickSettingsHostActivity extends Activity { }

    @Test
    public void quickSettingsDialogShowsAndReleasesItsViewTreeOnAndroid11() {
        ActivityController<QuickSettingsHostActivity> controller =
                Robolectric.buildActivity(QuickSettingsHostActivity.class);
        AlertDialog dialog = null;
        try {
            QuickSettingsHostActivity activity = controller.setup().get();
            FrameLayout host = new FrameLayout(activity);
            View settingsAnchor = new View(activity);
            settingsAnchor.setFocusable(true);
            FrameLayout.LayoutParams anchorLp = new FrameLayout.LayoutParams(80, 80,
                    Gravity.TOP | Gravity.RIGHT);
            anchorLp.topMargin = 50;
            anchorLp.rightMargin = 60;
            host.addView(settingsAnchor, anchorLp);

            View dock = new View(activity);
            FrameLayout.LayoutParams dockLp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 100, Gravity.BOTTOM);
            host.addView(dock, dockLp);
            activity.setContentView(host);

            int width = 1920;
            int height = 1080;
            host.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
            host.layout(0, 0, width, height);

            QuickSettingsDialog.Actions actions = new QuickSettingsDialog.Actions() {
                @Override public String languageSummary() { return "Indonesia"; }
                @Override public String powerTitle() { return "Power"; }
                @Override public String cacheTitle() { return "Clean cache"; }
                @Override public String wallpaperIntervalSummary() { return "5 minutes"; }
                @Override public void openInputs() { }
                @Override public void restart() { }
                @Override public void chooseLanguage() { }
                @Override public void openPowerMenu() { }
                @Override public void cleanCache() { }
                @Override public void nextPhoto() { }
                @Override public void chooseWallpaperInterval() { }
                @Override public void manageWallpaper() { }
                @Override public void openGlassSettings() { }
                @Override public void openAndroidSettings() { }
            };

            final boolean[] dismissed = {false};
            dialog = QuickSettingsDialog.show(activity, host, settingsAnchor, dock, actions,
                    QuickSettingsFocusGraph.INPUT, () -> dismissed[0] = true);

            assertNotNull("Quick Settings must create a dialog for a valid 1080p host", dialog);
            assertTrue("Quick Settings dialog must be visible", dialog.isShowing());
            assertNotNull("Quick Settings content must be attached to the dialog window",
                    dialog.getWindow().getDecorView().findViewWithTag("quick_settings_root"));
            assertNotNull("Input action must exist in the dialog focus graph",
                    dialog.getWindow().getDecorView().findViewWithTag("quick_settings_input"));

            dialog.dismiss();
            dialog = null;
            Shadows.shadowOf(Looper.getMainLooper()).idle();
            assertTrue("Dismiss callback must release the dialog-owned focus surface", dismissed[0]);
        } finally {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            controller.pause().stop().destroy();
            Shadows.shadowOf(Looper.getMainLooper()).idle();
        }
    }
}
