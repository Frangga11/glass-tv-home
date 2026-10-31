# Glass TV Home — Master Bug Repair Roadmap

**Roadmap ID:** GTVH-BRR-001  
**Version:** 1.0  
**Approved:** 1 November 2026  
**Status:** OFFICIALLY APPROVED — A01–A06 complete; B01–B03 source QC and exact-head CI pass; B04/B05 source audits underway; device testing deferred to F02  
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
| A06 | Establish baseline, backup, and rollback/checkpoint | IN PROGRESS | Exact tested source/APK checkpoint and rollback SOP documented; full read-only Mi TV Stick snapshot captured 1 Nov 2026. HOME resolver/preferred-activity discrepancy and installed APK hash limitation recorded for E01; no device mutation performed |

### Stage B — Global foundations and user experience

| ID | Task | Initial status |
|---|---|---|
| B01 | Audit and correct global navigation and cursor-position retention | IN PROGRESS |
| B02 | Ensure visible, consistent D-pad/remote focus indicators | IN PROGRESS |
| B03 | Correct popup/dialog opacity, layering, focus, sizing, and dismissal behavior | IN PROGRESS |
| B04 | Normalize global UI layout, spacing, sizing, and TV responsiveness | IN PROGRESS |
| B05 | Audit and complete Indonesian/English localization, including dynamic and error strings | IN PROGRESS |
| B06 | Standardize notifications, success/error messages, loading, and timeout feedback | NOT STARTED |
| B07 | Audit activity/dialog lifecycle and restore prior menu/subtab/focus after actions | NOT STARTED |

**Stage exit criteria:** remote navigation and focus are predictable; dialogs are visually isolated and readable; language selection is consistent; actions do not unexpectedly reset the user to the first tab.

### Stage C — Shared communication infrastructure

| ID | Task | Initial status |
|---|---|---|
| C01 | Audit QR generation, payload, token, and QR destination behavior | NOT STARTED |
| C02 | Audit TV local server lifecycle, LAN IP selection, and port handling | NOT STARTED |
| C03 | Verify phone-to-TV communication and same-network behavior | NOT STARTED |
| C04 | Audit HTTP endpoints, routing, methods, and response codes | NOT STARTED |
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

This is a task-count snapshot, not a percentage of application readiness. A01–A06 are complete. A04 did not inspect a separate local clone; this limitation is recorded. B01/B02 source work remains isolated on dedicated Draft PRs under the previously approved sequencing exception D-009. B01 exact-head CI #225 and B02 exact-head CI #234 passed; B02 second-pass source review found no listener collision in the touched paths. B03 source/CI gate PASS on Draft PR #12: shared dialog/panel/QR surfaces made opaque; explicit D-pad focus selectors added to standard dialog buttons/lists; QR dialog bounded and scrollable; Now Playing/card prompt widths clamped. Exact-head CI #239 passed both jobs, with release APK size 1,398 KB (<1.5 MiB); manual TV-test artifact publication was skipped. B03 physical acceptance remains deferred to F02. B04 initial source audit is underway; display metrics are needed before deciding whether fixed Recent Apps card geometry requires adjustment. A05 is complete with a direct byte-for-byte match between the user-uploaded tested APK and the APK extracted from CI artifact #13048951728; CI run #208 binds that artifact to PR #9 exact source head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`. A06 is DONE: the exact source/APK checkpoint and rollback procedure are documented, and the user supplied a fresh read-only Mi TV Stick snapshot. The resolver returns the stock launcher while the package preferred-activity table lists Glass TV Home; this discrepancy is carried into E01. The installed package metadata matches version 4.0.0/code 40, but its APK bytes/hash were not extracted from the TV. B03 is IN PROGRESS on branch `feature/b03-dialog-opacity-layering`, Draft PR #12, based on B02 head `01cbef2c2fc90498295971b22c369a72cdfa734a`; source/CI gate passed on head `56a4e1995aa8b8600614e6b5c4dca69d01c530c3`, CI #239. B04 is IN PROGRESS: source review found responsive home/settings/weather layouts, with fixed Recent Apps card and Nightstand clock dimensions requiring target display metrics before any adjustment. Next read-only values: `wm size` and `wm density`.

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
