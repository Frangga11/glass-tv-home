# 02 — Feature and Code Inventory

**Version:** 0.2-draft  
**Date:** 31 October 2026  
**Review status:** Source integration and exact-head CI #208 pass; physical device acceptance remains open

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
- `fix/avatar-qr-upload-single-photo` (source PR #6; selectively integrated in PR #9)
- `feature/profile-security-integration` (PR #8 base)
- `feature/profile-avatar-security-integration` (Draft PR #9; latest exact-head CI pass)

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
| Personal avatar photo | QR upload selectively integrated in PR #9 on PR #8 base; profile-scoped private file storage | SOURCE + CI #208 PASS; device verification pending |
| Child Mode | Per-profile flags, lock/hide sets, global parent PIN; PR #8 adds PIN gates and strict child-profile exit authorization | SOURCE + CI #197 PASS; device verification pending; see PROFILE_AUDIT.md |
| Top-bar avatar | Shared custom-photo resolver integrated in PR #9 and refreshed on profile/photo/Mode Anak changes | SOURCE + CI #208 PASS; device verification pending |
| Avatar in profile switcher | Uses same profile-scoped resolver and effective Child Mode ring as other surfaces | SOURCE + CI #208 PASS; device verification pending |
| Avatar in add-profile flow | Candidate add-profile dialog still only selects one of seven built-ins | PARTIAL |
| Wallpaper QR upload | Baseline bare-root endpoint replaced in draft PR #7 by random session-token-scoped page/upload/API/thumb routes | CODE IMPLEMENTED IN PR #7; CI PASS; device acceptance pending — see WALLPAPER_AUDIT.md |
| Standby gallery | MainActivity has a separate standby photo picker/store flow | CONFIRMED-CODE |
| Weather / AQI | Weather-related activity/overlay and weather-air-quality sync are present in MainActivity | CONFIRMED-CODE; data correctness and offline test open |
| Prayer times / Islamic shelf | PrayerTimes, Location, Azan and Islamic shelf are referenced in source | CONFIRMED-CODE; location/time accuracy test open |
| Default wallpaper | Code expects `assets/kabut-pagi-dempo.jpg`, but the CI release APK does not contain the asset; fresh-install Default has no bundled image | P1 BLOCKER — approved image received and package asset prepared; repository/APK packaging and device verification remain; see WALLPAPER_AUDIT.md |
| Icon animation | Spring / focus animations exist; exact per-icon animation policy needs audit | PARTIAL |
| Avatar visual report | Seven built-in avatars plus QR custom-photo flow; EXIF-aware downsampled rendering; shared surfaces | SOURCE + CI #208 PASS; real-device QC pending |
| Tab information layout | Tab names confirmed; final arrangement and screenshot acceptance not yet confirmed | PARTIAL |
| Indonesian localization | Indonesia / Pagar Alam localization branch and AppLanguage support are in repository history | CONFIRMED-CODE; full string audit open |
| OTA / update indicator | README / commit history describe background update checks and badge; current implementation needs dedicated audit | UNVERIFIED |
| QR upload separation | Wallpaper, Standby, and avatar QR routes/stores remain separate; avatar server binds to selected LAN IPv4 and is stopped on dialog close/expiry | SOURCE + CI #208 PASS; device/network acceptance pending |

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


## 5. Current profile/avatar integration — PR #8 + PR #9

- PR #8: `feature/profile-security-integration`; Draft; parent PIN gates and centralized child-profile exit policy. Exact-head CI #197 passed.
- PR #9: `feature/profile-avatar-security-integration`; Draft; base is PR #8 branch. Exact head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`; CI #208 passed both jobs.
- Tests: `ProfileSecurityPolicyTest`, `AvatarUploadServerPolicyTest`, `ProfileManagerAvatarPathTest`.
- APK artifact `13048951728`: 1,427,681 bytes; SHA-256 `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`.
- No PR merged and no APK installed. Actual Mi TV Stick/phone test is the remaining release gate.


## B01 global navigation implementation checkpoint — 1 November 2026 (second-pass audit)

| Area | Source finding | Remediation | Evidence / status |
|---|---|---|---|
| Home app-grid focus | Focus identity/slot were process-only and transient RecyclerView rebind events could overwrite saved preferences | Persist stable component key + fallback slot; snapshot, resolve against the rebuilt adapter, and restore the complete persisted snapshot | Draft PR #10; exact-head CI #224 passed previous code snapshot; current #225 pending |
| Top-bar focus | Resource-ID persistence was brittle; returning from external Android screens could restore the wrong destination | Persist semantic keys and resolve the current view; preserve destination across internal reloads | Draft PR #10; CI #225 pending |
| Settings tab | Dialog always initialized to Profile; tab IDs duplicated between persistence and dialog code | Persist tab/sidebar cursor and centralize canonical tab IDs in NavigationState | Draft PR #10; CI #225 pending |
| Empty app list | Rebuild could retain an invalid -1 slot; pending scroll listener could exit without focus | Normalize empty-list slot, clear stale component key, and use Wi-Fi then Settings fallback | Draft PR #10; CI #225 pending |
| Scroll-settle focus | Failed/missing target could leave focus unset or land on an unrelated card | Validate target visibility/focusability; retry requested slot and safely fall back | Draft PR #10; CI #225 pending |
| Settings right-pane action return | Content focus after an action/re-render is a lifecycle restoration concern | Track under B07 | Deferred; not a B01 blocker |

- Branch: `feature/b01-global-navigation-retention`
- Draft PR #10: https://github.com/Frangga11/glass-tv-home/pull/10
- Current source head: `bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5`
- CI #224 passed on preceding head `1613fef4d83102369a87178a881832a29295b8d7`; CI #225 is validating the current head.
- CI workflow now requires manual workflow-dispatch opt-in to publish the final TV-test APK. Normal PR runs do not publish it.
- No APK has been installed on the TV. Physical device acceptance is deferred until all A–E audits/fixes and F integration/regression are complete.
