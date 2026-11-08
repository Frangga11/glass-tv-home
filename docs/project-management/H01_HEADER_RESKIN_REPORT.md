# H01 — Header Reskin Implementation Report

**Status:** Cross-audit and final CI PASS; emulator/physical visual regression pending  
**Branch:** `feature/h01-header-reskin`  
**Base:** `main` at `23598f8b5202ed71fc38e8c8da5ed2eeaeac535b`  
**PR:** [#78](https://github.com/Frangga11/glass-tv-home/pull/78) — draft, not merged  
**Scope:** Header only. No changes to heroShelf, islamicShelf, grid/dock, wallpaper, WeatherActivity, HomeWeather, or PrayerTimes.

## Baseline

GitHub Actions run #1458 (`42454015758`) completed successfully against unchanged application source:
- `testReleaseUnitTest` + `assembleRelease`: PASS
- Launcher size check: PASS
- Release APK artifact: published

The source was read through the GitHub API and built on GitHub-hosted runners; a local repository ZIP was not required.

## Implementation

- Repositioned the existing clock/date block and added its vertical separator.
- Added a focusable location pill sourced from `Location.chosen()`; it opens the existing `LocationActivity` through `showPrayerZoneDialog()`. Pagar Alam altitude is configured as 800 m; other locations without altitude omit the altitude section.
- Repositioned the existing `weatherAirQuality` TextView below the location pill; kept its ID, click/key/focus listeners and existing data providers. Header formatting adds temperature/AQI emphasis and a rounded category badge only.
- Added the Home tab as a position marker only. Its selected state is derived from Activity lifecycle plus screensaver/wallpaper-fullscreen flags (not window focus, so dialogs do not deactivate it); Home click and remote Home share `returnToMainScreen()`.
- Moved the existing Profile and Settings views into the centered navigation capsule without changing their IDs or their click/long-click handlers.
- Kept `btnInputs` as the first child of `topButtons`. Its XML element was verified byte-for-byte identical to the baseline; its style, text, icon, size and click action are unchanged.
- Kept Wi-Fi and Assistant as right-side controls. Existing Wi-Fi update, visibility and long-click behavior remain attached to the same view.
- Added header-only glass/nav drawables and vector icons. Existing `weather_pill_bg.xml` and `styles.xml` were not modified.

## Changed files

1. `launcher/app/src/main/res/layout/activity_main.xml`
2. `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
3. `launcher/app/src/main/java/com/gathin/tvhome/Location.java`
4. `launcher/app/src/main/res/drawable/header_glass_pill_bg.xml`
5. `launcher/app/src/main/res/drawable/header_nav_tab_bg.xml`
6. `launcher/app/src/main/res/drawable/ic_header_home.xml`
7. `launcher/app/src/main/res/drawable/ic_header_location.xml`
8. `launcher/app/src/main/res/drawable/ic_header_mountain.xml`

## Function preservation checks

The following method bodies were compared with `main` and are unchanged:
- `showCandyBoxPopup()`
- `showQuickControlPopup()`
- `showWifiPopup()`
- `showInputs()`
- `triggerGoogleAssistant()`

The original `btnInputs` XML element is identical to baseline. The same view ID and existing click listener remain. D-pad preserves the established order: Home → Profile → Settings → Inputs → Wi-Fi → Assistant. Location ↓ Weather and Weather ↑ Location; Right from either left-side pill enters Home. Weather ↑ returns to Location; Weather ↓ returns to the grid/now-playing target.

## Layout values

- Safe side inset: existing 56dp `mainContent` padding retained.
- Clock: existing 56sp.
- Clock/date separator: 1dp × 38dp.
- Location pill: 30dp high, 26.5% of screen width.
- Weather/AQI pill: 32dp high, same dynamic width as location pill (26.5%).
- Navigation capsule: 32dp high, target 39% of screen width; shrinks symmetrically when measured left/right groups need more room.
- Wi-Fi: 82dp × 30dp.
- Assistant: 32dp × 32dp; 10dp gap from Wi-Fi.
- Home/Profile/Settings tabs share equal widths inside the centered capsule.
- Right group remains inside the existing 56dp safe inset.

## Verification limits

- Final implementation commit `21a4238189a1505b1303d6e6bb455c4c65abe49d` passed GitHub Actions run #1486 (`42455389359`): `testReleaseUnitTest` + `assembleRelease`, launcher size check, and release APK publication all PASS.
- Previous header build #1496 passed before restoring the owner-confirmed tab order. A new build is required for the corrected final source; do not use the earlier APK as the final order-corrected test build.
- No new Java/compiler warnings were introduced; the deprecation notices in the implementation run match the baseline run.
- No Android emulator screenshot/layout test has yet been run at 1280×720, 1920×1080 or 3840×2160.
- No physical-TV regression test has yet been run. CI success does not prove remote navigation, popup anchoring, focus rendering or overscan behavior on hardware.
- Cross-check of `LocationActivity` confirms that selecting a zone updates both `Location` and `AppLocationManager`, clears the `HomeWeather` cache, and reschedules Azan. The location label and weather/AQI source therefore follow the same selected zone; no provider ownership changes were required.
- Do not merge until the latest CI passes and the three-resolution visual/focus checks are completed or explicitly accepted as deferred.

## Cross-audit against untouched Home Screen regions

- XML subtrees are byte-for-byte identical to `main` for `heroShelf`, `islamicShelf`, `ayahBlock`, `prayerHeader`, all five prayer-time fields, `gridWrap`, `grid`, `glass`, and `nowPlayingCard`.
- Java method bodies are byte-for-byte identical for `setHeroText()`, `showHero()`, `hideHero()`, `updatePrayerTimes()`, `updateIslamicShelf()`, `parkDock()`, `updateTopFade()`, `setWallpaper()`, `showNextWallpaper()`, and the five preserved popup/action functions listed above.
- The existing `btnInputs` XML element remains identical to baseline and is still the first child of `topButtons`.
- Final source-level recheck confirms no duplicate XML view IDs and all new resource references resolve in the successful Android resource/build pipeline.
- The dock is parked using the existing bottom-anchored `parkDock()` calculation. Its method and `DockDecoration` implementation are unchanged; increasing the header stack does not alter the dock's bottom anchor.
- The only intended header layout growth is the added location pill above the existing weather pill. No title/description, ayah, prayer schedule, wallpaper, grid, dock, or glass geometry source code was modified.
- The approved navigation order is the original, owner-confirmed **Beranda → Profil → Pengaturan**, with Beranda first and active. Do not reorder these tabs. The location/weather D-pad sequence remains spatial: Down from Location reaches Weather, Up from Weather returns to Location, and Right from either left-side pill reaches Beranda. Header remeasurement listens to root, left-group, and right-group layout changes so changes in date/Inputs visibility cannot leave the centered capsule stale.

## Integration safety

PR #78 remains draft and unmerged. No merge to `main` has been performed. All implementation commits are isolated on `feature/h01-header-reskin`.
