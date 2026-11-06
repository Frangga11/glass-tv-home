# Glass TV Home — UI Refresh P3 Visual Prototype & Interaction Validation

**Document ID:** GTVH-UIR-P3-001  
**Date:** 6 November 2026  
**Status:** IN PROGRESS — INTERACTIVE V1 VERSIONED; WALKTHROUGH AND DESIGN QA REMAIN  
**Frozen design baseline:** GTVH-UIR-P2-001, P2 contract COMPLETE  
**Reference inventory:** GTVH-UIR-P1-001  
**Source baseline for current-state comparison:** integration/b-series @ 562cde0751de93748eb4e0d4cdece4fa1672ac4d

## 1. Purpose

Translate the six approved visual references and frozen P2 contract into TV-oriented prototype layouts and D-pad interaction paths before production implementation. The generated concept boards are exploratory visual artifacts attached in the conversation; they are not source code, interactive prototypes, or evidence of runtime behavior. The P2 contract is authoritative whenever generated concept artwork conflicts with a platform or interaction rule.

## 2. Prototype design system

- Canvas: 960×540 dp, landscape 16:9.
- Safe content region: 48 dp left/right and 27 dp top/bottom; wallpaper remains full bleed.
- 8 dp spacing rhythm; 56 dp minimum interactive height; 64 dp preferred for primary actions.
- Primary/body text 16 sp; metadata 14 sp minimum; section headings 20–24 sp; main titles 28–36 sp.
- Semantic radii 12/18/24 dp; consistent glass/solid variants.
- Blue-tinted glass surfaces, cyan focus ring, white text, dark scrim over high-detail wallpaper.
- D-pad focus is distinct from selected/active/connected state; Back restores origin focus.
- Motion is restrained; reduced-motion removes nonessential scale/translation/looping effects.
- P2's Wi-Fi platform capability boundary overrides any inaccurate switch depiction in concept imagery.

## 3. Six surface prototype specifications

### P3-V01 — Home Screen
**Regions:** top status/time/date; primary navigation; Wi-Fi and voice affordances; location/elevation and compact weather/AQI pills; main contextual information area; prayer/verse shelf; bottom Dock and app grid.

**Focus path:** Home navigation → Wi-Fi/voice affordances → compact weather/AQI → primary content actions → Dock row → app grid. Left/Right stays within a row; Up/Down crosses predictable regions; Back from a popup restores the invoking pill.

**Prototype checks:** full-bleed wallpaper with safe content margins; long date/location; unavailable location; no weather/AQI; no prayer data; no installed apps; long app labels; media absence; focus never overlaps dynamic content.

**Known architecture issue:** Dock must be an independent presentation region, not inferred from first N positions in the app grid. This is an implementation constraint for E06.

### P3-V02 — Weather & AQI Dashboard
**Regions:** location/update header; current condition and temperature; feels-like/min/max; AQI/category/explanation; primary metric cards; hourly forecast; seven-day forecast; secondary V4 detail region.

**Data scope:** preserve full locked V4 fields: current temp/condition/feels-like/min-max; humidity; wind speed/direction; pressure; dew point; visibility; sunrise/sunset; moon phase; rainfall/rain probability; UV index/category/recommendation; AQI value/category; PM2.5, PM10, CO, NO₂, SO₂; existing source/update/radar fields per V4.

**Focus path:** close/back → location/update controls → primary condition → AQI → primary metrics → hourly cards → daily cards → secondary detail region. Refresh must not steal focus.

**Prototype checks:** stale data timestamp; offline; no location; source unavailable; AQI unavailable; provider omits pollutant; large values/units; long location name; seven-day forecast scroll; secondary detail scroll; no metric silently represented as zero.

### P3-V03 — Profile Selector
**Regions:** title; horizontal profile cards; active profile state; Add Profile card.

**Focus path:** open selector → initial focus on active profile (or first available) → Left/Right through profiles → Add Profile → creation flow; Back returns to Home and restores origin focus.

