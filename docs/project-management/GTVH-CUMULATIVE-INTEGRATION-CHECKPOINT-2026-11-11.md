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
- PR #122 burst serialization: both the Dock reveal guard and row-scroll coalescing have been selectively ported directly onto this candidate. The active scroll animator is preserved; later settle logic resolves the newest stable app key against decorated row geometry. Regression contracts cover both behaviors. PR #131 was closed without merge.
- PR #117 stable Dock anchor lineage: audited as already represented by the permanent `dockParams.bottomMargin = dockBottomMargin` anchor and translation-only motion in the candidate. PR #132 was closed without merge as redundant.
- Remaining PR #123 commits: audited and PR #133 closed without merge. Its all-direction queued replay and white static focus conflict with the later single semantic UP/DOWN intent (#129) and shared cyan breathing focus (#125).
- PR #124 itself remains open/unmerged; the branch history is divergent against PR #123.

## Validation
- PR #115 exact head CI: success (run 42856200589).
- PR #117 exact head CI: success (run 42861676073).
- PR #122 exact head CI: success (run 42874843246).
- PR #129 exact head CI: success (runs 42898216570 and 42899464289).
- CI run 42901903450 completed SUCCESS on HEAD `d8b34dadc044fd16481ed20fde10b6bffc2f24d7`; both Launcher build/unit tests and Lists/tools/docs jobs passed. This validates the pre-coalescing integration state, not the current latest commits.
- Burst serialization ported: reveal guard MainActivity commit `5c4ebc3e14bf1e01514f4f7b839f585c9e2fb5b8`; reveal regression test commit `1fc6a5085af7aff065419ad1a975e8fac9cc25c4`.
- Grid row-scroll coalescing ported: MainActivity commit `92e006caf2f861789fbbfb662f928ea68a3ae62f`; regression-test commit `15e9e18189743217bcd6bcf978dd43f972fab9ce`. Exact latest-HEAD CI is pending.
- No claim of full integration or full physical-device acceptance.

## Safety / decision
- `main` was not modified.
- Do not merge this candidate into `main`.
- Do not branch future feature work from this candidate until the latest exact-head CI passes, the remaining PR #122 row-scroll coalescing behavior is audited, and the integrated APK receives physical Mi TV Stick regression acceptance.
- PR #132 and #133 closed as superseded; no code was merged from either.
- PR #131 closed without merge after selective port of its required behavior.
- `main` remains unchanged.
