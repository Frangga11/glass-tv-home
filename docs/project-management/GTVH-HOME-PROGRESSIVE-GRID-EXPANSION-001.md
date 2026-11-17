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
- When Grid row three joins, the visible frame is Dock row one plus Grid rows two and three. UP from Grid row three moves to Grid row two; UP again focuses the raised Dock row one without collapsing; UP from the raised Dock collapses the full frame and returns to the idle anchor.
- The viewport is a rolling window of up to two visible Grid rows beneath the Dock. When Grid row three first appears, Grid row two remains visible above it and the Dock remains above both Grid rows. As later rows are reached, the two-row Grid window advances without pinning the focused row to its top edge.
- The initial one-row Grid state and the existing Dock-to-Grid reveal remain unchanged. When the three-row total frame is active, every Home overlay/capsule/text element is temporarily hidden except the clock and date (`leftBlock` remains visible), including location/altitude, weather/AQI, navigation, Wi-Fi/microphone, wallpaper title/description, prayer schedule, Daily Ayah, and other Home elements. Their original visibility is restored when the frame collapses.
- The existing deterministic four-way neighbor mapping and hard row boundaries remain in force.

### App Move Mode

- The selected app remains identified by its stable component key while its adapter position changes.
- When a moved Grid app crosses the visible viewport, the Grid scrolls to reveal it; the Dock follows the same measured scroll delta.
- A delayed holder/layout pass must not end Move Mode merely because the moved tile is temporarily not attached.
- UP at Grid row one transfers the selected app to Dock row one when capacity allows. If the Dock is full, the app remains picked up in the Grid; Move Mode never exits or sends focus to Home chrome.
- Move Mode keeps the selected app visible inside the same rolling two-row Grid window beneath the Dock while it is repositioned. Long-OK on an app in any visible row must open the context menu without focus escaping to Home controls.
- The existing explicit OK/BACK handling remains the mode exit/confirmation path. When moving DOWN from the Dock, the hidden Grid is expanded first and Move Mode resumes on the same app identity after layout.

## Implementation

- Added an expanded Grid scroll accumulator in `MainActivity`.
- Extended the existing Grid scroll listener so the Dock translation is derived from the actual consumed vertical `dy` reported by the Grid scroll callback, accumulated in pixels.
- Reset the accumulated scroll delta after the initial Dock/Grid expansion settles and after collapse.
- During Grid Move Mode, request layout/scroll for the moved stable app identity when its holder is outside the viewport; retry using the current adapter position resolved from that identity. Repositioning aligns the two-row Grid window beneath the Dock rather than jumping the moved tile to the top.
- Changed normal row transitions and focus restoration to align the first row of a rolling three-row window, not the focused row itself.
- Added a reversible Home-chrome visibility gate for three-row mode. It preserves each affected view's original visibility and leaves the clock/date container (`leftBlock`) visible.
- Changed normal UP from the first Grid row to complete the existing synchronized collapse while retaining focus on the Dock row-one destination.
- Kept Move Mode UP at row one within the Grid region as a hard boundary.

## Scope lock

Only horizontal Grid viewport progression, Dock synchronization, and the related Grid Move Mode boundary/reveal behavior are changed. No visual styling, card dimensions, spacing, glass treatment, app inventory policy, Dock capacity mapping, wallpaper, Home shelves, Ayat, prayer timetable, popups, Wi-Fi, or unrelated feature behavior is intentionally changed.

## Validation

- Added/extended `tests/test_home_progressive_grid_expansion_contract.py` for pixel-synchronized Dock tracking, two-row Grid window alignment, Home-chrome visibility rules (including clock/date), collapse restoration, and Move Mode persistence/boundary.
- Updated the existing Home DPAD contracts to expect collapse-to-Dock-row-one behavior.
- Automated CI/build and unit tests must pass before this work is considered code-validated.
- Physical Mi TV Stick remote testing is still required for acceptance. No physical-device PASS is claimed by this document.

## Status

OPEN / DRAFT / UNMERGED. No merge or release authorization is implied.

## Follow-up audit 002 (three-row frame, focus and Move Mode)

