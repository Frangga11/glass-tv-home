# UI Refresh G3E — Wi-Fi Checkpoint

**Document:** GTVH-UIR-G3E-CHK-001  
**Date:** 7 November 2026  
**Status:** COMPLETE / SOURCE-HOST QC PASS  
**Device acceptance:** PENDING / NOT RUN

## 1. Exact source position

- Repository: `Frangga11/glass-tv-home`
- Owner branch: `feature/g3e-wifi`
- Exact HEAD: `89d044152b7d8460884466deb5baa47126f48027`
- PR #70: OPEN / DRAFT / UNMERGED — [G3E Wi-Fi V05](https://github.com/Frangga11/glass-tv-home/pull/70)
- PR base: `feature/g3d-quick-settings` @ `bbc7b240d2ad7bfc9d22dbaf3437f03b5953bcf8`
- G3D PR #69 remains unchanged and OPEN / DRAFT / UNMERGED.

## 2. Completed result

- V05-aligned Wi-Fi status/connected card, nearby scan list, security/signal information, horizontal Rescan and Android Wi-Fi Settings actions, and Close.
- Runtime fine-location permission and Location-service gating; `CHANGE_WIFI_STATE` added only for Android's scan request API.
- Scan receiver, asynchronous result state, timeout, throttling/last-result fallback, empty/unavailable/permission/location/radio-off states.
- System-managed selection/authentication/connect/disconnect/radio actions remain in Android Settings. No credentials or BSSID stored.
- Reused MainActivity's existing network-state owner; no duplicate ConnectivityManager callback.
- D-pad/Back, scan-row focus restoration, deferred parent return after system Settings and launch-failure fallback.
- Default/ID/EN Wi-Fi resources and regression/source-contract tests.

Detailed audit: [UI_REFRESH_G3E_AUDIT_2026-11-07.md](UI_REFRESH_G3E_AUDIT_2026-11-07.md).

## 3. Verification evidence

- Exact-head CI #1362 / `42346091802` on `89d044152b7d8460884466deb5baa47126f48027`: both required jobs SUCCESS.
- Lists/tools/docs: 127 Python tests PASS.
- Gradle: app and installer `testReleaseUnitTest` PASS; app and installer `assembleRelease` PASS; BUILD SUCCESSFUL.
- APK size diagnostic: 1,617,293 bytes; +8,920 bytes / approximately 0.55% vs G3D. Informational under D-023; no artifact published.
- Source-host QC: PASS.

## 4. Remaining boundary

- Physical Android TV/Mi TV Stick permission, Location, Wi-Fi scan/throttle, real network list, Android Settings handoff, D-pad/Back, OEM behavior and connection/authentication/disconnection acceptance: NOT RUN.
- `WifiManager.startScan()` is deprecated since API 28 and may be removed in a future Android release. Current active scan is best-effort and throttling is handled; future API migration remains a known risk.
- No APK install/publication, ADB, physical device mutation, merge, rebase, cherry-pick, force-push, integration/main change.

## 5. Next work

**G3F — Global Settings.** Start with read-only audit against V06, G1/G2 shared Settings/navigation/focus contracts, E01 and the actual current source. Do not reopen G3E or earlier completed packages without new reproducible defect evidence.

**Resume rule:** Start G3F audit from this checkpoint and exact source ancestry. Keep PR #70 OPEN / DRAFT / UNMERGED.
