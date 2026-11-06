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


## 10. Post-audit PR metadata consistency check

- Live PR metadata was re-read after inventory: #62 and #63 are both OPEN, Draft=true, merged=false, and their head/base SHAs still match the inventory table.
- PR #62 description contains a stale statement that the latest head has not received confirmed exact-head CI. This conflicts with the live Actions evidence: run #1204 is attached to exact head `588456144df24f2f6c015a01053ad700a04bee39`, completed SUCCESS, and both required jobs succeeded. The PR description was not edited in this P1 pass; reconcile its text during the next authorized owner-branch/PR documentation update.
- PR #63 description's exact-head CI statement matches live run #1198. Both PRs remain Draft and unmerged.
- The earlier compare at the first consistency checkpoint verified the docs branch at `5d891b42147a0dea71cc7b7f6ad0e0ce43783f12`; later consistency addenda were then committed. The final docs branch head is recorded in the latest HANDOVER checkpoint.


---

# P1 Expansion — Global UI Functional Inventory Completion
**Document:** GTVH-UIR-P1-001 / expansion 1  
**Audit date:** 6 November 2026  
**Source baseline:** `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`  
**Documentation branch:** `docs/project-master-inventory`  
**Disposition:** **PARTIAL — SOURCE MAPPING EXPANDED; VISUAL REFERENCE ACCEPTANCE BLOCKED**  
**Method:** read-only source inspection and documentation update. No production source, owner branch, PR metadata, integration/main, APK, ADB or device changed.

## 11. Scope and method of expansion

This is an additive expansion of the existing P1 inventory, not a second inventory. Existing E02 and E06 tables remain authoritative for their detailed controls. This section adds cross-module coverage for the six named UI surfaces, Settings categories and nested actions, shared components, ownership and implementation dependencies.

Evidence distinction:
- **Source-present:** implementation and entry point were found at the frozen integration source.
- **Partial:** source exists but behavior, states, runtime/device evidence or reference equivalence is incomplete.
- **Absent/not wired:** data model or adjacent capability exists but the requested user-facing surface is not implemented/wired.
- **Reference comparison blocked:** the six exact reference image assets were not found in the repository tree or the available source records for this audit. Existing promotional/live screenshots are not silently substituted for the user's six approved references. Source-to-surface mapping is possible; pixel/geometry comparison against the approved six images is not yet evidenced.

## 12. Six-surface visual reference to source mapping

