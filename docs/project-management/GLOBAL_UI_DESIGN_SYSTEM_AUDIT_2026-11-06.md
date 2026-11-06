# Glass TV Home — Global UI Design System Audit & Foundation

**Document ID:** GTVH-GDS-AUDIT-001  
**Checkpoint:** 6 November 2026  
**Status:** AUDIT IN PROGRESS — NOT PASS / NOT APPROVED FOR MASS IMPLEMENTATION  
**Repository:** `Frangga11/glass-tv-home`  
**Documentation branch:** `docs/project-master-inventory`  
**Source baseline inspected:** `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`  
**Related implementation:** PR #64 remains separate, OPEN / DRAFT / UNMERGED.

## 1. Mandate and decision

The supplied visual references V01–V06 are the controlling visual authority for the complete Glass TV Home experience. The design system must preserve their composition, hierarchy, materials, proportions, typography, icon language and interaction appearance. This is a fidelity exercise, not an invitation to reinterpret the product.

This document starts the read-only source audit and defines a traceable foundation proposal. It does not approve a production redesign or mass implementation.

**Current decision: BELUM PASS.** Existing shared UI primitives are reusable, but the source does not yet provide a complete, reference-aligned global design system. P3 visual/browser acceptance is also still open. Production implementation remains blocked.

## 2. Source-of-truth hierarchy

1. **Primary visual authority:** the user-supplied six-screen reference board, `Kontrak Desain UI Global Glass TV Home.png`, containing V01 Home, V02 Weather & AQI, V03 Profile Selector, V04 Quick Settings, V05 Wi-Fi Popup and V06 Global Settings.
2. **Additional screen detail:** the supplied Profile & Security settings image and Home Screen settings image. These clarify the richer Settings layout and must remain consistent with the six-screen contract.
3. **Normative interaction and behavior contract:** `UI_REFRESH_P2_SHARED_DESIGN_INTERACTION_CONTRACT_2026-11-06.md`. It controls focus/selection distinction, D-pad, Back/focus restoration, states, TV layout and preservation of Weather V4 scope.
4. **Prototype evidence:** V4/V5 and their static QC reports are implementation experiments, not a higher authority than the reference images.
5. **Existing Android source:** evidence of current reusable behavior only; it does not override the approved reference direction.

Where secondary infographic boards repeat or conflict on numerical values, do not average or silently choose. Preserve the visible reference appearance and record the unit/value conflict for calibration against an actual 1920×1080 render.

## 3. Reference inventory and visual contract

| Reference | Canonical role | Required visual structure |
|---|---|---|
| V01 Home | Main launcher surface | Scenic photographic wallpaper; clock/date and top navigation/status; location/elevation and compact weather/AQI chips; contextual hero/information and prayer shelf; translucent bottom Dock |
| V02 Weather & AQI | Expanded information dashboard | Location/update; current weather and feels-like/min-max; distinct AQI band; metric cards; hourly and 7-day forecast; preserve full approved Weather V4 data scope |
| V03 Profile Selector | Profile switch surface | Centered glass selector over the same wallpaper language; four profile choices (primary, family, child, add); strong active/focus distinction |
| V04 Quick Settings | Anchored quick-action popup | Centered/anchored glass panel over Home context; Wi-Fi, language, cache, power, wallpaper and launcher/system settings actions; close/Back restores origin |
| V05 Wi-Fi Popup | Connectivity popup | Wi-Fi switch/status; connected network treatment; network rows/lock states; scan and Android Wi-Fi handoff; loading/permission/offline/empty/error states |
| V06 Global Settings | Full settings workspace | Persistent icon-led category navigation and right detail pane; header/status; seven canonical categories; selected category visually distinct from D-pad focus |
| Profile & Security detail | Settings category detail | Profile cards, avatar/QR, child mode, app lock, settings lock, privacy/data and quick actions, with TV-sized labels and clear focus |
| Home Screen settings detail | Settings category detail | Live preview and Home/Dock, wallpaper, grid, widgets and behavior controls; do not let Dock settings change app-grid geometry |

