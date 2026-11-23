# GTVH — Standby Family Dashboard 001

**Date:** 23 November 2026  
**Status:** Isolated implementation prepared; CI and device validation pending.

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
- Preserve the existing user-uploaded StandBy photo overlay and rotation through `StandbyPhotoStore`.
- Do not modify Home, Dock, navigation, DPAD/focus, Glider, Quick Settings, Wi-Fi, popup geometry, prayer engine, weather engine, or storage schemas.
- No new network endpoint, preference store, or duplicate timetable implementation.

## Files in scope

- `ClockDream.java`
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

No APK installation or physical Mi TV Stick acceptance is claimed by this checkpoint.
