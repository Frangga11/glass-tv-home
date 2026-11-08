# GTVH-HOME-LAYOUT-FIX-001 — Balanced Safe Frame Cross-Audit

Date: 8 November 2026  
Target device: Mi TV Stick / Android TV 11 / 1920×1080 / 320dpi logical density (2px per dp)  
Status: SOURCE CORRECTIONS COMMITTED; EXACT-HEAD CI IN PROGRESS; PHYSICAL DEVICE ACCEPTANCE PENDING

## 1. Owner requirement and acceptance invariant

The Home screen must use one equal safe-frame inset on all four outer edges. Measure the visible Dock glass tray edge, not the RecyclerView host boundary, because the host intentionally extends beyond the tray to contain labels and focus effects.

Locked target: **34dp on top, bottom, left and right**. At density 2 this is **68px on each edge**.

## 2. Source-to-edge mapping

- Top: `mainContent` uses `paddingTop=34dp`.
- Left/right: `mainContent` uses `paddingStart=34dp` and `paddingEnd=34dp`.
- Left information shelf: `heroShelf` receives the same `DockGeometry.safeHorizontalInsetPx(density)` as its start margin.
- Right prayer/ayah shelf: `islamicShelf` receives that same inset for end margin and top margin.
- Bottom: `DockGeometry.horizontalDockBottomMarginPx()` offsets the host so the glass tray's actual bottom edge lands at `safeBottomInsetPx(density)`. The host's negative/positive outer margin is not itself the visible safe edge.
- Horizontal Dock host translation remains zero; its glass tray spans the host, while the icon group is centered using symmetric RecyclerView content padding. This preserves the full-width tray and aligned shelves.

For Mi TV Stick 1920×1080 at 320dpi: 34dp × 2 = 68px on all four visible frame edges.

## 3. Cross-audit corrections on PR #79

Repository: `Frangga11/glass-tv-home`  
Branch: `fix/home-balanced-frame-001`  
Current source HEAD: `a7a15e3a803dd173b302432c2665fc2c6db91426`  
PR #79: OPEN / DRAFT / UNMERGED.

Corrections:
1. Updated the stale `MainActivity.applyDockLayout()` comment from 48dp to 34dp so the source comment matches the active geometry.
2. Replaced the remaining literal `34f` in `DockGeometry.horizontalGridBottomReservePx()` with the shared `SAFE_VERTICAL_MARGIN_DP` constant.
3. Added a JVM regression test that checks the four-edge invariant across screen widths 1280/1696/1712/1920px, densities 1/1.5/1.75/2, and Dock capacities 4/5/6. It verifies the computed tray bottom edge against the same density-scaled inset and keeps the icon group horizontally centered.
4. Added a Python source-contract test proving the Dock tray bottom margin is wired to the shared bottom-inset geometry.
5. Cross-audit found that `showHomeWeather()` still wrote the legacy `HomeWeather.Line` text directly into the Home weather/AQI TextView, despite PR #79's stated shared G3B cache ownership. This could overwrite the location-scoped `WeatherAirQuality.getFormatted()` line. Corrected it: the visible text remains owned by the shared G3B formatter; the legacy refresh remains available only for the atmospheric wallpaper overlay. Added a regression contract test.

No Home shelf, prayer timetable, daily ayah, media card, wallpaper flow, app inventory, Dock glass, profile/settings action, or existing navigation feature was removed.

## 4. PR dependency and validation boundary

- PR #77 branch: `fix/home-layout-consolidated-001`; latest observed head `0ea3746f0304b7042a018eeebf850d759f357bf8`.
- PR #79 branch compares as 42 commits ahead / 1 behind PR #77, with merge base `0aeb3851ddce5deb47e3c5f96db44631b0343577`. The one missing PR #77 commit is workflow-only APK artifact publication configuration; no Home source file changed in that commit. Keep the dependency visible and reconcile the workflow before merge; do not silently claim the branch is fully synchronized.
- Earlier PR #79 CI #1521 / run `42463538759` passed both jobs on prior head `b8c3c48917b74395ba32fb6755619b501bc42d33`. That prior APK was 1,624,377 bytes, SHA-256 `1e86268869c8b2ea2172839d28f5c6d9d4ab92b91491e64ffd3a50da1daaf41b`. It is not the artifact for current HEAD.
- Exact-current-head CI #1526 / run `42463873162` is IN PROGRESS on `a7a15e3a803dd173b302432c2665fc2c6db91426`. Do not claim current-head CI or APK validation PASS until both jobs finish and the exact-head artifact is inspected and hashed.
- The earlier intermediate CI #1524 / run `42463825236` is not a substitute for #1526 because it predates the final weather ownership correction and its regression test.

## 5. Physical acceptance still required

CI and JVM/source contracts do not prove visual correctness on the physical TV. After exact-head CI and APK inspection, owner acceptance must verify:
- 68px visible margin from top, bottom, left and right screen edges at the 1920×1080 / 320dpi baseline.
- Dock glass tray left/right edges align with the content safe frame and its bottom edge matches the same 68px inset.
- Clock/date, center navigation, Quick Toggles, location/weather-AQI, prayer/ayah shelves remain inside the same frame without overlap.
- Dock icon group stays centered for capacities 4/5/6; sparse inventory does not shift the tray.
- One-row overflow remains hidden at rest and scrolls in place on DPAD Down/Up; no floating row, clipping, or focus theft.
- Existing visual treatments and functions remain intact.

PR #79 remains OPEN / DRAFT / UNMERGED. No merge, APK installation, ADB, default-launcher change, or device-setting mutation has been performed. Do not treat CI as physical visual acceptance.

## 6. Decision

Keep **34dp / 68px** as the four-edge safe-frame target for the current Mi TV Stick baseline. Preserve all already-good Home features. Only the explicitly identified geometry and weather-cache ownership defects were corrected. Final status remains **PENDING EXACT-HEAD CI AND PHYSICAL DEVICE ACCEPTANCE**.
