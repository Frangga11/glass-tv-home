# E08 — Wallpaper Settings Redesign Audit

Date: 2 November 2026  
Status: SOURCE IMPLEMENTATION + EXACT-HEAD CI PASS; PHYSICAL ACCEPTANCE DEFERRED

## Trigger and objective

E08 is the final Stage E module. The user requested a unified wallpaper control surface opened from the Settings gear, with convenient access to:
1. Change Wallpaper.
2. Shuffle and its automatic interval.
3. Next Wallpaper.

This is an explicit exception to the no-duplicate-functions rule: the three actions are shortcuts in one control surface, not new independent wallpaper engines.

## Input and baseline

- Parent branch: `feature/e07-animation-effects`.
- Parent head: `a0f2b0fff6eef0c42852d21b971904d5acd29d5c`.
- E08 branch: `feature/e08-wallpaper-settings-redesign`.
- Source/test head: `b386986f08951c4dcf12ba8d72638b091a264803`.
- Draft PR #41 targets E07 and remains open/unmerged under D-013.

## Audit findings

Existing wallpaper behavior was already implemented:
- Category selection and bundled Default wallpaper.
- Natural, Skyscraper, Space, Ocean and Minimalist catalog categories.
- Custom-photo manager, QR upload, slideshow, deletion and safe stored-image filtering.
- Shuffle cycle persistence using `shuffle_cycle`, `shuffle_position`, and `shuffle_last_file`.
- Wallpaper interval persistence using `wallpaper_interval_minutes`.
- Manual Next using the existing asynchronous wallpaper decoder, index persistence and rotation timer.
- Wallpaper description visibility.

The user-facing entry points were fragmented:
- Appearance exposed separate collection, interval and custom-manager cards.
- Quick Control sent the wallpaper row directly to the interval dialog.
- The collection dialog mixed category selection with interval, shuffle and Next actions.
- E00 finding E00-ARCH-03 remains valid: state is distributed among existing preference stores. E08 intentionally maps and reuses these owners rather than attempting a risky broad migration.

## Ownership and boundaries

- E03 remains owner of wallpaper providers, metadata, cache and existing category selection/rotation behavior.
- C-series upload pipeline remains owner of QR/network/upload transport and validation.
- D-series persistence contracts remain authoritative; E08 introduces no new preference store or storage schema.
- E08 owns the unified wallpaper control presentation and entry-point routing.
- B01/B03 shared dialog material contract is reused through `TvDialogStyle`.
- F owns physical 1080p/4K and D-pad acceptance.
- G owns grouped integration and final shared-file reconciliation.

## Implementation

- Added `WallpaperSettingsDialog`, a single control center with three focusable tabs:
  - Change Wallpaper — routes to the existing collection/custom-photo flow.
  - Shuffle Interval — toggles the existing shuffle preference and opens the existing interval picker.
  - Next Wallpaper — invokes the existing Next operation.
- `MainActivity.showWallpaperDialog()` now opens the control center; the prior category chooser is retained as `showWallpaperCollectionDialog()`.
- Shuffle mode changes reset only the existing shuffle-cycle keys. They do not unexpectedly replace the currently displayed image or reset the existing timer.
- Next reuses the current wallpaper list, asynchronous decoder, index persistence and timer rescheduling. A one-image collection returns an explanatory message instead of pretending to advance.
- The interval option at zero is now labelled accurately as manual-only/no automatic change; it does not falsely claim to disable the separate shuffle-order preference.
- Appearance now has one unified wallpaper entry; the custom manager remains available from the collection chooser.
- Quick Control routes to the same control center.
- Existing QR upload, deletion, custom slideshow, category switching, description toggle, metadata, cache, network, timer and decoder paths are retained.
- Dialog uses the shared modal material contract and focusable controls; no new third-party UI dependency.

## Verification

Exact source/test head: `b386986f08951c4dcf12ba8d72638b091a264803`.

GitHub Actions CI run #693 / `41469544565`: SUCCESS.
- Launcher build and unit tests: SUCCESS.
- `testReleaseUnitTest`: SUCCESS.
- `assembleRelease`: SUCCESS.
- Lists, tools and docs: SUCCESS.
- APK size diagnostic and size guard: SUCCESS.
- Release APK: 1,565,397 bytes.
- Budget: 1,572,864 bytes.
- Remaining headroom: 7,467 bytes.
- APK publication skipped by workflow policy; no APK was installed.

