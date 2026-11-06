# Glass TV Home — TRIGGER 4 Prototype Visual Validation

**Document ID:** GTVH-UIR-T4-VISUAL-001  
**Date:** 6 November 2026  
**Scope:** Prototype-only visual validation against approved V01–V06 references  
**Target viewport:** 1920×1080, 16:9  
**Reference baseline:** P2 Global UI Design & Interaction Contract, V01–V06  
**Prototype inspected:** `prototypes/UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V4.html`  
**Prototype blob:** `081d48f35a9e0eb5228642909be7a5697e3cdb27`

## 1. Decision

# PROTOTYPE VISUAL: REVISION REQUIRED

The six approved reference panels were recovered from the saved Global UI Design & Interaction Contract and UI Refresh overview boards. The V4 HTML source was inspected against those references.

**Important evidence boundary:** this session did not produce a browser-rendered screenshot of V4 at 1920×1080. Therefore no pixel-difference, exact geometry, blur-radius, color-sampling, or screenshot-based legibility claim is made. The findings below are source-to-reference composition findings, not screenshot measurements. The six panels are available in the saved composite reference board; independent full-resolution source images for each panel were not present in the retrieved reference set.

V4 passes its previous static interaction/source QC, but that does not establish visual fidelity. Its current layout differs materially from the approved compositions, so it is not eligible for visual approval.

## 2. Reference recovery and design baseline

Recovered references:
- V01 — Home Screen / Beranda
- V02 — Weather & AQI Dashboard
- V03 — Profile Selector
- V04 — Quick Settings Popup
- V05 — Wi-Fi Popup
- V06 — Global Settings / Split Detail + Navigasi Cepat

Shared visual language visible in the references:
- Blue-tinted glass surfaces over a photographic wallpaper; wallpaper remains visible and legible.
- Cyan/blue active and D-pad focus accents, with restrained glow.
- Layered translucent panels, rounded corners, fine bright edge strokes and soft depth/shadow.
- Compact, high-information TV typography with clear title, body, metadata and status hierarchy.
- Familiar pictographic icons for navigation, connectivity, weather, settings, profile and actions.
- Popup surfaces remain anchored over the underlying screen; they do not replace the full-screen context.
- V06 uses a left navigation rail and a right detail workspace, with a fast, direct category-navigation model.

The P2 contract remains authoritative for shared tokens and interaction semantics. The reference boards are visual direction; text and exact data values shown in the concepts are illustrative unless separately specified by P1/P2.

## 3. Surface-by-surface comparison

| Surface | Reference composition | V4 source composition | Finding / required correction |
|---|---|---|---|
| **V01 Home Screen** | Full-bleed scenic wallpaper; compact top bar with time/date, navigation, Wi-Fi and microphone; location and weather/AQI chips; main greeting/location content; prayer-time/information card; wide bottom Dock with distinct app tiles. | Dark radial-gradient frame; global prototype tab strip; simple greeting and generic hero; no photographic wallpaper, location/weather chips, prayer-time card or branded visual Dock. | **Major deviation.** Rebuild the Home composition around the wallpaper and reference hierarchy. Keep Dock at the lower edge and preserve the top system/navigation strip. |
| **V02 Weather & AQI** | Dedicated compact dashboard card over/alongside the wallpaper; location/update header; prominent weather icon, temperature and feels-like/min/max; AQI status band; humidity, wind, pressure; hourly and seven-day forecast cards; pollutant and environmental details. | Full-page dashboard with text-only hero, twelve generic metric tiles, and forecast values compressed into text lines. No weather iconography or reference-style AQI band. CO/NO₂/SO₂ are combined in one tile. | **Major deviation.** Recompose into the reference card hierarchy; give AQI its own visual band; use weather/metric icons and distinct forecast cards; expose pollutant values as individually readable fields. Preserve the full Weather V4 data scope. |
| **V03 Profile Selector** | Centered floating glass selector over wallpaper, four horizontally arranged cards: active user, family, child and add profile; avatar imagery and a strong cyan selected state. | Full-page surface with a three-column grid: two generic profile buttons and one add-profile button; no avatar imagery or centered floating selector. | **Major deviation.** Use a centered four-card selector over the same wallpaper; distinguish focus from active profile; add avatar/icon placeholders consistent with the reference. |
| **V04 Quick Settings** | Anchored floating glass popup over Home wallpaper; close control; two-column action layout; Wi-Fi, restart, language, power, cache; wallpaper actions and system-settings handoff. | Full-page Quick Settings surface with a generic three-column grid; no popup anchoring, close control, background context or glass overlay. | **Major deviation.** Convert to an anchored popup with the reference's two-column grouping, glass material, backdrop dim and return-focus behavior. |
| **V05 Wi-Fi Popup** | Narrow centered glass popup over wallpaper; Wi-Fi header/toggle state, vertically stacked SSIDs, connected/locked indicators, scan refresh and Android Wi-Fi settings handoff. | Full-page Wi-Fi surface; connection hero and only two generic scan-result cards; no reference-style vertical network list, lock/connected icon hierarchy or popup context. | **Major deviation.** Convert to a popup with a compact vertical network list, connected and secured indicators, scan status, refresh and system handoff. Do not imply unsupported direct radio/network management. |
| **V06 Global Settings** | Persistent app header/status; left category navigation with icons and active cyan selection; right detail pane; profile/avatar card and concise setting rows/toggles; fast navigation across seven categories. | Basic 25%/75% two-column shell; text-only category buttons; right pane renders generic control-name buttons, without structured detail controls, profile/avatar card, iconography or the reference's top identity/status bar. | **Major deviation.** Retain split detail, add category icons and clear selected state, restore the identity/status header, and build real representative detail rows/cards/toggles rather than generic buttons. |

