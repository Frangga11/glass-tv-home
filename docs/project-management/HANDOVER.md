# Project Handover

**Project:** Glass TV Home  
**Documentation branch:** `docs/project-master-inventory`  
**Last updated:** 1 November 2026

## Current wallpaper work

- User-approved wallpaper: **Kabut Pagi Dempo**.
- Required placement: existing **Default** tab only. No extra Dempo tab/category/collection.
- Code branch: `fix/wallpaper-qr-session-security`.
- Draft PR #7: https://github.com/Frangga11/glass-tv-home/pull/7
- Asset committed: `launcher/app/src/main/assets/kabut-pagi-dempo.jpg` (230,883 bytes; Git blob `758f4225581f8c01e62d26c8d73c9aec43adb0df`).
- Temporary `.gitkeep` removed; Assets directory currently contains only the wallpaper JPEG.
- Loader uses the packaged asset and refreshes the private runtime cache. Unknown/retired category values migrate to Default through a supported-category check; no Malaysia-specific source branch remains.
- Latest exact-head CI run #195 (`41214569233`) passed both jobs on PR #7 head `62b1e7ce63f34095b44afbde7fc80e19addc9b00`: Android unit tests/release build/size guard/artifact publication and lists/tools/docs checks all passed. Earlier CI #172 used an earlier commit and is superseded for current-head verification.
- Latest artifact ID `13047428749` was downloaded and inspected. Artifact ZIP: 1,332,722 bytes. APK `app-release.apk`: 1,419,677 bytes; SHA-256 `9b0e581a1c710ea8139c53fb001f3d11ed47ed55c0d36dd9b10f8adce66d6ed2`. APK contains exactly one image asset: `assets/kabut-pagi-dempo.jpg` (230,883 bytes; 1536 × 864 px; embedded SHA-256 `04e5793b92cab1fcaeaaf0e3b80769598f91c7521b8e3d62c4f7a38888982982`). No second Dempo image or additional image asset under `assets/` was found.
- Cleanup pass 31 Oct 2026: removed `my.com.tonton.screen` and `my.gov.rtm.tv` from the default dock; remaining configured dock apps and dock behavior are preserved. Checked MainActivity, config.xml, OnlineWallpapers, PlaceWallpapers, Config, StandbyPhotoStore, and WallpaperUploadServer for Malaysia/Kuala Lumpur/Petronas remnants; none remain in those inspected source files.
- Cleanup commits: MainActivity `54bbede6c621aa8e305a9dd255978725359023b8`; dock config `6a69dded53d8328164e01e49be8ba2f7c2da8ae5`. Subsequent regional cleanup and profile parental-settings commits are included in the exact-head CI #195 run.
- PR remains Draft and unmerged.

## Next required gates

