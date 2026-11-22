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
- show a restrained blur of the actual Activity-window crop behind the panel (wallpaper plus Home foreground Views), so text and high-contrast shapes do not ghost through; do not substitute the wallpaper-only shared Backdrop cache;
- use a precise upper specular highlight, fine silver rim and subtle inner reflection to create a clean glass sheen;
- keep capsule colours in the existing slate family; improve translucency and add a narrow top reflection rather than recolouring controls;
- clean typography with strong readability;
- restrained silver/cyan focus cursor that is easy to identify without visual excess.

## Root-cause observations
The first passes alternated between an overexposed white veil and an excessively dark smoked layer. The TV captures showed foreground Home labels and prayer text remaining sharp through the translucent Dialog. The shared Backdrop cache stores only the wallpaper bitmap; it cannot blur Activity foreground Views. Removing that blur exposed the underlying Home text even more clearly. The root correction captures the exact Activity-window crop beneath Quick Settings using PixelCopy, blurs that composite off the UI thread, and draws it inside the panel before applying the slate-silver tint and polished edge highlights.

## Scoped implementation
- Added UniversalGlassMaterial.quickSettingsPanel(...), a Quick Settings-only surface recipe. Existing generic popup material remains unchanged for Wi-Fi and other dialogs.
- Added quickSettingsControlIdle(...) for a slightly brighter idle capsule with a quiet neutral edge.
- Added QuickSettingsBackdropCapture: PixelCopy samples the panel's exact crop from the Activity Window before the Dialog is shown; the existing blur routine processes the crop on the managed background executor.
- Quick Settings draws this local blurred composite (wallpaper and Home foreground content) before applying a slate-silver veil. The shared wallpaper-only Backdrop cache, its worker, and other popup surfaces are unchanged.
- Before a successful capture, Quick Settings uses an opaque slate-silver fallback so foreground Home text cannot bleed through; after capture it switches to the translucent tint (0x78343E48) and balanced face gradient (0x30FFFFFF, 0x1CFFFFFF, 0x10343E48, 0x30343E48).
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
- Quick Settings contracts cover Activity-window crop capture, background blur, capture failure handling, bitmap recycling, white typography, slate RGB preservation, glass material, and isolated focus treatment.
- Popup-family contract verifies generic Wi-Fi panel material remains unchanged.
- Added a contract proving popup-only Glider shadow tuning does not change shared defaults.

## Validation status
- Source edits are committed on the work branch.
- Automated exact-head CI and release/QA artifact validation for the new Activity-window capture path: pending.
- Physical Mi TV Stick visual acceptance: pending; no device acceptance is claimed.
- Do not merge PR #177 or this follow-up as part of this workstream without explicit owner approval.

## Device acceptance checklist
1. Inspect the popup on both bright and dark wallpapers at 1920x1080 / 320dpi.
2. Confirm the panel reads as clean clear polished glass with retained slate-silver colour, visible wallpaper detail and controlled highlights.
3. Confirm title, capsule labels and summaries remain legible at normal TV viewing distance.
4. Navigate every row with DPAD and verify the cursor is obvious, restrained, correctly clipped and smoothly animated.
5. Verify all dimensions, spacing, popup position and exact Dock clearance remain unchanged.
6. Confirm Wi-Fi, Prayer popup, Home, Dock and other shared Glider behavior remain unaffected.