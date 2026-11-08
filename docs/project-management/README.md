> **CANONICAL CHECKPOINT — READ THIS FIRST**  
> The only authoritative restart point is [GTVH-CHECKPOINT-LATEST.md](GTVH-CHECKPOINT-LATEST.md) on branch `docs/project-master-inventory`. Start every continuation there; do not resume from historical checkpoint blocks, old PR descriptions, or branch-local notes. This file is supporting detail/history and must not establish a competing current state.  
> **Checkpoint authority:** GitHub repository `Frangga11/glass-tv-home`; canonical docs branch `docs/project-master-inventory`; single entry file `docs/project-management/GTVH-CHECKPOINT-LATEST.md`. The latest checkpoint supersedes all older status statements below.  

---

# Glass TV Home — Project Documentation

**Documentation set:** Project Master  
**Initial release:** v0.1-draft  
**Prepared:** 31 October 2026  
**Repository:** https://github.com/Frangga11/glass-tv-home  
**Current project checkpoint:** Official Project G03 physical Mi TV Stick acceptance remains OPEN/BLOCKED pending explicit readiness approval and device connection. Separate UI Refresh GTVH-UIR-002 v2.0 subtrack: G1, G2 and G3A–G3F COMPLETE at their recorded prototype/source-host gates; next is a separately approved G4 integration/reconciliation plan.
**Current stage:** Official Project G03 device acceptance remains a separate blocked gate. UI Refresh G3F Global Settings is complete at SOURCE-HOST QC; G4 planning requires explicit approval, and physical device acceptance remains separate.
**Documentation branch:** `docs/project-master-inventory`  
**F02 review PR:** https://github.com/Frangga11/glass-tv-home/pull/50

## Purpose

This directory is the durable project record for Glass TV Home. GitHub is the canonical, version-controlled source. A matching export is to be kept in ChatGPT Library as a backup.

## Documents

| File | Purpose |
|---|---|
| [MASTER_PROJECT.md](MASTER_PROJECT.md) | Goals, device baseline, agreed user requirements, product scope |
| [INVENTORY.md](INVENTORY.md) | Current feature/code inventory and evidence-based status |
| [DECISION_LOG.md](DECISION_LOG.md) | Approved decisions and unresolved choices |
| [OPERATIONS_LOG.md](OPERATIONS_LOG.md) | Concise chronological change history (third documentation layer) |
| [AUDIT_REGISTER.md](AUDIT_REGISTER.md) | Defects, risks, verification steps, and closure evidence |
| [AVATAR_AUDIT.md](AVATAR_AUDIT.md) | Original QR avatar audit, implementation record, and device acceptance checklist |
| [E02_PROFILE_AVATAR_LOCK_UI_AUDIT.md](E02_PROFILE_AVATAR_LOCK_UI_AUDIT.md) | E02 profile/avatar/app-lock source audit, corrections, exact-head CI and F-stage acceptance |
| [E05_WIDGETS_INFORMATION_AUDIT.md](E05_WIDGETS_INFORMATION_AUDIT.md) | E05 widget/information center audit, ayah no-truncation repair, and F-stage acceptance |
| [DOCUMENTATION_WORKFLOW.md](DOCUMENTATION_WORKFLOW.md) | Mandatory three-layer documentation standard and efficient update rules |
| [WALLPAPER_AUDIT.md](WALLPAPER_AUDIT.md) | Wallpaper/background audit, source findings, security issue, and acceptance criteria |
| [E01_DEVICE_SETTINGS_HOME_CONTROL_AUDIT.md](E01_DEVICE_SETTINGS_HOME_CONTROL_AUDIT.md) | E01 Device Settings source audit and exact-head verification |
| [B03_DIALOG_OPACITY_AUDIT.md](B03_DIALOG_OPACITY_AUDIT.md) | B03 global app-owned dialog/popup surface contract and Glass Effect OFF solid fallback |
| [B06_FEEDBACK_AUDIT.md](B06_FEEDBACK_AUDIT.md) | B06 notification, Toast, OTA loading, QR progress/timeout audit and exact-head CI evidence |
| [F03_QR_COMMUNICATION_FEASIBILITY.md](F03_QR_COMMUNICATION_FEASIBILITY.md) | F03 QR communication/upload feasibility evidence, CI, findings, and Project G device gates |
| [F04_FEATURE_ISOLATION_AUDIT.md](F04_FEATURE_ISOLATION_AUDIT.md) | F04 Avatar, Wallpaper and StandBy source isolation, repairs, CI and Project G gates |
| [HANDOVER.md](HANDOVER.md) | Current work position and how to resume in a new conversation |
| [UI_REFRESH_G3A_CHECKPOINT_2026-11-07.md](UI_REFRESH_G3A_CHECKPOINT_2026-11-07.md) | G3A Home & independent Dock source/host closeout evidence |
| [UI_REFRESH_G3D_AUDIT_2026-11-07.md](UI_REFRESH_G3D_AUDIT_2026-11-07.md) | G3D Quick Settings audit, root causes, remediation and source/host verification |
| [UI_REFRESH_G3D_CHECKPOINT_2026-11-07.md](UI_REFRESH_G3D_CHECKPOINT_2026-11-07.md) | G3D Quick Settings source/host closeout and next work point |
| [UI_REFRESH_G3F_AUDIT_2026-11-07.md](UI_REFRESH_G3F_AUDIT_2026-11-07.md) | G3F Global Settings audit and source-host QC evidence |
| [UI_REFRESH_G3F_CHECKPOINT_2026-11-07.md](UI_REFRESH_G3F_CHECKPOINT_2026-11-07.md) | G3F Global Settings source-host closeout and handover |
| [F07_PERFORMANCE_STABILITY_AUDIT.md](F07_PERFORMANCE_STABILITY_AUDIT.md) | Final F07 performance, memory, stability and storage host/source evidence |
| [PROJECT_G_READINESS_CHECKPOINT.md](PROJECT_G_READINESS_CHECKPOINT.md) | Official Project F closure, Project G work packages, device gates and governance |