All popups, dialogs, menus, cards and settings details must use the same material, radius, border, type hierarchy, icon treatment, spacing and focus semantics. Their geometry may differ by role; their visual identity may not.

## 4. Visual tokens — reference-derived baseline

The following values are transcribed from the primary Global UI Design & Interaction Contract reference board. They are a **candidate token baseline**, not yet a claim of measured pixel-perfect calibration.

### 4.1 Color

| Semantic token | Reference value | Intended use |
|---|---|---|
| Primary navy | `#0B2A5B` | Deep shell/background tint |
| Secondary blue | `#1E6BFF` | Main blue surface/selected treatment |
| Accent cyan | `#00E5FF` | Focus, key edge highlights and interactive accent |
| Accent purple | `#A855F7` | Restrained secondary/gradient accent |
| Success | `#22C55E` | Confirmed success/connected/healthy states only |
| Warning | `#F59E0B` | Warning/degraded states only |
| Danger | `#EF4444` | Destructive/error states only |
| Text primary | `#FFFFFF` | Main labels and values |
| Text secondary | `#B6C6FF` | Supporting labels/metadata |
| Glass base | `rgba(10,20,40,0.60)` | Blue-tinted glass foundation |

**Rule:** color is semantic. Do not apply neon glow to every card or use status colors decoratively. Focus is the strongest transient luminous signal. Active/selected, connected, success, warning and disabled must remain separately legible.

### 4.2 Material, border, radius and shadow

- Glass is layered: translucent navy/blue fill, backdrop softening/blur, subtle inner/edge highlight, restrained shadow and optional focus overlay.
- The reference board shows glass blur around 12–22 dp; another summary board states 20–40 px. These units are not interchangeable without display density and implementation measurements. **Blur strength remains UNCALIBRATED** pending actual render comparison.
- Border target: 1–2 dp, cyan/blue-tinted and low intensity on idle surfaces; stronger only for focused/selected states.
- Radius target: 12–24 dp with role-based tiers (small control, card, large modal/panel); do not assign unrelated radii to every module.
- Shadow target: soft, layered depth. Exact offsets/blur radii are provisional until Android render and screenshot measurement.
- Solid/high-opacity fallback is mandatory when glass is disabled or when dense text, error/permission details or bright wallpaper would compromise contrast.

### 4.3 Typography

Reference-board scale:
- Display / Title 1: 36sp, weight 700.
- Title 2: 28sp, weight 600.
- Title 3: 22sp, weight 600.
- Body: 18sp, weight 400.
- Caption: 14sp, weight 400.
- Small metadata: 12sp, weight 400.

These are reference anchors, not permission to force every component into fixed sizes. The component role, viewing distance, localization expansion and actual 1920×1080 screenshot govern final fitting. Numeric data should align consistently; long Indonesian labels and translated strings must not clip or hide actions/focus.

### 4.4 Spacing and geometry

- The P2 contract requires a 16:9 landscape TV layout and a safe region; its final inset must be measured rather than guessed.
- Use a shared spacing scale and role-based padding; exact values to be measured from the approved reference crops at the target viewport.
- Maintain consistent card gutters, row heights, icon-to-label gaps, modal padding and panel alignment.
- Do not copy a portrait crop's aspect ratio into the TV layout; V02–V05 are content/composition references rendered in landscape TV context.
- Preserve independent layout geometry for Dock and App Grid.

### 4.5 Icons, focus and motion

- Use one coherent rounded icon family with consistent optical size, stroke/filled weight and alignment. Do not mix emoji placeholders with final iconography.
- Focus and selected/active states are different states and must have separate visual cues.
- Focus must remain visible against bright wallpaper, glass, solid fallback and all day/normal/night modes.
- Reference-board motion anchor is approximately 150 ms and focus scale about 1.05; existing Android focus helpers currently vary. Final duration/scale must be reconciled against the P2 contract and actual TV performance before token freeze.
- Motion must be short, deterministic and optional/reduced-motion aware. No animation may steal focus or delay input.

