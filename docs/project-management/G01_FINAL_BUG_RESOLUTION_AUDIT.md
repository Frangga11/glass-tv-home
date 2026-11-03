# G01 — Final Bug Resolution Audit

**Checkpoint:** GTVH-G01-2026-11-03  
**Date:** 3 November 2026 (WIB)  
**Disposition:** G01 FULL PASS — source defect resolution, 26-path semantic reconciliation, isolated integration merge, and exact merge-commit CI verified  
**Canonical documentation branch:** `docs/project-master-inventory`  
**Accepted integration commit:** `integration/b-series` @ `f6f1f05b7a560381b8bb423eaa707a1d6d0e8832`  
**Release/device boundary:** integration-only merge completed; `main` untouched; no APK publication/installation or TV setting change performed.

## 1. Scope and method

G01 is **Penyelesaian Bug Terakhir (Final Bug Resolution)** under the user-approved Roadmap A–G v1.0. The roadmap remains exactly 47 work items; no work item was added, removed, or reordered.

The audit followed: Trigger → Input → Verification → Work/Root-Cause Review → Decision Point → Output → Quality Check → Final/Not Final.

The review covered the latest live owner PR heads for the high-risk remaining source contracts, the F02 cross-module integration head, and the frozen F06/F07 heads. Each selected PR head was checked against its associated GitHub Actions run and both required CI jobs. Relevant source and regression-test files were inspected directly. A passing source/host check is not treated as physical device acceptance.

## 2. Root-cause findings and disposition

| Finding / contract | Root cause established | Owner fix and regression protection | G01 disposition |
|---|---|---|---|
| Home single/double/long-press routing (F-019) | Immediate single-Home handling consumed the first press before a second press could route to Recent Apps / Close All. | B01 `HomeKeyPressTracker` state machine; 350 ms double window, delayed single action; JVM timing tests; explicit Home routing retained. | FIXED / OWNER + INTEGRATION CI PASS |
| Force Stop queue isolation (F-023) | Clearing callbacks on the shared Handler could cancel Home gesture and power-action timeout callbacks. | Dedicated `forceStopHandler`; Home/power callbacks remain on their own Handler; lifecycle cleanup and source regression guard. | FIXED / OWNER + INTEGRATION CI PASS |
| Restart action routing (F-025) | Accessibility global-action ID 12 is not reboot; it can invoke the accessibility-button chooser. | Restart now opens the visible system power dialog and selects supported localized/vendor labels; timeout fallback retained; contract test rejects action ID 12. | FIXED / OWNER + INTEGRATION CI PASS |
| Recent Apps external exposure (F-021/F-024) | Exported RecentAppsActivity exposed destructive Close All behavior to explicit external launches. | Activity is non-exported, unnecessary MAIN filter removed, explicit in-app HomeKeyService route retained; manifest security regression tests added. F-021 is superseded by F-024. | FIXED / OWNER + INTEGRATION CI PASS |
| Location configuration broadcast (F-015) | Exported receiver accepted unauthenticated configuration broadcasts. | Require `android.permission.DUMP`, preserve both documented actions and shell/system contract; manifest/source guard. | FIXED / OWNER + INTEGRATION CI PASS; real ADB-shell compatibility remains G03 |
| Azan activity exposure (F-016) | App-internal AzanActivity was externally launchable. | Set non-exported; preserve app-owned explicit Intent/PendingIntent path; manifest guard. | FIXED / OWNER + INTEGRATION CI PASS |
| Shared dialog contract (F-017/F-022) | Some dialog entry points bypassed shared global styling; source-only checks did not prove actual Window attributes. | MainActivity delegates to shared `TvDialogStyle`; Robolectric API 30 tests verify centering, dim, viewport sizing, custom sizing and dim clamping. | FIXED / F02 EXACT-HEAD CI PASS; physical rendering remains G03 |
| Wallpaper QR mutation vs session shutdown (F-026) | A request could pass an outer session check, then mutate state/files after session revocation. | Select/delete/slideshow/clear routes serialize with stop and recheck session inside the same monitor; HTTP 410 for expired/revoked session; upload commit critical sections covered. | FIXED / C07 + F02 CI PASS |
| QR upload deadlines (F-028) | Wall-clock time could jump; one test expectation also contained an arithmetic error. | Use monotonic elapsed realtime for session/request deadlines; correct assertion to 3500 ms for 6000−2500; clock-domain regression guards retained. | FIXED / C07 + F02 CI PASS |
| Concurrent QR upload storage (F-031) | Independent per-request free-space checks could overcommit the same free-space snapshot. | Process-wide atomic reservations shared by Wallpaper and StandBy, 64 MiB free-space margin, HTTP 507 on insufficient space, release on all exit paths. No arbitrary total Wallpaper cap added. | FIXED / C07 + F02 CI PASS |
| Wallpaper/StandBy commit vs stop coverage (F-032) | Prior tests did not independently guard both upload commit paths against shutdown races. | Tests assert Wallpaper file rename/callback and StandBy persistence/callback remain inside the stop monitor and recheck session validity. | FIXED / C07 + F02 CI PASS |
| Accessibility service binding (F-029) | InputKeyService was non-exported, preventing Android system binding despite installer opt-in. | Exported for system binding while retaining `BIND_ACCESSIBILITY_SERVICE`; manifest regression test added. | FIXED / E01 + F02 CI PASS |
| Wallpaper persistence / orphan uploads | Stale Custom pin and abandoned private QR upload parts could survive recovery. | E08 clears stale pin safely; C07 cleanup removes only orphaned private `custom_*.upload` parts and protects active workers. | FIXED / E08 + C07 CI PASS |
| F06 language and modal consistency | Dynamic labels, QR error/fallback copy, and selected custom dialogs had incomplete locale/global-style coverage. | Default/ID/EN resource parity and localization regressions; QR server/browser error paths localized; custom dialogs routed through shared modal style. | FIXED / F06 EXACT-HEAD CI PASS |
| F07 performance and memory stability | Per-frame shader allocation, UI-thread image decode/Binder polling, unbounded StandBy decode queue, incomplete low-memory handling, and cache growth. | Shader/gradient reuse and bounded ripple pool; background/coalesced work; bounded decode queue; OOM/rejected-worker guards; cache bounded by 150 files and 512 MiB. | FIXED / F07 EXACT-HEAD CI PASS |

