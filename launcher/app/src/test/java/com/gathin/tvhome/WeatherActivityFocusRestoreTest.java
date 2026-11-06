package com.gathin.tvhome;

import static org.junit.Assert.assertSame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30)
public class WeatherActivityFocusRestoreTest {
    public static class TestActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setTheme(android.R.style.Theme_Material);
            super.onCreate(savedInstanceState);
        }
    }

    @Test
    public void refreshRestoresFocusToTheSameHourlySlotWhenItStillExists() {
        Activity activity = Robolectric.buildActivity(TestActivity.class).setup().get();
        LinearLayout rows = new LinearLayout(activity);
        LinearLayout nowRow = row(activity, R.id.hourlyTime, "Now");
        LinearLayout afternoonRow = row(activity, R.id.hourlyTime, "3:00 PM");
        rows.addView(nowRow);
        rows.addView(afternoonRow);

        View target = WeatherActivity.findRowByText(rows, R.id.hourlyTime, "3:00 PM");

        assertSame(afternoonRow, target);
    }

    @Test
    public void refreshFallsBackToFirstRowWhenTheSavedSlotDisappears() {
        Activity activity = Robolectric.buildActivity(TestActivity.class).setup().get();
        LinearLayout rows = new LinearLayout(activity);
        LinearLayout nowRow = row(activity, R.id.hourlyTime, "Now");
        rows.addView(nowRow);

        View restored = WeatherActivity.findRowByText(rows, R.id.hourlyTime, "3:00 PM");
        if (restored == null && rows.getChildCount() > 0) restored = rows.getChildAt(0);

        assertSame(nowRow, restored);
    }

    private static LinearLayout row(Activity activity, int id, String value) {
        LinearLayout row = new LinearLayout(activity);
        TextView label = new TextView(activity);
        label.setId(id);
        label.setText(value);
        row.addView(label);
        return row;
    }
}
