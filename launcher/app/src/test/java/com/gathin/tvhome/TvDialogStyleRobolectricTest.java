package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30)
public class TvDialogStyleRobolectricTest {
    public static class TestActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setTheme(android.R.style.Theme_Material);
            super.onCreate(savedInstanceState);
        }
    }

    @Test
    public void sharedModalContractBoundsAndCentersDialogWindow() {
        Activity activity = Robolectric.buildActivity(TestActivity.class).setup().get();
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("Contract test")
                .setMessage("Host-side window contract")
                .create();

        dialog.show();
        TvDialogStyle.apply(dialog, activity, 520, 0);

        Window window = dialog.getWindow();
        assertNotNull(window);
        WindowManager.LayoutParams attrs = window.getAttributes();
        float density = activity.getResources().getDisplayMetrics().density;
        int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels;

        assertEquals(Gravity.CENTER, attrs.gravity);
        assertTrue((attrs.flags & WindowManager.LayoutParams.FLAG_DIM_BEHIND) != 0);
        assertEquals(0.68f, attrs.dimAmount, 0.001f);
        assertEquals(TvDialogStyle.resolveWidthPx(width, density, 520), attrs.width);
        assertEquals(ViewGroup.LayoutParams.WRAP_CONTENT, attrs.height);
        assertNotNull(window.getDecorView().getBackground());

        dialog.dismiss();
    }

    @Test
    public void customModalContractClampsDimAndKeepsViewportBounds() {
        Activity activity = Robolectric.buildActivity(TestActivity.class).setup().get();
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("Custom contract test")
                .create();

        dialog.show();
        TvDialogStyle.applyCustom(dialog, activity, 580, 460, 1.5f);

        Window window = dialog.getWindow();
        assertNotNull(window);
        WindowManager.LayoutParams attrs = window.getAttributes();
        float density = activity.getResources().getDisplayMetrics().density;
        int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels;

        assertEquals(Gravity.CENTER, attrs.gravity);
        assertEquals(1.0f, attrs.dimAmount, 0.001f);
        assertEquals(TvDialogStyle.resolveWidthPx(width, density, 580), attrs.width);
        assertEquals(TvDialogStyle.resolveHeightPx(height, density, 460), attrs.height);

        dialog.dismiss();
    }
}
