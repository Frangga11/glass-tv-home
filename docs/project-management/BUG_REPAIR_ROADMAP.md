# Glass TV Home — Master Bug Repair Roadmap

**Roadmap ID:** GTVH-BRR-001  
**Version:** 1.0  
**Approved:** 1 November 2026  
**Status:** OFFICIALLY APPROVED — A01–A06 complete; B01–B05 source/CI and integration merge chain complete; B06 source/CI complete; B07 next; device testing deferred to final F-stage acceptance  
**Target test device:** Mi TV Stick, Android TV 11 / API 30 (revalidate live before release)

## 1. Governance

This roadmap is the approved sequence for the post-installation user testing repair cycle. Work proceeds from global foundations to shared infrastructure and state management, then to specific modules, integration, and release.

- GitHub is the canonical technical record.
- ChatGPT memory is the continuity index and session handover aid; it is not a substitute for the repository.
- No task is marked complete without evidence. Distinguish source inspection, implementation, CI/build, and physical-device verification.
- Do not remove previously available features merely to simplify the UI without explicit user approval.
- Do not claim a task or session is synchronized until the GitHub write and resulting commit are verified.
- Re-ordering is allowed only when a dependency or newly discovered root cause justifies it; record the reason in the Decision Log and Operations Log.
- Preserve working behavior and maintain rollback points.
- User instruction, 1 November 2026: do not install or test any candidate APK on the TV while source audits and repairs are still underway. Complete the A–E audit/fix work and final integration/regression stage F first; only then create/publish one final TV-test APK after explicit readiness approval. CI may compile the release variant for source/build validation, but normal PR runs must not publish a TV-test artifact.

## 2. Status vocabulary

| Status | Meaning |
|---|---|
| DONE | Work completed and evidence recorded; required verification passed |
| IN PROGRESS | Work has started but is not complete |
| NOT STARTED | Work has not begun |
| BLOCKED | Cannot proceed until a dependency or user action is resolved |
| AWAITING APPROVAL | Proposal requires explicit user decision |

A code change or passing CI alone does not close a physical-device acceptance item. Physical-device testing is deferred until all source audits/fixes are complete and the final integrated candidate has passed the pre-device quality gate.

## 3. Approved sequence and task register

### Stage A — Preparation and audit

| ID | Task | Initial status | Completion evidence |
|---|---|---|---|
| A01 | Collect user test findings and screenshots | DONE | User's written test summary and supplied TV/phone screenshots, 1 Nov 2026 |
| A02 | Group findings by global foundations, shared systems, and feature modules | DONE | Approved discussion and this roadmap |
| A03 | Agree the repair sequence A–G | DONE | User explicitly approved the roadmap on 1 Nov 2026 |
| A04 | Audit repository, branches, source tree, and current working state | DONE | Remote repository identity, 13 branch heads, PR dependency chain, source tree/build/manifest/CI inventory recorded; local-clone limitation explicitly recorded in INVENTORY.md and AUDIT_REGISTER.md |
| A05 | Match the tested APK/version to its exact source branch and commit | DONE | Downloaded CI artifact #208, extracted `app-release.apk`, and verified byte-for-byte identity with the user-uploaded tested APK (1,427,681 bytes; SHA-256 `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`); CI run/artifact metadata maps to PR #9 exact head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc` |
| A06 | Establish baseline, backup, and rollback/checkpoint | DONE | Immutable checkpoint branch `checkpoint/a06-tested-apk-pr9-7e8392e1`, manifest and rollback SOP verified; full read-only Mi TV Stick snapshot captured 1 Nov 2026. HOME resolver/preferred-activity discrepancy and installed APK hash limitation recorded for E01; no device mutation performed |

### Stage B — Global foundations and user experience

