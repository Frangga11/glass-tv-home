<!-- CURRENT AUTHORITATIVE INVENTORY UPDATE — 9 NOVEMBER 2026. Supersedes earlier current inventory status below. -->
## Active Global Visual Workstream — Inventory Update

| Item | Current value |
|---|---|
| Workstream | GTVH-GLOBAL-FROSTED-SILVER-GLASS-001 |
| PR | #104 — OPEN / DRAFT / UNMERGED |
| Branch | feature/global-frosted-silver-glass-001 |
| Current exact HEAD | e6712777ca3ecd5c3e1714c5e2725aa9c5cddc3e |
| Last source-code CI PASS | #1703 — source HEAD 03408e7585e1cacd3fe32727502bca61752c5438 |
| Base PR / exact SHA | #103 / a3d697c793e23a59f9f9953e2074d83daa210cf9 |
| Commits / changed files | 69 / 50 |
| Automated tests | 223 Python PASS, 1 skipped; Android build/unit tests PASS |
| APK | 1,627,069 bytes |
| APK SHA256 | e146f9e80c333acbb72c34bb64f96b2be385583e60400239ab04b5c6f5474671 |
| Device installation | Not performed; hold until G1/G4 complete and owner authorization |

Baseline at PR #103: 17 app layouts; 121 app drawable resources; 19 app values XML; 100 app Java source files; 59 app Java unit-test files; 35 Python regression-test files.

Audited surface families: Home shell/header/navigation, location/elevation, weather/AQI, wallpaper/hero, Islamic shelf, prayer/selected Ayah, app dock/tiles, recent apps/media, Wi-Fi, Quick Settings, Global Settings, profile/avatar/Child Mode, wallpaper/location, modal surfaces, weather dashboard, floating Azan card, shared renderers/tokens and companion installer.

Initial changes include shared palette/colors, AppAdapter, Glider, GlassView, GlassRim, MainActivity dynamic dialogs, ModernSettingsDialog, QuickSettingsDialog, LocationActivity, AzanActivity, Home/dock/dialog/location/prayer/media/recent-app/tile/weather drawables, installer glass resources, regression tests and CI artifact allowlist. Full stale-resource sweep is still in progress.

---

<!-- CURRENT AUTHORITATIVE INVENTORY UPDATE — 9 NOVEMBER 2026. Supersedes earlier current inventory status below. -->
## Active Global Visual Workstream — Inventory Update

| Item | Current value |
|---|---|
| Workstream | GTVH-GLOBAL-FROSTED-SILVER-GLASS-001 |
| PR | #104 — OPEN / DRAFT / UNMERGED |
| Branch | feature/global-frosted-silver-glass-001 |
| Current exact HEAD | 03408e7585e1cacd3fe32727502bca61752c5438 |
| Last source-code validated HEAD | 7535a311639c07ab77abd004664510fca216b00b |
| Base PR / exact SHA | #103 / a3d697c793e23a59f9f9953e2074d83daa210cf9 |
| Commits / changed files | 68 / 50 |
| CI #1702 | PASS — source HEAD 7535a311 |
| CI #1703 | IN PROGRESS — docs-only HEAD 03408e7 |
| APK | 1,627,069 bytes |
| APK SHA256 | e5e5dfbcff4a2944c4bc8d3be38deb27743b1b358b542bf82a6a0effab530054 |
| Merge / device install | Not performed |

### Baseline inventory at PR #103
17 app layouts; 121 app drawable resources; 19 app values XML; 100 app Java source files; 59 app Java unit-test files; 35 Python regression-test files.

### Audited visual surface families
Home shell/header/navigation, location/elevation, weather/AQI, wallpaper/hero, Islamic shelf, prayer and selected Ayah; app dock/tiles, recent apps and media; Wi-Fi, Quick Settings, Global Settings, profile/avatar/Child Mode, wallpaper/location and modal surfaces; weather dashboard/cards/rows; floating Azan card; shared renderers/tokens; companion installer glass surfaces.

### Initial changed source/resource inventory
Shared palette/colors; AppAdapter focus behavior; Glider and GlassView focus rendering; GlassRim; MainActivity dynamic dialogs; ModernSettingsDialog; QuickSettingsDialog; LocationActivity; AzanActivity; Home/dock/dialog/location/prayer/media/recent-app/tile/weather drawable resources; installer glass resources/theme accents; regression tests; CI APK artifact allowlist; audit/design report.

Full component and changed-file evidence is on PR #104 and `GTVH-GLOBAL-FROSTED-SILVER-GLASS-001-AUDIT.md`. Remaining source/resource sweep is not complete; do not infer full visual acceptance from the inventory count.

---

<!-- CURRENT AUTHORITATIVE INVENTORY UPDATE — 9 NOVEMBER 2026, 17:01 WIB. Supersedes earlier current inventory status below. -->
## Active Global Visual Workstream — Inventory Update

| Item | Current value |
|---|---|
| Workstream | GTVH-GLOBAL-FROSTED-SILVER-GLASS-001 |
| PR | #104 — OPEN / DRAFT / UNMERGED |
| Branch | feature/global-frosted-silver-glass-001 |
| Exact HEAD | b34d785dced9b5e47e65adf9865b1f4deada532d |
| Base PR | #103 |
| Base branch | fix/home-wallpaper-prayer-spacing-arabic-rtl-102 |
| Exact base SHA | a3d697c793e23a59f9f9953e2074d83daa210cf9 |
| Commits / changed files | 51 / 40 |
| Exact-head CI | #1691 / 42599100302 — IN PROGRESS |
| Merge / device install | Not performed |

### Baseline file inventory at PR #103
17 app layouts; 121 app drawable resources; 19 app values XML; 100 app Java source files; 59 app Java unit-test files; 35 Python regression-test files.

### Audited visual surface families
- Home shell/header/navigation, location/elevation, weather/AQI, wallpaper/hero, Islamic shelf, prayer and selected Ayah.
- Application dock, app tiles, recent apps and media controls.
- Wi-Fi, Quick Settings, Global Settings, profile/avatar/Child Mode, wallpaper, location and other modal/popup surfaces.
- Weather dashboard/cards/rows, prayer focus treatment and floating Azan card.
- Shared renderers/tokens: TvUiTokens, GlassDrawable, GlassRim, Backdrop, Glider, GlassView, TvFocusIndicator, TvDialogStyle.
- Companion installer glass cards, buttons and input surface.

### Initial changed source/resource inventory
Shared palette and colors; AppAdapter focus behavior; Glider and GlassView focus rendering; GlassRim; LocationActivity; AzanActivity; Home/dock, dialog, location, prayer, media, recent-app, tile and weather shared drawables; installer glass resources/theme accents; regression test; CI APK artifact allowlist; audit/design report.

Full component and changed-file evidence is on PR #104 and `GTVH-GLOBAL-FROSTED-SILVER-GLASS-001-AUDIT.md`. Remaining full source/resource stale-style sweep is not yet complete. Do not infer that every resource has passed visual review.

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

# CURRENT PR INVENTORY UPDATE — CUMULATIVE HOME REFINEMENT — 9 NOVEMBER 2026

| PR | Workstream | Base / head | Status | Notes |
|---|---|---|---|---|
| #100 | GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-002 | Base PR #89 HEAD `4038c08f4825f7bacf965bc6708505d3c8c30ebc`; HEAD `2de055a3b08fa9a0e5c4d2bb86d2884dbd71f43b` | OPEN / DRAFT / UNMERGED | Cumulative PR #84–#89 plus re-applied PR #99 geometry; 11 files; CI #1677 PASS; APK artifact #13638675606 verified; visual audit pending |
| #99 | GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-001 | Base PR #83 HEAD `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`; HEAD `443d8491109c416915955c8a9ceb202860916a6b` | OPEN / DRAFT / UNMERGED | Isolated source candidate; do not use its APK as cumulative Home build |
| #84–#89 | Islamic shelf / prayer / daily Ayah sequential stack | #84 → #85 → #86 → #87 → #88 → #89; final #89 HEAD `4038c08f4825f7bacf965bc6708505d3c8c30ebc` | OPEN / DRAFT / UNMERGED | All six source PRs are inherited by PR #100 |

**Current exact-head validation:** CI #1677 / run `42533185178` passed both jobs; Python 208 tests (1 skipped); Android unit tests and release build passed. APK 1,626,857 bytes; artifact ZIP SHA-256 `e1428796d54847cd7e33c5c761b7339ad76c6eea004b8e26c39f6499d9502521`; extracted APK SHA-256 `b7ae8aa3919e0818b0879b8996427ea6eab9b91f14bf0fcff84b80fad32f292a`. Dedicated audit: [GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-002-AUDIT.md](GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-002-AUDIT.md).

No merge, ADB, TV installation or device-setting change. Rendered 1920×1080 visual review remains pending.

---

# CURRENT CHECKPOINT — GTVH-HOME-SCREEN-CROSS-AUDIT-001 — 8 NOVEMBER 2026

## Current source and validation
- Repository: `Frangga11/glass-tv-home`; branch: `fix/home-balanced-frame-001`.
- Exact audited HEAD: `e9fdecdb4dd44eefeed1464fddaf8edd2e2ec5e0`.
- PR #79: OPEN / DRAFT / UNMERGED; stacked PR #77 remains unmerged.
- CI #1560 / run `42484233880`: **SUCCESS** on exact HEAD; both jobs passed, release build successful.
- Release APK size: **1,624,625 bytes**. Validation artifact size: **1,424,041 bytes**; artifact ZIP SHA-256 `5fa3ea60f7e727b8e4ac663e08ea7488564a70b7acb60e4c3a7040fd821b9f86`.
- No APK installation, ADB, device/default-launcher settings change, physical TV test, or PR merge.

## Main Home Screen cross-audit result
- Dedicated report: [GTVH_HOME_SCREEN_CROSS_AUDIT_001.md](GTVH_HOME_SCREEN_CROSS_AUDIT_001.md).
- Overall: **PARTIAL / NOT PASS**.
- Main layout structure: 63 unique IDs, no duplicate IDs; all 52 unique `R.id` references in the audited MainActivity onCreate range exist in activity_main.xml.
- Findings:
  - HOME-001 P2 **FAIL** — Now Playing disappears for paused/buffering media; paused playback cannot be resumed from the Home card.
  - HOME-002 P2 **FAIL (popup entry path)** — prayer detail popup is wired only to hidden `prayerTimes` fallback view; visible timetable has no click/DPAD route.
  - HOME-003 P2 **PARTIAL/RISK** — delayed Dock focus retries lack cancellation generation and may steal focus.
  - HOME-004 P3 **PARTIAL** — Weather focus-scale listener is overwritten by shared Header listener.
  - HOME-005 P3 **FAIL** — Home atmospheric weather overlay refresh has no production entry point; the dynamic wallpaper weather effect can remain absent or stale.
- Clock/date, location, Header tabs, Inputs/Wi-Fi/Assistant, wallpaper/hero, ayah shelf, app inventory and Dock geometry have source-level wiring; device/runtime acceptance remains pending.
- Existing E06 audit is limited to the Home Screen Settings destination, not the full main Home surface.

## Remaining gates
- CI/build: **PASS**.
- Whole Home source audit: **PARTIAL / NOT PASS**.
- Rendered visual, DPAD, lifecycle/resume, weather/prayer refresh, atmospheric overlay refresh, media pause/resume and runtime error-log acceptance: **PENDING**.
- No source-code edits were made during this audit; documentation only.
- Keep PR #79 and PR #77 unmerged.


---

## Historical B04/B05 inventory checkpoint — 1 November 2026

