package com.gathin.tvhome;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30)
public class WeatherBackgroundViewPerformanceTest {
    private static Object field(Object target, String name) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    @Test
    public void repeatedFramesReuseRadialShadersInsteadOfReallocatingThem() throws Exception {
        Context context = RuntimeEnvironment.getApplication();
        WeatherBackgroundView view = new WeatherBackgroundView(context);
        view.setLowSpecMode(true);
        view.layout(0, 0, 1280, 720);

        Bitmap surface = Bitmap.createBitmap(1280, 720, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(surface);
        try {
            view.setCondition(WeatherBackgroundView.ConditionType.SUNNY);
            Object sunShader = field(view, "sunRadial");
            assertNotNull(sunShader);
            view.draw(canvas);
            view.draw(canvas);
            assertSame("sun shader must be cached across frames", sunShader, field(view, "sunRadial"));

            view.setCondition(WeatherBackgroundView.ConditionType.NIGHT);
            Object moonShader = field(view, "moonRadial");
            assertNotNull(moonShader);
            view.draw(canvas);
            view.draw(canvas);
            assertSame("moon shader must be cached across frames", moonShader, field(view, "moonRadial"));

            view.setCondition(WeatherBackgroundView.ConditionType.CLOUDY);
            List<?> clouds = (List<?>) field(view, "cloudPuffs");
            assertTrue("cloud scene should contain cached puffs", !clouds.isEmpty());
            Object firstPuff = clouds.get(0);
            Object cloudShader = field(firstPuff, "gradient");
            assertNotNull(cloudShader);
            view.draw(canvas);
            view.draw(canvas);
            assertSame("cloud shader must be reused across frames", cloudShader, field(firstPuff, "gradient"));
        } finally {
            surface.recycle();
        }
    }

    @Test
    public void allWeatherModesRenderInReducedMotionAndLowSpecMode() {
        WeatherBackgroundView view = new WeatherBackgroundView(RuntimeEnvironment.getApplication());
        view.setLowSpecMode(true);
        view.setReducedMotion(true);
        view.layout(0, 0, 1280, 720);
        Bitmap surface = Bitmap.createBitmap(1280, 720, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(surface);
        try {
            for (WeatherBackgroundView.ConditionType condition : WeatherBackgroundView.ConditionType.values()) {
                view.setCondition(condition);
                view.draw(canvas);
            }
        } finally {
            surface.recycle();
        }
    }
}