## 5. Reusable component inventory — source audit

| Existing source | Existing capability | Reuse / gap decision |
|---|---|---|
| `TvUiTokens.java` | Shared semantic colors, navigation/card dimensions, text sizes and radius values | Reuse as the sole token owner. Current neutral charcoal palette and compact Settings-specific scale do not match the blue-glass reference; central reconciliation required |
| `GlassDrawable.java` | Glass fill, veil, edge rim, focus-aware fading and solid fallback | Reuse; inspect backdrop cost and align material variants/tokens |
| `GlassRim.java` | Cached gradient edge stroke | Reuse; centralize stroke/shine values and verify on wide/tall shapes |
| `GlassView.java` | Floating focus rim over launcher tiles; hardware layer | Reuse only for its existing moving tile-selection role; do not assume it is a universal focus component |
| `TvFocusIndicator.java` | Avatar focus ring and rounded text focus helper | Extend through one API/contract to cards, rows, tabs, chips, toggles, list items and dialog actions; no duplicate focus frameworks |
| `TvDialogStyle.java` | App modal dim, centered geometry, viewport cap and glass/solid surface selection | Reuse for modal dialogs; anchored popup and full Settings shell remain distinct layout roles |
| `ModernSettingsDialog.java` | Existing Settings shell, seven-category navigation, dynamic detail pane, search and focus persistence | Evolve in place; avoid replacing working navigation/state flows merely to match appearance |
| `TvResponsiveLayout.java` | Existing responsive sizing helpers for selected surfaces | Extend only after shared viewport/safe-area/text-scale contract is measured |
| `TvFeedback`, `AppLanguage` | Shared feedback and localization infrastructure | Reuse; preserve truthful state feedback and locale parity |
| Existing popup/list/switch builders and drawable selectors | Reusable controls and state backgrounds | Inventory exact call sites before extension; consolidate only where behavior and geometry truly match |

**Finding:** reusable primitives exist, but a complete reusable global component library does not yet exist. The visual language in current `TvUiTokens` is materially different from the reference palette; several controls have local style construction and focus behavior. This is a genuine centralization gap, not a reason to create a second parallel UI framework.

## 6. Required global component contracts

These are component roles to standardize and reuse; implementation is not authorized by this audit alone.

1. **GlassPanel / Container:** shared material variants (glass, dense glass, solid fallback), radius tiers, border, shadow and content insets.
2. **GlassButton / SelectableCard:** idle, focused, selected, pressed, disabled, busy and error states.
3. **NavigationItem / Tab:** icon-label geometry; active category distinct from current focus; stable focus restoration.
4. **ModalDialog:** centered bounded modal, dim/surface policy, D-pad trap, Back close and origin restoration.
5. **AnchoredPopup:** anchor-aware placement, viewport collision handling, scroll fallback and exact focus return.
6. **Toggle / Switch:** checked state distinct from focus; clear enabled/disabled/busy semantics.
7. **ListRow / ScrollRegion:** shared row metrics, status/value/chevron slots, focused-row visibility and deterministic traversal.
8. **Badge / StatusIndicator:** semantic status palette and concise label; no status conveyed by color alone.
9. **Input / SearchField:** TV remote text entry, retained focus, clear/no-result states and localized hint.
10. **Progress / Loading / Feedback:** visible busy state, duplicate-action protection, success only after real completion, actionable failure.
11. **Icon / IconLabel:** one rounded icon family, common optical sizing and state tint.
12. **FocusLayer:** consistent outline/glow/scale behavior; never clipped and never confused with persistent selection.

Do not create duplicates if an existing source can be extended without breaking its behavior contract.

## 7. Global interaction and compatibility rules

