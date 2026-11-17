# GTVH-HOME-TWO-ROW-FOCUS-STATE-MACHINE-001

Date: 11 November 2026
Repository: `Frangga11/glass-tv-home`
PR: #128 — Draft / Open / Unmerged
Branch: `fix/home-two-row-focus-state-machine-001`
Base: `fix/home-dpad-focus-handoff-replay-001` (PR #127)

## Trigger

Physical Mi TV Stick testing after PR #127 still showed:
- UP from the overflow row could leave the focus marker absent; a subsequent UP could route to the microphone/header.
- DOWN after the UP hand-off could require an extra press to reach the first app row.

## Cross-PR audit

- PR #115 contains the baseline same-column mapping contract: overflow row UP returns to the corresponding Dock slot; Dock DOWN maps into the overflow Grid.
- PR #117 introduced the more stable permanent Dock LayoutParams anchor, but it is not in PR #127's ancestry.
- PR #124 remained on the re-anchoring model where Dock bottom margin changes with expanded state while translation is also animated.
- PR #126 guarded focus ownership during collapse.
- PR #127 added a multi-event DPAD queue and replayed synthetic ACTION_DOWN/ACTION_UP events through Android dispatch after focus restoration. This could re-run one physical intent through a second navigation path and was not a safe way to preserve user intent.
- PR #125's shared cyan breathing focus treatment is retained.

## Structural correction in PR #128

1. Dock bottom margin is fixed to the idle anchor in both states. The expanded/idle state no longer changes the Dock's LayoutParams bottom margin.
2. Expansion starts with Dock translation at zero and Grid translation at one measured row pitch. The coordinated animator moves Dock upward to negative pitch while Grid enters its viewport.
3. Collapse is the inverse transform: Dock returns from negative pitch to zero while Grid exits. At completion, transforms are reset, Grid is hidden, and idle layout is committed.
4. Replace the multi-event replay queue with a single semantic direction captured only on the initial ACTION_DOWN while collapse owns focus. Key repeats are not duplicated.
5. After a visible, focusable Dock card actually owns focus, the captured direction is applied once through launcher navigation:
   - DOWN resolves the same Dock column to the matching overflow Grid item and enters the row.
   - UP follows the normal Dock-to-header fallback.
6. No synthetic KeyEvent is dispatched. Adapter callbacks cannot enqueue a second copy while collapse owns the transition.

## Preserved scope

- Existing app inventory, order, identity, tile sizing, and launch behavior.
- Existing row mapping and horizontal row-scroll contract.
- Existing cyan breathing focus view from PR #125.
- Home header, wallpaper, hero and Islamic shelves, weather/AQI, Wi-Fi, and unrelated settings.
- No broad UI rewire or unrelated layout redesign.

## Regression coverage

Updated source-contract tests:
- `test_home_dpad_focus_handoff_replay.py`: one semantic intent, visible Dock focus acquisition, no synthetic KeyEvent replay.
- `test_home_focus_integration_regression.py`: focus ownership and inverse collapse transform.
- `test_home_two_row_dpad_weather_listener_contract.py`: same-column navigation and permanent anchor.
- `test_home_layout_bounds_contract.py`: Dock bottom anchor remains fixed.

## Validation status

- PR #128 CI is running against the branch head; an earlier run on an intermediate commit failed one newly written test assertion, which was narrowed to reject synthetic KeyEvent replay specifically rather than the Activity's ordinary `super.dispatchKeyEvent(event)` fallback.
- Android build/unit tests and all source-contract tests must pass on the latest exact PR head.
- Physical acceptance is still REQUIRED on Mi TV Stick / Android TV 11 / 1920x1080:
  1. Overflow row → UP once → same-column Dock focus.
  2. Dock → DOWN once → same-column overflow row focus.
  3. Repeat UP/DOWN cycles and rapid UP↔DOWN while transitions are active.
  4. Confirm cyan breathing focus remains visible, no focus loss/jump to microphone, and OK launches the selected app.
- CI PASS is not device PASS. Keep Draft/unmerged until physical acceptance and explicit owner approval.
