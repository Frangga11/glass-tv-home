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
