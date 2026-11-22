# PR #170 Integration Checkpoint — 22 November 2026

## Integration result
- PR: #170 — Fix Quick Settings Glider capsule corners and focus geometry.
- Status: **CLOSED / MERGED**.
- Target/base branch: `feat/popup-bright-frosted-silver-001`.
- PR #170 head at merge: `2208afbc896f6b08d699156714e7d58dc964f721`.
- Merge commit: `e47c7686233b4dd998fc24b7875e5bc235baecad`.
- Merge method: merge commit.
- PR #170 was marked Ready for Review immediately before merge; merge used expected head SHA `2208afbc...` to guard against head movement.

## Verified QA checkpoint
- Reference branch: `baseline/pr-170-2dp-qa-2026-11-22`.
- APK size: 1,653,150 bytes.
- SHA-256: `33724e5d3daba097178e943434c685d9feafeb502a8bf5d65284f62b9a5b6955`.
- Exact-head CI run: `44981544358`, required build/unit-test and docs/list jobs SUCCESS.
- Artifact ID: `14737539929`; extracted APK validated as ZIP and AndroidManifest presence verified.
- Release-publisher workflow was SKIPPED. This was a CI QA APK, not a release publication.
- No fresh physical Mi TV Stick test is claimed here.

## PR queue disposition
- #171: CLOSED / UNMERGED; do not revive or reuse.
- #170: CLOSED / MERGED into `feat/popup-bright-frosted-silver-001`.
- Live open-PR search after merge: #169, #168, #167, #166, #165, #164, #162, #161, #158, #62. No open PR above #170.
- The lower-numbered PRs are not automatically obsolete. PR #169 is retained as a separate open candidate/parent history; other open items remain outside this integration action. PR #62 remains deferred.

## Boundary for main integration
- This action merged PR #170 only into its declared base branch; it did not merge to `main`.
- Pre-merge comparison of the parent cumulative line to `main`: 96 commits ahead, 1 behind; merge base `71f3a051a25fd66cabfd1740756cc36442dc92cc`.
- Next gate before main: audit the cumulative diff file by file, reconcile the one-commit divergence on a dedicated branch, run exact-head CI, and verify the resulting APK.
- Preserve dimensions, spacing, DPAD graph, focus ownership, Glider animation, Dock/app movement, and popup structure. RE-SKIN, NOT REWIRE.


## Exact source ref clarification
- Exact tested source is pinned separately at `baseline/pr-170-qa-exact-2026-11-22` → `2208afbc896f6b08d699156714e7d58dc964f721`.
- `baseline/pr-170-2dp-qa-2026-11-22` now includes documentation-only commits after its initial creation; its current tip is not the tested APK source commit.
