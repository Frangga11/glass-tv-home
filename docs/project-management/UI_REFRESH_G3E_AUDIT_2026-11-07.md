# UI Refresh G3E — Wi-Fi Audit

**Document:** GTVH-UIR-G3E-AUDIT-001  
**Date:** 7 November 2026  
**Status:** COMPLETE — SOURCE-HOST QC PASS  
**Owner branch:** `feature/g3e-wifi`  
**Exact HEAD:** `89d044152b7d8460884466deb5baa47126f48027`  
**PR:** [#70 — G3E Wi-Fi V05](https://github.com/Frangga11/glass-tv-home/pull/70) — OPEN / DRAFT / UNMERGED  
**Base:** `feature/g3d-quick-settings` @ `bbc7b240d2ad7bfc9d22dbaf3437f03b5953bcf8`

## 1. Trigger and authoritative inputs

G3E follows G3D Quick Settings, which was already COMPLETE / SOURCE-HOST QC PASS. G3D was not reworked; its recorded owner branch, exact head, PR #69 and CI evidence were re-verified before G3E began.

Authoritative design and architecture inputs:
- G1 V6 prototype V05 — Wi-Fi popup: connected/status surface, nearby network list, scan status, horizontal Rescan and Android Wi-Fi Settings actions, Close; simulated permission, Location, offline, throttled, stale and empty states.
- G1/G2 shared contracts: `TvUiTokens`, `TvDialogStyle`, `TvFocusIndicator`, deterministic D-pad/Back behavior and localized resources.
- G3D Quick Settings: nested Wi-Fi entry and parent-return contract.
- E01/MainActivity: existing ConnectivityManager default-network/VPN callbacks, `updateWifiLabel()`, `getQuickSettingsNetworkSummary()`, and Android system-settings intent routing.
- Existing manifest permissions and actual app API baseline (minSdk 26, targetSdk 34, Java 17).

## 2. Baseline verification

| Item | Verified result |
|---|---|
| G3D owner branch | `feature/g3d-quick-settings` |
| G3D exact HEAD | `bbc7b240d2ad7bfc9d22dbaf3437f03b5953bcf8` |
| G3D PR #69 | OPEN / DRAFT / UNMERGED |
| G3D exact-head CI | #1328 / `42344123725`, both required jobs SUCCESS |
| G3D scope | COMPLETE / SOURCE-HOST QC PASS; not reopened |
| G3E starting ancestry | Created directly from the verified G3D exact HEAD |
| PR #70 base | G3D exact HEAD; no integration/main mutation |

## 3. Source and prototype findings

### 3.1 V05 contract

The V05 prototype explicitly rejects fake radio controls. Its intended surface is a system-status/connected-network card, a nearby network list, a short scan-state message, horizontal Rescan and Android Wi-Fi Settings actions, and Close. The prototype scenarios include permission denial, Location disabled, offline, scan throttling, stale results and no results.

### 3.2 Existing source owners

- The legacy Wi-Fi popup in `MainActivity` only displayed connection information and handed off to Android Settings. It did not implement nearby scanning, result presentation, loading/error/empty states or the V05 action layout.
- `MainActivity` already owns network-state observation through its default-network and VPN callbacks and refreshes the network pill through `updateWifiLabel()`. G3E reuses that owner to refresh the Wi-Fi dialog; it does not register a second ConnectivityManager network callback.
- `QuickSettingsDialog` already owns the G3D entry action and nested parent-return path. G3E preserves that dispatch and refreshes the parent summary when returning from Android Settings.
- Existing manifest entries included `ACCESS_WIFI_STATE` and `ACCESS_FINE_LOCATION`; the Wi-Fi flow did not previously request the fine-location permission at runtime.

### 3.3 Android permission and API verification

The official Android Wi-Fi scanning guidance confirms that, for apps targeting Android 10/API 29 or higher:
- `startScan()` requires `ACCESS_FINE_LOCATION`, `CHANGE_WIFI_STATE`, and Location services enabled.
- `getScanResults()` requires `ACCESS_FINE_LOCATION`, `ACCESS_WIFI_STATE`, and Location services enabled.
- Scan requests can be throttled; callers must inspect the boolean result and handle unsuccessful requests.
- On Android 10/API 29 and higher, `SCAN_RESULTS_AVAILABLE_ACTION` is also broadcast for full scans performed by the platform or other apps, allowing passive observation.

The Android API reference marks `WifiManager.startScan()` deprecated since API 28 and states that app-triggered scan requests may be removed in a future release. G3E therefore treats active scanning as best-effort, listens for asynchronous completion, keeps cached results when a request is throttled, and retains Android Settings as the supported operational fallback.

References:
- [Android Wi-Fi scanning overview](https://developer.android.com/develop/connectivity/wifi/wifi-scan)
- [Android WifiManager API reference](https://developer.android.com/reference/android/net/wifi/WifiManager)

## 4. Findings, root cause and disposition

| ID | Severity | Finding / root cause | Disposition |
|---|---|---|---|
| G3E-A01 | P1 scope gap | Legacy popup had no V05 nearby scan/list/rescan or scan-state handling. | Implemented in `WifiSettingsDialog`. |
| G3E-A02 | P1 permission gap | Active scan requires `CHANGE_WIFI_STATE`; scan-result access requires fine location, Wi-Fi state and Location service. | Added only the required manifest permission; runtime-gated fine-location request and Location check. |
| G3E-A03 | P1 lifecycle gap | No dialog-scoped scan-result receiver, timeout or cleanup existed. | Added receiver registration only while dialog is shown; unregister and Handler cleanup on dismissal. |
| G3E-A04 | P1 interaction gap | V05 network rows, horizontal actions, D-pad focus links and Back/return behavior were absent. | Implemented explicit view IDs, vertical focus graph, horizontal action links, Back dismissal and focus restoration. |
| G3E-A05 | P1 architecture boundary | Existing MainActivity already owns network-state callbacks; adding another callback would duplicate ownership. | Reused `MainActivity.updateWifiLabel()` to notify the dialog; no new ConnectivityManager callback in the dialog. |
| G3E-A06 | P1 platform boundary | Authentication, saved networks, connection/disconnection and radio control are system-managed; app APIs/policy do not justify inventing a replacement flow. | Network selection opens Android Wi-Fi Settings. No credentials, direct connect/disconnect, or radio-toggle API was added. |
| G3E-A07 | P2 compatibility risk | `startScan()` is deprecated and scan frequency is throttled by Android. | Best-effort request, boolean result handling, async broadcast, cached results, timeout/limited state and Android Settings fallback. Residual future API-removal risk remains explicit. |

No speculative defect was found in the G3D Quick Settings owner or existing E01 network-state owner.

## 5. Implementation completed

- Added `WifiSettingsDialog` and pure `WifiScanPolicy`.
- Replaced the legacy Wi-Fi popup while preserving the G3D Quick Settings nested entry.
- Implemented a V05-aligned connected/status card, nearby network list, signal/security labels, horizontal Rescan and Android Wi-Fi Settings actions, and Close.
- Reads SSID only when fine-location permission and Location services are enabled. Uses `NetworkCapabilities.getTransportInfo()` on API 29+ and the existing WifiManager connection-info fallback when needed. Unknown/redacted SSIDs are not shown.
- Filters blank/unknown SSIDs, deduplicates by SSID using the strongest observed signal, and caps the displayed list at 12 entries. No BSSID or credential is displayed or persisted.
- Gates scan/list/rescan on fine-location permission, Location enabled and Wi-Fi enabled. Handles permission denial, Location disabled, Wi-Fi disabled, scan request rejection/throttling, asynchronous completion, timeout, unavailable and empty states.
- Adds `CHANGE_WIFI_STATE` solely for `WifiManager.startScan()`; no `setWifiEnabled`, `enableNetwork`, `addNetwork`, disconnect or credential handling is used.
- Reuses MainActivity's existing network-state owner; no duplicate network callback or separate network subsystem.
- D-pad IDs and four-way focus links are explicit. Network-row focus is restored after result refresh; focus moves to a visible control if a permission/state change hides the current action.
- Back/Close return to Quick Settings. Android Settings handoff defers parent restoration until Activity resume so the Quick Settings network summary is refreshed; unavailable system Settings restores the parent immediately.
- Added default/Indonesian/English Wi-Fi strings and updated the Quick Settings source-contract tests for the V05 child dialog.

## 6. Verification and test evidence

### Automated tests

- `WifiScanPolicyTest`: permission, Location, Wi-Fi-disabled and allowed-attempt policy cases.
- `WifiSettingsDialogTest`: permission guidance, view/focus controls, Back dismissal, parent return after Android Settings resume, and fallback when Android Settings cannot launch.
- `tests/test_wifi_surface_contract.py`: Android-owned connection boundary, scan prerequisites, lifecycle, no duplicate network callback, handoff/fallback, focus/Back, manifest permission boundary and default/ID/EN resource parity.
- `tests/test_quick_settings_contract.py`: preserved G3D nested Wi-Fi entry and parent return.
- Exact-head Lists/tools/docs CI job: **127 Python tests PASS**.
- Exact-head Gradle validation: `testReleaseUnitTest` for app and installer, plus `assembleRelease` for app and installer — **BUILD SUCCESSFUL**.

### Exact-head CI

- Workflow run: **#1362 / `42346091802`**.
- Tested HEAD: `89d044152b7d8460884466deb5baa47126f48027`.
- Lists, tools and docs: SUCCESS.
- Launcher build and unit tests: SUCCESS.
- Release APK diagnostic: **1,617,293 bytes**. Compared with G3D's 1,608,373 bytes, growth is 8,920 bytes (approximately 0.55%). This is diagnostic only under D-023.
- APK artifact publication was not performed.

## 7. Quality disposition

| Gate | Result |
|---|---|
| Audit / root-cause analysis | PASS |
| Scope and Android API/permission verification | PASS |
| V05/source implementation | PASS |
| App and installer unit tests | PASS |
| Python repository checks | PASS — 127 tests |
| Release build | PASS |
| Localization resource parity | PASS |
| D-pad/Back and source-contract checks | PASS |
| Exact-head CI | PASS — both required jobs |
| SOURCE-HOST QC | **PASS** |
| Physical Android TV / remote / OEM DEVICE ACCEPTANCE | **PENDING / NOT RUN** |

## 8. Residual risks and explicit boundaries

1. Android's `WifiManager.startScan()` is deprecated since API 28 and may be removed in a future release. Current behavior is best-effort; Android throttling or firmware policy may prevent a fresh scan. The dialog must never represent a rejected or stale scan as a confirmed fresh result.
2. Physical permission prompts, Location toggles, real scan broadcasts, OEM Settings handoff, connection/authentication/disconnection and D-pad behavior still require authorized Android TV device acceptance.
3. No APK was installed or published; no ADB command or physical TV setting was changed.
4. PR #70 remains OPEN / DRAFT / UNMERGED. No merge, rebase, cherry-pick, force-push, integration branch or main change was made.
5. SOURCE-HOST QC PASS does not imply DEVICE ACCEPTANCE PASS.

**Disposition:** G3E is COMPLETE for source-host scope. Next UI Refresh package: **G3F — Global Settings**. Device acceptance remains a separate gate.
