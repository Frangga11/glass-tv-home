# Upstream Feature Adoption Audit — GIF Avatar Search & Dock Capacity

**Record:** U-ADOPT-AUDIT-2026-11-05-01  
**Date:** 5 November 2026  
**Target:** `Frangga11/glass-tv-home`  
**Upstream:** `aqmangge/glass-tv-home`  
**Disposition:** Preliminary source audit PASS; implementation in progress / not yet claimed.

## Pinned upstream baseline

- Upstream default branch `main` HEAD: `23064ec34ed597ce8bb044b3732f45be93e38324`.
- Latest commit message at audit: `perf(ui): optimize UI for low-spec devices, eliminate GC frame drops & add moon/mosque/rehal icons`.
- The upstream tree contains `GifSearchDialog.java`, `ProfileSettingsDialog.java`, `ProfileAvatarHelper.java`, `AppAdapter.java`, `Config.java`, and `MainActivity.java`.
- User-selected reference is limited to GIF search for profile avatar and Dock capacity 4/5/6.

## GIF search — upstream findings

Inspected:
- `GifSearchDialog.java` (396 lines; blob `160767cb701c77420f42d8d1d0c1df6aa19b58b7`)
- `ProfileSettingsDialog.java` (327 lines; blob `58ee3495bf64821728e96e11fcf1f8cf13f7ef03`)
- `AvatarCustomizerDialog.java`
- `ProfileAvatarHelper.java` references and call sites.

Positive behavior to adapt:
- Search field, suggested categories, loading feedback, result previews, selectable animated GIFs, and D-pad focus affordances.
- Profile selection callback is explicitly passed a profile ID.
- Upstream exposes the GIF option in the profile avatar selection flow.

Do NOT copy upstream implementation as-is:
1. The source embeds a literal GIPHY API key. This is a credential/configuration anti-pattern and must not be copied or reused.
2. Search query is run on a newly created raw Thread for each request; no request cancellation/generation guard is evident in the inspected flow.
3. Network response parsing collects URL strings only and silently swallows errors; user cannot distinguish offline, rate-limited, server, malformed-response, and empty-result states.
4. Download writes directly to the final avatar filename, without a demonstrated temporary-file + atomic-commit path, and no explicit byte ceiling/content-type/magic validation is visible in the inspected method.
5. Upstream stores under `files/avatars/avatar_<profileId>.gif`; target E02 uses hardened profile-scoped private storage under `files/profile_avatars` and `ProfileManager.replaceCustomAvatar(...)`. Reuse target storage contract; do not create a parallel avatar store.
6. No `rating` restriction is passed in the inspected Search request. Target implementation must use a conservative family-safe content rating and verify provider policy.
7. GIPHY attribution is mandatory. UI must show the approved Powered By GIPHY mark/attribution where the integration is used.
8. Search and media request policy, cache retention, rate limits, and provider terms must be documented and tested.

Official provider requirements checked:
- GIPHY Search requires an API key and client-side Search request; beta keys are rate-limited to 100 API calls/searches per hour.
- GIPHY requires conspicuous Powered By GIPHY attribution.
- GIPHY recommends MP4 for GIF playback and WEBP for stickers for performance; Android TV decoder compatibility and actual avatar animation support must be tested before selecting the stored rendition.
- Reference: https://developers.giphy.com/docs/api/quick-start-guide/ and https://developers.giphy.com/docs/api/

## GIF target design

- Keep E02 as sole owner of profile/avatar persistence and selection.
- Integrate the search entry into the existing E02 profile avatar options; preserve built-in avatar and QR upload paths.
- Use a provider abstraction and explicit API-key configuration; never commit a key or reuse upstream's key.
- Add debounce/manual submit, bounded page size, request cancellation, stale-response suppression, finite connect/read timeouts, bounded response body, strict HTTPS host validation, MIME/content checks, and maximum download bytes.
- Stage download to a temporary file in the target private avatar directory, validate it, then use the existing profile replacement transaction; clean temp files on failure/cancel.
- Profile ID must be validated and never used as an unchecked path fragment.
- Enforce conservative content rating, show attribution, and provide distinct loading/empty/offline/rate-limit/error/retry states.
- Use existing E02 profile isolation, avatar cleanup, B01 global modal, B06 feedback, B07 focus restoration, C07 network/upload safety and F-stage regression contracts.
- Support Indonesian and English; do not introduce a new background service.

## Dock capacity — upstream findings

Inspected:
- `AppAdapter.java` and `Config.java`.
- Upstream `MainActivity.java` methods `getDockColumns()`, `setDockColumns(int)`, and `showDockColumnsDialog(...)`.

Positive behavior to adapt:
- Three choices: 4, 5, 6.
- Default choice 5.
- Preference persistence.
- Rebuilds grid adapter/decoration and restores focus after selection.

