[object Object]

## Latest resume checkpoint — B06 complete, 1 November 2026

- Current completed workstream: B06 Notification Feedback.
- Source branch/head: `feature/b06-notification-feedback` / `bc86a250d73a5cbb83250217d42c3c3993cf29d7`.
- PR #16: OPEN, DRAFT, UNMERGED; target `integration/b-series`. Keep unmerged under D-013 until A–G source work is complete.
- Exact-head CI run #295 (`41280874732`) passed both jobs. APK publication skipped.
- 108 Toast call sites across 10 app-layer Java files now use `TvFeedback`; resource parity is 566/566/566 across default/ID/EN with zero duplicates/mismatches.
- OTA loading is lifecycle-safe, supports unknown/known progress, safe cancellation, and partial APK cleanup. QR upload pages have progress and timeout feedback; operation-specific timeout budgets are preserved.
- Detailed record: [B06_FEEDBACK_AUDIT.md](B06_FEEDBACK_AUDIT.md).
- Next stage: B07 Activity/dialog lifecycle and return-focus audit. Do not re-open B06 unless B07 or integrated regression finds a reproducible dependency defect.
- Final API 30/33+, D-pad/focus, same-Wi-Fi phone QR, and physical TV checks remain deferred to final F-stage acceptance, not waived. No APK installed and no device settings changed.

## Latest resume checkpoint — 1 November 2026

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


## Latest resume checkpoint — B07 implementation and CI, 1 November 2026

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


## Latest resume checkpoint — B07 source and CI complete, 1 November 2026

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


## Latest resume checkpoint — C03 source and CI complete, 1 November 2026

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


## Latest resume checkpoint — C04 source and CI complete, 1 November 2026

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


## Latest resume checkpoint — Project D D01, 1 November 2026

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
