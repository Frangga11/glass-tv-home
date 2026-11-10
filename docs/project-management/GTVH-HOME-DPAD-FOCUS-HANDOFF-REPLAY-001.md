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