| ID | Task | Initial status |
|---|---|---|
| B01 | Audit and correct global navigation and cursor-position retention | IN PROGRESS |
| B02 | Ensure visible, consistent D-pad/remote focus indicators | IN PROGRESS |
| B03 | Correct popup/dialog opacity, layering, focus, sizing, and dismissal behavior | IN PROGRESS |
| B04 | Normalize global UI layout, spacing, sizing, and TV responsiveness | IN PROGRESS |
| B05 | Audit and complete Indonesian/English localization, including dynamic and error strings | IN PROGRESS |
| B06 | Standardize notifications, success/error messages, loading, and timeout feedback | IN PROGRESS |
| B07 | Audit activity/dialog lifecycle and restore prior menu/subtab/focus after actions | NOT STARTED |

**Stage exit criteria:** remote navigation and focus are predictable; dialogs are visually isolated and readable; language selection is consistent; actions do not unexpectedly reset the user to the first tab.

### Stage C — Shared communication infrastructure

| ID | Task | Initial status |
|---|---|---|
| C01 | Audit QR generation, payload, token, and QR destination behavior | IN PROGRESS — source/CI pass; physical acceptance deferred |
| C02 | Audit TV local server lifecycle, LAN IP selection, and port handling | IN PROGRESS — source/CI pass; physical acceptance deferred |
| C03 | Verify phone-to-TV communication and same-network behavior | IN PROGRESS — source/CI pass; physical acceptance deferred |
| C04 | Audit HTTP endpoints, routing, methods, and response codes | IN PROGRESS — source/CI pass; physical acceptance deferred |
| C05 | Repair shared upload/receive-file pipeline where appropriate | NOT STARTED |
| C06 | Validate file types, dimensions, sizes, names, and storage paths | NOT STARTED |
| C07 | Handle disconnects, retries, expiry, cancellation, and timeouts safely | NOT STARTED |

**Stage exit criteria:** QR workflows reach the intended TV service, upload routes resolve correctly, invalid/expired requests fail safely, and independent content types remain isolated.

### Stage D — State management and persistence

| ID | Task | Initial status |
|---|---|---|
| D01 | Audit settings/configuration storage and ownership | NOT STARTED |
| D02 | Ensure settings changes propagate to all dependent UI immediately | NOT STARTED |
| D03 | Fix locked-app count/list refresh without leaving and re-entering Settings | NOT STARTED |
| D04 | Synchronize profile/avatar state across all surfaces | NOT STARTED |
| D05 | Synchronize photo collections and their metadata/counts | NOT STARTED |
| D06 | Verify persistence and recovery after process death and TV restart | NOT STARTED |
| D07 | Prevent stale state, concurrent writes, and cross-profile/content-store conflicts | NOT STARTED |

**Stage exit criteria:** a successful change is reflected immediately, persists where intended, and is not overwritten by stale state or another profile/module.

### Stage E — Specific feature modules

| ID | Module / task | Initial status |
|---|---|---|
| E01 | Device settings and launcher: default-home dialog, launcher status/help, accessibility, remote actions | NOT STARTED |
| E02 | Profile: avatar QR, built-in avatar selection, focus indicators, app-lock counts and policy UI | NOT STARTED |
| E03 | Appearance: wallpaper upload, QR flow, photo management, preview, delete and apply | NOT STARTED |
| E04 | Standby/screensaver: upload, preview, location, gallery visibility, selection and playback | NOT STARTED |
| E05 | Widgets/information: Information Center, Quick Actions placement/purpose, weather and AQI | NOT STARTED |
| E06 | Home Screen: restore/review missing subtabs and preserve required prior functionality | NOT STARTED |
| E07 | Animations/effects: inspect gaps, conflicts, memory/CPU cost, and remote-focus behavior | NOT STARTED |
| E08 | Wallpaper settings redesign: three subtabs — Change Wallpaper, Wallpaper Shuffle Interval, Next Wallpaper | NOT STARTED |

**E08 product requirement:** the three controls must operate as one coherent wallpaper system. Manual next must respect the selected collection and not corrupt shuffle/interval preferences. Final interaction details require implementation review and user acceptance.

### Stage F — Integration and full regression testing

