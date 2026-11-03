# F04 — Avatar, Wallpaper & StandBy Feature Isolation Audit

**Date:** 3 November 2026  
**Status:** SOURCE AUDIT PASS WITH ONE REPAIRED FINDING; latest exact-head CI is pending.  
**Integration PR:** #53 — Draft/open/unmerged  
**Integration branch:** `integration/project-f04-feature-isolation-v2`  
**Base:** `integration/project-f02-system-validation` at `2ede452b3137580bd51088661f80b6d0eeac7f4b`  
**Latest recorded F04 head:** `e895ee590a607a8194b2037585ed47f2cf464961`

## 1. Objective and boundary

F04 validates the integrated user flows and ownership boundaries for:
- Profile-scoped Avatar QR upload, replacement and cleanup.
- Wallpaper QR upload, gallery management, delete/select/slideshow and separate settings entry points.
- StandBy QR upload, photo collection, photo mode, in-app screensaver and Android DreamService renderer.
- Shared QR-dialog/server lifecycle and cross-feature storage isolation.

F04 is a host/source integration audit. It does not claim real phone/LAN, OEM DreamService, process-death/reboot, D-pad/remote, or physical TV acceptance. Those remain explicit Project G gates. F05 owns broader persistence/force-stop/reboot validation.

## 2. Verified source contracts

| Contract | Evidence | Result |
|---|---|---|
| Private storage roots are distinct | `profile_avatars`, `custom_wallpapers`, and `standby_photos` are separate app-private directories | PASS |
| Avatar ownership remains profile-scoped | QR server captures the requested `profileId`; callback calls `ProfileManager.replaceCustomAvatar(this, profileId, file)` | PASS |
| Avatar replacement is durable before old-file cleanup | Profile reference commit precedes unreferenced-file cleanup; candidate must resolve inside the private avatar directory | PASS |
| Wallpaper and StandBy QR routes are distinct | Session-specific `/wallpaper/<token>` and `/standby/<token>`; `standbyOnly` mode rejects cross-mode routes | PASS |
| Shared QR dialog closes server resources | Dismiss listener stops and clears both Wallpaper and Avatar server references | PASS |
| StandBy data is isolated | Dedicated `standby_prefs` and `standby_photos`; clear operation does not target wallpaper/avatar directories | PASS |
| StandBy upload state is durable | Photo URI collection and photo-mode flag are committed together; UI callback confirms durable collection membership | PASS |
| Both StandBy renderers share the same collection | MainActivity in-app overlay and `ClockDream` use `StandbyPhotoStore` | PASS |
| Wallpaper clear is scoped | Local manager and QR API target `custom_wallpapers`; no Avatar/StandBy path is used | PASS |
| Quick Control and Appearance remain distinct | Quick Control uses compact Change/Shuffle/Next surface; Appearance uses separate Collection, Interval and Personal Wallpaper Manager cards; both reuse shared wallpaper state | PASS |
| Default wallpaper metadata remains singular | Bundled Default metadata resolves only the designated built-in Dempo image | PASS |

## 3. Finding and repair

### F04-WALL-001 — false success on wallpaper delete failures

**Severity:** P2 reliability / state-consistency.  
**Root cause:** Single-delete, delete-all and keep-one/delete-others paths ignored some `File.delete()` return values. A failed deletion could leave a file behind while the UI displayed success. The QR API clear endpoint also returned immediately on the first failure without notifying the owner after earlier files might already have been deleted.

**Owner repairs:**
- E08 owner branch `feature/e08-wallpaper-settings-redesign`:
  - Check each single/bulk delete result.
  - Treat already-absent files as already removed.
  - Add localized `wallpaper_delete_failed` feedback in default, Indonesian and English resources.
  - Reconcile the remaining Custom collection and clear stale single-file pin state after partial bulk deletion.
  - Wire the QR API clear-failure callback to the same state-reconciliation path.
- C07 owner branch `feature/c07-upload-session-resilience`:
  - Add default `UploadListener.onClearFailed()`.
  - Attempt the remaining files after a delete failure, notify the owner to reconcile UI state, and return HTTP 500 rather than reporting success.
  - Keep the clear operation scoped to `custom_wallpapers`.

The owner changes were mirrored into F04 with a narrow source patch. The full F02-integrated `MainActivity` and `WallpaperUploadServer` were preserved; no wholesale owner-file replacement was accepted because it would have dropped B01 modal and D05 StandBy upload-result contracts.

## 4. Regression coverage

Added:
- `tests/test_media_feature_isolation.py`: storage-root isolation, profile-bound Avatar callback, safe replacement/cleanup, StandBy storage and renderer sharing, QR route/server lifecycle separation, Quick Control versus Appearance separation, Wallpaper deletion-result handling and partial-state reconciliation.
- `tests/test_wallpaper_api_clear_failure.py`: C07 API clear failure callback, HTTP error result and directory-boundary guards.

**Latest observed Python result:** F04 Lists/tools/docs job on head `e895ee590a607a8194b2037585ed47f2cf464961` passed **63 tests, 1 intentional skip** (README count normalization on integration branches). C07 owner test job passed **21 tests, 1 intentional skip**. Android unit/release build jobs were still in progress at the time of this checkpoint; do not mark the latest F04 head CI fully green until both required jobs finish successfully.

Earlier intermediate CI failures were used as regression signals:
- A too-strict test assertion was corrected to match the guarded `exists() && !delete()` contract.
- A whole-file owner-branch copy was rejected after CI showed it removed F02 B01 modal and D05 adapter contracts. F04 was restored from the approved F02 base and only the scoped changes were mirrored.
- E08 compilation exposed that its branch needed the new default listener callback declaration; the interface alignment was added and exact-head CI rerun.

## 5. Owner and integration evidence

| Workstream | Branch / PR | Head | CI state |
|---|---|---|---|
| C07 owner partial-clear repair | `feature/c07-upload-session-resilience`, PR #24 | `b7a40fdc2c52268ec69c95731983d2f95024eb57` | Lists/tools/docs PASS (21 tests, 1 intentional skip); Android build pending |
| E08 owner delete reliability repair | `feature/e08-wallpaper-settings-redesign`, PR #41 | `717cb37ab888b095fec6a83a192f81708739199b` | Lists/tools/docs PASS; Android build pending |
| F04 integrated audit | `integration/project-f04-feature-isolation-v2`, PR #53 | `e895ee590a607a8194b2037585ed47f2cf464961` | Lists/tools/docs PASS (63 tests, 1 intentional skip); Android build pending |

All three PRs remain Draft/open/unmerged under D-013. No APK publication or installation occurred.

## 6. Project G deferred acceptance

The following remain NOT VERIFIED, not PASS:
1. Real phone QR scanning and same-Wi-Fi/Ethernet upload on the target Mi TV Stick.
2. Profile switching and Avatar replacement with the actual remote/phone flow.
3. Network loss, retry, cancel, expiry and reconnect on the actual OEM network stack.
4. StandBy upload, rotation, in-app screensaver and DreamService behavior across screen-off/on, Activity/DreamService recreation and device reboot.
5. Wallpaper/StandBy/Avatar UI rendering, focus and Back behavior on physical 1080p and 4K screens.
6. Real-device low-storage and filesystem deletion-failure behavior.

## 7. Decision

- **Source isolation audit:** PASS.
- **F04-WALL-001:** repaired on owner branches and mirrored into F04; exact-head CI pending at this checkpoint.
- **Overall F04:** PARTIAL / NOT FINAL until latest F04 and owner CI finish green.
- Keep PR #24, #41 and #53 Draft/open/unmerged. Do not publish/install APK or perform TV settings changes in Project F.
