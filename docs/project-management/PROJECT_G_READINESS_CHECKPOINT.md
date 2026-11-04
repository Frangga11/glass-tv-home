# Project G — Final Integration & Device Acceptance Readiness

**Checkpoint ID:** GTVH-G-READY-2026-11-03  
**Status:** READY TO START — G01 BASELINE/DEPENDENCY RECONCILIATION ONLY  
**Project F:** Officially closed at HOST/SOURCE EXIT PASS (F01–F07)  
**Frozen F07 candidate:** `integration/project-f07-performance-stability` @ `976e068d48ecc1ede2811ac455372395d382ba76`  
**F07 PR:** #56 — Draft / Open / Unmerged  
**F07 exact-head CI:** #41568715178 — both required jobs SUCCESS

## Mission
Project G is the final controlled integration, full-system regression, real-device acceptance, and release decision stage for Glass TV Home. Its purpose is to prove that individually audited modules work together on the target Android TV device without regressions, crashes, freezes, corruption, or unacceptable performance/storage behavior.

## Entry baseline
- F01–F07 host/source exit gates are closed PASS.
- F07 CI: Python 99/99; launcher and installer unit tests; release build; APK diagnostics and size enforcement all PASS.
- Diagnostic APK size: 1,571,293 bytes; maximum 1,572,864 bytes; remaining headroom 1,571 bytes. Keep dependency/resource growth tightly controlled.
- No APK artifact was published or installed during F.
- No physical TV testing or actual device performance measurements are claimed.
- Target device baseline recorded in project docs: Mi TV Stick, Android TV 11 / API 30; revalidate model/build/resolution/density at the start of authorized device acceptance.

## Work packages and exit gates

### G01 — Branch, dependency and evidence reconciliation
- Enumerate all relevant feature-owner branches and PRs from the live repository.
- Record exact branch HEAD SHA, PR number, base branch and base SHA, CI run and result, changed paths, merge ancestry and dependency order.
- Compare every branch against its documented owner baseline; detect stale heads, duplicated commits, missing dependencies, unexpected divergence and undocumented changes.
- Preserve owner branches. Do not assume that a previous PR snapshot or old documentation is the live head.
- Output: verified branch/dependency matrix and approved integration order.

### G02 — Controlled integration candidate
- Create/continue a dedicated integration branch, separate from main and feature-owner branches.
- Integrate in dependency order, one controlled group at a time.
- Record conflict resolution and source ownership. If a defect belongs to a feature module, fix it on that owner branch, rerun owner CI, then replay it into the integration candidate.
- No feature-owner branch is deleted, rewritten or silently retargeted.
- Output: exact integration candidate SHA and auditable change ledger.

### G03 — Complete integrated source/CI audit
- Review the entire combined diff, not just individual PR summaries.
- Cross-check shared contracts: navigation/focus, global dialog styling, lifecycle, QR endpoints and upload reservations, avatar/wallpaper/StandBy persistence, localization, app-lock/kids mode, weather/information widgets, launcher/device controls, installer/update behavior, resource ownership and cache/storage boundaries.
- Run all Python contract suites, Android launcher and installer unit tests, Robolectric tests, resource/localization checks, manifest/security/export checks, release build and APK-size gate on the exact candidate SHA.
- Test failure, cancellation, stale callback, low-storage, offline/reconnect, process death and malformed/corrupt input paths where covered by host tests.
- Output: exact-head regression report; all reproducible source/CI findings repaired or explicitly blocked with owner and reason.

### G04 — Device-test readiness gate
- Confirm G01–G03 are complete and candidate SHA is frozen.
- Produce one clearly labelled test APK tied to that exact SHA only after source/CI gates pass.
- Present device-test scope, risk, backup/rollback approach and expected changes.
- Obtain explicit user approval before installation, changing HOME/default launcher, ADB settings, package state or user data.
- No approval means no installation and no device mutation.

