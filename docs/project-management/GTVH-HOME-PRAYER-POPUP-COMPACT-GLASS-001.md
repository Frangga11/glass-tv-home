# GTVH-HOME-PRAYER-POPUP-COMPACT-GLASS-001

Date: 11 November 2026 (WIB)
Repository: `Frangga11/glass-tv-home`
Branch: `feature/home-prayer-popup-compact-azans-001`
Base: PR #123 exact source checkpoint `6ac43ae4b56908233a64b55c23fdadee5ce7ab76`
Current implementation commit: `d720ca58f1bdde2c219de5c1f2f84504dae0f522`
Status: implementation submitted for CI/source QA; visual/device acceptance pending.

## Owner constraints

- Work only on the Home Waktu Sholat popup.
- Do not change the existing Glass material, blur, veil, rim, transparency, radius, or global tokens.
- Do not change the overall Home screen, wallpaper, shelves, Dock/Grid, header, Weather/AQI, Wi-Fi, app inventory, navigation, or Settings UI.
- Do not change shared `popup_inputs.xml`, `item_input.xml`, `GlassDrawable.java`, or the shared `showMenu()` implementation.
- Preserve prayer calculations, schedule source, Azan preferences, alarm scheduling, sound assets, and existing test activity.
- One compact centered frame; no ScrollView and no internal scrolling.

## Source audit

The previous Prayer popup inflated `popup_inputs.xml` and `item_input.xml`, which are also used by Inputs and application context menus. The existing `showMenu()` and `GlassDrawable` are shared. The Azan settings dialog already provides master enable, per-prayer toggles, five sound choices, sound fetch, and test activity.

## Implementation

Only the Prayer popup code path in `MainActivity.java` was changed.

- Replaced the Prayer popup's use of shared input layouts with a dedicated in-memory view tree.
- Reused the existing `GlassDrawable` constructor parameters and `Backdrop`/Glider companion treatment.
- Centered a fixed-size panel to avoid anchor-relative clipping and ScrollView.
- Displayed the six existing `PrayerTimes.getFullSchedule()` entries; Terbit remains informational and is not an Azan toggle.
- Highlighted the existing `item.isNext` entry with `TvUiTokens.ISLAMIC_GOLD`.
- Reused existing `Azan` preferences and `Azan.schedule()` for master and per-prayer switches.
- Made all five existing sound choices available inline by cycling on OK; reuses `Azan.setSoundChoice()` and `Azan.fetch()`.
- Reused the existing `AzanActivity` test path.
- Kept the existing location activity entry point.
- Added a Prayer-popup-specific centered overlay/focus path; the shared `showMenu()` method remains unchanged.

## Files intentionally untouched

- `launcher/app/src/main/res/layout/popup_inputs.xml`
- `launcher/app/src/main/res/layout/item_input.xml`
- `launcher/app/src/main/java/com/gathin/tvhome/GlassDrawable.java`
- `launcher/app/src/main/java/com/gathin/tvhome/PrayerTimes.java`
- `launcher/app/src/main/java/com/gathin/tvhome/Azan.java`
- Home layout XML, wallpaper, shelves, Dock/Grid, header, Settings, Weather/AQI, Wi-Fi, and app inventory.

## Validation gates

1. New source-contract test passes.
2. Existing Python regression suite passes.
3. Android unit tests and release build pass on exact PR HEAD.
4. Confirm changed-file list contains only the scoped MainActivity change, this test, and this checkpoint document.
5. Inspect rendered 1920x1080 popup: one frame, no scroll, no clipping, no wasted blank area, no full-screen takeover.
6. DPAD acceptance: focus moves through every actionable row, sound selection cycles, back/close restores focus to the original Home prayer anchor.
7. Regression acceptance: Inputs menu, app context menu, Settings, and all Home surfaces are unchanged.
8. Physical Mi TV Stick acceptance remains mandatory.

No merge, release, APK installation, ADB action, or whole-Home redesign is authorized by this task.
