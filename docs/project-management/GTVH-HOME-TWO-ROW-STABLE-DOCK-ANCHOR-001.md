# GTVH-HOME-TWO-ROW-STABLE-DOCK-ANCHOR-001

Date: 11 November 2026
Repository: `Frangga11/glass-tv-home`
Branch: `fix/home-two-row-stable-dock-anchor-001`
Base: `fix/home-two-row-up-collapse-visibility-001`
Status: corrective implementation candidate; CI and physical-device validation pending.

## Trigger
PR #116 passed CI but the supplied Mi TV Stick photo/video evidence confirms that after DPAD UP the Dock and app tiles are still absent. The focus outline remains near the bottom-left while the Home header and wallpaper remain visible.

## Deeper root-cause finding
The previous transition model used two simultaneous sources of vertical positioning:
1. changing the Dock's bottom LayoutParams margin by one row pitch when expanded; and
2. applying a positive translation during collapse to compensate for that margin change.

This makes the Dock's final screen position dependent on the exact ordering of a parent FrameLayout relayout and a child translation reset. PR #116 only moved the reset to pre-draw; it did not remove the coupled geometry model. Device evidence proves that timing-only correction is insufficient.

## Structural correction
- Make the Dock's bottom margin permanently equal to its idle anchor in both states.
- Keep the expanded Grid viewport anchored to the same bottom safe frame.
- Represent the entire two-row motion only with translations:
  - DOWN: Dock translates upward by one measured row pitch; Grid translates from below into its viewport.
  - UP: Dock translates back to zero; Grid translates down and fades out.
- At UP completion, reset both translations, hide Grid, apply idle Grid geometry, and restore same-column Dock focus. No Dock LayoutParams re-anchor and no pre-draw compensation handoff remain.
- Preserve measured row pitch, five-column mapping, app identity/order, tile sizing, and Weather/AQI behavior.

## Validation
- CI must pass against the exact PR HEAD.
- Install the resulting cumulative QA APK on Mi TV Stick Android TV 11, 1920x1080.
- Verify idle Dock, DOWN expansion, UP collapse, repeated cycles, rapid direction reversal, same-column focus, and all app tiles visible after every UP.
- Keep draft/unmerged until physical acceptance. CI PASS is not device PASS.
