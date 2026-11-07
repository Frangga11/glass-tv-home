# Image 2 Bottom Dock Correction — Checkpoint (7 November 2026)

## User feedback and root cause
- User supplied a real TV photo and reported that the previous APK showed no visible improvement; the application Dock remained much too high.
- Review of the screenshot and source found the actual cause: horizontal Dock layout still honored legacy `rows_below_dock` bottom reservation while the App Grid was padded toward the lower edge. This left the Dock floating above the bottom and App Grid tiles below it.
- The previous Image 2 APK only stabilized the information shelves in vertical Dock mode; it did not fix horizontal Dock placement. That earlier build is superseded and must not be presented as the corrective build.

## Corrective implementation
- Horizontal Dock now uses zero bottom margin and is anchored to the bottom edge of the Home content host.
- App Grid viewport is positioned above the Dock. Its bottom reservation is fixed using the largest supported Dock tile (capacity 4), so changing capacity 4/5/6 does not change the Grid viewport.
- Original XML top inset is restored rather than padding the Grid toward the bottom.
- Remote navigation follows the new adjacency: Dock DPAD-Up enters the nearest Grid tile in the row above; Grid DPAD-Down from its last row returns to the Dock; Grid first-row DPAD-Up returns toward weather/header.
- Vertical Dock side-rail behavior remains intact; vertical-mode information-shelf anchor remains independent of the focused tile.
- Regression test added for bottom-Dock-to-Grid focus mapping.

## Git / PR
- Owner branch: `fix/home-layout-image2-dock-geometry`
- Exact tested HEAD: `c3d749db7373052019063f5057ac08f0eda797b0`
- PR #73: OPEN / DRAFT / UNMERGED; base `feature/g4-parent-pin-hardening` @ `7146de58953325fdfc6c98501828676f143e64cf`.
- Changed files: `.github/workflows/ci.yml`, `DockLayoutModel.java`, `MainActivity.java`, `DockLayoutModelTest.java`.

## Exact-head validation and APK
- CI run #1404 / `42358639396`: both required jobs SUCCESS (launcher build/unit tests; lists/tools/docs).
- Artifact ID: `13568806731`; name: `glass-tv-home-image2-home-layout-a770e516f7bb8e7f3e8c97cf5fc718b0b793cf81`.
- Artifact ZIP size: 1,422,726 bytes.
- Artifact ZIP SHA-256: `a63aaf7577d15ad2cb2e7a1b8f1461b35027f9531a5e15e7ae492d9ef055e847`.
- Extracted `app-release.apk`: 1,622,545 bytes.
- APK SHA-256: `3b01c1126043c2e8025e1852d7de5d09812b091986af21e9c1c607188cb926ab`.
- APK archive integrity check: PASS; no ZIP errors.
- Test APK only; do not describe it as production-signed.
- Artifact retention expiry: 14 November 2026.

## Acceptance boundary
- Physical TV visual and remote acceptance: PENDING / NOT RUN after this corrective APK.
- No APK installation, ADB, device settings mutation, PR merge, base retarget or integration/main mutation was performed.
- Next gate: user installs/tests the new APK, checks horizontal Dock against bottom screen edge, Dock capacity 4/5/6, App Grid viewport stability, D-pad navigation and vertical Dock regression. Capture a new TV photo before claiming Image 2 visual acceptance.
