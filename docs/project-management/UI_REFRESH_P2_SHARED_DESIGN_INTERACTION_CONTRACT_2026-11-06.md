# Glass TV Home — UI Refresh P2 Shared Design & Interaction Contract

**Document ID:** GTVH-UIR-P2-001  
**Date:** 6 November 2026  
**Status:** DRAFT FOR USER REVIEW — P2 CONTRACT WORK IN PROGRESS  
**Authority:** User direction to follow the six supplied visual references V01–V06  
**Reference inventory:** `UI_REFRESH_P1_FUNCTIONAL_INVENTORY_2026-11-06.md`  
**Baseline audited:** `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`

## 1. Purpose and scope

Define one shared visual, navigation, focus, state, responsive and accessibility contract for the six approved UI reference surfaces. This document translates the references into testable design rules. It does not claim that current source or APK already matches them.

**In scope:** Home Screen, Weather & AQI Dashboard, Profile Selector, Quick Settings Popup, Wi-Fi Popup, Global Settings Split Detail; shared tokens and components; D-pad behavior; state patterns; TV layout constraints; ownership and dependency sequence.

**Out of scope:** production code changes, visual asset implementation, prototype approval, feature branch changes, PR changes, integration/merge, APK build/install, ADB or physical TV testing.

## 2. Approved visual direction

The six supplied images are the canonical visual direction:
- Blue-tinted glass surfaces over photographic wallpaper.
- Cyan/blue luminous focus and selection accents, used deliberately rather than as decoration on every element.
- White, high-contrast typography and recognizable icons at TV viewing distance.
- Rounded surfaces and cards with consistent radii, edge highlights, depth and spacing.
- Clear hierarchy: persistent navigation/status, primary content, secondary detail and actions.
- Wallpaper remains visible but must never compromise readability.
- The overall appearance should be elegant and controlled, not overloaded with blur, glow, animation or gradients.

### 2.1 Surface and contrast rules
1. Use translucent blue glass for overlays/cards only when foreground contrast remains adequate.
2. Place a controlled dark/neutral scrim behind text when wallpaper luminance or detail competes with content.
3. Prefer solid or higher-opacity fallback surfaces for dense data, small text, long lists, errors and permission explanations.
4. Do not place essential text directly over high-detail bright wallpaper without a contrast layer.
5. Keep borders and glow subordinate to content; focus ring is the strongest luminous accent.
6. Use consistent corner radii, padding, row heights, icon dimensions and elevation across all popup families.
7. Day/normal/night or adaptive brightness treatment must preserve the same semantic hierarchy and focus visibility.

### 2.2 Typography and TV legibility
- Establish a shared type scale for screen title, section heading, primary value, row label, supporting text, metadata and status.
- Use numeric tabular alignment for time, temperature, AQI and other changing values where practical.
- Avoid compressing labels into multi-line text unless the row height accommodates it.
- Use localized strings and allow expansion; do not hard-code Indonesian label widths.
- Text scaling must not clip primary actions, selected states, or focus indicators.

## 3. Shared navigation and focus contract

### 3.1 Focus is not selection
Every interactive component must distinguish:
- **Focused:** current D-pad target; visible high-contrast cyan outline/glow or equivalent.
- **Selected/active:** persistent choice such as active tab, selected profile, connected Wi-Fi or enabled toggle.
- **Pressed/activated:** transient confirmation that the action was invoked.
- **Disabled/unavailable:** visibly distinct and skipped by normal focus traversal unless an explanatory affordance is intentionally provided.

A focused item may be unselected; a selected item may not currently be focused. Never use one visual treatment to encode both states without a second clear cue.

### 3.2 D-pad rules
- Left/Right moves within a logical row or between sibling panes; Up/Down follows the visible visual hierarchy.
- Focus movement must be deterministic; no invisible focus jumps or traps.
- Enter/Center activates the focused control. Back closes the topmost popup/dialog first, then restores focus to the exact invoking control.
- Opening a popup places focus on its logical initial target (normally selected/current item or first enabled action); closing restores the origin.
- Scrollable lists move focus and scroll together; the focused row must remain fully visible.
- On tab changes, preserve a predictable focus target in the destination pane.
- Dialog close controls are reachable and have a visible focus state.
- Focus indicators must not be clipped by card bounds, screen edges or overscan-safe margins.

