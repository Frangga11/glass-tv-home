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
