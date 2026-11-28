package com.gathin.tvhome;

/** Pure interpolation math for the Classic vertical ScrollView's focus-glider handoff. */
final class ClassicGliderBlend {
    private ClassicGliderBlend() { }

    static float progress(int currentScrollY, int startScrollY, int targetScrollY) {
        if (startScrollY == targetScrollY) return 1f;
        float value = (currentScrollY - startScrollY)
                / (float) (targetScrollY - startScrollY);
        return Math.max(0f, Math.min(1f, value));
    }

    static float interpolate(float from, float to, float progress) {
        float p = Math.max(0f, Math.min(1f, progress));
        return from + (to - from) * p;
    }
}
