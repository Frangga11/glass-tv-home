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

1. Render the longest Arabic verse in the configured cycle; verify every glyph, harakat and final word is visible.
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
