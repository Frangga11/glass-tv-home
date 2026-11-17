# GTVH-HOME-TWO-ROW-FOCUS-PRECOMMIT-001

Date: 11 November 2026
Repository: `Frangga11/glass-tv-home`
PR: #129 — Draft / Open / Unmerged
Branch: `fix/home-two-row-focus-precommit-001`
Base: PR #128 head `dda7433cd3ef3a5ab5dd39b04f63a02fa9f3cca3`

## Trigger and physical evidence

Owner supplied a 29.34-second, 1920x1080 Mi TV Stick recording (`1000608910.mp4`) after installing PR #128.

Observed interaction sequence:
- DOWN reveals the overflow row and moves focus into the second app row.
- UP returns the Dock and app row to the idle geometry, but the focus marker is absent.
- A subsequent UP can land on the microphone/header.
- In the reverse sequence, the layout returns but focus is absent; another DOWN is needed before focus appears on the first app row.

The recording contains discrete remote/navigation transients. In the implementation, navigation sound is emitted by `handleTileFocused()` when a real focus callback is committed. The missing visual marker and missing/late navigation feedback are consistent with focus not being committed at the same time as the layout transition. The phone recording alone cannot identify the precise sound source; the app's focus callback and input timeline must be instrumented on-device if the issue persists.

## Root-cause finding in PR #128

The collapse sequence was ordered incorrectly:

1. Animate Dock and Grid back to idle geometry.
2. Set the Grid to `GONE` while the Grid still owns Android focus.
3. Reset transforms and apply Dock layout.
4. Only afterwards call `restoreDockFocusAfterCollapse()` and attempt `card.requestFocus()` for up to 32 frames.
5. If focus acquisition fails, route to `focusTopBarFallback()`.

This creates a focus vacuum: the focused Grid descendant is detached from the active focus path before the Dock destination has acquired focus. A visually completed animation is not evidence of committed Android focus. The later fallback can explain the subsequent UP reaching the microphone/header.

## Correction in PR #129

- Begin collapse in a guarded preparation phase.
- Resolve the same-column Dock destination while both Dock and Grid remain visible.
- Request focus on the Dock card before starting the collapse animation.
- Continue only after `card.isFocused()` confirms the destination owns focus and the synchronous `onDockTileFocused()` callback has committed.
- Capture the focus-generation token after that callback, not before it.
- Animate the Dock and Grid with the existing inverse translations while Dock already owns focus.
- Hide Grid only after animation completion and after confirming the focus-generation token is still current.
- If Dock focus cannot be acquired within the bounded preparation window, abort safely: keep the Grid visible and focused, restore the expanded geometry, log the reason, and do not send focus to the header.
- Preserve one semantic DPAD direction captured during the transition; dispatch it once after the collapse commits. No synthetic Android KeyEvent replay.
- Preserve same-column mapping, app order, tile geometry, launch behavior, and PR #125 cyan breathing focus view.

## Audio/focus contract

The app emits `SoundEffects.playNav()` from the actual tile focus callback (`handleTileFocused`). A successful pre-commit should therefore cause focus feedback at the moment the Dock card takes focus, while the card is still in the expanded scene. It must not wait until after Grid is hidden.

## Scope protection

No changes intended to app inventory/order/identity/launch, tile dimensions, Dock geometry outside the existing transition, header, microphone, Wi-Fi, wallpaper, hero/Islamic shelves, weather/AQI, settings, or cyan breathing focus rendering.

## Validation gates

- Source tests must prove pre-focus occurs before animation and Grid hiding.
- Android build and unit tests must pass on exact PR head.
- Physical acceptance remains mandatory on Mi TV Stick / Android TV 11 / 1920x1080:
  1. Dock DOWN → first overflow row, one press, one focus cue.
  2. First overflow row UP → same-column Dock, one press, focus marker and navigation cue remain.
  3. Repeat UP/DOWN at least 10 cycles.
  4. Rapid UP↔DOWN during the 260ms transition.
  5. Confirm no focus loss, no jump to microphone, no double navigation cue, and OK launches the selected app.
- CI PASS is not device PASS. PR stays Draft / Unmerged pending device acceptance and explicit owner approval.