**Prototype checks:** no profile/default invariant; one profile; many profiles; active vs focused visual distinction; add cancellation/failure; child profile; Child Mode/PIN boundary; profile unavailable; focus restoration after switching.

**Owner:** E02. Full rename/avatar/delete/locked apps/hidden apps/PIN management stays in Global Settings Profile detail.

### P3-V04 — Quick Settings
**Regions:** anchored title/close; two-column Wi-Fi/restart/language/power actions; cache cleanup; Wallpaper section with Next Photo and interval; Glass TV Settings; Android TV Settings.

**Focus path:** open from Settings pill → first logical action → row/column traversal → wallpaper actions → settings handoff → close; Back closes popup and restores Settings pill focus.

**Prototype checks:** anchor near screen edges; focus on close; destructive confirmation; restart/power action failure; cache busy/success/failure; Next Photo unavailable with one wallpaper; Next Photo success only after wallpaper changes; interval label localization; Android Settings intent unavailable.

**Next Photo:** visual target is approved. P4 must connect it to E08's canonical wallpaper controller; do not implement a second wallpaper collection/rotation source.

### P3-V05 — Wi-Fi Popup
**Regions:** title and current Wi-Fi status; connected SSID card; conditional nearby scan results; security/signal metadata; last scan time; Rescan; Manage Wi-Fi (Android Settings handoff).

**Critical visual correction:** do **not** draw or ship a direct in-app Wi-Fi master switch. Android 10+ target-SDK restrictions mean ordinary apps cannot enable/disable Wi-Fi directly. The current concept-board toggle depiction is rejected as nonconforming. Replace it with a status indicator and clearly labelled “Kelola Wi-Fi” system handoff.

**Conditional scan behavior:** scan list appears only when required permissions are granted and Location services are enabled. Show scanning state; preserve last successful results as stale after throttling/failure; show timestamp; prevent repeated D-pad activation from spamming scan requests. Selecting a network hands off to Android Wi-Fi Settings for joining. No direct saved-network enumeration, password persistence, forget action or app-side radio toggle.

**Focus path:** connected network/status → scan results (if available) → Rescan → Manage Wi-Fi → close. Focus remains visible during refresh; list scrolls with focused row.

**Prototype checks:** Wi-Fi off; permission denied; Location off; no scan results; scan throttled; stale results; duplicate SSIDs; open/secured network; system Settings intent unavailable; Back from system handoff.

### P3-V06 — Global Settings Split Detail
**Regions:** top product/device status; left category navigation; right content pane; profile preview/detail rows; independent right-pane scroll.

**Canonical categories:** Profil; Layar Utama; Tampilan; Widget & Informasi; Animasi & Efek; Layar Siaga; Pengaturan Perangkat.

**Focus path:** category nav → right pane initial control → control-level navigation → next category → search result → exact control focus; Back restores prior category/scroll/focus context.

**Prototype checks:** active category vs focus; right pane scroll; long translated labels; disabled/permission-limited controls; search no-result; search result for individual setting; switching category during scroll; close/back focus restore.

## 4. Cross-surface interaction validation plan

| ID | Scenario | Expected result | Status |
|---|---|---|---|
| P3-01 | Open/close each popup with D-pad and Back | History-based return and invoking-control restoration implemented; manual walkthrough pending | Implemented / review pending |
| P3-02 | Traverse all controls without touch | Spatial nearest-direction focus implemented; edge/dead-end walkthrough pending | Implemented / review pending |
| P3-03 | Long label and large value | No clipping/overlap; layout expands or scrolls | Prototype review pending |
| P3-04 | Bright and dark wallpaper | Text and focus remain legible | Prototype review pending |
| P3-05 | Loading → success/error | Focus stable; truthful state and recovery action | Prototype review pending |
| P3-06 | Permission denied/offline/unavailable | Honest explanation and safe system handoff | Prototype review pending |
| P3-07 | Weather refresh while a card is focused | Focus and scroll position retained | Prototype review pending |
| P3-08 | Wi-Fi scan throttled or stale | Timestamp and stale state shown; no fake live data | Prototype review pending |
| P3-09 | Next Photo with one/many/zero images | 2/1/0-image simulation; disabled when alternate unavailable; success after simulated index change | Implemented / review pending |
| P3-10 | Dock orientation/capacity change | Dock changes independently; app grid columns/order remain invariant | Prototype review pending |
| P3-11 | Search exact control | Search filters matching controls and selects owning category; exact-control focus walkthrough pending | Implemented / review pending |
| P3-12 | Reduced motion | No nonessential scale/translation; focus remains obvious | Prototype review pending |

