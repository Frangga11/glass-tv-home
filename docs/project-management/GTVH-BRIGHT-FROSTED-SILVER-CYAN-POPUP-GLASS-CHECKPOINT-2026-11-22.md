# Bright Frosted Silver-Cyan Popup Glass — Checkpoint 22 November 2026

## Purpose
Implement the owner-approved direction for popup surfaces: brighter, cool silver/cyan frosted glass; clear and decisive hierarchy; restrained cyan; legible dark-slate typography; no dominant charcoal/brown-black appearance.

## Baseline and isolation
- Parent baseline: PR #156 branch `feature/quick-settings-single-frame-glass-001`.
- This work is isolated on `feat/bright-frosted-silver-cyan-popups-001`.
- Popup Pengaturan / Quick Settings remains the material master; Wi-Fi adopts the same popup-only palette.
- No global `TvUiTokens`, Home, Dock, Quran/Islamic typography, navigation, or wallpaper tokens are changed.
- No merge to the PR #156 branch or `main` is authorized by this checkpoint.

## Popup palette
| Role | Color |
|---|---|
| Panel frosted veil / fallback | `#E8EFF4` |
| Panel veil opacity | `0xB8` |
| Panel border | `#B8C9D5` |
| Primary text | `#263746` |
| Secondary text | `#526675` |
| Icon tint | `#344D5D` |
| Idle capsule | `#DCE7EF` |
| Idle capsule border | `#AFC2CF` |
| Selected capsule | `#E3EFF4` |
| Focus capsule | `#DDF5F8` |
| Focus border | `#42BCD2` |
| Wi-Fi pointer veil | `#DCE8EF` with alpha |
| Connected network surface | `#DCE7EF` |

The existing live Backdrop blur and GlassRim / Window Light remain in place. Dark depth is no longer the dominant surface treatment. Wi-Fi connected-state green remains a semantic status color.

## Implemented
- Added popup-only `PopupGlassTokens.java`.
- Applied the brighter veil and opaque fallback to Quick Settings and Wi-Fi panel surfaces.
- Applied bright idle/focus capsule states and dark-slate text to Quick Settings.
- Applied matching pointer, connected-network surface, focus capsule and text palette to Wi-Fi.
- Updated source-level contracts and added regression coverage for palette isolation and geometry/focus preservation.

## Explicitly preserved
- Quick Settings fixed panel: 255dp × 320dp; existing insets, 36dp tile height, 115dp half-width, 235dp lane and 5dp gaps.
- Wi-Fi anchored geometry, width/height fractions, pointer geometry, scan/network lifecycle, actions and parent-focus return.
- DPAD graph, Glider implementation, focus routing, animation, dialog actions and all non-popup UI.
- Existing GlassDrawable / Backdrop / GlassRim implementation; no global material changes.

## Validation status
- Source changes and regression contracts committed on the isolated feature branch.
- Exact-head CI is required; status is pending until GitHub Actions completes on the final commit.
- Physical Mi TV Stick visual acceptance is pending. CI does not establish visual quality.
- Review under both bright and dark wallpapers is required. Confirm text readability, translucency, panel edge definition, capsule state distinction, and absence of overlap.
- No APK/device test result is claimed in this checkpoint.

## Next gate
1. Confirm exact-head CI passes.
2. Build and install the QA APK for the owner-controlled Mi TV Stick visual test.
3. Evaluate Settings first as the master; then compare Wi-Fi against it.
4. Correct any visual defect without changing locked geometry or behavior.
5. Keep this branch/PR unmerged until explicit owner acceptance and separate merge approval.
