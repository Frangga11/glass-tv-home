## Historical audit register checkpoint — B04/B05, 1 November 2026

**Integration head:** `89d769689eb566671725c52982e0b73c4f1e8c31` | **PR #15:** open, Draft.

| Audit | Result | Evidence / boundary |
|---|---|---|
| Resource key count | PASS | 560 keys in default, ID, EN |
| Duplicate resource keys | PASS | Zero duplicates; unit test now asserts duplicate rejection |
| Locale key parity | PASS | Zero missing/extra keys across locales |
| Format placeholder parity | PASS | Zero mismatches |
| Java resource references | PASS | 534 refs across 21 changed Java files; zero missing |
| Layout resource references | PASS | 71 refs across 17 layouts; zero missing |
| App-wide copy sweep | PASS with noted content exceptions | 68 Java files and 17 layouts scanned; translated ID/EN helper branches and data metadata retained; weather preview values/branding remain static |
| B04 exact-head CI #242 | PASS | Source branch only; not proof of integrated head |
| Integrated exact-head CI | PENDING | No workflow run or combined status for `89d769689eb566671725c52982e0b73c4f1e8c31` |
| API 30/API 33+ lifecycle & focus | PENDING | Requires Android runtime and TV/remote verification |
| QR upload phone/network verification | PENDING | Requires real phone on same Wi-Fi and TV server session |
| Physical F02/F06 acceptance | PENDING | Target TV not tested |

No PR merged, no APK published/installed, no device settings changed. Release status remains NOT FINAL.


## Final source checkpoint — 1 November 2026

- Integration head: `89d769689eb566671725c52982e0b73c4f1e8c31`; PR #15 is OPEN, DRAFT, and GitHub reports `mergeable_state=clean`. PR #13 and #14 are also OPEN/DRAFT. No merge has occurred.
- Localization static QC: 560 keys in default/ID/EN; zero duplicate keys, locale key differences, or placeholder mismatches. Duplicate-key rejection is now part of `LocalizationResourceParityTest`.
- App-wide scan: 68 Java files and 17 layout XML files; 21 changed Java files with 534 `R.string` refs and 17 layouts with 71 `@string` refs; zero unresolved references.
- B04 exact-head CI #242 passed. There is still no Actions run or combined status for the exact integration head. Do not treat `mergeable_state=clean` as a build/test result.
- Remaining gates are external: integrated CI; API 30/API 33+ runtime lifecycle/focus checks; phone-on-same-Wi-Fi QR upload verification; physical F02/F06 TV acceptance. No APK was published/installed and no TV settings were changed.


## CI trigger correction — 1 November 2026

| Audit | Result | Evidence / boundary |
|---|---|---|
| Pull-request CI activity types | FIXED | Added explicit `opened`, `synchronize`, `reopened`, and `ready_for_review`; default pull_request activity types omitted ready_for_review. |
| Integration branch push CI | CONFIGURED | Added `integration/**` to the workflow push branches. |
| Base/integration ancestry | PASS | Updated B04 base commit included as a second parent in integration history; PR #15 reports mergeable=true. |
| Exact integration CI | PENDING | No check runs or combined statuses observed for head `5e7481c4902c85352b1ab95b7f1ef41acdf9ed78`; no integrated CI pass claimed. |

Current PR #15 is OPEN and Ready for review, not merged. Release remains NOT FINAL until CI, runtime, QR network, and physical TV gates pass.


## Test scheduling decision — 1 November 2026

| Gate | Timing | Current status |
|---|---|---|
| B04/B05 exact-head GitHub Actions CI | Before merging B04/B05 and before B06/B07 | PENDING; no run exists for current head |
| Android API 30/33+ runtime/lifecycle/focus | Final overall system test after B06/B07 and remaining planned stages | DEFERRED by user; not waived |
| Phone QR upload over same Wi-Fi | Final overall system test | DEFERRED by user; not waived |
| Physical TV F02/F06 | Final overall system test | DEFERRED by user; not waived |

Workflow configuration now explicitly supports `ready_for_review` and `integration/**`. Reopening PR #15 did not start a run; manual workflow dispatch is required. Keep APK publication disabled. B04 source CI #242 is not an integrated-head CI result.

## B04/B05 CI and merge audit — 1 November 2026

