> **CANONICAL CHECKPOINT — READ THIS FIRST**  
> The only authoritative restart point is [GTVH-CHECKPOINT-LATEST.md](GTVH-CHECKPOINT-LATEST.md) on branch `docs/project-master-inventory`. Start every continuation there; do not resume from historical checkpoint blocks, old PR descriptions, or branch-local notes. This file is supporting detail/history and must not establish a competing current state.  
> **Checkpoint authority:** GitHub repository `Frangga11/glass-tv-home`; canonical docs branch `docs/project-master-inventory`; single entry file `docs/project-management/GTVH-CHECKPOINT-LATEST.md`. The latest checkpoint supersedes all older status statements below.  

---

# 03 — Decision Log

**Version:** 0.1-draft  
**Date:** 31 October 2026

## 1. Confirmed project decisions

| ID | Decision | Source | Status |
|---|---|---|---|
| D-001 | GitHub is the canonical source for project documentation; ChatGPT Library is the backup archive | User explicitly selected “both” | APPROVED |
| D-002 | Keep documentation modular; do not create one giant document | User's established documentation preference | APPROVED |
| D-003 | Keep profile avatar, home wallpaper, and standby/screen-saver photo collections as separate features and data stores | Product architecture / explicit clarification in current work | APPROVED |
| D-004 | Do not call a feature final solely because code is present; distinguish code inspection, CI/build, and real-device test | User's quality expectations and current audit protocol | APPROVED |
| D-005 | Maintain a separate documentation branch rather than mixing docs with application feature changes | Documentation workflow design | APPROVED FOR THIS DOCUMENTATION PASS |
| D-006 | Avatar photo upload must use a phone-scanned QR code; accept one image per session and delete the previous avatar only after the new file and profile reference are successfully committed | User explicitly requested QR upload and no retained old photo | APPROVED |
| D-007 | Project documentation uses three connected layers: Master, Audit/SOP, and Operational Log; one authoritative home per fact, minimal cross-links, GitHub canonical and ChatGPT Library milestone backup | User explicitly approved the efficient three-layer documentation approach on 31 October 2026 | APPROVED — STANDARD |

## 2. Baseline technical facts

- App ID: `com.gathin.tvhome`.
- Gradle version: 4.0.0 / versionCode 40.
- Android build configuration: minSdk 26, compileSdk 34, targetSdk 34, Java 17.
- User target: LG 32-inch display driven by Mi TV Stick, Android 11 / API 30 (must be revalidated before release).
- Main code branch under review: `feature/settings-reorganization-standby-gallery`.
- Avatar upload candidate: `fix/avatar-photo-upload-audit`.

## 3. Decisions still open

| ID | Question | Proposed direction | Status |
|---|---|---|---|
| O-001 | Avatar upload method | QR upload from phone on the same LAN; Android picker removed from active flow | DECIDED — QR ONLY |
| O-002 | Should a new profile be able to upload its photo during profile creation? | Create profile first, then select it and upload from Profile settings; inline creation upload can be considered later | DEFERRED |
| O-003 | Should custom avatars appear everywhere (top bar, settings card, switcher, add/edit dialogs)? | Use one profile image resolver across top bar, Settings sidebar/list, and profile switcher; add-profile starts with built-in avatar and can upload after creation | IMPLEMENTED IN PR #9; CI #208 PASS; DEVICE TEST OPEN |
| O-004 | Exact final order and density of information tabs | Decide after screenshot / remote navigation review | OPEN |
| O-005 | Default wallpaper: fixed image or rotating selection | Await asset/behavior audit and user confirmation | OPEN |
| O-006 | Icon animation: always animated or only on focus / interaction | Prefer focus-driven, restrained animation pending performance test | OPEN |
| O-007 | Weather/AQI location and update policy | Confirm precise chosen location and source | OPEN |
| O-008 | Prayer time zone/source and azan behavior | Confirm regional zone and enabled audio policy | OPEN |

## 4. Operational history

Chronological implementation and verification events are recorded once in [OPERATIONS_LOG.md](OPERATIONS_LOG.md). Detailed findings remain in the corresponding audit file. Do not duplicate operational history here.


## 5. Profile security and avatar integration decisions — 31 October 2026

- Parent PIN remains device-wide; Child Mode, locked apps, hidden apps, and avatar photo path are profile-scoped.
- Switching away from any active Child Mode profile requires parent PIN, including child-to-child. Adult-to-child and adult-to-adult switching do not require PIN.
- Parent PIN is required before editing locked/hidden app lists and before profile deletion; deletion retains a separate destructive confirmation.
- User explicitly keeps default PIN `0000` temporarily and will change it later. Do not force a first-run PIN change in this scope.
- User explicitly requires unlimited wrong PIN attempts; no cooldown, lockout, or retry limit.
- Avatar QR session: selected LAN IPv4, random token, 5-minute expiry, one successful upload, 15 MB maximum, 80 MP/12,000 px image bounds, app-private storage.
- Draft PR #8 → Draft PR #9 is the intended review/merge order after actual-device acceptance. Do not merge source PR #6/#7 directly.


## 6. Approved bug repair roadmap — 1 November 2026

| ID | Decision | Source | Status |
|---|---|---|---|
| D-008 | Adopt `BUG_REPAIR_ROADMAP.md` (GTVH-BRR-001 v1.0) as the official post-test repair sequence: A preparation/audit, B global foundations, C communication, D state/persistence, E specific modules, F integration/testing, G finalization/release. GitHub remains canonical; ChatGPT memory is the continuity/handover index. | User explicitly approved the A–G roadmap on 1 November 2026 | APPROVED |

Execution starts at A04. A01–A03 are complete planning tasks; no code repair or repository source audit has begun under this roadmap. Changes to order require a recorded technical dependency/root-cause rationale and user-facing checkpoint.


## 7. B01 execution sequencing — 1 November 2026

| ID | Decision | Source | Status |
|---|---|---|---|
| D-009 | At the user's explicit instruction to begin B01, source audit and a narrowly scoped fix were started before A05 tested-APK identity and A06 full baseline/backup were complete. To contain risk, work is isolated on a new branch created from the verified PR #9 exact head; no existing branch was overwritten, no PR merged, and no TV/device setting changed. A05 and A06 remain mandatory before installation/release acceptance. | User instruction to begin B01; verified GitHub repository tree, PR #9 head, and dedicated B01 branch | APPROVED EXECUTION EXCEPTION; A05/A06 OPEN |


## 8. No TV testing or candidate APK until audit completion — 1 November 2026

| ID | Decision | Source | Status |
|---|---|---|---|
| D-010 | Do not install or physically test any candidate APK while source audits and repairs are ongoing. Complete all A–E audit/fix work and F01–F07 integration/regression first. Only then produce and publish one final TV-test APK after explicit readiness approval. CI may compile the release variant for source validation, but normal PR CI must not publish a TV-test artifact. | User's explicit instruction in conversation, 1 Nov 2026 | APPROVED — binding workflow rule |


## 9. Lean Engineering operating model — 1 November 2026

| ID | Decision | Source | Status |
|---|---|---|---|
| D-011 | Adopt Lean Engineering for execution of the approved GTVH-BRR-001 A–G roadmap. Preserve the roadmap, all existing branches/PRs/commits, source and audit evidence, and the three-layer documentation standard. Eliminate duplicate audits and unnecessary paperwork; use risk-based verification, one active work unit at a time, and milestone-based checkpointing. Do not weaken required CI, regression, physical-device, safety, or release acceptance gates. | User explicitly selected Lean Engineering on 1 November 2026 | APPROVED |

### Operating rules
- Existing verified evidence is reusable only for the exact source commit and unchanged environment/scope; otherwise perform the smallest relevant re-verification.
- Keep one primary active work unit. Other work may remain open only when a real dependency or independent review requires it.
- A finding has one ID and one authoritative audit record. Other records link to it.
- Batch documentation updates at work-unit closure; update only affected records.
- Use L1/L2/L3 risk-based verification, but never skip mandatory roadmap stage gates or user-approved device/release restrictions.
- Preserve Draft/unmerged PRs and do not publish/install a candidate APK before all A–E work and F integration/regression gates and explicit readiness approval.



## 10. Permanent merge and quality gate policy — 1 November 2026

| ID | Decision | Source | Status |
|---|---|---|---|
| D-012 | Adopt GTVH-MQP-001 v1.0 in `MERGE_QUALITY_POLICY.md` as the mandatory merge and bug-fix standard. Use a two-level model: stage PRs merge to rolling `integration/b-series` only after all applicable audit, regression, static QC, test, and exact-head CI gates pass; merge to `main` only after full integration/regression, final acceptance, release review, and explicit user approval. For future bugs, use a risk-based impact map and scoped dependency audit, expanding to app-wide review only when shared foundations or evidence require it. Missing/stale evidence is not a pass. | User explicitly approved the proposed merge policy on 1 November 2026 | APPROVED — ACTIVE |

Implementation checkpoint: `integration/b-series` created from `d7b76c4a22c9eadffb8171a616283f3ad647d488`; existing B06 feature branch shares that base and is to target this integration branch. This does not mean B06 implementation is complete.


## 11. Deferred grouped merge for B06–G — 1 November 2026

| ID | Decision | Source | Status |
|---|---|---|---|
| D-013 | Starting with B06, defer all stage merges while A–G audits and repairs remain active. Keep work isolated and traceable in dependency-ordered feature branches/Draft PRs; continue source audits, regression tests, static QC, builds, and exact-head CI during development. After all planned source work is audited and repaired, freeze exact branch heads, audit the combined diff, then merge into `integration/b-series` by coherent dependency groups with CI and cross-group regression after each group. Complete final integrated acceptance, obtain explicit approval, merge to `main`, and build the final release APK from the exact approved `main` commit. A separate labelled test candidate may be needed for physical acceptance; it is not the final release APK. This supersedes D-012's rolling stage-merge timing for B06 onward; all quality gates and bug-audit rules remain active. | User explicitly directed that merging begin only at the end, in groups, after C–G may have introduced further changes | APPROVED — ACTIVE |