### 3.3 Shared interaction components
Reuse and extend existing B01 foundations rather than creating parallel systems:
- `TvUiTokens`: semantic color, typography, spacing, radius, elevation, motion and focus tokens.
- `TvDialogStyle` and `GlassDrawable`: shared glass/solid dialog surface variants.
- `TvFocusIndicator`: extend coverage beyond avatar ring and rounded text focus to cards, rows, toggles, chips, list items and dialog controls.
- `TvResponsiveLayout`: extend only after a defined 16:9 TV layout and safe-area contract.
- Existing Settings row/card/switch builders and popup row/menu infrastructure.
- `TvFeedback` and `AppLanguage`: shared action feedback and localization behavior.

## 4. Layout and responsive contract

- Primary target is 1920×1080 at 16:9; layouts must scale proportionally to supported TV resolutions.
- Keep all essential controls inside a conservative overscan-safe content region. Exact safe inset values are to be measured/validated in P3 on device; do not invent final pixel constants in P2.
- Maintain minimum comfortable D-pad target size and spacing; no tiny touch-oriented controls.
- Avoid fixed text widths where translated labels or dynamic values may overflow.
- Use stable anchoring: top navigation/status, contextual popup anchor, and content pane should not drift when data refreshes.
- Popups must fit within viewport and reposition or scroll when their anchor is near an edge.
- For portrait reference crops V02–V05, treat them as popup/detail composition references; the final TV implementation remains landscape 16:9 and must not rotate the TV interface into portrait.
- Home app grid and Dock must have independent geometry/configuration. The current shared RecyclerView/GridLayoutManager coupling is a design/architecture risk and must be resolved before independent Dock styling is locked.
- Settings uses a persistent left navigation pane and right detail pane, with a stable header/status area. The content pane may scroll independently from category navigation.

## 5. Shared state and feedback contract

Every asynchronous or stateful surface must define, where relevant:
1. Initial/loading.
2. Populated/success.
3. Empty/no data.
4. Offline/no connectivity.
5. Error/retry.
6. Permission denied or capability unavailable.
7. Busy/in-progress and duplicate-action prevention.
8. Interrupted/Back/cancelled.
9. Stale data with last-updated metadata.
10. Destructive-action confirmation and completion/failure feedback.

Rules:
- Never show a successful state before the underlying action reports success.
- Keep error text actionable and concise; provide retry or system-settings handoff where appropriate.
- Preserve last-known data only when it is labelled as stale and safe to display.
- Use the same iconography and language for equivalent states across Home, Weather, Profile, Wi-Fi and Settings.
- Avoid toast-only feedback for important TV actions; use visible inline/dialog feedback where needed.
- All user-facing messages must be localized.

## 6. Surface contracts

### V01 — Home Screen
**Composition:** top-left clock/date; centered primary navigation; Wi-Fi and voice/input affordances; location/elevation and compact weather/AQI pills; large contextual weather headline/description; prayer/verse information shelf; translucent bottom Dock with large app tiles.

**Behavior:**
- Navigation destinations: Beranda, Profil and Pengaturan; active destination is distinct from D-pad focus.
- Wi-Fi and microphone affordances show availability/status and open their respective flows.
- Weather/AQI compact pills open V02.
- Date/time and prayer information refresh without stealing focus.
- App grid and Dock are separate navigation/geometry regions; Dock tile order and focus must remain stable.
- Wallpaper and dynamic content must not cause text overlap or focus loss.

**Data/edge cases:** location unavailable, prayer data unavailable, weather stale/offline, AQI unavailable, no installed apps, long app names, media card absent, wallpaper load failure.

