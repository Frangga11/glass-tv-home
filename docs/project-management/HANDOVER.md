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
- CI run #166 (`41205741776`) passed both jobs. Release artifact `glass-tv-home-pagar-alam-release` was inspected: APK 1,420,713 bytes; exactly one wallpaper image entry `assets/kabut-pagi-dempo.jpg` (230,883 bytes); embedded JPEG SHA-256 `04e5793b92cab1fcaeaaf0e3b80769598f91c7521b8e3d62c4f7a38888982982`.
- Cleanup pass 31 Oct 2026: removed `my.com.tonton.screen` and `my.gov.rtm.tv` from the default dock; remaining configured dock apps and dock behavior are preserved. Checked MainActivity, config.xml, OnlineWallpapers, PlaceWallpapers, Config, StandbyPhotoStore, and WallpaperUploadServer for Malaysia/Kuala Lumpur/Petronas remnants; none remain in those inspected source files.
- Cleanup commits: MainActivity `54bbede6c621aa8e305a9dd255978725359023b8`; dock config `6a69dded53d8328164e01e49be8ba2f7c2da8ae5`. CI #172 (`41207661449`) was still running at documentation update; lists/tools/docs job passed and launcher build/unit-test job was in progress.
- PR remains Draft and unmerged.

## Next required gates

1. Verify CI with the updated size limit.
2. Inspect release APK ZIP entries for exactly one `assets/kabut-pagi-dempo.jpg` and no duplicate Dempo collection asset.
3. Test clean install and offline startup on the target TV.
4. Test upgrade from legacy Malaysia selection and stale cache replacement.
5. Complete QR/security and remote-control device tests.
6. Cross-audit corrected the stale Malaysia migration comment and incomplete Appearance card description in code commit `7bca68e56e51ea828c2ef7d22ca830470ca28381` (CI #167 passed).
7. Wallpaper upload and Standby photo stores remain distinct (`custom_wallpapers` vs `standby_photos`); QR sessions use separate routes. Device-level isolation tests remain pending.
8. Follow-up hardening commits `da1e43a241582c71a86e54530c730f20f225b2ef` and `91a31e757d796bd1384a962ced2054ce74badd96` prevent Default from silently falling back to a theme/legacy wallpaper and avoid reusing an unverified old cache when the packaged asset cannot be read. Commit `9f5a9411120dad879aaa1d8c1bb8cc6e90684924` clears the prior image and shows a safe black background if Default cannot load after manual selection. CI #167, #168, #169, and #170 all passed; latest CI #170 is https://github.com/Frangga11/glass-tv-home/actions/runs/41206969870.
9. Latest release artifact ID `13044766496` was downloaded and inspected. APK `app-release.apk`: 1,420,821 bytes; SHA-256 `b3ce140629ccca1f12e6677d990bb79ffa83bb65634812a6f12651f3da312560`. It contains exactly one image asset, `assets/kabut-pagi-dempo.jpg`, 230,883 bytes; SHA-256 matches the approved JPEG. No second Dempo image is present.

**Disposition: NOT FINAL. Source cleanup is committed, but CI #172 completion and physical device acceptance remain required. Do not merge or call the wallpaper device-ready until these gates pass.**
