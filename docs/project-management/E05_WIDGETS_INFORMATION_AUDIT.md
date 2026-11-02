# E05 — Widgets & Information Center Audit

**Date:** 2 November 2026  
**Branch:** `feature/e05-information-center-ayah`  
**Base:** E04 exact head `08076bb945a169de198905152a092f419f48aa82`  
**Status:** Source audit and targeted correction committed; exact-head CI and Project F device acceptance pending.

## Scope audited

- Home Islamic shelf: daily ayah, translation, prayer timetable and placement.
- Weekly ayah data/cache and language selection.
- Prayer-time calculation, selected-location time zone and fallback calculation.
- Home weather/AQI cache, request freshness and API handling.
- Weather detail activity and Information Center / Quick Actions.

## Confirmed findings and disposition

### E05-F01 — Daily ayah and translation were explicitly truncated (FIXED)

The Home layout limited Arabic to one line and translation to two lines, both with end ellipsis. `MainActivity.showAyah()` repeated the one-line Arabic limit and ellipsis at runtime. It also reduced Arabic font size based on the full unwrapped string width rather than allowing proper line wrapping.

**Correction**
- Arabic and translation now wrap naturally with high-quality line breaking and no ellipsis or finite line cap.
- Arabic is explicitly RTL and retains font padding so Quranic marks are less likely to clip vertically.
- The content column increased from 38% to 46% of usable width.
- Arabic and translation typography were increased for TV-distance readability.
- The Islamic shelf is already positioned using its measured height above the dock; additional verse lines therefore move the shelf upward rather than overlaying the app grid.
- No verse text is shortened or modified by the display layer.

**Files:** `MainActivity.java`, `activity_main.xml`.

#### E05-F02 — Smart Boost Quick Action bypassed the full cache-clean workflow (FIXED)

The old action deleted only immediate children of the app cache directory, ignored nested directories and deletion results, invoked the periodic `maybeRun()` scheduler rather than the explicit clean-now operation, then always reported success. This did not match the button's advertised purpose.

**Correction**
- Routed the Quick Action into the existing confirmed `showCleanTvCacheDialog()` workflow.
- Reused `CacheCleaner.cleanNow()`, which handles internal/external app cache, temporary downloads, optional system cache trim and a truthful completion report.
- Removed the duplicate one-off cleanup implementation.
- Clarified Indonesian and English action labels to describe cache cleaning/storage optimization rather than promising RAM release.

**Files:** `MainActivity.java`, default/ID/EN `strings.xml`.

### E05-F03 — Prayer/Ayah detail dialog mixed RTL Arabic into one generic message (FIXED)

The Prayer & Ayah Quick Action used a single AlertDialog message for location, prayer summary, Arabic, translation and reference. Although the message was scrollable, Arabic had no dedicated RTL text surface or typography controls.

**Correction**
- Replaced the mixed message with a dedicated scrollable content layout.
- Separated context, Arabic, translation and reference into distinct text views.
- Arabic uses RTL direction, Quran font, full wrapping, no ellipsis, high-quality line breaking and font padding for Quranic marks.
- Translation is separately wrapped and scrollable; D-pad down at the end moves focus to Close.
- Dialog size increased within safe display bounds.

**File:** `MainActivity.java`.

## Existing behavior verified by source inspection

- WeeklyAyah cycles through a fixed 52-verse list, caches Arabic plus Malay, English and Indonesian translations, and returns the last cached verse when a network refresh is unavailable.
- Weather and US AQI are independently requested from Open-Meteo; the UI labels AQI as a model estimate, not official Indonesian ISPU station data.
- HomeWeather uses a 15-minute refresh interval and generation/location/language checks to reject stale responses. These D07 protections are preserved.
- PrayerTimes uses the selected location to determine the Indonesian time zone and has a calculation fallback when the configured source returns no usable times.
- Information Center already provides system summary and four Quick Actions: Smart Boost, Weather/AQI, Prayer/Ayah, and TV tips. The dialog action list is scrollable and its actions are focusable for remote navigation.
- No broad state, network, prayer calculation, storage, QR, or shared UI refactor was needed.

## Verification

- Source inspection and targeted diff review completed.
- Exact-head GitHub Actions CI: PENDING at report creation; must pass both launcher build/unit tests and lists/tools/docs.
- Physical 1920x1080 Mi TV Stick rendering, long-verse clipping, D-pad/Back/scroll behavior, and performance remain Project F acceptance items. They are deferred, not waived.
- No APK publication/installation, physical device test, feature merge, or release approval is claimed.

## Project F acceptance cases

1. Render the longest Arabic verse in the Home shelf and Prayer/Ayah dialog; verify every glyph, harakat and final word is visible.
2. Render the longest Indonesian and English translations; verify the final punctuation and reference remain visible.
3. Verify wrapping at 1920x1080 and a 4K display profile.
4. Verify prayer timetable remains visible and the shelf does not cover the dock/app grid.
5. Verify wallpaper hero content and Islamic shelf coexist without collision.
6. Verify language change, offline cache fallback, next-day refresh, and return from Weather/Prayer dialogs.
7. Verify D-pad focus, Back, scrolling and remote operation.
8. Verify Weather/AQI location/language changes cannot display stale responses.
9. Verify prayer times at WIB/WITA/WIT location zones and after date rollover.

## Merge/release policy

PR remains Draft/open/unmerged under D-013. Do not merge early. Final source closure requires exact-head CI PASS and all source-level findings closed. Physical acceptance remains with Project F; grouped A–G integration and release approval remain later gates.