**Owner:** E06 Home/Dock (layout/content), E05 Weather/data (weather/AQI), E01 system affordances, B01 shared shell/focus.

### V02 — Weather & AQI Dashboard
**Composition:** compact location/weather/AQI triggers; expanded anchored glass dashboard with location and update time, current condition/temp, feels-like and min/max, AQI/category/explanation, humidity/wind/pressure, hourly forecast, seven-day forecast.

**Data preservation rule:** retain the full approved Weather V4 information scope even when the reference screenshot shows a subset: current temperature/condition/feels-like/min-max; humidity; wind speed/direction; pressure; dew point; visibility; sunrise/sunset; moon phase; rainfall and rain probability; UV index/category/recommendation; AQI value/category; PM2.5, PM10, CO, NO₂ and SO₂ pollutant breakdown. Additional existing radar/source/update fields remain subject to the approved V4 specification. P2 defines hierarchy/grouping, not deletion.

**Behavior:** compact pill opens dashboard; dashboard closes on Back/outside dismissal according to shared dialog rules; data refresh must not steal focus; stale timestamps are visible; secondary details may scroll while primary conditions remain easy to find.

**Data truthfulness:** show provider/source, update time and location scope. A modelled US AQI value must not be represented as official Indonesian ISPU. Verify local provider and Malaysia JAS/APIMS source-label path before showing a provider name.

**Owner:** E05 weather/AQI data and presentation; B01 popup/focus shell.

### V03 — Profile Selector
**Composition:** anchored horizontal panel with title and large cards for active personal profile, family profile, child profile and Add Profile. Active selection and current D-pad focus are distinct.

**Behavior:** select/switch profile; Add Profile enters creation flow; Child Mode follows profile policy; profile changes refresh dependent launcher state without losing navigation integrity. Full profile management (rename/delete/avatar, locked/hidden apps, PIN) remains in Profile settings rather than overloading the selector.

**States:** no profiles, one profile, multiple profiles, profile unavailable, add failure, delete confirmation, Child Mode/PIN required, avatar missing/loading.

**Owner:** E02 Profile/Avatar/Child Mode; B01 popup/focus shell.

### V04 — Quick Settings Popup
**Composition:** anchored to Settings pill; title and close; two-column quick actions; full-width cache action; Wallpaper section with Next photo and interval; Glass TV Settings and Android TV Settings entries.

**Target actions:** Wi-Fi handoff, Restart TV, Language, Power Off TV, Clear Cache, Next Photo, Wallpaper Interval, Glass TV Settings, Android TV Settings.

**Behavior:** destructive/power actions use confirmation where required; cache cleanup reports actual outcome; Next Photo is only enabled when wallpaper source supports it; external intents report unavailable/failure instead of silently doing nothing.

**Owner:** E01 system actions and Wi-Fi handoff; E08 wallpaper/Next Photo/interval; AppLanguage; B01 shared popup.

### V05 — Wi-Fi Popup
**Target composition:** master Wi-Fi state; connected SSID with selected indicator and security status; nearby network list with per-network security indicators; rescan action; Android Wi-Fi Settings handoff.

**Capability tiers:**
- **Tier 1 — guaranteed design baseline:** show actual Wi-Fi enabled/disabled state when accessible, current connection/SSID where available, clear unavailable/permission/system-managed state, and open Android Wi-Fi Settings.
- **Tier 2 — conditional enhanced experience:** scan/list nearby networks, refresh scan, show signal/security metadata, and select a network. This is not considered feasible until Android 11/API behavior, permissions, background/foreground restrictions, system image, device policy and security are audited.
- **Tier 3 — conditional connection management:** credential entry and direct connect/disconnect/radio control. Do not promise or expose this as functional until platform/API/permission/security review proves it is supported and safe on target devices.

**Security:** never log, persist or expose Wi-Fi passwords in diagnostics; mask credential entry; do not present fake networks or stale scan results as live. Explain when Android system settings must complete the operation.