Important architectural finding and target decision:
- Upstream calls the setting "Dock columns"; `setDockColumns` changes global `COLUMNS` and the GridLayoutManager span count as well as Dock capacity. This coupling keeps the Dock in one row.
- Target E06 also has one `COLUMNS` value for grid columns and Dock slots; AppAdapter uses it for tile geometry and Dock membership.
- A truly independent Dock capacity would require a separate horizontal Dock container/adapter or a more substantial span/layout redesign. Changing only the Dock threshold while leaving the current grid at fewer columns could wrap a 5- or 6-item Dock onto multiple rows and break the visual contract.
- **Safe first adaptation:** retain the existing single-RecyclerView architecture and let the selected Dock capacity (4/5/6) drive the matching column count, tile sizing, and Dock decoration as one coherent layout setting. Clearly label the effect in Settings so users understand that app-grid density also adapts. Default remains 5.
- Do not delete, reorder, or hide apps when capacity changes. Preserve the saved order; the first N ordered apps occupy the Dock and the rest remain in the grid.
- If a later design review approves a separate Dock/Grid layout, treat that as a distinct architecture project with its own focus/scroll regression matrix, not as a hidden refactor inside this feature.

## Target branch baselines and isolated work branches

- E02 owner PR #35 head: `3ab9b55f3cfe03ad9841a119590a9183cf114e96`; source branch: `feature/e02-profile-avatar-lock-ui`.
- E06 owner PR #39 head: `570b7c83efaec3ac635b78f1c6c119b777fc3b84`; source branch: `feature/e06-home-screen-subtabs`.
- New isolated branches created:
  - `feature/e02-giphy-avatar-search-v1` from E02 owner branch.
  - `feature/e06-dock-capacity-control-v1` from E06 owner branch.
- Existing owner branches and PRs remain unchanged. No merge, APK publication/installation, or TV settings changes.

## Required verification

### E02 GIF
- Provider request encoding, attribution, rating, pagination, rate-limit and HTTP error handling.
- Cancel/stale request behavior; malformed/oversized responses.
- Download MIME/magic/byte ceiling; temp cleanup; atomic replacement and failure rollback.
- Profile isolation, active/inactive profile behavior, QR coexistence, offline cache and process death.
- Indonesian/English resources, B01 modal, D-pad/Back/focus, Mi TV Stick RAM/CPU and animated media decoder behavior.

### E06 Dock
- Only values 4/5/6 accepted; invalid/corrupt preference falls back to 5.
- Grid columns remain unchanged when Dock capacity changes.
- Existing app order is unchanged; Dock membership is the first N ordered apps only.
- No app deletion/hiding/reordering; overflow remains in grid.
- Focus identity, D-pad movement, long-press reorder, app cards, Home Settings tile, Dock decoration, top-button alignment, 1080p/4K layout and persistence.
- Indonesian/English settings UI and exact-head CI/APK-size budget.

## Patent / ownership note

The project can establish a distinctive original implementation, design language, documentation, source history and branding. This audit does not establish patentability or legal exclusivity. A patent filing would require a separate novelty/prior-art and jurisdictional legal review. Do not claim an issued patent or legal monopoly.

## Current status

- User approval for both features: YES.
- Upstream source audit: PRELIMINARY PASS.
- Adaptation architecture: DEFINED.
- Isolated E02/E06 work branches: CREATED.
- Application source changes: NOT YET MADE.
- Tests/CI: NOT YET RUN.
- Device acceptance: DEFERRED to the project's approved final device gate.

## User direction update — reuse upstream provider, global UI/localization, Dock orientation

**Update:** 5 November 2026, after direct user clarification.

### GIF provider decision

- Reuse the upstream GIPHY integration flow and provider/catalog (search endpoint, result model, media URL selection and selection callback) as the functional baseline. Do not rebuild a second GIF catalog or invent a new provider backend.
- Clarification: this is not a bundled/local developer-owned GIF database. The inspected upstream code calls GIPHY's hosted API; therefore results and media remain dependent on GIPHY service, API key, network, rate limits and provider terms.
- Retain only provider/data behavior that fits our base. Replace upstream presentation and lifecycle code with Glass TV global components/contracts: B01 modal/dialog, global focus/D-pad, B06 loading/error/empty feedback, B07 focus restoration, and global localization resources (Indonesian + English). Avoid hard-coded text and ad-hoc colors/styles.
- Reuse target E02 profile identity and storage (`profile_avatars` + `ProfileManager.replaceCustomAvatar`); do not adopt upstream's `files/avatars` path or its embedded key. The upstream key is not ours to copy. GIPHY's required attribution and API-key configuration remain mandatory gates.
- Provider adaptation must preserve upstream search/trending behavior only where supported by the pinned source; do not claim a private database or offline catalog. Cache selected assets locally only for profile display/offline rendering.

