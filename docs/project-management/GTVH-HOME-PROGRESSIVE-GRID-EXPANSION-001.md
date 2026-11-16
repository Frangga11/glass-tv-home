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
- The viewport is a rolling window of up to three visible Grid rows, with the Dock remaining a separate row above: the established two-row step remains unchanged; when Grid row three first appears, Grid rows one and two remain visible above it and the Dock remains above the Grid. As later rows are reached, the three-row Grid window advances without pinning the focused row to its top edge.
- The initial one-row Grid state and the existing Dock-to-Grid reveal remain unchanged. When the three-row app frame is active, every Home overlay/capsule/text element is temporarily hidden except the clock and date (`leftBlock` remains visible), including location/altitude, weather/AQI, navigation, Wi-Fi/microphone, wallpaper title/description, prayer schedule, Daily Ayah, and other Home elements. Their original visibility is restored when the frame collapses.
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
- Added a reversible Home-chrome visibility gate for three-row mode. It preserves each affected view's original visibility and includes the complete clock/date container (`leftBlock`).
- Changed normal UP from the first Grid row to complete the existing synchronized collapse while retaining focus on the Dock row-one destination.
- Kept Move Mode UP at row one within the Grid region as a hard boundary.

## Scope lock

Only horizontal Grid viewport progression, Dock synchronization, and the related Grid Move Mode boundary/reveal behavior are changed. No visual styling, card dimensions, spacing, glass treatment, app inventory policy, Dock capacity mapping, wallpaper, Home shelves, Ayat, prayer timetable, popups, Wi-Fi, or unrelated feature behavior is intentionally changed.

## Validation

- Added/extended `tests/test_home_progressive_grid_expansion_contract.py` for pixel-synchronized Dock tracking, three-row window alignment, Home-chrome visibility rules (including clock/date), collapse restoration, and Move Mode persistence/boundary.
- Updated the existing Home DPAD contracts to expect collapse-to-Dock-row-one behavior.
- Automated CI/build and unit tests must pass before this work is considered code-validated.
- Physical Mi TV Stick remote testing is still required for acceptance. No physical-device PASS is claimed by this document.

## Status

OPEN / DRAFT / UNMERGED. No merge or release authorization is implied.

## Follow-up audit 002 (three-row frame, focus and Move Mode)

Root cause: the expanded Grid viewport was always ONE row tall. When row three (Grid row index 1) was reached, the window start was computed as the focused row itself, so row two scrolled out of the viewport and was clipped. UP from row three then targeted a row that was not attached, the transition fell back to `focusTopBarFallback()` and focus escaped to the header. Long-OK on row three and Move Mode into row three hit the same missing holder.

Changes:

- `horizontalGridViewportRows` (1 or 2 Grid rows). Reaching Grid row three expands the total frame to three visible rows: Dock row one plus two Grid rows. The frame stays latched while focus moves UP from Grid row three to row two, then to the raised Dock; the next UP from the Dock collapses the whole frame.
- Rolling window start is `max(0, targetRow - 1)`; Home chrome is hidden as soon as row three joins the frame and restored when the full frame collapses; the clock/date block remains visible. Home chrome is re-hidden after focus callbacks so it cannot flash back on.
- `animateGridViewportRows` grows/shrinks the frame as one motion (Dock + row two rise together, row three enters from below). Final geometry is always committed even if a newer key cancels it.
- `dockRaisedTranslationY()` is the single Dock offset formula (dock pitch + extra viewport rows + scroll). The Dock fades out once the rolling window scrolls past it.
- Collapse lowers the whole frame from its current state (any rows/scroll) and resets rows, scroll and Grid scroll position.
- UP on the raised Dock collapses the frame and keeps focus on Dock row one.
- `focusTopBarFallback()` never leaves Move Mode or an open frame for the Home header.
- Move Mode: UP from Grid row one hands the app to Dock row one (mirror of Dock DOWN). If the Dock is full the app is picked up again, so Move Mode never ends.

Open item: pressing UP during Dock-row Move Mode escaping to the Daily Ayah button could not be reproduced by static reading; UP is now consumed explicitly in `stepMove`. Needs physical-remote confirmation.


## Follow-up audit 003 — latched three-row frame and Move Mode hand-off

The supplied patch defines the three visible rows as Dock row one plus two Grid rows. The frame remains latched after Grid row three appears: UP from row three moves to row two, UP from row two focuses the raised Dock without collapsing, and UP from the raised Dock lowers the full frame while retaining Dock focus. The original two-row state remains unchanged. Dock movement is interpolated from its measured pre-grow scroll offset to the new frame anchor to avoid a final-position jump.

The Dock scroll offset is now derived from laid-out Grid geometry rather than only accumulated scroll callbacks. The clock/date block remains visible while other Home chrome is hidden and is re-hidden after focus-driven UI refreshes. Moving an app DOWN from the Dock opens the hidden Grid through the normal expansion transition and re-enters Move Mode once the target holder is laid out; DPAD/OK input is captured during that hand-off. A guarded recovery path releases the hand-off lock if the transition cannot complete.

Scope remains limited to Grid frame geometry, focus ownership, Dock scroll synchronization, and Move Mode hand-off. No weather, wallpaper, prayer/Ayah, popup, glass styling, or app inventory changes. Physical Mi TV Stick testing is still required for acceptance.
