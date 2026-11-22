# Obsidian Silver Glass legibility refinement — 22 November 2026

## Status
- Work branch: `fix/obsidian-silver-glass-legibility-001`
- Base: live `main` at `71f3a051a25fd66cabfd1740756cc36442dc92cc`
- Scope: visual/material refinement only; no geometry or navigation rewiring.
- CI: pending on the exact PR head.
- Physical Mi TV Stick validation: pending; no device pass is claimed.

## Trigger and observed symptoms
Owner supplied four photographs and a short Mi TV Stick recording:
1. Home navigation text and focus treatment lose clarity on bright wallpaper; the same UI reads well on darker wallpaper.
2. Quick Settings and Wi-Fi glass surfaces allow Home text (including Ayat and prayer timetable) to compete visually with popup content.
3. The owner requested a premium, restrained graphite/silver appearance without ambiguous glow or unreadable text.

## Source audit and root cause
- `MainActivity.moveTopHighlight()` assigned `Glider.DARK` to top-navigation focus.
- `Glider.drawShape()` used the general translucent silver `FOCUS_HALO` fill for DARK focus. On a bright backdrop, the light fill could wash out white navigation labels.
- `QuickSettingsDialog` explicitly set `dimAmount = 0f` and cleared `FLAG_DIM_BEHIND`; the Home layer therefore remained at full brightness behind the panel.
- `WifiSettingsDialog.popupDimAmount()` returned `0.08f`, an intentionally low dim level that did little to separate the popup from underlying Home text.
- Quick Settings used a lighter popup veil (`0x54`) than the shared default. Both popup surfaces relied on the shared blurred-wallpaper fill and a restrained but still quite transparent veil.

## Implemented changes
### Navigation
- Added semantic tokens for a darker navigation glass veil and graphite focus surface.
- Added `Glider.NAVIGATION_DARK`, a dedicated visual mode used only for top navigation. It shares the existing Glider, bounds, spring animation, focus ownership, and DPAD path; existing `DARK` remains unchanged for other consumers.
- Applied the darker navigation veil to the existing shared nav capsule and reduced only its decorative window-light glint.
- Added a restrained dark text shadow to the three navigation labels. Font size, capsule size, icon size, placement, tab order, and DPAD routing are untouched.

### Popup family
- Added one shared smoked-graphite popup veil and a shared background dim token (`0.22f`).
- Applied the same veil, opacity and restrained specular glint to Quick Settings and Wi-Fi.
- Quick Settings now enables `FLAG_DIM_BEHIND` and uses the shared dim amount instead of explicitly disabling backdrop dim.
- Wi-Fi now uses the same dim amount and veil; its pointer uses the same popup veil.
- Popup width/height, anchoring, internal content order, capsule sizes, text sizes, focus graph, Wi-Fi scan lifecycle, Android Wi-Fi handoff, and Quick Settings actions remain unchanged.

### Regression coverage
- Updated semantic palette synchronization checks.
- Updated Home focus contract to require the dark navigation focus variant while preserving the legacy DARK halo.
- Added popup material/scrim contract checks.
- Updated Wi-Fi dim test and added a Quick Settings test for backdrop dimming and fixed panel geometry.

## Protected scope
Explicitly unchanged:
- Dock and application icon dimensions, arrangement, and artwork.
- DPAD mechanisms, focus ownership, Glider animation/spring, and horizontal/vertical Dock movement.
- Popup geometry, placement, component order, capsule geometry, and text typography.
- Quick Settings and Wi-Fi functional actions, scan behavior, and Android-owned network operations.
- Wallpaper renderer, weather/AQI, Islamic shelf text, prayer timetable, and all unrelated modules.
- Accepted Horizontal Carousel / Glider behavior from the 22 November main integration checkpoint.

## Validation gates
1. Exact-head CI must pass for launcher build/unit tests and repository lists/tools/docs checks.
2. Review the resulting PR diff for scope leakage.
3. Physical Mi TV Stick validation must compare both bright and dark wallpaper:
   - idle nav labels remain crisp and icons clearly separated;
   - focus marker is immediately legible without washing out the label;
   - repeated DPAD movement retains the same timing and target geometry;
   - Quick Settings and Wi-Fi content remain distinct from Ayat/prayer text behind the panel;
   - fixed popup geometry and all existing controls still fit.
4. Do not merge or publish until exact-head CI and owner device acceptance are recorded, followed by explicit owner merge approval.

## Known limitation
The assistant can inspect source, uploaded photographs and recording, and CI evidence, but cannot directly operate the owner's Mi TV Stick in this session. Physical display quality remains an owner acceptance gate.
