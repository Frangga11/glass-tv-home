# 03 — Decision Log

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
| O-003 | Should custom avatars appear everywhere (top bar, settings card, switcher, add/edit dialogs)? | Use one profile image resolver across top bar, Settings sidebar/list, and profile switcher; add-profile starts with built-in avatar and can upload after creation | IMPLEMENTED IN PR #9; CI #208 PASS; DEVICE TEST OPEN |
| O-004 | Exact final order and density of information tabs | Decide after screenshot / remote navigation review | OPEN |
| O-005 | Default wallpaper: fixed image or rotating selection | Await asset/behavior audit and user confirmation | OPEN |
| O-006 | Icon animation: always animated or only on focus / interaction | Prefer focus-driven, restrained animation pending performance test | OPEN |
| O-007 | Weather/AQI location and update policy | Confirm precise chosen location and source | OPEN |
| O-008 | Prayer time zone/source and azan behavior | Confirm regional zone and enabled audio policy | OPEN |

## 4. Operational history

Chronological implementation and verification events are recorded once in [OPERATIONS_LOG.md](OPERATIONS_LOG.md). Detailed findings remain in the corresponding audit file. Do not duplicate operational history here.


## 5. Profile security and avatar integration decisions — 31 October 2026

- Parent PIN remains device-wide; Child Mode, locked apps, hidden apps, and avatar photo path are profile-scoped.
- Switching away from any active Child Mode profile requires parent PIN, including child-to-child. Adult-to-child and adult-to-adult switching do not require PIN.
- Parent PIN is required before editing locked/hidden app lists and before profile deletion; deletion retains a separate destructive confirmation.
- User explicitly keeps default PIN `0000` temporarily and will change it later. Do not force a first-run PIN change in this scope.
- User explicitly requires unlimited wrong PIN attempts; no cooldown, lockout, or retry limit.
- Avatar QR session: selected LAN IPv4, random token, 5-minute expiry, one successful upload, 15 MB maximum, 80 MP/12,000 px image bounds, app-private storage.
- Draft PR #8 → Draft PR #9 is the intended review/merge order after actual-device acceptance. Do not merge source PR #6/#7 directly.
