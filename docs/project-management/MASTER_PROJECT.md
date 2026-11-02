## Historical B06 completion checkpoint — 1 November 2026

- B06 source implementation and static quality checks are complete on `feature/b06-notification-feedback`; exact verified head: `bc86a250d73a5cbb83250217d42c3c3993cf29d7`.
- Draft PR #16 targets `integration/b-series`, remains OPEN/DRAFT/UNMERGED under D-013. No merge was performed.
- Migrated 108 Toast call sites across 10 app-layer Java files to the shared lifecycle-safe `TvFeedback` delivery path.
- OTA feedback/loading now handles HTTP failures, unknown/known download length, incomplete APK cleanup, pre-install file closure, and user cancellation.
- Avatar/Wallpaper/Standby QR pages now have progress and timeout feedback. Browser timeouts are 240s (Avatar) and 600s (Wallpaper/Standby); socket idle budgets remain distinct at 90s (Avatar) and 30s (Wallpaper/Standby); Avatar QR session remains 5 minutes.
- Wallpaper category synchronization now reports success/failure.
- Resource parity: 566 keys in default/ID/EN; zero duplicates, key differences, or placeholder mismatches.
- Exact-head GitHub Actions CI run #295 (`41280874732`) PASS: launcher unit tests/release build and lists/tools/docs jobs succeeded. APK publication was skipped.
- No APK was published/installed and no device/TV settings were changed.
- Final API 30/33+ lifecycle/focus, real-phone QR over same Wi-Fi, and physical TV acceptance remain deferred to final F-stage overall system test; not waived.
- Detailed evidence: [B06_FEEDBACK_AUDIT.md](B06_FEEDBACK_AUDIT.md). B07 is next; grouped merge remains deferred until A–G source work is complete.

## Historical B04/B05 integration QC checkpoint — 1 November 2026

- Integration branch `integration/b04-b05-reconciled`; exact head `89d769689eb566671725c52982e0b73c4f1e8c31`; Draft PR #15 targets B04 head. Source PR #13 and #14 remain open; no merge.
- B05 expanded beyond native Settings copy: profile/app-lock prompts, avatar QR page and HTTP errors, wallpaper/Standby QR pages and upload errors, weather mood picker, location setup, restart/power feedback, and radar illustration label now use Indonesian/English strings.
- Resource QC: 560 keys in default/ID/EN; zero duplicate keys, zero locale key-set differences, zero printf-placeholder mismatches. `LocalizationResourceParityTest` now fails on duplicate keys instead of silently overwriting them.
- App-wide scan covered all 68 Java files and all 17 layout XML files. Changed Java files: 21; 534 `R.string` references checked, zero missing. Layouts: 71 `@string` references checked, zero missing. No literal UI text remains in the 17 layouts except fixed weather preview values/location and the account branding handle.
- Remaining Java string literals are in language-aware ID/EN helper branches, weather-data descriptions/content metadata, internal protocol/system tokens, and dynamic values; they are not untranslated settings/control copy. Wallpaper catalogue titles/descriptions remain authored content, not control labels.
- B04 exact-head CI #242: PASS. Current integration head has no GitHub Actions workflow run or combined status; no integrated build/test pass is claimed. The connection exposes no workflow-dispatch action, and local environment has no Gradle/Android SDK or network access to run a substitute build.
- Runtime gates: API 30 locale/context/recreation; API 33+ per-app locale synchronization/configuration; Activity/DreamService lifecycle; settings tab and D-pad focus retention; QR upload on real phone/Wi-Fi; F02/F06 physical TV acceptance. No APK published/installed, no device settings changed.
- PR #15 remains Draft/unmerged pending exact-head CI and physical/runtime acceptance.


## Final source checkpoint — 1 November 2026