## Documentation architecture

The project uses three connected layers: **MASTER** (`MASTER_PROJECT.md`, `INVENTORY.md`, `DECISION_LOG.md`, `HANDOVER.md`), **AUDIT & SOP** (`AUDIT_REGISTER.md` plus focused feature audits/SOPs), and **OPERATIONAL LOG** (`OPERATIONS_LOG.md`). GitHub remains canonical; ChatGPT Library is the milestone backup. Follow DOCUMENTATION_WORKFLOW.md. Do not duplicate content across layers.

## Evidence and status rules

- **CONFIRMED-CODE:** verified by inspecting the named source file/branch.
- **CONFIRMED-USER:** explicitly supplied or agreed by the user.
- **CI-VERIFIED:** a GitHub Actions run or artifact proves the specified build/test passed.
- **DEVICE-VERIFIED:** tested on the actual target TV/streaming device with recorded evidence.
- **PLANNED:** requested or agreed, but not yet implemented.
- **PARTIAL:** implementation exists, but coverage or verification is incomplete.
- **UNVERIFIED:** insufficient evidence.
- **BLOCKED:** a known dependency prevents completion.

A feature is not called complete merely because its code exists. Build success is not the same as real-device verification.

## Update protocol

1. Record each new requirement or decision in the relevant document.
2. Record code changes with branch, commit SHA, files changed, and reason.
3. Update the audit register with reproducible checks and evidence.
4. At the end of every work session, update HANDOVER.md.
5. Never erase unresolved items; close them only with evidence.
6. Keep this documentation branch separate from application feature branches until reviewed.

## Current baseline

The application source inspected for this draft reports package `com.gathin.tvhome`, versionName `4.0.0`, versionCode `40`, minSdk 26, targetSdk 34, compileSdk 34, and Java 17 compile options. The user's target setup recorded in project context is an LG 32-inch display using a Mi TV Stick running Android 11 / API 30. This hardware baseline still needs confirmation against a fresh device audit before release.

## Important branch distinction