### Dock orientation decision and simulation

- New user-approved setting: Dock orientation = **Vertical** or **Horizontal**. Keep capacity as a separate setting: **4 / 5 / 6**, default **5**. Both preferences persist independently.
- Vertical means one column/rail with D-pad Up/Down traversal and vertical overflow scrolling. Horizontal means one row with D-pad Left/Right traversal and horizontal overflow scrolling.
- Existing E06 base is a single `RecyclerView` + `GridLayoutManager`; its Dock is currently the first horizontal grid row. `COLUMNS` is simultaneously used for grid span count, Dock threshold, tile sizing, decoration, top-button alignment, focus calculations and move/reorder logic. A simple orientation flag or changing `GridLayoutManager` orientation would corrupt app-grid layout and focus semantics.
- **Simulation outcome:** implement Dock as a logically separate presentation/adapter/container while retaining one authoritative ordered app model. Keep the main app grid as a grid. Dock capacity determines visible Dock slots; overflow stays available in the app grid and no app is deleted/hidden/reordered. Vertical and horizontal modes change only Dock presentation and directional focus/scroll mapping. This decouples Dock capacity from app-grid column count and avoids upstream coupling.
- Keep selected app/focus identity stable when orientation/capacity changes; preserve explicit app order; prevent duplicate/vanishing apps while moving between Dock and grid; test boundary navigation, long-press reorder, app cards and top-bar transitions.
- **Simulation dimensions:** baseline TV viewport 1920×1080 (Mi TV Stick); compare vertical rail and horizontal tray at capacities 4, 5, 6. Check safe margins, tile/icon legibility, focus/glass bounds, visible overflow affordance, top shelf alignment and available grid area. The current single-grid implementation is not yet a visual/device simulation; this is an architecture and interaction simulation contract pending executable preview and host tests.
- This layout separation is a scoped E06 refactor required by the new two-axis Dock controls; it must be isolated on the E06 feature branch and regression-tested, not mixed into unrelated modules.

### Revised implementation sequence

1. E02: import/adapt upstream GIPHY provider/data flow; replace UI with Glass TV global UI and localization; route selected media into E02's private avatar transaction.
2. E06: separate Dock presentation from the main app grid; add persisted orientation and capacity settings; adapt focus, reorder and overflow behavior.
3. Build a reproducible preview/simulation for both orientations × capacities 4/5/6 at 1080p and supported density profiles; then run unit/source-contract, integration and exact-head CI.
4. Physical Mi TV Stick acceptance remains the final device gate. No APK install or TV settings change before explicit approval.

## Revised status

- Upstream provider/data flow reuse: APPROVED.
- Glass TV global UI + localization adaptation: REQUIRED.
- Dock orientation vertical/horizontal: APPROVED.
- Independent Dock capacity 4/5/6: APPROVED; default 5.
- Source implementation, executable preview, tests and CI: NOT YET COMPLETE.
## Implementation slice 1 — 5 November 2026

### E02 provider foundation

- Added `GiphyCatalogClient.java` on `feature/e02-giphy-avatar-search-v1`, commit `37a11b7b59e186377eaa5977584e945ead85e0fe`.
- Added Gradle injection for project-owned `GIPHY_API_KEY` from Gradle property or environment variable; commit `6cb40d98fa152f6322eb6c6bacc96f4805259c76`. No key value is committed. Reminder: a client-side API key distributed in an APK is extractable/public.
- Provider adapter supports G-rated search, query bound, result cap, bounded JSON response, HTTPS and GIPHY media host checks, bounded 8 MiB download, MIME and GIF signature checks, and profile-private `.upload` staging. The caller must complete the existing `ProfileManager.replaceCustomAvatar` transaction.
- Draft PR #62 opened against the E02 owner branch: https://github.com/Frangga11/glass-tv-home/pull/62.
- Not yet integrated: user-facing GIF picker UI, GIPHY attribution in UI, preview animation, animated avatar playback, localization, request cancellation/stale-result suppression, and profile refresh callback. Therefore this is a provider foundation only, not a complete feature.

### E06 Dock preference foundation

- Added `DockPreferences.java` on `feature/e06-dock-capacity-control-v1`, commit `446b94945e683e87901fd5d44e3f40688ede2ace`.
- Model persists orientation (`VERTICAL`/`HORIZONTAL`) and capacity (4/5/6; default 5) independently, and intentionally does not touch `MainActivity.COLUMNS`.
- Draft PR #63 opened against E06 owner branch: https://github.com/Frangga11/glass-tv-home/pull/63.
- Not yet integrated: separate Dock container/adapter, UI settings, scroll/focus/reorder wiring, overflow handling, executable preview/simulation and localization. The preference model alone does not change the current launcher display.

