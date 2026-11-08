# GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-001 — Audit Report

**Date:** 9 November 2026  
**Repository:** `Frangga11/glass-tv-home`  
**PR:** [#99](https://github.com/Frangga11/glass-tv-home/pull/99)  
**Branch:** `verify/home-spacing-capsule-height-refinement-001-qa`  
**Exact HEAD:** `443d8491109c416915955c8a9ceb202860916a6b`  
**Base:** `fix/home-ui-proportion-glass-focus-refinement-003` at `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`  
**Status:** AUTOMATED CI PASS / APK VERIFIED / RENDERED VISUAL ACCEPTANCE PENDING — BELUM FINAL.

## 1. Owner-approved requirements

1. Left/right safe frame 30dp; bottom safe frame 30dp; top remains 29dp.
2. MIC/Assistant, Wi-Fi and Beranda/Profile/Settings capsule geometry 30dp.
3. Beranda and Profile icon-label spacing reduced to 2dp; Settings spacing remains 4dp.
4. Wallpaper description and prayer schedule visible lower edges have 30dp clearance to the actual horizontal Glass Dock tray.
5. Do not change Inputs, Dock/Grid tile sizing/order, wallpaper/prayer content or typography, icons, glass material, blur, transparency, colors, shadows, focus/click/DPAD behavior, business logic, preferences or storage.

## 2. Source findings and implementation

- `activity_main.xml`: Home content side padding changed from 29dp to 30dp; top remains 29dp. `navPill` outer height changed from 36dp to 30dp, vertical padding set to zero so each match-parent navigation segment receives the full 30dp height; horizontal padding remains 2dp. Beranda and Profile drawable padding changed from 4dp to 2dp. Wi-Fi and MIC/Assistant are explicitly 30dp. Inputs inherits its unchanged existing 42dp style.
- `DockGeometry.java`: horizontal and bottom safe margin constants changed to 30dp; a dedicated 29dp top inset accessor prevents accidental top-edge movement. Shelf-to-tray gap uses density-aware 30dp. Both horizontal and vertical Dock modes use the 30dp bottom safe inset.
- `MainActivity.java`: shelf side margins use the 30dp inset while the Islamic shelf top margin uses the dedicated 29dp inset. Horizontal shelf positions are derived from the actual tray top calculated from laid-out Dock card bounds and the existing 14dp tray inset. Wallpaper shelf bottom is `trayTop - 30dp`. Prayer table retains its XML `translationY=-6dp`; the runtime shelf anchor compensates that actual translation so the visible table lower edge is 30dp above the tray. The legacy vertical-Dock inter-shelf offset remains intact.
- No drawable, style, font, icon, color, blur, focus, click, or business-logic changes.

## 3. Measurements

| Target | Source value | New value | 320dpi equivalent |
|---|---:|---:|---:|
| Left safe frame | 29dp | 30dp | 60px |
| Right safe frame | 29dp | 30dp | 60px |
| Bottom safe frame | 29dp | 30dp | 60px |
| Top safe frame | 29dp | 29dp | 58px |
| MIC/Assistant | 32dp | 30dp | 60px |
| Wi-Fi | inherited 42dp | 30dp | 60px |
| Beranda/Profile/Settings capsule | 36dp outer; 34dp inner after 2dp padding | 30dp outer and 30dp segment bounds | 60px |
| Horizontal shelf-to-tray clearance | 37.5dp wallpaper / 27.5dp Islamic container anchor | 30dp visible lower edges | 60px |
| Beranda/Profile icon-label spacing | 4dp | 2dp | 4px |

These are source/layout-derived values. A rendered-device pixel measurement has not yet been performed.

## 4. Changed files and scope audit

Production:
- `launcher/app/src/main/res/layout/activity_main.xml`
- `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
- `launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java`

Regression:
- `launcher/app/src/test/java/com/gathin/tvhome/DockGeometryTest.java`
- `launcher/app/src/test/java/com/gathin/tvhome/DockLayoutModelTest.java`
- `tests/test_home_spacing_capsule_height_refinement_contract.py` (new)
- `tests/test_home_dock_islamic_shelf_navigation_refinement_contract.py`
- `tests/test_home_layout_bounds_contract.py`
- `tests/test_home_ui_proportion_glass_focus_refinement_contract.py`
- `tests/test_home_wallpaper_text_refinement_contract.py`
- `launcher/app/src/test/java/com/gathin/tvhome/DockGeometryTest.java`
- `launcher/app/src/test/java/com/gathin/tvhome/DockLayoutModelTest.java`
- `.github/workflows/ci.yml` (adds PR #99 to existing QA APK artifact allowlist)

PR contains 8 files total. Production changes are limited to the three files above. Test changes update obsolete assertions to the newly approved geometry and retain active checks. No unrelated production files changed.

## 5. Quality checks

- Python CI: **PASS — 190 tests, 1 skipped**.
- Lists/tools/docs CI job: **PASS**.
- Android `testReleaseUnitTest`: exact-head retest PENDING CI #1669/#1670 after updating four stale assertions in `DockGeometryTest` and `DockLayoutModelTest`.
- Android `assembleRelease`: PENDING exact-head CI #1669/#1670.
- APK artifact, size and SHA-256: PENDING.
- Rendered 1920×1080 visual audit: PENDING.
- Mi TV Stick physical acceptance: NOT PERFORMED by design; wait until automated and visual audits pass.
- Merge/installation/ADB: NOT PERFORMED.

## 6. PR history

- PR #94: CLOSED / NOT MERGED; superseded.
- PR #95: CLOSED / NOT MERGED; superseded.
- PR #96: CLOSED / NOT MERGED; superseded.
- PR #99: OPEN / DRAFT / UNMERGED; active review and CI target.

## 7. Final decision

**BELUM FINAL.** Python regression tests pass, but Android unit tests, release build, APK verification and rendered visual audit remain outstanding. No APK should be installed on the TV and no merge should occur before all gates pass.


## CI correction checkpoint — PR #99 — 9 November 2026

- PR #97 is now CLOSED / NOT MERGED and superseded by [PR #99](https://github.com/Frangga11/glass-tv-home/pull/99). PR #99 is OPEN / DRAFT / UNMERGED; base remains PR #83 at `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`.
- Active source branch: `verify/home-spacing-capsule-height-refinement-001-qa`; exact HEAD: `443d8491109c416915955c8a9ceb202860916a6b`.
- PR #99 retains the three production files and adds two Android unit-test updates to the five Python test changes: total 10 files (3 production, 7 test). No unrelated production files added.
- Exact-head CI #1670 / run `42531675366` and #1669 / run `42531662425` are in progress at checkpoint capture. Previous exact-head Python suite passed 190 tests (1 skipped); Lists/tools/docs passed. Previous Android unit tests compiled production Java and found four stale test assertions (old 29dp safe frame and 27.5dp shelf gap). PR #99 updates those two Android test files to assert 30dp geometry; exact-head Android retest remains PENDING.
- No APK QA asset, rendered 1920×1080 visual audit, or physical Mi TV Stick acceptance is available. No ADB, installation, device setting changes or merge.
- **Status remains BELUM FINAL.** Await exact-head Android unit tests and release build, APK size/hash verification, then rendered visual audit. Do not install or request TV testing before all automated and visual gates pass.


## QA artifact and Android regression checkpoint — PR #99 — 9 November 2026

- Active source PR: [#99](https://github.com/Frangga11/glass-tv-home/pull/99), OPEN / DRAFT / UNMERGED. Exact base: PR #83 branch SHA `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`.
- Active source branch: `verify/home-spacing-capsule-height-refinement-001-qa`; exact HEAD: `443d8491109c416915955c8a9ceb202860916a6b`.
- PR #99 adds one QA workflow adjustment to the PR #98 candidate: .github/workflows/ci.yml now allows this refinement PR to publish the existing Home layout QA APK artifact. Production source remains exactly 3 files; regression coverage remains 7 files. Total 11 files.
- Exact-head CI #1672 / run `42531805446` and #1671 / run `42531795756` are in progress. Earlier Python suite result: 190 tests, OK (1 skipped); Lists/tools/docs job PASS. Android unit-test and release-build outcome on PR #99 is pending.
- Publisher workflow is skipped by design; the CI workflow's Home layout test APK artifact step is enabled for PR #99. Artifact publication and APK verification are pending.
- No rendered 1920×1080 visual review, physical Mi TV Stick acceptance, ADB, installation, setting change or merge.
- **Status BELUM FINAL.** Complete exact-head Python/Android tests, release build, QA artifact size/hash and rendered visual audit before any device installation or final declaration.


### Exact-head CI update — PR #99 — 9 November 2026

- CI #1672 / run `42531805446`: Lists/tools/docs job completed SUCCESS; Python suite **190 tests, OK (1 skipped)** on PR #99 exact HEAD `443d8491109c416915955c8a9ceb202860916a6b`.
- Launcher build/unit-test job remains IN PROGRESS; Android `testReleaseUnitTest`, `assembleRelease`, APK size diagnostics and QA artifact upload are not yet verified.
- Status remains **BELUM FINAL**. No APK installation, ADB, device setting change, visual acceptance or merge.


## Exact-head CI and APK verification addendum — 9 November 2026

**Status: AUTOMATED GATES PASS / APK DOWNLOADED AND SHA-256 VERIFIED / RENDERED VISUAL AUDIT PENDING / NOT FINAL.**

- Correct active PR: [#99](https://github.com/Frangga11/glass-tv-home/pull/99), OPEN / DRAFT / UNMERGED. Branch `verify/home-spacing-capsule-height-refinement-001-qa`, exact HEAD `443d8491109c416915955c8a9ceb202860916a6b`, base PR #83 at `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`.
- Exact-head CI #1672 / run `42531805446`: both required jobs SUCCESS. Python **190 tests, OK (1 skipped)**; Android `testReleaseUnitTest` and `assembleRelease` completed; Gradle reports **BUILD SUCCESSFUL**. CI #1671 / run `42531795756` also completed successfully as a duplicate PR event.
- Release APK: **1,625,337 bytes**. Workflow artifact #13637424249, ZIP **1,426,876 bytes**, SHA-256 `1d2955027a07a2a80e7eb3398821639b6595542768860b35a4b6d185f5e38ff4`, expires 15 November 2026 UTC.
- Downloaded archive inspected; it contains exactly one `app-release.apk`. Extracted APK size **1,625,337 bytes**, SHA-256 `96d2bae44ac33a7c250467e37cb2d35c4d4cb85fc767993dc9625e6bd1e0eead`; recognized as an Android APK with APK Signing Block.
- Final PR changed-file inventory is 11 files: 3 production, 7 regression tests, 1 CI workflow. No other production file changed.
- APK-size diagnostic notes 1.5 MiB historical reference exceeded (actual 1,587 KiB); warning is explicitly non-blocking. Review size delta in normal QA, but it does not invalidate successful build/tests.
- The audit measurements remain source/layout-derived. No rendered 1920×1080 screenshot or physical Mi TV Stick result has been captured. Therefore visual edge clearance, capsule appearance and final on-device behavior are **not yet accepted**.
- No ADB, device installation, setting changes, merge, or PR readiness change performed.
- **Decision: BELUM FINAL.** Automated CI and artifact verification pass; rendered visual audit is the only outstanding pre-device gate. Keep PR #99 draft/unmerged.
