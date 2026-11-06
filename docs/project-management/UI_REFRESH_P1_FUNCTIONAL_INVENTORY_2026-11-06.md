# P1 — Functional Inventory

**Document:** GTVH-UIR-P1-001  
**Roadmap:** GTVH-UIR-001 v1.0 — LOCKED BASELINE  
**Audit date:** 6 November 2026  
**Disposition:** **PARTIAL — INVENTORY RECORDED; USER ACCEPTANCE PENDING**  
**Method:** read-only source/branch/CI audit; documentation only. No production code, PR metadata, integration/main refs, APK publication, ADB or device mutation.

## 1. Trigger, objective and acceptance rule

User instruction: start P1 Functional Inventory. Inventory every in-scope E02 Profile/Avatar/Child Mode and E06 Home Screen/Dock screen, tab, submenu, control, data source, action, navigation path, permission boundary, persistence point and applicable UI state. Every element must have an owner, current behavior, source/data, state coverage and test plan.

P1 is an inventory gate, not feature acceptance. An absent or incomplete feature is recorded as an explicit gap with an owner and test plan; it is not represented as implemented. P2/P3 design approval remains mandatory before P4 implementation.

## 2. Inputs and live verification

Read on `docs/project-master-inventory`:
- `UI_REFRESH_ROADMAP.md` v1.0 LOCKED.
- `HANDOVER.md`, `MASTER_PROJECT.md`, `INVENTORY.md`, `DECISION_LOG.md`, `AUDIT_REGISTER.md`.
- `UI_REFRESH_P0_BASELINE_2026-11-06.md`.
- `E02_E06_GIF_AVATAR_DOCK_AUDIT_2026-11-05.md`.

Live GitHub verification:
| Item | Exact live ref / SHA | Result |
|---|---|---|
| Frozen integration | `integration/b-series` / `562cde0751de93748eb4e0d4cdece4fa1672ac4d` | P0 baseline unchanged |
| E02 PR #62 | `feature/e02-giphy-avatar-search-v1` / `588456144df24f2f6c015a01053ad700a04bee39` | OPEN / DRAFT / UNMERGED |
| E02 base | `feature/e02-profile-avatar-lock-ui` / `3ab9b55f3cfe03ad9841a119590a9183cf114e96` | Verified |
| E06 PR #63 | `feature/e06-dock-capacity-control-v1` / `ecdeb0eba9fc7a574f1a6c9930205517687cd922` | OPEN / DRAFT / UNMERGED |
| E06 base | `feature/e06-home-screen-subtabs` / `570b7c83efaec3ac515b78f1c6c119b777fc3b84` | Verified |
| E02 exact-head CI | run #1204 / `41895866368` | Both required jobs SUCCESS; publication skipped |
| E06 exact-head CI | run #1198 / `41890835096` | Both required jobs SUCCESS; publication skipped |
| Documentation branch before this P1 update | `docs/project-master-inventory` / `cfc2a3b00d904454ddf92f5e797c28021770027d` | Verified identical to recorded head |

The PR heads are still divergent from integration: E02 19 ahead / 326 behind; E06 3 ahead / 297 behind. No rebase, merge, cherry-pick or force update was performed. E01 has three commits not contained in either owner base; semantic reconciliation remains mandatory before integration.

## 3. E02 — Profile, Avatar and Child Mode inventory

Ownership: **E02 Profile & Avatar**. Shared UI/dialog/focus behavior must reuse B01 contracts. Profile-scoped data is keyed by profile identity; parent PIN is device-wide.

