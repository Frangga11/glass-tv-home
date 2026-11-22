# Glass legibility refinement — restart from PR #162 — 22 November 2026

## Authoritative restart point
- Repository: `Frangga11/glass-tv-home`
- Base PR: #162 — Adopt Universal Home glass material across Dock, home capsules and popups
- Exact PR #162 HEAD: `adf4ef961537a630f7463eba53debf4ead5b37e3`
- Fresh work branch: `fix/glass-legibility-from-pr162-001`
- This branch was created directly from the exact PR #162 HEAD.
- PR #163 / Obsidian Silver Glass is NOT the source of truth and none of its commits are included in this branch.
- PR #162 itself is left unchanged.
- `main` is not the working baseline for this refinement.

## Why the previous attempt is rejected
The Obsidian Silver Glass attempt introduced a dark graphite navigation focus fill. The owner's device photo showed the focused navigation capsule becoming a visually heavy near-black block. This conflicts with the accepted Universal Glass identity and is rejected. A passing CI result does not make this visual decision acceptable.

## What is retained from PR #162
- Universal Home glass surface material: gradient, sweep reflection, outer rim and inner rim.
- Existing GTVH Glider remains the only moving navigation/app focus owner.
- Original silver translucent focus treatment:
  - `TvUiTokens.FOCUS_HALO = 0x35DCE5ED`
  - `TvUiTokens.FOCUS_BORDER = 0xFFDCE5ED`
- Existing `GliderFocusDrawable` layer order: capsule glass, moving focus overlay, then label/icon.
- Existing popup family material and control treatment.
- All approved geometry, typography, focus graph, DPAD behavior, animations, Dock horizontal/vertical motion and application layout.

## Re-audit findings from exact PR #162 source
### Navigation
- The nav capsule uses `UniversalGlassMaterial.surface(...)`.
- Each nav tab paints the shared `Glider` through `GliderFocusDrawable`.
- The original `Glider.DARK` mode uses a translucent silver halo and a silver rim; it is not the dark graphite block introduced by PR #163.
- The moving focus remains one shared Glider; no alternate focus system is permitted.

### Popup
- Quick Settings and Wi-Fi use `UniversalGlassMaterial.panel(...)`.
- The panel foundation is `PANEL_SHADE = 0xB31D1D1D` with the Universal glass drawable above it.
- Quick Settings explicitly disables backdrop dim in PR #162; Wi-Fi applies its own dim policy.
- The visible text bleed in the owner's photos must be diagnosed against actual panel composition, window dim and wallpaper rendering before selecting a correction. Do not simply make the panel opaque or black.

## Fresh correction strategy — investigation before implementation
1. Compare the PR #162 Universal Home material source values and GTVH call sites; retain the same material identity.
2. Navigation bright-wallpaper case: investigate a restrained surface-level contrast adjustment and text legibility treatment that does not replace the silver focus halo, alter label geometry, or add a black focus fill.
3. Popup bright-wallpaper case: isolate how much bleed comes from the window behind the popup versus the popup glass itself. Consider a restrained backdrop scrim and a controlled increase in smoked-glass panel density while retaining visible reflections/rims.
4. Validate bright and dark wallpaper cases independently. Avoid one global tint that fixes one scene but damages the other.
5. Implement only the smallest visual-only change after the source audit and compare before/after captures.

## Hard-locked invariants
Do not modify:
- Any capsule, popup, card, app icon or Dock width/height/position/radius/spacing.
- DPAD routing, focus graph, focus ownership, Glider motion/spring/animation timing.
- Horizontal or vertical Dock/carousel movement, move mode, app ordering or reveal behavior.
- Popup content order, typography sizes, button/capsule dimensions, anchoring or functional actions.
- Wallpaper renderer, weather/AQI, Islamic shelf content or prayer timetable.
- Universal Glass source material identity: gradient, sweep reflection, silver rims and restrained highlights.

## Validation gates
- Add regression tests for material and geometry invariants.
- Run exact-head CI on this fresh branch.
- Inspect the QA APK on Mi TV Stick (1920x1080 / 320dpi) using both bright and dark wallpapers.
- Capture Home navigation idle/focused and Quick Settings/Wi-Fi popup states.
- No merge until owner accepts the actual device appearance.

## Current status
- Fresh branch created from PR #162 HEAD.
- No PR #163 code is present on this branch.
- Re-audit of exact baseline source is underway.
- No visual fix has yet been claimed as complete.
