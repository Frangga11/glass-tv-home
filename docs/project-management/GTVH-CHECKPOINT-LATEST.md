# GTVH-CHECKPOINT-LATEST
**Status:** CANONICAL / SINGLE ENTRY POINT  
**Checkpoint date:** 8 November 2026  
**Repository:** Frangga11/glass-tv-home  
**Canonical documentation branch:** `docs/project-master-inventory`  
**Canonical path:** `docs/project-management/GTVH-CHECKPOINT-LATEST.md`  
**Current Home implementation PR:** [PR #79](https://github.com/Frangga11/glass-tv-home/pull/79)  
**Latest documentation checkpoint commit:** this file's creation commit (branch tip after all supporting-file updates).

---

## 0. Mandatory restart rule — one source, one door

**Every future Glass TV Home session MUST begin by reading this file first.** This is the only canonical checkpoint entry point. Do not select a starting point from chat summaries, old checkpoint names, historical sections in other documents, PR descriptions, branch names, or previous APK artifacts.

Authority order:
1. This file: current checkpoint, decisions, active source candidate, outstanding gates, PR inventory and next actions.
2. The seven supporting project documents listed in Section 8: detailed history and supporting evidence only. Their older status statements are historical and are superseded by this checkpoint.
3. Source code and CI evidence at the exact SHAs stated here.
4. Other PRs/branches: candidate or historical work only until explicitly reconciled into the canonical path.

**Single source of truth:** GitHub repository + canonical docs branch above. ChatGPT conversation/memory is a pointer and convenience copy, never a competing source of project status. Do not create parallel checkpoint documents or alternate restart routes. Any future update must first update this file, then synchronize the supporting documents in the same documentation pass.

**Checkpoint invocation phrase:** “Lanjutkan Glass TV Home dari GTVH-CHECKPOINT-LATEST.”  
The response to this phrase must always load this exact file from `docs/project-management` on `docs/project-master-inventory` before any code change.

## 1. Current project state — authoritative snapshot

### Home Header / whole Home screen
- Workstream: `GTVH-HOME-HEADER-REFINEMENT-001` + whole-Home source cross-audit.
- Active source branch: `fix/home-balanced-frame-001`.
- Active PR: #79, OPEN / DRAFT / UNMERGED; base `fix/home-layout-consolidated-001` (PR #77).
- Current PR #79 HEAD: `bf65621ae46b170294e4333928886fbafa0898b6` (30% shared width + identical 30dp pill geometry; CI #1590 PASS).
- Previous Home implementation baseline: `97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5`; latest pill-size source commits: `d6fa94b913ed2e694a0c3ab4c6990afbdc31d604` (30% width), `370b85b525014c7726262ee8b740e34f4d061f58` (height), `bf65621ae46b170294e4333928886fbafa0898b6` (final XML geometry correction).
- Latest regression-test commit: `1d38edf07fbb894b169d00ccfc0f975f2a9fde58`; current PR tip includes subsequent final XML correction `bf65621ae46b170294e4333928886fbafa0898b6`.
- Prior PR-branch documentation sync head: `c15e4383d7dd3f148b31a19f5e9b85423936b56d`; subsequent commits are the four weather/AQI refinement commits listed below.
- PR #79 remains open, draft and unmerged. Do not merge or retarget without a separately verified reconciliation and owner authorization.

### Approved layout constraints — locked
- Safe frame: **29dp on top, bottom, left and right**. At Mi TV Stick logical density 320dpi (2px/dp), each edge = **58px**.
- Header navigation: **Home → Profile → Settings**, one shared glass capsule, each segment independently clickable/focusable.
- Header glass pill radius 21dp; shared nav capsule radius 20dp; inner segment radius 14dp.
- Preserve existing Dock/Grid card sizing, Grid sizing, app order, shelf content and established working behavior. Do not rework correct components to solve an unrelated issue.
- Header groups are aligned from measured geometry; no arbitrary independent offsets.
- Clock/date visual weight is reduced; time/date formatting and data logic remain unchanged.
- Indonesian/English Header labels and navigation behavior are covered by source contracts.
- Pagar Alam location metadata currently records 800m altitude.

## 2. Fixes completed and evidence

### HOME-001 through HOME-005
1. **HOME-001 Now Playing:** supported active media sessions remain eligible in PLAYING, PAUSED, BUFFERING and CONNECTING; playback icon reflects actual state.
2. **HOME-002 Prayer timetable:** visible timetable is focusable/clickable; click, DPAD Center and Enter open the detail popup; explicit DPAD routes include entry and return paths.
3. **HOME-003 Dock/Grid focus race:** generation-guarded deferred focus; stale callbacks invalidated on newer non-Grid destinations and lifecycle pause.
4. **HOME-004 Weather/AQI focus feedback:** scale and Header highlight are composed in one listener, preventing listener replacement/collision.
5. **HOME-005 atmospheric wallpaper overlay:** Home resume and active minute tick trigger a separate due-gated refresh; 15-minute due gate, overlap guard, dynamic-effects preference guard and refresh-on-enable behavior retained. This remains separate from the G3B Weather/AQI text cache.

### Home / Header source cross-audit
- 63 unique IDs in `activity_main.xml`; no duplicate ID declarations.
- Header actions: Home updates active tab; Profile opens profile popup; Settings opens quick-control popup.
- Location pill click and DPAD Center/Enter route to prayer-zone dialog.
- Weather/AQI click and DPAD routes checked at source level.
- Header center capsule width recomputes from measured left/right/header groups.
- Visible prayer table activation and navigation routes checked.
- Existing Home shelves, daily Ayah, prayer data, media card, app inventory, Dock behavior, wallpaper flow and profile/settings actions were reviewed for source preservation.
- Three additional regression contracts cover location click/DPAD, Home/Profile/Settings actions and Header width recalculation after measured-group changes.

## 3. CI / build — exact evidence

| Gate | Exact commit / run | Result |
|---|---|---|
| Implementation CI #1581 | `97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5` / run `42486917618` | PASS; both required jobs, Android tests and release build |
| Test-only CI #1582 | `49f2777cee006a5afd50a33b85cb90a61a077e40` / run `42487572059` | PASS; 160 Python tests, 1 skipped; all 3 new tests passed; Android unit tests + release build |
| Docs-only tip CI #1583 | `c15e4383d7dd3f148b31a19f5e9b85423936b56d` / run `42487822397` | PASS; both jobs completed SUCCESS |
| Weather/AQI width refinement CI #1587 | `65544d9eea9bd0a5ae5156a9bd58724b1817fd2a` / run `42489445766` | PASS; both jobs, 161 Python tests (1 skipped), Android unit tests + release build |
| 30% / identical 30dp pill geometry CI #1590 | `bf65621ae46b170294e4333928886fbafa0898b6` / run `42490240670` | PASS; both jobs, 161 Python tests (1 skipped), pill regression PASS, Android unit tests + release build |

- Latest release APK diagnostic from CI #1590: **1,627,561 bytes** (artifact #13621062347; ZIP 1,426,708 bytes).
- Candidate test artifact: **#13619209786**. This is the latest candidate artifact recorded for the test-only source/test commit; no installation has occurred.
- CI #1583 validates a documentation-only PR tip; application source did not change after CI #1582.
- Release workflows #37/#38 published an older CI #1545 build for commit `c058935ed4398f5c7964d753fcf35a9cf77989bb`; they are **not** the current PR #79 candidate. Never mislabel those release assets as the latest build.

## 4. Acceptance status — do not overstate

| Area | Status |
|---|---|
| Source implementation and static cross-audit | PASS |
| HOME-001–HOME-005 source fixes | PASS |
| Automated Python regression suite | PASS (160, 1 skipped) |
| Android unit tests and release build | PASS |
| Latest docs-only CI #1583 | PASS |
| Rendered safe-frame / visual clipping on Mi TV Stick | PENDING |
| Full physical DPAD traversal / focus retention | PENDING |
| Runtime media, prayer, weather/AQI, overlay and lifecycle checks | PENDING |
| Runtime crash/log inspection | PENDING |
| Overall whole-Home acceptance | **PARTIAL — NOT FINAL** |

No APK installation, ADB session, physical Mi TV Stick test, device settings/default-launcher change, force-push or PR merge has been performed. These boundaries remain in force unless separately authorized.

## 5. PR inventory and consolidation control

GitHub currently reports **58 OPEN pull requests**. This is a repository-wide inventory, not a claim that all are active or all are defective. Several are stacked branches, parallel implementations, or historical drafts. Do not merge or close them blindly. Their code and dependencies must be compared with the canonical source path before disposition.

### Current Home consolidation cluster
- **#79** — active Home/Header candidate; current source/test/docs validation described above.
- **#77** — direct base/dependency of #79; consolidated Dock geometry and deterministic DPAD viewport work. Must be reviewed as the dependency, not merged automatically.
- **#78** — parallel H01 Header reskin branch based on `main`; competing/parallel Header work. Reconcile its unique changes against #79 before deciding superseded/retain.
- **#74, #75, #76** — earlier stacked Home/Dock layout branches. Compare their unique commits and test evidence with consolidated PR #77/#79; do not require merging all four as a chain and do not discard unreviewed unique work.

### All currently open PRs — immutable inventory snapshot
The table records the current GitHub head/base refs and head SHA prefix as observed during this checkpoint. It is an inventory for systematic reconciliation, not approval to merge.

| PR | Title | Head branch | Head SHA (12) | Base branch | State |
|---|---|---|---|---|---|
| #79 | fix(home): balance main-screen safe frame and restore header navigation | `fix/home-balanced-frame-001` | `c15e4383d7dd` | `fix/home-layout-consolidated-001` | Draft |
| #78 | feat(h01): header reskin preserving approved Home tab order | `feature/h01-header-reskin` | `aca3c6b8a9cf` | `main` | Draft |
| #77 | Home layout: consolidate Dock geometry and deterministic DPAD viewport fix | `fix/home-layout-consolidated-001` | `d6a5581d2975` | `feature/g4-parent-pin-hardening` | Draft |
| #76 | Home layout: keep Quick Toggles inside the safe screen frame | `fix/home-global-layout-bounds` | `f085c41356e2` | `fix/home-dock-alignment-row-scroll` | Draft |
| #75 | Home Dock: align full-width tray and scroll app rows in place | `fix/home-dock-alignment-row-scroll` | `21bc204a413f` | `fix/home-dock-visual-parity-v2` | Draft |
| #74 | Home Dock: adopt Patch 4.1 layout, glass and breathing focus | `fix/home-dock-visual-parity-v2` | `61d4e429be12` | `feature/g4-parent-pin-hardening` | Draft |
| #72 | G4: Harden parent PIN storage without changing retry behavior | `feature/g4-parent-pin-hardening` | `7146de589533` | `feature/g3f-global-settings` | Draft |
| #71 | G3F — Global Settings | `feature/g3f-global-settings` | `455b619c3eef` | `feature/g3e-wifi` | Draft |
| #70 | G3E — Wi-Fi V05 | `feature/g3e-wifi` | `9125043e24b8` | `feature/g3d-quick-settings` | Draft |
| #69 | G3D — Quick Settings V04 | `feature/g3d-quick-settings` | `bbc7b240d2ad` | `feature/g3c-profile-avatar-child-mode` | Draft |
| #68 | G3C — Profile, Avatar & Child Mode | `feature/g3c-profile-avatar-child-mode` | `8cf81f84bd62` | `feature/g3b-weather-aqi-v4` | Draft |
| #67 | G3B: Weather & AQI V4 dashboard | `feature/g3b-weather-aqi-v4` | `8cfc5c886950` | `feature/g3a-home-independent-dock` | Draft |
| #66 | G3A — Home Screen & Independent Dock | `feature/g3a-home-independent-dock` | `87302f52de84` | `feature/g2-shared-ui-foundation-repair` | Draft |
| #65 | G2: repair shared Global UI foundation | `feature/g2-shared-ui-foundation-repair` | `40fa0715bdbc` | `integration/b-series` | Draft |
| #64 | UI Refresh P4-01 — Home Screen & Dock: isolated preference foundation | `feature/ui-global-01-home-dock` | `e4bc99c9a482` | `integration/b-series` | Draft |
| #63 | feat(E06): add independent Dock presentation preference model | `feature/e06-dock-capacity-control-v1` | `ecdeb0eba9fc` | `feature/e06-home-screen-subtabs` | Draft |
| #62 | feat(E02): prepare upstream GIPHY avatar catalog integration | `feature/e02-giphy-avatar-search-v1` | `588456144df2` | `feature/e02-profile-avatar-lock-ui` | Draft |
| #56 | F07 — Performance, crash, freeze and stability hardening | `integration/project-f07-performance-stability` | `28e841a3475c` | `integration/project-f06-localization` | Draft |
| #55 | F06 — Bilingual localization and global UI consistency | `integration/project-f06-localization` | `2c369a0a48c7` | `integration/project-f05-persistence-recovery` | Draft |
| #54 | F05 — Persistence and Recovery Validation (Draft) | `integration/project-f05-persistence-recovery` | `1048b0b68ae5` | `integration/project-f04-feature-isolation-v2` | Draft |
| #53 | F04: verify Avatar, Wallpaper and StandBy feature isolation | `integration/project-f04-feature-isolation-v2` | `6ffc8b4c253b` | `integration/project-f02-system-validation` | Draft |
| #51 | D01 follow-up: restrict location configuration broadcasts | `fix/d01-location-broadcast-authorization` | `66352838e615` | `feature/d01-config-storage-audit` | Draft |
| #50 | F02 — Integrated System Validation (Draft) | `integration/project-f02-system-validation` | `2ede452b3137` | `integration/project-f-staged-full` | Draft |
| #41 | E08: unify wallpaper settings into three focused controls | `feature/e08-wallpaper-settings-redesign` | `edd24d72256d` | `feature/e07-animation-effects` | Draft |
| #40 | E07: audit and respect reduced motion for visual effects | `feature/e07-animation-effects` | `a0f2b0fff6ee` | `feature/e06-home-screen-subtabs` | Draft |
| #39 | E06: audit and localize Home Screen controls | `feature/e06-home-screen-subtabs` | `570b7c83efae` | `feature/e05-information-center-ayah` | Draft |
| #38 | E05: complete ayah rendering and audit information widgets | `feature/e05-information-center-ayah` | `6fe89fad0786` | `feature/e04-standby-screensaver` | Draft |
| #37 | E04: harden StandBy display power transition | `feature/e04-standby-screensaver` | `08076bb945a1` | `feature/e03-wallpaper-clean-metadata` | Draft |
| #36 | E03: separate Default wallpaper metadata from image content | `feature/e03-wallpaper-clean-metadata` | `e085f8aed6fd` | `feature/e02-profile-avatar-lock-ui` | Draft |
| #35 | E02: audit profile avatars, QR upload and app-lock UX | `feature/e02-profile-avatar-lock-ui` | `3ab9b55f3cfe` | `feature/e01-device-settings-home-control` | Draft |
| #34 | E01: improve device settings, HOME control and system actions | `feature/e01-device-settings-home-control` | `edd6461c9327` | `feature/d07-concurrent-profile-content-state` | Draft |
| #31 | D07: prevent stale and concurrent profile/content state writes | `feature/d07-concurrent-profile-content-state` | `7ce92d309112` | `feature/d06-process-death-recovery` | Draft |
| #30 | D06: restore persisted prayer alarms after TV reboot | `feature/d06-process-death-recovery` | `1bb6ce9bf04d` | `feature/d05-standby-photo-atomic-persistence` | Draft |
| #29 | D05: make StandBy photo persistence atomic and failure-aware | `feature/d05-standby-photo-atomic-persistence` | `7f043943ab0c` | `feature/d04-profile-avatar-state-sync` | Draft |
| #28 | D04: synchronize profile and avatar selection state | `feature/d04-profile-avatar-state-sync` | `302a3aa1a7fa` | `feature/d03-locked-app-ui-refresh` | Draft |
| #27 | D03: refresh locked and hidden app summaries | `feature/d03-locked-app-ui-refresh` | `7380796a69e2` | `feature/d02-settings-ui-propagation` | Draft |
| #26 | D02: audit immediate settings UI propagation | `feature/d02-settings-ui-propagation` | `909aaff5709a` | `feature/d01-config-storage-audit` | Draft |
| #22 | C05: unify and harden shared upload body reception | `feature/c05-shared-upload-pipeline` | `53cf9a883f06` | `feature/c04-http-endpoint-routing` | Draft |
| #25 | D01: audit settings and configuration storage ownership | `feature/d01-config-storage-audit` | `cd710681099f` | `feature/c07-upload-session-resilience` | Draft |
| #21 | C04: harden HTTP endpoint routing and response contracts | `feature/c04-http-endpoint-routing` | `aad9aeb163b4` | `feature/c03-phone-tv-network-verification` | Draft |
| #24 | C07: harden upload disconnect, retry, expiry and cancellation | `feature/c07-upload-session-resilience` | `2315f12472f4` | `feature/c06-upload-file-validation` | Draft |
| #20 | C03: verify phone-to-TV active LAN communication | `feature/c03-phone-tv-network-verification` | `0599ceb01a55` | `feature/c02-tv-local-server-ip` | Draft |
| #23 | C06: validate upload file types, dimensions, names and storage paths | `feature/c06-upload-file-validation` | `2bf119da4582` | `feature/c05-shared-upload-pipeline` | Draft |
| #19 | C02: audit TV local server and IP discovery | `feature/c02-tv-local-server-ip` | `b0e1ff048e36` | `feature/c01-local-communication-audit` | Draft |
| #18 | C01: harden local QR upload server foundation | `feature/c01-local-communication-audit` | `8a3a981f097e` | `feature/b07-menu-focus-restoration` | Draft |
| #17 | B07: restore Settings menu, submenu, and D-pad focus | `feature/b07-menu-focus-restoration` | `f62bdc5d9804` | `feature/b06-notification-feedback` | Draft |
| #16 | B06: harden notification feedback and network timeout handling | `feature/b06-notification-feedback` | `5b2f10095bd5` | `integration/b-series` | Draft |
| #12 | B03: improve dialog and popup background opacity | `feature/b03-dialog-opacity-layering` | `d87ffdf91485` | `feature/b02-remote-focus-indicators` | Draft |
| #11 | B02: make D-pad focus states visible and consistent | `feature/b02-remote-focus-indicators` | `c746063cb08e` | `feature/b01-global-navigation-retention` | Draft |
| #10 | B01: persist launcher navigation and cursor position | `feature/b01-global-navigation-retention` | `389e289e9392` | `feature/profile-avatar-security-integration` | Draft |
| #9 | Integrate profile avatar QR upload with profile security | `feature/profile-avatar-security-integration` | `7e8392e1a762` | `feature/profile-security-integration` | Draft |
| #8 | Profile security: parent PIN gates and isolated profile switching | `feature/profile-security-integration` | `1ba744efee61` | `fix/wallpaper-qr-session-security` | Draft |
| #7 | Wallpaper QR security and single Default Dempo wallpaper | `fix/wallpaper-qr-session-security` | `62b1e7ce63f3` | `feature/settings-reorganization-standby-gallery` | Draft |
| #6 | Avatar QR upload: single-photo replacement and cleanup | `fix/avatar-qr-upload-single-photo` | `0cb1b96e2c25` | `feature/settings-reorganization-standby-gallery` | Draft |
| #5 | Add custom photo avatars to Glass TV profiles | `fix/avatar-photo-upload-audit` | `29a5a8bb8b2f` | `feature/settings-reorganization-standby-gallery` | Draft |
| #4 | Reorganize settings and add personal-photo StandBy mode | `feature/settings-reorganization-standby-gallery` | `06f4fa2adf73` | `main` | Draft |
| #3 | Separate weather and icon glow effects; localize launcher to Indonesian | `feature/independent-visual-effects-id` | `888471fe1f6b` | `main` | Ready |
| #2 | Fix Android TV power-off accessibility selection | `fix/power-off-accessibility` | `ed320d03d5e2` | `main` | Ready |

**Disposition rule for every PR above:** classify as (A) already represented in canonical source, (B) unique changes to port/reconcile, (C) dependency needed by active work, (D) superseded duplicate to close only after diff/evidence review, or (E) unresolved defect requiring repair. Record the evidence, exact source SHA, CI status and decision in the audit register and decision log. No PR may be silently omitted. No PR is “completed” merely because it is Draft or old.

### Known dependency note
PR #79 is stacked on #77. Earlier comparison found one workflow-only commit after the common merge base on #77; it is not a functional source conflict. Preserve the dependency and reconcile ancestry before any merge; do not cherry-pick an equivalent workflow-only change just to make branch history look linear.

## 6. Single ordered next-work plan

1. **Freeze this checkpoint as the sole restart point.** Verify the canonical file and docs branch tip.
2. **PR reconciliation audit:** inspect all 58 open PRs and their unique diffs, dependencies, CI evidence and issue history. Start with #79/#77/#78/#74–76, then walk the remaining chains in dependency order.
3. **Build one canonical integration graph:** identify the authoritative branch/PR for each retained feature; eliminate duplicate/parallel paths only after proving their unique changes are represented or intentionally rejected.
4. **Resolve outstanding source defects:** only confirmed defects; narrow patches; add regression tests; CI on exact resulting HEAD.
5. **Synchronize documentation in one pass:** update this checkpoint first, then README, Master Project, Inventory, Decision Log, Audit Register, Handover and the dedicated audit report. All must point back here.
6. **Prepare physical acceptance only after source/CI/reconciliation gates pass** and after explicit owner authorization. Use the Mi TV Stick baseline: Android 11, 1920×1080, density 320dpi.
7. **Final merge/release decision:** only after dependencies, exact-head CI, PR reviews and device acceptance are all reconciled and owner-approved.

## 7. Quality-control rules — mandatory

- No speculative UI edits; preserve verified-good sizing, placement, behavior and app order.
- Never claim runtime/device acceptance from static source review or CI.
- Never use an older APK/release artifact as the current candidate.
- Never merge, close, force-push, install, ADB, or change device settings without explicit authorization.
- Record every source edit with branch + exact commit SHA + changed files + reason + tests + CI run + artifact.
- Record every unresolved issue with owner decision, severity, dependency and next action.
- Any stale or contradictory documentation must be corrected or explicitly marked historical; no parallel “latest” checkpoint.
- If any required evidence is unavailable, label the gate PENDING/UNKNOWN rather than infer PASS.

## 8. Supporting documents — detail only, not alternate entry points

All are on `docs/project-master-inventory` and must link back to this checkpoint:
- `README.md` — project overview and navigation.
- `MASTER_PROJECT.md` — goals, requirements, architecture and master history.
- `INVENTORY.md` — component/source inventory.
- `DECISION_LOG.md` — approved decisions and rationale.
- `AUDIT_REGISTER.md` — defects, evidence, remediation and dispositions.
- `HANDOVER.md` — operator handoff and restart procedure.
- `GTVH_HOME_SCREEN_CROSS_AUDIT_001.md` — detailed Home source cross-audit.
- PR-local `HOME_LAYOUT_RECONCILIATION_2026-11-08.md` is historical/implementation evidence only; it is not a restart point.

Supporting-document synchronization commits:
| File | Commit |
|---|---|
| README.md | `8ad0db3c41c4343c0081c1822d35d1d873bd193a` |
| MASTER_PROJECT.md | `fa83007db9e8ed4b521e56b96025953da76f84b6` |
| INVENTORY.md | `227c6ddae335f606a5d05ab8130f90d5bbfe77fb` |
| DECISION_LOG.md | `fc7832cb30305535fe3dcb0d3daa178d449295bc` |
| AUDIT_REGISTER.md | `7da5adf23060083f0fbec33d95f3acb01073a12c` |
| HANDOVER.md | `d680e638435311e1381a0e98c356ff732bcdab75` |
| GTVH_HOME_SCREEN_CROSS_AUDIT_001.md | `f9e9232ad75e21ac9955bdfe7c4ddbc51ea4c039` |

## 9. Active refinement — GTVH-HOME-WEATHER-PILL-WIDTH-REFINEMENT-002 (8 November 2026)

- Owner's revised decision: **both capsules use 30% of measured Header width**. Location–altitude remains the sizing reference; weather/AQI must match it exactly.
- Runtime implementation: `MainActivity.applyHeaderWidths()` computes `Math.round(width * 0.30f)` once and assigns that exact pixel width to both `locationPill` and `weatherAirQuality`. Both retain the same parent-start anchor.
- Physical geometry: both capsules are **30dp high**. Location is unchanged and remains the reference. Weather uses 30dp height, 12dp start/end padding, 0dp top/bottom padding and vertical centering, matching the location container's default vertical padding and center gravity. Both use the same `header_glass_pill_bg` background/radius. Existing 8dp vertical separation, fonts, icons, content, provider, click/DPAD/focus and animations are preserved. Weather remains single-line/end-ellipsized.
- At measured inner Header width 1,804px, 30% calculates to **541px** (rounded). At 320dpi, 30dp height corresponds to **60px**. These are source/layout calculations, not rendered-device measurements.
- Changed source files only: `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`, `launcher/app/src/main/res/layout/activity_main.xml`, and `tests/test_home_layout_bounds_contract.py`. The regression contract checks shared 30% width, both layout width assignments, common left anchor, equal 30dp height, horizontal padding, vertical centering, weather ellipsis and content-refresh invariance.
- Current PR #79 HEAD: `bf65621ae46b170294e4333928886fbafa0898b6`. PR remains OPEN / DRAFT / UNMERGED; no merge or retarget.
- CI #1590 / run `42490240670`: **BOTH JOBS SUCCESS**. Python: **161 tests, OK (1 skipped)**; new pill-size regression PASS. Android `testReleaseUnitTest` and `assembleRelease`: SUCCESS.
- Release APK: **1,627,561 bytes**. Artifact ID `13621062347`, ZIP size 1,426,708 bytes, expires 15 November 2026. Extracted APK SHA-256: `51c6921ad7055812dd4c3de844bce26516b0ffeea6dd8401abc5d94913dcb56d`. APK delivered to owner for Mi TV Stick test.
- Physical acceptance is **PENDING**. Owner to test on Mi TV Stick, 1920×1080 / 320dpi, Indonesian and English, short/long location/weather/AQI values; verify clipping, actual capsule bounds/height, left/right edge equality, vertical gap, focus/click/DPAD and unaffected Header components. Do not claim physical success until owner reports results.

## 10. Final checkpoint decision

**Current source/CI result:** PASS; CI #1590 and release APK build successful.  
**Overall Home acceptance:** PARTIAL / NOT FINAL; APK handed to owner; physical Mi TV Stick acceptance remains pending.  
**Repository cleanup:** NOT FINAL; 58 open PRs require evidence-based reconciliation.  
**Documentation architecture:** one canonical checkpoint entry point established by this file; supporting documents are subordinate detail/history.

**Next session must start here and nowhere else.**


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

# CHECKPOINT RESMI TERBARU — GTVH-APK-DIRECT-DELIVERY-001

**Tanggal:** 8 November 2026  
**Status:** BUILD PASS · DIRECT APK RELEASE VERIFIED · DEVICE DOWNLOAD ACCEPTANCE PENDING

## 1. Tujuan checkpoint
Menetapkan jalur build dan distribusi APK Glass TV Home yang terbukti menghasilkan aset Android .apk langsung, serta mencegah pengulangan kesalahan pengiriman .apk.bin, ZIP, atau BIN.

## 2. Source of truth
- Repository: Frangga11/glass-tv-home
- PR aktif: https://github.com/Frangga11/glass-tv-home/pull/79 — OPEN / DRAFT / UNMERGED
- Source branch: fix/home-balanced-frame-001
- Base branch: fix/home-layout-consolidated-001
- Exact source HEAD used for successful build: b4abdf0bd555a0106c135add20c3f287cf10b412
- Corrected publisher workflow is present on the PR base branch (commit 7e51a86cc83394ae47f7c9d32c22bf0c6042c9d1) and aligned on PR head branch (commit b4abdf0bd555a0106c135add20c3f287cf10b412).

## 3. Root cause — permanently recorded
The old publisher workflow hardcoded CI #1545's artifact ID/run ID and source SHA. More importantly, for a pull_request event, GitHub used the workflow definition from the PR base branch, not the PR head branch. Editing only the PR head copy therefore did not change the workflow GitHub executed. The old run log proved it republished the stale CI #1545 APK. Correction: maintain the workflow on the PR base branch and keep the PR head copy aligned to avoid regression after merge.

## 4. Mandatory build and delivery route (DO NOT DEVIATE)
1. Confirm PR number, base/head branches, and exact current source HEAD.
2. Ensure .github/workflows/publish-apk-release.yml is corrected on the PR base branch; align the same file on PR head branch.
3. Trigger a PR #79 synchronize event (or an authorized manual workflow dispatch if available). Do not assume a commit on the head branch alone controls the workflow definition.
4. Workflow must checkout github.event.pull_request.head.sha, run ./gradlew --no-daemon testReleaseUnitTest assembleRelease, validate APK package/manifest, and calculate size and SHA-256.
5. Publish the actual APK binary as a GitHub Release asset with a unique source-SHA-derived tag and filename ending exactly .apk.
6. Verify the Release API asset: exact filename ends .apk, state=uploaded, MIME application/vnd.android.package-archive, size and SHA-256 match workflow logs.
7. Send the user only the direct GitHub Release asset URL (the URL path itself must end in .apk). Never send sandbox:/mnt/data attachments, GitHub Actions artifact ZIPs, .zip, or .bin. Never rename a ZIP to .apk.
8. If a direct APK Release asset cannot be verified, stop and report the blocker; do not substitute another format or stale build.
9. Physical Samsung Browser download test and Mi TV Stick installation/visual acceptance remain pending until owner confirms.

## 5. Latest verified build
- Publisher workflow run: https://github.com/Frangga11/glass-tv-home/actions/runs/42491530852 — SUCCESS.
- Job: 128764760421 — SUCCESS.
- Build command: ./gradlew --no-daemon testReleaseUnitTest assembleRelease.
- Unit tests and assembleRelease: PASS; log reports BUILD SUCCESSFUL.
- APK filename: Glass-TV-Home-PR79-b4abdf0bd555a0106c135add20c3f287cf10b412.apk.
- Size: 1,627,561 bytes.
- SHA-256: 2bd779dc0da3a8cbf81b7f6f7dad1ee53d6f7be949eef9261d1d0b7018c09aa6.
- Release tag: glass-tv-home-pr79-b4abdf0bd555a0106c135add20c3f287cf10b412.
- Direct APK asset: https://github.com/Frangga11/glass-tv-home/releases/download/glass-tv-home-pr79-b4abdf0bd555a0106c135add20c3f287cf10b412/Glass-TV-Home-PR79-b4abdf0bd555a0106c135add20c3f287cf10b412.apk
- Release API independently confirms one uploaded asset, exact .apk filename, MIME application/vnd.android.package-archive, matching size and digest.

## 6. Acceptance boundary / next action
- Build: PASS.
- Release asset: PASS — direct APK verified at GitHub.
- Samsung Browser filename behavior: NOT YET CONFIRMED by owner.
- Mi TV Stick installation and visual acceptance: PENDING.
- PR #79 remains OPEN / DRAFT / UNMERGED. No merge authorized by this checkpoint.

**Resume instruction:** Start from this checkpoint. Do not repeat the stale CI #1545 publisher, do not edit only the PR head workflow when the PR base workflow is authoritative, and do not send sandbox/ZIP/BIN. First obtain the owner's Samsung Browser download and Mi TV Stick test result; if another build is needed, follow section 4 exactly.

## 11. Active scoped refinement — GTVH-HOME-WALLPAPER-TEXT-REFINEMENT-001 (8 November 2026)

**Status: SOURCE IMPLEMENTATION PASS · CI PASS · VISUAL/DEVICE ACCEPTANCE PENDING · NOT FINAL**

- Scope PR: [#80](https://github.com/Frangga11/glass-tv-home/pull/80) — OPEN / DRAFT / UNMERGED.
- Source branch: `fix/home-wallpaper-text-refinement-001`.
- Exact PR #80 HEAD: `c7d95c3de24bc70b671159d5aa9ef49451cbe112`.
- Base: `fix/home-balanced-frame-001`, exact base HEAD `b4abdf0bd555a0106c135add20c3f287cf10b412` (PR #79 remains OPEN / DRAFT / UNMERGED).
- Changed files: `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`; `launcher/app/src/main/res/layout/activity_main.xml`; new `tests/test_home_wallpaper_text_refinement_contract.py`.

### Approved implementation and measured geometry
- Wallpaper shelf order: location → title → description. Existing `WallpaperMetadata` values remain unchanged: `Pagar Alam, Sumatera Selatan`; `Kabut Pagi Dempo`; `Udara sejuk turun perlahan, menyelimuti lereng dan kebun teh Pagar Alam.` Location capitalization is now preserved as authored (not forced to uppercase).
- Both information shelves share the existing Dock-relative `heroBottom` anchor. Conversion: `5mm × 160 / 25.4 = 31.496dp`; at density `2.0` (320dpi), `62.992px`, rounded to **63px**. Existing 50dp shelf-to-tray gap becomes approximately **18.5dp** after the shift; a 12dp minimum-gap clamp prevents collision on other density/layout cases. No Dock geometry or global safe-frame values were changed.
- Prayer header, names and times receive restrained cool-to-warm gradients. Prayer data, order, provider and interaction remain unchanged.
- Daily ayah idle outline is removed and the open hint border is removed. A restrained 1dp focus outline remains while the ayah block has remote focus; existing click/DPAD activation remains intact.

### CI / build evidence
- CI run `42493469262`: both jobs SUCCESS.
- Python contract suite: **166 tests, OK (1 skipped)**; all five new GTVH wallpaper refinement contracts passed.
- Android `testReleaseUnitTest` and `assembleRelease`: PASS; log reports `BUILD SUCCESSFUL in 1m 37s`.
- Release APK diagnostic: **1,627,937 bytes** (about 1,589 KB), +376 bytes against the prior PR #79 recorded APK size of 1,627,561 bytes. The workflow emitted a warning above its historical 1.5 MiB reference; this is explicitly non-blocking.
- PR #80's APK-artifact upload step is skipped by the current workflow scope (which only publishes visual-test artifacts for PRs #74–#79). No user-facing APK release or device install was performed.

### Regression boundaries and remaining acceptance
- PR diff is limited to the two production files above plus one new regression test. `DockGeometry.java`, Dock/Grid sizes/order/scroll, Header and capsules, navigation/focus routes, global safe frame, wallpaper image/provider/rotation, prayer data and unrelated components are untouched.
- **Pending:** rendered visual review at 1920×1080; physical Mi TV Stick test for exact shelf movement, Dock collision, clipping, bright/dark wallpaper contrast, prayer legibility, daily ayah frame and remote focus.
- Do not merge PR #80, install an APK, or claim overall final acceptance until the owner completes the physical test.
- Detailed evidence: `GTVH_HOME_WALLPAPER_TEXT_REFINEMENT_001_AUDIT.md`.

**Next action:** owner visual/physical acceptance on Mi TV Stick; if corrections are required, keep changes strictly inside the approved five-component scope.


## 12. Owner-approved corrective refinement — GTVH-HOME-WALLPAPER-TEXT-REFINEMENT-001 (8 November 2026)

**Current status: SOURCE CHANGES COMMITTED · EXACT-HEAD CI PENDING · RENDERED/DEVICE ACCEPTANCE PENDING · NOT FINAL**

This section supersedes the earlier downward 5mm movement and 12dp minimum-gap values recorded in Section 11. The previous CI result applies only to the prior PR #80 HEAD, not to the corrective HEAD below.

- PR: [#80](https://github.com/Frangga11/glass-tv-home/pull/80), OPEN / DRAFT / UNMERGED.
- Source branch: `fix/home-wallpaper-text-refinement-001`.
- Corrective HEAD: `a085be150ebcf1d48b825ed7ac43b172261f3aa0`.
- Base branch / SHA: `fix/home-balanced-frame-001` / `b4abdf0bd555a0106c135add20c3f287cf10b412`.
- Changed files remain limited to two production files and one scoped regression test. The PR's existing workflow file remains part of the inherited PR diff; this corrective pass did not alter it.

### Corrective visual geometry
- The prior implementation moved the Home information shelves downward by 5mm (31.5dp at density 2.0), leaving only about 4.5dp of visual clearance above the tray. This is superseded.
- New wallpaper title/description baseline uses a 3.5dp offset against the existing 50dp Dock-relative geometry, giving a nominal **32.5dp** clearance above the tray at 320dpi. A **32dp minimum** clamp protects other density/layout cases. This moves the wallpaper shelf upward about **28dp** from the prior PR #80 rendering; the small difference from the original 24dp target is intentional to honor the approved 32–40dp Dock clearance.
- Daily Ayah parent shelf is offset so the Ayah block rises **18dp** from the prior PR #80 position.
- Prayer header and table each receive an additional **6dp** upward translation, so the timetable rises **24dp** total.
- No Dock geometry, safe-frame, grid, header or capsule measurements were changed.

### Text clarity correction
- Prayer header now uses solid high-contrast light text. Prayer names/times use solid white/warm active-prayer colors. Removed the per-glyph text gradient shader and runtime `Typeface.DEFAULT` overrides that had bypassed the XML Inter typeface.
- Prayer names across all six columns: **13sp**; times across all six columns: **17sp**. Existing timetable data, order, provider and activation remain unchanged.
- Daily Arabic: **20sp at 1080p / 22sp at 4K**, preserving Amiri Quran font, RTL direction and high-quality line breaking.
- Translation: **14sp at 1080p / 15sp at 4K**.
- Added a subtle transparent-to-dark gradient scrim behind the right information shelf; no bordered panel or idle frame was added. Existing focus-only feedback and click/DPAD activation remain.

### Verification boundary
- Exact-head workflow lookup did not yet return a run for corrective HEAD `a085be1`; exact-head CI is **PENDING**, not PASS.
- Prior HEAD `c7d95c3` had CI #1592 / run `42493469262` PASS (166 Python tests, 1 skipped; Android release unit tests and assembleRelease PASS). That evidence does not validate the corrective commits.
- Rendered 1920×1080 review and physical Mi TV Stick acceptance remain PENDING.
- No APK installation, ADB session, device setting change, PR merge or force-push was performed.
- Explicitly protected: Dock/Grid geometry/order/scroll/focus, Header/navigation/capsules, wallpaper image/crop/provider/rotation, prayer data/provider/order, safe frame and unrelated components.

**Next action:** monitor exact-head CI run `42497894475` (#1599) for HEAD `a085be1`; then inspect the rendered 1920×1080 composition and wait for owner Mi TV Stick visual acceptance. Do not merge or install automatically.


## 13. Exact-head CI completed — GTVH-HOME-WALLPAPER-TEXT-REFINEMENT-001 (8 Nov 2026)

**Status: SOURCE PASS · EXACT-HEAD CI PASS · RENDERED / PHYSICAL ACCEPTANCE PENDING · NOT FINAL**

- PR #80 remains OPEN / DRAFT / UNMERGED. Current source HEAD: `a085be150ebcf1d48b825ed7ac43b172261f3aa0`; base PR #79 SHA `b4abdf0bd555a0106c135add20c3f287cf10b412`.
- Exact-head CI #1599 / run `42497894475`: **SUCCESS**. Both jobs succeeded: `Launcher build and unit tests` and `Lists, tools and docs`.
- Python regression suite: **167 tests, OK (1 skipped)**. All six wallpaper-text refinement contracts passed, including explicit checks for all six prayer labels at 13sp and all six prayer times at 17sp.
- Android command `./gradlew --no-daemon testReleaseUnitTest assembleRelease`: **BUILD SUCCESSFUL in 1m 18s**. Both `:app:testReleaseUnitTest`, `:installer:testReleaseUnitTest`, and both release assemble tasks passed.
- Release APK diagnostic size: **1,627,685 bytes**. Historical 1.5 MiB warning is non-blocking; no release APK was published. The separate publisher workflow #54 was SKIPPED by design for PR #80.
- CI generated visual-parity artifact `glass-tv-home-dock-visual-parity-4e99e1ba497ae2724c46abfdff28087f484679cb`, artifact ID `13624193020`, ZIP size `1,426,812` bytes, expires 15 Nov 2026. This is a workflow artifact ZIP, not a direct APK release asset; do not present it as a direct APK download.
- PR total diff has 4 files: CI workflow, MainActivity.java, activity_main.xml, and the scoped regression test. The CI workflow adjustment enables the Home layout visual-parity artifact for PR #80; the corrective application edits remain limited to MainActivity.java and activity_main.xml, plus the focused test. No DockGeometry/Dock/Grid/Header/capsule/navigation/safe-frame/wallpaper provider/prayer data code was edited.
- No rendered 1920×1080 screenshot has been independently reviewed and no physical Mi TV Stick test, ADB session, installation, device setting change, or merge was performed.

**Next action:** inspect rendered 1920×1080 visual output and owner acceptance on Mi TV Stick. Keep PR #80 draft and unmerged until physical review passes. No APK release delivery yet.