| Ref ID | Requested surface | Source entry / principal files | Actual behavior and functional elements | State/data/owner | Disposition and gap |
|---|---|---|---|---|---|
| V01 | Home Screen | `MainActivity.java`, `activity_main.xml`, `AppAdapter.java`, `Config.java`, `DockDecoration.java` | Wallpaper/backdrop; top shelf; Inputs, Wi-Fi, Assistant, profile and Settings pills; date/time and prayer shelf; weather/AQI widget; media card; app grid and dock decoration; app launch, focus, package refresh, top-bar popups. | Installed app inventory from PackageManager; tile/favourite/hidden state and order; `theme_prefs`, `wallpaper_prefs`, `tiles/order`; owners E06 for Home/Dock, E05 for information widgets, E07 for visual effects, E01 for device/top-bar integration, E02 for profile identity, B01 for shared focus/navigation. | **Source-present / partial.** Existing Home is not an independent Dock architecture: one RecyclerView/GridLayoutManager and `COLUMNS` still couple grid columns, Dock membership, tile sizing, decoration and focus. The approved visual target cannot be compared pixel-by-pixel until V01 reference asset is attached/located. |
| V02 | Weather & AQI Dashboard | `WeatherActivity.java`, `activity_weather.xml`, `WeatherAirQuality.java`, `WeatherBackgroundView.java`, `HomeWeather.java`, metric views | Location header/current condition/temp/high-low; hourly forecast strip; multi-day forecast rows and temperature bars; wind/compass; sunrise/sunset; UV; feels-like; AQI value/category/bar/model reference; precipitation/rain chance; visibility; humidity; pressure; radar illustration; source/update footer. Location picker exposes city/station, Asia region and visual mood. | Open-Meteo/CAMS model weather and modelled US AQI, selected location/region/station and `app_weather_prefs`; owner E05 (widgets/information) with E01 for location/device integration and B01 for dialog/focus. | **Source-present / partial.** AQI is explicitly a model estimate, not official Indonesian ISPU observation. Source contains a Malaysia/JAS APIMS footer path; region/provider applicability must be checked for supported Indonesian target locations. Exact V2 reference equivalence is unverified. Weather popup details must retain the previously approved V4 scope, but the live dashboard source must be reconciled against that locked scope rather than assuming every V4 field is already displayed. |
| V03 | Profile Selector | `UserSwitchDialog.java`, `ProfileManager.java`, `ChildModeManager.java`, `ModernSettingsDialog.java` Profile tab, `TvFocusIndicator.java` | Horizontal profile selector with active-profile indication, Add Profile and Close; select another profile; Child Mode parent-PIN gate before switching away; profile creation/avatar choice; deletion protected by parent PIN and confirmation. Settings Profile tab also exposes avatar selection, QR upload, GIPHY picker, profile list, Child Mode, locked/hidden app pickers and PIN change. | Profile identity, avatar and package policies are profile-scoped; parent PIN remains device-wide. Owner E02; shared shell/focus B01. | **Source-present / partial.** Verify active/no-profile fallback, capacity/validation, long names, D-pad/Back return focus, child-to-adult and child-to-child paths, delete/rollback, profile isolation and QR/GIF lifecycle. Existing P1 E02 table remains the detailed control-level source of truth. |
| V04 | Quick Settings Popup | `MainActivity.showQuickControlPopup()`, `popup_inputs.xml`, `showMenu()`, `addInputRow()` | Anchored compact popup from Settings pill. Actions: Restart, Turn Off, Clean Cache, Wallpaper (interval summary), Language, Glass TV Settings, Android All Settings, Close. Popup uses shared menu/row builder and closes before invoking actions. | Device/system intents, wallpaper state, language preference, cache manager and power/cache flows; owner E01 for device/system actions, E08 for wallpaper management, B01 for shared popup/focus behavior. | **Source-present / partial.** This is not a full Wi-Fi or network settings dashboard; it is a device quick-action menu. Verify focus entry/return, clipping/overscan, action failure, Back/outside dismiss, restart/power safety and localization. The exact V04 visual reference asset is not present in repository evidence. |
| V05 | Wi-Fi Popup | `MainActivity.showWifiPopup()`, `popup_inputs.xml`, `ConnectivityManager`, `WifiManager`, `updateWifiLabel()` | Anchored popup reads active network transport, Wi-Fi SSID and IPv4 when available; status row shows connected/disconnected state; row opens Android Wi-Fi settings; Close dismisses. Long-press Wi-Fi pill opens system Wi-Fi settings directly. Network callbacks refresh top-bar label. | Android connectivity/network state; owner E01 for device/network surface, B01 for popup/focus shell. | **Source-present / limited.** This popup is a status + deep-link control, not an in-app scan/connect/password/forget network manager. SSID/IP may be unavailable due Android/OEM permission/privacy behavior; exceptions are currently caught and the popup falls back to generic status. Verify Wi-Fi disabled, Ethernet-only, VPN, unknown SSID, permission/privacy restrictions, disconnected, no active network, system intent unavailable, and status refresh. Do not promise network management functions absent from source. |
| V06 | Global Settings Split Detail | `ModernSettingsDialog.java`, `NavigationState.java`, `TvUiTokens.java`, `TvDialogStyle.java`, `TvFocusIndicator.java`, `SettingsFocusPath.java` | Two-column modal: searchable left sidebar/profile card and right content pane. Tabs: Profile; Home Screen; Appearance; Status Bar & Weather; Visual Effects; Standby; Device Settings. Right pane rebuilds by selected tab; tab, scroll and focus path are persisted/restored. | `NavigationState` settings tab/focus/scroll; per-feature managers/preferences. Owner B01 for shared shell/navigation/dialog/focus; E02 Profile; E06 Home/Dock; E05 weather/information; E07 effects; E04 standby/screensaver; E01 device settings; E08 wallpaper. | **Source-present / partial.** Shared visual tokens currently define dark charcoal/grey surfaces, white/grey text and cyan avatar focus ring; modal policy uses glass/solid dialog surface, 0.68 dim and 88% viewport cap. Settings shell itself builds a matte charcoal root directly. Need one coherent target contract and source-backed reference comparison; no competing global style system. |

## 13. Global Settings category and nested-control inventory