## 12. Cross-project coordination, integration, and product-quality principles — 1 November 2026

| ID | Decision | Source | Status |
|---|---|---|---|
| D-014 | Adopt the Cross-Project Coordination, Integration & Product-Quality Standard for all A–G work. Projects may be developed in isolated branches and work units, but they serve one roadmap, one coherent architecture, and one final product objective: a smooth, stable, complete Glass TV Home experience in which every feature and control works as intended. Each project retains a clear primary tupoksi to prevent duplicated or uncoordinated work; **these boundaries are coordination/ownership rules, not barriers to auditing, reporting, coordinating, or fixing cross-project defects**. Any mismatch, broken behavior, missing integration, regression, inconsistent data contract, or nonfunctional feature/control found across a project boundary must be investigated to root cause, assigned to the correct component owner, coordinated with affected projects, repaired where appropriate, and re-verified end-to-end. Preserve approved data, decisions, functionality, source history, and audit evidence; never silently omit, overwrite, reinterpret, or abandon prior work. Record material changes with rationale, impact, owner/project boundary, verification, and traceable evidence. Maintain synchronization through explicit dependency contracts and checkpoints. Before and during integration, reconcile cross-project interfaces and shared behavior; freeze exact heads before grouped integration, audit the combined diff, resolve conflicts, and run exact-head CI plus relevant functional, integration, and regression checks. Independent project builds are not proof that the whole application works. | User explicitly clarified on 1 November 2026 that cross-project synchronization must trigger audits and repairs when anything does not fit; all projects must form one mutually completing product and every feature/control must work as intended. | APPROVED — BINDING FOR A–G | 

### Mandatory operating rules

- **One product direction:** A–G follow the approved roadmap and common Glass TV Home objective. No isolated project may redefine the overall architecture or acceptance target unilaterally.
- **Clear ownership, no rigid silos:** Every work unit has a defined primary scope, inputs, outputs, dependencies, and completion evidence. Do not silently take over another project's work or duplicate its implementation. However, anyone conducting an audit may inspect relevant cross-project behavior, raise findings, trace root causes, and coordinate a fix with the responsible owner. A tupoksi boundary must never be used to leave a known defect or incompatibility unresolved.
- **Cross-project repair duty:** When a finding spans project boundaries, document the observed behavior, reproduction/evidence, affected projects and interfaces, suspected/root cause, responsible implementation owner, and verification plan. Fix the component that owns the cause; update dependent contracts/documentation and notify affected projects. If the correct owner or scope is uncertain, investigate and agree ownership at a checkpoint rather than ignore the finding.
- **Data and decision integrity:** No silent deletion, omission, overwrite, or abandonment of existing data, functionality, decisions, branches, commits, or audit findings. If removal or replacement is technically necessary, document the reason, impact, migration/retention plan, and approval where required.
- **Dependency discipline:** Work in the approved sequence while remaining responsive to dependencies discovered by audit. Consume verified outputs/contracts of prerequisite projects; communicate interface or behavior changes to affected downstream projects before treating them as final. A justified cross-project repair may be scheduled where the root cause resides without changing the overall roadmap order or merging prematurely.
- **Continuous synchronization:** Keep branch/PR status, exact commit heads, audit evidence, CI results, known limitations, ownership, and deferred tests consistent across canonical project records. GitHub documentation remains authoritative; ChatGPT context is a continuity aid, not a substitute.
- **Puzzle-fit integration:** Before combining projects, freeze exact heads and audit the combined diff, shared files, APIs/contracts, data ownership, resources/localization, lifecycle, navigation/focus behavior, and build configuration. Resolve conflicts deliberately; never rely on filename overlap alone or assume independent CI proves compatibility.
- **Feature and control functionality:** Every user-visible feature, action, button, menu, dialog, navigation path, and remote-control interaction must be verified against its intended behavior and relevant states. Check the full path from input → state/action → persistence/network effect where applicable → visible result/error handling. A control that merely renders or responds visually is not considered functional if its intended operation fails.
- **Risk-based quality, not audit theater:** Avoid redundant audits and unnecessary rework, but perform the smallest sufficient audit and quality check whenever evidence indicates a mismatch, defect, regression, or cross-project risk. Expand the scope when shared foundations or root-cause evidence require it. Every change must preserve previously verified behavior or include a justified, tested replacement.
- **Required verification:** Apply relevant functional, integration, regression, persistence/recovery, lifecycle, UI/UX, accessibility/focus/remote, static, build, and exact-head CI checks according to the affected scope. Do not claim tests that were not run. A green build or CI alone is not proof of complete functionality; physical-device tests and release gates remain governed by the approved roadmap and D-010/D-013.
- **Completion evidence:** A project is complete only when its own scope is met, relevant cross-project dependencies fit, required tests and exact-head CI pass, regressions are addressed, and remaining external/device tests are explicitly recorded as pending rather than silently treated as passed.
- **Final acceptance:** The goal is one coherent, stable, usable application whose projects support and complete one another—not a collection of independently passing projects. Do not promise absolute zero defects. Transparently track discovered defects and risks; resolve them or obtain explicit acceptance with rationale and limitations before release approval.



## E05-F04 checkpoint — centered daily-ayah glass reader (2 Nov 2026)
- Feature branch: `feature/e05-information-center-ayah`; PR #38 remains Draft/open/unmerged under D-013.
- Latest feature head: `37dab64cc99f11ef6670840c351f32fe91da6b24`.
- Added a bounded Home ayah preview (two lines per script plus explicit “Baca ayat lengkap” affordance) and a centered full-verse reader. Target geometry is 72% display width × 63% display height (about 45.4% of screen area), using the shared B01 `TvDialogStyle.applyCustom` contract. Full Arabic RTL and selected-language translation wrap and scroll inside the reader; the Home shelf/prayer table do not grow with full verse length.
- D-pad OK/Enter opens; D-pad scrolls; Close, Back/Escape and close-focus transitions are wired. Wallpaper and prayer layout remain visually unverified on physical TV and are Project F acceptance items.
- No qari audio, TTS, audio dependency, permission, or audio network path was added; audio remains out of scope for this roadmap.
- Changed feature files: `MainActivity.java`, `activity_main.xml`, default/ID/EN `strings.xml`, and `E05_WIDGETS_INFORMATION_AUDIT.md`.
- Exact-head Actions run #41427317877 for the feature head is currently in progress; Lists/tools/docs passed, Launcher build/unit tests still running at this checkpoint. Do not claim CI PASS until the run completes successfully.
- No APK publication/installation, physical-device test, merge, or release approval is claimed.
- Next: inspect exact-head CI result; fix any failures; Project F must verify 1920×1080 and 4K sizing, Arabic glyph/harakat integrity, long translation scrolling, wallpaper visibility, prayer timetable stability, D-pad/Back and repeated open/close.


## E05-F04 verification update — 2 Nov 2026
- Implementation head `37dab64cc99f11ef6670840c351f32fe91da6b24` passed exact-head GitHub Actions run #41427317877: launcher build/unit tests/release build/APK-size guard PASS and lists/tools/docs PASS.
- Current PR #38 head `d1d461c2c08609462354ebebd9dc7ce678e2e049` is a report-only documentation follow-up; application source/resources are unchanged from the tested implementation head.
- PR #38 remains Draft/open/unmerged. Physical Mi TV Stick/4K UI and remote acceptance remain Project F; no APK install or device test is claimed.


## F02 system-validation decisions — 3 November 2026

| ID | Decision | Rationale / boundary | Status |
|---|---|---|---|
| F-014 | Keep Project F and Project G strictly separated: F uses source audits, host-side tests, CI and integrated static validation; physical TV installation/launch/render/focus/network acceptance belongs to G. | Prevent a premature device test from masking missing source/contract defects; device-dependent cases remain DEFERRED_TO_G, never PASS. | APPROVED / ACTIVE |
| F-015 | The exported LocationBroadcastReceiver requires `android.permission.DUMP`, while preserving its two existing actions and documented ADB shell/system caller route. | Ordinary third-party application UIDs must not be able to change persistent location or test-weather state. Direct third-party assistant apps are not authorized by this contract; any such bridge needs a separate authenticated design. Actual shell permission compatibility is deferred to G. | IMPLEMENTED / OWNER + F02 EXACT-HEAD CI PASS / G03 ADB-SHELL ACCEPTANCE PENDING |
| F-016 | AzanActivity is app-internal and must be declared `android:exported="false"`. | Verified app-owned explicit Intent/PendingIntent route through AzanReceiver and Azan.showFullScreenNotification; no external direct-launch contract found. | IMPLEMENTED / OWNER + F02 EXACT-HEAD CI PASS / G03 REBOOT ACCEPTANCE PENDING |
| F-017 | MainActivity's `styleGlassDialog` entry point delegates to B01's shared `TvDialogStyle` contract; sized overload preserves bespoke dimensions after applying common surface/dim/viewport rules. | Avoid parallel dialog implementations; preserve custom geometry and intentional anchored/transparent surfaces as documented exceptions. | IMPLEMENTED / B01 + F02 EXACT-HEAD CI PASS / G03 TV RENDERING PENDING |
| F-018 | Do not close an F02 finding on code commit or build success alone. | Require source-owner exact-head CI, F02 exact-head CI, regression coverage, cross-branch diff review, and explicit Project G deferral for device-only acceptance. | APPROVED / ACTIVE |