### G05 — Mi TV Stick physical acceptance and soak
Record evidence, not impressions:
- Device identity, Android version/build, display resolution/density and available storage.
- Cold start, warm start, Home return, app switching, repeated Home/Back, D-pad navigation, focus retention and dialog open/close.
- Device settings, app permissions, launcher selection, app lock, kids mode and power/restart behavior.
- QR phone-to-TV over the same LAN: pairing, upload, retry, cancel, expiry, concurrent requests, disconnect/reconnect, low storage and cleanup.
- Avatar, wallpaper, wallpaper rotation/shuffle, corrupt/large images, 1080p/4K rendering and persistence after force-stop/process death/reboot.
- Weather animations (all conditions), reduced-motion/low-spec mode, Now Playing, StandBy/DreamService and photo rotation.
- Crash/ANR/logcat, frame time/FPS, CPU, memory (PSS/RSS/heap trend), thermal behavior, storage growth and long-run soak.
- Recheck recovery and data integrity after every failure scenario.
- Output: reproducible device test log, measurements, screenshots/logcat where appropriate, and defect list.

### G06 — Final decision, merge and release
- Reconcile all device and integrated regression findings; route source defects to owners and rerun exact-head CI.
- Confirm docs, inventory, decision log, audit register, PR status and branch ancestry match the final candidate.
- Present final acceptance report and residual risks.
- Merge to main, publish release APK or alter protected branch/release state only after explicit user approval.
- Output: signed-off release baseline and complete handover.

## Non-negotiable controls
1. D-013 remains binding: no early merge; owner branches preserved; integration staged and auditable.
2. No APK installation, HOME-default change, ADB/device setting change, package-state mutation or user-data change without explicit readiness approval.
3. Do not mark a physical test PASS based on unit tests, screenshots from source previews, or a successful build.
4. Do not claim zero defects absolutely; report the scope, evidence and limitations of every pass.
5. Keep APK under the 1,572,864-byte budget; current F07 headroom is only 1,571 bytes.
6. Use Trigger → Input → Verification → Work → Decision Point → Output → Quality Check → Final/Not Final.
7. End every session by syncing HANDOVER, OPERATIONS_LOG, MASTER_PROJECT, INVENTORY, DECISION_LOG, AUDIT_REGISTER and roadmap.

## Current decision
Project G is ready to begin at G01. This checkpoint does not authorize merging, producing/installing a TV-test APK, or modifying the physical TV. The immediate next action is live repository reconciliation of all owner branches and PR dependencies against the frozen F-series evidence.


## Superseding roadmap/status correction — 5 November 2026

This readiness document contains an earlier work-package numbering (G03 integrated source audit, G04 readiness, G05 device acceptance, G06 release). That sequence is historical and is **superseded** by the subsequently approved official five-stage Project G roadmap recorded in INVENTORY.md and HANDOVER.md. Do not use the older numbering to determine the current stage.

**Binding current sequence:**
1. G01 — Penyelesaian Bug Terakhir / branch reconciliation (HOST/SOURCE PASS).
2. G02 — Build APK Final yang Sudah Teruji (HOST BUILD + ARTIFACT PASS).
3. G03 — Uji Instalasi pada Mi TV Stick (current; preflight verified, physical acceptance not run).
4. G04 — Finalisasi Dokumentasi GitHub.
5. G05 — Finalisasi Memory dan Handover ChatGPT.

**Current G03 evidence:** live integration SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d`; G02 run #1185 both required jobs SUCCESS; artifact #13210715335 is available until 10 Nov 2026 06:37:50 UTC; downloaded artifact digest and extracted APK SHA-256 match the G02 canonical record; APK archive integrity PASS. No physical Mi TV Stick test or installation has been performed.

**Current G03 blocker:** no connected Mi TV Stick/ADB/signature-verification tools in the execution host. Explicit user readiness approval remains required before installation, package replacement, HOME/default launcher change, ADB setting, permission or data mutation. G03 remains OPEN/BLOCKED / NOT FINAL. See [G03_DEVICE_ACCEPTANCE_PREFLIGHT_2026-11-05.md](G03_DEVICE_ACCEPTANCE_PREFLIGHT_2026-11-05.md).

Preserve the original historical work-package descriptions above for audit traceability; this addendum is the current controlling roadmap/status.
