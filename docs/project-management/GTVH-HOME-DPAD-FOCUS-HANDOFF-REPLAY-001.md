# GTVH-HOME-DPAD-FOCUS-HANDOFF-REPLAY-001

## Trigger / observed regression
Physical Mi TV Stick testing reported two repeatable symptoms:
1. DPAD UP from the overflow (second) app row can leave the focus indicator missing; a subsequent UP appears to move focus to the microphone/header.
2. DPAD DOWN immediately after UP may require a second press before the first app row receives focus.

## Audit findings
- The previous collapse guard consumed all vertical DPAD events while `horizontalDockCollapseInProgress` was true. This protected the animation generation but silently discarded valid user input. It explains why the first DOWN during the hand-off could do nothing.
- After collapse, Dock focus restoration used a short three-frame retry and then fell back to the top bar. On slower TV layout/rebind timing, the Dock child may not yet be attached, so this fallback can move focus away from the app row.
- Android's default geometric focus search was not blocked at Activity dispatch level during the transition. If the Grid becomes GONE before Dock focus is reacquired, Android can route a later key using a stale/neighbor focus target.

## Corrective implementation
- Capture vertical DPAD events in Activity dispatch while collapse owns focus; cap the queue at eight events to bound memory and replay work.
- Consume key-up events for captured directions so the original key sequence does not leak to Android's default focus search.
- After idle geometry is committed, keep transition ownership until the target Dock card is attached, visible, focusable, and has actually acquired focus.
- Retry Dock child acquisition for up to 32 animation frames instead of three; do not move focus to the header during ordinary layout settling.
- Replay captured DPAD down/up pairs through normal View dispatch, in original order, only after Dock focus is confirmed.
- Keep the existing cyan breathing focus visual, tile geometry, app ordering, launcher activation, wallpaper, Home shelves, weather/AQI, and header design unchanged.

## Regression coverage
- Activity-level vertical key capture precedes Android fallback dispatch.
- Queue is bounded.
- Dock focus acquisition is verified before replay.
- Delayed layout retries and terminal fallback are explicit.
- Captured key pairs replay in order through normal Android dispatch.
- No silent-drop guard remains in the Grid listener.

## Verification gates
- CI: Python contract tests, Android unit tests, release APK build, APK diagnostics.
- Device acceptance on Mi TV Stick Android TV 11 at 1920x1080:
  - From overflow row 1: press UP once; cyan focus must land on the same Dock column.
  - Press UP again after hand-off; focus must move to the expected top navigation target, not disappear or jump unexpectedly to the microphone.
  - From the Dock, press DOWN once; overflow row 1 must receive focus.
  - Repeat UP/DOWN rapidly and hold keys; no lost input, duplicate movement, focus loss, or stale glass target.
  - Verify OK/Enter still launches the focused app.
- No PASS or merge claim before physical device acceptance.

## Status
Implementation branch: `fix/home-dpad-focus-handoff-replay-001`.
Base: PR #126 head `629dc938e1a42f724f870a1408dbfeda14ecb2ac`.


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
