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
