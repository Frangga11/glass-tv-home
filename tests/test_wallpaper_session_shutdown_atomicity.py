"""Guard that wallpaper mutation endpoints are atomic with QR-session shutdown."""
from pathlib import Path
import re
import unittest


ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome" / "WallpaperUploadServer.java"


class WallpaperSessionShutdownAtomicityTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.source = SOURCE.read_text(encoding="utf-8")

    def test_server_stop_and_mutation_endpoints_share_one_monitor(self):
        self.assertIn("public synchronized void stop()", self.source)
        for method in (
            "handleApiSelect(String fileName, OutputStream out)",
            "handleApiDelete(String fileName, OutputStream out)",
            "handleApiSlideshow(OutputStream out)",
            "handleApiClear(OutputStream out)",
        ):
            self.assertIn("private synchronized void " + method, self.source, method)

    def test_every_mutation_endpoint_rechecks_session_inside_monitor(self):
        for method in (
            "handleApiSelect(String fileName, OutputStream out)",
            "handleApiDelete(String fileName, OutputStream out)",
            "handleApiSlideshow(OutputStream out)",
            "handleApiClear(OutputStream out)",
        ):
            signature = "private synchronized void " + method + " {"
            section = self.source.split(signature, 1)[1].split("\n    }", 1)[0]
            self.assertRegex(
                section,
                re.compile(r"try\s*\{\s*if\s*\(!requireActiveSession\(out\)\)\s*return;"),
                method,
            )

    def test_session_guard_returns_explicit_gone_response(self):
        helper = self.source.split("private boolean requireActiveSession(OutputStream out)", 1)[1]
        helper = helper.split("\n    }", 1)[0]
        self.assertIn("if (isSessionActive()) return true;", helper)
        self.assertIn("sendSimpleJsonResponse(out, 410", helper)
        self.assertIn("session_expired", helper)
        self.assertIn('status == 410 ? "Gone"', self.source)

    def test_upload_commit_and_shutdown_remain_serialized(self):
        self.assertIn("synchronized (this)", self.source)
        self.assertIn("if (!isSessionActive())", self.source)


    def test_custom_wallpaper_file_commit_and_callback_are_inside_stop_monitor(self):
        route = self.source.split(
            '} else if (isRequestToPath(requestLine, "POST", getWallpaperUploadPath())) {',
            1,
        )[1].split("\n            } else {", 1)[0]
        start = route.index("synchronized (this) {")
        end = route.index("\n                        }\n                        String json", start)
        critical_section = route[start:end]
        self.assertIn("if (!isSessionActive())", critical_section)
        self.assertIn("targetFile.renameTo(storedFile)", critical_section)
        self.assertIn("listener.onUploaded(storedFile)", critical_section)

    def test_standby_photo_persistence_commit_is_inside_stop_monitor(self):
        method = self.source.split(
            "private void handleStandbyUpload(Socket client, InputStream in, OutputStream out, String[] lines)",
            1,
        )[1].split("\n    static int standbyUploadResponseStatus", 1)[0]
        start = method.index("synchronized (this) {")
        end = method.index("\n        }\n\n        String json", start)
        critical_section = method[start:end]
        self.assertIn("if (!isSessionActive() || client.isClosed())", critical_section)
        self.assertIn("StandbyPhotoStore.addUploadedFile(context, storedFile)", critical_section)
        self.assertIn("listener.onStandbyUploaded(storedFile)", critical_section)
        self.assertIn("storedFile.delete()", critical_section)


if __name__ == "__main__":
    unittest.main()
