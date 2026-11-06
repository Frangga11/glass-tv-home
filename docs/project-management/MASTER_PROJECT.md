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


## E04 follow-up — native idle ownership — 2 Nov 2026

- User reaffirmed that Android TV's native idle → StandBy/Dream → screensaver → sleep/power-off sequence must remain intact; custom photos are the only feature extension.
- Removed app-owned idle/power timers from `ClockDream` and `MainActivity`; retained manual Nightstand overlay and shared photo pipeline. Commits `75fa1b1` and `08076bb` on E04 branch.
- QR upload/store/decoder paths remain isolated and preserved. E01/E02/E03 boundaries and Project F hardware acceptance are recorded in `E04_STANDBY_SCREENSAVER_AUDIT.md`.
- Exact-head CI #629 pending at this checkpoint; no APK install, no hardware acceptance, no merge. E04 remains NOT FINAL.

- E04 exact-head CI #629 / run `41418393199` on `08076bb945a169de198905152a092f419f48aa82`: **SUCCESS**. Release APK 1,561,373 bytes; 1,572,864-byte budget; 11,491-byte headroom. No APK publication/install. Project F physical acceptance still pending.

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


## Cross-project weather/AQI and popup ownership audit — 2 Nov 2026
- Audit and owner matrix: `CROSS_PROJECT_WEATHER_AQI_AUDIT.md`.
- Confirmed E05 PR #38 does not change `WeatherAirQuality.java`, `WeatherActivity.java` or `HomeWeather.java`; its ayah-reader CI is not weather/AQI verification.
- WQ-01–WQ-08 identify E05 data/content, D07 freshness/state, B01–B03 shared popup primitives, F device/integration acceptance and G cross-project closure. No weather API defect is assigned to C01–C07; preserve C07.
- Source repairs remain OPEN; PR #38 stays Draft/open/unmerged under D-013. See audit report for required sequence and evidence.


## E05 weather/AQI source repair — exact-head closure checkpoint, 2 November 2026
- E05 PR #38 exact source/test head: `6fe89fad078692958db67030329a6de66cd7ae71`; Draft PR remains OPEN/DRAFT/UNMERGED under D-013.
- Corrected synthetic PM2.5/PM10 and weather/forecast fallbacks; unavailable values remain unavailable. Weather/AQI success timestamps are independent; detailed requests reject superseded generation/location/language responses; cached source data is bound to exact selected coordinates.
- Corrected forecast UI sentinel rendering and fake temperature-range fallback; neutral visual state is used when weather data is unavailable. AQI location is described as a model reference, and attribution identifies Open-Meteo/CAMS and modelled US AQI (not official Indonesian ISPU).
- Added three regression tests for weather-cache coordinate matching. Intermediate duplicate string-resource keys were removed from default/ID/EN resources after CI detected them.
- Exact-head CI run #663 / `41433527557` PASS: launcher unit tests/release build/APK-size guard and Lists/tools/docs both SUCCESS. APK publication skipped.
- D07 detailed-weather freshness follow-up head `7ce92d30911288eed98aa2e7b36eaf9942d0c355`, CI run #642 PASS. D07 and E05 PRs remain unmerged.
- Physical 1080p/4K weather presentation, popup focus/Back/scroll and stale/offline/location/language acceptance remain Project F gates. No APK installation or device test claimed.
- Detailed findings/evidence: [E05_WIDGETS_INFORMATION_AUDIT.md](E05_WIDGETS_INFORMATION_AUDIT.md) and [CROSS_PROJECT_WEATHER_AQI_AUDIT.md](CROSS_PROJECT_WEATHER_AQI_AUDIT.md).


## E05 source-scope closure — 2 November 2026
- **Decision:** E05 implementation/source scope CLOSED; physical acceptance transferred to Project F. This is not merge/release approval.
- PR #38 exact head `6fe89fad078692958db67030329a6de66cd7ae71`, OPEN/DRAFT/UNMERGED; exact-head Actions run #663 (`41433527557`) SUCCESS. Both launcher tests/release/APK-size guard and Lists/tools/docs passed; APK publication skipped.
- E05 ayah preview/reader, Weather/AQI source corrections, cache freshness/location isolation, regression tests and B01 shared popup contract source audit are closed at source level.
- Project F owns physical 1080p/4K acceptance: Arabic glyph/harakat and long text, D-pad/Back/scroll, popup clipping/consistency, weather offline/stale/location/language behavior, and Home/wallpaper/prayer/dock coexistence.
- If F finds reproducible defects, route to B (shared modal), D (state/freshness) or E05 (module content/UI). Do not duplicate global styling.
- D07 exact follow-up head `7ce92d30911288eed98aa2e7b36eaf9942d0c355`, CI #642 PASS.
- PR #38 remains Draft/open/unmerged under D-013. Grouped merge and release remain Project G gates. No APK installation or physical test is claimed.
- Detailed closure and F test matrix: [E05_WIDGETS_INFORMATION_AUDIT.md](E05_WIDGETS_INFORMATION_AUDIT.md).


## E06 source/CI closure and E07 start — 2 November 2026

- E06 Home Screen source audit and implementation are complete. PR #39 remains OPEN/DRAFT/UNMERGED under D-013; exact feature head `570b7c83efaec3ac515b78f1c6c119b777fc3b84`, exact-head CI run #667 / `41441963766` PASS. Four retained Home Screen controls and callbacks remain intact; ID/EN localization and shared B UI tokens are aligned. Five future feature candidates remain deferred. Physical acceptance belongs to F.
- E07 is active on isolated branch `feature/e07-animation-effects`, based on E06 exact head `570b7c83efaec3ac515b78f1c6c119b777fc3b84`; Draft PR #40 targets E06 and remains OPEN/DRAFT/UNMERGED.
- E07 audits the existing Animations & Visual Effects controls (icon animation, glass effect, wallpaper weather animation, icon ambient glow), reduced-motion behavior, rendering cost, lifecycle, and remote focus. No new effect or preference was introduced.
- Confirmed and repaired reduced-motion gaps in Home weather overlay and full Weather screen; Weather screen focus transitions now snap under reduced motion. Added lightweight reduced-motion config lookup, reused the existing 1.5 GiB low-memory threshold, and reduced rain/star/cloud particle density in low-memory/reduced-motion mode.
- Code checkpoint `da3135796156c26dd20795931eae15caa83458e9`; exact-head CI run #671 / `41443003016` PASS on both jobs. Diagnostic APK 1,563,885 bytes against 1,572,864-byte budget; 8,979-byte headroom; publication skipped.
- Feature-branch audit report follow-up commit `15840ed1e9a4d7f7bb7e3a013bbebf9c044b8a6a`; exact-head documentation CI is in progress at this checkpoint. Do not mark latest documentation head green until both jobs pass.
- F owns device acceptance: reduced/normal motion, settings toggles, Home↔Weather transitions, pause/resume, low-memory rendering, D-pad focus, 1080p/4K visuals and frame pacing. No APK installed or physical TV test claimed. D-013 remains in force.