- Integration head: `89d769689eb566671725c52982e0b73c4f1e8c31`; PR #15 is OPEN, DRAFT, and GitHub reports `mergeable_state=clean`. PR #13 and #14 are also OPEN/DRAFT. No merge has occurred.
- Localization static QC: 560 keys in default/ID/EN; zero duplicate keys, locale key differences, or placeholder mismatches. Duplicate-key rejection is now part of `LocalizationResourceParityTest`.
- App-wide scan: 68 Java files and 17 layout XML files; 21 changed Java files with 534 `R.string` refs and 17 layouts with 71 `@string` refs; zero unresolved references.
- B04 exact-head CI #242 passed. There is still no Actions run or combined status for the exact integration head. Do not treat `mergeable_state=clean` as a build/test result.
- Remaining gates are external: integrated CI; API 30/API 33+ runtime lifecycle/focus checks; phone-on-same-Wi-Fi QR upload verification; physical F02/F06 TV acceptance. No APK was published/installed and no TV settings were changed.


## B04/B05 cleanup checkpoint — CI and release gates, 1 November 2026

- Current integration head: `5e7481c4902c85352b1ab95b7f1ef41acdf9ed78`; PR #15 is OPEN, Ready for review, mergeable=true, not merged.
- CI trigger root cause corrected: explicit pull-request activity types now include `ready_for_review`; push trigger covers `integration/**`.
- Updated B04 base commit was integrated into the B04+B05 branch history with a two-parent merge commit. Source history retained; branch ancestry reconciled.
- Exact-head CI remains UNVERIFIED: no check runs or combined statuses exist for current integration head. B04 source CI #242 passed, but that is not an integrated-head result.
- Static localization QC remains: 560 aligned keys; zero duplicates, key parity errors, or placeholder mismatches; 68 Java files and 17 layouts audited; 534 Java and 71 layout resource references with zero unresolved references.
- Release gates still open: exact-head CI; API 30/API 33+ lifecycle/focus; real phone QR upload over same Wi-Fi; physical TV F02/F06 acceptance. No APK published/installed and no TV settings changed.
- Do not merge PR #15 until every gate passes. Once these gates are satisfied, close redundant source PR #14 with a superseded note and complete the B04/B05 merge chain cleanly; retain PR #13 as the B04 parent until that chain is merged.


## B04/B05 completion path and final test timing — 1 November 2026

- User decision: run integrated CI before progressing; defer all physical device/TV validation to the final overall B-series system test after B06/B07 and remaining planned stages. This is a scheduling decision, not a waiver.
- Current integrated head: `5e7481c4902c85352b1ab95b7f1ef41acdf9ed78`. Exact-head CI is still PENDING; no check run/status exists.
- Workflow triggers were corrected, but reopening the PR did not create a run. Manual GitHub Actions dispatch is required: select workflow CI, branch `integration/b04-b05-reconciled`, keep `publish_device_apk=false`.
- Next sequence: pass exact-head CI → complete B04/B05 merge chain → proceed B06/B07 → consolidate final device/TV test matrix for end-of-project acceptance.
- No APK has been published/installed and no device settings have changed.

## B04/B05 closure and next-stage checkpoint — 1 November 2026

