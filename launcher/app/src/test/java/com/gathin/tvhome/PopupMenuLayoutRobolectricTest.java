package com.gathin.tvhome;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30)
public class PopupMenuLayoutRobolectricTest {
    public static class TestActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setTheme(android.R.style.Theme_Material);
            super.onCreate(savedInstanceState);
        }
    }

    @Test
    public void longMenuRowsRemainScrollableInsideViewportBoundedPanel() {
        Activity activity = Robolectric.buildActivity(TestActivity.class).setup().get();
        FrameLayout panel = (FrameLayout) LayoutInflater.from(activity)
                .inflate(R.layout.popup_inputs, null, false);
        LinearLayout rows = panel.findViewById(R.id.rows);
        assertNotNull(rows);
        assertTrue(rows.getParent() instanceof ScrollView);

        for (int i = 0; i < 10; i++) {
            TextView row = new TextView(activity);
            row.setText("Menu item " + i);
            row.setFocusable(true);
            rows.addView(row, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 64));
        }

        panel.measure(View.MeasureSpec.makeMeasureSpec(480, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        assertTrue(panel.getMeasuredHeight() > 240);

        ScrollView scroll = (ScrollView) rows.getParent();
        activity.setContentView(panel);
        panel.measure(View.MeasureSpec.makeMeasureSpec(480, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(240, View.MeasureSpec.EXACTLY));
        panel.layout(0, 0, 480, 240);
        assertTrue(scroll.getMeasuredHeight() < 10 * 64);
        assertTrue(scroll.canScrollVertically(1));
    }
}