**Owner:** E01 device/system integration; B01 popup/focus shell. Requires explicit technical feasibility review before implementation.

### V06 — Global Settings Split Detail
**Canonical category taxonomy from reference:**
1. Profil
2. Layar Utama
3. Tampilan
4. Widget & Informasi
5. Animasi & Efek
6. Layar Siaga
7. Pengaturan Perangkat

**Composition:** top product/device status; persistent left category navigation; right detail pane; profile preview card and editable controls; clear focus and active states.

**Behavior:** left navigation remains stable while right detail scrolls; changing category has deterministic focus placement; search routes to a result/control and returns to its owning category; Back restores prior focus/scroll context; toggles reflect persisted state only after confirmation from the state owner.

**Taxonomy mapping:** existing “Status Bar & Weather” content maps into “Widget & Informasi” (E05 ownership retained). Appearance remains “Tampilan” (E08). Existing E01/E02/E04/E06/E07/E08 controls keep their module ownership regardless of their visual grouping in Settings.

**Owner:** B01 shared Settings shell/navigation/search/focus; category content remains with E01/E02/E04/E05/E06/E07/E08.

## 7. Ownership and dependencies

| Workstream | Contract responsibility | Dependency |
|---|---|---|
| B01 | shared tokens, glass/solid surfaces, focus semantics, dialog, responsive shell, navigation and state patterns | first |
| E01 | device/system settings, Wi-Fi popup feasibility, Quick Settings system actions | B01 |
| E05 | Weather V4 hierarchy, AQI/source truthfulness, location and stale/offline states | B01 |
| E02 | profile selector, profile settings, active/focused states, Child Mode/PIN | B01 |
| E06 | Home composition, app grid/Dock separation, Home navigation | B01 |
| E08 | wallpaper appearance, adaptation, Next Photo and interval | B01 |
| E07 | animation/effect controls within shared motion/focus rules | B01 |
| E04 | standby/screensaver within shared surface and focus rules | B01 |
| QA/Integration | cross-surface navigation, focus, state, localization and TV validation | all owner contracts |

**Proposed sequence (planning only):** B01 shared foundation → E01 system surfaces/feasibility → E05 Weather V4 → E02 Profile → E06 Home/Dock → E08/E07/E04 → integrated QA. Sequence is subject to dependency audit; no implementation is authorized by this document.

## 8. P2 acceptance criteria

P2 can be marked complete only when:
- All six surfaces have approved structure, actions, focus rules, state matrix and module owner.
- Shared token vocabulary covers color/surface, typography, spacing, radii, elevation, motion, focus and semantic status.
- Selected/active/focused/disabled states are explicitly distinguishable.
- Wallpaper contrast and popup placement rules are testable.
- 16:9 layout, safe-area/overscan and text-scaling validation approach is specified.
- Weather V4 full information scope is retained and source/standard labels are truthful.
- Wi-Fi capability tiers are documented and technical feasibility audit owner is assigned.
- Category taxonomy is consistently mapped to existing module owners and localization.
- No unresolved cross-module ownership conflict remains.
- User reviews and explicitly approves P2 before P3 prototype work begins.

## 9. Open technical gates
1. Wi-Fi scan/connect/radio control support on Android 11 and target device/system image.
2. Exact overscan-safe inset and readable type sizes at 1920×1080.
3. Dock/app-grid decoupling approach.
4. Search scope: category-only vs individual controls/actions.
5. Weather/AQI local provider mapping and truthful source labels.
6. Next Photo action availability and wallpaper provider lifecycle.
7. Runtime focus and Back restoration across all popup/dialog surfaces.
8. Day/normal/night adaptation and reduced-motion behavior.

## 10. Current quality status

**P2 contract status: DRAFT — awaiting technical verification and user review.** The reference direction and category taxonomy are adopted. Wi-Fi enhanced functionality is conditional, not yet technically approved. No prototype, production source change, feature branch, PR metadata change, integration/merge, APK build/install, ADB or physical TV test has been performed.

