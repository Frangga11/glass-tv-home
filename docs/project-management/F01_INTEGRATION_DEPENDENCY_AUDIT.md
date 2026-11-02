# F01 — Integration & Dependency Audit

**Checkpoint:** 2 November 2026  
**Stage:** Project F / F01  
**Status:** CLOSED — PASS (isolated combined source integration build and unit-test gate completed; physical-device acceptance remains pending in later F stages).  
**Repository:** `Frangga11/glass-tv-home`  
**Documentation branch:** `docs/project-master-inventory`

## Objective

Map the A–E dependency graph, inspect the current PR diffs for shared-file collisions and cross-module contracts, verify available exact-source CI evidence, and identify the safe next integration actions without violating D-013.

## Binding integration policy

- Keep feature PRs OPEN / DRAFT / UNMERGED. No merge was performed.
- Do not create or publish a TV-test APK and do not install an APK during this audit.
- Do not treat filename overlap as proof of a code conflict; inspect semantics and ownership before any combined integration.
- Preserve exact source/test heads and existing CI evidence. Documentation-only follow-ups must not be mistaken for source/test verification.
- D-013 reserves grouped integration/merge for Project G after source work and Project F gates, with explicit user approval.

## Dependency map

- B foundation: B01–B07; shared UI/navigation/focus/dialog/localization contracts are consumed by C, D and E.
- C communication: C01 → C02 → C03 → C04 → C05 → C06 → C07. C07 is the final upload-session resilience layer for Avatar, Wallpaper and StandBy.
- D state/persistence: D01 → D02 → D03 → D04 → D05 → D06 → D07. D01-F11's StandBy persistence defect is specifically repaired in D05 and protected by D07's content-store concurrency work.
- E feature chain: PR #34 E01 → #35 E02 → #36 E03 → #37 E04 → #38 E05 → #39 E06 → #40 E07 → #41 E08. E01 is based on D07. Each E stage inherits the prior approved feature baseline and shared B/C/D contracts.
- F validates the integrated product; G owns grouped merge and final release.

## Diff audit — observed shared-file hotspots