All related PRs remain Draft/open/unmerged. No APK publication, installation, or physical device test is performed during F02.


## F02 follow-up decisions — 3 November 2026

| ID | Decision | Rationale / boundary | Status |
|---|---|---|---|
| F-019 | B01 HomeKeyService must preserve single Home, double Home Recent Apps, double Home Close All, and long-press Settings as distinct gestures. | HomeKeyService consumes key events; immediate single-Home handling previously prevented the documented double-press gesture. Use a 350 ms double threshold and 400 ms delayed single action; retain disabled KEYCODE_APP_SWITCH policy. Project G verifies real remote timing. | IMPLEMENTED / B01 + F02 EXACT-HEAD CI PASS / G03 REMOTE TIMING PENDING |
| F-020 | LocationActivity is internal-only and declared non-exported. | No intent filter or external caller contract; MainActivity retains explicit in-app launch. | IMPLEMENTED / D01 CI PASS |
| F-021 | Keep RecentAppsActivity's exported ACTION_MAIN entry pending external contract verification; do not remove it blindly. | No in-repository external caller found, but it may be a legacy OEM/assistant/remote entry. Project G must verify; if unused, harden to non-exported. | SUPERSEDED BY F-024 / NO SEPARATE ACTION |
| F-022 | Use Robolectric host tests for actual AlertDialog Window attributes, in addition to source-level guards. | Improves CI proof of centering, dimming and viewport sizing without pretending to validate TV firmware/remote rendering. | IMPLEMENTED / F02 ROBOLECTRIC + EXACT-HEAD CI PASS / G03 TV RENDERING PENDING |


## F02 follow-up decisions — 3 November 2026

| ID | Decision | Rationale / boundary | Status |
|---|---|---|---|
| F-023 | Force Stop orchestration must use a dedicated Handler, separate from Home gesture and power-action callbacks. | `removeCallbacksAndMessages(null)` on a shared Handler cancels unrelated navigation/timeout work. | IMPLEMENTED / B01 + F02 EXACT-HEAD CI PASS / G03 DEVICE ACCEPTANCE PENDING |
| F-024 | RecentAppsActivity is internal-only; remove its exported MAIN entry. | Exported `ACTION_CLOSE_ALL` invoked destructive app-stop behavior and could be explicitly launched by another installed app. Preserve explicit in-app HomeKeyService route. | IMPLEMENTED / B01 + F02 MANIFEST REGRESSION + EXACT-HEAD CI PASS / G03 REMOTE ACCEPTANCE PENDING |


| F-025 | Restart requests must use the existing visible system power-menu selection path; never treat AccessibilityService global action ID 12 as reboot. | ID 12 is the accessibility-button chooser, not reboot. Preserve existing vendor-label matching and timeout fallback. | IMPLEMENTED / B01 + F02 EXACT-HEAD CI PASS / G03 OEM POWER-MENU ACCEPTANCE PENDING |


| F-026 | Wallpaper QR-session management mutations (select/delete/slideshow/clear) must be serialized with server shutdown and recheck session validity inside the same monitor. | Prevent an already-dispatched request from mutating wallpaper state/files after QR-session revocation; preserve existing API routes and UI. | IMPLEMENTED / C07 + F02 EXACT-HEAD CI PASS / G03 PHONE-LAN ACCEPTANCE PENDING |


## F02 continuation decisions — 3 November 2026

| ID | Decision | Rationale / boundary | Status |
|---|---|---|---|
| F-027 | Preserve the D05-aware StandBy upload result adapter in F02; do not replace it with C07's older boolean-based handler. | D05 is downstream of C07 and changes `StandbyPhotoStore.addUploadedFile` to return `UploadResult`; F02 must preserve both atomic persistence semantics and C07 session resilience. | CONFIRMED / INTEGRATED |
| F-028 | Keep the upload deadline regression assertion at 3500 ms for deadline 6000 ms and fake monotonic time 2500 ms. | The production calculation was correct; the 3000 ms expected value was arithmetic error in the test. Do not weaken or remove the test. | CORRECTED / C07 + F02 EXACT-HEAD CI PASS |


## F02 follow-up decisions — 3 November 2026

| ID | Decision | Rationale / boundary | Status |
|---|---|---|---|
| F-029 | AccessibilityService components must be exported for Android system binding and protected by `BIND_ACCESSIBILITY_SERVICE`. | InputKeyService was non-exported, preventing the system from binding it; exported=true does not allow ordinary third-party binding while the system-only permission remains. | IMPLEMENTED / E01 + F02 HOST CI PASS / DEVICE ACCEPTANCE G |
| F-030 | Treat APK size headroom below 8 KiB as a monitored build-budget risk; do not remove fonts or default wallpaper without measured, quality-safe optimization. | Latest diagnostic is 1,564,977 bytes against 1,572,864-byte cap (7,887 bytes headroom); R8 minify and resource shrinking are already enabled. | WITHIN BUDGET / MONITOR |


## F02 upload-session and storage safety decisions — 3 November 2026

| ID | Decision | Rationale / boundary | Status |
|---|---|---|---|
| F-021 supersession | The earlier provisional instruction to retain RecentAppsActivity's exported MAIN entry is superseded by F-024. | F02 confirmed the exported activity accepted a destructive ACTION_CLOSE_ALL path; it is now internal-only, with explicit in-app HomeKeyService routing preserved. | SUPERSEDED BY F-024 / IMPLEMENTED |
| F-031 | Reserve upload storage atomically across concurrent Wallpaper and StandBy QR transfers and preserve a 64 MiB free-space margin. | Per-request checks alone can overcommit a shared free-space snapshot; reservations are released in all completion/failure/cancellation paths. Keep existing per-file and StandBy collection limits; do not impose an arbitrary total wallpaper cap. | IMPLEMENTED / C07 + F02 EXACT-HEAD CI PASS / G03 LOW-STORAGE ACCEPTANCE PENDING |
| F-032 | Protect both Wallpaper and StandBy upload commit critical sections with explicit regression tests. | The production code already serialized final session checks and persistence with stop(); the earlier test did not independently guard both upload commit paths. | IMPLEMENTED / C07 + F02 EXACT-HEAD CI PASS / G03 DEVICE ACCEPTANCE PENDING |


## F02 final disposition — 3 November 2026
| Decision | Status |
|---|---|
| F-031 process-wide QR upload storage reservation | OWNER + F02 HOST VERIFIED — C07 #41521013896; F02 #41521145454 |
| F-032 Wallpaper/StandBy commit-vs-stop atomicity regression coverage | OWNER + F02 HOST VERIFIED — C07 #41521013896; F02 #41521145454 |
| D01 exported receiver authorization + internal-only LocationActivity | OWNER + F02 HOST VERIFIED — #51 run #41506847521; mirrored in F02 #41521145454 |
| F02 integrated host validation | PASS — exact head `e190e2863a4aacbe3aba61094fab00572f4b469f`, run #41521145454 |
| Physical Android TV / phone / LAN / ADB / OEM acceptance | DEFERRED_TO_G — cannot be truthfully closed by host CI |
| APK artifact publication / installation in F02 | NOT PERFORMED — by approved policy |

The remaining Draft/open PRs are intentional staged source/integration records pending Project G final audit and merge. They are not being handed to G as unresolved F02 code defects.


## F02 final exact-head CI evidence — 3 November 2026
- F02 head `190b698453a96da8ec2db31da2eefac836849a10` passed run #41522215859; both jobs SUCCESS, 42 Python tests / 1 intentional merge-time README count skip, Android unit tests and release build passed, APK 1,565,713 bytes with 7,151 bytes headroom; no artifact.
- C07 owner #24 and D01 owner #51 exact-head CI passed. Host/source exit criteria are complete; physical acceptance is DEFERRED_TO_G.


---

## CHECKPOINT RESMI — PROJECT F02 DITUTUP (3 November 2026)

**ID:** GTVH-F02-CP-2026-11-03  
**Keputusan:** F02 HOST VALIDATION = PARIPURNA / HOST EXIT PASS.  
**Batas:** keputusan ini tidak menyatakan penerimaan perangkat fisik atau merge final.

### Bukti final dan verifikasi dokumentasi
- Integrasi F02: branch `integration/project-f02-system-validation`, PR #50, head final saat checkpoint `2ede452b3137580bd51088661f80b6d0eeac7f4b`; CI #41523201383 — kedua job wajib SUCCESS pada exact head tersebut.
- Job Lists/tools/docs: SUCCESS. Python: 42 test, 1 skip disengaja untuk `test_readme_counts` (normalisasi hitungan README saat merge dan tetap strict pada main). Tidak ada kegagalan atau test perilaku aplikasi yang dilewati.
- Job Launcher build/unit tests: SUCCESS. Android `testReleaseUnitTest` dan `assembleRelease` berhasil.
- APK diagnostik: 1,565,713 byte dari batas 1,572,864 byte; sisa 7,151 byte. Size gate SUCCESS. Daftar artifact workflow kosong; APK tidak dipublikasikan maupun dipasang.
- CI ini juga memverifikasi perubahan dokumentasi final di branch integrasi; tidak ada pekerjaan CI tertunda untuk head #50 tersebut.
- C07 owner PR #24: head `442b78c68b1540394d543bab59bddf52f865ccf0`, CI #41521013896 kedua job SUCCESS.
- D01 owner PR #51: head `66352838e6155a42a9ea61477643a238f360434f`, CI #41506847521 kedua job SUCCESS.
- Status PR saat checkpoint: #50, #24, #51 semuanya OPEN / DRAFT / UNMERGED. Ini disengaja sesuai tata kelola integrasi bertahap; bukan defect yang ditinggalkan.

