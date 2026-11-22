# Quick Settings Clean Fresh Glass — 22 November 2026

## Workstream
- Repository: Frangga11/glass-tv-home
- Parent baseline: PR #177, HEAD 0a28e573704744752d0f8e3d8cb9a346390c187d
- Work branch: feat/quick-settings-clean-fresh-glass-001
- Scope: visual material and typography refinement for the Quick Settings popup only.
- PR disposition: intended as a separate draft follow-up to PR #177; no merge or release authorization.

## Owner design intent
The Quick Settings popup should feel fresh, clean, bright and premium:
- frosted silver-white glass, not a dark grey slab;
- wallpaper remains visible through the material;
- clean typography with strong readability;
- restrained silver/cyan focus cursor that is easy to identify without visual excess.

## Root-cause observations
The existing Quick Settings panel used the shared popup material with a dark 0x68151D25 veil and low-alpha white surface gradient. Its labels were white with black text shadows. The popup Glider used a 2dp rim and the shared default 22dp blur / 8dp drop / 0x9A shadow strength. Together these choices made the popup read darker and heavier than the requested clean-glass direction.

## Scoped implementation
- Added UniversalGlassMaterial.quickSettingsPanel(...), a Quick Settings-only surface recipe. Existing generic popup material remains unchanged for Wi-Fi and other dialogs.
- Added quickSettingsControlIdle(...) for a slightly brighter idle capsule with a quiet neutral edge.
- Added a dedicated clean Quick Settings surface mode to UniversalGlassDrawable:
  - white veil 0x2EFFFFFF;
  - surface gradient 0x50FFFFFF, 0x3AFFFFFF, 0x2CFFFFFF, 0x20FFFFFF;
  - existing backdrop blur, rim and window-light path retained.
- Quick Settings text uses bright-white typography 0xFF1B2933 and slate secondary 0xFF435663; heavy black text shadows are removed.
- The glass-disabled fallback is also changed to an opaque light-silver surface with matching light capsule fills, so the new bright-white typography typography remains readable when glass effects are turned off.
- Quick Settings Glider keeps the same spring animation, target bounds, companion clipping, single-rim ownership, DPAD graph and focus transitions. Its popup-only rim is reduced to 1.25dp and its instance shadow is tuned to 14dp blur, 3dp drop and alpha 0x58.
- Glider defaults remain 22dp blur, 8dp drop and alpha 0x9A for all other screens and modes.

## Hard-locked behavior and geometry
No changes intended to:
- panel width 245dp and maximum height 330dp;
- tile height 36dp, half width 110dp, wide width 225dp;
- 5dp section/capsule gaps and 10dp panel insets;
- popup anchoring, top and Dock clearances, or Android TV post-layout positioning from PR #177;
- DPAD focus graph, action routing, popup lifecycle, dock geometry, Home UI, Wi-Fi popup, Modern Settings, or Prayer popup.

## Regression coverage
- Quick Settings contract updated for clean ink, scoped bright panel, and reduced focus treatment.
- Popup-family contract verifies generic Wi-Fi panel material remains unchanged.
- Added a contract proving popup-only Glider shadow tuning does not change shared defaults.

## Validation status
- Source edits are committed on the work branch.
- Automated exact-head CI and release/QA artifact validation: pending.
- Physical Mi TV Stick visual acceptance: pending; no device acceptance is claimed.
- Do not merge PR #177 or this follow-up as part of this workstream without explicit owner approval.

## Device acceptance checklist
1. Inspect the popup on both bright and dark wallpapers at 1920x1080 / 320dpi.
2. Confirm the panel reads as clean frosted silver rather than opaque white or dark grey.
3. Confirm title, capsule labels and summaries remain legible at normal TV viewing distance.
4. Navigate every row with DPAD and verify the cursor is obvious, restrained, correctly clipped and smoothly animated.
5. Verify all dimensions, spacing, popup position and exact Dock clearance remain unchanged.
6. Confirm Wi-Fi, Prayer popup, Home, Dock and other shared Glider behavior remain unaffected.