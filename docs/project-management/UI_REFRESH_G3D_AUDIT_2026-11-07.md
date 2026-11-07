# UI Refresh G3D Audit — Quick Settings

**Date:** 7 November 2026  
**Disposition:** COMPLETE / SOURCE-HOST QC PASS  
**Governing plan:** GTVH-UIR-002 v2.0  
**Scope:** G3D Quick Settings only; G3E Wi-Fi and G3F Global Settings remain separate work packages.

## Baseline and dependencies

- Repository: `Frangga11/glass-tv-home`
- Parent baseline: `feature/g3c-profile-avatar-child-mode` @ `8cf81f84bd62d0b3da31532116553cfa21a5df98`
- G3D owner branch: `feature/g3d-quick-settings` @ `bbc7b240d2ad7bfc9d22dbaf3437f03b5953bcf8`
- PR #69: OPEN / DRAFT / UNMERGED; base is the exact G3C head above.
- G3C PR #68 remains OPEN / DRAFT / UNMERGED at the same exact head; it was not modified.
- Design authority: G1 V6 V04 Quick Settings; shared contracts: G2 `TvUiTokens`, `TvDialogStyle`, `TvFocusIndicator`, `TvResponsiveLayout`.
- Functional owners: `HomeKeyService`, `AppLanguage`, `CacheCleaner`, `WallpaperSettingsDialog` / E08 wallpaper controller, and `SystemSettingsAccess`.

## Audit findings, root causes and closure

| ID | Confirmed finding | Root cause | Remediation / disposition |
|---|---|---|---|
| G3D-01 | Quick Settings used a narrow legacy vertical list instead of the approved V04 two-column popup. | Legacy `showQuickControlPopup` rendered a 285dp `popup_inputs` menu. | Replaced it with a centered V04 modal using a two-column action grid, wallpaper row and retained legacy settings entry points. FIXED. |
| G3D-02 | D-pad navigation did not support the V04 grid; left/right were consumed by the legacy menu. | `showMenu` intentionally swallowed horizontal arrows for its one-dimensional list. | Added `QuickSettingsFocusGraph` with explicit four-direction links, reachable controls, header exit and focus-safe boundaries. FIXED. |
| G3D-03 | Wi-Fi opened as a separate menu without a deterministic return to Quick Settings. | Existing menu close path only restored its anchor and had no parent-return contract. | Added a scoped return action to the existing menu lifecycle; Back/top-or-bottom boundary returns to Quick Settings and restores Wi-Fi tile focus. FIXED. |
| G3D-04 | Wi-Fi anchor/status depended on the optional top-bar network pill. | Old popup anchored to `btnWifi`; the pill can be hidden, and its label updater intentionally exits when hidden. | Fall back to the Settings anchor; derive Quick Settings network summary independently from Android network capabilities and the optional pill. FIXED. |
| G3D-05 | Cache completion could attempt to show a dialog after the launcher Activity was destroyed or backgrounded. | Asynchronous callback strongly referenced `MainActivity` and used it without lifecycle/window-focus checks. | Added a weak Activity reference and finishing/destroyed/window-focus guards; no result dialog is shown against an inactive Activity. FIXED. |
| G3D-06 | Fixed-height controls risked clipping at larger text scales. | Tile rows were assigned fixed heights despite wrapped title/summary content. | Changed content rows to wrap content within the scrollable modal and retained a minimum 56dp D-pad target. FIXED. |
| G3D-07 | Initial CI exposed an unsupported AlertDialog custom-view overload. | Implementation used a `setView` overload not available to the project's SDK/API surface. | Replaced it with supported `setView(View)`; exact-head build subsequently passed. FIXED. |

No unresolved Critical/High source-host finding remains in the approved G3D scope.

## Implemented behavior and ownership

- **Wi-Fi:** displays Android-exposed network status and hands system-managed operations to the existing Wi-Fi popup / Android Settings. No fake radio switch or unsupported success claim was introduced. The full V05 Wi-Fi redesign remains G3E.
- **Restart / Power:** retain existing confirmation dialogs and `HomeKeyService` system power-menu handling.
- **Language:** retains the existing `AppLanguage` flow and locale behavior.
- **Cache:** retains `CacheCleaner` and its explicit system-trim success-marker semantics; asynchronous completion is lifecycle-safe.
- **Wallpaper:** Next Photo and interval use the existing E08 controller/timer; the wallpaper entry opens the existing `WallpaperSettingsDialog`. No duplicate wallpaper engine, preference store or timer was added.
- **Glass TV Settings / Android Settings:** retained as entry points; Android Settings continues through `SystemSettingsAccess` and its existing Child Mode authorization policy.
- Shared modal, glass/solid fallback, typography, focus treatment and D-pad target tokens reuse G2. Indonesian, English and default resource keys remain aligned.
- No manifest, permission, third-party dependency, integration/main or unrelated feature change.

## Verification

- Exact-head GitHub Actions run #1328 / `42344123725` on `bbc7b240d2ad7bfc9d22dbaf3437f03b5953bcf8`: **both required jobs SUCCESS**.
  - Launcher: `testReleaseUnitTest`, installer unit tests, and `assembleRelease` completed; `BUILD SUCCESSFUL`.
  - Lists/tools/docs: **120 Python tests passed**, including all G3D source-contract tests.
  - Quick Settings Robolectric coverage verifies all action dispatches, actual dialog D-pad links and Back dismissal.
  - Focus-graph unit tests verify navigation relationships and reachability.
- Release APK diagnostic: **1,608,373 bytes**, +6,012 bytes (+0.38%) against G3C's 1,602,361-byte baseline. Informational/non-blocking under D-023; no functionality or required asset was removed for size.
- Workflow artifacts: none. APK was not published, downloaded or installed.
- PR #69 remains OPEN / DRAFT / UNMERGED. No merge, rebase, cherry-pick, force-push, ADB, device setting change or physical test occurred.

## Final scope boundary

**G3D is COMPLETE for SOURCE-HOST scope.** This is not DEVICE-VERIFIED or full physical acceptance.

Deferred / not waived:
- Actual Mi TV Stick / Android TV remote D-pad, Back, focus visibility and repeated open/close.
- 1920×1080 and 4K sizing, OEM overscan, large text and daylight readability on physical panels.
- OEM Wi-Fi Settings handoff and live network-status behavior.
- Real restart/power system-menu labels and accessibility-service availability across OEM firmware.
- Cache-cleaning permission/debugging prompt, success/failure and background/lifecycle behavior on device.
- Wallpaper Next/interval/shuffle behavior with real collections, timer and process/lifecycle transitions.

These remain within the later authorized physical acceptance / Project G gate. G3E — Wi-Fi is the next UI Refresh package.
