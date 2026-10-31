# Wallpaper & Background Audit

**Audit ID:** W-001  
**Date opened:** 31 October 2026  
**Status:** PARTIAL — QR route remediation implemented in draft PR #7; CI PASS; default asset and device acceptance pending  
**Code baseline inspected:** `feature/settings-reorganization-standby-gallery`  
**Files inspected:** `MainActivity.java`, `WallpaperUploadServer.java`, `AndroidManifest.xml`  
**Target:** LG 32-inch display via Mi TV Stick (Android 11 / API 30; revalidate on device)

## 1. Scope

Audit the complete home-screen background system: bundled default, online categories, user-uploaded wallpaper, image decoding/cropping, rotation, blur/glass interaction, text legibility, resource use, QR upload security, and separation from standby photos and profile avatars.

## 2. Acceptance criteria

| ID | Acceptance criterion | Evidence required |
|---|---|---|
| W-AC-01 | Bundled default wallpaper appears on a fresh/offline install | asset/source check + cold-start device test |
| W-AC-02 | Default fallback is safe if asset is missing/corrupt | failure-path check + device test |
| W-AC-03 | Online category sync does not block Home or leave a broken screen | source/test + online/offline device test |
| W-AC-04 | Custom wallpaper QR upload works only while its dialog/session is active | phone-to-TV test, then closed-session rejection |
| W-AC-05 | Upload rejects malformed, oversized, truncated, and non-image content without crashing | negative upload tests |
| W-AC-06 | Upload endpoint is not exposed to unintended LAN clients or unrelated routes | security review and session/auth tests |
| W-AC-07 | Crop, aspect ratio, and composition are suitable for 16:9 TV display | approved visual review on target TV |
| W-AC-08 | Main text, app labels, prayer information, and status widgets remain readable over bright/dark images | visual contrast review across representative images |
| W-AC-09 | Wallpaper changes and glass blur remain synchronized without visible lag/flicker | device test |
| W-AC-10 | Decode, rotation, and blur remain within acceptable memory/CPU limits on Mi TV Stick | repeated rotation and long-run device test |
| W-AC-11 | Wallpaper, avatar, and standby collections remain separate | source and end-to-end workflow test |
| W-AC-12 | Settings and all QR controls are usable with TV remote only | D-pad/OK/Back test |

## 3. Confirmed source findings

### Background selection and default
- `MainActivity` reads the active category from `wallpaper_prefs`; the default is `Default`.
- Source code expects `assets/kabut-pagi-dempo.jpg` and copies it to app-private storage as `kabut-pagi-dempo-default.jpg`.
- **The expected asset is absent from the inspected release APK.** APK artifact from CI run 41200811301 contains only `assets/dexopt/baseline.prof` and `assets/dexopt/baseline.profm`; it does not contain `assets/kabut-pagi-dempo.jpg`. Attempts to fetch the expected source path from the baseline branch also returned not found.
- `getBundledDefaultWallpaper()` catches the missing-asset error and returns null. `Config.load()` has no built-in wallpaper file on a fresh install; therefore the Default category has no bundled image to display and falls back to the plain background (black by default).
- The Default category is described in the settings dialog as offline and separate from downloadable collections. This promise is not currently met until the asset is added to the APK.
- Fresh/legacy category values beginning with "Malaysia" are migrated to "Default".

### Rendering and rotation
- Wallpaper decoding uses screen metrics and `RGB_565` downsampling; the source comment targets a 1080p bitmap footprint for 4K input.
- The image view uses `CENTER_CROP`.
- Wallpaper transitions use an 800 ms cross-fade unless reduced motion is active.
- Backdrop blur is updated alongside the wallpaper; background decode/blur work uses a worker executor.
- Rotation interval defaults to 5 minutes; 0 means manual. Rotation is paused/restarted around Home focus lifecycle.
- Default is a single bundled image; online/custom categories can contain multiple images.
- The current source comments and logic indicate performance intent, not proof of performance on the user's TV.

### QR upload and storage
- Wallpaper upload starts a local `WallpaperUploadServer` on port 8080 with fallback ports (8080, 8888, 8081, 9090, then an ephemeral port).
- Baseline branch QR pointed to the bare server root URL, and the server exposed upload and gallery-management endpoints without a per-session path token.
- Remediation branch `fix/wallpaper-qr-session-security` now generates `/wallpaper/<random-session-token>` per server instance. The upload page, upload endpoint, gallery APIs, and thumbnail route use that tokenized base path; legacy unscoped routes are rejected. The QR page is marked `Cache-Control: no-store`.
- The server's wallpaper upload route accepts a positive Content-Length below 60 MiB, writes into app-private `custom_wallpapers`, and validates decodable image bounds (maximum dimension 16,384 and 100 million pixels).
- The QR dialog dismiss listener stops the upload server.
- Standby upload uses a distinct standby-only server mode and tokenized standby paths. Avatar upload is a separate feature and store.