E02 selectively carries the QR avatar implementation into the profile-security baseline and is tracked in Draft PR #35. PR #6 remains a historical isolated source branch and must not be merged independently. E02 source/CI gates pass; actual Mi TV Stick + phone/remote acceptance remains mandatory in Project F. See E02_PROFILE_AVATAR_LOCK_UI_AUDIT.md, AVATAR_AUDIT.md, INVENTORY.md, and AUDIT_REGISTER.md.


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


## E08 audit report
- [E08 Wallpaper Settings Redesign Audit](E08_WALLPAPER_SETTINGS_REDESIGN_AUDIT.md) — unified three-tab wallpaper control center, existing-state reuse, exact-head CI and Project F acceptance matrix.


## Current Project F02 checkpoint — 3 November 2026

- Integration branch: `integration/project-f02-system-validation`; PR #50 remains Draft/open/unmerged.
- Latest checkpoint recorded: `1658237bafdb24167391270f3ad8f694751a6c2a`. Exact-head CI was queued at the time of this documentation update.
- Three cross-module fixes are under owner-branch retest: D05/B01 modal contract, D01 location broadcast authorization, and D06 AzanActivity export boundary. D01 follow-up PR #51 exact-head CI passed; B01 #10, D05 #29, D06 #30 exact-head retests were still running.
- Earlier F02 exact-head CI on `27ecd5e4e5e283f5d1675cb28a75b2f1cbb0a6c7` passed both jobs, but it predates the MainActivity modal enforcement follow-up.
- No finding is closed until owner-branch and latest F02 integration CI both pass. Device-specific checks remain DEFERRED_TO_G.
- No PR merged, no APK published/installed, and no physical Android TV test performed.


### Latest F02 audit checkpoint — 3 November 2026
- Latest F02 branch checkpoint: `integration/project-f02-system-validation`, head `eea920469ffe5bef5ddb14e1dfd66687e82d8fed`, PR #50 Draft/open/unmerged; exact-head CI in progress.
- B01 fixes now include shared MainActivity modal enforcement and restored Home double-press Recent Apps routing; App Switch key remains disabled.
- D01 receiver authorization and internal-only LocationActivity fixes are CI PASS; D05 and D06 owner-branch retests are CI PASS. B01 latest exact-head CI and F02 latest integrated CI remain pending.
- F02 Robolectric window tests passed on previous integration head `5f6b0c60949c1284e0e904e614e85c2e0ca657f2`; latest head still requires retest.
- RecentAppsActivity exported ACTION_MAIN entry contract remains a documented P3 review item for Project G; no blind compatibility-breaking change made.


### F02 follow-up checkpoint — 3 November 2026
- B01 latest owner head: `a1e743ad88c28ca3a9602dfc9d0b5da16e46285d`, PR #10; exact-head CI in progress.
- F02 latest integration head: `3ea479d689dc732fe46594a0677d02b7762d7527`, PR #50; exact-head CI in progress.
- New findings fixed in source: Force Stop callback queue isolated from Home/power callbacks; RecentAppsActivity made internal-only after discovering exported ACTION_CLOSE_ALL could trigger Close All from another installed app.
- Added regression guards for Force Stop/Home handler isolation and Recent Apps manifest boundary. Exact-head CI must pass before closure.
- Previous F02 head `c9496d4e051980e640bc269ffce695b95ba71ad4` passed both CI jobs before these latest fixes.


