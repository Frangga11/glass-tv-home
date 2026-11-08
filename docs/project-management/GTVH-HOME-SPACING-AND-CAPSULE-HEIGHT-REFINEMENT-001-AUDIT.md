# GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-001 — Audit Report

**Date:** 9 November 2026  
**Repository:** `Frangga11/glass-tv-home`  
**PR:** [#97](https://github.com/Frangga11/glass-tv-home/pull/97)  
**Branch:** `verify/home-spacing-capsule-height-refinement-001-validated`  
**Exact HEAD:** `bb10228f930cc0b0e8c3dbf526aad9b0c94112fd`  
**Base:** `fix/home-ui-proportion-glass-focus-refinement-003` at `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`  
**Status:** BELUM FINAL — CI Android/release build and visual acceptance pending.

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
- `tests/test_home_spacing_capsule_height_refinement_contract.py` (new)
- `tests/test_home_dock_islamic_shelf_navigation_refinement_contract.py`
- `tests/test_home_layout_bounds_contract.py`
- `tests/test_home_ui_proportion_glass_focus_refinement_contract.py`
- `tests/test_home_wallpaper_text_refinement_contract.py`

PR contains 8 files total. Production changes are limited to the three files above. Test changes update obsolete assertions to the newly approved geometry and retain active checks. No unrelated production files changed.

## 5. Quality checks

- Python CI: **PASS — 190 tests, 1 skipped**.
- Lists/tools/docs CI job: **PASS**.
- Android `testReleaseUnitTest`: PENDING exact-head CI run #1668.
- Android `assembleRelease`: PENDING exact-head CI run #1668.
- APK artifact, size and SHA-256: PENDING.
- Rendered 1920×1080 visual audit: PENDING.
- Mi TV Stick physical acceptance: NOT PERFORMED by design; wait until automated and visual audits pass.
- Merge/installation/ADB: NOT PERFORMED.

## 6. PR history

- PR #94: CLOSED / NOT MERGED; superseded.
- PR #95: CLOSED / NOT MERGED; superseded.
- PR #96: CLOSED / NOT MERGED; superseded.
- PR #97: OPEN / DRAFT / UNMERGED; active review and CI target.

## 7. Final decision

**BELUM FINAL.** Python regression tests pass, but Android unit tests, release build, APK verification and rendered visual audit remain outstanding. No APK should be installed on the TV and no merge should occur before all gates pass.
