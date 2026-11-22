# Quick Settings Clean Fresh Glass — 22 November 2026

## Workstream
- Repository: Frangga11/glass-tv-home
- Parent baseline: PR #177, HEAD 0a28e573704744752d0f8e3d8cb9a346390c187d
- Work branch: feat/quick-settings-clean-fresh-glass-001
- Scope: visual material and typography refinement for the Quick Settings popup only.
- PR disposition: intended as a separate draft follow-up to PR #177; no merge or release authorization.

## Owner design intent
The Quick Settings popup should feel fresh, clean, bright and premium:
- substantial smoked-silver glass with controlled silver highlights, not a pale white wash or an opaque slab;
- wallpaper remains visible through the material, but does not overpower the panel;
- clean typography with strong readability;
- restrained silver/cyan focus cursor that is easy to identify without visual excess.

## Root-cause observations
The first clean-glass pass overcorrected the material toward a pale white veil and light idle capsules. On the TV, the wallpaper bled through too strongly and the panel lost its silver density. The corrective pass restores a stronger neutral-slate veil and smoked-silver capsule fills while preserving the white typography.

## Scoped implementation
- Added UniversalGlassMaterial.quickSettingsPanel(...), a Quick Settings-only surface recipe. Existing generic popup material remains unchanged for Wi-Fi and other dialogs.
- Added quickSettingsControlIdle(...) for a slightly brighter idle capsule with a quiet neutral edge.
- Tuned the dedicated Quick Settings surface mode to use a deeper neutral-slate veil (0x70151D25) and restrained silver highlight gradient (0x28FFFFFF, 0x18FFFFFF, 0x0CFFFFFF, 0x28151D25); existing backdrop blur, rim and window-light path are retained.
- Restored substantial smoked-silver idle capsules (0x70343E48 to 0x60303A44) with a neutral silver edge.
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
- Quick Settings contract updated for locked bright-white typography, denser smoked-silver panel and capsules, and reduced focus treatment.
- Popup-family contract verifies generic Wi-Fi panel material remains unchanged.
- Added a contract proving popup-only Glider shadow tuning does not change shared defaults.

## Validation status
- Source edits are committed on the work branch.
- Automated exact-head CI and release/QA artifact validation: pending.
- Physical Mi TV Stick visual acceptance: pending; no device acceptance is claimed.
- Do not merge PR #177 or this follow-up as part of this workstream without explicit owner approval.

## Device acceptance checklist
1. Inspect the popup on both bright and dark wallpapers at 1920x1080 / 320dpi.
2. Confirm the panel reads as clean substantial smoked-silver glass rather than pale white wash or opaque matte.
3. Confirm title, capsule labels and summaries remain legible at normal TV viewing distance.
4. Navigate every row with DPAD and verify the cursor is obvious, restrained, correctly clipped and smoothly animated.
5. Verify all dimensions, spacing, popup position and exact Dock clearance remain unchanged.
6. Confirm Wi-Fi, Prayer popup, Home, Dock and other shared Glider behavior remain unaffected.