## E07 final source/CI checkpoint — 2 November 2026

- E07 exact PR head `15840ed1e9a4d7f7bb7e3a013bbebf9c044b8a6a`; Draft PR #40 targets E06 head `570b7c83efaec3ac515b78f1c6c119b777fc3b84`, remains OPEN/DRAFT/UNMERGED under D-013. GitHub reports mergeable/clean; no merge authorization is implied.
- Exact-head CI run #672 / `41443305464`: PASS. Both launcher build/unit tests and lists/tools/docs jobs succeeded. Release build and APK-size gate passed.
- Exact diagnostic APK size: 1,563,885 bytes; cap 1,572,864 bytes; headroom 8,979 bytes. APK publication skipped; no APK installed.
- Source repair: reduced-motion behavior now stops continuous weather frame scheduling while preserving a static scene on Home and Weather screen; Weather screen focus transitions snap in reduced-motion mode. Full Weather screen now applies existing 1.5 GiB low-memory threshold and reduced rain/star/cloud particle density. Existing Effects-tab preference keys/callbacks and Project B focus/UI contracts preserved.
- E07 source and exact-head CI are COMPLETE. Project F device acceptance remains mandatory: normal/reduced motion, toggle/focus behavior, Home overlay and Weather screen lifecycle, low-memory behavior, D-pad focus, 1080p/4K visuals and frame pacing. No physical TV acceptance is claimed.


## B03/E07 cross-project repair — Glass Effect OFF fallback — 2 November 2026
- Follow-up audit established that Glass Effect OFF removed structural surfaces, not merely blur, from app-owned top pills, Home popup menus, anchored PopupWindows, dock tray and modal dialogs.
- Global contract: glass ON uses existing frosted material; glass OFF uses an opaque shared solid surface. Geometry, radius, row layout, text/icons, focus, D-pad and callbacks remain invariant. Android OS-owned Settings pages are outside this app-owned surface contract.
- B03 owns global shared tokens and fallback primitives. Implemented on `feature/b03-dialog-opacity-layering`, PR #12, head `8cf0a846f3a7738cc1bb20f663dac9cf961e98da`: shared POPUP_SURFACE/DOCK_SURFACE tokens, solid GlassDrawable and dock tray fallback, runtime modal material selection, and JVM fallback-token test. Exact-head CI #685 / `41447473358` was queued at checkpoint.
- E07 owns preference/toggle integration and live popup invalidation. E07 PR #40 head `7ecb45a634e6ed9792fd87f3110ffe0b6eaad1a2`, exact-head CI #682 / `41446777187` SUCCESS. APK diagnostic 1,564,117 bytes / 1,572,864 cap / 8,747 bytes headroom; APK publication skipped.
- PR #12 and #40 remain Draft/open/unmerged under D-013. G must reconcile duplicate shared-file changes by content and verify the combined exact head before grouped merge. F still owns physical 1080p/4K popup, dialog, dock, focus and repeated toggle acceptance. No APK installation or physical TV test claimed.
- Detailed source contract and test matrix: [B03_DIALOG_OPACITY_AUDIT.md](B03_DIALOG_OPACITY_AUDIT.md).


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
- User-approved milestone: **Project E (E01–E08) closed for feature development and module-level audit**. No further E-module scope is opened unless Project F finds a verified integration defect or the user explicitly reopens scope.
- Closure does **not** mean physical-device acceptance. Project F owns integrated regression, remote/D-pad/focus/Back behavior, popup/dialog consistency, Glass Effect ON/OFF, 1080p/4K layout, lifecycle, module interaction and device acceptance.
- Feature PRs remain OPEN/DRAFT/UNMERGED under D-013; no merge or APK installation is authorized by this closure. Current PR chain: #34 E01, #35 E02, #36 E03, #37 E04, #38 E05, #39 E06, #40 E07, #41 E08.
- Latest observed PR heads: #34 `090a956a9860c82eb36c2d96848d47046fba0f0c`; #35 `3ab9b55f3cfe03ad9841a119590a9183cf114e96`; #36 `e085f8aed6fdc85b4793b76bb26ab8543b26b2cf`; #37 `08076bb945a169de198905152a092f419f48aa82`; #38 `6fe89fad078692958db67030329a6de66cd7ae71`; #39 `570b7c83efaec3ac515b78f1c6c119b777fc3b84`; #40 `a0f2b0fff6eef0c42852d21b971904d5acd29d5c`; #41 `8dfdcabd19853a1140724ea68983877af17bed0c`. All are OPEN/DRAFT/UNMERGED at checkpoint.
- E08 final source/test head `fcd7d8f23a4833830a401652ef269db3282b7300`; exact-head CI #702 / `41473357877` PASS; APK diagnostic 1,562,709 bytes against 1,572,864-byte cap (10,155-byte headroom), publication skipped. PR #41's latest head is report-only after the verified source/test head.
- E08 UI distinction: Quick Control Wallpaper popup is the compact three-action control center; Appearance > Wallpaper management remains the fuller management route. Shared visual tokens/material rules are reused, not duplicate wallpaper engines. Project F must verify both entry points remain functionally distinct and state-consistent on TV.
- **Next active stage: Project F.** Start with integration/dependency and combined-diff audit; then CI/regression and physical device acceptance. Retain D-013: grouped integration/merge is deferred to G and requires explicit user approval.


## Project F01 — integration/dependency audit checkpoint (2 Nov 2026)
- F01 is IN PROGRESS. Source diffs and dependency mapping have been reviewed across B01–B03, C01–C07, D01–D07 and E01–E08. This is not yet a combined integration build or runtime acceptance.
- Main hotspots: `MainActivity.java`; Wallpaper/Avatar upload servers; `TvDialogStyle`/`TvUiTokens`; `ModernSettingsDialog`; weather/profile state; `StandbyPhotoStore`; localized string resources and CI workflow.
- Exact-source CI evidence for E01–E08 is PASS, including E04 CI #629. B01/B02/B03, C07, D05 and D07 checkpoints sampled in the F01 audit also PASS. See `F01_INTEGRATION_DEPENDENCY_AUDIT.md` for SHAs and evidence.
- Keep PRs Draft/open/unmerged under D-013. No merge, combined APK publication/installation, or physical TV test was performed. F01 remains open until frozen-head semantic diff and isolated combined software-gate evidence are complete.


