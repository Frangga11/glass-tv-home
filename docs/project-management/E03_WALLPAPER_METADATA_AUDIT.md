# E03 — Appearance & Wallpaper: Default Image / Metadata Audit

**Checkpoint date:** 2 November 2026  
**Status:** IN PROGRESS — source checkpoint only; visual asset replacement is still a blocking item.  
**Feature PR:** #36 — OPEN / DRAFT / UNMERGED.  
**Feature branch:** `feature/e03-wallpaper-clean-metadata`  
**Parent:** E02 exact source head `3ab9b55f3cfe03ad9841a119590a9183cf114e96`.

## Trigger and confirmed finding

The packaged `launcher/app/src/main/assets/kabut-pagi-dempo.jpg` contains the title “Kabut Pagi Dempo” and descriptive copy baked into the JPEG pixels. The launcher also has a native `heroShelf` with `heroName`, `heroDesc`, and `heroWhere`; `MainActivity.setPlace(File)` obtains descriptions from the place/online wallpaper providers. This creates duplicate text when the Default image itself already contains copy.

## Scope and ownership

- E03 owns the Default wallpaper asset and its content metadata.
- The existing B01 global UI/hero presentation remains the rendering owner.
- E03 must not introduce a competing global overlay, navigation/focus change, or new dialog style.
- Existing online/place wallpaper providers, custom wallpaper behavior, shuffle cycle, active index, interval, QR upload contracts and StandBy photo flow remain unchanged.

## Implemented source checkpoint

- Added `WallpaperMetadata`, a small isolated metadata provider for the exact cached filename `kabut-pagi-dempo-default.jpg`.
- `MainActivity.setPlace()` consults that provider first; if it returns no metadata, the original PlaceWallpapers/OnlineWallpapers path remains unchanged.
- Added unit tests for the Default metadata and exact filename isolation, including null, custom, other wallpaper and backup filename cases.
- No SharedPreferences keys, storage paths, upload endpoints, wallpaper rotation algorithms, or global UI components were changed.

## Remaining blocking work

1. Replace the packaged JPEG with a clean image that has no baked-in title/description. The current source asset has not yet been replaced; therefore the visible duplicate-text problem is not resolved by this checkpoint.
2. Add image provenance/dimensions/size verification and confirm the release APK remains below 1,572,864 bytes with safe headroom.
3. Add/complete regression coverage proving Default metadata is displayed once; downloaded/place wallpapers still use their own descriptions; custom wallpapers still suppress the hero description; disabling wallpaper descriptions remains respected; metadata follows the currently displayed image after Next/shuffle/restart.
4. Verify image decode, aspect/crop, visual readability, animation, D-pad focus and performance on the actual Mi TV Stick in Project F.
5. Confirm QR wallpaper and StandBy storage/routes remain isolated; no device install or APK publication is authorized during this checkpoint.

## Safety disposition

PR #36 remains Draft/open/unmerged under D-013. This checkpoint is not E03 closure, not device acceptance, and not release approval. Do not merge or publish an APK until the clean asset is committed, exact-head CI is green, and the complete visual/behavior regression set is passed.


## E03 asset + CI checkpoint — 2 Nov 2026

This checkpoint supersedes earlier statements that the clean Default wallpaper binary was not committed.
- Clean asset is now committed at `launcher/app/src/main/assets/kabut-pagi-dempo.jpg` on `feature/e03-wallpaper-clean-metadata`.
- Upload commit: `d6676c03474396c0f616284a3e43fc709226ed82`; exact PR head: `d6676c03474396c0f616284a3e43fc709226ed82`; PR #36 remains OPEN/DRAFT/UNMERGED, base `feature/e02-profile-avatar-lock-ui`.
- Git blob SHA `a73eb03584a706870e7b81447331ee3e47170e06` matches the locally prepared image byte-for-byte. Verified JPEG: 1920x1080, RGB, 230,997 bytes; filename and asset path match `MainActivity.getBundledDefaultWallpaper()` and cached metadata key `kabut-pagi-dempo-default.jpg`.
- Exact-head CI #625 / run `41410370222` on `d6676c03474396c0f616284a3e43fc709226ed82`: **SUCCESS**. Launcher build/unit tests and Lists/tools/docs both passed; `testReleaseUnitTest` and `assembleRelease` passed.
- Release APK: `1,561,913 bytes`; cap `1,572,864 bytes`; headroom `10,951 bytes`. Size gate passed. Manual TV-test APK publication was skipped; no APK was published or installed.
- Source path audit: Default copy uses the packaged asset and writes the exact cached filename expected by `WallpaperMetadata`; setPlace consults Default metadata first and preserves Place/Online fallback. Custom wallpaper and disabled-description suppression remain in the existing path. No B01 UI/focus, rotation/shuffle/index/interval, QR upload, StandBy or user-preference contract was intentionally changed.
- Remaining E03 acceptance: regression verification for Default/online/custom/description-toggle/Next/shuffle/restart and actual Mi TV/remote/phone visual/performance acceptance in Project F. QR/StandBy isolation remains to be included in integration checks. No physical device acceptance is claimed.
- Disposition: asset integration and CI are **PASS**; E03 overall remains **IN PROGRESS / NOT FINAL** until remaining regression and Project F acceptance. Preserve D-013: no early merge, no release approval.


## Offline Default wallpaper verification — 2 Nov 2026

- Source audit confirms Default wallpaper and its descriptive metadata are **offline-capable**: `getActiveWallpaperCategory()` defaults to `Default`; the Default startup branch calls `getBundledDefaultWallpaper()`; that method reads `kabut-pagi-dempo.jpg` from APK assets and stages it into app-private storage as `kabut-pagi-dempo-default.jpg`. It does not make a network request.
- `WallpaperMetadata.describe()` returns the title, description and location from hard-coded local metadata for the exact cached filename. `MainActivity.setPlace()` checks this provider before the Place/Online providers. No HTTP, feed, or online service is required for the Default description.
- The wallpaper and description therefore remain available after installation with Wi-Fi/internet disconnected, provided the installed APK contains the asset and the app can read/write its own files directory. A fresh install still requires the APK itself to have been installed first; no server fetch is involved at runtime.
- Visibility conditions remain unchanged: wallpaper descriptions must be enabled, and the existing hero shelf/focus behavior controls when the text is shown. Offline availability does not override the user's description setting.
- Scope caveat: online/place refreshers and other network-backed widgets/features may independently attempt background refreshes when configured. They are not dependencies for the Default asset or its metadata. This audit is a source-path verification; offline behavior has not yet been physically exercised on the Mi TV Stick. Add Wi-Fi-off cold-start and restart acceptance to Project F.
- Disposition: source-level offline dependency audit **PASS**; physical offline acceptance **PENDING Project F**. No source change was necessary; do not refactor the working Default path without a reproducible defect.
