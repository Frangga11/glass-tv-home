# Universal Glass Material Adoption — Checkpoint 22 November 2026

## Workstream
- Goal: adopt the visual glass-material system from Home Universal into Glass TV Home while preserving all accepted GTVH geometry, DPAD, focus, and animation behavior.
- GTVH repository: `Frangga11/glass-tv-home`.
- Official base: `main` @ `71f3a051a25fd66cabfd1740756cc36442dc92cc`.
- Work branch: `feat/universal-glass-material-adoption-001`.
- Latest implementation head: `fa42949b721bddad2cec32cab9879753eac53cd0`.
- Draft PR: [#162 — Adopt Universal Home glass material across Dock, home capsules and popups](https://github.com/Frangga11/glass-tv-home/pull/162).
- Upstream visual/source repository: [Frangga11/tv-home-universal](https://github.com/Frangga11/tv-home-universal), inspected at `1c57fa905bfa76a0b0e8e8593d5efd7d1d97d33e`.

## Reference-source audit
Inspected the actual source implementations and video references, not screenshots alone:
- `DockGlassDrawable.java`: layered dock/header-pill surface, sweep reflection, outer rim, inner rim, and outline.
- `GlassMenu.java`: dark translucent panel foundation plus DockGlassDrawable.
- `GlassControls.java`: idle and focused/pressed capsule surfaces.
- `SettingsPopup.java`: popup panel/control composition.
- `QuickSettingsPanel.java`: quick-settings surface and tile composition.
- `WifiNetworksActivity.java`: Wi-Fi surface routed through SettingsPopup.
- `MainActivity.java`: horizontal/vertical dock and header pill call sites.

Important distinction:
- Universal Home's bright edge is not a standalone neon border. It comes from the rim gradient plus the controlled sweep reflection.
- The earlier PR #160 did not port the complete material system; it covered a limited Wi-Fi pointer/transparency correction.
- PR #161 added a separate top-edge reflection primitive. It is not the basis of this full material adoption and is not merged into this branch.
- This branch starts directly from the official `main` baseline, not PR #161.

## Implementation
New source:
- `UniversalGlassDrawable.java`: port of Universal Home's surface gradient, sweep reflection, outer rim, and inner rim. The source focus-fill state is intentionally omitted because GTVH's existing Glider must remain the sole moving focus treatment.
- `UniversalGlassMaterial.java`: port of GlassMenu's dark panel layer and GlassControls' idle/focused/pressed control surfaces, with GTVH fallback handling.

Applied to:
- Dock tray through `DockDecoration`, for the existing horizontal and vertical rendering paths.
- Main Home header capsules: location, weather/AQI, Input, Wi-Fi, Assistant.
- Shared Home/Profile/Settings navigation capsule.
- Quick Settings panel and its idle control capsules.
- Wi-Fi popup shell, existing pointer geometry, status capsule, and controls.

Material details:
- Dock/header surface gradients, sweep reflection, outer rim and inner rim follow Universal Home's source values.
- Popup shell uses the Universal dark foundation `0xB31D1D1D` beneath the glass drawable.
- Control surfaces use Universal's translucent white idle gradient and restrained focus/pressed gradient/border.
- Existing GTVH corner radii and all component bounds are retained instead of changing the approved layout to Universal Home's geometry.
- `glass_effects_enabled` remains authoritative. When disabled, existing solid fallback surfaces and legacy GTVH focus fills are preserved.
- MIT attribution for the adapted upstream material is recorded in `THIRD_PARTY_NOTICES.md`.

## Hard-locked scope
No changes to:
- Dock/card/icon dimensions, positions, spacing, app order, or layout XML.
- DPAD routing, focus graph, vertical/horizontal dock mechanisms, carousel motion, or move mode.
- `Glider`, `GliderFocusDrawable`, or `DockBreathingBorderView` implementations/animations.
- Popup width/height, capsule width/height, typography, spacing, anchoring, content order, or action behavior.
- Wi-Fi scan/network lifecycle.
- Islamic Shelf or other non-target GlassDrawable/Backdrop surfaces.

## Validation
- Latest source head: `fa42949b721bddad2cec32cab9879753eac53cd0`.
- GitHub Actions CI #2528 / run `44961689958`: **SUCCESS** on the exact source head.
  - Launcher build and unit tests: SUCCESS.
  - Lists, tools and docs: SUCCESS.
  - APK size diagnostics: SUCCESS.
  - Cumulative integration QA artifact: SUCCESS.
- Release publisher workflow #890 was **SKIPPED**; no release APK was published.
- QA artifact ID: `14729423171`.
  - Archive size: 1,451,733 bytes.
  - SHA-256: `2aba5ce62acb765f7e5f349894a6c556a5da958b12612d60f7e83607adb22695`.
  - Expires: 28 November 2026.
- Exact-head CI #2529 is a duplicate run on the same source head; it is not required as the primary evidence because #2528 already passed.
- No Mi TV Stick visual acceptance has been performed for this new material delta. CI success is not visual acceptance.

## Acceptance and next actions
1. Inspect the QA candidate on the target Mi TV Stick at 1920×1080 / 320dpi.
2. Compare Dock, header capsules, Home/Profile/Settings navigation capsule, Quick Settings and Wi-Fi popup against the Universal Home video.
3. Check bright and dark wallpapers, edge continuity, reflection restraint, and pointer-to-popup continuity.
4. Confirm horizontal and vertical dock layouts and the existing dark raised Glider/focus animation are unchanged.
5. Record owner visual acceptance and any pixel/material corrections before considering the work complete.
6. Keep PR #162 OPEN / DRAFT / UNMERGED until explicit owner approval. No merge or release publication is authorized here.

## Protected historical work
- PR #62 remains OPEN / DRAFT / DEFERRED and untouched.
- PR #161 remains separate and unmerged; this workstream does not cherry-pick it.
- Main remains unchanged at `71f3a051a25fd66cabfd1740756cc36442dc92cc`.



## Superseding integration record — PR #170 is the governing baseline — 22 November 2026

The original planning section above records the earlier PR #162 proposal against `main`. That was not the final integration baseline. Owner direction subsequently established PR #170 as the authoritative accepted baseline.

- Immutable PR #170 source: `2208afbc896f6b08d699156714e7d58dc964f721`.
- Integration branch: `integration/pr170-universal-glass-sync-001`, created directly from that source.
- PR #162 was retargeted to the integration branch. The broad Universal Glass material adoption was already in the PR #170 cumulative ancestry; the only remaining source delta was the Wi-Fi pointer backdrop drawing adjustment in `UniversalGlassMaterial.java`.
- PR #162 merged successfully at `7df0a5bcf82b8fc67863abb886997e3b3df47de9` (merged head `b9866be19db1159f3a856199f8079c77bd50f7a4`).
- Exact-head PR CI after retargeting: run `44993172112`; Launcher build and unit tests SUCCESS; Lists, tools and docs SUCCESS.
- Post-merge CI and final documentation CI are still required. No independent Mi TV Stick acceptance is claimed for the post-sync APK.
- The PR #170 findings are binding for all glass/focus work: clip the moving Glider to the actual rounded capsule silhouette, align the rim to measured capsule bounds, use one rim only, and seed the initial focused capsule explicitly. Do not change capsule measurements, layout, spacing, DPAD graph, focus ownership, dock/app movement, or animation.
- PR #162 is no longer open. PR #62 remains deferred and untouched. No merge to `main` or release publication occurred in this workstream.
