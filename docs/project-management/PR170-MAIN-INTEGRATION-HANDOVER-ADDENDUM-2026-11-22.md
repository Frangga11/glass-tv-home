# Latest Handover Addendum — PR #170 cumulative baseline integrated into main
Date: 22 November 2026

This addendum is the latest continuation authority for the PR #170 → main integration workstream. It supersedes the earlier pre-merge PR #170 / PR #162 synchronization statements in `HANDOVER.md` that said the synchronized candidate was still separate from `main`, or that a merge to `main` had not yet occurred. Those entries remain historical records of the pre-merge state.

- **Current branch:** `main`
- **Verified merge commit / main HEAD at merge:** `296f73346fb0aaa8fe07fde19eb2dc55d92fcae2`
- **Merged PR:** [#172 — Integrate PR #170 cumulative baseline into main](https://github.com/Frangga11/glass-tv-home/pull/172)
- **PR #172 source head:** `44aa868aa80875442c67e5f01cbdddd95f0a5612`
- **Exact PR-head CI:** run `44994112614`, SUCCESS; both required jobs passed.
- **Authoritative accepted PR #170 source:** `2208afbc896f6b08d699156714e7d58dc964f721`.
- **Immutable QA ref:** `baseline/pr-170-qa-exact-2026-11-22`; do not move or mutate.
- **New detailed checkpoint:** [GTVH-PR170-MAIN-INTEGRATION-CHECKPOINT-2026-11-22.md](GTVH-PR170-MAIN-INTEGRATION-CHECKPOINT-2026-11-22.md).

## Carry-forward constraints
- Preserve rounded capsule clipping, measured Glider rim bounds, one glass rim only, initial focus seeding, and all locked dimensions/layout/spacing/DPAD/focus/Dock/app/animation behavior.
- RE-SKIN / MATERIAL SYNC only; no rewire.
- PR #62 remains OPEN / DRAFT / UNMERGED / DEFERRED and untouched.
- PR #171 remains CLOSED / UNMERGED. PR #153 and #154 remain closed unmerged; PR #155 remains merged.
- The owner-confirmed device acceptance remains limited to the exact PR #170 tested APK/source. The newer PR #172 candidate has not been independently device-accepted by this record.
- No release publication is claimed.

## Current quality gate
PR #172 was merged only after its exact source-head CI passed. This addendum itself triggers a new CI run on the updated `main`; record and verify that exact final-main run before declaring documentation closeout complete. The resulting final-main SHA, not the pre-documentation merge SHA above, becomes the latest continuation point once its CI is green.