| ID | Screen / element | Current behavior, source/data and action | State / persistence / permission boundary | Required verification |
|---|---|---|---|---|
| E02-01 | Settings shell, search, sidebar, active-profile card | Two-column Settings dialog; search and sidebar select tabs; profile card returns to Profile tab. Source: `ModernSettingsDialog.java`, `NavigationState`. | Selected tab, scroll and focus path are persisted in navigation preferences. Dialog dismiss/reopen and empty search results need coverage. | D-pad traversal, search result/no-result, Back, tab retention, focus restore, localization and text fit. |
| E02-02 | Profile avatar display surfaces | Active avatar is rendered in Settings sidebar, Profile list, user switcher and Home top bar using `ProfileManager.getAvatarDrawable`; Child Mode affects avatar presentation. | Profile ID is the identity boundary; active-profile refresh updates dependent surfaces. No-profile fallback is not independently evidenced. | Consistency across all surfaces; profile switch; missing/corrupt custom file fallback; Activity recreation. |
| E02-03 | Seven built-in avatar choices | Seven focusable/clickable avatar tiles; selection calls `ProfileManager.selectBuiltInAvatar`, clears custom selection transactionally and updates selection ring/top-bar/list avatar. | Profile-scoped avatar index/custom avatar. Write failure reports error; success gives feedback. | All seven choices, current-selection indicator, failed delete/reset, restart persistence, D-pad focus. |
| E02-04 | QR personal-photo upload | Profile tab opens `showAvatarUploadQrDialog(profileId, callback)`; successful upload refreshes avatar surfaces. | Local network upload/session/token and private profile storage; no new runtime permission prompt was identified in inspected UI. Manifest/endpoint authorization details remain a separate source check. | QR generation, wrong/expired token, offline/network denied, invalid/oversized image, cancellation, storage full/write failure, profile switch during upload, rollback and cleanup. |
| E02-05 | GIPHY search dialog — query/search/status | Search input + Search action; blank query defaults to “cute avatar”; query bounded to 50 chars; max 14 G-rated results; HTTPS API; bounded JSON; status prompt/loading/empty/count/rate-limit/failure/key-missing. Source: `GiphyAvatarDialog.java`, `GiphyCatalogClient.java`. | Requires configured `GIPHY_API_KEY`; internet/network availability. Key is embedded in BuildConfig at build time and is extractable from APK; it is not a secret. | Configured/missing key, offline, timeout, HTTP 429/4xx/5xx, malformed/oversized JSON, empty and stale response, repeated search, dismissal during request. |
| E02-06 | GIPHY result grid / preview tiles | Four-column RecyclerView; bounded still-frame previews, max 1 MiB per preview, three preview workers, holder recycling and stale-binding checks; selecting a tile starts private GIF staging. | Loading/preview failure placeholder needs explicit review; current preview is a still image, not animated. | Preview success/failure, recycle/rebind race, D-pad traversal, no results, large/malformed preview, memory bound and focus restoration. |
| E02-07 | GIPHY attribution and Cancel/Back | “Powered by GIPHY” attribution visible; Cancel dismisses dialog; dismiss increments generation, shuts executors and closes adapter. | Provider terms/rating/attribution must be verified against current provider policy. Executor cancellation does not prove active socket disconnect. | Attribution visible at TV bounds and in both locales; Back/Cancel while search/preview/download active; active connection disconnect and no late commit. |
| E02-08 | GIF selection / private staging / commit | Download validates HTTPS GIPHY host, MIME, GIF signature and 8 MiB cap; writes private `profile_avatars` staging; commits via `ProfileManager.replaceCustomAvatar`; deletes failed temporary file. | Must match original active profile before commit; write failure preserves previous avatar. | Cancel during blocking I/O, profile switch, dialog close, process interruption, storage full, failed atomic replacement, cleanup and previous-avatar preservation. |
| E02-09 | GIF avatar playback | Platform `Movie` via `AnimatedGifDrawable`; circular crop/ring; scheduling gated by visibility/callback; explicit `stop()` exists. | Decode/playback memory and attach/detach/replacement ownership not proven by CI. | Visibility, detach, drawable replacement, profile switch, screen off/on, malformed/large GIF, repeated open/close, frame scheduling stops. |
| E02-10 | Edit active profile name | Profile card opens edit dialog; Save rejects blank name; Cancel dismisses; callback updates profile. Source: `ModernSettingsDialog.java` / profile manager. | Name persisted against active profile. | Empty/long/locale text, save failure, Cancel/Back, restart persistence, profile switch while dialog open. |
| E02-11 | Profile list / switch / delete | Lists profile identity, avatar, Child Mode suffix and active state. Inactive row opens Switch/Delete choices. Switch routes through `UserSwitchDialog.requestProfileSwitch`; delete routes through parent-PIN gate and confirmation. | Switching away from Child Mode requires parent PIN. Deletion requires parent PIN and confirmation; active profile must not be deleted through this route. | No profiles/default profile, one/many profiles, cancel PIN, wrong PIN, switch child→adult and child→child, delete cancel/success/failure, profile data isolation. |
| E02-12 | Add profile | Profile tab and user-switch dialog expose Add Profile; form creates profile and refreshes list/switcher. | Profile metadata/avatar defaults persist in profile store. Exact capacity/validation/error states need confirmation from complete profile manager contract. | Empty/duplicate/long name, maximum profile count, storage failure, cancel, new profile default avatar and immediate switch. |
| E02-13 | Child Mode switch | Located under Profile > Parental Controls (not a separate navigation tab). Toggle requires PIN confirmation, then updates profile-scoped flag, reloads tiles and top-bar avatar/state. | Per-profile `child_mode_enabled`; parent PIN remains device-wide. Legacy settings have one-time migration. | Enable/disable, wrong/cancel PIN, per-profile isolation, migration once, process restart, switch while enabled. |
| E02-14 | Locked apps summary and picker | Summary shows count; picker merges Leanback + generic launcher entries, deduplicates packages, displays checkbox and count; selection writes immediately; Done/Back dismiss refreshes tiles. | Locked package set is profile-scoped; app launch policy applies when Child Mode is enabled. Parent PIN required before picker. | Empty app list, no matching launcher apps, package removed/renamed, count correctness, immediate persistence, Back, app launch blocked/unblocked, profile isolation. |
| E02-15 | Hidden apps summary and picker | Parallel to locked-app picker; package membership controls hidden apps and refreshes Home tiles on dismiss. | Hidden package set is profile-scoped; parent PIN required. | Empty list, hidden/unhide, package update/uninstall, ordering and restoration, profile isolation. |
| E02-16 | Change parent PIN / PIN keypad | Requires current PIN, then new 4-digit PIN and confirmation; keypad uses 0–9, Delete and OK; Cancel supported. Default PIN currently `0000`. | Device-wide PIN in `child_mode_prefs`; no attempt limit is specified by the accepted product decision. | Correct/wrong current PIN, cancel, malformed/short/mismatch, save/restart, all protected operations require same PIN. |
| E02-17 | User switcher popup | Top-bar profile button opens horizontal profile carousel, Add Profile and Close. Active profile is indicated; Child Mode profile requires parent PIN before switching away. | Reads profile store and profile-specific Child Mode. | D-pad horizontal scroll, active/invalid profile, add, switch, cancel PIN, Back/Close, focus return to invoking top-bar control. |

