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
