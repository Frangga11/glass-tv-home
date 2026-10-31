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
- CI run `41205556026` built the release APK and passed Gradle tasks, but failed the old 1.2 MiB APK-size guard (measured about 1,387 KiB). The guard was updated to 1.5 MiB; a passing run has not yet been verified.
- PR remains Draft and unmerged.

## Next required gates

1. Verify CI with the updated size limit.
2. Inspect release APK ZIP entries for exactly one `assets/kabut-pagi-dempo.jpg` and no duplicate Dempo collection asset.
3. Test clean install and offline startup on the target TV.
4. Test upgrade from legacy Malaysia selection and stale cache replacement.
5. Complete QR/security and remote-control device tests.

**Disposition: NOT FINAL. Do not merge or call the wallpaper device-ready until these gates pass.**
