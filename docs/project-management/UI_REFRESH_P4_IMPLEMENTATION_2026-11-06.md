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
