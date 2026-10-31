# Glass TV Home — Project Documentation

**Documentation set:** Project Master  
**Initial release:** v0.1-draft  
**Prepared:** 31 October 2026  
**Repository:** https://github.com/Frangga11/glass-tv-home  
**Documentation branch:** `docs/project-master-inventory`  
**Working-code branch under review:** `feature/settings-reorganization-standby-gallery`  
**Current avatar audit branch:** `fix/avatar-qr-upload-single-photo`
**Avatar review PR:** https://github.com/Frangga11/glass-tv-home/pull/6

## Purpose

This directory is the durable project record for Glass TV Home. GitHub is the canonical, version-controlled source. A matching export is to be kept in ChatGPT Library as a backup.

## Documents

| File | Purpose |
|---|---|
| [MASTER_PROJECT.md](MASTER_PROJECT.md) | Goals, device baseline, agreed user requirements, product scope |
| [INVENTORY.md](INVENTORY.md) | Current feature/code inventory and evidence-based status |
| [DECISION_LOG.md](DECISION_LOG.md) | Approved decisions and unresolved choices |
| [AUDIT_REGISTER.md](AUDIT_REGISTER.md) | Defects, risks, verification steps, and closure evidence |
| [AVATAR_AUDIT.md](AVATAR_AUDIT.md) | Dedicated QR avatar audit, implementation record, and device acceptance checklist |
| [DOCUMENTATION_WORKFLOW.md](DOCUMENTATION_WORKFLOW.md) | Mandatory three-layer documentation standard and efficient update rules |
| [WALLPAPER_AUDIT.md](WALLPAPER_AUDIT.md) | Wallpaper/background audit, source findings, security issue, and acceptance criteria |
| [HANDOVER.md](HANDOVER.md) | Current work position and how to resume in a new conversation |

## Documentation architecture

The project uses three connected layers: **MASTER** (project memory and navigation), **AUDIT & SOP** (feature evidence and repeatable procedures), and **OPERATIONAL LOG** (concise change history). GitHub remains canonical; ChatGPT Library is the milestone backup. Follow DOCUMENTATION_WORKFLOW.md. Do not duplicate content across layers.

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

The settings/gallery branch remains the rollback baseline. Avatar QR replacement is being reviewed in `fix/avatar-qr-upload-single-photo` through draft PR #6. The implementation is not release-approved until CI and actual Mi TV Stick acceptance pass. See AVATAR_AUDIT.md, INVENTORY.md, and AUDIT_REGISTER.md.
