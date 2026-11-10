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

## Audit findings

- The shared `Glider` is inserted behind the Home content tree.
- Dock has an explicit companion drawing path through `DockDecoration.onDraw()`, which repaints the focus pane in the Dock RecyclerView's background pass.
- App Grid had no equivalent companion drawing path. Its focus target could therefore be occluded by app tile content / drawing order, especially in the overflow row.
- Horizontal Dock/Grid expansion and collapse explicitly changed Grid alpha (0.72 to 1.0 and back) during a geometry-only transition. This unnecessary opacity tween altered the moving content plane and could be perceived as a glass/material change.
- DockDecoration's glass veil, blur fill, rim, border, and radius are fixed style parameters. They are retained; wallpaper sampling remains spatially correct as the tray moves over the wallpaper.

## Changes in PR #123

- Added `GridFocusDecoration` to draw the same shared Glider in the Grid's background pass, only while the Grid owns focus.
- Registered GridFocusDecoration after Glider initialization and added the Grid as a Glider companion so animation invalidates the Grid renderer.
- Removed Grid alpha modulation from expand/collapse transitions. Dock/Grid movement is geometry-only; Grid opacity remains unchanged.
- Added `tests/test_home_dock_glass_focus_overlay_contract.py` for rendering ownership, companion registration, and no-alpha-tween contracts.

## Protected scope

No changes to tile dimensions, application ordering/identity, DPAD routing, launch behavior, app inventory, wallpaper, Hero Shelf, Islamic Shelf, Weather/AQI data/providers/cadence, Wi-Fi, or unrelated modules. No merge, release, ADB, or physical device installation.

## Validation status

- Source changes and regression contracts committed.
- CI/build/unit-test status: pending verification against final PR HEAD.
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