| Category / navigation destination | Source-backed controls / submenus | Action, state and data boundary | Owner | Gap / verification |
|---|---|---|---|---|
| Profile & Multi-user | Active profile; 7 built-in avatars; QR personal photo; GIPHY avatar search/results/preview/attribution; profile list/switch/delete/add; Child Mode; locked apps; hidden apps; parent PIN change | Profile/avatar data and policy; PIN prompts; profile switch refreshes Home; GIPHY/QR network and private storage boundaries. | E02 | Reuse existing E02 inventory. Verify cancellation, lifecycle, profile isolation, provider terms, QR endpoint/token and failure rollback. |
| Home Screen | Daily Prayer Panel switch; Daily Ayah switch; Interface Sound dialog; Azan Settings dialog | `theme_prefs` prayer shelf; WeeklyAyah visibility/cache; SoundEffects selected theme; prayer schedule/audio/popup settings. | E06 for Home setting entry; E01 for Azan/device integration; E05 for information content. | Dock orientation/capacity is **not yet present** in this Settings tab despite the DockPreferences model on E06 owner branch. Add only after independent Dock architecture decision. Verify toggle propagation and restart persistence. |
| Appearance | Wallpaper Collection; Wallpaper Interval; Manage Uploaded Wallpapers | Existing collection/category, shuffle/interval, custom image management, QR upload/delete and default fallback flows. | E08 | Verify empty/error/download/offline/storage full/delete failure/default wallpaper fallback, metadata, image ownership and profile/device scope. Do not introduce another wallpaper settings system. |
| Status Bar & Weather / Widgets & Information | Prayer location; Weather Widget switch; Information Actions (weather summary, prayer & ayah summary) | LocationActivity and location/prayer preferences; weather visibility; information summary dialogs. | E05, with E01 location/device integration | Does not itself implement full WeatherActivity. Verify location permission/unavailable, offline/cache, stale data, AQI disclaimer, selected region/station and source labeling. |
| Visual Effects | Icon Animations; Glass Effects; Weather Animation | `wallpaper_prefs` icon animation/glass effects; weather dynamic-effect preference. Runtime setters update grid, glider, backdrop and overlays. | E07 | Global daylight/normal/night adaptive appearance is not evidenced as a single shared contract. Glass-off fallback must preserve solid readable surfaces. Verify reduced motion, low-memory device, focus visibility and background redraw. |
| Standby | Collection count; Upload via QR; Pick Photos; Use Photos switch; Clear collection (when non-empty); Preview screensaver | `StandbyPhotoStore`, QR/local upload, photo picker, standby mode and screensaver/nightstand flow. | E04 | Verify no photos, picker unavailable, QR timeout/offline, storage full, clear failure, mode persistence, process death/reboot and lifecycle. |
| Device Settings | Default launcher status/candidates; Home key accessibility service; Input key service; notification listener access; screensaver; display timeout; restart; power off; clear cache; storage capacity; device model/Android/resolution; app language; OTA update; Android Settings | Android Settings intents, PackageManager and Secure Settings read-only status, CacheCleaner, AppUpdateManager, language preference. | E01 | System settings availability and OEM-specific intent fallback must be tested. Sensitive actions require clear confirmation; never assume service enabled. API 26–34 and target device matrix remains open. |
| Settings Search | Search input; keyword matching; result list; not-found state; destination tab routing | Query terms and labels map to tab IDs; selecting result switches right pane. | B01 for search/navigation shell; each category owner for content correctness. | Current search is a curated keyword-to-tab map, not a general search index over every control. Ensure results never promise a control that is absent and return focus predictably. |
| Quick Control Popup (top bar) | Restart; Turn Off; Clean Cache; Wallpaper; Language; Glass TV Settings; Android All Settings; Close | Anchored PopupWindow/menu rows and downstream actions. | E01/E08/B01 | Different from Global Settings; preserve both entry paths and consistent Back/focus. |
| Wi-Fi Popup (top bar) | Current network status/SSID; Wi-Fi Settings deep link; Close; long-press direct system settings | Reads ConnectivityManager/WifiManager; no in-app network scan or connection editor. | E01/B01 | Keep scope honest; verify privacy restrictions and OEM behavior. |

## 14. Shared component reuse and gap register

