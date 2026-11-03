"""F07 regression guards for bounded work, UI-thread safety and animation allocation."""
from pathlib import Path
import re
import unittest


ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome"


def method_body(source, signature):
    start = source.find(signature)
    if start < 0:
        raise AssertionError(f"Missing method: {signature}")
    opening = source.find("{", start)
    depth = 0
    for index in range(opening, len(source)):
        if source[index] == "{":
            depth += 1
        elif source[index] == "}":
            depth -= 1
            if depth == 0:
                return source[opening + 1:index]
    raise AssertionError(f"Unclosed method: {signature}")


class F07PerformanceStabilityContractTest(unittest.TestCase):
    def test_weather_animation_reuses_radial_shaders(self):
        source = (JAVA / "WeatherBackgroundView.java").read_text(encoding="utf-8")
        for signature in (
            "private void drawSunnyAtmosphere(",
            "private void drawNightSky(",
            "private void drawClouds(",
        ):
            body = method_body(source, signature)
            self.assertNotIn("new RadialGradient", body, signature)
        self.assertIn("private void rebuildAtmosphereShaders()", source)
        self.assertIn("p.gradient = new RadialGradient", source)
        self.assertIn("p.shaderMatrix.setTranslate(p.x, p.y)", source)

    def test_rain_ripples_reuse_a_bounded_object_pool(self):
        source = (JAVA / "WeatherBackgroundView.java").read_text(encoding="utf-8")
        body = method_body(source, "private void drawRainAndRipples(")
        self.assertIn("ripplePool.isEmpty() ? new SplashRipple()", body)
        self.assertIn("ripplePool.size() < 16", body)
        self.assertIn("ripplePool.size() < 16", body)

    def test_wallpaper_fallback_decode_never_runs_inline_on_home_thread(self):
        source = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        self.assertNotRegex(
            source,
            r"setWallpaper\(decodeWallpaper\(cfg\.wallpaper\),\s*false\)",
        )
        self.assertIn("queueWallpaperDecode(cfg.wallpaper, false, false, false)", source)
        helper = method_body(source, "private void queueWallpaperDecode(")
        self.assertIn("wallDecoder.execute", helper)
        self.assertIn("wallpaperRequestGeneration.incrementAndGet()", helper)
        self.assertIn("generation != wallpaperRequestGeneration.get()", helper)
        self.assertIn("readyBitmap.recycle()", helper)
        self.assertIn("readyBlur.recycle()", helper)

    def test_qr_wallpaper_preview_decode_is_off_the_ui_thread(self):
        source = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        upload_callback = method_body(source, "private void onCustomWallpaperUploaded(")
        self.assertIn("queueWallpaperThumbnail(file)", upload_callback)
        self.assertNotIn("decodeWallpaper(file)", upload_callback)
        preview = method_body(source, "private void queueWallpaperThumbnail(")
        self.assertIn("wallDecoder.execute", preview)
        self.assertIn("wallpaperThumbnailGeneration.incrementAndGet()", preview)
        self.assertIn("qrUploadDialog.isShowing()", preview)
        self.assertIn("thumb.recycle()", preview)

    def test_wallpaper_selection_uses_latest_request_worker(self):
        source = (JAVA / "MainActivity.java").read_text(encoding="utf-8")
        body = method_body(source, "private void showNextWallpaper(")
        self.assertIn("queueWallpaperDecode(file, fade, !firstPicture, true)", body)
        self.assertNotIn("wallDecoder.execute", body)
        destroy = method_body(source, "protected void onDestroy()")
        self.assertIn("wallpaperRequestGeneration.incrementAndGet()", destroy)
        self.assertIn("handler.removeCallbacksAndMessages(null)", destroy)
        self.assertIn("wallDecoder.shutdownNow()", destroy)

    def test_media_session_and_notification_queries_run_on_worker(self):
        source = (JAVA / "NowPlayingManager.java").read_text(encoding="utf-8")
        public_refresh = method_body(source, "public static void updateCurrentMedia(")
        self.assertIn("MEDIA_WORKER.execute", public_refresh)
        self.assertIn("refreshQueued.compareAndSet(false, true)", public_refresh)
        self.assertIn("refreshAgain", public_refresh)
        self.assertNotIn("getActiveSessions", public_refresh)
        self.assertNotIn("getActiveNotifications", public_refresh)
        worker = method_body(source, "private static synchronized void refreshCurrentMedia(")
        self.assertIn("getActiveSessions", worker)
        self.assertIn("getActiveNotifications", worker)

    def test_backdrop_blur_handles_memory_pressure_and_worker_shutdown(self):
        source = (JAVA / "Backdrop.java").read_text(encoding="utf-8")
        update = method_body(source, "static void update(")
        blur = method_body(source, "static Bitmap blur(")
        self.assertIn("OutOfMemoryError | RuntimeException", update)
        self.assertIn("RejectedExecutionException", update)
        self.assertIn("OutOfMemoryError | RuntimeException", blur)
        self.assertIn("tinting.clear()", method_body(source, "static void setWorker("))

    def test_standby_decoder_queue_is_bounded_and_background_priority(self):
        source = (JAVA / "StandbyPhotoStore.java").read_text(encoding="utf-8")
        self.assertIn("new java.util.concurrent.ArrayBlockingQueue<>(2)", source)
        self.assertIn("new java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy()", source)
        self.assertIn("THREAD_PRIORITY_BACKGROUND", source)

    def test_cache_cleanup_does_not_force_global_garbage_collection(self):
        source = (JAVA / "CacheCleaner.java").read_text(encoding="utf-8")
        self.assertNotIn("System.gc()", source)
        self.assertIn("deleteDirContents(app.getCacheDir())", source)


if __name__ == "__main__":
    unittest.main()
