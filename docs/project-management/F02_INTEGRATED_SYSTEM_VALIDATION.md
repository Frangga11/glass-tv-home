# Project F02 — Integrated System Validation

## Status and authority

- Workstream: Project F — system-level verification of the integrated B–E implementation.
- Phase: F02.
- Validation branch: `integration/project-f02-system-validation`.
- Starting integration checkpoint: `8fe90a9fdd493c5d81ffdacbe16054630a26580a`.
- F02 commit: `09fbdd448fef795b827291667820e0d0d0011251` (CI workflow guard).
- Physical Android TV testing, APK publication for user testing, installation, remote-control acceptance, and device-side performance checks are reserved for Project G.
- CI may compile a release variant as a build-validation input, but must not publish or distribute an APK artifact during F.

## Objective

Prove, as far as host-side/system-level evidence allows, that the integrated application is internally coherent: modules communicate through stable contracts, state has a single owner, UI/navigation/focus rules are globally consistent, asynchronous work is lifecycle-safe, and failures do not silently corrupt or strand the application.

A green compile or unit-test run is necessary but not sufficient. Every claim must be tied to a reproducible test, static check, source audit, or explicitly deferred device acceptance case.

## Mandatory operating rules

1. Do not install, launch, or test the app on a physical Android TV device during Project F.
2. Do not publish or distribute an APK artifact during Project F. Release compilation is validation-only.
3. Keep source feature branches intact. Fix defects in the owning module branch, then reconcile the corrected source into the F integration branch.
4. Do not hide, bypass, weaken, or delete a failing test merely to obtain a green CI result.
5. Every finding records severity, evidence, root cause, owning module, corrective commit/PR, regression test, and retest result.
6. Global UI concerns belong to Project B; module-specific behavior belongs to the owning C/D/E module. Cross-module coordination is required when root cause crosses boundaries.
7. Mark untestable device-dependent behavior as DEFERRED_TO_G, never PASS.
8. Do not declare F complete while any blocker/critical defect, unresolved integration conflict, or unverified mandatory contract remains.

## F02 validation tracks

### F02-A — Build, repository and dependency integrity
- Re-run the full CI suite from the F02 branch.
- Verify Gradle dependency resolution, release compilation, unit-test discovery, APK size guard, and repository/documentation checks.
- Inspect warnings, skipped tests, flaky tests, and test exclusions; a successful exit code alone is not enough.
- Verify manifest declarations, permissions, exported components, intent filters, min/target SDK assumptions, and resource references.

### F02-B — Global UI, navigation and focus contracts (Project B)
- Inventory all screens, tabs, subtabs, dialogs, sheets, menus, and transient overlays.
- Verify one global dialog/focus/navigation policy is applied consistently rather than duplicated local implementations.
- Check focus entry, traversal, restoration, escape/back/close semantics, disabled/loading states, empty states, and error states through source-level tests or deterministic UI tests where available.
- Check layout constraints, density/scaling assumptions, clipping/overlap, z-order, insets, and dialog width/height bounds.
- Verify no popup steals focus permanently, traps navigation, or leaves stale overlays after dismissal.
- Device-specific remote behavior remains deferred to G.

### F02-C — State ownership, persistence and lifecycle (Project D)
- Trace each persisted setting from UI event → state owner/repository → storage → observer/UI propagation.
- Verify defaults, migrations, invalid/corrupt values, concurrent updates, profile isolation, and stale cache invalidation.
- Exercise process recreation/recovery using host-side tests/fakes where feasible.
- Check StandBy photo persistence, avatar/profile state, app-lock/hidden-app refresh, and D01-F11 recovery expectations.
- Check cancellation, duplicate callbacks, race windows, and state restoration after asynchronous operations.

### F02-D — Communication and upload contracts (Project C)
- Verify QR/session creation, endpoint ownership, request/response schemas, authentication/authorization assumptions, timeout/retry behavior, and cleanup.
- Test malformed, oversized, unsupported, interrupted, duplicate, and concurrent uploads.
- Preserve C07 upload-session resilience while verifying cleanup and recovery behavior.
- Confirm phone/server and launcher boundaries do not rely on hard-coded addresses or stale session state.
- Network behavior requiring a real TV/phone LAN pairing remains deferred to G unless covered by deterministic local integration tests.

