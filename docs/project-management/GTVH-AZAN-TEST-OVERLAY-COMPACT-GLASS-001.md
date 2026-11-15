# GTVH-AZAN-TEST-OVERLAY-COMPACT-GLASS-001

## Scope

Owner-requested refinement of the floating Azan playback/test overlay shown when the user runs **Tes Suara Azan**. This is the floating overlay implemented by `AzanActivity`, not the manual Waktu Sholat settings popup.

Baseline: `de3caacebef78fa51753c14c93ef4d1bc7251a4e` (PR #135 accepted visual baseline).

## Owner-locked constraints

- Reduce the overall card to the currently owner-approved target percentage recorded in the latest refinement section.
- Keep the existing top-centered placement and the 40dp top offset exactly unchanged.
- Keep the internal content order and relative placement in one frame: Arabic takbir, vertical divider, then title/location, prayer name/time, and hint.
- Match the approved Waktu Sholat popup's shared GlassDrawable, veil, blur source, and 20dp rim.
- Do not modify the Waktu Sholat popup, Home screen, wallpaper, Dock, navigation, schedule, Azan playback logic, sound assets, or any other component.

## Source audit findings

The screenshot's card corresponds to `AzanActivity`. The previous surface used a separate opaque `GradientDrawable` tint (`0xD936414B`) and an optional Android window blur path. The approved Waktu Sholat popup instead uses the project `GlassDrawable` and the shared `Backdrop.Fill` image, so the two surfaces were visually inconsistent, especially on Android TV 11 where cross-window blur is not available.

## Implemented change

- Applied a uniform `CARD_SCALE = 0.93f` to card typography and internal dp spacing/padding. A uniform 0.93 linear scale reduces projected card area by about 13.5%; the supplied photo's existing footprint is approximately 17% of the visible screen, bringing the result close to the requested 15%. Photo perspective means the percentage is an estimate, not a measured TV framebuffer result.
- Kept `Window.setGravity(TOP | CENTER_HORIZONTAL)` and `lp.y = dp(40)` unchanged. The popup remains top-centered at the same anchor and offset.
- Replaced the independent opaque tint with the same `GlassDrawable(card, 20 * density, density, null, 0.5f)` used by the Waktu Sholat popup.
- Registered the floating card with `Backdrop.watch(card)` so it redraws using the shared blurred Home backdrop.
- Retained a transparent foreground focus outline, click-to-dismiss, Back/OK dismissal, audio focus, sound selection, playback, and completion lifecycle.
- No other source files or global glass tokens are changed.

## Validation boundary

- Added source contract tests for glass parity, 0.93 proportional scale, unchanged top position, internal content order, focus, dismissal, and playback lifecycle.
- Exact-HEAD CI/build must pass before producing a fresh QA APK.
- Visual acceptance still requires a new 1920×1080 capture to confirm the popup occupies approximately 15% of the screen, the Arabic/title/time/hint remain legible in one frame, and glass blur/veil/rim match Waktu Sholat.
- Physical Mi TV Stick testing is not claimed until performed. No merge or installation is implied.


## 11% screen-area refinement — 15 November 2026

Owner requested a further reduction from the previous approximately 14.6% estimated footprint to approximately 11% of the main screen area. This target was subsequently superseded by the 12% refinement below.

- Updated uniform card scale from 0.93 to 0.806 of the original pre-reduction dimensions.
- Calculation against the prior estimate: 14.6% × (0.806 / 0.93)² ≈ 10.97%.
- The popup's top-centered gravity and 40dp top offset are deliberately unchanged.
- Internal content order, one-frame composition, and proportional typography/spacing are preserved.
- The glass corner radius, focus outline radius/stroke, and elevation are now scaled with the card so those details remain proportional. GlassDrawable remains the same project material and Backdrop lifecycle remains unchanged.
- No changes to playback, audio focus, dismissal, prayer popup, Home UI, or global glass system.
- 11% is a geometry estimate derived from the supplied photo and previous area estimate, not a framebuffer measurement. Final confirmation still requires a 1920×1080 capture on the target TV.


## CI correction and 11% acceptance contract — 15 November 2026

The first CI run after the 11% change reported one failing contract assertion: the test still expected the earlier fixed 20dp glass corner radius. The implementation had intentionally scaled that radius with the card to preserve the same relative shape. The contract is corrected to require the scaled 20dp base radius, scaled focus outline, and scaled elevation. No production behavior was changed by this test-only correction.

The 11% revision superseded the earlier 15% target at that checkpoint. It is now superseded by the 12% refinement below. The window remains top-centered at the same 40dp y-offset. CI and visual/device acceptance are still required; estimates are not direct screen-area measurements.


## 12% screen-area refinement — 15 November 2026

Owner requested a slight increase from the 11% target to approximately 12%, with changes restricted to scale only.

- Updated the uniform linear `CARD_SCALE` from `0.806f` to `0.843f`.
- Area estimate from the same prior photo-based baseline: 14.6% × (0.843 / 0.93)² ≈ 12.0%.
- The scale remains uniform across typography, internal dp spacing/padding, glass corner radius, focus outline, and elevation so proportions remain consistent.
- Popup anchor, top offset (`lp.y = dp(40)`), content order, glass material/backdrop, focus behavior, playback, audio focus, dismissal, and lifecycle are unchanged.
- Scope remains limited to `AzanActivity.java`, the focused contract test, and this checkpoint document. No other UI or audio components are modified.
- 12% remains a photo-based geometry estimate; exact screen-area and legibility confirmation require a fresh 1920×1080 capture and Mi TV Stick test.
