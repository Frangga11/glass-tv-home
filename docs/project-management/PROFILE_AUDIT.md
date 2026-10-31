# Profile Tab and Parental Controls Audit

**Audit date:** 31 October 2026  
**Status:** SECURITY FIXES IMPLEMENTED IN DRAFT PR #8; CI/AVATAR INTEGRATION/DEVICE ACCEPTANCE STILL OPEN  
**Baseline:** `feature/settings-reorganization-standby-gallery`  
**Profile/Child Mode candidate:** PR #7 head `62b1e7ce63f34095b44afbde7fc80e19addc9b00`  
**Avatar QR candidate:** PR #6 head `0cb1b96e2c2529e65795231519c6e0728b46390d`

## 1. User-approved requirements

- Active avatar must be identical in the top bar, Profile settings card, and profile switcher.
- Each profile owns its own photo; changing one profile must not change another.
- Reset to built-in avatar deletes only that profile's custom photo.
- Child Mode, app locks, and hidden-app lists are per profile.
- Parent PIN remains one device-wide/global PIN.
- Any switch away from an active Child Mode profile requires the global parent PIN, including child-to-child switches, to prevent switching into a less restrictive child profile.
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
| P-002 | P1 | App lock / hide list editor lacked parent-PIN verification | PR #8 adds parent-PIN verification before either editor opens; failed/cancelled verification does not expose the editor. | Verify wrong/cancelled PIN and all editor entry points on device. |
| P-003 | P1 | Profile deletion lacked parent-PIN authorization | PR #8 adds global parent-PIN verification before the destructive confirmation; last-profile protection remains in `ProfileManager`. | Verify wrong/cancelled PIN, confirmation cancel, and last-profile protection on device. |
| P-004 | P1 | Default parent PIN remains `0000` | `getPin()` falls back to `0000`. User explicitly accepts this temporarily and will change it later; forced change is outside this implementation. | User will change PIN later; keep PIN global and do not force setup in this scope. |
| P-005 | P2 | PIN retry throttling is intentionally absent | User explicitly rejects attempt limits, cooldowns, and lockout; incorrect entries continue to allow immediate retry. | No change requested; preserve unlimited attempts. |
| P-006 | P2 | Profile deletion clears per-profile Child Mode keys but avatar cleanup is not yet proven in PR #7 | PR #7 ProfileManager currently has no custom-avatar path; PR #6 contains private avatar-file cleanup on delete/reset. | During avatar integration, verify deletion removes only that profile's avatar and never another profile's image. |
| P-007 | P2 | Profile-specific controls lacked focused regression tests | PR #8 adds JVM tests for child-profile switch authorization and profile-key isolation; migration, persistence, UI gates, and deletion cleanup still need more coverage. | Extend regression coverage during avatar integration and verify on device. |
| P-008 | P2 | Remote navigation and persistence remain unverified on target hardware | No actual Mi TV Stick/phone acceptance evidence exists. | Run the device checklist in section 5 after an integrated candidate APK is produced. |

## 4. What is implemented in PR #7 and PR #8

PR #8 (`feature/profile-security-integration`, draft) adds:
- Parent PIN verification before either locked-app or hidden-app editor opens.
- Parent PIN verification before profile deletion; a separate confirmation remains after successful PIN verification.
- One shared profile-switch authorization method used by the top-bar switcher and Profile settings list.
- Any switch away from an active Child Mode profile requires the parent PIN, even when the destination is another child profile. This deliberately prevents a child from escaping a stricter policy by switching to a different child profile.
- JVM regression tests for switch authorization and profile preference-key separation.
- The default PIN remains `0000` by explicit user decision; no failed-attempt throttling is implemented by explicit user decision.

### Baseline behavior retained from PR #7

- Child Mode state, locked-app set, and hidden-app set are stored with profile-scoped keys.
- Parent PIN remains a single global preference key.
- One-time migration copies legacy device-wide state to the profile active at first migration.
- Profile deletion asks for confirmation, refuses deletion of the last profile, and clears that profile's Child Mode settings.
- Parental controls are grouped under Profile settings instead of a separate settings tab.

These are source findings only; they are not physical-device acceptance.

## 5. Required device acceptance after code integration

1. Create two adult profiles and two child profiles; set different avatar photos and verify identity on top bar, Profile card, and switcher.
2. Switch repeatedly among profiles; verify child restriction sets do not leak between profiles.
3. In child profile A, lock and hide different apps from child profile B; confirm B is unaffected.
4. Attempt to edit lock/hide lists without PIN; access must be denied.
5. Attempt to delete a profile without PIN; access must be denied.
6. Switch child → adult and child → another child without PIN; both must be denied. Enter correct PIN; switch succeeds. Switch adult → child; child restrictions activate.
7. Confirm one global parent PIN works for all profiles and changing it updates all profile gates.
8. Verify the current `0000` PIN works for all profiles until the user changes it; do not require a forced change in this release candidate.
9. Replace avatar twice; old file is removed only after successful profile-reference commit. Simulate invalid/interrupted upload; old photo remains.
10. Reset one profile to built-in; only its custom photo is deleted.
11. Delete one profile; only its avatar and Child Mode settings are cleaned.
12. Test portrait, landscape, EXIF-rotated, large, corrupt, unsupported image files.
13. Test D-pad focus, OK, Back, cancel, dialog dismissal, and return focus for every Profile and PIN dialog.
14. Force-stop/restart launcher and reboot TV; verify active profile, avatar, child mode, lock/hide sets, and PIN persist.
15. Verify wallpaper, standby, and avatar QR upload endpoints and storage directories remain isolated.

## 6. Verification and release boundary

- PR #7 exact-head CI #195 passed on `62b1e7ce63f34095b44afbde7fc80e19addc9b00`.
- PR #8 CI run #196 (`41216406132`) passed both jobs on its tested head `f0971b17ed752a9ac0bf16afa904990723c0c616`.
- Final UI cleanup commit `1ba744efee61fde662493cebd382743a5927aefa` is covered by CI run #197 (`41216508402`), which was in progress at last check. Only #197 can certify the latest PR head.
- PR #6 CI run #156 passed on its own head `0cb1b96e2c2529e65795231519c6e0728b46390d`; this does not validate the combined code.
- No APK has been installed on the user's Mi TV Stick as part of this audit.
- Stock launcher has not been changed.
- PR #7 remains Draft and unmerged.

**Disposition: NOT FINAL.** P-002 and P-003 have source-level fixes in PR #8; P-004 remains unchanged by explicit user choice; P-005 remains unlimited by explicit user choice. Avatar PR #6 is still not integrated. CI #196 passed on the previous PR #8 head; CI #197 is running on the latest head. Physical TV/phone acceptance and avatar PR #6 integration remain required before closure.
