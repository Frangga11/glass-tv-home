# 01 — Master Project Specification

**Version:** 1.2  
**Date:** 1 November 2026  
**Status:** Master map; not release approval

## 1. Project identity

- **Project:** Glass TV Home
- **Application ID / namespace:** `com.gathin.tvhome`
- **Primary repository:** https://github.com/Frangga11/glass-tv-home
- **Primary purpose:** replace the stock Android TV home experience with a clean, lightweight, visually polished launcher and practical home dashboard.
- **Current product baseline found in Gradle:** 4.0.0 (versionCode 40).
- **Current user-facing project language preference:** Indonesian, with multilingual support retained where implemented.

## 2. Target device baseline

| Item | Recorded baseline | Evidence / confidence |
|---|---|---|
| Display | LG 32-inch television | User project context |
| Android device | Mi TV Stick connected to the LG display | User project context |
| Android release | Android 11 | User project context; must re-check live device |
| SDK level | API 30 | User project context; must re-check live device |
| App minSdk | 26 | Gradle source inspected |
| compileSdk | 34 | Gradle source inspected |
| targetSdk | 34 | Gradle source inspected |
| Java compile options | Java 17 | Gradle source inspected |
| App ID | com.gathin.tvhome | Gradle source inspected |

**Compatibility note:** the launcher runs on the Android-based Mi TV Stick. It does not run natively on LG webOS. The LG panel is the display, not the Android execution environment.

## 3. User goals and product requirements captured so far

### Home and navigation
- TV-first interface navigable by remote / D-pad.
- Clean app grid and dock, with a moving glass focus/highlight effect.
- Clear top information area and top-bar controls.
- Responsive layout for supported TV resolutions and densities.
- Preserve direct access to the user's essential apps.

### Apps to preserve
The user's earlier keep-list includes:
- Vision+
- Vidio
- Disney+
- YouTube
- Netflix
- Prime Video
- WeTV
- Browser
- Google Play Store

The installed package names and current device state must be freshly inventoried before any debloat or package-disable action. Never disable a package based only on a display name.

### Profiles and avatar
- Multiple user profiles.
- Seven built-in animal avatars: Fox, Panda, Tiger, Lion, Frog, Koala, Octopus.
- Child profile / Child Mode controls and PIN protection.
- User requested ability to upload a personal photo for the profile avatar.
- Personal photo should persist locally after app restart and should be clearly separate from wallpaper and standby/screen-saver photo collections.
- Built-in avatar fallback and reset action must remain available.

### Appearance and visual effects
- Nature / landscape wallpaper direction, with user's own photos supported.
- Custom wallpaper upload and selection.
- Standby / screensaver photo collection managed separately from home wallpaper and profile avatar.
- Animated app icon / focus behavior and polished transitions.
- Default wallpaper must be documented, including exact asset, crop, and fallback behavior.
- User asked for an avatar report, default wallpaper report, icon animation report, weather behavior, and the layout of information tabs.

### Information and dashboard
- Clock and date.
- Weather and air-quality information.
- Prayer times: Subuh, Zuhur, Asar, Magrib, Isya.
- Location-aware data; Pagar Alam / South Sumatra context was previously discussed.
- Islamic information / ayah shelf and azan-related controls are present in the source tree; exact display order and user-approved final layout need screenshot-level confirmation.

### Device and settings
- Organized settings tabs for Profile, Home Screen, Appearance, Status & Weather, Effects, Standby, Child Mode, and Device Settings are present in the inspected settings implementation.
- TV resolution / UI scale information.
- Device settings shortcuts.
- Reliable behavior on Android TV 11 / API 30 is a key acceptance requirement.

## 4. Scope boundaries

In scope:
- Launcher UI, navigation, profiles, wallpapers, standby photos, weather/AQI, prayer-time display, settings, animations, Android TV compatibility, upload workflows, documentation, and release verification.

Out of scope unless explicitly approved:
- Replacing LG webOS.
- Rooting the TV or making irreversible firmware modifications.
- Disabling system packages without a reversible backup and package-by-package confirmation.
- Claiming device compatibility without a test on the actual Mi TV Stick.

## 5. Quality and release principles

1. Remote-first: every important action must be reachable and visibly focused using the TV remote.
2. Persistence: profile/avatar/wallpaper/settings must survive force-stop and reboot where intended.
3. Graceful fallback: missing images, offline weather, denied picker, or unavailable location must not crash the launcher.
4. Data separation: avatar photo, home wallpaper, and standby gallery are three different content types and stores.
5. Performance: control image dimensions and memory use; avoid loading original high-resolution photos repeatedly.
6. Safety: preserve a route back to the stock launcher and record every ADB setting changed.
7. Evidence: each acceptance criterion needs source, build, and/or device evidence.