| Component / contract | Existing source | Reuse decision | Required extension / replacement boundary | Owner |
|---|---|---|---|---|
| Visual tokens | `TvUiTokens.java` | Reuse as single authority | Add approved semantic tokens only after P2; do not scatter near-duplicate colors/dimensions. Existing palette is charcoal/grey with cyan focus accent, not yet the requested navy/cyan target system. | B01 |
| Dialog surface/geometry | `TvDialogStyle.java`, `bg_glass_dialog.xml` | Reuse | Distinguish centered modal, custom transparent modal and anchored PopupWindow contracts; test viewport/overscan and glass-off fallback. | B01 |
| Focus indicator | `TvFocusIndicator.java` | Reuse | Standardize focus ring color/weight/scale, selected-vs-focused distinction, clipping and focus return across all six surfaces. Existing helper covers avatar ring and rounded text focus only. | B01 |
| Responsive sizing | `TvResponsiveLayout.java` | Reuse where applicable | Extend after P2 for general TV viewport, overscan-safe margins, text scaling and 16:9/4K/density behavior; current helper is narrowly Recent Apps/Nightstand. | B01 |
| Popup row/menu | `popup_inputs.xml`, `addInputRow()`, `showMenu()` | Reuse | Shared semantics/accessibility/focus/selected status and failure behavior; keep anchored popup separate from modal. | B01/E01 |
| Settings row/card/switch | `ModernSettingsDialog.java` builders | Reuse | Normalize row height, typography, description wrapping, status/value affordance, focus and disabled/error state. Avoid new per-tab one-off card primitives. | B01 |
| Profile identity/avatar | `ProfileManager`, `UserSwitchDialog` | Reuse | Fix/verify lifecycle and no-profile fallback; all surfaces must share same active profile state. | E02 |
| Weather metric cards/charts | `activity_weather.xml`, custom metric views | Reuse selectively | Align hierarchy and metric scope with locked V4 dashboard; correct provider/region label and unavailable/stale state. | E05 |
| Dock model/view | `DockPreferences` on E06 owner PR; legacy `MainActivity.COLUMNS`, `AppAdapter`, `DockDecoration` on integration | **Do not reuse coupled geometry as target architecture** | Separate ordered pinned-app model, Dock view/adapter, horizontal/vertical layout, capacity 4/5/6 and overflow; grid span count remains independent. | E06 |
| State/feedback | `TvFeedback`, per-feature managers/preferences | Reuse | Define common loading/empty/error/offline/permission-denied/interrupted patterns and write-failure rollback. | B01 + feature owner |
| Locale | `AppLanguage`, `LocaleContext`, localized strings | Reuse | Validate every visible label, accessibility description, long text, numeric/date/unit formatting in Indonesian/English. | B01 + feature owner |

## 15. Cross-surface state and interaction matrix

| State / interaction | Home | Weather & AQI | Profile Selector | Quick Settings | Wi-Fi Popup | Global Settings |
|---|---|---|---|---|---|---|
| Initial/loading | App/package refresh; widget cache | Cached data then sync; weather animation | Local profile store | Immediate menu construction | Immediate network read | Dialog construction and persisted tab/focus restore |
| Empty | No apps/tiles; no media/widget | Missing weather/AQI/forecast/station | No profile/default profile invariant unproven | No known empty state | No SSID / no active network | Empty search results; some sections conditionally absent |
| Success | App launch, top pills, dock, widget | Metrics, hourly/daily forecasts, source timestamp | Profile selection/add/switch | Action dispatch | Status + system settings handoff | Tab switch, toggle/card action, search route |
| Error | App launch/package query failure | API/parse/cache/station failure | PIN/storage/create/delete/upload failure | System intent/action unavailable | WifiManager/ConnectivityManager read exception; fallback text | Preference write/system intent/permission failure |
| Offline | Core launcher should remain usable; weather cache | Cached/stale or unavailable data; sync failure | Built-in avatars/local profiles usable; QR/GIPHY fail safely | Local actions remain; update/network-dependent actions may fail | Status read may remain; no in-app connect | Local settings remain; OTA/network actions fail gracefully |
| Permission denied | Package visibility/service restrictions | Location source availability/provider restrictions | QR/local upload and file picker boundaries | Android system settings permission/intent | SSID redaction/permission restrictions | Accessibility/notification access not enabled; show status and route user to system UI |
| Interrupted / Back | Popup dismissal, activity recreation, app switch | Sync callback/activity finish, picker/dialog dismissal | PIN cancel, upload/search cancel, profile switch | Dismiss before action; Back returns to Home | Dismiss or return from Android Wi-Fi settings | Tab switch/re-render, search, nested dialog, Activity recreation |
| D-pad / focus | Top-pill route, widget, grid/Dock | Hourly/daily scroll, metric cards, dialogs | Horizontal profile carousel and avatar/list focus | Vertical menu rows and anchor return | Status/settings/close row and anchor return | Sidebar/search/right pane, nested dialog and remembered focus |
| Reduced motion | Tile/glass/ambient animations | Weather background and card focus | Avatar focus scaling and animated GIF lifecycle | Popup transitions | Popup transitions | Focus scaling/dialog transitions |
| Locale / text scaling | App names/date/prayer | Metric labels, source, units and forecast | Profile names, PIN, attribution | Action labels | SSID and system label | Search, long card descriptions, every tab and dialog |

