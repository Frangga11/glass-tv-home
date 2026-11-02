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
