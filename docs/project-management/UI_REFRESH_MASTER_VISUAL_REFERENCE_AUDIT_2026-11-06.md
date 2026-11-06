# Glass TV Home — Master Visual Reference Global UI Audit

**Document ID:** GTVH-UIR-MVR-AUDIT-001  
**Date:** 6 November 2026  
**Scope:** Read-only preparation audit for the Master Visual Reference Global UI  
**Repository:** `Frangga11/glass-tv-home`  
**Canonical documentation branch:** `docs/project-master-inventory`  
**Governing roadmap:** `docs/project-management/UI_REFRESH_ROADMAP.md` — GTVH-UIR-001 v1.0 LOCKED

## 1. Trigger and decision

User requested a comprehensive audit before implementation preparation: repository, branches, PRs, UI structure, global components, module dependencies and current source condition. This audit is not a redesign and does not authorize production source changes.

**Overall result: BELUM PASS — IMPLEMENTATION READINESS NOT ESTABLISHED.**

Sub-gates:
- P0 baseline and compatibility: PASS (baseline only).
- P1 functional inventory: PARTIAL / acceptance pending.
- P2 shared design and interaction contract: COMPLETE / frozen.
- P3 prototype and review: PARTIAL / walkthrough and design QA pending.
- P4 production implementation: NOT STARTED / BLOCKED by P1 acceptance and P3 review approval.

## 2. Inputs and method

Reviewed the locked roadmap, HANDOVER, MASTER_PROJECT, INVENTORY, DECISION_LOG, AUDIT_REGISTER, P0 baseline, P1 functional inventory, P2 shared design contract, P3 prototype specification, E02/E06 source audit, current relevant PR descriptions and changed-file lists. Read-only source inspection covered shared UI classes and the Home/Profile implementation points relevant to the visual refresh. Live compare operations were run for both E02/E06 PR heads against integration and their immediate base branches.

Method: Trigger → Input → Verifikasi → Analisis → Langkah Kerja → Decision Point → Output → Quality Check → Final.

## 3. Repository and branch baseline

| Role | Branch / ref | Exact SHA / finding |
|---|---|---|
| Frozen approved integration | `integration/b-series` | `562cde0751de93748eb4e0d4cdece4fa1672ac4d` |
| Main | `main` | `6eaf787c56226606b1d2e650c59540c553756c62`; main-only CI dispatch commit remains excluded from integration |
| E01 owner | `feature/e01-device-settings-home-control` | `edd6461c932705a109ed9768649cae7e3c162c59` |
| E02 PR #62 base | `feature/e02-profile-avatar-lock-ui` | `3ab9b55f3cfe03ad9841a119590a9183cf114e96` |
| E02 PR #62 head | `feature/e02-giphy-avatar-search-v1` | `588456144df24f2f6c015a01053ad700a04bee39` |
| E06 PR #63 base | `feature/e06-home-screen-subtabs` | `570b7c83efaec3ac515b78f1c6c119b777fc3b84` |
| E06 PR #63 head | `feature/e06-dock-capacity-control-v1` | `ecdeb0eba9fc7a574f1a6c9930205517687cd922` |
| Documentation | `docs/project-master-inventory` | Canonical documentation ref; audit files are read/written only on this branch |

The branch inventory also contains the B01–B07, C/D/E feature, integration, verification and build branches. Branch existence does not imply that every branch is current, integrated or accepted. For this audit, exact source refs were pinned for the frozen integration and the directly affected E01/E02/E06 branches above.

### 3.1 PR and dependency status

| PR | Owner | Current status | Exact-head CI | Scope |
|---|---|---|---|---|
| #62 | E02 Profile & Avatar | OPEN / DRAFT / UNMERGED | Run #1204 / 41895866368 — both required jobs SUCCESS; publication skipped | 9 changed paths: build config, GIF drawable/catalog/dialog, Settings/Profile integration, locale strings |
| #63 | E06 Home Screen & Dock | OPEN / DRAFT / UNMERGED | Run #1198 / 41890835096 — both required jobs SUCCESS; publication skipped | 2 changed paths: DockPreferences and its state test |

