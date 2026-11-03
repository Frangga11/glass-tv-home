# F05 — Persistence & Recovery Audit

**Date:** 3 November 2026  
**Status:** HOST/SOURCE EXIT PASS — physical acceptance remains DEFERRED_TO_G.  
**Integration branch:** `integration/project-f05-persistence-recovery`  
**Integration PR:** #54 — Draft / open / unmerged  
**Approved base:** F04 branch `integration/project-f04-feature-isolation-v2`, exact head `6ffc8b4c253beb2662d6978ba38033039c3c53f9`  
**Current F05 integration head:** `1048b0b68ae5ab5a2ad867183236b514e0c691fa`

## 1. Scope and boundary

F05 validates persistence and recovery for profile/Avatar data, Wallpaper collection and selection, StandBy collection/mode, Home focus and Settings tab state, interrupted QR uploads, and scheduled prayer alarms.

Host/source tests do not simulate Android force-stop, process death, or a real TV reboot. Those physical acceptance cases remain explicit Project G gates. No backup/reinstall/cloud/OEM device-to-device restore guarantee is introduced; `android:allowBackup="false"` remains unchanged.

## 2. Current integration and CI checkpoint

| Gate | Evidence | Result |
|---|---|---|
| Approved F04 ancestry | F05 is 14 commits ahead / 0 behind F04; five changed paths only | PASS |
| Cross-feature persistence source guards | F05 PR #54 run #41563437433: 80 Python tests, 1 intentional README-count skip | PASS |
| F05 Android unit/release build and APK budget | Run #41563437433 | IN PROGRESS at checkpoint |
| E08 owner fix for F05-WALL-001 | PR #41 head `edd24d72256d06da4578971ea7bac1802746a3a6`, run #41562661052; both jobs SUCCESS; 15 tests / 1 intentional skip; APK 1,564,197 bytes / 1,572,864-byte cap / 8,667-byte headroom | PASS |
| C07 owner fix for F05-WALL-002 | PR #24 head `2315f12472f469843c1441ad7d01c6898858c4c5`, run #41563429553; both jobs SUCCESS; 23 tests / 1 intentional skip; APK 1,551,661 bytes / 21,203-byte headroom | PASS |

No workflow artifact was published; no APK was installed. All source PRs remain Draft/open/unmerged.

## 3. Findings and repairs

### F05-WALL-001 — stale pinned Custom Wallpaper after interrupted deletion

**Root cause:** startup loaded `custom_single_mode` and `custom_single_file` without reconciling them against the actual private collection. If a selected image was deleted and the process stopped before UI preference cleanup, the saved pin could reference a missing file. An empty Custom collection could also remain selected.

**Repair owner:** E08, PR #41, branch `feature/e08-wallpaper-settings-redesign`.

**Repair:** added `reconcileActiveWallpaperCategoryForStartup()` before collection loading. It:
- validates the stored filename with `UploadFilePolicy.isSafeStoredImageName`;
- clears a stale/unsafe/missing/zero-length single-file pin;
- falls back to Natural when the Custom directory is genuinely empty;
- preserves saved state when an existing directory cannot be read, rather than interpreting an I/O failure as an empty collection.

The owner repair is mirrored in F05 MainActivity. The focused regression suite passed on the exact owner head and is included in the F05 integration tests.

### F05-WALL-002 — abandoned custom Wallpaper QR upload parts after process death

**Root cause:** the Wallpaper upload handler removed temporary `custom_*.upload` files on normal request paths, but a process kill bypassed the worker's cleanup. Startup had orphan cleanup for Avatar and StandBy, but not custom Wallpaper.

**Repair owner:** C07, PR #24, branch `feature/c07-upload-session-resilience`.

**Repair:** added `cleanupOrphanedCustomWallpaperUploads()` and invoked it during launcher startup. It targets only `custom_*.upload` parts in the private `custom_wallpapers` directory, validates canonical parent containment, and leaves committed image files untouched. A process-local synchronized active-upload registry protects live worker files; creation/registration is serialized against startup cleanup, and worker `finally` unregisters and removes incomplete parts. After process death the registry is gone, so remaining partial files are eligible for cleanup on next launch. The implementation does not introduce wall-clock logic into upload deadlines.

The owner source and regression test are mirrored into F05. Owner Python regression tests passed; Android exact-head CI remains in progress.

## 4. Cross-feature source contracts reviewed

- **Avatar/Profile:** profile JSON serializes custom avatar paths; active profile is restored; replacement commits the new reference before old-file cleanup; startup cleanup handles unreferenced/stale avatar uploads.
- **StandBy:** collection URI list and photo mode use one synchronous commit; clearing persists empty state before deleting photo files; startup orphan cleanup runs; MainActivity and ClockDream read the same StandbyPhotoStore.
- **Navigation:** Home focus keys/position and Settings tab are read and written through existing stable NavigationState keys.
- **Prayer alarms:** BOOT_COMPLETED receiver remains non-exported, validates action/zone through AzanBootRecoveryPolicy, and delegates to Azan.schedule. Backup remains disabled.
- **Wallpaper:** selected custom file and empty collection are reconciled on startup; interrupted QR upload parts have isolated cleanup.

These are source-contract checks, not device acceptance.

## 5. Final host/source exit and Project G handoff

- C07 owner PR #24 exact head `2315f12472f469843c1441ad7d01c6898858c4c5`, run #41563429553: both required jobs SUCCESS; 23 Python tests / 1 intentional skip; Android unit tests/release build and APK budget PASS; APK 1,551,661 bytes / 21,203-byte headroom; artifacts empty and publication skipped.
- E08 owner PR #41 exact head `edd24d72256d06da4578971ea7bac1802746a3a6`, run #41562661052: both required jobs SUCCESS; 15 Python tests / 1 intentional skip; APK 1,564,197 bytes / 8,667-byte headroom; artifacts empty and publication skipped.
- F05 integration PR #54 exact head `1048b0b68ae5ab5a2ad867183236b514e0c691fa`, run #41563437433: both required jobs SUCCESS; 80 Python tests / 1 intentional skip; Android `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and size gate PASS; APK 1,566,861 bytes / 6,003-byte headroom; artifact list empty.
- Owner/integration source cross-audit confirms F05-WALL-001 startup reconciliation helper and regression tests are byte-identical between E08 and F05; F05-WALL-002 cleanup method, active-upload registration/finally cleanup and regression tests are byte-identical between C07 and F05. Launcher startup invokes the cleanup method in both owner and integration source.
- PR #24, #41 and #54 remain Draft/open/unmerged. No APK publication/installation, device setting changes, or physical TV testing.

**Decision: F05 HOST/SOURCE EXIT PASS.** No remaining reproducible host/source blocker. This does not claim actual force-stop, process-death, reboot, alarm restoration, StandBy/DreamService, phone/LAN or OEM acceptance. Carry those device-only gates to Project G for real Mi TV Stick validation.