Unified diffs were retrieved for B01–B03 (#10–#12), C01–C07 (#18–#24), D01–D07 (#25–#31), and E01–E08 (#34–#41). The following are semantic-reconciliation hotspots, not automatically confirmed conflicts:

| Shared path / contract | PRs observed touching it | Integration risk / required preservation |
|---|---|---|
| `MainActivity.java` | #10, #12, #18, #20, #23, #26, #27, #31, #34–#41 (multiple E PRs) | Highest-risk central integration surface: preserve feature entry points, QR flows, weather freshness, settings propagation, profile refresh, lifecycle callbacks and wallpaper/StandBy behavior. |
| `WallpaperUploadServer.java` | #12, #18, #20–#24, #29 | Preserve endpoint/session contracts, framing, bounded body reception, file validation, cancellation/retry/expiry and truthful persistence responses. |
| `AvatarUploadServer.java` | #12, #20–#24 | Preserve token-scoped routes, security, upload limits, cancellation and final commit/session synchronization. |
| `TvDialogStyle.java`, `TvUiTokens.java` | B01–B03, D02 and E01/E07 | B owns canonical global tokens and modal/popup policy. Feature dialogs must consume the shared contract; no competing global style. |
| `ModernSettingsDialog.java` | B01–B03, D03/D04 and E01/E02/E06/E08 | Reconcile Settings shell, focus/selection, modal behavior, Profile controls, Home controls and wallpaper management without dropping callbacks or state. |
| `WeatherActivity.java`, `WeatherAirQuality.java`, `HomeWeather.java` | B/C touchpoints, D07, E01/E05/E07 | Preserve D07 generation/location/language freshness, truthful stale/unavailable states and E05 presentation; direct weather HTTPS is not the C QR upload server. |
| `UserSwitchDialog.java`, `ProfileManager.java`, `ChildModeManager.java` | B01–B03, D04/D07, E01/E02 | Preserve profile-scoped state, device-wide parent PIN, active-profile consistency, security gates and avatar cleanup. |
| `StandbyPhotoStore.java` and related tests | C06, D05, D07 | Preserve atomic photo collection + display-mode commit, accurate HTTP outcome, concurrent picker/QR merge and file/URI cleanup. |
| localized `strings.xml` resource sets | B03, C01/C04, D02 and E01/E02/E05 | Keep default/ID/EN resource parity; preserve Indonesian fallback and correct placeholders/plurals. |
| `.github/workflows/ci.yml` | B01/B03 | Preserve all required test/build/size gates; ensure combined integration CI still covers launcher and tools/docs. |

## Confirmed cross-project ownership and handovers

1. **B03 ↔ E07 Glass Effect fallback:** B03 owns the global solid-surface/dialog policy and shared fallback tokens; E07 owns the Glass Effect preference and invalidation of live app-owned popups. The source branches independently passed exact-head CI. G must reconcile overlapping shared files semantically; F must test Glass ON/OFF, live toggle, popups, dock, D-pad and Back on 1080p/4K.
2. **C07 ↔ D05/D07 StandBy upload:** C07 owns upload-session resilience and must remain intact. D05 owns atomic StandBy persistence and truthful success/failure; D07 owns concurrent content-store mutations. No C07 protocol/session rewrite is authorized by the D05/D07 repair.
3. **D07 ↔ E05 Weather/AQI:** D07 owns stale-response/cache freshness and concurrency. E05 owns weather/AQI data presentation and source semantics. B owns shared modal primitives. C01–C07 are not the owner of direct Open-Meteo requests.
4. **B global UI ↔ feature dialogs:** B owns global UI primitives; E/C/D feature owners retain their feature workflows and consume the shared contract. Android OS-owned Settings remains native.

## Exact-source CI evidence checked

| Area | Exact source/test head | CI evidence | Result |
|---|---|---:|---|
| B01 | Latest recorded global UI follow-up head not stated in the checkpoint | #563 | PASS |
| B02 | Latest recorded global UI follow-up head not stated in the checkpoint | #565 | PASS |
| B03 | `d87ffdf914851b8a1d2c67c2416ac9f5f1309d93` | #687 | PASS |
| C07 | `1cc6d7033daf6f9371805f667467b8860edcfb8f` | #371 | PASS |
| D05 | `835986c0e28c50311d4474f1060c73dd56104bf8` | #397 | PASS |
| D07 | `964163c918e281b68af5cf7114ca56ead2797486` | #404 | PASS |
| E01 | `b6fa70e406afc9f6e81d16a3635a69afc6c4d783` | #609 | PASS |
| E02 | `3ab9b55f3cfe03ad9841a119590a9183cf114e96` | #623 | PASS |
| E03 | `e085f8aed6fdc85b4793b76bb26ab8543b26b2cf` | #626 | PASS |
| E04 | `08076bb945a169de198905152a092f419f48aa82` | #629 | PASS |
| E05 | `6fe89fad078692958db67030329a6de66cd7ae71` | #663 | PASS |
| E06 | `570b7c83efaec3ac515b78f1c6c119b777fc3b84` | #668 | PASS |
| E07 | `6dc39d76e3c897e841854f425d549dbfebf1025c` | #688 | PASS |
| E08 | `fcd7d8f23a4833830a401652ef269db3282b7300` | #702 | PASS |

CI evidence above validates the listed exact source/test commits only. It does not prove that the full combined application has been built or run.

## F01 findings and open gates

- The highest integration concentration is `MainActivity.java`; the next are the upload server pair, global UI/dialog primitives, Settings dialog, weather/profile state and StandBy storage.
- Many overlapping files are expected because branches are dependency-ordered and sequential. The next step is a frozen-head semantic combined-diff review, not blind conflict resolution.
- All E01–E08 source/test checkpoints listed above have a passing exact-head CI record. E04 CI #629 is confirmed PASS.
- Isolated combined integration build and unit-test gate completed via Draft rehearsal PR #43; CI #716 PASS on synthetic merge commit `21c2eab34a3356d37d079eba04b281cc2373386d`. Full physical-device regression, Android TV installation, 1080p/4K visual acceptance, phone-to-TV QR acceptance and reboot test remain for later F-stage acceptance.
- PR #34–#41 and the relevant B/C/D feature PRs remain Draft/open/unmerged under D-013.
- No source defect is declared solely from the diff overlap. A specific owner repair is opened only after a reproducible semantic mismatch or failing test is identified.

## Next actions

1. Freeze and re-verify the current head of every dependency branch, including report-only follow-ups.
2. Inspect the complete combined semantic diff for the hotspots above, especially B03/E07 and C07/D05/D07.
3. Run the integrated software build/test gate only on an isolated rehearsal path after the dependency snapshot is fixed; do not merge feature PRs or publish/install an APK.
4. Route any confirmed defect to its owning project, then rerun exact-head CI and relevant cross-module regression.
5. Close F01 only when dependency/head consistency and combined software-gate evidence are recorded. Proceed to F02 after that gate.

## Safety / release boundary

No feature PR was merged. No APK was published or installed. No physical-device acceptance or release approval is claimed.


## F01 expanded dependency audit — 2 Nov 2026

### Complete B-series coverage

F01 also reviewed B04–B07 and the earlier B04/B05 integration history, in addition to B01–B03.

- PR #13 B04, PR #14 B05, and PR #15 B04/B05 reconciliation are already MERGED (pre-D-013). The reconciled B04/B05 branch is an ancestor of `integration/b-series`; the B-series integration checkpoint is CI #257 PASS.
- PR #16 B06 and PR #17 B07 remain OPEN/DRAFT/UNMERGED under D-013.
- B01/B02/B03/B06/B07 current branch-head CI: #563/#565/#687/#569/#571 — PASS. B04/B05 reconciliation CI #254 — PASS.
- B01 canonical `TvDialogStyle` and `TvUiTokens` blobs are identical in B01, B02, B07 and D04 branches. E01–E06 consume the same B01 blobs. B03 and E07/E08 have small comment-only differences in the two shared files; line comparison found no executable-code difference. B03 remains owner of global fallback policy; E07 remains owner of live Glass Effect toggle/invalidation.

### Parent/base drift register

The following PRs target the correct dependency branch but their recorded PR base SHA is older than the current target branch head. GitHub currently reports these PRs as mergeable; that is not a substitute for a combined integration build. Their latest parent changes must be preserved by dependency-ordered grouped integration and re-tested.

| PR | Target branch | Recorded base SHA | Current target HEAD | Assessment |
|---|---|---|---|---|
| #11 B02 | B01 | `bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5` | `d31a90e27d61ab26b57edac74769ef0cc266ae27` | Base advanced; shared Settings/tokens changes require semantic reconciliation. |
| #12 B03 | B02 | `48cb89a1c4138e7521c7991d31a9eb981f773f2a` | `c746063cb08ed393d69fe5634dd241baff61a873` | Base advanced; shared dialog/tokens changes require semantic reconciliation. |
| #17 B07 | B06 | `eb6c56e515dbde9c3621f12018e0c416dc5247bb` | `5b2f10095bd542f71829665422a36db79d7413f4` | B06 added two Settings/tokens commits after B07's recorded base. |
| #18 C01 | B07 | `3fcbf8d52d0baafe76d8239a5e5dab7634d0850e` | `f62bdc5d98046590e69a27bb935102afa95dc71c` | Base advanced; C01 must be reconciled with current B07 UI/focus baseline. |
| #19 C02 | C01 | `bba0801c9eb61ab27ae5046379c7a50aabebf570` | `8a3a981f097ea21be3b87580acabb69caa1b8fad` | Base advanced; preserve current C01 QR entry/feedback and route contracts. |
| #21 C04 | C03 | `d266287df287167e5a8e73fd675b51759b580ed6` | `0599ceb01a5522ff5840d29e58ecab4a970e8843` | Base advanced; preserve current network verification changes. |
| #24 C07 | C06 | `e74b6466c058ec930aa92c8db9603b11cf96a185` | `2bf119da458278e17d8aac6fc125824886bfe37c` | Base advanced; C07 upload resilience must be combined with current C06 file validation and shared UI baseline. |
| #29 D05 | D04 | `ad69d229a6b3cd6ca1810a595597dc88481c3ebd` | `302a3aa1a7fa06e7f9d11beaee8055bf2d3735fe` | Base advanced; D05 atomic StandBy persistence must preserve D04 profile/avatar state and shared UI changes. |
| #34 E01 | D07 | `964163c918e281b68af5cf7114ca56ead2797486` | `7ce92d30911288eed98aa2e7b36eaf9942d0c355` | Base advanced with D07 Weather/AQI source/test changes; must retain these while integrating E modules. |

Other PR parent snapshots inspected (#10, #13–#16, #20, #22–#23, #25–#28, #30–#31, #35–#41) match their recorded target dependency heads at the current checkpoint. PR #13–#15 are already merged; #16–#17 and #10–#12, #18–#12, #24–#41 remain under the D-013 boundary as applicable.

### Current source/test CI checkpoint

Latest branch-head or final source/test CI records checked on 2 Nov 2026:

| Group | Module/checkpoint | CI run | Result |
|---|---|---:|---|
| B | B01 / B02 / B03 | #563 / #565 / #687 | PASS |
| B | B04+B05 reconciliation / integration/b-series | #254 / #257 | PASS |
| B | B06 / B07 | #569 / #571 | PASS |
| C | C01 / C02 / C03 / C04 | #549 / #333–#334 / #550 / #355 | PASS |
| C | C05 / C06 / C07 | #361 / #551 / #371 | PASS |
| D | D01 / D02 / D03 / D04 | #375–#376 / #587 / #588 / #589 | PASS |
| D | D05 / D06 / D07 | #397 / #399 / #642 | PASS |
| E | E01 / E02 / E03 / E04 | #610 / #623 / #626 / #629 | PASS |
| E | E05 / E06 / E07 / E08 | #663 / #668 / #691 / #703 | PASS |

These are per-branch source/test checks. They do not independently prove the full product integration.

### Isolated F01 rehearsal

- Created isolated branch `integration/f01-rehearsal-only` from the synthetic PR #41 merge candidate; no feature branch or canonical integration branch was modified.
- Created Draft PR #42, base `feature/d07-concurrent-profile-content-state`, head `integration/f01-rehearsal-only`, to test the complete E01–E08 candidate against the latest D07 baseline.
- PR #42 current mergeability was recomputed as **MERGEABLE**. It remains a rehearsal-only PR; do not merge.
- CI run #708 completed successfully: launcher unit tests/release build, read-only APK diagnostics, APK size guard, tools/docs checks all PASS; manual APK publication step SKIPPED.
- CI runs #709 and #710 were IN PROGRESS at the time of this report update. Their final result must be recorded before closing F01.
- The synthetic merge candidate combines the E01–E08 feature chain with current D07. It is an isolated rehearsal, not a merge to `integration/b-series` or `main`.
- No APK artifact was published or installed. No physical Android TV acceptance was performed.

### F01 disposition

The dependency and ownership audit is complete. All module-level CI evidence checked is green. The PR chain is currently reported mergeable, and the isolated rehearsal has produced a successful CI run, with two additional runs still in progress.

**F01 remains OPEN until runs #709/#710 are final and their results are checked.** After that, close the rehearsal PR without merging, return it to Draft if still open, and hand the branch-drift register to Project F02/G for dependency-ordered grouped integration. Do not treat this as approval to merge or release.


## F01 FINAL CLOSURE — 2 November 2026

**Disposition: F01 CLOSED — PASS (source integration and dependency audit).**

### Integrated candidate and CI evidence

- First rehearsal PR #42 (E01–E08 against current D07) completed CI #709 and #710 PASS. It exposed the E08 wallpaper entry-point mismatch described below. PR #42 was closed without merge.
- F01 verified and repaired the E08 ownership/UX defect on the existing E08 feature branch. Exact updated E08 head: `1d6fba799012ff0e8dbbfbce5a7fbe4f193e1d0d`; exact-head CI #711 and #712 PASS.
- Final rehearsal PR #43 combined the updated E01–E08 feature chain against current D07 head `7ce92d30911288eed98aa2e7b36eaf9942d0c355`. GitHub synthetic merge candidate: `21c2eab34a3356d37d079eba04b281cc2373386d`.
- Integrated CI #716: **PASS**. GitHub Actions checked out `refs/remotes/pull/43/merge`; `testReleaseUnitTest`, `assembleRelease`, tools/docs tests, read-only APK diagnostics and APK size guard all passed.
- Diagnostic release APK: **1,563,769 bytes**; cap **1,572,864 bytes**; headroom **9,095 bytes**. Manual publication step **SKIPPED**.
- PR #43 was closed without merge after evidence capture. PR #42 was also closed without merge. PR #41 remains OPEN/DRAFT/UNMERGED under D-013.

### Verified E08 wallpaper entry-point repair

- Quick Control > Wallpaper remains the compact Change / Shuffle / Next control center.
- Settings > Appearance > Wallpaper is restored as the fuller management route with separate Collection, Interval and Personal Wallpaper Manager cards.
- Appearance management entry points keep the Settings surface as the return destination; they do not reopen the compact Quick Control popup.
- Both routes reuse the same wallpaper preferences, category selection, custom-photo manager, QR upload, shuffle/interval behavior, rotation and persistence. No duplicate engine, preference key or storage schema was added.
- Canonical E08 audit addendum is recorded in `E08_WALLPAPER_SETTINGS_REDESIGN_AUDIT.md`.

### Final boundaries and handover

- B01–B07, C01–C07, D01–D07 and E01–E08 dependency ownership and shared-file hotspots are mapped. All current module/source checkpoints sampled in F01 have passing CI evidence.
- Parent/base drift on PRs #11, #12, #17, #18, #19, #21, #24, #29 and #34 remains documented. The PRs are currently mergeable, but grouped integration must preserve the newer target-branch changes and rerun cross-group regression.
- No feature PR was merged. No APK was published or installed. No physical Android TV test, 1080p/4K acceptance, phone upload acceptance or reboot acceptance is claimed.
- D-013 remains binding: grouped integration/merge is reserved for Project G after all applicable F gates and explicit user approval.
- **Next stage:** F02 — integrated functional regression and controlled Android TV acceptance. F02 must use the verified F01 candidate lineage and test remote/D-pad/focus/Back, popup/dialog consistency, Glass ON/OFF, Wallpaper vs Appearance route distinction, profile/app-lock, Weather/AQI, QR upload, StandBy persistence/lifecycle, 1080p/4K and recovery.
