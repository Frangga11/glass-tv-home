<!-- CURRENT AUTHORITATIVE HANDOVER UPDATE — 9 NOVEMBER 2026. Supersedes previous restart instructions below; history retained. -->
## CURRENT RESTART — GTVH-GLOBAL-FROSTED-SILVER-GLASS-001

**Start here:** read `docs/project-management/GTVH-CHECKPOINT-LATEST.md` on `docs/project-master-inventory`. It is the only authoritative status/restart point.

**Active source:** PR #104 OPEN / DRAFT / UNMERGED; branch `feature/global-frosted-silver-glass-001`; current HEAD `e6712777ca3ecd5c3e1714c5e2725aa9c5cddc3e`. Base PR #103 exact SHA `a3d697c793e23a59f9f9953e2074d83daa210cf9`. Last source-code CI PASS #1703 on HEAD `03408e7585e1cacd3fe32727502bca61752c5438`; current head adds only audit markdown. Keep ancestry; do not retarget, force-push, merge or install.

**Automated evidence:** CI #1703 PASS; 223 Python tests passed, 1 skipped; Android unit tests and release build successful. APK 1,627,069 bytes; SHA256 `e146f9e80c333acbb72c34bb64f96b2be385583e60400239ab04b5c6f5474671`. APK is archived for later acceptance, not immediate device testing.

### Immediate next steps
1. Complete the remaining G1 sweep of drawable, values, layout and dynamically generated UI surfaces.
2. Complete G3 prototype visual review with matched 1920×1080 captures on bright, dark and mixed wallpapers.
3. Continue G4 in small resource families; preserve IDs, listeners, actions, focus graph, geometry, data and Arabic RTL.
4. G5 reconcile shared resources and search for stale blue/white focus treatments.
5. G6 run functional, visual, DPAD, typography and performance tests.
6. Only after G1/G4 completion and explicit owner authorization, obtain physical Mi TV Stick acceptance.
7. Synchronize all six project documents in one pass and update this checkpoint.

**Current gates:** source audit PARTIAL; design system IN PROGRESS; build/CI PASS; functional, visual, runtime DPAD and performance QA NOT RUN; device acceptance PENDING. Keep PR #104 Draft and unmerged. No installation or default launcher change without explicit owner approval.

**Audit/design report:** `GTVH-GLOBAL-FROSTED-SILVER-GLASS-001-AUDIT.md` on PR #104 branch.

---

<!-- CURRENT AUTHORITATIVE HANDOVER UPDATE — 9 NOVEMBER 2026. Supersedes previous restart instructions below; history retained. -->
## CURRENT RESTART — GTVH-GLOBAL-FROSTED-SILVER-GLASS-001

**Start here:** read `docs/project-management/GTVH-CHECKPOINT-LATEST.md` on `docs/project-master-inventory`. It is the only authoritative status and restart point.

**Active source:** PR #104 OPEN / DRAFT / UNMERGED, branch `feature/global-frosted-silver-glass-001`, current HEAD `03408e7585e1cacd3fe32727502bca61752c5438`. Base PR #103 exact SHA `a3d697c793e23a59f9f9953e2074d83daa210cf9`. Last source-code validated HEAD `7535a311639c07ab77abd004664510fca216b00b`. The latest commit only updates the audit report. Keep ancestry; do not retarget, force-push, merge or install.

**Automated evidence:** CI #1702 PASS on source HEAD 7535a311; 223 Python tests passed, 1 skipped; Android unit tests and release build successful. Current docs-only HEAD CI #1703 is in progress. APK size 1,627,069 bytes; SHA256 `e5e5dfbcff4a2944c4bc8d3be38deb27743b1b358b542bf82a6a0effab530054`.

### Immediate next steps
1. Confirm exact-current-head CI #1703.
2. Continue G1 sweep of all remaining drawable, values, layout and dynamically generated UI surfaces.
3. Complete G3 prototype visual review with matched 1920×1080 captures on bright, dark and mixed wallpapers.
4. Continue G4 in small resource families. Preserve IDs, listeners, actions, focus graph, geometry, data and Arabic RTL.
5. G5 reconcile shared resources and search for stale blue/white focus treatments.
6. G6 run functional, visual, DPAD, typography and performance tests; physical Mi TV Stick acceptance is separate and must be owner-tested.
7. Synchronize all six project documents in one pass and update this checkpoint.

**Current gates:** source audit PARTIAL; design system IN PROGRESS; build/CI PASS on source HEAD 7535a311 and current docs-only head run #1703 IN PROGRESS; functional, visual, runtime DPAD and performance QA NOT RUN; device acceptance PENDING. Keep PR #104 Draft and unmerged. No installation or default launcher change without explicit owner approval.

**Audit/design report:** `GTVH-GLOBAL-FROSTED-SILVER-GLASS-001-AUDIT.md` on PR #104 branch.

---

<!-- CURRENT AUTHORITATIVE HANDOVER UPDATE — 9 NOVEMBER 2026, 17:01 WIB. Supersedes previous restart instructions below; history retained. -->
## CURRENT RESTART — GTVH-GLOBAL-FROSTED-SILVER-GLASS-001

**Start here:** read `docs/project-management/GTVH-CHECKPOINT-LATEST.md` on `docs/project-master-inventory`. It is the only authoritative status and restart point.

**Active source:** PR [#104](https://github.com/Frangga11/glass-tv-home/pull/104), branch `feature/global-frosted-silver-glass-001`, exact HEAD `b34d785dced9b5e47e65adf9865b1f4deada532d`, OPEN / DRAFT / UNMERGED. Base is PR #103 branch `fix/home-wallpaper-prayer-spacing-arabic-rtl-102`, exact SHA `a3d697c793e23a59f9f9953e2074d83daa210cf9`. Keep this ancestry; do not retarget, squash via force-push, merge or install.

**Protected parent:** PR #103 remains OPEN / DRAFT / UNMERGED. Its Arabic RTL and Home spacing work must remain intact. Its CI #1689 and APK size are evidence for PR #103 only.

### Immediate next steps
1. Inspect exact-head CI #1691 / run 42599100302. If it fails, inspect the failing job/log and fix only evidenced issues.
2. Re-run exact-head CI after each correction; record final run, APK size and artifact identity.
3. Finish G1 sweep of all remaining drawable, values, layout and dynamically generated UI surfaces. Do not mark source audit PASS until every glass surface is mapped.
4. Complete G3 prototype visual review with matched 1920×1080 captures on bright, dark and mixed wallpapers.
5. Continue G4 in small resource families. Preserve IDs, listeners, actions, focus graph, geometry, data and Arabic RTL.
6. G5 reconcile shared resources and search for stale blue/white focus treatments.
7. G6 run functional, visual, DPAD, typography and performance tests; physical Mi TV Stick acceptance is separate and must be owner-tested.
8. Synchronize all six project documents in one pass and update this checkpoint.

**Current gates:** source audit PARTIAL; design system IN PROGRESS; CI IN PROGRESS; functional, visual, runtime DPAD and performance QA NOT RUN; device acceptance PENDING. Keep PR #104 Draft and unmerged. No installation or default launcher change without explicit owner approval.

**Audit/design report:** `docs/project-management/GTVH-GLOBAL-FROSTED-SILVER-GLASS-001-AUDIT.md` on the PR #104 branch.

---

<!-- CURRENT AUTHORITATIVE SYNC — 9 NOVEMBER 2026. This block supersedes older status snapshots below where they conflict. Historical entries are retained as history, not current instructions. -->
## CURRENT AUTHORITATIVE PROJECT SYNC — 9 NOVEMBER 2026

**Single source of truth:** `docs/project-management/GTVH-CHECKPOINT-LATEST.md` on `docs/project-master-inventory`. Every session starts there. This file alone governs current status, active workstream, dependency order, decisions, quality gates, and next action. Supporting documents provide detailed evidence/history and must not create competing restart points.

**Current source candidate:** PR [#103](https://github.com/Frangga11/glass-tv-home/pull/103), `GTVH-HOME-WALLPAPER-PRAYER-ZERO-GAP-ARABIC-RTL-FIX-103`; OPEN / DRAFT / UNMERGED. Branch `fix/home-wallpaper-prayer-spacing-arabic-rtl-102`; exact HEAD `a3d697c793e23a59f9f9953e2074d83daa210cf9`; base PR #101 at `08b521cadb8c913b49fce42acb4e536e3eb7e9ea`; 7 commits ahead / 0 behind; 5 changed files (2 production, 3 regression tests).

**Latest verified automation:** exact-head CI #1689 / run [42566072365](https://github.com/Frangga11/glass-tv-home/actions/runs/42566072365) SUCCESS; both required jobs passed; Gradle release build successful. APK size 1,626,637 bytes; 53,773 bytes above historical 1.5 MiB reference, diagnostic/non-blocking. No PR artifact ZIP was published for this Draft run.

**Remaining gates:** genuine rendered 1920×1080 visual review and physical Mi TV Stick acceptance are NOT RUN / PENDING. No ADB, installation, launcher-setting change, or merge. Overall PR #103 is NOT FINAL. Do not claim visual/device acceptance.

**Cumulative base:** PR #101 branch `integration/home-islamic-prayer-spacing-cumulative-101`, base HEAD `08b521cadb8c913b49fce42acb4e536e3eb7e9ea`, remains unmerged. PR #103 is a corrective child candidate; do not flatten, retarget, merge, or alter its ancestry without an explicit reconciliation and owner approval.

**Newly approved future workstream:** `GTVH-GLOBAL-FROSTED-SILVER-GLASS-001` — global visual reskin using Frosted Silver Glass + Electric Cyan DPAD Focus. Scope includes Home Screen, dock, Beranda–Profil–Pengaturan navigation, Wi-Fi/microphone, location/altitude, weather/AQI, popups/dialogs, settings/profile, prayer schedule, selected Ayah, Arabic and translation, and all other audited glass surfaces. Design/prompt approved by owner; implementation has NOT started; no branch, PR, source modification, prototype, or QA result is claimed.

**Global execution constraints:** RE-SKIN, NOT REWIRE. Preserve working IDs, listeners, events, data logic, navigation, DPAD behavior, wallpaper logic, component geometry, and prior approved work. Only change geometry where an audited, evidenced requirement is approved. Prioritize function, readability, stability, and device performance over decorative effects. Do not merge or install without the required evidence and explicit owner approval.

**Document roles:** CHECKPOINT = sole status/restart authority; MASTER_PROJECT = scope/architecture/roadmap; INVENTORY = files/components/branches/PR/dependencies; DECISION_LOG = approved decisions and constraints; AUDIT_REGISTER = evidence, findings, quality gates; HANDOVER = exact resume instructions. Update all six in the same documentation synchronization pass. Keep chronology; mark superseded statements rather than deleting history.

---

> **CANONICAL CHECKPOINT — READ THIS FIRST**  
> The only authoritative restart point is [GTVH-CHECKPOINT-LATEST.md](GTVH-CHECKPOINT-LATEST.md) on branch `docs/project-master-inventory`. Start every continuation there; do not resume from historical checkpoint blocks, old PR descriptions, or branch-local notes. This file is supporting detail/history and must not establish a competing current state.  
> **Checkpoint authority:** GitHub repository `Frangga11/glass-tv-home`; canonical docs branch `docs/project-master-inventory`; single entry file `docs/project-management/GTVH-CHECKPOINT-LATEST.md`. The latest checkpoint supersedes all older status statements below.  

## CRITICAL INTEGRATION GAP — PR #91–#93 — 9 NOVEMBER 2026

Audit silang live GitHub confirms PR #100 does not include PR #91–#93. PR #100 remains a verified partial candidate only.
- #91: Arabic RTL/right alignment for Home Shelf and full reader; Indonesian LTR/left alignment. HEAD ee8bfb34db8c00a801ac260428f52ead1c7c6662.
- #92: Islamic Shelf spacing (Arabic 1dp top, -2dp/0.92; Arabic-to-translation 0dp; translation 0dp/1.04; hint 3dp; font padding retained) and removal of prayer source labels from Home header/summary without changing calculations/fallback. Branch fix/home-islamic-shelf-ayah-spacing-and-prayer-label-001; final test commit 59d63a3c9854b646876aade98e26fc74a39d3140.
- #93: runtime shared fixed line-box width for Arabic preview and Indonesian translation; branch fix/home-islamic-shelf-arabic-right-align-corrective-001; HEAD 5b46ef0cea39fa439316e48bf85a3cbbb0944820; CI #1656 / 42529068497 passed on PR #92 ancestry only.
- Overlap with PR #100 exists in MainActivity.java, activity_main.xml and CI workflow. Do not cherry-pick blindly.
- Required action: produce a new Draft cumulative candidate with ancestry PR #84–#89 + #91–#93 + PR #100 geometry, reconcile overlaps, rerun all regression/build/artifact checks, then rendered 1920×1080 audit. No ADB, TV installation, or merge before visual gate and owner approval.
- Full evidence and preservation checklist: GTVH-HOME-CUMULATIVE-RECONCILIATION-003.md. Canonical checkpoint updated; PR #100 is not the latest complete build.

---

# CURRENT HANDOVER — CUMULATIVE HOME REFINEMENT — 9 NOVEMBER 2026

## Restart target
- Read [GTVH-CHECKPOINT-LATEST.md](GTVH-CHECKPOINT-LATEST.md) first.
- Continue from [PR #100](https://github.com/Frangga11/glass-tv-home/pull/100), branch `integration/home-spacing-capsule-height-pr89-cumulative-001`.
- Exact current source HEAD: `2de055a3b08fa9a0e5c4d2bb86d2884dbd71f43b`.
- Exact base PR #89 HEAD: `4038c08f4825f7bacf965bc6708505d3c8c30ebc`; cumulative ancestry includes PR #84–#89.
- PR #100 is OPEN / DRAFT / UNMERGED. Keep PR #84–#89 and #99 open/draft/unmerged too.

## Work already done
- Reapplied PR #99's three production-file changes, seven regression-test changes, and CI artifact allowlist adjustment onto PR #89.
- Reconciled the overlapping shelf test while retaining PR #84's single-anchor regression.
- Added PR #100 to the Home layout APK artifact allowlist.
- Candidate diff: 2 commits ahead, 0 behind exact PR #89 base; 11 changed files.
- Exact-head CI #1677 / run `42533185178`: both jobs SUCCESS; Python 208 tests (1 skipped); Android unit tests + release build PASS. APK 1,626,857 bytes; artifact #13638675606 ZIP SHA-256 `e1428796d54847cd7e33c5c761b7339ad76c6eea004b8e26c39f6499d9502521`; extracted APK SHA-256 `b7ae8aa3919e0818b0879b8996427ea6eab9b91f14bf0fcff84b80fad32f292a`. See [REFINEMENT-002 audit](GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-002-AUDIT.md).

## Next steps
1. Perform rendered 1920×1080 visual review against the approved safe-frame, capsule-height and shelf-to-tray measurements.
2. If visual review passes, prepare physical Mi TV Stick acceptance; do not use ADB or install before that gate.
3. Keep PR #84–#89, #99 and #100 OPEN / DRAFT / UNMERGED until the full reconciliation and owner approval gates pass.

## Locked scope
30dp left/right/bottom, 29dp top; target capsules 30dp excluding Inputs; Home/Profile icon-label gap 2dp, Settings 4dp; wallpaper/prayer visible lower edges 30dp above measured horizontal Glass Dock. Preserve PR #84–#89 prayer, daily Ayah, Arabic typography, navigation, focus, DPAD and data behavior.

---

# CURRENT CHECKPOINT — GTVH-HOME-SCREEN-CROSS-AUDIT-001 — 8 NOVEMBER 2026

## Canonical restart point
- Active workstream: `GTVH-HOME-SCREEN-CROSS-AUDIT-001`.
- Source repository/branch: `Frangga11/glass-tv-home` / `fix/home-balanced-frame-001`.
- Exact source HEAD audited: `e9fdecdb4dd44eefeed1464fddaf8edd2e2ec5e0`.
- PR #79: OPEN / DRAFT / UNMERGED. Stacked PR #77 remains unmerged. Do not merge either PR.
- Owner-approved Header geometry remains equal 29dp safe frame on all four edges (58px at 320dpi), shared 20dp navigation capsule, and compact clock/date sizing.

## Latest exact-head CI
- CI #1560 / run `42484233880`: **SUCCESS** on exact HEAD `e9fdecdb4dd44eefeed1464fddaf8edd2e2ec5e0`.
- Both jobs passed: Lists/tools/docs; Launcher build and unit tests.
- Release build: **BUILD SUCCESSFUL**.
- APK size: **1,624,625 bytes**.
- Uploaded validation artifact: `glass-tv-home-dock-visual-parity-b85cd558f614c4282bf610a81dc1bc8827f50e6d`, 1,424,041 bytes. Artifact ZIP SHA-256: `5fa3ea60f7e727b8e4ac663e08ea7488564a70b7acb60e4c3a7040fd821b9f86`.
- This is build/source validation only; no APK installation or device test.

## Whole Home Screen cross-audit
- Dedicated audit: [GTVH_HOME_SCREEN_CROSS_AUDIT_001.md](GTVH_HOME_SCREEN_CROSS_AUDIT_001.md).
- Overall result: **PARTIAL / NOT PASS**.
- Main layout structural check: 63 unique IDs; no duplicate ID declarations. All 52 unique MainActivity onCreate ID references exist in activity_main.xml.
- Confirmed findings:
  1. **HOME-001 P2 FAIL:** Now Playing card disappears for paused/buffering media because `MediaInfo.hasMusic()` requires `isPlaying == true`, despite the manager selecting paused/buffering sessions. Home cannot resume paused playback from its card.
  2. **HOME-002 P2 FAIL (entry path):** prayer detail popup is wired only to the hidden `prayerTimes` fallback view (`GONE`); visible `prayerTable` / `prayerHeader` have no click/DPAD entry path.
  3. **HOME-003 P2 PARTIAL/RISK:** delayed Dock focus retries lack a generation/cancellation guard unlike Grid focus retries; potential stale focus steal.
  4. **HOME-004 P3 PARTIAL:** Weather pill focus-scale listener is overwritten by the later shared Header focus listener.
  5. **HOME-005 P3 FAIL:** Home atmospheric weather overlay refresh has no production entry point; the dynamic wallpaper weather effect can remain absent or stale.
- Component source matrix: clock/date, location, header tabs, Inputs/Wi-Fi/Assistant, wallpaper/hero, ayah shelf, app inventory and core Dock geometry have source-level wiring; runtime and physical acceptance remain pending.
- Existing E06 audit concerns the four controls inside the Home Screen Settings destination, not the complete main Home screen.

## Safety / acceptance gates
- Source/build CI: **PASS**.
- Static Home source audit: **PARTIAL / NOT PASS**.
- Rendered screen, actual DPAD/focus, lifecycle/resume, weather/prayer live data, atmospheric overlay refresh and Now Playing pause/resume: **PENDING**.
- Crash/error log inspection: **PENDING** (no runtime session/logcat).
- APK installation, ADB, device settings/default-launcher changes: **NOT PERFORMED**.
- No source-code changes made during this audit; only audit documentation was updated.
- PR #79 and PR #77 remain OPEN/DRAFT/UNMERGED.

## Next actions
1. Resolve HOME-001 and add paused-session regression coverage.
2. Decide and wire a visible prayer timetable popup entry point if interaction is intended.
3. Guard Dock delayed focus retries against stale requests.
4. Combine Weather focus feedback with the shared Header listener.
5. Decide whether to restore or retire the dormant Home atmospheric weather overlay refresh path.
6. Run exact-head CI after source corrections.
7. Obtain explicit owner authorization before any physical Mi TV Stick test.

Mandatory workflow: Trigger → Input → Verification → Work/Analysis → Decision Point → Output → Quality Check → Final/Not Final. Label PASS / FAIL / PARTIAL / PENDING. Never claim device acceptance from source or CI alone.


---

# HISTORICAL BASELINE CHECKPOINT — GLASS TV HOME — 8 NOVEMBER 2026, 19:42 WIB

## Canonical restart point
This is the latest authoritative checkpoint and the starting point for all future Glass TV Home discussions. Read this section first, then consult the historical entries below. Do not restart from older checkpoint notes unless explicitly asked.

### Active workstream
- Workstream: `GTVH-HOME-FINAL-CROSS-AUDIT-001`
- Main PR: [PR #79](https://github.com/Frangga11/glass-tv-home/pull/79), branch `fix/home-balanced-frame-001`, current head `acf344bc31874dfc6b680ac0bd6089b72ce1c8fa`, **OPEN / DRAFT / UNMERGED**.
- Stacked base: PR #77, branch `fix/home-layout-consolidated-001`. Keep both PRs unmerged until owner acceptance.
- Canonical docs branch: `docs/project-master-inventory`.

### Latest verified build
- CI #1545 / run `42479472690`: **SUCCESS**. Android Launcher build, `testReleaseUnitTest`, release packaging, APK diagnostics, artifact upload, and Lists/tools/docs checks all passed.
- Validated source/test commit: `c058935ed4398f5c7964d753fcf35a9cf77989bb`.
- APK: `app-release.apk`, 1,624,589 bytes.
- SHA-256: `6c7981a6c71aa5fc6f5146ba5a6023b9fdbfc527c887d1c367dc500d04fd3b10`.

### Distribution defect and definitive remediation
- User's Samsung Browser repeatedly appended `.bin` to ChatGPT sandbox attachment downloads. Local APK and its contents were valid; changing local filename or re-linking the sandbox attachment did not fix browser behavior. Treat the ChatGPT attachment delivery path as unsuitable for this APK distribution.
- Added `.github/workflows/publish-apk-release.yml` to PR #77 base and PR #79 branches. Final workflow downloads the exact CI #1545 artifact, verifies byte size and SHA-256, and uploads that unmodified file as a direct GitHub Release asset. It does NOT rebuild/re-sign the APK.
- Important audit: a first rebuild-based attempt passed Gradle but failed exact SHA comparison (rebuilt hash `298f494e48d6dac56a0613ffb76536499a1c32bb6c291299ed0fe10607f27a01` vs expected CI hash); that approach was rejected. An initial artifact lookup also used a wrong artifact name; corrected.
- Final publishing workflow run #7 / run `42480957620`: **SUCCESS**. Release asset verified via GitHub API:
  - Release tag: `glass-tv-home-ci1545-20261108` (QA prerelease)
  - Asset ID: `723889891`
  - Filename: `app-release.apk`
  - MIME: `application/vnd.android.package-archive`
  - Size: 1,624,589 bytes
  - SHA-256: `6c7981a6c71aa5fc6f5146ba5a6023b9fdbfc527c887d1c367dc500d04fd3b10`
  - Direct download: https://github.com/Frangga11/glass-tv-home/releases/download/glass-tv-home-ci1545-20261108/app-release.apk
  - Release page: https://github.com/Frangga11/glass-tv-home/releases/tag/glass-tv-home-ci1545-20261108
- Server-side GitHub asset metadata and publication are **PASS**. Actual filename received by Samsung Browser is still **PENDING owner confirmation**. Do not claim user-side download success until confirmed.

### Current quality and safety gates
- CI/build: **PASS**.
- Exact CI artifact integrity: **PASS**.
- Direct GitHub Release publication and MIME/filename metadata: **PASS**.
- Samsung Browser received filename: **PENDING**.
- Static integration audit: **PARTIAL**.
- Physical Mi TV Stick visual/DPAD/resume acceptance: **PENDING**.
- APK installation, ADB, device/default-launcher setting changes: **NOT PERFORMED**.
- PR #79 and PR #77: **DO NOT MERGE** until owner confirms direct APK download and completes physical TV acceptance.

### Next action on restart
1. Ask owner to test the direct GitHub Release APK link above and confirm the downloaded filename is exactly `app-release.apk` (no `.bin`).
2. If correct, owner may proceed with the agreed Mi TV Stick test checklist; do not remotely install or alter device settings.
3. Record physical test results and complete static cross-audit.
4. Only then reassess PR merge readiness and final QA.

### Mandatory project handling
- This checkpoint supersedes older progress summaries as the starting point.
- Continue using the agreed workflow: Trigger → Input → Verification → Work/Analysis → Decision Point → Output → Quality Check → Final/Not Final.
- Clearly label PASS / FAIL / PARTIAL / PENDING.
- Preserve user-approved scope and do not merge prematurely.
- Keep canonical documentation synchronized at each meaningful checkpoint.

---

## Historical resume checkpoint — B06 complete, 1 November 2026

- Current completed workstream: B06 Notification Feedback.
- Source branch/head: `feature/b06-notification-feedback` / `bc86a250d73a5cbb83250217d42c3c3993cf29d7`.
- PR #16: OPEN, DRAFT, UNMERGED; target `integration/b-series`. Keep unmerged under D-013 until A–G source work is complete.
- Exact-head CI run #295 (`41280874732`) passed both jobs. APK publication skipped.
- 108 Toast call sites across 10 app-layer Java files now use `TvFeedback`; resource parity is 566/566/566 across default/ID/EN with zero duplicates/mismatches.
- OTA loading is lifecycle-safe, supports unknown/known progress, safe cancellation, and partial APK cleanup. QR upload pages have progress and timeout feedback; operation-specific timeout budgets are preserved.
- Detailed record: [B06_FEEDBACK_AUDIT.md](B06_FEEDBACK_AUDIT.md).
- Next stage: B07 Activity/dialog lifecycle and return-focus audit. Do not re-open B06 unless B07 or integrated regression finds a reproducible dependency defect.
- Final API 30/33+, D-pad/focus, same-Wi-Fi phone QR, and physical TV checks remain deferred to final F-stage acceptance, not waived. No APK installed and no device settings changed.

## Historical resume checkpoint — 1 November 2026

- Current branch/head: `integration/b04-b05-reconciled` / `89d769689eb566671725c52982e0b73c4f1e8c31`; PR #15 is Draft/open. PR #13 and #14 remain source references.
- Static localization QC: 560 aligned resource keys; no duplicates, locale-key differences, or placeholder mismatches. Duplicate key detection was added to the unit test.
- App-wide source audit: 68 Java files and 17 layout XML files. 21 changed Java files with 534 `R.string` refs and 17 layouts with 71 `@string` refs; no unresolved references.
- Localization includes Settings, profile/app-lock PIN prompts, weather/location controls, avatar QR upload, wallpaper QR upload, Standby QR upload, and server-side upload errors/statuses.
- B04 exact-head CI #242 PASS. No integrated CI run/status is available for current head; no integrated build/test pass is claimed. Local substitute build unavailable (no Gradle/Android SDK; network DNS unavailable).
- Remaining mandatory gates: exact-head integrated CI; API 30 and API 33+ locale/configuration lifecycle; Activity/DreamService recreation and focus/tab retention; QR upload from real phone on same Wi-Fi; physical F02/F06 acceptance.
- Do not merge PR #15 or publish/install APK until all gates pass. No TV settings/packages changed.


## Final source checkpoint — 1 November 2026

- Integration head: `89d769689eb566671725c52982e0b73c4f1e8c31`; PR #15 is OPEN, DRAFT, and GitHub reports `mergeable_state=clean`. PR #13 and #14 are also OPEN/DRAFT. No merge has occurred.
- Localization static QC: 560 keys in default/ID/EN; zero duplicate keys, locale key differences, or placeholder mismatches. Duplicate-key rejection is now part of `LocalizationResourceParityTest`.
- App-wide scan: 68 Java files and 17 layout XML files; 21 changed Java files with 534 `R.string` refs and 17 layouts with 71 `@string` refs; zero unresolved references.
- B04 exact-head CI #242 passed. There is still no Actions run or combined status for the exact integration head. Do not treat `mergeable_state=clean` as a build/test result.
- Remaining gates are external: integrated CI; API 30/API 33+ runtime lifecycle/focus checks; phone-on-same-Wi-Fi QR upload verification; physical F02/F06 TV acceptance. No APK was published/installed and no TV settings were changed.


## Resume checkpoint — CI trigger and integration cleanup, 1 November 2026

- Current integration branch/head: `integration/b04-b05-reconciled` / `5e7481c4902c85352b1ab95b7f1ef41acdf9ed78`.
- PR #15: OPEN, Ready for review, mergeable=true; not merged. PR #13 and #14 remain source PRs; no merge has occurred.
- Root cause found: the previous `pull_request` workflow used default activity types, which omit `ready_for_review`; Draft → Ready did not start CI.
- CI workflow now explicitly includes `opened`, `synchronize`, `reopened`, and `ready_for_review`; push validation also includes `integration/**` branches. Updated B04 base was merged into the integration branch with a two-parent commit, preserving history and restoring branch ancestry.
- Exact-head checks are still absent: no check runs or combined statuses are reported for `5e7481c4902c85352b1ab95b7f1ef41acdf9ed78`. Do not claim integrated CI success. B04 source CI #242 remains the only verified CI pass.
- Remaining gates: integrated CI evidence; Android API 30/API 33+ runtime lifecycle and focus; real-phone QR upload on same Wi-Fi; physical F02/F06 TV acceptance.
- No APK published/installed; no TV settings changed. Do not merge until every gate passes.


## Execution sequence update — 1 November 2026

- User confirmed: run CI now; defer physical device/TV tests until the final overall system test after B06/B07 and remaining planned B-series work.
- CI remains the immediate gate for B04/B05 integration. Exact-head workflow has not started; GitHub check-runs and combined status are empty for `5e7481c4902c85352b1ab95b7f1ef41acdf9ed78`.
- Workflow trigger configuration is corrected. Automatic PR reopen did not create a run; manual workflow dispatch is required from GitHub Actions → CI → Run workflow → branch `integration/b04-b05-reconciled`, with `publish_device_apk=false`.
- Once exact-head CI passes, finish the B04/B05 merge chain and proceed to B06/B07. Do not claim CI success before the run is green.
- Final system test (deferred, not waived): Android API 30/33+ lifecycle and focus; phone QR upload on same Wi-Fi; physical TV F02/F06. Keep a consolidated final acceptance matrix for the end of the B-series.

## B04/B05 integration completion — 1 November 2026

- Manual CI dispatch is now available from the default branch: `.github/workflows/ci.yml` on `main` includes `workflow_dispatch` (commit `6eaf787c56226606b1d2e650c59540c553756c62`). The user dispatched CI with `publish_device_apk=false`.
- Integrated B04+B05 CI passed on exact integration head `5932cf60e19640bc5fc648f43105f86c8752813c`: workflow run #254 (`41277076284`, PR event) and matching push run (`41277072693`) both SUCCESS. Jobs `Lists, tools and docs` and `Launcher build and unit tests` both passed. APK publish step was skipped; no device APK was published.
- CI exposed and fixes were committed on `integration/b04-b05-reconciled`: Android string apostrophe escaping in default/ID/EN resources; `AppUpdateManager` progress message now passes a localized `CharSequence`; `LocalizationResourceParityTest` compares placeholder lists with `List<String>`. Final integration head is `5932cf60e19640bc5fc648f43105f86c8752813c`.
- PR #15 merged into `feature/b04-responsive-layout` as `ec5ff4fce06fe0d2daf0e3fc49fe84fef9ad9838`. PR #13 then passed CI on exact head `ec5ff4fce06fe0d2daf0e3fc49fe84fef9ad9838` (run #256 (`41277353285`), SUCCESS) and merged into `feature/b03-dialog-opacity-layering` as `d7b76c4a22c9eadffb8171a616283f3ad647d488`.
- PR #14 is also CLOSED and GitHub reports it MERGED at `73817ce41e4ae5b034922f190052a95ac45886d9`; final B03 branch head after the merge chain is `d7b76c4a22c9eadffb8171a616283f3ad647d488`. Treat this final branch head as the current B-series continuation point.
- Exact CI evidence: https://github.com/Frangga11/glass-tv-home/actions/runs/41277076284 and https://github.com/Frangga11/glass-tv-home/actions/runs/41277353285.
- Deferred, not waived: Android API 30 and API 33+ lifecycle/configuration; Activity/DreamService recreation and focus/tab retention; real-phone QR upload on same Wi-Fi; physical TV F02/F06 acceptance. Perform these in the final overall system test after B06/B07 and remaining planned B-series work.
- No APK was published or installed; no device/TV settings were changed. B04/B05 merge chain is complete; next workstream may proceed to B06/B07.

## Historical resume checkpoint — B06 audit started (superseded by latest checkpoint above)

- B04/B05 integrated CI and merge chain completed; exact CI evidence is recorded in this file and the audit register.
- Current continuation head: `feature/b03-dialog-opacity-layering` / `d7b76c4a22c9eadffb8171a616283f3ad647d488`.
- Active B06 branch created: `feature/b06-notification-feedback`, based exactly on that continuation head.
- B06 baseline audit scanned 63 launcher Java files. Feedback is scattered across Toasts, AlertDialogs, one OTA ProgressDialog, and independent QR-page JavaScript statuses; no shared feedback helper/Snackbar was found. Transport timeouts differ by operation and must not be globally flattened.
- Next: complete B06 feedback inventory by grouping transient/info/success/error/loading/timeout and QR web states; design one consistent TV-safe, locale-aware pattern; implement with focused tests and exact-head CI. B07 remains NOT STARTED.
- Final runtime/phone QR/physical TV tests remain deferred to final overall B-series acceptance, not waived.


## Approved merge and quality policy — 1 November 2026

- User approved permanent policy GTVH-MQP-001 v1.0 in `docs/project-management/MERGE_QUALITY_POLICY.md`. It is mandatory for all later feature work and bug fixes.
- Two merge levels: stage PRs merge into rolling `integration/b-series` only after impact/source audit, regression protection, static/resource QC, required tests, and exact-head CI pass; final integrated result merges to `main` only after full regression/final acceptance, release review, and explicit user approval.
- `integration/b-series` was created from continuation SHA `d7b76c4a22c9eadffb8171a616283f3ad647d488`. Existing B06 branch `feature/b06-notification-feedback` has the same base; its PR target is `integration/b-series`.
- At the time this checkpoint was written, B06 source implementation had not started; the current state is recorded in the latest checkpoint above.
- For future bugs, use reproduce → fault boundary → impact map → scoped dependency audit → root-cause fix → regression test → exact-head verification → evidence/rollback record. Expand audit scope when global/shared dependencies or multi-module symptoms warrant it; do not repeat unrelated audits.
- No merge on missing/stale evidence. Device/runtime/phone/TV acceptance remains deferred to final acceptance, not waived.


## Updated merge timing decision — 1 November 2026

- User revised merge timing: starting with B06, defer stage merges until the A–G audit/fix work is complete. This preserves flexibility for cross-stage changes discovered during C–G.
- Policy GTVH-MQP-001 v1.1 and Decision D-013 supersede D-012 only on merge timing; source audits, regression, static QC, builds, and exact-head CI remain mandatory throughout development.
- Do NOT merge B06 or later stage branches into `integration/b-series` yet. That branch is reserved for final grouped integration.
- Keep work isolated and traceable in dependency-ordered feature branches/Draft PRs. Run verification continuously; only merge is deferred.
- End sequence: freeze exact branch heads → combined diff/dependency audit → grouped merges into `integration/b-series` → exact-head CI and cross-group regression → final acceptance → explicit approval → merge to `main` → build final APK from exact approved main SHA.
- If physical acceptance needs an APK before final release, label it as a test candidate built from the frozen integration head; it is not the final release APK.
- B06 implementation remains not started; only baseline audit is complete. Retain its existing branch; do not target integration until grouped integration is authorized.


## Historical resume checkpoint — B07 implementation and CI, 1 November 2026

- B07 scope: Settings menu/submenu, exact D-pad focus, and sidebar/right-pane scroll restoration after dialog/Activity recreation.
- Source branch: `feature/b07-menu-focus-restoration`; Draft PR #17: https://github.com/Frangga11/glass-tv-home/pull/17.
- Dependency is explicit: B07 is stacked on B06 exact head `bc86a250d73a5cbb83250217d42c3c3993cf29d7`; PR #17 base is `feature/b06-notification-feedback`.
- Current B07 source head at checkpoint: `d4eabc48e020ab6cbef54e0f8884cf125eff97ed`.
- Confirmed defect: Settings tab persisted but dialog reopening always requested focus on selected sidebar; exact right-pane focus, sidebar cursor, and scroll offsets were not restored.
- Implemented: persist focus area, bounded child-index path plus target signature, active tab, left sidebar scroll, and per-tab right-pane scroll. Invalid/stale paths safely fall back to the selected sidebar destination.
- Added Android-free `SettingsFocusPath` codec and four JVM tests. PR diff limited to `ModernSettingsDialog.java`, `SettingsFocusPath.java`, and `SettingsFocusPathTest.java`.
- Exact-head GitHub Actions CI run #298 / `41281838510` was IN PROGRESS at this checkpoint on the exact head above. Do not report PASS until both jobs finish successfully.
- B07 is IMPLEMENTED / VERIFICATION IN PROGRESS, not yet paripurna. Continue by checking run #298 and logs, inspect any failure, fix on the same branch, and rerun exact-head CI.
- Required deferred acceptance: API 30/API 33+ Activity recreation, D-pad return-focus and nested dialog behavior, physical TV submenu/scroll tests, then cross-B regression against B01–B06 during grouped integration.
- PR #17 remains Draft/open/unmerged. Do not merge into `integration/b-series` before A–G source work is frozen and grouped integration is approved under D-013.
- Detailed record: [B07_MENU_FOCUS_AUDIT.md](B07_MENU_FOCUS_AUDIT.md).


## Historical resume checkpoint — B07 source and CI complete, 1 November 2026

- B07 source branch/head: `feature/b07-menu-focus-restoration` / `d4eabc48e020ab6cbef54e0f8884cf125eff97ed`.
- Draft PR #17 remains OPEN/DRAFT/UNMERGED and is stacked on B06 exact head `bc86a250d73a5cbb83250217d42c3c3993cf29d7`.
- Confirmed defect fixed: Settings reopened with the selected sidebar item regardless of the last focused submenu control; exact sidebar/right-pane focus and scroll offsets were missing.
- B07 now persists focus area, active tab, bounded child-index path + target signature, sidebar scroll, and per-tab right-pane scroll. Stale paths fall back safely to the selected sidebar destination.
- Added four JVM tests in `SettingsFocusPathTest`.
- Exact-head CI #298 / `41281838510` PASS on `d4eabc48e020ab6cbef54e0f8884cf125eff97ed`: both jobs SUCCESS; `testReleaseUnitTest` and `assembleRelease` succeeded; release APK size 1509 KB (<1.5 MiB); APK publication skipped.
- B01–B07 source/CI evidence is now synchronized in `AUDIT_REGISTER.md`. B01/B02/B03/B06 remain Draft/unmerged under D-013; B04/B05 are merged into the B03 continuation history; B07 is stacked on B06.
- **Project B source/CI development is complete enough to proceed to Project C.** Do not merge stage PRs yet. Grouped integration, combined-diff audit, cross-B regression, API 30/33+ runtime, and physical TV acceptance remain required after A–G source work is complete; deferred is not waived.
- No APK was published or installed and no device/TV settings were changed.
- Detailed record: [B07_MENU_FOCUS_AUDIT.md](B07_MENU_FOCUS_AUDIT.md).


## Historical resume checkpoint — C03 source and CI complete, 1 November 2026

- **Current next task:** C04 — audit HTTP endpoints, routing, methods, and response codes.
- C01 branch/head: `feature/c01-local-communication-audit` / `bba0801c9eb61ab27ae5046379c7a50aabebf570`; Draft PR #18; exact-head CI #314 PASS.
- C02 branch/head: `feature/c02-tv-local-server-ip` / `b0e1ff048e36bf21f8da1e165a427075f85a7cb3`; Draft PR #19; exact-head CI #333 PASS (run #334 also SUCCESS).
- C03 branch/head: `feature/c03-phone-tv-network-verification` / `d266287df287167e5a8e73fd675b51759b580ed6`; Draft PR #20; exact-head CI #338 / `41287563951` PASS. PR #20 base is C02 exact head.
- C03 fixed the active network selection used by Avatar, Wallpaper, and StandBy QR uploads. The old resolver selected the first `wlan*`/`eth*` interface; the new shared `LocalNetworkAddress` resolver uses Android's active default Wi-Fi/Ethernet network and its LinkProperties IPv4. No fabricated fallback address is returned.
- Existing QR URLs still use the actual bound address and port. Same-Wi-Fi instructions, tokens, independent routes, upload limits, and operation-specific timeouts are preserved.
- Added `LocalNetworkAddressTest` for IPv4 eligibility, loopback/link-local/unspecified/multicast/IPv6 rejection, and invalid prefix rejection.
- CI #338 passed both jobs; launcher `testReleaseUnitTest` and `assembleRelease` succeeded. APK diagnostic: 1,546,045 bytes (<1.5 MiB); artifact publication skipped.
- **Not complete:** real phone/TV same-network reachability and Wi-Fi/Ethernet/VPN/reconnect behavior remain deferred to F-stage final acceptance; not waived.
- PRs #18–#20 remain OPEN/DRAFT/UNMERGED under D-013. No merge, APK publication/installation, or device-setting change.
- C03 detailed report: `C03_PHONE_TV_NETWORK_AUDIT.md` on the feature branch.


## Historical resume checkpoint — C04 source and CI complete, 1 November 2026

- **Next task:** C05 — audit shared upload/receive-file pipeline and repair only demonstrated shared pipeline defects.
- C01: `feature/c01-local-communication-audit` @ `bba0801c9eb61ab27ae5046379c7a50aabebf570`; Draft PR #18; CI #314 PASS.
- C02: `feature/c02-tv-local-server-ip` @ `b0e1ff048e36bf21f8da1e165a427075f85a7cb3`; Draft PR #19; CI #333 PASS.
- C03: `feature/c03-phone-tv-network-verification` @ `d266287df287167e5a8e73fd675b51759b580ed6`; Draft PR #20; CI #338 PASS.
- C04: `feature/c04-http-endpoint-routing` @ `f09ee1b30b80e45e793c1678935eff8867aedae4`; Draft PR #21 targets C03 exact head; exact-head CI #342 / `41288521501` PASS, both jobs.
- C04 added strict request-line parsing, complete-header checks, exact endpoint/method contracts, 404/405/413 behavior, deterministic API failure responses, duplicate/missing query rejection, and browser-side HTTP status handling. Added parser/route tests and localized API failure feedback.
- Release APK diagnostic: 1,547,845 bytes (1.5 MiB cap 1,572,864 bytes), only 15,019 bytes of headroom. APK publication skipped; avoid unnecessary growth in later stages.
- C01–C04 PRs remain OPEN/DRAFT/UNMERGED under D-013. No APK published/installed and no device settings changed.
- Physical phone/TV QR and browser acceptance remains deferred to F-stage, not waived.
- C04 detailed evidence: `C04_HTTP_ENDPOINT_AUDIT.md` on the C04 feature branch.


## C04 final exact-head verification — 1 November 2026

- Latest C04 feature branch head: `9094969d968c6fffef6598e1e8c5a7bd62e5efa8`.
- Exact-head CI #343 / `41288644099`: PASS; both `Launcher build and unit tests` and `Lists, tools and docs` jobs SUCCESS. `testReleaseUnitTest` and `assembleRelease` succeeded.
- APK diagnostic remains 1,547,845 bytes; 15,019 bytes below the 1.5 MiB cap. APK publication skipped.
- PR #21 remains OPEN/DRAFT/UNMERGED; base is C03 exact head `d266287df287167e5a8e73fd675b51759b580ed6`. No merge or device acceptance claimed.


## C04 final branch-head verification — 1 November 2026

- Current C04 feature head: `aad9aeb163b46ff4c13a8d93c160a3eec78f9770`; Draft PR #21 targets C03 exact head `d266287df287167e5a8e73fd675b51759b580ed6`.
- Exact-head CI #355 / `41289178858`: PASS; both jobs SUCCESS. Launcher and installer `testReleaseUnitTest` and `assembleRelease` succeeded.
- Release APK diagnostic: 1,548,097 bytes, 14,767 bytes below the 1.5 MiB cap. APK publication skipped.
- PR #21 remains OPEN/DRAFT/UNMERGED under D-013. Physical phone/TV browser acceptance remains deferred to F-stage, not waived.
- Next task: C05 shared upload/receive-file pipeline audit.


## Historical resume checkpoint — Project D D01, 1 November 2026

- **Current task:** D01 — Config & Storage Audit. Original inventory is complete; follow-up quality review and cross-project finding are recorded.
- Branch/head: `feature/d01-config-storage-audit` @ `cd710681099faece14905faa244f57245bae84d1`; Draft PR #25 targets C07 exact head `1cc6d7033daf6f9371805f667467b8860edcfb8f`; OPEN/DRAFT/UNMERGED under D-013.
- Exact-head CI #373 / run `41293666682` passed both jobs on prior head `9a2f2aa29812acd3c24a895c1c4feed5b4cf94eb`. Final-head CI after the latest audit-document update is PENDING; do not mark D01 closed until it passes.
- D01-F01–F11 are recorded in `D01_SETTINGS_STORAGE_OWNERSHIP_AUDIT.md`. D01-F11 is the cross-project StandBy upload/persistence consistency defect; D05/D07 must repair it and add regression coverage. Do not re-audit C07 wholesale; preserve C07's verified work and use targeted impact mapping.
- No source code, stored user data, preference keys, APK, or device settings were changed. No PR merged.
- **Next action:** verify exact-head CI for `cd710681099faece14905faa244f57245bae84d1`; then close D01 with evidence and start D02.



## Project D checkpoint — D01 closed, 1 November 2026

- **Next task:** D02. D01 source audit and exact-head CI are complete.
- D01 branch/head: `feature/d01-config-storage-audit` @ `cd710681099faece14905faa244f57245bae84d1`; Draft PR #25 targets C07 exact head `1cc6d7033daf6f9371805f667467b8860edcfb8f`; OPEN/DRAFT/UNMERGED under D-013.
- Final exact-head CI #375 / `41304411195` and #376 / `41304430928`: both PASS, with both jobs SUCCESS in each run.
- D01-F01–F11 are recorded in `D01_SETTINGS_STORAGE_OWNERSHIP_AUDIT.md`. D01-F11 remains a downstream repair item for D05/D07; preserve C07's other verified upload/session work.
- No app source changes, stored data changes, APK publication/installation, merge, or device-setting changes were made during D01.
- Runtime/process-death/reboot acceptance remains deferred to F-stage and is not waived.


## Historical resume checkpoint — Project D D02 in progress, 1 November 2026

- **Current task:** D02 — ensure settings changes propagate immediately to dependent UI.
- Branch/head: `feature/d02-settings-ui-propagation` @ `3bb4ae592904a49989fd76d7dabd92ed1cebd94b`; Draft PR #26 targets D01 exact head `cd710681099faece14905faa244f57245bae84d1`; OPEN/DRAFT/UNMERGED under D-013.
- Audit report: `D02_SETTINGS_UI_PROPAGATION_AUDIT.md`. Most settings already use owner callbacks and immediate view updates; no global SharedPreferences listener/event bus is justified.
- D02-F01 repaired: StandBy photo-mode setter now honors the synchronous commit result, preserves the current UI and reports localized failure if persistence fails. Default/ID/EN resources aligned. The Settings pane's existing rerender restores the switch from persisted state.
- Exact-head CI #380 / `41305432110` and #381 / `41305453205` are IN PROGRESS on exact head `3bb4ae592904a49989fd76d7dabd92ed1cebd94b`. Verify job outcomes before declaring D02 complete.
- D01-F11 upload collection/mode atomicity remains assigned to D05/D07; do not alter unrelated C07 upload/session hardening.
- No APK published/installed, no TV/device settings changed, no merge. Physical TV acceptance remains in F-stage.
- **Next action:** check CI #380/#381 and close D02 only after exact-head PASS; then start D03 locked-app count/list refresh audit.


## Historical resume checkpoint — Project D D02 closed, 1 November 2026

- **Next task:** D03 — verify locked/hidden app counts and lists refresh immediately in the open Settings pane.
- D02 branch/head: `feature/d02-settings-ui-propagation` @ `2dbfcc181651184914422ad0697a3c5c27d15614`; Draft PR #26 targets D01 exact head `cd710681099faece14905faa244f57245bae84d1`; OPEN/DRAFT/UNMERGED under D-013.
- D02 report: `D02_SETTINGS_UI_PROPAGATION_AUDIT.md`. D02-F01 repaired: StandBy photo-mode setter honors the synchronous commit result and reports localized failure without falsely refreshing/claiming success.
- Exact-head CI #382 / `41305782187`: PASS; both jobs SUCCESS. Release APK diagnostic 1,550,617 bytes (<1,572,864-byte cap); publication skipped.
- D02 source-audit and CI gates are closed. Physical TV acceptance remains deferred to F-stage, not waived.
- D01-F11 remains assigned to D05/D07 for upload collection/mode atomicity and regression tests; preserve C07 upload/session hardening.
- No merge, APK publication/installation, or device setting changes.


## Historical resume checkpoint — Project D D03 closed, 1 November 2026

- **Next task: D04 — synchronize profile/avatar state across all surfaces.** Begin with a targeted source/ownership audit and preserve the completed D01–D03 changes.
- D03 branch/head: `feature/d03-locked-app-ui-refresh` @ `c119198e5c5ecc0d3cfdc283049ea3299d0067ca`; Draft PR #27 targets D02 exact head `2dbfcc181651184914422ad0697a3c5c27d15614`; OPEN/DRAFT/UNMERGED under D-013.
- D03-F01 repaired: Profile Settings locked/hidden app counts refresh on picker dismissal; home tiles reload on both Done and Back. PIN cancellation before picker launch does not invoke picker refresh.
- Exact-head CI #385 / `41307515649`: PASS; both jobs SUCCESS; unit tests and release build succeeded. APK diagnostic 1,550,761 bytes, 22,103 bytes below the 1,572,864-byte cap; size gate PASS; APK publication skipped.
- D03 source-audit and CI gates are closed. Physical Android TV acceptance remains deferred to F-stage, not waived. No APK installation or merge.
- D01-F11 remains assigned to D05/D07. Preserve C07 upload/session hardening.

 
## Historical resume checkpoint — Project D D04 closed, 1 November 2026

- **Next task: D05 — targeted repair of D01-F11 StandBy upload persistence/HTTP-success consistency.**
- D04 branch/head: `feature/d04-profile-avatar-state-sync` @ `ad69d229a6b3cd6ca1810a595597dc88481c3ebd`; Draft PR #28 targets D03 exact head `c119198e5c5ecc0d3cfdc283049ea3299d0067ca`; OPEN/DRAFT/UNMERGED under D-013.
- D04-F01 fixed: built-in avatar selection ring now follows effective avatar state; custom-photo override hides built-in ring while preserving stored built-in avatar fallback, and default restoration reselects that fallback. Two JVM regression tests added.
- Exact-head CI #390 / `41309327872`: PASS; both jobs SUCCESS. App/installer release unit tests and Release build succeeded. APK 1,550,805 bytes, 22,059 bytes below 1,572,864-byte cap; size gate PASS; publication skipped.
- D04 source-audit and CI gates are CLOSED. Physical Android TV acceptance remains deferred to F-stage, not waived; no APK installed and no merge.
- D01-F11 remains open for D05/D07: targeted atomic/failure-aware handling of StandBy upload collection metadata and photo-mode persistence, plus truthful HTTP response and regression coverage. Preserve C07's unrelated upload/session hardening.


## Historical resume checkpoint — Project D D05 source/CI closed, 1 November 2026

- **Next task: D06.** Read the Project D roadmap for the exact D06 scope before touching code; preserve D01–D05 and do not broaden into unrelated C07 changes.
- D05 branch/head at implementation CI: `feature/d05-standby-photo-atomic-persistence` @ `835986c0e28c50311d4474f1060c73dd56104bf8`; Draft PR #29 is OPEN/DRAFT/UNMERGED under D-013, stacked on D04 exact head `ad69d229a6b3cd6ca1810a595597dc88481c3ebd`.
- D01-F11 source repair: StandBy photo collection and photo mode now use one synchronous preference commit; explicit upload outcomes prevent false success; HTTP 500 on persistence failure, HTTP 400 on validation/collection rejection, HTTP 200 only on successful persistence. Existing C07 upload/session hardening preserved.
- Regression coverage added for one-commit state update, commit failure, and HTTP response mapping.
- Exact-head CI #396 / `41311199958`: PASS; final branch head `835986c0e28c50311d4474f1060c73dd56104bf8` CI #397 PASS on implementation head; both jobs SUCCESS, app/installer release unit tests and release build passed. APK 1,551,005 bytes, 21,859 bytes below 1,572,864-byte cap; manual publication skipped.
- D05 source and CI gates closed. F-stage process-death/reboot/physical Android TV acceptance remains mandatory, deferred, and not waived. No APK installed and no merge performed.

## Historical resume checkpoint — Project D D06 source/CI closed, 1 November 2026

- **Next task: D07.** Before coding, read the canonical roadmap and handover for exact D07 assignment and dependencies. Preserve D01–D06; do not broaden scope.
- D06 branch/head: `feature/d06-process-death-recovery` @ `548581e909139dd67b2ddc725bb546a838c75702`; Draft PR #30 targets D05 exact head `835986c0e28c50311d4474f1060c73dd56104bf8`; OPEN/DRAFT/UNMERGED under D-013.
- D06-F01 fixed: Android clears AlarmManager schedules on reboot; added non-exported BOOT_COMPLETED receiver to restore the next prayer schedule from the saved zone through existing `Azan.schedule()`. Existing master/prayer enablement and scheduling logic remain authoritative.
- Added four JVM tests for boot dispatch, unrelated action, blank zone, and missing scheduler.
- Exact-head CI #398 / `41311958235`: PASS; both jobs SUCCESS. App/installer release unit tests and Release build passed.
- APK exact size **1,551,217 bytes**, **21,647 bytes** below 1,572,864-byte cap; size gate PASS; manual publication skipped.
- D06 source/CI gates closed. Process-death, force-stop and physical TV reboot acceptance remains mandatory in F-stage, deferred and not waived. No APK installed and no merge.


## Historical resume checkpoint — Project D D01–D07 source/CI closed, 1 November 2026

- **Project D source-audit and exact-head CI work is complete. Next project: E01.** Keep all Project A–G PRs OPEN/DRAFT/UNMERGED under D-013 until grouped integration and cross-project regression are approved.
- D06 final branch/head: `feature/d06-process-death-recovery` @ `78a12e9fc196a596fa98ea272edc735501047af1`; Draft PR #30 targets D05 exact head `835986c0e28c50311d4474f1060c73dd56104bf8`, OPEN/DRAFT/UNMERGED. D06-F01 reboot alarm recovery is implemented through non-exported BOOT_COMPLETED receiver delegating to existing `Azan.schedule()`. Exact-final-head CI #399 / `41312553920`: PASS; both jobs SUCCESS; APK 1,551,217 bytes, 21,647 bytes below 1,572,864-byte cap; publication skipped.
- D07 final branch/head: `feature/d07-concurrent-profile-content-state` @ `964163c918e281b68af5cf7114ca56ead2797486`; Draft PR #31 targets D06 exact head `78a12e9fc196a596fa98ea272edc735501047af1`, OPEN/DRAFT/UNMERGED.
- D07 closed three confirmed source defects: (1) Child Mode lost-update/profile-context risk, now serialized with one captured profile ID and UUID-backed new profile IDs; (2) StandBy picker/QR/clear collection conflicts, now serialized and atomically committing collection+mode; (3) stale weather/AQI responses after location/language changes, now rejected through request generations and freshness checks.
- D07 regression tests cover Child Mode profile/set behavior, StandBy merge/commit/clear, and weather request freshness. C07 upload/session protocol and D01–D06 behavior preserved; no preference schema migration, global state store, or event bus introduced.
- Exact-final-head CI #404 / `41313690556`: PASS on `964163c918e281b68af5cf7114ca56ead2797486`; both `Launcher build and unit tests` and `Lists, tools and docs` jobs SUCCESS. App/installer release unit tests and release build passed. APK exact size 1,552,197 bytes; 20,667 bytes below cap; publication skipped.
- D07 source-audit and exact-head CI gates CLOSED. Physical TV acceptance remains mandatory in F-stage: process death/force-stop/reboot/alarm recovery, profile switching and cross-profile isolation, Child Mode lock/hide, concurrent picker/QR operations, and location/language switch during refresh. Deferred, NOT waived. No APK installed or physical TV test claimed.
- Canonical audit evidence: `D07_CONCURRENT_PROFILE_CONTENT_STATE_AUDIT.md` on the D07 feature branch; D06 evidence in `D06_PROCESS_DEATH_REBOOT_RECOVERY_AUDIT.md`.



## Historical resume checkpoint — E00 documentation reconciliation, 2 November 2026

- **E00 status:** CLOSED. PR #32 merged into `docs/project-master-inventory` (merge commit `4ba80230cb9ddfbcc27657b7b0d07bed6905fc97`); exact-head CI #414 passed both jobs. PR #33 records the official closure and the CI artifact-publication guard.
- **New report:** `docs/project-management/E00_MASTER_INVENTORY_AND_DEPENDENCY_AUDIT.md`. It records the 36-branch inventory, refreshed PR states, protected B/C/D contracts, E01–E08 planning findings, F/G sequence, and explicit audit limitations.
- **Verified D07 source baseline:** `feature/d07-concurrent-profile-content-state` @ `964163c918e281b68af5cf7114ca56ead2797486`; PR #31 OPEN/DRAFT/UNMERGED; exact-final-head CI #404 / `41313690556` PASS.
- **PR inventory:** #10–#12 and #16–#31 OPEN/DRAFT/UNMERGED; B04/B05 reconciled merge history #13–#15 retained. Do not infer merge permission from source/CI closure.
- **Current phase status:** A01–A06 DONE; B01–B07, C01–C07, D01–D07 source/CI complete but final integrated/device acceptance pending F; E00 DONE — PR #32 merged and canonical documents re-verified; E01–E08, F and G not started.
- **Next action:** Begin E01 from its exact dependency heads, keeping source changes scoped and preserving all B/C/D protections.
- **Integration timing:** D-013 remains active. F01 is an isolated integration rehearsal/candidate assembly and combined-diff audit; do not merge feature PRs into the reserved `integration/b-series` branch. F02–F07 validate the candidate. G01 repairs residual findings through their owning projects. Only after all A–G source audits/fixes are complete and findings are closed or explicitly accepted may exact heads be frozen and merged into `integration/b-series` by coherent dependency groups, with exact-head CI/regression after each group. G02–G03 cover the traceable candidate/artifact and target-TV install/upgrade/rollback. Merge to `main` only after final acceptance and explicit user approval; G04–G05 finalize records and handover.
- **Safety:** no app source, feature PR, device setting or user data changed; no APK was installed or physical TV test performed. The pre-fix PR workflow uploaded two transient diagnostic artifacts (runs #414/#415); PR #33 adds a main-push-only upload guard.


## E00 official closure — 2 November 2026

- E00 documentation PR #32 was merged into `docs/project-master-inventory` (merge commit `4ba80230cb9ddfbcc27657b7b0d07bed6905fc97`).
- Exact PR head `541dd9accb66eb3b071367a3e4967769d742c2cd`; CI workflow run #414 / `41359874778`: PASS. Both `Launcher build and unit tests` and `Lists, tools and docs` jobs completed successfully. No review comments or change requests were present.
- Canonical E00 report, roadmap, inventory, audit register, operations log, master project and handover were re-read from `docs/project-master-inventory` after merge.
- E00 scope is closed: documentation/inventory/dependency reconciliation only. No application source, feature PR, APK, user data, TV setting, installation or physical-device acceptance was changed or claimed.
- **Next work unit: E01 — Device Settings & Launcher audit.** Begin with exact dependency heads and the A06 read-only HOME resolver/preferred-activity discrepancy. Preserve D-013/D-014; do not merge feature PRs early.

## Current resume checkpoint — E01 source audit closed, 2 November 2026

- **Next task: Stage E02 — Profile** (avatar QR, built-in avatar selection, focus indicators, app-lock counts and policy UI). The separate cross-project global UI consistency checkpoint is not the Stage E02 Profile module.
- **E01 source audit:** COMPLETE. Feature branch feature/e01-device-settings-home-control; source head validated at 9919dbaa0add6cc7ce2a84332354f46f9cc48d61; documentation-only feature-branch follow-up 7aad9ec4cf015dd415b3c948e2b4f80c1099955c. Draft PR #34 remains OPEN/DRAFT/UNMERGED, based on D07 exact head 964163c918e281b68af5cf7114ca56ead2797486.
- **Exact-head CI:** #602 / 41400508774 PASS. Android release build, JVM unit tests and lists/tools/docs checks passed. Diagnostic APK 1,560,565 bytes; 12,299 bytes below the 1,572,864-byte budget. No artifact published; manual TV-test APK step skipped.
- E01 removed silent Android system-setting/service writes; added read-only HOME/service status; split Android screensaver and display-timeout settings; corrected restart/power feedback; required an explicit ADB success marker before claiming system cache trim success; verified the installer QR target assets exist on main.
- E01 uses the exact B01 canonical TvDialogStyle/TvUiTokens. Do not create a competing modal or Settings style in E02; adopt B01's shared global contracts.
- **F-stage carry-forward:** HOME resolver/preferred-activities discrepancy and physical HOME key; service readback; OEM restart/power labels; actual ADB cache trim success/denied path; phone installer scan/install; remote D-pad visual/focus acceptance; ClockDream/StandBy/display-timeout interaction. These are deferred, not waived, and do not block E02 source audit.
- Keep D-013: PR #34 and all feature PRs stay Draft/open/unmerged until A–G source audits/fixes and grouped integration gates are complete. No APK installed, no TV settings or HOME default changed.

## E01 final cache-feedback accuracy correction — 2 November 2026

- This later checkpoint supersedes the earlier E01 source head and APK-size figures in this document.
- Final E01 source head: b6fa70e406afc9f6e81d16a3635a69afc6c4d783; exact-head CI #609 / 41401277076 PASS.
- Final diagnostic APK: 1,560,609 bytes; 12,255 bytes below the 1,572,864-byte cap. No artifact published; manual TV-test APK step skipped.
- Cache byte totals now count only successfully deleted app-cache/temp files; free-space deltas are no longer attributed to app-cache cleanup. User-facing no-files-removed feedback is neutral. Android system trim success requires the explicit ADB success marker. Regression tests cover marker parsing and actual deleted-byte counting.
- E01 audit report and PR #34 description updated. PR #34 remains OPEN/DRAFT/UNMERGED under D-013; latest feature branch documentation commit 090a956a9860c82eb36c2d96848d47046fba0f0c.
- E01 source audit remains COMPLETE; next Stage E task is E02 Profile. F-stage physical/OEM checks remain mandatory and unchanged. No device settings, HOME default, package state, user data or installed APK changed.


## Current resume checkpoint — E02 source audit closed, 2 November 2026

- **E02 Profile source audit + exact-head CI: COMPLETE. Next Stage E task: E03.** This closes source/CI work only; Project F still owns physical Mi TV Stick + phone + remote acceptance.
- E02 feature branch: `feature/e02-profile-avatar-lock-ui`; final head `3ab9b55f3cfe03ad9841a119590a9183cf114e96`; Draft PR #35 targets E01 branch `feature/e01-device-settings-home-control` @ `090a956a9860c82eb36c2d96848d47046fba0f0c`. PR #35 OPEN/DRAFT/UNMERGED under D-013.
- E02 corrections: merge Leanback + Launcher app queries and deduplicate package policies; defensive label/icon handling; refresh policy counts on Done/Back dismissal; atomically commit built-in avatar index + custom-photo reset.
- Retained contracts: 7 built-in avatars; profile-scoped custom photo and Child Mode/lock/hide sets; global parent PIN; PIN gates for lock/hide editing, deletion and switching away from Child Mode; five-minute single-success avatar QR session and 15 MB limit; private avatar storage; C07 and D04/D07 protections; B01 global modal/focus contract.
- Exact-head CI #623 / `41406190380`: both jobs PASS. App/installer release unit tests and Release build PASS; lists/tools/docs PASS.
- Diagnostic APK 1,561,717 bytes; budget 1,572,864 bytes; headroom 11,147 bytes. Manual publication skipped; artifact count 0.
- Default PIN `0000` and unlimited retries remain as explicitly approved. Do not force PIN change or introduce lockout.
- **Project F carry-forward:** same-Wi-Fi QR upload/replacement/retry/expiry/revocation; custom/built-in avatar identity and cleanup across profiles; Child Mode and PIN authorization; lock/hide functionality and counts; D-pad focus, Back/cancel, scroll/clipping and focus return; wallpaper/StandBy/avatar route and storage isolation. Deferred, NOT waived.
- No APK installed, no TV settings/HOME default changed, no data migration, and no physical acceptance claimed. Do not merge PR #35 early; keep D-013 grouped integration policy.
- Canonical report: [E02_PROFILE_AVATAR_LOCK_UI_AUDIT.md](E02_PROFILE_AVATAR_LOCK_UI_AUDIT.md).


## Resume checkpoint — E03 wallpaper metadata work in progress (2 November 2026)

- Current E03 branch: `feature/e03-wallpaper-clean-metadata`; PR #36 OPEN/DRAFT/UNMERGED; base E02 exact head `3ab9b55f3cfe03ad9841a119590a9183cf114e96`.
- Confirmed issue: the Default JPEG contains baked-in title/description; the app already has native `heroShelf` metadata UI.
- Source checkpoint head: `188bd49269a358ebf53f8ae09056c41a5eb80243`. Added exact cached-Default filename metadata provider and fallback to existing online/place metadata providers; tests added for non-leakage to custom/other files.
- CI #624 / run `41408606965`: lists/tools/docs PASS; launcher build/unit-test was still running at last check.
- **Blocking:** packaged JPEG is not yet replaced; the clean text-free image binary is not in the branch. Visual issue remains open. Do not merge, publish APK, install APK or mark E03 complete.
- Preserve B01 UI/focus contract, online/place providers, custom wallpaper suppression, shuffle/index/interval, QR upload and StandBy isolation. Physical checks remain in Project F.
- Next: finish exact-head CI; commit clean asset; add regression checks for Default/online/custom/description toggle/Next/shuffle/restart; rerun CI and APK-size gate; then visual/device acceptance in F.
- Canonical report: [E03_WALLPAPER_METADATA_AUDIT.md](E03_WALLPAPER_METADATA_AUDIT.md).

- CI #624 / `41408606965` subsequently completed **PASS** on `188bd49269a358ebf53f8ae09056c41a5eb80243`; APK 1,561,797 bytes (11,067 bytes below cap); publication skipped. This does not close E03 because the clean JPEG binary is still not committed and visual regressions remain outstanding.


## E03 asset + CI checkpoint — 2 Nov 2026

This checkpoint supersedes earlier statements that the clean Default wallpaper binary was not committed.
- Clean asset is now committed at `launcher/app/src/main/assets/kabut-pagi-dempo.jpg` on `feature/e03-wallpaper-clean-metadata`.
- Upload commit: `d6676c03474396c0f616284a3e43fc709226ed82`; exact PR head: `d6676c03474396c0f616284a3e43fc709226ed82`; PR #36 remains OPEN/DRAFT/UNMERGED, base `feature/e02-profile-avatar-lock-ui`.
- Git blob SHA `a73eb03584a706870e7b81447331ee3e47170e06` matches the locally prepared image byte-for-byte. Verified JPEG: 1920x1080, RGB, 230,997 bytes; filename and asset path match `MainActivity.getBundledDefaultWallpaper()` and cached metadata key `kabut-pagi-dempo-default.jpg`.
- Exact-head CI #625 / run `41410370222` on `d6676c03474396c0f616284a3e43fc709226ed82`: **SUCCESS**. Launcher build/unit tests and Lists/tools/docs both passed; `testReleaseUnitTest` and `assembleRelease` passed.
- Release APK: `1,561,913 bytes`; cap `1,572,864 bytes`; headroom `10,951 bytes`. Size gate passed. Manual TV-test APK publication was skipped; no APK was published or installed.
- Source path audit: Default copy uses the packaged asset and writes the exact cached filename expected by `WallpaperMetadata`; setPlace consults Default metadata first and preserves Place/Online fallback. Custom wallpaper and disabled-description suppression remain in the existing path. No B01 UI/focus, rotation/shuffle/index/interval, QR upload, StandBy or user-preference contract was intentionally changed.
- Remaining E03 acceptance: regression verification for Default/online/custom/description-toggle/Next/shuffle/restart and actual Mi TV/remote/phone visual/performance acceptance in Project F. QR/StandBy isolation remains to be included in integration checks. No physical device acceptance is claimed.
- Disposition: asset integration and CI are **PASS**; E03 overall remains **IN PROGRESS / NOT FINAL** until remaining regression and Project F acceptance. Preserve D-013: no early merge, no release approval.


## Offline Default wallpaper verification — 2 Nov 2026

- Source audit confirms Default wallpaper and its descriptive metadata are **offline-capable**: `getActiveWallpaperCategory()` defaults to `Default`; the Default startup branch calls `getBundledDefaultWallpaper()`; that method reads `kabut-pagi-dempo.jpg` from APK assets and stages it into app-private storage as `kabut-pagi-dempo-default.jpg`. It does not make a network request.
- `WallpaperMetadata.describe()` returns the title, description and location from hard-coded local metadata for the exact cached filename. `MainActivity.setPlace()` checks this provider before the Place/Online providers. No HTTP, feed, or online service is required for the Default description.
- The wallpaper and description therefore remain available after installation with Wi-Fi/internet disconnected, provided the installed APK contains the asset and the app can read/write its own files directory. A fresh install still requires the APK itself to have been installed first; no server fetch is involved at runtime.
- Visibility conditions remain unchanged: wallpaper descriptions must be enabled, and the existing hero shelf/focus behavior controls when the text is shown. Offline availability does not override the user's description setting.
- Scope caveat: online/place refreshers and other network-backed widgets/features may independently attempt background refreshes when configured. They are not dependencies for the Default asset or its metadata. This audit is a source-path verification; offline behavior has not yet been physically exercised on the Mi TV Stick. Add Wi-Fi-off cold-start and restart acceptance to Project F.
- Disposition: source-level offline dependency audit **PASS**; physical offline acceptance **PENDING Project F**. No source change was necessary; do not refactor the working Default path without a reproducible defect.


## E03 final source audit checkpoint — 2 Nov 2026

- Re-audited Default startup, bundled asset copy/cache, local metadata lookup, online/place metadata fallback, custom wallpaper suppression, description toggle, Next/shuffle index handling, interval scheduling and restart persistence.
- Found and fixed a repeat-selection UI edge case: selecting Default while Default was already active hid the hero shelf before re-applying the same metadata; the unchanged-title short-circuit could leave it hidden. Removed the premature hide in the Default branch so the existing hero state remains stable and metadata changes continue through the established `setPlace()` transition. No B01 UI implementation, focus contract, or wallpaper rotation algorithm was changed.
- Fix commit: `e085f8aed6fdc85b4793b76bb26ab8543b26b2cf` on `feature/e03-wallpaper-clean-metadata`.
- Exact-head CI #626 / run `41411445463` is currently **IN PROGRESS** at this checkpoint. Lists/tools/docs job has passed; Launcher build/unit tests and APK size gate are still running. Do not mark CI green until the final run conclusion is available.
- The prior clean asset remains 1920x1080 JPEG, 230,997 bytes; prior exact-head CI #625 passed at 1,561,913-byte APK (10,951-byte headroom). The new source commit requires its own fresh APK size result.
- Source audit disposition: code-path review complete; targeted fix committed; CI pending. Device-only acceptance (offline cold start/restart, visual sharpness/crop, focus/remote behavior, online/place/custom, Next/shuffle, QR/StandBy isolation and performance) remains assigned to Project F. No APK publication/install, no physical acceptance claim, no early merge under D-013.


## E03 exact-head CI #626 final result — 2 Nov 2026

This result supersedes the preceding note that CI #626 was in progress.
- Exact feature head: `e085f8aed6fdc85b4793b76bb26ab8543b26b2cf`.
- CI #626 / run `41411445463`: **SUCCESS**. Launcher build/unit tests and Lists/tools/docs jobs both passed.
- `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and APK budget gate all passed. Manual TV-test APK publication was skipped by workflow policy.
- Exact Release APK size: `1,561,889 bytes`; budget: `1,572,864 bytes`; remaining headroom: `10,975 bytes`.
- The targeted Default reselection hero-shelf fix is included in the tested commit. No APK was published or installed; no physical device acceptance is claimed.
- E03 source + exact-head CI disposition: **PASS / SOURCE FINAL**. Project F retains physical acceptance: offline cold start/restart, image crop/sharpness, remote/focus, Default/online/place/custom, description toggle, Next/shuffle, QR/StandBy isolation and performance. Preserve D-013: PR #36 remains OPEN/DRAFT/UNMERGED; no early merge or release approval.


## Handover — E04 StandBy / Screensaver — 2 Nov 2026

- Current task: E04 source audit/repair. Branch `feature/e04-standby-screensaver`, head `7c5e2677eac1c791675d9f5ef7d736eb47d37aca`, base E03 exact head `e085f8aed6fdc85b4793b76bb26ab8543b26b2cf`.
- Draft PR #37: https://github.com/Frangga11/glass-tv-home/pull/37 — OPEN/DRAFT/UNMERGED under D-013.
- One confirmed issue repaired: duplicate `HomeKeyService.sleepScreen()` plus shell `input keyevent 223` in `ClockDream`; now one supported Accessibility action with result logging.
- Preserve existing StandBy upload/session validation, shared store, atomic collection/mode persistence, picker merge, clear ordering and photo rotation.
- **Next:** obtain exact-head CI for `7c5e2677eac1c791675d9f5ef7d736eb47d37aca`; inspect failures if any. Then complete source/CI closure. Project F must physically test DreamService/overlay precedence, remote wake, 10-minute idle, 2-hour sleep, display-lock/OEM behavior, process lifecycle, QR upload/cancel/retry and photo rotation/orientation/offline behavior.
- E04 is NOT FINAL. No APK published/installed, no device acceptance, no merge. Keep D-013 grouped merge policy.


## Handover — E04 native idle preservation — 2 Nov 2026

- E04 source branch `feature/e04-standby-screensaver`, current head `08076bb945a169de198905152a092f419f48aa82`; PR #37 OPEN/DRAFT/UNMERGED, base E03 exact head `e085f8aed6fdc85b4793b76bb26ab8543b26b2cf`.
- Commits `75fa1b1` and `08076bb`: remove app-owned DreamService 2-hour sleep and MainActivity 10-minute/2h10m idle-power triggers. Android TV owns native idle → StandBy/Dream → screensaver → sleep sequence. Manual Nightstand overlay remains; photo pipeline unchanged.
- CI #629 / `41418393199`: Lists/tools/docs PASS; launcher build/unit tests in progress at checkpoint; APK size gate pending.
- Next: poll exact-head CI, fix any failures, record APK size; then Project F physical acceptance of native transition timings, DreamService selection/wake, QR upload/cancel/retry, photo rendering/orientation/rotation, lifecycle/offline behavior. Do not claim hardware acceptance or merge before gates. E04 NOT FINAL.

- CI #629 / run `41418393199` completed **SUCCESS** for `08076bb945a169de198905152a092f419f48aa82`; APK 1,561,373 bytes, 11,491-byte headroom. Source-level gates PASS. No APK install or physical acceptance claim. Project F must complete target-device/OEM sequence and photo-flow acceptance before global release/merge.

## Resume checkpoint — E05 source audit and ayah display repair (2 November 2026)

- Active branch: `feature/e05-information-center-ayah`; Draft PR #38 targets E04 branch `feature/e04-standby-screensaver`; PR OPEN/DRAFT/UNMERGED under D-013.
- Base E04 exact head: `08076bb945a169de198905152a092f419f48aa82`. Current E05 PR head: `60086e0a2d74b40e124f11caa219a683863e6e08`.
- E05-F01 fixed: Arabic ayah and translation no longer have one-line/two-line limits or end ellipsis; both wrap naturally, with wider column, RTL/high-quality Arabic line breaking, Arabic font padding and larger TV-readable type.
- Source audit covered WeeklyAyah cache/translations, PrayerTimes location time zones/fallback, HomeWeather/Open-Meteo/modelled US AQI/D07 freshness, and Information Center/four Quick Actions. No unrelated store, QR, prayer calculation or global UI refactor.
- Audit report: `E05_WIDGETS_INFORMATION_AUDIT.md`. Exact-head CI run #41422241456 is QUEUED; wait for both jobs and verify the final PR head before source closure.
- Project F still must verify longest Arabic/Indonesian/English verse, no clipped glyphs/marks, shelf/dock clearance, 1920x1080 + 4K, D-pad/Back/scroll, offline/cache/language refresh, prayer zone/date rollover and weather freshness. Deferred, not waived.
- No APK published/installed, no physical device acceptance, no merge. Next action: monitor CI #41422241456, inspect failures if any, then close source gate only after exact-head PASS.

- Follow-up E05-F02 found the Smart Boost Quick Action bypassed the explicit cache-clean workflow, deleted only top-level cache entries, and always reported success. It now opens the existing confirmation/result flow using `CacheCleaner.cleanNow()`; ID/EN labels now accurately describe cache/storage optimization without promising RAM release. Latest source PR head is being revalidated by exact-head CI.

- Follow-up E05-F03: Prayer & Ayah Quick Action now uses a dedicated scrollable dialog with separate RTL Arabic, translation and reference surfaces; no line cap/ellipsis, Quran font padding, and D-pad scroll-to-Close handling. Latest feature head is `7b8a5a7ce8c2f372d5d6a262847adf408d666033`; exact-head CI must be checked for this final documentation/source head.


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


## Cross-project handover — Weather/AQI & popups — 2 Nov 2026
- Resume with `CROSS_PROJECT_WEATHER_AQI_AUDIT.md`.
- D07 owner: inspect detailed WeatherAirQuality request generation/location/language freshness and per-source cache success/stale semantics; D01/D02 only if settings storage/propagation root cause is demonstrated.
- B01/B02/B03 owners: cross-check weather popups against canonical tokens/modal/focus/opacity contract; repair shared primitives only in B.
- E05 owner: correct weather/AQI API values, missing/stale presentation, labels, timestamps, source attribution and weather-specific popup content after dependency handover.
- F: integrated 1080p/4K, offline/location/language, popup/D-pad/Back/scroll acceptance. G: close and reconcile all owner findings before D-013 grouped merge.
- C is not assigned; direct Open-Meteo HTTPS fetches are separate from the local QR upload stack. Preserve C07.
- No repairs have yet been committed for WQ-01–WQ-08; do not infer completion from E05 ayah-reader CI.


## E05 Weather/AQI repair handover — 2 Nov 2026
- Current E05 PR #38 head: `6fe89fad078692958db67030329a6de66cd7ae71`; exact-head CI run #663 PASS (launcher tests/release build/APK-size guard and Lists/tools/docs). PR remains Draft/open/unmerged.
- Weather/AQI source repairs are implemented: actual CAMS PM fields only; no synthetic current/forecast values; separate successful-cache timestamps; generation/location/language guards; cache bound to selected coordinates; unavailable UI states; no fake forecast range; neutral automatic background when source unavailable; model-reference and Open-Meteo/CAMS attribution.
- Three new JVM tests cover exact, changed and non-finite coordinate cache checks. Intermediate duplicate localized source labels were removed; final CI passed resource merge.
- D07 PR #31 detailed-weather freshness extension passed CI #642 on `7ce92d30911288eed98aa2e7b36eaf9942d0c355`.
- Next: perform a scoped shared-popup audit against B01/B02/B03 canonical modal/focus contract; do not fork global dialog styling in E05. Then close remaining source-level findings and hand physical 1080p/4K, offline/stale, location/language, D-pad/Back/scroll and popup checks to Project F.
- No APK publication/installation, physical TV acceptance, merge, or release approval has occurred. Preserve D-013 grouped merge timing and C07 upload resilience.
- Evidence: [E05_WIDGETS_INFORMATION_AUDIT.md](E05_WIDGETS_INFORMATION_AUDIT.md), [CROSS_PROJECT_WEATHER_AQI_AUDIT.md](CROSS_PROJECT_WEATHER_AQI_AUDIT.md).


## E05 final handover to Project F — 2 Nov 2026
- E05 source scope is CLOSED. PR #38 exact head `6fe89fad078692958db67030329a6de66cd7ae71`; CI #663 SUCCESS on both jobs. PR remains Draft/open/unmerged.
- F acceptance matrix: Mi TV Stick 1080p and 4K; long Arabic verse glyph/harakat and translation scrolling; D-pad focus/Back/Escape; popup geometry/clipping; weather/AQI missing/offline/stale and location/language transitions; Home shelf/wallpaper/prayer/dock coexistence.
- Route any reproducible defect to its owner: B shared modal primitives, D freshness/state, E05 module-specific UI/content. No E05 popup-style fork.
- No APK install, physical device test or release approval is claimed. D-013 remains in force; grouped merge only at final G closure.
- Detailed record: [E05_WIDGETS_INFORMATION_AUDIT.md](E05_WIDGETS_INFORMATION_AUDIT.md).


## Handover — E07 Animations & Visual Effects — 2 Nov 2026

- E06 is source/CI complete at `570b7c83efaec3ac515b78f1c6c119b777fc3b84`; PR #39 remains Draft/open/unmerged under D-013. F physical acceptance remains pending.
- E07 branch: `feature/e07-animation-effects`; parent E06 exact head `570b7c83efaec3ac515b78f1c6c119b777fc3b84`; Draft PR #40 targets E06 and remains open/unmerged.
- Repaired reduced-motion behavior in Home weather overlay and full Weather screen. Weather screen focus transitions now snap under reduced motion. The full Weather screen uses the existing 1.5 GiB low-memory threshold; rain/star/cloud particle counts are reduced in low-memory/reduced-motion mode. Existing visual effect preferences and callbacks remain unchanged.
- Code checkpoint `da3135796156c26dd20795931eae15caa83458e9`: exact-head CI run #671 / `41443003016` PASS; APK diagnostic 1,563,885 bytes, 8,979 bytes under the 1.5 MiB budget. APK publication skipped.
- Feature audit report commit `15840ed1e9a4d7f7bb7e3a013bbebf9c044b8a6a`; exact-head CI run #672 / `41443305464` was in progress when this handover was written.
- Next: verify both jobs on the final documentation head; inspect any CI failure. Then complete a final source-level review of all four existing Effects-tab switches and update the audit report/PR if scope changes. Do not merge or publish an APK.
- Project F acceptance required: normal/reduced motion, toggling effects from Settings and returning focus, Home overlay on/off, Weather screen animation pause/resume, low-memory behavior, D-pad focus stability, frame pacing and visual quality on Mi TV Stick 1080p and 4K. Deferred, not waived.


## E07 final handover — 2 Nov 2026

- E07 source/CI work is COMPLETE at exact PR head `15840ed1e9a4d7f7bb7e3a013bbebf9c044b8a6a`; CI run #672 / `41443305464` passed both jobs. Diagnostic APK 1,563,885 bytes / 1,572,864-byte cap; 8,979-byte headroom. APK publication skipped.
- PR #40 remains OPEN/DRAFT/UNMERGED; base E06 head `570b7c83efaec3ac515b78f1c6c119b777fc3b84`; D-013 unchanged. Do not merge.
- Audit report: `E07_ANIMATIONS_EFFECTS_AUDIT.md` on the feature branch. Canonical master checkpoint updated on `docs/project-master-inventory`.
- Next feature task: E08 — Wallpaper Settings redesign (Change Wallpaper, Wallpaper Shuffle Interval, Next Wallpaper). Start from E07 exact head and map existing wallpaper state/callbacks before changing UI. Preserve E03/E04/E05 wallpaper, StandBy, upload, rotation, shuffle and interval contracts; no duplicated state or migration without evidence.
- Project F still owns all device/runtime acceptance, including E07 reduced/normal motion and frame pacing. No APK installation or physical TV test is claimed.


## Cross-project handover — Glass Effect OFF global surface fallback — 2 Nov 2026
- Root cause repaired in both owning layers. B03 global contract source: branch `feature/b03-dialog-opacity-layering`, PR #12, head `8cf0a846f3a7738cc1bb20f663dac9cf961e98da`; exact-head CI #685 / `41447473358` pending at this checkpoint. B03 adds shared opaque popup/dock tokens, solid OFF fallback for GlassDrawable and DockDecoration, and modal material selection in TvDialogStyle; JVM regression test added.
- E07 toggle integration source: PR #40 head `7ecb45a634e6ed9792fd87f3110ffe0b6eaad1a2`; exact-head CI #682 / `41446777187` PASS. The preference toggle refreshes active app-owned popup surfaces. Both PRs remain Draft/open/unmerged under D-013.
- Next source action: verify B03 exact-head CI #685; repair any CI findings; update report and PR #12. During G grouped integration, reconcile B03 and E07 overlapping shared files deliberately (do not blindly merge or duplicate styling). Preserve same visual contract and all existing E07 preference keys/callbacks.
- Project F must test glass ON/OFF on 1080p and 4K: top pills, Wi-Fi and Quick Settings menus, anchored PopupWindow, modal dialogs, dock tray, focus/D-pad, Back/dismiss and toggling while popup is open. Android OS-owned Settings remains OS-controlled.
- No APK published/installed and no physical acceptance claimed. Full evidence: [B03_DIALOG_OPACITY_AUDIT.md](B03_DIALOG_OPACITY_AUDIT.md).


## B03 exact-head verification update — 2 Nov 2026
- Supersedes the earlier pending-CI checkpoint: B03 source/test head `8cf0a846f3a7738cc1bb20f663dac9cf961e98da`, exact-head CI run #685 / `41447473358` **SUCCESS**. Both Launcher build/unit tests/release build/APK-size guard and Lists/tools/docs passed.
- Release APK diagnostic: 1503 KB (<1.5 MiB / 1536 KiB); APK publication skipped. PR #12 remains OPEN/DRAFT/UNMERGED and mergeable.
- E07 exact-head CI run #682 / `41446777187` also **SUCCESS** on head `7ecb45a634e6ed9792fd87f3110ffe0b6eaad1a2`; toggle/live-popup invalidation integration is source/CI verified.
- Source-level global fallback contract is now verified in both owner branches. G must reconcile shared-file overlap at grouped integration; F must still perform physical 1080p/4K visual and D-pad acceptance. No APK installed or device test claimed.
- Detailed matrix: [B03_DIALOG_OPACITY_AUDIT.md](B03_DIALOG_OPACITY_AUDIT.md).


## Final Glass OFF fade-preservation verification — 2 Nov 2026
- Follow-up refinement: the solid fallback fill and border in `GlassDrawable` now honor the existing fade alpha. This keeps the popup open/close animation when Glass Effect is OFF while preserving the settled opaque surface.
- B03 source/test head `d87ffdf914851b8a1d2c67c2416ac9f5f1309d93`; exact-head CI #687 / `41447950284` **SUCCESS** (Launcher build/unit tests/release/APK-size guard and Lists/tools/docs). Release APK diagnostic 1503 KB; publication skipped.
- E07 source/test head `6dc39d76e3c897e841854f425d549dbfebf1025c`; exact-head CI #688 / `41447965099` **SUCCESS** (both jobs). APK 1,564,137 bytes / 1,572,864 cap / 8,727 bytes headroom; publication skipped.
- Latest E07 PR branch head `f9779576505d1cb429b3563bd09317b59104a24c` is documentation-only after the tested source head; no source/resources/tests/build inputs changed by that follow-up. PR #12 and #40 remain Draft/open/unmerged under D-013.
- Global source/CI work is verified in both owner branches. Project F physical acceptance remains pending: glass ON/OFF, all app-owned popup/dialog/dock surfaces, focus/D-pad/Back, live toggle, 1080p and 4K. No APK installation or physical test claimed.
- Report: [B03_DIALOG_OPACITY_AUDIT.md](B03_DIALOG_OPACITY_AUDIT.md).


## Ownership clarification — B01 token catalog / B03 surface policy — 2 Nov 2026
- `TvUiTokens` remains B01-owned. B01 owns the canonical shared color/geometry token catalog; B03 does not take ownership of it.
- B03's source repair extends that catalog with `POPUP_SURFACE` and `DOCK_SURFACE` fallback entries and owns applying shared tokens to app-owned popup/dialog/dock surfaces. The existing `BORDER` token is reused.
- E07 owns the Glass Effect preference/toggle and live popup invalidation. G owns final shared-file reconciliation during D-013 grouped integration. F owns physical 1080p/4K acceptance.
- This clarification supersedes any earlier shorthand that described B03 as owner of the shared tokens themselves. Evidence and exact-head CI remain unchanged.
- B01 PR #10 description updated to preserve the two fallback token additions during grouped reconciliation; no merge or ownership transfer.


## Current final checkpoint — Glass OFF fallback source and ownership — 2 Nov 2026
- B03 source/test branch head `d87ffdf914851b8a1d2c67c2416ac9f5f1309d93`, PR #12 OPEN/DRAFT/UNMERGED; exact-head CI #687 / `41447950284` PASS. Release diagnostic 1503 KB; APK publication skipped.
- E07 source/test head `6dc39d76e3c897e841854f425d549dbfebf1025c`, exact-head CI #688 / `41447965099` PASS. APK 1,564,137 bytes; 8,727-byte headroom; publication skipped. Latest PR #40 head `a0f2b0fff6eef0c42852d21b971904d5acd29d5c` is documentation-only after that verified source/test head; PR remains OPEN/DRAFT/UNMERGED.
- B01 owns the `TvUiTokens` catalog; B03 owns applying the shared tokens to global popup/dialog/dock surface policy; E07 owns Glass Effect toggle and live popup invalidation; G owns grouped shared-file reconciliation; F owns physical acceptance.
- GlassDrawable solid OFF fallback preserves fade alpha, so popup transition behavior is retained. Custom-content dialogs with explicit root backgrounds remain content-owned; `TvDialogStyle.applyCustom()` is intentionally transparent and does not overwrite those surfaces.
- Remaining gate is Project F physical test only: glass ON/OFF, top pills, Wi-Fi/Quick Settings menus, anchored PopupWindow, shared modal dialogs, dock, D-pad/focus, Back/dismiss, live toggle, 1080p and 4K. No APK installation or physical test claimed.
- Full test matrix and source details: [B03_DIALOG_OPACITY_AUDIT.md](B03_DIALOG_OPACITY_AUDIT.md) and [E07_ANIMATIONS_EFFECTS_AUDIT.md](E07_ANIMATIONS_EFFECTS_AUDIT.md).


## Current resume checkpoint — E08 source/CI closed, 2 Nov 2026
- E08 source/test head `b386986f08951c4dcf12ba8d72638b091a264803`; Draft PR #41 targets E07 @ `a0f2b0fff6eef0c42852d21b971904d5acd29d5c`, remains OPEN/DRAFT/UNMERGED under D-013.
- Exact-head CI #693 / `41469544565` PASS; APK 1,565,397 bytes, 7,467 bytes below 1.5 MiB cap. Publication skipped.
- E08 creates one three-tab wallpaper control center (Change Wallpaper / Shuffle Interval / Next Wallpaper), consolidating Appearance and Quick Control access while reusing existing category, custom-photo, QR, shuffle, interval, decoder and rotation implementations.
- Canonical report: `E08_WALLPAPER_SETTINGS_REDESIGN_AUDIT.md`.
- Stage E source implementation is complete through E08. Next phase is Project F: isolated integration rehearsal, combined-diff/dependency audit and full regression/device acceptance. Keep D-013: do not merge feature PRs early; do not install APK or claim physical acceptance before approved F-stage procedure.


## Current resume checkpoint — E08 source/CI final, 2 Nov 2026
- Final E08 source/test head: `dcec6afc805a9d7c78702ad8d51b20b87bd751a3`; PR #41 OPEN/DRAFT/UNMERGED targets E07 @ `a0f2b0fff6eef0c42852d21b971904d5acd29d5c` under D-013.
- Exact-head CI #695 / `41470304782` PASS; APK 1,565,417 bytes, 7,447 bytes below the 1.5 MiB cap. Publication skipped.
- E08 unifies Change Wallpaper / Shuffle Interval / Next Wallpaper in one three-tab control center. Existing category, custom-photo/QR, shuffle, interval, decoder and rotation owners are reused. D-pad focus is retained after Shuffle state refresh.
- Feature branch latest commit `086ad1d112b346a281c88273fa620e9348d2cccc` is report-only after the verified source/test head.
- Canonical report: `E08_WALLPAPER_SETTINGS_REDESIGN_AUDIT.md`.
- Stage E source implementation is complete through E08. Next phase: Project F integration rehearsal, combined-diff/dependency audit, regression and physical acceptance. Keep D-013; no early merge or APK installation.


## Current resume checkpoint — E08 compact UI and CI final, 2 Nov 2026
- Final E08 source/test head: `fcd7d8f23a4833830a401652ef269db3282b7300`; PR #41 OPEN/DRAFT/UNMERGED targets E07 under D-013.
- Exact-head CI #702 / `41473357877` PASS; APK 1,562,709 bytes, 10,155 bytes below the 1.5 MiB cap. Publication skipped.
- Popup/subtab styling now follows shared B01 tokens and B01/B03 modal surface/viewport contract. Main Wallpaper Control Center max 680×420 dp; short tabs/action labels; category/interval lists compact and single-line; nested custom-photo dialogs share modal styling.
- Existing wallpaper actions and storage/QR/rotation owners are unchanged. D-pad focus is restored after Shuffle refresh.
- Canonical report: `E08_WALLPAPER_SETTINGS_REDESIGN_AUDIT.md`.
- Project F remains next: integration rehearsal and physical 1080p/4K/D-pad/Glass ON-OFF acceptance. No early merge or APK install under D-013.


## Project E closed — resume at Project F — 2 Nov 2026
- User explicitly closed Project E discussion and approved E01–E08 feature-development closure. Do not continue cosmetic or speculative E work absent a verified F-stage defect or explicit user reopening.
- Project F starts with: (1) map PR dependency chain #34→#35→#36→#37→#38→#39→#40→#41 and inspect combined diffs/shared-file collisions; (2) verify source/test evidence against exact heads; (3) run integrated CI and regression gates; (4) prepare and execute controlled Android TV acceptance only after software gates pass.
- Required device matrix: 1080p and 4K; D-pad navigation/focus visibility/OK/Back; dialogs/popups and focus restoration; Glass Effect ON/OFF including live toggle; Home/Profile/Appearance/StandBy/Widgets/Wallpaper/Animations interactions; app lifecycle and persisted state; QR/custom wallpaper upload and delete; shuffle/interval/Next; wallpaper versus StandBy storage isolation; low-memory/reduced-motion behavior.
- Specific E08 UX acceptance: Quick Control > Wallpaper is a compact three-action center (Change/Shuffle/Next); Settings > Appearance > Wallpaper management is a separate, fuller management surface. They must remain visually distinct in purpose, share the same underlying wallpaper state, and not duplicate engines or preferences.
- No TV test has been claimed or performed in this closure. Do not install APK until F's software gates and approved device procedure are ready. Do not merge feature PRs; D-013 reserves grouped integration/merge for G after all gates and explicit user approval.
- PR #34–#41 are OPEN/DRAFT/UNMERGED. E08 source/test head `fcd7d8f23a4833830a401652ef269db3282b7300`, exact-head CI #702 / `41473357877` PASS; APK 1,562,709 bytes / 1,572,864 cap, publication skipped. Latest PR #41 head `8dfdcabd19853a1140724ea68983877af17bed0c` is report-only after that tested source head.

## Project F01 — current checkpoint (2 Nov 2026)
- F01 source-diff/dependency scan has reviewed B01–B03, C01–C07, D01–D07 and E01–E08 PR diffs. The highest-risk semantic hotspots are `MainActivity.java`, QR upload servers, global dialog/token primitives, Settings dialog, weather/profile state, StandBy persistence and localized resources.
- E01–E08 exact source/test CI checkpoints all PASS, including E04 #629. B01/B02/B03, C07, D05 and D07 evidence sampled in F01 also PASS. Exact SHAs and PR overlap map are in `F01_INTEGRATION_DEPENDENCY_AUDIT.md`.
- Important owner contracts: B03 global Glass OFF surface policy / E07 live toggle invalidation; C07 upload session resilience / D05 atomic StandBy persistence / D07 concurrent content-store writes; D07 weather freshness / E05 weather presentation. Preserve these contracts.
- F01 remains IN PROGRESS: no frozen-head combined diff, integrated build, full integrated regression, APK publication/installation or physical TV test has yet been completed. PRs remain Draft/open/unmerged under D-013. No merge before G and explicit approval.
- Next: freeze exact branch heads, inspect the combined semantic diff, then run isolated software integration gates. Route confirmed defects to the owning project; do not invent conflicts from file overlap.


## Project F01 expanded checkpoint — 2 Nov 2026
- F01 now includes complete B01–B07, C01–C07, D01–D07 and E01–E08 dependency mapping. B04/B05/PR15 are already merged pre-D-013; B06/B07 remain Draft/open/unmerged. All current source/test CI checkpoints sampled across B/C/D/E are PASS.
- Parent/base drift exists on PRs #11, #12, #17, #18, #19, #21, #24, #29 and #34. GitHub currently reports them mergeable; do not infer integration from that status. Preserve newer target-branch changes when grouped integration is done.
- B01 canonical tokens are byte-identical in B01/B02/B07/D04 and E01–E06. B03/E07/E08 token files differ only in comments in the compared lines; no executable-code delta found. B03 owns global fallback policy; E07 owns toggle/invalidation.
- Isolated branch `integration/f01-rehearsal-only` and Draft PR #42 test E01–E08 against current D07. PR #42 is mergeable; CI #708 PASS, #709/#710 in progress. No feature PR merged; no APK published/installed; no physical TV acceptance.
- F01 remains open pending #709/#710 final result. Rehearsal PR #42 must be closed without merge after evidence capture. D-013 remains binding.


## F01 closed — handover to F02 (2 Nov 2026)
- F01 dependency/source integration audit is **CLOSED — PASS**. The complete B01–B07/C01–C07/D01–D07/E01–E08 dependency map and base-drift register are in `F01_INTEGRATION_DEPENDENCY_AUDIT.md`.
- F01 caught and repaired the E08 wallpaper route mismatch: Quick Control > Wallpaper is compact Change/Shuffle/Next; Settings > Appearance > Wallpaper is the fuller Collection/Interval/Personal Wallpaper Manager route. Both use the same state/engine. E08 updated head `1d6fba799012ff0e8dbbfbce5a7fbe4f193e1d0d`, CI #711/#712 PASS.
- Final combined rehearsal PR #43 against current D07 passed CI #716 on synthetic merge `21c2eab34a3356d37d079eba04b281cc2373386d`. Unit tests/release build/tools/docs/APK guard PASS; APK 1,563,769 bytes / cap 1,572,864 / headroom 9,095 bytes; manual publish skipped.
- Temporary rehearsal PR #42/#43 closed without merge. E08 feature PR #41 remains OPEN/DRAFT/UNMERGED; all feature PRs remain under D-013. No APK published/installed and no physical TV acceptance claimed.
- Parent/base drift on #11/#12/#17/#18/#19/#21/#24/#29/#34 is documented; preserve newer target branch changes during G's grouped integration.
- **Next stage: F02** — integrated functional regression and controlled Android TV acceptance. Do not merge feature PRs or publish/install APK without the D-013/G approval sequence.


## F/G staged revalidation checkpoint — handover to F02 (3 Nov 2026)
- Apply the new mandatory sequence: verify prior audit/dependency map; confirm exact source HEADs; combine incrementally on integration branches; test only changed/unproven areas; then finalize with commit/test evidence.
- B-series candidate is integration/b-series HEAD 9070894fa6d9ef8aec034f46dfe777e866709dfe; CI run #741 PASS (launcher unit tests/release build, tools/docs and APK size guard). APK 1506 KiB / 1536 KiB cap; publication skipped.
- Full D07+E08 candidate is integration/project-f-staged-full HEAD 8fe90a9fdd493c5d81ffdacbe16054630a26580a; exact file tree matches the F01 tested synthetic candidate 21c2eab34a3356d37d079eba04b281cc2373386d; CI #716 PASS is reused because no source-tree change occurred.
- C07 upload request-deadline handling remains intact; D05 atomic StandBy persistence, D07 state/concurrency/freshness and E08 distinct wallpaper routes remain in the tested candidate.
- PRs #44–#49 closed without merge; no feature PR, main or release branch merged. No APK publication/installation or physical TV acceptance.
- Next: F02 — controlled Android TV functional acceptance: remote/D-pad and focus restoration, Back/Close paths, popup consistency, Glass ON/OFF, 1080p/4K, Profile/App Lock/Child Mode, Weather/AQI, phone-to-TV QR upload, wallpaper route/state, StandBy persistence, process death and reboot recovery. Reopen a module owner only for a reproducible defect; preserve D-013.


## F02 opening / first audit handover — 3 Nov 2026
- F02 is OPEN. Entry audit and candidate verification PASS; functional regression and physical acceptance remain pending.
- Use `integration/project-f-staged-full` HEAD `8fe90a9fdd493c5d81ffdacbe16054630a26580a`. The live ref is 3 commits ahead of F01 tested candidate `21c2eab34a3356d37d079eba04b281cc2373386d` with no file changes; CI #716 / `41470304782` remains exact-tree evidence (both jobs PASS, APK publication skipped).
- Do not merge/overwrite from `integration/b-series` without semantic review. The selected full candidate includes SettingsFocusPath + JVM coverage and stronger NetworkFeedbackPolicy deadline/session controls absent from that checkpoint; common modal/fallback behavior and feedback files were cross-checked.
- Next: obtain a controlled TV-test APK from the selected frozen candidate, then record physical PASS/FAIL/BLOCKED evidence for D-pad/focus/Back, dialogs, Glass ON/OFF, profile/security, Weather/AQI, phone QR upload, wallpaper route/state, StandBy, process death/reboot and 1080p/4K.
- No APK has been published or installed; no physical acceptance is claimed. No feature PR or main/release merge. D-013 remains binding.
- Full test matrix and decision record: `F02_INTEGRATED_FUNCTIONAL_REGRESSION.md`.


## Current resume checkpoint — Project F02 cross-module remediation, 3 November 2026

- Active integration branch: `integration/project-f02-system-validation`; Draft PR #50 targets `integration/project-f-staged-full`. Latest documented F02 head: `1658237bafdb24167391270f3ad8f694751a6c2a`; exact-head CI was queued.
- Keep PR #50 Draft/open/unmerged. No APK publication, installation, physical Android TV testing, or merge to main.
- Findings and owners:
  - F02-UI-001: B01 owns global modal styling; D05 introduced a regression in Settings search dialog calls. Fixed on B01/D05 and mirrored to F02. MainActivity `styleGlassDialog` now delegates to `TvDialogStyle.apply`, retaining custom dimensions after helper application.
  - F02-SEC-001: D01 follow-up PR #51 adds `android.permission.DUMP` to LocationBroadcastReceiver while preserving both actions. Exact-head CI passed on `bbb13dd107c86cda85bd1482c2f050ca69dfc4e2`; ADB shell compatibility remains Project G.
  - F02-SEC-002: D06 PR #30 changes AzanActivity to exported=false; app-owned explicit Intent/PendingIntent path verified in source. Exact-head CI retest pending.
  - F02-TEST-001: no instrumentation source set found; host-side regression tests added; Robolectric feasibility still to assess. Real TV focus/render/firmware checks remain G.
- Latest source checkpoints: B01 PR #10 `b1c23024d0de740c36e1794ab775fdc43c7a3a20`; D05 PR #29 `7f043943ab0c1d2d1217fc56845d81ac7583c96d`; D06 PR #30 `1bb6ce9bf04d72f3fe33b4e1630af561e99fe0f7`; D01 PR #51 `bbb13dd107c86cda85bd1482c2f050ca69dfc4e2`.
- CI state at checkpoint: B01, D05, D06 owner retests running; F02 latest head queued. Earlier F02 head `27ecd5e4e5e283f5d1675cb28a75b2f1cbb0a6c7` passed both CI jobs but predates MainActivity global modal enforcement.
- Important discovered dependency issue: first D06 retest failed because the stale branch did not contain the B01 `TvDialogStyle` class. The class/test were added to D05 and D06; the subsequent exact-head retest must pass before declaring fixed.
- Next: poll exact-head Actions for B01/D05/D06/F02; inspect failures and repair them without bypassing tests; update F02_FINDINGS_REGISTER.md and this handover with the final SHAs/results; continue test-harness audit. Do not move device tests into F.


## Latest resume point — Project F02, 3 November 2026

- F02 branch `integration/project-f02-system-validation`, PR #50 Draft/open/unmerged, latest documented head `eea920469ffe5bef5ddb14e1dfd66687e82d8fed`; exact-head CI in progress.
- B01 branch `feature/b01-global-navigation-retention`, PR #10, latest head `867c7a9d0977e21b69c8078a400458b00ba2d2dd`; exact-head CI in progress.
- D05 PR #29 head `7f043943ab0c1d2d1217fc56845d81ac7583c96d` CI PASS; D06 PR #30 head `1bb6ce9bf04d72f3fe33b4e1630af561e99fe0f7` CI PASS; D01 PR #51 head `66352838e6155a42a9ea61477643a238f360434f` CI PASS.
- F02 fixes: global modal enforcement; D05 Settings search dialog restore; LocationBroadcastReceiver sender permission; AzanActivity internal-only; LocationActivity internal-only; HomeKeyService double Home routing; Robolectric API 30 dialog-window tests.
- Latest Home-key logic: 350 ms double-press window, 400 ms single-action delay, single Home returns/dismisses, double Home opens Recent Apps or triggers Close All, long Home retains Settings popup. Dedicated App Switch remains disabled. Settings-key input cancels pending Home actions.
- Residual P3: RecentAppsActivity remains exported with ACTION_MAIN; no in-repository external caller found, but verify OEM/assistant/remote contract in G before hardening.
- ADB shell permission compatibility, real remote event timing, focus/render/OEM behavior remain DEFERRED_TO_G.
- Robolectric window tests passed on previous F02 head `5f6b0c60949c1284e0e904e614e85c2e0ca657f2`; latest F02 exact head still pending.
- Next: poll B01 #10 and F02 #50 exact-head CI; fix any failure without skipping/weakening tests; update docs and findings with final SHA/run IDs; continue source audit. Do not merge, publish/install APK, or test on TV during F.


## Latest continuation point — F02, 3 November 2026

- B01 PR #10 owner head `a1e743ad88c28ca3a9602dfc9d0b5da16e46285d`; F02 PR #50 integration head `3ea479d689dc732fe46594a0677d02b7762d7527`. Both latest exact-head CI workflows were in progress at the last check.
- New B01/F02 fix 1: dedicated `forceStopHandler` prevents Force Stop queue cleanup from canceling Home gesture and power-action callbacks. Regression test: `tests/test_home_key_force_stop_handler_isolation.py`.
- New B01/F02 fix 2: `RecentAppsActivity` was exported and accepted `ACTION_CLOSE_ALL`, which could trigger destructive app-stop behavior from an external explicit Intent. Manifest now sets `exported=false` and removes its unnecessary MAIN filter; HomeKeyService explicit in-app route remains. Regression test: `tests/test_recent_apps_manifest_security.py`.
- Earlier F02 head `c9496d4e051980e640bc269ffce695b95ba71ad4` passed both jobs before these two latest changes. Do not claim latest head green until runs on exact SHA complete.
- Previous owner B01 head `10fb9c937a3c5d7fb6331a511f0ba899137b1630` passed both jobs; the newer manifest hardening/test needs owner retest.
- Keep PR #50 Draft/open/unmerged; no APK publish/install and no physical TV test. Project G must validate remote Home double press, Close All, long press Settings, and ADB permission compatibility.


## Latest validated checkpoint — 3 November 2026

- F02 PR #50 integrated head `5dd787ba136dfb070eaa2e833af653e57ec66015`: both CI jobs PASS — #41509632784 Launcher build/unit tests; #41509626957 Lists/tools/docs.
- B01 PR #10 head `791620ce4628ee9e909af7ce2f4758b6df77afeb`: both jobs PASS — #41509576279. The source then received a comment-only alignment commit `389e289e939276ce9e20d7e6c82c293d3f83c70f`; run #41509724289 queued at last check.
- Owner B01 and F02 integration `HomeKeyService.java` now have identical content/blob SHA `b3cd4b2b4092a90f4f1faefb1072c15e2203b314`.
- F02 findings include: global modal contract; location receiver authorization; AzanActivity and LocationActivity internal-only; Home double-press routing; Force Stop/Home Handler isolation; RecentAppsActivity exported Close All security; invalid restart action ID. Robolectric dialog-window tests and source/unit regression guards pass at F02 head 5dd787b.
- Continue source audit and retest any new commit at exact SHA. No merge, APK publication/installation, or physical TV test in F.


## C07/F02 latest continuation checkpoint — 3 November 2026

- C07 owner PR #24 branch `feature/c07-upload-session-resilience`, documented head `12b1f431f7687023727983089ea03eda0f97a73d`; exact-head CI in progress.
- F02 integration PR #50 branch `integration/project-f02-system-validation`, latest head `081496ca93fd0526025a21ce7cd0e3d7f9c3364a`; exact-head CI in progress.
- New finding C07-F08/F02-C07-001: wallpaper management mutation APIs select/delete/slideshow/clear were not synchronized with `stop()` and did not recheck session state after dispatch. Fixed by synchronized methods sharing the server monitor and in-lock active-session recheck; HTTP 410 Gone response on revoked session.
- Added `tests/test_wallpaper_session_shutdown_atomicity.py` to owner and F02.
- Existing upload commit synchronization, file validation, request limits, browser progress/cancel UX and StandBy persistence behavior are preserved.
- Previous F02 head `5dd787ba136dfb070eaa2e833af653e57ec66015` passed both CI jobs before this C07 change. Latest F02 exact-head retest mandatory.
- Do not merge, publish/install APK, or perform physical phone/TV tests in F; C07 phone-to-TV cancellation/retry/expiry acceptance remains G.


## Latest handover checkpoint — 3 November 2026

### Last completed work
- C07 owner branch `feature/c07-upload-session-resilience`, PR #24, head `80a92264c51631fb107c75f32a3cd8e4b8aa9d9a`. Added injectable `SocketTimeoutController` for deterministic JVM tests; production still delegates to `Socket.setSoTimeout`. Corrected test expected remaining timeout to 3500 ms.
- F02 integration branch `integration/project-f02-system-validation`, PR #50. Source/test checkpoint `ddf130a348182546b28ff59c292c9b5ceebacb2e` passed push CI #41512382568 (both jobs). Findings register and integrated-validation document then updated; latest docs-only head `b64715156e180edc3bfae25bffba6a089544247f` now requires fresh exact-head CI.
- Owner CI passed: C07 #41512386827; B01 #41509724289; D05 #41505852041; D06 #41505865772; D01 #41506847521.
- Cross-branch audit confirmed F02's StandBy upload handler correctly combines D05's persistence-aware `UploadResult` API with C07's newer monotonic deadline and session-revocation protections.

### Current status / blockers
- F02 is NOT FINAL until exact-head CI for `b647151` passes and remaining integrated-system audits are complete.
- PR #50 remains Draft/open/unmerged. Do not merge, publish APK, install or test on physical Android TV during Project F.
- Device-only acceptance is DEFERRED_TO_G: actual TV rendering/DPAD focus, remote Home gesture timing, OEM power menu, ADB shell permission behavior, LAN phone-to-TV disconnect/cancel/retry/expiry, and firmware behavior.

### Next work
1. Poll exact-head CI for F02 head `b64715156e180edc3bfae25bffba6a089544247f` and inspect logs if any job fails.
2. Continue risk-based F02 source/contract audit without repeating already-passing CI unnecessarily.
3. Fix findings in owning module branches and mirror into F02; preserve the C07 → D01–D05 dependency chain.
4. Keep device-only cases explicitly deferred to Project G.


## Latest handover checkpoint — 3 November 2026

### Completed since previous checkpoint
- Fixed F02-SEC-005 in E01 owner PR #34: `.InputKeyService` now has `android:exported="true"` while retaining `BIND_ACCESSIBILITY_SERVICE`. Regression test first failed on the old manifest and passed after the fix.
- E01 latest head `edd6461c932705a109ed9768649cae7e3c162c59`, CI #41513716451 both jobs PASS. Mirrored manifest and test to F02.
- F02 latest head `558762994dec05a7ae03a924c33a3ed11665c224`, push CI #41513940620 both jobs PASS. Matching PR run #41513947375 substantive steps passed; cleanup pending at last poll.
- Latest F02 diagnostic APK: 1,564,977 bytes, cap 1,572,864, headroom 7,887 bytes. R8 minify/resource shrinking already enabled; monitor without removing required assets.
- C07, B01, D05, D06 and D01 owner exact-head CI passed (runs recorded in INVENTORY.md).

### Remaining work / boundaries
- Continue F02 risk-based source/contract audit; F02 is not final until remaining integration checks and G handoff are complete.
- Project G alone performs physical Android TV testing: InputKeyService listing/enablement and KEYCODE_TV delivery, Home remote timing, visual rendering/DPAD focus, OEM power menu, ADB shell permission, and phone-to-TV upload disconnect/cancel/retry/expiry.
- PR #50 remains Draft/open/unmerged. Do not merge, publish APK, install or test on physical TV during Project F.


## 3 November 2026 — latest F02 exact-head verification

- F02 PR #50 latest head: `91ae962bae04914a3888210e5f997b24151d5a62` (findings-register documentation-only commit; no source/test changes after head `5587629`).
- PR workflow #41514480902 completed SUCCESS on this exact head: Lists/tools/docs and Launcher build/unit tests both passed. The launcher log confirms release-validation build success, 35 Python tests including the accessibility-service manifest guard, and APK size 1,564,977 bytes against 1,572,864-byte cap (7,887 bytes headroom).
- Matching push workflow #41514472847 initially failed only because Maven Central returned HTTP 429 while downloading Robolectric `nativeruntime-dist-compat-1.0.16`; this is external dependency rate limiting, not a source/test failure. Only the failed launcher job was re-run (attempt 2); it was still in progress at the time of this note. Lists/tools/docs job passed.
- PR #50 remains Draft/open/unmerged. No APK publication, installation or physical Android TV testing. Device acceptance remains Project G.


## F02 continuation checkpoint — 3 November 2026 (latest)

### Completed in this session
- Continued F02 source-level audit across Avatar upload, custom Wallpaper upload, StandBy photo persistence, MainActivity modal styling, ModernSettingsDialog, HomeKeyService, and exported Android components.
- Confirmed the Avatar, custom Wallpaper, and StandBy final commit paths serialize session checks and state/file mutations with server shutdown. Strengthened tests/test_wallpaper_session_shutdown_atomicity.py so both Wallpaper and StandBy upload commit sections are explicitly guarded.
- Found F02-RES-001: concurrent QR upload clients could overcommit a shared free-space snapshot; custom Wallpaper uploads lacked a free-space guard. Implemented overflow-safe reservation arithmetic, synchronized in-flight byte reservations for custom Wallpaper and StandBy uploads, 64 MiB minimum free-space margin, HTTP 507 Insufficient Storage, and release in all exit paths. Existing per-file limits and StandBy collection caps are unchanged; no arbitrary total wallpaper cap introduced.
- Added JUnit reservation boundary tests and tests/test_upload_storage_reservation.py source-contract checks. No physical device testing or APK publication performed.

### Current Git state and CI
- F02 branch: integration/project-f02-system-validation.
- PR #50 head: c1f212585633899d4c1f5327d3a982a9e136e2b7; base integration/project-f-staged-full; Draft/open/unmerged.
- Key commits in this continuation: 4e9219f (upload shutdown test hardening), b167fdb (storage reservation production fix), 1b8418a / 27626ff (policy and JUnit tests), 453f9b9 (Python source-contract test), f594d1a / c1f2125 (findings and validation docs).
- Exact-head CI run #41519813674: Lists/tools/docs SUCCESS; Launcher build/unit tests IN_PROGRESS at last poll. The new storage reservation and upload shutdown findings remain OPEN until both jobs pass on c1f2125.
- Earlier test-only head 7e18c588374ba90dc4d3d01a608c484dcf194108 passed both jobs in run #41519394898.

### Remaining work
1. Poll run #41519813674; inspect and correct any test/build/size-gate failure without weakening tests.
2. If exact-head CI passes, sync final evidence and current APK size/headroom into canonical project docs and F02 handover.
3. Continue only with remaining source-level risk checks; preserve all owner-branch boundaries and do not merge PR #50.
4. Project G must verify real TV UI/DPAD/remote behavior, ADB shell permission compatibility, phone-to-TV LAN cancellation/expiry/retry, concurrent uploads, and low-storage behavior.

### Project F boundary
- No merge, APK artifact publication, installation, or physical Android TV testing during F02.
- Device-only cases remain DEFERRED_TO_G, never marked PASS based on host CI.


## F02 exact-head upload storage retest — 3 November 2026

- F02 source/test head c1f212585633899d4c1f5327d3a982a9e136e2b7 passed PR CI #41519813674: both required jobs SUCCESS.
- Lists/tools/docs: 42 Python tests passed, including all five new upload-storage reservation source-contract checks and both new shutdown critical-section checks.
- Launcher: testReleaseUnitTest and assembleRelease passed; APK size diagnostics and 1.5 MiB guard passed. APK 1,565,621 bytes / 1,572,864 cap / 7,243 bytes headroom.
- F02-RES-001 and supplemental upload shutdown coverage are HOST/SOURCE VERIFIED. Storage remains a P3 budget watchlist; no asset quality tradeoff made.
- F02 findings-register documentation update produced new head 1845635091969c2475bb0d26783bc0f7eb58c1c0. Exact-head CI run #41520194974 was queued at last poll; keep F02 partial until it completes.
- PR #50 remains Draft/open/unmerged. No APK artifact publication, installation, or physical Android TV testing. Project G owns LAN concurrency, low-storage, cancellation and device acceptance.

## F02 process-wide reservation refinement — 3 November 2026
- Follow-up lifecycle review found that stopped WallpaperUploadServer workers can briefly drain while a replacement QR server instance is started. Per-instance reservation accounting would not include those residual writes in the replacement session's free-space calculation.
- Changed the reservation ledger to a process-wide static lock/counter shared across all WallpaperUploadServer instances. Old and new session reservations remain counted until each request's finally block releases them. Updated the source-contract test accordingly.
- Latest F02 head a95b21becad238dd634dda1e79aa9de36d749d8c; PR #50 remains Draft/open/unmerged. Exact-head CI run #41520409999 was queued at last poll. The prior instance-local implementation passed #41519813674, but that result does not close the process-wide refinement.
- No change to 64 MiB margin, per-file size limit, StandBy collection limits, or upload success behavior. No APK publication/installation or physical TV test; Project G retains LAN and low-storage acceptance.

## C07 owner + F02 integration verification — 3 November 2026

- C07 owner PR #24 head 442b78c68b1540394d543bab59bddf52f865ccf0 passed exact-head CI #41520866323: both jobs SUCCESS, 18 Python tests passed, launcher unit tests/release build passed, APK 1,550,949 bytes. The legacy manual APK publication step was SKIPPED for the PR workflow; artifact list empty.
- F02 PR #50 source/test head a95b21becad238dd634dda1e79aa9de36d749d8c passed #41520409999; exact-head findings-register docs head 2469c0c1d241a00be3e02235e12f3f0f6525deb9 passed #41520818608: both jobs SUCCESS, 42 Python tests, launcher unit tests/release build passed, APK 1,565,713 bytes with 7,151 bytes headroom.
- C07 owner and F02 integration now contain matching process-wide upload storage reservations and regression guards. F02 intentionally preserves D05's UploadResult adapter; C07 upstream branch keeps its boolean contract.
- Latest F02 findings-register sync head e190e2863a4aacbe3aba61094fab00572f4b469f; exact-head CI #41521145454 queued at last poll. Keep F02 partial until that docs-only head is green.
- PR #50 and #24 remain Draft/open/unmerged. No artifact published, no APK installed, no physical TV test. Project G retains real LAN, low-storage and device acceptance.

## F02 final host-validation checkpoint — 3 November 2026
- F02 integration PR #50 exact head `e190e2863a4aacbe3aba61094fab00572f4b469f` passed exact-head CI run #41521145454: Lists/tools/docs and Launcher build/unit tests both SUCCESS.
- Python suite: 42 tests, 1 intentional skip (`test_readme_counts`, merge-time README count normalization); no failures. Android `testReleaseUnitTest` and `assembleRelease` succeeded.
- Release APK diagnostic: 1,565,713 bytes against 1,572,864-byte cap (7,151 bytes headroom). Size gate passed. Workflow artifacts list is empty; no APK was published or installed.
- C07 owner PR #24 exact head `442b78c68b1540394d543bab59bddf52f865ccf0` also passed run #41521013896: both jobs SUCCESS; 18 Python tests, 1 intentional merge-time README count skip; Android unit tests/release build passed; APK 1,550,949 bytes; artifacts empty.
- D01 security follow-up PR #51 head `66352838e6155a42a9ea61477643a238f360434f` passed run #41506847521. Its exported receiver permission/action guard and internal-only LocationActivity contract are present identically in F02 integration manifest/test sources.
- F02-RES-001 process-wide reservation, 64 MiB free-space margin, HTTP 507 boundary, reservation release, and Wallpaper/StandBy shutdown atomicity regression guards are OWNER + INTEGRATION HOST VERIFIED.
- Host/source scope for F02 is complete. Physical Android TV, real phone/LAN, ADB-shell permission compatibility, low-storage behavior, OEM rendering/focus, and remote/power behavior remain explicitly DEFERRED_TO_G. This is a test boundary, not an unowned source defect.
- PR #50 and owner PRs remain Draft/open/unmerged under the staged integration decision; no APK artifact publication or device installation occurred.


## Final exact-head CI closure — 3 November 2026
- F02 integration PR #50 head `190b698453a96da8ec2db31da2eefac836849a10` passed exact-head run #41522215859; both required jobs SUCCESS.
- Python suite: 42 tests, 1 intentional merge-time README-count normalization skip; no failures. Android `testReleaseUnitTest` and `assembleRelease` succeeded.
- APK diagnostic: 1,565,713 bytes against 1,572,864-byte cap (7,151 bytes headroom); size-budget step SUCCESS. Workflow artifact list empty.
- C07 owner PR #24 head `442b78c68b1540394d543bab59bddf52f865ccf0` passed #41521013896; D01 owner PR #51 head `66352838e6155a42a9ea61477643a238f360434f` passed #41506847521.
- F02 host/source gates are complete. Physical TV, phone/LAN, ADB-shell compatibility, low-storage telemetry and OEM/remote acceptance remain DEFERRED_TO_G. No APK publication or installation. PR #50 and owner PRs remain Draft/open/unmerged under staged integration governance.


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

## F03 QR communication feasibility checkpoint — 3 November 2026

- F03 scope: QR communication and upload across Avatar, Wallpaper and StandBy (C01–C07): active LAN IP, server binding, routes, body framing, file validation, retry, timeout, expiry, cancellation, persistence boundary and storage pressure.
- Decision: HOST/SOURCE FEASIBILITY PASS; OVERALL F03 PARTIAL. No new source blocker identified in the reviewed scope. This is not physical device acceptance.
- No dedicated F03 source branch/PR exists; the integrated candidate used for host evidence is integration/project-f02-system-validation, PR #50, exact checkpoint 2ede452b3137580bd51088661f80b6d0eeac7f4b.
- Exact integrated CI run #41523201383 completed SUCCESS; both required jobs passed. 42 Python tests (one intentional merge-time README count normalization skip), Android unit tests and release validation build passed. APK diagnostic 1,565,713 bytes / 1,572,864-byte cap (7,151 bytes headroom). Artifact list empty.
- C07 owner PR #24 head 442b78c68b1540394d543bab59bddf52f865ccf0 CI #41521013896 completed SUCCESS; both jobs passed; artifact list empty.
- Targeted regression-source review: test_upload_storage_reservation.py, test_wallpaper_session_shutdown_atomicity.py, and test_upload_monotonic_deadline_clock.py. Reservation guard covers process-wide accounting, 64 MiB free-space margin, HTTP 507 and release; shutdown guard covers HTTP 410 and synchronized mutation/commit callbacks; deadline guard enforces elapsed realtime.
- C01–C07 owner CI evidence and scope details are recorded in F03_QR_COMMUNICATION_FEASIBILITY.md.
- Deferred to G, NOT PASS: real QR scan/phone browser over Mi TV Stick LAN; disconnect/retry/cancel/expiry/reconnect on-device; concurrent/low-storage behavior; Avatar/Wallpaper/StandBy persistence/isolation after process death/reboot; real browser/OEM/network-interface behavior.
- All source PRs remain staged Draft/open/unmerged. No APK publication/installation, physical TV test, or device setting change. Do not repeat source work absent a reproducible new finding.
- Next: retain these five device acceptance groups for Project G; continue remaining Project F validation without promoting device-only gates to PASS.

## Current resume checkpoint — Project F04 Avatar/Wallpaper/StandBy isolation — 3 November 2026

- Canonical F04 integration: PR #53 on `integration/project-f04-feature-isolation-v2`. Earlier PR #52 was closed as superseded after its base was found behind the approved F02/F03 integration head; no merge occurred.
- Active integration: `integration/project-f04-feature-isolation-v2`, PR #53 Draft/open/unmerged; base is F02/F03 integration branch `integration/project-f02-system-validation` at `2ede452b3137580bd51088661f80b6d0eeac7f4b`.
- Latest F04 source head `e895ee590a607a8194b2037585ed47f2cf464961` passed exact-head CI #41560230282 (both jobs; 63 Python tests / 1 intentional skip; Android unit/release build and size gate PASS; APK 1,566,045 bytes / 6,819-byte headroom; publication skipped). Latest F04 head `6ffc8b4c253beb2662d6978ba38033039c3c53f9` passed exact-head CI #41560811925 (both jobs): 65 Python tests / 1 intentional skip; Android `testReleaseUnitTest`, `assembleRelease`, APK diagnostic and size gate PASS. APK 1,566,045 bytes / 6,819-byte headroom; artifacts empty, no publication/install.
- Source isolation verified: distinct private roots `profile_avatars`, `custom_wallpapers`, `standby_photos`; Avatar replacement remains profile-scoped; Wallpaper and StandBy QR route/server modes are separate; shared QR dialog dismissal stops active upload servers; both StandBy renderers use `StandbyPhotoStore`; Quick Control and Appearance Wallpaper surfaces remain distinct while sharing wallpaper state.
- F04-WALL-001 found false-success feedback in single/bulk Wallpaper deletion. E08 owner repair now checks delete outcomes, shows localized failure feedback and reconciles remaining Custom files/pin state. C07 owner repair adds `UploadListener.onClearFailed()` for partial QR API clear, returns HTTP 500 and notifies UI to reconcile. Both fixes are mirrored into F04 without overwriting F02 B01 modal or D05 UploadResult contracts.
- Owner branches: C07 PR #24 head `b7a40fdc2c52268ec69c95731983d2f95024eb57` passed exact-head CI #41560200291 (both jobs; 21 tests / 1 intentional skip; APK 1,550,961 bytes). E08 PR #41 head `c613e3c5f593556748d1593d23f2899ecee36e4a` passed exact-head CI #41560600033 (both jobs; 11 tests / 1 intentional skip; APK 1,564,089 bytes). Both owner fixes are now HOST/CI VERIFIED.
- F04 report: `F04_FEATURE_ISOLATION_AUDIT.md`; regression tests: `tests/test_media_feature_isolation.py` and `tests/test_wallpaper_api_clear_failure.py`.
- F04 HOST/SOURCE EXIT PASS. C07 and E08 owner exact-head CI and F04 integration exact-head CI are all green. Physical phone/LAN/OEM/remote/StandBy acceptance remains DEFERRED_TO_G. F05 owns force-stop/process-death/reboot persistence validation. Next host/source workstream: proceed to F05.
- PR #24, #41, #53 remain Draft/open/unmerged under D-013. No APK publication/installation or physical TV test. Next action: begin F05 persistence, force-stop and reboot validation.


### Post-network-interruption F04 re-verification — 3 November 2026, 08:41 WIB

- Re-read live GitHub state. PR #24 (C07), #41 (E08), and #53 (F04) are all present in the open Draft PR set; none is merged.
- F04 integration remains 21 commits ahead / 0 behind approved base integration/project-f02-system-validation at 2ede452b3137580bd51088661f80b6d0eeac7f4b. Its seven-file delta is limited to MainActivity, WallpaperUploadServer, three locale resources, and two F04 test files.
- Latest exact-head CI runs rechecked: C07 #41560247050 (21 tests / 1 intentional skip; Android build and size PASS; APK 1,550,961 bytes); E08 #41560595614 (11 tests / 1 intentional skip; Android build and size PASS; APK 1,564,089 bytes); F04 #41560815870 (65 tests / 1 intentional skip; Android build and size PASS; APK 1,566,045 bytes). All jobs SUCCESS; all three artifact lists empty.
- Source spot-check confirms QR API clear failure callback/HTTP 500 and wallpaper UI delete-failure reconciliation; three locale strings are aligned. No new reproducible source blocker.
- This was a read-only re-verification of existing exact-head runs, not a new CI execution or physical-device test. F04 HOST/SOURCE EXIT remains PASS. Project G device acceptance and F05 process-death/force-stop/reboot validation remain open by design.
- No code changes were needed. Resume at F05 after user approval; retain PR #24/#41/#53 Draft/open/unmerged.


## Current resume checkpoint — Project F05 Persistence & Recovery — 3 November 2026

- F05 integration PR #54, branch `integration/project-f05-persistence-recovery`, Draft/open/unmerged; base is F04 integration exact head `6ffc8b4c253beb2662d6978ba38033039c3c53f9`.
- Current F05 head `1048b0b68ae5ab5a2ad867183236b514e0c691fa`; 14 commits ahead / 0 behind F04, five changed paths only.
- F05 PR run #41563437433: Lists/tools/docs PASS, 80 Python tests / 1 intentional README-count skip; Android unit/release build and size gate in progress.
- F05-WALL-001: E08 owner PR #41 head `edd24d72256d06da4578971ea7bac1802746a3a6`, exact CI #41562661052 both jobs SUCCESS; 15 tests / 1 intentional skip; APK 1,564,197 bytes, 8,667-byte headroom. Startup now clears stale single-file pin and falls back to Natural only when Custom collection is genuinely empty; unreadable directory preserves state.
- F05-WALL-002: C07 owner PR #24 head `2315f12472f469843c1441ad7d01c6898858c4c5`, run #41563429553 Lists/tools/docs PASS (23 tests / 1 intentional skip), Android build in progress. Cleanup targets only orphaned private `custom_*.upload` parts, skips active worker files via synchronized process-local registry, and cleans incomplete temp files in worker finally. Mirrored to F05.
- F05 cross-feature suite verifies Avatar/Profile serialization and replacement ordering, StandBy atomic commit/clear/cleanup and shared renderers, navigation/settings state, Wallpaper selection recovery, and prayer alarm boot receiver boundary.
- Next: complete C07 owner and F05 exact-head Android CI/size gates; inspect logs and APK artifact lists; rerun only failed jobs for external dependency throttling; cross-check owner/integration source equality; then update final audit register and checkpoint.
- Do not merge, publish/install APK, or test the TV in F05. Real force-stop/process-death/reboot, DreamService/OEM, alarm restore and filesystem acceptance remain Project G.
- Detailed evidence: `F05_PERSISTENCE_RECOVERY_AUDIT.md`.


## F05 host/source exit closure — 3 November 2026

- F05 integration PR #54 exact head `1048b0b68ae5ab5a2ad867183236b514e0c691fa`; run #41563437433 both jobs SUCCESS. Python: 80 tests / 1 intentional README-count skip. Android unit tests/release build and APK size gate PASS; APK 1,566,861 bytes / 6,003-byte headroom; artifacts empty.
- C07 owner PR #24 exact head `2315f12472f469843c1441ad7d01c6898858c4c5`, run #41563429553 both jobs SUCCESS; 23 tests / 1 intentional skip; APK 1,551,661 bytes / 21,203-byte headroom.
- E08 owner PR #41 exact head `edd24d72256d06da4578971ea7bac1802746a3a6`, run #41562661052 both jobs SUCCESS; 15 tests / 1 intentional skip; APK 1,564,197 bytes / 8,667-byte headroom.
- F05-WALL-001 stale Custom pin recovery and F05-WALL-002 abandoned QR temp-upload cleanup are fixed on their owner branches, mirrored to F05, and cross-audited. Scoped source/test blocks match exactly. All workflow artifact lists empty; no APK published/installed.
- PR #24, #41 and #54 remain Draft/open/unmerged. **F05 HOST/SOURCE EXIT PASS.** No physical force-stop/process-death/reboot test is claimed. Project G must perform real Mi TV Stick reboot/alarm, StandBy/DreamService, phone/LAN and filesystem acceptance.
- **Resume next at Project F06 — Localization.** Preserve the same owner-first repair, exact-head CI, cross-audit, staged integration and documentation checkpoint rules.


## Current resume checkpoint — Project F06 Localization & Global UI — 3 November 2026

- F06 integration PR #55: Draft/open/unmerged; branch `integration/project-f06-localization`; base F05 exact head `1048b0b68ae5ab5a2ad867183236b514e0c691fa`; final F06 exact head `487099013e6d9bd65cc07d72e760b1e14ab1f091`.
- Compare: 29 commits ahead / 0 behind, nine changed paths. PR remains mergeable and intentionally unmerged under D-013.
- Exact-head CI #41566633872: both required jobs SUCCESS. Python 89 tests / 1 intentional README-count skip. Android `testReleaseUnitTest`, `assembleRelease`, diagnostics and APK size gate PASS.
- APK 1,569,397 bytes / 1,572,864-byte cap / 3,467-byte headroom. Artifact list empty; no APK publication or installation.
- Resource sets: 615 keys each in default/Indonesian/English; no duplicates, key mismatches, placeholder mismatches or empty strings.
- F06 fixes dynamic bilingual labels, swapped daily-ayah locale values, Indonesian Glass Effect title, two custom dialogs that bypassed the shared modal contract, QR upload server/browser error text, gallery date locale and thumbnail accessibility label. Resource duplication was consolidated without removing visible coverage.
- **F06 HOST/SOURCE EXIT PASS.** No physical TV test, language-switch runtime test, phone/LAN upload acceptance, APK installation or merge is claimed.
- Next: Project F07 per the approved roadmap. Keep all Project G physical acceptance gates open: Android 11 and Android 13+ language switching, Home/settings/subtab/popup rendering, D-pad/focus, mobile QR upload/LAN and OEM behavior.
- Detailed evidence: `F06_LOCALIZATION_UI_AUDIT.md`. Preserve the same Trigger → Input → Verification → Work → Decision → Output → QC → Final/Not Final discipline.


## Current resume checkpoint — Project F07 Performance & Stability — 3 November 2026

- F07 PR #56: Draft/open/unmerged under D-013. Branch `integration/project-f07-performance-stability`; base F06 exact head `487099013e6d9bd65cc07d72e760b1e14ab1f091`; final F07 exact head `976e068d48ecc1ede2811ac455372395d382ba76`.
- Compare: 19 commits ahead / 0 behind; ten changed paths.
- Exact-head CI #41568715178 both jobs SUCCESS. Python 99 tests PASS. Launcher and installer `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and size enforcement PASS.
- APK 1,571,293 bytes / 1,572,864-byte cap / 1,571-byte headroom. Workflow artifact list empty; no APK publication or installation.
- F07 fixed weather frame shader allocation and ripple churn, wallpaper and QR preview UI-thread decode, stale bitmap/lifecycle handling, Now Playing main-thread Binder polling, Backdrop OOM/partial-array handling, StandBy unbounded decode queue, forced GC, and Online Wallpapers aggregate storage (512 MiB cap).
- **F07 HOST/SOURCE EXIT PASS. Overall performance acceptance remains PARTIAL.** No physical TV test or actual RAM/FPS/ANR/thermal/long-run metric is claimed.
- No merge, APK publication/installation or TV setting change. Keep PR #56 Draft/open/unmerged until Project G final integrated audit and explicit merge approval.
- Next: Project G — reconcile exact owner/integration heads and dependencies, audit the complete candidate diff, resolve cross-project regressions, group merges under D-013, and only after explicit readiness approval produce one labelled TV-test APK for target Mi TV Stick profiling/soak. Device matrix: Home/Settings/D-pad/Back, weather, wallpaper 1080p/4K and rapid switching, Now Playing, StandBy/DreamService, low-storage, network failure, process death/reboot and long-run memory/thermal stability.
- Detailed evidence: `F07_PERFORMANCE_STABILITY_AUDIT.md`. Preserve Trigger → Input → Verification → Work → Decision → Output → QC → Final/Not Final.


## OFFICIAL PROJECT F CLOSURE & PROJECT G HANDOVER — 3 NOVEMBER 2026

**Checkpoint ID:** GTVH-F-CLOSE-2026-11-03  
**Project F disposition:** CLOSED — HOST/SOURCE EXIT PASS (F01–F07)  
**Project G disposition:** READY FOR EXECUTION — FINAL INTEGRATION, DEVICE ACCEPTANCE & RELEASE GATES  
**Approval:** User explicitly declared Project F officially closed and directed synchronized documentation for Project G.

### Frozen Project F baseline
- F07 integration branch: `integration/project-f07-performance-stability`
- F07 exact source/test HEAD: `976e068d48ecc1ede2811ac455372395d382ba76`
- F07 base F06 HEAD: `487099013e6d9bd65cc07d72e760b1e14ab1f091`
- F07 Draft PR #56: OPEN / DRAFT / UNMERGED; preserve under D-013.
- Exact-head CI #41568715178: both required jobs SUCCESS; Python 99/99; launcher and installer unit tests PASS; release build, APK diagnostics and size gate PASS.
- Diagnostic APK size: 1,571,293 bytes / 1,572,864-byte cap / 1,571-byte headroom. Workflow artifact list empty; no APK published or installed.
- F01–F07 are closed for their approved host/source scope. This is not a claim of full real-device acceptance.

### Explicit boundary carried into Project G
No physical Mi TV Stick profiling or soak was performed during F. No measured FPS/frame time, RAM/PSS, ANR/crash rate, thermal behavior, OEM/remote behavior, phone-to-TV LAN QR acceptance, or reboot/process-death acceptance is claimed. These are mandatory Project G evidence, not waived items.

### Official Project G roadmap — approved v1.0 (3 November 2026)
Project G contains exactly five stages. The overall A–G roadmap remains 47 work items; do not add, remove, reduce, or reorder any item.
1. **G01 — Penyelesaian Bug Terakhir (Final Bug Resolution).**
2. **G02 — Build APK Final yang Sudah Teruji (Final Tested APK Build).**
3. **G03 — Uji Instalasi pada Mi TV Stick (Mi TV Stick Installation Test).**
4. **G04 — Finalisasi Dokumentasi GitHub (Final GitHub Documentation).**
5. **G05 — Finalisasi Memory dan Handover ChatGPT (Final ChatGPT Memory/Handover).**

### Non-negotiable governance
- D-013 remains binding: owner branches are preserved; integration is isolated and staged; findings are fixed at the owning source; no early merge; final merge is a Project G decision requiring explicit approval.
- No unapproved APK install, HOME-default change, ADB/device setting change, data wipe, or package-state mutation.
- Never label device-only gates PASS without device evidence. A successful CI build is not a substitute for real-device acceptance.
- Maintain the workflow Trigger → Input → Verification → Work → Decision Point → Output → Quality Check → Final/Not Final.
- At each Project G session end, update HANDOVER, OPERATIONS_LOG, MASTER, INVENTORY, AUDIT_REGISTER, DECISION_LOG and roadmap; keep README status accurate.

### Project F closure decision
Project F is formally closed at HOST/SOURCE EXIT PASS for F01–F07. The remaining physical and final integration obligations are transferred to Project G with explicit gates above. PR #56 and all owner PRs remain unmerged; the F07 baseline is frozen as evidence, not promoted to main.


## Immediate resume instruction
Start Project G with G01 — Penyelesaian Bug Terakhir only. Audit and close remaining source/host defects at their owning branches with root-cause fixes, cross-audit, regression and exact-head evidence. G02 is the final tested APK build; G03 is Mi TV Stick installation/physical acceptance and requires explicit readiness approval. Do not merge, publish/install APK, or change device settings without the required approval.


## G01 final bug resolution checkpoint — 3 November 2026

- **Disposition:** HOST/SOURCE DEFECT AUDIT PASS; no newly reproducible source defect found in the remaining high-risk contracts. No speculative production edit was made.
- **Evidence:** `docs/project-management/G01_FINAL_BUG_RESOLUTION_AUDIT.md` records the root-cause review, owner PR exact heads, associated exact-head GitHub Actions runs, both required jobs, regression protection, and deferred device gates.
- **Verified contracts:** B01 Home gestures/Force Stop/power routing/Recent Apps manifest; D01 receiver authorization; D05/D06 persistence and recovery; C07 QR deadlines, session-stop atomicity and shared storage reservation; E01 AccessibilityService binding; E08 wallpaper recovery; F02 cross-module dialog/security/upload regressions; F06 localization; F07 performance/memory hardening.
- **Latest F07 evidence:** PR #56 exact head `976e068d48ecc1ede2811ac455372395d382ba76`; CI run #41568721270, both required jobs SUCCESS; 99 Python tests; launcher/installer unit tests, release build and APK budget PASS; diagnostic APK 1,571,293 / 1,572,864 bytes; artifact list empty.
- **Governance:** all owner PRs remain Draft/open/unmerged under D-013. No APK publication/installation, merge, HOME-default change, ADB/device mutation, or TV setting change.
- **Not passed:** real Mi TV Stick remote/OEM/UI, ADB-shell permission, phone/LAN QR, low-storage, process-death/reboot, RAM/FPS/ANR/thermal/soak acceptance remain G03 device gates.
- **Next approved stage:** G02 final tested APK build. TV installation remains G03 and requires explicit readiness approval.



## G01 branch reconciliation addendum — 3 November 2026

Project F is closed at HOST/SOURCE EXIT PASS. G01 defect-resolution audit is PASS, but project-wide branch reconciliation remains OPEN/BLOCKING: current B-series and F07 integration candidates have diverged, with 26 shared-file differences requiring semantic reconciliation. G02 final APK build is blocked until a clean isolated combined candidate passes exact-head integrated CI. No feature PR/main merge, APK publication/installation, or TV state change has occurred.

- Live repository inventory covers PRs #1–#56. Merged history: #1, #13–#15, #32–#33. Closed without merge: #42–#49 and superseded #52. Open/unmerged source/integration PRs: #2–#12, #16–#31, #34–#41, #50–#51, #53–#56.
- F07 chain remains linear from staged-full through #50 → #53 → #54 → #55 → #56. F07 exact head `976e068d48ecc1ede2811ac455372395d382ba76` is 173 commits ahead / 0 behind staged-full `8fe90a9fdd493c5d81ffdacbe16054630a26580a`.
- Current `integration/b-series` is `9070894fa6d9ef8aec034f46dfe777e866709dfe`, not an ancestor of F07. Compare staged-full → B-series: DIVERGED, 60 ahead / 466 behind, 26 changed paths. Compare B-series → F07: DIVERGED, 639 ahead / 60 behind, 123 changed paths. The 26 shared paths are listed in `G01_BRANCH_RECONCILIATION_ADDENDUM.md`.
- Targeted F07 checks are positive for HomeKeyService, C07 monotonic deadline/session/storage safeguards, manifest security declarations, dialog and upload regression tests. But source hashes differ on several owner/integration files, including manifest, RecentAppsActivity, WallpaperUploadServer, AvatarUploadServer, MainActivity, locale resources and ProfileManager; these require 3-way semantic reconciliation, not blind overwrite.
- G01 defect-resolution subgate PASS; G01 project-wide combine/reconciliation OPEN/BLOCKING. G02 is not ready. No feature PR or main branch merged; D-013 remains binding.
- Next work: freeze all required owner heads; reconcile B-series with F07 on an isolated candidate; run exact-head integrated CI and cross-group regression; only then build the G02 candidate APK. G03 physical acceptance remains separate.


### PR #57 integration conflict gate — 3 November 2026

- Draft PR #57 opened from `integration/project-f07-performance-stability` @ `976e068d48ecc1ede2811ac455372395d382ba76` into `integration/b-series` @ `9070894fa6d9ef8aec034f46dfe777e866709dfe`.
- GitHub reports `mergeable=false`; PR #57 is OPEN/DRAFT/UNMERGED. Compare view: 639 commits / 123 changed files. Do not merge or force-push around the conflict.
- The 26 shared B-series/F-staged-full paths require three-way semantic resolution. No conflict was resolved by wholesale side selection. Owner branches remain unchanged.
- G01 overall remains OPEN/BLOCKING for branch reconciliation; source-defect subgate remains PASS. G02 APK build is blocked pending clean combined candidate and exact-head integrated CI. `main` is untouched.


### Final G01 handover — 3 November 2026

- **Current integration branch:** `integration/b-series`
- **Accepted exact head:** `f6f1f05b7a560381b8bb423eaa707a1d6d0e8832`
- **G01:** PASS. All 26 shared B-series/F07 paths have documented semantic disposition; F06 Daily Verse locale defect corrected; PR #58 merged; exact merge-commit CI #41583505195 passed both required jobs.
- **CI:** 99 Python tests all OK; launcher/installer Android unit tests PASS; release validation build SUCCESS; APK diagnostic 1,571,293 / 1,572,864 bytes; no workflow artifacts.
- **Main branch:** untouched. The one main-only workflow_dispatch manual APK-publishing change remains intentionally excluded.
- **G02:** READY TO START from the accepted integration commit. Build and retain a traceable TV-test candidate only; do not publish or install without the approved G02/G03 process.
- **G03:** physical Mi TV Stick acceptance not started. No APK installed and no TV settings changed.
- **Next session:** begin G02 from `f6f1f05b7a560381b8bb423eaa707a1d6d0e8832`; do not re-open G01 unless new reproducible regression evidence appears.

## Latest resume checkpoint — G01 re-audit complete, 3 November 2026

- **Current phase:** Project G; G01 source/host re-audit is PASS. G02 is READY TO START, not yet started.
- **Integration branch/head:** `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- **Merged PR:** #59; PR #58 remains the earlier B-series/F07 reconciliation merge. Owner branches remain preserved.
- **Exact tested tree:** candidate `9f2da8f39a20c8e8cc43ac0ff7e42d7a82b34676`; merge commit comparison has zero changed files.
- **CI:** #41586223809 / run #1177 SUCCESS. 109 Python tests, one intentional skip; launcher/installer unit tests and release validation build PASS.
- **APK diagnostic:** 1,572,293 bytes / 1,572,864 cap / 571 bytes headroom. No artifact, publication or installation. Treat the margin as narrow; avoid further code growth before G02 without a measured need.
- **G01 corrections:** E05 location-scoped weather/AQI cache and API-backed display restored; F07 reduced-motion/low-memory preserved; F06 high/low localization preserved; E08 delete-failure regression test restored.
- **G02 next:** build/record the traceable APK from exact merge commit `562cde0751de93748eb4e0d4cdece4fa1672ac4d`; capture APK size and SHA-256. Do not publish/install until the approved G02/G03 process.
- **G03:** physical Mi TV Stick acceptance not started; remote/D-pad, OEM, QR/LAN, storage, process death/reboot, RAM/FPS/ANR/thermal/soak remain untested.
- **G04–G05:** final documentation closure and continuity handover remain pending. `main` untouched; no device state changed.


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


## G03 device acceptance preflight — 5 November 2026

- **Current stage:** G03 physical Mi TV Stick acceptance; preflight completed, physical execution BLOCKED / NOT FINAL.
- Live `integration/b-series` still points to exact candidate `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- G02 artifact #1185 / run `41590889219`: both jobs SUCCESS. Live artifact ID `13210715335` remains available; expiry 10 Nov 2026 06:37:50 UTC.
- Re-downloaded artifact ZIP SHA-256 `669491bfc7c9f5279ecc6c30915e4a340aa492bee21531db203036caf8651c81`; extracted APK is 1,572,293 bytes, SHA-256 `dc85fcd590eb2c52eb367d0bc9d4f67735cb94ef1cbf6f2b80b86f0de36de0dd`. ZIP integrity PASS; required manifest/DEX/resources entries present.
- Current host has no adb/apksigner/aapt/zipalign and no connected Mi TV Stick. No signature validation, install, package mutation, HOME/default change or device setting change performed.
- PR #61 CLOSED/UNMERGED; integration head unchanged; main untouched.
- G03 matrix, blocker and evidence checklist: [G03_DEVICE_ACCEPTANCE_PREFLIGHT_2026-11-05.md](G03_DEVICE_ACCEPTANCE_PREFLIGHT_2026-11-05.md).
- **Decision:** device mutation remains gated by explicit readiness approval under the approved governance. User's instruction to continue routine technical work does not waive this gate. G03 remains OPEN/BLOCKED; do not claim device PASS.
- Next: after explicit installation/readiness authorization and device connection, capture device identity/build/resolution/density/free storage and rollback baseline; then execute G03 matrix, capture logs/screenshots/metrics, route defects to owning branches, rerun exact-head CI and retest.


## E02 GIF avatar + E06 Dock handover — 5 November 2026

- Current feature heads: E02 PR #62 / `ea9415ec3f63f599872a5c3acda153e30f84e200`; E06 PR #63 / `ecdeb0eba9fc7a574f1a6c9930205517687cd922`. Both are OPEN Draft and unmerged.
- Exact-head CI: E02 run #1200 (ID `41891880056`) both required jobs SUCCESS; E06 run #1198 (ID `41890835096`) both required jobs SUCCESS. This is isolated-head build/test evidence only.
- E02 remains incomplete: GIF result grid has text tiles, not animated thumbnails; request cancellation does not explicitly disconnect active HTTP connections; selection commit is not fully gated on dialog/profile lifecycle; drawable stop lifecycle is not wired; DPAD/storage/profile-switch/memory regression coverage is incomplete.
- E06 remains incomplete: only independent preference model + normalization tests exist. Current MainActivity still shares one `COLUMNS` between grid columns, Dock threshold, AppAdapter and DockDecoration. Separate Dock view/adapter, orientation-specific layouts, overflow, settings UI, focus graph/scroll/reorder and six-combination simulation are not implemented.
- Full source audit and ordered implementation sequence: [E02_E06_GIF_AVATAR_DOCK_AUDIT_2026-11-05.md](E02_E06_GIF_AVATAR_DOCK_AUDIT_2026-11-05.md).
- No merge, integration/main update, APK publication, installation, ADB or Mi TV Stick evaluation performed. G03 remains explicitly deferred by the user.
- **Disposition: BELUM FINAL.** Next work is E02-A..E and E06-A..F on their owner branches; then reconcile dependency bases, run combined integration tests and exact-head CI. Do not mark either feature ready on the basis of CI alone.


## E02 implementation follow-up — 5 November 2026

- Implemented bounded still-frame GIF result previews in PR #62, including HTTPS/GIPHY host checks, 1 MiB response cap, MIME/dimension validation, 3-worker preview pool, holder recycling cancellation and stale-binding guards.
- Added dialog selection guards for closed/interrupted work and active-profile identity; updated GIF drawable to stop frame scheduling when hidden/detached.
- CI #1201 exposed a missing `java.io.File` import; corrected on the owner branch. Latest head `dafcaf8a6dd8003300aca155d3c02395f1a28165`; CI #1203 / run `41895682457` is still in progress. Do not claim this head passes until both jobs complete.
- E02 still requires shared connection cancellation/disconnect, live GIF preview validation, lifecycle/profile/storage/DPAD/memory tests and GIPHY policy verification. E06 has not received source implementation changes in this pass; separate Dock architecture/UI/navigation remains open.
- Overall E02/E06: **BELUM FINAL**.


## UI Refresh planning standby checkpoint — 6 November 2026

- User requested the UI team be placed on standby, preparation for the next interface build, and the responsible projects be called.
- Responsible owners called on their existing Draft PRs: **E02 Profile & Avatar (#62)** and **E06 Home Screen & Dock (#63)**. Standby comments were posted to both PR conversations with preparation deliverables and explicit scope boundaries.
- Preparation scope: reconcile owner heads/dependencies against approved integration; inventory source-backed screens, controls and data; prepare a unified UI/UX brief and 1920×1080 simulations; document risks, acceptance criteria and dependency order.
- E02 preparation must account for request/connection cancellation, GIF preview/playback lifecycle, DPAD/focus, profile switching, private storage rollback, memory, localization and provider attribution/policy.
- E06 preparation must cover a separate Dock view/adapter and ordered pinned-app model, horizontal/vertical orientation × capacity 4/5/6, overflow, independent focus/navigation and proof that main-grid columns remain unchanged.
- Current evidence boundary: PR #62 and #63 remain Draft/open/unmerged; isolated CI is not feature acceptance. The 5 Nov audit records both features BELUM FINAL. Reconcile live heads before any implementation.
- **Authorization boundary:** standby call authorizes preparation/audit/design brief/simulation only. It does not authorize production implementation, merge, integration/main updates, APK publication/installation, ADB, HOME/default-launcher changes or physical TV/device mutation. G03 remains separately gated.
- Preserve Lean Engineering, source-backed UI only, simple/elegant/space-efficient/readable design, shared B01 visual/modal/focus contracts, and the APK size budget (G02 headroom was 571 bytes).
- GitHub Issues are disabled for this repository; project calls were recorded as comments on PR #62 and #63 instead.
- **Next decision point:** submit the consolidated UI refresh proposal for user review; do not start implementation until explicit approval.


## UI Refresh roadmap v1.0 — completeness update — 6 November 2026

- Added canonical roadmap: [UI_REFRESH_ROADMAP.md](UI_REFRESH_ROADMAP.md), document GTVH-UIR-001 v1.0.
- Completeness review added explicit coverage previously missing from the short proposal: full UI state matrix (loading/empty/error/offline/permission denied/interrupted/no profile); overscan, text scaling and localization; D-pad focus-path contract; security/privacy and provider constraints; API/device compatibility; lifecycle cancellation; measured performance; safe persistence/migration and rollback; defect severity/stop rules; traceability from requirement through commit/test/evidence.
- The roadmap remains seven lean stages P0–P6. E02/E06 can parallelize only after baseline, shared contracts and prototypes are approved. Each implementation slice must be tested before integration.
- Acceptance requires zero open Critical/High defects, complete in-scope UI traceability, exact-head CI, and separate device evidence; green CI is not physical TV acceptance.
- **Governance unchanged:** this document is a proposed baseline awaiting explicit user approval. It does not authorize production implementation, merge, integration/main updates, APK publication/installation, ADB or physical device mutation. E02 #62 and E06 #63 remain subject to live-head reconciliation; G03 remains separately gated.
- Roadmap commit: ac51f60277b5571ed8c8825e0cf16c293edcae9b.


## UI Refresh roadmap locked — 6 November 2026

- User requested a completeness check, correction of omissions, and locking of the roadmap once complete.
- Completeness additions were incorporated into GTVH-UIR-001: state/error matrix, accessibility/TV focus and overscan, localization/text scaling, security/privacy, compatibility, lifecycle, performance measurement, persistence/migration/rollback, defect stop rules and traceability.
- **Decision: LOCKED BASELINE v1.0.** Canonical document: [UI_REFRESH_ROADMAP.md](UI_REFRESH_ROADMAP.md).
- Roadmap commit: 66f0d434f529929c106f31bfec2286e6caf1055f.
- This locks scope, sequencing and acceptance criteria only. It does not authorize implementation, merge, integration/main updates, APK publication/installation, ADB or physical device changes. E02/E06 execution still requires live-head reconciliation and explicit stage authorization; G03 remains separately gated.



## UI Refresh P0 — Baseline & Compatibility — 6 November 2026

- **Disposition: P0 PASS — BASELINE VERIFIED** at frozen `integration/b-series` SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d`. This is source/baseline PASS only; no feature, integration or device acceptance is implied.
- Live E02 PR #62 head is `feature/e02-giphy-avatar-search-v1` @ `588456144df24f2f6c015a01053ad700a04bee39`, based on `feature/e02-profile-avatar-lock-ui` @ `3ab9b55f3cfe03ad9841a119590a9183cf114e96`. PR remains OPEN/DRAFT/UNMERGED. Exact-head CI #1204 / `41895866368`: both required jobs SUCCESS; APK 1,566,569 B; publication skipped.
- Live E06 PR #63 head is `feature/e06-dock-capacity-control-v1` @ `ecdeb0eba9fc7a574f1a6c9930205517687cd922`, based on `feature/e06-home-screen-subtabs` @ `570b7c83efaec3ac515b78f1c6c119b777fc3b84`. PR remains OPEN/DRAFT/UNMERGED. Exact-head CI #1198 / `41890835096`: both required jobs SUCCESS; APK 1,563,741 B; publication skipped.
- The live PR heads now supersede stale E02 #1203 pending / `dafcaf...` and earlier E02 #1200 / `ea9415...` checkpoints. E02 #1204 is the current verified head.
- Both PR bases are ancestors of integration; the PR heads themselves diverge from integration (E02 19 ahead/326 behind; E06 3 ahead/297 behind). E01 current head `edd6461c932705a109ed9768649cae7e3c162c59` has three commits not contained in either E02/E06 base. This is documented for three-way semantic reconciliation before integration; no rebase/merge/cherry-pick was performed.
- B01 contracts inspected from the frozen integration tree: `TvUiTokens`, `TvDialogStyle`, `TvFocusIndicator`, `TvResponsiveLayout`. E02/E06 scoped PR diffs do not replace the shared token/dialog contract. P2 must still define general overscan and text-scaling rules.
- Compatibility source: compileSdk 34, minSdk 26, targetSdk 34, Java 17; last recorded Mi TV Stick Android 11/API 30, 1920×1080, density 320 (not physically re-probed). No ADB or device mutation.
- APK ceiling 1,572,864 B: integrated G02 baseline 1,572,293 B (571 B headroom); E02 isolated build 1,566,569 B; E06 isolated build 1,563,741 B. Separate builds are not an integrated-size forecast.
- E02 remains blocked on shared HTTP cancellation/disconnect, preview/lifecycle/profile-switch/memory tests and current GIPHY policy verification. E06 remains blocked on independent Dock model/view/adapter, settings, orientation/capacity/overflow, focus/navigation and six 1920×1080 combinations.
- No Critical defect was reproduced in this read-only P0 audit. No code, PR state, branch ref, integration/main, APK publication/install, ADB, HOME/default launcher or physical device was changed.
- **Next exact step:** P1 Functional Inventory. P1 has not started. P2/P3 approval remains mandatory before P4 implementation.
- Full evidence: `UI_REFRESH_P0_BASELINE_2026-11-06.md`.


## UI Refresh P1 — Functional Inventory checkpoint — 6 November 2026

- **Current stage:** P1 inventory prepared; **PARTIAL / USER ACCEPTANCE PENDING**. Canonical output: [UI_REFRESH_P1_FUNCTIONAL_INVENTORY_2026-11-06.md](UI_REFRESH_P1_FUNCTIONAL_INVENTORY_2026-11-06.md).
- **Documentation branch:** `docs/project-master-inventory`. P1 inventory created in commit `6022e4d6f5e6f3656ffc4cd00b0f8bb0ffbae157`; subsequent canonical-record sync commits follow in this checkpoint (use live branch head, not this intermediate SHA).
- **Source baseline:** integration `562cde0751de93748eb4e0d4cdece4fa1672ac4d`; E02 PR #62 `feature/e02-giphy-avatar-search-v1` @ `588456144df24f2f6c015a01053ad700a04bee39`; E06 PR #63 `feature/e06-dock-capacity-control-v1` @ `ecdeb0eba9fc7a574f1a6c9930205517687cd922`. Bases remain E02 `3ab9b55f3cfe03ad9841a119590a9183cf114e96` and E06 `570b7c83efaec3ac515b78f1c6c119b777fc3b84`. PRs OPEN/DRAFT/UNMERGED.
- **Exact-head CI:** E02 run #1204 / `41895866368`, E06 #1198 / `41890835096`; both required jobs SUCCESS; publication skipped. This is isolated source CI, not device or integrated acceptance.
- **P1 completed work:** mapped Profile/avatar surfaces, built-in/photo/GIPHY avatar actions, profile lifecycle/actions, Child Mode, locked/hidden app policy, parent PIN, Home app grid, current Dock behavior/preferences, Home settings, focus/navigation and state matrix. Every identified element has owner, current behavior or explicit absence, data/state/persistence/permission boundary and a test plan.
- **Open P1 clarifications:** QR manifest/endpoint authorization; current GIPHY provider policy; default/no-profile and profile-capacity invariants; package visibility/API matrix. Existing high-risk feature gaps remain E02 cancellation/lifecycle and E06 independent Dock architecture/focus.
- **No mutations:** no production code, owner branch, PR state, integration/main, APK publication/installation, ADB, HOME/default launcher or physical device changes.
- **Next exact step:** user reviews and accepts P1 inventory/scope and confirms the listed clarifications may carry into P2/P5. Only then start P2 Shared Design & Interaction Contract. Do not begin P4; P2/P3 approval is mandatory.


### P1 post-audit consistency note — 6 November 2026

- Live PR metadata rechecked: #62 and #63 are OPEN, Draft=true, merged=false; exact head/base SHAs unchanged.
- PR #62 body still contains stale text saying its latest head has no confirmed exact-head CI. This is contradicted by live run #1204 on exact head `588456144df24f2f6c015a01053ad700a04bee39`, both required jobs SUCCESS. PR body was intentionally left untouched in P1; reconcile the description on the owner PR in a later authorized documentation update.
- PR #63 body and live run #1198 are consistent. Do not treat mergeable=true as permission or acceptance.
- Final docs branch head verified identical to `5d891b42147a0dea71cc7b7f6ad0e0ce43783f12` before this note.


### Final P1 documentation sync — 6 November 2026

- P1 inventory and canonical records have been rechecked after the consistency addendum. Final documentation branch head: `8c9f398b2043a9e13b37fe6128291d3014faaeee` before this HANDOVER-only update; the HANDOVER update below is the final branch commit.
- Inventory document SHA was refreshed after correcting its historical compare note. No source branch or PR was modified.




## UI Refresh P1 Global UI Inventory Expansion — 6 November 2026

- **Trigger:** user authorized expansion of the existing P1 inventory across six named surfaces: Home Screen, Weather & AQI Dashboard, Profile Selector, Quick Settings Popup, Wi-Fi Popup and Global Settings Split Detail.
- **Canonical update:** expanded the existing `UI_REFRESH_P1_FUNCTIONAL_INVENTORY_2026-11-06.md`; no duplicate inventory document was created. Commit: `91af6e1c94c8b9e20bb1246c0901d4f589482b3e`.
- **Frozen source baseline:** `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`. Live PR #62 and #63 were rechecked OPEN/DRAFT/UNMERGED at their previously recorded exact heads. No branch/PR/integration/main changes were made.
- **Coverage added:** V01–V06 source mapping; seven Global Settings categories and nested controls; Quick Settings and Wi-Fi popup behavior; cross-surface state matrix; shared component reuse/gap register; module ownership matrix; proposed dependency/implementation order; open decisions and quality gate.
- **Important source findings:** Wi-Fi popup is status/SSID/IP plus Android Wi-Fi Settings deep-link, not an in-app network manager. Quick Settings is a separate anchored action menu, not the full Settings shell. Global Settings currently has Profile, Home Screen, Appearance, Status Bar & Weather, Visual Effects, Standby and Device Settings categories. Home Screen settings do not yet expose Dock orientation/capacity. Home still has coupled single-grid/Dock geometry. Weather screen has hourly/daily forecast and metric modules; AQI is modelled US AQI, not official Indonesian ISPU. Weather provider/source labeling includes a Malaysia JAS/APIMS path that needs regional verification.
- **Shared components:** reuse B01 `TvUiTokens`, `TvDialogStyle`, `TvFocusIndicator`, `TvResponsiveLayout`, `GlassDrawable`, existing Settings row builders and popup row/menu infrastructure. Existing token palette and Settings root are charcoal/matte with cyan focus accents; do not call it already matching the target visual system. General overscan/text scaling/day-normal-night contract remains open for P2.
- **Visual reference blocker:** exact canonical files for the six approved reference images were not found in the inspected repository tree/evidence. Existing promo/live screenshots were not substituted. Source mapping is complete at surface level, but pixel/geometry reference comparison is not evidenced.
- **Disposition: PARTIAL — BELUM PASS.** Six surface source mappings and seven Settings categories recorded; nested/device verification and exact reference comparison remain open. P2/P3/P4 not started. Await user review/approval and canonical V01–V06 reference assets.
- **No mutations:** no production source, owner branch, PR metadata, integration/main, APK, ADB, HOME/default launcher or physical TV was changed. Documentation-only commit on `docs/project-master-inventory`.


## UI Refresh P1 — Six canonical references reconciled (6 Nov 2026)
- User supplied all six canonical visual references V01–V06: Home Screen, Weather & AQI Dashboard, Profile Selector, Quick Settings Popup, Wi-Fi Popup, and Global Settings Split Detail. Earlier missing-reference blocker is now closed.
- Expanded the existing P1 inventory in place; no duplicate document created. Added per-reference visual target, source mapping, source-to-reference gap, P1 requirements, cross-reference design contract candidates, and updated quality disposition.
- Main confirmed gap: V05 depicts an in-app Wi-Fi master switch, nearby network list, rescan and network selection; current source only provides connection summary and Android Wi-Fi Settings handoff. User decision required: retain lightweight handoff or scope an expanded Wi-Fi experience after feasibility/security/API audit.
- Other key items: V04 Next photo action in Quick Settings remains unverified; V06 settings category taxonomy differs from source labels and needs approval; V02 must retain full locked V4 weather/AQI scope even though the reference image shows only a subset; V01 app-grid/Dock geometry coupling remains an architecture risk.
- Visual mapping is complete at reference/surface level, but runtime/device/pixel comparison is not performed. Overall P1 remains **PARTIAL — BELUM PASS**; functional parity and runtime state gates remain open.
- Inventory commit: b8bef437e872e1c99c73631ac5a1c3c3ff8a5aa2. Updated inventory path: docs/project-management/UI_REFRESH_P1_FUNCTIONAL_INVENTORY_2026-11-06.md on docs/project-master-inventory.
- No production source, feature branches, PR metadata, integration/main, APK, ADB or TV device changed. P2/P3/P4 remain not started and gated pending user decisions/approval.


## UI Refresh P2 — Shared Design & Interaction Contract started (6 Nov 2026)
- User directed: “Lakukan pekerjaan sesuai rekomendasi gambar.” Adopted V01–V06 as the official design direction and began P2 documentation.
- Updated P1 inventory with explicit user direction: V06 taxonomy adopted (Profil, Layar Utama, Tampilan, Widget & Informasi, Animasi & Efek, Layar Siaga, Pengaturan Perangkat); V05 Wi-Fi is the target experience but enhanced scan/connect/radio capabilities are conditional on Android 11/API, permission, security and device feasibility; V04 Next photo is a target action pending source verification; V02 retains the complete approved Weather V4 scope.
- Created docs/project-management/UI_REFRESH_P2_SHARED_DESIGN_INTERACTION_CONTRACT_2026-11-06.md (GTVH-UIR-P2-001). It defines shared glass/surface and contrast rules, typography, D-pad focus vs selected/active semantics, popup/dialog behavior, responsive/overscan constraints, common state patterns, all six surface contracts, module ownership/dependency order, acceptance criteria and open technical gates.
- P2 status: DRAFT — technical verification and user review required. P3 prototype and P4 implementation have NOT started and remain gated on explicit P2 approval.
- P1 remains PARTIAL — BELUM PASS for runtime/device/state verification; the missing visual reference blocker is closed.
- P1 direction update commit: 55142dbaf25c6333491b0420b61ef376a97bd6eb. P2 contract creation commit: 92d4d263ebb3aa539d28228ffe51c2b67802c5c1.
- No production source, feature branches, PR metadata, integration/main, APK, ADB or TV device changed.


## Trigger 2 — Global UI Functional Inventory finalized (6 Nov 2026)
- User asked to continue Trigger 2 after providing six visual references and directing the team to follow them.
- Finalized the existing canonical P1 inventory in place (no duplicate). Added finalization matrix for V01–V06, seven target Settings categories and owner mapping, cross-surface states/interactions, reuse/build/replace register, confirmed-vs-unverified gap register, dependency order and final quality disposition.
- **Trigger 2 disposition: INVENTORY COMPLETE — VALIDATION PARTIAL / BELUM PASS.** Inventory coverage and six-reference mapping are PASS. Source functional parity remains PARTIAL: Wi-Fi scan/network management is not in the evidenced source surface; Quick Settings Next Photo is not source-verified; Dock is coupled to the app grid; Dock preferences are not wired to UI/renderer; shared focus coverage and runtime states need validation.
- No runtime/device/ADB testing was performed. Do not claim all target behaviors exist. Runtime QA remains separate. P2 is a separate workstream; P3 prototype/P4 implementation remain gated by explicit P2 approval.
- Final P1 inventory commit: 0f45cdf2a2fb3ced3ce99611db4465b3fb15fa37. Canonical path: docs/project-management/UI_REFRESH_P1_FUNCTIONAL_INVENTORY_2026-11-06.md on docs/project-master-inventory.
- No production source, owner branch, PR metadata, integration/main, APK, ADB, HOME/default launcher or physical TV changed.


## UI Refresh P2 — COMPLETE; P3 authorized by standing user instruction (6 Nov 2026)
- User explicitly instructed that once a Trigger is commanded, complete its scope independently without repeated per-item approvals, then proceed to the next Trigger. This standing instruction authorizes normal in-scope decisions and continuation to the next roadmap Trigger; it does not authorize risky external actions or production implementation outside the roadmap gates.
- Completed P2 Shared Design & Interaction Contract in place. Added audited Android 11 Wi-Fi platform constraints and solution: read-only scan list is conditionally feasible with FINE_LOCATION/CHANGE_WIFI_STATE/ACCESS_WIFI_STATE and Location enabled; scan throttling 4 per 2 minutes foreground; radio toggle and saved-network management excluded for ordinary apps; use system Wi-Fi panel/settings and user-approved add-network API only where applicable. Official Android documentation sources are cited in the P2 contract.
- Resolved shared design baselines: 960×540 dp 16:9 canvas; safe content margins 48dp L/R and 27dp T/B; 8dp spacing rhythm; 56dp minimum interactive height, 64dp preferred primary actions; typography and radii tokens; focus/motion and reduced-motion; Day/Normal/Night/Auto; wallpaper scrim; settings taxonomy/search; Weather V4 preservation; Dock/grid separation; Next Photo owner/action contract; common state/failure matrix.
- P2 acceptance checklist all PASS at contract level. Runtime/hardware validation and implementation verification remain explicitly assigned to P3/P4/P5; no claim of runtime pass.
- **P2 status: COMPLETE — CONTRACT BASELINE FROZEN FOR P3.**
- P2 commit: db5d7e70b09910f4f5d25d83fd815d024eee7d90. Canonical doc: docs/project-management/UI_REFRESH_P2_SHARED_DESIGN_INTERACTION_CONTRACT_2026-11-06.md on docs/project-master-inventory.
- P3 — Six-Surface Visual Prototype & Interaction Validation is the next Trigger and is now authorized to start under the user's standing instruction. P4 production implementation remains gated until prototype/interaction QA and the roadmap implementation gate are satisfied.
- No production source, owner branch, PR metadata, integration/main, APK, ADB, HOME/default launcher or physical TV changed in P2.


## P2 Final — Contract frozen; P3 started (6 Nov 2026)
- P2 contract status changed from Draft to **COMPLETE — CONTRACT BASELINE FROZEN FOR P3** after closing the design and platform decision register.
- Audited Android 11 Wi-Fi constraints using official Android developer/API documentation. Final design: nearby scan results are conditionally supported with permissions and Location enabled; foreground scan throttling must be respected; no app-side radio toggle or unrestricted saved-network manager; use Android Settings panel/settings handoff, with user-approved add-network API only for its intended use case.
- Resolved: 960×540dp 16:9 design canvas; 48dp L/R and 27dp T/B safe margins; 8dp spacing rhythm; TV text/target/radius baselines; focus/motion/reduced-motion; Day/Normal/Night/Auto; wallpaper contrast; Next Photo E08 action contract; independent Dock/app-grid model; seven Settings categories and control-level search; full Weather V4 retention; common state/failure matrix.
- P2 acceptance checklist marked PASS at contract level. P3/P4/P5 own runtime, source implementation and device verification; those are not claimed as completed.
- P2 finalization commit: db5d7e70b09910f4f5d25d83fd815d024eee7d90. Final metadata/link cleanup commit: 2bd62bdacc5703cb5a629c6670cb56533ba658cf.
- **P3 started:** generated visual concept boards for all six surfaces; these are design previews, not source-backed runtime or interactive prototypes. The Wi-Fi image treatment must follow the P2 correction (no fake in-app radio switch; show Wi-Fi status and system handoff, with conditional scan list). Visual boards are attached in the conversation and are not yet committed as repository assets.
- Next P3 work: produce per-surface layout/state specifications and D-pad interaction test paths, reconcile prototype with P2 Wi-Fi capability boundary, then complete cross-surface visual/interaction QA. No production source, feature branches, PR metadata, integration/main, APK, ADB or TV device changed.


## P3 — Six-surface visual prototype specification started (6 Nov 2026)
- Created docs/project-management/UI_REFRESH_P3_VISUAL_PROTOTYPE_2026-11-06.md (GTVH-UIR-P3-001), with per-surface region maps, D-pad focus paths, error/empty/offline/permission states, and 12 cross-surface interaction scenarios.
- Generated overview concept boards for six screens in this conversation. They are illustrative only, not editable/interactive assets and not source-accurate screenshots.
- Found visual discrepancy: generated Wi-Fi concept artwork depicts a switch-like control. It is explicitly rejected by P2's Android 11 capability boundary. Correct V05 must show Wi-Fi state plus “Kelola Wi-Fi” system handoff; nearby scan results remain conditional on fine-location permission, Location enabled and scan throttling.
- P3 status remains IN PROGRESS until the Wi-Fi visual is corrected, all six editable/interactive prototypes are available, the V4 full data hierarchy and Dock independence are represented, and P3-01..P3-12 are walked through.
- P2 contract is COMPLETE and frozen. P2 final doc commit: db5d7e70b09910f4f5d25d83fd815d024eee7d90; metadata cleanup commit: 2bd62bdacc5703cb5a629c6670cb56533ba658cf. P3 spec creation commit: f9f9437cdba92d08213cf0c8f7f5f3ffd5ff4871.
- No production source, owner branch, PR metadata, integration/main, APK, ADB, HOME/default launcher or physical TV changed.


### P3 V1 interaction refinement — 6 Nov 2026
- Audited the first interactive prototype navigation and found two prototype-level defects: arrow navigation was linear rather than spatial; Back used a fixed Home destination instead of restoring the invoking surface/control.
- Corrected V1 to use directional nearest-control focus based on visible element positions and a navigation history that restores the previous surface and invoking control when resolvable.
- Updated the P3 scenario register: P3-01, P3-02, P3-09 and P3-11 now have prototype behavior implemented, but manual walkthrough/evidence remains pending. Other scenarios remain partial/pending; P3 is not PASS.
- Latest prototype artifact commit: `f09515c2d434a809e0fe594cbed36880cc2e09ce`. P3 spec latest blob: `c8c4770b1450382f8be8bf88620ec96b543140b2`.
- No production source, feature branch, PR, integration/main, APK, ADB or physical device changed.


## UI Refresh P3 — V2 prototype checkpoint (6 November 2026)

- Active roadmap phase: **P3 — Prototype & Review**. P0 baseline is recorded PASS; P2 shared contract is frozen COMPLETE. P1 functional inventory is mapped but remains **PARTIAL — USER ACCEPTANCE PENDING**. Do not describe P1 as accepted/closed.
- Re-read current governing roadmap, HANDOVER, MASTER_PROJECT, INVENTORY, DECISION_LOG, AUDIT_REGISTER, P1 inventory, P2 contract, P3 spec and current prototype artifact before this checkpoint. Live repository/PR state was refreshed; stale commit statements in earlier handover sections are historical only.
- Live PR #62 remains OPEN/DRAFT/UNMERGED, base `feature/e02-profile-avatar-lock-ui`, head branch `feature/e02-giphy-avatar-search-v1`. Current PR body records head `588456144df24f2f6c015a01053ad700a04bee39`, but also says exact-head CI is unconfirmed; P1/AUDIT_REGISTER record CI #1204 / run `41895866368` as both required jobs SUCCESS. Treat PR description CI sentence as stale. E02 still has cancellation/disconnect, guarded commit, GIF preview/lifecycle, DPAD, rollback, memory and provider-policy gaps.
- Live PR #63 remains OPEN/DRAFT/UNMERGED, base `feature/e06-home-screen-subtabs`, head branch `feature/e06-dock-capacity-control-v1`, head `ecdeb0eba9fc7a574f1a6c9930205517687cd922`. CI #1198 / run `41890835096` has both required jobs SUCCESS. Preference model exists; independent Dock model/renderer/adapter, settings, overflow, focus/scroll/reorder remain absent in production source.
- Frozen integration baseline remains recorded as `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`; no integration/main or owner branch was changed in this P3 work.
- Created V2 prototype artifact: [UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V2.html](prototypes/UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V2.html). V1 remains preserved. V2 adds 1920×1080 16:9 review framing; all six Dock orientation/capacity combinations; a separate Dock preview with fixed four-column app-grid preview; weather loading/success/error/offline simulation; Wi-Fi permission/location/offline/throttle/stale/empty states; modal confirm/failure surfaces; profile add/failure simulation; Next Photo 0/1/2/5-image states; seven-category control search; Day-glare/Normal/Night and reduced-motion modes; keyboard spatial focus and Back restoration.
- Artifact commits: V2 creation `22a816ca90678c3c666db1de9a13cf9121136d82`; Wi-Fi handoff dialog correction `65d4fbfcacaa3af49c8fe964367ace3bf6e09d59`. P3 spec update commit `699bdd6cf5f20d191ea8902608ca36eff0543add`.
- Static source-presence checks passed for six Dock combinations, grid-column invariance representation, Wi-Fi permission/location/failure states, weather refresh states, dialog states, contrast modes, reduced motion, focus/Back handler and search. This is **not** a browser run, screenshot comparison, device test, D-pad remote test or human viewing-distance acceptance.
- P3-01..P3-12 have been updated in the P3 spec with evidence and honest dispositions. All remain walkthrough/review pending where interactive or visual evidence is required. P3 is **PARTIAL / IN PROGRESS**, not PASS. No user design approval is inferred.
- No production source, feature branch, PR metadata, integration/main, APK, ADB, HOME/default launcher or physical TV changed. No browser session or physical TV test was run. No merge, publish or install occurred.
- **Exact resume point:** continue P3 review against V2 and the 12-scenario matrix. Validate browser keyboard interactions and all six Dock combinations, collect screenshots at target frame/TV scale, perform long-label/contrast/focus/accessibility review, correct reproducible prototype defects, then update evidence. Keep P4 BLOCKED until P1 acceptance and P3 prototype/review approval are explicitly satisfied. Do not repeat P0/P1 inventory or P2 contract work unless live dependencies change.


### P3 V2 source-review correction — V06 search focus (6 Nov 2026)
- A source review caught a prototype defect: control filtering moved focus away from the search input on each keystroke. Fixed it so typing keeps focus in the input; D-pad Down transfers focus to the first matching control. Empty-query reset also preserves search focus.
- Artifact correction commit: `ce6d45ca6a353bae867607d63d84c82c67eec4fa`; inline JavaScript syntax check PASS. This is not a browser walkthrough.
- P3 spec correction recorded at `70c7a90f81d779f685891b894f13461b61da681d`. Continue with actual browser/keyboard test of multi-character search and Down/Back focus restoration before marking P3-11 accepted.



## Master Visual Reference Global UI — audit checkpoint (6 November 2026)

- User requested a comprehensive read-only audit before implementation preparation. Dedicated result: `docs/project-management/UI_REFRESH_MASTER_VISUAL_REFERENCE_AUDIT_2026-11-06.md` (GTVH-UIR-MVR-AUDIT-001).
- **Overall: BELUM PASS — implementation readiness not established.** P0 baseline PASS; P1 functional inventory PARTIAL / user acceptance pending; P2 shared design contract COMPLETE/frozen; P3 V2 prototype PARTIAL / walkthrough and visual QA pending; P4 NOT STARTED/BLOCKED.
- Reusable global source components confirmed: `TvUiTokens`, `TvDialogStyle`, `TvFocusIndicator`, `TvResponsiveLayout`, `GlassDrawable`, `ModernSettingsDialog`, `TvFeedback`, `AppLanguage`. Extend shared contracts centrally; do not create parallel style systems. Global focus coverage and overscan/text-scaling remain incomplete.
- E02 PR #62 remains OPEN/DRAFT/UNMERGED at `588456144df24f2f6c015a01053ad700a04bee39`; E06 PR #63 remains OPEN/DRAFT/UNMERGED at `ecdeb0eba9fc7a574f1a6c9930205517687cd922`. Exact-head CI runs #1204 and #1198 have both required jobs SUCCESS. Both heads are diverged from integration and require three-way semantic reconciliation. E02 cancellation/lifecycle gaps and E06 Dock/grid coupling remain blockers.
- Module impact/ownership: B01–B07 shared UI/interaction foundation; E01 device/system; E02 profile; E03 wallpaper metadata; E04 standby; E05 Weather/AQI; E06 Home/Dock; E07 animation/effects; E08 wallpaper/Next Photo. Dependency sequence and risk register are in the audit file.
- **Next work point:** finish P1 acceptance and P3 V2 walkthrough/design QA; do not start P4, redesign or production source edits until the required gates pass. No source/branch/PR/integration/main/APK/device mutation occurred in this audit.



## UI Refresh — continued P3 static review checkpoint (6 November 2026)

- Continued work stayed within Trigger P3 and was read-only. Detailed report: `docs/project-management/UI_REFRESH_P3_V2_STATIC_INTERACTION_REAUDIT_2026-11-06.md` (report commit `4bac6067b3b32f415c8decf01479ca3aa5425ac3`).
- Found two high-severity V2 prototype blockers: nested Wi-Fi handoff loses the original dialog/focus chain; D-pad/arrow navigation is disabled while a modal is open. Medium gaps: inert Language/Wallpaper Interval controls, no Next Photo failure path, normal Wi-Fi rescan stuck loading, no profile-switch simulation, generic Restart/Power Off confirmation.
- V2 remains unchanged. Next: create a new versioned prototype revision correcting R01–R07, static/syntax re-check, then browser keyboard walkthrough and visual review. P1 acceptance and owner decisions remain pending; no user approval is inferred.
- No production source, branch, PR, integration/main, APK, ADB or TV device changed.



## UI Refresh — V3 prototype static QC checkpoint (6 November 2026)

- V2 preserved; new V3 prototype created at `docs/project-management/prototypes/UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V3.html`.
- Corrected R01–R07: nested dialog stack/root focus; modal D-pad; Language; Wallpaper Interval; Next Photo failure/unavailable; Wi-Fi rescan completion; profile switch; distinct restart/power-off confirmation.
- Static QC **PASS** (JS syntax parse without execution, no duplicate IDs, correction presence checks). Evidence: `docs/project-management/UI_REFRESH_P3_V3_STATIC_QC_2026-11-06.md` (commit `22039e3ac4f9df1c9f70c2dac09ddc9f4239cee7`).
- P3 remains PARTIAL until browser keyboard walkthrough, 12-scenario validation and visual comparison against the six user-approved references. No production source, branch, PR, integration/main, APK or device changed.



## UI Refresh — P1 disposition / P3 walkthrough next point (6 November 2026)

- P1 inventory is **CLOSED FOR PROGRESSION** with approved V01–V06 direction and agreed functional boundaries; technical/runtime gaps remain explicitly carried, not waived. Evidence: `UI_REFRESH_P1_ACCEPTANCE_DISPOSITION_2026-11-06.md` (commit `bfd5f252e309319fcbeecba6ec1818c39cdba316`).
- V3 prototype static QC PASS. Manual 12-scenario browser/keyboard walkthrough plan created at `UI_REFRESH_P3_V3_BROWSER_WALKTHROUGH_PLAN_2026-11-06.md` (commit `86b55ed6c3f8d849cdc6c2f9dde80f81f70ef5db`); execution NOT RUN.
- Current stage remains P3 PARTIAL. Next permitted work is executing the V3 walkthrough and visual comparison against the six approved references. No P4 or production source change until P3 exits.



## UI Refresh — V4 static QC checkpoint (6 November 2026)

- V3 preserved; new V4 artifact: `docs/project-management/prototypes/UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V4.html`.
- Fixed three additional V3 risks: Weather Refresh focus retained while busy; stale Wi-Fi scan completion cancelled when prerequisites change; modal Tab/Shift+Tab focus trapped and Backspace remains editable in text fields.
- Static QC **PASS**; evidence: `docs/project-management/UI_REFRESH_P3_V4_STATIC_QC_2026-11-06.md` (commit `97c27d55e1c4ac007ba2506e3f6cc7ebe56be60e`).
- Browser walkthrough and visual comparison are NOT RUN. P3 remains PARTIAL; P4 blocked. No production source, branch, PR, integration/main, APK or device changed.


## UI Refresh — V4 P3 scenario mapping checkpoint (6 November 2026)

- P3-01 through P3-12 have now been mapped to V4 source evidence in `UI_REFRESH_P3_V4_STATIC_QC_2026-11-06.md` (latest QC commit `69a5e4f718636bb87ec3fcea2fd3d496c0fe3fb8`). Static presence is explicitly not treated as scenario execution PASS.
- Walkthrough plan retargeted to V4 in `UI_REFRESH_P3_V3_BROWSER_WALKTHROUGH_PLAN_2026-11-06.md` (commit `08d24d259d4e7c012a946d640dbf86c7084746e4`). Added modal Tab/Shift+Tab, safe Backspace, Weather Refresh focus, and in-flight Wi-Fi scan invalidation cases.
- Current: V4 static QC PASS; all 12 browser scenarios NOT RUN; V01–V06 visual comparison NOT RUN. P3 PARTIAL, P4 blocked.
- No production source, owner branch, PR, integration/main, APK, ADB or TV device changed.


## UI Refresh — TRIGGER 4 prototype visual validation (6 November 2026)

- Started the requested visual-validation gate against V01–V06. The six reference panels were recovered from the saved Global UI Design & Interaction Contract / UI Refresh overview boards.
- V4 source-to-reference composition review found material deviations: Home lacks the reference wallpaper/top-information/Dock composition; Weather is a generic full-page metrics layout rather than the reference card hierarchy; Profile uses three generic cards rather than the four-card avatar selector; Quick Settings and Wi-Fi are full pages rather than anchored glass popups; Global Settings lacks the reference icon-led navigation/detail content and profile card.
- Cross-surface material gaps: V4 panel opacity is high (rgba alpha .94), no backdrop blur is declared, glow is mostly focus-only, and iconography is sparse/text-based. P2 safe margin is represented as 5% (96×54 px at 1920×1080, equivalent to 48×27 dp at the assumed 2× scale).
- Dedicated report: [UI_REFRESH_TRIGGER4_PROTOTYPE_VISUAL_VALIDATION_2026-11-06.md](UI_REFRESH_TRIGGER4_PROTOTYPE_VISUAL_VALIDATION_2026-11-06.md), report commit `c99dc2b01bf69c1ee4a698f82114a220a316100a`.
- **Disposition: PROTOTYPE VISUAL — REVISION REQUIRED.** This is a source-to-reference composition finding, not a screenshot measurement. A browser render/screenshot at exactly 1920×1080 was NOT captured; pixel/geometry, blur, color sampling and rendered legibility remain unverified.
- V4 is preserved unchanged. No production source, owner branch, PR, integration/main, APK, ADB or physical device changed. P4/mass implementation remains BLOCKED.
- **Next:** create a new versioned visual prototype revision, then obtain actual 1920×1080 screenshots for all six surfaces and repeat side-by-side comparison before requesting owner approval.

## UI Refresh — TRIGGER 4 V5 visual revision checkpoint (6 November 2026)

- Created new prototype-only artifact [UI_REFRESH_TRIGGER4_VISUAL_PROTOTYPE_V5.html](prototypes/UI_REFRESH_TRIGGER4_VISUAL_PROTOTYPE_V5.html); V4 remains unchanged.
- V5 source revisions: Home now has clock/status/navigation, location/weather chips, prayer/information strip and bottom Dock; Profile Selector has four cards; Quick Settings and Wi-Fi have centered glass-popup compositions; Global Settings has an icon-led seven-category sidebar, identity/status header and profile detail card. Shared CSS adds translucent panels, backdrop blur and layered shadow.
- Temporary placeholders remain: CSS scenic gradient instead of original photographic wallpaper; emoji avatars/icons instead of final artwork. Rendering is not verified.
- V5 static QC PASS: JavaScript syntax parse, one script, no duplicate IDs, required structures present. Evidence: [UI_REFRESH_TRIGGER4_V5_STATIC_QC_2026-11-06.md](UI_REFRESH_TRIGGER4_V5_STATIC_QC_2026-11-06.md).
- **Final disposition remains PROTOTYPE VISUAL — REVISION REQUIRED.** No browser render or 1920×1080 screenshot was captured; actual geometry, overlay behavior, text fit, colors, blur, glow and legibility remain unverified.
- V4 preserved; no production source, feature branch, PR, integration/main, APK, ADB or TV device changed. P4/mass implementation remains BLOCKED.
- Next: render all six V5 surfaces at 1920×1080, capture screenshots, compare each against its reference crop, correct observed deviations and only then request owner visual approval.


## UI Refresh P4 implementation start — 6 November 2026

- User explicitly instructed staged production implementation of the Global UI modules, starting with Home Screen & Dock, using approved V01–V06 and frozen P2 contract.
- Frozen source baseline: `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- Module 1 branch: `feature/ui-global-01-home-dock`; Draft PR #64: https://github.com/Frangga11/glass-tv-home/pull/64. PR base is the frozen integration baseline; no merge.
- First slice adds isolated `DockPreferences` (horizontal/vertical, capacity 4/5/6) and unit-test source. `MainActivity.COLUMNS` is deliberately unchanged; existing Home rendering is not yet altered.
- Audit confirmed the current single RecyclerView/GridLayoutManager and AppAdapter ordering couple Dock membership, app-grid columns and DockDecoration. Independent Dock model/renderer/adapter, settings UI, overflow, DPAD/focus and six-combination testing remain to implement.
- Exact-head CI and execution of the added tests are PENDING; 1920×1080 visual QC and physical TV/remote tests are NOT RUN. Module 1 remains IN PROGRESS, not accepted.
- Full scope, risks and verification matrix: `UI_REFRESH_P4_IMPLEMENTATION_2026-11-06.md`.
- No PR merged, APK published/installed, ADB or device settings changed.


## Module 1 continuation — Dock partition model — 6 November 2026

- Continued the Home Screen & Dock implementation on `feature/ui-global-01-home-dock`.
- Added `DockLayoutModel`, an immutable partition boundary for ordered app entries: Dock capacity is normalized to 4/5/6, order is preserved, blank/null/duplicate stable keys are excluded, and snapshot lists cannot be mutated.
- Added `AppAdapter.dockSnapshot()` as the read-only adapter boundary for the upcoming dedicated Dock renderer. The existing combined RecyclerView, app-grid span, D-pad/focus behavior and Home visual layout are intentionally unchanged in this slice.
- Added `DockLayoutModelTest` covering source order, all three capacities, invalid-capacity fallback, duplicate/blank keys, immutability, empty inventory and required stable-key function.
- Latest source head: `d739d0edad1449918faf44f538d42da632831714`; Draft PR #64 updated with exact scope and explicit outstanding work.
- Exact-head CI run #42178311594: Lists/tools/docs SUCCESS; Launcher build/unit tests IN_PROGRESS at last poll (Android SDK setup underway). Do not report latest head green until job completes.
- Earlier exact-head CI #42177905579 passed both jobs on initial preference-only head `7fe684f559140b11355dfb67cf2a246f518c7724`; this does not replace the new exact-head check.
- Module 1 remains IN PROGRESS. Dedicated Dock RecyclerView/adapter, independent main-grid data source, horizontal/vertical rendering, overflow, settings UI, D-pad/focus/reorder lifecycle, six-combination functional acceptance, 1920×1080 visual QC and physical TV acceptance are not implemented/verified yet.
- No merge, APK publication/installation, ADB or device-setting changes.


### Dock identity invariant correction — 6 November 2026
- Tightened the partition model to match P2 section 11.8 exactly: Dock membership is now selected by explicit pinned package identity, not by first-N list positions. Dock order follows the pinned-key list; non-pinned apps preserve inventory order.
- Missing packages do not reserve slots, duplicate/blank identities are ignored, and pinned items beyond capacity stay visible in the app grid. The Dock snapshot reads the independent Dock capacity preference; it does not read or change `MainActivity.COLUMNS`.
- Added/expanded tests for explicit identity membership, separate Dock/grid order, overflow retention, missing apps, duplicate keys, immutable snapshots and 4/5/6 capacity.
- Latest source/audit head `9cfdeb902689d249e9e01e4ac9c6c91902905a62`; exact-head CI #42178556451 is IN_PROGRESS. Earlier run #42178311594 passed build/test and size-gate steps on the intermediate head but does not verify the latest explicit-identity change.
- Detailed source audit: `P4-01_HOME_DOCK_PARTITION_AUDIT_2026-11-06.md`. Dedicated Dock renderer and app-grid adapter separation remain unimplemented; Module 1 stays IN PROGRESS. No merge or device action.


### Exact-head CI closeout — 6 November 2026
- Latest audited E06 source/audit head remains `9cfdeb902689d249e9e01e4ac9c6c91902905a62`.
- GitHub Actions CI run #1212 (`42178556451`) completed successfully: Launcher build/unit tests/release validation/APK size budget PASS; Lists/tools/docs PASS.
- This is a verified source/build checkpoint, not a Module 1 acceptance. The current source still uses one combined Home RecyclerView and one adapter; `DockLayoutModel` is a model boundary only. A separate Dock renderer, separate app-grid renderer, orientation-specific layout, settings controls, overflow/D-pad/focus/reorder migration and rendered 1920×1080 visual validation are still outstanding.
- PR #64 remains Draft/open/unmerged. No APK installation, ADB or physical TV acceptance was performed.
- **Decision:** do not advance to the next trigger yet. Complete the independent two-region renderer and all interaction/visual acceptance gates first; a green CI run alone does not close E06.


### Component identity correction and exact-head verification — 6 November 2026
- Found and fixed a correctness risk in the Dock snapshot: package name is not a unique launcher-tile identity because one package can expose multiple Leanback launcher activities. `AppAdapter.dockSnapshot()` now expands configured package favourites into component identities (`ComponentName.flattenToShortString()`), then partitions by component key. This preserves separate activities and prevents accidental model-level tile collapse.
- Added regression test `distinctLauncherComponentsFromSamePackageRemainDistinctTiles` and updated the source audit to record package-config-to-component-identity mapping.
- Source PR #64 current head: `ba5ccecd4a55e748aec5de45343b778c2fe4595b`; PR remains OPEN/DRAFT/UNMERGED, base `integration/b-series`. Exact-head CI run #1215 (`42180187065`) is IN_PROGRESS at checkpoint; both jobs are still running. No PASS is claimed until completion.
- The earlier green CI #1212 applies only to prior head `9cfdeb902689d249e9e01e4ac9c6c91902905a62` and does not validate this correction.
- Module 1 remains IN PROGRESS. Dedicated two-region renderer, orientation/settings, focus/D-pad/reorder/install-remove behavior, six-combination acceptance, 1920×1080 visual QC and physical remote acceptance remain open. No merge, APK publication/installation, ADB or device-setting change.


### Exact-head CI closeout — component identity correction — 6 November 2026
- PR #64 source head `ba5ccecd4a55e748aec5de45343b778c2fe4595b` verified by GitHub Actions run #1215 (`42180187065`): **both required jobs SUCCESS**. Launcher build, unit tests, release validation and APK size budget passed; Lists/tools/docs passed.
- This CI result validates the component-level Dock identity correction and its regression test at the exact head. It does not complete the Dock renderer or Module 1 acceptance.
- PR #64 remains OPEN/DRAFT/UNMERGED, base `integration/b-series`. Dedicated Dock/App Grid rendering, settings, orientation/capacity behavior, overflow, D-pad/focus/reorder lifecycle, six-combination functional acceptance and 1920×1080 visual QC remain outstanding. No device test, APK publication/installation, ADB, merge or device-setting change.


### P4-01 continuation checkpoint — implementation sequence clarified — 6 November 2026
- Re-fetched PR #64 and source branch: head remains `ba5ccecd4a55e748aec5de45343b778c2fe4595b`; base `integration/b-series`; PR OPEN/DRAFT/UNMERGED and mergeable.
- Corrected the PR description: the former embedded CI note referenced prior head `9cfdeb...`; the current exact-head CI #1215 (`42180187065`) is SUCCESS in both required jobs. Android build, unit tests, release validation/APK size budget and Lists/tools/docs passed.
- Architecture sequence is now explicit: (1) extract one authoritative app inventory/identity boundary; (2) separate reusable tile binding/launch behavior from a single RecyclerView's positional assumptions; (3) introduce independent Dock and App Grid renderers while preserving App Grid span count; (4) connect orientation/capacity preferences; (5) migrate identity-based focus, D-pad, reorder and install/remove/hidden-app lifecycle; (6) validate all six orientation/capacity combinations and 1920×1080 visual QC.
- Guardrail: do not change `MainActivity.COLUMNS` to Dock capacity and do not add a second RecyclerView as a superficial patch. Both would leave existing tile geometry, decoration, parking, reorder and focus contracts inconsistent.
- No production source commit was made in this continuation because the required inventory/renderer/focus refactor is cross-cutting; a partial view insertion would create a misleading or unsafe state. Next source change must implement the shared identity/inventory boundary and its tests as one reviewable slice.
- Module 1 remains IN PROGRESS. No APK publication/installation, ADB, device setting change, physical TV test or merge.


### P4-01 source correction — saved pinned order — 6 November 2026
- Implemented `DockLayoutModel.orderedPinnedKeys(...)` and wired `AppAdapter.dockSnapshot()` to it. The Dock's explicit pinned component list now honors persisted user arrangement order among configured favourite packages; unarranged/new favourite components append in configured package order. The Home Settings shortcut is excluded from package matching.
- Added regression tests for saved pinned order and fallback ordering of newly installed/unarranged favourite components.
- Source branch `feature/ui-global-01-home-dock`, PR #64 current head `ade40dac76bf98621a41bdd8be08d0b0971dbf1c`; PR OPEN/DRAFT/UNMERGED, base `integration/b-series`.
- Exact-head CI #1218 / run ID `42185963306` is IN_PROGRESS at this checkpoint. No PASS is claimed until both required jobs finish successfully.
- Scope remains a model/order correction only. The two-region renderer, independent App Grid geometry, orientation/capacity settings, focus/D-pad/reorder and lifecycle migration, six-combination acceptance and 1920×1080 visual QC remain open. No APK publication/installation, ADB, device setting change, physical TV test or merge.


### Exact-head CI closeout — saved pinned-order correction — 6 November 2026
- PR #64 source head `ade40dac76bf98621a41bdd8be08d0b0971dbf1c` verified by GitHub Actions CI #1218 / run ID `42185963306`: **both required jobs SUCCESS**.
- `Lists, tools and docs`: SUCCESS. `Launcher build and unit tests`: SUCCESS, including unit tests, release build validation, APK size diagnostics and release APK size budget enforcement.
- This validates the saved pinned-order correction and new regression tests at the exact head. It remains a model/order slice only, not completion of P4-01 Module 1.
- PR #64 remains OPEN/DRAFT/UNMERGED, base `integration/b-series`; no APK publication/installation, ADB, device-setting change or physical TV test.
- Remaining: authoritative shared inventory/identity boundary, dedicated Dock and App Grid renderers with invariant grid span, orientation/capacity UI and live updates, focus/D-pad/reorder/install-remove lifecycle, six-combination functional acceptance and 1920×1080 visual QC.


### P4-01 implementation checkpoint — authoritative inventory boundary — 6 November 2026
- Implemented `AppInventory<T>` as the ordered inventory owner in `AppAdapter`, keyed by stable launcher component identity (`ComponentName.flattenToShortString()`). Rebuild now replaces the inventory through one deduplicating operation; tile movement uses an identity-preserving inventory move; stable-key lookup and detached immutable snapshots are available for the upcoming two-region renderer.
- Added `AppInventoryTest` coverage for duplicate/blank/null identity filtering, source-order retention, stable-key lookup after reorder, invalid moves, and detached immutable snapshots.
- Source commits: `c3186a67ec822224abbb50ac7bf52658ebbaf438` (inventory class), `93e9e582cd74707456876bc377f1a7d38fc50529` (AppAdapter integration), `2f5ba0051a24bce92c60f05445c106ab2e55f8d2` (tests). PR #64 branch `feature/ui-global-01-home-dock`, current head `2f5ba0051a24bce92c60f05445c106ab2e55f8d2`, base `integration/b-series`; PR remains OPEN/DRAFT/UNMERGED.
- Exact-head CI #1221 / run ID `42187109874`: `Lists, tools and docs` SUCCESS; `Launcher build and unit tests` still IN_PROGRESS at checkpoint. No full CI PASS is claimed until the Android build, unit tests, release validation and APK-size budget job completes.
- Scope is the authoritative inventory foundation only. The visible UI is still the legacy single RecyclerView; reusable tile binding/launch extraction, dedicated Dock/App Grid renderers, invariant App Grid span, orientation/capacity settings, overflow and D-pad/focus/reorder/package lifecycle remain outstanding.
- No APK publication/installation, ADB, device-setting change, physical TV test or merge. Module 1 remains IN PROGRESS; do not advance to the next trigger.


### P4-01 follow-up — APK budget correction and tile behavior extraction — 6 November 2026
- Exact-head CI for inventory implementation commit `2f5ba0051a24bce92c60f05445c106ab2e55f8d2`, run #1221 / `42187109874`: Android unit tests and release build passed; APK diagnostics passed; size-budget enforcement failed because the APK was 1,573,081 bytes, 217 bytes above the strict 1,572,864-byte ceiling. Lists/tools/docs passed. The budget was not raised.
- Reduced inventory bookkeeping overhead by removing the secondary HashSet and using identity scan on insertion. Inventory rebuild is infrequent and bounded by installed launcher-app count; identity deduplication semantics remain covered by tests. Commit: `3d29bda978e5e080cc2b169ce1c2b267d36546e3`.
- Extracted renderer-independent `TileBinder` for tile visual binding and activation/launch behavior. `AppAdapter.onBindViewHolder()` delegates tile appearance and launch to the binder; long-press/reorder callback remains adapter-owned because it is position/controller behavior. This creates a reusable binding/launch seam for Dock and App Grid adapters without changing layout or focus yet. Commits: `f8fa48c6eed1208d12a9646142f3249438425c4b` and `38373df4b4268b8da9b6c160865bd8771cdca6dd`; stale legacy bind block cleanup: `779f22736b31cbcea55f04386d3b2e09e2845eaa`.
- Current PR #64 source head: `779f22736b31cbcea55f04386d3b2e09e2845eaa`; branch `feature/ui-global-01-home-dock`, base `integration/b-series`; PR remains OPEN/DRAFT/UNMERGED. Exact-head CI #1224 / run ID `42191416346` is IN PROGRESS at checkpoint; no full PASS is claimed.
- The independent Dock/App Grid renderer is not yet implemented. MainActivity remains a single RecyclerView; do not treat binder extraction as renderer completion. Remaining: separate adapters/containers backed by shared inventory, App Grid span invariance, orientation/capacity settings and rendering, overflow, focus/D-pad/reorder and app lifecycle migration, six-combination functional tests, and 1920×1080 visual QC. Physical TV acceptance still requires explicit authorization.
- No APK publication/installation, ADB, device-setting change, physical TV test or merge. Module 1 remains IN PROGRESS; do not advance the next trigger.

## 6 Nov 2026 — continuation checkpoint (PR #64)
- Rechecked PR #64: OPEN / DRAFT / UNMERGED; base `integration/b-series` at `562cde0751de93748eb4e0d4cdece4fa1672ac4d`; source head `779f22736b31cbcea55f04386d3b2e09e2845eaa`.
- Exact-head CI #1224 (`42191416346`) completed FAILURE. `Lists, tools and docs` PASS; unit tests and release build PASS; APK diagnostics PASS; strict APK size enforcement FAIL.
- Exact APK size is **1,573,125 bytes**, against strict limit **1,572,864 bytes**: **261 bytes over**. Prior head #1221 was 1,573,081 bytes (217 bytes over), so the AppInventory bookkeeping reduction did not resolve the budget and the latest APK is 44 bytes larger. Threshold unchanged; no budget gaming.
- Source audit reconfirmed the single-RecyclerView coupling. `MainActivity` owns one `grid` with `GridLayoutManager(COLUMNS)` and `DockDecoration(COLUMNS)`; `AppAdapter.Holder` still routes focus/D-pad callbacks through combined adapter positions. `onTileVerticalKey`, `onTileHeld`, move mode, focus restore, tray drawing, top-button alignment and icon-animation updates all assume that one combined adapter. `activity_main.xml` still exposes one `R.id.grid`.
- `TileBinder` successfully centralizes visual binding and app launch, but it is not yet a complete renderer-independent interaction seam: `AppAdapter.Holder` still captures `TileListener` and reports combined positions. A two-region adapter must move focus, long-press and key routing to stable component identity / region-aware callbacks before separate adapters are safe.
- Next implementation gate: design and test identity-based tile interaction callbacks and a region-aware focus/navigation controller, preserving existing single-grid behavior during extraction. Then implement independent Dock/Grid adapters and layout as one cohesive migration; do not change `COLUMNS` to Dock capacity.
- Module 1 remains IN PROGRESS / NOT READY for trigger 2. No merge, APK publication/install, ADB, device setting change, or physical TV test authorized.

## 6 Nov 2026 — APK budget policy decision
- User approved increasing the release APK size ceiling rather than removing fonts, wallpaper, assets or functionality to recover a few hundred bytes.
- Updated `.github/workflows/ci.yml` on `feature/ui-global-01-home-dock` at commit `e4bc99c9a4822057ce3bd5490df0ad1471763409`: release APK maximum is now **1,677,722 bytes (1.6 MiB)**, enforced with `<=`; the check remains active and bounded.
- The latest measured APK (1,573,125 bytes) is below the new ceiling, leaving approximately 104.6 KB of headroom. This is a policy adjustment, not a claim that the new exact-head CI has passed; a fresh PR CI run is required.
- Preserve product quality: do not trim required fonts, default wallpaper, accessibility, features or UI behavior merely to satisfy the previous 1.5 MiB threshold. Revisit the ceiling only if actual growth warrants it, with explicit rationale and review.
- PR #64 remains OPEN / DRAFT / UNMERGED. Architecture and functional work for independent Dock/App Grid renderers remains outstanding; Module 1 is still IN PROGRESS.


## 6 Nov 2026 — Global UI Design System audit started; PR #64 CI closed
- PR #64 source branch `feature/ui-global-01-home-dock`, head `e4bc99c9a4822057ce3bd5490df0ad1471763409`, base `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`. PR remains OPEN / DRAFT / UNMERGED.
- Exact-head GitHub Actions run `42192107405`: both required jobs completed SUCCESS. Launcher build/unit tests/release validation/APK diagnostics/1.6 MiB budget enforcement PASS; Lists/tools/docs PASS. APK log reports 1,536 KB rounded; do not infer an exact byte count from that display.
- This closes the pending CI trigger only. P4-01 Home & Dock remains IN PROGRESS; separate Dock/App Grid renderers, region-aware focus/D-pad/reorder, six-combination tests and 1920×1080 visual QC remain open.
- Began Global UI Design System audit from the saved reference board V01–V06 plus Profile & Security and Home Settings detail images. New canonical audit: `GLOBAL_UI_DESIGN_SYSTEM_AUDIT_2026-11-06.md`, commit `9412b1cef40b6496ac9560771a3ca9e73471cbaf`.
- Source audit confirms reusable `TvUiTokens`, `GlassDrawable`, `GlassRim`, `GlassView`, `TvFocusIndicator`, `TvDialogStyle`, `ModernSettingsDialog`, `TvResponsiveLayout`, `TvFeedback` and `AppLanguage`. Current `TvUiTokens` uses charcoal/gray surfaces and compact Settings-specific dimensions, materially unlike the reference's navy/blue/cyan glass palette. Focus and material helpers are reusable but not universal; focus coverage and blur/material measurements remain incomplete.
- Global UI Design System disposition: AUDIT IN PROGRESS / BELUM PASS. V5 static QC is not browser rendering; P3 12-scenario walkthrough, actual 1920×1080 screenshots, side-by-side visual review and owner approval remain blockers. No mass implementation or production source changes authorized/performed.
- No merge, APK publication/installation, ADB, device-setting change or physical TV test.


## 6 Nov 2026 — Global UI implementation request / dependency map
- User authorized staged implementation across Home, Profile/Avatar/Child Mode, Global Settings, Wi-Fi, Weather/AQI, Dock, Widgets, Effects, StandBy, Device Settings and all submenus/popups/dialogs, with no function loss, no premature merge, and per-module audit → implementation → integration → remote/focus tests → visual QC → documentation.
- Clarification: Global UI Design System is NOT yet complete/verified PASS. The canonical audit explicitly records token mismatch, incomplete focus/material coverage, incomplete source call-site inventory, and missing real 1920×1080 browser/screenshots. Therefore no mass production styling has begun.
- Added rollout ownership/dependency matrix to `GLOBAL_UI_DESIGN_SYSTEM_AUDIT_2026-11-06.md`, commit `45f1a4ae6cbf19c5bd7d79f5072d650eb297bdfc`. Sequence: P3 visual acceptance → B01 tokens/components → B02 focus → B03 dialogs/popups → B06/B07 feedback/focus restoration → E01–E08 by owner → Project F integration.
- Open PR inventory checked: B01–B03 #10–12; B06/B07 #16–17; E01–E08 #34–41; follow-ups #62/#63; P4-01 #64; Project F #50–56. All are Draft/open; no merges performed. #62/#63 remain owner follow-ups, not replacements for parent modules.
- Exact-head CI #1225 / run `42192107405` on PR #64 head `e4bc99c9a4822057ce3bd5490df0ad1471763409` is SUCCESS for both jobs. This closes CI only; P4-01 remains in progress and PR #64 remains Draft/unmerged.
- Immediate next task: actual V5 browser walkthrough and 1920×1080 screenshot comparison; then complete B01 call-site inventory before central token/focus changes. No production source change in this continuation.


## R0 reconciliation checkpoint — 6 November 2026
- R0 audit completed and recorded in `R0_RECONCILIATION_AUDIT_2026-11-06.md` (audit result COMPLETE; overall project readiness NOT PASS).
- Live refs: integration `562cde0751de93748eb4e0d4cdece4fa1672ac4d`; PR #62 `588456144df24f2f6c015a01053ad700a04bee39`; PR #63 `ecdeb0eba9fc7a574f1a6c9930205517687cd922`; PR #64 `e4bc99c9a4822057ce3bd5490df0ad1471763409`. All three PRs remain OPEN/DRAFT/UNMERGED.
- Exact-head CI: #62 run #1204 SUCCESS (both jobs); #63 run #1198 SUCCESS (both jobs); #64 run #1225 SUCCESS (both jobs, including its 1.6 MiB budget gate). PR #62 and #64 descriptions corrected to remove stale CI-pending statements.
- #62 is 19 ahead / 326 behind integration; #63 is 3 ahead / 297 behind; both are diverged and require semantic reconciliation. #64 is 25 ahead / 0 behind integration. No merge/rebase/cherry-pick performed.
- P1 ACCEPTED/CLOSED FOR PROGRESSION; P2 COMPLETE/FROZEN; P3 PARTIAL. V5 static QC PASS only; real browser rendering at 1920×1080, P3-01..P3-12 execution, screenshot comparison and owner approval remain NOT RUN. P4 broad rollout remains blocked.
- **Resume point:** P3 — render V5 in a real browser at 1920×1080, capture V01–V06, execute the 12-scenario walkthrough and record deviations/evidence. Do not infer browser/device PASS from static QC or CI. No production source, integration/main, APK, ADB or physical TV changed in R0.

## Current resume checkpoint — UI Refresh G1 complete (6 November 2026)

- Governing UI refresh plan: `GTVH-UIR-002 v2.0`; work package **G1 COMPLETE / internal QC PASS**.
- Canonical prototype: `prototypes/UI_REFRESH_G1_GLOBAL_UI_PROTOTYPE_V6.html`; full record: `UI_REFRESH_G1_CHECKPOINT_2026-11-06.md`.
- V01–V06 rendered and visually reviewed against the six supplied references at 1920×1080. All six Dock variants (horizontal/vertical × 4/5/6) were rendered. Browser functional suite: 12 PASS / 0 FAIL; JS syntax PASS.
- Tested D-pad arrow movement, popup/back focus restoration, nested dialogs, Child Mode PIN gate, Next Photo edge/error/success, Weather V4 missing/offline/stale states, Wi-Fi permission/Location/offline/throttle/stale/empty states, Settings category/search, daylight and large-text presentation.
- **Scope boundary:** prototype and documentation only. No production source, PR #62/#63/#64, integration/main, APK, ADB or physical TV changed. No merge/rebase/cherry-pick was performed.
- Owner visual sign-off is still a separate product-acceptance gate before broad production rollout. Browser simulation is not device acceptance.
- **Resume at G2 — Shared UI Foundation Repair.** Preserve GTVH-UIR-002 order and existing owner-branch/merge/device-mutation boundaries. The separate official Project G03 Mi TV Stick gate remains OPEN/BLOCKED.


## Current resume checkpoint — G2 complete (6 November 2026)

- G2 Shared UI Foundation Repair is **COMPLETE / SOURCE-HOST QC PASS**. Canonical checkpoint: `UI_REFRESH_G2_CHECKPOINT_2026-11-06.md`.
- Source branch `feature/g2-shared-ui-foundation-repair`, exact head `40fa0715bdbcd324042927fbc56ec19f91d1947a`; PR #65 is OPEN/DRAFT/UNMERGED and targets `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- Exact-head CI #1263 / run `42252069520` passed both required jobs, including app and installer unit tests and release build. APK diagnostic: 1,573,713 bytes; size threshold is non-blocking under the user's updated policy.
- Shared palette/tokens, glass/solid material, dialog sizing/fallback, safe popup geometry/scrolling, focus motion/restore, 56dp D-pad targets and Weather/AQI shared palette are implemented.
- No merge/rebase/cherry-pick, APK publication/install, ADB or physical TV testing. Physical TV/remote/OEM overscan acceptance remains deferred.
- **Resume at G3 — Surface Implementation**, in order 3A Home & independent Dock → 3B Weather/AQI V4 → 3C Profile/Avatar/Child Mode → 3D Quick Settings → 3E Wi-Fi → 3F Global Settings.


## Current resume checkpoint — UI Refresh G3A complete (7 November 2026)

- Governing plan: GTVH-UIR-002 v2.0. **G3A Home Screen & Independent Dock: COMPLETE / SOURCE-HOST QC PASS.** Detailed evidence: `UI_REFRESH_G3A_CHECKPOINT_2026-11-07.md`.
- Owner branch `feature/g3a-home-independent-dock` exact HEAD `87302f52de844954392bac5438cbe82ec5d14a30`; PR #66 OPEN/DRAFT/UNMERGED. G2 branch exact HEAD `40fa0715bdbcd324042927fbc56ec19f91d1947a`; PR #65 OPEN/DRAFT/UNMERGED. Both remain unmerged; no rebase/cherry-pick/force-push.
- Exact-head CI #1296 / run `42266095885` SUCCESS; both required jobs passed. Launcher unit tests, release validation build and lint-vital PASS. APK diagnostic 1,589,825 bytes, informational under D-023.
- G3A includes independent Dock/Grid projections; horizontal/vertical orientation; capacity 4/5/6; four-column Grid invariant; persisted pin order/unpin exclusions; overflow; D-pad navigation/reorder/pin/unpin; focus restoration; empty/full states; Home Settings and localization.
- No APK publication/installation, ADB, integration/main change, merge or physical Android TV/remote/OEM overscan test. No device PASS is claimed.
- **Resume at G3B — Weather & AQI V4.** Start with current exact branch/source audit and data/cache/state/test contract; do not reopen G3A absent new evidence. Official Project G03 physical Mi TV Stick acceptance remains a separate OPEN/BLOCKED gate.

## Current resume checkpoint — UI Refresh G3B complete (7 November 2026)

- Governing plan: GTVH-UIR-002 v2.0. G1, G2, G3A and G3B are COMPLETE / SOURCE-HOST QC PASS. Physical device acceptance remains separately OPEN/BLOCKED.
- G3B branch feature/g3b-weather-aqi-v4 exact HEAD 8cfc5c886950cc9d27d4530ba901df872b8e5c44; PR #67 OPEN/DRAFT/UNMERGED, base G3A exact HEAD 87302f52de844954392bac5438cbe82ec5d14a30. CI run 42274182438 passed on functional commit 8f2162036991ded803c5ca0981ad1f3e777cfb54; branch HEAD differs only by one test comment.
- G2 branch remains 40fa0715bdbcd324042927fbc56ec19f91d1947a / PR #65 OPEN/DRAFT/UNMERGED. G3A remains 87302f52de844954392bac5438cbe82ec5d14a30 / PR #66 OPEN/DRAFT/UNMERGED. integration/b-series remains 562cde0751de93748eb4e0d4cdece4fa1672ac4d.
- G3B implements complete Weather V4 metrics and states, Open-Meteo/CAMS US AQI, five separate pollutant values, legacy JAS cache invalidation, coordinate-scoped freshness, source timestamps, focus restoration and Activity lifecycle handling.
- Exact CI: both required jobs SUCCESS; release build, release unit tests, lint-vital and repository contract tests PASS. APK diagnostic 1,600,893 bytes, informational under D-023.
- Physical TV/remote/OEM overscan, high-font-scale and daylight/glare acceptance were not performed. No APK install, ADB, merge, rebase, cherry-pick, force-push or integration/main change.
- **Resume at G3C — Profile, Avatar & Child Mode.** Do not reopen G1/G2/G3A/G3B without new defect evidence. See UI_REFRESH_G3B_CHECKPOINT_2026-11-07.md.


## Current resume checkpoint — G3C P1 complete (7 November 2026)

- G3C P1 Security Policy & Authorization: **COMPLETE / SOURCE-HOST QC PASS**. Canonical checkpoint: `UI_REFRESH_G3C_P1_CHECKPOINT_2026-11-07.md`.
- Source branch `feature/g3c-profile-avatar-child-mode` exact HEAD `16577e0210470762423ed0bf6cc7624c33537850`; PR #68 OPEN/DRAFT/UNMERGED, base G3B exact HEAD `8cfc5c886950cc9d27d4530ba901df872b8e5c44`.
- Exact-head CI run #1307 / `42338164003`: both required jobs SUCCESS; release build, release unit tests and installer unit tests passed. APK diagnostic 1,601,221 bytes, informational under D-023; no artifact published.
- Central PIN gate covers selected launcher-controlled Android Settings main, Accessibility, Notification Access and Default Home/Launcher routes. Wi-Fi, Display, Dream/Screensaver and ordinary Quick Settings remain exempt. Existing PIN mechanism unchanged; launcher PIN is not system-wide parental control.
- No merge/rebase/cherry-pick/force-push, APK installation, ADB or physical device test. Physical acceptance remains deferred.
- **Resume at G3C P2 — Profile Selector & Navigation.** First perform read-only source audit against approved G1 V6 V03 and G2 focus/dialog foundations; map active-vs-focused profile, profile cards/count, add-profile and Back/navigation behavior. No code until P2 scope is reviewed. P3–P5 NOT STARTED.


## Current resume checkpoint — UI Refresh G3C complete (7 November 2026)

- **G3C Profile, Avatar & Child Mode — P1–P5 COMPLETE / SOURCE-HOST QC PASS.** Canonical evidence: `UI_REFRESH_G3C_CHECKPOINT_2026-11-07.md`.
- Owner branch `feature/g3c-profile-avatar-child-mode` exact HEAD `8cf81f84bd62d0b3da31532116553cfa21a5df98`; PR #68 OPEN / DRAFT / UNMERGED, base G3B exact HEAD `8cfc5c886950cc9d27d4530ba901df872b8e5c44`.
- P1 security policy remains closed and unchanged. P2 aligns the selector with G1 V6 V03 and G2 focus/dialog foundations: active profile is labelled separately from focus, and initial focus returns to the active profile.
- P3 preserves profile-scoped built-in/custom avatar and QR photo upload; QR commit requires a live, unexpired session. Separate GIPHY PR #62 was not pulled into this scope.
- P4 keeps Child Mode, App Lock and Hidden Apps profile-scoped. Picker changes are staged: Done commits once; Back/dismiss cancels without writing.
- P5 makes profile and policy writes durable; active-profile deletion/fallback is atomic; legacy settings migrate before deletion; duplicate/missing IDs are repaired and persisted; malformed profile JSON is backed up before fallback recovery.
- Exact-head CI #1313 / `42340859666` and #1314 / `42340866781`: both required jobs SUCCESS. `testReleaseUnitTest assembleRelease` completed BUILD SUCCESSFUL. APK diagnostic: 1,602,361 bytes; historical 1.5 MiB warning is informational/non-blocking under D-023.
- No merge/rebase/cherry-pick/force-push, integration/main mutation, APK publication/installation, ADB, device configuration or physical TV/remote/OEM test. Physical acceptance remains deferred, not waived. Existing PIN default `0000` and plain-text SharedPreferences remain residual security risks; PIN control is launcher-scoped.
- **Next resume: G3D — Quick Settings.** Do not reopen G3C or earlier completed work without new verified defect evidence.


## Current resume checkpoint — UI Refresh G3D complete (7 November 2026)

- **Phase:** G3D Quick Settings COMPLETE / SOURCE-HOST QC PASS. Governing plan GTVH-UIR-002 v2.0.
- **Owner branch/head:** `feature/g3d-quick-settings` @ `bbc7b240d2ad7bfc9d22dbaf3437f03b5953bcf8`; PR #69 OPEN/DRAFT/UNMERGED, base G3C `8cf81f84bd62d0b3da31532116553cfa21a5df98`. G3C PR #68 and branch are unchanged.
- **Verified:** V04 two-column modal; G2 shared styling/focus; all action dispatches; D-pad graph and Back; Wi-Fi return/fallback anchor/network summary; cache Activity lifecycle guard; Indonesian/English/default resource parity.
- **Exact CI:** #1328 / `42344123725`, both required jobs SUCCESS. Launcher/installer unit tests and release build PASS; Lists/tools/docs 120 tests PASS. APK 1,608,373 bytes (+6,012 / 0.38% vs G3C), informational under D-023. No artifact published.
- **Audit/checkpoint:** `UI_REFRESH_G3D_AUDIT_2026-11-07.md` and `UI_REFRESH_G3D_CHECKPOINT_2026-11-07.md`.
- **Remaining:** physical Android TV/Mi TV Stick, remote D-pad/Back, OEM Wi-Fi/power/cache behavior, 1080p/4K, daylight/large-text and overscan acceptance remain DEFERRED / NOT RUN. No APK installation, ADB or device mutation.
- **Next single action:** begin G3E Wi-Fi with read-only audit against G1 V6 V05, E01 network/system-service owners and the G3D nested-entry/return contract. Do not reopen G3D absent new defect evidence.
- Official Project G03 physical acceptance remains separately OPEN/BLOCKED pending explicit readiness approval.


## Current resume checkpoint — UI Refresh G3E complete (7 November 2026)

- **G3E Wi-Fi: COMPLETE / SOURCE-HOST QC PASS.** Canonical checkpoint: `UI_REFRESH_G3E_CHECKPOINT_2026-11-07.md`; audit: `UI_REFRESH_G3E_AUDIT_2026-11-07.md`.
- Owner branch `feature/g3e-wifi` exact HEAD `9125043e24b8f13232e41193ba5fe10e1821f7cd`; PR #70 OPEN / DRAFT / UNMERGED, based on G3D exact HEAD `bbc7b240d2ad7bfc9d22dbaf3437f03b5953bcf8`. G3D PR #69 remains unchanged.
- V05 Wi-Fi status/list/rescan/system-settings surface implemented. Android owns authentication, saved networks, connection/disconnection and radio control. MainActivity's existing network-state owner is reused; no duplicate network callback.
- Fine-location permission, Location-service prerequisite, scan throttling/timeout/empty/error states, receiver lifecycle, D-pad/Back, row focus restoration and parent return after Android Settings are covered. Default/ID/EN resources are present.
- Exact-head CI #1363 / `42346491017`: both required jobs SUCCESS; 127 Python tests; app/installer `testReleaseUnitTest` and `assembleRelease` PASS. APK diagnostic 1,617,373 bytes (+9,000 / ~0.56% vs G3D), informational under D-023; no artifact published.
- `WifiManager.startScan()` deprecation/throttling is a known Android platform risk; active scanning is best-effort and Android Settings remains fallback.
- Physical Android TV/Mi TV Stick, remote D-pad/Back, real permission/Location prompts, scan results/throttling, OEM Settings handoff, connection/authentication/disconnection and visual acceptance remain PENDING / NOT RUN. No APK install, ADB, device mutation, merge or integration/main change.
- **Next resume: G3F — Global Settings.** Start with read-only audit against its approved G1 V6 design, G1/G2 shared dialog/focus contracts and actual current E01 source. Do not reopen G3E without reproducible defect evidence.


## Current resume checkpoint — UI Refresh G3F complete (7 November 2026)

- **Phase:** G3F Global Settings COMPLETE / SOURCE-HOST QC PASS. Canonical evidence: `UI_REFRESH_G3F_CHECKPOINT_2026-11-07.md`; audit: `UI_REFRESH_G3F_AUDIT_2026-11-07.md`.
- **Owner branch/head:** `feature/g3f-global-settings` @ `455b619c3eefe417ac0152a51f3cb12478a8f4a9`; PR #71 OPEN/DRAFT/UNMERGED, base G3E `9125043e24b8f13232e41193ba5fe10e1821f7cd`. PR #70 remains OPEN/DRAFT/UNMERGED and unchanged.
- **Verified:** V06 header/search/status/split-pane; seven categories; profile summary; localized control search and target focus; focus migration/restoration; MainActivity/E01 network owner; lifecycle refresh/cleanup; default/ID/EN resource parity.
- **Exact-head CI:** #1376 / `42348837445`, both required jobs SUCCESS; 134 Python tests; app/installer unit tests, release builds and lint-vital PASS. APK diagnostic 1,620,193 bytes (+2,820 / ~0.17% vs G3E); informational only under D-023.
- **Remaining:** physical Android TV/Mi TV Stick, remote D-pad/Back, OEM Settings handoff, overscan, 1080p/4K, daylight/glare and large-text acceptance remain PENDING / NOT RUN. No APK install/publication, ADB or device mutation.
- **Next single action:** prepare G4 integration/reconciliation scope and combined-tree audit only after explicit approval; do not merge, retarget or mutate the stacked PR chain.


## Current resume checkpoint — UI Refresh G4 complete (7 November 2026)

- **G4-A–G4-J COMPLETE / SOURCE-HOST QC PASS.** Canonical audit: `UI_REFRESH_G4_AUDIT_2026-11-07.md`; checkpoint: `UI_REFRESH_G4_CHECKPOINT_2026-11-07.md`.
- Final G4 owner branch `feature/g4-parent-pin-hardening` @ `f46adf27bbc6b2156c7e9bb49d48cf4fd0fcdb5e`; PR #72 OPEN/DRAFT/UNMERGED, based on exact G3F HEAD `455b619c3eefe417ac0152a51f3cb12478a8f4a9`.
- Exact synthetic merge candidate `376b5b69e2aed56e1f813169f35dbb9459ada554`; exact combined-tree CI #1394 / `42353368760`, both jobs SUCCESS. Android app/installer unit tests, release builds and lint-vital PASS; 134 Python tests / 1 intentional skip.
- Parent PIN hardening complete: salted PBKDF2-HMAC-SHA256, legacy migration, default `0000` preserved, corrupt/lost credential fail-closed, no retry restrictions. Parent PIN remains device-wide; Child Mode/App Lock/Hidden Apps remain profile-scoped.
- PR #66–#72 remain OPEN/DRAFT/UNMERGED. No merge/rebase/cherry-pick/force-push, base retarget, integration/main mutation, APK publication/installation, ADB or device configuration.
- Release APK diagnostic 1,622,401 bytes; informational under D-023, no artifact.
- Four-digit PIN under unlimited attempts remains guessable; do not claim brute-force immunity.
- **Next separate gate:** physical Android TV/Mi TV Stick/remote/OEM acceptance, only when explicitly authorized. It is PENDING / NOT RUN; source-host completion does not imply device verification.


## Current resume checkpoint — Image 2 Home layout recovery (7 November 2026)

- **Build delivered / CI PASS.** Canonical checkpoint: `UI_REFRESH_IMAGE2_HOME_LAYOUT_CHECKPOINT_2026-11-07.md`.
- Owner branch `fix/home-layout-image2-dock-geometry` @ `31886651d7f275155f8a526302e252dc04687e4f`; source implementation commit `e8d403142043d27bbfe4c5b4c7cb2b67eb058eaa`. PR #73 OPEN/DRAFT/UNMERGED, base G4 branch exact SHA `7146de58953325fdfc6c98501828676f143e64cf`.
- Corrected Home information-shelf anchoring so vertical Dock focus movement does not move wallpaper description, Ayah and prayer-schedule shelves. Existing horizontal behavior and Dock controls are retained.
- Exact-head CI #1397 / `42357686726`: both required jobs SUCCESS. APK artifact ID `13567869928`, ZIP SHA-256 `2c9b6a3ee8bbfc74971ca8734354fc819dfcdcd97a95499786998856f28c1298`; extracted APK 1,622,565 bytes, SHA-256 `cd2b301f5fb48ffdb6b9cafd74927c5da6515a4ab9aebf6dc47e6db4ddccb4ac`; APK archive integrity PASS. Artifact expires 14 Nov 2026.
- This is a CI test APK, not a production-signing claim. Physical TV visual/remote acceptance remains PENDING / NOT RUN; no APK installation, ADB, device mutation, merge or retarget was performed.
- **Next:** controlled Image 2 acceptance on TV, including wallpaper focal position, Ayah/prayer area, Dock capacities 4/5/6, horizontal/vertical orientation, D-pad focus and 1080p geometry. Fix only reproducible scoped defects.


## Current resume checkpoint — Image 2 bottom Dock correction (7 November 2026)

- User TV photo exposed that the previous APK did not fix the primary defect: horizontal Dock remained too high. Root cause was legacy `rows_below_dock` positioning plus App Grid padding toward the bottom. Previous APK is superseded.
- **Corrective source/build: COMPLETE / exact-head CI PASS.** Evidence: `UI_REFRESH_IMAGE2_BOTTOM_DOCK_CORRECTION_2026-11-07.md`.
- Branch `fix/home-layout-image2-dock-geometry` @ `c3d749db7373052019063f5057ac08f0eda797b0`; PR #73 OPEN/DRAFT/UNMERGED, base G4 exact SHA `7146de58953325fdfc6c98501828676f143e64cf`.
- Horizontal Dock now anchored to bottom edge with zero bottom margin. App Grid viewport above it uses a capacity-independent reserve based on capacity 4; original XML top inset restored. DPAD navigation now moves Dock-Up to adjacent Grid row and Grid-Down from last row to Dock. Vertical side rail retained.
- Exact-head CI #1404 / `42358639396`: both required jobs SUCCESS. Artifact #`13568806731`; ZIP SHA-256 `a63aaf7577d15ad2cb2e7a1b8f1461b35027f9531a5e15e7ae492d9ef055e847`; APK size 1,622,545 bytes; APK SHA-256 `3b01c1126043c2e8025e1852d7de5d09812b091986af21e9c1c607188cb926ab`; ZIP integrity PASS. Artifact expires 14 Nov 2026.
- Physical TV visual/remote acceptance remains PENDING / NOT RUN. No install, ADB, device mutation or merge.
- **Next:** user acceptance-test this replacement APK and provide a new photo; verify Dock at bottom, 4/5/6 capacity, Grid viewport, DPAD and vertical orientation before claiming visual PASS.


## Active starting point reset — Glass TV Home 4.0.0 (7 November 2026)

- User directed a full return to application 4.0.0. Verified source baseline: `feature/g4-parent-pin-hardening` @ `7146de58953325fdfc6c98501828676f143e64cf`; clean work branch `reset/baseline-4.0.0` created at that exact SHA.
- Exact baseline CI #1395 / `42355197900` PASS. APK package `com.gathin.tvhome`, versionName 4.0.0, versionCode 40, size 1,622,401 bytes, SHA-256 `ec125ca46bf98af8426fef060e974c963932f9ae7f863507d135ae40df9bb605`.
- PR #73 (failed UI/Dock rework) CLOSED, not merged. Its branch/commits preserved. PR #72 remains OPEN/DRAFT/UNMERGED and corresponds to baseline branch.
- No branch deletion, force-push, merge, TV install, ADB or device setting changes. Baseline APK is extracted at `/mnt/data/glass-tv-home-4.0.0-baseline/app-release.apk` in the active session.
- Canonical report: `RESET_BASELINE_4.0.0_2026-11-07.md`. Do not resume new work until user specifies next step.


## Home Dock visual parity V2 — 7 November 2026

- User resumed Home Screen only and reaffirmed the supplied reference screenshot as the locked visual master. Scope is Dock/icon position and glass parity; wallpaper, hero description, ayah/prayer shelf and header remain untouched.
- Source audit: Patch 4.0/4.1 MainActivity identical; current G3A item_app XML matches 4.0. Legacy 180×101dp tile contract and 6/12dp offsets, 14dp tray inset, +18dp Dock height and +34dp Grid reserve are captured in DockGeometry.
- Branch fix/home-dock-visual-parity-v2 @ 4ba1d7f14ba26e0c4051fcdf958f289234b6e7ae; PR #74 OPEN/DRAFT/UNMERGED, base G4 7146de58953325fdfc6c98501828676f143e64cf.
- Exact-head CI #1407 / 42378227691: both required jobs SUCCESS; app/installer release unit tests and builds passed; repository contract checks passed. APK 1,622,573 bytes; SHA-256 19d1435c7c20491b9093747d65c8dbab6a0627971461bee401702990538a142c; artifact #13576552604.
- Source-host QC PASS; controlled TV visual/remote/overscan acceptance READY but NOT RUN. No install, ADB, device mutation or merge. Full checklist: HOME_DOCK_VISUAL_PARITY_V2_2026-11-07.md.


## Patch 4.1 Dock & Liquid Glass adoption — 7 November 2026

- Device test photo rejected prior V2: all apps remained in two Grid rows, Dock/tray/focus visuals absent.
- Root cause: Patch 4.1 fills the first Dock row from configured favourites followed by ordered inventory; V2's separate Dock adapter used only configured/user-pinned keys and could collapse when none were available. Restored fill-to-capacity semantics with explicit unpins respected.
- Adopted Patch 4.1 GlassRim iridescent gradient + Window Light, GlassView hardware-layer rim, DockBreathingBorderView and item_app layer. Wired breathing rim to Dock holders only; Window Light restored to DockDecoration, GlassDrawable and Glider.
- Branch fix/home-dock-visual-parity-v2 @ 239390db27fafcc39b2fefa68ddfa9f0ba30608f; PR #74 OPEN/DRAFT/UNMERGED.
- Exact-head CI #1415 / 42379388302 both jobs SUCCESS. APK 1,624,741 bytes; SHA-256 23ffb28d48b05f975be4b5bd13aa1086c08e47eef1feb5b8e13916799f32fafc; artifact #13576403262.
- SOURCE-HOST QC PASS / READY FOR CONTROLLED TV ACCEPTANCE. New build not yet physically tested. No install, ADB, device mutation or merge. Evidence: PATCH41_DOCK_GLASS_ADOPTION_2026-11-07.md.


## Patch 4.1 Dock & Liquid Glass adoption — final source-host checkpoint (7 November 2026)

- Device photo rejected prior V2: Dock tray and focus visuals absent; source audit found Dock could collapse because the split adapter only used pinned keys. Restored Patch 4.1 first-row fill from preferred pins then ordered inventory; explicit unpins respected.
- Adopted Patch 4.1 GlassRim iridescent gradient/Window Light, GlassView, DockBreathingBorderView and item_app layer. Window Light restored to DockDecoration, GlassDrawable and Glider.
- Restored Patch 4.1 horizontal Dock bottom safe offset of 16dp; Grid reserve includes the same gap.
- Branch fix/home-dock-visual-parity-v2 @ 2d1f744a4b77406712e7868a7e7c6747a3bb88ed; PR #74 OPEN/DRAFT/UNMERGED.
- Exact-head CI #1416 / 42379710925 both jobs SUCCESS. APK 1,624,733 bytes; SHA-256 5da75cfd30956224bb9bf7c61ced34a11068356d83227cdb28d2fd9fb6d00684; artifact #13577033231.
- SOURCE-HOST QC PASS / READY FOR CONTROLLED TV ACCEPTANCE. New device test pending; no install, ADB, device mutation or merge. Evidence: PATCH41_DOCK_GLASS_ADOPTION_2026-11-07.md.


## Patch 4.1 overflow-app recovery — 7 November 2026

- Re-audit confirmed PR #74 made non-Dock apps unreachable in horizontal mode: Grid was hidden and DPAD Down from Dock was consumed. This explains the user's report that remaining apps disappeared.
- Restored an on-demand overflow path: horizontal Grid remains hidden at idle; DPAD Down reveals it and focuses the aligned app; DPAD Up from first Grid row returns to Dock; DPAD Up from Dock returns to top shelf. Returning focus to Dock collapses Grid. Stable-key focus restoration now supports horizontal overflow apps.
- Source branch fix/home-dock-visual-parity-v2 @ 48f07a983ebee4370bbf13a80f85edd947c2eb31; PR #74 OPEN/DRAFT/UNMERGED.
- Added horizontal Dock/Grid navigation mapping unit test. Exact-head CI run 42382202545 was IN PROGRESS at this checkpoint; no PASS claim. Physical TV acceptance NOT RUN. No TV install, ADB, device mutation or merge.
- Detailed audit and acceptance gates: HOME_DOCK_PATCH41_RECOVERY_2026-11-07.md.


## Patch 4.1 overflow-app recovery — validation update (7 November 2026)

- Exact source head 48f07a983ebee4370bbf13a80f85edd947c2eb31 CI run #42382202545 SUCCESS; both required jobs passed, including unit tests and release build.
- Release APK 1,624,937 bytes (+204 bytes versus prior build); non-blocking historical 1.5 MiB diagnostic warning.
- Test artifact #13578301246; ZIP 1,424,751 bytes; SHA-256 5c9ea538f64743a0b31c91b14add41b47f091c3d57110d2f0bce83c86e537922; expires 14 Nov 2026.
- Physical Mi TV Stick visual/remote acceptance remains NOT RUN. PR #74 OPEN/DRAFT/UNMERGED; no install, ADB, device mutation or merge.


## Horizontal Dock centering correction — 7 November 2026

- Owner's Mi TV Stick photo showed five Dock icons grouped left of screen center; prior source changes corrected holder width but did not center the group.
- Confirmed cause: horizontal Dock RecyclerView spans the viewport and its LinearLayoutManager starts children at the left edge. Per-item width alone cannot center the group.
- Corrected DockGeometry with horizontalCenterOffsetPx(innerWidth, tileWidth, itemCount) and applied the resulting X translation to the horizontal Dock host. Offset is based on actual visible item count and slot width; full viewport remains the width-measurement basis. Translation resets before vertical/empty layouts.
- Added regression tests for capacities 4/5/6 at densities 1.0, 1.5, 1.75 and 2.0, plus empty/oversized groups.
- Source branch fix/home-dock-visual-parity-v2 @ b14a7bc3d1e99cf8cf0c74e944c39983d776f341; PR #74 OPEN/DRAFT/UNMERGED. PR description now records the source finding and new acceptance gates.
- CI run #42384023835 failed because the first regression assertion incorrectly required exact pixel parity under integer rounding. The assertion was corrected to allow a one-pixel left/right margin difference. Latest-head CI run #42384207367 is IN PROGRESS; APK pending.
- Physical TV visual/remote acceptance NOT RUN. No install, ADB, device mutation or merge. The next APK must be built from the latest centered Dock source and then checked on the Mi TV Stick.

- Final latest-head CI #1424 / run 42384207367 SUCCESS; both required jobs passed, including 296+ unit tests and release build, repo contract checks, APK diagnostics and artifact publication.
- Release APK 1,624,997 bytes; SHA-256 8e3183addc092e5bce1b41b09d939fc64682d6b16f595a0e4e1caa0a5f982664. Artifact #13578283307; ZIP 1,424,983 bytes; SHA-256 72014680fd7e7b02cf36ed5029d55be465ac3394d649fc95ee718af05e234d17; expires 14 Nov 2026.
- APK extracted and verified as Android package. Physical TV visual/remote acceptance still PENDING / NOT RUN; PR #74 OPEN/DRAFT/UNMERGED. No install, ADB, device mutation or merge.


## Home Dock P2 safe-geometry and bottom-anchor correction — 8 November 2026

- Applied to source branch `fix/home-dock-visual-parity-v2`; PR #74 remains OPEN / DRAFT / UNMERGED.
- Source head: `dd575a13002ba613af69e59382344180f54f2763`.
- Corrected the horizontal Dock to use the P2 48dp left/right safe content width and 27dp bottom safe margin. Tray padding remains symmetric at 14dp. Dock slots and Grid cells share the same derived tile width and cell spacing at capacities 4 / 5 / 6.
- Anchored the horizontal overflow Grid to the Dock tray: Grid bottom margin is derived from Dock host height, bottom safe offset, item top offset, tray inset and the 8dp Grid-to-tray gap. Grid uses bottom-stacked rows while idle remains hidden.
- Reworked horizontal overflow focus mapping so DPAD Down from Dock targets the bottom-most Dock-adjacent Grid row, and DPAD Up from that row returns to Dock. Remaining rows stay in the scrollable Grid.
- Aligned the left/right shelf baselines through the shared Dock tile-top anchor; shelf bottom is 36dp above tray top (14dp tray inset + 36dp shelf gap). Side margins changed to the P2 48dp safe inset.
- Added geometry unit coverage for left/right symmetry, densities 1.0 / 1.5 / 1.75 / 2.0, capacities 4 / 5 / 6, and viewport widths 1280 / 1920.
- Validation: CI run #1433 caught a duplicate `horizontalCenterOffsetPx(int,int,int)` declaration on an earlier head; removed in commit `6422c37a18cefbb26487d04cf948c52ba9968bdc`. CI run #1437 completed SUCCESS on exact source head `dd575a13002ba613af69e59382344180f54f2763`: `./gradlew --no-daemon testReleaseUnitTest assembleRelease` completed with `BUILD SUCCESSFUL` (1m 34s). Release APK size is 1,625,553 bytes; the workflow emitted a non-blocking warning because it exceeds the historical 1.5 MiB reference. Test APK artifact ID: `13583800658` (expires 14 Nov 2026). The local command was not run because the execution environment could not resolve GitHub to retrieve the repository.
- Physical TV acceptance: NOT RUN. No ADB, device interaction, APK installation or merge performed.
- Source validation: CI PASS on exact head. Physical TV acceptance: BELUM DIUJI DI TV. Remaining: owner TV checklist for tray symmetry, idle wallpaper clearance, DPAD Down/Up, and both shelf/tray collision clearance; do not merge until that acceptance is complete.


## PR #74 APK sideload signature cross-check — 8 November 2026

- Audited the actual GitHub Actions artifact ZIPs from runs #1436 (artifact 13583024862) and #1437 (artifact 13583800658), not just CI summaries.
- Run #1437 artifact contains exactly `app-release.apk` (1,625,553 bytes); ZIP integrity is valid. APK package is `com.gathin.tvhome`, versionCode 40 / versionName 4.0.0, minSdk 26, targetSdk 34, one DEX, no native libraries, and APK Signature Scheme v2 block is present.
- Device baseline previously recorded for Mi TV Stick is Android 11 / API 30, which satisfies minSdk 26; no ABI-specific native library requirement was found.
- Critical signing finding: both CI artifacts are signed with `CN=Android Debug`, but signer SHA-256 fingerprints differ: run #1436 `985107516947810672cc2143fb617cadca84297797e466b0c50fd33a0db64571`; run #1437 `3113f7823b649334fe2dc5347804d88ad60cb6f234422019fcfb8c6bd26671bc`. This confirms the CI-generated debug signing identity is not stable between runs.
- Likely failure mode if the user is installing over an existing `com.gathin.tvhome`: Android rejects the update due to signing-certificate mismatch (`INSTALL_FAILED_UPDATE_INCOMPATIBLE` / “App not installed”). This is not yet confirmed as the user's actual symptom; do not uninstall the existing launcher or erase app data without confirmation.
- No source-code or workflow signing changes made pending the exact TV error. The APK's ZIP structure and declared Android compatibility were checked, but physical installation/launch on TV remains NOT TESTED.
- Next diagnostic required: distinguish installer failure/update-signature conflict from successful installation followed by launch crash. Ask for the exact TV message or a photo of the screen. For a durable CI sideload APK, configure a stable owner-controlled signing keystore via protected GitHub Actions secrets; do not commit a private keystore into the repository.

## Mi TV Stick cold-start crash diagnosis and source fix — 8 November 2026

- Owner supplied on-device Android-shell `logcat` from Mi TV Stick (Android 11 / API 30). Runtime confirmed `FATAL EXCEPTION: main` during `MainActivity.onResume()`.
- Exact exception: `UnsupportedOperationException: GridLayoutManager does not support stack from end. Consider using reverse layout`.
- Stack: `MainActivity.applyDockLayout()` → `reloadTiles()` → `reloadApps()` → `onResume()`. This proves the direct launcher-start crash; it is not a speculative performance diagnosis.
- Corrected `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java` by removing the unsupported `GridLayoutManager.setStackFromEnd(...)` call. Retained GridLayoutManager's natural adapter order; horizontal overflow remains governed by FrameLayout geometry and DPAD-driven visibility/focus.
- Fix commit `61d4e429be129263533ecfa75a729bbe0a15833f` on `fix/home-dock-visual-parity-v2`; PR #74 remains OPEN / DRAFT / UNMERGED.
- Exact-head CI run `42398810251` was observed IN_PROGRESS at checkpoint. Previous CI success on older head does not validate this fix.
- Physical TV re-test is NOT YET RUN. Do not install/declare fixed until exact-head CI passes and owner tests cold start + resume on Mi TV Stick. Do not change default launcher, uninstall, clear data, or merge.


## HOME-STARTUP-CRASH-001 — latest build artifact verified (8 November 2026)

- Source branch/head: `fix/home-dock-visual-parity-v2` @ `61d4e429be129263533ecfa75a729bbe0a15833f`; PR #74 remains OPEN/DRAFT/UNMERGED.
- Exact-head CI run #1438 / `42398810251`: COMPLETED / SUCCESS. Both required jobs passed: Lists, tools and docs; Launcher build and unit tests. Release unit tests and `assembleRelease` succeeded.
- Latest exact-head workflow artifact #13584279494 downloaded and inspected. ZIP: 1,425,448 bytes; contains `app-release.apk` of 1,625,545 bytes. APK SHA-256: `0f65ef8b7a97b334aeaff5b0493cbaa73400531f3b5d1618ce24c1b06d66a385`. File identifies as an Android package with APK Signing Block.
- Artifact is the latest successful build available for this exact source HEAD; it was downloaded and extracted to provide the APK. No new workflow_dispatch was executed; PR-branch workflow has no manual dispatch trigger.
- Source/host QC: PASS. Physical Mi TV Stick cold start, resume, DPAD, Dock/Grid visibility, orientation and OEM visual acceptance: PENDING / NOT RUN. This artifact is a test candidate, not a final release. Do not merge PR #74 or change default launcher until physical acceptance and remaining audits pass.


## HOME-DOCK-ALIGNMENT-ROW-SCROLL-001 — owner video follow-up (8 November 2026)

**User evidence:** owner-provided Mi TV video shows the horizontal Dock tray visibly narrower than the Home information shelves. After DPAD Down, a separate app row appears floating in the middle while the Dock remains fixed below it; this is not the requested replacement/scroll behavior.

**Source findings and changes**
- The Dock host was already nested inside `mainContent` with its own 56dp horizontal content inset, but `DockGeometry.horizontalContentWidthPx` subtracted another 48dp on both sides. Removed this duplicate inset.
- The Islamic shelf uses a 48dp end margin while the left Home content starts at 56dp. Extended the horizontal Dock host 8dp to the right, anchored its left edge to the Home content start, and expanded the horizontal glass tray to the full Dock host width. Dock icons remain centered in the tray.
- Horizontal overflow Grid previously filled the entire available vertical space and DPAD Down selected the last Grid row adjacent to the Dock. Changed it to reveal the first overflow row directly above the Dock and limit the visible Grid viewport to one app row. DPAD Down advances by one Grid row and scrolls it into the same viewport; DPAD Up returns to the previous row and then the matching Dock slot.
- Geometry is recalculated on Grid expand/collapse. Added tests for full-width content geometry and one-row viewport sizing.

**Implementation evidence**
- Branch `fix/home-dock-alignment-row-scroll`; Draft PR #75 stacked on PR #74 branch `fix/home-dock-visual-parity-v2`.
- Current source HEAD `260bb1cf14a284f6f67a21005a056eb053942516`.
- Changed: `DockGeometry.java`, `MainActivity.java`, `DockDecoration.java`, `DockLayoutModelTest.java`.
- Exact-head CI run #1439 / `42400927777`: repository tests PASS; Android launcher unit tests/release build still IN PROGRESS at checkpoint. No APK artifact or build PASS claimed.
- PR #75 remains OPEN/DRAFT/UNMERGED. Physical Mi TV visual/remote acceptance NOT RUN. Do not merge or change default launcher until exact-head CI and physical acceptance pass.


### CI/build follow-up — 8 November 2026

- Latest PR #75 head: `c72c07f9af1ee83b43ff19b34f2e704bdc570eea` (includes initial Grid-focus retry and CI artifact publication for PR #75).
- CI run #1441 / run ID `42401401988`: both jobs SUCCESS. Release unit tests, `assembleRelease`, APK size diagnostics and artifact publication all SUCCESS.
- Artifact: `glass-tv-home-dock-visual-parity-78b6510fd8f6a4c70d4bb93792abf53f9d5ee542`, artifact ID `13585890269`, ZIP 1,422,728 bytes. Extracted `app-release.apk`: 1,622,469 bytes; SHA-256 `61d5705915802e65fdecab6c7b6b91973b7187c4a276a972704d6ab1bbd242b8`.
- CI/build gate: PASS. Device/remote validation of horizontal edge alignment, single-row viewport, DPAD Up/Down focus continuity and end-of-list behavior: PENDING. PR #75 remains OPEN/DRAFT/UNMERGED; no default-launcher change.


## Active checkpoint — GTVH-HOME-LAYOUT-FIX-001 (8 November 2026)

- Owner supplied 23.14s, 1920×1080 physical-TV video. Pre-fix evidence shows Google Play isolated at the left of the lower glass tray and a separate five-app row above it.
- PR #74/#75 already contain the Dock inventory fill, duplicate-inset removal, full-width tray and one-row overflow/DPAD row progression. PR #75 exact-head CI #1441 passed; APK 1,622,469 bytes, SHA-256 `61d5705915802e65fdecab6c7b6b91973b7187c4a276a972704d6ab1bbd242b8`. This is a PR #75 baseline only.
- New source finding: `MainActivity.alignTopButtons()` translated the entire top menu against the last Dock card and was called by Grid and Dock global-layout listeners. This coupled Wi-Fi/microphone/profile/Settings placement to Dock count and could shift controls left.
- Minimal source fix: remove the two listeners and Dock-relative translation; preserve XML `topButtons` end alignment inside `mainContent` with 56dp start/end padding.
- PR #76: https://github.com/Frangga11/glass-tv-home/pull/76; branch `fix/home-global-layout-bounds`; current head `cf9d661dc7e9d0520608dc771aed2b9d75bb1a01`; OPEN/DRAFT/UNMERGED; base PR #75 exact head `c72c07f9af1ee83b43ff19b34f2e704bdc570eea`.
- Added `tests/test_home_layout_bounds_contract.py`. CI #1442 exposed an ElementTree XPath lookup failure in the new test; corrected to namespaced-ID iteration. Exact-head CI #1443 / `42403684594` was IN PROGRESS at checkpoint. No PR #76 APK or checksum available yet.
- Physical Mi TV Stick install/remote/visual acceptance NOT RUN. No ADB, launcher-default change, device mutation or merge. Do not mark visual PASS from source/CI alone.
- Detailed report: `docs/project-management/HOME_LAYOUT_FIX_2026-11-08.md`.

### PR #76 CI and APK publication follow-up — 8 November 2026

- Exact-head source/test commit `cf9d661dc7e9d0520608dc771aed2b9d75bb1a01`: CI run #1443 / `42403684594` SUCCESS, both required jobs. Python suite: 137 tests, 1 intentional skip; Android unit tests and release build passed.
- Run #1443 produced no artifact because the existing upload step was scoped to PR #74/#75. Updated `.github/workflows/ci.yml` to include PR #76 and publish artifact `glass-tv-home-layout-fix-${{ github.sha }}`.
- Latest PR #76 head is now `ea681545bbf356105683ef2c7da6664e29a83723` (workflow-only addition on top of the passing source/test head). Exact-head CI run #1447 / `42403880493` is in progress to rebuild and publish the APK.
- Until run #1447 succeeds and the artifact is verified, PR #76 APK size/SHA-256 remain PENDING. PR #76 remains Draft/unmerged; physical TV acceptance remains NOT RUN.

## GTVH-HOME-LAYOUT-FIX-001 — Dock centering and CI follow-up (8 November 2026)

- Source audit found the prior Dock centering offset translated the entire dockRecycler, which also moved the full-width tray drawn by DockDecoration.
- PR #75 now centers icons using symmetric RecyclerView padding while keeping the Dock host translation at zero. DockAdapter calculates horizontal tile/slot dimensions from the full viewport, independent of centering padding; vertical/empty layouts reset padding.
- Added horizontalDockPaddingCentersIconsWithoutMovingTheTrayHost() regression coverage in DockLayoutModelTest.java.
- PR #75 branch fix/home-dock-alignment-row-scroll, latest commit 21bc204a413fce3c527e0e8761d2016bc5dc87c6. CI #1446 / 42403799908 ran on preceding head 78539148930bc6e3613a03db0dd3997073c725ee and completed SUCCESS; exact-head CI #1448 / 42403925244 is now IN PROGRESS on 21bc204...
- Updated .github/workflows/ci.yml so PR #76 also publishes a test APK. PR #76 CI #1443 / 42403684594 passed both jobs, but its artifact step was skipped under the older condition; a new cumulative run is required after the PR #75 base update.
- PR #76 remains the dedicated Quick Toggles correction: remove Dock-relative top-menu translation/listeners and preserve XML end alignment within mainContent's 56dp safe padding.
- No current-head APK, checksum, post-fix screenshot or physical Mi TV Stick acceptance yet. The previous PR #75 artifact from c72c07f... is baseline-only.
- Verify actual Dock pin/unpin preferences on device before changing app membership: source fills the five-slot default from ordered inventory except explicitly unpinned apps.
- No merge, ADB, installation, default-launcher change or device mutation.
- Detailed report: HOME_LAYOUT_FIX_2026-11-08.md.

### Latest GTVH-HOME-LAYOUT-FIX-001 CI and APK checkpoint

- PR #75 CI #1446 / 42403799908 completed SUCCESS on source head 78539148930bc6e3613a03db0dd3997073c725ee; both required jobs passed.
- Artifact #13585808852 ZIP 1,422,660 bytes; extracted APK 1,622,497 bytes; SHA-256 38d818902ce3ccf800b8de5cc0428125b91b7e9c27ba81810759a53925c65d0f. ZIP and APK integrity checks passed.
- This is an intermediate PR #75 Dock-only APK. It does not include PR #76 Quick Toggles changes and predates the later workflow-only head 21bc204a413fce3c527e0e8761d2016bc5dc87c6. Do not treat it as the final cumulative APK.
- PR #75 latest head 21bc204... CI #1448 / 42403925244 IN PROGRESS. PR #76 latest head ea681545bbf356105683ef2c7da6664e29a83723 CI #1447 / 42403880493 IN PROGRESS; the artifact condition now includes PR #76.
- Physical Mi TV Stick acceptance and post-fix visual evidence remain NOT RUN. No merge, ADB, installation or default-launcher change.

### PR #76 base reconciliation — 8 November 2026

- PR #75 latest head advanced to `21bc204a413fce3c527e0e8761d2016bc5dc87c6` with a workflow-only change adding PR #76 to APK publication. PR #76 was rebased onto this exact latest base; duplicate workflow change removed from its own diff.
- PR #76 was reopened as Draft after the temporary branch reset; it is OPEN/DRAFT/UNMERGED. Base is PR #75 branch at `21bc204a413fce3c527e0e8761d2016bc5dc87c6`; head is `fix/home-global-layout-bounds` @ `012fba1c4012c7e4a05de292b69030c17a594d90`.
- Compare confirms 2 commits ahead / 0 behind and only two changed files: `MainActivity.java` and `tests/test_home_layout_bounds_contract.py`.
- Exact-head CI #1449 / `42404084112` is IN PROGRESS. Earlier CI #1443 passed on the pre-rebase source/test head but is not the latest exact-head gate. PR #76 APK/size/SHA-256 pending; physical TV acceptance not run.

### Exact cumulative Home layout CI and APK — 8 November 2026

- PR #75 latest head 21bc204a413fce3c527e0e8761d2016bc5dc87c6: CI #1448 / 42403925244 SUCCESS, both jobs; artifact #13586239278.
- PR #76 latest head ea681545bbf356105683ef2c7da6664e29a83723: CI #1447 / 42403880493 SUCCESS, both jobs; artifact #13586598248 (ZIP 1,422,482 bytes).
- Downloaded and verified the PR #76 cumulative APK: 1,622,153 bytes; SHA-256 12dc35e31fc2e67bcba1e0ddcee548937f5f23f9bf90d654316fc6fdc34f17c7. Outer ZIP and APK ZIP integrity passed; manifest, DEX and resources table present.
- PR #76 build includes the PR #75 Dock host/padding and one-row DPAD work plus the Quick Toggles safe-frame correction. This is a controlled test candidate, not proof of visual acceptance.
- Technical CI/build/APK gate PASS. Physical Mi TV Stick visual/remote acceptance remains PENDING; no installation, ADB, default-launcher change, device mutation or merge.

### Current PR #76 checkpoint — 8 November 2026

- Latest source head `f085c41356e2e5f53ca13f51bc0676bdb82d350a`, based on latest PR #75 head `21bc204a413fce3c527e0e8761d2016bc5dc87c6`; 3 ahead / 0 behind.
- Final reconciliation preserved PR #75's newer Dock host centering via symmetric RecyclerView padding and removed only the Dock-relative top-menu translation/listeners from MainActivity. Old Dock `translationX(dockCenterOffset)` is absent.
- PR #76 changed files: MainActivity.java and `tests/test_home_layout_bounds_contract.py`; PR #76 OPEN/DRAFT/UNMERGED.
- Exact-head CI #1450 / `42404262057`: Lists/tools/docs job SUCCESS (137 tests, 1 skip); Android unit tests/release build still IN PROGRESS. APK/size/SHA-256 pending.
- No post-fix visual evidence and no physical Mi TV Stick/remote test. Current disposition PARTIAL PASS, not visual PASS. No merge/device mutation.

### Latest PR #76 candidate and exact-head synchronization — 8 November 2026

- PR #76 CI #1449 / 42404084112 SUCCESS on head 012fba1c4012c7e4a05de292b69030c17a594d90, base PR #75 head 21bc204a413fce3c527e0e8761d2016bc5dc87c6. Both required jobs passed.
- Artifact #13586419148: ZIP 1,422,343 bytes; APK 1,622,117 bytes; SHA-256 fe7db8ca54be34781b716454256e50da62b5ee3bf168fc211aa7e45095fa95b6. ZIP/APK integrity and core APK entries verified.
- PR #76 then advanced to f085c41356e2e5f53ca13f51bc0676bdb82d350a, reapplying the same Dock host/padding correction already present in its PR #75 base. CI #1450 / 42404262057 is running on this latest branch head; do not attribute #1449 to f085c.
- The #1449 APK remains a valid test candidate for the cumulative runtime tree. Physical Mi TV Stick visual/remote acceptance remains pending; no default-launcher change, ADB, installation or merge.
- PR #75 exact-head CI #1448 / 42403925244 also passed both required jobs on head 21bc204a413fce3c527e0e8761d2016bc5dc87c6.

### Final exact-head Home layout result — 8 November 2026

- PR #76 current head f085c41356e2e5f53ca13f51bc0676bdb82d350a, base PR #75 head 21bc204a413fce3c527e0e8761d2016bc5dc87c6.
- CI #1450 / 42404262057 SUCCESS; both required jobs passed, including regression contract, Android unit tests and release build.
- Artifact #13585769799: ZIP 1,422,487 bytes; APK 1,622,153 bytes; SHA-256 90fe5131a84231d1a725c4eb02b672d94a1c6c1a07f2cda8db710bc7c8ddc135. ZIP/APK integrity passed; manifest, DEX and resources table present.
- PR #75 CI #1448 / 42403925244 also SUCCESS on head 21bc204a413fce3c527e0e8761d2016bc5dc87c6.
- Final status: PENDING DEVICE ACCEPTANCE. No post-fix physical video, remote acceptance, ADB, installation, default-launcher change or merge. All three PRs remain Draft/unmerged.

### CI test-count and APK size note

- CI #1450 Lists/tools/docs: 137 Python tests OK, 1 intentional skip. Android unit tests/release build and artifact publication succeeded.
- APK 1,622,153 bytes is 49,289 bytes above the historical 1.5 MiB reference (non-blocking CI warning), but 316 bytes smaller than the earlier PR #75 baseline APK.
- No size-budget failure was configured; track this as a non-blocking optimization item.


### GTVH-HOME-LAYOUT-FIX-001 — consolidated repair follow-up (8 Nov 2026)

- Owner's latest Mi TV Stick photo is treated as failed visual acceptance: Dock icons appear stacked/misaligned and the overflow app row is reported as floating near screen center. Previous CI is not accepted as proof of visual correctness.
- Cross-audit confirmed PR chain #74 → #75 → #76 is stacked. Rather than waiting for those PRs to merge, created consolidated Draft PR #77 directly against `feature/g4-parent-pin-hardening`; old PRs #74/#75/#76 remain OPEN/DRAFT/UNMERGED and unmodified as audit history.
- PR #77 branch `fix/home-layout-consolidated-001`, exact head `bd789b6b180b6668f6d60904283ca559633d7fb1`.
- New source corrections: Dock holder slot/card/name geometry is recalculated on bind from the latest measured host width; a post-layout width callback triggers a safe rebind; DPAD focus restoration uses generation tokens to cancel stale targets and a bounded post-scroll retry.
- Retains PR #75 one-row overflow viewport and PR #76 top-controls safe-frame correction. Added geometry test coverage across 1280/1696/1712/1920px widths and Dock capacities 4/5/6. CI workflow now publishes APK for PR #77.
- CI run #1453 / 42405457829 was in progress when this note was written. Exact-head CI result, APK size and SHA-256 must be appended only after completion; no pass is claimed yet.
- Physical Mi TV Stick visual/remote acceptance remains PENDING. No merge, installation, ADB, default-launcher change or device mutation.
- Full report: `docs/project-management/HOME_LAYOUT_FIX_2026-11-08_CROSS_AUDIT.md`.


### Exact-head correction after source review — 8 Nov 2026

- Consolidated PR #77 advanced after a follow-up code review to `3c1bd036206b48f745e9edc9ea2a69857af91d34` on `fix/home-layout-consolidated-001`.
- Bounded Grid focus retry was corrected to perform one scroll attempt only, then retry layout/focus a finite number of frames; returning to Dock increments the request generation and cancels any delayed Grid focus. This avoids an unbounded scroll/retry loop and stale focus theft.
- Exact-head CI #1455 / run 42405589002 is running at the time of this update. Lists/tools/docs job PASS; Android unit-test/release-build job still in progress. Earlier CI #1453 / run 42405457829 passed both jobs on prior head bd789b6b180b6668f6d60904283ca559633d7fb1, but is not substituted for exact-head validation.
- Current PR #77 head must be treated as the only consolidated candidate. Physical Mi TV Stick acceptance remains pending; no merge or device installation.


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


## Resume checkpoint — Home final cross-audit, 8 November 2026

- Active Home repair: PR #79 `fix/home-balanced-frame-001`, exact latest source/documentation head `ddd8750a688460cf1c8bc4b27c77189294301844`; OPEN / DRAFT / UNMERGED. Parent PR #77 remains OPEN / DRAFT / UNMERGED at `0ea3746f0304b7042a018eeebf850d759f357bf8`.
- Cross-audit found an incomplete stale-focus cancellation path: Grid reveal callbacks could re-enter focus without checking the captured generation, and Dock/header focus plus Activity pause did not invalidate pending Grid requests.
- Corrected in PR #79: generation guard on deferred Grid reveal, cancellation on Dock focus, header-pill focus, and onPause. Added a Python source-contract regression test.
- Updated source files: `MainActivity.java`, `tests/test_home_layout_bounds_contract.py`, and `HOME_LAYOUT_RECONCILIATION_2026-11-08.md` on the PR branch.
- Previous exact-head CI: PR #77 run #1506 PASS; PR #79 run #1526 PASS. These are pre-correction results. Fresh CI for `ddd8750a688460cf1c8bc4b27c77189294301844` is not yet visible; do not claim the new commits passed CI.
- Dependency audit: PR #79's base SHA `0aeb3851ddce5deb47e3c5f96db44631b0343577` is one commit behind PR #77 head; the intervening commit changes only the CI workflow, and both branch workflow contents were inspected and are identical. Reconcile ancestry before merge.
- Device visual/DPAD/overscan acceptance remains NOT RUN. No APK installed, no ADB or device settings changed. No PR merged.
- Next: obtain fresh exact-head CI for PR #79, inspect resulting APK diagnostics/artifact, complete source review, then prepare the Mi TV Stick acceptance matrix. Do not test or merge before CI and source review pass.


## Follow-up source correction — bilingual altitude, 8 November 2026

- Latest PR #79 head: `1fff5b7b8eea4ee53198fe3e65f6c75c91340be3`; OPEN / DRAFT / UNMERGED.
- Stale-focus correction committed at prior head `ddd8750a688460cf1c8bc4b27c77189294301844`; exact-head CI #1529 passed both jobs.
- Additional audit found the altitude pill hard-coded Indonesian and used an uncertainty symbol. Added `location_altitude_format` in default/ID/EN resources; Indonesian/default `%1$d mdpl`, English `%1$d m ASL`; updated MainActivity and added resource regression coverage.
- Fresh CI #1534 for latest head is IN PROGRESS; do not claim final PASS until complete.
- PR #77/#79 remain Draft and unmerged; physical TV acceptance remains pending. No APK installed or device settings changed.


## Resume checkpoint — GTVH-HOME-FINAL-CROSS-AUDIT-001 CI and build review (8 November 2026)

- PR #79 `fix/home-balanced-frame-001` latest exact HEAD: `1fff5b7b8eea4ee53198fe3e65f6c75c91340be3`; OPEN / DRAFT / UNMERGED; GitHub reports mergeable=true. PR #77 `fix/home-layout-consolidated-001` HEAD: `0ea3746f0304b7042a018eeebf850d759f357bf8`; OPEN / DRAFT / UNMERGED; mergeable=true.
- Exact-head PR #79 CI #1534 / run `42477517935` completed SUCCESS. Both jobs passed: Lists/tools/docs and Launcher build/unit tests. Release build, `testReleaseUnitTest`, APK size diagnostics and test-APK publication all completed successfully.
- PR #77 exact-head CI #1506 / run `42462553724` also completed SUCCESS.
- Release APK measured `1,624,589 bytes` (about 1,586 KiB), above the historical 1.5 MiB reference (1,572,864 bytes) by 51,725 bytes (~3.3%). CI warning is non-blocking. Largest entries: `classes.dex` 378,210 compressed bytes; `res/SR.ttf` 200,452; `assets/kabut-pagi-dempo.jpg` 230,997 (uncompressed / equal compressed). Review size delta before release; no size regression baseline has yet been established in this audit.
- Test APK artifact uploaded for run #1534: artifact ID `13615852690`, ZIP size 1,423,863 bytes, SHA-256 `b11d3b46ed25b1a54b3107f9b080587b628cdda48c97f97e44888d6b6ed59e86`; retention expires 15 Nov 2026. This is a CI artifact only, not a device acceptance result.
- PR dependency check: PR #79 targets PR #77 branch. Comparing PR #77 head to PR #79 head reports 53 commits ahead / 1 behind from merge base `0aeb3851ddce5deb47e3c5f96db44631b0343577`. GitHub currently reports both PRs mergeable. The CI workflow file content at both exact heads is byte-identical (blob `5d271a43c5bfada87d85b144740a0074aea3e56b`). Preserve the stacked dependency and verify the final PR diff/ancestry after PR #77 integration; do not force-push or merge as part of this audit.
- Source follow-ups previously committed on PR #79 remain: stale Grid focus callback generation guard/cancellation and bilingual altitude formatting (`%1$d mdpl` / `%1$d m ASL`).
- Overall source/CI status: PASS for exact current head; overall Home layout acceptance remains PARTIAL because physical Mi TV Stick visual/DPAD/overscan and whole-Home regression tests have not been performed.
- No APK installed; no ADB/device settings/default-launcher mutation; no PR merged. Keep both PRs Draft until owner device acceptance and final ancestry review.


### APK size baseline comparison — 8 November 2026

- Compared PR #79 exact-head release APK (`1,624,589 bytes`) against PR #77 exact-head CI #1506 release APK (`1,622,585 bytes`). The PR #79 build is only **2,004 bytes (+0.12%)** larger than the parent PR #77 build.
- Both builds already exceed the historical 1.5 MiB warning threshold; therefore the threshold warning is pre-existing, not a material regression introduced by the balanced-frame/header changes. Continue monitoring APK size, but do not classify this PR as causing the full 51,725-byte threshold overage.
- Parent artifact: ID `13609978757`, ZIP 1,422,754 bytes. Latest PR #79 artifact: ID `13615852690`, ZIP 1,423,863 bytes. These are compressed artifact archive sizes; compare release APK payload sizes separately as above.


## Resume focus-restoration audit follow-up — 8 November 2026

- Further lifecycle audit found `restoreFocus()` queued a resume callback that could force the saved focus destination after a newer DPAD focus action. Fixed by capturing `gridFocusRequestGeneration` and skipping stale restoration; resume bookkeeping still completes.
- Expanded cancellation: global focus observer now invalidates queued Grid/resume focus work whenever focus moves to any non-Grid destination, covering header, Dock, prayer/media controls and popups. Added Python regression tests for resume-generation guard and global non-Grid invalidation.
- PR #79 branch `fix/home-balanced-frame-001` latest recorded head `162cfdeb681cdd52e160c13d756679f84c0a3f2a` (docs-only commit after source/test head `434d32afb424c4625a45979cc08d632be6443cc3`). Remains OPEN / DRAFT / UNMERGED; PR #77 dependency remains in place.
- Exact-head source/test CI #1540 / run `42478445302` validates source/test head `434d32afb424c4625a45979cc08d632be6443cc3`: Lists/tools/docs job PASS; Android build + JVM tests IN PROGRESS. Do not mark source validation PASS until the Android job completes. A prior run #1537 validated an earlier source revision and is not a substitute.
- Physical Mi TV Stick DPAD/return-from-app/visual/overscan acceptance remains PENDING. No APK installed, no ADB/device settings/default-launcher mutation, no PR merged.


## CI #1540 PASS and dependency reconciliation follow-up — 8 November 2026

- CI #1540 / run `42478445302` completed SUCCESS for both jobs. Android Launcher build, `testReleaseUnitTest`, release assembly, APK diagnostics and artifact upload all PASS; Lists/tools/docs checks PASS. The run validates exact source/test commit `434d32afb424c4625a45979cc08d632be6443cc3`; PR #79 current head is `162cfdeb681cdd52e160c13d756679f84c0a3f2a`, a docs-only commit after that source/test commit.
- Release APK exact size: **1,624,505 bytes** (~1,586 KiB). Historical 1.5 MiB threshold warning remains non-blocking. Compared with PR #77 CI #1506 APK size 1,622,585 bytes, delta is **+1,920 bytes (+0.118%)**; no material size regression. Compared with PR #79 CI #1534, this build is 84 bytes smaller.
- Artifact ID `13616452360`, ZIP size 1,423,819 bytes, SHA-256 `6fc79448375af7b38cc02edcd0a3971d84460f190bff529c7741e7e28523ebcd`, expires 15 November 2026. Artifact is build output only; not installed.
- **Dependency audit correction:** PR #79 remains based on branch `fix/home-layout-consolidated-001`, but its recorded base SHA is `0aeb3851ddce5deb47e3c5f96db44631b0343577`, while PR #77 current head is `0ea3746f0304b7042a018eeebf850d759f357bf8`. Comparing PR #77 head → PR #79 head `162cfdeb...` reports 60 ahead / 1 behind from merge base `0aeb3851...`. The one-commit difference includes a change to `.github/workflows/ci.yml` (the PR #77 branch updated the Home layout artifact upload condition/name). This supersedes the earlier note that the workflow files were identical. GitHub currently reports both PRs mergeable, but stacked ancestry is **NOT reconciled**; do not merge or force-push. Decide and verify a safe parent-base synchronization before final review.
- PR #77 and PR #79 remain OPEN / DRAFT / UNMERGED. Physical Mi TV Stick visual, DPAD, resume, overscan and whole-Home acceptance remain PENDING. No APK installation, ADB, default-launcher change, or device settings mutation.


### Dependency diff clarification — same workflow tree content

- Follow-up blob-level verification compared `.github/workflows/ci.yml` directly at PR #77 head `0ea3746f0304b7042a018eeebf850d759f357bf8` and PR #79 head `162cfdeb681cdd52e160c13d756679f84c0a3f2a`: both resolve to the **same blob SHA** `5d271a43c5bfada87d85b144740a0074aea3e56b`, and the Home layout artifact upload condition/name are identical in both trees. The earlier compare-commits file list reflects divergent commit ancestry, not a remaining workflow-content difference. Keep the ancestry status at 60 ahead / 1 behind pending identification of the unique commit; do not describe this as a workflow conflict or content mismatch.


## Home focus restoration follow-up and PR ancestry audit — 8 November 2026

- PR #79 CI #1540 / run `42478445302` is fully **SUCCESS** for source/test head `434d32afb424c4625a45979cc08d632be6443cc3`: Android build, JVM unit tests, release APK diagnostics/artifact upload and Lists/tools/docs checks all PASS. APK 1,624,505 bytes; artifact ID `13616452360` (ZIP 1,423,819 bytes, expires 15 Nov 2026). This does not validate later commits.
- PR ancestry audit identified the only PR #77-side commit after merge base: `0ea3746f0304b7042a018eeebf850d759f357bf8`, CI artifact publishing for PR #79. PR #79 contains parallel commit `f38b9663e2422ef793f075acc47f800200f677f7` with an identical patch and identical resulting workflow blob `5d271a43c5bfada87d85b144740a0074aea3e56b`. It is a duplicate equivalent CI change, not missing functional source. Do not cherry-pick it into PR #79 solely for ancestry; preserve no-force-push/no-merge constraints.
- Further static focus audit found horizontal Grid resume restoration did not explicitly reapply the glass when the target card was already focused. Corrected `focusTileWhenLaidOut()` to call `onTileFocused(position, card)` for an already-focused card; added Python source-contract regression test.
- PR #79 source commit `c52a1cba8ea3190299bce994a52f010457dddbd4`; regression test commit `de4ae44c53e6f99bce2e8fc2d0694a8982a117a3`; audit report commit `edf93d80cca713cfb89b504bf437c64f93c64ac2`. Fresh exact-head CI pending; no PASS claim for these changes yet.
- PR #77/#79 remain OPEN / DRAFT / UNMERGED. Physical Mi TV Stick visual/DPAD/resume/overscan and whole-Home acceptance remain pending. No APK installation, ADB, default-launcher or device settings mutation.


### Retained-focus restoration correction and CI status — 8 November 2026

- Additional audit found already-focused horizontal Grid restoration could leave the glass highlight hidden because `requestFocus()` may not emit a new focus callback. First patch targeted the similarly shaped Dock helper by mistake; CI #1543 correctly caught this via the new regression test. That attempt is **FAIL / superseded**, not accepted.
- Corrected source commit `728fb356be892b15c4903ea97ff950b1fbdde9fc`: already-focused Grid card explicitly calls `onTileFocused(position, card)`; already-focused Dock card calls `onDockTileFocused(dockAdapter.keyAt(position), card)`. Otherwise normal `requestFocus()` remains.
- Regression test commit `c058935ed4398f5c7964d753fcf35a9cf77989bb` covers both restoration helpers. CI #1545 / run `42479472690` is validating the exact source/test commit; both jobs were still in progress at last poll. No PASS claim yet.
- PR #79 latest docs-only audit report commit `3bfaa7047989dcfd04a11eb58ec7e6d94882e685`. PR remains OPEN / DRAFT / UNMERGED. Physical Mi TV Stick acceptance pending; no APK installation, ADB, default-launcher/device settings mutation, force-push, or merge.


### CI #1545 final result — 8 November 2026

- CI #1545 / run `42479472690` completed **SUCCESS** on corrected source/test commit `c058935ed4398f5c7964d753fcf35a9cf77989bb`: Launcher build + `testReleaseUnitTest` PASS; release packaging and APK diagnostics PASS; Lists/tools/docs checks PASS; test APK artifact upload PASS.
- Corrected retained-focus handling covers both horizontal Grid and Dock: already-focused cards explicitly reapply the corresponding focus handler; otherwise normal `requestFocus()` is used. Regression test covers both helpers. Earlier CI #1543 failure is recorded as superseded, not passed.
- APK exact size **1,624,589 bytes (1,586 KB)**; versus PR #77 CI #1506 size 1,622,585 bytes: **+2,004 bytes (+0.12%)**. Historical 1.5 MiB threshold warning remains non-blocking.
- Artifact ID `13616788353`, ZIP size 1,423,762 bytes, SHA-256 `549ca1201857ec44968a86ec8961f4431354ea7bd29f0de93bfca99b7f0d39c9`, expires 15 Nov 2026. Artifact not installed.
- PR #79 remains OPEN / DRAFT / UNMERGED; PR #77 remains OPEN / DRAFT / UNMERGED. Static integration audit PARTIAL; physical Mi TV Stick acceptance PENDING. No ADB, APK installation, device/default-launcher settings changes, force-push, or merge.
- PR #79 CI result comment posted, GitHub comment ID `6721430044`.


### Direct APK distribution remediation — 8 November 2026

- User reported Samsung Browser saved the ChatGPT sandbox download as `app-release.apk.bin` despite the local artifact being a valid APK. Audit confirmed both local files were valid Android packages (1,624,589 bytes; SHA-256 `6c7981a6c71aa5fc6f5146ba5a6023b9fdbfc527c887d1c367dc500d04fd3b10`). Root cause for the observed suffix is the ChatGPT attachment delivery path/response metadata, not the Gradle build; direct sandbox re-links and renaming were not a reliable fix.
- Added `.github/workflows/publish-apk-release.yml` to PR #77 base branch and PR #79 branch. Workflow executes on PR #79, downloads the exact CI #1545 artifact, verifies size + SHA-256, and publishes the unmodified APK as a GitHub Release asset. PR #79 workflow commit `acf344bc31874dfc6b680ac0bd6089b72ce1c8fa`; base workflow commit `d6a5581d2975ebe61c4862ca4614474c60e53cca`.
- A first approach rebuilt the same source commit and failed byte-for-byte SHA verification (`298f494e...` vs CI artifact `6c7981a6...`), so it was blocked. Rebuild is not byte-for-byte reproducible in this environment; final workflow avoids repackaging/re-signing and publishes the exact CI artifact. Initial artifact-name mismatch was corrected.
- Final publishing workflow run #7 / run ID `42480957620`: **SUCCESS**. GitHub Release `glass-tv-home-ci1545-20261108` (prerelease/QA) published. Direct asset `app-release.apk`, asset ID `723889891`, MIME `application/vnd.android.package-archive`, size 1,624,589 bytes, SHA-256 `6c7981a6c71aa5fc6f5146ba5a6023b9fdbfc527c887d1c367dc500d04fd3b10`.
- Direct release page: https://github.com/Frangga11/glass-tv-home/releases/tag/glass-tv-home-ci1545-20261108
- Direct APK asset: https://github.com/Frangga11/glass-tv-home/releases/download/glass-tv-home-ci1545-20261108/app-release.apk
- PR #79 remains OPEN / DRAFT / UNMERGED. No APK installation, ADB, device/default-launcher settings mutation, or merge. Physical Mi TV Stick acceptance remains PENDING. User must confirm the browser's received filename; the server-side GitHub asset metadata is correct, but actual Samsung Browser download confirmation has not yet been observed.


## Home Screen remediation checkpoint — 8 November 2026
- Active audit: `GTVH-HOME-SCREEN-CROSS-AUDIT-001`; source branch `fix/home-balanced-frame-001`; exact source/test HEAD `701487f7f708d0b4342b613205fde9fba7c0dd72`.
- PR #79 remains OPEN / DRAFT / UNMERGED, stacked on PR #77. No merge or device operation.
- Applied source corrections: HOME-001 retain supported paused/buffering/connecting media sessions with playback state snapshotted off the UI path; HOME-002 visible prayer table opens popup and supports DPAD activation; HOME-003 generation-guarded Dock retries; HOME-004 combined Weather/AQI scale + Header highlight listener; HOME-005 Home atmospheric overlay refresh on resume using its existing due cadence.
- Added source-contract regression coverage for all five findings.
- Exact-head CI for this HEAD has not yet appeared in workflow/status results. Earlier CI #1560 does not validate these later commits; tests are not claimed passing.
- APK installation, ADB, device settings/default launcher mutation, physical Mi TV Stick verification and merge remain prohibited/pending separate owner authorization.
- Canonical detailed record: `GTVH_HOME_SCREEN_CROSS_AUDIT_001.md`.


## Exact-head CI result — 8 November 2026
- PR #79 exact head `701487f7f708d0b4342b613205fde9fba7c0dd72`; CI #1574 / run `42486582167` completed **SUCCESS** on PR merge ref `fa5e4feb4ba451d6209762278ce18a6337edbdba`, which includes that exact head.
- Both jobs passed: Lists/tools/docs; Launcher build/unit tests. Release build: **BUILD SUCCESSFUL**.
- Release APK size diagnostic: **1,627,417 bytes**. PR test APK artifact upload succeeded (artifact ID `13619931751`).
- Five Home Screen remediation fixes and their source-contract/build CI validation are PASS. End-to-end acceptance on Mi TV Stick remains pending; no APK installation, ADB, device settings/default launcher changes, or physical test.
- PR #79 and dependency PR #77 remain OPEN / DRAFT / UNMERGED.
- Detailed record: `GTVH_HOME_SCREEN_CROSS_AUDIT_001.md`.


## Home Screen remediation — latest DPAD/weather hardening (8 Nov 2026)

- PR #79 latest source/test HEAD: 97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5; PR remains OPEN / DRAFT / UNMERGED, stacked on PR #77.
- HOME-002 DPAD path tightened: DPAD Down from btnAssistant enters the visible prayer timetable; DPAD Up returns to visible Header controls; DPAD Down returns to the app area. Click, Center and Enter still open the popup.
- HOME-005 tightened: Home atmospheric overlay refresh runs on resume and active minute tick, retains 15-minute due cadence, prevents overlapping fetches, and now avoids network refresh while the user has disabled the dynamic overlay. Enabling the overlay triggers a due refresh immediately.
- Regression source-contract tests cover these routes and the enabled/disabled refresh behavior, alongside HOME-001/003/004 coverage.
- Exact-head CI for 97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5 has not yet been observed. Prior CI runs on earlier heads do not validate these latest changes; no exact-head test pass is claimed.
- No APK installation, ADB, device settings/default launcher changes, physical TV testing, force-push or merge.
- Canonical audit: GTVH_HOME_SCREEN_CROSS_AUDIT_001.md.


## Latest Home Screen remediation validation — 8 November 2026
- Latest PR #79 head: `765634e7d806f48bc38014119cd170a8697ea442`; CI #1579 / run `42486810729`: **SUCCESS** on merge ref `09c7b98534f151cb292f1b03c50b9d9ed21c3c90`, which includes that exact head.
- Both CI jobs passed (Lists/tools/docs; Launcher build/unit tests); release build successful; APK diagnostic size **1,627,581 bytes**; test artifact ID `13620310383`.
- HOME-002 test was extended for DPAD Up and Header Assistant → prayer timetable focus routing. Latest exact-head CI validates the final test commit.
- All five source corrections and latest-head CI validation: PASS. Whole Home Screen end-to-end acceptance: **PARTIAL / NOT PASS** pending separately authorized Mi TV Stick verification.
- PR #79 and dependency PR #77 remain OPEN / DRAFT / UNMERGED. No APK installation, ADB, device settings/default launcher changes, or physical TV test.
- Canonical record: `GTVH_HOME_SCREEN_CROSS_AUDIT_001.md`.


## Final latest-head Home Screen remediation validation — 8 November 2026
- Latest PR #79 head: `97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5`; CI #1581 / run `42486917618`: **SUCCESS** on merge ref `5203d9cab667b976a3cd47ecaceac444ecd8e06b`, which includes that exact head.
- Both jobs passed; release build successful; APK diagnostic size **1,627,577 bytes**; test artifact ID `13620161574`.
- Regression coverage now includes the dynamic-weather-effects disabled guard and settings-toggle refresh behavior.
- HOME-001–HOME-005: source fixes and latest-head CI validation PASS. Whole Home Screen end-to-end/device acceptance remains **PARTIAL / NOT PASS** pending separate owner authorization and Mi TV Stick verification.
- PR #79 remains OPEN / DRAFT / UNMERGED; dependency PR #77 remains unmerged. No APK installation, ADB, device settings/default launcher changes, or physical TV test.
- Canonical report: `GTVH_HOME_SCREEN_CROSS_AUDIT_001.md`.


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


## Handover — Home weather/AQI pill width refinement (8 Nov 2026)

Resume only from `GTVH-CHECKPOINT-LATEST.md` on `docs/project-master-inventory`. Active task: `GTVH-HOME-WEATHER-PILL-WIDTH-REFINEMENT-001` in PR #79, branch `fix/home-balanced-frame-001`.

Current PR HEAD `65544d9eea9bd0a5ae5156a9bd58724b1817fd2a`; implementation commit `3c9821864a08bb50f6f1b13473964f8d5b5baeb7`. Location width remains the 34% reference; weather/AQI now uses that same width. Both left anchors remain unchanged. Regression test added. CI #1587 / run `42489445766` Lists/tools/docs PASS; Android test/build still in progress at handover. Next: verify CI conclusion and test output; update checkpoint and supporting docs with final exact status. Then, only when a device test is separately authorized, visually verify 1920×1080 at 320dpi, ID/EN, long location/weather/AQI values, clipping and Header non-regression. Do not merge PR #79 or install APK as part of this task.


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


## Mandatory APK delivery rule — 8 November 2026 (OWNER DIRECTIVE)

**NON-NEGOTIABLE: every user-facing build delivery must be a direct Android package file ending exactly in .apk. Never deliver .bin, .zip, or a renamed archive as the installable app.**

- Build the Android release APK (assembleRelease) from the exact verified source HEAD.
- The final user-facing deliverable is the actual APK binary, directly downloadable and installable on Android TV; its filename must end in .apk.
- If GitHub Actions stores/transfers the build artifact inside a ZIP archive, that ZIP is an internal transport container only. Extract the APK, verify the extracted file is a valid Android package, and distribute only the extracted .apk to the owner.
- Never send the workflow-artifact ZIP itself to the owner. Never change an archive's extension to .apk; verify file type, size, SHA-256, and exact source commit/build run.
- Before delivery, check the final link/filename and confirm that it points directly to the APK asset, not a ZIP/BIN attachment.
- If a direct .apk cannot be produced or verified, stop and report the blocker; do not substitute ZIP/BIN.
- This rule supersedes any older handover wording that treated the workflow ZIP as the deliverable. CI artifact archive sizes are diagnostic metadata only.


## APK delivery failure follow-up — 8 November 2026 (OWNER REPORTED)

- Owner supplied a Samsung Browser screenshot showing the ChatGPT sandbox attachment `Glass-TV-Home-30pct-Pills.apk` is presented by the browser as `Glass-TV-Home-30pct-Pills.apk.bin`. The prior delivery therefore **FAILED the APK-only delivery requirement**. Do not mark delivery fixed merely because the local file is a valid APK or its local filename ends in `.apk`.
- Root cause of the repeated failure: assistant again used a `sandbox:/mnt/data/...` attachment link after the owner had already reported that this delivery path adds `.bin` in Samsung Browser. This was a process error; the documented rule was not followed.
- Audit of `.github/workflows/publish-apk-release.yml` on PR #79 found the prior publisher was hardcoded to download and publish CI #1545 artifact (`c058935...`), not the current CI #1590 candidate. Therefore the existing direct GitHub Release asset was stale and cannot be reused as the latest build.
- Corrected publisher workflow commit on PR #79 branch `fix/home-balanced-frame-001`: `16ba3bf72f9b2e080fac23510f744c4ea5efb399`. It now checks out the exact PR head, runs `testReleaseUnitTest assembleRelease`, validates the APK package, and publishes the actual APK binary as a GitHub Release asset with a tag derived from the exact source SHA. It does not upload a ZIP as the user-facing asset.
- PR #79 remains OPEN / DRAFT / UNMERGED. At documentation time, no workflow run for commit `16ba3bf72f9b2e080fac23510f744c4ea5efb399` had yet been returned by the workflow-run lookup. **Direct APK delivery is NOT YET VERIFIED/FIXED.** Do not give another sandbox attachment link. Wait for the publisher workflow to complete, verify the GitHub Release asset name ends exactly `.apk`, verify MIME `application/vnd.android.package-archive`, size and SHA-256 against the run, then provide only the direct GitHub Release asset URL.
- If the workflow does not trigger or fails, stop and report the exact blocker; do not fall back to sandbox, `.bin`, or ZIP. Never claim a new build or successful delivery until the exact-head workflow and direct asset are confirmed.


## Root-cause audit and workflow correction — 8 November 2026

- Owner reported Samsung Browser saves the prior ChatGPT sandbox attachment as `Glass-TV-Home-30pct-Pills.apk.bin`; prior delivery failed the APK-only acceptance criterion.
- Root cause verified from GitHub Actions run `42490240680` (publisher run #45) job log `128760816419`: it executed the old workflow definition and downloaded hardcoded artifact from CI #1545 (`run-id: 42479472690`, artifact `13616788353`), verified old source commit `c058935ed4398f5c7964d753fcf35a9cf77989bb`, then uploaded that stale binary to release tag `glass-tv-home-ci1545-20261108`. This is why the release never contained the latest PR #79 build.
- Exact workflow-dispatching cause: PR #79 is based on `fix/home-layout-consolidated-001`. For `pull_request` events, GitHub uses the workflow definition from the PR base branch, not the edited workflow copy on PR #79's head branch. The base-branch workflow still contained the CI #1545 hardcoding, so changing only the PR #79 head copy did not change the executed workflow. This behavior is confirmed by the run #45 logs.
- Corrected `.github/workflows/publish-apk-release.yml` on PR #79 base branch `fix/home-layout-consolidated-001`, commit `7e51a86cc83394ae47f7c9d32c22bf0c6042c9d1`: it now checks out the exact `github.event.pull_request.head.sha`, runs `testReleaseUnitTest assembleRelease`, validates the APK manifest/package, and publishes a uniquely tagged direct `.apk` asset named `Glass-TV-Home-PR79-<source-sha>.apk`. No workflow ZIP is used as the user-facing deliverable.
- Aligned the same corrected workflow into PR #79 head branch to prevent a future merge from restoring the stale publisher: commit `b4abdf0bd555a0106c135add20c3f287cf10b412`. PR #79 also contains prior trigger/diagnostic commit `4fefd3ae0640eb1559b83856d9f907dd687f7987`.
- PR #77 received a publisher workflow run `42491455206` which was **SKIPPED** by design because the workflow is scoped to PR #79; its companion CI run `42491455159` was still in progress at audit time. No new PR #79 publisher run was visible yet after the base workflow correction and head alignment. The latest Release listing still contained only the stale CI #1545 asset.
- Therefore workflow source correction is committed, but new build + direct APK release + Samsung Browser download verification remain **PENDING / NOT ACCEPTED**. Do not claim completion, do not reuse old release, and do not send sandbox links, ZIP or BIN. Next: obtain a PR #79 synchronize/dispatch event using the corrected base workflow, verify successful unit tests/build, inspect new release asset exact `.apk` name and `application/vnd.android.package-archive` MIME, verify size/SHA against workflow logs, then give only the direct GitHub Release asset URL.


## Direct APK rebuild completed — 8 November 2026

- Owner requested rebuild. The corrected PR #79 base-branch publisher ran successfully: workflow run `42491530852` (job `128764760421`, conclusion `success`).
- Exact source HEAD: `b4abdf0bd555a0106c135add20c3f287cf10b412`.
- Build command: `./gradlew --no-daemon testReleaseUnitTest assembleRelease`; both app and installer release unit-test tasks passed and `:app:assembleRelease` completed; log reports `BUILD SUCCESSFUL`.
- Verified output: `Glass-TV-Home-PR79-b4abdf0bd555a0106c135add20c3f287cf10b412.apk`.
- Size: `1,627,561` bytes. SHA-256: `2bd779dc0da3a8cbf81b7f6f7dad1ee53d6f7be949eef9261d1d0b7018c09aa6`.
- GitHub Release tag: `glass-tv-home-pr79-b4abdf0bd555a0106c135add20c3f287cf10b412`.
- Release API verification confirms exactly one asset with the exact `.apk` filename, size `1,627,561`, state `uploaded`, and content type `application/vnd.android.package-archive`. Direct asset URL: `https://github.com/Frangga11/glass-tv-home/releases/download/glass-tv-home-pr79-b4abdf0bd555a0106c135add20c3f287cf10b412/Glass-TV-Home-PR79-b4abdf0bd555a0106c135add20c3f287cf10b412.apk`.
- Separate general CI run `42491530833` was still in progress at the time of this note; the publisher workflow itself independently passed unit tests + release APK build. Device installation and Samsung Browser filename behavior remain pending owner confirmation.
- User-facing delivery rule observed: provide only the direct GitHub Release `.apk` asset URL. No sandbox attachment, ZIP, or BIN.

## Permanent delivery procedure — GTVH-APK-DIRECT-DELIVERY-001

For all future Glass TV Home builds, use the workflow definition on the pull request's base branch for pull_request events; keep the PR head copy synchronized. The previous publisher hardcoded CI #1545, and changing only the PR head workflow did not affect the workflow GitHub executed. Always build from the exact PR head SHA with testReleaseUnitTest assembleRelease, validate the APK, publish the actual APK binary as a source-SHA-tagged GitHub Release asset, and verify filename, MIME, size, and SHA-256 against the workflow log. Give the owner only the direct Release asset URL ending in .apk. Never provide sandbox attachments, Actions artifact ZIPs, .zip, or .bin. If direct APK verification fails, stop instead of substituting a format or stale build.

Latest verified direct APK and build evidence are recorded in GTVH-CHECKPOINT-LATEST.md under GTVH-APK-DIRECT-DELIVERY-001. Samsung Browser download behavior and Mi TV Stick installation acceptance are still awaiting owner confirmation; PR #79 remains OPEN / DRAFT / UNMERGED.

## Current handover — GTVH-HOME-WALLPAPER-TEXT-REFINEMENT-001 (8 Nov 2026)

1. Restart by reading `GTVH-CHECKPOINT-LATEST.md` on `docs/project-master-inventory` first.
2. Scoped implementation is PR [#80](https://github.com/Frangga11/glass-tv-home/pull/80), branch `fix/home-wallpaper-text-refinement-001`, HEAD `c7d95c3de24bc70b671159d5aa9ef49451cbe112`, stacked on PR #79 HEAD `b4abdf0bd555a0106c135add20c3f287cf10b412`.
3. Source/CI gates: PASS. Python: 166 tests (1 skipped); Android `testReleaseUnitTest` and `assembleRelease`: PASS. APK diagnostic 1,627,937 bytes; +376 bytes versus the prior recorded build; historical threshold warning is non-blocking.
4. Do not merge PR #80 or install on device automatically. Next acceptance step is owner visual/physical test on Mi TV Stick 1920×1080: verify location → title → description, 5mm down shift for both shelves, no Dock collision, prayer gradient contrast, doa integration, and unchanged Dock/Header/navigation/safe frame.
5. If visual/device issues are found, repair only the named in-scope component. Record out-of-scope findings separately. Do not mark final until physical acceptance is confirmed.


## Corrective handover — GTVH-HOME-WALLPAPER-TEXT-REFINEMENT-001 (8 Nov 2026)

- Read `GTVH-CHECKPOINT-LATEST.md` first; Section 12 is the current corrective state and supersedes the prior 5mm downward-shift instructions in older handover entries.
- PR #80 remains OPEN / DRAFT / UNMERGED on `fix/home-wallpaper-text-refinement-001`.
- Current corrective HEAD: `a085be150ebcf1d48b825ed7ac43b172261f3aa0`; base PR #79 HEAD `b4abdf0bd555a0106c135add20c3f287cf10b412`.
- Corrected geometry: wallpaper shelf up ~28dp from the prior PR #80 rendering, nominal 32.5dp clearance above Dock; daily Ayah up 18dp; prayer header/table up 24dp total.
- All six prayer names/times use 13sp/17sp solid high-contrast text and retain the XML Inter font; Arabic/translation enlarged to 20/14sp at 1080p (22/15sp at 4K); subtle right-shelf gradient scrim added without a bordered panel.
- Three changed files for the corrective pass: MainActivity.java, activity_main.xml, and test_home_wallpaper_text_refinement_contract.py. DockGeometry, Dock/Grid, Header/capsules, navigation, wallpaper image/crop/provider/rotation, prayer data/provider/order and safe frame remain untouched.
- Exact-head CI #1599 / run `42497894475` for `a085be1` is IN PROGRESS. Prior CI #1592 / run `42493469262` belongs to old HEAD `c7d95c3` only.
- No APK install, ADB, device setting change or merge. Next: verify exact-head CI, review rendered 1920×1080, then owner Mi TV Stick visual/remote acceptance.


## Exact-head CI pass handover — GTVH-HOME-WALLPAPER-TEXT-REFINEMENT-001 (8 Nov 2026)

- Current PR #80 source HEAD: `a085be150ebcf1d48b825ed7ac43b172261f3aa0`; PR OPEN / DRAFT / UNMERGED.
- Exact-head CI #1599 / run `42497894475`: SUCCESS, both jobs passed.
- Python suite: **167 tests, OK (1 skipped)**; all six refinement regression contracts passed, including all six prayer labels at 13sp and times at 17sp.
- Android `./gradlew --no-daemon testReleaseUnitTest assembleRelease`: BUILD SUCCESSFUL in 1m 18s. Release APK diagnostic size 1,627,685 bytes; historical 1.5 MiB warning is informational/non-blocking.
- Publisher workflow #54 is SKIPPED by design for PR #80; no direct APK Release asset exists from this run. A visual-parity artifact ZIP (ID `13624193020`, 1,426,812 bytes, expires 15 Nov 2026) was uploaded; do not describe this ZIP as a direct APK.
- PR total diff contains 4 files including the CI workflow adjustment that enables the PR #80 visual-parity artifact. Corrective application edits remain only MainActivity.java and activity_main.xml plus the scoped regression test.
- Rendered 1920×1080 visual review and physical Mi TV Stick acceptance remain pending. No ADB, install, device setting change or merge. Next: inspect visual output and await owner acceptance.


## APK package handover — PR #80 (8 Nov 2026)

- Release APK built successfully by exact-head CI #1599 / run `42497894475` from PR #80 HEAD `a085be150ebcf1d48b825ed7ac43b172261f3aa0`.
- Artifact ID `13624193020` downloaded; ZIP verified to contain one `app-release.apk` of **1,627,685 bytes**.
- SHA-256: `67ecc3fddc6b4bab6c36025c6d285973fe329591eabe9f29c108fbc49d8fe602`.
- User-facing filename: `Glass-TV-Home-PR80-a085be1.apk`.
- This is a successful release build from the current PR head, not a merged/final release. PR #80 remains OPEN / DRAFT / UNMERGED. Physical Mi TV Stick installation and owner acceptance are still pending.


---

## Handover — GTVH-HOME-DOCK-ISLAMIC-SHELF-NAVIGATION-REFINEMENT-001 (8 Nov 2026)

- Read canonical `GTVH-CHECKPOINT-LATEST.md` first. Current scoped continuation is PR #81, not a modification to PR #80.
- Repository: `Frangga11/glass-tv-home`.
- Branch: `fix/home-dock-islamic-shelf-navigation-001`.
- PR #81: OPEN / DRAFT / UNMERGED; base branch `fix/home-wallpaper-text-refinement-001` (PR #80).
- Exact source HEAD: `2130675f1f2723f8e7c895cbc6ac63327d2efd60`; exact base HEAD: `a085be150ebcf1d48b825ed7ac43b172261f3aa0`.
- PR #80 remains unchanged and OPEN / DRAFT / UNMERGED.
- Scope completed in source: Dock-only 0.95 card scale and 44dp icon; total horizontal host reduction ~8dp with 6/8dp horizontal decoration offsets; stable Islamic shelf anchor and bottom-edge height correction; signed smooth DOWN/UP row scrolling with generation cancellation and lifecycle reset.
- CI #1620 / run `42501726878`: SUCCESS on exact HEAD. Python 176 tests, 1 skipped; Android app/installer release unit-test tasks and `assembleRelease` passed; BUILD SUCCESSFUL in 1m19; 99 actionable tasks.
- Release APK size 1,629,469 bytes; SHA-256 `caa3289641184e516b5758b8ce647d7bc33616ee55cb5f022576e53add572e24`. Artifact ID `13625911589`; ZIP SHA-256 `e9dbc0307aa7823d019ccf464e21e633f6dfe1a3a5906407feee3ada6fa1ea41`. Extracted APK is available from the conversation's PR #81 build output; it is not a final merged release.
- **Do not mark final:** rendered 1920×1080 review and physical Mi TV Stick remote acceptance are still pending. No ADB, installation, device settings change or merge was performed.
- Next: owner tests the exact-head APK on Mi TV Stick, including Dock capacities 4/5/6, repeated DOWN/UP, fast reversal, incomplete last row, Islamic shelf stability and return from launched apps. Record acceptance before merge.


---

## Handover — GTVH-HOME-ISLAMIC-SHELF-VERTICAL-POSITION-AND-GRID-ROW-SCROLL-FIX-002 (9 Nov 2026)

- **Start here:** read canonical `GTVH-CHECKPOINT-LATEST.md` first. This handover is supporting context only.
- PR #82: OPEN / DRAFT / UNMERGED; [PR #82](https://github.com/Frangga11/glass-tv-home/pull/82).
- Branch: `fix/home-islamic-shelf-vertical-position-grid-row-scroll-fix-002`.
- Exact HEAD: `d6f594933b712b36b0660e119a0d4ee8b7de3134`.
- Base branch: `fix/home-dock-islamic-shelf-navigation-001`, current tip `3cd36b8a5fbc788a7b15e178e4aaf0606e919c61` (PR #81). PR #82 is synchronized with that base (0 commits behind; mergeable). PR #80 remains OPEN / DRAFT / UNMERGED at `a085be150ebcf1d48b825ed7ac43b172261f3aa0`; PR #81 remains OPEN / DRAFT / UNMERGED. PR #81 received workflow-only CI/publisher eligibility updates to support exact-head PR #82 APK delivery; its application source was not changed.
- Scope: (A) measure actual horizontal Glass tray edge and target 27.5dp package clearance; (B) deterministic real Grid row scroll using decorated row pitch, cancellable `ValueAnimator` + incremental `RecyclerView.scrollBy()`, stable app key and aligned focus; (C) stronger Islamic headings and uppercase prayer labels.
- Exact-head CI #1630 / run `42505427017`: SUCCESS, both jobs passed. Python **177 tests, OK (1 skipped)**. Android app/installer release unit tests and `assembleRelease` passed; BUILD SUCCESSFUL in 1m37s.
- Direct APK publisher #85 / run `42505426954`: SUCCESS. Exact-head release APK **1,630,737 bytes**; SHA-256 `524963af6aad72444d38e1cd896eac25d29be05cac9e765b8819e0f69dcba735`.
- CI emitted the historical 1.5 MiB APK-size reference warning; it is explicitly non-blocking. The APK is 1,268 bytes larger than PR #81's 1,629,469-byte APK; continue monitoring the size delta.
- Direct APK: [Glass-TV-Home-PR82-d6f594933b712b36b0660e119a0d4ee8b7de3134.apk](https://github.com/Frangga11/glass-tv-home/releases/download/glass-tv-home-pr82-d6f594933b712b36b0660e119a0d4ee8b7de3134/Glass-TV-Home-PR82-d6f594933b712b36b0660e119a0d4ee8b7de3134.apk).
- Detailed evidence: `GTVH-HOME-ISLAMIC-SHELF-VERTICAL-POSITION-AND-GRID-ROW-SCROLL-FIX-002-AUDIT.md`.
- **Not final:** no physical Mi TV Stick installation, ADB, device setting change, rendered screenshot review or owner acceptance has occurred. CI proves source/build contracts, not actual visual scroll behavior.
- Next: owner installs only the exact-head APK on Mi TV Stick 1920×1080 / density320; verifies 27.5dp gap, stable package, DOWN/UP opposite real content movement, fast reversals, incomplete last row, capacity 4/5/6, app return focus and locked-component regressions. Capture screenshot/video if any discrepancy. Keep PR #82 DRAFT / UNMERGED until acceptance.


### Session closure — 9 November 2026

The FIX-002 implementation session is closed at checkpoint. Resume from canonical `GTVH-CHECKPOINT-LATEST.md`; next action is physical Mi TV Stick acceptance using the exact-head PR #82 APK. PR #80/#81/#82 remain OPEN / DRAFT / UNMERGED. Do not merge or declare final until measured 27.5dp clearance and the actual DOWN/UP row behavior are confirmed on device. No further source change is authorized until device findings are recorded.

---

# CURRENT HANDOVER — GTVH-HOME-UI-PROPORTION-GLASS-AND-FOCUS-REFINEMENT-003 — 9 NOVEMBER 2026

## Canonical restart
- Read `GTVH-CHECKPOINT-LATEST.md` first on `docs/project-master-inventory`.
- Source repository: `Frangga11/glass-tv-home`.
- Active source PR: [#83](https://github.com/Frangga11/glass-tv-home/pull/83), OPEN / DRAFT / UNMERGED.
- Source branch: `fix/home-ui-proportion-glass-focus-refinement-003`.
- Exact source HEAD: `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`.
- Base: PR #82 branch `fix/home-islamic-shelf-vertical-position-grid-row-scroll-002`, exact base HEAD `d6f594933b712b36b0660e119a0d4ee8b7de3134`.
- PRs #80/#81/#82 remain unmerged; no merge was performed.

## Completed implementation
- Manual-only update checks; no automatic launch/resume prompt or Settings red badge; APK install path retained.
- Navigation capsule 36dp, 2dp inner padding and 4dp icon/text gap.
- Horizontal Dock extra height 12dp; horizontal Dock icon optical scale 1.045; Grid icon bounds unchanged.
- Daily Doa divider and softened Islamic Shelf scrim.
- Shared 20dp glass radius and shared Glider focus indicator across the header pills.
- Regression coverage updated and added.

## Exact-head validation
- CI #1637 / run `42509424284`: SUCCESS. Python 184 tests, OK (1 skipped); Android app/installer Release unit tests and Release build passed.
- Publisher #92 / run `42509424259`: SUCCESS; exact-head APK published.
- APK: `Glass-TV-Home-PR83-947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803.apk`; 1,625,213 bytes; SHA-256 `2d14894da872f616188bace686be5f85021766914bbe65260cc76a7848baee26`.
- Release: https://github.com/Frangga11/glass-tv-home/releases/tag/glass-tv-home-pr83-947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803.
- CI artifact ID `13628113675`; ZIP size 1,427,011 bytes; ZIP digest `7caf8c43502a4db15c61a098bf1d55559ecf6a0fdf2f8eb73eeaecf0d32dbd08`; expires 15 November 2026.

## Remaining owner action
Install the exact-head APK on Mi TV Stick at 1920×1080 / density 320. Verify manual OTA behavior, all seven focus targets, navigation capsule proportions, Dock icon/height, wallpaper/Islamic Shelf readability, divider, clipping and regressions. Record screenshot/video and actual remote behavior. Do not merge or mark final before physical acceptance.

Detailed evidence: [GTVH-HOME-UI-PROPORTION-GLASS-AND-FOCUS-REFINEMENT-003-AUDIT.md](GTVH-HOME-UI-PROPORTION-GLASS-AND-FOCUS-REFINEMENT-003-AUDIT.md).

---

# PRE-MERGE HANDOVER — PR #77–#83 — 9 NOVEMBER 2026

- Owner requested a checkpoint and merge of PR #77–#83. No merge has been executed yet.
- Live refs, exact HEADs, base branches, CI status and ancestry are recorded in `GTVH-CHECKPOINT-LATEST.md` section “Pre-merge integration audit”.
- Active stacked Home path: #77 → #79 → #80 → #81 → #82 → #83.
- PR #78 is a separate old-main header branch overlapping eight files with #79; PR #79's own reconciliation document says #78 is not a valid candidate for the newer G3A–G4 / #77 stack. Do not merge #78 alongside #79 without explicit reconciliation.
- #77's base is PR #72 head branch; #72 is based on #71 head; #71 is based on #70 head. These are all OPEN / DRAFT / UNMERGED. Merging #77–#83 into their configured stacked bases alone will not deliver the chain to `main`.
- #77 current head vs #79 head comparison: 117 ahead / 5 behind. GitHub reports mergeable, but ancestry needs reconciliation.
- Exact-head CI for #77, #78, #79, #80, #81, #82 and #83 all reports success (runs #1591, #1500, #1592, #1599, #1628, #1630 and #1637 respectively). Physical Mi TV Stick acceptance remains pending.
- Await owner's decision: merge the active stack into each configured base (skip #78), or integrate the upstream dependency chain to `main` including #70–#72 and reconcile #78 separately. Do not force-push or change PR bases until confirmed.


---

## Owner decision — PR #78 closed and excluded — 9 November 2026

- Owner confirmed PR #78 was a mistake and instructed to remove it from the integration path.
- PR #78 has been closed on GitHub and verified CLOSED / NOT MERGED: https://github.com/Frangga11/glass-tv-home/pull/78. A closure note in its description says not to merge or revive it. GitHub PRs cannot be permanently deleted through the normal PR API; closing is the correct action.
- Do not include #78 in future audit, rebase, merge, or release planning.
- Focus only on #77 and #79–#83. Remaining path: #77 → #79 → #80 → #81 → #82 → #83, subject to explicit reconciliation of #77/#79 ancestry and the upstream #70–#72 dependency chain.
- No merge was executed in this session. Recheck live refs, reviews, threads, exact-head CI, and device acceptance before the next merge operation.


## Active refinement — GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-001 — 9 November 2026

- Active source PR: [#97](https://github.com/Frangga11/glass-tv-home/pull/97), OPEN / DRAFT / UNMERGED. Base: PR #83 source branch at exact SHA `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`.
- Source branch: `verify/home-spacing-capsule-height-refinement-001-validated`; exact HEAD: `bb10228f930cc0b0e8c3dbf526aad9b0c94112fd`.
- Superseded PRs #94, #95 and #96 are CLOSED / NOT MERGED. They were replaced to ensure exact-head CI ran against the final implementation and corrected regression contracts. Do not reopen or merge them.
- Owner-approved boundaries: horizontal safe margins 30dp; bottom safe margin 30dp in horizontal and vertical Dock modes; top safe margin remains 29dp. MIC/Assistant, Wi-Fi and the full Beranda/Profile/Settings navigation capsule are 30dp high. Inputs is intentionally untouched. Beranda/Profile drawable spacing is 2dp; Settings remains 4dp.
- Home wallpaper description bottom is anchored to the measured horizontal Glass tray top at 30dp. Prayer table retains its existing -6dp translation; the Islamic shelf anchor compensates the actual translation so the visible prayer-table lower edge is also 30dp from the tray.
- At 320dpi (density 2.0), 30dp = 60px. Runtime uses display density, not fixed pixel constants.
- Production files changed: `activity_main.xml`, `MainActivity.java`, `DockGeometry.java`. Regression files changed: new `test_home_spacing_capsule_height_refinement_contract.py` plus four existing contracts updated from superseded 29dp/36dp/27.5dp expectations. Total PR diff: 8 files; no other production files changed.
- CI #1668 / run `42531491295`: Lists/tools/docs job PASS. Python suite: **190 tests, OK (1 skipped)**. Android unit tests + Release build still IN PROGRESS at checkpoint capture; do not claim PASS until exact-head run completes.
- Publisher workflow skipped; no APK QA asset is available yet. No rendered 1920×1080 visual audit, physical Mi TV Stick test, ADB, installation, setting change or PR merge has been performed.
- **Status: BELUM FINAL.** Gates pending: exact-head Android unit tests and assembleRelease; APK artifact/size/hash verification; rendered 1920×1080 layout inspection; source/diff audit final pass; owner device acceptance after all gates.
- Detailed report: `GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-001-AUDIT.md`.
- Next action: complete exact-head CI #1668, verify APK QA artifact and inspect final diff. Keep PR #97 draft/unmerged; do not install on TV or request device testing before the automated and visual audits pass.


## CI correction checkpoint — PR #98 — 9 November 2026

- PR #97 is now CLOSED / NOT MERGED and superseded by [PR #98](https://github.com/Frangga11/glass-tv-home/pull/98). PR #98 is OPEN / DRAFT / UNMERGED; base remains PR #83 at `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`.
- Active source branch: `verify/home-spacing-capsule-height-refinement-001-android-tests`; exact HEAD: `4817edd2c7109fa7ab8c253bbb5d8037ec55a511`.
- PR #98 retains the three production files and adds two Android unit-test updates to the five Python test changes: total 10 files (3 production, 7 test). No unrelated production files added.
- Exact-head CI #1670 / run `42531675366` and #1669 / run `42531662425` are in progress at checkpoint capture. Previous exact-head Python suite passed 190 tests (1 skipped); Lists/tools/docs passed. Previous Android unit tests compiled production Java and found four stale test assertions (old 29dp safe frame and 27.5dp shelf gap). PR #98 updates those two Android test files to assert 30dp geometry; exact-head Android retest remains PENDING.
- No APK QA asset, rendered 1920×1080 visual audit, or physical Mi TV Stick acceptance is available. No ADB, installation, device setting changes or merge.
- **Status remains BELUM FINAL.** Await exact-head Android unit tests and release build, APK size/hash verification, then rendered visual audit. Do not install or request TV testing before all automated and visual gates pass.


## QA artifact and Android regression checkpoint — PR #99 — 9 November 2026

- Active source PR: [#99](https://github.com/Frangga11/glass-tv-home/pull/99), OPEN / DRAFT / UNMERGED. Exact base: PR #83 branch SHA `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`.
- Active source branch: `verify/home-spacing-capsule-height-refinement-001-qa`; exact HEAD: `443d8491109c416915955c8a9ceb202860916a6b`.
- PR #99 adds one QA workflow adjustment to the PR #98 candidate: .github/workflows/ci.yml now allows this refinement PR to publish the existing Home layout QA APK artifact. Production source remains exactly 3 files; regression coverage remains 7 files. Total 11 files.
- Exact-head CI #1672 / run `42531805446` and #1671 / run `42531795756` are in progress. Earlier Python suite result: 190 tests, OK (1 skipped); Lists/tools/docs job PASS. Android unit-test and release-build outcome on PR #99 is pending.
- Publisher workflow is skipped by design; the CI workflow's Home layout test APK artifact step is enabled for PR #99. Artifact publication and APK verification are pending.
- No rendered 1920×1080 visual review, physical Mi TV Stick acceptance, ADB, installation, setting change or merge.
- **Status BELUM FINAL.** Complete exact-head Python/Android tests, release build, QA artifact size/hash and rendered visual audit before any device installation or final declaration.


## Cumulative integration QA update — PR #101 — 9 November 2026

- Corrected source HEAD: `b8389e1f90b9fb7f65d91cfd0d7a41782f343d83`; base remains `4038c08f4825f7bacf965bc6708505d3c8c30ebc`. PR #101 remains OPEN / DRAFT / UNMERGED; 4 commits ahead, 0 behind; 16 changed files (5 production/source/workflow, 11 regression/test). Source PRs remain untouched.
- Exact-head CI retest #1681 / run [42535212323](https://github.com/Frangga11/glass-tv-home/actions/runs/42535212323) completed SUCCESS. Both jobs passed: Lists/tools/docs **211 tests, OK (1 skipped)**; Launcher build and unit tests **SUCCESS**, including `testReleaseUnitTest` for app and installer and `assembleRelease` (`BUILD SUCCESSFUL`).
- PR artifact published: artifact ID `13639167072`, name `glass-tv-home-dock-visual-parity-569500eacf0669f8d7bc5f191328a9d49790cf1e`, ZIP 1,428,388 bytes, SHA-256 `6ceb83fcf6a9530cd22afb24c2ada37d88a2df15aa2c9d688193c7e79f943139`. It contains `app-release.apk` (not a layout screenshot/test APK). Extracted APK: 1,626,525 bytes; SHA-256 `5423f7746d6b771fcb0c76ca3fcdca5186c5957d2967f66437554cb6e7d62398`; ZIP integrity PASS; manifest, DEX and resources present.
- Size diagnostic: 1,626,525 bytes is 53,661 bytes above the workflow's historical 1.5 MiB warning threshold. However, compared with the earlier PR #83 recorded release APK (1,625,213 bytes), the increase is only 1,312 bytes (+0.081%). This is a non-blocking warning, not a CI failure; retain for size trend monitoring, no unscoped optimization.
- CI artifact is built from GitHub's PR merge ref `569500eacf0669f8d7bc5f191328a9d49790cf1e`, which merges source HEAD above into base. It is not a production release publication; the separate release-publisher workflow was skipped by design for a PR.
- Final static package integrity and changed-file scope checks are recorded. Rendered 1920×1080 visual audit is NOT RUN: current available execution environment has no Android emulator, ADB, aapt/apkanalyzer or screenshot renderer. Physical Mi TV Stick acceptance remains NOT RUN. No ADB, installation, launcher setting mutation, or merge.
- **Status: AUTOMATED QA PASS; VISUAL / DEVICE ACCEPTANCE PENDING; OVERALL BELUM FINAL.** Keep PR #101 draft/unmerged. Next gate is a genuine rendered 1920×1080 visual inspection; only after it passes may owner-approved Mi TV Stick acceptance proceed. No installation or merge without explicit owner authorization.

### Handover checkpoint — 9 November 2026
Resume PR #101 from source HEAD `b8389e1f90b9fb7f65d91cfd0d7a41782f343d83` on `integration/home-islamic-prayer-spacing-cumulative-101`; base SHA `4038c08f4825f7bacf965bc6708505d3c8c30ebc`. PR remains OPEN / DRAFT / UNMERGED.

Automated QA is complete: CI #1681 / run `42535212323` SUCCESS; Python 211 tests (1 skipped), Android app/installer unit tests and Release build PASS. Artifact `13639167072` contains `app-release.apk`; size 1,626,525 bytes; SHA-256 `5423f7746d6b771fcb0c76ca3fcdca5186c5957d2967f66437554cb6e7d62398`; package ZIP integrity PASS. Size warning is non-blocking and delta vs PR #83 is only +1,312 bytes (+0.081%).

Do NOT claim visual pass: no rendered 1920×1080 screenshot exists. Current execution environment lacks Android emulator/renderer. Next required work: arrange genuine rendering/visual audit, then explicit owner-authorized Mi TV Stick acceptance. No ADB/install/default-launcher changes or merge before those gates and explicit approval.

## Authoritative handover — single-door restart — 9 November 2026

**Resume instruction:** first read `docs/project-management/GTVH-CHECKPOINT-LATEST.md` from branch `docs/project-master-inventory`. Do not resume from chat summaries or older PR notes.

**Current implementation work:** PR #103, OPEN / DRAFT / UNMERGED, branch `fix/home-wallpaper-prayer-spacing-arabic-rtl-102`, HEAD `a3d697c793e23a59f9f9953e2074d83daa210cf9`, base PR #101 HEAD `08b521cadb8c913b49fce42acb4e536e3eb7e9ea`. Exact-head CI #1689 PASS. Rendered 1920×1080 visual review and physical Mi TV Stick acceptance remain pending. Do not install, alter launcher settings, or merge without explicit owner authorization.

**Next sequence:** (1) complete this six-document sync and verify its commit; (2) preserve PR #103 as Draft/unmerged; (3) arrange genuine visual review for PR #103; (4) after visual pass, request owner-authorized device acceptance; (5) only after those gates, update checkpoint and decide next integration step.

**Upcoming workstream:** GTVH-GLOBAL-FROSTED-SILVER-GLASS-001 is approved in concept and prompt only. It has not started. Do not create source changes until the existing checkpoint gates and dependency status are reviewed and a complete component inventory/baseline is ready.

**Safety:** preserve all earlier approved functionality and component-specific geometry. No blind cherry-picks, rebases, branch resets, force pushes, merges, device installs, or launcher-setting changes.


## Current handover — GTVH-GLOBAL-FROSTED-SILVER-GLASS-001 — 9 November 2026
Resume from docs/project-management/GTVH-CHECKPOINT-LATEST.md only. Active PR #104 is OPEN / DRAFT / UNMERGED on feature/global-frosted-silver-glass-001; current HEAD b737b617aa53fbfc112ccdfe8a7cd4959043d3ec; base is PR #103 branch fix/home-wallpaper-prayer-spacing-arabic-rtl-102 at a3d697c793e23a59f9f9953e2074d83daa210cf9. Exact-head CI #1758 / 42605960427 PASS (235 Python tests passed, 1 skipped; Android unit tests and release build successful; APK diagnostic 1,628,165 bytes). Recovery fixes and evidence are recorded in GTVH-GLOBAL-FROSTED-SILVER-GLASS-001-AUDIT.md. Continue with genuine 1920×1080 visual review, full function/DPAD regression, performance/GPU profiling, then owner-authorized Mi TV Stick acceptance. No install, ADB, default-launcher setting change or merge without explicit owner approval. Keep RE-SKIN NOT REWIRE and geometry-stable focus; selected/active state remains distinct from cyan focus. Overall workstream is BELUM FINAL until all acceptance gates close.
