# Cumulative Home Integration Checkpoint — 11 November 2026

## Purpose
Temporary integration candidate for reconciling the two-row Dock/Grid DPAD focus work. This is NOT a final canonical baseline and is NOT a release branch.

## Branch and starting point
- Branch: `integration/glass-tv-home-cumulative-2026-11-11`
- Starting commit: `a87b7af2c60f79d4d83d1011ec8a6ba7f007f015` (PR #129 source head)
- PR #129 CI on this exact source head: success (runs 42898216570 and 42899464289)
- PR #129 APK had user-reported positive Mi TV Stick acceptance. This does not validate the wider integration candidate.

## Included by ancestry
- PR #115 two-row Dock/Grid navigation base.
- PR #123 older common-ancestor portion only; PR #123 current head contains 9 additional commits not present in this branch.
- PRs #125, #126, #127, #128 and #129 through the #124 lineage.
- The PR #128 semantic DPAD intent and PR #129 Dock-focus precommit model are retained.

## Not yet reconciled
- PR #122 burst serialization: branch diverged from this candidate; its expansion guard is not present in the current candidate's `MainActivity.java`. PR #131 is open and GitHub reports mergeable=false.
- PR #117 stable Dock anchor lineage: branch diverged; PR #132 is open and GitHub reports mergeable=false. The later state-machine branch has a permanent Dock anchor, so changes must be compared semantically rather than blindly merged.
- Remaining PR #123 commits: PR #133 is open and GitHub reports mergeable=false. The #124–#129 chain branched from an older #123 base.
- PR #124 itself remains open/unmerged; the branch history is divergent against PR #123.

## Validation
- PR #115 exact head CI: success (run 42856200589).
- PR #117 exact head CI: success (run 42861676073).
- PR #122 exact head CI: success (run 42874843246).
- PR #129 exact head CI: success (runs 42898216570 and 42899464289).
- No CI result yet for this newly created integration branch.
- No claim of full integration or full physical-device acceptance.

## Safety / decision
- `main` was not modified.
- Do not merge this candidate into `main`.
- Do not branch future feature work from this candidate until PRs #131–#133 are reconciled, exact-head CI passes, and the integrated APK receives physical Mi TV Stick regression acceptance.
- Next: resolve conflicts in `MainActivity.java`, `AppAdapter.java`, `Glider.java`, `GridFocusDecoration.java`, and corresponding tests while preserving the latest semantic focus ownership and cyan breathing indicator.
