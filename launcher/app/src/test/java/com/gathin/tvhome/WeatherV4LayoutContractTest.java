package com.gathin.tvhome;

import static org.junit.Assert.assertNotNull;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30)
public class WeatherV4LayoutContractTest {
    public static class TestActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setTheme(android.R.style.Theme_Material);
            super.onCreate(savedInstanceState);
        }
    }

    @Test
    public void weatherDashboardContainsTheApprovedV4MetricSurfaces() {
        Activity activity = Robolectric.buildActivity(TestActivity.class).setup().get();
        View root = LayoutInflater.from(activity).inflate(R.layout.activity_weather, null, false);

        assertNotNull(root.findViewById(R.id.dewPointValText));
        assertNotNull(root.findViewById(R.id.pm25ValueText));
        assertNotNull(root.findViewById(R.id.pm10ValueText));
        assertNotNull(root.findViewById(R.id.coValueText));
        assertNotNull(root.findViewById(R.id.no2ValueText));
        assertNotNull(root.findViewById(R.id.so2ValueText));
        assertNotNull(root.findViewById(R.id.pollutantMissingNote));
        assertNotNull(root.findViewById(R.id.moonPhaseName));
        assertNotNull(root.findViewById(R.id.moonriseMoonsetText));
        assertNotNull(root.findViewById(R.id.uvAdviceText));
        assertNotNull(root.findViewById(R.id.weatherFooter));
    }
}