### CI status at checkpoint

- PR #62 head `37a11b7b59e186377eaa5977584e945ead85e0fe`, workflow run #1186 / ID `41890161400`: queued then in progress at last poll; docs/tools job succeeded; Android build job still running. No CI PASS claim.
- PR #63 head `446b94945e683e87901fd5d44e3f40688ede2ace`, workflow run #1187 / ID `41890170063`: docs/tools job succeeded; Android build job still running at last poll. No CI PASS claim.
- No merge, release artifact publication, APK installation or TV setting changes.
## Implementation slice 2 — E02 UI and animated-avatar playback

- Added `AnimatedGifDrawable.java` using Android's platform `Movie` decoder (no new third-party decoder dependency), commit `06325e6dfa9e5835e3dc7bfd1baf8c3002e5628d`; corrected Drawable lifecycle annotation in `d88afa4bdbd3bc84da90c2b4340395028a3469ec`.
- Updated `ProfileManager.getAvatarDrawable` to detect the staged `.gif` extension and return the animated drawable while preserving the profile ring; commit `e296f30669d79e712d7559ec3b880cdbd5c5a251`.
- Staged GIF downloads now retain a `.gif` extension so the existing private profile store can identify the animated asset; commit `7a66358988a26e1329d1d591defe8389cc08480f`.
- Added `GiphyAvatarDialog.java` as the Glass TV dialog shell around the provider adapter, commit `d3f7dcb9094ccef98227707596ed28ec5caa7b45`; dialog close after successful transaction corrected in `029326d44b6e7b2f8b409343f724dae2bdaadc45`.
- Added Indonesian and English strings for the GIPHY picker, loading/empty/error/rate-limit/success states and attribution; commits `e48676433e07de2afc7c7ea14a55bb8f50a6c7de` and `c462e35edbafaf3252513cdf1c2fe9b57504ed98`.
- Added the GIF action to the existing Profile settings avatar actions, preserving QR upload and built-in avatar controls; commit `2ae8a8557c953528286ba44f3a5227d3410b5bfa`.
- Remaining E02 gaps: visual GIF thumbnails/previews in the search result grid, stronger active HTTP cancellation (stale results are suppressed and the worker is shut down on dismissal), end-to-end test coverage for GIF animation on target Android TV, and explicit attribution/legal/provider policy review. UI currently lists selectable results by title rather than showing thumbnail previews.

### Updated CI status

- E06 PR #63 exact head `446b94945e683e87901fd5d44e3f40688ede2ace`, workflow run #1187 / ID `41890170063`: both required jobs succeeded (`Lists, tools and docs`; `Launcher build and unit tests`). This validates only the preference-model foundation, not Dock UI behavior.
- E02 initial run #1186 failed because AGP disabled generated BuildConfig by default. Corrected by enabling `android.buildFeatures.buildConfig true` in commit `a03242376f98f6e32adeec0425ed3c138eb8535c`.
- E02 later run #1196 / ID `41890645498` on current implementation at the time of polling: docs/tools job succeeded; launcher build job was still in progress. Earlier intermediate commit run #1192 succeeded, but it predates the full UI/resource integration and is not a substitute for latest-head CI.
## Implementation slice 3 — integration fixes and preference tests

- Fixed the latest E02 UI compile error in `GiphyAvatarDialog.java`: `TextView.setText` conditional now returns a consistent `CharSequence` type. Commit `c49080c6c52657432043b3950b86238d1a6694a4`.
- E02 PR #62 now includes the GIPHY picker entry in Profile settings, Indonesian/English resources, attribution label, profile-safe GIF storage and platform animated-avatar rendering. It remains incomplete because results are title tiles without animated image thumbnails/previews and no physical playback test has been performed.
- E06 `DockPreferences` now exposes deterministic capacity/orientation normalization and bounds checks (commit `442bda52c392d47fdb7f2483b7a1ad26c1b58001`); added `DockPreferencesStateTest` covering valid/invalid capacity, orientation fallback and Dock position bounds (commit `ecdeb0eba9fc7a574f1a6c9930205517687cd922`).
- E06 PR #63 test-bearing head `ecdeb0eba9fc7a574f1a6c9930205517687cd922`, CI run #1198 / ID `41890835096`: docs/tools job PASS; Android build/unit-test job IN PROGRESS at last poll. Initial model-only run #1187 remains PASS.
- E02 PR #62 current head `c49080c6c52657432043b3950b86238d1a6694a4`, CI run #1199 / ID `41890891814`: docs/tools job PASS; Android build/unit-test job IN PROGRESS at last poll.
- Both PRs remain Draft and unmerged. Main and existing owner branches remain untouched; no artifact publication, APK installation or TV settings change.