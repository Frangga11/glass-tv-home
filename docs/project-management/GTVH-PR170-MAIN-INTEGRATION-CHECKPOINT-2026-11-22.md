# PR #170 → main Integration Checkpoint — 22 November 2026

## Authoritative result
- Repository: `Frangga11/glass-tv-home`
- Governing accepted source: PR #170 exact source `2208afbc896f6b08d699156714e7d58dc964f721`.
- Immutable QA pin: `baseline/pr-170-qa-exact-2026-11-22` → `2208afbc896f6b08d699156714e7d58dc964f721`. Do not move or mutate.
- PR #170 cumulative baseline and synchronized Universal Glass work were reconciled onto `main` through PR #172.
- PR #172: **CLOSED / MERGED** on 22 November 2026.
- PR #172 source head: `44aa868aa80875442c67e5f01cbdddd95f0a5612`.
- Merge commit and verified `main` HEAD immediately after merge: `296f73346fb0aaa8fe07fde19eb2dc55d92fcae2`.
- Previous `main` HEAD: `49d8cd5e1285823cb0e23da3fc9bd3ac516d9f7f`.
- PR: https://github.com/Frangga11/glass-tv-home/pull/172

## Reconciliation record
- Dedicated integration source: `integration/pr170-universal-glass-sync-001`, containing the PR #170 baseline and synchronized PR #162 work.
- PR #172 was built from the then-current `main` and reconciled the exact final contents of 23 paths from the synchronized integration branch. This handled the independent add/add history around `UniversalGlassMaterial.java` without blindly overwriting the main-side version.
- Result was a source-tree reconciliation onto main, not a replay of the historical 130-commit ancestry.
- `UniversalGlassMaterial.java` in the integration candidate was verified byte-for-byte against the synchronized integration source.
- PR #172 changed 23 files (983 additions, 179 deletions).

## Exact-head CI and merge gate
- Exact PR #172 source SHA: `44aa868aa80875442c67e5f01cbdddd95f0a5612`.
- GitHub Actions run `44994112614`: **SUCCESS**. Both required jobs passed: `Launcher build and unit tests` and `Lists, tools and docs`.
- PR metadata reported `mergeable=true`, `mergeable_state=clean` before merge.
- Merge was performed using expected head SHA `44aa868aa80875442c67e5f01cbdddd95f0a5612`.
- Post-merge verification confirmed PR #172 `merged=true`, state `closed`, merge SHA `296f73346fb0aaa8fe07fde19eb2dc55d92fcae2`, and `main` branch SHA exactly matched that merge SHA.
- **Important:** the CI result above validates the exact PR source head before merge. A fresh CI run on the final main documentation head is a separate closeout gate and must be recorded independently.

## Mandatory PR #170 invariants carried forward
1. Clip Glider companion to the actual rounded capsule silhouette; no rectangular clipping/corner wedges.
2. Align outer Glider rim to measured capsule bounds; no arbitrary inset drift.
3. One glass rim only; no stacked cyan focus outline.
4. Seed Glider immediately on the initially focused capsule.
5. Preserve capsule dimensions, layout, spacing, DPAD graph, focus ownership, Dock/app behavior and existing animations.
6. RE-SKIN / MATERIAL SYNC only; no rewire.

## QA and acceptance boundaries
- Owner-confirmed physical-device acceptance applies only to the exact PR #170 tested source/APK: source `2208afbc896f6b08d699156714e7d58dc964f721`, APK SHA-256 `33724e5d3daba097178e943434c685d9feafeb502a8bf5d65284f62b9a5b6955`.
- Post-sync QA candidate before PR #172: source `4d43dcc35c76d389015dcec26abf61dab21dae2c`; CI run `44993626885` SUCCESS; APK 1,653,162 bytes; SHA-256 `7131197a4a6d4e10989ccf516c48b1b6fb59ed2440852e70bb7670fe0da1fbce`.
- The PR #172 reconciled candidate is a newer source tree. Do not claim the old PR #170 device acceptance automatically covers this newer candidate. Separate Mi TV Stick acceptance remains outstanding unless owner explicitly confirms testing this exact candidate.
- No release publication is included in PR #172.

## Protected PRs and refs
- PR #62 remains OPEN / DRAFT / UNMERGED / DEFERRED. Do not modify, merge, or reuse its code without a separate audit and explicit approval.
- PR #171 remains CLOSED / UNMERGED.
- PR #153 and #154 remain CLOSED / UNMERGED historical candidates; PR #155 remains merged.
- The immutable PR #170 QA pin remains unchanged.

## Next exact actions
1. Run and verify CI on the final documentation-updated `main` SHA after this checkpoint and handover addendum are committed.
2. Record that exact final-main CI run and its two required jobs in the handover.
3. Treat `main` at the resulting verified SHA as the new official continuation point.
4. Build a separate QA APK from that exact final main SHA only when requested; do not call it device-accepted until the owner tests that exact APK on the Mi TV Stick.

## Final disposition
- PR #170 cumulative baseline integration into `main`: **MERGED**.
- PR #172 exact-source CI: **PASS**.
- Post-merge main documentation CI: **PENDING**.
- New candidate physical-device acceptance: **PENDING / NOT CLAIMED**.
- PR #62: **DEFERRED / UNTOUCHED**.
