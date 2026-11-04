# Upstream Feature Adoption — Animated GIF Avatar & Dock Capacity

**Decision ID:** U-ADOPT-2026-11-05-01  
**Date:** 5 November 2026  
**Status:** APPROVED FOR ADAPTATION / SOURCE IMPLEMENTATION NOT YET STARTED  
**Source project:** `aqmangge/glass-tv-home` (upstream v4.1 reference)  
**Target project:** `Frangga11/glass-tv-home`

## User-approved scope

Adopt only these two capabilities from the upstream application:

1. **Animated GIF search for profile avatars** — a user can search an online GIF catalog, preview a result, select it, and assign it to the currently selected profile.
2. **Configurable Dock capacity** — allow exactly 4, 5, or 6 visible Dock app slots; default remains 5.

All other features shown in the supplied reference screenshots are explicitly out of scope for this adoption.

## Important source discrepancy

The supplied screenshot labels the GIF search provider as **Giphy**. The upstream repository's current user guide describes its existing GIF avatar search as **Tenor**. Do not assume these are the same implementation. Audit the upstream source and provider/API contract, then implement the user's explicitly requested Giphy capability unless a documented technical/legal blocker requires a decision.

## Ownership and dependency

- GIF avatar capability belongs to **E02 — Profile / Avatar**. E02 source branch and PR must remain the owner of profile state, avatar selection, and avatar persistence.
- Dock capacity belongs to **E06 — Home Screen**. E06 source branch and PR must remain the owner of Dock layout, visible slot count, app ordering, and Home focus behavior.
- Do not move either responsibility into E01 or a shared module merely for convenience.
- Implement as two separate owner-branch changes and separate Draft PRs. Preserve existing dependency heads and D-013 staged-merge policy.

## Required behavior — GIF avatar

- Search only after explicit user input; provide loading, empty, error, retry, and rate-limit states.
- Show selectable animated previews and a clear selected/focused state using the shared TV dialog/focus contract.
- Save the selected GIF to the active profile only after explicit confirmation; never overwrite another profile's avatar.
- Persist a stable provider asset reference and a locally cached asset where appropriate; handle cache eviction/offline startup without a broken avatar.
- Bound image dimensions, decoded memory, download size, and concurrent requests for low-memory Android TV devices.
- Keep provider credentials out of source control and the APK; audit API key handling, attribution, terms, privacy, content filtering, and rate limits before selecting the production integration.
- Preserve existing avatar options, QR upload, profile isolation, localization, DPAD/Back behavior, and persistence/recovery contracts.
- Support Indonesian and English.

## Required behavior — Dock capacity

- Expose exactly three choices: **4 / 5 / 6**; default **5** for existing and new installations.
- Changing capacity changes only the number of visible Dock slots. It must not delete apps, alter the saved app order, or lose apps beyond the visible capacity; overflow remains available in the app grid according to the existing ordering contract.
- Persist the preference and restore it after process death/reboot.
- Recompute layout and spacing without clipping or disproportionate scaling at supported display sizes/densities.
- Preserve existing Dock focus, selected app, reorder/move behavior, DPAD navigation, accessibility labels, and fallback surfaces.
- Support Indonesian and English.

## Audit and implementation gates

1. Pin the exact upstream commit and inspect the actual GIF provider implementation, API usage, dependencies, license/terms, cache and failure behavior.
2. Inspect current E02 and E06 owner heads before editing; map existing preference keys, profile model, avatar storage, Dock layout and focus restoration.
3. Record conflicts and adopt/adapt/keep/reject decisions. Do not copy upstream code blindly.
4. Implement GIF and Dock capacity independently on their owning branches; add focused unit/source-contract and regression tests.
5. Run exact-head CI on each owner branch; verify APK-size budget, localization parity, low-memory behavior, persistence, and existing QR/profile/Dock regressions.
6. Mirror narrowly scoped fixes into any active integration candidate only through the approved reconciliation process.
7. Physical Mi TV Stick acceptance remains a separate final device gate; host CI must not be represented as TV acceptance.

## Explicit exclusions

No changes to wallpaper, weather/AQI, UI animations, UI sounds, prayer settings, Gemini assistant, child mode, or other upstream features as part of this decision. No early merge, APK publication/installation, or TV setting changes.

## Current disposition

The user has approved the two-feature scope. This document records the decision and acceptance contract only. **No application source code has been changed and neither feature is claimed implemented or tested yet.**

## User clarification — implementation style and Dock orientation (5 November 2026)

- Reuse the upstream GIPHY provider/catalog and data flow as the functional baseline; do not rebuild a separate GIF database/provider. The inspected upstream is an API client to GIPHY, not a bundled developer-owned database.
- Keep provider/data behavior; replace presentation with Glass TV global UI, shared focus/dialog/feedback behavior and Indonesian/English localization. Integrate selected media into E02's existing private profile-avatar storage/transaction. Do not copy the upstream embedded API key or parallel storage path; retain required GIPHY attribution and provider-policy compliance.
- Add independent Dock settings: **orientation Vertical / Horizontal** and **capacity 4 / 5 / 6** (default 5).
- Vertical = one-column Dock rail navigated/scrollable Up/Down. Horizontal = one-row Dock tray navigated/scrollable Left/Right.
- Keep the app grid as a grid and Dock as its own presentation surface, backed by one ordered app model. Capacity/orientation must not change the app-grid column count, delete apps, reorder apps, or hide overflow. Preserve stable focus and long-press reorder behavior.
- A separate Dock presentation is required because the current E06 single-RecyclerView implementation couples Dock membership and grid geometry. Do not implement orientation by merely changing the current GridLayoutManager orientation.
- Verify 1080p/4K, density, focus, scroll, app-card and reorder behavior in an executable preview and exact-head CI before device acceptance.