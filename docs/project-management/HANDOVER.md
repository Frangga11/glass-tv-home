# Project Handover

**Project:** Glass TV Home  
**Documentation branch:** `docs/project-master-inventory`  
**Last updated:** 31 October 2026

## Current wallpaper work

- User-approved wallpaper: **Kabut Pagi Dempo**.
- Required placement: existing **Default** tab only. No extra Dempo tab/category/collection.
- Code branch: `fix/wallpaper-qr-session-security`.
- Draft PR #7: https://github.com/Frangga11/glass-tv-home/pull/7
- Asset committed: `launcher/app/src/main/assets/kabut-pagi-dempo.jpg` (230,883 bytes; Git blob `758f4225581f8c01e62d26c8d73c9aec43adb0df`).
- Temporary `.gitkeep` removed; Assets directory currently contains only the wallpaper JPEG.
- Loader uses the packaged asset and refreshes the private runtime cache. Unknown/retired category values migrate to Default through a supported-category check; no Malaysia-specific source branch remains.
- Latest exact-head CI run #195 (`41214569233`) passed both jobs on PR #7 head `62b1e7ce63f34095b44afbde7fc80e19addc9b00`: Android unit tests/release build/size guard/artifact publication and lists/tools/docs checks all passed. Earlier CI #172 used an earlier commit and is superseded for current-head verification.
- Latest artifact ID `13047428749` was downloaded and inspected. Artifact ZIP: 1,332,722 bytes. APK `app-release.apk`: 1,419,677 bytes; SHA-256 `9b0e581a1c710ea8139c53fb001f3d11ed47ed55c0d36dd9b10f8adce66d6ed2`. APK contains exactly one image asset: `assets/kabut-pagi-dempo.jpg` (230,883 bytes; 1536 × 864 px; embedded SHA-256 `04e5793b92cab1fcaeaaf0e3b80769598f91c7521b8e3d62c4f7a38888982982`). No second Dempo image or additional image asset under `assets/` was found.
- Cleanup pass 31 Oct 2026: removed `my.com.tonton.screen` and `my.gov.rtm.tv` from the default dock; remaining configured dock apps and dock behavior are preserved. Checked MainActivity, config.xml, OnlineWallpapers, PlaceWallpapers, Config, StandbyPhotoStore, and WallpaperUploadServer for Malaysia/Kuala Lumpur/Petronas remnants; none remain in those inspected source files.
- Cleanup commits: MainActivity `54bbede6c621aa8e305a9dd255978725359023b8`; dock config `6a69dded53d8328164e01e49be8ba2f7c2da8ae5`. Subsequent regional cleanup and profile parental-settings commits are included in the exact-head CI #195 run.
- PR remains Draft and unmerged.

## Next required gates

1. CI exact-head verification: PASS — run #195 (`41214569233`) on PR head `62b1e7ce63f34095b44afbde7fc80e19addc9b00`.
2. APK packaging inspection: PASS — artifact `13047428749`; one `assets/kabut-pagi-dempo.jpg`, 230,883 bytes, 1536 × 864 px, hash matches approved source; no duplicate image asset.
3. Test clean install and offline startup on the target TV.
4. Test upgrade from legacy Malaysia selection and stale cache replacement.
5. Complete QR/security and remote-control device tests.
6. Cross-audit corrected the stale Malaysia migration comment and incomplete Appearance card description in code commit `7bca68e56e51ea828c2ef7d22ca830470ca28381` (CI #167 passed).
7. Wallpaper upload and Standby photo stores remain distinct (`custom_wallpapers` vs `standby_photos`); QR sessions use separate routes. Device-level isolation tests remain pending.
8. Follow-up hardening commits `da1e43a241582c71a86e54530c730f20f225b2ef` and `91a31e757d796bd1384a962ced2054ce74badd96` prevent Default from silently falling back to a theme/legacy wallpaper and avoid reusing an unverified old cache when the packaged asset cannot be read. Commit `9f5a9411120dad879aaa1d8c1bb8cc6e90684924` clears the prior image and shows a safe black background if Default cannot load after manual selection. CI #167, #168, #169, and #170 all passed; latest CI #170 is https://github.com/Frangga11/glass-tv-home/actions/runs/41206969870.
9. Historical artifact `13044766496` was superseded by the exact-head artifact `13047428749` (see verification above).

**Disposition: NOT FINAL / NOT READY FOR MERGE. Exact-head CI #195 and APK asset inspection PASS. Physical Mi TV Stick + phone acceptance remains mandatory: clean install/offline cold start, legacy upgrade/cache refresh, remote navigation, QR session isolation/revocation, and wallpaper/standby/avatar separation. Keep PR #7 Draft until device evidence is recorded.**