## Weather/AQI source-data repair checkpoint — 2 Nov 2026

This section supersedes the earlier pending weather/AQI source-repair status above. Cross-project ownership remains defined by `CROSS_PROJECT_WEATHER_AQI_AUDIT.md`.

### E05 weather/AQI corrections implemented
- Removed fabricated PM2.5/PM10 calculations. Values are parsed only from the Open-Meteo Air Quality response fields `current.pm2_5` and `current.pm10`; missing values remain unavailable.
- Removed synthetic current weather, AQI, hourly/daily forecast values and hardcoded sunrise/sunset fallback. Missing data is represented as unavailable rather than a plausible-looking number.
- Weather and AQI successful-cache timestamps are independent. A failed source request does not overwrite that source's last successful JSON or freshness timestamp.
- Detailed weather requests use generation, selected-coordinate and language guards; superseded responses cannot commit.
- Weather and AQI cache records now retain their own selected coordinates. A location change cannot render the previous location's cache while the new request is pending or fails.
- Hourly/daily temperature rows render an unavailable marker instead of `-1°`; forecast range bars are hidden unless valid source min/max values exist. Empty condition icons are hidden. The automatic background uses a neutral cloudy visual when weather data is unavailable; weather text remains the API condition.
- AQI location is labelled as a model reference, not a physical monitoring station. Attribution identifies Open-Meteo/CAMS and clarifies that the US AQI is a model estimate, not official Indonesian ISPU measurement.
- WMO handling includes freezing drizzle/rain, snow/snow showers and the corresponding condition icons. The footer uses the API current timestamp, not render time.

### Regression and CI evidence
- D07 detailed-weather request freshness and E05 integration were tested on D07 PR #31 exact head `7ce92d30911288eed98aa2e7b36eaf9942d0c355`; CI run #642 PASS.
- E05 PR #38 exact head `6fe89fad078692958db67030329a6de66cd7ae71`; GitHub Actions run #663 PASS: launcher unit tests/release build/APK-size guard and Lists/tools/docs both succeeded. Three additional tests cover exact, changed and non-finite weather-cache coordinates.
- A duplicate localized resource key was detected by CI on an intermediate head and removed from default/ID/EN resource files. The final exact-head run #663 passed resource merge/build.
- APK publication step was skipped. No APK was installed and no physical TV test was performed.

### Remaining owner gates
- E05 source corrections are implemented and exact-head CI passed. Weather/AQI visual acceptance, missing/stale state legibility, source attribution, popup layout and remote navigation remain part of Project F's integrated 1080p/4K acceptance.
- B01/B02/B03 shared popup primitive audit remains owned by Project B; E05 must consume shared dialog primitives and must not fork global modal styling.
- PR #38 remains OPEN/DRAFT/UNMERGED under D-013. No grouped merge or release approval is claimed.


## E05 source-scope finalization and Project F handover — 2 Nov 2026

**Decision: E05 implementation/source scope CLOSED; physical acceptance transferred to Project F.** This is not a release or merge approval.

### Final source verification
- PR #38 head is exactly `6fe89fad078692958db67030329a6de66cd7ae71`; PR is OPEN, DRAFT, UNMERGED, with base `feature/e04-standby-screensaver`.
- Exact-head GitHub Actions run #663 (`41433527557`) completed SUCCESS. Both jobs passed: launcher unit tests/release build/APK-size diagnostics/size guard, and Lists/tools/docs. Manual APK publication was SKIPPED.
- PR file audit: 9 changed files (MainActivity, WeatherActivity, WeatherAirQuality, activity_main layout, default/ID/EN strings, HomeWeatherRequestTest and this audit report); 36 commits; no submitted review records. This is a scope snapshot, not proof of device acceptance.
- WeatherActivity popup audit: all four AlertDialogs use B01-owned `TvDialogStyle.apply(...)`; no competing E05 modal geometry/dimming implementation found. Shared popup styling remains B-owned.
- D07 detailed-weather freshness follow-up exact head `7ce92d30911288eed98aa2e7b36eaf9942d0c355`, CI run #642 PASS.

### What is closed
- E05 ayah preview/reader implementation and source-level integration.
- Weather/AQI data semantics, no-fabrication behavior, per-source cache success timestamps, generation/location/language guards, coordinate-bound cache, unavailable UI, WMO conditions and Open-Meteo/CAMS model attribution.
- Regression tests and exact-head CI for the current PR head.
- Source-level check that E05 consumes the shared B01 popup contract.

### What is explicitly transferred to Project F (not an E05 source blocker)
1. Physical Mi TV Stick 1920x1080 and 4K acceptance.
2. D-pad focus order, Back/Escape, scroll, long-verse wrapping, Arabic glyph/harakat integrity and repeated open/close.
3. Weather/AQI offline/stale behavior, location/language changes and missing-data legibility.
4. Popup dimensions, clipping, visual consistency, and interaction on actual TV remotes.
5. Home shelf, wallpaper, prayer timetable, dock and app-grid coexistence.

If F discovers a reproducible defect, return it to the owning source project (B for shared modal primitives, D for state/freshness, E05 for module-specific content/UI); do not reopen unrelated completed work or fork shared UI styling.

### Closure boundaries
- E05 is **source-complete and CI-verified**, not physically accepted and not release-ready.
- PR #38 remains Draft/open/unmerged under D-013. Do not merge this PR independently; grouped A–G merge and final release remain Project G gates.
- No APK publication/installation or physical device test is claimed.
- Preserve C07 upload resilience and all cross-project ownership decisions.
