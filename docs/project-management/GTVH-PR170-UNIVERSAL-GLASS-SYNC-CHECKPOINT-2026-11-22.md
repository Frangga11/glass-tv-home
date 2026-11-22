# PR #170 Baseline — Universal Glass Sync Checkpoint
Date: 22 November 2026

## Decision and governing baseline
- Owner direction: PR #170 is the authoritative baseline for this workstream. Do not substitute `main`.
- Exact owner-accepted PR #170 source: `2208afbc896f6b08d699156714e7d58dc964f721`.
- Immutable reference: `baseline/pr-170-qa-exact-2026-11-22` → the exact SHA above. This ref was not modified.
- Dedicated integration branch: `integration/pr170-universal-glass-sync-001`, created directly from the exact accepted source SHA.

## Baseline reconciliation
- PR #162 originally targeted `main`; this created an incorrect comparison for the current delivery.
- Comparing its head against PR #170 showed the broad Universal Glass material adoption already exists in the PR #170 cumulative ancestry.
- The only remaining application-source delta was in `launcher/app/src/main/java/com/gathin/tvhome/UniversalGlassMaterial.java`: Wi-Fi pointer backdrop drawing now uses the pointer bounds and paints the backdrop before clipping the triangle's gradient/reflection pass.
- No additional capsule dimensions, layout, spacing, DPAD routing, focus ownership, Glider behavior, dock/app movement, or animation changes were introduced by this sync.

## PR #162 integration
- PR #162 retargeted to `integration/pr170-universal-glass-sync-001`.
- Exact merged PR head: `b9866be19db1159f3a856199f8079c77bd50f7a4`.
- PR #162 status: CLOSED / MERGED.
- Merge commit: `7df0a5bcf82b8fc67863abb886997e3b3df47de9`.
- Retargeted exact-head CI run: `44993172112`.
  - Launcher build and unit tests: SUCCESS.
  - Lists, tools and docs: SUCCESS.
  - Cumulative integration QA artifact step: SUCCESS.
- Documentation synchronization commits on the integration branch:
  - HANDOVER: `580baa133a8515a0d170706d607215154151170f`
  - PR #170 checkpoint: `a6bad43d0ad4a7fa6d8d7c58eb5631b867c7648a`
  - Open PR audit: `2e9a0846b0ba611617f04ebc457dbd58f75bb38d`
  - Universal Glass checkpoint: `a4c240eccb28ab180213c8ceadcdc0a182e1a1a5`

## PR #170 findings — mandatory invariants
1. Clip the Glider companion to the actual rounded capsule silhouette; never rectangularly clip the moving shadow into dark corner wedges.
2. Align the outer Glider rim to the measured capsule bounds; do not introduce an arbitrary inset.
3. Render a single glass rim. Do not stack a second cyan focus stroke over the glass rim.
4. Seed the Glider immediately at the initially focused capsule after the dialog appears.
5. Preserve actual capsule width/height, layout, spacing, DPAD graph, focus ownership, Home dock/app behavior, and existing animations.
6. Keep the change RE-SKIN / MATERIAL SYNC only; no rewire.

## Acceptance and validation boundary
- The owner-confirmed physical-device acceptance applies to the exact PR #170 APK/source snapshot only.
- The synchronized Wi-Fi pointer source delta is a new candidate; no independent Mi TV Stick acceptance is claimed for it.
- Retargeted PR CI passed on the exact PR head. Post-merge CI on the final merge/documentation head must pass before declaring this checkpoint fully validated.
- Do not publish a release or merge this integration branch into `main` as part of this checkpoint.
- PR #62 remains OPEN / DRAFT / DEFERRED and untouched. PR #171 remains CLOSED / UNMERGED.

## Current next gate
Run/verify CI on the final integration branch HEAD after the documentation commits, then retrieve and verify the cumulative QA APK artifact (ZIP integrity, APK manifest, size and SHA-256). Physical-device QA remains a separate owner acceptance gate for the post-sync APK.
