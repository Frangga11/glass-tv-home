# 05 — Project Handover

**Version:** 1.2  
**Date:** 31 October 2026  
**Current phase:** Wallpaper audit W-001. Correct scope is the existing Default tab only; no extra Dempo tab or collection. The JPEG binary is still not committed; CI #162 is in progress.

## 1. Resume protocol

Read `MASTER_PROJECT.md` and `DOCUMENTATION_WORKFLOW.md`, then use this handover as the current resume point. Do not repeat tests whose exact commit and result are already recorded.

## 2. Authoritative branches and reviews

- Repository: https://github.com/Frangga11/glass-tv-home
- Documentation: `docs/project-master-inventory`
- Code baseline: `feature/settings-reorganization-standby-gallery`
- Wallpaper branch: `fix/wallpaper-qr-session-security`
- Wallpaper draft PR #7: https://github.com/Frangga11/glass-tv-home/pull/7
- Avatar draft PR #6: https://github.com/Frangga11/glass-tv-home/pull/6

## 3. Verified position

- User's actual-TV photo is accepted as visual proof of the selected Dempo wallpaper composition and legibility. Do not repeat this visual check.
- Existing `getActiveWallpaperCategory()` migrates legacy category values beginning with `Malaysia` to `Default`.
- Existing `showWallpaperDialog()` has one Default option. The category list remains unchanged.
- Startup and manual Default selection use `getBundledDefaultWallpaper()` and a singleton wallpaper list.
- The mistaken extra Dempo category and collection loader were removed in commit `530c2dcc431d5ce42806f254e9a9cc468fc7f9f7`.
- Commit `fbab186c3253ad6c37ed06d206b5597b660230a8` refreshes the existing private Default runtime copy from the APK asset on launch, preventing a stale cached Malaysia/older image from persisting after upgrade.
- Single source asset destination: `launcher/app/src/main/assets/kabut-pagi-dempo.jpg` (runtime asset name `kabut-pagi-dempo.jpg`).
- Expected storage: one packaged JPEG plus one private runtime cache copy required by the current File-based decoder. No additional category folder or duplicate APK image.
- The JPEG is not yet committed. Current CI #162 (41203203377) for source commit `fbab186c3253ad6c37ed06d206b5597b660230a8` was in progress at last check: https://github.com/Frangga11/glass-tv-home/actions/runs/41203203377
- No new APK containing the Dempo JPEG has been built or inspected.

## 4. Next action

Add the approved JPEG at `launcher/app/src/main/assets/kabut-pagi-dempo.jpg`. Then run CI once, inspect the APK for exactly that one asset, and test: clean install/offline start; upgrade from a legacy Malaysia selection; replacement of any stale cached Default image; actual TV display. Keep the supplied photo as the already-passed visual evidence.

## 5. Safety and merge gate

Keep PR #7 and PR #6 draft. Do not merge or make ADB/default-launcher changes until the asset is packaged and the remaining device acceptance and rollback plan are confirmed. CI is not a device pass.