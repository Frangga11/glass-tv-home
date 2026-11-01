# E01 — Device Settings and HOME Launcher Audit

**Status:** Source corrections in progress; exact-head CI and device acceptance pending.  
**Base:** D07 final head `964163c918e281b68af5cf7114ca56ead2797486`.  
**Branch:** `feature/e01-device-settings-home-control`.  
**Policy:** Draft / open / unmerged. No canonical integration merge or release approval.

## Scope

Audit and improve the complete **Pengaturan Perangkat** tab: HOME launcher selection/status, remote accessibility services, notification access, screensaver/display settings, restart, power/standby, cache maintenance, device information, language, OTA updates, and Android Settings navigation.

## Confirmed findings

1. `MainActivity.ensureSystemScreensaverAndSleepSettings()` ran on every launcher startup and attempted to write screen timeout, sleep timeout, screensaver, accessibility-service and notification-listener secure settings. Exceptions were silently ignored and there was no read-back verification or user consent.
2. The manifest declared `WRITE_SECURE_SETTINGS`, but the normal installer did not grant it. Android documents this permission as **not for third-party applications**. The system-write operation also had no `WRITE_SETTINGS` special-access flow. The startup writes therefore could fail silently on normal installs.
3. Startup code attempted to enable `HomeKeyService` and `MediaNotificationListener` without the user's explicit choice. This contradicted the installer policy that remote accessibility helpers are opt-in and Android's user-managed notification-listener access.
4. HOME status in the Device tab relied on one `resolveActivity` result and did not show the HOME candidates. The A06 device snapshot also recorded a mismatch between HOME resolution and the preferred-activities dump; neither output alone proves the full user-visible HOME behavior.
5. The HOME settings dialog had useful entry points, but the ADB action only displayed a toast rather than copying the command, and the status could not be refreshed within the dialog.
6. `HomeKeyService.restartTv()` called `performGlobalAction(12)` on Android 11+, describing it as reboot. Android's public AccessibilityService API identifies action ID 12 as the accessibility-button chooser on newer Android versions, not reboot. Restart must use the visible system power menu and its supported action labels.
7. Manual cache-clean feedback ignored the `systemTrimmed` result. The UI could imply system caches had been processed even when the TV's ADB service was unavailable or the user declined access.

## Corrections made in this branch

- Removed the startup system-settings mutation helper and its call. Glass TV no longer silently overwrites Android display/sleep settings or adds accessibility/notification services at startup.
- Removed the unused privileged `WRITE_SECURE_SETTINGS` manifest declaration.
- Added read-only HOME candidate/resolved-launcher and service-status helpers. Added HOME intent visibility query so Android 11+ package visibility can expose other HOME candidates.
- Reworked the Device tab into clearer sections for HOME/remote/accessibility, display/standby, maintenance, device information, system settings and updates.
- Added separate live status cards for HomeKeyService, InputKeyService and Now Playing notification access. Each opens the corresponding Android user-managed settings page; no service is enabled by the app.
- Added a native Android screensaver settings entry with display-settings fallback. Internal Glass TV StandBy behavior remains app-owned and unchanged.
- Improved HOME dialog with the resolved launcher label, detected HOME candidates, explicit system picker fallback, an ADB command copy action, an in-dialog status refresh action, and a QR-guided phone-installer download flow.
- Fixed restart to use the system power dialog instead of misusing accessibility global-action ID 12.
- Made restart feedback accurately state that the power menu was opened and manual selection may be needed on unsupported firmware.
- Made manual cache-clean result explicitly report whether Android system caches were actually trimmed.
- Clarified accessibility-service descriptions and added Indonesian/English UI strings.

## Preserved behavior

- HOME intent filter and launcher entry point.
- Remote key handling remains opt-in through installer flags / Android Accessibility settings.
- Now Playing remains dependent on user-approved notification access.
- Glass TV's internal StandBy clock/photo feature and its persisted collection were not changed.
- Existing restart/power confirmation dialogs, cache cleaner, device metadata, language, OTA and Android Settings entry points remain present.
- No preference schema, profile state, wallpaper state, upload pipeline, or D07 behavior was changed.

## Verification and acceptance boundary

- [ ] Exact-head CI: Android build, JVM unit tests, documentation checks.
- [ ] Review HOME query visibility and launcher candidate rendering on Android 11 and newer.
- [ ] Verify the phone-installer QR destination and downloaded APK version before public release.
- [ ] Verify HOME picker fallback on OEM firmware; no device HOME default was changed during source work.
- [ ] Verify accessibility and notification-access status against actual Android Settings state.
- [ ] Verify restart and power menu labels on target TV; unsupported OEM labels must fall back to manual user selection.
- [ ] Verify cache-clean feedback for both successful ADB trim and unavailable/declined ADB.
- [ ] Inspect full Device tab and all dialog focus paths with a TV remote; visual/device acceptance remains in Stage F.
- [ ] Recheck APK size against the existing 1.5 MiB cap.

No TV settings, package states, HOME defaults, APK installation, or physical-device actions were changed or performed as part of this source work. No APK is being published. E01 remains unaccepted until the checks above are completed.