## F01 expanded dependency audit — 2 Nov 2026
- Expanded F01 coverage to B01–B07, C01–C07, D01–D07 and E01–E08. B04/B05/PR15 are already merged pre-D-013; B-series integration CI #257 PASS. B06/B07 and the other post-D-013 feature PRs remain Draft/open/unmerged.
- Latest module source/test CI records checked are green, including E01–E08 #610/#623/#626/#629/#663/#668/#691/#703 and B/C/D chains. Per-module CI is not integrated acceptance.
- Parent/base drift was identified on PRs #11, #12, #17, #18, #19, #21, #24, #29 and #34. GitHub reports them mergeable; the newer base changes must be preserved by dependency-ordered grouped integration and cross-group regression.
- Isolated rehearsal branch `integration/f01-rehearsal-only` and Draft PR #42 combine E01–E08 with latest D07 without merging. CI #708 PASS; #709/#710 pending at checkpoint. No APK published/installed or physical TV test.
- F01 remains open until #709/#710 final results are verified. D-013 remains binding; no feature PR merge or release approval.


## Project F01 closure — 2 Nov 2026
- **F01 CLOSED — PASS** at source/dependency integration level. Complete B01–B07/C01–C07/D01–D07/E01–E08 mapping and parent/base drift register are in `F01_INTEGRATION_DEPENDENCY_AUDIT.md`.
- Verified and repaired E08 wallpaper entry-point mismatch: Quick Control remains compact Change/Shuffle/Next; Appearance has separate Collection/Interval/Personal Wallpaper Manager cards over the same state/engine. Updated E08 head `1d6fba799012ff0e8dbbfbce5a7fbe4f193e1d0d`, CI #711/#712 PASS.
- Final integrated rehearsal PR #43 CI #716 PASS on synthetic merge `21c2eab34a3356d37d079eba04b281cc2373386d`; unit tests, release build, docs/tools and APK guard PASS. APK 1,563,769 bytes / 1,572,864 cap / 9,095-byte headroom; publication skipped.
- PR #42/#43 closed without merge; PR #41 remains Draft/open/unmerged. No feature PR merge, APK publication/installation or physical TV test. D-013 remains binding.
- **Next: F02** integrated functional regression and controlled Android TV acceptance.


## Project F/G execution policy update — 3 Nov 2026
The mandatory execution sequence is: (1) re-verify prior audits/dependency mapping against live GitHub state; (2) verify exact source HEADs and parent/base ancestry; (3) combine in controlled stages on integration branches; (4) rerun only changed, dependency-affected or previously unproven tests; (5) finalize with exact branch/commit, test evidence and residual acceptance boundaries. Mergeability alone is not a safety gate. Feature PRs remain unmerged until Project G and explicit approval.

### Revalidation checkpoint
- B-series staged integration: integration/b-series HEAD 9070894fa6d9ef8aec034f46dfe777e866709dfe; CI-only PR #48 / run #741 PASS. APK size guard PASS at 1506 KiB / 1536 KiB cap; publication skipped.
- Full D07+E08 candidate: integration/project-f-staged-full HEAD 8fe90a9fdd493c5d81ffdacbe16054630a26580a; tree identical to F01 synthetic candidate 21c2eab34a3356d37d079eba04b281cc2373386d; existing CI #716 PASS remains valid.
- Temporary PRs #44–#49 were closed without merge. No feature PR or main/release branch was merged/modified.
- F01 revalidation: PASS. Next stage remains F02 — integrated functional regression and controlled Android TV acceptance; physical device acceptance is not claimed.


## Project F02 opening checkpoint — 3 Nov 2026
- F02 OPEN. Entry/source audit PASS; functional and physical acceptance are not yet passed.
- Selected candidate: `integration/project-f-staged-full` HEAD `8fe90a9fdd493c5d81ffdacbe16054630a26580a`. Live comparison against F01 tested candidate `21c2eab34a3356d37d079eba04b281cc2373386d` shows 3 commits ahead, 0 behind, no changed files; exact-tree CI #716 is reusable.
- CI #716 / `41470304782`: launcher build/unit tests/release build/APK guard and Lists/tools/docs jobs SUCCESS; manual APK publication SKIPPED.
- Compared with `integration/b-series` HEAD `9070894fa6d9ef8aec034f46dfe777e866709dfe`, the full candidate retains identical fallback values and modal behavior, exact TvFeedback/FeedbackPolicy files, plus SettingsFocusPath tests and stronger upload request/session deadline policy. Do not overwrite the full candidate with the B-series branch blindly.
- Physical tests remain NOT RUN: D-pad/Back/focus, dialogs, Glass ON/OFF, Profile/App Lock/Child Mode, Weather/AQI, phone QR upload, wallpaper route/state, StandBy, process death/reboot and 1080p/4K.
- No APK publication/installation or feature/main/release merge. D-013 remains binding. Detailed matrix: `F02_INTEGRATED_FUNCTIONAL_REGRESSION.md`.


## Project F02 current checkpoint — 3 November 2026

- Project F02 is the active stage: controlled integration, system-level static review, host-side regression testing, CI, and readiness evidence. Project G retains all physical Android TV installation, launch, remote/focus, rendering, network/ADB compatibility, and final APK acceptance.
- F02 branch: `integration/project-f02-system-validation`; Draft PR #50; latest documented checkpoint `1658237bafdb24167391270f3ad8f694751a6c2a`. Exact-head CI was queued when this record was written.
- Cross-module fixes are being carried by their source owners: B01 modal enforcement; D05 Settings search dialog dependency repair; D01 sender authorization for LocationBroadcastReceiver; D06 AzanActivity internal-only boundary.
- A previous D06 retest correctly failed compilation because the branch lacked the B01 helper dependency. The dependency and its geometry test were synchronized; no test or compile gate was bypassed.
- D01 follow-up PR #51 exact-head CI passed on `bbb13dd107c86cda85bd1482c2f050ca69dfc4e2`. B01/D05/D06 owner-branch retests were running; see F02_FINDINGS_REGISTER.md for exact IDs and statuses.
- Current status: PARTIAL / NOT FINAL. No finding closes on code commit or successful build alone; require owner exact-head CI, integrated exact-head CI, regression evidence, and explicit Project G deferrals.
- No APK publication/installation, physical Android TV testing, or merge to main is authorized during F02.


## Latest Project F02 checkpoint — 3 November 2026

- F02 integration head `eea920469ffe5bef5ddb14e1dfd66687e82d8fed`, PR #50 Draft/open/unmerged; exact-head CI in progress.
- B01 global modal enforcement and Home double-press routing are implemented; HomeKeyPressTracker uses a 350 ms double-press window and 400 ms single-action delay. Single Home, double Home, long-press Settings and disabled App Switch policy are preserved.
- D01 LocationBroadcastReceiver requires `android.permission.DUMP`; LocationActivity is now non-exported. D01 latest exact-head CI PASS.
- D05 Settings modal contract and D06 AzanActivity non-exported fixes have owner-branch exact-head CI PASS.
- Robolectric API 30 dialog-window tests passed on previous F02 head `5f6b0c60949c1284e0e904e614e85c2e0ca657f2`; latest integration retest is still running.
- RecentAppsActivity's exported ACTION_MAIN filter remains P3 pending external caller contract verification in G.
- Current status PARTIAL / NOT FINAL. No merge, APK publication/installation, or physical Android TV testing during F02.


