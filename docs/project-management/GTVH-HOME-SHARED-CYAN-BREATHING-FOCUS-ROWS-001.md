# GTVH-HOME-SHARED-CYAN-BREATHING-FOCUS-ROWS-001

Date: 11 November 2026

## User-approved visual direction

Preserve the existing animated cyan breathing focus treatment seen on the first Dock row and apply the same treatment to focused application tiles in the second/expanded Grid row. Do not replace it with a static white-only outline.

## Audit findings

- The existing effect is implemented by `DockBreathingBorderView.java`.
- It is not a plain cyan stroke: it layers a cyan atmospheric glow, a white-to-cyan vertical glass rim, and a top specular glint.
- Its `ValueAnimator` runs a 1600 ms inhale/exhale cycle using `AccelerateDecelerateInterpolator`, `REVERSE`, and `INFINITE`.
- The shared `item_app.xml` already contains the same `DockBreathingBorderView` for every app card.
- `AppAdapter.Holder` previously made that view visible only when `focused && dockTile`. Grid holders are created with `dockTile=false`, so row 2 never received the animation.
- `GridFocusDecoration` separately drew a static white outline over the Grid. Keeping it alongside the animated cyan border would create two competing focus indicators.

## Implementation

- Show the existing `DockBreathingBorderView` whenever any app card receives focus, regardless of whether it belongs to Dock or Grid.
- Hide/stop the same view when focus leaves the card; the view's existing visibility/attachment lifecycle controls its animator.
- Keep the original 1600 ms breathing animation, cyan glow, white-to-cyan rim gradient, top glint, stroke-width breathing, and corner-radius calculation unchanged.
- Remove the Grid-only static `GridFocusDecoration` registration and its Glider companion invalidation, preventing a second focus cursor.
- Do not change app ordering, Dock/Grid geometry, key routing, focus transitions, tile scale behavior, launch listeners, wallpaper, Hero Shelf, Islamic Shelf, weather/AQI, or unrelated modules.

## Regression coverage

- Both Dock and Grid use the same `item_app.xml` and `DockBreathingBorderView`.
- The adapter focus listener no longer gates the breathing border on `dockTile`.
- The original animator duration, repeat mode, cyan token, gradient and glint remain intact.
- The static Grid outline is not registered.
- The border view does not scale or translate the card.

## Validation gate

This commit contains source changes and regression contracts only. Fresh CI/build and physical Mi TV Stick acceptance are required. Do not claim device PASS or merge before confirming:
1. Cyan breathing effect appears identically on Dock and Grid rows.
2. Exactly one focus indicator is visible at a time.
3. UP/DOWN mapping and focus restoration remain stable, including rapid key presses during transitions.
4. Focus rim tracks the focused card and does not cover its banner/icon/label.
5. No change to app geometry, order, launch behavior, or unrelated Home surfaces.
