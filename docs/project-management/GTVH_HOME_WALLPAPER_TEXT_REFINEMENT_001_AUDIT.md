# GTVH-HOME-WALLPAPER-TEXT-REFINEMENT-001 — Source Audit & Implementation Record

**Date:** 8 November 2026  
**Repository:** `Frangga11/glass-tv-home`  
**PR:** [#80](https://github.com/Frangga11/glass-tv-home/pull/80) — OPEN / DRAFT / UNMERGED  
**Branch:** `fix/home-wallpaper-text-refinement-001`  
**HEAD:** `c7d95c3de24bc70b671159d5aa9ef49451cbe112`  
**Base:** `fix/home-balanced-frame-001` at `b4abdf0bd555a0106c135add20c3f287cf10b412` (PR #79)

## 1. Trigger and scope

Owner requested a localized Home visual refinement. Only five visual areas were authorized: wallpaper title, wallpaper description, location placement in the wallpaper title group, prayer timetable presentation, and daily doa/ayah presentation. All Dock/Grid, Header/capsule, navigation/remote, wallpaper provider/rotation, global layout/safe-frame and unrelated components were treated as protected.

## 2. Input and baseline verification

- Canonical restart file read first: `docs/project-management/GTVH-CHECKPOINT-LATEST.md` on `docs/project-master-inventory`.
- PR #79 verified OPEN / DRAFT / UNMERGED. Its current source branch `fix/home-balanced-frame-001` was at `b4abdf0bd555a0106c135add20c3f287cf10b412`; the immediately preceding UI source candidate is `bf65621ae46b170294e4333928886fbafa0898b6`. The b4abdf commit itself only aligns the PR #79 APK publisher workflow.
- Existing wallpaper metadata in `WallpaperMetadata.java` already contained the requested location/title/description. No metadata or provider edit was needed.
- Existing `heroShelf` XML order was title → location → description. Both `heroShelf` and `islamicShelf` were positioned through shared `heroBottom`, computed from Dock geometry. Prayer data was dynamically rendered from `PrayerTimes.getPrayerItems()`. Daily ayah focus listener drew a low-opacity border even when not focused.

## 3. Before / after

| Component | Before | After |
|---|---|---|
| Wallpaper group | Title → location → description | Location → title → description |
| Location capitalization | Forced uppercase | Original mixed-case string preserved |
| Vertical placement | Shared Dock-relative baseline | Both shelves moved down by 5mm, same shared baseline |
| Prayer timetable | Static pale/white colors, amber next-prayer color | Restrained cool-to-warm text gradients, next prayer retains distinct warm emphasis |
| Daily ayah | Idle frame stroke plus stronger hint border | No idle frame stroke; softer hint; restrained focus-only outline |

Existing metadata remains exactly: 
- Location: `Pagar Alam, Sumatera Selatan`
- Title: `Kabut Pagi Dempo`
- Description: `Udara sejuk turun perlahan, menyelimuti lereng dan kebun teh Pagar Alam.`

## 4. Measured vertical conversion

Android density at the target Mi TV Stick profile is 320dpi, i.e. `density = 2.0`.

- `5mm × 160dp/in ÷ 25.4mm/in = 31.496dp`.
- `31.496dp × 2.0px/dp = 62.992px`, rounded to **63px**.
- Existing `DockGeometry.shelfBottomOffsetFromTileTopPx()` returns 50dp (14dp tray padding + 36dp shelf-to-tray spacing). After the 31.5dp shift, the nominal gap is approximately **18.5dp**.
- Runtime clamp retains at least 12dp between the information shelf baseline and Dock tray; at density 2.0 the requested 63px movement fits without clamping.
- Both `heroShelf` and `islamicShelf` use the same updated `heroBottom`. Dock geometry itself is unchanged.

## 5. Implementation files

1. `launcher/app/src/main/res/layout/activity_main.xml` — reorder only `heroWhere` and `heroName`; set location text to normal case; maintain existing typography and description.
2. `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java` — density-based shared shelf offset; restrained prayer text gradients; remove idle daily-ayah stroke and soften the hint; keep focus and DPAD/click handlers.
3. `tests/test_home_wallpaper_text_refinement_contract.py` — five regression contracts for ordering, metadata preservation, 5mm shared anchor, prayer gradients and ayah focus behavior.

No edits were made to `DockGeometry.java`, prayer data/provider, wallpaper metadata/provider/rotation, header/capsule geometry, navigation, global safe-frame values, Dock/Grid files or other modules.

## 6. Quality checks

| Gate | Result | Evidence |
|---|---|---|
| PR scope / changed-file review | PASS | Exactly 3 changed files (2 production + 1 test) |
| Wallpaper metadata preservation | PASS | New regression test confirms all three exact values |
| Shelf order | PASS | XML parsed by CI test; location → title → description |
| 5mm geometry contract | PASS | Shared anchor, conversion and 12dp collision guard verified by test |
| Prayer gradient contract | PASS | Header, six labels and six time views receive gradients; prayer data source/order unchanged |
| Daily ayah frame/focus contract | PASS | No idle stroke; focused outline remains; click/DPAD routes retained |
| Python suite | PASS | 166 tests, 1 skipped; all five new tests passed |
| Android release unit tests | PASS | `testReleaseUnitTest` completed successfully |
| Release build | PASS | `assembleRelease`; `BUILD SUCCESSFUL in 1m 37s` |
| APK size diagnostic | PASS WITH NON-BLOCKING WARNING | 1,627,937 bytes (+376 bytes vs 1,627,561); historical 1.5 MiB threshold warning only |
| Rendered screenshot at 1920×1080 | PENDING | No emulator/screenshot rendering evidence available in this run |
| Physical Mi TV Stick acceptance | PENDING | No ADB, install or device test performed |

CI run: `42493469262`. Python job: `128770608532`. Android job: `128770608299`.

## 7. Regression review and remaining risks

- PR diff shows only `MainActivity.java`, `activity_main.xml`, and the new test file. Dock, Header, capsule widths, navigation, global safe frame and unrelated component implementations were not changed.
- The 12dp guard protects Dock separation on different densities/layouts, but actual rendered shelf height and the visual 5mm appearance must be checked on device.
- Gradient appearance can vary against bright/dark wallpaper images; owner must verify readability from normal TV viewing distance.
- Focus-only border must remain visible and easy to track with the remote while visually disappearing at rest.
- APK size increased 376 bytes; no functional impact indicated. CI warning is informational and non-blocking.

## 8. Final disposition

**SOURCE IMPLEMENTATION: PASS. CI: PASS. OVERALL ACCEPTANCE: NOT FINAL.**

PR #80 remains OPEN / DRAFT / UNMERGED. No APK was installed, no ADB session was run, no device settings changed, no force-push or merge occurred. Next step is owner visual/physical acceptance on Mi TV Stick at 1920×1080. Any follow-up correction must remain inside the same five-component scope.
