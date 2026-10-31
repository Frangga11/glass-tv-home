# 05 — Project Handover

**Version:** 1.0  
**Date:** 31 October 2026  
**Current phase:** Wallpaper/background audit W-001. QR security remediation passed CI; user supplied the approved image and a target-TV photo proving visual display. W-AC-07 passes for this image; repository/APK packaging and remaining device/network acceptance remain.

## 1. Resume protocol

Read `MASTER_PROJECT.md` and `DOCUMENTATION_WORKFLOW.md`, then use this handover as the current resume point. Do not repeat tests whose exact commit and result are already recorded.

## 2. Authoritative branches and reviews

- Repository: https://github.com/Frangga11/glass-tv-home
- Documentation: `docs/project-master-inventory`
- Code baseline / rollback branch: `feature/settings-reorganization-standby-gallery`
- Wallpaper security branch: `fix/wallpaper-qr-session-security`
- Wallpaper draft PR #7: https://github.com/Frangga11/glass-tv-home/pull/7
- Avatar branch: `fix/avatar-qr-upload-single-photo`
- Avatar draft PR #6: https://github.com/Frangga11/glass-tv-home/pull/6

## 3. Verified position

- Source baseline reports package `com.gathin.tvhome`, version 4.0.0 / code 40, minSdk 26, targetSdk 34, compileSdk 34, Java 17.
- Avatar QR implementation CI passed in run 41199199372. Actual Mi TV Stick/phone acceptance remains open; PR #6 remains draft.
- Wallpaper QR routes are now session-token scoped in PR #7. Upload page, upload endpoint, gallery APIs, and thumbnail routes use a random per-server path; legacy unscoped paths are rejected. QR page is no-store.
- Wallpaper CI run 41200811301 passed: unit tests, release build, APK size gate, and artifact publication.
- Wallpaper artifact ID 13042880821; size 1,102,820 bytes; SHA-256 `dd570c72b238bdca082c3096e857f877469b66af04ccfbee5ecf672de5fc8d3c`.
- APK inspection confirmed that `assets/kabut-pagi-dempo.jpg` is absent from the prior release artifact. The code expects this file; on a clean install the Default category therefore has no bundled offline wallpaper until the binary asset is committed and rebuilt.
- User supplied a photograph showing the selected Dempo wallpaper displayed on the target TV behind the launcher UI. Reuse this evidence: W-AC-07 passes for the approved image; initial legibility for this image is visually acceptable. This does not prove the exact optimized JPEG hash is included in the APK.
- QR token security has CI evidence only; foreign-token and closed-session rejection still need the phone/TV acceptance pass.

## 4. Next action

**Next implementation gate:** commit the already-approved binary JPEG at `launcher/app/src/main/assets/kabut-pagi-dempo.jpg`. The GitHub connection currently available in this session exposes text/blob operations but no direct binary-file upload handoff; do not falsely record the image as committed. Once uploaded, verify it is packaged in the APK and run CI once for the changed commit.

After that, perform one structured device pass covering W-AC-01–W-AC-12. Include phone upload, invalid upload, legacy/foreign route rejection, dialog-close revocation, crop/legibility, offline startup, rotation, and remote-only navigation.

## 5. Safety and merge gate

Keep PR #6 and PR #7 in draft. Do not merge, change the TV's default launcher, disable packages, or perform ADB modifications until the relevant device acceptance and rollback plan are confirmed. A CI pass is not a device pass.
