# B07 — Menu, submenu & focus restoration audit

**Date:** 1 November 2026  
**Status:** IMPLEMENTED — VERIFICATION IN PROGRESS  
**Repository:** `Frangga11/glass-tv-home`  
**Source branch:** `feature/b07-menu-focus-restoration`  
**Source PR:** [#17](https://github.com/Frangga11/glass-tv-home/pull/17) — Draft / open / unmerged  
**Base dependency:** `feature/b06-notification-feedback` at `bc86a250d73a5cbb83250217d42c3c3993cf29d7`  
**Current source head:** `d4eabc48e020ab6cbef54e0f8884cf125eff97ed`  
**Exact-head CI:** run #298 / `41281838510` — IN PROGRESS at checkpoint; no PASS claim yet.

## 1. Trigger and scope

B07 covers restoration of the Settings menu, active submenu/content pane, D-pad focus target, and scroll position after the Settings dialog or owning Activity is recreated/reopened. The audit is dependency-aware: B01 Home navigation persistence, B02 focus indicators, B03 dialog behavior, B05 localization, and B06 feedback/lifecycle changes are treated as upstream contracts.

## 2. Audit findings

| ID | Finding | Impact | Classification |
|---|---|---|---|
| B07-F01 | Settings tab selection is persisted, but reopening always requests focus on the selected sidebar item. | A user who was working inside a right-pane control loses the exact D-pad position. | CONFIRMED from source |
| B07-F02 | Right-pane focus target and scroll offset are not persisted. | Deep settings controls can reopen outside the previous viewport and require repeated navigation. | CONFIRMED from source |
| B07-F03 | Sidebar scroll offset and exact sidebar focus target are not persisted together. | Search/sidebar cursor position can be lost on recreation. | CONFIRMED from source |
| B07-F04 | Settings content is rebuilt dynamically and localized. | A raw persisted child index can become stale and could point at a different control. | CONFIRMED risk |

B01 already owns Home tile/top-pill focus restoration. B07 does not replace that mechanism. Existing transient popup/menu dismissal during Activity pause remains intentional; B07 focuses on restoring the persistent Settings navigation context rather than reopening transient action dialogs.

## 3. Root cause

`ModernSettingsDialog.show()` loaded the persisted `SETTINGS_TAB`, rebuilt the pane, and then unconditionally requested focus on `activeSidebarView`. No state represented whether focus had been in the sidebar or right pane, which control held focus, or the right/left scroll offsets. Rebuilt content therefore had no restoration target beyond the selected tab.

## 4. Implemented correction

- Persist focus area (`sidebar` / `right`), active tab, validated view-tree child-index path, and a target signature (class, view ID, and stable label/hint).
- Restore focus only when the saved tab matches and the resolved target is still visible, enabled, focusable, and signature-identical.
- Fall back to the selected sidebar destination when the path is malformed, stale, or no longer maps to the same control.
- Preserve left-sidebar scroll and per-tab right-pane scroll offsets.
- Save the current pane's scroll offset before dynamic re-render.
- Remove the global focus listener when the Settings dialog is dismissed to avoid retaining stale view references.
- Add Android-free `SettingsFocusPath` encoding/decoding with depth/index/length bounds and JVM tests for round-trip, zero index, malformed values, and out-of-range inputs.

## 5. Files changed

- `launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java`
- `launcher/app/src/main/java/com/gathin/tvhome/SettingsFocusPath.java`
- `launcher/app/src/test/java/com/gathin/tvhome/SettingsFocusPathTest.java`

## 6. Verification matrix

| Gate | Status | Evidence / next action |
|---|---|---|
| Scope and dependency map | PASS | B01–B06 contracts reviewed; source work is isolated on B07 branch stacked on B06. |
| Root-cause source audit | PASS | Confirmed unconditional sidebar focus and missing right-pane/sidebar cursor+scroll snapshot. |
| Scoped implementation | PASS | PR #17 diff contains only the three files listed above. |
| Focus-path bounds and JVM test coverage | IMPLEMENTED / CI PENDING | Four tests added; require exact-head CI result. |
| Android release build and full JVM suite | IN PROGRESS | GitHub Actions run #298 on exact head `d4eabc48e020ab6cbef54e0f8884cf125eff97ed`. |
| Lists/tools/docs CI | IN PROGRESS | Same exact-head run #298. |
| Runtime recreation/API 30 and API 33+ | DEFERRED | Final B-series/F-stage device matrix; not waived. |
| Physical TV D-pad, submenu, and scroll acceptance | DEFERRED | Requires target TV and remote; not waived. |
| Cross-B regression against B01–B06 | PENDING | Re-run during grouped integration after all A–G source work is frozen. |
| Merge | DEFERRED BY D-013 | PR #17 remains Draft/open/unmerged; no stage merge before grouped integration. |

## 7. Residual risks and acceptance cases

Manual acceptance must cover: focus on a sidebar item; focus on a deep right-pane control; right-pane scroll; sidebar scroll; Settings close/reopen; Activity recreation/process reclaim; switching tabs then returning; search-to-tab navigation; child modal dismissal; content changes that invalidate a stored path; Indonesian/English locale changes; and D-pad Up/Down/Left/Right navigation.

A stale or mismatched path must always fall back safely to the active tab's sidebar destination. Physical acceptance is deferred, not passed.

## 8. Decision

B07 source implementation is in place but is **not yet paripurna**. Do not mark B07 complete, merge PR #17, or advance Project B to final closure until exact-head CI is green, code review/QC is complete, and the remaining deferred runtime/device tests are carried into the final B-series acceptance matrix. Grouped merge remains governed by D-013.
