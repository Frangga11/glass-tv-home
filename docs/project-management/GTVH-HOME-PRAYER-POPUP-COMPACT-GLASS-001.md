# GTVH-HOME-PRAYER-POPUP-COMPACT-GLASS-001

Date: 11 November 2026 (WIB)
Repository: `Frangga11/glass-tv-home`
PR: #134 — OPEN / DRAFT / UNMERGED
Branch: `feature/home-prayer-popup-compact-azans-001`
Base: PR #123 source checkpoint `6ac43ae4b56908233a64b55c23fdadee5ce7ab76`
Previous implementation HEAD validated by CI: `46430419d21fd0250c7443f75c3cabf9b3fe5754`\nLatest Silver Glass refinement commits: source `2b48fe49d932ba8340526cc12befa1b9799a9239`; contract tests `24c5d1c4b708ef2356d22eb28909d450a205cef4`
Status: Silver Glass refinement and source-host CI validation PASS on implementation HEAD `e0271a997548ebf0c8535a93d48edf9b5c90b77f`; rendered visual QA and physical-device acceptance PENDING.

## Owner constraints

- Work only on the Home Waktu Sholat popup.
- Do not change the existing Glass material, blur, veil, rim, transparency, radius, or global tokens.
- Do not change the overall Home screen, wallpaper, shelves, Dock/Grid, header, Weather/AQI, Wi-Fi, app inventory, navigation, or Settings UI.
- Do not change shared `popup_inputs.xml`, `item_input.xml`, `GlassDrawable.java`, or the shared `showMenu()` implementation.
- Preserve prayer calculations, schedule source, Azan preferences, alarm scheduling, sound assets, and existing test activity.
- One compact centered frame; no ScrollView and no internal scrolling.
- No merge, release, APK installation, ADB, or device/default-launcher changes without explicit authorization.

## Source audit

The previous Prayer popup inflated `popup_inputs.xml` and `item_input.xml`, which are also used by Inputs and application context menus. The existing `showMenu()` and `GlassDrawable` are shared. The Azan settings dialog already provides master enable, per-prayer toggles, five sound choices, sound fetch, and test activity.

The current implementation keeps the change local to the Prayer popup path and its dedicated overlay/helper methods in `MainActivity.java`. The shared Inputs and context-menu implementations remain present and are not replaced.

## Implementation

- Replaced the Prayer popup's use of shared input layouts with a dedicated in-memory view tree.
- Reused the existing `GlassDrawable` constructor parameters and `Backdrop`/Glider companion treatment.
- Centered a fixed-size panel (maximum 520dp × 370dp, constrained by safe insets); no ScrollView.\n- Refined the content hierarchy into a next-prayer strip, compact two-column timetable, and three clearly described Azan controls.\n- Used existing Silver Glass semantic text/section tokens and existing GlassDrawable material; no cyan accent token was introduced into the Prayer popup content.\n- Added explicit descriptions for master Azan, sound selection, and test audio; right-side labels indicate the OK action.\n- Added a compact DPAD instruction footer. The next-prayer highlight remains `TvUiTokens.ISLAMIC_GOLD`.
- Displayed the six existing `PrayerTimes.getFullSchedule()` entries; Terbit remains informational and is not an Azan toggle.
- Highlighted the existing `item.isNext` entry with `TvUiTokens.ISLAMIC_GOLD`.
- Reused existing `Azan` preferences and `Azan.schedule()` for master and per-prayer switches.
- Made all five existing sound choices available inline by cycling on OK; reuses `Azan.setSoundChoice()` and `Azan.fetch()`.
- Reused the existing `AzanActivity` test path and `LocationActivity` zone-selection entry point.
- Added a Prayer-popup-specific centered overlay/focus path; the shared `showMenu()` method remains unchanged.
- Added a source-contract regression test.

## Changed-file audit

Exactly three files are changed in PR #134:

