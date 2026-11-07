# Reset to Glass TV Home 4.0.0 Baseline — 7 November 2026

## Decision
User explicitly requested to stop the recent rework and return the project to the Glass TV Home 4.0.0 application baseline.

## Verified baseline
- Package: `com.gathin.tvhome`
- Version name: `4.0.0`
- Version code: `40`
- Source branch at baseline: `feature/g4-parent-pin-hardening`
- Exact baseline source HEAD: `7146de58953325fdfc6c98501828676f143e64cf`
- Exact successful CI run: #1395 / `42355197900`, success, built from the exact baseline SHA above.
- Baseline artifact ID: `13567925039`
- Baseline APK size: 1,622,401 bytes
- Baseline APK SHA-256: `ec125ca46bf98af8426fef060e974c963932f9ae7f863507d135ae40df9bb605`
- Baseline artifact ZIP SHA-256: `e1b462dff3a020ecd2da1a33bee4bacb5b1754217852a66847e515ff787b187c`
- APK archive integrity check: PASS.

## Reset actions performed
- Created clean working branch `reset/baseline-4.0.0` directly from exact baseline commit `7146de58953325fdfc6c98501828676f143e64cf`.
- Closed PR #73 (the failed Image 2/Dock rework). Its branch and commits remain preserved for audit/recovery; no history was deleted.
- PR #72 remains OPEN/DRAFT/UNMERGED and is the PR associated with the baseline branch.
- Extracted and verified the original CI baseline APK to `/mnt/data/glass-tv-home-4.0.0-baseline/app-release.apk`.
- No force-push, deletion of branches, merge, TV installation, ADB, or device settings changes were performed.

## Current starting point
Use `reset/baseline-4.0.0` @ `7146de58953325fdfc6c98501828676f143e64cf` as the clean working starting point. Treat later UI/Dock work as abandoned/superseded for active development; preserve it only as historical evidence until the user decides otherwise.

## Next boundary
Do not start new feature work or install to TV until the user directs the next step. The reset establishes a repository working baseline and provides the exact 4.0.0 APK; it does not itself install the app on the device.
