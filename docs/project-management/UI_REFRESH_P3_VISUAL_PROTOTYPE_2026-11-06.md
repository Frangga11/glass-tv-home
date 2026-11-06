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


## 5.2 V2 — Expanded 1920×1080 review prototype (6 Nov 2026)

- Added canonical artifact: `prototypes/UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V2.html`. It is a separate V2 artifact; V1 remains preserved for traceability.
- The frame is 16:9 and labelled 1920×1080. Its browser preview scales responsively; the design content remains aligned to the frozen 960×540 dp / safe-margin contract. It is a simulation, not a native Android render or proof of physical pixel density.
- V01 exposes independent Dock orientation and capacity selectors. The prototype supports all six combinations: Horizontal × 4/5/6 and Vertical × 4/5/6. Dock slots are rendered in a separate container; the app-grid preview retains a fixed four-column grid in every combination. This validates the visual model only; no production adapter/model has changed.
- V02 retains the Weather V4 metric families, hourly and seven-day summary, AQI and pollutant detail, source/update disclaimer, missing-value treatment, and simulated loading/success/error/offline refresh. Focus is not deliberately moved by the refresh handler.
- V03 includes active-vs-focused profile treatment, add-profile simulation and a failure state that preserves the previous state.
- V04 includes confirmation, cache busy/success/failure affordances and Next Photo 0/1/2/5-image modes; the action is disabled when fewer than two images exist and only advances the simulated index when an alternate exists.
- V05 exposes permission denied, Location disabled, offline, Android-throttled, stale-result and no-network states. Scan results are explicitly illustrative. The only network-management action is the Android Settings handoff; no in-app Wi-Fi radio switch is present.
- V06 includes all seven approved categories, control-level search and category-specific controls. A search match renders the owning category and focuses its first matching control.
- Shared review controls include Normal/Day-glare/Night contrast modes, reduced-motion mode, keyboard/D-pad directional focus, modal confirmation/failure surfaces, and Back/Escape close with invoking-control restoration where the origin remains available.
- A follow-up source review corrected the Wi-Fi system-handoff dialog action so opening the Android handoff simulation does not immediately close the new dialog.
- Static source checks performed against the committed HTML: all six orientation/capacity combinations are representable; grid remains explicitly fixed at four columns; permission/location gates and four Wi-Fi failure modes exist; weather loading/result states, confirmation/failure dialogs, contrast modes, reduced-motion toggle, focus/Back handler and control search are present. These are source-presence checks, not browser interaction or visual acceptance tests.
- Artifact commits: initial V2 `22a816ca90678c3c666db1de9a13cf9121136d82`; dialog handoff refinement `65d4fbfcacaa3af49c8fe964367ace3bf6e09d59`.

### P3 scenario disposition after V2

| ID | Scenario | Evidence now available | Disposition |
|---|---|---|---|
| P3-01 | Open/close popup and restore origin focus | Modal origin is retained and close restores the invoking element in the prototype source | Implemented in source; interactive walkthrough pending |
| P3-02 | D-pad traverse controls | Spatial nearest-direction keyboard handler is present | Implemented in source; edge/dead-end walkthrough pending |
| P3-03 | Long label / large value | Flexible cards and responsive text are represented; no systematic worst-case locale/scale test | Partial; review pending |
| P3-04 | Bright/dark wallpaper contrast | Day-glare, Normal and Night contrast modes are available | Implemented in source; visual legibility review pending |
| P3-05 | Loading/success/error | Weather refresh, cache result and save-failure simulations exist | Implemented in source; walkthrough pending |
| P3-06 | Permission denied/offline/unavailable | Wi-Fi permission/location/offline and system-handoff states exist | Implemented in source; walkthrough pending |
| P3-07 | Weather refresh focus stability | Refresh does not intentionally call focus or rebuild the focused metric region | Source-inspected; browser focus-retention test pending |
| P3-08 | Wi-Fi throttled/stale | Throttle and stale states plus timestamp are represented | Implemented in source; walkthrough pending |
| P3-09 | Next Photo 0/1/many | 0/1/2/5 selector; disabled below two; index advances on valid action | Implemented in source; walkthrough pending |
| P3-10 | Dock orientation × capacity | All six combinations selectable; separate Dock container; fixed four-column grid | Source-verified; visual comparison/walkthrough pending |
| P3-11 | Search exact control | Seven category dataset and query-to-category/control filtering | Implemented in source; exact-control keyboard walkthrough pending |
| P3-12 | Reduced motion | Reduced-motion toggle removes nonessential transition/animation | Implemented in source; visual/focus walkthrough pending |

**P3 disposition remains IN PROGRESS / PARTIAL.** V2 closes the missing prototype-state and six-combination representation gaps at source level, but does not close the review gate. No actual browser session, remote-control hardware, physical TV, 4K/1080p panel, or human TV-distance readability review was executed in this session. No approval of the design has been inferred. P1 remains PARTIAL pending user acceptance; P4 remains NOT STARTED and BLOCKED by P1 acceptance and P3 review/approval.


### V2 review refinement — search focus stability
- Source review found that filtering V06 controls moved focus away from the search input on each keystroke, which would interrupt multi-character input. Corrected V2 so filtering preserves focus in the search field; Down moves focus to the first matching control for D-pad selection. Empty-query reset also retains search focus.
- Corrective artifact commit: `ce6d45ca6a353bae867607d63d84c82c67eec4fa`. Inline JavaScript syntax compilation check: PASS. This remains source-level verification; manual keyboard/browser walkthrough is pending.


## V2 static interaction re-audit — 6 November 2026