Root cause: the expanded Grid viewport was always ONE row tall. When row three (Grid row index 1) was reached, the window start was computed as the focused row itself, so row two scrolled out of the viewport and was clipped. UP from row three then targeted a row that was not attached, the transition fell back to `focusTopBarFallback()` and focus escaped to the header. Long-OK on row three and Move Mode into row three hit the same missing holder.

Changes:

- `horizontalGridViewportRows` (1 or 2 Grid rows). Reaching Grid row index 1 (the third overall row: Dock + Grid row 1 + Grid row 2) expands the total frame to three visible rows. The frame stays latched while focus moves UP from overall row three to overall row two, then to the raised Dock; the next UP from the Dock collapses the whole frame.
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

The Dock scroll offset is now derived from laid-out Grid geometry rather than only accumulated scroll callbacks. When the rolling window advances, the absolute measured offset is rebased to the new first row so later scrolling cannot double-count the row that was already passed. The clock/date block remains visible while other Home chrome is hidden and is re-hidden after focus-driven UI refreshes. Moving an app DOWN from the Dock opens the hidden Grid through the normal expansion transition and re-enters Move Mode once the target holder is laid out; DPAD/OK input is captured during that hand-off. A guarded recovery path releases the hand-off lock if the transition cannot complete.

Scope remains limited to Grid frame geometry, focus ownership, Dock scroll synchronization, and Move Mode hand-off. No weather, wallpaper, prayer/Ayah, popup, glass styling, or app inventory changes. Physical Mi TV Stick testing is still required for acceptance.

## Follow-up audit 004 — correct row-three threshold and coordinate-stable Move Mode

The third overall application row is Grid row index 1 because the Dock is overall row one and Grid row index 0 is overall row two. The expansion threshold is therefore `targetRow >= 1`, not `targetRow >= 2`. This ensures the second Grid row enters the viewport as soon as focus reaches it; the viewport then shows Dock + two Grid rows.

Move Mode now retains the selected app's stable component key for the full pickup session. Before each DPAD step it resolves the key to the current adapter position; LEFT/RIGHT/UP/DOWN targets are calculated from that current position through `DockLayoutModel.gridNeighborPosition`, which preserves the latest column on vertical movement and respects incomplete-row boundaries. After each reorder, the selected key is resolved again and the new position is used for restoring the tile. This prevents stale adapter positions from pulling the selected app back to its earlier slot.

Acceptance scenario: start a Move Mode pickup in a Grid row, move UP or DOWN, move RIGHT/LEFT to another column, then reverse vertically. The next row transition must use the new column, not the initial pickup column or the row's first slot. Automated tests cover the threshold and stable-identity/coordinate contract. Physical Mi TV Stick remote testing remains mandatory.

## Follow-up audit 005 — raised Dock navigation, Dock-to-Grid placement, and Move Mode shadow

Three device findings were traced to separate paths:

1. In the latched three-row frame, horizontal movement on the raised Dock was calling `focusDockTile()`. That helper collapses any expanded horizontal frame. LEFT/RIGHT now use the non-collapsing raised-Dock focus path only while the two-Grid-row frame is active. UP remains the only Dock key that collapses the three-row frame; DOWN returns to the matching Grid column. Raised-Dock focus is resolved by stable app key and retried until the Dock card really owns focus, so a failed `requestFocus()` cannot silently leave the logical cursor behind.
2. Dock Move Mode DOWN previously called the generic `unpinFromDock()`, which left the app in its old arranged Grid slot. A dedicated transfer now inserts the selected app into the Grid at the current Dock column. It preserves the order of the other Grid apps and persists the new arrangement. The subsequent vertical Move Mode steps continue from that live position.
3. Move Mode temporarily attached one shared `DefaultItemAnimator` instance to both RecyclerViews. Full `notifyDataSetChanged()` reorders can retain old-holder snapshots and transient transforms, visually resembling a second moving shadow. Move Mode now keeps RecyclerView item animation disabled and resets the selected/previous card transforms synchronously. Cross-region transfer clears the old glider shadow before the destination card becomes active.

Cross-checks added: raised-Dock LEFT/RIGHT frame latch and focus retry; Dock-to-Grid insertion at the selected slot and vertical column preservation; absence of RecyclerView move-animation snapshots and stale rotation transforms. No Home styling, weather, wallpaper, prayer/Ayah, popup, normal app inventory order, or unrelated DPAD edges were intentionally changed. Physical Mi TV Stick validation remains required, especially the sequence: Grid row two UP to Dock, LEFT/RIGHT across Dock, DOWN back to the Grid; then re-enter Dock and UP to collapse.