1. CI exact-head verification: PASS — run #195 (`41214569233`) on PR head `62b1e7ce63f34095b44afbde7fc80e19addc9b00`.
2. APK packaging inspection: PASS — artifact `13047428749`; one `assets/kabut-pagi-dempo.jpg`, 230,883 bytes, 1536 × 864 px, hash matches approved source; no duplicate image asset.
3. Test clean install and offline startup on the target TV.
4. Test upgrade from legacy Malaysia selection and stale cache replacement.
5. Complete QR/security and remote-control device tests.
6. Cross-audit corrected the stale Malaysia migration comment and incomplete Appearance card description in code commit `7bca68e56e51ea828c2ef7d22ca830470ca28381` (CI #167 passed).
7. Wallpaper upload and Standby photo stores remain distinct (`custom_wallpapers` vs `standby_photos`); QR sessions use separate routes. Device-level isolation tests remain pending.
8. Follow-up hardening commits `da1e43a241582c71a86e54530c730f20f225b2ef` and `91a31e757d796bd1384a962ced2054ce74badd96` prevent Default from silently falling back to a theme/legacy wallpaper and avoid reusing an unverified old cache when the packaged asset cannot be read. Commit `9f5a9411120dad879aaa1d8c1bb8cc6e90684924` clears the prior image and shows a safe black background if Default cannot load after manual selection. CI #167, #168, #169, and #170 all passed; latest CI #170 is https://github.com/Frangga11/glass-tv-home/actions/runs/41206969870.
9. Historical artifact `13044766496` was superseded by the exact-head artifact `13047428749` (see verification above).

**Disposition: NOT FINAL / NOT READY FOR MERGE. Exact-head CI #195 and APK asset inspection PASS. Physical Mi TV Stick + phone acceptance remains mandatory: clean install/offline cold start, legacy upgrade/cache refresh, remote navigation, QR session isolation/revocation, and wallpaper/standby/avatar separation. Keep PR #7 Draft until device evidence is recorded.**


## Profile Tab audit — 31 October 2026

- Detailed source audit: [PROFILE_AUDIT.md](PROFILE_AUDIT.md).
- PR #6 avatar branch and PR #7 wallpaper/profile branch diverged from the common settings/gallery base; both change MainActivity, ModernSettingsDialog, ProfileManager, and UserSwitchDialog. Do not merge blindly.
- PR #7 contains per-profile Child Mode/locked/hidden lists and global parent PIN, but source review found P1 gaps: lock/hide list editor lacks PIN gate; profile deletion lacks PIN authorization; default PIN 0000 is not forced to change.
- At the time of the original PR #7 handover, custom avatar QR flow was not integrated; it is now selectively integrated in PR #9.
- CI #195 validates PR #7 exact head only; PR #6 CI validates its own isolated head. Neither validates a combined integration.
- Keep PR #6 and #7 Draft/unmerged. No APK install or stock-launcher modification has occurred.
- Next code gate: create a dedicated integration branch from PR #7 head; selectively integrate avatar changes; fix P1 authorization gaps; add profile regression tests; run CI on the combined head.
- Next device gate: D-pad/dialog/profile/avatar/persistence tests on actual Mi TV Stick and phone.


## Profile security implementation — 31 October 2026

- Dedicated integration branch: `feature/profile-security-integration`; draft PR #8 targets PR #7's branch. This preserves PR #7 and PR #6 without direct merging.
- PR #8 source changes: parent PIN gate before opening locked/hidden app editors; parent PIN gate before profile deletion plus a second destructive confirmation; shared switch policy across top-bar and Settings switchers.
- Strict switching rule: while current profile has Child Mode active, any switch to another profile requires parent PIN, including child-to-child, preventing policy downgrade by changing profile.
- Locked/hidden lists and Child Mode remain per profile; parent PIN remains device-global.
- User decisions: retain default PIN `0000` temporarily (user will change it later); do not add failed-PIN limits, cooldown, or lockout.
- Regression tests added for switch policy and profile-key isolation.
- CI #196 (`41216406132`) on PR #8 head `f0971b17ed752a9ac0bf16afa904990723c0c616` was in progress at last check. Physical Mi TV Stick/phone acceptance remains pending.
- Avatar QR PR #6 remains a divergent source branch; its changes are selectively integrated in PR #9. Do not merge PR #6 or #7 directly.


## Profile security follow-up — PR #8

- Draft PR #8: https://github.com/Frangga11/glass-tv-home/pull/8; branch `feature/profile-security-integration`, latest head `1ba744efee61fde662493cebd382743a5927aefa`, based on PR #7 head.
- Parent PIN is required before editing locked/hidden app lists and before profile deletion (with a second destructive confirmation).
- Switching away from any active Child Mode profile requires parent PIN, including child-to-child switching; top-bar and Settings entry points share the same switch authorization method.
- Child Mode, locked apps, and hidden apps remain per-profile; PIN remains device-global.
- User decisions: keep default PIN `0000` temporarily; user will change later. No failed-PIN attempt limit, cooldown, or lockout.
- CI #197 (`41216508402`) passed both jobs on exact latest PR #8 head `1ba744efee61fde662493cebd382743a5927aefa`.
- Release artifact `13048321564`: APK 1,420,005 bytes; SHA-256 `735be1ff6e8ff88d31bf9de73940e527a570bbaace96c5ec9a001ce95610a44e`.
- Avatar PR #6 remains unintegrated due to branch divergence; selectively integrate after profile security CI. No PR merged and no APK installed.


## Profile + avatar integration — PR #9 (31 October 2026)

- Draft PR #9: https://github.com/Frangga11/glass-tv-home/pull/9; branch `feature/profile-avatar-security-integration`, base PR #8 branch `feature/profile-security-integration`.
- Exact head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`; CI #208 (`41218171447`) passed both jobs, including `testReleaseUnitTest`, `assembleRelease`, APK size guard, and artifact publication.
- Avatar implementation is selectively integrated: private per-profile file store, secure QR server bound to selected LAN IPv4, five-minute one-success session, 15 MB/80 MP bounds, image decode validation, fsync before profile reference commit, bounded clients and stop/expiry cleanup.
- Avatar views are synchronized across top bar, Settings sidebar, active profile row, and profile switcher. Child Mode toggles refresh top bar/sidebar/list state; profile rename refreshes top-bar accessibility text/sidebar/list.
- Profile deletion persists list removal before avatar cleanup and clears only that profile's parental-control keys after releasing the profile lock; device-wide PIN is preserved.
- User decisions remain: PIN 0000 temporarily; unlimited incorrect PIN attempts; no cooldown/lockout.
- APK artifact `13048951728`: 1,427,681 bytes; SHA-256 `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`.
- Physical Mi TV Stick + phone acceptance is still open. No PR merged and no APK installed.
- Merge sequence if approved after device tests: PR #8 → PR #9. PR #6 and PR #7 remain unmerged source branches.


## Official repair roadmap checkpoint — 1 November 2026

- Approved roadmap: [BUG_REPAIR_ROADMAP.md](BUG_REPAIR_ROADMAP.md), ID `GTVH-BRR-001`, version 1.0.
- A01–A03: DONE.
- A04: IN PROGRESS — repository identity, branch graph, source tree, PR #9 exact-head baseline, and relevant navigation code inspected. The remote GitHub source was inspected; a local worktree status was not available in this session.
- A05: NOT STARTED — user's tested APK has not yet been matched to an exact source revision.
- A06: NOT COMPLETE — a dedicated GitHub branch provides a code rollback checkpoint, but a full baseline/backup package and device-state backup have not been produced.
- B01: IN PROGRESS by explicit user direction; sequencing exception D-009 recorded.
- B01 source baseline: PR #9 exact head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`.
- B01 working branch: `feature/b01-global-navigation-retention`.
- B01 Draft PR #10: https://github.com/Frangga11/glass-tv-home/pull/10.
- B01 current source head: `bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5`.
- Exact-head CI #225: https://github.com/Frangga11/glass-tv-home/actions/runs/41229763017 — PASS, both jobs; unit tests and release build succeeded; APK size 1,395 KB (<1.5 MiB); final TV-test artifact publication was skipped and no artifact was produced.
- CI workflow now builds/tests release variant but publishes a TV-test APK only through manual workflow_dispatch opt-in after explicit readiness approval.
- Implemented so far: home app focus key + slot persisted; stable semantic top-bar focus key; snapshot/restore full focus state across RecyclerView rebind; normalized empty-list persistence; safe fallback for empty/failed scroll focus; Settings selected tab/sidebar cursor persistence; canonical shared tab IDs; expanded JVM navigation-state tests.
- B01 source-level audit and exact-head CI #225 are PASS. Remaining acceptance is deferred to final integrated regression stage F02; keep roadmap B01 IN PROGRESS until then.
- B01 PR #10 is isolated on `feature/b01-global-navigation-retention`, head `bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5`.
- B02 is in progress on `feature/b02-remote-focus-indicators`, based on B01; Draft PR #11: https://github.com/Frangga11/glass-tv-home/pull/11.
- B02 current head: `01cbef2c2fc90498295971b22c369a72cdfa734a`; exact-head CI #234 is pending. CI #233 passed the preceding snapshot. Physical Mi TV Stick acceptance is deferred to final integration stage F02, after all A–E audit/fix work. Settings right-pane action-return focus is tracked under B07.
- No APK has been installed in this repair session; no device settings changed; PR #8/#9/#10 remain unmerged Draft/open. User explicitly requires no TV testing until all audits and repairs are complete.
- Next action: pause further B02/B03 work. A04 remote repository/source audit is complete and recorded. Obtain the exact identity of the APK most recently tested on the Mi TV Stick (A05); then establish the matching source checkpoint, APK backup/hash, device-state backup, and rollback procedure (A06). Continue A–E audits/fixes and F01–F07 before generating/publishing one final TV-test APK.
- At each session close, update roadmap statuses, audit/operations records, this handover, and ChatGPT continuity memory. Report synchronization only after verifying the GitHub commit.


## Latest checkpoint — A04 complete; A05/A06 pending — 1 November 2026

- Official roadmap: `GTVH-BRR-001 v1.0`.
- A04: DONE for remote repository/source audit. Evidence in `INVENTORY.md` and `AUDIT_REGISTER.md`.
- Repository: `Frangga11/glass-tv-home`, a fork of `aqmangge/glass-tv-home`.
- Current B02 branch head: `01cbef2c2fc90498295971b22c369a72cdfa734a`; Draft PR #11 remains open/unmerged.
- B01 branch head: `bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5`; Draft PR #10 remains open/unmerged.
- Exact-head CI #225 (B01) and #234 (B02) both PASS; each ran launcher tests/build and lists/tools/docs checks. Manual TV-test APK publication was skipped.
- A04 limitation: this session used GitHub remote inspection only; no local checkout/worktree was opened or changed. A separate local clone, if any, has not been inspected.
- A05: no exact APK identity has been matched. Existing Gradle metadata 4.0.0/code 40 is not unique to a branch. Need installed APK evidence (preferably extracted APK file and SHA-256; package dump/path as supporting metadata).
- A06: do not create the final checkpoint until A05 maps the tested APK to an exact commit. Then capture source ref, APK/hash, device-state backup, and rollback instructions.
- **Execution hold:** no further B02/B03 implementation, no PR merge, no candidate APK installation, no device setting changes, and no TV-test artifact publication while A05/A06 and the remaining audits are unresolved.
- Next resume point: A05 — identify the installed/tested APK from the Mi TV Stick.


## Latest checkpoint — A05 complete; A06 device snapshot pending — 1 November 2026

- A05 DONE: user supplied the exact APK used in the Mi TV Stick test. The APK was directly extracted from CI artifact `13048951728` (run #208 / `41218171447`) and compared byte-for-byte with the upload: both 1,427,681 bytes, SHA-256 `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`.
- CI artifact ZIP is a separate object: 1,340,733 bytes, SHA-256 `85475980a94b35a1a356cd5b5156bae3b50e7b405bc76ca1c22cd1e9557d32d6`. Workflow metadata maps the artifact to PR #9, branch `feature/profile-avatar-security-integration`, exact source commit `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`.
- A06 source checkpoint branch: `checkpoint/a06-tested-apk-pr9-7e8392e1`; created directly from exact tested source commit.
- Checkpoint manifest: `docs/project-management/checkpoints/A06_TESTED_APK_BASELINE.md` on the checkpoint branch.
- A06 remains IN PROGRESS. Historical chat evidence recovered: `com.mitv.tvhome` appeared disabled; `com.google.android.tvlauncher` and `com.gathin.tvhome` appeared enabled; `get-home-activities` returned `unknown`; `set-home-activity` returned `Success`; `am start` said the launcher activity was already running. This does not prove current HOME ownership or installed APK hash. Still required: fresh read-only `getprop`, `pm path`, `dumpsys package`, `resolve-activity HOME`, enabled/disabled package lists, and relevant secure/global settings before any device changes.
- No PR merged; no new APK installed; no device settings changed.
- B01/B02 implementation remains paused at user's request until A06 is closed.
- Next action: collect the read-only ADB baseline listed in the A06 checkpoint manifest; then complete rollback-readiness review.


## Latest repair handover — A06 rollback documentation advanced

- User-confirmed PR #9 APK remains the official known-good tested baseline; its historical test is accepted and will not be requested again.
- A05 is DONE: exact uploaded APK ↔ CI artifact #13048951728 match, 1,427,681 bytes, SHA-256 `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`, source commit `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`.
- A06 checkpoint branch: `checkpoint/a06-tested-apk-pr9-7e8392e1`. Updated manifest and added [A06_ROLLBACK_PROCEDURE.md](https://github.com/Frangga11/glass-tv-home/blob/checkpoint/a06-tested-apk-pr9-7e8392e1/docs/project-management/checkpoints/A06_ROLLBACK_PROCEDURE.md).
- Rollback SOP: verify APK hash, capture state, restore stock HOME through system UI without guessing an activity component, and reinstall the baseline with `adb install -r` only after package/signature compatibility is confirmed. Avoid uninstall/clear-data/factory-reset actions.
- Historical ADB observations are documented but do not establish current HOME owner or installed APK path/hash. The only remaining A06 gap is a fresh read-only device snapshot; it is not a request to retest the APK.
- No candidate APK installed, no TV setting changed, no PR merged.
- **Resume point:** obtain the read-only Bugjaeger/ADB snapshot in the A06 manifest when convenient; review it, close A06, then resume the approved roadmap. B01/B02 implementation remains paused until A06 closes.


## Latest repair handover — A06 CLOSED; resume B01/B02 source work

- A06 is **DONE**. User supplied the complete read-only Bugjaeger/ADB snapshot on 1 November 2026.
- Device: `MiTV-AYFR0`, Android 11 / SDK 30, fingerprint `Xiaomi/soul/soul:11/RTT0.211222.001/1546:user/release-keys`.
- Glass TV Home installed at `/data/app/…/com.gathin.tvhome-…/base.apk`, version 4.0.0/code 40. Installed APK bytes/hash were not extracted; only package metadata matches baseline version.
- HOME resolver returns `com.google.android.tvlauncher/.MainActivity`, but `dumpsys package` preferred-activity table lists `com.gathin.tvhome/.MainActivity`. Carry this discrepancy into E01; do not change HOME settings or assume the two outputs are equivalent.
- Current enabled list includes both `com.google.android.tvlauncher` and `com.gathin.tvhome`. Current disabled list includes `com.mitv.tvhome.atv`; do not confuse this with the historical package name `com.mitv.tvhome`.
- A06 checkpoint manifest and rollback SOP are on `checkpoint/a06-tested-apk-pr9-7e8392e1`. User's historical PR #9 APK test remains accepted; no retest requested.
- No device settings/packages changed; no candidate APK installed; no PR merged.
- **Next execution point:** resume source-level work on B01/B02 in their existing isolated Draft PRs. Physical TV acceptance remains deferred until final integrated regression stage F02 and explicit final APK approval.


## B02 second-pass audit — 1 November 2026

- PR #11 remains open/Draft, branch `feature/b02-remote-focus-indicators`, exact head `01cbef2c2fc90498295971b22c369a72cdfa734a`.
- Reviewed all five changed files and relevant focus-listener context. No source-level listener collision was identified in the inspected paths.
- CI #234 / run `41230495879`: Launcher build/unit tests and Lists/tools/docs both PASS; APK size guard PASS; manual TV-test artifact publication SKIPPED. No candidate artifact created.
- B02 source review/CI gate PASS, but task remains IN PROGRESS until integrated remote acceptance in F02.
- A06 is now DONE after receipt and documentation of the read-only Mi TV Stick snapshot. E01 must investigate the resolver/preferred-activity mismatch without changing device state.
- **Next source task:** B03 — audit dialog/popup opacity, layering, focus, sizing and dismissal. No TV APK installation/testing or PR merge.


## B03 execution checkpoint — opacity patch in CI

- A06 closed after the user-provided read-only device snapshot. Resolver/preferred-activity mismatch is a carry-forward for E01.
- B02 second-pass source audit and exact-head CI #234 passed; physical acceptance remains F02.
- B03 working branch: `feature/b03-dialog-opacity-layering`; Draft PR #12: https://github.com/Frangga11/glass-tv-home/pull/12.
- Current head: `3f11618b34f7edb8618c74e6b2fe48dd7c9e6ccf`; base: B02 head `01cbef2c2fc90498295971b22c369a72cdfa734a`.
- Changes: shared AlertDialog background, shared glass panel, and phone QR upload panel made fully opaque. Interactive blurred-wallpaper `GlassDrawable` popups intentionally retained.
- CI #235 / run `41233304847`: Lists/tools/docs job passed; Launcher build/unit-test job was still running at last check. No TV-test APK artifact published.
- Next: finish B03 source audit for dialog geometry/focus/dismissal, verify CI, then proceed B04. No device test or PR merge.


### B03 follow-up — focus and viewport geometry

- Latest PR #12 head: `56a4e1995aa8b8600614e6b5c4dca69d01c530c3`.
- Added explicit D-pad focus backgrounds to standard AlertDialog action buttons and list rows.
- QR upload dialog now has a 90%-viewport maximum width/height, a vertical ScrollView, and D-pad Up/Down scrolling while the Close button remains the focus target. Its existing onDismiss server-stop cleanup remains intact.
- Now Playing and module-card prompt widths are clamped to 90% of the TV viewport.
- Exact-head CI #239 / run `41233548406`: Lists/tools/docs PASS; Launcher build/unit-test job running at last check.


### B03 source/CI gate — PASS

- Exact head `56a4e1995aa8b8600614e6b5c4dca69d01c530c3`; CI #239 / run `41233548406` succeeded in both jobs.
- Launcher unit tests and release build succeeded; APK size check reported 1,398 KB (<1.5 MiB).
- Lists/tools/docs checks passed. Manual final TV-test artifact step was skipped; no artifact was published.
- Source-level B03 changes are ready for final integrated acceptance, but B03 remains IN PROGRESS until F02 physical TV regression.
- **Next source task:** B04 — audit global layout, spacing, sizing, and TV responsiveness. Keep PR #12 Draft and do not install or test candidate APKs.


## B04 initial audit — display metrics pending

- B04 is IN PROGRESS. Source review confirms the home grid computes tile width from available width/column count, hero and Islamic shelf widths are proportional, Settings is bounded/scrollable, and WeatherActivity uses a full-screen ScrollView.
- Potential viewport-sensitive surfaces: Recent Apps cards are fixed 420×250dp with 64dp side padding; Nightstand clock is fixed at 110sp without explicit width cap. These are not proven defects.
- Do not resize speculatively. Need the target TV's current read-only display metrics: run `wm size` and `wm density` without arguments. This does not change settings.
- Final visual acceptance remains in F02; no TV APK installation/testing is authorized now.
- B03 source/CI gate passed at head `56a4e1995aa8b8600614e6b5c4dca69d01c530c3`, CI #239 PASS; PR #12 remains Draft.

## Latest checkpoint — B01–B03 source QC PASS; B04/B05 underway — 1 November 2026

- A04, A05, A06: DONE.
- B01 source QC PASS: PR #10, branch feature/b01-global-navigation-retention, head bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5; exact-head CI #230 run 41230264083 PASS, APK 1,395 KB, no artifact.
- B02 source QC PASS: PR #11, branch feature/b02-remote-focus-indicators, head 01cbef2c2fc90498295971b22c369a72cdfa734a; exact-head CI #234 run 41230495879 PASS, APK 1,396 KB, no artifact.
- B03 source QC PASS: PR #12, branch feature/b03-dialog-opacity-layering, head 56a4e1995aa8b8600614e6b5c4dca69d01c530c3; exact-head CI #239 run 41233548406 PASS, APK 1,398 KB, no artifact.
- All three remain Draft/unmerged and remain IN PROGRESS in the roadmap until integrated regression and physical acceptance at F02. No candidate APK installed; no TV settings changed.
- B04 is IN PROGRESS. Home grid, Settings, and custom dialogs are largely responsive/bounded. Potential viewport-sensitive elements: Recent Apps 420×250dp cards + 64dp side padding; Nightstand clock 110sp; Weather bento block 385dp inside ScrollView. No speculative resize. Obtain read-only target metrics with wm size and wm density (no arguments).
- B05 is IN PROGRESS. Initial audit: AppLanguage preference supports Indonesian/English and migrates legacy Malay setting, but default Android strings mix languages; values-en-rUS only changes date format; no values-id or generic values-en resource set; many Java UI labels/Toast/dialog/error strings remain hardcoded. First scan found 187 candidate user-facing string-call patterns across 12 high-traffic Java files (not a complete count).
- Next: continue B04 responsive audit after receiving target display metrics; continue B05 complete string inventory and localization design. Keep all PRs Draft; no TV APK publication or device testing until A–E and F integration/regression gates complete and readiness is explicitly approved.


## Latest handover — B01–B03 renewed source QC PASS; B04/B05 expanded audit — 1 November 2026

### Confirmed completed gates
- A04, A05, A06 are DONE. A06 manifest and rollback SOP were verified on checkpoint branch `checkpoint/a06-tested-apk-pr9-7e8392e1`; the full read-only device snapshot was captured. Its HOME resolver/preferred-activity discrepancy and unverified installed APK hash are E01 carry-forwards, not reasons to reopen A06.
- B01/B02/B03 renewed source QC and exact-head CI all PASS. PR #10/#11/#12 remain Draft/open/unmerged; no candidate APK artifact was published, no APK installed, and no TV setting/package changed.
- B01 exact head `bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5`, CI #230 / run `41230264083`, 1,395 KB.
- B02 exact head `01cbef2c2fc90498295971b22c369a72cdfa734a`, CI #234 / run `41230495879`, 1,396 KB.
- B03 exact head `56a4e1995aa8b8600614e6b5c4dca69d01c530c3`, CI #239 / run `41233548406`, 1,398 KB.
- Keep B01–B03 roadmap tasks IN PROGRESS until integrated F01/F02 and physical TV acceptance. Do not merge or create/install a final TV-test APK before the approved gate and explicit readiness approval.

### B04 — current source audit
- Home tiles adapt to grid width/column count (180dp design cap; 80dp minimum).
- Recent Apps cards remain fixed 420×250dp with 64dp side padding.
- Nightstand clock remains 110sp in a centered wrap-content layout.
- Weather page is scrollable, but its Bento body is fixed at 385dp and metric rows around 91–92dp.
- No source-level clipping defect is proven without actual TV logical display metrics. No speculative resizing was made.
- **Next required input:** from Bugjaeger ADB, run `wm size` and `wm density` with no arguments and send the exact outputs. These are read-only commands.

### B05 — current source audit
- Reviewed 66 app Java files, 18 layout XML files, 13 app string-resource XML files, AppLanguage, locale config, manifest, and dependencies.
- AppLanguage has 90 helper/translation methods; saved selection is ID/EN, but legacy Malay branches remain.
- Default Android resources mix ID/EN; no dedicated values-id or generic values-en directory exists; values-en-rUS only overrides date_format. Other locale folders are partial. localeConfig advertises 12 locales but omits ID, while in-app selector offers ID/EN.
- Language selection updates only selected home content/date/toast; it does not set the Android resource locale or refresh all visible resource-backed UI.
- At least 95 direct literal UI-setter calls were found in seven high-traffic Java files, plus hardcoded UI text in eight launcher layouts. This is a lower-bound scan, not a final unique-string inventory.
- Proposed direction: support ID/EN consistently; apply locale-aware contexts for API 26–32 (including API 30 target), synchronize API 33+ app locale where appropriate, complete resource translations, migrate hardcoded UI text, preserve focus/state during language changes, and add parity/migration tests. Companion installer remains a separate scope.
- No B05 source/resource changes applied yet; finish design and implementation after B04's shared layout surfaces are settled to avoid overlapping XML edits.

### Resume order
1. Receive target `wm size` and `wm density` outputs; finish B04 viewport calculations and decide whether any source change is justified.
2. Create/continue B04 branch from B03 head only if a measured defect or worthwhile adaptive improvement is confirmed; run exact-head CI.
3. Then implement B05 on top of the accepted B04 head, so localization XML edits do not conflict with B04 layout work.
4. Keep all PRs Draft and defer physical testing to F02. No APK installation/publication, device changes, or PR merge until all A–E and F integration/regression gates are complete and user explicitly approves readiness.


## Latest handover — B04 target metrics received; B05 ready for implementation audit — 1 November 2026

### B04 measured viewport
- User supplied read-only Bugjaeger output: \`wm size\` = Physical size \`1920x1080\`; \`wm density\` = Physical density \`320\`.
- Logical viewport is \`960×540 dp\` at 2.0 px/dp.
- Recent Apps two-card geometry is a near-fit: 420dp × 2 + 64dp × 2 = 968dp, 8dp wider than viewport before inter-card spacing. This is not by itself a defect; verify intended partial next-card visibility, item spacing and D-pad scroll at F02.
- Nightstand 110sp clock/19sp date and Weather's scrollable 385dp Bento body do not prove clipping at this viewport. Weather body is ~71.3% of logical screen height.
- B04 source + target-metric audit PASS; no speculative layout edits. Physical visual/remote acceptance remains F02. Keep overall roadmap B04 IN PROGRESS until integrated/physical gate.

### B05 implementation guardrails
- Supported app languages: Indonesian and English. Keep the existing persisted preference and migration from legacy \`ms\` to \`id\`.
- Target Android 11/API 30 requires locale handling that works below Android 13; do not rely only on system per-app language APIs.
- Audit all Activity and service lifecycle entry points before selecting the locale wrapper strategy. Ensure every user-facing Activity and dynamic message uses the selected locale.
- Complete Indonesian/English resource parity; correct localeConfig so it advertises only supported complete languages; migrate hardcoded XML/Java UI text including dynamic/error/notification messages.
- Language switching must refresh visible UI without losing navigation tab/sidebar/focus or altering unrelated user data.
- Keep installer module localization separate unless explicitly brought into scope.
- Next action: inventory every Activity/service class and language-sensitive resource/call site, then implement B05 in a dedicated branch from B03 (B04 requires no code delta at current measured viewport). Run exact-head unit tests/build and resource parity checks. Keep PR Draft; no APK publication/install or TV settings change.


### B05 branch opened for isolated implementation audit
- Created feature/b05-id-en-localization from exact B03 source head 56a4e1995aa8b8600614e6b5c4dca69d01c530c3. This is an isolated working branch; no source changes have been committed yet.
- Verified the six manifest Activity entry points: MainActivity, WeatherActivity, RecentAppsActivity, LocationActivity, AzanActivity, and HomeSettingsActivity. The first five are user-facing surfaces; HomeSettingsActivity is a no-display helper and must be assessed separately.
- ClockDream is a DreamService with visible date/prayer text and needs locale-aware handling; HomeKeyService and MediaNotificationListener are service components but do not render ordinary app UI.
- AppLanguage currently stores ID/EN and migrates MS to ID; 90 helper/translation methods exist. MainActivity's language selector currently writes preference and updates selected content but does not recreate the Activity or apply a resource locale. values/strings.xml has 44 keys with mixed ID/EN text; values-en-rUS only overrides date_format; localeConfig advertises 12 incomplete languages and omits ID.
- Implementation must inspect saved navigation/focus restoration around MainActivity recreation before changing the selector. Do not implement a locale wrapper until lifecycle coverage and Activity/service context behavior are verified.
- Next: inspect all 44 resource keys/placeholders, complete Activity and DreamService locale-context strategy, inventory hardcoded user-facing text by screen and dynamic state, then implement in small testable commits. Keep branch unmerged and no APK install/publication.


### Latest B05 checkpoint — implementation underway — 1 November 2026

**Source branch:** `feature/b05-id-en-localization`.
**Draft PR:** [#14](https://github.com/Frangga11/glass-tv-home/pull/14), base `feature/b03-dialog-opacity-layering` at exact head `56a4e1995aa8b8600614e6b5c4dca69d01c530c3`.
**Last audited source head:** `cce889f231aeff3a28805fd517853a3f0a91e79c`.

#### Completed in this checkpoint
- Three Android resource sets (default, `values-id`, `values-en`) have 158 keys each; static key/placeholder comparison passed with no duplicate or mismatched entries.
- `locales_config.xml` lists only Indonesian and English. Manifest labels for Weather, Recent Apps, and the media listener now use resources.
- `LocaleContext` wraps resource configuration for API 26–32; it is applied to MainActivity, WeatherActivity, RecentAppsActivity, LocationActivity, AzanActivity, and ClockDream. HomeSettingsActivity is a no-display helper and is not treated as a normal UI Activity.
- AppLanguage normalization/migration and Android 13+ per-app locale synchronization added. MainActivity recreates after language change on API 26–32 and reopens Settings. B01 restores the saved selected tab/sidebar cursor; right-pane action focus falls back to the sidebar.
- Layout text migrated in Home, Recent Apps, Weather, media hub, OTA/support, close-all, daily forecast row, and Nightstand. Profile switching/creation/deletion, parental PIN feedback, weather conditions, and update/install messages partially migrated.
- Added tests: supported-language normalization in `LocationAndLanguageTest`; resource key/placeholder parity in `LocalizationResourceParityTest`.

#### Not yet complete / risks
- B05 remains IN PROGRESS. Direct Java UI strings remain in MainActivity, ModernSettingsDialog, WeatherActivity dialogs, and other dynamic/error/notification paths. Continue classification and migration; do not claim complete localization.
- Exact-head CI is not verified: no Actions run was present for the current B05 head in the latest query. Static parity is not a substitute for Gradle tests/build. Run CI and inspect both jobs/logs before marking source gate passed.
- Need a careful lifecycle review of DreamService locale wrapping and Android 13 system locale changes; verify language persistence, date/prayer labels, weather, dialogs, dynamic errors, and focus after recreation.
- Installer module remains separate from TV launcher localization unless the user expands scope.

#### Next resume actions
1. Re-audit current branch head before editing; continue migrating Java literals in MainActivity and ModernSettingsDialog, then WeatherActivity and remaining dynamic/error/notification surfaces.
2. Re-scan all app Java/XML for hardcoded user-facing text and check `values-en-rUS` interaction with generic `values-en`.
3. Verify XML well-formedness, key parity, placeholder parity, and all resource references; inspect AppLanguage/LocaleContext and Activity/DreamService lifecycle behavior.
4. Trigger exact-head CI (the connected GitHub actions available in this session only exposed run/job inspection, not workflow dispatch). Keep the manual artifact publication input disabled; inspect tests, release build, size guard, and Lists/tools/docs job.
5. Update PR #14 description and audit/operations records only after evidence. Keep PR Draft/unmerged. No APK publication/install, TV settings change, or physical device testing until A–E and F regression gates are complete and user explicitly approves readiness.


## Latest handover — B04/B05 reconciled source checkpoint — 1 November 2026

### Current GitHub state
- PR #13 B04 head `baf0e7dad2dfa28a9f79c7bb25a58502243f04ae`: exact-head CI #242 PASS; seven-file responsive-layout source scope. F02 physical viewport acceptance remains pending.
- PR #14 B05 head `04cf544ca240e32a87baef525f266f3d038ff668`: no exact-head CI evidence.
- Integration branch `integration/b04-b05-reconciled`; current head `fd8c14630254e27d42e257be342a5324bd40522c`; Draft PR #15 targets B04. Two-parent integration commit `73817ce41e4ae5b034922f190052a95ac45886d9` preserves both original histories.
- B04/B05 overlap manually reconciled in ClockDream, RecentAppsActivity, item_recent_close_all.xml, and view_nightstand.xml. B04-only adapter/helper/tests retained.

### Completed in this checkpoint
- B04 source review and exact-head CI verification.
- Selective B04+B05 integration branch and Draft PR #15; no source PR was merged.
- Additional high-traffic localization fixes in Home, Auto-Update, Adhan popup, media playback, wallpaper shuffle, Child Mode/effect feedback, hide/lock dialog, and Weather UV/precipitation/visibility.
- Static resource audit: 347 keys in default/ID/EN; identical key sets; no duplicates or placeholder mismatches; no missing R.string references in the three audited Java files.

### Remaining / do not close
- Complete ModernSettingsDialog hardcoded user-facing text sweep, including profile, wallpaper, StandBy, device/system, OTA, and launcher guidance.
- Run a complete Java/XML resource-reference scan and validate XML/resource formatting.
- Exact-head integrated CI is still absent. The connected GitHub actions tools in this session expose run/job inspection and reruns, but no workflow-dispatch action; do not report CI PASS without a real run.
- Review API 30 locale wrapping and Android 13+ LocaleManager behavior, including Activity/DreamService recreation, dialog state, focus, and persisted Settings tab.
- Physical TV acceptance remains F02/F06. No APK artifact, installation, TV settings change, or merge has occurred.

### Next resume actions
1. Continue the remaining ModernSettingsDialog and app-wide hardcoded UI text migration on `integration/b04-b05-reconciled`.
2. Run full locale key/placeholder/reference audit after the last code edit.
3. Obtain exact-head CI for current PR #15 and inspect both jobs and logs; resolve all failures before readiness.
4. Review locale lifecycle/focus behavior on Android 11/API 30 and Android 13+; defer physical acceptance to the approved F stage.
5. Keep PR #13, #14, and #15 unmerged and Draft until all source/CI/regression gates pass.


### QC follow-up — 1 November 2026

- Latest integration head: `fd8c14630254e27d42e257be342a5324bd40522c` (Draft PR #15).
- Additional parental PIN fallback prompts are now localized. Static resource audit now reports 347 keys in each of default/ID/EN, with no duplicates, key-set differences, placeholder mismatches, or missing R.string references in the audited Java files.
- Exact-head Actions run is still absent. Do not infer build/test success from static checks.
- B05 remains IN PROGRESS: broader ModernSettingsDialog copy sweep, app-wide final scan, exact-head CI, API 30/API 33 lifecycle checks, and physical acceptance remain open.
