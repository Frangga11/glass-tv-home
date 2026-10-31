# 05 — Project Handover

**Version:** 1.1  
**Date:** 31 October 2026  
**Current phase:** Wallpaper audit W-001. QR route security passed its dedicated CI; the new built-in Dempo collection category is in source; the JPEG binary is not yet committed; CI #160 is in progress.

## 1. Resume protocol

Read `MASTER_PROJECT.md` and `DOCUMENTATION_WORKFLOW.md`, then use this handover as the current resume point. Do not repeat tests whose exact commit and result are already recorded.

## 2. Authoritative branches and reviews

- Repository: https://github.com/Frangga11/glass-tv-home
- Documentation: `docs/project-master-inventory`
- Code baseline / rollback branch: `feature/settings-reorganization-standby-gallery`
- Wallpaper branch: `fix/wallpaper-qr-session-security`
- Wallpaper draft PR #7: https://github.com/Frangga11/glass-tv-home/pull/7
- Avatar draft PR #6: https://github.com/Frangga11/glass-tv-home/pull/6

## 3. Verified position

- User supplied a photo of the actual TV showing the approved Kabut Pagi Dempo wallpaper with the Glass TV Home UI. The shown composition and legibility are accepted; do not repeat this visual check.
- Wallpaper QR session security was implemented in PR #7 and CI run 41200811301 passed before the latest category change.
- Source commit `5829212cfae018aeca25b07e7f9e35ec8955122b` adds the `Kabut Pagi di Dempo` built-in offline collection directly after Default and before Nature & Landscapes.
- Follow-up commit `586d561b806869b4433ccf56c464f9c39332df2c` restores the selected Dempo category on launcher startup using bundled assets.
- The new category reads `assets/wallpapers/dempo/`, copies images into app-private storage, and uses offline wallpaper rotation.
- CI #159 (41202506621) passed for the category implementation before the startup-restoration follow-up.
- CI #160 (41202626180) for the latest source commit was in progress at last check: https://github.com/Frangga11/glass-tv-home/actions/runs/41202626180
- Blocking: the JPEG is not yet committed. Required path: `launcher/app/src/main/assets/wallpapers/dempo/kabut-pagi-dempo.jpg`.
- Original image bytes are available as `kabut-pagi-dempo-original.jpg`, JPEG 1536×864 RGB, SHA-256 `fc2a06324e4dd98ac6ab2abdaaf45cc7f34ff899a08f8abaea575c8e2ae5773`.
- No APK containing the new category image has been built or inspected; clean-install/offline test remains open.

## 4. Next action

Upload the original JPEG to the required repository path. The GitHub connection in this session supports text-file writes and Git object operations, but has no direct local-binary upload handoff. Do not record the image as committed until the repository confirms it. Once uploaded: verify path/dimensions/hash, let CI run once, inspect the APK asset entry, then perform one clean-install/offline/category-order/rotation test.

## 5. Safety and merge gate

Keep PR #6 and PR #7 in draft. Do not merge or make ADB/default-launcher changes until device acceptance and rollback are confirmed. CI is not a device pass.