## 4. Findings requiring action

| ID | Severity | Finding | Required action | Status |
|---|---|---|---|---|
| W-F-01 | P1 | Baseline wallpaper QR exposed upload and gallery-management routes on the LAN without a per-session path token. | Token-scoped page/upload/API/thumb routes implemented; verify unauthorized paths, token isolation, and dialog-close revocation on device. | IMPLEMENTED IN DRAFT PR #7 — CI PASS; device verification pending |
| W-F-02 | P1 | Expected `kabut-pagi-dempo.jpg` is missing from the release APK; `getBundledDefaultWallpaper()` returns null on a fresh install, leaving Default without its offline image. | Obtain/approve the final wallpaper image, package it at the exact assets path, verify APK contents, then test cold-start/offline/fallback behavior. | OPEN — USER IMAGE/ART DIRECTION REQUIRED |
| W-F-03 | P2 | CENTER_CROP may cut important image content depending on source aspect ratio and composition. | Test representative portrait, 16:9, ultrawide, and 4K assets; decide whether upload preview/crop guidance is needed. | OPEN |
| W-F-04 | P2 | Text/widget legibility is not programmatically or visually guaranteed for bright wallpaper images. | Evaluate dim overlay/gradient and text contrast on real TV using worst-case images. | OPEN |
| W-F-05 | P2 | Performance comments describe intended memory reduction, but actual memory, blur latency, and long-run stability have not been measured on Mi TV Stick. | Run repeated rotation and long-duration test; record freezes, frame drops, and reloads. | OPEN |
| W-F-06 | P2 | Wallpaper upload accepts image bounds after writing the upload; negative cases and resource-exhaustion behavior are not yet tested. | Test truncated body, false Content-Length, corrupt payload, oversized body, large dimensions, and interrupted client. | OPEN |
| W-F-07 | P2 | File naming uses a .jpg suffix for received bytes even when source content may be PNG; decoder appears to inspect content, but all downstream filters and management paths need validation. | Test JPEG/PNG and confirm extension/filter consistency. | OPEN |
| W-F-08 | P2 | Wallpaper, standby, and avatar are intended to be separate, but the end-to-end user flows have not been revalidated together. | Verify each upload changes only its own store and screen. | OPEN |

## 5. Remediation currently under review

- Branch: `fix/wallpaper-qr-session-security` (4 commits ahead, 0 behind the baseline at last comparison).
- Draft PR: https://github.com/Frangga11/glass-tv-home/pull/7
- Changed: `WallpaperUploadServer.java`, `MainActivity.java`; added `WallpaperUploadSessionRouteTest.java`.
- Unit tests cover accepted session routes, legacy root/upload/API rejection, foreign tokens, and unsupported methods.
- Latest CI run: https://github.com/Frangga11/glass-tv-home/actions/runs/41200811301 — PASS (unit tests, release build, APK size gate, artifact publication).
- Artifact: `glass-tv-home-pagar-alam-release`, ID `13042880821`, size 1,102,820 bytes, SHA-256 `dd570c72b238bdca082c3096e857f877469b66af04ccfbee5ecf672de5fc8d3c`.
- Artifact inspection confirmed the expected `assets/kabut-pagi-dempo.jpg` is absent. This is a separate P1 default-wallpaper blocker not fixed by PR #7.
- No physical TV/phone test has been performed.

## 6. Work sequence — no duplicate passes

1. Resolve W-F-02: user approves/provides the default Dempo image; package it at `launcher/app/src/main/assets/kabut-pagi-dempo.jpg`.
2. Verify the image is present in the APK and inspect its dimensions/aspect ratio. Do not rerun the already-passed QR security checks unless code changes.
3. Finish the remaining source review of custom manager, image extension handling, file limits, and lifecycle.
4. Run CI once after the asset/code change.
5. Run one structured TV/phone acceptance pass against W-AC-01–W-AC-12, including QR foreign-token/legacy-route rejection and dialog-close revocation.
6. Close only passed criteria; retain device-dependent items as NOT TESTED until actual TV/phone evidence exists.

## 7. Current disposition

**NOT FINAL / NOT YET DEVICE-READY.** Source inspection confirms the core wallpaper architecture exists. Two P1 issues remain at different gates: (1) QR route security is implemented in draft PR #7 and CI passes, but unauthorized/closed-session behavior still needs device/network verification; (2) the expected default Dempo wallpaper asset is absent from the release APK and requires an approved image asset. No device testing has been performed in this audit.