### E02 data / persistence / permission register

- Profile identity/name/avatar selection/custom avatar: profile store and private app files; owner E02.
- GIF API/search/download: GIPHY HTTPS API; bounded request/response and media validation; provider key is build-time configuration, not a secret; no runtime Android permission prompt in inspected dialog.
- QR personal-photo upload: local server/session and private avatar storage; network and token boundaries must be tested; exact manifest/endpoint permission enforcement remains a verification item.
- Child Mode enabled, locked packages, hidden packages: `child_mode_prefs` with profile-keyed values and one-time legacy migration. Parent PIN key remains device-wide.
- Navigation tab, scroll and focus path: `NavigationState` preferences.
- Current source has explicit loading/empty/error feedback for GIPHY search and empty app-picker state. No explicit global “no profile” screen was located in inspected UI; verify default-profile invariant before P2.

## 4. E06 — Home Screen and Dock inventory

Ownership: **E06 Home Screen & Dock**. Main app-grid span count is a separate invariant and must not be changed by Dock orientation/capacity.

| ID | Screen / element | Current behavior, source/data and action | State / persistence / permission boundary | Required verification |
|---|---|---|---|---|
| E06-01 | Home surface / app grid | MainActivity displays launcher tiles sourced from installed Leanback activities, with generic Launcher fallback for configured packages; module cards may represent apps. Hidden-app policy excludes entries. Source: `MainActivity.java`, `AppAdapter.java`, `Config`. | Installed-package list is dynamic; configured favourites, hidden apps and module cards influence list. | No apps, package removed/updated, no Leanback entry, app launch failure, hidden app, reload and process restart. |
| E06-02 | Dock membership/order (current legacy implementation) | First `MainActivity.COLUMNS` adapter positions are treated as Dock/favourites; `AppAdapter` orders configured `dock_apps`, cards and alphabetical apps, with user reorder persisted in `tiles/order`. | One RecyclerView/GridLayoutManager and shared `COLUMNS` drives grid span, Dock membership, tile sizing, decoration and focus geometry. This coupling is a root blocker, not a desired contract. | Prove current ordering and migration; app install/uninstall/hidden transitions; reorder across boundary; no data loss; main-grid columns invariant. |
| E06-03 | Dock glass decoration / focus overlay | `DockDecoration` draws tray behind first Dock positions; glass preference controls material while focus feedback remains. | Decoration is position-based and tied to current Dock size; no separate Dock view exists. | Focus ring visible with glass on/off; first/last Dock tile, clipping, scroll, resize, reduced motion and focus overlay. |
| E06-04 | Dock orientation preference model | `DockPreferences` supports HORIZONTAL/VERTICAL; default HORIZONTAL; invalid value normalizes to HORIZONTAL. | Separate `dock_presentation` SharedPreferences namespace. Model exists on E06 branch only; no Settings UI or renderer consumes it yet. | Defaults, invalid/corrupt preference, restart, profile/device scope decision, read/write failure. |
| E06-05 | Dock capacity preference model | Capacity 4/5/6, default 5; invalid capacity normalizes to 5; save rejects unsupported values; membership predicate is position < capacity. | Same `dock_presentation` namespace. Not yet wired to visible UI or app ordering. | 4/5/6 boundaries, 0/negative/large values, restart, visible slot count and overflow behavior after implementation. |
| E06-06 | Separate Dock view/adapter | **Not implemented.** Existing single app grid is still responsible for both Dock and remaining app tiles. | Required architecture: one authoritative ordered app model; independent Dock view/adapter; orientation changes Dock row/rail only; capacity controls visible Dock slots only; overflow remains reachable in grid. | Model consistency, stable app identity, independent adapter updates, no duplicate/missing apps, no main-grid column change. |
| E06-07 | Dock settings controls | **Not implemented.** Home Screen settings tab currently exposes daily prayer panel, daily ayah, interface sound and Azan settings; it does not expose Dock orientation/capacity. | Dock preference persistence exists without a user-facing settings writer. | Localized setting rows, defaults, save/read, Cancel/Back, focus and preference propagation. |
| E06-08 | Horizontal Dock layout | **Not implemented.** Target contract is a one-row horizontal tray with Left/Right navigation/scroll. | Must not mutate app-grid span count or app order. | 1920×1080 preview, capacity 4/5/6, focus entry/exit, overflow, scroll, reorder and restore. |
| E06-09 | Vertical Dock layout | **Not implemented.** Target contract is a one-column vertical rail with Up/Down navigation/scroll. | Must not mutate app-grid span count or app order. | 1920×1080 preview, capacity 4/5/6, focus entry/exit, overflow, scroll, reorder and restore. |
| E06-10 | App tile activation / long-press reorder | Tile click launches app; long press routes to existing tile move/reorder flow; adapter preserves stable IDs and saves order. | Reorder saved in `tiles/order`; configured favourites and hidden apps interact with list rebuilding. | OK/Enter launch, long-press threshold, move/cancel, stable focus after reload, cross Dock boundary, app removed during reorder. |
| E06-11 | Home Screen settings tab | Current controls: daily prayer panel toggle; daily ayah toggle; interface sound dialog; Azan settings dialog. | Prayer/ayah settings persist through their respective managers/preferences; sound/Azan have separate dialogs and data. These are existing Home settings, not Dock controls. | Toggle persistence, dialog open/close, disabled/empty data, localization, focus return and restart. |
| E06-12 | Home focus and navigation restoration | App tiles expose directional key handling and focus callbacks; Home stores focused key/position and top-pill state in NavigationState; tile zoom responds to focus/reduced-motion settings. | Focus state and user tile order persist separately. Top bar, widget and media cards have their own key handlers. | D-pad path through top bar/widgets/grid/Dock; Back; process/activity recreation; no focus trap; reduced-motion and animation disabled. |