## 6. Definition of done

The project is ready for user acceptance only when:
- All agreed requirements are mapped to implemented features.
- No P0/P1 audit defects remain open.
- Release build succeeds and artifact SHA/version are recorded.
- Install/upgrade/uninstall or rollback steps are documented.
- Actual Mi TV Stick testing covers remote navigation, persistence, cold start, network loss, image upload, and all key information modules.
- User reviews the final screenshots and explicitly accepts the result.

## 7. Current work and status — 31 October 2026

| Workstream | Current state | Next gate |
|---|---|---|
| Profile security + avatar QR | Selectively integrated in Draft PR #9 on PR #8 base; exact-head CI #208 passed; release APK produced | Physical Mi TV Stick/phone acceptance, then review merge order PR #8 → PR #9 |
| Wallpaper QR security | Token-scoped wallpaper page/upload/API/thumb routes implemented on `fix/wallpaper-qr-session-security`; PR #7 draft; CI run 41200811301 passed | Verify route rejection and dialog-close revocation on TV/phone |
| Default wallpaper | `assets/kabut-pagi-dempo.jpg` is present in the PR #9 CI release APK (230,883 bytes in the ZIP listing) | Verify cold-start display and offline fallback on target TV |
| Visual wallpaper QC | CENTER_CROP, 800 ms cross-fade, and Backdrop blur exist in source | Review composition/legibility and performance on target TV |

## 8. Open confirmations

- Confirm exact Mi TV Stick model / generation, RAM and storage.
- Confirm current installed Glass TV build and branch/commit before device acceptance.
- Verify the bundled `kabut-pagi-dempo.jpg` wallpaper on a clean install and cold start.
- Confirm final information-tab order using a screenshot / mockup review.
- Confirm icon animation style and which icons are animated.
- Confirm exact weather location and data source; AQI source and refresh cadence.
- Confirm prayer-time zone/source for Pagar Alam and whether azan audio is enabled.

## 9. Documentation navigation

- `DOCUMENTATION_WORKFLOW.md` — approved three-layer documentation standard.
- `INVENTORY.md` and `DECISION_LOG.md` — current feature map and approved/open decisions.
- `AUDIT_REGISTER.md` — central audit status.
- `BUG_REPAIR_ROADMAP.md` — approved A–G repair sequence and task-level callback register.
- `WALLPAPER_AUDIT.md` and `AVATAR_AUDIT.md` — detailed feature evidence.
- `OPERATIONS_LOG.md` — concise change history.
- `HANDOVER.md` — resume point.

The Master is the project map. Keep detailed test results in the relevant audit; do not duplicate them here.


## 9. Profile and avatar integration snapshot — 31 October 2026

- Draft PR #8 adds parent-PIN authorization for locked/hidden app policy edits and profile deletion, and requires PIN for every switch away from an active Child Mode profile, including child-to-child.
- Draft PR #9 selectively integrates QR avatar upload on top of PR #8. Exact head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`; CI #208 passed both jobs.
- Release APK: 1,427,681 bytes; SHA-256 `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`. Direct byte-for-byte match confirmed against `app-release.apk` extracted from CI #208 artifact `13048951728`; source branch/head: `feature/profile-avatar-security-integration` / `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`. The CI ZIP archive itself is 1,340,733 bytes (not the APK size).
- User-approved PIN decisions: keep `0000` temporarily; unlimited incorrect attempts; no cooldown/lockout.
- PR #8 and PR #9 remain Draft/unmerged. Physical Mi TV Stick + phone acceptance remains mandatory.


## 10. Approved post-test repair roadmap — 1 November 2026

- User formally approved [BUG_REPAIR_ROADMAP.md](BUG_REPAIR_ROADMAP.md), roadmap ID `GTVH-BRR-001`, version 1.0.
- The approved sequence is A: preparation/audit → B: global UI/navigation/localization foundations → C: communication/QR/upload infrastructure → D: state and persistence → E: specific feature modules → F: integration/regression testing → G: finalization/release.
- Completed: A01–A03 planning and A04 remote repository/branch/source-tree audit. A04 evidence and the explicit local-worktree limitation are recorded in `INVENTORY.md` and `AUDIT_REGISTER.md`.
- A05 is complete: the user-supplied tested APK SHA-256 matches the PR #9 CI #208 release APK record and exact source commit `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`.
- A06 is in progress: a preservation branch and APK/hash manifest are created at `checkpoint/a06-tested-apk-pr9-7e8392e1`; read-only Mi TV Stick state capture and rollback-readiness review remain.
- Track task-level statuses and evidence only in `BUG_REPAIR_ROADMAP.md`; use the audit register for defect details and HANDOVER for the resume point.