| Check | Result | Evidence |
|---|---|---|
| Manual CI trigger on default branch | PASS | `workflow_dispatch` added to `main`, commit `6eaf787c56226606b1d2e650c59540c553756c62`; user ran workflow with APK publication disabled. |
| Exact B04+B05 integration CI | PASS | Run #254 `41277076284` on `5932cf60e19640bc5fc648f43105f86c8752813c`; both CI jobs SUCCESS. Matching push run `41277072693` SUCCESS. |
| Resource compiler errors | FIXED | Apostrophe escaping corrected in default/ID/EN Android string resources. |
| Java compile error in update dialog | FIXED | `ProgressDialog.setMessage` now receives `activity.getString(R.string.download_update_message)`. |
| Localization test compile error | FIXED | Expected placeholder type corrected to `List<String>`. |
| PR #15 B04+B05 integration | MERGED | Merge commit `ec5ff4fce06fe0d2daf0e3fc49fe84fef9ad9838`. |
| PR #13 B04 parent into B03 | MERGED | Exact-head CI run #256 `41277353285` SUCCESS; merge commit `d7b76c4a22c9eadffb8171a616283f3ad647d488`. |
| PR #14 B05 source PR | CLOSED / MERGED | GitHub reports merge commit `73817ce41e4ae5b034922f190052a95ac45886d9`; final branch head after chain is `d7b76c4a22c9eadffb8171a616283f3ad647d488`. |
| APK publication | NOT PERFORMED | `publish_device_apk=false`; artifact publication step skipped. |

Final B04/B05 integration chain is complete and CI-gated. Runtime/device/QR/physical TV tests remain DEFERRED BY USER until final overall B-series acceptance; they are not waived.

## B06 baseline audit — 1 November 2026

- Historical baseline: `feature/b06-notification-feedback` was created from B03 continuation head `d7b76c4a22c9eadffb8171a616283f3ad647d488`; at that point no B06 source changes had been made. See the B06 audit and closure section below for the current state.
- Scanned the complete launcher Java source directory (63 Java files) for Toast, dialogs, progress/loading, delayed callbacks, and network/socket timeouts.
- Transient feedback is scattered across `MainActivity`, `ModernSettingsDialog`, `WeatherActivity`, `RecentAppsActivity`, `HomeSettingsActivity`, `LocationBroadcastReceiver`, `ChildModeManager`, `UserSwitchDialog`, `AppUpdateManager`, and `AppAdapter`. No shared in-app feedback abstraction or Snackbar usage was found in the scanned Java sources.
- OTA download uses `ProgressDialog` in `AppUpdateManager`; it has no cancel action and reports download failure via Toast. This is a candidate for a consistent, lifecycle-safe loading/error pattern.
- QR upload pages use independent JavaScript status strings in `AvatarUploadServer` and `WallpaperUploadServer`; their success/error/connection-lost states need parity review with native TV feedback.
- Existing transport timeouts are intentionally varied by operation (e.g. OTA check/download, weather fetches, local upload sockets, cache-cleaner socket stages, and power-action timeout). Do not globally normalize transport timeout values: classify them by operation and only standardize user-visible timeout messaging/cleanup.
- B06 implementation must preserve Indonesian/English locale context, TV D-pad behavior, focus ownership, and existing feature semantics. Audit asynchronous callback ownership and Activity lifecycle before replacing any feedback surface.


## Merge and bug-fix quality policy — 1 November 2026

**Policy:** GTVH-MQP-001 v1.0 — APPROVED / ACTIVE. Canonical details: `MERGE_QUALITY_POLICY.md`.

| Control | Status | Evidence / rule |
|---|---|---|
| Rolling integration branch | ESTABLISHED | `integration/b-series` created from `d7b76c4a22c9eadffb8171a616283f3ad647d488`. |
| B06 branch alignment | PASS | `feature/b06-notification-feedback` was created from the same base SHA; its PR must target `integration/b-series`. |
| Stage merge gate | ACTIVE | Impact/source audit, regression coverage, static/resource QC, required tests, exact-head CI, PR/diff integrity, and evidence record all required. Missing/stale evidence is NOT PASS. |
| Final main merge | ACTIVE | Only after full integration/regression, final acceptance, release review, and explicit user approval. |
| Bug-fix audit method | ACTIVE | Reproduce, fault boundary, impact map, scoped dependency audit, root-cause fix, regression test, exact-head verification, close-loop evidence/rollback. |
| Audit expansion | ACTIVE | Expand for global/shared foundations, cross-module contracts, common infrastructure, build/resources/permissions, or multi-module regressions; otherwise keep audit scoped to affected dependencies. |
| Device/runtime acceptance | DEFERRED | Final overall acceptance only; not waived and must not be recorded as PASS before performed. |

At the time of this policy checkpoint, B06 source implementation had not started. See the B06 audit and closure section below for the current verified state; merge timing remains deferred under D-013.


## Deferred grouped merge decision — 1 November 2026

**Decision:** D-013 / policy GTVH-MQP-001 v1.1 — APPROVED / ACTIVE.

| Control | Status | Rule |
|---|---|---|
| Stage merges from B06 onward | DEFERRED BY DECISION | No merge to integration while A–G audits/fixes remain active. |
| Verification during development | REQUIRED | Continue scoped audits, regression tests, static QC, builds, and exact-head CI; only merge is deferred. |
| Feature branch strategy | REQUIRED | Keep work isolated, traceable, and dependency-ordered; retain Draft PRs as review records. |
| `integration/b-series` | RESERVED | Final grouped integration/staging destination; do not use as a rolling merge target. |
| Grouped integration | PENDING | After A–G source work is complete: freeze heads, audit combined diff, merge coherent dependency groups, run exact-head CI and cross-group regression. |
| Final `main` merge | PENDING | Only after final acceptance and explicit user approval. |
| Final APK | PENDING | Build from exact approved `main` SHA; record version, artifact, workflow run, and SHA-256. A separately labelled test candidate may be required for physical acceptance. |