- Target 1920×1080 landscape at 16:9; scale to supported Android TV sizes while preserving composition.
- All controls are operable with D-pad/Enter/Back; no touch-only affordances.
- Focus moves deterministically; dialogs trap focus; Back closes the topmost surface and restores focus to its invoking control.
- Scrolling and focus move together; focused items remain fully visible and not clipped by safe areas.
- Preserve all existing feature flows, permissions, persistence, state transitions and Weather V4 data.
- Day/normal/night or adaptive appearance changes contrast/material only; it must not change semantic meaning or layout hierarchy.
- Glass effects must have a solid fallback and a reduced-motion path.
- Avoid repeated expensive blur layers, per-frame object allocation, unbounded animation or unnecessary hardware layers. Profile rendering on target-class hardware before approving global blur use.
- No physical TV, ADB, APK installation or device setting change is authorized by this audit.

## 8. Audit findings and quality gate

| Gate | Current result | Evidence / remaining work |
|---|---|---|
| Reference inventory V01–V06 | PASS | Primary reference board and additional Profile/Home Settings references visually inspected |
| P2 visual/interaction contract | PASS as normative contract | P2 frozen; does not prove source conformance |
| Reusable source inventory | PARTIAL | Main shared classes inspected; full call-site and module-by-module component map remains |
| Token alignment to references | FAIL / REVISION REQUIRED | Current `TvUiTokens` palette is charcoal/gray; reconcile against reference-derived navy/blue/cyan tokens |
| Glass material consistency | PARTIAL | `GlassDrawable`/rim/fallback exist; blur and alpha need measurement; not every surface uses same material contract |
| Focus coverage | PARTIAL | Helper coverage is limited; existing scale/duration differ from reference anchor; all component classes/call sites need audit |
| Typography / spacing / icon registry | PARTIAL | Some shared values exist; not a complete semantic scale or icon-size registry; locale/TV-distance evidence missing |
| Popup/modal/settings role separation | PARTIAL | Dialog style and Settings shell exist; anchored popup behavior and all call sites require mapping |
| 1920×1080 browser prototype render | NOT RUN | V5 static QC is not a render test |
| Screenshot-to-reference geometry/material comparison | NOT RUN | Capture actual render for all six screens and additional Settings detail references |
| D-pad and focus runtime acceptance | NOT RUN | P3 12-scenario walkthrough remains open |
| Performance/rendering on TV-class hardware | NOT RUN | Need frame-time/blur-layer and animation evidence; physical test needs separate authorization |
| Cross-module compatibility | NOT PASS | Need module ownership and dependency matrix against E01/E02/E05/E06 and B01 |
| Global UI Design System final PASS | **NOT PASS** | Blocked by the above; no mass implementation |

## 9. Work sequence

1. Finish the open P3 trigger: execute the V5 browser walkthrough and all 12 scenarios; capture real 1920×1080 screenshots and compare against V01–V06; obtain owner visual acceptance.
2. Complete source inventory: enumerate every component/style/focus path across Home, Profile, Settings, Weather/AQI, Wi-Fi, quick popups and dialogs.
3. Reconcile semantic tokens centrally in B01; document color, opacity/material, radius, border, shadow, typography, spacing, icon, focus and motion tokens with measured values and fallback behavior.
4. Define and map component contracts to existing classes; identify extensions versus genuinely missing components.
5. Add unit/static tests for token completeness, component state coverage, focus semantics, locale expansion and viewport constraints.
6. Run browser/prototype visual regression at 1920×1080, then Android build/tests and performance checks; physical TV acceptance only after explicit authorization.
7. Request design-owner review. Only after every required gate passes may the Global UI Design System be marked READY and mass implementation begin.

## 10. Current implementation boundary

- This checkpoint is documentation and read-only source audit only.
- No production Android source, feature branch, integration branch or main branch was modified.
- PR #64 remains OPEN / DRAFT / UNMERGED and is not a substitute for P3 visual acceptance.
- No APK publication/installation, ADB, device setting change or physical TV test.
- The next source changes, if later authorized by the gate, must extend B01's shared foundation and preserve module ownership. No parallel design system.

**Final disposition: GLOBAL UI DESIGN SYSTEM AUDIT IN PROGRESS — BELUM PASS. P3 VISUAL ACCEPTANCE AND TOKEN/COMPONENT CONFORMANCE REMAIN BLOCKING.**