## F02 additional findings — 3 November 2026

- F02-FUNC-002: Force Stop queue previously cleared the shared HomeKeyService Handler, potentially canceling Home single/double/long-press and power callbacks. Dedicated `forceStopHandler` now owns Force Stop timeouts and queue continuations; regression test added.
- F02-SEC-004: exported RecentAppsActivity accepted `ACTION_CLOSE_ALL`, which invokes the destructive Close All operation. Set `exported=false` and removed the unnecessary MAIN filter; explicit internal HomeKeyService route retained. Manifest regression test added.
- B01 latest owner head `a1e743ad88c28ca3a9602dfc9d0b5da16e46285d`; F02 latest head `3ea479d689dc732fe46594a0677d02b7762d7527`. Exact-head CI in progress.
- Previous F02 checkpoint `c9496d4e051980e640bc269ffce695b95ba71ad4` passed both jobs, but it predates these fixes.
- Status remains PARTIAL / NOT FINAL; no merge, APK publication/installation, or physical Android TV test.


## Exact-head CI results — 3 November 2026

- F02 PR #50 head `5dd787ba136dfb070eaa2e833af653e57ec66015`: both CI jobs PASS — run #41509632784 Launcher build/unit tests; run #41509626957 Lists/tools/docs.
- B01 PR #10 head `791620ce4628ee9e909af7ce2f4758b6df77afeb`: both jobs PASS — run #41509576279. A subsequent comment-only source alignment commit `389e289e939276ce9e20d7e6c82c293d3f83c70f` has an exact-head run pending.
- F02 exact-head green is evidence for the integrated host-side source/test gates, not physical TV readiness. Project G still owns real remote key timing, rendering/focus, OEM power menu, ADB permission and firmware checks.
- Keep F02 partial until remaining cross-module audits and final F02 exact-head retest are complete.


## C07/F02 session-revocation hardening — 3 November 2026

- Finding C07-F08 / F02-C07-001: wallpaper API state-changing routes select/delete/slideshow/clear could continue after QR-session closure because only the outer request dispatcher checked session state.
- Fixed on C07 owner branch and mirrored to F02: all four mutation methods now share the server monitor with synchronized `stop()`, rechecking active session under lock before mutation/listener callback. Expired/revoked operations return HTTP 410 Gone JSON.
- Regression guard: `tests/test_wallpaper_session_shutdown_atomicity.py`.
- C07 PR #24 latest documented head `12b1f431f7687023727983089ea03eda0f97a73d`; F02 PR #50 latest head `081496ca93fd0526025a21ce7cd0e3d7f9c3364a`; exact-head CI pending.
- No upload route/API/browser UX or StandBy storage contract was changed. Physical phone-to-TV cancellation/retry/expiry tests remain Project G.


## F02 continuation checkpoint — 3 November 2026

- F02 integration branch `integration/project-f02-system-validation`; latest head after findings/validation documentation commits: `b64715156e180edc3bfae25bffba6a089544247f`.
- Immediately preceding source/test head `ddf130a348182546b28ff59c292c9b5ceebacb2e`: exact-head push CI #41512382568 passed both required jobs. Matching PR run #41512391849 passed all substantive build/test/release-validation/APK-size steps; cleanup was still in progress at last poll. Documentation-only head requires fresh exact-head CI.
- C07 owner PR #24 head `80a92264c51631fb107c75f32a3cd8e4b8aa9d9a`: run #41512386827 passed both jobs. Corrected one test arithmetic expectation from 3000 to 3500 ms; production calculation was already correct.
- Verified C07/D05 dependency contract: D05's downstream `UploadResult` persistence adaptation in `WallpaperUploadServer` is intentionally retained in F02 alongside C07's monotonic deadlines and session-shutdown synchronization. Do not overwrite it with C07's older boolean-based StandBy handler.
- B01 #10 run #41509724289, D05 #29 run #41505852041, D06 #30 run #41505865772, and D01 #51 run #41506847521 passed on their recorded exact heads.
- Status: F02 still PARTIAL pending exact-head CI on `b647151`, remaining cross-module audits and documented G handoff. No merge, APK publication/installation, or physical Android TV test.


## F02 latest integrated checkpoint — 3 November 2026

- F02 branch `integration/project-f02-system-validation`, PR #50, latest head `558762994dec05a7ae03a924c33a3ed11665c224`.
- Exact-head push CI #41513940620: both Lists/tools/docs and Launcher build/unit tests SUCCESS. Matching PR run #41513947375: both jobs' substantive steps passed; final Gradle cleanup was still running at last poll.
- Preceding F02 source/test head `5a01785cfd4ab81e816a955bd7647c0e766c6b2f` passed PR CI #41513906117 (both jobs). Its Python suite ran 35 tests, including the new AccessibilityService manifest guard.
- New F02-SEC-005: InputKeyService had `exported=false` despite being an opt-in AccessibilityService. E01 owner PR #34 changed it to `exported=true` while retaining system-only `BIND_ACCESSIBILITY_SERVICE`; latest E01 head `edd6461c932705a109ed9768649cae7e3c162c59`, run #41513716451 PASS. The new guard first failed before the manifest fix, then passed after.
- F02 APK diagnostic: 1,564,977 bytes against 1,572,864-byte cap; 7,887 bytes headroom. R8 minify/resource shrink already enabled; monitor size without degrading fonts or default wallpaper.
- C07 owner PR #24 #41512386827 PASS; B01 #10 #41509724289 PASS; D05 #29 #41505852041 PASS; D06 #30 #41505865772 PASS; D01 #51 #41506847521 PASS.
- F02 remains PARTIAL pending remaining cross-module audits and G handoff. PR #50 remains Draft/open/unmerged. No APK publication/installation or physical Android TV test.


## 3 November 2026 — latest F02 exact-head verification

- F02 PR #50 latest head: `91ae962bae04914a3888210e5f997b24151d5a62` (findings-register documentation-only commit; no source/test changes after head `5587629`).
- PR workflow #41514480902 completed SUCCESS on this exact head: Lists/tools/docs and Launcher build/unit tests both passed. The launcher log confirms release-validation build success, 35 Python tests including the accessibility-service manifest guard, and APK size 1,564,977 bytes against 1,572,864-byte cap (7,887 bytes headroom).
- Matching push workflow #41514472847 initially failed only because Maven Central returned HTTP 429 while downloading Robolectric `nativeruntime-dist-compat-1.0.16`; this is external dependency rate limiting, not a source/test failure. Only the failed launcher job was re-run (attempt 2); it was still in progress at the time of this note. Lists/tools/docs job passed.
- PR #50 remains Draft/open/unmerged. No APK publication, installation or physical Android TV testing. Device acceptance remains Project G.


## F02 continuation — upload shutdown and storage safety — 3 November 2026

