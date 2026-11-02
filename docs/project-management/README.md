# Glass TV Home — Project Documentation

**Documentation set:** Project Master  
**Initial release:** v0.1-draft  
**Prepared:** 31 October 2026  
**Repository:** https://github.com/Frangga11/glass-tv-home  
**Documentation branch:** `docs/project-master-inventory`  
**Current feature branch:** `feature/e02-profile-avatar-lock-ui` (PR #35, Draft/open/unmerged)  
**Current stage:** E02 Profile — source/CI gate complete; physical acceptance remains in Project F.
**E02 review PR:** https://github.com/Frangga11/glass-tv-home/pull/35

## Purpose

This directory is the durable project record for Glass TV Home. GitHub is the canonical, version-controlled source. A matching export is to be kept in ChatGPT Library as a backup.

## Documents

| File | Purpose |
|---|---|
| [MASTER_PROJECT.md](MASTER_PROJECT.md) | Goals, device baseline, agreed user requirements, product scope |
| [INVENTORY.md](INVENTORY.md) | Current feature/code inventory and evidence-based status |
| [DECISION_LOG.md](DECISION_LOG.md) | Approved decisions and unresolved choices |
| [OPERATIONS_LOG.md](OPERATIONS_LOG.md) | Concise chronological change history (third documentation layer) |
| [AUDIT_REGISTER.md](AUDIT_REGISTER.md) | Defects, risks, verification steps, and closure evidence |
| [AVATAR_AUDIT.md](AVATAR_AUDIT.md) | Original QR avatar audit, implementation record, and device acceptance checklist |
| [E02_PROFILE_AVATAR_LOCK_UI_AUDIT.md](E02_PROFILE_AVATAR_LOCK_UI_AUDIT.md) | E02 profile/avatar/app-lock source audit, corrections, exact-head CI and F-stage acceptance |
| [E05_WIDGETS_INFORMATION_AUDIT.md](E05_WIDGETS_INFORMATION_AUDIT.md) | E05 widget/information center audit, ayah no-truncation repair, and F-stage acceptance |
| [DOCUMENTATION_WORKFLOW.md](DOCUMENTATION_WORKFLOW.md) | Mandatory three-layer documentation standard and efficient update rules |
| [WALLPAPER_AUDIT.md](WALLPAPER_AUDIT.md) | Wallpaper/background audit, source findings, security issue, and acceptance criteria |
| [E01_DEVICE_SETTINGS_HOME_CONTROL_AUDIT.md](E01_DEVICE_SETTINGS_HOME_CONTROL_AUDIT.md) | E01 Device Settings source audit and exact-head verification |
| [B06_FEEDBACK_AUDIT.md](B06_FEEDBACK_AUDIT.md) | B06 notification, Toast, OTA loading, QR progress/timeout audit and exact-head CI evidence |
| [HANDOVER.md](HANDOVER.md) | Current work position and how to resume in a new conversation |

## Documentation architecture

The project uses three connected layers: **MASTER** (`MASTER_PROJECT.md`, `INVENTORY.md`, `DECISION_LOG.md`, `HANDOVER.md`), **AUDIT & SOP** (`AUDIT_REGISTER.md` plus focused feature audits/SOPs), and **OPERATIONAL LOG** (`OPERATIONS_LOG.md`). GitHub remains canonical; ChatGPT Library is the milestone backup. Follow DOCUMENTATION_WORKFLOW.md. Do not duplicate content across layers.

## Evidence and status rules

- **CONFIRMED-CODE:** verified by inspecting the named source file/branch.
- **CONFIRMED-USER:** explicitly supplied or agreed by the user.
- **CI-VERIFIED:** a GitHub Actions run or artifact proves the specified build/test passed.
- **DEVICE-VERIFIED:** tested on the actual target TV/streaming device with recorded evidence.
- **PLANNED:** requested or agreed, but not yet implemented.
- **PARTIAL:** implementation exists, but coverage or verification is incomplete.
- **UNVERIFIED:** insufficient evidence.
- **BLOCKED:** a known dependency prevents completion.

A feature is not called complete merely because its code exists. Build success is not the same as real-device verification.

## Update protocol

1. Record each new requirement or decision in the relevant document.
2. Record code changes with branch, commit SHA, files changed, and reason.
3. Update the audit register with reproducible checks and evidence.
4. At the end of every work session, update HANDOVER.md.
5. Never erase unresolved items; close them only with evidence.
6. Keep this documentation branch separate from application feature branches until reviewed.

## Current baseline

The application source inspected for this draft reports package `com.gathin.tvhome`, versionName `4.0.0`, versionCode `40`, minSdk 26, targetSdk 34, compileSdk 34, and Java 17 compile options. The user's target setup recorded in project context is an LG 32-inch display using a Mi TV Stick running Android 11 / API 30. This hardware baseline still needs confirmation against a fresh device audit before release.

## Important branch distinction

E02 selectively carries the QR avatar implementation into the profile-security baseline and is tracked in Draft PR #35. PR #6 remains a historical isolated source branch and must not be merged independently. E02 source/CI gates pass; actual Mi TV Stick + phone/remote acceptance remains mandatory in Project F. See E02_PROFILE_AVATAR_LOCK_UI_AUDIT.md, AVATAR_AUDIT.md, INVENTORY.md, and AUDIT_REGISTER.md.
