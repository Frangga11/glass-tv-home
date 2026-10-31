# 03 — Decision and Change Log

**Version:** 0.1-draft  
**Date:** 31 October 2026

## 1. Confirmed project decisions

| ID | Decision | Source | Status |
|---|---|---|---|
| D-001 | GitHub is the canonical source for project documentation; ChatGPT Library is the backup archive | User explicitly selected “both” | APPROVED |
| D-002 | Keep documentation modular; do not create one giant document | User's established documentation preference | APPROVED |
| D-003 | Keep profile avatar, home wallpaper, and standby/screen-saver photo collections as separate features and data stores | Product architecture / explicit clarification in current work | APPROVED |
| D-004 | Do not call a feature final solely because code is present; distinguish code inspection, CI/build, and real-device test | User's quality expectations and current audit protocol | APPROVED |
| D-005 | Maintain a separate documentation branch rather than mixing docs with application feature changes | Documentation workflow design | APPROVED FOR THIS DOCUMENTATION PASS |
| D-006 | Avatar photo upload must use a phone-scanned QR code; accept one image per session and delete the previous avatar only after the new file and profile reference are successfully committed | User explicitly requested QR upload and no retained old photo | APPROVED |
| D-007 | Project documentation uses three connected layers: Master, Audit/SOP, and Operational Log; one authoritative home per fact, minimal cross-links, GitHub canonical and ChatGPT Library milestone backup | User explicitly approved the efficient three-layer documentation approach on 31 October 2026 | APPROVED — STANDARD |

## 2. Baseline technical facts

- App ID: `com.gathin.tvhome`.
- Gradle version: 4.0.0 / versionCode 40.
- Android build configuration: minSdk 26, compileSdk 34, targetSdk 34, Java 17.
- User target: LG 32-inch display driven by Mi TV Stick, Android 11 / API 30 (must be revalidated before release).
- Main code branch under review: `feature/settings-reorganization-standby-gallery`.
- Avatar upload candidate: `fix/avatar-photo-upload-audit`.

## 3. Decisions still open

| ID | Question | Proposed direction | Status |
|---|---|---|---|
| O-001 | Avatar upload method | QR upload from phone on the same LAN; Android picker removed from active flow | DECIDED — QR ONLY |
| O-002 | Should a new profile be able to upload its photo during profile creation? | Create profile first, then select it and upload from Profile settings; inline creation upload can be considered later | DEFERRED |
| O-003 | Should custom avatars appear everywhere (top bar, settings card, switcher, add/edit dialogs)? | Use the same profile image resolver for top bar, settings card, and profile switcher; device verification remains | IMPLEMENTED IN AUDIT BRANCH; DEVICE TEST OPEN |
| O-004 | Exact final order and density of information tabs | Decide after screenshot / remote navigation review | OPEN |
| O-005 | Default wallpaper: fixed image or rotating selection | Await asset/behavior audit and user confirmation | OPEN |
| O-006 | Icon animation: always animated or only on focus / interaction | Prefer focus-driven, restrained animation pending performance test | OPEN |
| O-007 | Weather/AQI location and update policy | Confirm precise chosen location and source | OPEN |
| O-008 | Prayer time zone/source and azan behavior | Confirm regional zone and enabled audio policy | OPEN |

## 4. Change log

| Date | Branch | Change | Evidence |
|---|---|---|---|
| 2026-10-31 | `docs/project-master-inventory` | Created documentation branch from settings/gallery branch | GitHub branch created |
| 2026-10-31 | `docs/project-master-inventory` | Added documentation index, master specification, feature inventory, decision log, audit register, and handover | GitHub commits in documentation branch |
| 2026-10-31 | `fix/avatar-photo-upload-audit` | Found a candidate implementation for custom profile photo upload | Source inspection only; not accepted as final |
| 2026-10-31 | `fix/avatar-qr-upload-single-photo` | Added QR-only single-photo avatar upload, transactional replacement/reset, EXIF-aware rendering, and stale upload cleanup | Draft PR #6; CI/device acceptance pending |
| 2026-10-31 | `fix/wallpaper-qr-session-security` | Scoped wallpaper upload page, upload endpoint, gallery APIs, and thumbnails to a random QR session path; added JVM route tests | Draft PR #7; CI run 41200811301 in progress; device acceptance pending |

## 5. Change-record template

For each future code change, record:
- Date and author
- Requirement / issue ID
- Branch and base commit
- Files changed
- What changed and why
- Risk / compatibility impact
- Test command and result
- Device test and evidence
- Commit SHA
- Rollback method