## 4. Cross-surface visual property audit

| Property | Reference expectation | V4 source evidence | Disposition |
|---|---|---|---|
| Composition / proportion | Six distinct layouts; full-screen Home/Weather/Settings and anchored popup/selector surfaces | V4 uses a common full-page shell for all six surfaces | **REVISION REQUIRED** |
| Position / safe area | P2 48 dp horizontal and 27 dp vertical safe margin; anchored popup placement | V4 frame uses 5% safe margins (96×54 px at 1920×1080, equivalent to 48×27 dp at 2× scale); popup anchoring not implemented for V04/V05 | Safe-area ratio source-aligned; popup placement **REVISION REQUIRED** |
| Color | Blue glass, cyan focus/selection, white text and readable muted metadata | Blue palette and cyan focus exist; reference-like selected/active treatment is incomplete | **REVISION REQUIRED** |
| Transparency | Visible wallpaper through glass panels | Main panel background is rgba(..., .94), close to opaque | **REVISION REQUIRED** |
| Blur | Background softened behind translucent glass | No `backdrop-filter` blur is defined in V4 | **REVISION REQUIRED** |
| Glow / depth | Restrained cyan edge glow and layered shadow | Glow is primarily applied to focused controls; ordinary panels use generic shadow | **REVISION REQUIRED** |
| Typography | Compact TV information hierarchy, consistent label and metadata sizing | Basic Segoe UI/Arial stack and generic global sizing; surface-specific hierarchy is incomplete | **REVISION REQUIRED** |
| Icons | Recognizable icons across weather, network, profile, navigation and actions | Mostly text labels and Unicode symbols; no consistent icon system | **REVISION REQUIRED** |
| Legibility | Strong contrast over photographic wallpaper, including daylight and long labels | No rendered wallpaper/brightness/zoom evidence; current high-opacity panels improve contrast but suppress the reference glass effect | **NOT PROVEN; recheck after correction** |

## 5. 1920×1080 target verification

- Prototype source declares a 16:9 frame and the target label “1920 × 1080”.
- At a 1920×1080 viewport, the 5% safe-area variables resolve to 96 px horizontally and 54 px vertically, matching the P2 48 dp / 27 dp safe margin only under the assumed 2× pixel-to-dp scale.
- The file has no evidence from an actual 1920×1080 browser render in this validation session.
- No screenshot exists for measurement of panel bounds, alignment, crop, line-wrap, typography, transparency, blur, glow or readability.
- A CSS declaration or viewport label is not proof that the browser rendered at the target resolution.

