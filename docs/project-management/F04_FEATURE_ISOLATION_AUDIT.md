# F04 — Avatar, Wallpaper & StandBy Feature Isolation Audit

**Date:** 3 November 2026  
**Status:** F04 HOST/SOURCE EXIT PASS; C07/E08 owner exact-head CI PASS; physical acceptance remains deferred to Project G.  
**Integration PR:** #53 — Draft/open/unmerged  
**Integration branch:** `integration/project-f04-feature-isolation-v2`  
**Base:** `integration/project-f02-system-validation` at `2ede452b3137580bd51088661f80b6d0eeac7f4b`  
**Latest recorded F04 head:** `6ffc8b4c253beb2662d6978ba38033039c3c53f9`

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

**Latest observed Python and Android result:** F04 integration head `6ffc8b4c253beb2662d6978ba38033039c3c53f9` passed exact-head CI #41560811925 (both required jobs): **65 Python tests, 1 intentional skip**; Android `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and size gate PASS. APK **1,566,045 bytes** against 1,572,864-byte cap (6,819-byte headroom); workflow artifacts empty and publication skipped. C07 owner exact-head CI #41560200291 passed both jobs (21 tests / 1 intentional skip; APK 1,550,961 bytes). E08 owner exact-head CI #41560600033 passed both jobs (11 tests / 1 intentional skip; APK 1,564,089 bytes).

Earlier intermediate CI failures were used as regression signals:
- A too-strict test assertion was corrected to match the guarded `exists() && !delete()` contract.
- A whole-file owner-branch copy was rejected after CI showed it removed F02 B01 modal and D05 adapter contracts. F04 was restored from the approved F02 base and only the scoped changes were mirrored.
- E08 compilation exposed that its branch needed the new default listener callback declaration; the interface alignment was added and exact-head CI rerun.

## 5. Owner and integration evidence

| Workstream | Branch / PR | Head | CI state |
|---|---|---|---|
| C07 owner partial-clear repair | `feature/c07-upload-session-resilience`, PR #24 | `b7a40fdc2c52268ec69c95731983d2f95024eb57` | Exact-head CI #41560200291 PASS (both jobs; 21 tests, 1 intentional skip; APK 1,550,961 bytes) |
| E08 owner delete reliability repair | `feature/e08-wallpaper-settings-redesign`, PR #41 | `c613e3c5f593556748d1593d23f2899ecee36e4a` | Exact-head CI #41560600033 PASS (both jobs; 11 tests, 1 intentional skip; APK 1,564,089 bytes) |
| F04 integrated audit | `integration/project-f04-feature-isolation-v2`, PR #53 | `6ffc8b4c253beb2662d6978ba38033039c3c53f9` | Exact-head CI #41560811925 PASS (both jobs; 65 tests / 1 intentional skip; APK 1,566,045 bytes) |

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
