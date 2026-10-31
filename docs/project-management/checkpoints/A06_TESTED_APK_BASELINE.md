# A06 — Tested APK baseline checkpoint

**Roadmap:** GTVH-BRR-001 v1.0  
**Checkpoint created:** 1 November 2026  
**Status:** SOURCE + APK IDENTITY LOCKED; ROLLBACK PROCEDURE DOCUMENTED; LIVE DEVICE SNAPSHOT NOT YET CAPTURED  
**Checkpoint branch:** `checkpoint/a06-tested-apk-pr9-7e8392e1`

## 1. Official tested APK identity — accepted baseline

The user supplied `glass-tv-home-profile-avatar-test.apk` and confirmed it is the APK used for testing on the Mi TV Stick. That historical user test is accepted as the known-good tested baseline. It is not a B01/B02 test and is not being requested again.

| Field | Verified value |
|---|---|
| Uploaded APK | `glass-tv-home-profile-avatar-test.apk` |
| File size | 1,427,681 bytes |
| SHA-256 | `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315` |
| CI artifact ID | `13048951728` |
| CI artifact name | `glass-tv-home-pagar-alam-release` |
| CI artifact archive size | 1,340,733 bytes |
| CI archive SHA-256 | `85475980a94b35a1a356cd5b5156bae3b50e7b405bc76ca1c22cd1e9557d32d6` |
| CI run | #208 / `41218171447` |
| CI conclusion | success; both jobs passed |
| Source branch | `feature/profile-avatar-security-integration` |
| Exact source commit | `7e8392e1a76223b2c1aef314c10fa54d0b813fdc` |
| Pull request | Draft PR #9, open/unmerged |

The CI artifact ZIP was downloaded and extracted. Its `app-release.apk` is byte-for-byte identical to the uploaded tested APK. The archive ZIP is a separate object with its own size/hash above. CI run/artifact metadata maps the artifact to PR #9 and the exact source commit above.

Evidence:
- CI run: https://github.com/Frangga11/glass-tv-home/actions/runs/41218171447
- Artifact: https://github.com/Frangga11/glass-tv-home/actions/runs/41218171447/artifacts/13048951728
- PR #9: https://github.com/Frangga11/glass-tv-home/pull/9
- Source commit: https://github.com/Frangga11/glass-tv-home/commit/7e8392e1a76223b2c1aef314c10fa54d0b813fdc

## 2. Immutable source checkpoint

Branch `checkpoint/a06-tested-apk-pr9-7e8392e1` was created directly from exact source commit `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`. It is a preservation ref. Do not move it. Development must continue on designated feature branches. No PR was merged.

## 3. Device-state evidence and limitation

Historical Bugjaeger/ADB outputs previously shared by the user (31 October 2026):
- `pm list packages -d`: `com.mitv.tvhome` appeared in the disabled-package list.
- Enabled-package search: `com.google.android.tvlauncher` and `com.gathin.tvhome` appeared.
- `get-home-activities` returned `unknown`.
- `set-home-activity` returned `Success`.
- `am start` reported that the Glass TV Home launcher activity was already running.

These are accepted as historical device observations. They are not a fresh snapshot and do not identify the current HOME resolver or prove the installed APK bytes/hash. This is a bounded documentation gap only; it does not invalidate or reopen the user's historical APK test.

**No device mutation is authorized by this checkpoint.** Before any future change to package enabled states, HOME/default launcher, or installed APK, take a fresh read-only snapshot using the commands in section 5. Until then, the rollback steps must be treated as conditional and the actual current HOME owner as unknown.

## 4. Rollback procedure

The detailed procedure is in [A06_ROLLBACK_PROCEDURE.md](A06_ROLLBACK_PROCEDURE.md). Key safeguards:
- Preserve the tested APK file and verify its SHA-256 before reinstalling.
- Prefer restoring the stock Android TV HOME through the TV's system UI; do not guess an activity component.
- If the stock launcher package is disabled, re-enable it only as an intentional rollback action.
- Reinstall the tested baseline with `adb install -r` only after confirming package ID/signature compatibility; do not uninstall first.
- Do not factory-reset, clear app data, disable packages, or change HOME defaults during baseline capture.
- No PR merge, candidate APK install, or launcher-default change is authorized by this checkpoint.

## 5. Read-only snapshot required before any device mutation

When convenient, run this capture in Bugjaeger/ADB and retain the complete output:

```sh
getprop ro.product.model
getprop ro.build.version.release
getprop ro.build.version.sdk
getprop ro.build.fingerprint
pm path com.gathin.tvhome
dumpsys package com.gathin.tvhome
cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.HOME
pm list packages -d
pm list packages -e
settings get secure enabled_accessibility_services
settings get secure default_input_method
settings get global stay_on_while_plugged_in
```

This is read-only. It is requested to document present-day device state before a future device operation, not to repeat, validate, or challenge the user's previous test. No need to run it merely to reconfirm the APK baseline.

## 6. Completion decision

- A05 tested APK-to-source matching: **DONE**.
- A06 exact source ref and APK identity checkpoint: **DONE**.
- A06 rollback procedure: **DOCUMENTED**.
- A06 live device-state snapshot: **NOT CAPTURED — historical evidence retained; current HOME owner and installed APK path/hash remain unknown**.
- Overall A06: **IN PROGRESS / DEVICE-MUTATION GATE** until a fresh read-only snapshot is available. This is the only remaining A06 evidence gap; it does not block continued source-only work if the user elects to defer device operations.