## 3. Exact owner and integration verification

All listed runs were associated with the exact live PR head shown below. Both required jobs — **Lists, tools and docs** and **Launcher build and unit tests** — completed SUCCESS.

| PR | Workstream | Exact head | CI run | Result |
|---:|---|---|---:|---|
| #10 | B01 navigation/Home gestures | `389e289e939276ce9e20d7e6c82c293d3f83c70f` | 41509724289 | PASS |
| #24 | C07 upload/session resilience | `2315f12472f469843c1441ad7d01c6898858c4c5` | 41563429553 | PASS |
| #29 | D05 StandBy atomic persistence | `7f043943ab0c1d2d1217fc56845d81ac7583c96d` | 41505852041 | PASS |
| #30 | D06 process-death/alarm recovery | `1bb6ce9bf04d72f3fe33b4e1630af561e99fe0f7` | 41505865772 | PASS |
| #34 | E01 device/accessibility settings | `edd6461c932705a109ed9768649cae7e3c162c59` | 41513716451 | PASS |
| #41 | E08 wallpaper settings/recovery | `edd24d72256d06da4578971ea7bac1802746a3a6` | 41562661052 | PASS |
| #50 | F02 integrated source validation | `2ede452b3137580bd51088661f80b6d0eeac7f4b` | 41523201383 | PASS |
| #51 | D01 location broadcast authorization | `66352838e6155a42a9ea61477643a238f360434f` | 41506847521 | PASS |
| #55 | F06 localization/global UI | `487099013e6d9bd65cc07d72e760b1e14ab1f091` | 41566633872 | PASS |
| #56 | F07 performance/stability | `976e068d48ecc1ede2811ac455372395d382ba76` | 41568721270 | PASS |

Additional exact-head evidence:
- F02 run #41523201383: 42 Python tests, one intentional merge-time README-count skip; Android unit tests and release validation build PASS; APK diagnostic 1,565,713 bytes / 1,572,864-byte cap; no workflow artifacts.
- F06 run #41566633872: 89 Python tests, one intentional README-count skip; Android unit tests and release validation build PASS; APK diagnostic 1,569,397 bytes / 1,572,864-byte cap.
- F07 run #41568721270: 99 Python tests; launcher and installer unit tests, release build and APK size gate PASS; APK diagnostic 1,571,293 bytes / 1,572,864-byte cap. Artifact list empty.
- C07 source inspection confirmed monotonic elapsed-realtime deadlines, atomic storage reservation, HTTP 507 boundary, and serialized Wallpaper mutation/stop. The shutdown atomicity regression test covers both Wallpaper and StandBy commit paths.

## 4. Quality decision

