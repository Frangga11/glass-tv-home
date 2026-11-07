# Image 2 Home Layout Recovery — Build Checkpoint (7 November 2026)

## Status
- Source change: COMPLETE for the scoped Dock-orientation geometry defect.
- CI/build: PASS.
- APK artifact: downloaded and integrity-checked.
- Pull request: #73 OPEN / DRAFT / UNMERGED.
- Physical Android TV acceptance: PENDING / NOT RUN. This checkpoint is not a visual acceptance sign-off.

## Approved target and implementation
- Restore the Image 2 Home information layout without redesigning or removing approved components.
- Keep wallpaper description, Ayah and prayer-schedule information anchored independently of the focused Dock card when Dock orientation is vertical.
- Preserve horizontal Dock behavior and the existing capacity/orientation controls.
- Change is scoped to `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`; CI workflow is scoped to publishing this PR's test APK.

## Git evidence
- Owner branch: `fix/home-layout-image2-dock-geometry`
- Exact tested HEAD: `31886651d7f275155f8a526302e252dc04687e4f`
- Source implementation commit: `e8d403142043d27bbfe4c5b4c7cb2b67eb058eaa`
- Workflow/artifact commit: `31886651d7f275155f8a526302e252dc04687e4f`
- Base branch: `feature/g4-parent-pin-hardening`
- Base SHA: `7146de58953325fdfc6c98501828676f143e64cf`
- PR: https://github.com/Frangga11/glass-tv-home/pull/73

## CI and APK
- Workflow run: #1397 / `42357686726`
- Result: both required jobs SUCCESS — Lists/tools/docs; Launcher build and unit tests.
- Artifact ID: `13567869928`
- Artifact name: `glass-tv-home-image2-home-layout-1c857f30a29d8f011d2251d8eb2032d103513d7a`
- Artifact archive size: 1,422,862 bytes
- Artifact ZIP SHA-256: `2c9b6a3ee8bbfc74971ca8734354fc819dfcdcd97a95499786998856f28c1298`
- APK: `app-release.apk`, 1,622,565 bytes
- APK SHA-256: `cd2b301f5fb48ffdb6b9cafd74927c5da6515a4ab9aebf6dc47e6db4ddccb4ac`
- APK ZIP integrity: PASS (all entries tested; no errors).
- Artifact retention expiry: 14 November 2026.
- This is a CI test APK, not a production-signed release claim.

## Decision and remaining gates
- Keep PR #73 OPEN / DRAFT / UNMERGED; do not merge or retarget.
- No APK installation, ADB or device configuration was performed.
- Source-host CI does not establish the requested Image 2 visual match on a television. Physical review must still check Home placement, wallpaper focal area, Ayah/prayer panel, Dock capacity 4/5/6, horizontal/vertical behavior, remote focus and 1080p layout.
- Next gate: provide this APK for controlled acceptance testing; record any reproducible visual/functional defects before merge consideration.
