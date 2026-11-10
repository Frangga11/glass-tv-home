# GTVH-HOME-DOCK-GLASS-FOCUS-OVERLAY-001

Date: 11 November 2026 (WIB)  
Repository: `Frangga11/glass-tv-home`  
PR: [#123](https://github.com/Frangga11/glass-tv-home/pull/123)  
Branch: `fix/home-dock-glass-focus-overlay-001`  
Base: `fix/home-two-row-dpad-weather-listener-001` (PR #115 cumulative candidate)  
Status: OPEN / DRAFT / UNMERGED

## Trigger

User reviewed a Mi TV recording of repeated DPAD UP/DOWN movement and reported:
1. Dock glass appearance changes while the primary row moves upward to reveal the second row.
2. The focus cursor disappears or becomes obscured on the second application row, making the active app position unclear.

## Cross-audit findings

- The shared `Glider` is inserted behind the Home content tree.
- Dock has an explicit companion drawing path through `DockDecoration.onDraw()`, which repaints the focus pane in the Dock RecyclerView's background pass.
- Initial Grid companion implementation used `ItemDecoration.onDraw()`. Android RecyclerView draws this before child views, so opaque tile cards/art can still cover the focus shape. This did not fully close the reported visibility failure.
- Corrective implementation changes Grid focus repaint to `ItemDecoration.onDrawOver()`, so the focus frame is rendered after Grid children, while remaining scoped to Grid focus ownership.
- Horizontal Dock/Grid expansion and collapse previously changed Grid alpha (0.72 to 1.0 and back) during a geometry-only transition. This unnecessary opacity tween altered the moving content plane and could be perceived as a glass/material change.
- DockDecoration's glass veil, blur fill, rim, border, and radius remain unchanged.
- CI workflow's special Home layout APK artifact allow-list omitted PR #123. The PR was added so this exact PR now emits a dedicated layout APK artifact on successful CI.

## Corrective changes

- Replaced GridFocusDecoration background-pass rendering with foreground overlay rendering via `onDrawOver`.
- Retained shared Glider coordinates, focus-owner guard, companion invalidation, and clipping.
- Added/updated source-contract regression to require `onDrawOver` and forbid the old `onDraw` path.
- Added PR #123 to the dedicated Home layout APK artifact allow-list; CI still runs unit tests and `assembleRelease` before upload.
- Kept Grid alpha fixed through Dock/Grid transitions.

## Protected scope

No changes to tile dimensions, application ordering/identity, DPAD routing, launch behavior, app inventory, wallpaper, Hero Shelf, Islamic Shelf, Weather/AQI data/providers/cadence, Wi-Fi, or unrelated modules. No merge, release, ADB, or physical device installation.

## Validation status

- Cross-audit identified and corrected the Grid draw-order gap.
- Regression test and fresh CI/build: pending on the new exact PR HEAD.
- Physical Mi TV Stick DPAD and visual acceptance: pending.
- Do not claim PASS or merge before CI and device acceptance.

## Required device acceptance

1. Idle Home shows only the primary Dock row.
2. DOWN from each primary Dock column reveals the corresponding second-row column.
3. Focus cursor remains visible around each of the five second-row app cards, including rapid repeated UP/DOWN.
4. UP from second row returns to the matching Dock column.
5. Dock glass material keeps stable veil/blur/rim/opacity while the Dock changes vertical position.
6. No focus cursor is hidden by tile art, clipped by RecyclerView, or left behind after transitions.
7. App activation and all unrelated Home controls remain unchanged.


## Video evidence audit — 11 November 2026

Uploaded Mi TV Stick recording: 18.43 seconds, 1920×1080, approximately 30 fps.

Observed sequence:
- During navigation into the top/header region, the application rows are absent from the visible Home composition while the header remains available. This is consistent with the Home focus-region presentation and is not, by itself, evidence of inventory loss.
- When the second row returns, the focused Prime Video tile displays a focus outline but its banner/artwork is visually absent. Other second-row tiles remain visible.
- The focus frame is therefore visible, but the focused tile's visual content is not reliably preserved. This is a distinct regression from a hidden focus cursor.

### Root cause confirmed by source inspection

GridFocusDecoration.onDrawOver() called Glider.drawShape() for the entire shared shape. In GLASS style, drawShape() paints the blurred backdrop, veil, rim, and focus stroke. Because onDrawOver() runs after RecyclerView children, this foreground pass can paint the glass pane over the focused app tile's banner/icon/label. The earlier change fixed cursor occlusion but used an over-broad drawing primitive and could obscure the tile itself.

### Corrective implementation

- Added Glider.drawFocusOutline() to render only the focus stroke in a foreground canvas.
- Changed GridFocusDecoration.onDrawOver() to call the outline-only method.
- Kept the full shared Glider pane on its existing root/background rendering path, behind tile content.
- Updated the source-contract regression to require outline-only foreground drawing and reject drawShape() in the Grid foreground decoration.
- Updated this checkpoint based on actual video evidence; no app inventory, tile geometry, launch behavior, DPAD routing, or unrelated Home modules changed.

### Validation gate

The source correction is committed to this PR branch. A fresh CI run against the resulting exact HEAD is required before issuing a new QA APK. Physical Mi TV Stick acceptance remains mandatory; do not claim device PASS based on source inspection or CI alone.

## DPAD transition audit — corrective pass, 11 November 2026

### Root cause confirmed in the horizontal collapse path

The Grid card remains the focused Android view while `collapseHorizontalOverflowToDock()` animates the Dock and Grid back to their idle positions. Before this pass, `dispatchKeyEvent()` continued forwarding repeated DPAD events to that old Grid card during the 260 ms collapse window.

That allowed a second UP to call `focusDockTile()` before the first collapse had settled, or a DOWN to call `requestHorizontalGridRow()`. Both paths increment `gridFocusRequestGeneration`. The collapse animator's end callback checks that generation and returns early when it has changed. Its cleanup (hide Grid, reset translations, clear collapse state, and hand focus to the Dock) was therefore skipped. This is a deterministic race between a still-focused Grid child and the asynchronous Dock collapse—not a color or tile-geometry defect.

The resulting stale/partially collapsed state can leave no reliable Home tile focus owner; subsequent directional movement may fall through to Android's normal focus traversal. This matches the reported disappearing focus and unexpected header/microphone destination.

### Corrective changes in this pass

- Intercept DPAD direction events while horizontal Dock collapse is active; do not let the old Grid child start a competing transition.
- Retain the latest direction as one pending intent and replay it against the Dock only after the collapse has completed and Dock focus has actually been restored.
- This makes one DOWN immediately after UP deterministic: it is replayed as the Dock's DOWN action and opens the matching Grid column without requiring a second press.
- Remove the Dock-only `DockBreathingBorderView` from the focus path. It was a second animated cyan/white rim layered over the shared Glider.
- Lock the shared `FOCUS_BORDER` and halo to neutral white. Cyan remains an ambient theme accent, not a second DPAD cursor.
- Add regression source-contract checks for collapse-event serialization and one white focus indicator.

### Validation status after corrective pass

- Source-level race identified and guarded.
- Source-contract tests added; execution and fresh CI result are pending.
- Physical Mi TV Stick validation is still required for UP/DOWN in every Dock column, repeated key presses during animation, and single-outline visual parity.
- No device PASS is claimed. PR remains OPEN / DRAFT / UNMERGED.
