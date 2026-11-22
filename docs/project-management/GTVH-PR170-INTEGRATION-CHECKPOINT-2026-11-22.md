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


## Physical Mi TV Stick acceptance — owner-confirmed
- Owner confirms the APK from exact source commit `2208afbc896f6b08d699156714e7d58dc964f721` was tested on the target Mi TV Stick and works well.
- Device gate for this cumulative candidate: **PASS**.
- Scope includes PR #169's parent popup material implementation and PR #170's Quick Settings Glider/focus geometry correction.
- This is owner-reported acceptance, not an independently executed device test by the assistant. It does not extend to later source changes or unrelated PRs.


## Follow-on integration — Universal Glass sync — 22 November 2026

- Owner direction: use PR #170 as the authoritative baseline for subsequent synchronization; do not substitute `main`.
- Dedicated integration branch: `integration/pr170-universal-glass-sync-001`, created at exact accepted PR #170 source `2208afbc896f6b08d699156714e7d58dc964f721`.
- PR #162 was retargeted to that branch. Its effective source delta against PR #170 was only the Wi-Fi pointer backdrop drawing adjustment in `UniversalGlassMaterial.java`; the rest of the Universal Glass adoption already existed in the cumulative PR #170 ancestry.
- PR #162 merged: merge commit `7df0a5bcf82b8fc67863abb886997e3b3df47de9`; exact merged head `b9866be19db1159f3a856199f8079c77bd50f7a4`.
- Retargeted PR CI run `44993172112` passed both required jobs. Post-merge CI on the merge commit and final docs commit must still pass before declaring closeout complete.
- This does not modify the immutable exact-source pin and does not merge to `main`. Physical device acceptance remains specific to the tested PR #170 APK; the synchronized build is a new QA candidate.
- Preserve all PR #170 focus/capsule findings and locked geometry/DPAD/animation invariants for future work.


## Post-sync QA candidate
- PR #170 baseline synchronization merged as described above. QA candidate source before the final documentation update: `4d43dcc35c76d389015dcec26abf61dab21dae2c`.
- Exact-head CI run `44993626885`: both required jobs SUCCESS.
- QA artifact ID `14743495296`; archive size 1,454,550 bytes; archive SHA-256 `c54a7032718c7ffc8235658db9295c92cc1227009d57cc67912983518238f40c`.
- Extracted APK size 1,653,162 bytes; SHA-256 `7131197a4a6d4e10989ccf516c48b1b6fb59ed2440852e70bb7670fe0da1fbce`. APK archive integrity and manifest presence verified.
- This new APK is not represented as physically accepted. The previous owner-confirmed device acceptance is scoped to the exact PR #170 source snapshot only.