- Source integration head: `89d769689eb566671725c52982e0b73c4f1e8c31` (`integration/b04-b05-reconciled`, PR #15 Draft).
- B04 responsive sizing changes retained; exact-head CI #242 passed. F02 physical viewport check is still pending.
- B05 expanded to native UI plus QR upload experiences: avatar, wallpaper, and Standby upload pages; their user-visible status/error text; weather mood/location controls; profile PIN prompts; power/restart feedback; radar illustration label.
- Resource baseline: 560 string keys across default/ID/EN; no duplicate keys, key-set mismatch, or placeholder mismatch. Test now explicitly rejects duplicate resource names.
- Audit coverage: all 68 launcher Java files and 17 layout XML files scanned. 21 changed Java files / 534 resource references and 17 layouts / 71 resource references; zero missing references. Only fixed weather preview/location values and branding handle remain literal in layouts.
- Integrated CI remains pending (no workflow run/status for exact head); no build or test pass is claimed. API 30/33+ lifecycle and physical F02/F06 acceptance remain open.


## Historical source checkpoint — 1 November 2026

- Integration head: `89d769689eb566671725c52982e0b73c4f1e8c31`; PR #15 is OPEN, DRAFT, and GitHub reports `mergeable_state=clean`. PR #13 and #14 are also OPEN/DRAFT. No merge has occurred.
- Localization static QC: 560 keys in default/ID/EN; zero duplicate keys, locale key differences, or placeholder mismatches. Duplicate-key rejection is now part of `LocalizationResourceParityTest`.
- App-wide scan: 68 Java files and 17 layout XML files; 21 changed Java files with 534 `R.string` refs and 17 layouts with 71 `@string` refs; zero unresolved references.
- B04 exact-head CI #242 passed. There is still no Actions run or combined status for the exact integration head. Do not treat `mergeable_state=clean` as a build/test result.
- Remaining gates are external: integrated CI; API 30/API 33+ runtime lifecycle/focus checks; phone-on-same-Wi-Fi QR upload verification; physical F02/F06 TV acceptance. No APK was published/installed and no TV settings were changed.


## C03 source inventory checkpoint — 1 November 2026

- Feature branch: `feature/c03-phone-tv-network-verification`; Draft PR #20; parent C02 exact head `b0e1ff048e36bf21f8da1e165a427075f85a7cb3`; latest head `d266287df287167e5a8e73fd675b51759b580ed6`.
- Added `launcher/app/src/main/java/com/gathin/tvhome/LocalNetworkAddress.java` — active default Wi-Fi/Ethernet IPv4 resolver.
- Added `launcher/app/src/test/java/com/gathin/tvhome/LocalNetworkAddressTest.java` — address eligibility and prefix regression coverage.
- Modified `WallpaperUploadServer.java`, `AvatarUploadServer.java`, and `MainActivity.java` to use the shared resolver for QR preflight and server binding.
- Added `docs/project-management/C03_PHONE_TV_NETWORK_AUDIT.md` on the feature branch.
- Exact latest branch-head CI #338 / `41287563951`: PASS. No unrelated modules changed; C01 and C02 remain separate.


## C04 source inventory checkpoint — 1 November 2026

- Feature branch/head: `feature/c04-http-endpoint-routing` / `f09ee1b30b80e45e793c1678935eff8867aedae4`; Draft PR #21; parent C03 exact head `d266287df287167e5a8e73fd675b51759b580ed6`.
- Added `launcher/app/src/main/java/com/gathin/tvhome/UploadHttpRequest.java` and JVM test `UploadHttpRequestTest.java`.
- Modified `AvatarUploadServer.java` and `WallpaperUploadServer.java` for strict request parsing, complete-header enforcement, endpoint/method contracts, status/error responses, query parsing, and gallery API semantics.
- Added `AvatarUploadRouteTest.java`; expanded `WallpaperUploadSessionRouteTest.java`.
- Added aligned `wallpaper_page_action_failed` resource in default, Indonesian, and English strings.
- Added `docs/project-management/C04_HTTP_ENDPOINT_AUDIT.md`.
- Exact-head CI #342 / `41288521501`: PASS. APK size 1,547,845 bytes (<1.5 MiB cap); publication skipped.


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



## E00 current inventory reconciliation — 2 November 2026

**Authoritative current snapshot:** see [E00_MASTER_INVENTORY_AND_DEPENDENCY_AUDIT.md](E00_MASTER_INVENTORY_AND_DEPENDENCY_AUDIT.md). This dated section supersedes older status snapshots below where they conflict; earlier entries remain as historical evidence and are not deleted.

- Current remote branch listing: 36 branch names. Branch existence alone does not establish merge/readiness/currentness.
- Verified PR state: #10–#12 and #16–#31 OPEN/DRAFT/UNMERGED; #13–#15 retain their earlier B04/B05 reconciled merge history. D-013 applies to B06 onward and grouped integration.
- B01–B07, C01–C07, D01–D07 source/CI work is recorded complete; their final integrated and physical-device acceptance remains pending in F. C01–C07 are PR #18–#24; D01–D07 are PR #25–#31.
- D07 baseline: `feature/d07-concurrent-profile-content-state` @ `964163c918e281b68af5cf7114ca56ead2797486`; PR #31 open/Draft/unmerged; exact-final-head CI #404 / `41313690556` PASS.
- D07 resource parity spot-check: 569 unique keys each in default, Indonesian and English sets; zero duplicates, key-set differences or printf-placeholder mismatches. Runtime localization acceptance remains in F.
- Latest D07 APK diagnostic: 1,552,197 bytes / 1,572,864-byte cap; 20,667 bytes headroom. No APK published or installed.
- No application source, device settings, user data, or feature PR was changed by E00.


## E00 official closure — 2 November 2026

- E00 documentation PR #32 was merged into `docs/project-master-inventory` (merge commit `4ba80230cb9ddfbcc27657b7b0d07bed6905fc97`).
- Exact PR head `541dd9accb66eb3b071367a3e4967769d742c2cd`; CI workflow run #414 / `41359874778`: PASS. Both `Launcher build and unit tests` and `Lists, tools and docs` jobs completed successfully. No review comments or change requests were present.
- Canonical E00 report, roadmap, inventory, audit register, operations log, master project and handover were re-read from `docs/project-master-inventory` after merge.
- E00 scope is closed: documentation/inventory/dependency reconciliation only. No application source, feature PR, APK, user data, TV setting, installation or physical-device acceptance was changed or claimed.
- **Next work unit: E01 — Device Settings & Launcher audit.** Begin with exact dependency heads and the A06 read-only HOME resolver/preferred-activity discrepancy. Preserve D-013/D-014; do not merge feature PRs early.

## E01 source inventory checkpoint — 2 November 2026

- Branch: feature/e01-device-settings-home-control; Draft PR #34; base D07 exact head 964163c918e281b68af5cf7114ca56ead2797486; PR remains OPEN/DRAFT/UNMERGED.
- Exact source head validated: 9919dbaa0add6cc7ce2a84332354f46f9cc48d61. E01 audit documentation follow-up: 7aad9ec4cf015dd415b3c948e2b4f80c1099955c.
- Added DeviceSettingsStatus.java for read-only HOME candidate/resolver and accessibility/notification status. Removed silent startup writes to screen/sleep/screensaver/accessibility/notification settings and removed WRITE_SECURE_SETTINGS from the manifest.
- Updated ModernSettingsDialog.java and MainActivity.java for HOME status/candidates, user-managed service settings, separate Android screensaver and display-timeout entries, shared B01 popup contract, restart/power feedback, and cache result feedback. HomeKeyService restart no longer misuses accessibility global-action ID 12.
- Added TvDialogStyle.java and TvUiTokens.java matching B01 canonical blobs exactly; added TvDialogStyleTest.java and CacheCleanerTest.java.
- CacheCleaner now requires the explicit pm trim-caches success marker; ADB stream closure alone is not treated as success.
- QR target on main was checked: release/glass-tv-installer.apk, release/glass-tv-home.apk and release/version.json are present; version metadata reports 4.0.0.
- Exact-head CI #602 / 41400508774 PASS. Diagnostic APK 1,560,565 bytes; 12,299 bytes below the 1,572,864-byte cap. Artifact count zero; manual TV APK publication skipped.
- F-stage acceptance remains open: HOME resolver/preferred-activities discrepancy and physical HOME key; service state readback; OEM power labels; actual ADB trim success/failure; phone installer end-to-end; D-pad popup/Settings visual acceptance; ClockDream/StandBy/display timeout behavior. No APK installed and no TV setting changed.

## E01 final cache-feedback accuracy correction — 2 November 2026

- This later checkpoint supersedes the earlier E01 source head and APK-size figures in this document.
- Final E01 source head: b6fa70e406afc9f6e81d16a3635a69afc6c4d783; exact-head CI #609 / 41401277076 PASS.
- Final diagnostic APK: 1,560,609 bytes; 12,255 bytes below the 1,572,864-byte cap. No artifact published; manual TV-test APK step skipped.
- Cache byte totals now count only successfully deleted app-cache/temp files; free-space deltas are no longer attributed to app-cache cleanup. User-facing no-files-removed feedback is neutral. Android system trim success requires the explicit ADB success marker. Regression tests cover marker parsing and actual deleted-byte counting.
- E01 audit report and PR #34 description updated. PR #34 remains OPEN/DRAFT/UNMERGED under D-013; latest feature branch documentation commit 090a956a9860c82eb36c2d96848d47046fba0f0c.
- E01 source audit remains COMPLETE; next Stage E task is E02 Profile. F-stage physical/OEM checks remain mandatory and unchanged. No device settings, HOME default, package state, user data or installed APK changed.


## E05 Widgets & Information Center — 2 Nov 2026

- E05 started on isolated branch `feature/e05-information-center-ayah`, based on E04 exact head `08076bb945a169de198905152a092f419f48aa82`; Draft PR #38 targets `feature/e04-standby-screensaver` and remains OPEN/DRAFT/UNMERGED under D-013.
- Source audit found E05-F01: Home Arabic ayah was forced to one line and translation to two lines, both ellipsized in XML and again at runtime. Repaired on the feature branch: expanded the content width, enabled full natural multiline wrapping, removed ellipsis/finite line caps, applied RTL/high-quality line breaking, retained Arabic font padding for Quranic marks, and improved TV-distance font sizing.
- Audited WeeklyAyah caching and ID/EN/MS translation selection; PrayerTimes location-based WIB/WITA/WIT timezone and calculation fallback; HomeWeather 15-minute refresh, Open-Meteo weather/modelled US AQI and D07 stale-response rejection; Information Center and its four focusable Quick Actions. Existing data/state/network contracts were preserved.
- Audit report: `E05_WIDGETS_INFORMATION_AUDIT.md` on feature branch. Exact PR head `60086e0a2d74b40e124f11caa219a683863e6e08`; Actions CI run #41422241456 is QUEUED at checkpoint. Do not claim CI PASS until both jobs complete successfully.
- Mi TV Stick 1920x1080 long-verse glyph/translation visibility, shelf/dock collision, 4K layout, remote focus/scroll, offline/language refresh and runtime performance remain Project F acceptance items; deferred, not waived. No APK published/installed, no device test or merge.

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


## B03 global surface fallback — 2 Nov 2026
- B03 branch `feature/b03-dialog-opacity-layering`, PR #12 Draft/open/unmerged; current source head `8cf0a846f3a7738cc1bb20f663dac9cf961e98da`.
- Added shared opaque `POPUP_SURFACE` / `DOCK_SURFACE` tokens, solid OFF-state backgrounds for GlassDrawable and DockDecoration, and OFF-state solid modal background selection in TvDialogStyle. Added `GlassSurfaceFallbackTest` JVM regression test.
- Exact-head CI #685 / `41447473358` pending at documentation checkpoint.
- E07 branch PR #40 has matching toggle invalidation integration and exact-head CI #682 / `41446777187` PASS. E07 exact source head: `7ecb45a634e6ed9792fd87f3110ffe0b6eaad1a2`.
- B03 owns global material contract; E07 owns setting integration; G owns final shared-file reconciliation; F owns 1080p/4K device acceptance. No merge, APK publication/installation or physical TV test.
- Report: [B03_DIALOG_OPACITY_AUDIT.md](B03_DIALOG_OPACITY_AUDIT.md).


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


## E08 source/CI closure checkpoint — 2 Nov 2026
- E08 branch: `feature/e08-wallpaper-settings-redesign`; Draft PR #41 targets E07 exact parent head `a0f2b0fff6eef0c42852d21b971904d5acd29d5c`; PR remains OPEN/DRAFT/UNMERGED under D-013.
- Source/test head: `b386986f08951c4dcf12ba8d72638b091a264803`; exact-head CI #693 / `41469544565`: **SUCCESS**. Launcher build/unit tests, release build, APK-size guard, and Lists/tools/docs all passed.
- Release APK diagnostic: 1,565,397 bytes; 1,572,864-byte cap; 7,467 bytes headroom. APK publication skipped; no installation/device test claimed.
- Unified wallpaper control center now has three tabs: Change Wallpaper, Shuffle Interval, Next Wallpaper. Appearance and Quick Control route to the same entry point. Existing category selection, custom manager/QR upload, shuffle-cycle preferences, interval timer, asynchronous decoder, metadata and rotation engine are reused; no duplicate wallpaper engine or preference schema was introduced.
- Shuffle toggle preserves the currently displayed image; Next handles single-image collections explicitly; interval zero is accurately described as manual-only/no automatic change. Existing QR, deletion, slideshow, cache/provider and StandBy paths remain owned by their existing projects.
- Detailed report: [E08_WALLPAPER_SETTINGS_REDESIGN_AUDIT.md](E08_WALLPAPER_SETTINGS_REDESIGN_AUDIT.md).
- Project F still must verify D-pad/Back/focus, Glass ON/OFF, 1080p/4K, category/online/offline, shuffle cycle, interval/manual-only, Next single/multi-image, process lifecycle, QR upload and Wallpaper/StandBy storage isolation. No physical acceptance is claimed.
- E08 source/CI: PASS. Grouped integration and merge remain deferred under D-013; no release approval or merge was performed.


## E08 final exact-head verification — 2 Nov 2026
- This checkpoint supersedes the earlier E08 source/test head and APK figures above.
- Final source/test head: `dcec6afc805a9d7c78702ad8d51b20b87bd751a3`; Draft PR #41 remains OPEN/DRAFT/UNMERGED and targets E07 exact parent `a0f2b0fff6eef0c42852d21b971904d5acd29d5c` under D-013.
- Exact-head GitHub Actions CI #695 / `41470304782`: **SUCCESS**. Launcher build/unit tests, `testReleaseUnitTest`, `assembleRelease`, APK-size diagnostic/guard, and Lists/tools/docs all passed.
- Final APK diagnostic: 1,565,417 bytes; 1,572,864-byte cap; 7,447 bytes headroom. Publication skipped; no installation.
- Final D-pad refinement preserves focus on the Shuffle control after its status panel refreshes. No wallpaper engine, preference schema, QR route or storage ownership changed.
- Current feature branch head `086ad1d112b346a281c88273fa620e9348d2cccc` is a documentation-only audit follow-up after the verified source/test head; no app source/resources/tests/build inputs changed.
- Project F physical acceptance remains mandatory: D-pad/focus/Back, Glass ON/OFF, 1080p/4K, category and online/offline paths, shuffle cycle, interval/manual-only, Next one/multi-image, process lifecycle, QR upload and Wallpaper/StandBy storage isolation. No physical acceptance is claimed.
- E08 source/CI: PASS. D-013 grouped integration/merge remains deferred pending F/G and explicit user approval.


## E08 compact popup/UI consistency checkpoint — 2 Nov 2026
- E08 branch final source/test head: `fcd7d8f23a4833830a401652ef269db3282b7300`; Draft PR #41 remains OPEN/DRAFT/UNMERGED, targeting E07, under D-013.
- UI audit found bespoke styling in the new control center and older styling in category/interval/custom-photo subdialogs. These are now aligned with B01 `TvUiTokens` and B01/B03 shared `TvDialogStyle` material/viewport contract; no global token ownership change was made.
- Main control center is compact (max 680×420 dp), with short Change/Shuffle/Next tabs and concise actions. Category/interval pickers use compact single-line card rows with common selected/focus states and no redundant explanatory copy. Photo manager/confirmation/action dialogs use the same modal material and viewport sizing contract.
- Function wiring remains unchanged: category selector, shuffle/interval preferences and Next reuse the existing wallpaper engine, keys, timer, QR upload and storage owners. D-pad focus is retained after Shuffle refresh; no new engine/schema/permission/network path.
- Exact-head CI #702 / `41473357877`: **SUCCESS**. Launcher build/unit tests/release/APK-size guard and Lists/tools/docs passed. APK 1,562,709 bytes; 1,572,864-byte cap; 10,155 bytes headroom. Publication skipped.
- Detailed report: [E08_WALLPAPER_SETTINGS_REDESIGN_AUDIT.md](E08_WALLPAPER_SETTINGS_REDESIGN_AUDIT.md).
- Project F still must verify the compact modal on 1080p/4K, D-pad/focus/Back, Glass ON/OFF, category selection, shuffle/interval/Next, QR photo manager and lifecycle persistence. No physical device acceptance or APK installation is claimed.
- E08 source/UI/CI: PASS. Project F physical acceptance and D-013 grouped integration/merge remain pending.


## Project E closure / Project F handover — 2 Nov 2026
- E01–E08 feature development and module-level audit are closed by user approval. Any further E changes require a verified Project F integration defect or explicit scope reopening.
- PR chain #34–#41 remains OPEN/DRAFT/UNMERGED under D-013. Latest observed heads: #34 `090a956a9860c82eb36c2d96848d47046fba0f0c`; #35 `3ab9b55f3cfe03ad9841a119590a9183cf114e96`; #36 `e085f8aed6fdc85b4793b76bb26ab8543b26b2cf`; #37 `08076bb945a169de198905152a092f419f48aa82`; #38 `6fe89fad078692958db67030329a6de66cd7ae71`; #39 `570b7c83efaec3ac515b78f1c6c119b777fc3b84`; #40 `a0f2b0fff6eef0c42852d21b971904d5acd29d5c`; #41 `8dfdcabd19853a1140724ea68983877af17bed0c`.
- E08 exact source/test head `fcd7d8f23a4833830a401652ef269db3282b7300`; CI #702 / `41473357877` PASS; APK 1,562,709 bytes / 1,572,864 cap / 10,155 bytes headroom; publication skipped. PR #41 latest head is documentation-only after that source/test head.
- Project F must validate the complete integrated chain, shared UI/popup contract, D-pad/focus/Back, Glass ON/OFF, 1080p/4K, all module interactions, lifecycle/persistence and wallpaper management versus Quick Control distinction. No physical acceptance claimed yet.
- Do not merge or install APK during closure. G owns grouped final reconciliation/package; merge only after full gates and explicit approval.


## Project F01 — integration/dependency audit checkpoint (2 Nov 2026)
- Status: IN PROGRESS; source-diff/dependency mapping completed, combined integration build and runtime validation not yet executed.
- Audited B01–B03 (#10–#12), C01–C07 (#18–#24), D01–D07 (#25–#31), and E01–E08 (#34–#41) diffs. Shared-file hotspots and ownership contracts are recorded in `F01_INTEGRATION_DEPENDENCY_AUDIT.md`.
- Exact-head CI evidence checked: E01 #609, E02 #623, E03 #626, E04 #629, E05 #663, E06 #668, E07 #688, E08 #702 — all PASS on the recorded source/test SHAs. B01/B02/B03, C07, D05 and D07 checkpoints also sampled as PASS.
- PRs remain Draft/open/unmerged under D-013. No APK published/installed and no physical acceptance claimed.


## F01 expanded dependency audit — 2 Nov 2026
- Coverage expanded to B01–B07, C01–C07, D01–D07 and E01–E08. B04/B05/PR15 already merged before D-013; B-series integration CI #254/#257 PASS. B06/B07 remain Draft/open/unmerged.
- Current source/test CI checks across B/C/D/E branches are PASS; detailed run numbers and exact branch heads are in `F01_INTEGRATION_DEPENDENCY_AUDIT.md`.
- Parent/base drift register: PR #11/#12/#17/#18/#19/#21/#24/#29/#34. All currently mergeable, but newer parent changes require preservation and integrated regression during grouped integration.
- Isolated branch `integration/f01-rehearsal-only`, Draft PR #42 against current D07, is mergeable. CI #708 PASS; #709/#710 pending at this checkpoint. No merge, APK publication/installation or physical acceptance.


## Project F01 final inventory — 2 Nov 2026
- Status: **CLOSED — PASS** for source/dependency audit and isolated combined build/test.
- Complete B01–B07/C01–C07/D01–D07/E01–E08 dependency map, shared-file hotspots and stale-parent register are in `F01_INTEGRATION_DEPENDENCY_AUDIT.md`.
- Verified/fixed E08 wallpaper entry-point mismatch. Quick Control remains compact; Appearance restores full Collection/Interval/Personal Wallpaper Manager cards. E08 head `1d6fba799012ff0e8dbbfbce5a7fbe4f193e1d0d`, CI #711/#712 PASS.
- Final combined rehearsal PR #43 CI #716 PASS on merge candidate `21c2eab34a3356d37d079eba04b281cc2373386d`; APK 1,563,769 bytes, 9,095 bytes below cap; publication skipped.
- PR #42/#43 closed without merge; PR #41 remains Draft/open/unmerged. No APK install or physical acceptance. D-013 unchanged.
- Next workstream: **F02** integrated functional regression and controlled Android TV acceptance.


## Project F staged integration inventory — 3 Nov 2026
- Governing F/G rule: re-verify audit/dependencies and exact source commits; combine in stages on integration branches; rerun only changed or unproven tests; finalize with evidence and residual boundaries.
- B-series integration branch: integration/b-series, HEAD 9070894fa6d9ef8aec034f46dfe777e866709dfe; CI-only PR #48 synthetic merge b25ab78c62493f6e74676b2e321fb7cc39f23449, run #741 PASS. APK size 1506 KiB, below 1536 KiB cap; publication skipped.
- Full D07+E08 candidate: integration/project-f-staged-full, HEAD 8fe90a9fdd493c5d81ffdacbe16054630a26580a; tree diff against F01 tested candidate 21c2eab34a3356d37d079eba04b281cc2373386d is empty; CI #716 PASS remains applicable.
- Temporary probes #44–#49 closed without merge. Feature PRs remain subject to D-013; no APK published/installed and no physical TV acceptance.
- Next: F02 controlled functional regression and Android TV acceptance. The stale parent/base register remains a G-stage reconciliation item.


## F02 initial regression inventory — 3 Nov 2026
- Status: OPEN; entry audit PASS, physical acceptance NOT RUN.
- Selected candidate `integration/project-f-staged-full` @ `8fe90a9fdd493c5d81ffdacbe16054630a26580a`; exact tree unchanged from F01 tested candidate `21c2eab34a3356d37d079eba04b281cc2373386d` (3 commits ahead, no file changes).
- Reused exact-tree CI #716: both launcher/build/test and Lists/tools/docs jobs PASS; APK publication skipped.
- Full candidate retains B03 fallback/dialog policy, B06 feedback components, SettingsFocusPath test coverage and C07 request-deadline/session protection. B-series branch remains a distinct checkpoint; no blind merge/overwrite.
- Device matrix remains pending: remote/focus/Back; popup/dialog and Glass ON/OFF; profile/security; Weather/AQI; QR upload; wallpaper Quick Control vs Appearance; StandBy; process death/reboot; 1080p/4K.
- No device APK published/installed; no PR/main/release merge. D-013 active. See `F02_INTEGRATED_FUNCTIONAL_REGRESSION.md`.


## Project F02 source and validation inventory — 3 November 2026

| Area | Current source / evidence | Status |
|---|---|---|
| F02 validation branch | `integration/project-f02-system-validation`, PR #50 Draft; checkpoint `1658237bafdb24167391270f3ad8f694751a6c2a` | IN PROGRESS |
| F02 test policy | `F02_INTEGRATED_SYSTEM_VALIDATION.md` | ACTIVE; physical tests deferred to G |
| Findings register | `F02_FINDINGS_REGISTER.md` | OPEN / RETEST PENDING |
| B01 modal owner | `feature/b01-global-navigation-retention`, PR #10; latest test checkpoint `b1c23024d0de740c36e1794ab775fdc43c7a3a20` | CI IN PROGRESS |
| D05 modal regression | `feature/d05-standby-photo-atomic-persistence`, PR #29; latest checkpoint `7f043943ab0c1d2d1217fc56845d81ac7583c96d` | CI IN PROGRESS |
| D06 Azan export boundary | `feature/d06-process-death-recovery`, PR #30; latest checkpoint `1bb6ce9bf04d72f3fe33b4e1630af561e99fe0f7` | CI IN PROGRESS |
| D01 broadcast authorization | `fix/d01-location-broadcast-authorization`, PR #51; head `bbb13dd107c86cda85bd1482c2f050ca69dfc4e2` | CI PASS; ADB compatibility DEFERRED_TO_G |
| Regression tests | `tests/test_dialog_modal_contract.py`, `tests/test_main_activity_modal_contract.py`, `tests/test_location_broadcast_manifest_security.py`, `tests/test_prayer_component_manifest_security.py` | ADDED; exact-head CI gates apply |
| CI workflow | `.github/workflows/ci.yml` | Release compilation validation-only; no APK publication |

No branch was merged. Source branches remain preserved. No physical Android TV test or APK installation/publication was performed.


## Latest Project F02 inventory — 3 November 2026

| Item | Branch / head / PR | Status |
|---|---|---|
| F02 integrated validation | `integration/project-f02-system-validation` / `eea920469ffe5bef5ddb14e1dfd66687e82d8fed` / PR #50 | CI IN PROGRESS |
| B01 global modal + Home-key routing | `feature/b01-global-navigation-retention` / `867c7a9d0977e21b69c8078a400458b00ba2d2dd` / PR #10 | CI IN PROGRESS |
| D05 Settings modal repair | `feature/d05-standby-photo-atomic-persistence` / `7f043943ab0c1d2d1217fc56845d81ac7583c96d` / PR #29 | CI PASS |
| D06 AzanActivity export boundary | `feature/d06-process-death-recovery` / `1bb6ce9bf04d72f3fe33b4e1630af561e99fe0f7` / PR #30 | CI PASS |
| D01 receiver + LocationActivity security | `fix/d01-location-broadcast-authorization` / `66352838e6155a42a9ea61477643a238f360434f` / PR #51 | CI PASS |
| Host-side UI tests | `TvDialogStyleRobolectricTest.java`, `HomeKeyPressTrackerTest.java` | Robolectric/window tests passed at previous F02 head; latest retest pending |
| Source regression guards | `tests/test_dialog_modal_contract.py`, `tests/test_main_activity_modal_contract.py`, `tests/test_home_key_recent_apps_contract.py`, `tests/test_location_broadcast_manifest_security.py`, `tests/test_prayer_component_manifest_security.py` | Added; latest F02 CI pending |
| Open residual review | `.RecentAppsActivity` exported ACTION_MAIN filter | P3 external caller contract verification deferred to G |

No merge or device test has been performed.


## F02 latest follow-up inventory — 3 November 2026

| Item | Branch / head / PR | Status |
|---|---|---|
| B01 global navigation/security fixes | `feature/b01-global-navigation-retention` / `a1e743ad88c28ca3a9602dfc9d0b5da16e46285d` / PR #10 | CI IN PROGRESS |
| F02 integrated system validation | `integration/project-f02-system-validation` / `3ea479d689dc732fe46594a0677d02b7762d7527` / PR #50 | CI IN PROGRESS |
| Force Stop/Home Handler isolation | `forceStopHandler` separated from `homeHandler`; test `test_home_key_force_stop_handler_isolation.py` | FIX COMMITTED / CI PENDING |
| Recent Apps external-entry hardening | `RecentAppsActivity exported=false`, no intent filter; test `test_recent_apps_manifest_security.py` | FIX COMMITTED / CI PENDING |
| Prior integrated checkpoint | `c9496d4e051980e640bc269ffce695b95ba71ad4` | BOTH CI JOBS PASS; predates latest fixes |

No merge or physical device test performed.


## Exact-head validation snapshot — 3 November 2026

| Branch / PR | Exact head | CI evidence | Status |
|---|---|---|---|
| F02 / #50 | `5dd787ba136dfb070eaa2e833af653e57ec66015` | #41509632784 Launcher PASS; #41509626957 Lists/tools/docs PASS | BOTH PASS |
| B01 / #10 | `791620ce4628ee9e909af7ce2f4758b6df77afeb` | #41509576279 both jobs PASS | BOTH PASS; subsequent comment-only SHA retest pending |
| D05 / #29 | `7f043943ab0c1d2d1217fc56845d81ac7583c96d` | #41505852041 both jobs PASS | PASS |
| D06 / #30 | `1bb6ce9bf04d72f3fe33b4e1630af561e99fe0f7` | #41505865772 both jobs PASS | PASS |
| D01 / #51 | `66352838e6155a42a9ea61477643a238f360434f` | #41506847521 both jobs PASS | PASS |

F02 head `5dd787b` includes the latest integrated callback-isolation, Recent Apps manifest hardening, Home gesture and power-action regression tests. A newer F02 documentation-only checkpoint is queued for exact-head retest.


## C07/F02 session revocation audit — 3 November 2026

| Item | Branch / head / PR | Status |
|---|---|---|
| C07 upload/session owner | `feature/c07-upload-session-resilience` / `12b1f431f7687023727983089ea03eda0f97a73d` / PR #24 | CI IN PROGRESS |
| F02 integration | `integration/project-f02-system-validation` / `081496ca93fd0526025a21ce7cd0e3d7f9c3364a` / PR #50 | CI IN PROGRESS |
| Wallpaper mutation shutdown guard | synchronized select/delete/slideshow/clear handlers with in-lock active-session recheck; HTTP 410 on revoked session | FIX COMMITTED / CI PENDING |
| Regression guard | `tests/test_wallpaper_session_shutdown_atomicity.py` | ADDED / CI PENDING |

Previous F02 checkpoint `5dd787ba136dfb070eaa2e833af653e57ec66015` passed both jobs before the latest C07 fix.


## Current F02/owner branch validation snapshot — 3 November 2026

| Branch / PR | Exact head | CI evidence | Status |
|---|---|---|---|
| F02 / #50 | `b64715156e180edc3bfae25bffba6a089544247f` | New docs-only head; exact-head CI queued after preceding source/test head `ddf130a` passed run #41512382568 (both jobs) | RETEST REQUIRED |
| C07 / #24 | `80a92264c51631fb107c75f32a3cd8e4b8aa9d9a` | #41512386827 both jobs SUCCESS | PASS |
| B01 / #10 | `389e289e939276ce9e20d7e6c82c293d3f83c70f` | #41509724289 SUCCESS | PASS |
| D05 / #29 | `7f043943ab0c1d2d1217fc56845d81ac7583c96d` | #41505852041 SUCCESS | PASS |
| D06 / #30 | `1bb6ce9bf04d72f3fe33b4e1630af561e99fe0f7` | #41505865772 SUCCESS | PASS |
| D01 / #51 | `66352838e6155a42a9ea61477643a238f360434f` | #41506847521 SUCCESS | PASS |

- The 3000 ms failing test expectation was corrected to 3500 ms; no test was weakened or skipped.
- F02's `WallpaperUploadServer` deliberately contains D05's `UploadResult` persistence handling plus C07's later monotonic deadline/session revocation changes. This is an intentional dependency-chain integration, not a source-sync defect.
- All device-dependent checks remain DEFERRED_TO_G.


## Latest F02 and owner-branch validation — 3 November 2026

| Branch / PR | Exact head | CI evidence | Status |
|---|---|---|---|
| F02 / #50 | `558762994dec05a7ae03a924c33a3ed11665c224` | #41513940620 push: both jobs SUCCESS; #41513947375 PR: substantive steps passed, cleanup pending at last poll | PASS ON PUSH / PR CLEANUP |
| E01 / #34 | `edd6461c932705a109ed9768649cae7e3c162c59` | #41513716451 both jobs SUCCESS | PASS |
| C07 / #24 | `80a92264c51631fb107c75f32a3cd8e4b8aa9d9a` | #41512386827 both jobs SUCCESS | PASS |
| B01 / #10 | `389e289e939276ce9e20d7e6c82c293d3f83c70f` | #41509724289 both jobs SUCCESS | PASS |
| D05 / #29 | `7f043943ab0c1d2d1217fc56845d81ac7583c96d` | #41505852041 both jobs SUCCESS | PASS |
| D06 / #30 | `1bb6ce9bf04d72f3fe33b4e1630af561e99fe0f7` | #41505865772 both jobs SUCCESS | PASS |
| D01 / #51 | `66352838e6155a42a9ea61477643a238f360434f` | #41506847521 both jobs SUCCESS | PASS |

- InputKeyService manifest correction was proven by regression: test failed on pre-fix F02 checkpoint `fc3e3fe` because exported was false; after setting exported true while retaining BIND_ACCESSIBILITY_SERVICE, run #41513906117 passed both jobs.
- Latest F02 APK size: 1,564,977 bytes / 1,572,864-byte cap / 7,887-byte headroom. Watchlist only; no current failure.
- All device-dependent checks remain DEFERRED_TO_G.


## 3 November 2026 — latest F02 exact-head verification

- F02 PR #50 latest head: `91ae962bae04914a3888210e5f997b24151d5a62` (findings-register documentation-only commit; no source/test changes after head `5587629`).
- PR workflow #41514480902 completed SUCCESS on this exact head: Lists/tools/docs and Launcher build/unit tests both passed. The launcher log confirms release-validation build success, 35 Python tests including the accessibility-service manifest guard, and APK size 1,564,977 bytes against 1,572,864-byte cap (7,887 bytes headroom).
- Matching push workflow #41514472847 initially failed only because Maven Central returned HTTP 429 while downloading Robolectric `nativeruntime-dist-compat-1.0.16`; this is external dependency rate limiting, not a source/test failure. Only the failed launcher job was re-run (attempt 2); it was still in progress at the time of this note. Lists/tools/docs job passed.
- PR #50 remains Draft/open/unmerged. No APK publication, installation or physical Android TV testing. Device acceptance remains Project G.


## F02 continuation validation — 3 November 2026

| Branch / PR | Exact head | CI evidence | Status |
|---|---|---|---|
| F02 / #50 | c1f212585633899d4c1f5327d3a982a9e136e2b7 | Run #41519813674: Lists/tools/docs SUCCESS; Launcher build/unit tests IN_PROGRESS at last poll | RETEST IN PROGRESS |
| F02 prior test-only checkpoint | 7e18c588374ba90dc4d3d01a608c484dcf194108 | Run #41519394898: both jobs SUCCESS | PASS (pre-storage-fix) |

- New F02-RES-001: concurrent QR uploads could overcommit the free-space snapshot; added synchronized byte reservations and a 64 MiB minimum free-space margin for custom Wallpaper and StandBy upload paths. HTTP 507 indicates insufficient storage; reservations release on all exit paths.
- New tests: JUnit storage reservation boundary cases, Python source-contract tests for both QR upload routes, and stronger upload-shutdown critical-section assertions.
- Production changes are limited to storage-safety enforcement; existing per-file and StandBy collection limits remain unchanged.
- Current F02 findings: QR upload shutdown test hardening and storage reservation remain OPEN until exact-head CI passes. Physical LAN/concurrency/low-storage behavior remains DEFERRED_TO_G.


## F02 upload storage hardening — exact-head retest — 3 November 2026

| Branch / PR | Exact head | CI evidence | Status |
|---|---|---|---|
| F02 source/test / #50 | c1f212585633899d4c1f5327d3a982a9e136e2b7 | #41519813674: both jobs SUCCESS; 42 Python tests; launcher unit tests and release build pass | HOST PASS |
| F02 findings-register docs head / #50 | 1845635091969c2475bb0d26783bc0f7eb58c1c0 | #41520194974 queued at last poll | RETEST PENDING |

- APK size on c1f2125: 1,565,621 bytes / 1,572,864 cap / 7,243 bytes headroom. P3 watchlist; do not remove required fonts/wallpaper without measured optimization.
- QR upload storage reservation and explicit upload-shutdown regression coverage passed host CI. Project G still owns physical concurrent LAN, cancellation, expiry and low-storage acceptance.

## F02 process-wide reservation refinement — 3 November 2026
| Branch / PR | Exact head | CI evidence | Status |
|---|---|---|---|
| F02 / #50 | a95b21becad238dd634dda1e79aa9de36d749d8c | #41520409999 queued at last poll | RETEST REQUIRED |
| Prior per-instance reservation source/test | c1f212585633899d4c1f5327d3a982a9e136e2b7 | #41519813674 both jobs SUCCESS; 42 Python tests; launcher build/unit tests pass | PASS (superseded by process-wide refinement) |

- Reservation accounting now spans all WallpaperUploadServer instances so old workers draining after session stop are included in free-space checks for a replacement session.
- Project G still owns real concurrent LAN, session replacement, cancellation, and low-storage acceptance.

## F02-RES-001 owner/integration verification — 3 November 2026

| Branch / PR | Exact head | CI evidence | APK / artifact status |
|---|---|---|---|
| C07 owner / #24 | 442b78c68b1540394d543bab59bddf52f865ccf0 | #41520866323 both jobs SUCCESS; 18 Python tests; launcher unit tests/build PASS | 1,550,949 bytes; manual publish skipped; no artifacts |
| F02 integration / #50 | 2469c0c1d241a00be3e02235e12f3f0f6525deb9 | #41520818608 both jobs SUCCESS; 42 Python tests; launcher unit tests/build PASS | 1,565,713 bytes; 7,151-byte headroom; no artifacts |
| F02 latest docs head / #50 | e190e2863a4aacbe3aba61094fab00572f4b469f | #41521145454 queued at last poll | RETEST PENDING |

- Storage reservation and upload-shutdown coverage are now owner + integration host verified. Physical LAN concurrency, low-storage, cancellation and expiry remain Project G.

## Project F04 current inventory — 3 November 2026

| Component | Current source owner | F04 source result | Remaining boundary |
|---|---|---|---|
| Profile Avatar QR / replacement / cleanup | E02 + C07 communication contracts | Profile ID is captured by the QR flow; private avatar root and reference-aware cleanup verified | Real phone/profile/remote acceptance in G |
| Wallpaper QR / custom collection / manager | C07 upload server + existing Wallpaper UI (E08 surface) | Separate private root and route; delete failures now surfaced and remaining state reconciled | Real phone/LAN and filesystem failure acceptance in G |
| StandBy QR / collection / photo mode | C07 upload server + D05 persistence adapter | Separate private root/preferences; durable photo+mode commit; UI membership confirmation | DreamService/OEM/screen-off/reboot acceptance in G; persistence lifecycle in F05/G |
| StandBy renderers | MainActivity overlay + ClockDream | Both consume the same StandbyPhotoStore collection | Physical visual/rotation/remote checks in G |
| Wallpaper entry surfaces | MainActivity + ModernSettingsDialog + E08 | Quick Control remains compact Change/Shuffle/Next; Appearance retains distinct Collection/Interval/Personal Manager cards | Physical D-pad/focus/Back checks in G |

**F04 integration checkpoint:** source head `e895ee590a607a8194b2037585ed47f2cf464961` passed #41560230282 (63 Python tests / 1 intentional skip; Android unit/release build and APK size gate PASS). APK 1,566,045 bytes, 6,819-byte headroom; no artifact. Latest test-only head `6ffc8b4c253beb2662d6978ba38033039c3c53f9` passed exact-head CI #41560811925 (both jobs; 65 tests / 1 intentional skip; Android unit/release build and APK size gate PASS). APK 1,566,045 bytes / 6,819-byte headroom; no artifacts.

**Owner synchronization:** C07 PR #24 head `b7a40fdc2c52268ec69c95731983d2f95024eb57` passed #41560200291 (both jobs, 21 tests / 1 intentional skip; APK 1,550,961 bytes). E08 PR #41 head `c613e3c5f593556748d1593d23f2899ecee36e4a` passed #41560600033 (both jobs, 11 tests / 1 intentional skip; APK 1,564,089 bytes). Owner synchronization HOST/CI PASS. F04 HOST/SOURCE EXIT PASS; only physical Project G acceptance and F05 persistence lifecycle validation remain.

See `F04_FEATURE_ISOLATION_AUDIT.md` for full evidence, repaired F04-WALL-001 and Project G gates.


## Project F05 — Persistence & Recovery inventory — 3 November 2026

| Component | Recovery contract | Current result |
|---|---|---|
| Profile / Avatar | Profile JSON stores custom avatar path; replacement persists reference before old-file cleanup; startup removes orphaned/stale uploads | PASS (source contract) |
| Wallpaper selection | Startup validates pinned custom file; clears stale pin; empty Custom collection returns to Natural; unreadable directory preserves saved state | FIXED / OWNER + F05 SOURCE VERIFIED |
| Wallpaper QR temp uploads | Private `custom_*.upload` parts are registered while active; startup removes unowned parts only; worker finally unregisters/deletes incomplete temp file | FIXED / OWNER + F05 SOURCE VERIFIED; Android CI pending |
| StandBy | Photo URI collection and mode are one synchronous commit; clear persists empty state before file deletion; startup orphan cleanup; both renderers share store | PASS (source contract) |
| Home / Settings navigation | Home focus and Settings tab restore through NavigationState preference keys | PASS (source contract) |
| Prayer alarms | Non-exported BOOT_COMPLETED receiver delegates to existing Azan scheduling policy; backup remains disabled | PASS (source contract); real reboot DEFERRED_TO_G |

F05 integration PR #54 is Draft/open/unmerged, based on F04 exact head `6ffc8b4c253beb2662d6978ba38033039c3c53f9`; current head `1048b0b68ae5ab5a2ad867183236b514e0c691fa`, 14 commits ahead / 0 behind. Latest F05 run #41563437433 passed 80 Python tests (1 intentional skip); Android unit/release build and APK size gate are pending. Actual force-stop, process-death and reboot acceptance remains Project G.

See `F05_PERSISTENCE_RECOVERY_AUDIT.md` and `HANDOVER.md`.


### F05 host/source exit closure — 3 November 2026

- F05 integration PR #54 exact head `1048b0b68ae5ab5a2ad867183236b514e0c691fa` passed run #41563437433: both jobs SUCCESS; 80 tests / 1 intentional skip; APK 1,566,861 bytes / 6,003-byte headroom; no artifacts.
- C07 owner PR #24 exact head `2315f12472f469843c1441ad7d01c6898858c4c5` passed run #41563429553: both jobs SUCCESS; 23 tests / 1 intentional skip; APK 1,551,661 bytes.
- E08 owner PR #41 exact head `edd24d72256d06da4578971ea7bac1802746a3a6` passed run #41562661052: both jobs SUCCESS; 15 tests / 1 intentional skip; APK 1,564,197 bytes.
- F05-WALL-001 and F05-WALL-002 are owner-fixed, mirrored and source-cross-audited. PR #24/#41/#54 remain Draft/open/unmerged. **F05 HOST/SOURCE EXIT PASS**; physical device acceptance remains Project G.
- Next host/source stage: F06 Localization. See `F05_PERSISTENCE_RECOVERY_AUDIT.md`.


## Project F06 — Bilingual localization and global UI inventory — 3 November 2026

| Surface | Contract / verification | Result |
|---|---|---|
| Language selection | ID/EN only; Android 13+ per-app LocaleManager; API 26–32 LocaleContext wrapper | PASS (source/CI) |
| Home, settings, tabs/subtabs | Dynamic labels and resource references checked; no known hardcoded language-only schedule copy remains | PASS (source/CI) |
| Android modal dialogs | MainActivity standard/custom dialogs use TvDialogStyle; Settings uses TvUiTokens and shared modal helpers | PASS (source/CI) |
| Weather summary | High/low labels use bilingual resource formatting | PASS |
| Avatar QR page | HTML lang, localized page/status/errors; method-error body uses canonical localized copy | PASS (source) |
| Wallpaper/StandBy QR pages | Localized server error bodies, fallback text, cancellation, gallery dates and thumbnail alt label | PASS (source/CI) |
| Resource parity | 615 keys in default/ID/EN; no duplicates, key or placeholder mismatch, or empty strings | PASS |
| Release build / APK budget | CI #41566633872; APK 1,569,397 B / 3,467 B headroom | PASS |

F06 integration PR #55 remains Draft/open/unmerged under D-013. Physical language switching, D-pad/focus, popup rendering, phone/LAN upload and OEM/browser acceptance remain Project G gates. See `F06_LOCALIZATION_UI_AUDIT.md`.


## Project F07 — Performance, memory, stability and storage inventory — 3 November 2026

| Surface | Source/CI contract | Result |
|---|---|---|
| Weather animation | Sun/moon/cloud radial shaders cached across frames; ripple pool capped at 16 | PASS (Robolectric + source guard) |
| Wallpaper startup/change | Background decode; generation-based stale request rejection; stale bitmap release | PASS |
| QR wallpaper preview | Background decode; preview invalidated on dismiss/new request | PASS |
| Now Playing | MediaSession/notification Binder polling on dedicated single-thread worker; refresh coalescing | PASS (source/CI) |
| Backdrop blur | OOM/runtime/rejected-worker handling; both scratch arrays recover after partial allocation | PASS |
| StandBy photo decode | One background worker, bounded queue of two, discard-oldest policy | PASS |
| Cache cleanup | No forced System.gc(); existing cache deletion semantics preserved | PASS |
| Online wallpaper storage | 150-file ceiling plus 512 MiB aggregate cache cap; oldest-first pruning and companion metadata cleanup | PASS (JUnit) |
| Python source-contract suite | 99 tests | PASS |
| Android launcher/installer unit tests | `testReleaseUnitTest` both modules | PASS |
| Release/APK gate | CI #41568715178; APK 1,571,293 B / 1,571 B headroom | PASS |

F07 PR #56 is Draft/open/unmerged. Host/source exit is PASS; real Mi TV Stick RAM/FPS/frame-time/ANR/thermal/soak profiling and physical interaction acceptance remain Project G. See `F07_PERFORMANCE_STABILITY_AUDIT.md`.

- F07 exact integration head: `976e068d48ecc1ede2811ac455372395d382ba76`; base F06 head: `487099013e6d9bd65cc07d72e760b1e14ab1f091`; PR #56 Draft/open/unmerged; CI #41568715178 PASS; APK 1,571,293 B / 1,571 B headroom. Full report: `F07_PERFORMANCE_STABILITY_AUDIT.md`.


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


## Final G01 integration inventory — 3 November 2026

- Canonical combined code branch: `integration/b-series`.
- Accepted integration head: `f6f1f05b7a560381b8bb423eaa707a1d6d0e8832`.
- Source lineage includes the B-series history, F02–F07 integration lineage, and corrected F06 localization commit.
- PR #58 is merged; PR #57 is superseded/closed.
- 26 shared paths have documented semantic dispositions. Both `GlassSurfaceFallbackTest.java` and `GlassFallbackSurfaceTest.java` remain in the tree.
- Corrected English/Indonesian Daily Verse copy and explicit regression assertions are included.
- Exact merge-commit CI #41583505195: PASS; 99 Python tests all OK; Android launcher/installer unit tests and release validation build PASS; APK 1,571,293 bytes under 1,572,864-byte cap; no workflow artifacts.
- `main` remains separate and untouched. Its manual APK-publishing workflow-dispatch commit is deliberately excluded.
- G01: PASS. G02: ready to start. G03: physical Mi TV Stick acceptance pending.

## Current integrated source inventory — 3 November 2026

- Integration branch: `integration/b-series`.
- Latest merge commit: `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- Tested candidate: `9f2da8f39a20c8e8cc43ac0ff7e42d7a82b34676`; merge tree comparison reports zero changed files.
- Coverage re-audit: A01–A06 baseline retained; B01–B07, C01–C07, D01–D07, E01–E08 source/CI coverage present; F01–F07 host/source exit pass; G01 pass.
- E05 weather/AQI cache now has per-source success timestamps and per-source cached coordinates; both Home summary and Weather dashboard reject stale-location cache. API condition text remains data-backed; F07 reduced-motion/low-memory behavior and F06 localized high/low labels are preserved.
- E08 deletion-failure regression test is now included alongside existing startup recovery, QR API clear-failure and upload cleanup guards.
- Legacy Pagar Alam branch review: Location, Azan, PrayerTimes and WallpaperShuffle files match; combined tree has newer language handling, localized location setup, retired Malay category and bounded online wallpaper cache.
- Exact tested-tree CI #41586223809: 109 Python tests / 1 intentional skip; Android unit tests/release validation PASS; APK diagnostic 1,572,293 bytes / 1,572,864-byte cap; no artifacts.
- Physical acceptance is not in this inventory pass; G03 remains pending.


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

- Official roadmap: G01 source/branch reconciliation PASS; G02 final tested APK host build/artifact PASS; **G03 Mi TV Stick physical acceptance is the current stage**.
- Live integration branch `integration/b-series` remains at `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- G02 artifact #13210715335 is currently available until 10 Nov 2026 06:37:50 UTC. Downloaded ZIP digest and extracted APK SHA-256 were independently revalidated; details in [G03 preflight](G03_DEVICE_ACCEPTANCE_PREFLIGHT_2026-11-05.md).
- No physical TV test has been performed. Host lacks ADB/signature verification utilities and has no connected Mi TV Stick. Explicit readiness approval remains mandatory before install or device mutation.
- G03 status: PREFLIGHT VERIFIED; PHYSICAL ACCEPTANCE NOT RUN / BLOCKED / NOT FINAL.


## UI Refresh P1 — Functional Inventory (6 November 2026)

- Canonical inventory: [UI_REFRESH_P1_FUNCTIONAL_INVENTORY_2026-11-06.md](UI_REFRESH_P1_FUNCTIONAL_INVENTORY_2026-11-06.md).
- Scope mapped: E02 Profile, seven built-in avatars, QR photo upload, GIPHY search/preview/selection/playback, profile add/edit/switch/delete, Child Mode, locked/hidden apps and parent PIN; E06 Home app grid, current Dock membership/order/decoration, Dock preference model, Home settings and focus/navigation.
- Every identified element records owner, actual behavior or explicit absence, data/state/persistence/permission boundary and test plan.
- Explicit not implemented: separate Dock model/view/adapter, Dock settings UI, horizontal/vertical renderer, capacity wiring, overflow and independent focus/scroll/reorder.
- P1 result PARTIAL pending user acceptance and source/permission/provider clarifications. No implementation authorization.


## R0 live reconciliation — 6 November 2026
- Canonical integration baseline: `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- E02 parent PR #35: `feature/e02-profile-avatar-lock-ui` @ `3ab9b55f3cfe03ad9841a119590a9183cf114e96`; follow-up PR #62 @ `588456144df24f2f6c015a01053ad700a04bee39`, 19 ahead/326 behind integration, OPEN/DRAFT/UNMERGED. Exact-head CI #1204 SUCCESS.
- E06 parent PR #39: `feature/e06-home-screen-subtabs` @ `570b7c83efaec3ac515b78f1c6c119b777fc3b84`; follow-up PR #63 @ `ecdeb0eba9fc7a574f1a6c9930205517687cd922`, 3 ahead/297 behind integration, OPEN/DRAFT/UNMERGED. Exact-head CI #1198 SUCCESS.
- P4-01 PR #64 @ `e4bc99c9a4822057ce3bd5490df0ad1471763409`, based directly on integration @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`, 25 ahead/0 behind, OPEN/DRAFT/UNMERGED. Exact-head CI #1225 SUCCESS; branch-only 1.6 MiB budget gate passed.
- Detailed scope gaps and safe reconciliation disposition are in `R0_RECONCILIATION_AUDIT_2026-11-06.md`. CI PASS does not close module acceptance; no early merge.


## G2 Shared UI Foundation Repair — 6 November 2026
- Source PR #65 / branch `feature/g2-shared-ui-foundation-repair` @ `40fa0715bdbcd324042927fbc56ec19f91d1947a`, base integration `562cde0751de93748eb4e0d4cdece4fa1672ac4d`; OPEN/DRAFT/UNMERGED.
- Scope: 29 changed files across shared Java components, XML palette/material resources, Weather/AQI surfaces, popup/profile/settings layouts and host tests.
- Exact-head CI #1263 / run `42252069520`: both jobs PASS; `:app:testReleaseUnitTest`, `:installer:testReleaseUnitTest`, release build all PASS.
- Release APK size diagnostic: 1,573,713 bytes. CI now reports size without blocking quality work.
- No APK published/installed; no ADB or physical TV test.
- Canonical result: `UI_REFRESH_G2_CHECKPOINT_2026-11-06.md`; next work package G3 (3A–3F).


## UI Refresh G3A — Home Screen & Independent Dock (7 November 2026)

- **Status:** COMPLETE / SOURCE-HOST QC PASS.
- **Branch/head:** `feature/g3a-home-independent-dock` @ `87302f52de844954392bac5438cbe82ec5d14a30`; PR #66 OPEN/DRAFT/UNMERGED. Stacked on G2 `40fa0715bdbcd324042927fbc56ec19f91d1947a`; PR #65 remains OPEN/DRAFT/UNMERGED.
- **Implemented:** independent DockAdapter and AppAdapter projections; horizontal tray / vertical rail; capacity 4/5/6 (default 5); four-column Grid invariant; persisted ordered pins/unpin exclusions; overflow accessibility; D-pad navigation, move/reorder/pin/unpin, focus restoration; empty/full state handling; Home Settings controls and localization.
- **Verification:** exact-head CI #1296 / `42266095885` SUCCESS, both jobs; launcher unit tests, release build and lint-vital PASS. Source/host QC reviewed identity, persistence, geometry, focus, lifecycle and regression cases.
- **APK diagnostic:** 1,589,825 bytes; informational only under D-023.
- **Not verified:** physical TV/remote/OEM overscan. No APK install, ADB, merge or integration change.
- Evidence: [UI_REFRESH_G3A_CHECKPOINT_2026-11-07.md](UI_REFRESH_G3A_CHECKPOINT_2026-11-07.md). Next: G3B Weather & AQI V4.

## UI Refresh G3B — Weather & AQI V4 (7 November 2026)

- **Status:** COMPLETE / SOURCE-HOST QC PASS; physical Android TV/remote/OEM overscan acceptance DEFERRED.
- **Branch/head:** feature/g3b-weather-aqi-v4 @ 8cfc5c886950cc9d27d4530ba901df872b8e5c44. PR #67 OPEN/DRAFT/UNMERGED; base feature/g3a-home-independent-dock @ 87302f52de844954392bac5438cbe82ec5d14a30.
- **CI:** run 42274182438 SUCCESS on functional commit 8f2162036991ded803c5ca0981ad1f3e777cfb54; both required jobs passed. Current branch HEAD differs only by one comment in tests/test_weather_cache_location_contract.py.
- **Implementation:** WeatherAirQuality.java, WeatherActivity.java, activity_weather.xml, MoonPhaseView.java, SunArcView.java, AqiSpectrumBarView.java; Indonesian/English/default strings.
- **Data contract:** Open-Meteo Forecast API; Open-Meteo Air Quality API with CAMS-based US AQI; PM2.5, PM10, CO, NO₂, SO₂. US AQI is not official JAS/APIMS IPU or Indonesian ISPU station data. CO displayed in mg/m³ after conversion; PM/NO₂/SO₂ displayed in µg/m³.
- **State/cache:** coordinate-scoped freshness; per-source success timestamps and refresh-failure flags; legacy JAS/APIMS cache cannot suppress a CAMS refresh; source location/time and stale/offline/unavailable/refresh-failed states.
- **UI/interaction:** dew point, rainfall, UV category/advice, sunrise/sunset, moon phase/rise/set, hourly/daily forecasts, separate pollutant grid, missing-value note, focus restoration and resume-time refresh. Existing G2 shared styles and G1 V6 direction retained.
- **Regression tests:** WeatherAirQualityFreshnessTest, WeatherV4LayoutContractTest, WeatherActivityFocusRestoreTest and tests/test_weather_cache_location_contract.py.
- **APK diagnostic:** 1,600,893 bytes; non-blocking under D-023. Physical visual, remote and OEM overscan acceptance not performed.
- **Next:** G3C Profile, Avatar & Child Mode. Do not reopen G1/G2/G3A without new evidence.


## UI Refresh G3C — Profile, Avatar & Child Mode (7 November 2026)

- **Disposition:** P1–P5 COMPLETE / SOURCE-HOST QC PASS.
- **Branch/head:** `feature/g3c-profile-avatar-child-mode` @ `8cf81f84bd62d0b3da31532116553cfa21a5df98`; PR #68 OPEN/DRAFT/UNMERGED; base G3B @ `8cfc5c886950cc9d27d4530ba901df872b8e5c44`.
- **P2:** active profile status is independent of focus state; profile selector initially focuses the active profile; G1 V03 and G2 shared focus/dialog foundations reused.
- **P3:** profile-scoped built-in/custom avatar and QR photo upload retained; upload session expiry and stop-state are checked before commit.
- **P4:** Child Mode, App Lock and Hidden Apps remain profile-scoped and PIN-gated. Policy picker is transactional: Done saves; Back/dismiss cancels.
- **P5:** durable profile and policy writes, atomic active-profile fallback on deletion, legacy settings migration ownership, duplicate/missing ID repair and malformed JSON backup.
- **Evidence:** exact-head CI #1313 / `42340859666` and #1314 / `42340866781`, both required jobs SUCCESS; release unit tests and APK assembly PASS. APK 1,602,361 bytes, informational warning above historical 1.5 MiB reference.
- **Scope boundary:** physical TV/remote/OEM acceptance deferred/not run; no APK publication/install, ADB, device configuration or merge. PIN default/storage limitations unchanged. GIPHY PR #62 remains separate.
- **Next:** G3D Quick Settings. Canonical checkpoint: `UI_REFRESH_G3C_CHECKPOINT_2026-11-07.md`.


## UI Refresh G3D — Quick Settings (7 November 2026)

- **Disposition:** COMPLETE / SOURCE-HOST QC PASS; physical Android TV acceptance DEFERRED / NOT RUN.
- **Branch/head:** `feature/g3d-quick-settings` @ `bbc7b240d2ad7bfc9d22dbaf3437f03b5953bcf8`; PR #69 OPEN/DRAFT/UNMERGED, base G3C @ `8cf81f84bd62d0b3da31532116553cfa21a5df98`.
- **Implementation:** `QuickSettingsDialog` V04 centered two-column modal; `QuickSettingsFocusGraph` four-way D-pad links; retained existing restart, power, language, cache, wallpaper, Glass TV Settings and Android Settings owners; Wi-Fi child-menu return and hidden-pill-safe anchor/status.
- **Lifecycle/security:** Cache completion uses a weak Activity reference and finishing/destroyed/window-focus guards. No new permission or manifest change; Android Settings still routes through `SystemSettingsAccess`; no fake Wi-Fi/radio control.
- **Localization:** Default/ID/EN quick-settings strings present exactly once.
- **Regression:** `QuickSettingsDialogTest`, `QuickSettingsFocusGraphTest`, `tests/test_quick_settings_contract.py`.
- **CI:** exact-head run #1328 / `42344123725`, both jobs SUCCESS; `testReleaseUnitTest`, installer unit tests, `assembleRelease`, and 120 Python tests PASS.
- **APK diagnostic:** 1,608,373 bytes (+6,012 bytes / approximately 0.38% vs G3C 1,602,361 bytes); informational under D-023. No artifact published.
- **Not verified:** physical TV/remote/OEM Wi-Fi/power/cache behavior, 1080p/4K, overscan, daylight and large-text acceptance. No APK install, ADB, device setting change or merge.
- **Next:** G3E Wi-Fi. See [G3D audit](UI_REFRESH_G3D_AUDIT_2026-11-07.md) and [G3D checkpoint](UI_REFRESH_G3D_CHECKPOINT_2026-11-07.md).


## UI Refresh G3E — Wi-Fi (7 November 2026)

- **Disposition:** COMPLETE / SOURCE-HOST QC PASS; physical Android TV acceptance PENDING / NOT RUN.
- **Branch/head:** `feature/g3e-wifi` @ `9125043e24b8f13232e41193ba5fe10e1821f7cd`; PR #70 OPEN/DRAFT/UNMERGED; base G3D exact HEAD `bbc7b240d2ad7bfc9d22dbaf3437f03b5953bcf8`.
- **Implementation:** `WifiSettingsDialog`, `WifiScanPolicy`; V05 status/connected card, nearby scan list, security/signal labels, horizontal actions and Close. Android Settings owns network selection/authentication/connect/disconnect/radio state.
- **Architecture/security:** existing MainActivity network-state owner reused; no duplicate ConnectivityManager callback. Fine-location and Location service gate SSID/scan results; CHANGE_WIFI_STATE is used only to request a scan. No credentials/BSSID persistence or direct radio/connect/disconnect calls.
- **Lifecycle/focus:** scan receiver and timeout scoped to dialog; D-pad graph and IDs, Back, focus restoration, parent return after Android Settings and launch-failure fallback.
- **Localization/tests:** default/ID/EN strings; WifiScanPolicyTest, WifiSettingsDialogTest, updated Quick Settings and Wi-Fi source-contract tests.
- **Exact-head CI:** #1363 / `42346491017`, both required jobs SUCCESS; 127 Python tests, app/installer release unit tests and app/installer release builds PASS.
- **APK diagnostic:** 1,617,373 bytes (+9,000 / approximately 0.56% vs G3D); informational under D-023, no artifact published.
- **Known risk:** Android deprecates `WifiManager.startScan()` since API 28 and throttles active scans; best-effort scan plus cached/broadcast/system-settings fallback. Future API migration remains open.
- **Device boundary:** no physical TV test, APK installation/publication, ADB, device mutation, merge or integration/main change.
- **Next:** G3F Global Settings. See `UI_REFRESH_G3E_AUDIT_2026-11-07.md` and `UI_REFRESH_G3E_CHECKPOINT_2026-11-07.md`.

## UI Refresh G3F — Global Settings inventory checkpoint (7 November 2026)

| Item | Current evidence-based status |
|---|---|
| Owner branch / HEAD | `feature/g3f-global-settings` @ `455b619c3eefe417ac0152a51f3cb12478a8f4a9` |
| PR / dependency | #71 OPEN/DRAFT/UNMERGED; base G3E exact HEAD `9125043e24b8f13232e41193ba5fe10e1821f7cd` |
| V06 Global Settings shell | COMPLETE / SOURCE-HOST QC PASS |
| Seven categories and localized control search | PASS; category routing and target focus covered |
| Profile, Wi-Fi and device state ownership | PASS; existing ProfileManager, ChildModeManager and MainActivity/E01 owners reused |
| Focus/lifecycle | PASS at source-host gate; physical remote acceptance not run |
| Exact-head CI | #1376 / `42348837445`; both jobs SUCCESS; 134 Python tests; app/installer unit tests and release builds PASS |
| APK diagnostic | 1,620,193 bytes; +2,820 bytes (~0.17%) vs G3E; informational under D-023 |
| Device acceptance | PENDING / NOT RUN |
| Integration / merge | NOT AUTHORIZED; PR remains draft and unmerged |

- Canonical evidence: `UI_REFRESH_G3F_AUDIT_2026-11-07.md` and `UI_REFRESH_G3F_CHECKPOINT_2026-11-07.md`.
- **Next:** G4 integration/reconciliation planning, subject to explicit approval. No APK publication/install, ADB or physical device mutation.


## UI Refresh G4 — final parent PIN security inventory (7 November 2026)

| Area | Final state | Evidence |
|---|---|---|
| Parent PIN storage | PASS | Salted PBKDF2-HMAC-SHA256, 16-byte salt, 120,000 iterations, 256-bit hash |
| Legacy custom PIN | PASS | Lossless migration only after successful verification; plaintext removed after successful hash write |
| Default PIN | PASS | Fresh-install and legacy default remain `0000`; no forced first-use update |
| Retry policy | PASS | No counters, limits, cooldown, lockout, forced delay or temporary restriction |
| Corrupt credential | PASS | Malformed, mistyped, partial or missing previously stored credential fails closed; no default fallback |
| UI responsiveness | PASS / SOURCE | KDF/verification runs on background executor; UI result posts to main looper |
| Profile/device scope | PASS | Parent PIN device-wide; Child Mode/App Lock/Hidden Apps profile-scoped |
| Regression tests | PASS | 11 focused PinCredentialStore tests plus profile persistence regression |
| Combined-tree CI | PASS | PR #72 synthetic merge `376b5b69e2aed56e1f813169f35dbb9459ada554`; CI #1394 both jobs SUCCESS |
| Physical device acceptance | PENDING / NOT RUN | Separate approval gate; no APK installation, ADB or device mutation |

- Final G4 source HEAD: `f46adf27bbc6b2156c7e9bb49d48cf4fd0fcdb5e`; base G3F `455b619c3eefe417ac0152a51f3cb12478a8f4a9`.
- Combined diff: five files only — ChildModeManager, ModernSettingsDialog, PinCredentialStore, PinCredentialStoreTest and ProfileManagerPersistenceTest. No docs files are included in PR #72.
- Release APK diagnostic: 1,622,401 bytes; +2,208 bytes vs G3F; informational under D-023.
- Canonical evidence: `UI_REFRESH_G4_AUDIT_2026-11-07.md` and `UI_REFRESH_G4_CHECKPOINT_2026-11-07.md`.


## Image 2 Home layout recovery — inventory (7 November 2026)

| Gate | Evidence-based status |
|---|---|
| Home shelf/Dock geometry source fix | COMPLETE on PR #73 |
| Owner branch / exact HEAD | `fix/home-layout-image2-dock-geometry` @ `31886651d7f275155f8a526302e252dc04687e4f` |
| PR | #73 OPEN / DRAFT / UNMERGED |
| Exact-head CI | #1397 / `42357686726`; both required jobs SUCCESS |
| APK artifact | #`13567869928`; 1,622,565-byte APK; ZIP integrity PASS |
| APK SHA-256 | `cd2b301f5fb48ffdb6b9cafd74927c5da6515a4ab9aebf6dc47e6db4ddccb4ac` |
| Physical TV visual/remote acceptance | PENDING / NOT RUN |
| Install / ADB / merge | NOT PERFORMED |

- Canonical evidence: `UI_REFRESH_IMAGE2_HOME_LAYOUT_CHECKPOINT_2026-11-07.md`.


## Image 2 bottom Dock correction — inventory (7 November 2026)

| Gate | Evidence-based status |
|---|---|
| Horizontal Dock bottom anchoring | SOURCE FIX COMPLETE |
| App Grid viewport above Dock | SOURCE FIX COMPLETE; fixed reserve independent of capacity 4/5/6 |
| D-pad adjacency / regression test | IMPLEMENTED |
| Vertical Dock side rail | PRESERVED |
| Owner branch / exact HEAD | `fix/home-layout-image2-dock-geometry` @ `c3d749db7373052019063f5057ac08f0eda797b0` |
| PR #73 | OPEN / DRAFT / UNMERGED |
| Exact-head CI #1404 | BOTH REQUIRED JOBS SUCCESS |
| Corrective APK | 1,622,545 bytes; SHA-256 `3b01c1126043c2e8025e1852d7de5d09812b091986af21e9c1c607188cb926ab` |
| Physical TV visual/remote acceptance | PENDING / NOT RUN |
| Installation / ADB / merge | NOT PERFORMED |

- The earlier Image 2 APK did not correct Dock height and is superseded. Use only the APK recorded in `UI_REFRESH_IMAGE2_BOTTOM_DOCK_CORRECTION_2026-11-07.md` for the next acceptance test.


## Reset inventory — Glass TV Home 4.0.0 (7 November 2026)

| Item | Current reset state |
|---|---|
| Active baseline branch | `reset/baseline-4.0.0` |
| Exact baseline SHA | `7146de58953325fdfc6c98501828676f143e64cf` |
| Application version | 4.0.0 / versionCode 40 |
| Baseline APK size | 1,622,401 bytes |
| Baseline APK SHA-256 | `ec125ca46bf98af8426fef060e974c963932f9ae7f863507d135ae40df9bb605` |
| PR #73 UI/Dock rework | CLOSED / NOT MERGED; branch retained |
| PR #72 baseline lineage | OPEN / DRAFT / UNMERGED |
| Device installation | NOT PERFORMED |

- Full evidence: `RESET_BASELINE_4.0.0_2026-11-07.md`.


## Home Dock visual parity V2 — inventory (7 November 2026)

| Gate | Status |
|---|---|
| Patch 4.0/4.1 source baseline audit | COMPLETE |
| Shared Dock geometry contract | IMPLEMENTED |
| Capacity 4/5/6 and density regression tests | PASS |
| Exact-head CI #1407 | BOTH REQUIRED JOBS SUCCESS |
| Test APK | 1,622,573 bytes; SHA-256 19d1435c7c20491b9093747d65c8dbab6a0627971461bee401702990538a142c |
| Artifact | #13576552604; expires 14 Nov 2026 |
| PR #74 | OPEN / DRAFT / UNMERGED |
| Physical TV visual/remote/overscan acceptance | READY / NOT RUN |
| Install / ADB / device mutation / merge | NOT PERFORMED |

- Owner branch: fix/home-dock-visual-parity-v2 @ 4ba1d7f14ba26e0c4051fcdf958f289234b6e7ae.
- Evidence: HOME_DOCK_VISUAL_PARITY_V2_2026-11-07.md.


## Patch 4.1 Dock & Liquid Glass adoption — inventory (7 November 2026)

| Gate | Status |
|---|---|
| Patch 4.1 Dock fill semantics restored | IMPLEMENTED |
| Short/empty pin list fills Dock from ordered inventory | IMPLEMENTED / TESTED |
| Explicit unpinned keys respected | TESTED |
| Patch 4.1 GlassRim + Window Light | ADOPTED |
| Patch 4.1 GlassView hardware-layer rim | ADOPTED |
| DockBreathingBorderView and Dock-only focus wiring | ADOPTED |
| Exact-head CI #1415 | BOTH REQUIRED JOBS SUCCESS |
| Test APK | 1,624,741 bytes; SHA-256 23ffb28d48b05f975be4b5bd13aa1086c08e47eef1feb5b8e13916799f32fafc |
| Artifact | #13576403262; expires 14 Nov 2026 |
| PR #74 | OPEN / DRAFT / UNMERGED |
| Physical TV visual/remote acceptance | PENDING / NOT RUN |
| Install / ADB / device mutation / merge | NOT PERFORMED |

- Owner branch fix/home-dock-visual-parity-v2 @ 239390db27fafcc39b2fefa68ddfa9f0ba30608f.
- Evidence: PATCH41_DOCK_GLASS_ADOPTION_2026-11-07.md.


## Patch 4.1 Dock & Liquid Glass adoption — final inventory (7 November 2026)

| Gate | Status |
|---|---|
| First-row Dock fill-to-capacity | IMPLEMENTED / TESTED |
| Explicit unpins respected | TESTED |
| Patch 4.1 GlassRim / Window Light | ADOPTED |
| Patch 4.1 GlassView | ADOPTED |
| DockBreathingBorderView / Dock-only focus | ADOPTED |
| Patch 4.1 16dp bottom safe offset | RESTORED / TESTED |
| Exact-head CI #1416 | BOTH REQUIRED JOBS SUCCESS |
| Test APK | 1,624,733 bytes; SHA-256 5da75cfd30956224bb9bf7c61ced34a11068356d83227cdb28d2fd9fb6d00684 |
| Artifact | #13577033231; expires 14 Nov 2026 |
| PR #74 | OPEN / DRAFT / UNMERGED |
| Physical TV visual/remote acceptance | PENDING / NOT RUN |
| Install / ADB / device mutation / merge | NOT PERFORMED |

- Owner branch fix/home-dock-visual-parity-v2 @ 2d1f744a4b77406712e7868a7e7c6747a3bb88ed.
- Evidence: PATCH41_DOCK_GLASS_ADOPTION_2026-11-07.md.


## Patch 4.1 overflow-app recovery — 7 November 2026

| Gate | Status |
|---|---|
| Confirm horizontal non-Dock apps were inaccessible | SOURCE AUDIT CONFIRMED |
| Grid collapsed at idle, reveal on Dock DPAD Down | IMPLEMENTED |
| Overflow Grid Up returns to Dock | IMPLEMENTED |
| Dock Up returns to top shelf | IMPLEMENTED |
| Stable-key horizontal Grid focus restoration | IMPLEMENTED |
| Navigation mapping unit test | ADDED / CI PASS |
| Exact-head CI #42382202545 | BOTH REQUIRED JOBS SUCCESS |
| Release APK | 1,624,937 bytes |
| Test artifact | #13578301246; ZIP 1,424,751 bytes; SHA-256 5c9ea538f64743a0b31c91b14add41b47f091c3d57110d2f0bce83c86e537922 |
| Physical TV visual/remote acceptance | PENDING / NOT RUN |
| PR #74 | OPEN / DRAFT / UNMERGED |
| Install / ADB / device mutation / merge | NOT PERFORMED |

- Source branch fix/home-dock-visual-parity-v2 @ 48f07a983ebee4370bbf13a80f85edd947c2eb31.
- Evidence: HOME_DOCK_PATCH41_RECOVERY_2026-11-07.md.


## Horizontal Dock centering correction — inventory (7 November 2026)

| Gate | Status |
|---|---|
| Confirm aggregate Dock left-alignment defect from owner TV photo | SOURCE AUDIT CONFIRMED |
| Aggregate centering offset based on viewport, tile width and visible item count | IMPLEMENTED |
| Preserve full viewport tile-width measurement | IMPLEMENTED |
| Reset offset for vertical/empty Dock | IMPLEMENTED |
| Regression tests: capacities 4/5/6, four densities, edge cases | ADDED |
| Latest source HEAD | b14a7bc3d1e99cf8cf0c74e944c39983d776f341 |
| Latest-head CI | RUN #42384207367 IN PROGRESS; initial test rounding assertion corrected |
| Latest centered APK | PENDING |
| Physical TV visual/remote acceptance | PENDING / NOT RUN |
| PR #74 | OPEN / DRAFT / UNMERGED |
| Install / ADB / device mutation / merge | NOT PERFORMED |

- Final latest-head CI #1424 / run 42384207367 SUCCESS; both required jobs passed, including 296+ unit tests and release build, repo contract checks, APK diagnostics and artifact publication.
- Release APK 1,624,997 bytes; SHA-256 8e3183addc092e5bce1b41b09d939fc64682d6b16f595a0e4e1caa0a5f982664. Artifact #13578283307; ZIP 1,424,983 bytes; SHA-256 72014680fd7e7b02cf36ed5029d55be465ac3394d649fc95ee718af05e234d17; expires 14 Nov 2026.
- APK extracted and verified as Android package. Physical TV visual/remote acceptance still PENDING / NOT RUN; PR #74 OPEN/DRAFT/UNMERGED. No install, ADB, device mutation or merge.


## GTVH-HOME-LAYOUT-FIX-001 — inventory (8 November 2026)

| Item | Status / evidence |
|---|---|
| Owner video evidence | Received; 23.14s, 1920×1080; pre-fix only |
| Dock inventory/tray/overflow correction | Existing PR #74/#75; source implemented, PR #75 CI #1441 PASS |
| Shared Dock/Grid geometry | `DockGeometry.java`; existing tests cover capacities 4/5/6, densities 1/1.5/1.75/2, 1280/1920 viewports |
| DPAD row progression | Source path present in `MainActivity`; physical remote test pending |
| Quick Toggle root cause | Confirmed: `alignTopButtons()` Dock-relative translation via Grid/Dock global-layout listeners |
| Quick Toggle correction | PR #76 removes listeners/method; XML right/safe-frame anchor retained |
| New regression test | `tests/test_home_layout_bounds_contract.py` |
| PR #76 branch/head | `fix/home-global-layout-bounds` @ `cf9d661dc7e9d0520608dc771aed2b9d75bb1a01` |
| PR #76 | OPEN / DRAFT / UNMERGED; base is PR #75 |
| Exact-head CI #1443 | IN PROGRESS at checkpoint; #1442 first attempt failed due test XPath lookup, corrected in new commit |
| PR #76 APK size / SHA-256 | PENDING |
| Post-fix visual evidence | PENDING |
| Physical Mi TV Stick / remote acceptance | PENDING / NOT RUN |
| Install / ADB / device mutation / merge | NOT PERFORMED |

- Detailed checkpoint: `HOME_LAYOUT_FIX_2026-11-08.md`.

### PR #76 CI follow-up

| Gate | Status |
|---|---|
| Exact source/test head CI #1443 | PASS — both jobs |
| Python tests | PASS — 137 tests, 1 intentional skip |
| Android unit tests / release build | PASS on `cf9d661dc7e9d0520608dc771aed2b9d75bb1a01` |
| APK artifact workflow scope | Corrected to include PR #76 |
| Latest branch head | `ea681545bbf356105683ef2c7da6664e29a83723` |
| Latest-head CI #1447 | IN PROGRESS |
| PR #76 APK size / SHA-256 | PENDING artifact publication and verification |
| Physical Mi TV Stick acceptance | PENDING / NOT RUN |

### GTVH-HOME-LAYOUT-FIX-001 — latest CI and APK verification

| Gate | Status / evidence |
|---|---|
| PR #75 CI #1446 on head 78539148930bc6e3613a03db0dd3997073c725ee | BOTH REQUIRED JOBS SUCCESS |
| PR #75 intermediate artifact | #13585808852; ZIP 1,422,660 bytes |
| Intermediate APK | 1,622,497 bytes; SHA-256 38d818902ce3ccf800b8de5cc0428125b91b7e9c27ba81810759a53925c65d0f |
| ZIP/APK integrity | PASS |
| Intermediate APK scope | Dock host/padding correction; excludes PR #76 Quick Toggles fix |
| Latest PR #75 head | 21bc204a413fce3c527e0e8761d2016bc5dc87c6; CI #1448 IN PROGRESS |
| Latest PR #76 head | ea681545bbf356105683ef2c7da6664e29a83723; CI #1447 IN PROGRESS |
| Combined PR #76 APK / SHA-256 | PENDING |
| Physical Mi TV Stick acceptance | PENDING / NOT RUN |

### PR #76 base reconciliation — latest inventory

| Item | Status |
|---|---|
| Latest PR #75 base | `21bc204a413fce3c527e0e8761d2016bc5dc87c6` |
| PR #76 head | `012fba1c4012c7e4a05de292b69030c17a594d90` |
| Base → head | 2 ahead / 0 behind |
| PR #76 changed files | `MainActivity.java`; `tests/test_home_layout_bounds_contract.py` |
| PR #76 state | OPEN / DRAFT / UNMERGED |
| Exact-head CI #1449 | IN PROGRESS |
| PR #76 APK / size / SHA-256 | PENDING |
| Physical Mi TV Stick acceptance | PENDING / NOT RUN |

### GTVH-HOME-LAYOUT-FIX-001 — final cumulative build candidate

| Gate | Status / evidence |
|---|---|
| PR #75 latest-head CI #1448 | BOTH REQUIRED JOBS SUCCESS |
| PR #75 head | 21bc204a413fce3c527e0e8761d2016bc5dc87c6 |
| PR #76 latest-head CI #1447 | BOTH REQUIRED JOBS SUCCESS |
| PR #76 head | ea681545bbf356105683ef2c7da6664e29a83723 |
| Cumulative PR #76 artifact | #13586598248; ZIP 1,422,482 bytes |
| Verified APK | 1,622,153 bytes |
| APK SHA-256 | 12dc35e31fc2e67bcba1e0ddcee548937f5f23f9bf90d654316fc6fdc34f17c7 |
| ZIP/APK integrity and core entries | PASS |
| Source / unit tests / CI / release build | PASS |
| Post-fix visual evidence | PENDING |
| Physical Mi TV Stick / remote acceptance | PENDING / NOT RUN |
| Merge / install / ADB / device mutation | NOT PERFORMED |

### Current exact-head inventory — 8 November 2026

| Item | Status |
|---|---|
| PR #75 base | `21bc204a413fce3c527e0e8761d2016bc5dc87c6` |
| PR #76 current head | `f085c41356e2e5f53ca13f51bc0676bdb82d350a` |
| Base → head | 3 ahead / 0 behind |
| Latest Dock symmetric padding/host geometry | PRESERVED |
| Dock-relative top-menu translation/listeners | REMOVED |
| PR #76 files | MainActivity.java; tests/test_home_layout_bounds_contract.py |
| Exact-head CI #1450 | PARTIAL: Python job PASS (137 tests, 1 skip); Android job IN PROGRESS |
| APK artifact / size / SHA-256 | PENDING |
| Post-fix visual evidence | PENDING |
| Physical Mi TV Stick acceptance | PENDING / NOT RUN |
| PR #76 | OPEN / DRAFT / UNMERGED |

### GTVH-HOME-LAYOUT-FIX-001 — cumulative APK candidate and current head

| Gate | Status / evidence |
|---|---|
| PR #75 exact-head CI #1448 | PASS — both jobs; head 21bc204a413fce3c527e0e8761d2016bc5dc87c6 |
| PR #76 CI #1449 | PASS — both jobs; head 012fba1c4012c7e4a05de292b69030c17a594d90 |
| PR #76 current head | f085c41356e2e5f53ca13f51bc0676bdb82d350a |
| PR #76 exact-head CI #1450 | IN PROGRESS; latest commit re-applies base Dock geometry correction |
| Candidate artifact | #13586419148; ZIP 1,422,343 bytes |
| Candidate APK | 1,622,117 bytes |
| Candidate APK SHA-256 | fe7db8ca54be34781b716454256e50da62b5ee3bf168fc211aa7e45095fa95b6 |
| ZIP/APK integrity and core entries | PASS |
| Cumulative source / tests / build | PASS on CI #1449 tree |
| Physical Mi TV Stick / remote acceptance | PENDING / NOT RUN |
| Merge / install / ADB / device mutation | NOT PERFORMED |

### GTVH-HOME-LAYOUT-FIX-001 — final exact-head technical gate

| Gate | Status / evidence |
|---|---|
| PR #75 CI #1448 | PASS — both jobs; head 21bc204a413fce3c527e0e8761d2016bc5dc87c6 |
| PR #76 latest head | f085c41356e2e5f53ca13f51bc0676bdb82d350a |
| PR #76 CI #1450 | PASS — both jobs; run 42404262057 |
| Cumulative APK artifact | #13585769799; ZIP 1,422,487 bytes |
| APK size | 1,622,153 bytes |
| APK SHA-256 | 90fe5131a84231d1a725c4eb02b672d94a1c6c1a07f2cda8db710bc7c8ddc135 |
| ZIP/APK integrity and core entries | PASS |
| Source / regression tests / release build | PASS |
| Post-fix visual evidence | PENDING |
| Physical Mi TV Stick / remote acceptance | PENDING / NOT RUN |
| PR #74/#75/#76 merge | NOT PERFORMED; all remain Draft |
| ADB / install / default-launcher change / device mutation | NOT PERFORMED |

| CI #1450 Python contract suite | 137 tests OK; 1 intentional skip |
| APK size diagnostic | 1,622,153 bytes; 49,289 bytes above historical 1.5 MiB reference (non-blocking) |
| Size vs earlier PR #75 baseline | 316 bytes smaller |


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


## GTVH-HOME-FINAL-CROSS-AUDIT-001 — 8 November 2026

| Item | Evidence / status |
|---|---|
| PR #77 consolidated Home layout | OPEN / DRAFT / UNMERGED; head `0ea3746f0304b7042a018eeebf850d759f357bf8`; prior exact-head CI #1506 PASS |
| PR #79 balanced safe frame/header | OPEN / DRAFT / UNMERGED; latest head `ddd8750a688460cf1c8bc4b27c77189294301844`; fresh exact-head CI pending/unobserved |
| Stale Grid reveal callback | FIXED in PR #79; captured generation checked before deferred callback re-enters focus flow |
| Dock focus cancellation | FIXED in PR #79; Dock focus invalidates queued Grid focus generation |
| Header focus cancellation | FIXED in PR #79; top/header pill focus invalidates queued Grid focus generation |
| Activity pause cancellation | FIXED in PR #79; onPause invalidates queued Grid focus generation |
| Regression test | Added Python source-contract test for all four cancellation/guard paths |
| PR dependency ancestry | PARTIAL: PR #79 base SHA is one CI-only commit behind PR #77 head; CI workflow files inspected and identical; reconcile before merge |
| Mi TV Stick visual/DPAD/overscan | NOT RUN; requires owner device acceptance |
| APK installation / device mutation | NOT DONE |
| Merge / release | NOT AUTHORIZED / NOT DONE |

Previous CI #1506 and #1526 predate the latest stale-focus correction and must not be used as evidence for the new exact head.


## GTVH-HOME-FINAL-CROSS-AUDIT-001 follow-up — 8 November 2026

| Item | Status |
|---|---|
| Stale-focus cancellation and callback guard | FIXED; exact-head CI #1529 PASS (prior head) |
| Bilingual altitude resource | FIXED in latest PR #79 head `1fff5b7b8eea4ee53198fe3e65f6c75c91340be3` |
| Default/Indonesian altitude | `%1$d mdpl` |
| English altitude | `%1$d m ASL` |
| Localization regression test | ADDED; CI #1534 IN PROGRESS |
| PR #79 merge status | OPEN / DRAFT / UNMERGED |
| Mi TV Stick acceptance | NOT RUN |


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


## Active refinement — GTVH-HOME-HEADER-REFINEMENT-001 (8 November 2026)
- Owner-approved safe frame: 29dp on all four edges (58px at density 2.0); source PR #79 HEAD `354b855b65d95db688cf9a43af5b74e4a14a229b`.
- Header: clock 44sp; AM/PM/date/Shamsi date 11sp; single shared navigation capsule with independent transparent focus targets; capsule radius 20dp, segment radius 14dp, other Header glass pills 21dp.
- Source implementation and regression files listed in `GTVH_HOME_HEADER_REFINEMENT_001_AUDIT.md`.
- CI #1559 pending at checkpoint; runtime TV test and screenshot evidence pending; PR #79 and #77 remain draft/unmerged.


## CI correction — GTVH-HOME-HEADER-REFINEMENT-001
- Updated source HEAD: `e9fdecdb4dd44eefeed1464fddaf8edd2e2ec5e0`.
- CI #1559 failed 1/310 tests because DockLayoutModelTest retained the old 34dp safe-reserve expectation; test corrected to 29dp.
- CI #1560 / run `42484233880` is pending/in progress; no build pass claimed.


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


## Source inventory delta — Home weather/AQI pill width (8 Nov 2026)

Changed on PR #79 branch `fix/home-balanced-frame-001`:
- `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java` — one shared `locationWidth` calculation (existing 34% location reference) now sets both location and weather/AQI runtime widths.
- `tests/test_home_layout_bounds_contract.py` — regression contract for equal width source, common left anchors, weather text ellipsis, and content updates not changing layout parameters.

No XML layout, resource strings, weather provider, location model, Header navigation, safe frame, or other Home component files changed in this refinement. Compare from previous PR #79 HEAD `c15e4383d7dd3f148b31a19f5e9b85423936b56d` to current test HEAD `65544d9eea9bd0a5ae5156a9bd58724b1817fd2a`: exactly these two files changed (4 commits ahead). CI #1587 Android job is pending.


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


## Home source inventory addendum — GTVH-HOME-WALLPAPER-TEXT-REFINEMENT-001

- PR #80 source branch: `fix/home-wallpaper-text-refinement-001`; HEAD `c7d95c3de24bc70b671159d5aa9ef49451cbe112`; base is PR #79 HEAD `b4abdf0bd555a0106c135add20c3f287cf10b412`.
- Changed source: `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java` (shared shelf anchor, prayer text gradient, ayah focus/frame styling); `launcher/app/src/main/res/layout/activity_main.xml` (location/title order and spacing); added `tests/test_home_wallpaper_text_refinement_contract.py`.
- Unchanged: `DockGeometry.java`, Dock/Grid layout and ordering, Header/capsules, global safe frame, wallpaper providers/rotation, prayer data/provider/order and remote navigation routes.
- CI PASS; rendered/device acceptance pending. Full evidence: `GTVH_HOME_WALLPAPER_TEXT_REFINEMENT_001_AUDIT.md`.

---

## Home source inventory addendum — GTVH-HOME-UI-PROPORTION-GLASS-AND-FOCUS-REFINEMENT-003

- Source PR #83: https://github.com/Frangga11/glass-tv-home/pull/83; branch `fix/home-ui-proportion-glass-focus-refinement-003`; exact HEAD `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`; base PR #82 exact HEAD `d6f594933b712b36b0660e119a0d4ee8b7de3134`.
- Changed 20 files:
  - `.github/workflows/ci.yml`
  - `.github/workflows/publish-apk-release.yml`
  - `launcher/app/src/main/java/com/gathin/tvhome/AppUpdateManager.java`
  - `launcher/app/src/main/java/com/gathin/tvhome/DockAdapter.java`
  - `launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java`
  - `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
  - `launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java`
  - `launcher/app/src/main/res/drawable/header_glass_pill_bg.xml`
  - `launcher/app/src/main/res/drawable/header_nav_tab_bg.xml`
  - `launcher/app/src/main/res/drawable/pill_bg.xml`
  - `launcher/app/src/main/res/layout/activity_main.xml`
  - `launcher/app/src/main/res/layout/dialog_ota_coffee.xml`
  - `launcher/app/src/main/res/values-en/strings.xml`
  - `launcher/app/src/main/res/values-id/strings.xml`
  - `launcher/app/src/main/res/values/strings.xml`
  - `launcher/app/src/test/java/com/gathin/tvhome/DockLayoutModelTest.java`
  - `tests/test_f06_localization_ui_contract.py`
  - `tests/test_home_dock_islamic_shelf_navigation_refinement_contract.py`
  - `tests/test_home_ui_proportion_glass_focus_refinement_contract.py`
  - `tests/test_home_wallpaper_text_refinement_contract.py`
- PR #83 implements manual-only updates, 36dp navigation capsule, 4dp icon/text spacing, 12dp horizontal Dock extra height, 1.045 horizontal Dock icon optical scale, 1dp Daily Doa/prayer divider, softer Islamic Shelf scrim, shared 20dp glass and Glider focus.
- Grid icon bounds (48dp), Dock icon bounds (44dp), APK download/install permission and PackageInstaller pipeline remain intact.
- CI #1637 PASS; 184 Python tests (1 skipped); Android app/installer Release unit tests and `assembleRelease` PASS.
- Publisher #92 PASS; APK 1,625,213 bytes; SHA-256 `2d14894da872f616188bace686be5f85021766914bbe65260cc76a7848baee26`; [release page](https://github.com/Frangga11/glass-tv-home/releases/tag/glass-tv-home-pr83-947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803).
- No physical Mi TV Stick test or PR merge. Physical acceptance pending.


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

### PR #101 cumulative integration — 9 November 2026
- Current source candidate: PR #101, OPEN / DRAFT / UNMERGED; branch `integration/home-islamic-prayer-spacing-cumulative-101`; HEAD `b8389e1f90b9fb7f65d91cfd0d7a41782f343d83`; base SHA `4038c08f4825f7bacf965bc6708505d3c8c30ebc`.
- 16 changed files (5 production/source/workflow; 11 regression/test), 4 commits ahead / 0 behind.
- Exact-head CI #1681 PASS: 211 Python tests, 1 skipped; Android app/installer unit tests and release assemble PASS.
- QA artifact ID `13639167072`; extracted APK 1,626,525 bytes; SHA-256 `5423f7746d6b771fcb0c76ca3fcdca5186c5957d2967f66437554cb6e7d62398`; package ZIP integrity PASS.
- Historical APK threshold warning (+53,661 bytes above 1.5 MiB); only +1,312 bytes vs PR #83 baseline 1,625,213 bytes (+0.081%). Non-blocking; monitor, no unscoped optimization.
- Visual 1920×1080 render and physical Mi TV Stick acceptance remain NOT RUN. No ADB/install/merge. Candidate is not final.

## Inventory reconciliation — active candidate and approved visual scope — 9 November 2026

- Current active corrective source candidate: PR #103; use the exact branch/HEAD/base and five-file scope in the authoritative sync block above.
- Cumulative parent PR #101 remains OPEN / DRAFT / UNMERGED. PR #103 is based on its exact source HEAD; preserve ancestry and do not cherry-pick/rebase blindly.
- Before Frosted Silver implementation, produce a source-backed inventory of every glass surface, shared style/theme/drawable, popup/dialog, state, focus target, and resource dependency. Include Home, dock, navigation, Wi-Fi/microphone, location/altitude, weather/AQI, settings, profile, prayer, Ayah, Arabic/translation, and conditional/secondary screens.
- Record each component's source path, owner/shared-resource relationship, current geometry, interaction/focus behavior, and whether it is in-scope. The inventory must distinguish shared components from one-off surfaces to prevent unintended global side effects.
- Older PR lists and component snapshots remain historical; current active PR/status is governed only by GTVH-CHECKPOINT-LATEST.md.

## Complete project-management documentation catalog — 9 November 2026

Directory audit result: **96 Markdown files + 1 `prototypes/` directory**. All files below are accounted for. Detailed historical evidence is preserved in its focused document; current status is never inferred from a historical file. The canonical checkpoint remains the only current-status/restart authority.

### Core control documents
- GTVH-CHECKPOINT-LATEST.md — sole current checkpoint and restart point.
- MASTER_PROJECT.md — project scope, architecture, baseline and roadmap.
- INVENTORY.md — source/feature/PR/dependency and documentation inventory.
- DECISION_LOG.md — approved decisions and constraints.
- AUDIT_REGISTER.md — audit findings, evidence and closure status.
- HANDOVER.md — concise resume instructions.
- README.md — documentation navigation.
- DOCUMENTATION_WORKFLOW.md — documentation governance.
- OPERATIONS_LOG.md — chronological work log.
- MERGE_QUALITY_POLICY.md — merge and quality gates.

### Roadmaps, reconciliation and global UI
- BUG_REPAIR_ROADMAP.md
- UI_REFRESH_ROADMAP.md
- PROJECT_G_READINESS_CHECKPOINT.md
- GTVH-HOME-CUMULATIVE-RECONCILIATION-003.md
- GLOBAL_UI_DESIGN_SYSTEM_AUDIT_2026-11-06.md
- G01_BRANCH_RECONCILIATION_ADDENDUM.md
- G01_COVERAGE_REAUDIT_2026-11-03.md
- G01_FINAL_BUG_RESOLUTION_AUDIT.md
- G03_DEVICE_ACCEPTANCE_PREFLIGHT_2026-11-05.md
- R0_RECONCILIATION_AUDIT_2026-11-06.md
- RESET_BASELINE_4.0.0_2026-11-07.md
- HOME_DOCK_PATCH41_RECOVERY_2026-11-07.md
- HOME_DOCK_VISUAL_PARITY_V2_2026-11-07.md
- HOME_LAYOUT_CALIBRATION_AUDIT_2026-11-08.md
- HOME_LAYOUT_FIX_2026-11-08.md
- HOME_LAYOUT_FIX_2026-11-08_CROSS_AUDIT.md
- PATCH41_DOCK_GLASS_ADOPTION_2026-11-07.md
- UI_REFRESH_MASTER_VISUAL_REFERENCE_AUDIT_2026-11-06.md
- UI_REFRESH_P0_BASELINE_2026-11-06.md
- UI_REFRESH_P1_ACCEPTANCE_DISPOSITION_2026-11-06.md
- UI_REFRESH_P1_FUNCTIONAL_INVENTORY_2026-11-06.md
- UI_REFRESH_P2_SHARED_DESIGN_INTERACTION_CONTRACT_2026-11-06.md
- UI_REFRESH_P3_V2_STATIC_INTERACTION_REAUDIT_2026-11-06.md
- UI_REFRESH_P3_V3_BROWSER_WALKTHROUGH_PLAN_2026-11-06.md
- UI_REFRESH_P3_V3_STATIC_QC_2026-11-06.md
- UI_REFRESH_P3_V4_STATIC_QC_2026-11-06.md
- UI_REFRESH_P3_VISUAL_PROTOTYPE_2026-11-06.md
- UI_REFRESH_P4_IMPLEMENTATION_2026-11-06.md
- UI_REFRESH_TRIGGER4_PROTOTYPE_VISUAL_VALIDATION_2026-11-06.md
- UI_REFRESH_TRIGGER4_V5_STATIC_QC_2026-11-06.md
- UI_REFRESH_G1_CHECKPOINT_2026-11-06.md
- UI_REFRESH_G2_CHECKPOINT_2026-11-06.md
- UI_REFRESH_G3A_CHECKPOINT_2026-11-07.md
- UI_REFRESH_G3B_AUDIT_2026-11-07.md
- UI_REFRESH_G3B_CHECKPOINT_2026-11-07.md
- UI_REFRESH_G3C_CHECKPOINT_2026-11-07.md
- UI_REFRESH_G3C_P1_CHECKPOINT_2026-11-07.md
- UI_REFRESH_G3D_AUDIT_2026-11-07.md
- UI_REFRESH_G3D_CHECKPOINT_2026-11-07.md
- UI_REFRESH_G3E_AUDIT_2026-11-07.md
- UI_REFRESH_G3E_CHECKPOINT_2026-11-07.md
- UI_REFRESH_G3F_AUDIT_2026-11-07.md
- UI_REFRESH_G3F_CHECKPOINT_2026-11-07.md
- UI_REFRESH_G4_AUDIT_2026-11-07.md
- UI_REFRESH_G4_CHECKPOINT_2026-11-07.md
- UI_REFRESH_IMAGE2_BOTTOM_DOCK_CORRECTION_2026-11-07.md
- UI_REFRESH_IMAGE2_HOME_LAYOUT_CHECKPOINT_2026-11-07.md

### Feature audits and engineering evidence
- APK_SIZE_AUDIT.md
- AVATAR_AUDIT.md
- B03_DIALOG_OPACITY_AUDIT.md
- B06_FEEDBACK_AUDIT.md
- B07_MENU_FOCUS_AUDIT.md
- CROSS_PROJECT_WEATHER_AQI_AUDIT.md
- E00_MASTER_INVENTORY_AND_DEPENDENCY_AUDIT.md
- E02_E06_GIF_AVATAR_DOCK_AUDIT_2026-11-05.md
- E02_PROFILE_AVATAR_LOCK_UI_AUDIT.md
- E03_WALLPAPER_METADATA_AUDIT.md
- E04_STANDBY_SCREENSAVER_AUDIT.md
- E05_WIDGETS_INFORMATION_AUDIT.md
- E08_WALLPAPER_SETTINGS_REDESIGN_AUDIT.md
- F01_INTEGRATION_DEPENDENCY_AUDIT.md
- F02_INTEGRATED_FUNCTIONAL_REGRESSION.md
- F03_QR_COMMUNICATION_FEASIBILITY.md
- F04_FEATURE_ISOLATION_AUDIT.md
- F05_PERSISTENCE_RECOVERY_AUDIT.md
- F06_LOCALIZATION_UI_AUDIT.md
- F07_PERFORMANCE_STABILITY_AUDIT.md
- GTVH-HOME-DAILY-AYAH-CACHE-STATUS-AND-LABEL-FIX-001-AUDIT.md
- GTVH-HOME-DAILY-AYAH-OFFLINE-RETRY-BACKOFF-001-AUDIT.md
- GTVH-HOME-ISLAMIC-SHELF-AND-PRAYER-STACK-CROSS-AUDIT-001.md
- GTVH-HOME-ISLAMIC-SHELF-ARABIC-TYPOGRAPHY-001-AUDIT.md
- GTVH-HOME-ISLAMIC-SHELF-SINGLE-ANCHOR-FIX-004-AUDIT.md
- GTVH-HOME-ISLAMIC-SHELF-VERTICAL-POSITION-AND-GRID-ROW-SCROLL-FIX-002-AUDIT.md
- GTVH-HOME-PRAYER-SOURCE-LABEL-TRANSPARENCY-001-AUDIT.md
- GTVH-HOME-PRAYER-TIMES-REFRESH-AND-NEXT-SUBUH-FIX-001-AUDIT.md
- GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-001-AUDIT.md
- GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-002-AUDIT.md
- GTVH-HOME-UI-PROPORTION-GLASS-AND-FOCUS-REFINEMENT-003-AUDIT.md
- GTVH_HOME_DOCK_ISLAMIC_SHELF_NAVIGATION_REFINEMENT_001_AUDIT.md
- GTVH_HOME_HEADER_REFINEMENT_001_AUDIT.md
- GTVH_HOME_SCREEN_CROSS_AUDIT_001.md
- GTVH_HOME_WALLPAPER_TEXT_REFINEMENT_001_AUDIT.md
- PROFILE_AUDIT.md
- WALLPAPER_AUDIT.md
- UPSTREAM_FEATURE_ADOPTION_AUDIT_2026-11-05.md
- UPSTREAM_FEATURE_ADOPTION_GIF_DOCK.md

### Prototype directory
- `prototypes/` — 6 entries currently listed: UI_REFRESH_G1_GLOBAL_UI_PROTOTYPE_V6.html, UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V1.html, UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V2.html, UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V3.html, UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V4.html, UI_REFRESH_TRIGGER4_VISUAL_PROTOTYPE_V5.html.

**Catalog rule:** This list accounts for the project-management document set as it existed during this audit. New documents must be registered here and linked from the canonical checkpoint if they change current status or define a new workstream. Historical evidence must not be rewritten merely to make dates/statuses look current.
