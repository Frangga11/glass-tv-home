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