- Final QR-session audit verified Avatar, custom Wallpaper, and StandBy commit callbacks are guarded by the same server monitor as stop/revocation. The regression suite was strengthened to explicitly protect the Wallpaper rename/callback and StandBy persistence/callback critical sections.
- New finding F02-RES-001: up to eight QR clients could upload concurrently against a per-request free-space snapshot; custom Wallpaper lacked a minimum-free-space guard. Added overflow-safe reservation policy and synchronized in-flight byte reservations for both custom Wallpaper and StandBy uploads. The policy preserves 64 MiB free, returns HTTP 507 when space is insufficient, and releases reservations in finally paths. Existing 60 MiB per-file limits and StandBy 100-photo / 512 MiB collection policy remain unchanged; no total custom-wallpaper cap introduced.
- Added JUnit boundary tests and Python source-contract regression tests. Production behavior changes are limited to refusing uploads that would breach the free-space safety margin; no UI, image-quality, persistence, or collection semantics were otherwise changed.
- F02 PR #50 exact head c1f212585633899d4c1f5327d3a982a9e136e2b7; CI run #41519813674 Lists/tools/docs SUCCESS, Launcher build/unit tests IN_PROGRESS at last poll. F02 remains PARTIAL pending exact-head CI and G handoff.
- Keep PR #50 Draft/open/unmerged. No APK publication/installation or physical TV testing during Project F. Project G owns actual low-storage/concurrent-LAN behavior and all OEM/remote/display acceptance.


## F02 upload storage hardening — exact-head result — 3 November 2026

- F02 source/test head c1f212585633899d4c1f5327d3a982a9e136e2b7 passed exact-head PR CI #41519813674 (both jobs). Lists/tools/docs passed 42 Python tests; Launcher unit tests and release validation build passed.
- Release APK diagnostic: 1,565,621 bytes against 1,572,864-byte cap; 7,243 bytes headroom. Keep F02-BUILD-001 as P3 watchlist.
- New QR storage reservation policy and upload shutdown test hardening are host/source verified. Real low-storage/concurrent LAN behavior remains Project G.
- Latest F02 docs-only head 1845635091969c2475bb0d26783bc0f7eb58c1c0; CI #41520194974 queued at last poll. PR #50 remains Draft/open/unmerged; no APK publication or physical TV test.

## F02 process-wide upload reservation refinement — 3 November 2026
- Lifecycle review found that old upload workers may briefly drain after stop() while MainActivity starts a replacement QR server. Per-instance storage reservations would miss those residual writes.
- Reservation lock/counter is now static and process-wide across WallpaperUploadServer instances; each reservation remains held until the request finally path releases it. Source-contract regression test updated.
- Earlier source/test head c1f212585633899d4c1f5327d3a982a9e136e2b7 passed CI #41519813674, but the latest refinement head a95b21becad238dd634dda1e79aa9de36d749d8c requires exact-head CI #41520409999 (queued at last poll). F02 remains partial.

## C07/F02 owner synchronization result — 3 November 2026
- C07 owner PR #24 head 442b78c68b1540394d543bab59bddf52f865ccf0 passed CI #41520866323 (both jobs, 18 Python tests, launcher unit tests/release build). APK 1,550,949 bytes; manual publication step skipped and no artifacts produced.
- F02 source/test head a95b21becad238dd634dda1e79aa9de36d749d8c passed #41520409999; F02 findings-register head 2469c0c1d241a00be3e02235e12f3f0f6525deb9 passed #41520818608 (both jobs, 42 Python tests, launcher unit tests/release build). APK 1,565,713 bytes; 7,151 bytes headroom.
- Owner and integration source now both include process-wide storage reservations. F02 preserves the downstream D05 UploadResult adapter; no upstream contract was overwritten.
- Latest F02 docs head e190e2863a4aacbe3aba61094fab00572f4b469f; exact-head CI #41521145454 queued at last poll. PRs remain Draft/open/unmerged.

## F02 completion status — 3 November 2026
- Final F02 integration head `e190e2863a4aacbe3aba61094fab00572f4b469f` passed exact-head CI #41521145454 (both required jobs). Python 42 tests / 1 intentional merge-time README count skip; Android unit tests and release build succeeded.
- APK diagnostic 1,565,713 bytes / 1,572,864-byte budget; 7,151 bytes headroom. No artifact produced.
- C07 owner PR #24 head `442b78c68b1540394d543bab59bddf52f865ccf0` passed #41521013896 (both jobs, 18 Python tests / 1 intentional merge-time README count skip, Android unit tests/build, APK 1,550,949 bytes; no artifact).
- D01 owner PR #51 head `66352838e6155a42a9ea61477643a238f360434f` passed #41506847521 and its manifest/test contract is mirrored in F02.
- F02 source, owner synchronization, integrated host validation, and audit documentation are complete. Real device/LAN/OEM acceptance remains Project G. PR #50 remains Draft/open/unmerged by the approved staged-merge policy.


## F02 final exact-head CI — 3 November 2026
- Latest documentation-reconciled F02 head `190b698453a96da8ec2db31da2eefac836849a10` passed #41522215859 (both jobs SUCCESS). Python 42 tests / 1 intentional merge-time README count skip; Android unit tests and release build passed.
- APK 1,565,713 bytes / 1,572,864-byte cap; 7,151 bytes headroom; size gate passed; no artifact.
- C07 owner #24 and D01 owner #51 exact-head CI also passed. F02 host exit criteria complete; G-only physical acceptance remains deferred.


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


## CHECKPOINT PROJECT F04 — HOST/SOURCE INTEGRATION (3 November 2026)

- F04 integration branch `integration/project-f04-feature-isolation-v2`, PR #53 Draft/open/unmerged; exact head `e895ee590a607a8194b2037585ed47f2cf464961`.
- Exact-head CI #41560230282 passed both required jobs. Python suite: 63 tests, 1 intentional skip for merge-time README count normalization. Android `testReleaseUnitTest`, `assembleRelease`, APK diagnostic and 1.5 MiB size guard passed.
- Release APK diagnostic: 1,566,045 bytes / 1,572,864-byte limit / 6,819-byte headroom. No artifact was published and no APK was installed.
- F04 verified private storage separation, profile-bound Avatar QR, route/server lifecycle isolation, durable StandBy collection+mode commit, shared StandBy renderer store, distinct Quick Control vs Appearance wallpaper surfaces, and scoped destructive cleanup.
- F04-WALL-001 (ignored wallpaper delete failures) was repaired on E08 owner branch and mirrored into F04. C07 QR API partial-clear failure now notifies owner UI and returns HTTP 500; owner and integration source changes are separated and cross-checked.
- C07 owner PR #24 and E08 owner PR #41 exact-head CI were still pending Android build completion at this checkpoint. Do not close F04 until both owner heads pass.
- Overall F04 remains PARTIAL pending owner CI and Project G device acceptance. PR #24/#41/#53 remain Draft/open/unmerged; no APK publication/install or physical TV testing.
- Project G retains phone/LAN, OEM DreamService, remote/focus, 1080p/4K, and actual filesystem/network failure acceptance. F05 retains process-death/force-stop/reboot persistence.
- Full evidence: `F04_FEATURE_ISOLATION_AUDIT.md`; resume instructions: `HANDOVER.md`.


