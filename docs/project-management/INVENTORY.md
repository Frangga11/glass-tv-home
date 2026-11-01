# 02 — Feature and Code Inventory

**Version:** 0.2-draft  
**Date:** 1 November 2026  
**Review status:** A04 repository/branch/source inventory PASS; A05 tested-APK identity PASS; A06 baseline/rollback/device snapshot DONE; B01/B02/B03 renewed source/CI QC PASS; B04/B05 source audits IN PROGRESS; physical device acceptance deferred

## 1. Repository and branches

Repository: https://github.com/Frangga11/glass-tv-home

Branches verified from GitHub on 1 November 2026 (branch head SHA abbreviated):
- `docs/project-master-inventory` — latest documentation branch (documentation)
- `feature/b03-dialog-opacity-layering` — `56a4e199` (B03; Draft PR #12)
- `checkpoint/a06-tested-apk-pr9-7e8392e1` — `5713dc9d` (immutable tested-baseline checkpoint)
- `feature/b01-global-navigation-retention` — `bf899c7c` (B01; Draft PR #10)
- `feature/b02-remote-focus-indicators` — `01cbef2c` (B02; Draft PR #11)
- `feature/independent-visual-effects-id` — `888471fe`
- `feature/profile-avatar-security-integration` — `7e8392e1` (PR #9)
- `feature/profile-security-integration` — `1ba744ef` (PR #8)
- `feature/settings-reorganization-standby-gallery` — `06f4fa2a`
- `fix/avatar-photo-upload-audit` — `29a5a8bb`
- `fix/avatar-qr-upload-single-photo` — `0cb1b96e` (PR #6 source)
- `fix/power-off-accessibility` — `ed320d03`
- `fix/wallpaper-qr-session-security` — `62b1e7ce` (PR #7)
- `indonesia-pagar-alam` — `e8e4d0a7`
- `main` — `fec70ffa`
- `checkpoint` branches: none present at audit time.

Repository identity: `Frangga11/glass-tv-home` is a public fork of `aqmangge/glass-tv-home`; default branch is `main`. Do not confuse the fork's `main` with the feature-branch chain used by the open draft PRs.

Current dependency chain:
- PR #8 `feature/profile-security-integration` → PR #9 `feature/profile-avatar-security-integration`
- PR #9 → PR #10 `feature/b01-global-navigation-retention`
- PR #10 → PR #11
- PR #11 → PR #12 `feature/b02-remote-focus-indicators`

PRs #8–#12 remain open Draft/unmerged at this checkpoint. Keep each feature isolated; do not merge or retarget branches as part of A04.

Remote working state: branch refs and PR metadata were read from GitHub. No local clone/worktree was opened or modified in this audit session; therefore no claim is made about uncommitted files on the user's own computer.

**Important:** branches are separate snapshots. A feature found on one branch must not be attributed to another branch until merged or cherry-picked.

## 2. Build configuration — source-confirmed

Files: `launcher/settings.gradle`, `launcher/build.gradle`, `launcher/app/build.gradle`

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
| Default wallpaper | `launcher/app/src/main/assets/kabut-pagi-dempo.jpg` is present in the PR #9/B01/B02 source chain; Git blob `758f4225581f8c01e62d26c8d73c9aec43adb0df`; source image 230,883 bytes | SOURCE CONFIRMED; CI/APK packaging previously inspected on PR #7; fresh-install/device verification remains open — see WALLPAPER_AUDIT.md |
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
- Uploaded/extracted APK: 1,427,681 bytes; SHA-256 `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`. Direct extraction from CI artifact `13048951728` produced a byte-for-byte identical APK. The CI artifact ZIP itself is 1,340,733 bytes; its SHA-256 is `85475980a94b35a1a356cd5b5156bae3b50e7b405bc76ca1c22cd1e9557d32d6`.
- No PR merged and no new candidate APK installed during the current repair session. The uploaded APK was user-confirmed as previously tested on the Mi TV Stick; further device acceptance for repaired modules remains deferred.


## B01 global navigation implementation checkpoint — 1 November 2026 (second-pass audit)

| Area | Source finding | Remediation | Evidence / status |
|---|---|---|---|
| Home app-grid focus | Focus identity/slot were process-only and transient RecyclerView rebind events could overwrite saved preferences | Persist stable component key + fallback slot; snapshot, resolve against the rebuilt adapter, and restore the complete persisted snapshot | Draft PR #10; exact-head CI #225 PASS |
| Top-bar focus | Resource-ID persistence was brittle; returning from external Android screens could restore the wrong destination | Persist semantic keys and resolve the current view; preserve destination across internal reloads | Draft PR #10; CI #225 PASS |
| Settings tab | Dialog always initialized to Profile; tab IDs duplicated between persistence and dialog code | Persist tab/sidebar cursor and centralize canonical tab IDs in NavigationState | Draft PR #10; CI #225 PASS |
| Empty app list | Rebuild could retain an invalid -1 slot; pending scroll listener could exit without focus | Normalize empty-list slot, clear stale component key, and use Wi-Fi then Settings fallback | Draft PR #10; CI #225 PASS |
| Scroll-settle focus | Failed/missing target could leave focus unset or land on an unrelated card | Validate target visibility/focusability; retry requested slot and safely fall back | Draft PR #10; CI #225 PASS |
| Settings right-pane action return | Content focus after an action/re-render is a lifecycle restoration concern | Track under B07 | Deferred; not a B01 blocker |

- Branch: `feature/b01-global-navigation-retention`
- Draft PR #10: https://github.com/Frangga11/glass-tv-home/pull/10
- Current source head: `bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5`
- Exact-head CI #225: https://github.com/Frangga11/glass-tv-home/actions/runs/41229763017 — both jobs PASS; unit tests and release build succeeded; APK size 1,395 KB, below 1.5 MiB.
- Final TV-test APK publication step was skipped as required; CI run has no published artifact.
- CI workflow now requires manual workflow-dispatch opt-in to publish the final TV-test APK. Normal PR runs do not publish it.
- No APK has been installed on the TV. Physical device acceptance is deferred until all A–E audits/fixes and F integration/regression are complete.


## 8. A04 repository and source-tree audit — 1 November 2026

### Repository snapshot

- Audited commit: B02 head `01cbef2c2fc90498295971b22c369a72cdfa734a`.
- Baseline integration commit: PR #9 head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`.
- PR #10 B01 head: `bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5`.
- GitHub recursive tree at B02 head: 647 entries (496 files, 151 directories); tree response was not truncated.
- PR #9 baseline tree: 644 entries (493 files, 151 directories).
- B02 source tree contains 66 Java files under the app package, 14 JVM test files across app/installer, 159 XML files, and one packaged Default wallpaper JPEG.
- Principal roots: `.github`, `docs`, `examples`, `launcher`, `release`, `tests`, `themes`, `tools`, and `webos`.

### Android build and app configuration

- Gradle project root: `launcher/` (not repository root); modules `:app` and `:installer`.
- Android Gradle Plugin: 8.5.2.
- App ID/namespace: `com.gathin.tvhome`; versionName 4.0.0; versionCode 40.
- minSdk 26; compileSdk 34; targetSdk 34; Java 17.
- Release build enables R8 minification and resource shrinking; release signing is configured with the debug signing config for sideloading.
- CI workflow runs JVM release unit tests, assembles release, enforces APK size below 1.5 MiB, and runs lists/tools/docs tests. Publishing the TV-test APK is manual workflow-dispatch opt-in only.
- Manifest declares Android TV/Leanback support as optional, the HOME and LEANBACK_LAUNCHER entry points, app permissions, activities, receivers, and services. Device permissions and actual HOME ownership must be verified read-only before final acceptance.

### Important source ownership map

- `MainActivity.java`: home UI, app grid, top bar, widgets, profile/wallpaper/standby entry points.
- `ModernSettingsDialog.java`: settings tabs and settings workflows.
- `NavigationState.java`: persisted navigation/focus identifiers introduced in B01.
- `TvFocusIndicator.java`: shared focus indicator helper introduced in B02.
- `ProfileManager.java` / `UserSwitchDialog.java`: profile model, avatar resolver and profile switching.
- `AvatarUploadServer.java`: profile avatar QR server.
- `WallpaperUploadServer.java`: wallpaper/StandBy QR HTTP server.
- `StandbyPhotoStore.java`: isolated StandBy photo persistence.
- `WeatherActivity.java`, `WeatherAirQuality.java`, `PrayerTimes.java`, `AzanActivity.java`, `LocationActivity.java`: weather/AQI, prayer, azan, and location modules.

### A04 disposition

- Repository identity, remote branch graph, open-PR dependency chain, source tree, Gradle configuration, manifest, CI workflow, and principal source ownership have been inspected and recorded.
- No local worktree was created in this remote-only session; no local changes were made. A user's separate local clone, if any, is outside the available evidence.
- A04 remote repository/source audit: PASS. A05 and A06 remain separate mandatory gates; this does not authorize APK installation, device testing, PR merge, or release.


## 9. Tested APK identity and A06 checkpoint — 1 November 2026

- User-confirmed tested file: `glass-tv-home-profile-avatar-test.apk`.
- Size: 1,427,681 bytes.
- SHA-256: `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`.
- Downloaded CI artifact `13048951728` from run #208 (`41218171447`), extracted `app-release.apk`, and compared it directly with the uploaded APK: both are 1,427,681 bytes and SHA-256-identical (`f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`). CI archive ZIP is 1,340,733 bytes with SHA-256 `85475980a94b35a1a356cd5b5156bae3b50e7b405bc76ca1c22cd1e9557d32d6`. Workflow metadata binds run #208 to branch `feature/profile-avatar-security-integration` and exact head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`; PR #9 is that exact head.
- Exact source commit: `7e8392e1a76223b2c1aef314c10fa54d0b813fdc` on `feature/profile-avatar-security-integration`; PR #9 remains Draft/open/unmerged.
- A05: PASS. APK identity is matched by exact SHA-256 and byte size to the CI artifact evidence.
- A06: DONE. Immutable source checkpoint branch `checkpoint/a06-tested-apk-pr9-7e8392e1` was created directly from the exact tested commit. The [A06 baseline manifest](https://github.com/Frangga11/glass-tv-home/blob/checkpoint/a06-tested-apk-pr9-7e8392e1/docs/project-management/checkpoints/A06_TESTED_APK_BASELINE.md) and [rollback SOP](https://github.com/Frangga11/glass-tv-home/blob/checkpoint/a06-tested-apk-pr9-7e8392e1/docs/project-management/checkpoints/A06_ROLLBACK_PROCEDURE.md) are verified on that checkpoint branch.
- A06 read-only device-state snapshot was captured 1 November 2026: MiTV-AYFR0 / Android 11 / API 30; package metadata, enabled/disabled packages, HOME resolver, accessibility, input method, and power setting recorded. HOME resolver/preferred-activity mismatch and unverified installed APK hash remain E01 carry-forwards; no device state was changed.

## B01–B03 renewed source QC — 1 November 2026

- B01 PR #10 exact head bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5: source review PASS; exact-head CI #230 PASS; APK 1,395 KB; no TV-test artifact.
- B02 PR #11 exact head 01cbef2c2fc90498295971b22c369a72cdfa734a: source review PASS; exact-head CI #234 PASS; APK 1,396 KB; no TV-test artifact.
- B03 PR #12 exact head 56a4e1995aa8b8600614e6b5c4dca69d01c530c3: source review PASS; exact-head CI #239 PASS; APK 1,398 KB; no TV-test artifact.
- All three PRs remain Draft/open/unmerged. Physical acceptance is deferred to F02; no candidate APK installed and no TV settings changed.

## B04/B05 source inventory checkpoint

- B04: home grid is dynamically sized; Settings and B03 dialogs are viewport bounded; Weather uses a ScrollView. Recent Apps cards remain fixed 420×250dp with 64dp side padding; Nightstand clock remains 110sp. Target wm size/density are needed before deciding on adjustment.
- B05: AppLanguage provides app-level ID/EN selection, but Android string resources mix Indonesian/English and there are many hardcoded UI messages. First scan flagged 187 candidate strings across 12 high-traffic Java files. Full localization inventory remains open.


## B05 implementation inventory — 1 November 2026

- Working branch `feature/b05-id-en-localization`; Draft PR #14; current audited source head `cce889f231aeff3a28805fd517853a3f0a91e79c`; base is B03 exact head `56a4e1995aa8b8600614e6b5c4dca69d01c530c3`.
- Added locale resource sets `values-id/strings.xml` and `values-en/strings.xml`; default/ID/EN sets each contain 158 keys with no static key or placeholder mismatch.
- Added `LocaleContext.java`; locale context applied to MainActivity, WeatherActivity, RecentAppsActivity, LocationActivity, AzanActivity, and ClockDream. AppLanguage normalization and Android 13+ per-app locale synchronization added.
- Localized static strings in eight launcher layouts and programmatic profile/PIN/weather-condition/update flows. Added normalization and resource-parity unit tests.
- Remaining: direct Java UI strings in MainActivity, ModernSettingsDialog, WeatherActivity dialogs, and other dynamic/error/notification paths. B05 remains IN PROGRESS.
- Exact-head CI not yet verified; no B05 Actions run was present in the latest run query. No APK artifact, installation, device setting change, or physical acceptance.


## B04/B05 integrated source checkpoint — 1 November 2026

- B04 PR #13 source head `baf0e7dad2dfa28a9f79c7bb25a58502243f04ae` passed exact-head CI #242. The seven-file B04 scope includes responsive Recent Apps card sizing, proportional overlap, Close All slot sizing, Nightstand responsive bounds, and JVM sizing tests. Physical visual acceptance remains F02.
- B05 PR #14 source head `04cf544ca240e32a87baef525f266f3d038ff668` remains without exact-head CI evidence.
- Reconciliation branch `integration/b04-b05-reconciled`, current head `fd8c14630254e27d42e257be342a5324bd40522c`; Draft PR #15 targets `feature/b04-responsive-layout`. Commit `73817ce41e4ae5b034922f190052a95ac45886d9` is the two-parent integration of exact B04 and B05 heads; subsequent localization-remediation commits extend that integration branch.
- Four overlapping files were selectively combined. B04-only RecentAppsAdapter, TvResponsiveLayout, and TvResponsiveLayoutTest were retained. B05 locale resources, LocaleContext, language normalization, and tests were retained.
- Current default/ID/EN string sets contain 347 keys each. Static audit: no duplicates, same key sets, no printf-placeholder mismatches; no missing resource references in the three directly audited Java files.
- Additional localized feedback: StandBy mode, Adhan popup, Auto-Update status/actions, Now Playing fallbacks, playback state, wallpaper shuffle, next prayer suffix, Child Mode and visual-effect feedback, hide/lock app dialog labels, UV bands, precipitation summary, and visibility descriptions.
- Remaining B05 source debt: ModernSettingsDialog still contains user-facing Indonesian literals in profile, wallpaper, StandBy, device/system, OTA, and launcher guidance sections; perform a full app-wide Java/XML scan. Exact-head integrated CI has not reported; no local Gradle execution was available through the connected GitHub actions interface. API 30/API 33 runtime and physical acceptance remain pending.


### QC follow-up — 1 November 2026

- Latest integration head: `fd8c14630254e27d42e257be342a5324bd40522c` (Draft PR #15).
- Additional parental PIN fallback prompts are now localized. Static resource audit now reports 347 keys in each of default/ID/EN, with no duplicates, key-set differences, placeholder mismatches, or missing R.string references in the audited Java files.
- Exact-head Actions run is still absent. Do not infer build/test success from static checks.
- B05 remains IN PROGRESS: broader ModernSettingsDialog copy sweep, app-wide final scan, exact-head CI, API 30/API 33 lifecycle checks, and physical acceptance remain open.