This timing decision supersedes the rolling stage-merge schedule previously recorded under D-012. It does not relax any quality gate or waive final device/runtime acceptance.


## B06 audit and closure — 1 November 2026

## B06 completion checkpoint — 1 November 2026

- B06 source implementation and static quality checks are complete on `feature/b06-notification-feedback`; exact verified head: `bc86a250d73a5cbb83250217d42c3c3993cf29d7`.
- Draft PR #16 targets `integration/b-series`, remains OPEN/DRAFT/UNMERGED under D-013. No merge was performed.
- Migrated 108 Toast call sites across 10 app-layer Java files to the shared lifecycle-safe `TvFeedback` delivery path.
- OTA feedback/loading now handles HTTP failures, unknown/known download length, incomplete APK cleanup, pre-install file closure, and user cancellation.
- Avatar/Wallpaper/Standby QR pages now have progress and explicit timeout/connection feedback. Browser timeouts are 240s (Avatar) and 600s (Wallpaper/Standby); socket idle budgets remain distinct at 90s (Avatar) and 30s (Wallpaper/Standby); Avatar QR session remains 5 minutes.
- Wallpaper category synchronization now reports success/failure.
- Resource parity: 566 keys in default/ID/EN; zero duplicates, key differences, or placeholder mismatches.
- Exact-head GitHub Actions CI run #295 (`41280874732`) PASS: launcher unit tests/release build and lists/tools/docs jobs succeeded. APK publication was skipped.
- No APK was published/installed and no device/TV settings were changed.
- Final API 30/33+ lifecycle/focus, real-phone QR over same Wi-Fi, and physical TV acceptance remain deferred to final F-stage overall system test; not waived.
- Detailed evidence: [B06_FEEDBACK_AUDIT.md](B06_FEEDBACK_AUDIT.md). B07 is next; grouped merge remains deferred until A–G source work is complete.


| Gate | Result | Evidence |
|---|---|---|
| Shared feedback abstraction | PASS | 108 Toast call sites migrated across 10 app-layer Java files to TvFeedback. |
| OTA loading and failure handling | PASS | HTTP status, length validation, resource closure, partial cleanup, lifecycle guards, and cancel action. |
| QR upload feedback | PASS | Avatar/Wallpaper/Standby progress, timeout/connection messages, localized response details. |
| Timeout policy | PASS | Browser and socket budgets remain operation-specific and are covered by NetworkFeedbackPolicyTest. |
| Localization parity | PASS | 566 keys in default/ID/EN; zero duplicates, key differences, or placeholder mismatches. |
| Exact-head CI | PASS | Run #295 / 41280874732 on bc86a250d73a5cbb83250217d42c3c3993cf29d7; both jobs SUCCESS. |
| Merge | DEFERRED BY D-013 | PR #16 OPEN/DRAFT/UNMERGED; grouped merge after A–G work. |
| Physical/runtime/phone QR | DEFERRED, NOT WAIVED | Final F-stage overall system acceptance. |


## B07 menu, submenu & focus restoration — 1 November 2026

**Outcome:** SOURCE & CI VERIFIED / PASS. Runtime and physical TV acceptance remains deferred, not waived.

| Gate | Result | Evidence |
|---|---|---|
| Source branch | SOURCE COMPLETE | `feature/b07-menu-focus-restoration`, stacked on B06 exact head `bc86a250d73a5cbb83250217d42c3c3993cf29d7`. |
| Draft PR | OPEN / DRAFT / UNMERGED | PR #17; base `feature/b06-notification-feedback`; grouped merge deferred by D-013. |
| Root cause | CONFIRMED | Settings tab persisted, but show() always focused selected sidebar; right-pane/sidebar exact cursor and scroll state were not persisted. |
| B07 source implementation | PASS | Persist active focus area, validated focus path/signature, active tab, left scroll, and per-tab right scroll. |
| Stale-path protection | PASS | Require visible/enabled/focusable target and matching class/ID/label signature; fallback to selected sidebar. |
| JVM regression tests | PASS | Four `SettingsFocusPathTest` cases ran under `testReleaseUnitTest`. |
| Exact-head CI | PASS | Run #298 / `41281838510` on `d4eabc48e020ab6cbef54e0f8884cf125eff97ed`; both jobs SUCCESS. |
| Android release build | PASS | `testReleaseUnitTest` + `assembleRelease` SUCCESS; APK size 1509 KB (<1.5 MiB). |
| APK publication | SKIPPED | No APK published or installed. |
| Runtime/API 30/33+ and physical TV | DEFERRED, NOT WAIVED | Final B-series/F-stage acceptance matrix. |
| Grouped merge | DEFERRED BY D-013 | Do not merge PR #17 before A–G source work and grouped integration. |

