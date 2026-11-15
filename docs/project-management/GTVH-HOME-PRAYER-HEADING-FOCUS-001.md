# GTVH-HOME-PRAYER-HEADING-FOCUS-001

## Objective
Move the Home Waktu Sholat DPAD focus target from the six prayer-time cells (Subuh–Isya) to the complete heading row, for example: “WAKTU SHOLAT PAGAR ALAM • 6 JUMADIL AKHIR 1448H”.

## Baseline
- Repository: `Frangga11/glass-tv-home`
- Parent branch: `fix/home-daily-ayah-proportional-layout-001`
- Parent HEAD: `804cb33676a4e2e8d78fdb864b8b380fe6f8b998`
- Work branch: `fix/home-prayer-heading-focus-001`
- Workstream is isolated from and does not modify PR #139.

## Implemented
- `R.id.prayerHeader` is now the single focusable/clickable Home entry point for the prayer timetable.
- The silver/cyan focus indicator is attached to the heading row, not the six prayer cells.
- OK, Enter, Numpad Enter, and remote BUTTON_A activate the existing `showPrayerPopup()` action.
- DOWN from the heading enters the existing first Dock tile (or existing tile fallback).
- UP from the heading returns to the Ayat “Baca Ayat Lengkap” target, with the existing assistant fallback.
- RIGHT from the heading preserves the existing route to Inputs/Wi-Fi/Settings.
- The Subuh–Isya timetable remains visible and data-driven, but the table container is explicitly non-focusable and non-clickable.
- DOWN from the Ayat action now targets the prayer heading.

## Strict scope protection
Only the Home prayer focus target and its DPAD/click routing are changed. No prayer labels, times, calculations, providers, typography, layout geometry, glass styling, wallpaper, Ayat content, Dock/Grid, header navigation, Wi-Fi, Weather/AQI, prayer popup internals, or Azan behavior are intentionally changed. No global focus system or shared focus drawable is modified.

## Validation
- Added `tests/test_home_prayer_heading_focus_contract.py` covering Ayat-to-heading navigation, heading activation and directional behavior, and non-focusable prayer cells.
- Automated CI/build and Mi TV Stick remote verification are pending.
- No physical-device acceptance is claimed until tested on the TV.

## Status
OPEN / DRAFT / UNMERGED. No merge or release authorization is implied.


## Follow-up: GTVH-HOME-DPAD-THREE-ROW-NAVIGATION-001

- Branch: `fix/home-dpad-three-row-navigation-001`
- Parent: PR #140 HEAD `9c6fe7dbde503cff18b47da1915c34f2b5e8d7c5`
- Added deterministic four-way Grid neighbour calculation. UP/DOWN preserve the source column where possible and clamp to the nearest available column in a partial row; LEFT/RIGHT stop at row boundaries.
- In horizontal Dock mode, all four directions now pass through the same pending-target/transition coordinator. During an active row animation, the pending logical target is used instead of Android geometric focus search, and the latest requested destination wins.
- Existing first-row UP-to-Dock behavior and vertical-Dock UP-to-header fallback are retained.
- Added unit coverage for 9, 10, 11, and 12 app layouts, partial-row column mapping, and hard edges.
- No layout, tile dimensions, spacing, visual styling, app ordering, prayer/Ayat, or popup behavior is changed.
- Dock capacities 5/6 were reviewed but their visual slot-to-grid mapping was not changed without a confirmed layout contract; this remains a device/configuration verification item.
- CI and physical Mi TV Stick remote verification must pass before acceptance.