### Cakupan yang dinyatakan selesai
Audit sumber lintas modul, perbaikan temuan pada lingkup F02, sinkronisasi owner branch dan integration branch, regression checks, build/unit tests host, APK size gate, dan rekonsiliasi dokumentasi telah selesai. Tidak ada blocker sumber kritis yang diketahui tersisa dalam lingkup audit F02.

### Wajib dibawa ke Project G — belum diuji dan tidak boleh diberi status PASS
1. Penerimaan Mi TV Stick/firmware nyata: tampilan UI, popup, D-pad/focus, remote Home/double-press/long-press, Recent Apps/Close All, OEM power menu, dan AccessibilityService.
2. Kompatibilitas izin ADB shell, termasuk `android.permission.DUMP`.
3. QR transfer telepon-ke-TV melalui LAN nyata: disconnect, cancel, expiry, retry, concurrency, dan telemetry low-storage.
4. Persistensi wallpaper/avatar/StandBy setelah process death/reboot serta performa dan stabilitas perangkat.

### Keputusan tata kelola dan batas kerja
- PR #50/#24/#51 tidak di-merge pada F02. Project G melakukan final integrated audit, pemeriksaan dependency/branch-head, lalu merge sesuai urutan yang disetujui.
- Tidak ada APK release yang dipublikasikan/dipasang dan tidak ada uji fisik TV pada F02.
- Headroom APK 7,151 byte dicatat sebagai P3 watchlist; jangan mengorbankan font atau wallpaper tanpa optimasi terukur yang menjaga kualitas.
- Checkpoint ini mengungguli catatan historis yang masih menyebut CI/retet pending untuk pekerjaan yang sama. Catatan lama dipertahankan sebagai jejak kronologis, bukan status aktif.

**Status serah terima:** F02 ditutup pada sisi host/source dan dokumen pada exact head #50 telah lulus CI. Kandidat integrasi diserahkan ke Project G untuk final audit, penerimaan perangkat nyata, dan merge — bukan untuk mengulang pekerjaan perbaikan F02 yang telah terverifikasi.


## D-015 — Formal Project F closure and Project G handover (3 November 2026)

- User explicitly approved official closure of Project F and directed synchronized documentation for Project G.
- Project F is CLOSED at HOST/SOURCE EXIT PASS for F01–F07. Real-device and full integrated acceptance are explicitly not implied.
- Project G is authorized to begin final branch/dependency reconciliation, isolated staged integration, integrated regression, gated Mi TV Stick acceptance, and final merge/release review.
- D-013 remains unchanged and binding: no early merge; preserve owner branches; no APK installation/device changes before explicit readiness approval; no final merge/release without explicit user approval.
- Frozen F07 baseline: `integration/project-f07-performance-stability` @ `976e068d48ecc1ede2811ac455372395d382ba76`; PR #56 Draft/open/unmerged; CI #41568715178 both required jobs SUCCESS.


## D-016 — G01 final bug resolution checkpoint (3 November 2026)

- User-approved Project G scope is exactly five stages: G01 final bug resolution; G02 final tested APK build; G03 Mi TV Stick installation test; G04 final GitHub documentation; G05 final ChatGPT memory/handover. The overall A–G roadmap remains 47 items, unchanged.
- G01 cross-audited the remaining high-risk source contracts and their owner branches against F02/F06/F07 integration evidence. Root causes, owner fixes, regression tests, exact heads and CI run IDs are recorded in G01_FINAL_BUG_RESOLUTION_AUDIT.md.
- B01 PR #10, C07 #24, D05 #29, D06 #30, E01 #34, E08 #41, F02 #50, D01 #51, F06 #55 and F07 #56 exact-head runs all have both required jobs SUCCESS. F02 #41523201383, F06 #41566633872 and F07 #41568721270 are the latest recorded integration gates.
- No new reproducible host/source defect was found; no speculative production change was made. Stale “CI pending” labels for findings verified by these exact-head runs are superseded by the G01 audit. F-021 is superseded by F-024.
- Device-only acceptance remains G03; no APK publication/installation, merge, HOME-default change, ADB/device mutation or TV setting change was performed.


## G01 branch reconciliation addendum — 3 November 2026

Project F is closed at HOST/SOURCE EXIT PASS. G01 defect-resolution audit is PASS, but project-wide branch reconciliation remains OPEN/BLOCKING: current B-series and F07 integration candidates have diverged, with 26 shared-file differences requiring semantic reconciliation. G02 final APK build is blocked until a clean isolated combined candidate passes exact-head integrated CI. No feature PR/main merge, APK publication/installation, or TV state change has occurred.

- Live repository inventory covers PRs #1–#56. Merged history: #1, #13–#15, #32–#33. Closed without merge: #42–#49 and superseded #52. Open/unmerged source/integration PRs: #2–#12, #16–#31, #34–#41, #50–#51, #53–#56.
- F07 chain remains linear from staged-full through #50 → #53 → #54 → #55 → #56. F07 exact head `976e068d48ecc1ede2811ac455372395d382ba76` is 173 commits ahead / 0 behind staged-full `8fe90a9fdd493c5d81ffdacbe16054630a26580a`.
- Current `integration/b-series` is `9070894fa6d9ef8aec034f46dfe777e866709dfe`, not an ancestor of F07. Compare staged-full → B-series: DIVERGED, 60 ahead / 466 behind, 26 changed paths. Compare B-series → F07: DIVERGED, 639 ahead / 60 behind, 123 changed paths. The 26 shared paths are listed in `G01_BRANCH_RECONCILIATION_ADDENDUM.md`.
- Targeted F07 checks are positive for HomeKeyService, C07 monotonic deadline/session/storage safeguards, manifest security declarations, dialog and upload regression tests. But source hashes differ on several owner/integration files, including manifest, RecentAppsActivity, WallpaperUploadServer, AvatarUploadServer, MainActivity, locale resources and ProfileManager; these require 3-way semantic reconciliation, not blind overwrite.
- G01 defect-resolution subgate PASS; G01 project-wide combine/reconciliation OPEN/BLOCKING. G02 is not ready. No feature PR or main branch merged; D-013 remains binding.
- Next work: freeze all required owner heads; reconcile B-series with F07 on an isolated candidate; run exact-head integrated CI and cross-group regression; only then build the G02 candidate APK. G03 physical acceptance remains separate.


## D-017 — G01 full branch reconciliation is a pre-G02 gate (3 November 2026)

- User requested a fresh project-wide integration/synchronization audit and asked whether combine/merge is required before final APK build.
- Decision: yes, a coherent combined integration candidate is mandatory before G02. It must be built on an isolated branch and pass exact-head integrated CI plus cross-group regression. This is not a merge to `main`.
- Live GitHub inventory #1–#56: merged #1/#13–#15/#32–#33; closed without merge #42–#49 and superseded #52; open/unmerged #2–#12/#16–#31/#34–#41/#50–#51/#53–#56.
- F07 is linear from F-staged-full (173 ahead/0 behind), through #50→#53→#54→#55→#56. However, `integration/b-series` @ `9070894fa6d9ef8aec034f46dfe777e866709dfe` is not in that lineage. It diverges from F-staged-full/F07; compare evidence and 26 shared paths are recorded in `G01_BRANCH_RECONCILIATION_ADDENDUM.md`.
- G01 source-defect audit PASS does not close project-wide G01. Branch reconciliation is OPEN/BLOCKING until all owner heads are frozen and included/superseded/excluded with evidence, B-series/F07 are semantically reconciled on an isolated candidate, and exact-head integrated CI/regression pass.
- No feature PR or `main` merge was performed. Owner branches and current integration branches remain unchanged. No APK publication/installation or device setting change occurred.
- D-013 remains binding: preserve owner branches, merge in coherent dependency groups only after audit, do not merge to `main` or release without final acceptance and explicit user approval.


### PR #57 integration conflict gate — 3 November 2026

- Draft PR #57 opened from `integration/project-f07-performance-stability` @ `976e068d48ecc1ede2811ac455372395d382ba76` into `integration/b-series` @ `9070894fa6d9ef8aec034f46dfe777e866709dfe`.
- GitHub reports `mergeable=false`; PR #57 is OPEN/DRAFT/UNMERGED. Compare view: 639 commits / 123 changed files. Do not merge or force-push around the conflict.
- The 26 shared B-series/F-staged-full paths require three-way semantic resolution. No conflict was resolved by wholesale side selection. Owner branches remain unchanged.
- G01 overall remains OPEN/BLOCKING for branch reconciliation; source-defect subgate remains PASS. G02 APK build is blocked pending clean combined candidate and exact-head integrated CI. `main` is untouched.

## D-018 — G01 project-wide coverage re-audit and second isolated merge (3 November 2026)

- User directed a fresh audit of whole-project coverage, then conflict resolution, then a complete audit of the combined result before G02.
- Reopened G01 because current E05 owner changes were not fully represented in the previous combined tree and the E08 deletion-failure regression test was absent.
- Decision: restore E05 location-scoped weather/AQI cache ownership and API-backed weather text; preserve F07 reduced-motion/low-memory behavior and F06 localized high/low labels; restore E08 delete-failure regression coverage.
- PR #59 was merged only into isolated `integration/b-series` at `562cde0751de93748eb4e0d4cdece4fa1672ac4d`. Tested head `9f2da8f39a20c8e8cc43ac0ff7e42d7a82b34676` and merge commit have identical trees (zero file delta).
- Exact tested-tree CI #41586223809 / run #1177 passed both jobs: 109 Python tests / one intentional skip; Android launcher/installer unit tests and release validation build PASS. APK 1,572,293 bytes / 1,572,864 cap / 571-byte headroom; no artifact/publication.
- G01 is PASS; G02 is READY TO START from the merge commit. G03 physical Mi TV Stick acceptance remains pending. D-013 remains binding: preserve owner branches, no main merge/release or TV installation without explicit approval. `main` remains untouched.