**Next permitted step:** finish the targeted technical feasibility checks and present this contract for user review. P3 prototype must remain gated until explicit P2 approval.


---

## 11. P2 Closeout — Audited Decisions and Resolved Contract Questions

**Decision status:** The following are binding P2 contract decisions for downstream prototype and implementation planning. They close design ambiguity; they do not claim runtime verification or grant permission to bypass platform restrictions.

### 11.1 Shared TV layout baseline
- Design canvas: **960 × 540 dp**, landscape 16:9. Build primary compositions on this reference grid and scale using dp/sp, not fixed physical pixels.
- Safe area for critical UI: **48 dp left/right and 27 dp top/bottom** on the 960×540 design canvas (approximately 5%). At 1920×1080 on a 2× mapping this corresponds to 96 px left/right and 54 px top/bottom. Background wallpaper remains full bleed.
- Use a 12-column composition grid with consistent gutters where useful; do not force every screen into a rigid grid.
- TV UI remains landscape. Portrait V02–V05 images describe popup/detail composition only.
- Device-specific safe-area behavior remains a P3 hardware validation item.

### 11.2 Typography, spacing and target sizing
Initial P3 prototype baselines:
- Spacing: 8 dp base rhythm; 4 dp for optical adjustments; larger gaps in 16/24/32 dp increments.
- Interactive target height: **56 dp minimum**, primary TV actions target 64 dp where layout permits.
- Icon sizes: 24 dp standard, 28–32 dp for primary navigation/action icons.
- Text: 16 sp primary row/body; 14 sp minimum for non-critical metadata; 20–24 sp section heading; 28–36 sp primary title/value.
- Corner radii: semantic small/medium/large tokens, initial values 12/18/24 dp.
- These are P2 baselines, not claims that legacy screens already meet them. P3/P5 may justify measured adjustments, which must be recorded.

### 11.3 Motion and reduced-motion behavior
- Focus transition target: 120–180 ms; popup/dialog entry and exit target: 160–220 ms.
- Focus scale maximum target about 1.03×; must not move surrounding layout or clip.
- Avoid continuous decorative motion behind text and dense dashboard data.
- Reduced-motion removes scale/translation and nonessential looping effects; state changes remain clear through immediate visual state, outline and text.
- Transitions must be interruptible by Back and must not delay remote input.

### 11.4 Day / normal / night appearance
- Provide three semantic modes: **Day, Normal, Night**, plus **Auto** only when local device time is available.
- Default: **Normal**. Auto uses local device time and deterministic configurable boundaries; if time is unavailable, fall back to Normal.
- Appearance changes surface opacity, background scrim and luminance hierarchy, not semantic color or focus meaning.
- Focus remains high-contrast in every mode. Do not infer ambient brightness without a supported sensor.
- Explicit user selection persists across restart.

### 11.5 Wallpaper readability
- Background is full bleed; foreground content sits within safe margins.
- Adaptive scrim/contrast treatment behind top-level text and data. Dense dashboard/list/dialog surfaces use higher-opacity blue/neutral glass or solid fallback.
- Blur alone is never sufficient for readability.
- Wallpaper luminance changes do not alter focus color semantics.
- P3 visual review includes bright sunrise/cloud, high-detail forest, saturated sunset and dark/night samples.

### 11.6 Wi-Fi feasibility audit — final platform decision
**Root cause:** Android 10+ privacy/security restrictions prevent ordinary apps targeting API 29+ from managing the system Wi-Fi radio and saved-network database as privileged Settings apps. Android 11 (API 30) does not remove these restrictions.

