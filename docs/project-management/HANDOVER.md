# 05 — Project Handover

**Version:** 0.1-draft  
**Date:** 31 October 2026  
**Current phase:** Documentation and baseline reconciliation

## 1. How to resume this project in a new conversation

Attach or link the latest Project Master export and provide:
- repository: https://github.com/Frangga11/glass-tv-home
- documentation branch: `docs/project-master-inventory`
- working branch to review: `feature/settings-reorganization-standby-gallery`
- related avatar candidate: `fix/avatar-photo-upload-audit`

Then ask to continue from HANDOVER.md and not to repeat closed checks. The assistant must first inspect the latest GitHub commit and compare it with this document's version/date.

## 2. Current verified position

- Documentation branch has been created from `feature/settings-reorganization-standby-gallery`.
- Initial documentation files are being added under `docs/project-management/`.
- Gradle source confirms app version 4.0.0 / code 40, minSdk 26, compileSdk 34, targetSdk 34, Java 17.
- Settings/gallery branch contains seven built-in profile avatars, profile JSON with avatarIndex, and no personal avatar upload control in the Profile settings.
- Separate `fix/avatar-photo-upload-audit` branch contains a candidate custom-avatar feature:
  - `customAvatarPath` profile field,
  - Android document picker,
  - private app storage under `filesDir/profile_avatars`,
  - upload and reset buttons in the active Profile settings,
  - custom image used in top-bar avatar.
- Candidate branch still has gaps in profile switcher, profile card/sidebar, add-profile flow, image orientation, picker availability, and lifecycle/replacement testing.

## 3. Work not yet performed

- No application source code has been changed in this documentation pass.
- No avatar code has been merged or cherry-picked.
- No release APK has been built in this session.
- No APK has been installed on the user's TV in this session.
- No live device test has been performed in this session.
- No final QC approval has been issued.

## 4. Next ordered work

1. Finish source inventory for wallpaper defaults, icon animation, information tab layout, weather/AQI, prayer times, upload server, and ADB safety.
2. Compare branch ancestry and relevant commits to identify the safest application baseline.
3. Review the avatar candidate implementation line-by-line and resolve A-001 through A-010.
4. Integrate only reviewed changes into the chosen working branch.
5. Run static checks and GitHub Actions / Gradle release build where available.
6. Install on Mi TV Stick and execute device acceptance checklist.
7. Update documentation, export backup to ChatGPT Library, and publish a versioned handover.

## 5. Resume instruction

Do not assume any item is complete merely because it appears in the source. Read the current branch and commit first. Do not modify the user's TV, disable packages, replace the launcher, or merge feature branches without a clear rollback plan and user authorization.
