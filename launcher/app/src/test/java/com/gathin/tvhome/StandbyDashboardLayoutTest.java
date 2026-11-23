package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30)
public class StandbyDashboardLayoutTest {
    public static class TestActivity extends Activity {
        @Override protected void onCreate(Bundle savedInstanceState) {
            setTheme(android.R.style.Theme_Material);
            super.onCreate(savedInstanceState);
        }
    }

    @Test
    public void standbyDashboardHasWallpaperAndCenteredMetricSections() {
        Activity activity = Robolectric.buildActivity(TestActivity.class).setup().get();
        View root = LayoutInflater.from(activity).inflate(R.layout.view_nightstand, null, false);

        assertNotNull(root.findViewById(R.id.nightstandWallpaper));
        assertNotNull(root.findViewById(R.id.nightstandPhoto));
        assertNotNull(root.findViewById(R.id.nightstandPhotoScrim));
        assertNotNull(root.findViewById(R.id.nightstandContent));
        assertNotNull(root.findViewById(R.id.nightstandPrayer));
        assertNotNull(root.findViewById(R.id.nightstandHijri));
        assertNotNull(root.findViewById(R.id.nightstandClock));
        assertNotNull(root.findViewById(R.id.nightstandDate));
        assertNotNull(root.findViewById(R.id.nightstandWeatherTemp));
        assertNotNull(root.findViewById(R.id.nightstandAqiValue));
        assertNotNull(root.findViewById(R.id.nightstandPrayerScheduleTitle));
        assertNotNull(root.findViewById(R.id.nightstandFajrTime));
        assertNotNull(root.findViewById(R.id.nightstandDhuhrTime));
        assertNotNull(root.findViewById(R.id.nightstandAsrTime));
        assertNotNull(root.findViewById(R.id.nightstandMaghribTime));
        assertNotNull(root.findViewById(R.id.nightstandIshaTime));

        LinearLayout panel = root.findViewById(R.id.nightstandContent);
        assertTrue((panel.getGravity() & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) panel.getLayoutParams();
        assertEquals(Gravity.START | Gravity.CENTER_VERTICAL, params.gravity);
        assertTrue(params.width > 0);
    }
}