## F04 owner-branch verification update — 3 November 2026

- C07 owner PR #24 head `b7a40fdc2c52268ec69c95731983d2f95024eb57` passed exact-head CI #41560200291 (both jobs; 21 Python tests / 1 intentional skip; APK 1,550,961 bytes; no publication).
- E08 owner PR #41 head `c613e3c5f593556748d1593d23f2899ecee36e4a` passed exact-head CI #41560600033 (both jobs; 11 Python tests / 1 intentional skip; APK 1,564,089 bytes; no publication).
- Latest F04 test-only head `6ffc8b4c253beb2662d6978ba38033039c3c53f9` passed Lists/tools/docs with 65 Python tests / 1 intentional skip in #41560811925. Android build and size gate remain in progress; previous source head `e895ee590a607a8194b2037585ed47f2cf464961` passed both jobs and APK budget.
- F04-WALL-001 is fixed and owner-synchronized. Keep F04 PARTIAL until latest test-only exact-head CI is green. PR #24/#41/#53 remain Draft/open/unmerged; no physical TV test or APK publication.


## PROJECT F04 HOST/SOURCE EXIT — FINAL CHECKPOINT (3 November 2026)

- F04 integration PR #53, branch `integration/project-f04-feature-isolation-v2`, exact head `6ffc8b4c253beb2662d6978ba38033039c3c53f9`.
- Exact-head CI #41560811925: both required jobs SUCCESS. Lists/tools/docs: 65 Python tests / 1 intentional README-count skip. Android `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and size gate PASS.
- APK diagnostic: 1,566,045 bytes / 1,572,864-byte cap / 6,819-byte headroom. Workflow artifact list empty; no APK publication or installation.
- C07 owner PR #24 head `b7a40fdc2c52268ec69c95731983d2f95024eb57` passed #41560200291 (both jobs, 21 tests / 1 intentional skip; APK 1,550,961 bytes).
- E08 owner PR #41 head `c613e3c5f593556748d1593d23f2899ecee36e4a` passed #41560600033 (both jobs, 11 tests / 1 intentional skip; APK 1,564,089 bytes).
- F04-WALL-001 (Wallpaper deletion false-success and partial QR API clear reconciliation) fixed on C07/E08 owner branches, mirrored to F04 and verified by exact-head CI.
- **Decision: F04 HOST/SOURCE EXIT PASS.** The feature isolation audit and owner synchronization are complete. PR #24/#41/#53 remain Draft/open/unmerged under D-013. No physical device test or APK publication.
- Project G still owns real phone/LAN, OEM DreamService, D-pad/focus, 1080p/4K and actual filesystem/network failure acceptance. F05 owns force-stop/process-death/reboot persistence validation.
- **Next host/source workstream: Project F05.**


## CHECKPOINT PROJECT F05 — PERSISTENCE & RECOVERY (3 November 2026)

- F05 integration PR #54, branch `integration/project-f05-persistence-recovery`, is Draft/open/unmerged and based on approved F04 exact head `6ffc8b4c253beb2662d6978ba38033039c3c53f9`.
- Current F05 integration head `1048b0b68ae5ab5a2ad867183236b514e0c691fa`; compare with F04: 14 commits ahead / 0 behind, five scoped paths.
- F05 host/source suite on run #41563437433: 80 Python tests, 1 intentional README-count skip; Lists/tools/docs PASS. Android unit/release build and APK budget are still running.
- F05-WALL-001 repaired on E08 owner PR #41 head `edd24d72256d06da4578971ea7bac1802746a3a6`; exact-head run #41562661052 both jobs SUCCESS, 15 Python tests / 1 intentional skip, APK 1,564,197 bytes / 8,667-byte headroom. Repair is mirrored in F05.
- F05-WALL-002 repaired on C07 owner PR #24: startup cleanup for orphaned `custom_*.upload` parts uses a process-local active-upload registry, canonical private-path checks, and finally cleanup. Latest owner head `2315f12472f469843c1441ad7d01c6898858c4c5`; run #41563429553 Lists/tools/docs PASS (23 tests / 1 intentional skip), Android build still running. Mirrored into F05.
- Cross-feature source guards cover Avatar/Profile, StandBy atomic collection/mode persistence, Home focus/Settings tab, wallpaper startup recovery, QR temp-upload cleanup, and prayer alarm BOOT_COMPLETED recovery.
- F05 remains IN PROGRESS until both owner/integration Android exact-head CI and APK size gates pass. Actual force-stop/process-death/reboot and OEM behavior remain Project G device gates. No APK publication/installation, no device setting changes, no merge.
- Full evidence: `F05_PERSISTENCE_RECOVERY_AUDIT.md`; resume instructions: `HANDOVER.md`.


## PROJECT F05 HOST/SOURCE EXIT — FINAL CHECKPOINT (3 November 2026)