**Verified platform constraints:**
1. WifiManager.setWifiEnabled() cannot toggle Wi-Fi for ordinary apps targeting Android 10+; it returns false. Device-owner/profile-owner/system-app exemptions do not apply unless the launcher is actually provisioned with those privileges. Do not assume it is. <Cite refs={["turn810250search0","turn810250search1"]}/>
2. Legacy getConfiguredNetworks() and add/update/remove/reconnect operations are restricted for ordinary apps targeting Android 10+. Do not build the UI around reading or mutating the full saved-network list. <Cite refs={["turn810250search0","turn810250search7"]}/>
3. Foreground scanning with WifiManager.startScan() and getScanResults() is technically possible on Android 10/11 with required permissions and Location services enabled. For target SDK 29+, scan requires ACCESS_FINE_LOCATION and CHANGE_WIFI_STATE; reading results requires ACCESS_FINE_LOCATION and ACCESS_WIFI_STATE, with Location services enabled. SecurityException, unavailable location, OEM restrictions and stale results must be handled. <Cite refs={["turn558081search1","turn558081search14"]}/>
4. Scan throttling remains: foreground apps are limited to four scans in a two-minute period. Do not continuously scan or let D-pad repeat spam scan requests. Failed/throttled scans may leave old results; show the last successful scan timestamp and label stale results. <Cite refs={["turn558081search1","turn558081search4"]}/>
5. Android 11 Settings.ACTION_WIFI_ADD_NETWORKS can request user-approved addition of up to five supplied network configurations to the saved list. It is not an unrestricted network manager; user approval is mandatory. <Cite refs={["turn810250search2","turn810250search8"]}/>
6. WifiNetworkSuggestion is a system-mediated suggestion path, not a guarantee that a selected SSID connects immediately. The platform chooses and controls the connection. <Cite refs={["turn558081search0","turn810250search5"]}/>
7. Android Wi-Fi Settings/Settings Panel handoff is the reliable baseline for radio enable/disable and normal network join/forget operations. <Cite refs={["turn810250search0","turn810250search18"]}/>

**P2 design resolution for V05:**
- Keep the reference popup structure, connected-network emphasis, nearby network list, rescan affordance and settings entry.
- Replace the literal in-app radio toggle with an honest Wi-Fi status/control row that opens Android Wi-Fi Settings Panel or Settings page. It may look switch-like only if clearly labelled as a system handoff and does not pretend the app toggled the radio.
- Show nearby scan results only after permission and Location prerequisites are satisfied. Provide permission explanation, Location Settings handoff, scanning/loading, results, empty, stale, throttled and failure states.
- Selecting a network opens Android system Wi-Fi selection/settings. Do not claim tapping a row connects directly unless a separately audited user-approved system API flow is implemented.
- Optional Add Network path may use ACTION_WIFI_ADD_NETWORKS only when the user supplies credentials and explicitly approves the system confirmation; never persist or log passwords in Glass TV Home.
- No password manager, saved-network enumeration, forget-network operation, or direct radio switch in the app.
- **Conclusion:** enhanced V05 is feasible as read-only nearby scan + connected status + system handoff, subject to target device/OEM permission validation. Full Settings-equivalent network management is not feasible for an ordinary launcher app and is excluded from P2 scope.

### 11.7 Quick Settings Next Photo
- V04 includes a visible **Next Photo** action owned by E08.
- Contract requires one wallpaper-controller operation (logical interface: advance to next eligible wallpaper), shared with existing wallpaper rotation/collection source. Do not create a parallel wallpaper list or bypass wallpaper ownership.
- Disable the action when no alternate eligible wallpaper exists; show feedback only after the active wallpaper changes.
- Current P1 evidence did not verify this action inside Quick Settings. It is a downstream E08 implementation/verification item, not a completed source feature. P3 shows the control; P4 wires it to the canonical wallpaper controller and tests one-item, multiple-item, empty, unavailable image and failure cases.

### 11.8 Dock architecture
- E06 maintains one authoritative ordered app model and two independent presentation regions: Dock and app grid.
- Dock orientation/capacity affects only Dock rendering; it must never change app-grid span count or silently drop apps.
- Dock membership/order is explicit and stable by app identity, not inferred from first N RecyclerView positions.
- Capacity options remain 4/5/6 with default 5 unless source/data migration audit finds a compatibility issue; overflow remains accessible from app grid.
- Existing preference model on E06 owner branch must be reconciled with integration source before implementation; do not assume it is integrated or wired to UI.

