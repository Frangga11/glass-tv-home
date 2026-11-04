# E02 GIF Avatar + E06 Dock — Source Audit and Next-Stage Work Plan
Date: 5 November 2026
Repository: `Frangga11/glass-tv-home`

## Scope and exclusions
This checkpoint covers only:
- E02 animated GIF avatar catalog/search, preview, selection, storage and playback.
- E06 independent Dock orientation (Vertical/Horizontal) and visible capacity (4/5/6, default 5).

G03 / physical Mi TV Stick acceptance is explicitly out of scope. No device installation, ADB mutation, HOME/default-launcher change, APK publication, PR merge or integration/main ref update was performed.

## Exact source heads and CI evidence

| Workstream | PR | Branch head | CI run | Required jobs |
|---|---:|---|---:|---|
| E02 GIF avatar | #62 | `ea9415ec3f63f599872a5c3acda153e30f84e200` | #1200 / `41891880056` | Launcher build + unit tests SUCCESS; Lists/tools/docs SUCCESS |
| E06 Dock | #63 | `ecdeb0eba9fc7a574f1a6c9930205517687cd922` | #1198 / `41890835096` | Launcher build + unit tests SUCCESS; Lists/tools/docs SUCCESS |

Both PRs are OPEN, Draft, unmerged. E02 targets `feature/e02-profile-avatar-lock-ui` at `3ab9b55f3cfe03ad9841a119590a9183cf114e96`; E06 targets `feature/e06-home-screen-subtabs` at `570b7c83efaec3ac515b78f1c6c119b777fc3b84`. The branches are isolated and not yet rebased/combined with the latest integration tree. A successful CI run proves build/tests for that exact isolated head only; it does not prove feature acceptance or integration compatibility.

## E02 — audit findings

### Verified present
- GIPHY API key is supplied through Gradle property/environment variable; no key is committed in this branch.
- Search uses HTTPS, a G-rated query, bounded query length, result count and JSON response size.
- Media URLs are restricted to HTTPS GIPHY hosts; redirects are disabled; download size is capped at 8 MiB; MIME and GIF signature are checked.
- GIF is staged under private app storage and handed to `ProfileManager.replaceCustomAvatar`.
- Indonesian/English strings and visible Powered By GIPHY attribution are present.
- Platform `Movie` drawable is used for GIF playback without adding a decoder dependency.

### Open defects / gaps
1. Result cells are text-only; `previewUrl` is collected but not rendered. Thumbnail/animated preview is a core user-visible requirement and is not implemented.
2. Search worker uses `shutdownNow()` on dismiss, but the blocking `HttpURLConnection` is not explicitly disconnected/cancelled. Generation checks prevent stale UI updates, but do not guarantee network cancellation or prevent queued selection work from committing after dismissal.
3. Selection worker has no explicit cancellation/commit gate tied to dialog lifecycle. A request already in progress can outlive the dialog.
4. `AnimatedGifDrawable` schedules itself every 33 ms while drawing and has a `stop()` method, but the current integration does not show a reliable lifecycle owner that calls `stop()` when an avatar drawable is replaced/detached. This risks unnecessary frame callbacks after profile/avatar changes.
5. GIF decode/playback and malformed/large animation behavior need source-level tests and memory bounds; current CI success alone does not establish safe TV playback.
6. DPAD navigation, focus restoration, empty/error/loading states with preview cells, attribution visibility at actual dialog bounds, profile switch during search/selection, storage replacement/rollback and previous-avatar preservation need explicit regression coverage.
7. Provider terms/rating and attribution requirements must be verified against current GIPHY requirements before feature-ready; do not assume a G-rated query alone establishes all policy compliance.

