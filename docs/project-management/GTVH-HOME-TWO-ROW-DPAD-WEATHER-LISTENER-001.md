# GTVH-HOME-TWO-ROW-DPAD-WEATHER-LISTENER-001

Date: 10 November 2026  
Repository: `Frangga11/glass-tv-home`  
PR: [#115](https://github.com/Frangga11/glass-tv-home/pull/115)  
Branch: `fix/home-two-row-dpad-weather-listener-001`  
Base: `fix/home-focus-overlay-dpad-001` (PR #114 cumulative candidate)  
Current source HEAD before this documentation update: `e81cc3ce25eaba3ddebad0fa99a2b348e99441c1`  
Status: OPEN / DRAFT / UNMERGED

## Trigger
User confirmed that the intended first app row is BrowserHere, Disney+, Miracast, Netflix, Google Play Store; the second row is Prime Video, Vidio, Vision+, WeTV, YouTube. The second row must sit below the first and appear only on DPAD DOWN from the primary row.

## Verified findings
1. Header capsule DPAD DOWN called `focusTile(0)`, revealing the hidden overflow Grid and bypassing the primary Dock row.
2. In horizontal mode, the first overflow Grid row's DPAD UP returned to the Home/header instead of the Dock item in the same column.
3. The supplied TV recording showed the visual rows reversed: overflow Grid above the primary Dock.
4. `weatherAirQualityView.setOnKeyListener(...)` was registered twice. The later registration replaced the earlier listener through Android's `View.setOnKeyListener` behavior.

## Changes implemented
- Header DPAD DOWN now targets `focusDockTile(0)`; it does not reveal the overflow Grid.
- Dock DPAD DOWN reveals the overflow Grid on demand, preserving the same column mapping.
- First overflow row DPAD UP returns to the corresponding Dock column. Later Grid rows continue using five-column row transitions.
- Horizontal Dock/Grid geometry anchors were swapped so the primary Dock row is above the overflow Grid row. Tile dimensions, item identities, five-column Grid span and one-row overflow viewport are preserved.
- Weather/AQI has one authoritative key listener. UP/LEFT returns to Location, RIGHT returns to Beranda, DOWN preserves the Now Playing stop and then enters the primary Dock.
- Inline Now Playing and prayer timetable DOWN paths enter the primary Dock.
- Added three Java regression tests covering the five-item primary/overflow partition, aligned column mapping and primary-above-overflow geometry.
- Added five Python source-contract tests covering header-to-Dock routing, exactly one Weather/AQI listener, same-column UP mapping, swapped visual row anchors and one-row overflow viewport.

## Files changed
- `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
- `launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java`
- `launcher/app/src/test/java/com/gathin/tvhome/DockLayoutModelTest.java`

## Quality checks
- Static source audit: exactly one Weather/AQI `setOnKeyListener` registration.
- Initial CI run exposed two source-contract test expectations; both were corrected while preserving the intended behavior:
  - Weather location focus contract retains `locationPill.requestFocus()`.
  - Grid viewport sizing expression retains the expected source-contract form.
- GitHub Actions run #1927 (`42845972311`) on the code HEAD: Lists/tools/docs job PASS; Android `testReleaseUnitTest assembleRelease` was still in progress at the last checkpoint.
- Earlier Android validation of the same functional code passed `testReleaseUnitTest assembleRelease` with `BUILD SUCCESSFUL`; it ran before the final source-contract-only formatting and Python source-test addition, so it is not a substitute for run #1927.
- Physical TV visual and DPAD acceptance is still PENDING. Do not claim device acceptance from CI alone.

## Protected scope
No intended changes to app inventory ordering, package/component identity, app launch behavior, Weather/AQI providers or refresh cadence, wallpaper, Hero Shelf, Islamic content/data, Wi-Fi behavior, or Dock/Grid item rendering.

## Next actions
1. Confirm CI against the final PR HEAD.
2. Inspect generated PR APK and verify artifact corresponds to exact HEAD.
3. Install/test on TV only after build validation: idle Home shows only the primary row; Header DOWN focuses BrowserHere; Dock DOWN reveals Prime Video below BrowserHere; DOWN/UP preserves same column; UP from primary row returns to header; verify all five columns and rapid repeated presses.
4. Keep PR #115 draft/unmerged until physical acceptance is recorded.


## Repository synchronization addendum — 17 November 2026

This document is retained as the historical record of its original trigger and implementation. Its earlier branch/PR status and pending gates are historical, not the current integration status. The cumulative successor work was integrated through PR #155 into `main` and validated on the latest documentation checkpoint:

- Current `main` checkpoint: `33776e2c772138325da7dd515ea84c2196f58d2d`.
- Main integration commit: `81010b0941237600758036827531aff8194fb273` (PR #155 merged).
- Exact latest-main CI run: `44058685408`; both `Launcher build and unit tests` and `Lists, tools and docs` completed SUCCESS.
- Cumulative QA artifact: `14317436391`, `glass-tv-home-cumulative-integration-127-33776e2c772138325da7dd515ea84c2196f58d2d`; contains `app-release.apk` (1,640,745 bytes). APK SHA-256: `29b89a90bd6093ed75e9d8876d02b78188bb4bd45696ef0321539aaa92871b5c`. ZIP integrity check passed after extraction.
- The user reported physical Mi TV Stick testing of the cumulative candidate as clear and clean. This is user-reported acceptance; it is not an independent device test by the assistant.
- The active DPAD/focus cumulative implementation is represented by the PR #153/#154 stack integrated to main. The weather cache restore in PR #154 reapplies cached overlay state before the 15-minute network-fetch gate; the gate remains for fetching only.
- Preserve the original scope boundaries: RE-SKIN, NOT REWIRE; no unrelated app inventory, layout, prayer/Ayah, popup, or wallpaper changes are implied by this synchronization.

For current continuation, use the final cumulative checkpoint document and the latest `HANDOVER.md` section as the canonical handoff. Do not reopen or merge the historical draft branches listed above as if they were current candidates. Any newly reported TV issue becomes a new, narrowly scoped corrective task.