### F02-E — Feature integration (Project E)
- Verify device settings and home-control actions call the correct state owners.
- Verify profile/avatar/app-lock/kids-mode boundaries and navigation placement.
- Verify widget data states and graceful degradation when providers are unavailable.
- Verify home screen composition and global UI integration.
- Verify animation/effect lifecycle and performance-safe fallbacks.
- Verify Appearance wallpaper management remains distinct from Quick Control wallpaper entry points while sharing the correct underlying state and asset pipeline.
- Verify wallpaper selection, preview, persistence, reset/default, and error handling do not overwrite unrelated preferences.

### F02-F — Cross-module regression and negative-path testing
- Build a requirement-to-test matrix for B–E and their interdependencies.
- Add or strengthen tests for invalid input, null/empty state, unavailable providers, storage failure, network timeout, repeated actions, rapid state changes, activity recreation, and partial failure.
- Check for duplicate sources of truth, circular dependencies, direct UI-to-storage shortcuts, duplicated dialog styling, and hidden side effects.
- Validate no-op/disabled/loading/error feedback for every user action path that can fail.

### F02-G — Static quality and security review
- Inspect crash-prone calls, unsafe casts, null handling, threading, executor ownership, leaked listeners/callbacks, unbounded work, and exception swallowing.
- Review exported Android components, intent handling, local server endpoints, file validation, URI permissions, and sensitive data exposure.
- Verify resource cleanup and lifecycle cancellation.
- Report static-analysis coverage honestly; do not claim a tool ran if it was unavailable.

## Required evidence and exit gates

For each validation track record:
- exact branch and commit SHA;
- command/test suite or source paths inspected;
- expected behavior and observed result;
- pass/fail/blocked/deferred status;
- logs or reproducible evidence;
- defect ID and owning module where applicable.

F02 may pass only when:
- all available CI jobs pass on the exact integration head;
- no unexplained skipped/disabled tests remain;
- all critical cross-module contracts have tests or explicit evidence;
- no blocker/critical defect remains open;
- all discovered defects have been fixed at source and retested;
- all device-dependent cases are listed for G and are not misrepresented as passed;
- documentation and integration branch are synchronized.

## Project G handoff

Project G receives:
- the exact F-approved integration SHA;
- full test matrix and CI run links;
- known limitations and all DEFERRED_TO_G cases;
- release build instructions and artifact publication approval;
- device acceptance checklist for the target Android TV hardware.

Project F does not publish or install the final TV-test APK.


## Host-side UI test layer — 3 November 2026

- Host-side test coverage now includes Robolectric API 30 window tests for the B01 shared dialog contract. Assertions cover centering, dim-behind flag/amount, width viewport bound, wrap-content height, custom dimensions, and dim clamping.
- Robolectric is `testImplementation` only; it must not enter the release APK. Android resources are enabled only for local JVM unit tests.
- These tests supplement source-level modal-contract regression tests. They are not a substitute for Project G acceptance of actual TV renderer, remote/DPAD focus, OEM window policy, HDMI/display scaling, firmware and ADB shell behavior.
- The new dependency and tests must pass the exact F02 head CI; no test may be skipped or softened to obtain a green run.


## Home-key / Recent Apps routing regression gate — 3 November 2026

- HomeKeyService intercepts HOME/ALL_APPS key events. Its former immediate ACTION_UP behavior prevented the documented double-press HOME gesture from reaching RecentAppsActivity.
- B01 now uses a pure-Java HomeKeyPressTracker (350 ms double-press threshold, 400 ms single-action delay) and routes single/double/long Home without changing the existing disabled KEYCODE_APP_SWITCH policy.
- Host-side unit tests cover timing boundaries, expiry, cancellation and double-press classification; a source-contract test guards RecentApps launch, Close All routing and single-Home behavior.
- Project G must still verify real remote key timing, OEM accessibility filter delivery, single-Home latency, double-press deck launch, double-press Close All and long-press Settings coexistence. Host CI is not device acceptance.


## Exact-head retest checkpoint — 3 November 2026

- F02 integration branch `integration/project-f02-system-validation`, head `ddf130a348182546b28ff59c292c9b5ceebacb2e`.
- Exact-head push CI run #41512382568: Lists/tools/docs SUCCESS; Launcher build/unit tests SUCCESS. Release compilation was validation-only; read-only APK diagnostics and the 1.5 MiB size gate passed. No APK artifact was published.
- Matching PR CI run #41512391849: Lists/tools/docs SUCCESS; launcher unit tests, release build, APK diagnostics and size gate all completed successfully; final job cleanup remained in progress at the checkpoint.
- C07 owner PR #24 head `80a92264c51631fb107c75f32a3cd8e4b8aa9d9a`, run #41512386827: both jobs SUCCESS.
- The last C07/F02 upload deadline failure was a bad test expectation (3000 ms instead of 3500 ms for 6000 ms deadline at 2500 ms elapsed). Corrected on owner and integration branches; no test weakened.
- F02 intentionally combines C07 upload-session protections with D05's downstream `StandbyPhotoStore.UploadResult` persistence contract. Do not overwrite the D05-aware StandBy upload handler with the older C07 boolean-based handler.
- B01 #10, D05 #29, D06 #30 and D01 #51 exact-head owner CI runs also passed (see findings register).
- PR #50 remains Draft/open/unmerged. No APK publication/installation and no physical Android TV testing. All device-dependent acceptance remains DEFERRED_TO_G.


