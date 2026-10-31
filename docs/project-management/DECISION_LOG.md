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
| O-001 | Should avatar upload use Android picker, phone QR transfer, or both? | Keep Android picker as first path; consider QR fallback if TV picker is unavailable | OPEN |
| O-002 | Should a new profile be able to upload its photo during profile creation? | Yes, ideally; can also allow editing immediately after creation | OPEN |
| O-003 | Should custom avatars appear everywhere (top bar, settings card, switcher, add/edit dialogs)? | Yes, consistent identity across all profile surfaces | PROPOSED |
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