## G01 exact-merge-commit CI gate — 3 November 2026 (latest recheck)

- Rechecked the exact integration merge commit `562cde0751de93748eb4e0d4cdece4fa1672ac4d` after the project-wide coverage audit and semantic conflict reconciliation.
- The tested PR candidate `9f2da8f39a20c8e8cc43ac0ff7e42d7a82b34676` has two completed successful CI runs: #41586223809 (run #1177) and #41586510603 (run #1178). Each run contains exactly the two required jobs, `Lists, tools and docs` and `Launcher build and unit tests`; both jobs concluded `success`.
- Git compare confirms the merge commit is one commit ahead of the tested candidate with zero changed files. This proves identical file tree, but it does **not** prove CI ran on the exact merge commit SHA.
- A direct exact-SHA workflow-run lookup for `562cde0751de93748eb4e0d4cdece4fa1672ac4d` returned no workflow runs; combined commit status returned no status entries. Therefore exact-merge-SHA CI is **NOT VERIFIED** and must not be marked PASS.
- A verification-only Draft PR #60 was created with head SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d` and base at the tested candidate. GitHub reported zero changed files and did not produce a CI run. PR #60 was closed unmerged; no source changes, main merge, APK publication/installation, or device changes occurred. Verification branch `verification/g01-exact-merge-ci-20261103` remains as an audit-only ref.
- The repository integration workflow supports push to `integration/**` and pull-request events, with two required jobs. The GitHub connector available in this session exposes no workflow-dispatch action. Do not invoke the main-branch manual publish workflow: its workflow file includes artifact publication and is intentionally excluded by release-safety policy.
- **Disposition:** coverage re-audit and semantic conflict resolution are complete; combined-tree audit is source/test PASS; candidate CI is PASS; exact merge-commit CI remains OPEN/BLOCKING. G01 is therefore **NOT FINAL**. G02 remains blocked until both required jobs complete successfully on exact SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d` (or a newly designated candidate SHA after any change). Keep owner branches and `main` untouched; no APK publication/installation or TV setting changes.


## Exact-SHA CI execution update — 3 November 2026

- To obtain a push-triggered run on the exact integration merge SHA without changing source or `main`, created audit-only branch `integration/verify-g01-merge-ci-20261103` from tested parent `9f2da8f39a20c8e8cc43ac0ff7e42d7a82b34676` and fast-forwarded it to exact merge commit `562cde0751de93748eb4e0d4cdece4fa1672ac4d` (expected-SHA lease succeeded).
- GitHub Actions CI run #41589530683 / run #1180 is now attached to exact SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- Job `Lists, tools and docs`: SUCCESS.
- Job `Launcher build and unit tests`: IN PROGRESS at last check; `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and size-budget gate have not yet been confirmed complete.
- Workflow artifact list was empty at the last check. No APK publication/installation, main merge, owner-branch change, or TV state change occurred.
- Integration/b-series still compares IDENTICAL to exact SHA `562cde...` (0 ahead / 0 behind / 0 changed files).
- **Current disposition:** G01 source coverage, conflict resolution and combined-tree audit are complete; exact-SHA CI is PARTIAL/OPEN until run #1180's second job succeeds. G01 remains NOT FINAL and G02 remains blocked. Do not freeze or promote the candidate until both required jobs in run #1180 conclude SUCCESS.


## Exact-commit CI final result — 3 November 2026

