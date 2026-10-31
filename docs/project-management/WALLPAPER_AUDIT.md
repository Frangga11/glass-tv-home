# Wallpaper & Background Audit

**Audit ID:** W-001  
**Date opened:** 31 October 2026  
**Status:** PARTIAL — target-TV visual proof accepted; Dempo offline collection category source implemented in draft PR #7; CI run #159 in progress; binary asset packaging and device/network checks pending  
**Code baseline inspected:** `feature/settings-reorganization-standby-gallery`  
**Files inspected:** `MainActivity.java`, `WallpaperUploadServer.java`, `AndroidManifest.xml`  
**Target:** LG 32-inch display via Mi TV Stick (Android 11 / API 30; revalidate on device)

## 1. Scope

Audit the complete home-screen background system: bundled default, online categories, user-uploaded wallpaper, image decoding/cropping, rotation, blur/glass interaction, text legibility, resource use, QR upload security, and separation from standby photos and profile avatars.

## 2. Acceptance criteria

**User visual evidence received (31 October 2026):** user supplied a photograph of the actual LG TV showing the Kabut Pagi Dempo wallpaper in Glass TV Home with clock/date, weather, prayer times, doa, and app icons visible. This is accepted as PASS for the specific visual composition and legibility shown in that photograph. It does not prove fresh-install asset packaging, offline fallback, QR/network security, animation performance, or other wallpaper images.

**Scope clarification (user reconfirmed 31 Oct 2026):** Kabut Pagi Dempo is the image for the existing `Default` tab. Do not add a new category, tab, or separate Dempo collection. Reuse the former Malaysia default method: legacy `Malaysia*` selections migrate to `Default`, which loads one bundled APK asset at `assets/kabut-pagi-dempo.jpg`.

| ID | Acceptance criterion | Evidence required |
|---|---|---|
| W-AC-01 | Bundled default wallpaper appears on a fresh/offline install | asset/source check + cold-start device test |
| W-AC-02 | Default fallback is safe if asset is missing/corrupt | failure-path check + device test |
| W-AC-03 | Online category sync does not block Home or leave a broken screen | source/test + online/offline device test |
| W-AC-04 | Custom wallpaper QR upload works only while its dialog/session is active | phone-to-TV test, then closed-session rejection |
| W-AC-05 | Upload rejects malformed, oversized, truncated, and non-image content without crashing | negative upload tests |
| W-AC-06 | Upload endpoint is not exposed to unintended LAN clients or unrelated routes | security review and session/auth tests |
| W-AC-07 | Crop, aspect ratio, and composition are suitable for 16:9 TV display | USER VISUAL PASS — photo of actual TV supplied 31 Oct 2026; shown composition is acceptable |
| W-AC-08 | Main text, app labels, prayer information, and status widgets remain readable over bright/dark images | USER VISUAL PASS for the supplied Dempo image and shown layout; representative bright/dark collection images still need review |
| W-AC-09 | Wallpaper changes and glass blur remain synchronized without visible lag/flicker | device test |
| W-AC-10 | Decode, rotation, and blur remain within acceptable memory/CPU limits on Mi TV Stick | repeated rotation and long-run device test |
| W-AC-11 | Wallpaper, avatar, and standby collections remain separate | source and end-to-end workflow test |
| W-AC-12 | Settings and all QR controls are usable with TV remote only | D-pad/OK/Back test |

## 3. Confirmed source findings

### User-approved Dempo collection implementation
- The user's target-TV photograph is accepted as visual proof for the supplied composition: wallpaper, clock/date, weather, prayer times, doa, and app row are visible. Do not repeat this same visual review.
- Source commit `5829212cfae018aeca25b07e7f9e35ec8955122b` adds a `Dempo` category with visible label `Kabut Pagi di Dempo`, placed directly after `Default` and before `Natural` / `Nature & Landscapes`.
- The category reads images from `assets/wallpapers/dempo/`, copies them into app-private `bundled_wallpapers/dempo/`, and uses the existing wallpaper rotation pipeline without online sync.
- A follow-up at `586d561b806869b4433ccf56c464f9c39332df2c` restores the selected Dempo category on launcher startup using the bundled asset loader.
- The image binary is still not present in the repository. Required path: `launcher/app/src/main/assets/wallpapers/dempo/kabut-pagi-dempo.jpg`. Until then, the new tab displays an unavailable message rather than downloading the image.
- Existing `Default` category behavior is preserved separately.

### User-approved Dempo collection implementation
- The user's target-TV photograph is accepted as visual proof for the supplied composition: wallpaper, clock/date, weather, prayer times, doa, and app row are visible. Do not repeat this same visual review.
- Source commit `5829212cfae018aeca25b07e7f9e35ec8955122b` adds a `Dempo` category with visible label `Kabut Pagi di Dempo`, placed directly after `Default` and before `Natural` / `Nature & Landscapes`.
- The new category reads images from `assets/wallpapers/dempo/`, copies them into app-private `bundled_wallpapers/dempo/`, and uses the existing wallpaper rotation pipeline. This is designed to work offline; it does not rely on QR upload or online category sync.
- The image binary is still not present in the repository. The code path will therefore show a not-available message until `kabut-pagi-dempo.jpg` is committed at `launcher/app/src/main/assets/wallpapers/dempo/kabut-pagi-dempo.jpg`.
- The existing `Default` category is intentionally preserved; the new collection is a separate category.

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