## Finding disposition matrix — 3 November 2026

The host-side remediation/test outcomes for F02-UI-001, F02-SEC-001/002/003/004, F02-TEST-001, F02-FUNC-001/002, F02-PLAT-001 and F02-C07-001/002 are recorded in `F02_FINDINGS_REGISTER.md`. The source/test integration checkpoint `ddf130a348182546b28ff59c292c9b5ceebacb2e` passed exact-head push CI #41512382568 (both jobs), including Robolectric API 30 dialog-window tests, all JVM regression tests, release compilation, read-only APK diagnostics and the size budget.

This is not a full Project F exit: latest documentation/status reconciliation commits require a new exact-head CI run, and device-dependent remote/focus/rendering/OEM/ADB/LAN behavior remains DEFERRED_TO_G. PR #50 stays Draft/open/unmerged; no APK publication or physical TV test.


## Additional manifest and build-budget checks — 3 November 2026

- F02-SEC-005: E01 owner corrected InputKeyService manifest export to `true` while retaining `android.permission.BIND_ACCESSIBILITY_SERVICE`; owner source/test CI #41513629734 passed. The same change and regression guard are mirrored into this F02 branch; exact-head retest required. Actual remote Input-button acceptance remains DEFERRED_TO_G.
- F02-BUILD-001 watchlist: exact F02 source/test build produced a 1,564,981-byte APK against the 1,572,864-byte cap, leaving only 7,883 bytes. R8 minify and resource shrink are already enabled. Largest packaged assets are classes.dex, two font resources, resources.arsc and the default Dempo wallpaper. Do not trade away required fonts/wallpaper quality for an unmeasured size reduction; monitor the cap on every subsequent build.


## QR upload shutdown commit guard — 3 November 2026

- Final risk-based source audit traced the stop/revoke boundary through Avatar upload, custom Wallpaper upload, and StandBy photo persistence.
- Each final mutation/commit path performs its session check and durable state/callback operation while holding the same server monitor used by `stop()`; the StandBy persistence-failure retention policy remains intact.
- Added explicit regression assertions for the custom Wallpaper rename/callback and StandBy store/callback critical sections in `tests/test_wallpaper_session_shutdown_atomicity.py`. This closes a test-coverage gap; no production behavior was changed.
- The new exact-head CI run must pass before this supplemental gate is marked complete. Real phone-to-TV cancellation, Wi-Fi loss, expiry and retry remain DEFERRED_TO_G.


## Concurrent QR upload storage protection — 3 November 2026

- Risk-based audit found that the StandBy free-space check was per-request rather than reservation-based, while the custom Wallpaper endpoint had no equivalent free-space margin. With up to eight active clients, concurrent requests could overcommit the same available-space snapshot.
- Added a shared overflow-safe storage-reservation policy and synchronized in-flight byte reservations in WallpaperUploadServer. Both custom Wallpaper and StandBy upload paths reserve declared bytes before writing and release reservations in all exit paths.
- The existing per-file upload size, StandBy collection count/byte limits, and wallpaper collection behavior are unchanged. No arbitrary total wallpaper cap was added. A 64 MiB free-space safety margin is retained; insufficient space returns HTTP 507, while expired sessions remain HTTP 410.
- Added JVM boundary tests and source-contract regression checks. Exact-head CI is required before marking this gate PASS.
- Physical concurrent LAN uploads, cancellation and low-storage behavior on target Android TV remain DEFERRED_TO_G.


### Reservation scope across replacement QR sessions — 3 November 2026
- Extended the storage reservation ledger from per-server-instance state to a process-wide static lock/counter. This accounts for old session workers that are still draining after stop() while MainActivity starts a replacement QR session.
- The free-space calculation now includes reservations held by all WallpaperUploadServer instances until their request finally blocks release them. Session revocation checks remain per server instance.
- Updated the source-contract regression test to require the shared ledger. Exact-head CI is required on the latest integration commit.