**No new reproducible host/source defect was found in the audited remaining bug contracts. No production source change was justified in this G01 pass.** This is deliberate: do not introduce speculative changes to already verified code or weaken existing regression tests.

All historical source findings above have owner-side fixes and exact-head CI evidence. F-021 is superseded by F-024. Historical “CI pending” labels for F-019 and F-022–F-026/F-031/F-032 are stale relative to the exact-head runs recorded here and must be treated as closed for host/source scope.

## 5. Explicitly not passed — G03 physical acceptance

These are not unresolved source bugs and must not be relabelled PASS from CI:
- Mi TV Stick / Android TV 11 actual UI, D-pad, Back, Home/double-Home/long-press, Force Stop, power-menu and OEM behavior.
- Real ADB-shell compatibility for `android.permission.DUMP`.
- Phone-to-TV QR/LAN upload: disconnect, cancel, expiry, retry, concurrency, low-storage and recovery.
- Actual process death, force-stop, reboot, prayer-alarm restore, StandBy/DreamService and filesystem recovery.
- Measured RAM/PSS, frame timing/FPS, ANR/crash rate, thermal trend and long-run soak.

No APK was published or installed; no TV settings, HOME default, ADB state, or device package state were changed.

## 6. Official Project G roadmap (user-approved, unchanged)

The official Roadmap A–G v1.0 remains exactly 47 work items. Project G has exactly these five stages:
1. **G01 — Penyelesaian Bug Terakhir (Final Bug Resolution).**
2. **G02 — Build APK Final yang Sudah Teruji (Final Tested APK Build).**
3. **G03 — Uji Instalasi pada Mi TV Stick (Mi TV Stick Installation Test).**
4. **G04 — Finalisasi Dokumentasi GitHub (Final GitHub Documentation).**
5. **G05 — Finalisasi Memory dan Handover ChatGPT (Final ChatGPT Memory/Handover).**

No stage was added, removed, reduced, or reordered. This approved five-stage roadmap supersedes the older six-step Project G execution-order wording in historical handover notes.

## 7. Next checkpoint

G01 defect-resolution subgate is PASS, but G01 project-wide branch reconciliation remains OPEN/BLOCKING. Do not start G02 until B-series and F07 have been reconciled on an isolated candidate, all required owner heads have an explicit disposition, and exact-head integrated CI plus cross-group regression pass. G03 remains the physical Mi TV Stick installation/acceptance gate and requires explicit readiness approval.


## G01 branch-reconciliation status correction — 3 November 2026

The source/host bug-resolution review remains PASS. The project-wide G01 gate is NOT CLOSED because the live compare found that `integration/b-series` @ `9070894fa6d9ef8aec034f46dfe777e866709dfe` and the F07 candidate @ `976e068d48ecc1ede2811ac455372395d382ba76` are diverged, with 26 shared-file differences. The five-stage Project G roadmap is unchanged; G02 is blocked pending a clean isolated combined candidate and exact-head integrated CI. See `G01_BRANCH_RECONCILIATION_ADDENDUM.md`.


## 8. G01 final integration acceptance — 3 November 2026

- B-series and F07 were semantically reconciled across all 26 shared paths. F07 implementations were retained where they contain later lifecycle, persistence, timeout, concurrency, or performance hardening; B-series-only fallback test coverage was preserved.
- A real F06 defect was corrected: four Daily Verse values had been assigned to the wrong language resources. Explicit semantic locale assertions were added; F06 exact-head CI passed.
- Reconciled candidate PR #58 was merged into `integration/b-series` at exact commit `f6f1f05b7a560381b8bb423eaa707a1d6d0e8832`. Candidate and merge commit share identical tree SHA `a016951e69d99603a2a90722420cfe118c93eed1`.
- Exact merge-commit CI #41583505195: both required jobs SUCCESS; Python 99 tests all OK; Android launcher/installer unit tests PASS; release validation build SUCCESS; APK diagnostic 1,571,293 bytes / 1,572,864-byte budget; no workflow artifacts.
- Main-only commit `6eaf787c56226606b1d2e650c59540c553756c62` enables manual workflow_dispatch APK publishing. It was intentionally excluded from integration to preserve the validation-only release gate.
- G01 source/host integration gate: **PASS**. G02 final tested APK build: **READY TO START** from `f6f1f05b7a560381b8bb423eaa707a1d6d0e8832`. G03 physical Mi TV Stick acceptance: NOT STARTED.
- No merge to `main`, no APK publication/installation, and no TV setting changes.
