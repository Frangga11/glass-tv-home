# GTVH — Standby Family Dashboard 001

**Date:** 23 November 2026  
**Status:** Standby code and regression validation passed on CI run #2887; original wallpaper asset import and physical-device visual QA remain pending.

## Objective

Replace the black OLED nightstand-only standby presentation with the owner-approved family wallpaper and a compact Frosted Silver-Cyan dashboard. Match the widget proportions of the supplied TV reference and keep every item centered inside the panel.

## Baseline and branch

- Repository: `Frangga11/glass-tv-home`
- Parent workstream: PR #181, `fix/home-islamic-shelf-startup-anchor-001`
- Exact parent head: `74bdf3dbd5667e6f85d03194fdf7c8a4039841a2`
- New isolated branch: `feature/standby-family-dashboard-001`
- PR #181 remains unchanged and unmerged.

## Approved visual structure

- Compact left-side panel, vertically centered, 280dp target width with responsive narrow-screen bound.
- All panel content centered.
- Next-prayer summary, Gregorian date, retained Hijri date, and dominant digital clock.
- Weather and AQI in two balanced side-by-side metric surfaces.
- Five equal prayer columns: Subuh/Fajr, Dzuhur/Dhuhr, Ashar/Asr, Maghrib, Isya/Isha.
- Translucent silver-blue glass, restrained white edge, and cyan separators.

## Implementation boundaries

- Reuse `PrayerTimes` for next prayer, Hijri date, and today's prayer times.
- Reuse `WeatherAirQuality` for weather/AQI cache and its existing refresh lifecycle.
- Share `StandbyDashboardBinder` between `ClockDream` and the `MainActivity` standby preview so both entry paths render identical metrics.
- Preserve the existing user-uploaded StandBy photo overlay and rotation through `StandbyPhotoStore`.
- Do not modify Home, Dock, navigation, DPAD/focus, Glider, Quick Settings, Wi-Fi, popup geometry, prayer engine, weather engine, or storage schemas.
- No new network endpoint, preference store, or duplicate timetable implementation.

## Files in scope

- `ClockDream.java`
- `StandbyDashboardBinder.java`
- The focused `MainActivity.updateNightstandScreensaver()` path
- `view_nightstand.xml`
- Standby-only drawable and localized string resources.
- Standby layout/contract tests.
- This checkpoint.

## Asset note

The original 16:9 family image remains the approved wallpaper source. The current repository patch uses a temporary Frosted Silver-blue drawable fallback under the final resource name so the layout can be compiled and tested without changing the image pipeline. The original image binary still needs to be imported into `drawable-nodpi/standby_family_wallpaper.webp` before visual acceptance; the fallback must then be replaced, not layered with a second widget baked into the image. The current patch does not claim the family wallpaper is bundled.

## Validation gates

1. Source review for lifecycle, data ownership, image overlay, and layout bounds.
2. Python contract suite.
3. Android `testReleaseUnitTest` and `assembleRelease`.
4. Exact-head CI on the PR head.
5. Visual QA at 1920×1080 on the target Mi TV Stick; verify panel proportion/readability, weather/AQI cache fallback, prayer schedule updates, custom photo overlay, wake behavior, and OLED drift.
6. Record device acceptance separately; CI is not physical-device proof.

## Validation record

- CI run #2887: **SUCCESS** — Launcher build and unit tests, Lists/tools/docs.
- CI run #2886: **SUCCESS** — duplicate validation run on the same tested code head.
- Release build and unit-test tasks completed successfully; no claim of Mi TV Stick visual acceptance.
- The current wallpaper resource is still a temporary Silver-Cyan fallback. Import the exact owner-approved family image before visual acceptance and before treating this as final.

No APK installation or physical Mi TV Stick acceptance is claimed by this checkpoint.


## Layar Siaga settings completion work — PR #184

- Added four settings subtabs: **Umum**, **Foto**, **Tata Letak**, and **Sinkronisasi**.
- **Umum:** enable/disable the launcher's 15-minute idle presentation and show the agreed 15/30/60-minute timeline.
- **Foto:** QR upload is the user-facing intake route; the existing local upload server and StandbyPhotoStore remain the storage owners. No photo picker or photo-library manager is exposed in this tab.
- **Tata Letak:** horizontal position (left/center/right), vertical position (top/center/bottom), and panel corner shape (soft/rounded/square) are persisted and applied to the standby panel. Frosted Silver-Cyan gradient and border are preserved.
- **Sinkronisasi:** provides the Android screensaver settings entry point and documents that Google Backdrop and Android power timing remain system-owned.
- The idle coordinator now respects the enable switch and is rescheduled when that setting changes.
- Added tests/test_standby_settings_subtabs_contract.py for source-level integration contracts.

**Validation status:** source-level implementation committed on the isolated completion branch. CI/build and physical TV validation have not yet been recorded for PR #184. Do not treat this as accepted or release-ready until both code validation and the separate Mi TV Stick checks are complete.


## UI and DPAD refinement — follow-up

- Replaced oversized filled subtab chips with a compact 34dp text-tab strip, equal-width alignment, restrained active underline, and consistent Silver-Cyan focus treatment.
- Added explicit horizontal DPAD movement between the four subtabs, DPAD DOWN entry into the first actionable control, and DPAD UP return to the selected subtab.
- Added deterministic vertical DPAD order for every focusable action in the active Layar Siaga subtab. The control list is rebuilt with the active subtab, and focus is restored to the selected tab after switching.
- Invalid persisted subtab indexes fall back to **Umum**.
- Added source-contract checks for compact tab dimensions and DPAD routing.
- Awaiting CI for the final refinement commit and physical Mi TV Stick verification; no merge or release is implied.
