# GTVH-HOME-PRAYER-TIMES-REFRESH-AND-NEXT-SUBUH-FIX-001 — Audit & Implementation

**Date:** 9 November 2026  
**Status:** IMPLEMENTATION COMMITTED · EXACT-HEAD CI PASS · PHYSICAL ACCEPTANCE PENDING · NOT FINAL

## Source authority
- Repository: `Frangga11/glass-tv-home`
- PR: [#86](https://github.com/Frangga11/glass-tv-home/pull/86), OPEN / DRAFT / UNMERGED
- Branch: `fix/prayer-times-refresh-next-subuh-001`
- Exact current HEAD at this audit: `746beaba2648b167dc174dfd3a61f5e859ed9fc2`
- Base: PR #85 branch `fix/home-islamic-shelf-arabic-typography-001`
- Exact base HEAD: `a5eaae843f8998c53238870609102e4c20b05ded`
- Branch comparison: 5 commits ahead / 0 behind; merge base is the exact PR #85 HEAD.

## Trigger and verified source findings
1. `PrayerTimes.syncIfDue()` was called during Home setup, but the active minute tick did not re-check due status. A Home process left running across month/year rollover could keep relying on the prior cached annual timetable until a later restart.
2. `getPrayerItems()` and `getFullSchedule()` selected index 0 (today's Subuh) after all prayers had passed. The compact summary already resolved tomorrow's Subuh, so views could disagree: a past timestamp was marked as the next prayer.
3. A stale cache plus repeated due checks during network failure could launch repeated background sync threads. This could cause avoidable work and repeated connection attempts.

## Changes committed
- Re-check `PrayerTimes.syncIfDue()` during Home `onResume()` and active `minuteTick`; existing cache validation by active zone, year and month remains the source of truth.
- Add an atomic single-flight guard so only one prayer sync thread runs at a time.
- Add a six-hour retry cooldown keyed by active zone and current year/month. This prevents retry storms without delaying a new location/month's first attempt. Explicit forced sync bypasses cooldown.
- Centralize next-prayer selection in `findNextPrayerIndex()`, consistently skipping sunrise and returning -1 after Isya.
- Add a shared tomorrow lookup and `nextSubuhTime()` fallback.
- After Isya, the Subuh row in `getPrayerItems()`, compact Home text and `getFullSchedule()` now displays tomorrow's Subuh and is marked upcoming rather than elapsed.
- Preserve existing provider, annual Kemenag JSON validation, location/timezone logic, astronomical fallback parameters, layout geometry and Arabic styling.

## Regression coverage
- Android `PrayerTimesNextPrayerTest`: next prayer before/after times, sunrise skip, after-Isya no-same-day result, tomorrow Subuh and fallback timestamp.
- Python `test_prayer_times_refresh_next_subuh_contract.py`: single-flight/cooldown contract, Home resume/minute refresh, shared next-prayer selection and tomorrow Subuh row.
- Exact-head CI #1642 / run `42524091486`: **SUCCESS**; both jobs passed.
- Python suite: **192 tests, 1 skipped**; all four new prayer refresh/next-Subuh contracts passed.
- Android `testReleaseUnitTest` and `assembleRelease`: **PASS / BUILD SUCCESSFUL**.
- Release APK diagnostic: **1,625,865 bytes**. Publisher workflow #97 skipped because PR #86 is Draft; no APK release asset is claimed.
- Release APK publisher workflow #97 / `42524091555`: skipped because PR #86 is Draft; not a CI failure and no APK release asset is claimed.

## Quality and acceptance gates
1. Exact-head CI #1642 has passed, including Android `testReleaseUnitTest`, `assembleRelease` and APK diagnostics.
2. Confirm the CI logs show the new Java tests and Python contracts actually ran.
3. Physical Mi TV Stick acceptance at 1920×1080 / density 320:
   - Home remains open across a month/year boundary and the due-check path refreshes the active annual timetable.
   - Offline failure does not spawn repeated syncs every minute; retry occurs after the scoped cooldown or explicit force.
   - After Isya, the Subuh row shows tomorrow's time and is highlighted as upcoming, not past.
   - Compact Home text, Islamic Shelf rows and full schedule agree.
   - Verify before Subuh, after Subuh, around sunrise, around Maghrib and after Isya.
4. Do not claim authoritative schedule accuracy from source metadata alone; independently verify the live annual mirror and compare sample dates against Kemenag data before final acceptance.

## Decision
**BELUM FINAL.** Source changes and regression tests are committed. CI and physical device acceptance remain gates. Keep PR #86 OPEN / DRAFT / UNMERGED; no merge or device test has been performed.
