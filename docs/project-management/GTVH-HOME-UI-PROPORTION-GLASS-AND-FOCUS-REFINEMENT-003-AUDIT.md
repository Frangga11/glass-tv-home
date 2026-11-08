# GTVH-HOME-UI-PROPORTION-GLASS-AND-FOCUS-REFINEMENT-003 — Audit Report

**Date:** 9 November 2026  
**Repository:** `Frangga11/glass-tv-home`  
**Workstream:** `GTVH-HOME-UI-PROPORTION-GLASS-AND-FOCUS-REFINEMENT-003`  
**PR:** [#83 — GTVH-HOME-UI-PROPORTION-GLASS-AND-FOCUS-REFINEMENT-003](https://github.com/Frangga11/glass-tv-home/pull/83)  
**Source branch:** `fix/home-ui-proportion-glass-focus-refinement-003`  
**Exact source HEAD:** `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`  
**Base branch:** `fix/home-islamic-shelf-vertical-position-grid-row-scroll-002`  
**Base exact HEAD:** `d6f594933b712b36b0660e119a0d4ee8b7de3134`  
**PR state:** OPEN / DRAFT / UNMERGED  
**Result:** IMPLEMENTATION + AUTOMATED VALIDATION PASS / PHYSICAL ACCEPTANCE PENDING

---

## 1. Trigger and scope

This work continues from canonical checkpoint `GTVH-CHECKPOINT-2026-11-09`. PR #82 is the immediate source baseline for Islamic Shelf positioning and Grid navigation. This is a separate refinement; PR #80, #81 and #82 were not merged or rewritten.

The seven requested priority groups were audited and implemented:
A. Manual-only app updates.
B. Navigation capsule proportions.
C. Icon-to-label spacing.
D. Glass Dock icon proportions and host height.
E. Prayer timetable / Daily Doa separation and Islamic Shelf gradient.
F. Shared glass material and focus indicator.
G. Cross-audit and regression coverage.

## 2. Baseline findings

The exact source baseline was PR #82 HEAD `d6f594933b712b36b0660e119a0d4ee8b7de3134`.

| Area | Baseline observed |
|---|---|
| Update flow | Automatic checks were scheduled 5 seconds after Home launch and 1.5 seconds after Home resume. A non-manual check could show the update dialog. |
| Settings badge | `badgeSettingsUpdate` was wired to update availability. |
| Navigation | Outer navigation capsule was 40dp high with 3dp inner padding; icon/text drawable spacing was 5dp. |
| Dock | Horizontal Dock extra host height was 14dp; Dock icon bounds were 44dp; Grid icon bounds were 48dp. |
| Islamic Shelf | Right-side gradient ended at alpha `0x44`, with middle stop `0x1F`. |
| Focus | Top pills used a shared Glider and GlassDrawable, but the three navigation tabs also received a second foreground selector focus treatment. |
| Glass fallback | Header and general pill XML fallback selectors had different focused material treatments. |

## 3. Implementation results by priority

### A — Manual-only updates
- Removed automatic update checks from Home launch and resume.
- Removed automatic update scheduling/time controls and their obsolete localized strings.
- Update dialogs are gated by `manualCheck == true`; non-manual checks no longer prompt.
- Removed the Settings red update badge and its state wiring.
- Retained the Settings update entry point, manual check action, version comparison, download flow, unknown-app install permission and PackageInstaller handoff.
- A user-initiated deferred update no longer promises a persistent red dot.

### B/C — Navigation
- Outer capsule height: **40dp → 36dp**.
- Inner padding: **3dp → 2dp**; inner segment height becomes 32dp.
- Icon/text drawable spacing: **5dp → 4dp** on Beranda, Profil and Pengaturan.
- Equal segment weighting and centered content are preserved.

### D — Glass Dock
- Horizontal Dock extra host height: **14dp → 12dp**.
- Existing bottom-anchor and tray geometry helpers are retained; the Dock/Grid unit test now verifies the refined relationship.
- Horizontal Dock icon view uses a restrained `scaleX = 1.045`; vertical Dock scale remains 1.0.
- Dock icon bounds remain 44dp and Grid icon bounds remain 48dp.

### E — Islamic Shelf
- Added a 1dp divider, 180dp wide, inside the Daily Doa block. It disappears naturally when that block is gone.
- Rebalanced the previous 8dp outer margin into 6dp top + 2dp bottom divider spacing, keeping the section rhythm nearly unchanged.
- Scrim gradient stops changed from `0x00000000, 0x1F000000, 0x44000000` to `0x00000000, 0x12000000, 0x26000000`.

### F — Focus and glass
- All top header pills use `GlassDrawable` with a 20dp radius.
- Header and general pill XML fallback backgrounds now use the same 20dp radius/material.
- Shared Glider remains the single focus indicator for Location, Weather, Inputs, Wi-Fi, Microphone, Beranda, Profil and Pengaturan.
- The navigation tab selector now paints only a restrained selected-tab tint; its duplicate focused-state overlay was removed.
- Glider corner radius is capped at 20dp and respects shorter pill heights.

### G — Audit and tests
- Added a dedicated Home UI refinement Python contract.
- Updated Dock geometry, wallpaper scrim and localization regression contracts.
- Updated Android Dock geometry unit expectations for the 12dp host.

## 4. Changed files

Total: **20 files**.

- `.github/workflows/ci.yml`
- `.github/workflows/publish-apk-release.yml`
- `launcher/app/src/main/java/com/gathin/tvhome/AppUpdateManager.java`
- `launcher/app/src/main/java/com/gathin/tvhome/DockAdapter.java`
- `launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java`
- `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
- `launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java`
- `launcher/app/src/main/res/drawable/header_glass_pill_bg.xml`
- `launcher/app/src/main/res/drawable/header_nav_tab_bg.xml`
- `launcher/app/src/main/res/drawable/pill_bg.xml`
- `launcher/app/src/main/res/layout/activity_main.xml`
- `launcher/app/src/main/res/layout/dialog_ota_coffee.xml`
- `launcher/app/src/main/res/values-en/strings.xml`
- `launcher/app/src/main/res/values-id/strings.xml`
- `launcher/app/src/main/res/values/strings.xml`
- `launcher/app/src/test/java/com/gathin/tvhome/DockLayoutModelTest.java`
- `tests/test_f06_localization_ui_contract.py`
- `tests/test_home_dock_islamic_shelf_navigation_refinement_contract.py`
- `tests/test_home_ui_proportion_glass_focus_refinement_contract.py`
- `tests/test_home_wallpaper_text_refinement_contract.py`

## 5. Automated validation

### CI #1637
- Workflow run: `42509424284`.
- Result: **SUCCESS**; both jobs passed.
- Python suite: **184 tests, OK (1 skipped)**.
- Android: app and installer `testReleaseUnitTest` passed.
- Android Release: `assembleRelease` passed; `BUILD SUCCESSFUL`.
- Release APK size diagnostic: **1,625,213 bytes**.

### Direct APK publisher #92
- Workflow run: `42509424259`.
- Result: **SUCCESS**.
- Publisher checked out exact PR HEAD `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`, ran Release unit tests and `assembleRelease`, verified APK package/manifest, calculated SHA-256, and uploaded the direct APK asset.
- APK filename: `Glass-TV-Home-PR83-947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803.apk`.
- APK size: **1,625,213 bytes**.
- APK SHA-256: `2d14894da872f616188bace686be5f85021766914bbe65260cc76a7848baee26`.
- Release page: [PR #83 QA APK](https://github.com/Frangga11/glass-tv-home/releases/tag/glass-tv-home-pr83-947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803).

### CI validation artifact
- Artifact ID: `13628113675`.
- Name: `glass-tv-home-dock-visual-parity-32aa0c862b9d57e47cb5b703c598e7ffe4fb8b2f`.
- ZIP size: **1,427,011 bytes** (not the APK size).
- ZIP SHA-256: `7caf8c43502a4db15c61a098bf1d55559ecf6a0fdf2f8eb73eeaecf0d32dbd08`.
- Expires: 15 November 2026.

### APK size comparison
- PR #82 APK: 1,630,737 bytes.
- PR #83 APK: 1,625,213 bytes.
- Difference: **5,524 bytes smaller** than PR #82.
- The 1.5 MiB historical diagnostic threshold is exceeded by 52,349 bytes. This is a non-blocking warning; the new APK is smaller than the previous PR #82 APK.

## 6. Quality and risk assessment

**Passed by source/CI**
- Manual-only update control path and preserved install pipeline are covered by contract tests and the Release build.
- Navigation capsule dimensions and spacing are covered by XML-based contracts.
- Dock icon scale and host geometry are covered by Python and Android unit tests.
- Islamic divider and gradient stops are covered by source contracts.
- Shared Glider and glass fallback consistency are covered by source/XML contracts.
- No CI or Android Release build errors remain at the exact HEAD.

**Not yet verified physically**
- Visual perception of the 36dp capsule and 4dp spacing on the target TV.
- Actual icon optical width, Dock height, focus alignment and clipping on Mi TV Stick.
- All seven requested focus targets across normal/focused/pressed/disabled states using the remote.
- Wallpaper readability and the divider's visual weight on multiple wallpapers.
- Cold launch/resume behavior with no automatic update reminder.
- Live version endpoint, manual update discovery, install-permission screen and PackageInstaller handoff on device.

No Mi TV Stick installation, ADB session, screenshot capture, device setting change or physical acceptance was performed in this audit.

## 7. Final decision

**IMPLEMENTATION COMPLETE / CI PASS / RELEASE APK PUBLISHED / NOT FINAL.**

PR #83 remains OPEN / DRAFT / UNMERGED. PR #80, #81 and #82 remain unmerged. Do not merge or declare final acceptance until the owner verifies the exact-head APK on Mi TV Stick at 1920×1080 / density 320 and records the results against the physical acceptance checklist.

Next action: install only the exact-head APK from the release page, test all A–F visual/functional acceptance points, and report observed discrepancies before any further source change.
