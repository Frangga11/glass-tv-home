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
- A06 remains IN PROGRESS until read-only Bugjaeger/ADB output captures Mi TV Stick build fingerprint, installed package details/path, current HOME resolver, enabled/disabled packages, and relevant secure/global settings. Review before any device changes.
- No PR merged; no new APK installed; no device settings changed.
- B01/B02 implementation remains paused at user's request until A06 is closed.
- Next action: collect the read-only ADB baseline listed in the A06 checkpoint manifest; then complete rollback-readiness review.