Detailed audit and acceptance matrix: [B07_MENU_FOCUS_AUDIT.md](B07_MENU_FOCUS_AUDIT.md).


## B-series dependency and CI synchronization — 1 November 2026

| Stage | Source / PR state | Exact-head CI evidence | Integration boundary |
|---|---|---|---|
| B01 Global navigation | PR #10 OPEN / DRAFT / UNMERGED | Runs #230 and #225 SUCCESS on `bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5`. | Retain source history; grouped merge under D-013. |
| B02 Remote focus indicators | PR #11 OPEN / DRAFT / UNMERGED | Run #234 SUCCESS on `01cbef2c2fc90498295971b22c369a72cdfa734a`. | Depends on B01. |
| B03 Dialog opacity/layering | PR #12 OPEN / DRAFT / UNMERGED | Run #257 SUCCESS on `d7b76c4a22c9eadffb8171a616283f3ad647d488`. | Depends on B02; B04/B05 work is included in the continuation head through the reconciled merge chain. |
| B04 Responsive layout | PR #13 MERGED | Integrated run #254 and follow-up exact-head run #256 SUCCESS. | Merged into B03 continuation chain. |
| B05 Localization | PR #14 MERGED; PR #15 reconciled integration MERGED | Integrated run #254 and follow-up exact-head run #256 SUCCESS. | 566 aligned keys on B06/B07 continuation; final locale/runtime acceptance deferred. |
| B06 Notification feedback | PR #16 OPEN / DRAFT / UNMERGED | Run #295 SUCCESS on `bc86a250d73a5cbb83250217d42c3c3993cf29d7`. | B07 is stacked directly on this exact head. |
| B07 Menu/submenu/focus restoration | PR #17 OPEN / DRAFT / UNMERGED | Run #298 SUCCESS on `d4eabc48e020ab6cbef54e0f8884cf125eff97ed`. | Source/CI complete; cross-B regression and physical acceptance remain in final grouped test. |

**B-series decision:** B01–B07 source/CI work is verified sufficiently to continue to Project C. This does not authorize early merges. Under D-013, preserve Draft PRs and exact heads, then perform combined-diff audit, dependency-group integration, cross-B regression, and final API/device acceptance after A–G source work is complete. No final APK was published or installed.


## APK size investigation — 1 November 2026

- CI run #298 reported 1509 KiB; the current configured gate is 1.5 MiB = 1536 KiB. The APK passed and did not exceed that gate, with about 27 KiB headroom.
- The same reported size is approximately 1.545 MB decimal, so it would exceed a strict 1.5 MB decimal target by about 45 KB. Keep MB and MiB distinct.
- Release build already has R8 minification and resource shrinking enabled.
- Largest source candidates include the intentional 230,883-byte default wallpaper, font resources (Inter and Outfit are byte-identical by Git blob SHA), QR artwork, avatars, and audio. Source sizes do not prove final APK contribution; the resource shrinker may remove unreferenced resources.
- A read-only APK ZIP breakdown step was added to the B07 PR workflow. Its output is pending the next CI execution; no asset/function was removed or modified.
- Detailed record: [APK_SIZE_AUDIT.md](APK_SIZE_AUDIT.md).


## C03 — Phone-to-TV communication and active LAN resolution, 1 November 2026

| Gate | Result | Evidence |
|---|---|---|
| Active network selection | PASS | Shared `LocalNetworkAddress` resolves Android active default Wi-Fi/Ethernet IPv4; rejects VPN-default, cellular-only, loopback, link-local, unspecified, multicast, IPv6, and unsupported prefixes. |
| Avatar QR resolver consistency | PASS | Preflight and server bind use the same resolver; URL uses the bound IPv4 and actual listening port. |
| Wallpaper QR resolver consistency | PASS | Preflight and server bind use the same resolver; URL uses bound address, actual port, and session-specific route. |
| StandBy QR resolver consistency | PASS | Same resolver and independent StandBy session route; wallpaper store remains separate. |
| Same-Wi-Fi user instructions | PASS | Existing ID/EN/default resource strings explicitly instruct phone and TV to use the same Wi-Fi network. |
| JVM regression tests | PASS | `LocalNetworkAddressTest` covers accepted LAN IPv4 and rejected address/prefix classes. |
| Exact-head CI | PASS | Run #338 / `41287563951` on `d266287df287167e5a8e73fd675b51759b580ed6`; both jobs SUCCESS. |
| Release build and APK size | PASS | `testReleaseUnitTest` + `assembleRelease` succeeded; APK 1,546,045 bytes (<1.5 MiB cap). |
| Real phone/TV same-network test | DEFERRED, NOT WAIVED | Final F-stage acceptance: scan all three QR flows, verify upload/store isolation, alternate network reachability, Wi-Fi reconnect, Ethernet/Wi-Fi switch, VPN, and QR revocation. |
| Merge / APK publication | DEFERRED / NOT PERFORMED | PR #20 OPEN/DRAFT/UNMERGED under D-013; no APK published or installed. |