## Follow-up audit 006 — visual focus geometry and Dock-owned Glider synchronization

The focus shape now accounts for translation on the focused card and every ancestor up to the Home root. This closes the mismatch where Android descendant-rectangle conversion used layout/scroll coordinates while the raised Dock was visually translated.

When the actual focused card belongs to the Dock, Grid scroll deltas are no longer applied to the Dock's Glider as though it were a Grid child. Instead, the Glider is synchronized from the Dock card's measured visual bounds. The same synchronization runs on each update frame of the Dock raise, three-row viewport animation, collapse, and scroll-driven Dock repositioning. During these coordinated animations the Glider is hard-synced to the Dock's own animation progress rather than repeatedly retargeting independent springs.

Move Mode retains the live stable-key resolution and Dock-slot insertion path from PR #148; no fallback to the older stale-position move sequence is introduced. Additional source contracts cover ancestor translation, Dock/Grid scroll ownership, per-frame synchronization, and the current Dock slot used for DOWN transfer.

Scope remains focus geometry, Dock/Glider synchronization, and Move Mode placement validation. The three-row frame and DPAD routing are not rewritten. Automated validation and physical Mi TV Stick acceptance remain required.


Validation note: the Dock-to-Grid transfer contract now verifies that the selected stable key is resolved to its current Dock slot immediately before converting that slot to a Grid insertion position. This guards against stale caller-supplied positions after lateral Move Mode shifts.

## Follow-up audit 007 — defer Home chrome restoration until frame shrink commits

A review of the three-row viewport transition found that `animateGridViewportRows(false, ...)` restored Home chrome at animation start through `setThreeRowFrameChromeHidden(grow)`. The Dock and Grid were still visibly transitioning from the three-row frame to the one-row frame, so the clock-adjacent Home chrome could reappear behind an unfinished transition.

The transition now hides chrome before a grow begins, but retains the hidden state during a shrink. The shrink end callback first commits the one-row viewport geometry and only then restores the original Home chrome visibility. A source-contract test locks this ordering. The normal DPAD route and the three-row frame geometry are unchanged.

This is a source-level correction; CI and physical Mi TV Stick validation are still required. It does not authorize merging PR #149 or this follow-up.



## Follow-up audit 008 — latest DPAD and Move Mode findings

The latest 94-second Mi TV Stick recording exposes three linked issues:

- Focus appeared only after Dock-to-Grid reveal completed, and coordinator-driven Grid scroll moved the Glider with the outgoing ViewHolder before the new row acquired focus.
- A raised-Dock focus callback could call `showHero()` while the three-row frame remained latched. Restoring visibility on a later posted frame allowed wallpaper title/description and navigation capsules to flash. Dock Move Mode could also reveal the wallpaper copy on row one.
- Dock-to-Grid Move Mode insertion was calculated against the pre-transfer Grid snapshot. When an overflow-pinned app filled the Dock vacancy, that app was filtered back into the Dock after the insertion index was selected, shifting the moved app one column left.

Corrections on the follow-up branch:

- Acquire focus on an attached first-row Grid card before the Dock reveal motion; synchronize the Glider to the actual focused Dock or Grid card through container translations. During coordinator-driven row scroll, keep the cursor in the destination screen slot rather than scrolling the old cursor away.
- Use a shared 240ms duration for adjacent Grid-row and Dock reveal transitions.
- Suppress Home shelf presentation during Move Mode and the latched three-row frame, and reassert hidden frame chrome synchronously in the focus callback.
- Rebuild the Dock/Grid partition after unpinning so overflow-pinned entries refill the Dock vacancy first; insert the moved app into the resulting Grid at its live Dock column.

Regression tests cover the overflow-pinned vacancy round trip, early focus acquisition, cursor behavior during row scroll, and Home chrome suppression. Automated CI is still required, and physical Mi TV Stick acceptance remains outstanding. Keep the change unmerged until the same DPAD and Move Mode sequences pass on device.


## Follow-up audit 009 — Move Mode shadow, focus glide, immediate Home restore

Three additional observations from the latest device test:

