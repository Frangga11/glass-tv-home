# F01 — Integration & Dependency Audit

**Checkpoint:** 2 November 2026  
**Stage:** Project F / F01  
**Status:** IN PROGRESS — source-diff and dependency mapping completed; combined integration build and runtime validation not yet executed.  
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
| B01 | `090a956a9860c82eb36c2d96848d47046fba0f0c` (recorded source checkpoint) | #610 | PASS |
| B02 | `7e8392e1a76223b2c1aef314c10fa54d0b813fdc` | #208 | PASS |
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
- No combined integration build, full integrated regression, Android TV installation, 1080p/4K visual acceptance, phone-to-TV QR acceptance or reboot test was executed in this F01 audit.
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