1. `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
2. `tests/test_home_prayer_popup_compact_azans_contract.py`
3. `docs/project-management/GTVH-HOME-PRAYER-POPUP-COMPACT-GLASS-001.md`

## Validation — previous implementation HEAD\n\nThe prior implementation HEAD `46430419d21fd0250c7443f75c3cabf9b3fe5754` passed CI as recorded below. The latest Silver Glass refinement has not yet completed CI validation.\n\n## Validation — exact implementation HEAD

Validated commit: `46430419d21fd0250c7443f75c3cabf9b3fe5754`

- GitHub Actions CI run #2017 (`42908744475`): SUCCESS.
- GitHub Actions CI run #2018 (`42908750162`): SUCCESS.
- Latest run completed both `Launcher build and unit tests` and `Lists, tools and docs` jobs successfully.
- Repository Python regression suite: 285 tests run, 1 intentional skip, remaining tests OK.
- Android release build: `BUILD SUCCESSFUL`.
- Cumulative integration QA artifact uploaded by CI; it is a validation artifact, not a public release and has not been installed on a device.
- Release APK diagnostic reported 1,633,589 bytes (1,595 KB), above the historical 1.5 MiB reference; this is an informational warning, not a build failure. No size optimization is authorized within this popup-only task.
- Separate public APK release workflow was skipped.

## Acceptance still pending — no PASS claimed

1. Rendered visual review at 1920×1080: confirm centered compact frame, no clipping, no wasted blank area, no scroll, and no full-screen takeover.
2. DPAD acceptance: focus traverses all actionable rows; UP/DOWN boundary behavior; Back/close; focus returns to the original Home prayer anchor.
3. Function acceptance: master Azan, per-prayer switches, all five sound choices, test audio, and location/zone entry.
4. Regression acceptance: Inputs menu, app context menu, Settings, and all other Home surfaces remain unchanged.
5. Physical Mi TV Stick acceptance remains mandatory and has not been run.

No merge, public release, APK installation, ADB action, or device/default-launcher mutation has been performed. The latest Silver Glass refinement is not yet accepted; visual and physical-device QA remain pending.


## Validation update — Silver Glass refinement and exact tested HEAD

- Silver Glass refinement introduced the existing neutral/silver semantic palette used by the Jadwal Sholat popup; cyan accent is not used for Prayer popup content. Existing GlassDrawable material and constructor treatment remain unchanged.
- A stale contract assertion initially failed on CI run #2022 because it expected the previous schedule-index expression. The implementation was correct; the assertion was updated to check `scheduleIndex` and the informational Terbit mapping.
- Exact tested implementation HEAD: `e0271a997548ebf0c8535a93d48edf9b5c90b77f`.
- GitHub Actions CI run #2023 (`42912126612`): SUCCESS; both `Launcher build and unit tests` and `Lists, tools and docs` jobs SUCCESS.
- Repository Python regression suite: 287 tests run, 1 intentional skip, all remaining tests OK. All nine focused Prayer popup contract tests passed.
- Android release build: `BUILD SUCCESSFUL`.
- Release APK diagnostic: 1,633,977 bytes (1,595 KB), informational warning above historical 1.5 MiB reference; not a CI failure and no size optimization authorized in this scoped task.
- Cumulative integration QA artifact uploaded by CI (1,435,476 bytes); validation artifact only, not a public release and not installed.
- Public APK release workflow #441 was skipped.

## Remaining acceptance boundary

Source-host CI is PASS only. Rendered visual QA at 1920×1080, complete DPAD/focus behavior, audio playback, and physical Mi TV Stick acceptance remain PENDING / NOT RUN. No merge, public release, APK installation, ADB, or device/default-launcher changes have been performed.


## User device feedback and corrective pass — 11 November 2026

The owner supplied a Mi TV Stick / 1920×1080 photo of the first installed build and reported that the popup did not meet the visual reference and DPAD navigation did not work. This feedback supersedes the earlier assumption that the centered layout was visually acceptable.

### Required target
- Match the supplied reference hierarchy: framed capsule title/location header; prominent next-prayer strip; readable two-column timetable; three clearly separated Azan controls with descriptions and right-side current values/actions; compact footer.
- Position the popup at the screen's right-center safe area (not screen center).
- Increase text sizing for TV viewing while preserving the existing Silver Glass palette and GlassDrawable material.
- Make DPAD focus movement explicit and deterministic across nested rows; OK/Enter invokes the focused action; Back closes and restores the invoking Home anchor.
- Keep the task popup-only. No Home layout, wallpaper, shelf, Dock/Grid, header, weather, Wi-Fi, or shared menu/material changes.

### Corrective source changes
- Popup frame changed from centered to right-center with a 42dp right safe margin.
- Content typography and panel dimensions increased for TV readability.
- Overlay now collects focusable popup controls into a deterministic focus order and explicitly handles DPAD up/down/left/right, OK/Enter, and Back.
- Added a regression contract for right-center placement and explicit DPAD handling.
- Source commit: `15665557965ff70fff783b22ea996fba40ebf37b`.
- Contract test commit: `dd8e6a063a80f6b3d620296131b64e47e746b926`.

### Acceptance status
These are source-level corrections only. CI, release build, and the next installable APK must be verified before device retest. DPAD and visual acceptance remain PENDING until the owner tests the new APK on Mi TV Stick and supplies feedback. No merge or public release.


## Vertical reference redesign — 11 November 2026

The owner supplied a portrait reference and explicitly requested a slim vertical panel with dense, proportional spacing. The installed prior build was rejected; prior visual and DPAD assumptions are superseded.

### Source changes now staged on the PR branch (CI not yet accepted)

- Rebuilt only `showPrayerPopup()` as a vertical hierarchy: title/location header, next-prayer card, compact schedule heading, three two-column schedule rows, and three Azan action rows.
- Narrowed the panel to a maximum 430dp width and 500dp height, right-center aligned with a 30dp safe margin.
- Tightened row heights and inter-row margins to remove unnecessary blank bands while retaining readable TV typography.
- Preserved the current Silver Glass popup material and existing system semantic colors. The reference image controls shape and hierarchy, not its cyan palette.
- Schedule cells have clear focusable targets. Focus outline uses existing `TvUiTokens.FOCUS_BORDER`; selected next-prayer treatment retains existing Islamic gold.
- Replaced linear index-only navigation with nearest visual-direction candidate selection for DPAD up/down/left/right. OK/Enter invokes the focused action; Back/Escape closes.
- Fixed the invalid initial-focus reference by selecting the first actual focusable control from the explicit focus order.
- Existing Azan settings, sound selection, test activity, prayer schedule source, and shared popup/menu implementations remain the functional owners.

### Commits
- Vertical popup structure and card treatment: `b53b8627316c5fb50dfeb709911eb365df02576b`
- Direction-aware DPAD candidate mapping: `6a4843b483b74101784729330a3c02ca3a8d1f55`
- Focus/vertical layout regression contract: `c7a7b32854c8394ff9c8bdf139389db0ef55f35c`
- Compact spacing refinement: `254ddab5482f9866d706e5f5fb77daa0ee8c7d3c`
- Reverted two unrelated dialog spacing edits: `d79981f0a15432505dbe50da0ed491783e90b0f1`

### Current acceptance state
- Source changes are not yet CI-validated.
- Visual identity, exact density fit, focus outline rendering, DPAD traversal, Azan control operation, and physical Mi TV Stick behavior are NOT PASS until a fresh APK is tested on device.
- No merge, public release, installation, ADB, or changes to other Home surfaces.