### E06 data / persistence / permission register

- Installed app inventory: Android PackageManager Leanback and Launcher activity queries.
- Favourites and hidden packages/module cards: `Config` sources; user tile order: `tiles/order` SharedPreferences.
- Current Dock geometry: `MainActivity.COLUMNS`, `AppAdapter.dockSize`, `DockDecoration.dockSize` — coupled legacy implementation.
- New Dock presentation preference model: `dock_presentation.orientation` and `dock_presentation.capacity`; no user-facing control or independent renderer yet.
- Home settings: prayer shelf, ayah, interface sound and Azan use their own existing managers/dialogs.
- No new Android runtime permission prompt was identified in the Dock model or Home settings code. Package visibility/launcher-query compatibility is a platform test item, not assumed PASS.

## 5. Cross-cutting state matrix

| State | E02 Profile / Avatar / Child Mode | E06 Home / Dock |
|---|---|---|
| Loading | GIPHY search spinner/status; avatar QR/network progress must be verified; profile list reads local store | Package discovery/reload; no explicit Dock loading model yet |
| Empty | GIPHY no results; app-policy picker no apps; profile list/default-profile invariant needs proof | Empty installed-app grid; no pinned apps; Dock overflow/empty state not implemented |
| Success | Avatar/profile/Child Mode updates refresh relevant surfaces; GIF stage+commit; profile switch reloads tiles | App launch/reorder/reload; Dock preference model saves only, no rendered success path |
| Error | GIPHY key missing, rate limit, request failure; image upload/storage/commit errors require coverage; PIN validation feedback | App launch failure, preference write failure, package refresh failure; Dock render error path not implemented |
| Offline / network unavailable | GIPHY search/download and QR upload must fail safely; no late commit; retry behavior required | Core app grid should remain usable offline; Dock itself should be local-only |
| Permission denied | No runtime permission dialog found in inspected avatar/Dock UI; verify manifest, local server and Android 11+ file/network constraints | PackageManager query and app launch compatibility; no Dock-specific runtime permission |
| No active profile | No explicit empty-state screen found; prove default-profile invariant and safe null handling | Profile identity is consumed by top-bar/avatar, but grid/Dock should remain usable with default profile |
| Interrupted / lifecycle | Dismiss, Back, Activity finish/destroy, profile switch, network cancellation, GIF detach/replacement and storage rollback | Activity recreation, package list change, focus restore, scroll/reorder interruption, Dock preference change |
| Reduced motion / focus | D-pad focus and modal return-focus must be deterministic; profile list and picker focus coverage open | Existing reduced-motion behavior for tile focus; future horizontal/vertical focus graphs require six-layout validation |
| Locale / scaling / overscan | Indonesian/English resources exist; verify long names, attribution, dialogs, keypad and profile list | Existing Home strings and app labels; verify labels in both locales, focus bounds and overscan-safe layouts |