- F05 integration PR #54, branch `integration/project-f05-persistence-recovery`, exact head `1048b0b68ae5ab5a2ad867183236b514e0c691fa`; exact-head CI #41563437433 both required jobs SUCCESS.
- Python suite: 80 tests, 1 intentional README-count skip. Android `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and size gate PASS.
- APK: 1,566,861 bytes / 1,572,864-byte cap / 6,003-byte headroom. Artifact list empty; no APK publication or installation.
- F05-WALL-001 fixed on E08 owner PR #41 head `edd24d72256d06da4578971ea7bac1802746a3a6`; exact CI #41562661052 both jobs SUCCESS; 15 tests / 1 intentional skip; APK 1,564,197 bytes.
- F05-WALL-002 fixed on C07 owner PR #24 head `2315f12472f469843c1441ad7d01c6898858c4c5`; exact CI #41563429553 both jobs SUCCESS; 23 tests / 1 intentional skip; APK 1,551,661 bytes.
- Owner/integration cross-audit confirms the E08 startup-reconciliation helper and tests, and the C07 active-upload registry/orphan cleanup and tests, are synchronized byte-for-byte in F05. PR #24/#41/#54 remain Draft/open/unmerged.
- **Decision: F05 HOST/SOURCE EXIT PASS.** No remaining reproducible host/source blocker. Real force-stop/process-death/reboot, prayer alarm restoration, StandBy/DreamService, phone/LAN and OEM behavior remain DEFERRED_TO_G. No APK publication/installation or physical TV testing.
- **Next host/source workstream: Project F06 — Localization.** Project G retains the physical acceptance gates.
- Full evidence: `F05_PERSISTENCE_RECOVERY_AUDIT.md`; resume instructions: `HANDOVER.md`.


## PROJECT F06 — LOCALIZATION & GLOBAL UI CONSISTENCY — FINAL HOST/SOURCE CHECKPOINT (3 November 2026)

- F06 integration PR #55, branch `integration/project-f06-localization`, remains Draft/open/unmerged under D-013. Base is F05 exact head `1048b0b68ae5ab5a2ad867183236b514e0c691fa`; F06 exact head `487099013e6d9bd65cc07d72e760b1e14ab1f091`; compare 29 commits ahead / 0 behind, nine changed paths.
- Exact-head CI #41566633872: both required jobs SUCCESS. Python suite 89 tests / 1 intentional README-count skip; Android `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and size gate PASS.
- APK 1,569,397 bytes / 1,572,864-byte cap / 3,467-byte headroom. Workflow artifacts empty; no APK publication or installation.
- Three canonical resource sets (`values`, `values-id`, `values-en`) contain 615 keys each, with zero duplicates, key-set mismatches, placeholder mismatches or empty strings. Android locale config advertises only `id` and `en`; API 33+ LocaleManager and API 26–32 LocaleContext paths are source-guarded across visible Activities and StandBy DreamService.
- F06 fixed hardcoded Indonesian schedule labels, weather H/L abbreviations, reversed daily-ayah translations, untranslated Indonesian Glass Effect title, two custom dialogs bypassing the global modal contract, and QR upload/browser error fallbacks, gallery date locale and thumbnail accessibility label. Equivalent error text was consolidated onto canonical localized resources to recover APK headroom.
- **Decision: F06 HOST/SOURCE EXIT PASS.** PR #55 stays Draft/open/unmerged. No merge, APK publication/installation, device setting changes or physical TV testing.
- Project G retains physical Android 11/13+ language switching, rendered UI/popup/D-pad, phone/LAN QR and mobile browser acceptance. Next host/source work unit: Project F07.
- Full evidence: `F06_LOCALIZATION_UI_AUDIT.md`; resume instructions: `HANDOVER.md`.


## PROJECT F07 — PERFORMANCE, CRASH, FREEZE, RAM & STABILITY — HOST/SOURCE CHECKPOINT (3 November 2026)

- F07 integration PR #56 remains Draft/open/unmerged under D-013. Branch `integration/project-f07-performance-stability`; base F06 exact head `487099013e6d9bd65cc07d72e760b1e14ab1f091`; F07 exact head `976e068d48ecc1ede2811ac455372395d382ba76`; compare 19 ahead / 0 behind, ten changed paths.
- Exact-head CI #41568715178 both required jobs SUCCESS. Python suite 99 tests, all PASS. Launcher and installer `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and size enforcement PASS.
- APK 1,571,293 bytes / 1,572,864-byte cap / 1,571-byte headroom. Workflow artifact list empty; no APK published or installed.
- F07 repairs: weather per-frame RadialGradient allocations and ripple object churn; MainActivity fallback and QR preview UI-thread image decode; stale wallpaper task/bitmap lifecycle; main-thread Now Playing Binder polling; Backdrop OOM and partial work-array recovery; unbounded StandBy photo decode queue; forced `System.gc()`; and Online Wallpapers count-only cache with new 512 MiB aggregate budget.
- **Decision: F07 HOST/SOURCE EXIT PASS; overall performance acceptance PARTIAL.** No physical Mi TV Stick profiling/soak, no FPS/RAM/ANR/thermal measurement, no APK publication/installation, no merge or device setting change.
- Project G must measure actual target-device performance and long-run stability after explicit approval for one labelled TV-test APK. PR #56 stays Draft/open/unmerged. Next workstream: Project G final integrated audit under D-013.
- Full evidence: `F07_PERFORMANCE_STABILITY_AUDIT.md`; roadmap status: `BUG_REPAIR_ROADMAP.md`; resume: `HANDOVER.md`.


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


## G01 integrated acceptance checkpoint — 3 November 2026

- G01 is now **FULL PASS**: source defect resolution, 26-path B-series/F07 semantic reconciliation, isolated merge, and exact merge-commit CI.
- Accepted integration branch: `integration/b-series` @ `f6f1f05b7a560381b8bb423eaa707a1d6d0e8832`.
- PR #58 merged successfully; PR #57 was superseded by the accepted candidate and closed.
- Exact merge-commit CI #41583505195: both jobs SUCCESS; 99 Python tests all OK; launcher/installer Android unit tests PASS; release validation build SUCCESS; APK diagnostic 1,571,293 / 1,572,864 bytes; no artifacts.
- The one main-only commit enabling manual APK publishing is intentionally excluded. `main` remains untouched.
- G02 final tested APK build is READY TO START from the accepted integration commit. G03 physical Mi TV Stick acceptance remains pending; no APK has been published/installed and no device setting changed.

## Latest G01 coverage re-audit — 3 November 2026

- Re-audited the full A–G roadmap coverage and owner-branch deltas before allowing G02.
- Confirmed legacy PR #2–#5/#7–#8 ancestry in the integration lineage; PR #6 was selectively superseded through PR #9, not merged wholesale.
- Reconciled the 26 shared B-series/F07 paths; candidate retains stronger F07 bounded queues, shader reuse, upload deadlines/storage reservation and lifecycle/persistence behavior.
- Reopened E05 after finding stale-location weather/AQI cache risk and non-API-backed condition text in the prior candidate. Restored per-source timestamps and coordinate ownership, retained API-backed weather display, preserved F07 reduced-motion/low-memory behavior and F06 localized high/low labels.
- Restored the missing E08 wallpaper-delete failure regression test.
- PR #59 merged to `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`. Tested tree `9f2da8f39a20c8e8cc43ac0ff7e42d7a82b34676` is identical to merge tree (zero file delta).
- CI #41586223809 / run #1177 SUCCESS: 109 Python tests / 1 intentional skip; Android unit tests/release build PASS; APK 1,572,293 bytes, 571-byte headroom; no artifacts.
- G01 PASS; G02 READY; G03–G05 pending. `main` untouched; no APK publication/installation or TV setting changes.


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

