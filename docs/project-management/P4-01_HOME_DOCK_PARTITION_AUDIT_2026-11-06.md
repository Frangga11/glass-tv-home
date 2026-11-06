# P4-01 Home Screen & Dock — Partition Model Audit

**Checkpoint:** 6 November 2026  
**Branch:** `feature/ui-global-01-home-dock`  
**Baseline:** `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`  
**Draft PR:** #64

## Contract invariants

From the frozen P2 Shared Design & Interaction Contract, section 11.8:

1. Dock and app grid are independent presentation regions.
2. Dock orientation/capacity must not change app-grid span count.
3. Dock membership/order is explicit and stable by app identity, not inferred from the first N RecyclerView positions.
4. Capacity is 4/5/6; overflow remains reachable in the app grid.
5. No app may be silently dropped.

## Source audit

- Current `MainActivity` uses one `RecyclerView` and `GridLayoutManager(COLUMNS)` for both Dock and app grid.
- `AppAdapter` maintains one ordered `items` list; the legacy renderer treats the first `dockSize` positions as Dock.
- `DockDecoration`, tile sizing, `parkDock()`, top-button alignment, and D-pad navigation are also coupled to `MainActivity.COLUMNS`.
- Directly changing `COLUMNS` to the Dock capacity would alter app-grid geometry and could regress focus/navigation. That shortcut is prohibited.

## Current implementation slice

- `DockPreferences` persists orientation and capacity independently.
- `DockLayoutModel` now partitions an ordered inventory using an explicit pinned-key list and stable app identity.
- Dock order follows the explicit pinned-key list; the app grid preserves source order for all non-pinned entries.
- Missing installed apps do not reserve slots. Duplicate/blank keys are ignored.
- Pinned entries beyond capacity remain in the app grid as overflow; none are discarded.
- `AppAdapter.dockSnapshot()` exposes the immutable snapshot and reads capacity from `DockPreferences`, not `MainActivity.COLUMNS`.
- Existing legacy combined RecyclerView remains the visible renderer until a separate Dock view/adapter and focus controller are introduced.

## Verification boundary

The pure model tests cover explicit membership, order, capacities 4/5/6, invalid-capacity fallback, missing/duplicate keys, duplicate inventory entries, immutable snapshots and empty input. Exact-head CI must pass before this slice is accepted.

Not yet implemented or verified:
- Dedicated Dock container and adapter.
- Independent app-grid adapter/data source.
- Orientation-specific rendering and overflow navigation.
- D-pad focus/reorder and install/remove lifecycle across the two regions.
- Settings UI and preference-change live update.
- 1920×1080 screenshots, visual comparison and physical TV remote acceptance.

**Disposition:** partition model slice only; Module 1 remains IN PROGRESS. No merge, APK publication, installation or device-setting changes.
