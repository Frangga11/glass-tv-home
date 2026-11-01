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