| ID | Task | Initial status |
|---|---|---|
| F01 | Integrate all repaired modules and check shared dependencies | NOT STARTED |
| F02 | Test remote navigation, focus, back behavior, and retained cursor position | NOT STARTED |
| F03 | Test QR generation, phone connection, upload, expiry, and cancellation | NOT STARTED |
| F04 | Test avatar, wallpaper, and standby image flows and verify store separation | NOT STARTED |
| F05 | Test settings/profile/photo persistence after close, force-stop, and reboot | NOT STARTED |
| F06 | Test Indonesian and English across screens, dialogs, and dynamic errors | NOT STARTED |
| F07 | Test performance, crashes, freezes, memory use, and long-running behavior on Mi TV Stick | NOT STARTED |

### Stage G — Finalization and release

| ID | Task | Initial status |
|---|---|---|
| G01 | Resolve remaining release-blocking and regression findings | NOT STARTED |
| G02 | Produce release APK and record version, commit, artifact, and SHA-256 | NOT STARTED |
| G03 | Verify installation, upgrade, launcher behavior, and rollback on target TV | NOT STARTED |
| G04 | Update GitHub master, inventory, audit register, decision log, operations log, and handover | NOT STARTED |
| G05 | Update ChatGPT continuity memory/handover and confirm next resume point | NOT STARTED |

## 4. Progress snapshot — 1 November 2026 (after A06)

- Tasks in roadmap: 47
- DONE: 6 (A01, A02, A03, A04, A05, A06)
- IN PROGRESS: 5 (B01, B02, B03, B04, B05)
- NOT STARTED: 36
- BLOCKED: 0
- AWAITING APPROVAL: 0

This is a task-count snapshot, not a percentage of application readiness. A01–A06 are complete. A04 did not inspect a separate local clone; this limitation is recorded. B01–B03 source work remains isolated on dedicated Draft PRs under the previously approved sequencing exception D-009. Renewed source QC passed for all three modules; exact-head CI #230/#234/#239 passed and no TV-test artifact was published. B03 source/CI gate PASS on Draft PR #12: shared dialog/panel/QR surfaces made opaque; explicit D-pad focus selectors added to standard dialog buttons/lists; QR dialog bounded and scrollable; Now Playing/card prompt widths clamped. Exact-head CI #239 passed both jobs, with release APK size 1,398 KB (<1.5 MiB); manual TV-test artifact publication was skipped. B03 physical acceptance remains deferred to F02. B04 source + target-metric audit is complete: the user's read-only `wm size`/`wm density` outputs establish 1920×1080 px at 320 dpi (960×540 dp). Recent Apps two-card geometry is a near-fit carousel (420dp × 2 + 64dp side padding × 2 = 968dp, 8dp over viewport before inter-card spacing); Nightstand 110sp and Weather's scrollable 385dp Bento body do not establish a source-level clipping defect. No speculative B04 layout change is justified; physical visual/remote acceptance remains F02. B05 full inventory is complete and localization implementation is next. A05 is complete with a direct byte-for-byte match between the user-uploaded tested APK and the APK extracted from CI artifact #13048951728; CI run #208 binds that artifact to PR #9 exact source head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`. A06 is DONE: the exact source/APK checkpoint and rollback procedure are documented, and the user supplied a fresh read-only Mi TV Stick snapshot. The resolver returns the stock launcher while the package preferred-activity table lists Glass TV Home; this discrepancy is carried into E01. The installed package metadata matches version 4.0.0/code 40, but its APK bytes/hash were not extracted from the TV. B03 is IN PROGRESS on branch `feature/b03-dialog-opacity-layering`, Draft PR #12, based on B02 head `01cbef2c2fc90498295971b22c369a72cdfa734a`; source/CI gate passed on head `56a4e1995aa8b8600614e6b5c4dca69d01c530c3`, CI #239. B04 is IN PROGRESS: source review found responsive home/settings/weather layouts, with fixed Recent Apps card and Nightstand clock dimensions requiring target display metrics before any adjustment. Next read-only values: `wm size` and `wm density`.

## 5. Current execution point