**1920×1080 rendering gate: NOT VERIFIED.**

## 6. Required prototype revision plan

1. Create a new versioned prototype; preserve V4 unchanged.
2. Establish a shared photographic wallpaper layer and a common blue-glass material with controlled translucency, backdrop blur, edge stroke and shadow.
3. Rebuild V01 Home around the reference's top strip, location/weather chips, hero/information card and bottom Dock.
4. Rebuild V02 as a compact, icon-led Weather V4 dashboard with a distinct AQI band, separate pollutant fields and forecast cards.
5. Rebuild V03 as a centered four-card profile selector over wallpaper.
6. Rebuild V04 and V05 as true anchored popups over the underlying Home surface, preserving close/Back/focus restoration.
7. Rebuild V06 as the reference's icon-led split-detail Settings workspace with seven fast-navigation categories and representative detail controls.
8. Render all six states in a real browser at exactly 1920×1080 and capture screenshots. Record viewport, browser/device scale factor and prototype commit.
9. Compare each screenshot side-by-side with its reference crop. Record measured panel bounds, offsets, proportions and text-fit observations; inspect color/material/blur/glow visually and sample pixels where feasible.
10. Correct deviations and repeat screenshots before requesting owner approval.

## 7. Gate status

| Gate | Status | Evidence |
|---|---|---|
| Six reference surfaces identified | PASS | V01–V06 recovered from saved Global UI design/reference boards |
| V4 static interaction QC | PASS (prior evidence only) | `UI_REFRESH_P3_V4_STATIC_QC_2026-11-06.md` |
| Source-to-reference composition comparison | REVISION REQUIRED | Section 3 of this report |
| Browser rendering at 1920×1080 | NOT RUN | No rendered screenshot captured |
| Screenshot-to-reference pixel/geometry comparison | NOT RUN | Requires rendered screenshots and per-surface reference crops |
| Typography / legibility / daylight check | NOT VERIFIED | Requires actual rendered screenshots at target scale |
| Owner visual approval | NOT REQUESTED | Revision first |
| P4 / mass implementation | BLOCKED | Must remain blocked until visual and interaction acceptance |

## 8. Change boundary

- This report is a prototype-validation artifact only.
- V4 remains unchanged and preserved.
- No production Android source, E02/E06 owner branch, PR, integration/main, APK, ADB or physical TV was changed.
- No implementation mass work or P4 activity is authorized by this report.

**Final output: PROTOTYPE VISUAL — REVISION REQUIRED.**  
**Next permitted action:** prepare a versioned visual revision and obtain real 1920×1080 browser screenshots before any visual PASS claim.

## 9. V5 prototype revision checkpoint

A new V5 prototype was created after the source-to-reference findings above. V4 remains unchanged.

- Artifact: [UI_REFRESH_TRIGGER4_VISUAL_PROTOTYPE_V5.html](prototypes/UI_REFRESH_TRIGGER4_VISUAL_PROTOTYPE_V5.html).
- V5 adds a wallpaper-led Home composition (including top clock/status/navigation, location/weather chips, information/prayer strip and bottom Dock); a centered four-card Profile Selector; centered glass-popup compositions for Quick Settings and Wi-Fi; and a more complete icon-led Global Settings split-detail shell.
- Shared glass CSS now includes translucent surfaces, backdrop blur and layered shadow. The scenic gradient and emoji avatars are temporary placeholders, not the original photo wallpaper or final icon/avatar assets.
- Static QC: PASS (one script, JavaScript syntax parse, no duplicate IDs, six surfaces and required V5 structures present). Evidence: [UI_REFRESH_TRIGGER4_V5_STATIC_QC_2026-11-06.md](UI_REFRESH_TRIGGER4_V5_STATIC_QC_2026-11-06.md).
- The V5 screenshot/rendering gate remains NOT RUN. Do not infer visual accuracy from the CSS or source. The popup overlay context, actual 1920×1080 geometry, text fit, color/material/blur/glow and wallpaper readability still require browser screenshots and side-by-side comparison.

**Updated disposition remains PROTOTYPE VISUAL — REVISION REQUIRED.** V5 is a candidate revision for rendering review, not an approved prototype.
