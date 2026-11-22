# Quick Settings Clean Fresh Glass — 22 November 2026

## Workstream
- Repository: Frangga11/glass-tv-home
- Parent baseline: PR #177, HEAD 0a28e573704744752d0f8e3d8cb9a346390c187d
- Work branch: feat/quick-settings-clean-fresh-glass-001
- Scope: visual material and typography refinement for the Quick Settings popup only.
- PR disposition: intended as a separate draft follow-up to PR #177; no merge or release authorization.

## Owner design intent
The Quick Settings popup should feel clean, clear and premium, like polished glass:
- retain the existing cool slate-silver hue and bright-white typography;
- show wallpaper detail through a restrained existing low-radius blur, so text and high-contrast shapes behind controls do not ghost through; Quick Settings must not consume the shared blurred-backdrop layer;
- use a precise upper specular highlight, fine silver rim and subtle inner reflection to create a clean glass sheen;
- keep capsule colours in the existing slate family; improve translucency and add a narrow top reflection rather than recolouring controls;
- clean typography with strong readability;
- restrained silver/cyan focus cursor that is easy to identify without visual excess.

## Root-cause observations
The first passes alternated between an overexposed white veil and an excessively dark smoked layer. The TV captures then showed a persistent fog effect caused by stacking the shared blurred wallpaper, a slate veil, broad white surface gradients and a wide reflection sweep. The corrective optical pass keeps a reduced-strength version of the existing low-radius wallpaper blur, restores sufficient slate-silver surface opacity to separate foreground content, removes the broad reflection sweep, and reserves shine for the rim and narrow edge highlights.

## Scoped implementation
- Added UniversalGlassMaterial.quickSettingsPanel(...), a Quick Settings-only surface recipe. Existing generic popup material remains unchanged for Wi-Fi and other dialogs.
- Added quickSettingsControlIdle(...) for a slightly brighter idle capsule with a quiet neutral edge.
- Restored the existing low-radius wallpaper blur at 0.72 strength for Quick Settings only; the global blur pipeline and other popup surfaces remain unchanged.
- Tuned the Quick Settings tint to defined slate-silver (0x78343E48) and a balanced face gradient (0x30FFFFFF, 0x1CFFFFFF, 0x10343E48, 0x30343E48).
- Removed the broad face reflection sweep that caused a fog-like wash; retained the fine polished silver rim, window-light edge and subtle inner edge reflection.
- Preserved the capsule slate RGB values (0x343E48 / 0x303A44), restored sufficient opacity (0x88 / 0x78), and retained a soft upper specular overlay (0x30FFFFFF).
- Quick Settings primary and secondary text use the established bright-white tokens (TvUiTokens.TEXT_PRIMARY / TEXT_SECONDARY); only a restrained shadow is retained for readability.
- The glass-disabled fallback retains the existing dark-silver structural surface and matching capsule fills so bright-white typography remains readable when glass effects are turned off.
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
- Quick Settings contract updated for locked bright-white typography, slate RGB preservation, translucent glass alpha, controlled gloss and isolated focus treatment.
- Popup-family contract verifies generic Wi-Fi panel material remains unchanged.
- Added a contract proving popup-only Glider shadow tuning does not change shared defaults.

## Validation status
- Source edits are committed on the work branch.
- Automated exact-head CI and release/QA artifact validation: pending.
- Physical Mi TV Stick visual acceptance: pending; no device acceptance is claimed.
- Do not merge PR #177 or this follow-up as part of this workstream without explicit owner approval.

## Device acceptance checklist
1. Inspect the popup on both bright and dark wallpapers at 1920x1080 / 320dpi.
2. Confirm the panel reads as clean clear polished glass with retained slate-silver colour, visible wallpaper detail and controlled highlights.
3. Confirm title, capsule labels and summaries remain legible at normal TV viewing distance.
4. Navigate every row with DPAD and verify the cursor is obvious, restrained, correctly clipped and smoothly animated.
5. Verify all dimensions, spacing, popup position and exact Dock clearance remain unchanged.
6. Confirm Wi-Fi, Prayer popup, Home, Dock and other shared Glider behavior remain unaffected.