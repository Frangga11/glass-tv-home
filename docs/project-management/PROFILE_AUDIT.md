# Profile Tab and Parental Controls Audit

**Audit date:** 31 October 2026  
**Status:** PROFILE SECURITY + AVATAR QR SELECTIVELY INTEGRATED IN DRAFT PR #9; EXACT-HEAD CI #208 PASS; PHYSICAL DEVICE ACCEPTANCE OPEN  
**Baseline:** `feature/settings-reorganization-standby-gallery`  
**Profile/Child Mode candidate:** PR #7 head `62b1e7ce63f34095b44afbde7fc80e19addc9b00`  
**Avatar QR source:** PR #6 head `0cb1b96e2c2529e65795231519c6e0728b46390d`, selectively integrated into PR #9 (not merged directly)

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

Historical conclusion: the avatar QR implementation was **not present in PR #7** and PR #6 could not be merged blindly. That selective integration is now implemented in PR #9, preserving PR #8's profile-scoped Child Mode behavior and security gates.

## 3. Source findings

| ID | Severity | Finding | Evidence / impact | Required action |
|---|---|---|---|---|
| P-001 | P1 | Avatar QR feature was isolated in PR #6 | PR #9 selectively integrates avatar storage, QR server, top bar, settings sidebar/list, and profile switcher on top of PR #8 security branch. | Physical TV/phone validation of all surfaces and upload lifecycle. |
| P-002 | P1 | App lock / hide list editor lacked parent-PIN verification | PR #8 adds parent-PIN verification before either editor opens; failed/cancelled verification does not expose the editor. | Verify wrong/cancelled PIN and all editor entry points on device. |
| P-003 | P1 | Profile deletion lacked parent-PIN authorization | PR #8 adds global parent-PIN verification before the destructive confirmation; last-profile protection remains in `ProfileManager`. | Verify wrong/cancelled PIN, confirmation cancel, and last-profile protection on device. |
| P-004 | P1 | Default parent PIN remains `0000` | `getPin()` falls back to `0000`. User explicitly accepts this temporarily and will change it later; forced change is outside this implementation. | User will change PIN later; keep PIN global and do not force setup in this scope. |
| P-005 | P2 | PIN retry throttling is intentionally absent | User explicitly rejects attempt limits, cooldowns, and lockout; incorrect entries continue to allow immediate retry. | No change requested; preserve unlimited attempts. |
| P-006 | P2 | Profile deletion must isolate avatar and parental-data cleanup | PR #9 persists profile removal before cleaning only unreferenced files inside the private avatar directory, and clears only that profile's Child Mode/app-lock/hidden-app keys after releasing the profile lock. | Device test: delete one profile while other profiles retain photos and restrictions. |
| P-007 | P2 | Profile-specific controls and avatar policy need regression coverage | PR #8 tests switch authorization and profile-key isolation; PR #9 adds QR route/token, upload-size, image-dimension, and private-path containment tests. End-to-end persistence and UI behavior still require device evidence. | Keep extending tests if device testing exposes gaps. |
| P-008 | P2 | Remote navigation and persistence remain unverified on target hardware | No actual Mi TV Stick/phone acceptance evidence exists. | Run the device checklist in section 5 after an integrated candidate APK is produced. |

## 4. What is implemented in PR #7, PR #8, and PR #9

PR #8 (`feature/profile-security-integration`, draft) adds:
- Parent PIN verification before either locked-app or hidden-app editor opens.
- Parent PIN verification before profile deletion; a separate confirmation remains after successful PIN verification.
- One shared profile-switch authorization method used by the top-bar switcher and Profile settings list.
- Any switch away from an active Child Mode profile requires the parent PIN, even when the destination is another child profile. This deliberately prevents a child from escaping a stricter policy by switching to a different child profile.
- JVM regression tests for switch authorization and profile preference-key separation.
- The default PIN remains `0000` by explicit user decision; no failed-attempt throttling is implemented by explicit user decision.

### PR #9 avatar integration

PR #9 (`feature/profile-avatar-security-integration`, Draft) selectively integrates the avatar source from PR #6:
- Private per-profile avatar file storage; path containment checks and orphan cleanup.
- QR server bound to selected LAN IPv4, random session token, 5-minute expiry, one successful upload, max 15 MB, max 80 MP / 12,000 px, bounded client count, socket revocation, image decode validation, and file fsync before profile-reference commit.
- Profile reference is committed before old unreferenced photo cleanup; upload failure preserves old photo.
- Avatar rendering is consistent across top bar, settings sidebar, active profile row, and profile switcher; EXIF orientation is handled.
- Built-in selection and reset update all visible surfaces without resetting TV focus.
- Profile deletion commits the profile list before avatar cleanup and clears that profile's Child Mode/app-lock/hidden-app preferences without clearing the global PIN.
- JVM tests cover exact QR routes, upload-size/dimension boundaries, and private avatar path containment.

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
- PR #8 exact head `1ba744efee61fde662493cebd382743a5927aefa` passed CI #197 (`41216508402`).
- PR #9 exact head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc` passed CI #208 (`41218171447`): both jobs succeeded, including release unit tests/build, APK size guard, artifact publication, and lists/tools/docs checks.
- Release artifact `13048321564` was published: APK 1,420,005 bytes; SHA-256 `735be1ff6e8ff88d31bf9de73940e527a570bbaace96c5ec9a001ce95610a44e`.
- PR #6 CI run #156 passed on its isolated head; this does not validate the combined code. PR #9 CI #208 now validates the combined profile-security + avatar source snapshot.
- No APK has been installed on the user's Mi TV Stick as part of this audit.
- Stock launcher has not been changed.
- PR #7 remains Draft and unmerged.

**Disposition: SOURCE/CI GATES PASS; NOT RELEASE-FINAL.** P-002/P-003 are fixed in PR #8; P-004 remains `0000` temporarily by explicit user choice; P-005 remains unlimited by explicit user choice. Avatar QR is selectively integrated in PR #9 and CI #208 passed on its exact head. PR #8 and PR #9 remain Draft/unmerged. Physical Mi TV Stick + phone acceptance remains mandatory before release closure.


## PR #9 latest integration acceptance — 31 October 2026

- Draft PR #9: https://github.com/Frangga11/glass-tv-home/pull/9; base PR #8 branch `feature/profile-security-integration`; head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`.
- CI #208: https://github.com/Frangga11/glass-tv-home/actions/runs/41218171447 — SUCCESS on exact head; both jobs passed.
- Artifact `13048951728`: `app-release.apk`, 1,427,681 bytes; SHA-256 `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`; below 1.5 MiB CI budget.
- Cross-source call-site audit: no additional direct profile-switch, app-lock/hide mutation, profile deletion, or avatar-rendering entry points were found outside the audited core classes.
- Device gate remains open: QR scan/upload/replacement/reset/expiry/cancel, bad/oversize/corrupt image, reboot persistence, profile isolation, D-pad focus, and wallpaper/StandBy/avatar route separation on the actual Mi TV Stick + phone.
- No PR has been merged and no APK has been installed on the TV.
