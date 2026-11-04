# Glass TV Home — Project Documentation

**Documentation set:** Project Master  
**Initial release:** v0.1-draft  
**Prepared:** 31 October 2026  
**Repository:** https://github.com/Frangga11/glass-tv-home  
**Current project checkpoint:** G01 HOST/SOURCE PASS; G02 HOST BUILD + ARTIFACT PASS; G03 physical Mi TV Stick acceptance OPEN/BLOCKED pending explicit readiness approval and device connection.
**Current stage:** Project G — G03 Mi TV Stick installation and physical acceptance. G04 documentation closure and G05 ChatGPT handover follow after G03.
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
