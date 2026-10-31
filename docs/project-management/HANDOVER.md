# 05 — Project Handover

**Version:** 0.1-draft  
**Date:** 31 October 2026  
**Current phase:** Avatar QR implementation passed CI; actual device acceptance pending

## 1. How to resume this project in a new conversation

Attach or link the latest Project Master export and provide:
- repository: https://github.com/Frangga11/glass-tv-home
- documentation branch: `docs/project-master-inventory`
- working branch to review: `feature/settings-reorganization-standby-gallery`
- avatar QR audit branch: `fix/avatar-qr-upload-single-photo`
- draft PR: https://github.com/Frangga11/glass-tv-home/pull/6
- avatar audit report: `docs/project-management/AVATAR_AUDIT.md`

Then ask to continue from HANDOVER.md and not to repeat closed checks. The assistant must first inspect the latest GitHub commit and compare it with this document's version/date.

## 2. Current verified position

- Documentation branch has been created from `feature/settings-reorganization-standby-gallery`.
- Initial documentation files are being added under `docs/project-management/`.
- Gradle source confirms app version 4.0.0 / code 40, minSdk 26, compileSdk 34, targetSdk 34, Java 17.
- Settings/gallery branch contains seven built-in profile avatars, profile JSON with avatarIndex, and no personal avatar upload control in the Profile settings.
- The earlier `fix/avatar-photo-upload-audit` branch contained a picker-based custom-avatar candidate.
- The current `fix/avatar-qr-upload-single-photo` branch adds the QR-only replacement workflow:
  - `customAvatarPath` profile field,
  - Dedicated QR server with one-image upload, random session token, and five-minute expiry,
  - private app storage under `filesDir/profile_avatars`,
  - QR upload and reset buttons in the active Profile settings,
  - custom image used in top bar, settings card, and profile switcher.
  - transactional profile reference update before deleting the previous avatar file.
  - EXIF-aware downsampled avatar rendering and stale upload cleanup.
- Candidate branch still has gaps in profile switcher, profile card/sidebar, add-profile flow, image orientation, picker availability, and lifecycle/replacement testing.

## 3. Work not yet performed

- Avatar source changes have been committed to `fix/avatar-qr-upload-single-photo`; see draft PR #6.
- No changes have been merged into the settings/gallery baseline.
- No avatar code has been merged or cherry-picked.
- GitHub CI passed in workflow run 41199143712 for the avatar code commit: docs/tools, Android unit tests, release build, APK-size gate, and artifact publication all succeeded.
- Release APK artifact size: 1,107,399 bytes; SHA-256: 65a60f1fa2a2fce23520721337a82ecf88ddb287ffd87ad801ff27d14bf8fab9.
- Artifact / workflow: https://github.com/Frangga11/glass-tv-home/actions/runs/41199143712.
- A later commit updates only the user guide; the latest PR CI docs job passed, and the launcher job was still running at this handover snapshot.
- No release APK has been installed on the user's TV in this session.
- No APK has been installed on the user's TV in this session.
- No live device test has been performed in this session.
- No final QC approval has been issued.

## 4. Next ordered work

1. Finish source inventory for wallpaper defaults, icon animation, information tab layout, weather/AQI, prayer times, upload server, and ADB safety.
2. Compare branch ancestry and relevant commits to identify the safest application baseline.
3. Run the AVATAR_AUDIT.md phone/TV acceptance matrix and collect evidence; do not repeat CI unless code changes.
4. Update audit register and merge PR #6 only after device acceptance.
6. Continue with the next audit area only after avatar reaches final acceptance.
7. Update documentation, export backup to ChatGPT Library, and publish a versioned handover.

## 5. Resume instruction

Do not assume any item is complete merely because it appears in the source. Read the current branch and commit first. Do not modify the user's TV, disable packages, replace the launcher, or merge feature branches without a clear rollback plan and user authorization.