Live compare results:
- E02 head vs `integration/b-series`: DIVERGED, 19 ahead / 326 behind.
- E06 head vs `integration/b-series`: DIVERGED, 3 ahead / 297 behind.
- E02 head vs its own base: 19 ahead / 0 behind.
- E06 head vs its own base: 3 ahead / 0 behind.

Both bases are ancestors of the frozen integration tree. E01 has three commits not contained in the E02/E06 bases; source differences include MainActivity, ModernSettingsDialog, ProfileManager, ChildModeManager, UserSwitchDialog, locale resources and related tests. E06 history also differs in AppLanguage, ClockDream, WallpaperMetadata, activity_main.xml and related tests.

**Risk:** high integration/reconciliation risk. Do not merge or rebase either Draft PR blindly. Before any combined implementation, perform a three-way semantic reconciliation against the frozen integration tree and preserve owner-specific behavior.

**Metadata consistency finding:** PR #62 body still says exact-head CI is unconfirmed, but run #1204 is recorded as successful in the canonical P0/P1 audit evidence. Treat that sentence as stale; do not treat CI as feature acceptance.

## 4. Existing UI architecture and reusable global components

| Component / source | Verified current role | Audit decision |
|---|---|---|
| `TvUiTokens.java` | Shared semantic colors, surfaces, text, focus, navigation and card dimensions; includes popup and Dock fallback surfaces | Reuse as the only shared token vocabulary; extend centrally when a missing semantic token is proven |
| `TvDialogStyle.java` | Central app-dialog surface, dim amount 0.68, centered geometry, viewport cap 88%, glass/solid fallback | Reuse; do not create parallel modal styling |
| `TvFocusIndicator.java` | High-contrast focus ring and rounded text focus helper; avatar-ring helper is present | Extend coverage to cards, rows, toggles, chips, list items and dialog actions; distinguish focus from selected/active |
| `TvResponsiveLayout.java` | Sizing helpers for Recent Apps and Nightstand | Reuse helpers; extend to a tested global TV viewport, safe-area/overscan and text-scaling contract |
| `GlassDrawable` / glass surfaces | Existing glass material and solid fallback path | Reuse material behavior; preserve readable solid/high-opacity fallback |
| `ModernSettingsDialog.java` | Existing two-pane Settings shell, category navigation, search and control builders | Evolve the existing shell; do not replace it wholesale |
| `TvFeedback` | Shared user-action feedback path | Reuse for truthful success/failure and avoid silent actions |
| `AppLanguage` | Shared language/localization behavior | Preserve localization ownership and resource parity |
| Existing popup row/menu infrastructure | Existing anchored popup and menu presentation | Reuse and align to P2 popup/focus/Back contract |

### 4.1 Important implementation boundary

The shared foundations exist, but they are not yet a complete universal component library. In particular:
- focus helper coverage is narrower than the P2 universal focus contract;
- responsive sizing does not yet establish a general overscan/text-scaling system;
- Settings shell, modal dialogs and anchored PopupWindows have distinct presentation contracts and must not be collapsed into one generic container without a demonstrated need;
- semantic tokens should be extended in B01, not duplicated in E02/E06 or other feature modules.

## 5. Current source condition and concrete blockers

### E02 — Profile / Avatar / Child Mode
Verified PR scope: `GiphyAvatarDialog`, `GiphyCatalogClient`, `AnimatedGifDrawable`, `ProfileManager`, `ModernSettingsDialog`, build configuration and localized resources.

Present: bounded G-rated search, bounded result/download handling, HTTPS/media-host checks, GIF signature validation, private profile staging, GIPHY attribution and platform Movie playback.

