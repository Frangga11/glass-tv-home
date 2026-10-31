# Wallpaper & Background Audit

**Audit ID:** W-001  
**Last checked:** 31 October 2026  
**Status:** NOT FINAL — bundled Default asset committed and CI #172 is running for the cleanup pass; device acceptance remains pending.  
**Code branch:** `fix/wallpaper-qr-session-security`  
**Draft PR:** https://github.com/Frangga11/glass-tv-home/pull/7

## 1. Approved scope

Kabut Pagi Dempo is the single bundled image **inside the existing Default wallpaper tab**. Do not add a Dempo tab, category, collection, or separate collection loader. Retired category values are normalized to the supported catalog and fall back to Default; no Malaysia-specific branch remains in the active source.

## 2. Asset verification

- Repository path: `launcher/app/src/main/assets/kabut-pagi-dempo.jpg`
- GitHub blob SHA: `758f4225581f8c01e62d26c8d73c9aec43adb0df`
- Size: 230,883 bytes.
- GitHub directory listing confirms this is the only file in `launcher/app/src/main/assets/`.
- Temporary `.gitkeep` marker was removed after upload.
- Source loader calls `getAssets().open("kabut-pagi-dempo.jpg")` and refreshes the private runtime cache from the packaged asset.
- The APK asset has been committed, but exact contents inside the release APK still require artifact inspection.

## 3. Code and CI

- Default-only source changes are in PR #7. The accidental extra Dempo category/collection was removed.
- `getBundledDefaultWallpaper()` refreshes the private cache through a staged temporary file and replacement, avoiding reuse of obsolete wallpaper cache on upgrade.
- Unknown or retired active category values migrate to `Default`; supported categories remain Default, Natural, Skyscraper, Space, Ocean, Minimalist, and Custom. This preserves upgrade safety without retaining a Malaysia-specific code path.
- CI run `41205556026`: Android unit tests and release build completed successfully; the job failed only because the APK size guard was 1.2 MiB. Measured APK: approximately 1,387 KiB. The lists/tools/docs job passed.
- CI workflow was updated to a 1.5 MiB ceiling, reflecting the intentional bundled 230,883-byte wallpaper. This is a size-budget adjustment, not evidence that the next CI run has passed.
- PR #7 remains Draft and unmerged.

- Cleanup pass on 31 October 2026: removed the two Malaysia-market streaming package IDs (`my.com.tonton.screen`, `my.gov.rtm.tv`) from the default dock configuration; retained the dock mechanism and remaining configured apps. MainActivity and the inspected wallpaper/store source files contain no Malaysia/Kuala Lumpur/Petronas references. CI #172 is running for this change.

## 4. Acceptance status

| Check | Status | Evidence / remaining work |
|---|---|---|
| Existing Default tab only | PASS | Source audit; no Dempo category/collection |
| Malaysia-to-Default migration | PASS in source | Upgrade test still required |
| Asset committed at expected path | PASS | GitHub file and directory listing |
| Duplicate asset in assets folder | PASS | One file listed |
| Build and unit tests | BUILD PASS | Prior run failed only on obsolete APK size guard |
| Updated CI size guard | PENDING | Await new workflow result |
| Asset embedded in release APK | PENDING | Inspect APK ZIP entry and hash |
| Fresh install / offline startup | PENDING | Test on LG TV + Mi TV Stick |
| Upgrade replacing stale cache | PENDING | Test with previous Malaysia/default cache |
| QR security and upload session | SOURCE IMPLEMENTED | Device and negative-path tests pending |
| Long-run rendering/performance | PENDING | Device test required |

## 5. Remaining work — in order

1. Confirm CI passes with the updated 1.5 MiB size ceiling.
2. Download the resulting APK artifact for CI #172 and inspect its ZIP contents. Confirm exactly one `assets/kabut-pagi-dempo.jpg`, with the expected dimensions/content; check no Dempo collection folder or duplicate asset.
3. Install fresh on the TV, disconnect internet, launch, and verify Default displays Kabut Pagi Dempo.
4. Upgrade an installation with a legacy Malaysia selection and stale wallpaper cache; verify it migrates to Default and refreshes to Dempo.
5. Test remote navigation, wallpaper rendering, and the QR upload security/session-close cases.
6. Keep PR #7 Draft until all required device checks pass.

## 6. Final disposition

**NOT FINAL / NOT READY FOR MERGE.** The image is now in the correct repository location and the source points to it. A successful Gradle build has been observed, but the previous CI run failed its size threshold. The updated CI result, APK-content inspection, and physical TV tests are still required.
