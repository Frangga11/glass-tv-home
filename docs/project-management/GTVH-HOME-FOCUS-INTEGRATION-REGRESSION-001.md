# GTVH-HOME-FOCUS-INTEGRATION-REGRESSION-001

Date: 11 November 2026  
Repository: `Frangga11/glass-tv-home`  
PR: pending creation from this branch  
Branch: `fix/home-focus-integration-regression-001`  
Base: PR #125 exact source HEAD `33ea36ff4891f5879a070d668e4f28bf56106c81`

## Consolidated decisions

- Canonical app-card focus is the existing cyan breathing rim/glow, with the original white-to-cyan rim, top glint and 1600 ms reverse pulse.
- Dock row and expanded Grid row reuse the same `DockBreathingBorderView` from `item_app.xml`.
- No additional white Glider/Grid cursor is registered. The legacy Grid decoration is not authoritative.
- Focus decoration must not cover app banner, icon or label.
- Dock/Grid transitions must not tween Grid alpha or alter tile geometry, app order, inventory or launch listeners.
- Idle Home shows the primary Dock only. DOWN reveals the overflow row; UP returns to the corresponding Dock column.
- The collapse end callback must clear both translations, commit idle layout, then restore focus to the requested Dock position.

## Cross-audit findings and corrective change

PR #123 documented that a repeated direction event during Dock collapse could invalidate the transition's generation token and strand cleanup/focus restoration. PR #124 corrected the double-applied vertical displacement, and PR #125 restored the user-approved cyan breathing visual across both rows.

The integrated PR #125 source still allowed Grid direction handling and repeated Dock focus requests while `horizontalDockCollapseInProgress` was true. Those requests could advance `gridFocusRequestGeneration` before the collapse callback completed.

This corrective branch adds two guards:
1. `focusDockTile()` does not steal focus while the collapse animator owns the hand-off.
2. `onTileVerticalKey()` consumes Grid direction input while collapse is in progress, preventing a new row transition from invalidating the collapse generation.

This deliberately serializes the collapse boundary rather than replaying queued input. It prioritizes deterministic cleanup and prevents a stale transition. A future queued-input implementation must preserve the same generation ownership and must be separately tested; this change does not claim queued replay.

## Regression coverage added

- Same cyan focus view and original pulse remain shared by Dock/Grid.
- Collapse owns focus until idle geometry is committed.
- Grid key handling cannot start a competing row transition during collapse.
- Collapse clears translations before applying idle layout and restores the same Dock position.
- No second static Grid overlay is registered.

## Validation gates

- Source-contract test and full CI/build must pass on the exact final HEAD.
- Physical Mi TV Stick test is still required for visual parity, banner/icon/label visibility, DPAD navigation, rapid repeated UP/DOWN, Dock return visibility, app activation and unrelated Home controls.
- Do not merge, release or claim device PASS before physical acceptance.


## CI artifact routing

The dedicated Home layout test APK and cumulative integration QA APK are both enabled for PR #126. The workflow file is part of this PR so the artifact-routing change is versioned and reviewable with the source correction.