- G01 HOST/SOURCE PASS and G02 HOST BUILD + ARTIFACT PASS remain the documented entry gates.
- G03 preflight revalidated live integration SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d`, run #1185 with both jobs SUCCESS, and available artifact ID `13210715335`.
- Downloaded artifact digest matches GitHub SHA-256 `669491bfc7c9f5279ecc6c30915e4a340aa492bee21531db203036caf8651c81`; extracted APK is 1,572,293 bytes with SHA-256 `dc85fcd590eb2c52eb367d0bc9d4f67735cb94ef1cbf6f2b80b86f0de36de0dd`; archive integrity PASS.
- Physical Mi TV Stick acceptance is not performed: host lacks ADB/signature tools and no TV is connected. Installation and device/package/ADB changes require explicit readiness approval.
- G03 remains OPEN/BLOCKED, not PASS. See [G03_DEVICE_ACCEPTANCE_PREFLIGHT_2026-11-05.md](G03_DEVICE_ACCEPTANCE_PREFLIGHT_2026-11-05.md). Artifact expiry: 10 November 2026 06:37:50 UTC.


## UI Refresh — P0 Baseline & Compatibility (6 November 2026)

- P0 **PASS — BASELINE VERIFIED**. Frozen integration: `562cde0751de93748eb4e0d4cdece4fa1672ac4d`; main remains at `6eaf787c56226606b1d2e650c59540c553756c62`, untouched.
- Live owner PRs: E02 #62 head `588456144df24f2f6c015a01053ad700a04bee39` (branch `feature/e02-giphy-avatar-search-v1`), base `3ab9b55f3cfe03ad9841a119590a9183cf114e96`; E06 #63 head `ecdeb0eba9fc7a574f1a6c9930205517687cd922` (branch `feature/e06-dock-capacity-control-v1`), base `570b7c83efaec3ac515b78f1c6c119b777fc3b84`. Both OPEN/DRAFT/UNMERGED.
- Exact-head CI: E02 #1204 both jobs SUCCESS, APK 1,566,569 B; E06 #1198 both jobs SUCCESS, APK 1,563,741 B. Publication skipped. Integrated baseline APK is 1,572,293 B / 1,572,864 B cap / 571 B headroom.
- Dependency ancestry is recorded in `UI_REFRESH_P0_BASELINE_2026-11-06.md`: PR bases are ancestors of integration; feature heads are diverged; E01 has three unique commits relative to the E02/E06 bases. Three-way semantic reconciliation remains mandatory before integration.
- B01 tokens/modal/focus contracts and Android configuration were inspected. Last recorded Mi TV Stick baseline is Android 11/API 30, 1920×1080, density 320; not physically re-probed in P0.
- E02 cancellation/lifecycle/provider-policy and E06 independent Dock architecture/interaction remain open acceptance blockers. No Critical defect reproduced; no production or device change.
- P1 Functional Inventory is the next stage and has NOT STARTED. No P4 implementation authorization is implied.
- Evidence: `UI_REFRESH_P0_BASELINE_2026-11-06.md`.


## UI Refresh — P1 Functional Inventory (6 November 2026)

- P1 source inventory document: [UI_REFRESH_P1_FUNCTIONAL_INVENTORY_2026-11-06.md](UI_REFRESH_P1_FUNCTIONAL_INVENTORY_2026-11-06.md).
- Live E02 #62 head `588456144df24f2f6c015a01053ad700a04bee39`, base `3ab9b55f3cfe03ad9841a119590a9183cf114e96`; E06 #63 head `ecdeb0eba9fc7a574f1a6c9930205517687cd922`, base `570b7c83efaec3ac515b78f1c6c119b777fc3b84`. Both remain OPEN/DRAFT/UNMERGED.
- Exact-head CI rechecked: E02 run #1204 and E06 #1198, both required jobs SUCCESS; APK publication skipped. No device evidence implied.
- P1 mapped Profile/avatar/QR/GIPHY/profile management/Child Mode/app policy and Home/app grid/Dock/settings/focus surfaces to owner, current behavior/data/state/persistence and tests.
- P1 disposition: inventory recorded; PARTIAL pending user acceptance and listed clarifications. P2/P3 remain required before P4. No production or device changes.



## UI Refresh — Master Visual Reference Global UI audit (6 November 2026)

- Read-only audit recorded at [UI_REFRESH_MASTER_VISUAL_REFERENCE_AUDIT_2026-11-06.md](UI_REFRESH_MASTER_VISUAL_REFERENCE_AUDIT_2026-11-06.md).
- Overall result **BELUM PASS** for implementation readiness. P0 PASS; P1 PARTIAL; P2 COMPLETE/frozen; P3 PARTIAL; P4 NOT STARTED/BLOCKED.
- Existing B01 shared UI components are reusable; focus coverage and global responsive/overscan/text-scaling need extension. E02 and E06 owner PRs remain Draft/unmerged and diverged from frozen integration; E02 cancellation/lifecycle and E06 Dock/grid separation remain blockers.
- Impacted modules: B01–B07 and E01–E08. Responsibility, exact refs, current gaps and recommended dependency order are documented in the audit file.
- No production source, owner branch, PR metadata, integration/main, APK, ADB or device was changed. No redesign or implementation was performed.



## UI Refresh — P3 V2 static interaction re-audit (6 November 2026)

- Detailed result: [UI_REFRESH_P3_V2_STATIC_INTERACTION_REAUDIT_2026-11-06.md](UI_REFRESH_P3_V2_STATIC_INTERACTION_REAUDIT_2026-11-06.md).
- P3 remains PARTIAL / BELUM PASS. Two high-severity prototype blockers: nested dialog root-origin/focus restoration is broken; modal arrow-key/D-pad navigation is disabled. Medium gaps: inert Language/Wallpaper Interval, missing Next Photo failure path, Wi-Fi rescan loading completion, profile switch simulation and action-specific restart/power confirmation.
- Preserve V2 unchanged; fix in a new versioned prototype only. P1 acceptance remains pending. No production source or branch/PR/integration/APK/device mutation.



## UI Refresh — V3 prototype static QC checkpoint (6 November 2026)

- V3 prototype created; V2 preserved. Static QC PASS for JavaScript syntax parse, unique IDs and R01–R07 correction presence. Full evidence: [UI_REFRESH_P3_V3_STATIC_QC_2026-11-06.md](UI_REFRESH_P3_V3_STATIC_QC_2026-11-06.md).
- P3 remains PARTIAL pending browser/keyboard walkthrough, all 12 scenarios and visual comparison with the six user-approved references. No production source, branch/PR/integration/APK/device mutation.



## UI Refresh — P1/P3 current disposition (6 November 2026)

- P1 inventory CLOSED FOR PROGRESSION with known technical/runtime gaps carried forward: [P1 acceptance disposition](UI_REFRESH_P1_ACCEPTANCE_DISPOSITION_2026-11-06.md).
- V3 prototype static QC PASS; browser walkthrough plan is versioned but NOT RUN: [P3 V3 walkthrough plan](UI_REFRESH_P3_V3_BROWSER_WALKTHROUGH_PLAN_2026-11-06.md).
- P3 remains PARTIAL; P4 remains blocked until 12-scenario browser walkthrough and visual comparison against approved V01–V06 references are completed.



## UI Refresh — V4 current checkpoint (6 November 2026)

- V4 prototype-only hardening addresses Weather Refresh focus retention, stale Wi-Fi scan cancellation and modal Tab focus containment / input-safe Backspace.
- Static QC PASS; browser walkthrough and V01–V06 visual acceptance NOT RUN. Evidence: [UI_REFRESH_P3_V4_STATIC_QC_2026-11-06.md](UI_REFRESH_P3_V4_STATIC_QC_2026-11-06.md).
- P3 remains PARTIAL; P4 remains blocked. No production source change.
