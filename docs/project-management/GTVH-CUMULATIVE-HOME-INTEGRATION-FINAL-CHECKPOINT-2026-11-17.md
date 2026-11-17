# Cumulative Home Integration — Final Main Checkpoint (17 November 2026)

## Integration
- PR #155, "GTVH cumulative integration — synchronize PR #154 with main", is MERGED.
- Main integration commit: `81010b0941237600758036827531aff8194fb273`.
- Latest main documentation checkpoint commit: `33776e2c772138325da7dd515ea84c2196f58d2d`.
- PR #155 exact-head CI on merge candidate `355cd187a4d7053e52bdaf2df393a9cc201d1923`: both required jobs passed in runs #2291 / `44057900947` and #2292 / `44057890762`.
- Main push CI run #2292 / `44058255339` validated the integration commit. The later checkpoint-only commit was independently validated by run `44058685408`.
- Synchronization reconciled main's CI workflow and HANDOVER records. No Android source was modified during conflict resolution or the final documentation-only checkpoint.

## Latest-main CI and QA artifact
- Exact head: `33776e2c772138325da7dd515ea84c2196f58d2d`.
- Workflow run: `44058685408`; both required jobs completed SUCCESS:
  - `Launcher build and unit tests`: SUCCESS, including `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and cumulative QA artifact publication.
  - `Lists, tools and docs`: SUCCESS.
- Artifact ID: `14317436391`.
- Artifact name: `glass-tv-home-cumulative-integration-127-33776e2c772138325da7dd515ea84c2196f58d2d`.
- Artifact ZIP size: 1,442,422 bytes; extracted `app-release.apk`: 1,640,745 bytes.
- APK SHA-256: `29b89a90bd6093ed75e9d8876d02b78188bb4bd45696ef0321539aaa92871b5c`.
- ZIP integrity validation: PASS; APK archive test: PASS, no compressed-data errors.
- This is a CI-built main-branch QA APK, not a signed store/release publication. Package/version metadata beyond the existing filename convention was not independently inspected with aapt.

## Previous main QA artifact (superseded by the exact latest-main artifact above)
- Integration commit `81010b0941237600758036827531aff8194fb273`, workflow run `44058255339`, artifact ID `14316149691`.
- Previous APK SHA-256: `66a2fa128ccdedbad6dc9c1c7765c44315ff6a3bb4f342948dc49ea1802ac194`.
- Retained here for audit history; use the latest-main artifact for the current checkpoint.

## Device acceptance and scope
- User reported physical Mi TV Stick testing of the cumulative QA candidate as clear and clean. This is user-reported acceptance for the tested scope; no independent remote device instrumentation is claimed.
- Cumulative work includes the DPAD source-first Dock→Grid focus handoff and continuous Move Mode GLASS Glider/black-shadow behavior from PR #153, plus Home weather cache restoration from PR #154.
- PR #154 reapplies cached weather overlay state before the 15-minute network-fetch gate; refresh cadence gates network fetch only.
- No unrelated UI skin/layout, prayer/Ayah, popup, or wallpaper-selection changes were introduced by main-sync conflict resolution.
- Any newly reported physical defect should be handled as a scoped corrective task, retaining RE-SKIN, NOT REWIRE.

## Result
- Main integration: COMPLETE.
- Latest-main exact-head CI: PASS.
- Latest-main cumulative QA APK: BUILT AND STRUCTURALLY VERIFIED.
- Physical Mi TV Stick acceptance: USER-REPORTED CLEAR/CLEAN for the tested candidate.
- Current handoff: use this checkpoint plus the latest `HANDOVER.md`; do not treat historical draft PR branches as active merge candidates.
