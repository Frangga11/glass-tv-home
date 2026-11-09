# GTVH-GLOBAL-FROSTED-SILVER-GLASS-001 — Source Audit & Design Contract

**Date:** 9 November 2026  
**Repository:** `Frangga11/glass-tv-home`  
**Baseline:** PR #103 HEAD `a3d697c793e23a59f9f9953e2074d83daa210cf9`  
**Work branch:** `feature/global-frosted-silver-glass-001`  
**Base:** exact PR #103 HEAD above  
**Status:** G1 inventory established; initial shared design system and prototype implementation are in place. Full stale-resource sweep, rendered visual validation and device acceptance continue.

## 1. Change-control boundary

This workstream is a visual-only reskin. Preserve view IDs, click/focus/key listeners, event handlers, DPAD routing, state ownership, data and persistence, wallpaper behavior, and established component geometry. Do not alter PR #103 or its ancestry. The new branch is a child of PR #103 so its Arabic RTL, Ayah spacing, prayer and cumulative Home corrections remain in the dependency chain.

Do not merge, retarget, flatten, reset, force-push, install, or change the default launcher as part of this work. A functional issue found outside the visual scope is recorded separately rather than silently fixed.

## 2. Baseline inventory

At the exact PR #103 source tree, the repository contains:

| Inventory | Count |
|---|---:|
| App layout XML files | 17 |
| App drawable resource files (all drawable density/variant folders) | 121 |
| App values resource XML files | 19 |
| App Java source files | 100 |
| App Java unit-test files | 59 |
| Python regression-test files | 35 |

These are file counts, not counts of distinct glass components. Icon artwork and non-glass assets are not to be restyled merely because they are drawable resources.

## 3. Glass surface inventory

### Home and shared shell
- `activity_main.xml`: Home header/navigation, location and altitude, weather/AQI, wallpaper/hero text, Islamic shelf, prayer timetable, selected Ayah, app grid and dock.
- Shared rendering: `GlassDrawable`, `GlassRim`, `Backdrop`, `Glider`, `GlassView`.
- Resources: `bg_floating_dock.xml`, `header_glass_pill_bg.xml`, `header_nav_tab_bg.xml`, `glass_panel.xml`, `pill_bg.xml`, `tile_bg.xml`, `bg_app_pill_focused.xml`, `bg_app_pill_unfocused.xml`, `bg_media_app_pill.xml`, `bg_track_info_selector.xml`, `bg_np_round_btn.xml`, `prayer_pill_bg.xml`.

### Modal, popup and profile surfaces
- Runtime surfaces: `TvDialogStyle`, `QuickSettingsDialog`, `WifiSettingsDialog`, `ModernSettingsDialog`, `UserSwitchDialog`, `WallpaperSettingsDialog`, `LocationActivity`.
- Resources: `bg_glass_dialog.xml`, `selector_glass_dialog_button_bg.xml`, `selector_glass_dialog_list_item_bg.xml`, `bg_dialog_btn.xml`, `input_row_bg.xml`, `bg_location_state_pill.xml`, `bg_location_zone_card.xml`.

### Weather and recent-app surfaces
- Layouts: `activity_weather.xml`, `item_weather_daily.xml`, `item_weather_daily_row.xml`, `item_weather_hourly.xml`, `activity_recent_apps.xml`, `item_recent_app.xml`, `item_recent_close_all.xml`.
- Resources: `weather_card_bg.xml`, `weather_pill_bg.xml`, `bg_weather_tab_active.xml`, `selector_daily_row_bg.xml`, `bg_recent_apps_glass.xml`, `bg_recent_card.xml`, `bg_circle_close_all.xml`.

### Media, notification and companion surfaces
- Layouts: `dialog_media_hub.xml`, `dialog_ota_coffee.xml`, `dialog_card_prompt.xml`, `popup_cards.xml`.
- Runtime: inline media card in `MainActivity`; floating Azan glass card in `AzanActivity`.
- Companion installer resources: `launcher/installer/.../bg_btn_glass.xml`, `bg_card_glass.xml`. These are included in the visual audit as a separate companion surface; installer behavior is outside the launcher reskin.

## 4. Shared foundation findings

1. `TvUiTokens.java` and `res/values/colors.xml` already define a shared semantic palette, but the current structural surfaces are predominantly deep blue.
2. `GlassRim.java` currently includes violet and gold thin-film colors. Those accents conflict with the requested neutral silver identity.
3. `GlassDrawable.java` uses a cached blurred wallpaper fill, an even veil, and a rim. The optional-glass-off path retains an opaque surface and border; preserve this fallback contract.
4. `Backdrop.java` precomputes wallpaper blur at reduced resolution and reuses cached output. Android 11 does not support `RenderEffect`; do not add live blur or a new dependency.
5. `TvDialogStyle` centralizes modal dimming, bounds and fallback material. Preserve its sizing, gravity and dimming behavior unless a separate measured visual defect is proven.
6. Dynamic dialogs already use `TvUiTokens` in many places, but some hard-coded colors and standalone drawable selectors remain inconsistent and need controlled reconciliation.

## 5. DPAD geometry finding and applied controls