## 16. Module ownership matrix

| Surface / concern | Accountable owner | Supporting owner(s) | Explicit boundary |
|---|---|---|---|
| Home layout, app grid, Dock | E06 | B01, E01, E02, E05, E07 | E06 cannot change global dialog/token contract unilaterally; Dock must be decoupled from grid columns. |
| Weather/AQI dashboard and weather widget | E05 | E01, B01, E07 | E05 owns metric/data presentation; E01 owns device/location integration; AQI model disclaimers remain visible. |
| Profile selector, avatar, Child Mode, app policy | E02 | B01, E06 | Profile data remains isolated; parent PIN is device-wide; Child Mode/package sets are profile-scoped. |
| Quick Settings Popup | E01 | B01, E08 | Device actions and system intents belong to E01; wallpaper action delegates to E08; popup shell is shared. |
| Wi-Fi Popup | E01 | B01 | Status/deep-link only unless user explicitly approves expanded network management scope. |
| Global Settings shell/sidebar/search/focus | B01 | All category owners | B01 owns reusable shell and interaction primitives; feature owners own content/action/data. |
| Home Screen Settings content | E06 | E01, E05 | Existing prayer/ayah/sound/Azan entries need explicit sub-owner mapping; Dock settings remain a gap. |
| Appearance/wallpaper content | E08 | B01, E04 | Wallpaper library/upload/interval/delete and fallback; do not duplicate wallpaper manager. |
| Visual effects | E07 | B01, E05, E06 | Shared tokens/animations and reduced motion must be consistent. |
| Standby/screensaver | E04 | E01, B01 | Photo collection, QR, picker, persistence/recovery and preview. |
| Device settings/system access | E01 | B01 | Android intents, service status, launcher status, power/cache/update/language. |
| QA, integration, evidence | QA/Integration | All owners | Cross-surface tests, source reconciliation, exact-head CI and device gate; no owner self-acceptance by CI alone. |

## 17. Dependency and implementation order

1. **B01 / UI Architecture:** consolidate the shared UI shell, semantic component contract, dialog-vs-popup distinction, focus/Back contract, responsive/overscan and text scaling. Do not implement yet; this is P2.
2. **E01 / system surface audit:** verify Quick Settings, Wi-Fi popup, system intent fallbacks, network/SSID privacy and TV-specific device settings.
3. **E05 / Weather & AQI:** reconcile live screen against approved V4 scope, Indonesian provider/region behavior, cache/offline/error states and metric/source disclaimers.
4. **E02 / Profile:** reconcile current PR head/base with integration and E01 dependencies; finish request cancellation, profile isolation, QR authorization, GIF lifecycle and focus tests.
5. **E06 / Home & Dock:** resolve independent ordered app model, Dock view/adapter and overflow before designing orientation/capacity controls; preserve main-grid columns.
6. **E08 / Appearance + E07 / Effects + E04 / Standby:** normalize wallpaper, glass/animation and screensaver surfaces against shared components and existing persistence/recovery audits.
7. **QA/Integration:** create one traceability matrix and cross-surface regression suite; owner branches remain isolated; no merge/release/device operation without the later explicit gate.

Dependency rule: P2 design and P3 prototype approval are mandatory before P4. This order is a proposed dependency order, not implementation authorization.

## 18. Open decisions and unresolved evidence