- Exact candidate SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d`, CI run #41589530683 / run #1180: **SUCCESS**.
- Both required jobs completed successfully: `Lists, tools and docs` — SUCCESS; `Launcher build and unit tests` — SUCCESS.
- Launcher job steps all succeeded: Android `testReleaseUnitTest`, `assembleRelease`, read-only APK diagnostics, and the 1.5 MiB size-budget enforcement.
- Exact APK size: 1,572,293 bytes; budget: 1,572,864 bytes; remaining headroom: 571 bytes. Size gate PASS, but margin is narrow and must be preserved; avoid unmeasured source/resource growth in G02.
- Workflow artifacts: none. APK was not published or installed.
- `integration/b-series` compares IDENTICAL to exact SHA `562cde...` (0 ahead, 0 behind, 0 changed files). `main` and owner branches remain untouched.
- **G01 disposition:** project-wide coverage audit PASS; semantic conflict reconciliation PASS; combined-tree audit PASS; exact-commit CI PASS. G01 HOST/SOURCE gate is now **PASS / READY FOR CHECKPOINT**. G02 may proceed only under the approved sequence, beginning with final APK build from exact SHA `562cde...`; no APK publication/installation or TV setting changes without the explicit G02/G03 approvals. Physical Mi TV Stick acceptance remains G03 and is not claimed by CI.


## G02 final APK build checkpoint — 3 November 2026

- Final source baseline remains exact audited integration tree: `562cde0751de93748eb4e0d4cdece4fa1672ac4d`. Build-only branch `build/g02-final-apk-artifact-20261103` changed only `.github/workflows/ci.yml`; no launcher source, owner branch, `integration/b-series`, or `main` changes.
- Workflow build hardening commit: `1960dfd7fa9dd39e41299ef091ce2bc2cf3dc4ca`. It retries the complete Gradle test/release build once with `--refresh-dependencies` only when recognized transient dependency/repository resolution errors occur; non-network/build/test failures remain fatal.
- CI #1184 (run ID `41590861573`) and CI #1185 (run ID `41590889219`), both on workflow commit `1960dfd7fa9dd39e41299ef091ce2bc2cf3dc4ca`: **both required jobs SUCCESS** in both runs. Unit tests and release build succeeded; APK diagnostics and strict size-budget gate succeeded; artifact upload succeeded.
- Both builds produced APKs of exactly 1,572,293 bytes (budget 1,572,864; headroom 571 bytes). ZIP/APK integrity check passed; manifest, DEX and resources entries present. Extracted APK SHA-256 from selected run #1185: `dc85fcd590eb2c52eb367d0bc9d4f67735cb94ef1cbf6f2b80b86f0de36de0dd`.
- Selected artifact: `glass-tv-home-final-562cde0`, artifact ID `13210715335`, from run #1185; GitHub artifact digest `sha256:669491bfc7c9f5279ecc6c30915e4a340aa492bee21531db203036caf8651c81`; retention expires 10 Nov 2026 06:37:50 UTC. Downloaded and extracted locally for integrity checks. A second successful run #1184 generated the same-size APK with different whole-file SHA; its ZIP entries have identical names and contents. Treat run #1185 artifact as the canonical handoff build.
- Local handoff file: `Glass-TV-Home-G02-Final.apk` (1,572,293 bytes; SHA-256 above). No TV/device installation or physical acceptance testing has been performed. No GitHub Release or public release was created.
- Build-only PR #61 was closed unmerged after artifact retrieval. It must not be merged into integration. `main` and all launcher source branches remain untouched.
- **G02 disposition: HOST BUILD + ARTIFACT PASS.** G03 physical Mi TV Stick acceptance remains pending and is a separate gate. APK signature verification/device compatibility have not been independently established by this host-side artifact check.


## D-019 — Upstream GIF avatar search and Dock capacity adoption (5 November 2026)

- User approved adoption of exactly two upstream capabilities: animated GIF search for profile avatars and configurable Dock capacity 4/5/6 (default 5). All other upstream features remain excluded.
- Upstream audit pinned `aqmangge/glass-tv-home` main HEAD `23064ec34ed597ce8bb044b3732f45be93e38324`.
- GIF source audit found a hard-coded GIPHY API key, raw per-request Threads, silent error swallowing, direct final-file writes without visible atomic/size/type validation, no conservative rating parameter in the inspected query, and a storage path incompatible with E02's hardened `profile_avatars` store. Do not copy or reuse the upstream key or storage path. Require GIPHY attribution and compliant provider configuration.
- Official GIPHY docs checked: Search API requires an API key and client-side search; beta keys are limited to 100 API calls/searches per hour; conspicuous Powered By GIPHY attribution is required. Production implementation must include safe rating, bounded requests/downloads, cancellation/stale-response protection, atomic private-profile storage, and localized failure states.
- Dock source audit found upstream's `setDockColumns` changes both Dock capacity and global grid column count. Target E06 currently also uses one `COLUMNS` value for grid geometry and Dock threshold. Preserve single-row Dock geometry and explicitly account for the effect on app-grid density; verify DPAD, focus, order, alignment and persistence.
- Ownership: GIF search remains in E02; Dock capacity remains in E06. Isolated branches created: `feature/e02-giphy-avatar-search-v1` from `feature/e02-profile-avatar-lock-ui`, and `feature/e06-dock-capacity-control-v1` from `feature/e06-home-screen-subtabs`. Existing owner branches, PRs, integration branch and `main` are unchanged.
- Detailed audit: `UPSTREAM_FEATURE_ADOPTION_AUDIT_2026-11-05.md`; approved scope: `UPSTREAM_FEATURE_ADOPTION_GIF_DOCK.md`.
- Status at this checkpoint: source audit PRELIMINARY PASS; architecture/adaptation contract defined; isolated work branches created; application source implementation, tests and CI NOT YET COMPLETE. No APK publication/installation, merge, or TV setting changes.
- Patent wording means establish original project-specific design/implementation and preserve evidence. No patentability or legal exclusivity is claimed; formal filing requires separate novelty/prior-art and legal review.

## D-020 — GIPHY upstream reuse + Glass TV global UI; Dock orientation modes (5 November 2026)

- User clarified to reuse the upstream developer's GIF system/data source rather than rebuilding it. Source inspection confirms the upstream GIF dialog is a GIPHY API client, not a bundled developer-owned GIF database. Adopt the provider/catalog/data-flow behavior; replace the UI and lifecycle integration with Glass TV global UI, focus/dialog/feedback contracts and Indonesian/English localization. Use E02's existing private avatar storage/transaction. Do not copy the upstream embedded API key or legacy avatar directory; preserve GIPHY attribution and policy requirements.
- User added Dock orientation selection: Vertical or Horizontal, in addition to capacity 4/5/6 (default 5). Vertical is a one-column rail with Up/Down navigation/scroll; Horizontal is a one-row tray with Left/Right navigation/scroll.
- Target E06 currently uses a single RecyclerView/GridLayoutManager and shared COLUMNS for grid geometry, Dock membership, sizing, decoration and focus. Therefore an orientation toggle on the existing layout manager is unsafe. Decision: separate Dock presentation from the main app grid, backed by one authoritative ordered app model; capacity controls visible Dock slots only, overflow remains in the grid, and orientation/capacity do not change app-grid columns or app ordering.
- Simulate the six combinations (2 orientations × 3 capacities) at 1920×1080 and supported density profiles, then validate focus, scroll, reorder, app cards, top-bar transitions, localization, unit/source contracts and exact-head CI. Device acceptance remains separate.
- Status: architecture updated and documented; source implementation and executable visual preview not yet completed. No merge, APK publication/installation, or TV setting changes.


### D-018 — G03 installation and device-mutation readiness gate reaffirmed (5 November 2026)

- G02 candidate artifact #13210715335 is retrievable and integrity-verified; exact extracted APK SHA-256 is `dc85fcd590eb2c52eb367d0bc9d4f67735cb94ef1cbf6f2b80b86f0de36de0dd`.
- G03 physical acceptance requires a connected Mi TV Stick and explicit user readiness approval before APK installation, package replacement, HOME/default-launcher change, ADB setting change, permission change or user-data mutation.
- Proceed autonomously with non-mutating audit, artifact verification, test preparation and documentation. Do not infer device-mutation approval from a general instruction to continue project work.
- G03 remains NOT FINAL until physical evidence and full acceptance matrix are complete.


## D-021 — E02/E06 feature acceptance gates and implementation order (5 November 2026)

- E02 PR #62 exact-head CI #1200 and E06 PR #63 exact-head CI #1198 each passed both required jobs. These are source/build test gates only, not feature-complete acceptance.
- E02 cannot close until animated result previews are rendered; HTTP work is cancellable/disconnected; selection commits are guarded against dialog dismissal/profile changes; GIF drawable scheduling has a reliable stop lifecycle; DPAD, storage rollback, memory, localization, attribution and profile-switch regressions are covered.
- E06 cannot close until Dock presentation is structurally separated from the main app grid. One authoritative ordered app model feeds independent Dock and app-grid adapters; orientation controls Dock row/rail only; capacity controls visible Dock slots only; overflow remains reachable. Settings UI, focus/DPAD/scroll/reorder/restoration and all six orientation/capacity combinations must be validated.
- Existing branches are materially diverged from current integration. Do not merge Draft PR #62/#63 or combine their heads until dependency bases are reconciled and owner-branch fixes are validated.
- Ownership remains E02 and E06 respectively. Preserve source branches and fix regressions on their owning branches. Main/integration remain untouched until approved Project G integration sequence.
- G03 physical Mi TV Stick acceptance is explicitly out of scope until user announces that evaluation phase.
- Full acceptance checklist: [E02_E06_GIF_AVATAR_DOCK_AUDIT_2026-11-05.md](E02_E06_GIF_AVATAR_DOCK_AUDIT_2026-11-05.md).
- Status: **BELUM FINAL**.


## D-022 — UI Refresh P1 inventory disposition (6 November 2026)

- P1 inventory was executed read-only against the locked GTVH-UIR-001 roadmap, live owner PRs/heads, source diffs and exact-head CI.
- Ownership remains E02 for Profile/Avatar/Child Mode and E06 for Home Screen/Dock. B01 shared tokens/dialog/focus contracts remain authoritative.
- Inventory records the current implementation and missing behavior separately. In particular, E06 DockPreferences exists but there is no separate Dock renderer/settings/focus graph; the existing shared COLUMNS coupling remains an architectural blocker.
- E02 open acceptance gaps remain explicit: active HTTP disconnect cancellation, animated-vs-still preview decision, drawable/profile lifecycle and memory tests, QR authorization/permission audit, current GIPHY policy, and profile no-profile/error invariants.
- Decision: P1 inventory may be reviewed for completeness, but it is not marked accepted/closed until the user accepts the document and clarifications. P2/P3 approval remains a hard gate before P4. No code, merge, integration/main, APK publication/install or device action is authorized.
- Evidence: [UI_REFRESH_P1_FUNCTIONAL_INVENTORY_2026-11-06.md](UI_REFRESH_P1_FUNCTIONAL_INVENTORY_2026-11-06.md).


## D-023 — APK size is a diagnostic, not a UI quality gate (6 November 2026)

- User direction: allow APK capacity to increase when the increase materially supports UI quality, accessibility, stability or feature completeness. Do not simplify approved visual treatments or functionality merely to satisfy an arbitrary size limit.
- CI must continue to print the exact APK byte size and provide a warning for unusually large growth, but the historical 1.5 MiB threshold must not fail the build. Unnecessary dependencies/assets should still be reviewed; required fonts, wallpaper, UI effects, accessibility and behavior are not to be stripped for size alone.
- G2 implementation: `.github/workflows/ci.yml` now uses a non-blocking APK size diagnostic.
- G2 exact-head CI #1263 measured 1,573,713 bytes; both required jobs passed.
- Status: **APPROVED**.

## D-024 — Weather V4 uses coordinate-based Open-Meteo/CAMS data, not official station IPU (7 November 2026)

- Retain the existing Open-Meteo Forecast and Air Quality API integration for the Weather V4 dashboard. Air-quality output is labelled US AQI (CAMS model estimate), not official JAS/APIMS IPU or Indonesian ISPU station measurement.
- The UI must show source location, source/update time and per-source stale/offline/unavailable/refresh-failed states. Do not describe a CAMS grid estimate as a ground-station reading.
- Status: IMPLEMENTED / SOURCE-HOST VERIFIED. Physical acceptance remains separate.

## D-025 — Pollutant units and missing-value semantics (7 November 2026)

- PM2.5, PM10, NO₂ and SO₂ remain in µg/m³. The Open-Meteo carbon_monoxide value is converted from µg/m³ to mg/m³ to match the approved G1 V6 presentation.
- A missing or invalid provider pollutant remains unavailable and is shown as an em dash with a provider-missing note; never substitute zero.
- Status: IMPLEMENTED / REGRESSION TESTED.

## D-026 — Cache freshness is coordinate- and source-scoped (7 November 2026)

- A cache can suppress a refresh only when its payload belongs to the currently selected coordinates and is within the freshness interval.
- Legacy JAS/APIMS cache data is not interchangeable with the current CAMS/US AQI contract. It is ignored until a successful CAMS response replaces it; failed refreshes must not relabel legacy data as CAMS.
- Per-source last-success timestamps advance only after a successful response.
- Status: IMPLEMENTED / REGRESSION TESTED.

## D-027 — Weather V4 pollutant breakdown remains individually readable (7 November 2026)

- Follow G1 V6 visual authority: show PM2.5, PM10, CO, NO₂ and SO₂ as distinct metric values rather than compressing them into a single long text line.
- Reuse G2 shared card/material/focus foundations; do not duplicate shared UI primitives or remove existing weather functions.
- Status: IMPLEMENTED / LAYOUT CONTRACT TESTED.

## D-028 — Refresh must preserve D-pad focus and respect Activity lifecycle (7 November 2026)

- Weather UI refreshes on resume and restores the prior hourly/daily focus target when it still exists; if the saved slot disappears, it falls back to the first row.
- Async callbacks must not mutate a finishing/destroyed Activity.
- Status: IMPLEMENTED / REGRESSION TESTED.


## D-029 — G3D Quick Settings V04 implementation boundary (7 November 2026)

- Implement Quick Settings as the approved G1 V6 V04 centered two-column modal, reusing G2 shared dialog, material, typography and focus contracts.
- Preserve existing functional owners for restart/power, language, cache, E08 wallpaper controls, Glass TV Settings and Android Settings. Do not create duplicate system or wallpaper engines.
- Wi-Fi remains an Android-owned status/settings handoff: no fake in-app radio toggle or claim of unsupported system control. G3D provides the nested entry/return contract; the full V05 Wi-Fi redesign remains G3E.
- The optional top-bar network pill must not be a dependency for Quick Settings Wi-Fi status or popup anchoring.
- Asynchronous cache feedback must not retain or update a finishing, destroyed or backgrounded Activity.
- No new permission, manifest change or third-party dependency is justified for G3D.
- G3D source-host closure does not waive physical Android TV/remote/OEM/overscan acceptance. PR #69 remains Draft/unmerged; physical testing and merge require their existing separate approvals.
- Status: IMPLEMENTED / EXACT-HEAD CI VERIFIED; physical acceptance DEFERRED.


## D-030 — G3E Wi-Fi remains an Android-owned network operation surface (7 November 2026)

- Implement the G1 V6 V05 Wi-Fi status, nearby list and best-effort rescan surface; reuse G2 dialog/material/focus contracts and G3D Quick Settings entry/return.
- Reuse MainActivity's existing network-state owner; do not register a duplicate ConnectivityManager callback or create a parallel network subsystem.
- Fine-location permission, Location service and Wi-Fi enabled state gate scan/result access. Declare CHANGE_WIFI_STATE only because Android requires it for WifiManager.startScan; never use it to toggle the radio.
- Android Settings remains the owner for network selection, saved networks, authentication, connection/disconnection and radio state. Do not collect/store credentials or claim unsupported direct control.
- Treat startScan as best-effort: Android may throttle requests and the API is deprecated since API 28. Preserve last results, asynchronous completion, timeout/error states and Android Settings fallback; do not represent a rejected/stale scan as fresh.
- Restore Quick Settings after Android Settings returns so its network summary is refreshed. If system Settings cannot launch, restore the parent immediately.
- SOURCE-HOST QC may pass independently; physical Android TV/remote/OEM acceptance remains a separate gate.
- Exact owner: `feature/g3e-wifi` @ `9125043e24b8f13232e41193ba5fe10e1821f7cd`; PR #70 OPEN/DRAFT/UNMERGED; exact-head CI #1363 / `42346491017` — both required jobs SUCCESS.
- Status: IMPLEMENTED / SOURCE-HOST QC PASS; DEVICE ACCEPTANCE PENDING / NOT RUN.


## D-031 — G3F Global Settings V06 structure and ownership (7 November 2026)

- Implement Global Settings against the approved G1 V6 V06 composition: branded header, global control search, read-only Wi-Fi status, persistent seven-category navigation and category detail pane.
- Keep search localized to existing control labels/summaries; route each result to its owning category and focus the target control. Preserve saved-focus migration when the search moves from sidebar to header and when the profile identity summary is inserted.
- Profile identity and child-mode state remain owned by ProfileManager and ChildModeManager. Wi-Fi/network state remains owned by MainActivity/E01; Global Settings must not add a duplicate network callback, radio switch or system-setting mutation.
- Restore invoking-control focus on dialog dismissal and refresh external Android Settings state on host resume without retaining destroyed Activity/view objects.
- SOURCE-HOST QC may pass independently; physical Android TV/remote/OEM/overscan/daylight acceptance remains a separate gate. Keep PR #71 OPEN/DRAFT/UNMERGED until separately approved integration.
- Exact owner: `feature/g3f-global-settings` @ `455b619c3eefe417ac0152a51f3cb12478a8f4a9`; PR #71 OPEN/DRAFT/UNMERGED; exact-head CI #1376 / `42348837445` — both required jobs SUCCESS.
- Status: IMPLEMENTED / SOURCE-HOST QC PASS; DEVICE ACCEPTANCE PENDING / NOT RUN.


## G4 final security policy and source-host checkpoint — 7 November 2026

- Preserve the user's approved policy: default parent PIN remains `0000`; no forced first-use change; unlimited wrong attempts; no retry counter, limit, cooldown, lockout, forced delay or temporary restriction.
- Parent PIN remains device-wide. Child Mode, App Lock and Hidden Apps remain profile-scoped.
- Custom PIN at-rest storage is hardened with salted PBKDF2-HMAC-SHA256; legacy custom PIN migration is lossless after successful verification. Corrupt or previously stored-but-lost credential state fails closed and never silently reverts to `0000`.
- The four-digit PIN remains guessable under unlimited attempts. Hashing reduces plaintext exposure; it does not make the PIN brute-force-proof.
- **G4-A–G4-J COMPLETE / SOURCE-HOST QC PASS.** Final G4 HEAD `f46adf27bbc6b2156c7e9bb49d48cf4fd0fcdb5e`; exact G3F base `455b619c3eefe417ac0152a51f3cb12478a8f4a9`; synthetic merge `376b5b69e2aed56e1f813169f35dbb9459ada554`; CI #1394 / `42353368760` both jobs SUCCESS.
- PR #72 remains OPEN/DRAFT/UNMERGED. PR #66–#71 dependency chain remains unchanged and OPEN/DRAFT/UNMERGED. No merge/rebase/cherry-pick/force-push, APK publication/install, ADB or device mutation.
- Physical TV/remote/OEM acceptance remains a separate PENDING / NOT RUN gate. Canonical checkpoint: `UI_REFRESH_G4_CHECKPOINT_2026-11-07.md`.


## Decision — Home Dock visual parity V2 (7 November 2026)

- Preserve the supplied Home screenshot as the locked visual master; do not redesign the Dock or surrounding Home composition.
- Reuse Patch 4.0/4.1 geometry and preserve legacy density rounding. Centralize sizing/spacing in DockGeometry; the independent G3A Dock/Grid architecture remains.
- Do not import Patch 4.1's extra DockBreathingBorderView in this pass: current G3A Glider already provides focus feedback and a second animated border would alter the approved focus appearance.
- PR #74 remains Draft/unmerged on fix/home-dock-visual-parity-v2 @ 4ba1d7f14ba26e0c4051fcdf958f289234b6e7ae. No integration branch changes.
- Source-host CI #1407 PASS; test APK is ready for controlled device acceptance. Do not claim visual PASS until owner confirms TV screenshot/remote behavior.


## Decision — Patch 4.1 Dock & Liquid Glass adoption (7 November 2026)

- Adopt Patch 4.1's first-row Dock fill semantics: configured favourites/user pins retain precedence; ordered inventory fills remaining visible slots; explicit unpins remain excluded; overflow remains in Grid.
- Adopt Patch 4.1 GlassRim iridescent gradient, Window Light, GlassView hardware-layer focus rim and DockBreathingBorderView. Breathing rim is scoped to Dock cards only.
- Keep current G3A independent DockAdapter/AppAdapter architecture, stable identity, 4/5/6 capacity and orientation preferences. Do not replace the whole application with Patch 4.1.
- PR #74 remains Draft/unmerged at 239390db27fafcc39b2fefa68ddfa9f0ba30608f. Exact-head CI #1415 PASS.
- Device visual/remote acceptance is pending; no claim of final visual PASS until the owner confirms the new TV photo.


## Decision — Patch 4.1 Dock & Liquid Glass adoption final (7 November 2026)

- Adopt Patch 4.1 first-row Dock fill semantics: configured/user pins lead; ordered inventory fills remaining visible slots; explicit unpins are respected; overflow stays in Grid.
- Adopt Patch 4.1 GlassRim iridescent gradient, Window Light, GlassView hardware-layer focus rim and DockBreathingBorderView. Breathing rim is scoped to Dock cards only.
- Restore Patch 4.1's 16dp horizontal Dock bottom safe offset; the Grid reservation already includes this gap so the Dock and Grid meet cleanly.
- Keep current G3A independent DockAdapter/AppAdapter architecture, stable identity, 4/5/6 capacity and orientation preferences. Do not replace the whole application with Patch 4.1.
- PR #74 remains Draft/unmerged at 2d1f744a4b77406712e7868a7e7c6747a3bb88ed. Exact-head CI #1416 PASS.
- Device visual/remote acceptance remains pending; no final visual PASS until owner confirms the new TV photo.


## Decision — horizontal Dock overflow recovery (7 November 2026)

- Keep the dedicated Dock/Grid renderers for now, but restore Patch 4.1's user-facing interaction contract: horizontal Home rests on Dock only; all remaining apps remain in the authoritative Grid and are reachable with DPAD Down.
- Do not permanently hide or discard overflow apps. Reveal Grid only while the user browses overflow; Up from the first Grid row returns to the aligned Dock slot; Up from Dock returns to the top shelf; Dock focus collapses Grid.
- Stable component identity remains the source of focus restoration. Keep vertical Dock, 4/5/6 capacity, pin/unpin and inventory split behavior intact.
- Exact source head 48f07a983ebee4370bbf13a80f85edd947c2eb31; CI #42382202545 PASS. PR #74 remains OPEN/DRAFT/UNMERGED pending physical TV acceptance.
- Physical TV visual/remote acceptance is NOT RUN; CI is not visual PASS.


## Decision — horizontal Dock aggregate centering correction (7 November 2026)

- Treat the five Dock icons as one centered group, not five independently positioned tiles. The owner photo proves the prior left-aligned group was visibly wrong.
- Compute the horizontal offset from the full viewport width, measured tile width, and actual visible Dock item count; translate the Dock host without shrinking its measurement viewport.
- Reset translation on vertical orientation/empty Dock. Preserve card size, spacing, tray inset, bottom safe gap, glass, focus and surrounding Home composition.
- Add geometry tests for capacities 4/5/6 and multiple densities. Latest source HEAD b14a7bc3d1e99cf8cf0c74e944c39983d776f341; PR #74 remains OPEN/DRAFT/UNMERGED.
- Latest-head CI and physical TV acceptance remain pending. Do not merge or call visual acceptance PASS before both are resolved.

- Final latest-head CI #1424 / run 42384207367 SUCCESS; both required jobs passed, including 296+ unit tests and release build, repo contract checks, APK diagnostics and artifact publication.
- Release APK 1,624,997 bytes; SHA-256 8e3183addc092e5bce1b41b09d939fc64682d6b16f595a0e4e1caa0a5f982664. Artifact #13578283307; ZIP 1,424,983 bytes; SHA-256 72014680fd7e7b02cf36ed5029d55be465ac3394d649fc95ee718af05e234d17; expires 14 Nov 2026.
- APK extracted and verified as Android package. Physical TV visual/remote acceptance still PENDING / NOT RUN; PR #74 OPEN/DRAFT/UNMERGED. No install, ADB, device mutation or merge.

## Decision — remove unsupported GridLayoutManager stackFromEnd on Home resume (8 November 2026)

- Owner-provided Mi TV Stick logcat proves a fatal crash in `MainActivity.onResume()` caused by `GridLayoutManager.setStackFromEnd(...)` called from `applyDockLayout()`.
- Do not replace with `reverseLayout`: that would risk reversing app inventory / DPAD order. Preserve natural adapter order and existing horizontal overflow geometry/focus behavior; remove the unsupported call.
- Fix committed on PR #74 branch `fix/home-dock-visual-parity-v2`, HEAD `61d4e429be129263533ecfa75a729bbe0a15833f`.
- Exact-head CI run `42398810251` was IN_PROGRESS at checkpoint. Physical Mi TV Stick cold-start/resume validation remains NOT RUN. PR #74 stays OPEN/DRAFT/UNMERGED; no merge or default-launcher change.


## Resume checkpoint — GTVH-HOME-LAYOUT-FIX-001 balanced safe-frame cross-audit (8 November 2026)

- Owner repeated the requirement: equal visible safe-frame margins on all four Home screen edges; preserve all already-good features and correct only identified defects.
- Locked baseline: Mi TV Stick / Android TV 11 / 1920×1080 / 320dpi logical density (2px per dp). Target safe frame is 34dp = 68px on top, bottom, left and right.
- Audited PR #79 branch `fix/home-balanced-frame-001`; current source HEAD `a7a15e3a803dd173b302432c2665fc2c6db91426`.
- Cross-audit fixes: corrected stale 48dp source comment; replaced a remaining hardcoded 34f with the shared safe-frame constant; added four-edge regression coverage across 1280/1696/1712/1920px, densities 1/1.5/1.75/2 and Dock capacities 4/5/6.
- Found and fixed a separate PR #79 scope defect: legacy `HomeWeather.Line` could overwrite the shared G3B Weather/AQI text. Home text now remains owned by `WeatherAirQuality.getFormatted()`; legacy HomeWeather remains for wallpaper atmospheric overlay only. Added a source-contract regression test.
- Exact-current-head CI #1526 / run `42463873162` on `a7a15e3a803dd173b302432c2665fc2c6db91426` is IN PROGRESS. Earlier CI #1521 passed on prior head `b8c3c48917b74395ba32fb6755619b501bc42d33`; its APK is not the current-head candidate.
- PR #79 remains OPEN / DRAFT / UNMERGED. PR #77 latest observed head is `0ea3746f0304b7042a018eeebf850d759f357bf8`; PR #79 compares 42 ahead / 1 behind with merge base `0aeb3851ddce5deb47e3c5f96db44631b0343577`. The one missing PR #77 commit is workflow-only APK artifact publication; no Home source file changed there. Keep this dependency visible and reconcile before merge.
- Physical Mi TV Stick visual/remote acceptance remains PENDING. No merge, APK installation, ADB, default-launcher change, or device-setting mutation.
- Detailed audit: `docs/project-management/HOME_LAYOUT_CALIBRATION_AUDIT_2026-11-08.md`.


## Superseding Home safe-frame decision — 8 November 2026

- The earlier 34dp balanced-frame checkpoint was an intermediate calibration state and is superseded for the current Home Header refinement.
- Final owner-approved target for PR #79 is **29dp on top, bottom, left and right**. At 320dpi logical density (2px/dp), this is 58px per edge.
- Keep the existing left, bottom and right visual alignment baseline; calibrate the top edge to the same 29dp frame. Do not alter Dock/Grid dimensions or behavior as part of this Header refinement.
- Current source/test HEAD: `97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5`; PR #79 remains Draft/Open/Unmerged.
- Exact-head CI #1581 / run `42486917618` and companion release workflow #36 / `42486917728` succeeded. Device visual/remote acceptance remains pending; CI is not physical-TV acceptance.


## Home Header + whole-Home cross-audit follow-up — 8 November 2026
- Implementation HEAD `97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5`: HOME-001–HOME-005 source corrections and exact-head CI #1581 PASS; both required jobs and companion release workflow #36 succeeded. Release APK diagnostic: 1,627,577 bytes; artifact #13620161574.
- Static review inventory: 63 unique IDs in `activity_main.xml`; Home Header actions, location/weather controls, visible prayer timetable activation and DPAD paths reviewed. No additional source-level missing-handler defect confirmed.
- Added three source-contract regression tests for location pill activation/DPAD, Home/Profile/Settings actions and measured Header-width recalculation. Test-only commit `49f2777cee006a5afd50a33b85cb90a61a077e40`; exact-head CI pending observation.
- PR #79 remains OPEN / DRAFT / UNMERGED and stacked on PR #77. No APK installation, ADB, device setting/default-launcher change or physical TV test.
- Whole Home Screen remains PARTIAL / NOT PASS pending Mi TV Stick rendered, DPAD, lifecycle and feature acceptance.
- Canonical audit: `GTVH_HOME_SCREEN_CROSS_AUDIT_001.md`.


## Exact-head CI #1582 and reconciliation-doc update — 8 November 2026

- Test-only commit `49f2777cee006a5afd50a33b85cb90a61a077e40` is **CI #1582 PASS** (run `42487572059`). The only diff from implementation HEAD `97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5` is `tests/test_home_layout_bounds_contract.py` (+59 lines).
- Python CI: **160 tests, OK (1 skipped)**; all three newly added location/Header regression tests passed.
- Android CI: `testReleaseUnitTest` + `assembleRelease` **BUILD SUCCESSFUL**; APK diagnostic 1,627,577 bytes; artifact #13619209786.
- Updated PR-branch reconciliation document to replace the obsolete intermediate 34dp safe frame with the approved 29dp frame. PR-branch docs-only commit: `c15e4383d7dd3f148b31a19f5e9b85423936b56d`. CI #1583 is validating that documentation-only tip; no need to change application source.
- Release workflow #37 is explicitly **not** the current candidate: its logs show CI #1545 / older commit `c058935ed4398f5c7964d753fcf35a9cf77989bb`. Do not cite that release asset as the latest PR #79 APK.
- PR #79 remains OPEN / DRAFT / UNMERGED; physical Mi TV Stick acceptance remains NOT RUN. No APK installation, ADB, device settings/default launcher mutation or merge.


## Decision — equalize Home left information pills (8 Nov 2026)

**Decision:** Preserve the location/altitude pill as the fixed reference. Calculate its existing runtime width once from the measured Header width (34%) and assign the same pixel width to the weather/AQI pill. Both controls already share the same parent-start anchor, so no horizontal offset or padding change is needed.

**Rationale:** The previous 34% vs 44% sizing caused the right-edge mismatch. Reusing the location width produces identical left/right coordinates and avoids changing an already-approved component. Long weather text remains single-line/end-ellipsized inside its fixed slot; data and interaction behavior remain untouched.

**Scope exclusions:** no vertical movement, margins, padding, font/icon/radius, clock/date, Wi-Fi/Assistant, Home/Profile/Settings, safe frame, provider, click/DPAD/focus/animation, or other Home changes.

**Implementation:** PR #79 `fix/home-balanced-frame-001`; source `3c9821864a08bb50f6f1b13473964f8d5b5baeb7`; regression test/current HEAD `65544d9eea9bd0a5ae5156a9bd58724b1817fd2a`. CI #1587 Android job pending; device visual acceptance pending.


## CI #1587 final validation — 8 Nov 2026

CI #1587 / run `42489445766` completed **SUCCESS** on PR #79 head `65544d9eea9bd0a5ae5156a9bd58724b1817fd2a`. Both required jobs passed. Python regression suite: **161 tests, OK (1 skipped)**; the new `test_weather_aqi_pill_matches_location_pill_width_and_left_anchor` passed. Android `testReleaseUnitTest` and `assembleRelease`: **BUILD SUCCESSFUL**. Release APK diagnostic: **1,627,545 bytes**; CI artifact ID **13621187055**, ZIP size **1,426,725 bytes**. CI proves source/build/test contracts only; no rendered Mi TV Stick or physical visual acceptance is claimed. PR #79 remains OPEN / DRAFT / UNMERGED.


## Latest checkpoint — GTVH-HOME-WEATHER-PILL-WIDTH-REFINEMENT-002 (8 Nov 2026)

The approved reference is the location/altitude capsule. Both left Header capsules now use **30% of measured Header width**, the same **30dp height**, 12dp horizontal padding, 0dp vertical padding on the weather TextView (matching the location container's default zero vertical padding), centered vertical content, and the same glass-pill background drawable/radius. Both retain the same parent-start anchor and existing 8dp vertical gap. The 30% width is computed once in `MainActivity.applyHeaderWidths()` and assigned to both controls. Weather remains single-line with end ellipsis. No data provider, font/icon, focus/click/DPAD, animation, clock/date, nav, Wi-Fi/Assistant, safe-frame or other Home geometry changes intended.

Changed PR #79 branch `fix/home-balanced-frame-001`:
- `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java` — shared width formula changed from 34% to 30%.
- `launcher/app/src/main/res/layout/activity_main.xml` — weather pill height 30dp, vertical padding zero, vertical centering; location pill remains reference.
- `tests/test_home_layout_bounds_contract.py` — regression checks 30% shared width, equal 30dp height, equal horizontal padding, vertical alignment and content update invariants.

Current PR #79 HEAD / implementation source SHA: `bf65621ae46b170294e4333928886fbafa0898b6`. CI #1590 / run `42490240670`: SUCCESS, both jobs pass. Python suite: **161 tests, OK (1 skipped)**; the new pill-geometry regression test passes. Android `testReleaseUnitTest` and `assembleRelease` completed successfully. Release APK: **1,627,561 bytes**. Downloadable workflow artifact ID `13621062347` (ZIP 1,426,708 bytes; expires 15 Nov 2026). Local extracted APK SHA-256: `51c6921ad7055812dd4c3de844bce26516b0ffeea6dd8401abc5d94913dcb56d`.

PR #79 remains OPEN / DRAFT / UNMERGED. No physical device test is claimed. Owner will install/test this APK on Mi TV Stick at 1920×1080, 320dpi; Indonesian/English; short/long location, weather and AQI content; inspect clipping, exact visual height/width, common left/right edges, vertical spacing, focus/click/DPAD and unaffected Header elements. Documentation checkpoint is synced after build/test; physical acceptance remains pending.
