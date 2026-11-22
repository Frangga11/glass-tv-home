# PR #170 Baseline and Integration Synchronization Checkpoint — 22 November 2026

## Authoritative starting point
- Repository: `Frangga11/glass-tv-home`
- Baseline PR: #170 — Fix Quick Settings Glider capsule corners and focus geometry.
- Exact baseline commit: `2208afbc896f6b08d699156714e7d58dc964f721`.
- Reference branch: `baseline/pr-170-2dp-qa-2026-11-22` (created at the exact baseline commit).
- Verified APK: `Glass-TV-Home-PR170-2208afbc.apk`, 1,653,150 bytes.
- SHA-256: `33724e5d3daba097178e943434c685d9feafeb502a8bf5d65284f62b9a5b6955`.

## CI and artifact verification
- CI run: `44981544358` — required build/unit-test and lists/tools/docs jobs SUCCESS.
- QA artifact archive ID: `14737539929`; extracted `app-release.apk` size 1,653,150 bytes.
- APK ZIP integrity and AndroidManifest presence verified; SHA-256 exactly matches the owner's checkpoint.
- Release publisher workflow was SKIPPED. Do not describe this QA artifact as a release publication.
- This checkpoint records CI/APK verification only. It does not claim a new physical Mi TV Stick test.

## PR disposition
- #170: OPEN / DRAFT / UNMERGED; it is the active baseline candidate.
- #171: CLOSED / UNMERGED; do not revive or reuse.
- Live open-PR search found no open PR numbered higher than #170.
- #169 remains open as the parent/preceding stack; lower-numbered PRs are not automatically obsolete merely because #170 is the chosen checkpoint. Do not close or merge those unrelated/parent PRs without an individual audit.
- PR #62 remains a separate deferred workstream; do not pull it into this integration.

## Integration comparison and safety gate
- Compare: `main` → `baseline/pr-170-2dp-qa-2026-11-22`.
- Result: `diverged`; baseline line is 107 commits ahead and 1 commit behind.
- Merge base: `71f3a051a25fd66cabfd1740756cc36442dc92cc`.
- The cumulative diff includes source changes in DockDecoration, Glider, GliderFocusDrawable, MainActivity, ModernSettingsDialog, QuickSettingsDialog, SettingsFocusCursorDrawable, UniversalGlassDrawable, UniversalGlassMaterial, WifiSettingsDialog, related tests, and project documentation.
- Because this is a broad cumulative diff rather than a single isolated two-line focus adjustment, direct merge to `main` is blocked pending a file-by-file source audit and resolution of the one-commit divergence.
- No source code was changed in this synchronization pass. The baseline branch remains a reference point; do not force-update it.

## Locked constraints
- Preserve capsule/dock/app geometry, sizing, spacing, DPAD graph, focus ownership, Glider animation, and popup behavior except where a future explicitly scoped task authorizes a change.
- Maintain RE-SKIN, NOT REWIRE.
- Do not reuse PR #171 or switch the baseline back to PR #169.
- Do not claim integration complete until the audited integration branch has exact-head CI and the expected resulting commit is verified.

## Next integration gate
1. Audit the cumulative PR #170 diff against current `main`, identifying the exact intended accepted changes and any duplicate/obsolete commits.
2. Resolve the one-commit main divergence without force-updating the checkpoint branch.
3. Integrate through a dedicated, reviewable branch/PR; keep #170 as the authoritative source checkpoint until the integration result is proven.
4. Run exact-head CI and verify the resulting APK identity before calling integration complete.