- B04+B05 integrated CI passed on exact integration head `5932cf60e19640bc5fc648f43105f86c8752813c` (run #254 / `41277076284`; both CI jobs passed). Follow-up PR #13 exact-head CI passed on `ec5ff4fce06fe0d2daf0e3fc49fe84fef9ad9838` (run #256 (`41277353285`)).
- CI-discovered defects were corrected: apostrophe escaping in Android localized strings, localized update-progress message type, and localization parity test expected type.
- Merge chain: PR #15 merged as `ec5ff4fce06fe0d2daf0e3fc49fe84fef9ad9838`; PR #13 merged as `d7b76c4a22c9eadffb8171a616283f3ad647d488`; GitHub reports PR #14 merged as `73817ce41e4ae5b034922f190052a95ac45886d9`. Current B03 continuation branch head is `d7b76c4a22c9eadffb8171a616283f3ad647d488`.
- Manual CI dispatch is now enabled on `main` (commit `6eaf787c56226606b1d2e650c59540c553756c62`). APK publishing remained disabled; no APK was published/installed.
- B04/B05 merge gate is complete. Proceed to B06 and B07 from the current B03 continuation head, maintaining CI/static QC at each stage.
- Final system acceptance still requires Android API 30/33+ lifecycle/configuration, Activity/DreamService recreation and focus/tab retention, real-phone QR upload over same Wi-Fi, and physical TV F02/F06. User deferred these until final overall B-series testing; not waived.

## Historical B06 execution checkpoint — initial baseline (superseded by the latest checkpoint above)

- B04/B05 source + CI gate and merge chain are complete. Current B03 continuation head: `d7b76c4a22c9eadffb8171a616283f3ad647d488`.
- B06 is now IN PROGRESS on isolated branch `feature/b06-notification-feedback`, based on the current B03 continuation head.
- Baseline scan of 63 Java files found scattered Toast feedback, AlertDialog usage, OTA `ProgressDialog`, and separate JavaScript feedback states in the QR upload pages. No shared feedback abstraction or Snackbar usage was found.
- Transport timeout values are operation-specific; preserve their engineering purpose. B06 should standardize user-visible timeout messages and cleanup, not blindly equalize socket/network timeout values.
- B07 remains NOT STARTED. Device/runtime/QR/TV acceptance remains deferred until final overall B-series testing.


## B07 implementation checkpoint — 1 November 2026

- B07 audit identified a reproducible source-level restoration gap: selected Settings tab survived reopening, but focus always returned to the sidebar; exact sidebar/right-pane cursor and scroll state were not preserved.
- Implemented restoration state for focus area, active tab, validated focus path + target signature, sidebar scroll, and per-tab submenu/right-pane scroll.
- Stale path, changed content, or non-focusable target falls back to the selected sidebar destination.
- Added `SettingsFocusPath` codec and `SettingsFocusPathTest` JVM coverage.
- Source branch/head: `feature/b07-menu-focus-restoration` / `d4eabc48e020ab6cbef54e0f8884cf125eff97ed`.
- Draft PR #17 targets the B06 branch and remains unmerged under D-013.
- Exact-head CI #298 / `41281838510` was IN PROGRESS at this checkpoint. B07 is not yet marked complete.
- Final API 30/33+, Activity recreation, physical TV D-pad, and grouped B01–B06 regression remain required; device acceptance is deferred, not waived.
- Detailed evidence and test matrix: [B07_MENU_FOCUS_AUDIT.md](B07_MENU_FOCUS_AUDIT.md).


## B-series source/CI closure checkpoint — 1 November 2026

- B01–B07 source work has exact-head CI evidence recorded in `AUDIT_REGISTER.md`; B07 exact-head run #298 / `41281838510` passed both jobs on `d4eabc48e020ab6cbef54e0f8884cf125eff97ed`.
- B07 restores the exact Settings sidebar/submenu focus and scroll position using validated focus paths and target signatures, with safe fallback. Four JVM tests pass; release build passes; APK size 1509 KB (<1.5 MiB).
- Current B07 source PR #17 is OPEN/DRAFT/UNMERGED and stacked on B06 exact head `bc86a250d73a5cbb83250217d42c3c3993cf29d7`.
- B06 PR #16 and B07 PR #17 remain unmerged by D-013. B01–B03 source PRs remain traceable; B04/B05 integration is already reconciled into the B03 continuation history.
- **B-series source/CI development is complete enough to proceed to Project C.** This is not the final integrated release or device acceptance.
- After A–G source work is complete: freeze exact heads, audit combined diff/dependencies, perform grouped integration, run cross-B regression, complete API 30/33+ lifecycle/focus, same-Wi-Fi QR and physical TV acceptance, then seek explicit release approval. Deferred tests are not waived.
- No APK was published/installed and no device/TV settings were changed.
- B07 detailed evidence: [B07_MENU_FOCUS_AUDIT.md](B07_MENU_FOCUS_AUDIT.md).


## Historical Project C checkpoint — 1 November 2026

- C01 source/CI pass: Draft PR #18, `feature/c01-local-communication-audit` @ `bba0801c9eb61ab27ae5046379c7a50aabebf570`, CI #314 PASS.
- C02 source/CI pass: Draft PR #19, `feature/c02-tv-local-server-ip` @ `b0e1ff048e36bf21f8da1e165a427075f85a7cb3`, CI #333 PASS.
- C03 source/CI pass: Draft PR #20, `feature/c03-phone-tv-network-verification` @ `d266287df287167e5a8e73fd675b51759b580ed6`, CI #338 PASS. Active default Wi-Fi/Ethernet IPv4 resolution now drives Avatar, Wallpaper, and StandBy QR services.
- C01–C03 remain OPEN/DRAFT/UNMERGED under D-013. Real phone/TV same-network acceptance remains deferred to F-stage and is not waived.
- Next: C04 HTTP endpoint/routing/method/response-code audit. Detailed resume point is in `HANDOVER.md`; evidence is in `AUDIT_REGISTER.md`.


## Historical Project C C04 source checkpoint — 1 November 2026

- C01 source/CI pass: Draft PR #18, `feature/c01-local-communication-audit` @ `bba0801c9eb61ab27ae5046379c7a50aabebf570`, CI #314 PASS.
- C02 source/CI pass: Draft PR #19, `feature/c02-tv-local-server-ip` @ `b0e1ff048e36bf21f8da1e165a427075f85a7cb3`, CI #333 PASS.
- C03 source/CI pass: Draft PR #20, `feature/c03-phone-tv-network-verification` @ `d266287df287167e5a8e73fd675b51759b580ed6`, CI #338 PASS.
- C04 code/test head: `f09ee1b30b80e45e793c1678935eff8867aedae4`, CI #342 PASS; Draft PR #21. The subsequent audit-document-only branch head is `9094969d968c6fffef6598e1e8c5a7bd62e5efa8`; exact-head documentation CI #343 is in progress at checkpoint.
- C04 added strict HTTP request-line parsing, exact endpoint/method contracts, 404/405/413 behavior, deterministic gallery API errors, duplicate/missing query rejection, and browser-side HTTP status handling.
- APK diagnostic on code/test head: 1,547,845 bytes, 15,019 bytes below the 1.5 MiB cap; artifact publication skipped. Avoid unnecessary APK growth in later stages.
- C01–C04 remain OPEN/DRAFT/UNMERGED under D-013. Physical phone/TV QR/browser acceptance remains deferred to F-stage and is not waived.
- Next: C05 shared upload/receive-file pipeline audit. Detailed resume state is in `HANDOVER.md`; audit evidence is in `AUDIT_REGISTER.md`.


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


## Project D source/CI closure checkpoint — 1 November 2026

- D01–D07 source-audit and exact-head CI work is complete. All Project D PRs remain OPEN/DRAFT/UNMERGED under D-013; no early merge or APK publication.
- D01–D06 outcomes and exact CI evidence are recorded in BUG_REPAIR_ROADMAP.md, HANDOVER.md, AUDIT_REGISTER.md and OPERATIONS_LOG.md. D06 final head 78a12e9fc196a596fa98ea272edc735501047af1; exact-final-head CI #399 / 41312553920 PASS.
- D07 final head 964163c918e281b68af5cf7114ca56ead2797486; Draft PR #31 targets D06 exact head and remains OPEN/DRAFT/UNMERGED. Exact-final-head CI #404 / 41313690556 PASS on that exact head; both jobs succeeded, app/installer release unit tests and release build passed.
- D07 repaired Child Mode profile-scoped concurrent mutations, StandBy picker/QR/clear content-store conflicts, and stale weather/AQI responses after location/language changes. New profile IDs use UUIDs. C07 upload/session hardening, existing profile schema and device-wide parent PIN policy were preserved.
- Final D07 APK diagnostic: 1,552,197 bytes, 20,667 bytes below the 1,572,864-byte cap; manual APK publication skipped.
- Physical TV acceptance remains deferred to Stage F and is not waived: process death/force-stop/reboot, alarm restoration, profile switching/isolation, Child Mode, picker/QR concurrency and location/language freshness.
- **Next stage: E01.** After A–G source work, freeze exact heads, perform grouped integration and combined-diff audit, run cross-project regressions, then complete Stage F device acceptance before any merge/release approval.



## E00 master inventory reconciliation — 2 November 2026

- Current state and dependency map: [E00_MASTER_INVENTORY_AND_DEPENDENCY_AUDIT.md](E00_MASTER_INVENTORY_AND_DEPENDENCY_AUDIT.md).
- A01–A06 are recorded DONE. B01–B07, C01–C07 and D01–D07 source/CI work is complete; final integration/runtime/physical acceptance remains pending in F.
- B01–B03 (#10–#12) and B06–B07 (#16–#17) remain open/Draft/unmerged; B04/B05 reconciled merge history (#13–#15) is preserved. C01–C07 (#18–#24) and D01–D07 (#25–#31) remain open/Draft/unmerged under D-013.
- D07 final source head: `964163c918e281b68af5cf7114ca56ead2797486`; exact-final-head CI #404 / `41313690556` PASS. Diagnostic APK 1,552,197 bytes; no artifact published or installed.
- Next feature task: E01. E00 documentation PR #32 is merged and canonical documents have been re-read; PR #33 records the official closure. Do not merge feature PRs early. F01 is an isolated integration rehearsal and combined-diff audit, not a canonical feature merge. F02–F07 validate the candidate. G01 closes findings through their owning projects. Only after all A–G source audits/fixes are complete and findings are closed or explicitly accepted may grouped integration into `integration/b-series` proceed. G02–G03 cover candidate/artifact and target-device verification; final `main` merge and final APK build require final acceptance and explicit user approval.


## E00 official closure — 2 November 2026

- E00 documentation PR #32 was merged into `docs/project-master-inventory` (merge commit `4ba80230cb9ddfbcc27657b7b0d07bed6905fc97`).
- Exact PR head `541dd9accb66eb3b071367a3e4967769d742c2cd`; CI workflow run #414 / `41359874778`: PASS. Both `Launcher build and unit tests` and `Lists, tools and docs` jobs completed successfully. No review comments or change requests were present.
- Canonical E00 report, roadmap, inventory, audit register, operations log, master project and handover were re-read from `docs/project-master-inventory` after merge.
- E00 scope is closed: documentation/inventory/dependency reconciliation only. No application source, feature PR, APK, user data, TV setting, installation or physical-device acceptance was changed or claimed.
- **Next work unit: E01 — Device Settings & Launcher audit.** Begin with exact dependency heads and the A06 read-only HOME resolver/preferred-activity discrepancy. Preserve D-013/D-014; do not merge feature PRs early.


## E02 global UI consistency checkpoint — 2 November 2026

- Source-level audit and repairs completed across B01/B02/B03/B06/B07, C01/C03/C06, D03/D04 and E01. C02 installer was checked and has no app-owned modal windows; it uses inline progress/status feedback.
- Canonical `TvUiTokens` and `TvDialogStyle` are identical across the 8 Settings-owning B/D/E branches; modal helper is also identical on C01/C03/C06.
- Settings shell, sidebar tabs, selected states, shared card/switch rows, search input and modal bounds now consume the B01 global contracts. Weather, profile switch and update/permission/progress dialogs were standardized in the affected branches.
- Exact-head CI passed: B01 #563, B02 #565, B03 #567, B06 #569, B07 #571, D03 #574, D04 #576, E01 #578. Branch dependency synchronization restored B03 and B07 mergeability. No feature PR merged.
- Remaining acceptance: F-stage integrated UI review and physical Android TV/D-pad tests. Do not claim visual perfection or close runtime acceptance before this gate. Full details in `AUDIT_REGISTER.md`, E02 section.


## E02 D02 follow-up closure — 2 November 2026

- D02 PR #26 now uses canonical B01 `TvDialogStyle`/`TvUiTokens`; its shared AlertDialogs, Now Playing/card-prompt custom dialogs and QR upload window use the shared contract. The only remaining direct window sizing is the intentional 16:9 media-hub aspect-ratio layout.
- D02 exact-head CI #587 PASS; D03 #588 PASS; D04 #589 PASS. All three branches mergeable. No PR merged.
- B01/B02/B03/B06/B07, C01/C03/C06, D02/D03/D04 and E01 UI source changes have exact-head CI PASS. C02/C04/C05/C07 and D01 were checked and correctly require no additional visual component changes in their current scope.
- F-stage remains mandatory for integrated Settings tabs/subtabs and remote/modal acceptance; no physical-TV test is claimed.

## E01 source audit closure — 2 November 2026

- E01 source audit and repair work is COMPLETE; next Stage E task is E02 — Profile. Physical Android TV/OEM acceptance remains assigned to F and is not waived.
- E01 branch: feature/e01-device-settings-home-control. Source head validated by exact-head CI: 9919dbaa0add6cc7ce2a84332354f46f9cc48d61. Documentation-only follow-up commit on the feature branch: 7aad9ec4cf015dd415b3c948e2b4f80c1099955c.
- Draft PR #34 remains OPEN/DRAFT/UNMERGED, based on D07 exact head 964163c918e281b68af5cf7114ca56ead2797486. D-013 remains binding; no feature merge.
- E01 removed silent Android system-setting/service writes, improved read-only HOME/service status, separated Android screensaver and display-timeout entry points, corrected restart/power feedback, and hardened cache-trim success verification. The QR target was verified to contain the installer APK and release metadata.
- E01 adopts B01's canonical TvDialogStyle and TvUiTokens; their blobs match B01 exactly. No competing popup style was introduced.
- Exact source-head CI #602 / 41400508774 PASS: Android release build, JVM unit tests and docs/tools checks. APK 1,560,565 bytes against 1,572,864-byte cap (12,299 bytes headroom); no artifact published; manual TV-test APK step skipped.
- F-stage still owns HOME resolver/OEM behavior, services readback, power labels, on-device cache ADB result, phone installer flow, remote visual/focus acceptance, and ClockDream/StandBy/display-timeout interaction. No device setting, HOME default, package state, user data or APK installation was changed.
- The global UI consistency checkpoint recorded elsewhere is cross-project B01 adoption/verification; Stage E02 in the approved roadmap remains the Profile module. Do not conflate those labels.

## E01 final cache-feedback accuracy correction — 2 November 2026

- This later checkpoint supersedes the earlier E01 source head and APK-size figures in this document.
- Final E01 source head: b6fa70e406afc9f6e81d16a3635a69afc6c4d783; exact-head CI #609 / 41401277076 PASS.
- Final diagnostic APK: 1,560,609 bytes; 12,255 bytes below the 1,572,864-byte cap. No artifact published; manual TV-test APK step skipped.
- Cache byte totals now count only successfully deleted app-cache/temp files; free-space deltas are no longer attributed to app-cache cleanup. User-facing no-files-removed feedback is neutral. Android system trim success requires the explicit ADB success marker. Regression tests cover marker parsing and actual deleted-byte counting.
- E01 audit report and PR #34 description updated. PR #34 remains OPEN/DRAFT/UNMERGED under D-013; latest feature branch documentation commit 090a956a9860c82eb36c2d96848d47046fba0f0c.
- E01 source audit remains COMPLETE; next Stage E task is E02 Profile. F-stage physical/OEM checks remain mandatory and unchanged. No device settings, HOME default, package state, user data or installed APK changed.


## E02 Profile source audit closure — 2 November 2026

- **E02 source audit and exact-head CI are COMPLETE.** Next Stage E task: E03. Physical Mi TV/phone/remote acceptance remains mandatory in Project F; it is deferred, not waived.
- Feature branch `feature/e02-profile-avatar-lock-ui`; final source head `3ab9b55f3cfe03ad9841a119590a9183cf114e96`; Draft PR #35 targets E01 branch `feature/e01-device-settings-home-control` @ `090a956a9860c82eb36c2d96848d47046fba0f0c`. PR #35 remains OPEN/DRAFT/UNMERGED under D-013.
- E02 retained profile-scoped avatar/Child Mode/lock/hide state, global parent PIN, child-profile switch/delete authorization, QR upload/session/storage safeguards, and B01 global modal/focus contracts.
- Corrected app-policy enumeration to merge LEANBACK_LAUNCHER + LAUNCHER results, deduplicate by package, and handle null labels/icons. Picker count now refreshes on every dismissal path. Built-in avatar index and custom-photo reference are committed atomically.
- Exact-head CI #623 / `41406190380`: both Android build/unit-test and lists/tools/docs jobs PASS. App and installer release unit tests and Release build PASS.
- Diagnostic APK: 1,561,717 bytes; cap 1,572,864 bytes; headroom 11,147 bytes. Manual artifact publication skipped; artifact count zero.
- Parent PIN default `0000` and unlimited retries remain by explicit user decision; no forced PIN change or throttling was added.
- No APK installed, no device setting/HOME default changed, no data migration and no physical-device acceptance claimed.
- Full evidence and F-stage checklist: [E02_PROFILE_AVATAR_LOCK_UI_AUDIT.md](E02_PROFILE_AVATAR_LOCK_UI_AUDIT.md).


## E03 wallpaper metadata checkpoint — 2 November 2026

- E03 has started on isolated branch `feature/e03-wallpaper-clean-metadata`, based on E02 exact head `3ab9b55f3cfe03ad9841a119590a9183cf114e96`; PR #36 is OPEN/DRAFT/UNMERGED under D-013.
- Confirmed issue: Kabut Pagi Dempo title/description are baked into the packaged JPEG while the launcher already has a native hero metadata shelf.
- Added isolated `WallpaperMetadata` lookup for the exact cached Default filename and routed `setPlace()` through it with original online/place provider fallback. Added focused unit tests for strict non-leakage to custom/other/backup filenames.
- **E03 remains IN PROGRESS.** Packaged JPEG replacement with a clean text-free image is not yet committed; duplicate text is not yet considered fixed. Exact-head CI #624 is pending at this checkpoint. No APK publication/installation or device acceptance.
- Scope protection: no B01 global UI, focus/navigation, shuffle/index/interval, QR upload, StandBy storage, or preference contracts changed. Physical TV/remote/phone acceptance remains in Project F.
- Full checkpoint: [E03_WALLPAPER_METADATA_AUDIT.md](E03_WALLPAPER_METADATA_AUDIT.md).

- E03 checkpoint update: exact-head CI #624 / run `41408606965` **PASS** on `188bd49269a358ebf53f8ae09056c41a5eb80243`; both jobs passed, release unit tests/build passed. APK 1,561,797 bytes, 11,067 bytes below cap; manual publication skipped. Clean JPEG binary replacement remains outstanding, so E03 remains IN PROGRESS.


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


## E04 StandBy / Screensaver — targeted repair checkpoint — 2 Nov 2026

- E04 branch `feature/e04-standby-screensaver`, based on E03 exact head `e085f8aed6fdc85b4793b76bb26ab8543b26b2cf`; current source head `7c5e2677eac1c791675d9f5ef7d736eb47d37aca`; Draft PR #37 targets E03 and remains OPEN/DRAFT/UNMERGED under D-013.
- Confirmed duplicate display-off path in `ClockDream`: `HomeKeyService.sleepScreen()` was followed unconditionally by shell `input keyevent 223`. Removed the unsupported duplicate shell action, checked the Accessibility result, and logged unavailable service state.
- Shared photo store, QR upload validation/session controls, atomic collection+mode persistence, picker merge and clear semantics were preserved.
- Exact-head CI is PENDING; no APK publication/install and no physical device acceptance claimed. DreamService/overlay trigger precedence, remote wake, 2-hour sleep and OEM display-lock behavior remain mandatory Project F checks.
- Detailed report: `E04_STANDBY_SCREENSAVER_AUDIT.md`. E04 is NOT FINAL until exact-head CI and Project F acceptance are complete.
