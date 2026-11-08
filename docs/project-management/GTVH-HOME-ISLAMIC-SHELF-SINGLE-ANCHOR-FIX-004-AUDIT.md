# GTVH-HOME-ISLAMIC-SHELF-SINGLE-ANCHOR-FIX-004 — Audit & Implementation Record

**Date:** 9 November 2026  
**Status:** SOURCE FIX COMMITTED · EXACT-HEAD CI IN PROGRESS · PHYSICAL ACCEPTANCE PENDING · NOT FINAL

## Source authority
- Repository: `Frangga11/glass-tv-home`
- PR: [#84](https://github.com/Frangga11/glass-tv-home/pull/84), OPEN / DRAFT / UNMERGED
- Branch: `fix/home-islamic-shelf-single-anchor-004`
- Exact current HEAD: `d821842c2211cb6e65bbde506253cde7b3929771`
- Base: PR #83 branch `fix/home-ui-proportion-glass-focus-refinement-003`
- Exact base HEAD: `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`

## Root cause confirmed in source
`MainActivity` had two layout-change listeners capable of writing the vertical position of the same Islamic Shelf:
1. The retained listener delegates to `applyIslamicShelfPosition()`, which sets Y from the measured `islamicShelfBottomAnchor`.
2. A second legacy listener set Y from `heroBottom - islamicShelf.getHeight()`.

In horizontal Dock mode, `heroBottom` is deliberately offset from `islamicShelfBottomAnchor` by `ISLAMIC_SHELF_BOTTOM_OFFSET_DP` (10dp). Therefore the two callbacks did not express the same baseline. When content remeasurement and Dock focus callbacks interleaved, the legacy callback could overwrite the stable anchor position. This is the source-level explanation for the reported vertical jump; physical reproduction remains unverified.

## Changes committed
- Removed only the legacy `islamicShelf` layout listener using `heroBottom`.
- Kept the single content-layout listener that calls `applyIslamicShelfPosition()`.
- Kept `showHero()` applying the same anchor.
- Kept the existing measured Dock tray edge and 27.5dp shelf-to-tray gap unchanged.
- Added a regression contract requiring exactly one `islamicShelf.addOnLayoutChangeListener`, requiring that listener to delegate to the anchor, and forbidding `heroBottom` in that listener.
- Preserved `heroBottom` use for `heroShelf`; wallpaper shelf geometry was not removed or altered.

## Scope intentionally unchanged
No changes to Dock geometry, Dock focus/navigation, wallpaper safe frame, prayer timetable calculation or source, WeeklyAyah/Quran data, translations, Islamic Shelf typography, Glider, or glass styling.

## Validation
- GitHub Actions CI run #1639 / run ID `42523095371` was observed **IN_PROGRESS** at checkpoint time.
- Exact-head CI result is pending; do not report tests or Release build as passed until GitHub reports success.
- No physical Mi TV Stick install, ADB measurement, screenshot review, or remote-control test was performed.

## Acceptance checklist
1. CI exact HEAD passes, including the new Python regression contract and Android Release unit/build jobs.
2. On Mi TV Stick at 1920×1080 / density 320, verify the Islamic Shelf bottom edge remains fixed while focus moves across every Dock icon repeatedly.
3. Verify ayah visibility changes, text remeasurement, and minute refresh do not move the shelf's bottom anchor.
4. Verify measured clearance remains 27.5dp (55px at density 2.0).
5. Verify wallpaper hero shelf retains its existing independent `heroBottom` behavior.
6. Verify horizontal and vertical Dock modes, Dock capacities 4/5/6, return-to-Home, and rapid DPAD sequences.
7. Keep PR #84 OPEN / DRAFT / UNMERGED until physical acceptance is recorded.

## Decision
The duplicate vertical-position writer has been removed in source and guarded by a regression contract. This is an implementation checkpoint, not final device acceptance. Next: confirm exact-head CI, then proceed to Mi TV Stick physical acceptance before moving on to prayer-data refresh and ayah cache-status work.