## Quality check / residual acceptance

Source and CI gates pass. The APK remains below the cap, with limited remaining headroom; avoid unnecessary code/resource growth in later modules.

Project F must still verify:
- D-pad traversal across all three tabs, actions, Back and Close.
- 1920×1080 and 4K layout, text wrapping, focus visibility and dialog bounds.
- Glass Effect ON/OFF surfaces.
- Category changes, Default reselection, online sync success/failure and offline fallback.
- Shuffle ON/OFF without an immediate unwanted image change; no repeats within a cycle.
- Interval values, manual-only mode, timer rescheduling and Activity pause/resume.
- Next behavior for multi-image and single-image collections.
- Custom QR upload, collection manager, slideshow, delete-one/delete-all and empty collection fallback.
- Wallpaper/StandBy photo storage isolation and no unintended data deletion.
- Process restart persistence of active category, interval, shuffle and current index.

No physical TV, emulator or real-phone acceptance is claimed here. No merge, APK publication or installation occurred. D-013 remains active.

## Final disposition

E08 source implementation and exact-head CI: PASS.  
Project F physical acceptance: PENDING.  
Grouped integration / merge: DEFERRED until the full A–G sequence and explicit user approval.


## Final focus-restoration refinement and exact-head verification — 2 Nov 2026
- Follow-up audit found that toggling Shuffle rebuilt the status panel and could discard the remote's current focus target. The refreshed Shuffle action now explicitly restores focus to its own control, preserving D-pad continuity.
- Final source/test head: `dcec6afc805a9d7c78702ad8d51b20b87bd751a3`.
- Exact-head GitHub Actions CI run #695 / `41470304782`: **SUCCESS**. Launcher build/unit tests, `testReleaseUnitTest`, `assembleRelease`, APK-size diagnostic/guard, and Lists/tools/docs all passed.
- Final release APK diagnostic: 1,565,417 bytes; cap 1,572,864 bytes; headroom 7,447 bytes. APK publication skipped.
- The feature branch's later audit-report commit is documentation-only; no application source/resources/tests/build inputs changed after the verified source/test head. Project F acceptance remains pending.


## UI consistency and compact-popup audit — 2 Nov 2026

### Audit result
- The first E08 control center used a bespoke blue/transparent material and lengthy helper text, unlike the shared Settings card/subtab treatment.
- The category and interval pickers also used older white-on-focus row styling, two-line explanatory copy, and independently sized dialogs.
- The uploaded-photo manager and single-photo actions did not consistently use the shared modal viewport/surface contract.

### Corrections
- Reworked the main control center into a compact 680 × 420 dp maximum modal: one title, three short tabs (Change/Shuffle/Next), concise action labels, and no redundant instructional paragraphs.
- Reused B01-owned `TvUiTokens` for card surface, focused surface, border, selected state, typography and radius. Focus treatment now follows the same neutral TV Settings language as the shared card rows rather than introducing a new blue button system.
- Category and interval lists now use the same card surface/focus/selected tokens, compact single-line labels, and no long per-row descriptions. Current selection remains visibly marked.
- Category, interval, uploaded-photo manager, empty-manager, delete confirmation and single-photo action dialogs now apply the shared `TvDialogStyle` material, dimming, centering and viewport sizing after the existing `styleGlassDialog` typography/list treatment.
- The three primary actions still route to existing category selection, shuffle/interval preferences and Next rotation. No preference key, storage schema, timer engine, upload flow or wallpaper provider changed.
- Removed an unnecessary focus override that referenced a view with no generated ID; D-pad traversal remains based on the actual focusable view hierarchy. Shuffle re-render explicitly restores focus to its toggle.

### Final exact-head verification
- Final source/test head: `fcd7d8f23a4833830a401652ef269db3282b7300`.
- GitHub Actions run #702 / `41473357877`: **SUCCESS** (launcher unit tests/release build/APK guard and Lists/tools/docs).
- APK: 1,562,709 bytes; cap 1,572,864; headroom 10,155 bytes. APK publication skipped.
- No physical TV acceptance is claimed. Project F must verify popup appearance, D-pad/focus/Back, Glass ON/OFF and 1080p/4K, plus all existing wallpaper actions.
