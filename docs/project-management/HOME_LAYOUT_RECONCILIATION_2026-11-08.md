# Home Layout Reconciliation — 8 November 2026

## Status
Implementation in progress on `fix/home-balanced-frame-001`, stacked on PR #77 (`fix/home-layout-consolidated-001`). PR #79 is draft and must not be merged before the dependency chain and owner device verification.

## Owner-approved constraints
- Header navigation order: **Beranda → Profil → Pengaturan**. Do not reorder.
- Preserve existing Home shelves, wallpaper metadata, daily ayah, prayer schedule, weather/AQI feature, app inventory, media card, Dock glass, profile/settings actions, and the G3A/G3B/G4 foundations.
- Change only the reported defects and the explicitly requested layout balance.
- Horizontal Dock rests on its first row. App-grid overflow is hidden at rest and revealed by DPAD Down; returning focus to a Dock item collapses overflow.

## Cross-audit findings
1. PR #78 / CI #1500 was built from the old `main` baseline and did not include the newer G3A–G4 / PR #77 Home layout chain. Its APK is not a valid candidate for this correction.
2. PR #79 is based on the consolidated PR #77 branch, not old `main`.
3. The default wallpaper asset `launcher/app/src/main/assets/kabut-pagi-dempo.jpg` exists in the current source tree (230,997 bytes). Startup and category selection both stage this asset into app-private storage before applying it.
4. Header navigation is now a dedicated centered pill with owner-approved order. Width is computed between measured clock/date content and the right-side controls to avoid overlap.
5. Location and weather/AQI controls share the same left content edge. Pagar Alam has a source entry with 800 m altitude metadata.
6. Home was previously rendering a separate lightweight `HomeWeather` cache before the full `WeatherAirQuality` cache. That allowed the Home line to show temperature only even when the G3B dashboard cache contained AQI. The Home line now reads the same location-scoped G3B formatter/cache as the Weather dashboard; unavailable AQI is explicit, not silently omitted.
7. A single 34dp content safe frame is applied to top/left/right and Dock tray bottom. At the tested 1920×1080 / density 320 device mapping (2 px per dp), reducing the earlier 48dp frame to 34dp moves the Dock tray down by 28 px (approximately 1 cm) while keeping all four edge margins equal.
8. The row-2 requirement is covered by PR #77's existing overflow behavior: grid hidden at rest, exposed only on DPAD Down, and collapsed when focus returns to a Dock tile.

## Files changed in PR #79
- `activity_main.xml`: centered navigation, location pill, shared 34dp top/side inset, and weather pill anchored below location.
- `MainActivity.java`: header labels/measurement, focus restoration and explicit D-pad routes, equal-frame shelf placement, and one shared weather/AQI display source.
- `DockGeometry.java`: 34dp shared horizontal/vertical safe frame.
- `Location.java`: optional altitude field and Pagar Alam = 800 m.
- `NavigationState.java`: stable focus keys for location, weather and Home tab.
- `HomeHeaderGeometry.java` plus JVM tests: measured center navigation geometry.
- `DockGeometryTest.java`: equal 34dp safe-frame contract.
- Header vector/background drawables and CI artifact publishing for PR #79.

## Validation
- CI #1513 (pre-34dp final geometry): release build, unit tests, docs checks and artifact publication passed. APK exact size: 1,627,969 bytes.
- CI #1514/#1515: weather source consolidation and width adjustment passed.
- CI #1516–#1519: final 34dp geometry commits are running; do not treat them as final until exact-head CI completes.
- CI success is source/build validation only. It does not replace visual emulator inspection or owner testing on the TV.
- Do not use the previous PR #78 APK for device verification.
- No merge authorized.
