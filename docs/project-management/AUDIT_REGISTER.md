[object Object]

## Latest audit register — B04/B05, 1 November 2026

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