Still open:
1. Active HTTP connections are not all owned by one cancellable request handle; cancelling a Future does not guarantee socket disconnect.
2. Result preview is a bounded still frame, not a live animated preview; product behavior needs an explicit acceptance decision.
3. Dialog dismissal/profile-switch cancellation and commit race coverage remains incomplete.
4. Drawable attach/detach/replacement and playback lifecycle require regression/runtime evidence.
5. DPAD/focus, stale response, profile switch, storage rollback, malformed/large GIF, memory and localization/attribution coverage remain incomplete.
6. Current GIPHY provider policy/rating/attribution must be verified before feature acceptance.
7. QR manifest/endpoint authorization, profile no-profile/capacity failure invariants and API/package-visibility matrix remain P1 clarifications.

### E06 — Home Screen / Dock
Present: `DockPreferences` persists HORIZONTAL/VERTICAL and capacity 4/5/6 (default 5), with basic normalization/state tests.

Root architectural blocker in frozen integration:
- Home uses one RecyclerView/GridLayoutManager and shared `MainActivity.COLUMNS`.
- `AppAdapter` treats the first N positions as Dock membership.
- `DockDecoration` decorates the first N items as the Dock tray.
- Dock capacity/orientation, grid columns, item sizing, decoration and focus therefore remain coupled.

Still absent from the PR #63 diff:
1. One authoritative ordered app model separating pinned Dock membership from main-grid content.
2. Separate Dock container/adapter for horizontal tray and vertical rail.
3. Settings UI wiring for orientation/capacity.
4. Visible capacity slots and reachable overflow behavior.
5. Independent DPAD/focus/scroll/reorder/restoration and hidden/removed-app transitions.
6. Six executable layout combinations and tests proving app-grid columns/order remain invariant.

Changing `MainActivity.COLUMNS` to implement Dock orientation/capacity is explicitly unsafe and rejected.

### Global design and platform gaps
- Global focus and responsive/overscan coverage are incomplete.
- Wi-Fi scan results are conditional on Android permissions, Location enabled and scan throttling. Direct in-app Wi-Fi radio toggle and unrestricted saved-network management are excluded; use Android system handoff.
- Quick Settings Next Photo is a design target owned by E08; P1 has not verified it in the current Quick Settings source. It must call the canonical E08 wallpaper controller, not create a second wallpaper source.
- Weather V4 scope must remain intact; source/update/location truthfulness and offline/stale states must be preserved.
- Existing G02 integrated APK is 1,572,293 bytes against a 1,572,864-byte ceiling: only 571 bytes headroom. Separate E02/E06 APK sizes do not predict the combined candidate. Any integration must rebuild and measure exact SHA.

## 6. Impacted modules and responsibility

| Module | Visual/UI responsibility | Required coordination |
|---|---|---|
| B01 | Global tokens, glass/solid surfaces, shared Settings shell/navigation, dialog, state and responsive contract | Architecture owner; first dependency |
| B02 | Remote/D-pad focus indicators and focus movement | Align every interactive component to the P2 focus semantics |
| B03 | Dialog opacity, layering, modal surface and stacking | Reuse B01 dialog contract; avoid competing dim/surface policies |
| B04 | Responsive layout and viewport behavior | Extend safe-area/overscan and scale rules centrally |
| B05 | Indonesian/English localization and text expansion | Validate long labels and layout at every surface |
| B06 | Feedback, progress and operation outcomes | Ensure actions report actual outcomes |
| B07 | Menu/dialog lifecycle and return-focus restoration | Preserve exact invoking-control focus and Back behavior |
| E01 | Device/system settings, Wi-Fi and Quick Settings system actions | Depends on B01; platform feasibility required |
| E02 | Profile selector, profile detail, avatars, Child Mode and profile state | Depends on B01; preserve profile-scoped data/lifecycle |
| E03 | Wallpaper metadata/content information | Coordinate with E08; no duplicate source of wallpaper truth |
| E04 | Standby/screensaver surface | Apply global surface, motion, contrast and focus rules |
| E05 | Weather/AQI dashboard and information widgets | Preserve Weather V4 data and truthful source/location labels |
| E06 | Home composition, app grid, Dock and Home navigation | Separate Dock model/view from grid before styling is locked |
| E07 | Animation/effect controls and motion behavior | Follow global reduced-motion/focus contract |
| E08 | Wallpaper appearance, adaptation, Next Photo and interval | Own canonical wallpaper controller and visual settings |

