# E01 — Device Settings and HOME Launcher Audit

**Status:** Source audit/corrections complete; exact-head CI passed. Physical-device acceptance remains pending.  
**Base:** D07 final head `964163c918e281b68af5cf7114ca56ead2797486`.  
**Branch:** `feature/e01-device-settings-home-control`.  
**Policy:** PR #34 remains Draft / open / unmerged. No canonical integration merge or release approval.
**Verified source head:** `ac46f8d74172cd63c62b3fbe6e7082e2f3bc4b8b`.
**CI:** Workflow run `41363727217` — Android release build, unit tests, docs checks and APK budget all PASS.
**APK diagnostic size:** 1,559,445 bytes; cap 1,572,864 bytes; headroom 13,419 bytes. Diagnostic only; no APK artifact was published.

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
- Improved HOME dialog with the resolved launcher label, detected HOME candidates, explicit system picker fallback, an ADB command copy action, an in-dialog status refresh action, and a QR-guided phone-installer download flow. ADB guidance is labelled advanced and explicitly warns against manually disabling the stock launcher; the installer is the guarded route.
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

- [x] Exact-head CI: Android build, JVM unit tests, documentation checks and APK size budget — PASS at `ac46f8d74172cd63c62b3fbe6e7082e2f3bc4b8b`.
- [ ] Review HOME query visibility and launcher candidate rendering on Android 11 and newer.
- [ ] Verify the phone-installer QR destination and downloaded APK version before public release.
- [ ] Verify HOME picker fallback on OEM firmware; no device HOME default was changed during source work.
- [ ] Verify accessibility and notification-access status against actual Android Settings state.
- [ ] Verify restart and power menu labels on target TV; unsupported OEM labels must fall back to manual user selection.
- [ ] Verify cache-clean feedback for both successful ADB trim and unavailable/declined ADB.
- [ ] Inspect full Device tab and all dialog focus paths with a TV remote; visual/device acceptance remains in Stage F.
- [x] Recheck APK size against the existing 1.5 MiB cap — 1,559,445 bytes, 13,419 bytes below cap.

No TV settings, package states, HOME defaults, APK installation, or physical-device actions were changed or performed as part of this source work. The PR workflow did not publish an APK. E01 source work is ready for the F-stage physical/visual acceptance checks; do not mark the complete project accepted until those checks pass.

## Follow-up — HOME launcher popup opacity and layout hardening (2 Nov 2026)

A focused source audit of `showDefaultLauncherDialog()` found that the custom popup content had an opaque inner panel, but the enclosing `AlertDialog` window and `ScrollView` were explicitly assigned transparent backgrounds. This left an avoidable OEM/theme-dependent risk of the launcher showing through around or behind the popup, matching the previously reported visual concern.

Correction on the E01 branch:
- Give the popup scroll surface and dialog window explicit opaque dark backgrounds (`#1E2026`) with a consistent rounded outline and border.
- Keep the content column transparent over the opaque scroll surface so the full panel remains visually continuous.
- Clip the scroll surface to its rounded outline, keep child content clipped, disable overscroll glow, and preserve fill-viewport behavior.
- Retain the existing bounded dialog dimensions, scroll support, and initial focus on the system HOME picker action.

**Verification boundary:** source correction committed as `c56c0a800b43768756905dfc76daaa2e6015ffb1`. Exact-head CI run `41376900940` is pending at the time of this audit update. The source change removes the explicit transparent surfaces, but visual acceptance on the target TV (including D-pad focus, clipped corners, and long-content scrolling) still requires Stage F device verification. No launcher default or device setting was changed.

## Follow-up — shared Device Settings popup visual system (2 Nov 2026)

A tab-level popup inventory covered the app-owned windows reachable from Device Settings: HOME launcher status/details and phone-installer QR; restart and power confirmations; cache confirmation and result; language selection; OTA control and schedule; update-available, install-permission and download-progress dialogs.

A shared `MainActivity.styleDeviceSettingsPopup()` contract now applies the same opaque `bg_glass_dialog` surface, centered placement, 0.68 dim amount, bounded screen-relative width, shared typography/list styling and consistent dialog sizing. The HOME and phone-installer custom dialogs use the same shared window surface; the OTA custom layout no longer paints a second nested glass panel. Existing per-dialog content, action labels, callbacks, state changes and focus targets are retained; primary confirmation actions receive focus where appropriate.

The update-available, install-permission and download-progress windows also use the shared contract when launched from `MainActivity`. Android-owned settings pages (HOME picker, accessibility, notification access, Dream/screensaver and general Settings) are intentionally excluded because their visual presentation belongs to the TV firmware/Android OS, not Glass TV Home.

**Verification boundary:** source-level styling consolidation is committed on the E01 branch. Exact latest-head CI and Stage F target-TV visual/remote testing are still required before acceptance. No device settings, HOME resolver preference, or user data are modified by this styling work.