### Default image received and validated — 31 October 2026
- User supplied the intended official default image, titled **Kabut Pagi Dempo**.
- The uploaded file's actual content is JPEG despite the uploaded filename ending in .png.
- Verified dimensions: 1536 × 864 pixels (exact 16:9); RGB; suitable for a 16:9 TV frame without geometric crop.
- A package-ready optimized JPEG was prepared as `kabut-pagi-dempo.jpg`, quality 82, 230,883 bytes.
- SHA-256: `04e5793b92cab1fcaeaaf0e3b80769598f91c7521b8e3d62c4f7a38888982982`.
- Intended repository destination: `launcher/app/src/main/assets/kabut-pagi-dempo.jpg`.
- User subsequently supplied a photograph of the actual TV showing this wallpaper behind the Glass TV Home interface (31 October 2026). The displayed wallpaper composition, app row, clock, weather, prayer times, and information text are visible and reasonably legible in this evidence. Mark the approved-image visual check W-AC-07 as PASS; mark the single approved wallpaper's visual legibility check as PASS, but retain W-AC-08 as PARTIAL because alternate bright/dark wallpapers have not been tested.
- The photograph proves that a visually matching wallpaper has run on the TV; it does not by itself prove the exact optimized JPEG hash above is bundled in the APK. The binary asset has **not yet been committed to GitHub or verified inside a new APK**. Do not mark W-F-02 closed until those steps and a clean-install/offline TV test pass.

## 4. Findings requiring action

| ID | Severity | Finding | Required action | Status |
|---|---|---|---|---|
| W-F-01 | P1 | Baseline wallpaper QR exposed upload and gallery-management routes on the LAN without a per-session path token. | Token-scoped page/upload/API/thumb routes implemented; verify unauthorized paths, token isolation, and dialog-close revocation on device. | IMPLEMENTED IN DRAFT PR #7 — CI PASS; device verification pending |
| W-F-02 | P1 | Existing Default loader expects `assets/kabut-pagi-dempo.jpg`, but the approved JPEG is not yet committed to APK assets. | Add exactly one JPEG at `launcher/app/src/main/assets/kabut-pagi-dempo.jpg`, verify APK contents, then test clean install and legacy Malaysia upgrade. | CODE CORRECTED; BINARY ASSET OPEN |
| W-F-03 | P2 | CENTER_CROP may cut important image content depending on source aspect ratio and composition. | Test representative portrait, 16:9, ultrawide, and 4K assets; decide whether upload preview/crop guidance is needed. | OPEN |
| W-F-04 | P2 | Text/widget legibility is not programmatically or visually guaranteed for bright wallpaper images. | Evaluate dim overlay/gradient and text contrast on real TV using worst-case images. | OPEN |
| W-F-05 | P2 | Performance comments describe intended memory reduction, but actual memory, blur latency, and long-run stability have not been measured on Mi TV Stick. | Run repeated rotation and long-duration test; record freezes, frame drops, and reloads. | OPEN |
| W-F-06 | P2 | Wallpaper upload accepts image bounds after writing the upload; negative cases and resource-exhaustion behavior are not yet tested. | Test truncated body, false Content-Length, corrupt payload, oversized body, large dimensions, and interrupted client. | OPEN |
| W-F-07 | P2 | File naming uses a .jpg suffix for received bytes even when source content may be PNG; decoder appears to inspect content, but all downstream filters and management paths need validation. | Test JPEG/PNG and confirm extension/filter consistency. | OPEN |
| W-F-08 | P2 | Wallpaper, standby, and avatar are intended to be separate, but the end-to-end user flows have not been revalidated together. | Verify each upload changes only its own store and screen. | OPEN |

## 5. Remediation currently under review

- Branch: `fix/wallpaper-qr-session-security` (5 commits ahead, 0 behind the baseline at last comparison).
- Draft PR: https://github.com/Frangga11/glass-tv-home/pull/7
- Changed: `WallpaperUploadServer.java`, `MainActivity.java`; added `WallpaperUploadSessionRouteTest.java`.
- `MainActivity.java` now includes the offline Dempo category and restores it on startup.
- `MainActivity.java` also adds the offline `Dempo` collection category and asset-copy/rotation path.
- Unit tests cover accepted session routes, legacy root/upload/API rejection, foreign tokens, and unsupported methods.
- Security-only CI run 41200811301 passed before the Dempo category change.
- CI run #159 for source commit `5829212cfae018aeca25b07e7f9e35ec8955122b`: https://github.com/Frangga11/glass-tv-home/actions/runs/41202506621 — in progress at last check.
- The prior artifact 13042880821 predates the category code and does not contain the new collection asset. A new artifact must be built after the binary is committed.
- No physical TV/phone test has been performed.

## 6. Work sequence — no duplicate passes

1. Check CI #162 for the corrected Default-only code and asset-refresh loader.
2. Add exactly one approved JPEG at `launcher/app/src/main/assets/kabut-pagi-dempo.jpg`.
3. Rebuild once after the binary is committed; verify the exact APK asset path, dimensions, and SHA-256. Confirm there is no separate Dempo folder/category asset.
4. Test clean install and offline startup; test upgrade with a legacy `Malaysia*` selection and an older cached Default image to confirm it is replaced by the packaged Dempo asset.
5. Reuse the supplied TV photo for the already-passed visual composition check. Test only remaining cases: install/upgrade, offline start, remote focus, QR security, and performance.
6. Keep PR #7 draft until CI, APK verification, and required device/network acceptance pass.

## 7. Current disposition

**NOT FINAL.** Target-TV photo evidence passes the visual composition check for the approved image. The `Dempo` collection category source is implemented in draft PR #7, directly above Nature & Landscapes. The binary JPEG has not yet been committed to `assets/wallpapers/dempo/`, and CI #159 was still running at last check. Offline APK loading and remaining device/network acceptance are open.