## 6. Permissions, security, privacy and ownership boundaries

- E02 owns profile identity, avatar selection/storage, GIPHY integration, Child Mode and per-profile app policy UI.
- E06 owns Home grid/Dock presentation and navigation; Android PackageManager remains the source of installed launcher activities.
- B01 remains authoritative for `TvUiTokens`, `TvDialogStyle`, `TvFocusIndicator` and responsive helpers. P1 found no reason to alter those shared contracts.
- GIPHY API key is extractable from a client APK; treat it as public client configuration, enforce provider-side quota/abuse controls, and verify current terms/rating/attribution. Never commit a private key.
- GIF and QR avatar content must stay in app-private profile storage; profile switching must not expose another profile's avatar or Child Mode data.
- Parent PIN protects profile deletion, switching away from Child Mode, and locked/hidden app policy edits. Parent PIN remains device-wide; Child Mode and package sets are profile-scoped.
- Dock preferences must not be conflated with app-grid columns, app order or profile-specific settings unless a later explicit product decision says otherwise.
- No Android runtime permission request was found in the inspected UI paths. This is not a substitute for manifest, endpoint, target-SDK and runtime denial verification in P5.

## 7. Traceability and open acceptance gaps

| Gap / risk | Owner | Severity pending reproduction | Required evidence |
|---|---|---|---|
| Active HTTP connection is not guaranteed to disconnect when GIPHY dialog/Future is cancelled | E02 | High candidate; confirm blocking-I/O reproduction before severity finalization | Request owner, disconnect test, no late UI/commit |
| GIF still-frame previews are not animated; product decision needed | E02 | Medium / product scope | User-approved preview behavior; bounded preview tests |
| GIF attach/detach, profile switch and memory lifecycle not proven | E02 | High candidate until deterministic tests | Lifecycle, memory and profile-isolation regression suite |
| QR avatar manifest/endpoint permission and token boundary not fully re-audited in P1 | E02 | Security review open | Manifest/endpoint audit, expired/invalid token and offline tests |
| Current GIPHY policy/terms/rating/attribution not freshly verified | E02 | Provider compliance open | Current provider policy evidence and visible attribution |
| Default/no-profile invariant and profile capacity/error behavior not fully established in this P1 source slice | E02 | Medium / verification gap | ProfileManager contract tests and no-profile/failure-state tests |
| Shared `COLUMNS` couples grid, Dock, sizing and focus | E06 | High architectural blocker | Separate Dock model/view/adapter; grid-column invariance tests |
| Dock preferences have no UI wiring or rendered orientation/capacity | E06 | High functional gap | Settings UI, six layouts, overflow and persistence tests |
| Dock focus/scroll/reorder/focus restoration are not independently modeled | E06 | High functional gap | D-pad graph and six-layout focus/scroll/reorder tests |
| Package visibility/query behavior across API 26–34 not re-proven | E06 | Compatibility verification gap | API matrix and installed-app discovery tests |