A04, A05, and A06 are DONE. The user's historical PR #9 APK test remains accepted and will not be repeated. B01–B03 have passed renewed source-level QC and exact-head CI. Their Draft PRs remain isolated and unmerged: PR #10 / feature/b01-global-navigation-retention @ bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5 (CI #230 PASS); PR #11 / feature/b02-remote-focus-indicators @ 01cbef2c2fc90498295971b22c369a72cdfa734a (CI #234 PASS); PR #12 / feature/b03-dialog-opacity-layering @ 56a4e1995aa8b8600614e6b5c4dca69d01c530c3 (CI #239 PASS). Unit tests, release builds, APK size gates, and lists/tools/docs checks passed; no TV-test artifacts were published. B01–B03 remain IN PROGRESS only for final integrated/physical acceptance at F02.

B04 is IN PROGRESS. Source review finds the home grid, Settings, and most custom dialogs responsive/bounded. The Recent Apps card is fixed at 420×250dp with 64dp side padding; the Nightstand clock is fixed at 110sp. Do not resize speculatively. Obtain read-only target values using wm size and wm density without arguments, then decide whether adaptive dimensions are required.

B05 is IN PROGRESS. Initial localization audit found mixed Indonesian/English strings in default Android resources, only a date-format override in values-en-rUS, no dedicated values-id or generic values-en resource set, and many user-facing hardcoded strings in high-traffic Java classes. Continue a full string inventory, centralize translations, verify dynamic/error messages, and test language switching without changing the user's saved preferences unexpectedly.

No PR merge, candidate APK installation, device-setting change, or physical TV test is authorized before all A–E source audits/fixes and F integration/regression gates are complete and readiness is explicitly approved.
## 6. Session closeout and callback protocol

At the end of every work session:
1. Update task statuses only when supported by evidence.
2. Record changed files, branch, commit, tests, results, and unresolved risks.
3. Update the relevant audit and operational records.
4. Update HANDOVER.md with the exact resume point and next action.
5. Synchronize ChatGPT continuity memory with the same checkpoint.
6. Verify the GitHub commit and file contents before reporting synchronization complete.

Useful callback requests:
- "Tampilkan roadmap perbaikan Glass TV Home."
- "Lanjutkan dari titik terakhir roadmap Glass TV Home."
- "Tampilkan pekerjaan yang selesai, berjalan, dan belum dimulai."
- "Tampilkan hasil audit tahap [ID]."

## 7. Related project records

- [MASTER_PROJECT.md](MASTER_PROJECT.md)
- [INVENTORY.md](INVENTORY.md)
- [DECISION_LOG.md](DECISION_LOG.md)
- [AUDIT_REGISTER.md](AUDIT_REGISTER.md)
- [OPERATIONS_LOG.md](OPERATIONS_LOG.md)
- [HANDOVER.md](HANDOVER.md)


## 6. Latest execution checkpoint — 1 November 2026

- B04 source + target-metric audit PASS: user-supplied `1920×1080` at `320 dpi` gives `960×540 dp`; no measured source-level clipping defect justified a speculative layout change. F02 physical acceptance remains.
- B05 remains IN PROGRESS. Draft PR #14 / `feature/b05-id-en-localization` is stacked on B03 exact head `56a4e1995aa8b8600614e6b5c4dca69d01c530c3`; current source head `cce889f231aeff3a28805fd517853a3f0a91e79c`.
- Current B05 implementation: 158-key default/ID/EN resource parity; locale config ID/EN; API 26–32 locale context; Android 13+ app locale synchronization; five user-facing Activity wrappers plus ClockDream; Settings reopen using B01 tab/sidebar persistence; eight launcher layout migrations; partial programmatic profile/PIN/weather/update migration; normalization and resource-parity tests.
- B05 is NOT source/CI complete: Java hardcoded strings remain in MainActivity, ModernSettingsDialog, WeatherActivity dialogs and other dynamic/error/notification paths. No exact-head CI run was available in the latest Actions query.
- Keep PR #14 Draft/unmerged. No candidate APK publication/installation or TV setting change. Continue B05 string migration, then exact-head CI and source QC; physical language acceptance belongs to F06.