- `AppAdapter.FOCUS_SCALE = 1.07f` is used both by focus zoom and by tile-width reservation. Do not simply change this shared constant: doing so would also change tile geometry. Separate the visual focus scale from the existing layout reservation.
- `TvUiTokens.CARD_FOCUS_SCALE` and `NAV_FOCUS_SCALE` are 1.015f and feed multiple dialog/header focus animations.
- Home weather-pill focus also applies the card scale.
- `WeatherActivity` uses a scale callback, while card focus currently remains at 1.0.
- Selected-state and focus-state treatments overlap in some selectors and need to become visually distinct without changing selection ownership or focus listeners.

**Applied controls on the work branch:**
- AppAdapter visual FOCUS_SCALE is 1.0; separate LAYOUT_FOCUS_RESERVATION_SCALE remains 1.07 and continues to size the tile grid.
- Focused app labels no longer translate vertically.
- TvUiTokens CARD_FOCUS_SCALE and NAV_FOCUS_SCALE are 1.0, so dialog, navigation and card focus animations preserve measured bounds.
- Glider DARK and GLASS states and GlassView use an inset Electric Cyan outline with a restrained translucent cyan fill.
- Location, settings, media, prayer, dialog and weather selectors distinguish cyan focus from silver selected state.
- MainActivity dynamic Azan settings and information-action rows now use shared silver/cyan focus surfaces; existing click and focus listeners remain intact.

**Design constraint:** visual focus must not scale, translate, resize or reposition its component. Preserve the current layout reservation so the pre-existing tile geometry does not move.

## 6. Implemented shared design tokens — initial palette

| Token | Proposed value | Purpose |
|---|---|---|
| Shell fallback | `#FF2C333B` | Cool neutral shell |
| Surface fallback | `#FF35404A` | General silver-grey surface |
| Focused surface | `#FF3F4B56` | Neutral focus fill; not selected state |
| Selected surface | `#FF4C5C68` | Persistent selected/active state |
| Idle surface | `#FF2D3741` | Idle interactive surface |
| Input surface | `#FF333E48` | Readable input surface |
| Popup fallback | `#FF303A44` | Opaque modal fallback |
| Dock fallback | `#FF242E38` | Distinct opaque dock fallback |
| Silver border | `#FF9EABB8` | Structural fallback border |
| Primary text | `#FFFFFFFF` | Primary contrast |
| Secondary text | `#FFD6E0E8` | Cool silver secondary text |
| Electric Cyan | `#FF00E5FF` | Focus identity |
| Selected border | `#99DCE5ED` | Silver selected-state marker |
| Glass veil | `#88343E48` | Neutral wallpaper veil |
| Focus halo | `#4000E5FF` | Restrained cyan reflection |

The Java and XML token sources are synchronized and covered by regression tests. Keep opaque fallbacks for accessibility and when decorative glass is disabled. Blur and transparency remain surface-specific; do not force one alpha across every component.

## 7. Initial implementation and exact-head validation

**PR:** #104 OPEN / DRAFT / UNMERGED.  
**Branch:** `feature/global-frosted-silver-glass-001`.  
**Base:** PR #103 exact HEAD `a3d697c793e23a59f9f9953e2074d83daa210cf9`.  
**Last CI-validated HEAD:** `7535a311639c07ab77abd004664510fca216b00b`.

- Python regression: **223 tests passed, 1 skipped**.
- Android unit tests and release build: **BUILD SUCCESSFUL**.
- Exact CI run: [#1702](https://github.com/Frangga11/glass-tv-home/actions/runs/42599811383) — SUCCESS.
- Release APK: **1,627,069 bytes** (1,588 KB).
- Artifact ID: `13665501684`; artifact ZIP SHA256 `a570dc0585502c75cbf0862e3c8546263eccb6f5e3f6886cee7c8761ac6a5684`.
- Extracted APK SHA256: `e5e5dfbcff4a2944c4bc8d3be38deb27743b1b358b542bf82a6a0effab530054`.
- The APK is a CI build for owner testing only; no physical device test has been performed.

**Interpretation:** source build and automated suites pass for the exact validated HEAD. Visual QA, full functional regression, runtime DPAD QA, performance profiling and physical Mi TV Stick acceptance remain pending.

## 8. Remaining prototype and quality gates

Remaining prototype order:
1. Home shared header/navigation and dock material.
2. Wi-Fi modal.
3. Information panel / Weather card.
4. Validate normal, focused, selected, pressed and disabled states independently.
5. Validate bright, dark and mixed wallpapers before expanding to the remaining surfaces.

Required automated contracts:
- Java/XML token parity.
- Minimum text and focus contrast.
- Opaque distinct dock/popup fallback surfaces.
- Focus scale remains 1.0 while layout reservation remains unchanged.
- Focus and selected remain distinct.
- Existing view IDs, layout bounds and interaction contracts are preserved.
- Arabic shaping, RTL and physical right alignment remain unchanged.
- No real-time blur or unbounded animation allocation.

No visual, functional, DPAD, performance or device gate may be marked PASS without corresponding evidence. Rendered 1920×1080 review and physical Mi TV Stick acceptance remain pending until actually performed.
