# Profile Tab and Parental Controls Audit

**Audit date:** 31 October 2026  
**Status:** SOURCE AUDIT COMPLETE FOR CURRENT SNAPSHOTS; NOT RELEASE-READY  
**Baseline:** `feature/settings-reorganization-standby-gallery`  
**Profile/Child Mode candidate:** PR #7 head `62b1e7ce63f34095b44afbde7fc80e19addc9b00`  
**Avatar QR candidate:** PR #6 head `0cb1b96e2c2529e65795231519c6e0728b46390d`

## 1. User-approved requirements

- Active avatar must be identical in the top bar, Profile settings card, and profile switcher.
- Each profile owns its own photo; changing one profile must not change another.
- Reset to built-in avatar deletes only that profile's custom photo.
- Child Mode, app locks, and hidden-app lists are per profile.
- Parent PIN remains one device-wide/global PIN.
- Switching from a child-restricted profile to an unrestricted/adult profile requires the global parent PIN.
- Profile/avatar state must persist after launcher process restart and TV reboot.
- All profile, PIN, and dialog interactions must be usable by D-pad/remote.
- Wallpaper, standby, and avatar image stores/routes must remain separate.

## 2. Branch reconciliation — critical

GitHub compare results:
- Avatar PR #6 branch is 28 commits ahead of the settings/gallery base, 0 behind.
- PR #7 branch is 43 commits ahead of the same base, 0 behind.
- Comparing PR #6 head to PR #7 head reports **diverged**: PR #7 is 43 commits ahead and 28 commits behind PR #6; merge base is the common settings/gallery base.
- Both branches modify `MainActivity.java`, `ModernSettingsDialog.java`, `ProfileManager.java`, and `UserSwitchDialog.java`.

Conclusion: the avatar QR implementation is **not present as an integrated feature in PR #7**. PR #6 cannot be merged blindly into PR #7. A deliberate three-way integration is required, preserving PR #7's profile-specific Child Mode behavior and PR #6's custom-avatar lifecycle/QR server.

## 3. Source findings

| ID | Severity | Finding | Evidence / impact | Required action |
|---|---|---|---|---|
| P-001 | P1 | Avatar QR feature is isolated in PR #6, not integrated into PR #7 | PR #7 ProfileManager currently exposes built-in avatar model only; its Profile settings card and switcher use built-in avatar rendering. | Integrate avatar implementation selectively into PR #7; preserve per-profile resolver across top bar, Profile card, and switcher. |
| P-002 | P1 | App lock / hide list editor does not verify parent PIN before changing restrictions | `showAppLockPicker()` directly writes `setAppLocked` / `setAppHidden` from row clicks. No `promptPin` gate is present at entry or commit. A user in a child session who can open Settings can edit the restriction lists. | Require global parent PIN before opening or saving either editor. Re-render/cancel safely on failed or cancelled PIN. |
| P-003 | P1 | Profile deletion has confirmation but no parent-PIN authorization | PR #7's delete menu opens a destructive confirmation then calls `ProfileManager.deleteProfile()` directly. | Require global parent PIN before destructive deletion (at minimum while a child profile is active; recommended for every profile deletion). Preserve last-profile protection and cleanup. |
| P-004 | P1 | Default parent PIN is `0000`; enabling Child Mode does not force first-time PIN setup | `getPin()` falls back to `0000`; UI explicitly displays `0000 [Lalai]`. Anyone who knows/defaults to this can pass PIN gates until changed. | Add first-run PIN setup/mandatory change before Child Mode can be activated or protected actions used. Never disclose the PIN in the settings summary. |
| P-005 | P2 | PIN retry throttling is absent in the inspected keypad flow | Wrong attempts clear the input and allow immediate retries; no delay/lockout is present. | Add a bounded cooldown/backoff and test Back/cancel behavior; avoid permanent lockout that could strand the owner. |
| P-006 | P2 | Profile deletion clears per-profile Child Mode keys but avatar cleanup is not yet proven in PR #7 | PR #7 ProfileManager currently has no custom-avatar path; PR #6 contains private avatar-file cleanup on delete/reset. | During avatar integration, verify deletion removes only that profile's avatar and never another profile's image. |
| P-007 | P2 | Profile-specific Child Mode has no focused regression tests in PR #7 | Current PR #7 changed-file test additions cover regional cleanup and wallpaper QR routes; no dedicated tests cover profile migration/isolation, switch authorization, or deletion cleanup. | Add tests for per-profile flags/lists, one-time legacy migration, global PIN, adult-switch gate, and deletion cleanup. |
| P-008 | P2 | Remote navigation and persistence remain unverified on target hardware | No actual Mi TV Stick/phone acceptance evidence exists. | Run the device checklist in section 5 after an integrated candidate APK is produced. |

## 4. What is implemented in PR #7

- Child Mode state, locked-app set, and hidden-app set are stored with profile-scoped keys.
- Parent PIN remains a single global preference key.
- One-time migration copies legacy device-wide state to the profile active at first migration.
- Switching from current Child Mode to a target profile without Child Mode prompts for the parent PIN.
- Profile deletion asks for confirmation, refuses deletion of the last profile, and clears that profile's Child Mode settings.
- Parental controls are grouped under Profile settings instead of a separate settings tab.

These are source findings only; they are not physical-device acceptance.

## 5. Required device acceptance after code integration

1. Create two adult profiles and two child profiles; set different avatar photos and verify identity on top bar, Profile card, and switcher.
2. Switch repeatedly among profiles; verify child restriction sets do not leak between profiles.
3. In child profile A, lock and hide different apps from child profile B; confirm B is unaffected.
4. Attempt to edit lock/hide lists without PIN; access must be denied.
5. Attempt to delete a profile without PIN; access must be denied.
6. Switch child → adult without PIN; must be denied. Enter correct PIN; switch succeeds. Switch adult → child; child restrictions activate.
7. Confirm one global parent PIN works for all profiles and changing it updates all profile gates.
8. Verify first-run default PIN cannot remain silently at 0000 when enabling parental controls.
9. Replace avatar twice; old file is removed only after successful profile-reference commit. Simulate invalid/interrupted upload; old photo remains.
10. Reset one profile to built-in; only its custom photo is deleted.
11. Delete one profile; only its avatar and Child Mode settings are cleaned.
12. Test portrait, landscape, EXIF-rotated, large, corrupt, unsupported image files.
13. Test D-pad focus, OK, Back, cancel, dialog dismissal, and return focus for every Profile and PIN dialog.
14. Force-stop/restart launcher and reboot TV; verify active profile, avatar, child mode, lock/hide sets, and PIN persist.
15. Verify wallpaper, standby, and avatar QR upload endpoints and storage directories remain isolated.

## 6. Verification and release boundary

- PR #7 exact-head CI #195 passed on `62b1e7ce63f34095b44afbde7fc80e19addc9b00`; this validates the PR #7 source snapshot only, not avatar integration or physical-device behavior.
- PR #6 CI run #156 passed on its own head `0cb1b96e2c2529e65795231519c6e0728b46390d`; this does not validate the combined code.
- No APK has been installed on the user's Mi TV Stick as part of this audit.
- Stock launcher has not been changed.
- PR #7 remains Draft and unmerged.

**Disposition: NOT FINAL.** The source audit identified P1 authorization gaps (P-002, P-003, P-004) and an unintegrated avatar implementation (P-001). Fix and test these before treating the Profile tab as complete. Physical TV/phone acceptance remains mandatory.