### 11.9 Settings taxonomy and search
- Canonical V06 taxonomy: Profil; Layar Utama; Tampilan; Widget & Informasi; Animasi & Efek; Layar Siaga; Pengaturan Perangkat.
- Existing Status Bar & Weather controls move visually under Widget & Informasi; ownership of weather/location/information remains E05/E01.
- Settings search indexes **individual visible controls and actions**, not only category names. Results identify destination category and exact control; selection navigates to and focuses the control. Hidden/disabled/unavailable controls must not be presented as actionable.
- Search index is generated from canonical control IDs and localized labels/keywords; do not maintain a second hand-written keyword map that can drift.

### 11.10 Weather V4 and data truthfulness
- All locked V4 information fields remain in product scope. V02 screenshot controls hierarchy only; it does not remove secondary metrics.
- Group secondary metrics in a scrollable/detail region with clear labels and units.
- Every weather/AQI view identifies selected location, source/provider and last update where available.
- Label modelled US AQI as US AQI; do not relabel as Indonesian ISPU. If local ISPU source is unavailable, say so rather than synthesizing it.
- Provider-specific pollutant omissions or unavailable measurements are shown as unavailable, not zero.

### 11.11 State, focus and failure acceptance matrix
All interactive surfaces must pass these contract checks in P3/P5:
- Focus can enter, traverse, activate, scroll, dismiss and restore to origin using D-pad only.
- Active/selected/connected, focused, pressed, disabled and unavailable states are visually distinct.
- Permission denied, location disabled, offline, empty, stale, throttled, provider error, missing intent, action failure and interrupted/Back states are designed explicitly.
- No asynchronous refresh steals focus or changes the currently focused item.
- No success feedback appears before action confirmation from its owner.
- All visible actions have deterministic owner, action contract and failure path.
- Localized labels may expand without clipping; focus outline remains inside safe area.

## 12. P2 final acceptance checklist

| Acceptance criterion | Result | Evidence / downstream gate |
|---|---|---|
| Six canonical surfaces have composition, action and owner contracts | PASS | Sections 6 and 7 |
| Shared glass, contrast, typography, spacing, radius and motion vocabulary | PASS | Sections 2, 11.2–11.5 |
| Focus vs selected/active/disabled semantics | PASS | Section 3 |
| D-pad, Back and focus restoration rules | PASS | Section 3 and 11.11 |
| 16:9 TV layout, responsive and overscan baseline | PASS | Section 11.1; hardware validation P5 |
| Weather V4 full scope and data truthfulness | PASS | Section 11.10 |
| Wi-Fi API feasibility and honest capability boundary | PASS | Section 11.6; device/OEM validation P5 |
| Quick Settings Next Photo owner and action contract | PASS | Section 11.7; source implementation verification P4 |
| Dock architecture and data invariants | PASS | Section 11.8; implementation verification P4 |
| Settings taxonomy and search scope | PASS | Section 11.9 |
| Shared loading/empty/error/offline/permission/interrupted states | PASS | Sections 5 and 11.11 |
| Module ownership and dependency order | PASS | Section 7 |
| User direction incorporated; no additional per-item approval needed | PASS | User instruction in conversation |

**P2 disposition: COMPLETE — CONTRACT BASELINE FROZEN FOR P3.** All design and platform-feasibility decisions required to create prototypes are resolved. Runtime and implementation verification are explicitly assigned to P3/P4/P5; they are not misrepresented as P2 design blockers.

**Implementation guardrails:** no direct Wi-Fi radio toggle or saved-network management; no fake success states; no removal of V4 metrics; no Dock/grid coupling; no duplicate wallpaper controller; no per-module competing design-token systems.

**Next trigger:** P3 — Six-Surface Visual Prototype & Interaction Validation. Build visual prototypes against this frozen contract, then conduct structured visual/interaction QA. Production code remains untouched until the project roadmap's implementation gate.
