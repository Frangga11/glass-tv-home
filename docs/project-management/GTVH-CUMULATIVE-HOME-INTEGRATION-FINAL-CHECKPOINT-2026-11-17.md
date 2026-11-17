# Cumulative Home Integration — Final Main Checkpoint (17 November 2026)

## Integration
- PR #155, "GTVH cumulative integration — synchronize PR #154 with main", is MERGED.
- Final integration commit on `main`: `81010b0941237600758036827531aff8194fb273`.
- PR #155 exact-head CI on merge candidate `355cd187a4d7053e52bdaf2df393a9cc201d1923`: both required jobs passed in runs #2291 / `44057900947` and #2292 / `44057890762`.
- The merge preserved the cumulative integration tree and reconciled main's CI workflow and HANDOVER records. No Android source was modified during conflict resolution.

## Final-main build evidence
- Main push CI run #2292 / `44058255339`, exact head `81010b0941237600758036827531aff8194fb273`: SUCCESS.
- `Launcher build and unit tests`: SUCCESS; `testReleaseUnitTest` and `assembleRelease` completed successfully.
- `Lists, tools and docs`: SUCCESS.
- Cumulative integration QA artifact ID: `14316149691`.
- Artifact name: `glass-tv-home-cumulative-integration-127-81010b0941237600758036827531aff8194fb273`.
- Artifact ZIP size: 1,442,433 bytes. It contains `app-release.apk` (1,640,745 bytes).
- Extracted APK was verified as an Android package; ZIP integrity test passed with no compressed-data errors.
- APK SHA-256: `66a2fa128ccdedbad6dc9c1c7765c44315ff6a3bb4f342948dc49ea1802ac194`.
- APK filename delivered for QA: `Glass-TV-Home-4.0.0-main-2026-11-17.apk`.
- This is a CI-built main-branch QA APK, not a signed store/release publication. Package/version metadata beyond the filename has not been independently inspected with aapt.

## Device acceptance and scope
- User reported physical Mi TV Stick testing of the cumulative QA candidate as clear and clean. This is user-reported acceptance for the tested scope; no independent remote device instrumentation is claimed.
- The cumulative work includes the DPAD source-first Dock→Grid handoff and continuous Move Mode GLASS Glider/black-shadow behavior from PR #153, plus weather cache restoration from PR #154.
- No unrelated UI skin/layout, prayer/Ayah, popup, or wallpaper-selection changes were introduced by the main-sync conflict resolution.
- Any newly reported physical defect should be handled as a scoped corrective task, retaining the RE-SKIN, NOT REWIRE boundary.

## Result
- Main integration: COMPLETE.
- Exact-main CI: PASS.
- Main-branch QA APK: BUILT AND STRUCTURALLY VERIFIED.
- Physical Mi TV Stick acceptance: USER-REPORTED CLEAR/CLEAN for the tested candidate.
