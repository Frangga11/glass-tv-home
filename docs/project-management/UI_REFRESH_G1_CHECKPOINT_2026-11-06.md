# GTVH-UIR-002 — G1 Visual Prototype Completion Checkpoint

**Date:** 6 November 2026  
**Work package:** G1 — Penyelesaian Prototipe Visual Global UI  
**Status:** **QC PASS — G1 COMPLETE (prototype scope only)**  
**Canonical branch:** `docs/project-master-inventory`  
**Parent documentation head:** `7dfbedd3fd0710cac66cf71eea5876c07eb7d08a`  
**Target render:** Android TV 1920×1080, 16:9  
**Browser:** Chromium 144.0.7559.96, device scale factor 1

## 1. Result

Completed the V01–V06 interactive visual prototype revision against the six owner-supplied references and the locked P1/P2 contracts. V6 supersedes the earlier V5 prototype as the current G1 review artifact; V5 and its audit history remain preserved.

Canonical artifact: [UI_REFRESH_G1_GLOBAL_UI_PROTOTYPE_V6.html](prototypes/UI_REFRESH_G1_GLOBAL_UI_PROTOTYPE_V6.html)

SHA-256: `7cbbcea5e48a1be91ec619d15e3d89e0490060b1c084adbcda2d74913ec341de`

The prototype is self-contained, including its scenic wallpaper asset. Prototype-only controls are exposed with the URL query `?lab=1`; normal viewing hides the test toolbar.

## 2. Delivered surfaces

| Surface | Delivered composition and interaction |
|---|---|
| V01 — Home & Dock | Home hierarchy, location/weather/AQI chips, prayer timetable and bottom Dock. Six Dock variants are selectable: horizontal/vertical × 4/5/6. App grid remains a separate four-column region and does not change with Dock configuration. |
| V02 — Weather & AQI V4 | Current condition, feels-like/min/max, humidity, wind speed/direction, pressure, dew point, visibility, sunrise/sunset, moon phase, rainfall/probability, UV/category/advice, AQI/category/explanation, PM2.5, PM10, CO, NO₂ and SO₂, hourly and seven-day forecast, source/update status. |
| V03 — Profile Selector | Four cards (Frangga, Keluarga, Anak, Tambah Profil), active-vs-focused distinction, spatial D-pad movement and Child Mode PIN gate. |
| V04 — Quick Settings | Separate popup with Wi-Fi, restart, language, power, cache, Next Photo and wallpaper interval. Confirmation dialogs are explicitly simulated and never claim to change the real device. |
| V05 — Wi-Fi Popup | Connected SSID/status, conditional scan list, secured network rows, rescan and Android Settings handoff. No in-app Wi-Fi master switch is presented. |
| V06 — Global Settings Split Detail | Seven canonical categories, left category navigation, right detail pane, profile detail and control-level search routing. |

## 3. Verification evidence

**Functional browser suite: 12 PASS / 0 FAIL.** Results are recorded in the execution artifact `G1_FUNCTIONAL_TEST_RESULTS.json` (SHA-256 `cfc2b6b184d6b8ae3e56884041166ca6e0e29fa3e3b44822d4563ddab2fa7ef1`).

1. All six Global UI surfaces mounted.
2. All six Dock combinations rendered; independent four-column app grid invariant held; vertical Dock items fit without clipping or overlapping the app grid.
3. Home → Wi-Fi → Back restored the invoking control.
4. Quick Settings → Wi-Fi → Back restored the parent popup and focus.
5. Quick Settings close restored the exact Home invoking control.
6. Nested dialog stack restored parent and root focus correctly.
7. Weather V4 field coverage plus offline, refresh failure, stale, missing location, AQI unavailable, source unavailable and omitted-pollutant states.
8. Profile active/focus behavior, spatial D-pad traversal and Child Mode PIN gate.
9. Next Photo with zero/two images, unavailable/failure/success feedback.
10. Wi-Fi permission denied, Location off, offline, throttled, stale and empty-result states; bounded rescan completion.
11. Seven Settings categories and control-level search routing.
12. All six surfaces remained inside the 1920×1080 viewport with large-text mode enabled.

Additional checks: JavaScript syntax validation with Node.js v22.16.0 PASS; Chromium browser rendering and 13 screenshots captured at exactly 1920×1080; manual visual comparison against the six supplied references completed. Daylight/bright-wallpaper contrast mode and normal mode were rendered.

### Screenshot evidence captured

- Six main surfaces: `G1_V01_1920x1080.png` … `G1_V06_1920x1080.png`
- Six Dock variants: `G1_Dock_horizontal_4_1920x1080.png`, `G1_Dock_horizontal_5_1920x1080.png`, `G1_Dock_horizontal_6_1920x1080.png`, `G1_Dock_vertical_4_1920x1080.png`, `G1_Dock_vertical_5_1920x1080.png`, `G1_Dock_vertical_6_1920x1080.png`
- Daylight view: `G1_V01_Daylight_1920x1080.png`
- Main contact sheet SHA-256: `c79640ea3fabb70fa87a9dde52a4b707ada1d9392b5e13506854e9eb986625dc`
- Six-Dock contact sheet SHA-256: `b37f1f2706f3476e2ad3d74beb71782985d52ea81f3cd13ff05dc060f1c2f918`

The contact sheets and full-resolution screenshots are attached to the G1 completion response; the canonical repository stores the self-contained HTML prototype and this checkpoint.

## 4. Quality boundary and residual gates

- **No production source was modified.** No owner branch (#62/#63/#64), integration branch, application code, APK, or build configuration was changed.
- No PR was merged, rebased, cherry-picked, or force-pushed.
- No APK was built/published/installed; no ADB, Android device, TV settings or physical TV were accessed or changed.
- Weather, AQI, SSIDs, profile actions, wallpaper rotation, PIN and device actions are labelled/simulated prototype states. They are not live provider/API integrations or proof of production implementation.
- G1 is a browser-prototype gate, not Android runtime/device acceptance. Physical TV/remote, overscan, 4K, live data, Android permission behavior and production component conformance remain later verification gates.
- Owner visual sign-off on V6 remains a separate product-acceptance decision before broad production rollout. This does not invalidate the completed internal G1 prototype/QC gate.

## 5. Decision and next work

**G1: COMPLETE / QC PASS.** Proceed to G2 — Shared UI Foundation Repair under GTVH-UIR-002 v2.0. Keep production rollout and broad styling changes behind the G2–G6 sequence and the existing owner-branch/merge/device-approval boundaries.
