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
- UP from the first Grid row collapses the expanded surface and returns focus to the Home header after the collapse settles.
- The existing deterministic four-way neighbor mapping and hard row boundaries remain in force.

### App Move Mode

- The selected app remains identified by its stable component key while its adapter position changes.
- When a moved Grid app crosses the visible viewport, the Grid scrolls to reveal it; the Dock follows the same measured scroll delta.
- A delayed holder/layout pass must not end Move Mode merely because the moved tile is temporarily not attached.
- UP at Grid row one is a hard boundary in horizontal Grid Move Mode. It does not move the app into the Dock and does not end Move Mode.
- The existing explicit OK/BACK handling remains the mode exit/confirmation path.

## Implementation

- Added an expanded Grid scroll baseline in `MainActivity`.
- Extended the existing Grid scroll listener so the Dock translation is derived from the Grid's measured vertical scroll offset relative to that baseline.
- Captured the baseline after the initial Dock/Grid expansion settles and reset it after collapse.
- During Grid Move Mode, request layout/scroll for the moved stable app identity when its holder is outside the viewport; retry using the current adapter position resolved from that identity.
- Changed normal UP from Grid row one to complete the existing synchronized collapse, then dispatch the UP intent to the Home header.
- Kept Move Mode UP at row one within the Grid region as a hard boundary.

## Scope lock

Only horizontal Grid viewport progression, Dock synchronization, and the related Grid Move Mode boundary/reveal behavior are changed. No visual styling, card dimensions, spacing, glass treatment, app inventory policy, Dock capacity mapping, wallpaper, Home shelves, Ayat, prayer timetable, popups, Wi-Fi, or unrelated feature behavior is intentionally changed.

## Validation

- Added `tests/test_home_progressive_grid_expansion_contract.py` for pixel-synchronized Dock tracking, expansion/collapse baseline, normal row-one UP behavior, and Move Mode persistence/boundary.
- Updated the existing Home DPAD contract to expect collapse-to-header behavior.
- Automated CI/build and unit tests must pass before this work is considered code-validated.
- Physical Mi TV Stick remote testing is still required for acceptance. No physical-device PASS is claimed by this document.

## Status

OPEN / DRAFT / UNMERGED. No merge or release authorization is implied.
