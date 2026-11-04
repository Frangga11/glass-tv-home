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