No speculative code change was made. Existing findings are recorded as gaps, not defects confirmed by new runtime reproduction.

## 8. P1 quality check and disposition

- Roadmap and all five canonical project-management records were read before audit.
- Live PRs, owner branches, bases, integration SHA and exact-head CI runs were rechecked.
- Source diffs and current owner/base source were inspected for E02 GIF dialog/client/drawable/profile integration, E06 Dock preferences/app adapter/decoration, and shared Profile/Child Mode/user-switch UI.
- Every identified in-scope screen/control is mapped above to owner, current behavior or explicit absence, data/state/persistence boundary and test plan.
- E02 #1204 and E06 #1198 both have two required CI jobs SUCCESS; publication was skipped. CI is not runtime/device acceptance.
- No Critical defect was reproduced during this read-only inventory. High-risk functional gaps remain in E02 cancellation/lifecycle and E06 Dock architecture/focus; they are not waived.
- No production code, owner branch, PR state, integration/main, APK, ADB or physical device was changed.

**P1 inventory result: source mapping complete for identified in-scope E02/E06 UI elements; stage disposition PARTIAL pending user acceptance and the explicitly listed source/permission/provider clarifications.** P2 design decisions must not silently assume unresolved behavior. P4 remains gated by P2/P3 approval.

## 9. Decision point and exact next step

User acceptance is requested for this inventory and its recorded boundaries:
1. Confirm the mapped E02/E06 scope and explicit “not implemented” Dock states.
2. Confirm P1 may close with the listed verification gaps carried into P2/P5, rather than treating them as implemented or waived.
3. Keep P2/P3 design/prototype approval mandatory before any production implementation.

Next authorized work after acceptance: P2 shared design & interaction contract, using the existing B01 contracts and this P1 traceability map. Do not start P4 from this document alone.