| ID | Decision / evidence gap | Owner | Required resolution | Blocks |
|---|---|---|---|---|
| OI-01 | The six exact approved visual reference files are not in the repository evidence inspected during this run. | UI/UX Architecture / user | Attach or point to canonical V01–V06 images and identify version; do not substitute marketing screenshots. | Pixel/geometry comparison and P1 visual PASS |
| OI-02 | Weather V4 approved information scope vs currently rendered WeatherActivity fields needs explicit item-by-item reconciliation. | E05 | Build V4-to-source field traceability and identify absent/derived fields. | Weather surface acceptance |
| OI-03 | Weather provider/source label has a Malaysia JAS/APIMS branch; supported Indonesia behavior and data attribution must be verified. | E05/E01 | Verify source routing and disclaimers for Indonesia and other supported regions. | Weather/AQI acceptance |
| OI-04 | Wi-Fi popup currently exposes status/SSID/IP and Android settings deep link only. | E01 / user | Confirm this limited scope is intended; any scan/connect/forget/password workflow is new product scope. | Wi-Fi design lock |
| OI-05 | Quick Settings popup is a distinct anchored menu, not the full Settings shell. | B01/E01 | Confirm naming and entry hierarchy; preserve both top-bar quick menu and full settings. | Navigation contract |
| OI-06 | Global Settings uses seven categories; Home Screen has no Dock orientation/capacity control yet. | E06/B01 | Confirm category and placement for Dock controls after independent Dock architecture is approved. | P2/P3 Dock design |
| OI-07 | Home/Dock currently shares one grid and COLUMNS geometry. | E06 | Separate model/view/adapter; prove grid span invariant. | Dock prototype and implementation |
| OI-08 | B01 tokens/dialog/focus helpers are partial and Settings root uses direct matte styling. | B01 | Define one semantic surface policy and ensure glass-off readable fallback; no duplicate style system. | P2 |
| OI-09 | General overscan-safe margins, text scaling and day/normal/night behavior are not complete shared contracts. | B01 | Define measurable rules and device/resolution test cases. | P2/P3 |
| OI-10 | Settings search is curated keyword-to-tab routing, not search over every individual control. | B01 | Confirm current search scope or expand it with a traceable index. | Search acceptance |
| OI-11 | Quick Settings and Wi-Fi popup failure, focus restoration and system intent compatibility have not been proven on target Mi TV Stick. | E01/QA | Unit/UI tests plus separately authorized device evidence. | P5 |
| OI-12 | Exact live E02/E06 heads remain open Draft PRs and diverge substantially from integration. | E02/E06/Integration | Three-way semantic reconciliation with E01 and integration; no blind rebase/merge. | P4/integration |
| OI-13 | Integrated APK baseline has only 571 B headroom; isolated E02/E06 APKs are not additive estimates. | QA/Integration | Rebuild exact combined candidate and measure before adding assets/dependencies. | P4/P6 |
| OI-14 | Reference image visual fit, daylight glare, readability at viewing distance and remote focus cannot be signed off from source inspection alone. | UI/UX/QA | Approved six images, source-informed prototypes and actual display review. | P3/P5 |

## 19. P1 expansion quality gate and disposition

| Gate | Result | Evidence |
|---|---|---|
| Six named surfaces mapped to source entry points | **PASS (source mapping)** | V01–V06 table above |
| All Settings navigation categories mapped | **PASS (source mapping)** | Seven categories plus search, quick menu and Wi-Fi popup |
| Nested controls/submenus/actions identified | **PARTIAL** | Main categories and principal actions traced; existing E02 P1 remains detailed; several deep E01/E08/E04 dialog states require their source audit records and device tests |
| State/data/persistence/owner mapped | **PARTIAL** | Cross-surface matrix and ownership table; unresolved provider/permission/device details retained |
| Reusable vs new vs replace component register | **PASS (source-level disposition)** | Section 14 |
| Dependency and implementation order | **PASS (proposed; not authorized)** | Section 17 |
| Open gaps and decisions recorded | **PASS** | Section 18 |
| Exact six approved images compared against source | **NOT PASS** | Canonical image assets not located in repository evidence |
| Runtime/device/remote acceptance | **NOT RUN / NOT AUTHORIZED** | No ADB, install, TV interaction or physical device mutation |

**P1 Expansion result: PARTIAL — BELUM PASS.** The source mapping for all six named surfaces and the Settings categories is recorded. The quality gate is not passed because the exact six approved visual references are not available in the audited repository evidence, several nested/device states still require verification, and no runtime/device comparison was authorized or performed.

No P2 design contract, P3 prototype, P4 implementation, merge, APK build/publication/installation, ADB or device action is started by this expansion. Stop here and await user review/approval. P2 remains gated by the six canonical reference images and closure/explicit acceptance of the listed open decisions.


## P1 Addendum — Canonical Visual Reference Reconciliation (6 Nov 2026)