## 7. Recommended implementation dependency order

Planning recommendation only; this audit does not authorize P4.

1. **B01 shared foundation:** semantic tokens, glass/solid surface variants, global typography/spacing/radius/motion, shared Settings shell, responsive safe area, state patterns.
2. **B02/B03/B04/B05/B06/B07 shared behavior:** focus, dialog layering, responsive/overscan, localization, feedback and return-focus; validate as one shared contract rather than parallel competing implementations.
3. **E01 system surfaces:** device settings, Quick Settings and Wi-Fi status/system handoff after platform boundary is respected.
4. **E05 information surfaces:** Weather V4 dashboard and AQI source/stale/offline hierarchy.
5. **E02 profile surfaces:** profile selector/settings, avatar/GIF lifecycle, Child Mode and profile isolation.
6. **E06 Home/Dock:** Home composition and separate Dock/grid model, view, capacity, orientation, overflow and focus graph.
7. **E03/E08 wallpaper:** metadata, appearance, canonical Next Photo/interval controller and wallpaper state.
8. **E07/E04 effects and standby:** shared motion/contrast rules applied to animation and screensaver surfaces.
9. **QA/integration:** cross-surface visual/focus/state/localization/lifecycle regression, three-way branch reconciliation, exact-head CI and APK budget.

Parallel owner implementation should start only after the shared contracts, owner boundaries and branch bases are reconciled. The order above is a dependency proposal, not permission to start source changes.

## 8. Existing design/reference readiness

- P2 contract is COMPLETE and frozen. It defines six surfaces V01–V06, 960×540 dp 16:9 design canvas, safe margins 48 dp left/right and 27 dp top/bottom, 8 dp spacing rhythm, 56 dp minimum target height, 64 dp preferred primary actions, typography/radii, Day/Normal/Night/Auto, reduced motion, focus/selection distinction and state behavior.
- P3 V2 prototype exists at `docs/project-management/prototypes/UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V2.html`. It represents six surfaces, all six Dock combinations, Weather V4 hierarchy, Next Photo 0/1/2/5-image modes, Wi-Fi permission/location/offline/throttle/stale/empty states, seven Settings categories, contrast modes and reduced motion.
- Static source checks and inline JavaScript syntax check are recorded. Browser/keyboard walkthrough, screenshot comparison, worst-case localization/text scaling, TV-distance readability and actual remote behavior are not verified.
- P3-01..P3-12 are not all accepted; most have source-level prototype behavior but still require walkthrough/review. P3 remains PARTIAL.
- P1 inventory is documented but remains PARTIAL pending user acceptance and technical clarifications.

## 9. Quality check and exclusions

Confirmed:
- PR changed-file scopes and live compare divergence for E02/E06.
- Shared global source classes and Home/Dock coupling points.
- Existing P1/P2/P3 gates and design contract.
- Current blockers, module ownership and recommended dependency order.

Not performed:
- No production source edits.
- No feature branch, integration branch or main ref updates.
- No PR metadata changes, merge, rebase or cherry-pick.
- No APK build/publication/installation.
- No ADB, HOME/default-launcher, package, permission or TV device changes.
- No redesign, design approval or P4 implementation.

## 10. Final disposition

**MASTER VISUAL REFERENCE GLOBAL UI AUDIT: BELUM PASS.**

Reason: the audit baseline and P2 contract are ready, but P1 acceptance and P3 walkthrough/design approval remain open; E02/E06 branches are divergent; E02 lifecycle/cancellation and E06 Dock/grid architecture remain unresolved; global focus/responsive contracts need implementation-ready validation.

**Decision:** keep the frozen integration baseline and all feature PRs untouched. Finish P1 acceptance and P3 visual/interaction review, close the named technical blockers and reconcile owner dependencies before opening P4 implementation. Do not redesign or modify production source as part of this audit.
