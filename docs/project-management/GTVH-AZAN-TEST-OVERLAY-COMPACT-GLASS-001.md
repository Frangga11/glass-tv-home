# GTVH-AZAN-TEST-OVERLAY-COMPACT-GLASS-001

## Scope

Owner-requested refinement of the floating Azan playback/test overlay shown when the user runs **Tes Suara Azan**. This is the floating overlay implemented by `AzanActivity`, not the manual Waktu Sholat settings popup.

Baseline: `de3caacebef78fa51753c14c93ef4d1bc7251a4e` (PR #135 accepted visual baseline).

## Owner-locked constraints

- Reduce the overall card to approximately 15% of the visible screen area.
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