## 8. Latest execution checkpoint — 1 November 2026 (B04/B05 CI and merge completion)

- B04/B05 exact integration CI PASS on `5932cf60e19640bc5fc648f43105f86c8752813c`, run #254 (`41277076284`); both jobs passed. Follow-up PR #13 exact-head CI PASS on `ec5ff4fce06fe0d2daf0e3fc49fe84fef9ad9838`, run #256 (`41277353285`).
- CI findings fixed: Android string apostrophe escaping across default/ID/EN resources; `AppUpdateManager` localized progress message type; `LocalizationResourceParityTest` placeholder-list type.
- PR #15 merged to B04 branch (`ec5ff4fce06fe0d2daf0e3fc49fe84fef9ad9838`); PR #13 merged to B03 continuation (`d7b76c4a22c9eadffb8171a616283f3ad647d488`). GitHub also reports PR #14 merged (`73817ce41e4ae5b034922f190052a95ac45886d9`). Current B03 continuation head: `d7b76c4a22c9eadffb8171a616283f3ad647d488`.
- B04/B05 source + CI gate is complete. B01–B05 still require final integrated/physical acceptance in F; do not mark the entire Stage B exit criteria complete until B06/B07 and final acceptance are done.
- B06 (notification, success/error, loading and timeout feedback) and B07 (activity/dialog lifecycle and restore menu/subtab/focus) remain NOT STARTED. Start B06 first, then B07, from the current B03 continuation head; keep each work unit isolated and CI-gated.
- Physical/runtime acceptance remains deferred, not waived: API 30/33+ lifecycle/configuration, Activity/DreamService recreation and focus/tab retention, real-phone QR upload over same Wi-Fi, physical TV F02/F06. No APK was published/installed; `publish_device_apk=false`.


## 9. Latest execution checkpoint — B06 started

- B04/B05 integration CI and merge chain are complete; see the latest execution checkpoint in HANDOVER.md and AUDIT_REGISTER.md.
- B06 baseline audit started on `feature/b06-notification-feedback`, based on `d7b76c4a22c9eadffb8171a616283f3ad647d488`.
- Initial scan: 63 Java files; scattered Toast/AlertDialog feedback; OTA `ProgressDialog`; separate QR upload page statuses; no shared feedback abstraction/Snackbar found. Keep transport timeouts operation-specific.
- B06 implementation is IN PROGRESS. B07 remains NOT STARTED. Final physical/runtime acceptance remains deferred until Stage F.


## 10. Mandatory merge and bug-fix quality policy

All subsequent work is governed by [MERGE_QUALITY_POLICY.md](MERGE_QUALITY_POLICY.md), policy GTVH-MQP-001 v1.0, approved 1 November 2026. Stage PRs target the rolling `integration/b-series` branch and may merge only after scope/impact audit, relevant regression protection, static/resource QC, required build/tests, and exact-head CI all pass. `main` remains the stable release line and receives the completed integrated result only after full regression, final acceptance, release review, and explicit user approval. A green CI or GitHub mergeable flag alone is not sufficient.

For each new bug, reproduce and define it, locate its owning path and dependencies, create an impact map, audit the relevant surface, fix the root cause, add regression coverage, verify the exact diff/head, and record evidence/rollback. Expand to app-wide audit only when a shared foundation, cross-module contract, or evidence justifies it. Missing or stale evidence is NOT PASS. Deferred device/runtime checks remain pending, never waived.


## 11. Merge timing amendment — D-013, 1 November 2026

Starting with B06, all stage merges are deferred until the A–G audit and repair work is complete. Keep each work unit isolated and traceable in dependency-ordered branches/Draft PRs. Continue all scoped audits, regression protection, static QC, builds, and exact-head CI while development proceeds; only merge timing is deferred. Reserve `integration/b-series` for final grouped integration.

At the end, freeze exact branch heads, audit the combined diff and dependencies, merge into `integration/b-series` by coherent groups, run exact-head CI and cross-group regression after each group, complete final acceptance, obtain explicit user approval, merge to `main`, and build the final APK from the exact approved `main` SHA. If a physical test candidate is needed before final release, label it clearly as a non-release candidate. See `MERGE_QUALITY_POLICY.md` v1.1 and Decision D-013. This amendment supersedes D-012's rolling stage-merge timing for B06 onward; it does not weaken quality gates.


