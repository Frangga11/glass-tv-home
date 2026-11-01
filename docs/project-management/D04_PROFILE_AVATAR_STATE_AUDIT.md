# D04 Profile/Avatar State Synchronization Audit

Date: 1 November 2026  
Status: source repair and initial CI passed; final report commit and exact-head CI rerun pending  
Parent: D03 exact head `c119198e5c5ecc0d3cfdc283049ea3299d0067ca`

## Scope

Audit the profile and avatar state paths across the Home top bar, Settings sidebar profile card, Profile Settings editor, Settings profile list, and top-bar profile switcher. Trace profile switching, profile rename, built-in avatar selection, custom-photo QR upload, default-avatar restoration, profile creation, and profile deletion.

D04 is limited to keeping the displayed profile/avatar and selection state coherent. It does not redesign profile storage, change preference keys, alter avatar upload/session behavior, or introduce a global observer/event bus.

## Ownership and refresh map

- `ProfileManager` owns the profile catalogue, active-profile pointer, built-in avatar index, and profile-scoped custom avatar path.
- Custom avatar bytes remain in the private `files/profile_avatars` directory; `AvatarUploadServer` commits a validated photo through `ProfileManager.replaceCustomAvatar()`.
- Home top-bar avatar/name is rendered by `MainActivity.updateTopBarUserIcon()`; profile switching and avatar/name changes explicitly refresh it. Tile reload also refreshes the top-bar icon.
- Settings sidebar profile card is refreshed through `ModernSettingsDialog.updateSidebarSelection`; name changes, profile switching, add/delete completion and sidebar focus/rerender paths refresh it.
- Profile Settings shows the active avatar in its detail image and active-profile row. Built-in selection, custom upload, and default restoration update those images.
- Top-bar profile switcher renders every profile from the current profile records when opened; profile changes dismiss or rebuild the modal through the existing flow.
- Profile switching updates the active-profile pointer, reloads home tiles, updates the top-bar icon, and invokes the Settings completion callback when initiated from Settings.
- Profile creation and deletion use explicit completion callbacks to refresh the Settings pane; deleting the active profile falls back to a remaining profile and refreshes the top bar.

No missing refresh was found in these mutation-to-surface paths that would justify a global preference listener or event bus.

## Confirmed finding D04-F01 — custom photo vs built-in selection indicator

The Profile Settings avatar row always showed the stored built-in `avatarIndex` ring, even when a custom photo was the effective avatar. The rendered avatar images correctly preferred the custom photo, but the selection ring continued to mark an animal avatar as active. The same mismatch remained after QR upload because the callback refreshed the images but not the ring state. This made the editor present two conflicting avatar-selection states.

### Repair

- Added the pure policy helper `ProfileManager.isBuiltInAvatarSelected(profile, index)`. A built-in avatar is selected only when the profile has no custom-photo override and the stored built-in index matches.
- Initial ring visibility now follows the effective avatar state.
- Successful custom-photo QR upload hides all built-in selection rings while retaining the stored built-in index as fallback.
- Restoring the default avatar re-evaluates and restores the ring for the stored built-in index.
- Selecting a built-in avatar continues to clear the custom-photo reference first, then refreshes the ring using the same policy.
- Added JVM regression tests for custom-photo override and fallback restoration.

## Scope protection

- No profile JSON schema, preference key, active-profile ownership, legacy mirror, migration, custom-avatar file path policy, or upload protocol changed.
- No change to Child Mode/PIN policy, profile switching/deletion authorization, C07 upload/session hardening, or D01–D03 repairs.
- No broad refactor or global observer/event bus introduced.

## Verification

- Source review and targeted tests are included on the D04 branch.
- CI run #388 (41308871048) completed successfully on the PR merge ref containing D04 branch head `8a082f7bcee411019c658f8024f3c7cdfd49c2c7`.
- Both required jobs passed: `Launcher build and unit tests`; `Lists, tools and docs`.
- Release unit tests passed for both app and installer modules; `assembleRelease` completed successfully.
- APK exact size: **1,550,805 bytes** against the strict **1,572,864-byte** limit; remaining headroom: **21,059 bytes**. APK size gate passed.
- Manual APK publication was skipped as designed.
- This CI result verifies the D04 source branch as incorporated into the PR merge ref. Because this report update creates a new branch commit, a fresh CI run on the final updated head is required before closing D04 source/CI gates.
- Physical Android TV acceptance remains required in Project F: switch profiles, rename, select built-in avatar, upload custom photo, restore default, verify Home top bar, Settings sidebar/detail/list, and profile switcher; verify no cross-profile avatar leakage.
- No APK publication/installation or physical-device test is claimed.

## Governance

Branch: `feature/d04-profile-avatar-state-sync`  
Base: D03 exact head `c119198e5c5ecc0d3cfdc283049ea3299d0067ca`  
PR remains Draft/open/unmerged under D-013. Grouped integration only after A–G source work is complete and approved.