### Required implementation sequence
E02-A: define a cancellable request handle/token that owns and disconnects active connections; suppress commit after cancellation/dismissal/profile change.
E02-B: add bounded preview loading with per-holder request identity/recycling cancellation, decode off main thread, placeholder/error state, and no unbounded bitmap retention.
E02-C: make GIF playback lifecycle-aware (start/stop on drawable visibility/attachment/replacement) and constrain decode/memory work.
E02-D: add tests for URL validation, response/download bounds, signature rejection, cancellation, stale response, staging cleanup, replacement failure and avatar fallback.
E02-E: test DPAD, focus, localization, attribution and dialog lifecycle; exact-head CI and APK size gate.

## E06 — audit findings

### Verified present
- `DockPreferences` persists orientation and capacity independently in its own preference namespace.
- Allowed capacities 4/5/6 and fallback default 5 are tested.
- Orientation parsing safely falls back to HORIZONTAL; Dock position bounds have unit coverage.
- The preference model explicitly says it must not change `MainActivity.COLUMNS`.

### Root architectural blocker
The current home screen uses one `RecyclerView` / `GridLayoutManager(COLUMNS)`, `AppAdapter(..., COLUMNS, cfg)`, and `DockDecoration(COLUMNS, density)`. The first COLUMNS app positions are treated as the Dock. Thus Dock size, grid columns, Dock decoration, focus index and layout are coupled. Merely changing `DockPreferences` or the decoration threshold will either change the app grid or create a visual/focus mismatch.

### Open requirements
1. No separate Dock container/adapter exists.
2. No settings UI writes/reads the Dock preferences.
3. No live wiring of the selected capacity into the visible pinned slots/overflow.
4. No vertical rail or horizontal tray rendering; no six-combination executable preview/simulation.
5. No independent focus graph/DPAD navigation, scroll/overflow, reorder semantics, or focus restoration across orientation/capacity changes.
6. No regression tests proving main-grid columns remain unchanged, Dock order persists, app cards/hidden apps behave correctly, or focus does not jump.
7. Localization and accessibility/content descriptions are not complete.

### Required implementation sequence
E06-A: introduce one authoritative ordered app model separating pinned Dock membership from the main app-grid dataset; preserve stable app identity.
E06-B: add a separate Dock view/adapter with Horizontal row and Vertical rail layouts. Capacity controls visible slots only; overflow remains reachable without dropping apps.
E06-C: wire orientation/capacity settings into the Home Screen settings tab with localized labels and persisted selection.
E06-D: implement orientation-aware DPAD, scroll, focus restoration, reorder and app launch behavior; define transitions when a pinned app disappears or is hidden.
E06-E: add executable layout simulations/previews at 1920×1080 and supported density profiles for all six combinations; add unit/source regression tests.
E06-F: run exact-head CI, integration conflict audit and APK budget check.

## Integration and regression constraints
- Do not merge either Draft PR as-is.
- Reconcile the dependency bases against the current approved integration tree before combined validation; the PRs are based on separate owner feature heads and are substantially behind/diverged from `integration/b-series`.
- Preserve E02 and E06 owner branches. Fix each defect on its owning branch.
- Verify the main app grid column count remains unchanged by E06 orientation/capacity.
- Keep APK size gate and current baseline in view; prior G02 APK headroom was only 571 bytes.
- Physical Mi TV Stick evaluation remains deferred until the user explicitly announces that phase.

## Quality disposition
- E02 isolated-head build and unit tests: PASS (CI #1200, both required jobs).
- E06 isolated-head build and unit tests: PASS (CI #1198, both required jobs).
- E02 feature acceptance: FAIL / INCOMPLETE (no thumbnails; cancellation/lifecycle and regression coverage incomplete).
- E06 feature acceptance: FAIL / INCOMPLETE (preference model only; rendering, settings and navigation not implemented).
- Cross-branch integration/regression: NOT RUN.
- Device acceptance: NOT IN SCOPE.

**Overall: BELUM FINAL.** Next work is E02-A through E02-E and E06-A through E06-F, performed on owner branches with exact-head evidence. Do not report feature PASS until each acceptance requirement is implemented and tested.