## B06 current status update — 1 November 2026

B06 is **DONE for source implementation, static QC, and exact-head CI**. The initial-status table above records the stage's starting state and is not a current-status override. See [B06_FEEDBACK_AUDIT.md](B06_FEEDBACK_AUDIT.md) for the complete implementation inventory, timeout policy, resource parity, tests, and CI evidence. Runtime/device/phone QR acceptance remains deferred to final F-stage system testing, not waived. PR #16 remains Draft/unmerged under D-013.


## 10. Project C execution checkpoint — 1 November 2026

- **C01:** source/CI pass on `feature/c01-local-communication-audit`, Draft PR #18, exact head `bba0801c9eb61ab27ae5046379c7a50aabebf570`, CI #314 PASS. QR upload server foundation, selected-interface binding, framing, concurrency, and image validation were hardened.
- **C02:** source/CI pass on `feature/c02-tv-local-server-ip`, Draft PR #19, exact head `b0e1ff048e36bf21f8da1e165a427075f85a7cb3`, CI #333 PASS (run #334 also succeeded). Installer active IPv4 discovery, bounded subnet scan, APK HTTP server lifecycle, routing, and cleanup were hardened.
- **C03:** source/CI pass on `feature/c03-phone-tv-network-verification`, Draft PR #20, exact head `d266287df287167e5a8e73fd675b51759b580ed6`, CI #338 / 41287563951 PASS on exact C03 head `d266287df287167e5a8e73fd675b51759b580ed6`; both jobs succeeded. The launcher unit-test/release-build job passed, including `testReleaseUnitTest`, `assembleRelease`, and the 1.5 MiB APK size gate. APK publication was skipped.
- C03 corrected the QR upload IP resolver: Avatar, Wallpaper, and StandBy now use Android's active default Wi-Fi/Ethernet IPv4 rather than selecting an interface by its name. Added JVM tests for usable IPv4 and invalid address/prefix rejection.
- Release APK diagnostic on C03: 1,546,045 bytes (1,509 KiB), below the 1,572,864-byte / 1.5 MiB gate by 26,819 bytes. No APK was published or installed.
- **Physical acceptance remains DEFERRED, NOT WAIVED:** real phone/TV same-Wi-Fi QR reachability, uploads, Wi-Fi reconnect, Ethernet/Wi-Fi switching, VPN behavior, and route revocation after closing the QR dialog must be verified during F03/final integrated acceptance.
- PRs #18, #19, and #20 remain OPEN/DRAFT/UNMERGED under D-013. Do not merge until A–G source work is complete and grouped integration is approved.
- **Next task:** C04 — audit HTTP endpoints, route matching, methods, and response codes. Preserve C01–C03 changes unless a concrete dependency or defect requires adjustment.


## C04 execution checkpoint — 1 November 2026

- Branch/head: `feature/c04-http-endpoint-routing` / `f09ee1b30b80e45e793c1678935eff8867aedae4`; Draft PR #21 targets C03 exact head `d266287df287167e5a8e73fd675b51759b580ed6`.
- Audit: `C04_HTTP_ENDPOINT_AUDIT.md`; HTTP request parsing, token-scoped route matching, per-endpoint method contracts, query parsing, response codes, and gallery browser action handling reviewed.
- Exact-head CI #342 / `41288521501`: PASS, both jobs SUCCESS; launcher and installer unit tests and release builds succeeded.
- APK size: 1,547,845 bytes; 15,019 bytes below the 1.5 MiB cap. Artifact publication skipped.
- Physical phone/TV QR browser acceptance remains deferred to F-stage, not waived. PR #21 remains OPEN/DRAFT/UNMERGED under D-013.
- Next: C05 shared upload/receive-file pipeline audit; preserve C01–C04 and expand scope only for demonstrated shared pipeline defects.
