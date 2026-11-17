# GTVH-HOME-AYAH-PILL-FOCUS-ALIGN-001

## Scope
Only the Home Ayat Pilihan preview interaction, translation alignment, and the gap between the Baca Ayat Lengkap capsule and the Waktu Sholat heading.

## Changes
- Make the Ayat preview container display-only and non-focusable/non-clickable.
- Move DPAD focus and activation to the Baca Ayat Lengkap capsule (ayahOpenHint). OK/Enter opens the existing complete-verse glass reader.
- Keep the navigation path from the microphone/header and from the prayer timetable, but route it to the capsule rather than the preview card.
- Apply the focus indicator to the capsule only; the full Ayat card no longer receives a focus halo.
- Right-align the Indonesian translation preview.
- Reduce the divider top margin from 6dp to 1dp. With the existing divider height, bottom margin, parent bottom padding/margin and prayer heading translation, this changes the visual capsule-to-heading gap from approximately 15dp to 10dp. No other prayer layout dimensions are changed.

## Explicitly untouched
Prayer timetable content and popup, Azan playback, global glass implementation, Home wallpaper, top navigation, Dock, app grid, and other Home layout elements.

## Validation
A source-contract test verifies focus ownership, capsule activation, right-aligned translation, and the 10dp spacing calculation. Build and CI validation are pending.


## Repository synchronization addendum — 17 November 2026

This document is retained as the historical record of its original trigger and implementation. Its earlier branch/PR status and pending gates are historical, not the current integration status. The cumulative successor work was integrated through PR #155 into `main` and validated on the latest documentation checkpoint:

- Current `main` checkpoint: `33776e2c772138325da7dd515ea84c2196f58d2d`.
- Main integration commit: `81010b0941237600758036827531aff8194fb273` (PR #155 merged).
- Exact latest-main CI run: `44058685408`; both `Launcher build and unit tests` and `Lists, tools and docs` completed SUCCESS.
- Cumulative QA artifact: `14317436391`, `glass-tv-home-cumulative-integration-127-33776e2c772138325da7dd515ea84c2196f58d2d`; contains `app-release.apk` (1,640,745 bytes). APK SHA-256: `29b89a90bd6093ed75e9d8876d02b78188bb4bd45696ef0321539aaa92871b5c`. ZIP integrity check passed after extraction.
- The user reported physical Mi TV Stick testing of the cumulative candidate as clear and clean. This is user-reported acceptance; it is not an independent device test by the assistant.
- The active DPAD/focus cumulative implementation is represented by the PR #153/#154 stack integrated to main. The weather cache restore in PR #154 reapplies cached overlay state before the 15-minute network-fetch gate; the gate remains for fetching only.
- Preserve the original scope boundaries: RE-SKIN, NOT REWIRE; no unrelated app inventory, layout, prayer/Ayah, popup, or wallpaper changes are implied by this synchronization.

For current continuation, use the final cumulative checkpoint document and the latest `HANDOVER.md` section as the canonical handoff. Do not reopen or merge the historical draft branches listed above as if they were current candidates. Any newly reported TV issue becomes a new, narrowly scoped corrective task.
