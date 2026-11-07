# G3C P1 Checkpoint — Security Policy & Authorization
**Date:** 7 November 2026  
**Status:** COMPLETE / SOURCE-HOST QC PASS  
**Branch:** `feature/g3c-profile-avatar-child-mode`  
**Exact HEAD:** `16577e0210470762423ed0bf6cc7624c33537850`  
**PR:** #68 — OPEN / DRAFT / UNMERGED; base `feature/g3b-weather-aqi-v4` @ `8cfc5c886950cc9d27d4530ba901df872b8e5c44`

## Scope and approved policy
During Child Mode, require the existing parent PIN before launcher-controlled navigation to:
- Android Settings main (`ACTION_SETTINGS`)
- Accessibility Settings
- Notification Access
- Default Home / Launcher selection (`ACTION_HOME_SETTINGS`, `ACTION_MANAGE_DEFAULT_APPS_SETTINGS`)

Explicit exemptions remain: Wi-Fi Settings, Display Settings, Screensaver/Dream Settings, and ordinary Quick Settings. Glass TV Home Profile Settings continues to use the previously approved sensitive-action matrix. Existing PIN mechanism is unchanged.

## Implementation
- Added `SystemSettingsAccess` as the central launcher-scoped authorization/launch path.
- Routed MainActivity “All Settings” and ModernSettingsDialog system-settings destinations through the central gate.
- Retained standard Android action on Xiaomi Settings fallback intents so policy evaluation remains action-aware.
- Successful PIN authorization is retained while firmware fallback intents are attempted; cancellation does not launch the protected page.
- Added action allowlist/exemption unit tests.

## Final source audit
- MainActivity Settings entrypoints: Android Settings main routes through `openSystemSettings`; Wi-Fi long-press and Wi-Fi popup routes remain exempt and unchanged.
- ModernSettingsDialog: Accessibility, Notification Access, Android Settings main and Home/Launcher picker routes all delegate to `SystemSettingsAccess.open`.
- Display and Dream/Screensaver routes remain exempt as approved; if their preferred action is unavailable and the fallback is Android Settings main, the fallback correctly triggers the PIN policy.
- No additional direct selected-page launch bypass was found in the audited source paths.
- Policy is launcher-scoped only. It does not protect Android Settings opened through notification shade, other apps, or external Android pathways.

## Verification evidence
- Exact HEAD: `16577e0210470762423ed0bf6cc7624c33537850`.
- GitHub Actions CI run `42338164003` (run #1307): SUCCESS.
- Required jobs: **Launcher build and unit tests — SUCCESS**; **Lists, tools and docs — SUCCESS**.
- Release unit tests, installer unit tests, Java compilation and release APK assembly passed; build log reported `BUILD SUCCESSFUL`.
- APK diagnostic: **1,601,221 bytes** (approximately 1,563 KiB); above historical 1.5 MiB reference, informational/non-blocking under D-023. No APK artifact published.
- PR #68 remains OPEN/DRAFT/UNMERGED. No review/merge action performed.

## Residual limitations and deferred acceptance
- Existing PIN implementation is deliberately unchanged; its existing default `0000` and plain-text SharedPreferences storage remain a known security limitation, not fixed by P1.
- No claim of system-wide Android parental control.
- Physical Android TV/remote/OEM firmware acceptance is DEFERRED / NOT RUN. No APK installation, ADB, device configuration or physical testing performed.
- No merge, rebase, cherry-pick, force-push or integration/main modification.

## Exit decision
**P1 SOURCE-HOST QC: PASS.** Approved policy, source routing, fallback handling, unit coverage and exact-head CI are verified. P1 is closed for source/host scope; deferred physical-device acceptance remains a separate overall project gate.

## Next
Proceed to **G3C P2 — Profile Selector & Navigation**. Start with a read-only audit of the current G3C branch against approved G1 V6 V03 and G2 focus/dialog foundations. Map current behavior, gaps, profile count/state, focus-vs-active distinction and Back/navigation behavior before coding. No implementation until the P2 audit and scope are reviewed; no merge or device testing.
