# GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-002 — Cumulative Audit Report

**Date:** 9 November 2026  
**Repository:** `Frangga11/glass-tv-home`  
**PR:** [#100](https://github.com/Frangga11/glass-tv-home/pull/100)  
**Branch:** `integration/home-spacing-capsule-height-pr89-cumulative-001`  
**Exact HEAD:** `2de055a3b08fa9a0e5c4d2bb86d2884dbd71f43b`  
**Base:** PR #89 branch `fix/daily-ayah-offline-retry-backoff-001`, exact SHA `4038c08f4825f7bacf965bc6708505d3c8c30ebc`  
**Status:** CUMULATIVE SOURCE / CI / RELEASE APK VERIFIED / RENDERED VISUAL ACCEPTANCE PENDING — **BELUM FINAL**.

## 1. Objective and ancestry

The original PR #99 was based on PR #83 HEAD `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`, so its APK omitted the subsequent Islamic shelf, prayer and daily-Ayah fixes. The owner authorized repeating the complete PR #99 process from PR #89.

PR #100 is based directly on the exact PR #89 HEAD `4038c08f4825f7bacf965bc6708505d3c8c30ebc`. PR #89 is itself stacked on PR #88, which is stacked on #87, #86, #85 and #84. Therefore this candidate inherits the full sequential chain:

- #84 — Islamic shelf single-anchor fix
- #85 — Arabic typography
- #86 — prayer refresh and next Subuh
- #87 — daily Ayah cache status and label
- #88 — prayer source label transparency
- #89 — daily Ayah offline retry/backoff

All PRs #84–#89 were rechecked live and remain OPEN / DRAFT / UNMERGED. Source PR #99 also remains OPEN / DRAFT / UNMERGED and is not the cumulative candidate.

## 2. Owner-approved scope — preserved

1. Left/right/bottom safe frame: **30dp**; top remains **29dp**.
2. MIC/Assistant, Wi-Fi and Beranda/Profile/Settings capsules: **30dp** outer height. Inputs is explicitly excluded.
3. Beranda and Profile icon-label gap: **2dp**; Settings remains **4dp**.
4. Wallpaper description and prayer timetable visible lower edges: **30dp** above the measured horizontal Glass Dock tray.
5. Preserve Inputs, Dock/Grid tile sizing and order, content, typography, icons, glass/blur/transparency/colors/shadows, focus/click/DPAD, prayer/Ayah business logic, preferences and storage.

At 320dpi (2px/dp), 30dp = 60px and 29dp = 58px.

## 3. Integration reconciliation

PR #99 changed three production files and eight QA/workflow files (11 total). Its source patches were reapplied against the PR #89 exact tree rather than retargeting or blindly cherry-picking the old PR branch.

The main overlapping conflict was `tests/test_home_dock_islamic_shelf_navigation_refinement_contract.py`:
- The obsolete 27.5dp measured-tray assertion was replaced by PR #99's exact 30dp assertion.
- PR #84's additional single-bottom-anchor regression test was retained.
- The resulting Python contract suite passed.

The candidate compare against PR #89 reports **2 commits ahead, 0 behind**, with exactly 11 changed files:
- Production (3): `activity_main.xml`, `MainActivity.java`, `DockGeometry.java`.
- Android tests (2): `DockGeometryTest.java`, `DockLayoutModelTest.java`.
- Python regression contracts (5): Home Dock/Islamic shelf navigation, Home layout bounds, new spacing/capsule refinement, Home UI proportion/glass/focus, Home wallpaper text.
- CI workflow (1): PR #100 added to the Home layout APK artifact allowlist.

No unrelated production file was changed.

## 4. Source-level verification

- Main Home content XML: 30dp start/end padding, 29dp top.
- DockGeometry: 30dp horizontal and bottom constants; separate 29dp top accessor; 30dp measured shelf-to-tray gap.
- MainActivity: shelf horizontal margins use 30dp; Islamic shelf top uses dedicated 29dp inset; bottom tray margin uses 30dp. Wallpaper lower edge anchors at measured tray top minus 30dp. Prayer table's existing `translationY=-6dp` is compensated using its actual runtime translation to align the visible lower edge.
- Navigation capsule is 30dp; Wi-Fi and MIC/Assistant are 30dp; Beranda/Profile drawable gap 2dp; Settings retains 4dp; Inputs has no new explicit height.
- Candidate retains PR #89's `WeeklyAyah.shouldAttemptRefresh(this)`, persisted 30-minute cooldown, and atomic single-flight guard. PrayerTimes and its refresh/next-Subuh code are inherited from the PR #86–#88 chain.
- Regression test source confirms both PR #84's single-anchor test and PR #99's 30dp measured-tray test are present.

These checks establish source and automated-contract consistency; they do not prove final rendered pixels.

## 5. Exact-head CI and tests

- CI workflow: #1677 / run `42533185178`.
- Exact tested HEAD: `2de055a3b08fa9a0e5c4d2bb86d2884dbd71f43b`.
- Lists/tools/docs job ID `128891684465`: **SUCCESS**.
- Launcher build and unit-test job ID `128891684759`: **SUCCESS**.
- Python regression suite: **208 tests, OK (1 skipped)**.
- Gradle command: `./gradlew --no-daemon testReleaseUnitTest assembleRelease`.
- Android unit tests and release assembly: **BUILD SUCCESSFUL**; 99 actionable Gradle tasks executed.
- APK artifact allowlist explicitly includes PR #100; upload step succeeded.

## 6. Release APK and artifact verification

| Item | Verified value |
|---|---:|
| Release APK size | **1,626,857 bytes** |
| APK size diagnostic | 1,588 KB |
| Historical 1.5 MiB reference | 1,572,864 bytes |
| Amount above reference | 53,993 bytes (non-blocking warning) |
| Artifact ID | **13638675606** |
| Artifact ZIP size | **1,428,688 bytes** |
| Artifact ZIP SHA-256 | `e1428796d54847cd7e33c5c761b7339ad76c6eea004b8e26c39f6499d9502521` |
| Extracted APK SHA-256 | `b7ae8aa3919e0818b0879b8996427ea6eab9b91f14bf0fcff84b80fad32f292a` |

Artifact URL: https://github.com/Frangga11/glass-tv-home/actions/runs/42533185178/artifacts/13638675606

The ZIP was downloaded and inspected. It contains exactly one `app-release.apk`; its local ZIP SHA-256 matches GitHub's artifact digest. The extracted file is identified as an Android package with an APK Signing Block. The APK hash above was calculated independently after extraction.

The APK is larger than the historical 1.5 MiB reference; this is diagnostic and non-blocking. Compared with isolated PR #99's prior 1,625,337-byte APK, the cumulative APK is 1,520 bytes larger.

## 7. Acceptance status

| Gate | Result |
|---|---|
| PR #84–#89 ancestry inherited | PASS |
| PR #99 scope reapplied | PASS |
| Overlapping shelf test reconciled without deleting PR #84 test | PASS |
| Changed-file scope (11 files) | PASS |
| Python regression suite | PASS — 208, 1 skipped |
| Android unit tests | PASS |
| Release build | PASS |
| APK artifact publication | PASS |
| ZIP integrity / single APK entry / ZIP digest | PASS |
| Extracted APK identification and SHA-256 | PASS |
| Rendered 1920×1080 visual geometry and clipping review | **PENDING** |
| Physical Mi TV Stick visual / DPAD / runtime acceptance | **PENDING** |
| Merge | NOT AUTHORIZED / NOT PERFORMED |

No ADB session, APK installation, device setting/default-launcher change or merge has been performed.

## 8. Next action

Perform rendered 1920×1080 visual review of the exact cumulative build and compare actual visible edges/capsule heights against the approved measurements. Only after that review passes should physical Mi TV Stick acceptance be considered. Keep PR #84–#89, #99 and #100 OPEN / DRAFT / UNMERGED until owner approval.
