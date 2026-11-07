# UI Refresh G3C Checkpoint — Profile, Avatar & Child Mode

**Date:** 7 November 2026  
**Disposition:** COMPLETE / SOURCE-HOST QC PASS  
**Governing plan:** GTVH-UIR-002 v2.0

## Repository and PR

- Repository: `Frangga11/glass-tv-home`
- Owner branch: `feature/g3c-profile-avatar-child-mode`
- Exact HEAD: `8cf81f84bd62d0b3da31532116553cfa21a5df98`
- PR #68: OPEN / DRAFT / UNMERGED
- Base branch: `feature/g3b-weather-aqi-v4`
- Base exact HEAD: `8cfc5c886950cc9d27d4530ba901df872b8e5c44`
- G3C was kept on its existing owner branch. No merge, rebase, cherry-pick, force-push or integration/main mutation.

## Completed work

### P1 — Security Policy & Authorization

- Existing parent PIN required during Child Mode for Android Settings main, Accessibility, Notification Access and Default Home / Launcher.
- Wi-Fi, Display, Screensaver / Dream and ordinary Quick Settings remain exempt.
- Centralized launcher-scoped gate preserves authorization across firmware fallback attempts.
- Existing PIN mechanism was not changed. Default `0000` and plain-text SharedPreferences remain residual security risks. This is not system-wide Android parental control.

### P2 — Profile Selector & Navigation

- Active profile has a separate visible status label; D-pad focus remains a separate cyan focus treatment.
- Selector initial focus returns to the active profile rather than always the first profile.
- Reuses G1 V6 V03 direction and G2 shared focus/dialog tokens.
- Profile create, switch, rename and delete surface persistence failures rather than reporting false success.

### P3 — Avatar & QR Upload

- Preserves seven built-in avatars, profile-scoped custom photo storage, QR upload, and reset-to-built-in behavior.
- QR upload requires a running, unexpired session at the commit boundary.
- Existing route, image format/size/dimension, private-path, storage rollback and upload policy tests remain in place.
- Separate GIPHY avatar PR #62 was not merged or pulled into this G3C change set.

### P4 — Child Mode, App Lock & Hidden Apps

- Child Mode, locked apps and hidden apps remain profile-scoped.
- Existing parent-PIN gates for policy editing, profile deletion and switching away from Child Mode are preserved.
- App-policy picker now stages edits in memory. **Done** writes the complete selected set once; **Back/dismiss** writes nothing.
- Failed writes keep the picker open for retry. Successful writes refresh Home and the Settings summary.

### P5 — Persistence, Lifecycle & Regression

- Profile creation, active-profile switching, rename, deletion, Child Mode and app-policy writes use durable commits and failure handling.
- Active-profile deletion and fallback selection are one persisted transaction.
- Legacy device-wide Child Mode/app-policy settings migrate while the original owning profile is still active, before deletion cleanup.
- Missing/duplicate profile IDs are repaired and persisted. Unreadable profile JSON is preserved in a backup key before fallback recovery.
- Added `ProfileManagerPersistenceTest` for profile CRUD, profile isolation/cleanup, ID repair, corrupt-data recovery, avatar storage/reset, legacy migration and active-profile deletion ownership.
- Added `AvatarUploadSessionPolicyTest` for stopped, active and exact-expiry session boundaries.

## Verification and evidence

- P1 exact-head CI #1307 / `42338164003`: both required jobs SUCCESS on P1 head `16577e0210470762423ed0bf6cc7624c33537850`.
- Final exact-head CI #1313 / `42340859666` and #1314 / `42340866781`: both required jobs SUCCESS on `8cf81f84bd62d0b3da31532116553cfa21a5df98`.
  - Launcher build and unit tests: SUCCESS.
  - Lists, tools and docs: SUCCESS.
  - `testReleaseUnitTest assembleRelease`: BUILD SUCCESSFUL.
- Final release APK diagnostic: **1,602,361 bytes**. This is 1,140 bytes above the P1 exact-head diagnostic of 1,601,221 bytes and above the historical 1.5 MiB reference. D-023 classifies the size warning as informational/non-blocking; no quality/functionality was removed for size.
- No APK artifact was published.

## Final scope boundary

- Source/host verification is complete.
- Physical Android TV, remote-control, OEM firmware, overscan and same-Wi-Fi phone upload acceptance remain **DEFERRED / NOT RUN**, not waived.
- No APK installation/publication, ADB, device configuration, merge, rebase, cherry-pick, force-push or integration/main change occurred.

## Next work point

**G3D — Quick Settings.** G1, G2, G3A, G3B and G3C are closed for source-host scope. Do not reopen completed work without new verified defect evidence.
