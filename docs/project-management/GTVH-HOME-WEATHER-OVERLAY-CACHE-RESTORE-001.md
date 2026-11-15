# GTVH-HOME-WEATHER-OVERLAY-CACHE-RESTORE-001

## Objective
Restore the existing dynamic weather animation on the Home/Beranda wallpaper when the weather toggle is ON, including after Android recreates the Home activity.

## Baseline
- Repository: `Frangga11/glass-tv-home`
- Parent checkpoint: PR #145 head `bc6d224ea9466d33317e2bb733c1424fb96cf309`
- Working branch: `fix/home-weather-overlay-cache-restore-001`
- Scope: isolated weather-overlay lifecycle/cache restoration only.

## Evidence and diagnosis
- The supplied TV settings photo shows **Animasi Cuaca pada Wallpaper** enabled.
- `MainActivity` already creates a transparent `WeatherBackgroundView` above the wallpaper and maps localized rain/storm text to its existing particle animation.
- `HomeWeather` retains a location- and language-scoped cached weather line for 15 minutes.
- Before this fix, `updateHomeWeather()` returned immediately when `HomeWeather.due()` was false. That meant a newly recreated `MainActivity` could keep a valid weather cache but never apply it to its newly created (initially GONE) wallpaper overlay until the next refresh interval. The setting could therefore be ON while no effect appeared on Beranda.

## Change
- `updateHomeWeather()` now reapplies `HomeWeather.last(this)` to the wallpaper overlay immediately whenever Home weather updates are requested and the feature is enabled.
- The 15-minute cadence remains the gate for network fetching only; it no longer gates cached visual restoration.
- Existing weather classification, particle rendering, reduced-motion handling, low-spec safeguards, wallpaper layering, and independent settings remain unchanged.

## Scope lock
- No Home layout, wallpaper selection, dock, navigation, glass, icon animation, prayer timetable, Ayah, popup, or weather dashboard redesign.
- No new weather renderer; preserve the existing `WeatherBackgroundView` effect.
- Do not merge unrelated open PRs.

## Validation status
- Source change committed; CI/build and Robolectric suite pending GitHub Actions.
- Physical Mi TV Stick verification is still required for visual parity, rain visibility, DPAD responsiveness, and frame stability.
- No APK or device PASS is claimed at this checkpoint.
