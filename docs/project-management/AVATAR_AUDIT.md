# 06 — Avatar QR Upload Audit

**Audit date:** 31 October 2026  
**Audit status:** CI/build passed; device acceptance pending  
**Code branch:** fix/avatar-qr-upload-single-photo  
**Base branch:** feature/settings-reorganization-standby-gallery  
**Pull request:** https://github.com/Frangga11/glass-tv-home/pull/6  
**Latest reviewed head:** 0cb1b96e2c2529e65795231519c6e0728b46390d
**Latest CI head:** 0cb1b96e2c2529e65795231519c6e0728b46390d

## 1. User acceptance requirements

- Upload one personal profile photo by scanning a QR code with a phone on the same local network as the TV.
- The QR session must be short-lived and scoped to the selected profile.
- Replacing a photo must not accumulate previous avatar files.
- The previous photo must remain intact if the new upload or profile metadata update fails.
- The active avatar must remain consistent across the top bar, Settings profile card, and profile switcher.
- Built-in avatar reset must remove the custom photo.
- Wallpaper and standby photo upload workflows must remain separate.

## 2. Findings in the original candidate

| Finding | Original state | Action in audit branch |
|---|---|---|
| Avatar upload method | Android document picker, not QR | Removed picker flow from MainActivity; QR is the only visible upload action |
| QR avatar endpoint | Not present; existing QR routes were for wallpaper/standby | Added dedicated AvatarUploadServer |
| Upload scope | Existing photo upload endpoints were collection-oriented | Avatar endpoint accepts exactly one file for one profile |
| Session security | No avatar-specific token or expiry | Random session token and five-minute expiry |
| File size / image validation | Picker path accepted large images subject to bounds | QR upload capped at 15 MB and validates image bounds |
| Replacement semantics | Temp/backup rename path needed stronger transactional handling | New file is committed as profile reference before previous file is deleted |
| Failed metadata commit | Previous photo should not be lost | Failed profile commit returns upload error and preserves previous photo |
| Old file cleanup | Not consistently transactional | Old file deletion restricted to app-private avatar directory after successful commit |
| Image rendering | Full-size decode and EXIF uncertainty | Downsample before rendering and apply EXIF orientation |
| Abandoned upload files | Interrupted uploads could leave temp files | Startup cleanup removes stale .upload files older than ten minutes |
| Built-in reset | File cleanup followed asynchronous preference write | Synchronous preference commit precedes deletion |

## 3. Implemented source changes

- AvatarUploadServer.java
  - Separate single-photo endpoint.
  - Random unguessable route token.
  - Five-minute session expiry.
  - Enforces one successful upload per QR session; failed uploads release the claim so the user can retry.
  - 15 MB upload limit.
  - Image bounds validation.
  - Temporary upload file in private filesDir/profile_avatars.
  - Reports success only after the profile metadata commit succeeds.
- ProfileManager.java
  - Profile-specific custom avatar reference.
  - Transactional replacement and deletion of the superseded file.
  - Transactional reset to built-in avatar.
  - Downsampled rendering and EXIF orientation handling.
  - Startup cleanup for unreferenced avatar files, old backup/temp files, and abandoned uploads.
  - Cleanup is also run immediately after replacement/reset/profile deletion.
- MainActivity.java
  - QR session lifecycle and status feedback.
  - UI refresh after successful replacement.
  - Stops the QR endpoint when the dialog closes or QR generation fails.
  - Removes the legacy avatar document-picker path.
- ModernSettingsDialog.java
  - Avatar upload action now opens the dedicated QR flow.
  - Built-in avatar selection/reset reports deletion failures instead of silently claiming success.
- PANDUAN_PENGGUNA.md
  - Documents the QR upload flow, 15 MB limit, five-minute session, and replacement behavior.

## 4. Branch reconciliation

GitHub comparison confirms:
- fix/avatar-photo-upload-audit is eight commits ahead of feature/settings-reorganization-standby-gallery, with no commits behind.
- fix/avatar-qr-upload-single-photo is based on the avatar candidate branch and adds the QR-specific implementation.
- Draft PR #6 targets feature/settings-reorganization-standby-gallery.
- Do not merge until CI and device acceptance pass.

## 5. Verification status

| Verification | Status | Evidence / next action |
|---|---|---|
| Source inspection | COMPLETE | Reviewed avatar model, settings, top bar, switcher, upload server, and profile persistence |
| Branch ancestry | COMPLETE | GitHub compare result: candidate branch is a direct descendant of settings/gallery baseline |
| Documentation consistency | COMPLETE | User guide updated for QR rather than picker |
| GitHub CI — docs/tools job | PASS | Latest PR workflow run 41199199372 |
| GitHub CI — Android unit tests/release build | PASS | Latest PR workflow run 41199199372; launcher unit tests and release build passed |
| QR upload from physical phone | NOT TESTED | Requires TV and phone on same Wi-Fi |
| Replace photo twice; verify old file removed | NOT TESTED | Inspect private app storage / app behavior |
| Failed upload / failed profile commit rollback | NOT TESTED | Exercise invalid image, interrupted transfer, and storage failure |
| Process death / reboot persistence | NOT TESTED | Test on target Mi TV Stick |
| EXIF portrait / landscape / large / corrupt files | NOT TESTED | Device test matrix |
| D-pad focus, OK, Back, cancel | NOT TESTED | Remote-only acceptance |
| Final release approval | NOT APPROVED | CI passed; actual Mi TV Stick and phone evidence still required |

## 6. Build artifact

- Workflow run: https://github.com/Frangga11/glass-tv-home/actions/runs/41199199372
- Artifact: glass-tv-home-pagar-alam-release (artifact ID 13042201386)
- APK size: 1,107,392 bytes (below the 1.2 MiB CI limit)
- SHA-256: b4dc78502db5104e9b226fd75c232f0f090239a8cff979560e6784e5e5208ca8
- Artifact expiry: 14 November 2026

## 7. Device acceptance checklist

1. Open Settings → Profile and start QR upload.
2. Scan QR from a phone on the same Wi-Fi; verify the upload page opens.
3. Upload a valid portrait JPEG and verify the active avatar changes.
4. After the first successful upload, try to upload again using the same QR; it must be rejected. Close the dialog and create a new QR for the next replacement.
5. Upload a second photo using the new QR; verify the first file is no longer present and only one active photo remains for that profile.
6. Repeat with landscape JPEG and PNG.
7. Test EXIF-rotated phone photo.
8. Try oversized, corrupt, and non-image files; verify failure and that the previous avatar remains.
9. Close the QR dialog before upload; verify the old QR route no longer works.
10. Wait beyond five minutes; verify the expired QR session is rejected.
11. Force-stop/restart launcher and reboot TV; verify avatar persistence.
12. Switch profiles; verify each profile displays its own avatar.
13. Reset to built-in; verify custom photo file is deleted and built-in animal appears everywhere.
14. Test with TV remote only: focus, OK, Back, cancel.
15. Confirm wallpaper and standby QR upload still work independently.

## 8. Rollback

- Do not merge PR #6 until all required checks pass.
- If the change fails CI or device acceptance, close/revise the draft PR and keep the current feature/settings-reorganization-standby-gallery branch unchanged.
- The existing settings/gallery branch remains the rollback baseline.
- No device installation or launcher replacement has been performed by this audit.

## 9. Final disposition

**NOT FINAL.** Code implementation, documentation, CI, unit tests, and release build have passed on the dedicated branch. No physical-device test has been performed. Do not label the avatar feature release-ready until the checklist above is evidenced on the actual Mi TV Stick and phone.
