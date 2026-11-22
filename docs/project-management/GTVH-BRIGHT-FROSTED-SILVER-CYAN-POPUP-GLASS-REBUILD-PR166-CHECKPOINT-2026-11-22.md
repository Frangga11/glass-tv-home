# Bright Frosted Silver-Cyan Popup Glass — PR #166 Rebuild Checkpoint

## Baseline
- Parent: PR #166 branch `feat/silver-cyan-windowlight-home-capsules-001`
- Exact parent SHA: `9b77bfa166afc9cd152f850826120bc01659b3ba`
- Work branch: `feat/bright-frosted-popups-on-pr166-001`
- PR #167 is not the base; no PR #167 commit was merged or cherry-picked.

## Audit finding
PR #166 popup surfaces are built with `UniversalGlassMaterial`, not the older `GlassDrawable` popup constructor. Its default panel material includes `PANEL_SHADE = 0xB31D1D1D`, which made the popup dark. The Wi-Fi pointer uses the same dark panel shade. The initial PR #167 implementation targeted an older source shape and was therefore not safe to transplant wholesale.

## Rebuild
- Added popup-only `PopupGlassTokens`.
- Added opt-in overloads to `UniversalGlassMaterial.panel`, `controlIdle`, `controlStateBackground`, and `drawPointer`.
- Existing default Universal material behavior remains the same for non-popup callers.
- Quick Settings and Wi-Fi explicitly opt into bright panel/capsule/pointer surfaces.
- Dark-slate typography and restrained cyan focus edge are used for readability.
- Wi-Fi connected status green remains semantic status color.

## Protected behavior and geometry
- Quick Settings: 255dp × 320dp, existing 36dp tiles, 115dp half tiles, 235dp wide lane, 5dp gaps, existing insets and anchoring.
- Wi-Fi: existing width/height fractions, pointer geometry, network viewport, scan lifecycle and parent-focus return.
- Existing Glider, DPAD graph, focus routing, animation, actions and non-popup surfaces remain unchanged.
- No global token palette, Home, Dock, Quran, navigation or wallpaper changes.

## Validation
- A dedicated source contract was added at `tests/test_bright_popup_glass_palette_contract.py`.
- Exact-head CI has not yet been confirmed.
- No APK/device test or physical Mi TV Stick visual acceptance is claimed.
- Visual acceptance remains pending under both bright and dark wallpapers.

## Status
- This branch is a clean rebuild from PR #166, not a retarget or merge of PR #167.
- PR #166 and PR #167 remain open/draft/unmerged.
- Do not merge or publish until exact-head CI, source review, owner visual acceptance and separate explicit merge approval.
