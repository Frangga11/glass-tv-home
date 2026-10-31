# A06 — Tested APK baseline checkpoint

**Roadmap:** GTVH-BRR-001 v1.0  
**Checkpoint created:** 1 November 2026  
**Status:** A06 COMPLETE — SOURCE/APK BASELINE LOCKED; DEVICE SNAPSHOT CAPTURED; ROLLBACK SOP DOCUMENTED  
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

## 3. Device-state snapshot — captured 1 November 2026

The user supplied the following read-only Bugjaeger/ADB output from the target device.

### Device identity

| Field | Captured value |
|---|---|
| Model | `MiTV-AYFR0` |
| Android release | 11 |
| SDK | 30 |
| Build fingerprint | `Xiaomi/soul/soul:11/RTT0.211222.001/1546:user/release-keys` |

### Installed Glass TV Home package

| Field | Captured value |
|---|---|
| Package | `com.gathin.tvhome` |
| APK path | `/data/app/~~816nBIzOXnBrEs1qYP1s1w==/com.gathin.tvhome-PKQ3gE07n8Ebj8ILlNzjxg==/base.apk` |
| Version name / code | `4.0.0` / `40` |
| minSdk / targetSdk | 26 / 34 |
| Install / update time | 2026-11-01 00:38:53 / 2026-11-01 00:38:49 |
| User 0 state | installed=true; hidden=false; suspended=false; stopped=false; enabled=0 (default enabled) |
| Signing info shown by dumpsys | APK signing v2; signature digest prefix `9458f7bd` |

The installed package metadata matches the baseline's recorded version name/code, but the installed `base.apk` was not pulled and hashed. Therefore the live installed APK has **not** been proven byte-for-byte identical to the accepted baseline APK. This does not change the user's accepted historical test or the independently verified uploaded baseline APK identity.

### HOME resolver and package state

- `cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.HOME` returned:
  `com.google.android.tvlauncher/.MainActivity`.
- In the same `dumpsys package com.gathin.tvhome` output, the Preferred Activities section lists `com.gathin.tvhome/.MainActivity` as preferred/always, selected from the Google TV launcher, Glass TV Home, and FallbackHome candidates.
- These two outputs conflict. Record the direct resolver result as the current command's resolved HOME activity, but do **not** claim the preference table is reconciled or infer the full user-visible HOME behavior from one command alone. This discrepancy is carried into E01 (device settings / launcher audit) for source and read-only device investigation before any launcher-default change.
- `pm list packages -e` includes both `com.google.android.tvlauncher` and `com.gathin.tvhome`.
- `pm list packages -d` does not list either of those packages. It lists `com.mitv.tvhome.atv` (among other packages) as disabled. This is a different package name from the historical `com.mitv.tvhome` observation; do not conflate them.
- Accessibility service: `com.google.android.marvin.talkback/com.android.switchaccess.SwitchAccessService`.
- Default input method: `com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME`.
- `stay_on_while_plugged_in=0`.

### Snapshot interpretation / residual risks

1. Device identity is now captured and matches the previously recorded Android 11 / API 30 target.
2. Both stock Google TV launcher and Glass TV Home packages are enabled. The stock launcher is returned by the HOME resolver, while the package's preferred-activity table records Glass TV Home. Treat this as a resolver/preferred-activity discrepancy, not as permission to change anything.
3. Current installed package metadata is version 4.0.0/code 40, but its APK bytes/hash have not been extracted from the TV.
4. Historical observation that `com.mitv.tvhome` was disabled differs from the fresh list, which reports `com.mitv.tvhome.atv` disabled. Package IDs must remain exact; no package action is authorized.
5. No device setting or package state was changed during this capture.

## 4. Rollback procedure

The detailed procedure is in [A06_ROLLBACK_PROCEDURE.md](A06_ROLLBACK_PROCEDURE.md). Key safeguards:
- Preserve the tested APK file and verify its SHA-256 before reinstalling.
- Prefer restoring the stock Android TV HOME through the TV's system UI; do not guess an activity component.
- If the stock launcher package is disabled, re-enable it only as an intentional rollback action.
- Reinstall the tested baseline with `adb install -r` only after confirming package ID/signature compatibility; do not uninstall first.
- Do not factory-reset, clear app data, disable packages, or change HOME defaults during baseline capture.
- No PR merge, candidate APK install, or launcher-default change is authorized by this checkpoint.

## 5. Snapshot provenance and future read-only checks

The complete read-only command set was run by the user and the output was received on 1 November 2026. No additional capture is required to close A06. If E01 needs to resolve the HOME/preferred-activity discrepancy, use a narrowly scoped read-only follow-up such as:

```sh
cmd package query-activities --brief -a android.intent.action.MAIN -c android.intent.category.HOME
cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.HOME
dumpsys package preferred-activities
```

Do not use `set-home-activity`, `pm enable`, `pm disable`, uninstall, clear-data, or install commands during that investigation.

## 6. Completion decision

- A05 tested APK-to-source matching: **DONE**.
- A06 exact source ref and APK identity checkpoint: **DONE**.
- A06 rollback procedure: **DOCUMENTED**.
- A06 live device-state snapshot: **CAPTURED 1 November 2026**; model/build, installed package path/metadata, HOME resolver, enabled/disabled packages, accessibility, input method, and power setting recorded.
- Residual E01 findings: HOME resolver returns stock launcher while preferred-activity table lists Glass TV Home; installed APK bytes/hash have not been extracted from the TV. Both are explicitly recorded and must not be silently inferred.
- Overall A06: **DONE** — tested APK/source checkpoint, rollback SOP, and requested read-only device snapshot are documented. No device mutation was performed or authorized.