### Latest exact-head validation update — 3 November 2026
- F02 integration checkpoint `5dd787ba136dfb070eaa2e833af653e57ec66015`: both required CI jobs PASS (run #41509632784 Launcher build/unit tests; #41509626957 Lists/tools/docs).
- B01 owner checkpoint `791620ce4628ee9e909af7ce2f4758b6df77afeb`: both jobs PASS (run #41509576279); subsequent comment-only alignment commit `389e289e939276ce9e20d7e6c82c293d3f83c70f` exact-head CI pending.
- Integrated fixes now cover modal consistency, Home double press, Force Stop callback isolation, internal-only Recent Apps (including Close All action), and correct Android TV restart action routing.
- No physical Android TV testing, APK publication/installation, or merge has occurred.


### Latest C07/F02 audit finding — 3 November 2026
- C07 cross-module audit found wallpaper management POST operations (select/delete/slideshow/clear) could race QR-session shutdown after the outer request check. They are now synchronized with server stop and recheck the active session inside the same monitor; revoked operations return HTTP 410.
- C07 owner PR #24 latest documented head `12b1f431f7687023727983089ea03eda0f97a73d`; F02 PR #50 latest head `081496ca93fd0526025a21ce7cd0e3d7f9c3364a`. Both exact-head CI workflows were in progress.
- Added `tests/test_wallpaper_session_shutdown_atomicity.py`; no upload routes, browser UX, limits, or StandBy persistence semantics changed.


### F02 continuation checkpoint — 3 November 2026
- Final QR upload audit confirmed Avatar, custom Wallpaper and StandBy commit operations are serialized with session shutdown; explicit regression guards now cover both Wallpaper and StandBy upload commit sections.
- Added F02-RES-001 storage-safety correction: synchronized concurrent upload byte reservations, overflow-safe capacity arithmetic, 64 MiB minimum free-space margin, HTTP 507 for insufficient space, and guaranteed reservation release. Existing upload-size and StandBy collection limits remain unchanged; no arbitrary custom-wallpaper total cap added.
- Added JUnit boundary tests and Python source-contract tests. F02 PR #50 head c1f212585633899d4c1f5327d3a982a9e136e2b7; CI #41519813674 Lists/tools/docs SUCCESS, Launcher build/unit tests IN_PROGRESS at last poll.
- Keep F02 partial until exact-head CI completes. PR #50 remains Draft/open/unmerged; no APK publication/installation or physical Android TV test. Device acceptance remains Project G.


### F02 upload storage hardening — 3 November 2026
- F02 source/test head c1f212585633899d4c1f5327d3a982a9e136e2b7 passed exact-head PR CI #41519813674: both jobs SUCCESS; 42 Python tests, Launcher unit tests and release validation build passed.
- APK 1,565,621 bytes against 1,572,864-byte cap, leaving 7,243 bytes. Keep build-budget P3 watchlist active.
- Concurrent QR upload storage reservations and upload-shutdown critical-section tests are HOST/SOURCE VERIFIED; physical behavior remains Project G.
- New findings-register docs head 1845635091969c2475bb0d26783bc0f7eb58c1c0 has exact-head CI #41520194974 queued at last poll. PR #50 remains Draft/open/unmerged; no APK publication or physical TV test.

### F02 process-wide QR storage reservation refinement — 3 November 2026
- Changed upload reservation accounting to a static process-wide lock/counter after identifying a narrow overlap where old workers may drain while a replacement QR session starts.
- This ensures the new session accounts for outstanding bytes from the old session until each worker releases its reservation.
- Latest F02 head a95b21becad238dd634dda1e79aa9de36d749d8c; CI #41520409999 queued at last poll. Earlier per-instance version passed #41519813674, but the latest refinement still requires exact-head CI.
- PR #50 remains Draft/open/unmerged; no APK publication or physical Android TV test.

### C07/F02 exact-head verification — 3 November 2026
- C07 owner PR #24 head 442b78c68b1540394d543bab59bddf52f865ccf0 passed both jobs in #41520866323 (18 Python tests, launcher tests/build). APK 1,550,949 bytes; manual publication step skipped; no artifacts.
- F02 PR #50 source/test head a95b21becad238dd634dda1e79aa9de36d749d8c and findings-register head 2469c0c1d241a00be3e02235e12f3f0f6525deb9 passed #41520409999 and #41520818608 respectively (both jobs; 42 Python tests; launcher tests/build). APK 1,565,713 bytes, 7,151 bytes headroom; no artifacts.
- Owner and integration now share process-wide QR upload storage reservations; F02 preserves D05 UploadResult persistence adaptation.
- Latest F02 docs head e190e2863a4aacbe3aba61094fab00572f4b469f; exact-head CI #41521145454 queued at last poll. PRs remain Draft/open/unmerged; no physical TV testing.

## Official transition — 3 November 2026

Project F is officially closed at HOST/SOURCE EXIT PASS for F01–F07. Project G is ready to begin at G01. The frozen F07 candidate is `integration/project-f07-performance-stability` @ `976e068d48ecc1ede2811ac455372395d382ba76`; PR #56 remains Draft/open/unmerged. Physical Mi TV Stick acceptance and full integrated regression remain Project G obligations. See [PROJECT_G_READINESS_CHECKPOINT.md](PROJECT_G_READINESS_CHECKPOINT.md).


## Current Project G03 checkpoint — 5 November 2026

- Project F is closed HOST/SOURCE EXIT PASS. G01 HOST/SOURCE PASS and G02 HOST BUILD + ARTIFACT PASS are recorded in HANDOVER.
- Current G03 task: Mi TV Stick physical acceptance. Non-mutating preflight confirms integration SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d`, G02 run #1185 both jobs SUCCESS, artifact #13210715335 available, artifact digest and extracted APK SHA-256 match the canonical record, and APK ZIP integrity PASS.
- G03 is OPEN/BLOCKED: no connected TV/ADB tools in this host; explicit readiness approval is required before installation/device mutation. No physical acceptance is claimed.
- See [G03_DEVICE_ACCEPTANCE_PREFLIGHT_2026-11-05.md](G03_DEVICE_ACCEPTANCE_PREFLIGHT_2026-11-05.md). Artifact retention expires 10 Nov 2026 06:37:50 UTC.

## Global UI Refresh subtrack — G1 checkpoint (6 November 2026)

- GTVH-UIR-002 v2.0 work package G1 is **COMPLETE / internal QC PASS**. The six V01–V06 surfaces and all six Dock orientation/capacity variants were rendered and reviewed in Chromium at 1920×1080.
- Canonical interactive artifact: [G1 V6 prototype](prototypes/UI_REFRESH_G1_GLOBAL_UI_PROTOTYPE_V6.html); verification record: [G1 checkpoint](UI_REFRESH_G1_CHECKPOINT_2026-11-06.md).
- Browser functional suite: 12 PASS / 0 FAIL; JavaScript syntax check PASS; 13 screenshots captured. Prototype only: no production source, owner branch, integration, APK or device was changed.
- Next UI-refresh work package: G2 — Shared UI Foundation Repair. Owner visual sign-off remains a separate acceptance gate before broad production rollout.
- This UI-refresh subtrack is distinct from official Project G03 device acceptance, which remains OPEN/BLOCKED as recorded above.


## Global UI Refresh G3A closeout — 7 November 2026

- G3A Home Screen & Independent Dock is **COMPLETE / SOURCE-HOST QC PASS** on `feature/g3a-home-independent-dock` @ `87302f52de844954392bac5438cbe82ec5d14a30`; PR #66 remains OPEN/DRAFT/UNMERGED.
- Exact-head CI #1296 / `42266095885`: both required jobs SUCCESS; launcher tests, release validation build and lint-vital PASS. APK diagnostic 1,589,825 bytes; informational under D-023.
- Canonical evidence: [G3A checkpoint](UI_REFRESH_G3A_CHECKPOINT_2026-11-07.md). Next UI Refresh work package is G3B Weather & AQI V4.
- No merge, APK publication/installation, ADB or physical TV/remote/overscan test. Official Project G03 device acceptance remains separately OPEN/BLOCKED.

## Global UI Refresh G3D closeout — 7 November 2026

- G3D Quick Settings is **COMPLETE / SOURCE-HOST QC PASS** on `feature/g3d-quick-settings` @ `bbc7b240d2ad7bfc9d22dbaf3437f03b5953bcf8`; PR #69 OPEN/DRAFT/UNMERGED, based on unchanged G3C exact HEAD `8cf81f84bd62d0b3da31532116553cfa21a5df98`.
- Exact-head CI #1328 / `42344123725`: both required jobs SUCCESS; 120 Python tests, launcher/installer unit tests and release validation build passed. APK diagnostic 1,608,373 bytes, informational under D-023.
- Canonical evidence: [G3D audit](UI_REFRESH_G3D_AUDIT_2026-11-07.md) and [G3D checkpoint](UI_REFRESH_G3D_CHECKPOINT_2026-11-07.md).
- No merge, APK publication/installation, ADB or physical TV/remote/overscan test. Official Project G03 device acceptance remains separately OPEN/BLOCKED. Next UI Refresh work package: G3E Wi-Fi.

## Global UI Refresh G3F closeout — 7 November 2026

- G3F Global Settings is **COMPLETE / SOURCE-HOST QC PASS** on `feature/g3f-global-settings` @ `455b619c3eefe417ac0152a51f3cb12478a8f4a9`; PR #71 remains OPEN/DRAFT/UNMERGED, based on unchanged G3E exact HEAD `9125043e24b8f13232e41193ba5fe10e1821f7cd`.
- Exact-head CI #1376 / `42348837445`: both jobs SUCCESS; 134 Python tests, app/installer unit tests, release builds and lint-vital PASS.
- APK diagnostic 1,620,193 bytes (+2,820 bytes / approximately 0.17% vs G3E); informational under D-023, no artifact published.
- Canonical evidence: [G3F audit](UI_REFRESH_G3F_AUDIT_2026-11-07.md) and [G3F checkpoint](UI_REFRESH_G3F_CHECKPOINT_2026-11-07.md).
- Physical Android TV/Mi TV Stick acceptance remains PENDING / NOT RUN. No APK install/publication, ADB, physical-device mutation, merge or integration/main change. Next: separately approved G4 integration/reconciliation planning.


## Home Screen cross-audit and header refinement — 8 November 2026

- Current implementation candidate: PR #79, branch `fix/home-balanced-frame-001`, exact HEAD `97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5`.
- Owner-approved Home safe frame is **29dp on all four edges** (58px at the Mi TV Stick's 320dpi logical density); preserve existing Dock/Grid sizing and behavior.
- HOME-001–HOME-005 source remediation and regression coverage are complete: Now Playing paused/buffering session retention; visible prayer timetable activation/focus routes; generation-guarded deferred Dock/Grid focus; composed Weather/AQI focus animation; and due-gated atmospheric overlay refresh with preference guard.
- Exact-head CI #1581 / run `42486917618`: both required jobs SUCCESS; release build SUCCESS; APK diagnostic size 1,627,577 bytes; test artifact #13620161574. Companion release workflow #36 / run `42486917728`: SUCCESS.
- Status: **SOURCE + EXACT-HEAD CI PASS; WHOLE-HOME DEVICE ACCEPTANCE PENDING**. PR #79 and dependency PR #77 remain OPEN / DRAFT / UNMERGED. No APK installation, ADB, device setting/default-launcher mutation, or physical TV test.
- Detailed evidence: [Home Screen cross-audit](GTVH_HOME_SCREEN_CROSS_AUDIT_001.md).


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


## Latest Home Header refinement — weather/AQI pill width (8 Nov 2026)

Canonical checkpoint: [GTVH-CHECKPOINT-LATEST.md](GTVH-CHECKPOINT-LATEST.md). The active task `GTVH-HOME-WEATHER-PILL-WIDTH-REFINEMENT-001` fixes the weather/AQI capsule width without changing the location/altitude reference. Source PR #79 remains OPEN / DRAFT / UNMERGED.

- Root cause: `applyHeaderWidths()` assigned 34% of measured Header width to `locationPill` and 44% to `weatherAirQuality`; both XML views already align to the same parent-start edge.
- Fix: calculate the existing location width once (`Math.round(width * 0.34f)`) and apply that exact value to both views. No margin, padding, X-position, vertical geometry, font, icon, radius, or data-provider changes.
- Source commit: `3c9821864a08bb50f6f1b13473964f8d5b5baeb7`; regression-test commit / current PR #79 HEAD: `65544d9eea9bd0a5ae5156a9bd58724b1817fd2a`.
- Regression contract checks shared width assignment, common left anchors, weather single-line/end ellipsis, and confirms weather content refresh does not rewrite layout parameters.
- CI #1587 / run `42489445766`: Lists/tools/docs job PASS; Android unit-test + release-build job IN PROGRESS at documentation sync. No CI PASS claim yet for the Android job.
- No APK installation, ADB, physical TV validation, force-push, PR merge, or device setting changes. Rendered 1920×1080 / 320dpi acceptance remains pending.


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