1. Move Mode shadow differed between Dock row one and Grid rows two/three because the Dock could enter from the DARK focus path while Grid entered from GLASS. The shadow renderer is only active in GLASS; pane hiding alone does not normalize that style.
2. The Dock/Grid transition correctly acquired focus before moving, but hard-syncing the Grid Glider on every animation frame cancelled its spring motion. The row movement remained fast, but the focus capsule lost its visible glide.
3. On UP from the raised Dock, the collapse completion restored Home view visibility while the Hero and Islamic shelf alpha could remain zero. Dock already owned focus, so no subsequent focus callback occurred to restore those shelves.

Corrections:
- Added one explicit Move Mode visual state: force GLASS + fully enabled black drop shadow and hide only the pane, for both Dock and Grid; restore the user's configured normal focus style when Move Mode ends and after each holder rebind.
- During existing Dock/Grid and viewport animations, keep Dock focus hard-synced to its translated tray, but retarget the Grid Glider's spring toward the moving Grid card. This restores visible focus motion without adding a second animation phase or delaying DPAD focus.
- At the end of UP collapse, after the three-row frame is removed and the Dock is back at its idle anchor, rebuild the wallpaper Hero text and set visible Hero/Islamic shelves to full alpha immediately. This does not wait for LEFT/RIGHT.

Regression coverage checks identical Move Mode shadow state, spring retargeting in the existing transition duration, and Home shelf restoration within the UP collapse callback. CI and physical Mi TV Stick acceptance are still required; keep the PR unmerged until the three cases pass on device.

## Follow-up audit 010 — restore source-to-destination focus glide and continuous Move Mode shadow

The 17 November Mi TV Stick recording showed two remaining visual defects:

1. On the first DOWN from Dock row one to Grid row two, the focus capsule appeared to enter from the lower edge instead of travelling down from the focused Dock icon.
2. During Move Mode, the black shadow did not use the same continuous movement as Grid row-two/row-three moves when the selected app crossed between Dock and Grid.

Source review identified the following causes:

- The Dock-to-Grid reveal acquired focus on the Grid target before the reveal animation began. That made the Glider target the Grid card while its container was still translated below the visible row. The source Dock card no longer owned focus for the duration of the reveal.
- A cross-region Move Mode transfer called the ordinary finish path, which restored normal Glider state, and then called `glider.hideNow()`. The destination therefore received a newly shown shadow rather than the same moving shadow used by in-Grid row changes.
- The Grid branch of `syncFocusedAppGlider` had a hard-sync alternative during Move Mode. That cancelled spring movement and made its motion inconsistent with ordinary Grid focus changes.

Corrections in `fix/home-dpad-origin-glide-movemode-shadow-003`:

- Remove pre-reveal Grid focus acquisition. Keep the source Dock tile focused and tracked through the Dock/Grid reveal; acquire the Grid target only after its final row position is committed. The focus capsule can then glide from the Dock icon into Grid row two.
- Add a Move Mode handoff finish path that stops the source card wiggle and clears the old holder state but retains the same GLASS Glider and black shadow.
- Keep `moveHandoffPending` active while the destination RecyclerView holder is prepared. Suppress frame-by-frame retargeting to a stale/recycled source holder during this interval; the destination focus callback retargets the retained Glider to the new card.
- Use the same spring-based `glider.moveTo()` path for Grid focus in and out of Move Mode. Dock remains hard-synced only while the Dock itself is physically translated by the coordinated container animation.
- If the hidden-Grid handoff times out, explicitly restore normal Glider state and hide the stale Move Mode shadow before fallback focus.

Regression contracts now cover source-first Dock-to-Grid focus timing, retained cross-region shadow identity, handoff input gating, and a shared spring path for Grid focus. DPAD direction mapping, row/column calculations, app inventory order, layout geometry, wallpaper, popup, and visual skin are not intentionally changed.

**Validation status:** source and regression-contract changes are committed on the stacked follow-up branch. CI must pass before this change is considered code-validated. Physical Mi TV Stick acceptance is still required for: (a) one-press DOWN from row one with a visible top-to-bottom focus glide, and (b) identical black-shadow travel between row one/two and rows two/three in both directions. Keep the PR Draft and unmerged until those device observations are confirmed.
