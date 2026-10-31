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
- Loader uses the packaged asset and refreshes the private runtime cache. Legacy Malaysia selections migrate to Default.
- CI run #166 (`41205741776`) passed both jobs. Release artifact `glass-tv-home-pagar-alam-release` was inspected: APK 1,420,713 bytes; exactly one wallpaper image entry `assets/kabut-pagi-dempo.jpg` (230,883 bytes); embedded JPEG SHA-256 `04e5793b92cab1fcaeaaf0e3b80769598f91c7521b8e3d62c4f7a38888982982`.
- PR remains Draft and unmerged.

## Next required gates

1. Verify CI with the updated size limit.
2. Inspect release APK ZIP entries for exactly one `assets/kabut-pagi-dempo.jpg` and no duplicate Dempo collection asset.
3. Test clean install and offline startup on the target TV.
4. Test upgrade from legacy Malaysia selection and stale cache replacement.
5. Complete QR/security and remote-control device tests.
6. Cross-audit corrected the stale Malaysia migration comment and incomplete Appearance card description in code commit `7bca68e56e51ea828c2ef7d22ca830470ca28381` (CI #167 passed).
7. Wallpaper upload and Standby photo stores remain distinct (`custom_wallpapers` vs `standby_photos`); QR sessions use separate routes. Device-level isolation tests remain pending.
8. Follow-up hardening commits `da1e43a241582c71a86e54530c730f20f225b2ef` and `91a31e757d796bd1384a962ced2054ce74badd96` prevent Default from silently falling back to a theme/legacy wallpaper and avoid reusing an unverified old cache when the packaged asset cannot be read. A further commit `9f5a9411120dad879aaa1d8c1bb8cc6e90684924` clears the prior image and shows a safe black background if Default cannot load after manual selection. CI #168 passed; CI #169 covers the cache-loader hardening. The final manual-selection fallback commit still needs its own CI result.

**Disposition: NOT FINAL. Do not merge or call the wallpaper device-ready until these gates pass.**
