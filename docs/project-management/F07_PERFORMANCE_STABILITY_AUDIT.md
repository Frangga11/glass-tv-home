# Project F07 — Performance, Crash, Freeze, RAM & Stability Audit

**Checkpoint ID:** GTVH-F07-CP-2026-11-03  
**Date:** 3 November 2026  
**Disposition:** HOST/SOURCE EXIT PASS — DEVICE PROFILING DEFERRED_TO_G  
**Integration PR:** #56 — Draft / Open / Unmerged  
**Integration branch:** `integration/project-f07-performance-stability`  
**Exact head:** `976e068d48ecc1ede2811ac455372395d382ba76`  
**Base:** `integration/project-f06-localization` at `487099013e6d9bd65cc07d72e760b1e14ab1f091`  
**Compare:** 19 commits ahead / 0 behind; ten changed paths.

## 1. Trigger and acceptance objective

F07 audits performance, crash/freeze risk, memory behavior, storage efficiency and lifecycle stability before the staged F-series integration is frozen. The target hardware remains the user's Mi TV Stick / Android TV 11, but physical profiling is not authorized while the staged source audits remain open. This checkpoint therefore records host/source evidence only; no FPS, RAM, ANR, thermal or long-run soak result is claimed.

## 2. Audit method and inputs

- Exact F06 source baseline and F07 isolated integration branch.
- Full changed-path review, targeted call-graph and lifecycle inspection for MainActivity wallpaper handling, Backdrop rendering/cache, WeatherBackgroundView animation, NowPlayingManager Binder/notification polling, StandBy image decode, OnlineWallpapers storage and CacheCleaner.
- Existing upload, persistence, shutdown, navigation, localization, resource and UI regression tests.
- New Python source-contract guards and Robolectric weather rendering/cache tests.
- Exact-head GitHub Actions run including launcher and installer unit tests, release build and APK size enforcement.
- No candidate APK was published, installed or used to modify TV settings.

## 3. Findings and repairs

| ID | Finding | Risk | Repair | Result |
|---|---|---|---|---|
| F07-PERF-001 | Sunny, Night and Cloudy weather rendering allocated RadialGradient shaders during every animation frame. | Repeated native/Java allocation and GC pressure. | Build sun/moon shaders when viewport/condition changes; cache per-cloud gradients and reuse shader matrices. | FIXED |
| F07-PERF-002 | Rain splash animation created short-lived ripple objects repeatedly. | Avoidable allocation churn during continuous animation. | Reuse a bounded pool of at most 16 ripple objects. | FIXED |
| F07-UI-001 | MainActivity's legacy configured-wallpaper fallback decoded the image inline during Activity creation. | Startup UI stall / freeze risk on slower TV storage/CPU. | Route through the background wallpaper decoder. | FIXED |
| F07-UI-002 | QR wallpaper upload preview decoded a bitmap on the UI thread. | UI stall immediately after upload. | Decode on the background worker; invalidate on dialog dismissal and release stale preview bitmaps. | FIXED |
| F07-LIFE-001 | Rapid wallpaper requests could leave obsolete work/results queued and could post bitmaps after Activity teardown. | Stale wallpaper overwrite, wasted decode/blur work and retained bitmap risk. | Add latest-request generation tokens, stale-result recycling, executor rejection handling, generation invalidation and Handler callback cleanup on destroy. | FIXED |
| F07-PERF-003 | Now Playing refresh performed MediaSession and notification Binder queries from the main-thread polling callback. | Binder latency could delay focus/navigation and cause jank. | Move query body to a dedicated single-thread worker; coalesce overlapping refresh events; post listener updates to main. | FIXED |
| F07-MEM-001 | Backdrop blur did not guard render allocation/runtime failures; partial allocation could leave one reusable work array null. | Low-memory blur could fail repeatedly or terminate worker task. | Catch OOM/runtime failures; handle executor shutdown; reallocate both arrays when either is absent; clear abandoned tint markers when worker changes. | FIXED |
| F07-MEM-002 | StandBy image decoder used an unbounded single-thread executor queue. | Slow content-provider reads could accumulate stale decode requests. | Use one background-priority worker with a bounded two-item queue and discard-oldest policy. | FIXED |
| F07-GC-001 | Cache cleanup explicitly invoked `System.gc()`. | Unnecessary stop-the-world GC request during launcher use. | Remove forced GC; filesystem deletion releases storage and ART manages heap reclamation. | FIXED |
| F07-STOR-001 | Online wallpaper cache was limited by file count (150) and per-image size (25 MiB) but had no aggregate-byte budget. | Theoretical cache growth could approach 3.75 GiB. | Add a 512 MiB aggregate cache budget; prune oldest images and companion metadata until both file-count and byte limits are met. | FIXED |

