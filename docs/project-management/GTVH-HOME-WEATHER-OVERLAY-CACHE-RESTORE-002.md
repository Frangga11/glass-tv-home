# GTVH-HOME-WEATHER-OVERLAY-CACHE-RESTORE-002 — Rebased onto DPAD cumulative candidate

## Integration baseline
- Repository: `Frangga11/glass-tv-home`
- Parent integration branch: `fix/home-dpad-origin-glide-movemode-shadow-003`
- Parent commit: `b91f93d0953d43d5157e96fe71d390440895882d`
- This branch reapplies the isolated weather cache fix from superseded PR #146 onto the current DPAD cumulative candidate.
- The old PR #146 remains a historical branch based on the older `bc6d224` snapshot and must not be merged directly.

## Objective
Restore the existing dynamic weather animation on the Home/Beranda wallpaper when the setting is ON, including after Android recreates MainActivity, without changing the existing renderer or weather refresh cadence.

## Diagnosis
The Home activity could retain a valid location/language-scoped `HomeWeather` cache while its newly created wallpaper overlay starts hidden. Previously, `updateHomeWeather()` returned immediately when `HomeWeather.due()` was false, so the valid cached weather state was not reapplied until the next network refresh interval.

## Change
- Keep the existing guards for missing weather view, disabled dynamic weather setting, and an already-running refresh.
- Read `HomeWeather.last(this)` and reapply it to the existing overlay before checking the refresh cadence.
- Keep `HomeWeather.due(this)` as the gate for network fetching only.
- Preserve the existing particle renderer, localized condition mapping, reduced-motion/low-spec handling, wallpaper layering, and independent setting.

## Regression coverage
Added `tests/test_home_weather_overlay_cache_restore_contract.py` to verify:
1. Cached overlay state is reapplied before the refresh-cadence check.
2. The 15-minute cadence still gates network fetch only.
3. Disabled setting, missing view, and in-flight refresh guards remain intact.

## Scope lock
No changes to Home layout, Dock, DPAD routing, glass, icon animation, Ayah, prayer timetable, popups, weather dashboard, or wallpaper selection.

## Validation gate
The new PR must pass exact-head CI/build and automated tests. Physical Mi TV Stick verification remains required for rain visibility after Home recreation, DPAD responsiveness, and frame stability. CI does not replace device acceptance.

## Integration decision
Keep this PR Draft and unmerged until the exact-head CI result and physical-device observations are recorded. The old PR #146 is not merged because its base snapshot is stale; this rebased PR is the only candidate for integration with the current DPAD stack.