## 5. Prototype artifact and discrepancy register

### 5.1 Versioned interactive prototype V1
- Canonical artifact: `prototypes/UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V1.html` (same documentation branch).
- Artifact includes six switchable surface views, D-pad/keyboard arrow traversal, Enter activation, Esc/Back return behavior, category navigation, control-level search demonstration, weather V4 metric hierarchy, independent Dock presentation, and Wi-Fi system-handoff treatment.
- V04 includes a controllable 2/1/0-photo simulation: Next Photo is disabled when there is no alternate image and reports success only after the simulated wallpaper index changes.
- V05 contains clearly labelled illustrative scan results, a rescan state, and Android Wi-Fi Settings handoff; it does not expose an in-app radio switch or claim a real network scan/connection.
- All weather and Wi-Fi values in the prototype are illustrative sample data, not live device/provider output.
- Artifact is a browser-based interaction prototype, not Android source, APK, or evidence of runtime/device acceptance. Keyboard navigation is a review aid; physical TV remote behavior remains unverified.
- Prototype artifact latest documentation commit: `f09515c2d434a809e0fe594cbed36880cc2e09ce`.
- V1 interaction refinement: arrow-key movement now selects the nearest visible control in the requested spatial direction rather than moving through a flat list; Back/Esc restores the prior surface and invoking control where it can be resolved.



- Generated overview boards for all six surfaces are attached in the conversation on 6 Nov 2026. They are concept references only and are not committed as repository image assets. The corrected, versioned HTML prototype is the current review artifact.
- The generated Wi-Fi popup artwork includes a switch-like visual. This conflicts with P2 and is explicitly rejected. The authoritative prototype must replace it with a Wi-Fi state indicator and Android system handoff.
- Generated artwork is not pixel-accurate source output. Typography, logos, data values and icons must be normalized in a true editable prototype before P3 is marked PASS.
- The concept boards are not interactive. The D-pad paths and acceptance cases in this document are the interaction specification; they still require an interactive prototype or structured prototype walkthrough before P3 closure.

## 6. P3 exit criteria

P3 can close only when:
1. Six screens have consistent, reviewable prototypes that conform to P2.
2. V05 Wi-Fi switch depiction is corrected to system handoff; conditional scan states are explicit.
3. V02 shows hierarchy for the complete V4 data scope without deleting metrics.
4. V01 Dock is visually and behaviorally independent from app grid.
5. V04 Next Photo is visible and has defined disabled/success/failure behavior.
6. V06 taxonomy, category navigation and control-level search are represented.
7. All P3-01 to P3-12 interaction scenarios have expected outcomes and no unresolved design contradiction.
8. Prototype assets/specification are versioned and linked in canonical docs.
9. Cross-surface ownership and dependencies remain consistent with P2.

## 7. Current disposition

**P2:** COMPLETE — frozen.  
**P3:** IN PROGRESS — six-surface interactive HTML prototype V1 versioned; spatial focus and Back restoration implemented; full 12-scenario walkthrough, visual comparison and accessibility/edge-case QA remain outstanding.  
**P4:** NOT STARTED — production implementation remains gated by P3 exit and roadmap implementation checks.  
**P5:** NOT STARTED — integrated QA/device validation remains separate.

No production source, feature branch, PR metadata, integration/main, APK, ADB, HOME/default launcher or physical TV was changed by this P3 kickoff.
