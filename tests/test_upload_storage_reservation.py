"""Regression guards for bounded storage reservations on QR upload endpoints."""
from pathlib import Path
import unittest


ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "launcher" / "app" / "src" / "main" / "java" / "com" / "gathin" / "tvhome"
POLICY = JAVA / "NetworkFeedbackPolicy.java"
SERVER = JAVA / "WallpaperUploadServer.java"


class UploadStorageReservationTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.policy = POLICY.read_text(encoding="utf-8")
        cls.server = SERVER.read_text(encoding="utf-8")

    def test_policy_uses_overflow_safe_available_space_arithmetic(self):
        self.assertIn("canReserveUploadStorage(long usableBytes, long reservedBytes", self.policy)
        self.assertIn("long availableAfterReservations = usableBytes - reservedBytes;", self.policy)
        self.assertIn("incomingBytes <= availableAfterReservations - minimumFreeBytes", self.policy)

    def test_server_reserves_against_shared_minimum_free_space(self):
        self.assertIn("MIN_FREE_UPLOAD_BYTES = 64L * 1024L * 1024L", self.server)
        self.assertIn("private static final Object UPLOAD_STORAGE_LOCK", self.server)
        self.assertIn("private static long reservedUploadBytes;", self.server)
        self.assertIn("synchronized (UPLOAD_STORAGE_LOCK)", self.server)
        self.assertIn("private boolean reserveUploadStorage(long incomingBytes)", self.server)
        self.assertIn("private void releaseUploadStorage(long reservedBytes)", self.server)

    def test_custom_wallpaper_upload_reserves_and_releases_bytes(self):
        route = self.server.split(
            'isRequestToPath(requestLine, "POST", getWallpaperUploadPath())',
            1,
        )[1].split("\n            } else {", 1)[0]
        self.assertIn("reserveUploadStorage(contentLength)", route)
        self.assertIn("reservedStorageBytes = contentLength", route)
        handler = self.server.split("private void handleClient(Socket client)", 1)[1]
        self.assertIn("releaseUploadStorage(reservedStorageBytes)", handler)

    def test_standby_upload_reservation_is_released_in_finally(self):
        route = self.server.split(
            'isRequestToPath(requestLine, "POST", getStandbyUploadPath())',
            1,
        )[1].split("\n            } else if", 1)[0]
        self.assertIn("reserveUploadStorage(incomingBytes)", route)
        self.assertIn("finally", route)
        self.assertIn("releaseUploadStorage(incomingBytes)", route)

    def test_insufficient_storage_uses_http_507(self):
        self.assertIn('sendTextResponse(out, 507, "Insufficient Storage"', self.server)
        self.assertIn('sendUploadError(out, 507,', self.server)
        self.assertIn('status == 507 ? "Insufficient Storage"', self.server)


if __name__ == "__main__":
    unittest.main()
