# 02 — Feature and Code Inventory

**Version:** 0.1-draft  
**Date:** 31 October 2026  
**Review status:** Initial source inspection; broad audit still open

## 1. Repository and branches

Repository: https://github.com/Frangga11/glass-tv-home

Branches found:
- `main`
- `feature/settings-reorganization-standby-gallery`
- `fix/avatar-photo-upload-audit`
- `fix/avatar-qr-upload-single-photo`
- `feature/independent-visual-effects-id`
- `fix/power-off-accessibility`
- `indonesia-pagar-alam`

Current documentation branch:
- `docs/project-master-inventory` (created from `feature/settings-reorganization-standby-gallery`).

Working baseline selected for the first comparison:
- `feature/settings-reorganization-standby-gallery`

Related avatar implementation candidate:
- `fix/avatar-photo-upload-audit`

Current QR avatar audit branch:
- `fix/avatar-qr-upload-single-photo` (draft PR #6 into settings/gallery baseline)

**Important:** branches are separate snapshots. A feature found on one branch must not be attributed to another branch until merged or cherry-picked.

## 2. Build configuration — source-confirmed

File: `launcher/app/build.gradle`

| Property | Value |
|---|---|
| namespace | com.gathin.tvhome |
| applicationId | com.gathin.tvhome |
| versionName | 4.0.0 |
| versionCode | 40 |
| minSdk | 26 |
| compileSdk | 34 |
| targetSdk | 34 |
| Java source / target | 17 |
| Release minify | enabled |
| Release resource shrinking | enabled |
| Release signing config | debug signing config |

This confirms the Gradle configuration only. It does not prove that a new APK was built in this audit session.

## 3. Feature inventory

| Area | Current finding | Status |
|---|---|---|
| Launcher home / app grid / dock | MainActivity and AppAdapter are part of the app; home is TV/D-pad oriented | CONFIRMED-CODE; device test open |
| Glass focus / spring movement | MainActivity documents a moving glass highlight and uses spring animation classes | CONFIRMED-CODE; visual QC open |
| Settings navigation | ModernSettingsDialog defines 8 tabs: Profile, Home Screen, Appearance, Status & Weather, Effects, Standby, Child Mode, Device Settings | CONFIRMED-CODE |
| Multi-user profiles | ProfileManager stores profile ID, name, avatar index, child flag in SharedPreferences JSON | CONFIRMED-CODE |
| Built-in avatars | Seven animal avatars are registered | CONFIRMED-CODE |
| Personal avatar photo | QR upload implementation on fix/avatar-qr-upload-single-photo; single-photo profile-scoped endpoint, old-photo cleanup after metadata commit | CODE IMPLEMENTED; CI and device verification pending |
| Child Mode | Child mode / PIN manager is referenced by profile and settings flows | CONFIRMED-CODE; security and UX test open |
| Top-bar avatar | QR audit branch calls getAvatarDrawable for active profile | CONFIRMED-CODE; device test pending |
| Avatar in profile switcher | Candidate branch renders getAvatarDrawable for each profile | CONFIRMED-CODE; device test pending |
| Avatar in add-profile flow | Candidate add-profile dialog still only selects one of seven built-ins | PARTIAL |
| Wallpaper QR upload | Baseline bare-root endpoint replaced in draft PR #7 by random session-token-scoped page/upload/API/thumb routes | CODE IMPLEMENTED IN PR #7; CI PASS; device acceptance pending — see WALLPAPER_AUDIT.md |
| Standby gallery | MainActivity has a separate standby photo picker/store flow | CONFIRMED-CODE |
| Weather / AQI | Weather-related activity/overlay and weather-air-quality sync are present in MainActivity | CONFIRMED-CODE; data correctness and offline test open |
| Prayer times / Islamic shelf | PrayerTimes, Location, Azan and Islamic shelf are referenced in source | CONFIRMED-CODE; location/time accuracy test open |
| Default wallpaper | Code expects `assets/kabut-pagi-dempo.jpg`, but the CI release APK does not contain the asset; fresh-install Default has no bundled image | P1 BLOCKER — requires approved image asset and APK verification; see WALLPAPER_AUDIT.md |
| Icon animation | Spring / focus animations exist; exact per-icon animation policy needs audit | PARTIAL |
| Avatar visual report | Seven built-in avatars plus QR custom-photo flow; EXIF-aware downsampled rendering | PARTIAL; real-device QC pending |
| Tab information layout | Tab names confirmed; final arrangement and screenshot acceptance not yet confirmed | PARTIAL |
| Indonesian localization | Indonesia / Pagar Alam localization branch and AppLanguage support are in repository history | CONFIRMED-CODE; full string audit open |
| OTA / update indicator | README / commit history describe background update checks and badge; current implementation needs dedicated audit | UNVERIFIED |
| QR upload fallback | Wallpaper upload server is documented as fail-safe in release history; actual current branch ports/security require code audit | UNVERIFIED |

## 4. Avatar implementation comparison

### A. settings/gallery branch
Files inspected:
- `ProfileManager.java` — blob `5dc6b8a4fa0acd9d3a321d0440251f449a08187c`
- `ModernSettingsDialog.java` — blob `bb468e61185fffb32d323f940ebc79dd9912acc9`
- `MainActivity.java` — blob `20cd6a9819b4e1df155a68d0ec7d79da5287027d`

Finding:
- Seven built-in animal avatars.
- Profile JSON stores avatarIndex, not a custom image path.
- No personal-avatar picker/upload button in the inspected Profile tab.
- Standby image picker is a separate workflow and is not an avatar uploader.

### B. avatar-audit candidate branch
Files inspected:
- `ProfileManager.java` — blob `8405ae9bd44ea08ebfdbd77ccc8443831ebed2ba`
- `ModernSettingsDialog.java` — blob `55f85cb6376c790a6472f566cdb745c1dacac5e8`
- `UserSwitchDialog.java` — blob `3523edc1145ec14273d29b63dac28fc915e66513`
- `MainActivity.java` — blob `80110ad7216e7a6b40d0995544886e82b324f03d`

Finding:
- Adds `customAvatarPath` to the profile JSON model.
- Adds `getAvatarDrawable`, `setCustomAvatar`, and `clearCustomAvatar`.
- Adds “Unggah Foto Sendiri” and “Gunakan Avatar Bawaan” controls to the active Profile settings tab.
- Uses `ACTION_OPEN_DOCUMENT` for one image and copies the selected image into app-private `filesDir/profile_avatars`.
- Top-bar icon uses `getAvatarDrawable`.
- Does not yet consistently use custom images in profile card/sidebar, profile-switch cards, or add-profile flow.
- Requires validation of picker availability, EXIF rotation, lifecycle, replacement/rollback, and TV remote navigation.

### C. QR replacement audit branch

Branch: `fix/avatar-qr-upload-single-photo` (draft PR #6).

- Adds `AvatarUploadServer.java`, a dedicated single-image QR endpoint with a random token, five-minute expiry, 15 MB cap, and image validation.
- QR upload is separate from wallpaper and standby photo endpoints.
- `ProfileManager.replaceCustomAvatar` commits the new profile path before deleting the previous file.
- Reset-to-built-in uses a synchronous preference commit before deleting the old custom file.
- Avatar rendering downsamples images and reads EXIF orientation.
- Stale `.upload` temporary files older than ten minutes are cleaned at launcher startup.
- Android document-picker avatar upload was removed from the active flow.
- CI and actual TV/phone tests remain pending; see AVATAR_AUDIT.md.

## 5. Key files identified

| File | Responsibility / relevance |
|---|---|
| `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java` | Main launcher activity, home UI, top bar, weather/Islamic shelf, photo flows |
| `launcher/app/src/main/java/com/gathin/tvhome/ProfileManager.java` | Profile persistence and avatar drawing |
| `launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java` | Main settings UI and tab content |
| `launcher/app/src/main/java/com/gathin/tvhome/UserSwitchDialog.java` | Profile switcher and new profile creation |
| `launcher/app/src/main/java/com/gathin/tvhome/WallpaperUploadServer.java` | Phone-to-TV wallpaper upload server |
| `launcher/app/src/main/AndroidManifest.xml` | App permissions, activities, services, provider |
| `launcher/app/build.gradle` | Android build configuration |

## 6. Evidence references

GitHub source URLs:
- [ProfileManager — settings/gallery](https://github.com/Frangga11/glass-tv-home/blob/feature/settings-reorganization-standby-gallery/launcher/app/src/main/java/com/gathin/tvhome/ProfileManager.java)
- [ModernSettingsDialog — settings/gallery](https://github.com/Frangga11/glass-tv-home/blob/feature/settings-reorganization-standby-gallery/launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java)
- [ProfileManager — avatar audit candidate](https://github.com/Frangga11/glass-tv-home/blob/fix/avatar-photo-upload-audit/launcher/app/src/main/java/com/gathin/tvhome/ProfileManager.java)
- [ModernSettingsDialog — avatar audit candidate](https://github.com/Frangga11/glass-tv-home/blob/fix/avatar-photo-upload-audit/launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java)
- [MainActivity — avatar audit candidate](https://github.com/Frangga11/glass-tv-home/blob/fix/avatar-photo-upload-audit/launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java)
- [UserSwitchDialog — avatar audit candidate](https://github.com/Frangga11/glass-tv-home/blob/fix/avatar-photo-upload-audit/launcher/app/src/main/java/com/gathin/tvhome/UserSwitchDialog.java)
- [Build configuration](https://github.com/Frangga11/glass-tv-home/blob/feature/settings-reorganization-standby-gallery/launcher/app/build.gradle)

## 7. Next inventory passes

- Compare branch ancestry and commit diffs to establish which branch contains the latest approved baseline.
- Audit default wallpaper resource, fallback, and image crop behavior.
- Audit icon animation classes, durations, focus scaling, and reduce-motion/performance behavior.
- Map all information widgets and their exact on-screen order.
- Audit weather/AQI source, location fallback, cache, and refresh policy.
- Audit prayer-time zone and date/time calculations for Pagar Alam.
- Inspect wallpaper upload server for authentication, upload validation, port failover, and lifecycle cleanup.
- Inspect Android manifest and ADB installation scripts for safe rollback.
- Verify tests and CI runs, then build and test on the actual Mi TV Stick.