C03 finding C03-F01 (interface-name-based address selection) was remediated by active-default-network resolution. C03-F02 (separate preflight and server address discovery) now uses the same resolver in both paths. C03-F03 (unisolated address policy) is covered by the new helper and JVM tests.


## C04 — HTTP endpoint, routing, method, and response contract, 1 November 2026

| Gate | Result | Evidence |
|---|---|---|
| Request-line parsing | PASS | Shared Android-free parser requires exactly method/target/version, uppercase method, origin-form path, HTTP/1.0 or HTTP/1.1. |
| Header completion | PASS | Avatar and Wallpaper/StandBy reject EOF before CRLFCRLF with 400; oversized headers return 431. |
| Avatar routes | PASS | Exact page/upload token paths; query string does not alter path; wrong method returns 405 + Allow; unknown route 404. |
| Wallpaper routes | PASS | Exact known endpoint map; per-endpoint GET/POST/OPTIONS; unknown session endpoint 404; wrong method 405 + Allow. |
| StandBy routes | PASS | Exact page GET and upload POST; wrong method 405; unknown path 404. |
| Query parameters | PASS | Missing, malformed, and duplicate file parameters rejected; query cannot alter route identity. |
| Gallery API status | PASS | Select/delete/slideshow/clear report validation, missing file, unavailable callback, and operation failures rather than unconditional 200. |
| Upload length status | PASS | Invalid/missing Content-Length 400; numeric size over limit 413; unsupported chunked transfer rejected. |
| Browser action handling | PASS | Gallery POST actions check response.ok, refresh on success, and show localized error feedback. |
| Regression tests | PASS | UploadHttpRequestTest, AvatarUploadRouteTest, expanded WallpaperUploadSessionRouteTest, and inherited C01/C03 tests pass in CI. |
| Exact-head CI | PASS | Run #342 / `41288521501` on `f09ee1b30b80e45e793c1678935eff8867aedae4`; both jobs SUCCESS; launcher/installer testReleaseUnitTest and assembleRelease succeeded. |
| APK size | PASS | 1,547,845 bytes; 15,019 bytes below 1,572,864-byte cap. Publication skipped. |
| Physical phone/TV browser acceptance | DEFERRED, NOT WAIVED | Final F-stage QR browser and upload acceptance. |
| Merge / APK publication | DEFERRED / NOT PERFORMED | PR #21 OPEN/DRAFT/UNMERGED under D-013; no APK published or installed. |


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


## D01 — Settings/configuration storage ownership, 1 November 2026

| Gate | Result | Evidence / boundary |
|---|---|---|
| Storage ownership inventory | PASS | SharedPreferences, resource defaults, Config overlay, app-private files, external caches, profile/device scope, and consumers mapped in `D01_SETTINGS_STORAGE_OWNERSHIP_AUDIT.md`. |
| Cross-project consistency review | FINDING RECORDED | D01-F11 traces StandBy collection/mode commit handling through C07 `WallpaperUploadServer` to its HTTP 200 success response; D05/D07 repair and regression coverage required. |
| Android backup semantics | PASS — corrected | D01-F10 distinguishes Android 11/API 30 Auto Backup behavior from Android 12+ OEM D2D-transfer exceptions; no unsupported recovery guarantee claimed. |
| Data/code mutation | NONE | Audit-only; no preference key/store, migration, source behavior, APK, or device setting changed. |
| Exact-head CI #373 | PASS — prior head | Run #373 / 41293666682 passed both jobs on `9a2f2aa29812acd3c24a895c1c4feed5b4cf94eb`. |
| Final exact-head CI | PENDING | Current D01 head `cd710681099faece14905faa244f57245bae84d1`; run required after final audit-doc correction. |
| PR / merge | OPEN / DRAFT / UNMERGED | PR #25, branch `feature/d01-config-storage-audit`, base C07 exact head `1cc6d7033daf6f9371805f667467b8860edcfb8f`; D-013 merge deferral applies. |
| Runtime/process-death/reboot | DEFERRED, NOT WAIVED | Final F-stage acceptance; D06 will define and verify persistence/recovery behavior. |



## D01 final exact-head verification — 1 November 2026

- Final D01 head `cd710681099faece14905faa244f57245bae84d1`: CI #375 / `41304411195` PASS and CI #376 / `41304430928` PASS; both jobs SUCCESS in each run.
- D01 source-audit and exact-head CI gates: PASS. No source code/data migration performed; no APK published or installed.
- PR #25 remains OPEN/DRAFT/UNMERGED on `feature/d01-config-storage-audit`, based on C07 exact head `1cc6d7033daf6f9371805f667467b8860edcfb8f`, under D-013.
- D01-F11 remains an open downstream repair item owned by D05/D07; runtime/process-death/reboot acceptance remains deferred to F-stage, not waived.


