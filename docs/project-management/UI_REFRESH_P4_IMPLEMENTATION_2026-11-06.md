# UI Refresh P4 — Staged Global UI Implementation Log

**Date:** 6 November 2026  
**Scope:** Begin production implementation under the user's explicit instruction to implement all Global UI modules in dependency order.  
**Design authority:** Approved V01–V06 direction and frozen P2 Shared Design & Interaction Contract (GTVH-UIR-P2-001).  
**Implementation rule:** One module at a time; audit → implementation → functional test → visual QC → documentation. Keep all source PRs unmerged until the complete integration and acceptance gates pass.

## 1. Trigger and baseline

- User explicitly directed implementation of the ten Global UI workstreams, beginning with Home Screen & Dock.
- Frozen source baseline: `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- Existing E06 PR #63 remains OPEN/DRAFT/UNMERGED and is based on a divergent Home subtabs branch. Its preference-only implementation is not treated as integrated or feature-complete.
- New isolated owner branch: `feature/ui-global-01-home-dock`.
- New Draft PR #64: https://github.com/Frangga11/glass-tv-home/pull/64.
- PR #64 base is the frozen integration baseline; it is not merged.

## 2. Module 1 — Home Screen & Dock

### Audit findings

- Current Home uses one RecyclerView/GridLayoutManager and one `MainActivity.COLUMNS` value for app-grid columns, Dock membership and Dock decoration/geometry.
- `AppAdapter` treats the first N entries as the Dock; this couples the Dock and main app grid.
- Changing `MainActivity.COLUMNS` to implement Dock orientation/capacity would change app-grid density and can break focus, tile sizing, decoration and navigation.
- Therefore the first slice adds an independent persisted Dock preference model only. It does not change the existing Home renderer or grid behavior.

### Implemented in PR #64

- `DockPreferences`: independent Dock orientation (horizontal/vertical) and capacity (4/5/6), with safe normalization and synchronous persistence.
- `DockPreferencesStateTest`: validates allowed capacities, safe orientation fallback, and bounded Dock membership.
- Explicit invariant: `MainActivity.COLUMNS` remains the main app-grid span count and is not modified by Dock preferences.

### Remaining Module 1 work

1. Introduce one authoritative ordered app model separating pinned Dock membership from main-grid contents.
2. Add a dedicated Dock container/renderer/adapter while preserving the current app-grid span and tile sizing.
3. Implement capacity-limited pinned slots and overflow reachability without losing app order.
4. Add localized Home Screen settings for Dock orientation/capacity and connect persistence to the renderer.
5. Implement D-pad movement, focus restoration, scrolling, reorder, app install/remove/hidden-app transitions.
6. Add functional tests for all six orientation/capacity combinations and invariant tests proving app-grid columns do not change.
7. Capture and compare rendered 1920×1080 screenshots against V01; then perform target-TV remote/focus acceptance.

## 3. Verification status

| Gate | Status | Evidence / boundary |
|---|---|---|
| Source audit | PASS — initial architecture finding | MainActivity, AppAdapter, DockDecoration and Home layout inspected at frozen baseline |
| Preference model added | PASS — source present | PR #64, two added files |
| Static unit-test source | PASS — test cases authored | Test source exists; execution is pending |
| Exact-head GitHub Actions | PENDING | No workflow run or combined status returned at checkpoint |
| Dock renderer / functional behavior | NOT IMPLEMENTED | Follow-up slices in Module 1 |
| Visual QC at 1920×1080 | NOT RUN | No rendered screenshot evidence |
| Physical TV / D-pad remote | NOT RUN | Deferred to system acceptance |
| Merge / APK publish / install | NOT PERFORMED | PR remains Draft; baseline untouched |

**Disposition:** MODULE 1 IN PROGRESS — FOUNDATION SLICE ONLY. Not feature-complete and not visually accepted.

## 4. Sequence control

The user's requested module order is retained:
1. Home Screen & Dock.
2. Global Settings and all categories.
3. Profile, Avatar and Child Mode.
4. Quick Settings and Wi-Fi.
5. Weather & AQI Dashboard.
6. Widget & Information.
7. Animation & Effects.
8. Standby Screen.
9. Device Settings.
10. All popups, submenus, dialogs and supporting components.

Cross-cutting B01 shared tokens/focus/dialog/navigation foundations are to be extended as required by each dependent slice; do not create parallel design systems. E01/E02/E05/E06/E07/E08/E04 source ownership remains intact.

## 5. Quality and merge policy

- Preserve existing behavior; any regression fix must have a reproducible finding and test.
- Keep feature branches isolated and PRs Draft/unmerged while module work is active.
- Reconcile each source PR against the frozen integration baseline before integration.
- No APK publication, installation, ADB or TV system-setting changes are authorized by this log.
- Do not mark any module complete until source audit, implementation, functional tests, visual QC, documentation and applicable runtime gates are evidenced.


## Module 1 continuation — Dock partition model — 6 November 2026

- Continued the Home Screen & Dock implementation on `feature/ui-global-01-home-dock`.
- Added `DockLayoutModel`, an immutable partition boundary for ordered app entries: Dock capacity is normalized to 4/5/6, order is preserved, blank/null/duplicate stable keys are excluded, and snapshot lists cannot be mutated.
- Added `AppAdapter.dockSnapshot()` as the read-only adapter boundary for the upcoming dedicated Dock renderer. The existing combined RecyclerView, app-grid span, D-pad/focus behavior and Home visual layout are intentionally unchanged in this slice.
- Added `DockLayoutModelTest` covering source order, all three capacities, invalid-capacity fallback, duplicate/blank keys, immutability, empty inventory and required stable-key function.
- Latest source head: `d739d0edad1449918faf44f538d42da632831714`; Draft PR #64 updated with exact scope and explicit outstanding work.
- Exact-head CI run #42178311594: Lists/tools/docs SUCCESS; Launcher build/unit tests IN_PROGRESS at last poll (Android SDK setup underway). Do not report latest head green until job completes.
- Earlier exact-head CI #42177905579 passed both jobs on initial preference-only head `7fe684f559140b11355dfb67cf2a246f518c7724`; this does not replace the new exact-head check.
- Module 1 remains IN PROGRESS. Dedicated Dock RecyclerView/adapter, independent main-grid data source, horizontal/vertical rendering, overflow, settings UI, D-pad/focus/reorder lifecycle, six-combination functional acceptance, 1920×1080 visual QC and physical TV acceptance are not implemented/verified yet.
- No merge, APK publication/installation, ADB or device-setting changes.


### Dock identity invariant correction — 6 November 2026
- Tightened the partition model to match P2 section 11.8 exactly: Dock membership is now selected by explicit pinned package identity, not by first-N list positions. Dock order follows the pinned-key list; non-pinned apps preserve inventory order.
- Missing packages do not reserve slots, duplicate/blank identities are ignored, and pinned items beyond capacity stay visible in the app grid. The Dock snapshot reads the independent Dock capacity preference; it does not read or change `MainActivity.COLUMNS`.
- Added/expanded tests for explicit identity membership, separate Dock/grid order, overflow retention, missing apps, duplicate keys, immutable snapshots and 4/5/6 capacity.
- Latest source/audit head `9cfdeb902689d249e9e01e4ac9c6c91902905a62`; exact-head CI #42178556451 is IN_PROGRESS. Earlier run #42178311594 passed build/test and size-gate steps on the intermediate head but does not verify the latest explicit-identity change.
- Detailed source audit: `P4-01_HOME_DOCK_PARTITION_AUDIT_2026-11-06.md`. Dedicated Dock renderer and app-grid adapter separation remain unimplemented; Module 1 stays IN PROGRESS. No merge or device action.


### Exact-head CI closeout — 6 November 2026
- Latest audited E06 source/audit head remains `9cfdeb902689d249e9e01e4ac9c6c91902905a62`.
- GitHub Actions CI run #1212 (`42178556451`) completed successfully: Launcher build/unit tests/release validation/APK size budget PASS; Lists/tools/docs PASS.
- This is a verified source/build checkpoint, not a Module 1 acceptance. The current source still uses one combined Home RecyclerView and one adapter; `DockLayoutModel` is a model boundary only. A separate Dock renderer, separate app-grid renderer, orientation-specific layout, settings controls, overflow/D-pad/focus/reorder migration and rendered 1920×1080 visual validation are still outstanding.
- PR #64 remains Draft/open/unmerged. No APK installation, ADB or physical TV acceptance was performed.
- **Decision:** do not advance to the next trigger yet. Complete the independent two-region renderer and all interaction/visual acceptance gates first; a green CI run alone does not close E06.


### Component identity correction and exact-head verification — 6 November 2026
- Found and fixed a correctness risk in the Dock snapshot: package name is not a unique launcher-tile identity because one package can expose multiple Leanback launcher activities. `AppAdapter.dockSnapshot()` now expands configured package favourites into component identities (`ComponentName.flattenToShortString()`), then partitions by component key. This preserves separate activities and prevents accidental model-level tile collapse.
- Added regression test `distinctLauncherComponentsFromSamePackageRemainDistinctTiles` and updated the source audit to record package-config-to-component-identity mapping.
- Source PR #64 current head: `ba5ccecd4a55e748aec5de45343b778c2fe4595b`; PR remains OPEN/DRAFT/UNMERGED, base `integration/b-series`. Exact-head CI run #1215 (`42180187065`) is IN_PROGRESS at checkpoint; both jobs are still running. No PASS is claimed until completion.
- The earlier green CI #1212 applies only to prior head `9cfdeb902689d249e9e01e4ac9c6c91902905a62` and does not validate this correction.
- Module 1 remains IN PROGRESS. Dedicated two-region renderer, orientation/settings, focus/D-pad/reorder/install-remove behavior, six-combination acceptance, 1920×1080 visual QC and physical remote acceptance remain open. No merge, APK publication/installation, ADB or device-setting change.


### Exact-head CI closeout — component identity correction — 6 November 2026
- PR #64 source head `ba5ccecd4a55e748aec5de45343b778c2fe4595b` verified by GitHub Actions run #1215 (`42180187065`): **both required jobs SUCCESS**. Launcher build, unit tests, release validation and APK size budget passed; Lists/tools/docs passed.
- This CI result validates the component-level Dock identity correction and its regression test at the exact head. It does not complete the Dock renderer or Module 1 acceptance.
- PR #64 remains OPEN/DRAFT/UNMERGED, base `integration/b-series`. Dedicated Dock/App Grid rendering, settings, orientation/capacity behavior, overflow, D-pad/focus/reorder lifecycle, six-combination functional acceptance and 1920×1080 visual QC remain outstanding. No device test, APK publication/installation, ADB, merge or device-setting change.
