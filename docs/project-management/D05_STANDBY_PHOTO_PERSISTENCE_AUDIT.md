# D05 — StandBy photo collection persistence and upload response audit

**Date:** 1 November 2026  
**Finding:** D01-F11  
**Status:** Source audit and implementation closed; exact-head CI PASS on final implementation head `427dd6a92601e472bae01c3964c6059e551307a5` (CI #395 / run `41310507890`).

## 1. Scope and ownership

D05 is a targeted continuation of D01-F11. The owning persistence component is `StandbyPhotoStore`; the upload boundary is `WallpaperUploadServer.handleStandbyUpload()`.

The audited state consists of:
- `standby_prefs/photo_uris`: newline-separated private photo URI collection.
- `standby_prefs/display_mode`: whether StandBy photo mode is enabled.
- `files/standby_photos/`: private image files referenced by the collection.

The server already deletes a newly stored image and sends a non-2xx response when the store reports failure. The confirmed defect was inside the store: it committed the URI list, ignored the boolean result of a second mode commit, and returned success regardless.

## 2. Root cause

Previous upload persistence sequence:
1. Commit updated `photo_uris`.
2. Call `setPhotoMode(context, true)`.
3. Ignore the mode commit result.
4. Return `true`, allowing the server to send HTTP 200.

A failed second commit could therefore leave the collection and mode inconsistent on durable storage while the phone received a success response.

## 3. Implementation

- Introduced explicit upload outcomes: `ADDED`, `ALREADY_PRESENT`, `REJECTED`, and `PERSISTENCE_FAILED`.
- The upload path now writes `photo_uris` and `display_mode=true` through one `SharedPreferences.Editor` and one synchronous `commit()`.
- The upload path returns `PERSISTENCE_FAILED` if that commit reports failure; it no longer claims success after a failed durable write.
- The server maps successful/already-present outcomes to HTTP 200, validation/collection rejection to HTTP 400, and preference persistence failure to HTTP 500.
- The server removes the newly stored image on validation/collection rejection and invokes the existing success callback only after successful persistence. On a reported persistence failure it retains the file temporarily because Android may already have updated the in-memory preference map before the disk-write failure is returned; existing orphan cleanup removes the file after restart if the URI was not durable.
- Existing session validation, request framing, upload limits, file signature/dimension validation, temporary-file handling, session expiry/cancellation, and C07 upload/session behavior are preserved.

## 4. Regression coverage

- Added a JVM test using a recording `SharedPreferences` / `Editor` proxy to verify both keys are set through one editor commit.
- Added a test proving a false commit result is propagated as failure.
- Added response mapping tests for successful, duplicate, rejected, and persistence-failed outcomes.
- Existing StandBy image rotation and upload route/framing tests remain unchanged.

## 5. Boundaries and caveats

- This change does not consolidate other preference stores, introduce a database, change keys/schema, or migrate existing user data.
- It does not broaden into C07 rework.
- A single SharedPreferences commit makes the two related keys one atomic durable write operation. Android may update its in-memory preference map before a disk-write failure is reported; therefore the API result is treated as failure and the upload is not acknowledged. Process-death/reboot and interrupted-write behavior remain part of D06/F-stage verification.
- A failed commit is reported as HTTP 500. The file is retained to avoid a dangling URI if the process-local SharedPreferences map already contains the new collection entry; the existing `cleanupOrphanedUploads()` path removes it after restart if the collection update was not durable. No new cleanup policy is introduced here.
- No APK publication/installation or physical Android TV acceptance is part of D05.

## 6. Verification gates

| Gate | Result |
|---|---|
| Targeted source audit | PASS — root cause and caller path confirmed |
| Atomic collection + mode commit | IMPLEMENTED |
| Failure-aware upload result | IMPLEMENTED |
| HTTP failure mapping | IMPLEMENTED |
| JVM regression tests | PASS | Atomic preference commit/failure and HTTP outcome mapping tests included in successful exact-head CI. |
| Exact-head CI | PASS | CI #395 / `41310507890`; both jobs SUCCESS; app/installer `testReleaseUnitTest` and `assembleRelease` succeeded. |
| C07 unrelated upload/session hardening | PRESERVED |
| PR / merge | Draft, open, unmerged under D-013 |
| Physical device acceptance | Deferred to F-stage, not waived |

## 7. Final CI evidence

- Exact implementation head tested: `427dd6a92601e472bae01c3964c6059e551307a5`.
- CI #395 / run `41310507890`: PASS; both `Launcher build and unit tests` and `Lists, tools and docs` jobs SUCCESS.
- `./gradlew --no-daemon testReleaseUnitTest assembleRelease`: BUILD SUCCESSFUL; app and installer release unit-test tasks passed.
- Release APK exact size: **1,551,005 bytes**, leaving **21,859 bytes** below the 1,572,864-byte cap. Size gate passed; manual APK publication skipped.
- No APK installation or physical Android TV acceptance claimed; those remain F-stage gates.

## 8. Changed files

- `launcher/app/src/main/java/com/gathin/tvhome/StandbyPhotoStore.java`
- `launcher/app/src/main/java/com/gathin/tvhome/WallpaperUploadServer.java`
- `launcher/app/src/test/java/com/gathin/tvhome/StandbyPhotoStoreTest.java`
- `launcher/app/src/test/java/com/gathin/tvhome/WallpaperUploadSessionRouteTest.java`
- `docs/project-management/D05_STANDBY_PHOTO_PERSISTENCE_AUDIT.md`