## D02 settings-to-UI propagation — 1 November 2026

| Gate | Result | Evidence / boundary |
|---|---|---|
| Settings propagation audit | SOURCE AUDIT PASS | Home widgets/effects, prayer panel/Ayah, wallpaper, profile/Child Mode, location/weather, language and cross-Activity return paths traced in `D02_SETTINGS_UI_PROPAGATION_AUDIT.md`. |
| D02-F01 StandBy mode failure handling | FIX IMPLEMENTED | MainActivity now checks StandbyPhotoStore commit result, does not claim success or refresh the live screensaver on failure, and shows localized error; Settings pane rerenders from persisted state. |
| Localization parity | IMPLEMENTED / CI PENDING | New `standby_mode_save_failed` key added to default, Indonesian and English resources. |
| Exact-head CI | IN PROGRESS | Runs #380 / 41305432110 and #381 / 41305453205 on `3bb4ae592904a49989fd76d7dabd92ed1cebd94b`; both Lists/tools/docs jobs have succeeded; Launcher build/unit-test jobs are still running. |
| PR / merge | OPEN / DRAFT / UNMERGED | PR #26, `feature/d02-settings-ui-propagation` stacked on D01 exact head; D-013 merge deferral applies. |
| D01-F11 upload persistence contract | OPEN — D05/D07 | Separate upload atomicity/failure propagation issue remains open; not closed by D02. |
| Physical Android TV acceptance | DEFERRED, NOT WAIVED | Final F-stage acceptance. No APK published or installed. |


## D02 final exact-head verification — 1 November 2026

| Gate | Result | Evidence / boundary |
|---|---|---|
| Settings-to-UI propagation audit | PASS | Home widgets/effects, prayer panel/Ayah, wallpaper, profile/Child Mode, location/weather, language and cross-Activity return paths traced in D02 report. |
| D02-F01 StandBy mode failure handling | FIXED | Setter checks commit result, preserves live state on failure, and shows localized failure feedback; existing Settings rerender restores persisted value. |
| Localization parity | PASS | Default, Indonesian and English strings aligned. |
| Final exact-head CI | PASS | #382 / 41305782187 on `2dbfcc181651184914422ad0697a3c5c27d15614`; both jobs SUCCESS; unit tests, release builds, APK diagnostics and size gate passed. |
| APK size | PASS, LOW HEADROOM | 1,550,617 bytes; 12,247 bytes below 1,572,864-byte cap. |
| PR / merge | OPEN / DRAFT / UNMERGED | PR #26; D-013 merge deferral applies. |
| Physical Android TV acceptance | DEFERRED, NOT WAIVED | Final F-stage acceptance; no APK published or installed. |
| D01-F11 | OPEN — D05/D07 | Distinct upload collection/mode atomicity issue remains downstream. |


## D03 locked/hidden app refresh — 1 November 2026

| Gate | Result | Evidence / boundary |
|---|---|---|
| Root cause | CONFIRMED | Profile Settings count labels read ChildModeManager locked/hidden collections; picker writes persisted immediately but the parent pane was not rerendered on dismissal. Tile reload was previously tied to Done only. |
| Picker dismissal refresh | FIXED | Narrow completion callback refreshes the owning Settings right pane; dialog dismissal reloads home tiles for both Done and Back. |
| PIN cancellation | PRESERVED | Cancellation before the app picker opens does not execute the picker dismissal refresh path. |
| Scope control | PASS | No ChildModeManager storage/schema/migration, PIN policy, package discovery, global listener/event bus, or unrelated C07/D01/D02 changes. |
| Exact-head CI | PASS | Run #385 / 41307515649 on `c119198e5c5ecc0d3cfdc283049ea3299d0067ca`; both jobs SUCCESS; unit tests and release build succeeded. |
| APK size | PASS | 1,550,761 bytes; 22,103 bytes below 1,572,864-byte cap. Manual publication skipped. |
| PR / merge | OPEN / DRAFT / UNMERGED | PR #27; D-013 grouped-integration rule applies. |
| Physical Android TV acceptance | DEFERRED, NOT WAIVED | F-stage must verify Done/Back, separate locked/hidden counts, home tile refresh, PIN cancellation, and D-pad focus. No device test or APK installation claimed. |

D03 source-audit and exact-head CI gates are closed. D01-F11 remains an independent downstream repair assigned to D05/D07.

 
## D04 profile/avatar state synchronization — 1 November 2026

