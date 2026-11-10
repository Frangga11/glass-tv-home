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


## Follow-up corrective audit — 11 November 2026

User acceptance feedback identified two repeat defects:
1. The Grid focus cursor must be a marker around the active app tile only; it must never paint a filled glass pane over app artwork.
2. Pressing UP from the overflow row must not make the Dock disappear during the return-to-idle transition.

### Root cause and correction

- Replaced the Grid's shared-Glider foreground rendering with a dedicated RecyclerView.ItemDecoration stroke. It resolves the currently focused descendant at draw time, finds its R.id.card ancestor, converts the card's actual bounds into RecyclerView coordinates, and draws a transparent stroke only. There is no fill, blur, veil, or stale cached focus rectangle.
- The UP collapse previously ended its animation at translationY = +pitch, then changed the Dock's bottom margin from expanded to idle (which itself moves the Dock down by one pitch), while retaining the translation. This double-applied the vertical displacement and could push the Dock below the visible viewport until a later callback cleared the translation.
- The corrected collapse commits the idle geometry and clears Dock/Grid translations in the same UI callback before the next frame, then restores focus to the same Dock column.
- Regression contracts now assert focused-card-only outline rendering and the single-displacement UP collapse ordering.

### Scope and validation

No app inventory, card content, card geometry, launch listeners, weather/AQI, wallpaper, header, or unrelated modules changed. CI and physical Mi TV Stick acceptance are required against the exact follow-up HEAD. PR remains draft/unmerged until acceptance.