**Evidence received:** six user-supplied reference images, designated V01–V06. This closes the earlier missing-reference blocker for visual mapping. These images are design references, not evidence that the current APK already implements these layouts or behaviors. No runtime screenshot comparison was performed.

### V01 — Home Screen (1024×576 reference)
- **Visual target:** 16:9 composition; photographic Dempo/mountain sunrise background; top-left time/date; centered navigation pills (Beranda/Profil/Pengaturan); Wi-Fi and microphone actions; location/elevation and compact weather/AQI pills; large contextual weather headline; Qur'an verse and prayer-time strip; translucent glass dock with five large app tiles.
- **Source mapping:** MainActivity.java, activity_main.xml, AppAdapter.java, Config.java, DockDecoration.java, HomeWeather.
- **Source-to-reference gap:** source inventory confirms the home shelf, navigation/actions, weather/AQI, date/time/prayer shelf, media card, app grid and Dock. It does not establish that the exact visual hierarchy, proportional geometry, dock surface, or content composition in V01 is implemented. The current single RecyclerView/GridLayoutManager coupling of app grid and Dock is an architecture risk for independently tuning the dock.
- **P1 requirement:** preserve TV-safe 16:9 composition, readable hierarchy and D-pad focus; decouple Dock geometry from app-grid column geometry before visual tuning. Validate dynamic content overflow and safe margins.

### V02 — Weather & AQI Dashboard (473×1024 reference)
- **Visual target:** compact location/elevation and weather/AQI trigger pills; expanded anchored dashboard with location/update time, current condition and temperature, feels-like and high/low, AQI/category/explanation, humidity, wind, pressure, hourly forecast, seven-day forecast, strong glass/cyan outline and legible nested cards.
- **Source mapping:** WeatherActivity.java, activity_weather.xml, WeatherAirQuality.java, WeatherBackgroundView.java, HomeWeather.
- **Source-to-reference gap:** source contains current weather, hourly/daily forecasts, wind/compass, sunrise/sunset, UV, feels-like, AQI/category, precipitation/rain chance, visibility, humidity, pressure, radar and source/update footer. The reference image does not display every field in the locked V4 dashboard scope. Therefore V02 is a layout reference, not permission to drop V4 data fields. Retain the full approved V4 information scope, deciding through P2 how secondary data is grouped or scrolled.
- **P1 requirement:** document which fields are primary vs secondary; ensure AQI pollutant breakdown and local source/region labels are truthful. Existing modelled US AQI must not be labelled Indonesian official ISPU. Verify the Malaysia JAS/APIMS source-label path and regional provider behavior.

### V03 — Profile Selector (473×1024 reference)
- **Visual target:** anchored horizontal selector with title and four large cards: active personal profile, family, child, and add profile. Active profile has clear cyan focus/selection ring; avatar and labels are legible at TV distance.
- **Source mapping:** UserSwitchDialog.java, ProfileManager.java, ChildModeManager.java, plus Profile tab in ModernSettingsDialog.
- **Source-to-reference gap:** profile switching, add profile, Child Mode, avatar, locked/hidden apps and PIN are present in source inventory. Exact four-card geometry, active-vs-focused state distinction, and focus restoration need explicit verification. The reference selector itself does not show the full management controls; those remain in the Profile settings detail.
- **P1 requirement:** separate active-profile indicator from D-pad focus indicator; define empty/one/many-profile states, add-profile flow, delete confirmation, Child Mode/PIN gate, and Back/focus restoration.

### V04 — Quick Settings Popup (473×1024 reference)
- **Visual target:** popup anchored to Settings pill; title/close; two-column quick actions for Wi-Fi, restart, language, power-off; full-width cache cleanup; Wallpaper section with next-photo and interval; Glass TV Settings and Android TV Settings entries.
- **Source mapping:** MainActivity.showQuickControlPopup() and existing popup/menu row infrastructure.
- **Source-to-reference gap:** source action inventory substantially matches the reference: Wi-Fi, restart, power off, cache, wallpaper interval summary, language, Glass TV Settings and Android Settings. The screenshot depicts a direct Next photo action and interval control; source inventory records wallpaper interval summary but not a verified Next photo action in this popup. Confirm that action exists before marking parity. Reference layout and glass styling are not established by source presence alone.
- **P1 requirement:** specify action availability/confirmation/feedback and failure states for restart, power-off, cache, wallpaper change and external settings intents.

