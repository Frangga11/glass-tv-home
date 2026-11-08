# GTVH-HOME-DAILY-AYAH-OFFLINE-RETRY-BACKOFF-001 — Audit & Implementation

**Date:** 9 November 2026  
**Status:** IMPLEMENTATION COMMITTED · EXACT-HEAD CI PASS · PHYSICAL ACCEPTANCE PENDING · NOT FINAL

## Source authority
- Repository: `Frangga11/glass-tv-home`
- PR: [#89](https://github.com/Frangga11/glass-tv-home/pull/89), OPEN / DRAFT / UNMERGED
- Branch: `fix/daily-ayah-offline-retry-backoff-001`
- Exact HEAD: `4038c08f4825f7bacf965bc6708505d3c8c30ebc`
- Base: PR #88 branch `fix/prayer-source-label-transparency-001`, exact base HEAD `a4b979540def5fc1414e66eec6df516a1ef22fe8`

## Verified finding
- `updateIslamicShelf()` is called on the active minute tick and invokes `updateAyah()`.
- When today's verse was not cached and the Quran.com request failed, the next minute tick could attempt another fetch. The previous verse remained visible, but network requests could repeat every minute.
- `ayahRunning` prevented overlapping workers only within one Activity instance; it did not persist a cooldown across Activity recreation or process restart.

## Changes
- Add a persisted **30-minute retry cooldown**, keyed by the exact WIB day index.
- Add an atomic process-wide single-flight guard in `WeeklyAyah.refresh()`.
- Add `shouldAttemptRefresh()` so Home minute ticks do not create a worker thread while the retry cooldown is active.
- A new WIB day can trigger an immediate first refresh even if the previous day's attempt was recent.
- Preserve the last valid cached verse and the freshness labels introduced by PR #87 when the request fails.
- No Quran verse list, API provider, translation IDs, prayer timetable, shelf geometry or focus behavior changed.

## Regression coverage
- Android unit tests: same-day cooldown, cooldown expiry, new-day reset and first attempt.
- Python contract verifies single-flight guard, persisted cooldown keys and MainActivity preflight.
- Exact-head CI #1649 / run `42525047258`: **SUCCESS**; both jobs passed.
- Python/docs suite: **202 tests, 1 skipped**.
- Android `testReleaseUnitTest` and `assembleRelease`: **PASS / BUILD SUCCESSFUL**.
- APK diagnostic: **1,626,745 bytes**. Publisher workflow skipped because PR #89 is Draft; no APK release asset was published.
- Publisher workflow #104 skipped because PR #89 is Draft; no APK release asset is claimed.

## Acceptance gates
1. Exact-head CI has passed Android `testReleaseUnitTest`, `assembleRelease` and APK diagnostics.
2. Physical offline test: cache yesterday's verse, disable network, keep Home open for more than 30 minutes; confirm there is no minute-by-minute request storm and the saved verse remains labelled `TERSIMPAN`.
3. Restore network after cooldown; confirm today's verse updates and freshness label changes to current.
4. Restart Activity/process during cooldown; confirm persisted cooldown still suppresses repeated requests.
5. Cross midnight; confirm the new WIB day is eligible for an immediate refresh.

## Decision
**BELUM FINAL.** Implementation and Python regression contracts are committed; Android CI and physical offline/online acceptance remain pending. Keep PR #89 OPEN / DRAFT / UNMERGED.