The 512 MiB limit applies only to the online wallpaper cache. It does not impose a new arbitrary cap on the user's private Custom Wallpaper collection. Existing StandBy limits (100 photos / 512 MiB with a 64 MiB free-space margin) and upload storage reservations remain unchanged.

## 4. Regression and functional test coverage

- Python F07 suite guards: cached weather shaders, bounded ripple pool, async wallpaper fallback and QR preview, stale-result cleanup, worker-based Now Playing refresh, Backdrop OOM/rejection recovery, bounded StandBy queue, cache-cleaner behavior and online wallpaper aggregate budget.
- New Robolectric `WeatherBackgroundViewPerformanceTest`: renders every weather condition in low-spec/reduced-motion mode and verifies radial shader object identity remains stable across repeated frames.
- Extended `OnlineWallpapersTest`: verifies oldest-first pruning satisfies aggregate-byte and file-count budgets and deletes associated metadata.
- Existing upload reservation/deadline/cancellation, orphan cleanup, persistence recovery, D-pad/navigation, UI modal, locale/resource and installer tests remain in the CI matrix.

## 5. Exact-head CI evidence

**Workflow run:** #41568715178  
**Exact head:** `976e068d48ecc1ede2811ac455372395d382ba76`

| Gate | Result |
|---|---|
| Lists, tools and docs | SUCCESS |
| Python source-contract suite | 99 tests; all PASS |
| Launcher `testReleaseUnitTest` | SUCCESS |
| Installer `testReleaseUnitTest` | SUCCESS |
| Release `assembleRelease` | SUCCESS |
| APK diagnostics | SUCCESS |
| APK size enforcement | SUCCESS |
| Workflow artifacts | Empty; no APK publication |

**APK exact size:** 1,571,293 bytes  
**Budget:** 1,572,864 bytes  
**Headroom:** 1,571 bytes

The APK is under the established 1.5 MiB budget. Headroom is narrow and must remain a monitored constraint; do not add dependencies or large resources without an explicit size review. No feature has been removed to meet the budget.

## 6. Integration and governance

- PR #56 remains Draft / Open / Unmerged under D-013.
- Base is F06 exact head `487099013e6d9bd65cc07d72e760b1e14ab1f091`; F07 exact head is `976e068d48ecc1ede2811ac455372395d382ba76`.
- Compare is 19 commits ahead / 0 behind, ten changed paths.
- No merge to `integration/b-series` or `main`; no APK publication/installation; no TV setting changes.
- No source/test changes were made to feature-owner branches in this F07 integration task.

## 7. Explicit device acceptance boundary

Host/source tests cannot establish real device FPS, frame time, heap/RSS, PSS, ANR/crash rate, thermal throttling, graphics-driver behavior or long-run stability. These remain Project G measurements on the target Mi TV Stick after explicit readiness approval for one labelled TV-test APK.

Project G must profile/soak at minimum:
1. Cold launcher start and return from heavy apps; repeated Home/Settings/Back/focus navigation.
2. Weather SUNNY/CLOUDY/RAIN/THUNDERSTORM/NIGHT animations, reduced-motion and low-spec behavior.
3. Repeated wallpaper rotation, rapid Next/Shuffle, 1080p and 4K, large/corrupt images and low-memory conditions.
4. Now Playing polling while music starts/stops, notification listener reconnects and app transitions.
5. StandBy photo rotation, screensaver/DreamService start-stop, content-provider failures and process lifecycle.
6. QR upload concurrency, cancellation, network disconnect, low storage, temp cleanup and recovery.
7. Long-run soak with memory trend, CPU/frame timing, ANR/logcat, thermal and storage observations.

No physical result is claimed by F07. Device acceptance is pending Project G.

## 8. Decision and handover

**Decision: F07 HOST/SOURCE EXIT PASS.** The identified reproducible source-level performance, memory, lifecycle and storage risks are repaired; Python and Android/installer unit suites, release build and APK size gate pass on the exact head.

**Overall performance acceptance remains PARTIAL until Project G device profiling and soak testing.** Keep PR #56 Draft/open/unmerged until grouped Project G final audit and explicit merge approval.

Next: Project G final integration audit, exact branch-head/dependency reconciliation, staged group merges only after approval, then one approved TV-test APK and measured target-device acceptance. Full F-series integration is not proven by independent PR CI alone.
