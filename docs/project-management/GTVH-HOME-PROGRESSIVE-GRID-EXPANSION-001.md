# GTVH-HOME-PROGRESSIVE-GRID-EXPANSION-001

## Objective

Make every app row reachable in horizontal Dock mode through a progressive, rolling viewport. Keep the Dock and Grid row one synchronized as a single visual motion unit. Preserve the selected app identity and Move Mode while the Grid viewport reveals further rows.

## Baseline and isolation

- Repository: `Frangga11/glass-tv-home`
- Parent PR: #141 — `fix/home-dpad-three-row-navigation-001`
- Parent HEAD: `7c288b821cab9e4ddf5ca926aa200733cc02729d`
- Work branch: `fix/home-progressive-grid-expansion-001`
- This is an isolated follow-up. PRs #139 and #140 are not modified.

## Required behavior

### Normal navigation

- The initial Home state remains unchanged: Dock and app Grid overflow are at rest/hidden.
- DOWN from the Dock reveals the first Grid row using the existing coordinated Dock/Grid expansion.
- As the Grid scrolls to reveal later rows, the Dock follows the Grid's actual vertical scroll offset pixel-for-pixel. The Dock and row one therefore rise together instead of the Dock remaining parked while the Grid moves independently.
- UP from Grid row three to row two to row one keeps focus inside the Grid. UP from row one collapses the complete frame, returns rows two/three below the viewport, and restores Dock + row one to the initial anchor; focus remains on Dock row one, not Beranda.
- The viewport is a rolling window of up to three visible app rows (Dock row one plus two Grid rows): the established two-row step remains unchanged; when Grid row three first appears, Grid row two remains visible above it and the Dock remains the first row. As later rows are reached, the two-row Grid window advances without pinning the focused row to its top edge.
- The two-row state is unchanged. Home chrome is hidden only when the viewport reaches three rows; clock and date remain visible. Location/altitude, weather/AQI, navigation, Wi-Fi/microphone, wallpaper title/description, prayer schedule, Daily Ayah, and other Home capsules/text are temporarily hidden and restored to their prior visibility when the frame collapses.
- The existing deterministic four-way neighbor mapping and hard row boundaries remain in force.

### App Move Mode

- The selected app remains identified by its stable component key while its adapter position changes.
- When a moved Grid app crosses the visible viewport, the Grid scrolls to reveal it; the Dock follows the same measured scroll delta.
- A delayed holder/layout pass must not end Move Mode merely because the moved tile is temporarily not attached.
- UP at Grid row one is a hard boundary in horizontal Grid Move Mode. It does not move the app into the Dock and does not end Move Mode.
- Move Mode keeps the selected app visible inside the same rolling three-row window while it is repositioned. Long-OK on an app in any visible row must open the context menu without focus escaping to Home controls.
- The existing explicit OK/BACK handling remains the mode exit/confirmation path.

## Implementation

- Added an expanded Grid scroll accumulator in `MainActivity`.
- Extended the existing Grid scroll listener so the Dock translation is derived from the actual consumed vertical `dy` reported by the Grid scroll callback, accumulated in pixels.
- Reset the accumulated scroll delta after the initial Dock/Grid expansion settles and after collapse.
- During Grid Move Mode, request layout/scroll for the moved stable app identity when its holder is outside the viewport; retry using the current adapter position resolved from that identity. Repositioning aligns the three-row window rather than jumping the moved tile to the top.
- Changed normal row transitions and focus restoration to align the first row of a rolling three-row window, not the focused row itself.
- Added a reversible Home-chrome visibility gate for three-row mode. It preserves each affected view's original visibility and deliberately excludes the clock/date block.
- Changed normal UP from the first Grid row to complete the existing synchronized collapse while retaining focus on the Dock row-one destination.
- Kept Move Mode UP at row one within the Grid region as a hard boundary.

## Scope lock

Only horizontal Grid viewport progression, Dock synchronization, and the related Grid Move Mode boundary/reveal behavior are changed. No visual styling, card dimensions, spacing, glass treatment, app inventory policy, Dock capacity mapping, wallpaper, Home shelves, Ayat, prayer timetable, popups, Wi-Fi, or unrelated feature behavior is intentionally changed.

## Validation

- Added/extended `tests/test_home_progressive_grid_expansion_contract.py` for pixel-synchronized Dock tracking, three-row window alignment, Home-chrome visibility rules (clock/date excluded), collapse restoration, and Move Mode persistence/boundary.
- Updated the existing Home DPAD contracts to expect collapse-to-Dock-row-one behavior.
- Automated CI/build and unit tests must pass before this work is considered code-validated.
- Physical Mi TV Stick remote testing is still required for acceptance. No physical-device PASS is claimed by this document.

## Status

OPEN / DRAFT / UNMERGED. No merge or release authorization is implied.
