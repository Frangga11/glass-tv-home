# Wallpaper & Background Audit

**Audit ID:** W-001  
**Date opened:** 31 October 2026  
**Status:** PARTIAL — source inspection started; no device acceptance  
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
- The built-in category uses the bundled asset `assets/kabut-pagi-dempo.jpg`, copied to app-private storage as `kabut-pagi-dempo-default.jpg`.
- The Default category is described in the settings dialog as offline and separate from downloadable collections.
- If the bundled file cannot be opened, `getBundledDefaultWallpaper()` returns null and logs an error. The complete user-visible fallback path still needs verification.
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
- The QR currently points to the server root URL, not to a unique tokenized wallpaper route.
- The server's wallpaper upload route accepts a positive Content-Length below 60 MiB, writes into app-private `custom_wallpapers`, and validates decodable image bounds (maximum dimension 16,384 and 100 million pixels).
- The QR dialog dismiss listener stops the upload server.
- Standby upload uses a distinct standby-only server mode and tokenized standby paths. Avatar upload is a separate feature and store.

## 4. Findings requiring action

| ID | Severity | Finding | Required action | Status |
|---|---|---|---|---|
| W-F-01 | P1 | Wallpaper QR URL uses the bare server root; the wallpaper upload route is not session-token scoped. On the same LAN, another client may reach the upload page/route while the server is running. | Design a short-lived, unpredictable, wallpaper-specific session path/token; scope allowed routes to that session; stop/revoke it on dialog close; test unauthorized paths and expired sessions. | OPEN — security fix required |
| W-F-02 | P2 | Bundled default asset failure returns null; the complete fallback behavior and first-frame experience are not yet proven. | Inspect layout/background initialization and test missing/corrupt asset path. | OPEN |
| W-F-03 | P2 | CENTER_CROP may cut important image content depending on source aspect ratio and composition. | Test representative portrait, 16:9, ultrawide, and 4K assets; decide whether upload preview/crop guidance is needed. | OPEN |
| W-F-04 | P2 | Text/widget legibility is not programmatically or visually guaranteed for bright wallpaper images. | Evaluate dim overlay/gradient and text contrast on real TV using worst-case images. | OPEN |
| W-F-05 | P2 | Performance comments describe intended memory reduction, but actual memory, blur latency, and long-run stability have not been measured on Mi TV Stick. | Run repeated rotation and long-duration test; record freezes, frame drops, and reloads. | OPEN |
| W-F-06 | P2 | Wallpaper upload accepts image bounds after writing the upload; negative cases and resource-exhaustion behavior are not yet tested. | Test truncated body, false Content-Length, corrupt payload, oversized body, large dimensions, and interrupted client. | OPEN |
| W-F-07 | P2 | File naming uses a .jpg suffix for received bytes even when source content may be PNG; decoder appears to inspect content, but all downstream filters and management paths need validation. | Test JPEG/PNG and confirm extension/filter consistency. | OPEN |
| W-F-08 | P2 | Wallpaper, standby, and avatar are intended to be separate, but the end-to-end user flows have not been revalidated together. | Verify each upload changes only its own store and screen. | OPEN |

## 5. Work sequence — no duplicate passes

1. Finish source review of upload server, custom manager, asset dimensions/format, config loading, and lifecycle.
2. Fix W-F-01 first; do not expose the existing unauthenticated route as release-ready.
3. Add focused automated tests for route/session authorization and image validation where the current test architecture permits.
4. Run CI once after code changes.
5. Run one structured TV/phone acceptance pass against W-AC-01–W-AC-12; record results in this file.
6. Close only passed criteria; retain any device-dependent item as NOT TESTED until the user tests it.

## 6. Current disposition

**NOT FINAL / NOT YET DEVICE-READY.** Source inspection confirms the core wallpaper architecture exists. A high-priority QR upload session-scope issue is identified and must be corrected before the wallpaper upload workflow can be considered safe for release. No device testing has been performed in this audit.
