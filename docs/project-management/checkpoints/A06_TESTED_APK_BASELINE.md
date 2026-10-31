# A06 — Tested APK baseline checkpoint

**Roadmap:** GTVH-BRR-001 v1.0  
**Checkpoint created:** 1 November 2026  
**Status:** SOURCE + APK IDENTITY LOCKED; DEVICE-STATE SNAPSHOT PENDING  
**Checkpoint branch:** `checkpoint/a06-tested-apk-pr9-7e8392e1`

## 1. Tested APK identity — user-confirmed

The user supplied `glass-tv-home-profile-avatar-test.apk` and confirmed it is the APK used for testing on the Mi TV Stick.

| Field | Verified value |
|---|---|
| Uploaded APK | `glass-tv-home-profile-avatar-test.apk` |
| File size | 1,427,681 bytes |
| SHA-256 | `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315` |
| CI artifact ID | `13048951728` |
| CI artifact name | `glass-tv-home-pagar-alam-release` |
| CI artifact archive size | 1,340,733 bytes |
| CI run | #208 / `41218171447` |
| CI conclusion | success; both jobs passed |
| CI published release APK | yes |
| Source branch | `feature/profile-avatar-security-integration` |
| Exact source commit | `7e8392e1a76223b2c1aef314c10fa54d0b813fdc` |
| Pull request | Draft PR #9, open/unmerged |

The GitHub CI artifact ZIP was downloaded and extracted. Its `app-release.apk` is byte-for-byte identical to the user-uploaded tested APK (same 1,427,681 bytes and SHA-256 above). The archive ZIP itself is a different object: 1,340,733 bytes with SHA-256 `85475980a94b35a1a356cd5b5156bae3b50e7b405bc76ca1c22cd1e9557d32d6`. CI job logs show the release APK build path and report 1,394 KB (integer KB display). Workflow metadata binds run #208 / `41218171447` and artifact ID `13048951728` to branch `feature/profile-avatar-security-integration` and exact head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`; PR #9 has the same head.

Evidence:
- CI run: https://github.com/Frangga11/glass-tv-home/actions/runs/41218171447
- PR #9: https://github.com/Frangga11/glass-tv-home/pull/9
- Source commit: https://github.com/Frangga11/glass-tv-home/commit/7e8392e1a76223b2c1aef314c10fa54d0b813fdc

## 2. Source checkpoint

This branch was created directly from exact source commit `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`. It is a preservation ref only; no source code was changed and no PR was merged.

Do not move this checkpoint branch. Any subsequent development must continue on its designated feature branch.

## 3. Device-state backup — still required

The source and tested APK identity are locked, but a complete A06 device-state snapshot has not yet been collected.

### Historical device evidence recovered from earlier chat — partial only

The user previously shared these Bugjaeger/ADB outcomes (31 October 2026):
- `pm list packages -d`: `com.mitv.tvhome` appeared in the disabled-package list.
- Enabled-package search: `com.google.android.tvlauncher` and `com.gathin.tvhome` appeared.
- `get-home-activities` returned `unknown`.
- `set-home-activity` returned `Success`.
- `am start` reported that the launcher activity was already running.

These are historical observations, not a fresh A06 snapshot. They do not establish which HOME activity is currently resolved, nor do they identify the installed APK bytes/hash. In particular, both the stock launcher and Glass TV Home appearing in the enabled-package results is not enough to determine the active HOME owner. Do not infer or change the current default from these outputs.

### Required fresh read-only capture

Before calling A06 fully complete, capture read-only state from the Mi TV Stick with Bugjaeger/ADB:

1. Android release, SDK, model and build fingerprint:
   `getprop ro.build.version.release`
   `getprop ro.build.version.sdk`
   `getprop ro.product.model`
   `getprop ro.build.fingerprint`
2. Installed Glass TV Home package metadata and APK path:
   `dumpsys package com.gathin.tvhome`
   `pm path com.gathin.tvhome`
3. Current HOME resolver:
   `cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.HOME`
4. Launcher package enabled state:
   `pm list packages -d`
   `pm list packages -e`
5. Relevant secure/global settings:
   `settings get secure enabled_accessibility_services`
   `settings get secure default_input_method`
   `settings get global stay_on_while_plugged_in`

Capture command output before changing launcher defaults, disabling packages, or installing a candidate build. Do not run destructive commands or alter device settings during this baseline collection.

If the TV currently has a different installed APK from the uploaded tested file, record that discrepancy explicitly; do not silently replace one identity with the other.

## 4. Rollback rule

- Keep the uploaded tested APK and its SHA-256 as the verified rollback installation package.
- Keep this branch as the exact source checkpoint.
- Before any future installation, verify the candidate APK package ID, version metadata, and SHA-256, and ensure the current launcher can be restored.
- No candidate APK installation or HOME/default-launcher change is authorized by this checkpoint alone.

## 5. Completion decision

- A05 tested APK-to-source matching: **DONE**.
- A06 exact source ref and APK identity checkpoint: **DONE**.
- A06 device-state snapshot and rollback-readiness evidence: **PENDING USER-PROVIDED ADB OUTPUT**.
- Overall A06: **NOT YET CLOSED** until the device-state evidence is captured and reviewed.