- Read-only review recorded in [UI_REFRESH_P3_V2_STATIC_INTERACTION_REAUDIT_2026-11-06.md](UI_REFRESH_P3_V2_STATIC_INTERACTION_REAUDIT_2026-11-06.md).
- **P3 remains PARTIAL / BELUM PASS.** Found two high-severity prototype blockers: nested Wi-Fi handoff overwrites the root focus origin and closing the child dialog hides the whole modal; arrow-key/D-pad traversal is explicitly disabled while a modal is open.
- Medium gaps: Language and Wallpaper Interval quick tiles are inert; Next Photo has no failure/unavailable-source path; normal Wi-Fi rescan can remain stuck in “Memindai…”; profile switching is not simulated; Restart and Power Off share generic confirmation text.
- V2 remains preserved unchanged. No prototype or production source was modified. Correct these in a new versioned revision, then run browser keyboard walkthrough and visual comparison before P3 acceptance.


## V3 — Static interaction corrections and QC (6 November 2026)

- New artifact: [UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V3.html](prototypes/UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V3.html). V2 is preserved unchanged.
- V3 corrects all R01–R07 findings from the V2 static re-audit: nested dialog stack/root focus restoration; modal D-pad traversal; Language and Wallpaper Interval actions; Next Photo unavailable/failure paths; bounded Wi-Fi rescan completion; profile switch simulation; distinct restart/power-off confirmations.
- Static QC: **PASS**. One embedded JS script parsed successfully without execution; no duplicate HTML IDs; all seven correction groups were source-checked. QC record: [UI_REFRESH_P3_V3_STATIC_QC_2026-11-06.md](UI_REFRESH_P3_V3_STATIC_QC_2026-11-06.md).
- P3 remains **IN PROGRESS / PARTIAL** until browser keyboard walkthrough, 12-scenario validation and visual comparison against the six user-approved references. No production Android source or device was changed.


## V3 browser walkthrough plan

- Reproducible 12-scenario manual browser/keyboard test plan: [UI_REFRESH_P3_V3_BROWSER_WALKTHROUGH_PLAN_2026-11-06.md](UI_REFRESH_P3_V3_BROWSER_WALKTHROUGH_PLAN_2026-11-06.md).
- Execution status: **NOT RUN**. V3 static source QC remains PASS; P3 is not closed until the plan has actual browser evidence and reference comparison.


## V4 — Deeper interaction hardening (6 November 2026)

- New artifact: [UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V4.html](prototypes/UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V4.html). V3 remains preserved unchanged.
- Additional V3 findings corrected: (1) Weather Refresh no longer disables its focused invoking button during loading; busy state blocks duplicate activation without removing focus. (2) Wi-Fi prerequisite changes invalidate an in-flight simulated scan so a stale callback cannot overwrite the newer condition. (3) Modal Tab/Shift+Tab now wraps focus inside the dialog; Backspace remains editable in text inputs while Escape closes/back.
- V4 static source QC: **PASS**. Evidence: [UI_REFRESH_P3_V4_STATIC_QC_2026-11-06.md](UI_REFRESH_P3_V4_STATIC_QC_2026-11-06.md).
- Browser walkthrough and visual acceptance remain NOT RUN. P3 stays PARTIAL; no P4/source implementation.


## TRIGGER 4 — Prototype visual validation (6 November 2026)

- Six reference panels V01–V06 recovered from saved Global UI Design & Interaction Contract / UI Refresh overview boards.
- V4 source-to-reference composition comparison: **PROTOTYPE VISUAL — REVISION REQUIRED**. Main deviations: Home lacks reference wallpaper-led layout and Dock composition; Weather hierarchy is too generic; Profile selector has three generic cards instead of four avatar cards; Quick Settings and Wi-Fi are full pages instead of anchored popups; Global Settings lacks the complete icon-led split-detail composition.
- Glass material also needs revision: V4 panels are near-opaque (alpha .94), no backdrop blur is declared, and glow/iconography are underdeveloped relative to references.
- Browser render and screenshot capture at 1920×1080: **NOT RUN**. This is a source-to-reference composition audit only; no screenshot-based geometry, pixel or legibility claims are made.
- Full findings and correction plan: [UI_REFRESH_TRIGGER4_PROTOTYPE_VISUAL_VALIDATION_2026-11-06.md](UI_REFRESH_TRIGGER4_PROTOTYPE_VISUAL_VALIDATION_2026-11-06.md).
- V4 preserved unchanged; P4/mass implementation remains BLOCKED pending revised prototype, actual screenshot comparison, and explicit owner visual approval.

## TRIGGER 4 — V5 visual revision checkpoint (6 November 2026)

- New V5 prototype-only artifact: [UI_REFRESH_TRIGGER4_VISUAL_PROTOTYPE_V5.html](prototypes/UI_REFRESH_TRIGGER4_VISUAL_PROTOTYPE_V5.html). V4 remains preserved.
- V5 corrects source-to-reference composition gaps in Home, four-card Profile Selector, Quick Settings popup, Wi-Fi popup and Global Settings split-detail shell; shared glass CSS adds translucency, backdrop blur and layered shadow.
- Scenic gradient and emoji avatar/icon artwork are placeholders, not the original wallpaper or final iconography.
- Static source QC PASS; evidence: [V5 Static QC](UI_REFRESH_TRIGGER4_V5_STATIC_QC_2026-11-06.md).
- Browser rendering and screenshot comparison at 1920×1080 remain NOT RUN. Therefore overall result remains **PROTOTYPE VISUAL — REVISION REQUIRED**; no owner approval or P4 authorization.
