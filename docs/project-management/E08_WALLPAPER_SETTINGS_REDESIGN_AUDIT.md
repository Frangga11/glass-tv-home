# E08 — Wallpaper Settings Redesign Audit

Date: 2 November 2026  
Status: SOURCE IMPLEMENTATION + EXACT-HEAD CI PASS; PHYSICAL ACCEPTANCE DEFERRED

## Objective
Unify wallpaper selection, shuffle/interval, and manual Next access from the Settings gear. This is an explicit exception to the no-duplicate-functions rule: the three tabs are shortcuts into existing behavior, not new wallpaper engines.

## Baseline
- Parent: `feature/e07-animation-effects` @ `a0f2b0fff6eef0c42852d21b971904d5acd29d5c`.
- Feature branch: `feature/e08-wallpaper-settings-redesign`.
- Source/test head: `b386986f08951c4dcf12ba8d72638b091a264803`.
- Draft PR #41 targets E07; open/unmerged under D-013.

## Audit findings
Existing code already owns category selection, bundled Default, online categories, custom-photo management, QR upload, slideshow, deletion, shuffle-cycle persistence, interval scheduling, Next rotation, metadata and description visibility.

The problem was fragmented access:
- Appearance exposed separate collection, interval and custom-manager cards.
- Quick Control opened the interval dialog directly.
- The collection dialog mixed category selection with interval, shuffle and Next actions.
- E00-ARCH-03 correctly noted that wallpaper state is distributed across existing preference stores. E08 reuses the current keys and owners; it does not attempt a broad persistence migration.

## Ownership
- E03 owns wallpaper providers, metadata, cache and category/rotation behavior.
- C-series owns QR/network/upload transport and validation.
- D-series persistence contracts remain authoritative.
- E08 owns the unified control presentation and entry-point routing.
- B01/B03 shared modal styling is reused through `TvDialogStyle`.
- F owns physical 1080p/4K and D-pad acceptance; G owns grouped integration/reconciliation.

## Implementation
- Added `WallpaperSettingsDialog` with three D-pad-operable tabs: Change Wallpaper, Shuffle Interval, Next Wallpaper.
- Change Wallpaper reuses the existing category/custom-photo flow.
- Shuffle reuses the existing `shuffle_mode`, `shuffle_cycle`, `shuffle_position`, and `shuffle_last_file` preferences, plus the existing interval picker. Toggling shuffle does not unexpectedly replace the displayed image.
- Next reuses the existing asynchronous decoder, current wallpaper list, index persistence and timer rescheduling. A one-image collection returns a clear message rather than pretending to advance.
- The zero-minute interval is labelled accurately as manual-only/no automatic change; it does not falsely claim to disable shuffle order.
- Appearance exposes one unified wallpaper entry; Quick Control opens the same control center. Custom-photo manager remains reachable through collection selection.
- Existing QR upload, deletion, slideshow, category, description, metadata, cache, network and rotation behavior are preserved.
- No new engine, preference store/key, storage schema, network route, timer implementation or UI dependency was added.

## Exact-head verification
Source/test head: `b386986f08951c4dcf12ba8d72638b091a264803`.

GitHub Actions CI run #693 / `41469544565`: **SUCCESS**.
- Launcher build and unit tests: SUCCESS.
- `testReleaseUnitTest` and `assembleRelease`: SUCCESS.
- Lists/tools/docs: SUCCESS.
- APK diagnostic and size guard: SUCCESS.
- Release APK: 1,565,397 bytes; budget 1,572,864 bytes; headroom 7,467 bytes.
- APK publication skipped; no APK installed.

## Project F acceptance
Still required:
- D-pad traversal across tabs/actions, Back and Close.
- 1920×1080 and 4K layout, focus visibility, clipping and text wrapping.
- Glass ON/OFF surface behavior.
- Category selection, Default reselection, online sync success/failure and offline fallback.
- Shuffle ON/OFF, no repeats within a cycle, interval values and manual-only mode.
- Next behavior for multi-image and one-image collections.
- Pause/resume, process restart persistence, custom QR upload, slideshow, deletion and empty collection fallback.
- Wallpaper/StandBy storage isolation and no unintended deletion.

No physical TV, emulator or real-phone acceptance is claimed. No merge, APK publication or installation occurred. D-013 remains active.

## Disposition
E08 source implementation and exact-head CI: **PASS**.  
Project F physical acceptance: **PENDING**.  
Grouped integration/merge: **DEFERRED** pending A–G completion and explicit user approval.
