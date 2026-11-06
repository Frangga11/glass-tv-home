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
