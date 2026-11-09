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
| App drawable resource files (all drawable density/variant folders) | 112 |
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
**Last exact-head CI validation:** HEAD `03408e7585e1cacd3fe32727502bca61752c5438`, run #1703 — SUCCESS.

- Python regression: **223 tests passed, 1 skipped**.
- Android unit tests and release build: **BUILD SUCCESSFUL**.
- Exact CI run: [#1703](https://github.com/Frangga11/glass-tv-home/actions/runs/42600150812) — SUCCESS on HEAD `03408e7585e1cacd3fe32727502bca61752c5438`.
- Release APK: **1,627,069 bytes** (1,588 KB).
- Artifact ID: `13664798205`; artifact ZIP SHA256 `34da6ef190bde1aa7a49c3bc7eb564e969d0107c6e8f4e514b957486784f3a2e`.
- Extracted APK SHA256: `e146f9e80c333acbb72c34bb64f96b2be385583e60400239ab04b5c6f5474671`.
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


## 9. Force-close recovery, corrective implementation and exact-head validation — 9 November 2026

**Recovery point:** continue from the existing PR #104 branch. No branch reset, force-push, rebase, retarget, merge, ADB, device install, or launcher-setting mutation was performed.

**Corrective source changes after the first failed CI:**
- Corrected TvFocusIndicator.createOvalStateBackground() so normalFill is an int; this resolved the Android Java float-to-int compile error.
- Updated the legacy Home weather/header regression contract. It now asserts the shared header listener includes weatherAirQuality, increments the focus request generation, moves the top highlight, and does not scale/resize the header control. It no longer expects the removed per-weather scale behavior.
- Replaced the wallpaper interval row's locally built state list with the shared selectable state background. Disabled, pressed, focused and selected states now use shared semantics; active selection remains independent from DPAD focus.
- Removed no-op scaleX(1.0f)/scaleY(1.0f) animations from WeatherActivity; focus changes elevation only.
- Completed dynamic settings/profile focus treatment: settings search, edit-name cards, settings cards/rows, Done/Close actions and profile switch surfaces use shared silver/cyan stateful backgrounds. Active profile state remains separate from cyan DPAD focus.
- Added/updated regression coverage for wallpaper interval selection, shared focus-state helpers, dynamic profile selection, weather/header focus, recent-app cards, disabled/pressed/focused state order and Islamic typography preservation.
- Recent Apps and Close All no longer zoom on focus; Recent Apps exposes a dedicated cyan focus outline. Media popup and weather controls remain geometry-stable.
- Dynamic Ayah and prayer content retain RTL/right alignment and their data/navigation behavior while visual color treatment uses the cool-silver token system.

**First failed exact-head CI and disposition:**
- CI #1753 / run 42605112203 failed for two identified causes: the oval state helper's normalFill type was wrong; a legacy weather test asserted the old focus-scale branch.
- Both causes were corrected in source/test contracts. The test suite was not disabled or weakened; new assertions validate the current no-geometry-change design.

**Corrective exact-head evidence:**
- Source HEAD validated: 263b693b5be7e05fa37949259142f5bc8f091699.
- CI #1757 / run [42605527816](https://github.com/Frangga11/glass-tv-home/actions/runs/42605527816): **SUCCESS**.
- Lists/tools/docs: **SUCCESS**.
- Python regression: **235 tests passed, 1 skipped**.
- Android unit tests and release build: **BUILD SUCCESSFUL**.
- Release APK build diagnostic: **1,628,165 bytes (1,590 KB)**. The size report is diagnostic, not a blocking threshold.
- Workflow uploaded artifact ID **13668281444**, named glass-tv-home-dock-visual-parity-6aeffba4caf3eb37b2cce6ae7d70daaf50e5eac7, size **1,429,132 bytes**. This is the Home layout test artifact; do not label it as the release APK.
- Exact PR #104 remains **OPEN / DRAFT / UNMERGED**. No release APK has been installed on the Mi TV Stick.

**Still pending / not claimed PASS:** rendered 1920×1080 screenshot review, bright/dark/mixed wallpaper readability comparison, full end-to-end function regression, live DPAD focus-path test, performance/GPU profiling, and owner-authorized physical Mi TV Stick acceptance. No real device test has been performed in this recovery pass.

**Current disposition:** source fixes and exact-head automated CI are PASS; visual, functional, performance and device acceptance remain PENDING. PR #104 remains Draft/unmerged. Overall workstream remains **BELUM FINAL** until all required acceptance evidence is recorded.


## 10. G1/G4 continuation — Dock breathing rim token drift correction — 9 November 2026

**Finding:** the resource/source cross-check identified one remaining decorative dock focus/rim path outside the initial changed-file set: `DockBreathingBorderView.java` used hard-coded RGB `(56, 189, 248)` / `#38BDF8` for both the atmospheric glow and lower edge of the rim gradient. This was inconsistent with the approved shared Electric Cyan token `TvUiTokens.ACCENT_CYAN = #00E5FF`.

**Correction on the same PR #104 branch:**
- Both glow and gradient cyan now derive their RGB channels from `TvUiTokens.ACCENT_CYAN`.
- No change to animator duration/repeat/interpolator, alpha envelope, stroke widths, bounds, corner radius, drawing order, visibility lifecycle, IDs, listeners, dock geometry or DPAD behavior.
- Added `test_dock_breathing_rim_uses_shared_electric_cyan_token` to prevent recurrence and verify the shared token is used for both paint paths.

**Commits:** source correction `0f90e90cb0d99ccd9c34ec1e36f5976af4fb9120`; regression contract `27eca44f42b17fc9b3f7cf711b5bbf488ef4e12a`.

**Exact-head automation:** CI #1760 / [run 42608935919](https://github.com/Frangga11/glass-tv-home/actions/runs/42608935919) **SUCCESS** on exact PR HEAD `27eca44f42b17fc9b3f7cf711b5bbf488ef4e12a`. Both required jobs succeeded: Python unittest discovery and Android `testReleaseUnitTest assembleRelease`. Artifact ID `13670145336`, uploaded as the workflow's Home layout visual-parity artifact (1,429,060 bytes). This artifact is not separately represented as an independently downloadable release APK in the evidence collected here. Exact APK byte size was not captured in the available run summary; do not copy the previous-head size forward as if it were measured for this head.

**G1 inventory cross-check:** 141 app drawable/values files were enumerated against the PR #103 baseline; 36 differ and 105 are unchanged. Unchanged icons, weather artwork, localization strings and resources already using semantic tokens are not defects merely because they were untouched. A complete stale-color/style classification of every unchanged item remains open; this is a scoped inventory result, not a full audit PASS.

**Remaining gates:** full stale-resource/source sweep; matched rendered 1920×1080 review on bright/dark/mixed wallpapers; full end-to-end runtime functional regression; live DPAD path; GPU/performance profiling; physical Mi TV Stick acceptance only after G1/G4 completion and explicit owner authorization.

**Disposition:** Dock rim cyan token drift corrected; exact-head automated CI PASS. Source audit and global implementation remain PARTIAL; visual/runtime/performance/device acceptance remain NOT RUN / PENDING. PR #104 remains OPEN / DRAFT / UNMERGED; overall workstream **BELUM FINAL**.


## 11. G1/G4 continuation — exact-head CI closure and hard-coded color classification — 9 November 2026

**Exact-head automation closure:** PR #104 HEAD `4575b09b262ad664f6766ce286d5e5ed90e92780`; CI #1761 / run [42609316862](https://github.com/Frangga11/glass-tv-home/actions/runs/42609316862) is now **SUCCESS**. Both jobs completed successfully:
- Lists, tools and docs.
- Android launcher unit tests, `testReleaseUnitTest assembleRelease`, APK size diagnostics and workflow artifact publication.

This CI run validates the current PR HEAD, including the source correction already validated at source HEAD `27eca44f42b17fc9b3f7cf711b5bbf488ef4e12a` and the subsequent audit-document update. The published artifact remains the workflow's Home layout visual-parity artifact; do not mislabel it as a separately verified release APK. Exact release APK byte size was not extracted from the run evidence.

**Java hard-coded color classification pass (targeted source review):**
- `TvUiTokens.java` and `values/colors.xml` contain the shared neutral/cyan semantic palette and its XML mirror. Existing parity contract remains the automated guard.
- `GlassRim.java` and `Glider.java` contain intentional white/silver specular highlights, translucent window-light gradients and black shadow values. These are material-lighting/shadow layers, not competing interactive focus palettes; no change made without rendered evidence.
- `RecentAppsAdapter.java` uses per-app brand/icon colors and shadow colors; these are identity/rendering values, not global surface/focus tokens.
- `AppAdapter.java` derives icon/background colors from app imagery and uses a neutral fallback; not a global UI palette path.
- `ModernSettingsDialog.java` uses semantic success/warning/danger colors for state labels, including green success/active values; retained as semantic status colors rather than converting them to cyan.
- `LocationActivity.java` uses white/silver text, a green current-location badge, and translucent neutral card fill/border. The current-location green is semantic status; card fill/border are a localized glass fallback. The surface state helper already uses shared tokens for focused/selected/pressed/disabled states. No behavioral or appearance change made without screenshot comparison.
- `AzanActivity.java` uses a warm gold takbir accent as a distinct devotional content accent and neutral glass/silver card treatment; retained intentionally, not treated as a focus color.
- `DockBreathingBorderView.java` no longer contains the legacy cyan RGB drift; both glow and rim derive from `TvUiTokens.ACCENT_CYAN`.

**Disposition of this pass:** no additional source correction was justified by static color inspection alone. Brand identity, semantic status, devotional content accent, specular reflection, shadow and translucency are explicitly classified rather than mechanically recolored. No IDs, listeners, geometry, navigation, data, persistence, RTL or lifecycle behavior changed in this pass.

**Open gates remain:** full classification of all 141 drawable/values resources is not yet complete; rendered 1920×1080 review on bright/dark/mixed wallpapers, end-to-end runtime functional regression, live DPAD path, GPU/performance profiling and owner-authorized physical Mi TV Stick acceptance remain NOT RUN / PENDING. CI PASS does not imply visual/device PASS.

**Current status:** PR #104 OPEN / DRAFT / UNMERGED; current HEAD `4575b09b262ad664f6766ce286d5e5ed90e92780`; exact-head CI #1761 PASS. Overall workstream **BELUM FINAL**.


## 12. G1/G4 continuation — changed-resource diff and geometry-risk scan — 9 November 2026

**Exact current-head CI closure:** CI #1762 / run [42610588965](https://github.com/Frangga11/glass-tv-home/actions/runs/42610588965) completed **SUCCESS** on exact PR HEAD `05d50e73e7da41fa900c5ba8e3c86f60392750ba`. Both Lists/tools/docs and Android launcher unit-test/release-build jobs passed. The Android job also completed read-only APK diagnostics and published the workflow Home layout test artifact. This validates the latest audit-note-only PR HEAD; no application source changed after source HEAD `27eca44f42b17fc9b3f7cf711b5bbf488ef4e12a`.

**Changed XML resource diff review:** the PR patch contains 37 changed XML resource/layout files. Their visual changes generally fall into the intended neutral silver/cyan material and state system: neutral fallback fills, silver borders, cyan focus strokes, disabled/pressed/selected states, readable secondary text and opaque installer fallbacks. The XML token mirror remains consistent with the shared semantic palette; many translucent values are component-specific alpha variants, not a second focus hue. Installer resources are a separate Android module, so they use literal equivalents where the app module's `@color` resources are unavailable.

**Geometry/style risk flags requiring evidence before closure:**
- `drawable/bg_ram_pill.xml`: intrinsic drawable padding changed from 20dp horizontal / 9dp vertical to 12dp / 5dp. This is a real content-inset/geometry delta, not merely a color change. MainActivity's Java source has no direct reference; complete repository reference/use verification is still needed. Do not silently treat this as approved. Next pass must confirm whether it is referenced by a layout/other class and either restore the original padding or record an explicit, owner-approved exception supported by visual evidence.
- `drawable/bg_recent_card.xml`: corner radius changed from 24dp to 26dp, matching the new recent-card focus drawable. This may be a deliberate radius unification, but must be checked against the approved card geometry in the rendered comparison.
- `layout/activity_main.xml`: the static Ayah text color changed from warm gold `#FFFFC45A` to primary white, and its shadow changed from warm brown to black. Arabic layout direction, gravity and typeface were not changed in this diff. Validate legibility and visual intent on bright/dark wallpaper before calling this final.
- `installer/res/values/styles.xml`: theme `android:windowBackground`, status bar and navigation bar retain `#0C0E14` although the installer content root now uses shell `#2C333B`. This may be an intentional pre-content startup fallback; confirm no visible transition/flash during rendered or device acceptance.

**Other geometry scan:** the source diff removes focus scale/translation effects from multiple controls and insets outlines so focus is drawn inside existing bounds. These changes support the geometry-stable focus contract. No Home grid/dock parent geometry, view IDs, click listeners or data wiring was altered in this resource review. The potential `bg_ram_pill` padding delta is kept as an explicit open item, not silently accepted.

**Disposition:** exact-head automated CI PASS; changed-resource review PARTIAL with the above four visual/geometry decisions requiring resolution/evidence. The 141-resource full inventory still has 105 unchanged entries that require semantic classification; unchanged is not automatically a defect. Rendered 1920×1080 review, full runtime function/DPAD test, GPU/performance profiling and owner-authorized Mi TV Stick acceptance remain NOT RUN / PENDING. PR #104 remains OPEN / DRAFT / UNMERGED. Overall **BELUM FINAL**.