| Gate | Result | Evidence / boundary |
|---|---|---|
| Profile/avatar ownership and refresh audit | PASS | Home top bar, Settings sidebar/detail/list, profile switcher, profile switching/rename/avatar mutations traced in D04 report. |
| D04-F01 effective avatar indicator | FIXED | Built-in selection ring is shown only when no custom photo overrides the stored built-in avatar; default restoration reselects the stored built-in fallback. |
| Regression tests | PASS | Added JVM tests for custom-photo override and fallback restoration. |
| Scope control | PASS | No profile schema/preferences, migration, upload protocol, Child Mode/PIN, global observer/event bus, or unrelated C07/D01–D03 changes. |
| Final exact-head CI | PASS | #390 / 41309327872 on `ad69d229a6b3cd6ca1810a595597dc88481c3ebd`; both jobs SUCCESS; app/installer unit tests and Release build passed. |
| APK size | PASS | 1,550,805 bytes; 22,059 bytes below 1,572,864-byte cap. Manual publication skipped. |
| PR / merge | OPEN / DRAFT / UNMERGED | PR #28; D-013 grouped-integration rule applies. |
| Physical Android TV acceptance | DEFERRED, NOT WAIVED | F-stage must verify profile switching, rename, built-in/custom/default avatar, Home/Settings/profile switcher consistency, and cross-profile isolation. No device test or APK installation claimed. |

D04 source-audit and exact-head CI gates are closed. D01-F11 remains a separate downstream repair assigned to D05/D07.


## D05 StandBy photo persistence / D01-F11 — 1 November 2026

| Gate | Result | Evidence / boundary |
|---|---|---|
| Root-cause audit | PASS | `addUploadedFile()` ignored second photo-mode commit result after collection commit. |
| Atomic collection + mode persistence | FIXED | One SharedPreferences editor commit writes photo URI collection and `display_mode=true`; duplicate path also persists mode. |
| Truthful HTTP result | FIXED | 200 success, 400 validation/collection rejection, 500 persistence failure; success callback only after persisted success. |
| Failure/file consistency | PASS | File retained on reported commit failure to avoid dangling in-memory URI; existing orphan cleanup handles non-durable file after restart. |
| Regression tests | PASS | One-commit state, commit-failure result, and HTTP outcome mapping tests. |
| C07 preservation | PASS | No unrelated upload/session changes; C07 validation/framing/limits/expiry/cancellation retained. |
| Exact-head CI | PASS | #395 / 41310507890 on implementation head 835986c0e28c50311d4474f1060c73dd56104bf8; both jobs SUCCESS; app/installer unit tests and release build passed. |
| APK size | PASS | 1,551,005 bytes; 21,859 bytes below 1,572,864-byte cap. Manual publication skipped. |
| PR / merge | OPEN / DRAFT / UNMERGED | PR #29; D-013 grouped-integration rule applies. |
| Runtime / device acceptance | DEFERRED, NOT WAIVED | F-stage must verify upload failure/retry, process death/reboot and Android TV behavior. No APK installation or physical test claimed. |

D05 source-audit and CI gates are closed. See `D05_STANDBY_PHOTO_PERSISTENCE_AUDIT.md` on the feature branch for detailed findings and scope.

## D06 process-death / reboot recovery — 1 November 2026

| Gate | Result | Evidence / boundary |
|---|---|---|
| App-owned persistence/recovery path audit | PASS | Settings, profile/avatar, Child Mode, StandBy photos, wallpaper, language, cached weather/prayer data, config overlay and scheduled alarms traced in D06 report. |
| D06-F01 reboot alarm recovery | FIXED | Non-exported BOOT_COMPLETED receiver restores selected-zone prayer alarm through existing Azan.schedule(); master/prayer toggles remain authoritative. |
| Regression tests | PASS | Four JVM tests cover boot dispatch, unrelated broadcast, missing zone and missing scheduler. |
| Scope preservation | PASS | No preference schema/key migration, backup policy, user data, broad refactor, or unrelated C07/D01–D05 changes. |
| Exact-final-head CI | PASS | #399 / 41312553920 on `78a12e9fc196a596fa98ea272edc735501047af1`; both jobs SUCCESS; app/installer unit tests and Release build passed. |
| APK size | PASS | 1,551,217 bytes; 21,647 bytes below 1,572,864-byte cap. Manual publication skipped. |
| PR / merge | OPEN / DRAFT / UNMERGED | PR #30; D-013 grouped-integration rule applies. |
| Runtime / physical TV acceptance | DEFERRED, NOT WAIVED | Process death, force-stop, full reboot and actual alarm restoration must be verified in F-stage. No APK installation or physical test claimed. |

D06 source-audit and exact-head CI gates are closed. Detailed report: `D06_PROCESS_DEATH_REBOOT_RECOVERY_AUDIT.md` on the feature branch.


## D07 concurrent profile/content-store state — 1 November 2026

