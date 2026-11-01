# D07 — Concurrent profile and content-store state audit

**Date:** 1 November 2026  
**Status:** Implementation complete; exact-head CI pending.  
**Branch:** `feature/d07-concurrent-profile-content-state`  
**Parent:** D06 final exact head `78a12e9fc196a596fa98ea272edc735501047af1`  
**Merge policy:** D-013 — isolated Draft PR; no early merge.

## 1. Scope and audit method

D07 addresses the roadmap requirement to prevent stale state, concurrent writes that overwrite one another, and conflicts between profiles or content stores.

The source audit traced read/modify/write and asynchronous completion paths in:

- `ProfileManager`: profile catalogue, active-profile pointer, avatar references and private avatar-file cleanup.
- `ChildModeManager`: profile-scoped Child Mode, locked-app and hidden-app sets, profile deletion cleanup, and device-wide parent PIN.
- `StandbyPhotoStore` and `MainActivity`: QR-uploaded and picker-selected photo membership, photo-mode state, clear operations, persisted URI grants and private file cleanup.
- `WallpaperUploadServer`: upload finalization and callback boundary; existing C07 session, request and file validation behavior.
- `HomeWeather`, `Location` and `MainActivity`: asynchronous weather/AQI refresh, location change and language change.
- `OnlineWallpapers`, `PlaceWallpapers` and navigation state for adjacent cache and UI ownership checks.

No global state manager, event bus, preference schema migration or broad storage rewrite was justified.

## 2. Confirmed findings and repairs

### D07-F01 — Child Mode read/modify/write and profile-context consistency

**Finding:** Locked/hidden package sets were copied, modified and written without a shared mutation lock. Two near-simultaneous updates could both read the same old set and the later write could discard the earlier change. Some checks also resolved the active profile separately for the Child Mode flag and package list, allowing a profile switch between reads to combine state from different profiles. Profile deletion cleanup was not serialized with these package-set writes.

**Repair:**
- Serialize Child Mode profile-scoped reads and mutations on the existing `ChildModeManager.class` monitor.
- Capture the active profile ID once per operation and use explicit-profile read paths for the matching enabled flag and package set.
- Serialize profile settings cleanup with the same monitor; preserve the device-wide parent PIN.
- Keep defensive copies of preference-backed string sets.
- Replace timestamp-only profile IDs with UUID-backed IDs to eliminate same-millisecond profile ID collisions.

**Regression coverage:** JVM tests verify copy-on-update behavior, sequential additions without dropping earlier entries, package-set separation by profile key, and removal preserving other entries.

### D07-F02 — StandBy picker, QR upload and clear-operation conflict

**Finding:** Picker selection used a stale collection snapshot and then overwrote the whole URI list. QR upload could commit a new entry while the picker was open, after which the picker write could erase it. Clearing collection membership, deleting managed files and disabling photo mode were separate operations; photo-mode commit failure was ignored, and the URI snapshot used to release external grants could itself become stale.

**Repair:**
- Centralize collection append and clear operations in synchronized `StandbyPhotoStore` mutation paths, sharing the same monitor as QR upload persistence.
- Merge picker additions against the latest persisted collection and re-enforce the maximum collection size at commit time.
- Commit collection membership and `display_mode=true` in one preference transaction for picker additions.
- Clear collection membership and `display_mode=false` in one commit; delete managed files only after that commit succeeds.
- Return the exact URI snapshot cleared by the committed operation so MainActivity releases the matching persisted URI grants.
- Release newly granted picker permissions for entries that did not survive the serialized merge.
- Preserve C07 upload/session validation, framing, limits, image validation, expiry/cancellation and HTTP response contracts.

**Regression coverage:** tests cover deduplicating merges against the latest collection, limit enforcement, one-commit upload activation, one-commit clear, and persistence-failure reporting.

### D07-F03 — Stale asynchronous weather/AQI response

**Finding:** A weather request could begin for one location, then finish after LocationActivity changed the selected zone and cleared the cache. The old response could repopulate the cache and display stale location data. A slower request could also overwrite a newer refresh.

**Repair:**
- Add a monotonically increasing request-generation token to the existing weather preference store.
- Increment the generation for each refresh and when the cache is invalidated.
- Before persisting a response, verify its generation is still current and that both selected coordinates and language still match the request snapshot.
- Discard superseded responses without changing the cache.
- When MainActivity receives a discarded response, retry immediately if the cache is still due; ordinary network failures retain the existing refresh interval behavior.

**Regression coverage:** JVM tests verify current-generation acceptance and rejection for superseded generations, changed location and changed language.

## 3. Deliberately unchanged

- No profile JSON shape, preference names/keys, active-profile ownership model, migration markers or parent PIN policy changed.
- No Child Mode lock/hide semantics, package discovery, or authorization flow changed.
- No StandBy upload endpoint, protocol, session lifecycle, limits or C07 hardening changed.
- No wallpaper content ownership or unrelated file cleanup policy changed.
- No broad preference listener, global event bus, global state store or cross-project refactor introduced.
- D01–D06 fixes are retained as the D07 parent history; no earlier project branch was rewritten or merged.

## 4. Verification and acceptance boundary

| Gate | Result |
|---|---|
| Roadmap and handover scope checked | PASS |
| Source ownership and concurrency-path audit | PASS |
| D07-F01 Child Mode/profile mutation repair | IMPLEMENTED |
| D07-F02 StandBy collection transaction repair | IMPLEMENTED |
| D07-F03 weather response freshness repair | IMPLEMENTED |
| Targeted JVM regression tests | ADDED; exact-head CI pending |
| Release build and APK size gate | Pending exact-head CI |
| Physical Android TV, D-pad, picker/QR concurrency and location-switch acceptance | Deferred to Stage F; not waived |
| Grouped A–G integration and cross-project regression | Deferred until all project branches are complete |
| APK publication/installation or physical-device mutation | Not performed |

The source-level tests exercise pure merge/freshness policies and preference commit outcomes. They do not substitute for device-level stress testing of simultaneous QR uploads, picker operations, profile switching, location changes or process lifecycle. Those remain in Stage F.

## 5. Finalization rule

D07 may be marked closed for source audit and CI only after both jobs pass on the final PR head, including app/installer unit tests, release build and the 1.5 MiB APK size gate. If any final report or code commit changes the feature head, exact-head CI must be rerun. PR remains OPEN/DRAFT/UNMERGED under D-013; no merge or release approval is implied.
