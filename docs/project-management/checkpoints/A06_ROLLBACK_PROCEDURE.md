# A06 — Device rollback procedure

**Applies to:** Mi TV Stick / Android TV 11 (API 30), subject to fresh device snapshot  
**Known-good app baseline:** Glass TV Home APK from PR #9, source commit `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`  
**APK SHA-256:** `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`  
**Package noted in project records:** `com.gathin.tvhome`  
**Important:** historical device state is partial. Do not assume which HOME activity is currently selected.

## 1. Before any operation

1. Keep the tested APK file unchanged. Verify its hash on the computer that will perform ADB installation:
   ```sh
   sha256sum glass-tv-home-profile-avatar-test.apk
   ```
   Expected SHA-256:
   `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`
2. Capture the read-only device snapshot in `A06_TESTED_APK_BASELINE.md`.
3. Record the output of `pm path com.gathin.tvhome`, the HOME resolver, and enabled/disabled package lists.
4. Do not proceed if the APK hash differs, package identity is uncertain, or the stock HOME activity cannot be identified.

## 2. Restore the stock Android TV launcher

Preferred route is the TV's Android system interface:
1. Open Android TV Settings.
2. Open Apps / Default apps (wording can vary by firmware).
3. Open Home app / Launcher.
4. Select the stock Android TV launcher entry (expected package family from historical output: `com.google.android.tvlauncher`, but confirm the displayed entry on the device).
5. Press HOME and confirm the stock launcher opens.

If the stock launcher is absent from the chooser, stop and inspect package state. Historical output showed `com.mitv.tvhome` disabled, but do not blindly toggle it or assume it is the correct HOME activity for this firmware.

Optional ADB recovery action, only after confirming the stock package is installed and intentionally choosing to restore it:
```sh
pm enable com.google.android.tvlauncher
```
Then use the system UI to select the stock Home app. Do not guess an activity component with `cmd package set-home-activity`.

## 3. Reinstall the known-good Glass TV Home baseline

Only if the goal is to restore the app package itself (not necessarily make it the default HOME app):
```sh
adb install -r glass-tv-home-profile-avatar-test.apk
```
Use `-r` to replace/update while preserving app data where Android permits. Do not uninstall first. If ADB reports a signature or version downgrade conflict, stop; do not use destructive flags or clear data. Confirm the package name from the APK before installation and compare the resulting `pm path`/package metadata afterwards.

Installing the APK does not itself authorize or require changing the default HOME app.

## 4. Recovery checks

- Press HOME and verify the intended launcher opens.
- Open Glass TV Home explicitly and confirm it starts without crashing.
- Confirm the stock launcher remains selectable in system settings.
- Verify remote Back/HOME behavior.
- Re-run the read-only HOME resolver and package enabled/disabled queries; save the outputs.

## 5. Prohibited without separate explicit approval

- Factory reset.
- `pm uninstall`, `pm clear`, or clearing launcher data.
- Disabling the stock launcher or changing HOME defaults as an experiment.
- Installing any candidate APK before A–E and F integration/regression are complete.
- Merging PRs or modifying device settings merely because this rollback document exists.

## 6. Current uncertainty

The historical outputs show `com.mitv.tvhome` disabled and both `com.google.android.tvlauncher` and `com.gathin.tvhome` enabled, but do not establish current HOME resolution. The exact stock activity component is therefore deliberately not hardcoded. A fresh read-only snapshot is the precondition for any actual device recovery operation.
