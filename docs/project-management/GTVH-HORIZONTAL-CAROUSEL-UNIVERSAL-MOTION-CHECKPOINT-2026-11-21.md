# Horizontal Carousel & Glider Acceptance Checkpoint — 21 November 2026

## Authoritative source
- Repository: `Frangga11/glass-tv-home`
- Workstream: PR #159 — Fix horizontal carousel clipping and Universal motion on latest Quick Settings baseline
- Branch: `fix/home-horizontal-carousel-universal-001`
- Base: `feature/quick-settings-single-frame-glass-001` (PR #156)
- Implementation head before this documentation synchronization: `b1c12b52a7c4784f9758bd9b487e5549d6e50a59`
- PR #159 remains the isolated horizontal-carousel change set; its scope is not expanded by this checkpoint.

## Accepted behavior
- Owner tested the QA APK on the target setup and reported the horizontal movement / Glider behavior as **“Sempurna”**.
- Preserve the existing shared spring-driven Glider focus box. During adjacent horizontal DPAD moves that pan the carousel, the Glider remains anchored to the Universal focus lane while the app row moves underneath; on return to the first slot, it springs back into that slot.
- Preserve `DockBreathingBorderView` as a separate card-rim treatment. Glider and breathing rim are complementary and both remain enabled.
- Vertical / Classic navigation and Move Mode remain outside this correction.
- Do not alter card/icon dimensions, spacing, tile geometry, dock placement, clipping, scroll-tail behavior, or unrelated popup/UI behavior in follow-up work unless a separate approved task requires it.

## Root cause and implementation
- The horizontal pan previously called `moveToScreenImmediately()` at the start and on every scroll-animation frame. That repeatedly cancelled the Glider spring transition and caused focus jumps.
- `MainActivity.java` now separates spring movement from immediate synchronization through `moveFocusedDockGlider(boolean immediate)`.
- Adjacent sequential DPAD moves that require carousel panning defer the Glider movement and keep it anchored to the Universal focus lane while the row pans.
- Returning to the first slot releases the anchor and lets the Glider spring back. Programmatic/non-adjacent focus changes are excluded from the anchored path.
- Anchor/defer state is reset on pause and when leaving Horizontal mode. Move Mode is not routed through this correction.
- Regression contract coverage was added in `tests/test_home_dock_islamic_shelf_navigation_refinement_contract.py`.

## Change boundary
- The Glider implementation itself was not removed or rewritten.
- `DockBreathingBorderView`, card geometry, icon sizing, spacing, dock layout, clipping, scroll-tail, and Vertical / Classic navigation were preserved.
- The focused final correction changed only `MainActivity.java` and the horizontal-navigation contract test relative to the preceding PR #159 head.

## Automated verification
- Exact implementation head: `b1c12b52a7c4784f9758bd9b487e5549d6e50a59`.
- GitHub Actions CI run #2489 / `44918592733`: SUCCESS.
- Required jobs: Launcher build and unit tests — SUCCESS; Lists, tools and docs — SUCCESS.
- Release publisher workflow for this PR head was skipped; this does not represent a formal release.
- Cumulative QA artifact: ID `14712099341`, name `glass-tv-home-cumulative-integration-127-6f62feec987885ad8133d45a893fb5e34f7d1eef`.
- Extracted QA APK: 1,649,218 bytes. SHA-256: `bd526fb06fac12aca97685c82206579314ee7f4b2f925042f6c5278aca166501`. APK archive integrity check passed.
- The artifact is a cumulative CI QA APK, not a signed store/release publication.

## Integration disposition

- Owner acceptance is recorded for the tested horizontal Glider / DPAD behavior; the owner reported the result as **“Sempurna”** on the target Mi TV Stick.
- PR #159 was **CLOSED / MERGED** into the PR #156 parent branch on 21 November 2026.
- Merge commit / resulting PR #156 head: `f8c1f554fff4379bb3bd4d426e3218fcc820b9e1`.
- The parent PR #156 exact-head CI run #2493 / `44919477582` completed **SUCCESS** on that exact merge commit. Both required jobs passed: Launcher build and unit tests; Lists, tools and docs.
- PR #159 is no longer stacked/open. Its accepted horizontal-carousel implementation is now part of the PR #156 branch; the parent relationship to `main` remains unchanged.
- Horizontal Carousel / Glider code integration, owner acceptance record, documentation synchronization, and exact-head CI verification are **COMPLETE**.
- PR #156 itself remains OPEN / DRAFT / UNMERGED pending its own Quick Settings physical-device acceptance gates and explicit owner approval. This checkpoint does not authorize merging PR #156 to `main`.
- `main` remains unchanged at `0662c80bf80a6df06248262eeb0599a93aaba3e5`.
- PR #62 remains deferred and untouched.
- Any subsequent defect must be handled as a new, narrowly scoped corrective task. Do not disturb the accepted Glider motion.
