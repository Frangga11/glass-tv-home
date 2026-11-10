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
- PR #123 older common-ancestor portion, including the outline-only foreground Grid overlay; its remaining 9 commits were audited and closed as superseded (PR #133).
- PRs #125, #126, #127, #128 and #129 through the #124 lineage.
- The PR #128 semantic DPAD intent and PR #129 Dock-focus precommit model are retained.

## Not yet reconciled
- PR #122 burst serialization: the Dock reveal guard and regression contract have been ported directly onto this candidate. PR #131 remains open only as a historical source branch and must not be merged; the remaining row-scroll coalescing change requires separate verification against the current row-transition implementation.
- PR #117 stable Dock anchor lineage: audited as already represented by the permanent `dockParams.bottomMargin = dockBottomMargin` anchor and translation-only motion in the candidate. PR #132 was closed without merge as redundant.
- Remaining PR #123 commits: audited and PR #133 closed without merge. Its all-direction queued replay and white static focus conflict with the later single semantic UP/DOWN intent (#129) and shared cyan breathing focus (#125).
- PR #124 itself remains open/unmerged; the branch history is divergent against PR #123.

## Validation
- PR #115 exact head CI: success (run 42856200589).
- PR #117 exact head CI: success (run 42861676073).
- PR #122 exact head CI: success (run 42874843246).
- PR #129 exact head CI: success (runs 42898216570 and 42899464289).
- CI run 42901230265 completed SUCCESS for code SHA `a87b7af2c60f79d4d83d1011ec8a6ba7f007f015` (build, unit tests, docs/tools checks, APK diagnostics). It does NOT validate the subsequent integration commits.
- Burst serialization ported on 11 Nov 2026: MainActivity commit `5c4ebc3e14bf1e01514f4f7b839f585c9e2fb5b8`; regression-test commit `1fc6a5085af7aff065419ad1a975e8fac9cc25c4`. Fresh exact-HEAD CI is required.
- No claim of full integration or full physical-device acceptance.

## Safety / decision
- `main` was not modified.
- Do not merge this candidate into `main`.
- Do not branch future feature work from this candidate until the latest exact-head CI passes, the remaining PR #122 row-scroll coalescing behavior is audited, and the integrated APK receives physical Mi TV Stick regression acceptance.
- PR #132 and #133 closed as superseded; no code was merged from either.
- PR #131 remains OPEN/DRAFT/UNMERGED and is not safe to merge blindly.
- `main` remains unchanged.