| Gate | Result | Evidence / boundary |
|---|---|---|
| Child Mode/profile state audit | PASS | Found package-set read/modify/write lost-update risk and repeated active-profile resolution. Mutations/reads and profile cleanup now share the ChildModeManager monitor; operations use one captured profile ID. |
| Profile identifier collision | FIXED | New profile IDs use UUIDs rather than millisecond timestamps; existing IDs and stored profile JSON remain unchanged. |
| StandBy picker/QR content-store race | FIXED | Picker additions merge against the latest collection under the store lock; capacity is rechecked; collection+mode activation commits atomically. |
| StandBy clear consistency | FIXED | Collection+mode clear commits atomically; managed files are deleted only after successful commit; the committed URI snapshot is used to release external grants. |
| Stale weather/AQI response | FIXED | Request generation, selected coordinates and language are checked before cache writes; superseded responses are discarded and current location is retried. |
| Regression tests | PASS | JVM tests cover Child Mode profile/set behavior, StandBy merge/commit/clear and weather freshness policy. |
| Scope preservation | PASS | No preference schema migration, global state store/event bus, profile ownership rewrite, PIN policy change, or C07 upload/session protocol change. D01–D06 parent history preserved. |
| Final exact-head CI | PASS | #404 / 41313690556 on 964163c918e281b68af5cf7114ca56ead2797486; both jobs SUCCESS; app/installer testReleaseUnitTest and assembleRelease passed. |
| APK size | PASS | 1,552,197 bytes; 20,667 bytes below 1,572,864-byte cap. Manual publication skipped. |
| PR / merge | OPEN / DRAFT / UNMERGED | PR #31; D-013 grouped-integration rule applies. |
| Runtime / physical TV acceptance | DEFERRED, NOT WAIVED | F-stage must verify profile switching/isolation, Child Mode, concurrent picker/QR operations, location/language refresh, process death and reboot. No APK installation or physical test claimed. |

D07 source-audit and exact-head CI gates are closed. Detailed findings and repairs: D07_CONCURRENT_PROFILE_CONTENT_STATE_AUDIT.md on the D07 feature branch.



## E00 master inventory and integration-boundary reconciliation — 2 November 2026

| Gate | Result | Evidence / boundary |
|---|---|---|
| Remote branch inventory | PASS | 36 remote branch names returned; branch existence is not treated as merge/readiness evidence. |
| PR state refresh | PASS | Individually queried PR #16–#31: all OPEN/DRAFT/UNMERGED. PR #10–#12 also remain open/Draft/unmerged; B04/B05 reconciled merge history #13–#15 retained. |
| B/C/D source/CI versus acceptance status | RECONCILED | B01–B07, C01–C07 and D01–D07 source/CI gates recorded complete; integrated/runtime/physical acceptance remains mandatory in F, not waived. |
| D07 baseline | PASS | `964163c918e281b68af5cf7114ca56ead2797486`; PR #31; exact-final-head CI #404 / `41313690556` PASS. |
| Resource parity spot-check | PASS — STATIC ONLY | 569 unique keys in default/ID/EN; zero duplicate keys, locale key-set differences or printf-placeholder mismatches. Runtime screen/translation acceptance remains F06. |
| E00 documentation corrections | PREPARED | Roadmap, inventory, audit register, operations log, master project and handover updated on `docs/e00-inventory-reconciliation`; review/integration into `docs/project-master-inventory` pending. |
| Application source / APK / device mutation | NONE | E00 is documentation-only. No application source, APK, TV setting, user data or feature PR changed. |

**Integration disposition:** D-013 remains active. F01 is an isolated integration rehearsal/candidate assembly and combined-diff audit; it must not merge feature PRs into the reserved `integration/b-series` branch. F02–F07 perform candidate integrated acceptance. G01 returns failures to their owning projects and closes/re-verifies them. Only after all A–G source audits/fixes are complete and findings are closed or explicitly accepted may exact heads be frozen and merged into `integration/b-series` by coherent dependency groups, with exact-head CI and cross-group regression after each group. G02–G03 handle the traceable candidate/artifact and target-device verification. Merge to `main` only after final acceptance, release review and explicit user approval.

Detailed scope, protected contracts, E01–E08 findings and limitations: [E00_MASTER_INVENTORY_AND_DEPENDENCY_AUDIT.md](E00_MASTER_INVENTORY_AND_DEPENDENCY_AUDIT.md).


## E00 official closure — 2 November 2026

- E00 documentation PR #32 was merged into `docs/project-master-inventory` (merge commit `4ba80230cb9ddfbcc27657b7b0d07bed6905fc97`).
- Exact PR head `541dd9accb66eb3b071367a3e4967769d742c2cd`; CI workflow run #414 / `41359874778`: PASS. Both `Launcher build and unit tests` and `Lists, tools and docs` jobs completed successfully. No review comments or change requests were present.
- Canonical E00 report, roadmap, inventory, audit register, operations log, master project and handover were re-read from `docs/project-master-inventory` after merge.
- E00 scope is closed: documentation/inventory/dependency reconciliation only. No application source, feature PR, APK, user data, TV setting, installation or physical-device acceptance was changed or claimed.
- **Next work unit: E01 — Device Settings & Launcher audit.** Begin with exact dependency heads and the A06 read-only HOME resolver/preferred-activity discrepancy. Preserve D-013/D-014; do not merge feature PRs early.
