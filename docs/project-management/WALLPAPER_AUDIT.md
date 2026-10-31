# Wallpaper & Background Audit

**Audit ID:** W-001  
**Last checked:** 31 October 2026  
**Status:** NOT FINAL — exact-head CI #195 and release APK asset inspection PASS; physical device acceptance remains pending.  
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
- Earlier CI run `41205556026`: Android unit tests and release build completed successfully; the job failed only because the APK size guard was 1.2 MiB. The workflow ceiling was subsequently adjusted to 1.5 MiB.
- CI workflow was updated to a 1.5 MiB ceiling, reflecting the intentional bundled 230,883-byte wallpaper. Exact-head CI #195 later passed this guard.
- PR #7 remains Draft and unmerged.

- Cleanup pass on 31 October 2026: removed the two Malaysia-market streaming package IDs (`my.com.tonton.screen`, `my.gov.rtm.tv`) from the default dock configuration; retained the dock mechanism and remaining configured apps. MainActivity and the inspected wallpaper/store source files contain no Malaysia/Kuala Lumpur/Petronas references.

## 4. Acceptance status

| Check | Status | Evidence / remaining work |
|---|---|---|
| Existing Default tab only | PASS | Source audit; no Dempo category/collection |
| Malaysia-to-Default migration | PASS in source | Upgrade test still required |
| Asset committed at expected path | PASS | GitHub file and directory listing |
| Duplicate asset in assets folder | PASS | One file listed |
| Build and unit tests | BUILD PASS | Prior run failed only on obsolete APK size guard |
| Updated CI size guard | PASS | Exact-head CI #195 (`41214569233`) on `62b1e7ce63f34095b44afbde7fc80e19addc9b00` |
| Asset embedded in release APK | PASS | Artifact `13047428749`; APK 1,419,677 bytes; SHA-256 `9b0e581a1c710ea8139c53fb001f3d11ed47ed55c0d36dd9b10f8adce66d6ed2`; exactly one image asset `assets/kabut-pagi-dempo.jpg` (230,883 bytes, 1536 × 864 px); embedded hash matches approved JPEG |
| Fresh install / offline startup | PENDING | Test on LG TV + Mi TV Stick |
| Upgrade replacing stale cache | PENDING | Test with previous Malaysia/default cache |
| QR security and upload session | SOURCE IMPLEMENTED | Device and negative-path tests pending |
| Long-run rendering/performance | PENDING | Device test required |

## 5. Remaining work — in order

1. CI exact-head verification: PASS — run #195 (`41214569233`) on PR #7 head `62b1e7ce63f34095b44afbde7fc80e19addc9b00`.
2. APK artifact inspection: PASS — artifact `13047428749`; APK contains exactly one image asset `assets/kabut-pagi-dempo.jpg`, no duplicate image asset, and embedded JPEG hash matches the approved image.
3. Install fresh on the TV, disconnect internet, launch, and verify Default displays Kabut Pagi Dempo.
4. Upgrade an installation with a legacy Malaysia selection and stale wallpaper cache; verify it migrates to Default and refreshes to Dempo.
5. Test remote navigation, wallpaper rendering, QR foreign-token/legacy-route rejection, expiry, and dialog-close revocation.
6. Verify wallpaper, standby, and avatar QR/data stores remain isolated.
7. Keep PR #7 Draft until all required device checks pass.

## 6. Final disposition

**NOT FINAL / NOT READY FOR MERGE.** Exact-head CI #195 and APK-content inspection have passed. Physical TV/phone acceptance, including offline, upgrade/cache, remote, and QR-security tests, remains required.