### V05 — Wi-Fi Popup (473×1024 reference)
- **Visual target:** Wi-Fi master switch; current connected SSID with selected check and lock/security icon; scrollable nearby network list; per-network security indicator; rescan action; Android Wi-Fi settings entry.
- **Source mapping:** Wi-Fi pill and MainActivity.showWifiPopup().
- **Critical functional gap:** current source behavior recorded in this audit is connection-state/SSID/IP summary, Android Wi-Fi Settings launch and Close; long press opens Android Wi-Fi Settings. It is **not** an in-app nearby-network scanner, network list, connect flow, password-entry flow or Wi-Fi radio controller. V05 requires materially broader functionality than the current source surface.
- **P1 decision required:** explicitly choose (a) retain lightweight status + Android settings handoff, revising V05 as aspirational, or (b) approve an expanded in-app Wi-Fi experience after Android 11/API, permissions, security, lifecycle and device feasibility audit. Do not imply scan/connect support until implemented and verified.

### V06 — Global Settings Split Detail (827×468 reference)
- **Visual target:** 16:9 settings screen with persistent left category navigation and right detail pane; top product/device status; active Profile category; profile/avatar preview card; clear editable rows and parental control switch; luminous cyan focus and glass panels. Reference category labels: Profil, Layar Utama, Tampilan, Widget & Informasi, Animasi & Efek, Layar Siaga, Pengaturan Perangkat.
- **Source mapping:** ModernSettingsDialog.java, TvUiTokens.java, TvDialogStyle.java, TvFocusIndicator.java, TvResponsiveLayout.java, GlassDrawable; settings category builders and underlying E01/E02/E04/E05/E06/E07/E08 owners.
- **Source-to-reference gap:** the existing Settings screen already has a seven-tab split modal with search, profile card/sidebar and right pane, but current source labels/categories are Profile, Home Screen, Appearance, Status Bar & Weather, Visual Effects, Standby, Device Settings. The reference renames/reframes at least “Widget & Informasi” vs “Status Bar & Weather”. This is a semantic taxonomy decision, not merely a label translation. Current shared tokens and root surface are charcoal/matte with cyan focus, not yet the target blue glass visual language. Focus component coverage is narrower than the complete target interaction system.
- **P1 requirement:** settle canonical category taxonomy and localization keys; retain clear ownership for each control; define focus traversal, scroll/focus restore, selected vs focused states, search result scope, and responsive/overscan behavior before P2 design lock.

### Cross-reference design contract candidates (for P2 approval)
1. **Surface language:** blue-tinted translucent glass, restrained blur/highlight, cyan edge/focus treatment, readable contrast; do not overuse transparency over busy wallpaper.
2. **Focus semantics:** D-pad focus must be unmistakable and distinct from selected/active/connected states; define focus for cards, rows, toggles, chips, scroll lists, dialogs and close controls.
3. **Hierarchy:** persistent navigation/actions, primary content and secondary detail must remain distinguishable at 1920×1080 TV distance; use consistent spacing, radii, icon sizes and typography.
4. **Wallpaper adaptation:** text and controls must remain legible over bright sunrise, cloud, forest and dark images; define scrim/contrast adaptation rather than relying on one fixed background.
5. **State completeness:** loading, empty, populated, offline, error, permission denied, unavailable, interrupted/Back, and action confirmation/result states must have shared patterns.
6. **Motion/accessibility:** define reduced-motion behavior, animation duration, focus transition, text scaling, overscan-safe layout and day/normal/night appearance.
7. **Data truthfulness:** source, update time, location scope, AQI model/standard and permission state must be explicit where relevant.

### Updated disposition
- **Visual reference availability:** PASS — all six user-supplied images received and mapped V01–V06.
- **Source-to-reference surface reconciliation:** PARTIAL — all six surfaces mapped; exact pixel measurements, runtime behavior and device screenshots have not been compared.
- **Functional parity:** NOT PASS — V05 Wi-Fi is a confirmed scope gap; V04 Next photo action is unverified; several focus/empty/error/permission states remain source/runtime verification items.
- **Taxonomy/design decisions:** OPEN — V06 category naming and broader P2 contract require approval.
- **Overall P1:** PARTIAL — BELUM PASS. Reference blocker is closed, but functional and runtime evidence gates remain open.
- **Gate:** do not start P2/P3/P4 or implementation until user reviews the Wi-Fi scope decision, category taxonomy and proposed shared design contract. No production source, feature branch, PR metadata, integration/main, APK, ADB or TV device was changed.
