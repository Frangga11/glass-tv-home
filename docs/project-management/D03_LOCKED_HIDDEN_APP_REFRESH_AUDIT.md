# D03 — Locked/Hidden App UI Refresh Audit

Date: 1 November 2026  
Status: IMPLEMENTED; exact-head CI pending  
Branch: `feature/d03-locked-app-ui-refresh`  
Parent: D02 exact head `2dbfcc181651184914422ad0697a3c5c27d15614`

## Scope

Verify that locked-app and hidden-app counts in the Profile > Parental Controls section refresh after editing the corresponding package selections, while the Settings dialog remains open. Also verify that home tiles are reloaded for every picker dismissal path.

Out of scope: Child Mode preference schema/migration, PIN policy, package-discovery policy, global preference listeners/event buses, and D01-F11 upload behavior.

## Confirmed finding

**D03-F01 — stale restriction summaries and incomplete dismissal refresh.**

- `renderRightPane` reads `ChildModeManager.getLockedApps()` and `getHiddenApps()` and embeds their sizes in the two Settings card titles.
- The picker persists each checkbox change immediately through `setAppLocked()` / `setAppHidden()`.
- Before this repair, the parent Settings pane was not rerendered when the picker closed, so the visible count could remain stale until a later navigation/rerender.
- `activity.reloadTiles()` was only called by the Done button. Dismissing the picker through Back could leave the home tile presentation unreloaded even though the selection had already been persisted.

## Repair

- Pass a narrowly scoped completion callback from the Profile Settings pane into the picker.
- Move tile reload responsibility from the Done-button click handler to the picker dialog's `OnDismissListener`.
- On every picker dismissal, provided the Activity is still valid:
  1. call `activity.reloadTiles()`;
  2. rerender the currently selected Settings right pane through the existing `renderRightPane` owner callback.
- This covers Done and Back dismissal without duplicating refresh logic.
- Parent PIN cancellation does not open the picker and therefore does not trigger an unnecessary refresh.
- Existing immediate preference writes, profile-scoped keys, package discovery, checkbox behavior, PIN authorization, and D-pad structure are preserved.

## Impact / preservation

Only `ModernSettingsDialog.java` is changed for application source. No preference keys, data migration, ChildModeManager behavior, C07 upload/session code, D01/D02 fixes, or global listeners are changed.

## Verification

- Source diff from D02 parent: one Java file, 16 additions / 6 deletions.
- Parent commit: `2dbfcc181651184914422ad0697a3c5c27d15614`.
- Implementation commit: `8066268d8533bcf44255d1ac2f1598e0f5fa6b99`.
- Exact-head CI: pending at report creation; do not mark D03 closed until both required workflow jobs succeed on the final head.
- No APK publication/installation, device setting change, or PR merge.
- The repository currently has JVM unit tests but no app `androidTest` UI harness. Therefore this Android dialog dismissal behavior is not claimed as physically/regression-tested by a UI test. Final TV acceptance must explicitly cover both Done and Back dismissal, both lock and hide pickers, refreshed counts, and home-tile visibility.

## Acceptance checklist

- [ ] Exact-head CI passes both jobs on final branch head.
- [ ] Done closes picker, reloads tiles, and refreshes count in the still-open Settings pane.
- [ ] Back closes picker, reloads tiles, and refreshes count in the still-open Settings pane.
- [ ] Lock and hide counts update independently.
- [ ] PIN cancellation leaves data and displayed count unchanged.
- [ ] D-pad focus remains usable after returning to the Profile Settings pane.
- [ ] Physical Android TV acceptance recorded in Project F; deferred checks are not waived.

## Governance

Keep the D03 PR Draft/open/unmerged under D-013. Do not merge into the integration branch during Project D execution.
