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
- Pre-F01 baseline finding: Appearance exposed one unified wallpaper entry and Quick Control opened the same control center. This was identified as a requirement mismatch and repaired in the F01 addendum below.
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


## F01 integration repair — 2 Nov 2026

- F01 found that both Quick Control > Wallpaper and Settings > Appearance > Wallpaper called the same compact `WallpaperSettingsDialog`. This contradicted the agreed UX boundary.
- Repaired the E08 feature branch `feature/e08-wallpaper-settings-redesign` (PR #41 remains Draft/open/unmerged):
  - Quick Control continues to open the compact three-action Change / Shuffle / Next control center.
  - Appearance again exposes the fuller three-card management route: Wallpaper collection, Wallpaper interval, and Personal wallpaper manager.
  - The Appearance actions call dedicated management entry points and keep the Settings surface as the return destination; they do not reopen the Quick Control popup.
  - Both routes reuse the same existing wallpaper categories, interval/shuffle preferences, custom-photo manager, QR upload, rotation and persistence owners. No duplicate wallpaper engine, preference key, or storage schema was added.
- Source commits: `6f93c9c8e1ef7ee02876255c2044c760eb3da028` (MainActivity routing) and `1d6fba799012ff0e8dbbfbce5a7fbe4f193e1d0d` (Appearance three-card UI).
- Exact updated E08 head: `1d6fba799012ff0e8dbbfbce5a7fbe4f193e1d0d`; CI runs #711 and #712 completed PASS. Integrated rehearsal PR #43 is running against current D07; its combined result is recorded in the F01 audit.
- Physical TV D-pad/focus/Back and 1080p/4K acceptance remain assigned to Project F; no APK installation or device acceptance is claimed.


## F01 final verification addendum — 2 Nov 2026
- The F01 repair restored the three full Appearance management cards and kept Quick Control's compact three-action dialog separate.
- Exact E08 source head `1d6fba799012ff0e8dbbfbce5a7fbe4f193e1d0d`: CI #711/#712 PASS.
- Final combined rehearsal PR #43 synthetic merge `21c2eab34a3356d37d079eba04b281cc2373386d`: CI #716 PASS; APK 1,563,769 bytes / 1,572,864-byte cap / 9,095-byte headroom; manual publication skipped.
- PR #43 closed without merge. Physical 1080p/4K and D-pad/Back acceptance remain F02.
