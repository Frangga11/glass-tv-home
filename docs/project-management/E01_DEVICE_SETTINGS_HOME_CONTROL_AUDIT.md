# E01 — Device Settings and HOME Launcher Audit

**Status:** Source corrections and popup visual unification implemented; Android build, unit tests, docs checks and APK budget PASS. Physical-device visual acceptance remains pending.  
**Base:** D07 final head `964163c918e281b68af5cf7114ca56ead2797486`.  
**Branch:** `feature/e01-device-settings-home-control`.  
**Policy:** PR #34 remains Draft / open / unmerged. No canonical integration merge or release approval.
**Verified source head:** `dc1edd222b6a6a3aaa1a7010090a034510c12b5f` (popup source changes); validation workflow `41377496081` / run #460 at `99512b0960cce08e3ce1b6b0f2d9c3af3461d4d1` passed with the same source tree. The final branch commit after that is documentation-only.
**CI:** Android release build, JVM unit tests, docs checks and APK size budget — PASS.
**APK diagnostic size:** 1,559,641 bytes; cap 1,572,864 bytes; headroom 13,223 bytes. Diagnostic only; APK publishing skipped; no artifact published.

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

- [x] Android release build, JVM unit tests, documentation checks and APK size budget — PASS for the complete popup source changes (workflow `41377496081`, run #460).
- [ ] Review HOME query visibility and launcher candidate rendering on Android 11 and newer.
- [ ] Verify the phone-installer QR destination and downloaded APK version before public release.
- [ ] Verify HOME picker fallback on OEM firmware; no device HOME default was changed during source work.
- [ ] Verify accessibility and notification-access status against actual Android Settings state.
- [ ] Verify restart and power menu labels on target TV; unsupported OEM labels must fall back to manual user selection.
- [ ] Verify cache-clean feedback for both successful ADB trim and unavailable/declined ADB.
- [ ] Inspect full Device tab and all dialog focus paths with a TV remote; visual/device acceptance remains in Stage F.
- [x] Recheck APK size against the existing 1.5 MiB cap — 1,559,641 bytes, 13,223 bytes below cap.

No TV settings, package states, HOME defaults, APK installation, or physical-device actions were changed or performed as part of this source work. The PR workflow did not publish an APK. E01 source work is ready for the F-stage physical/visual acceptance checks; do not mark the complete project accepted until those checks pass.

## Follow-up — HOME launcher popup opacity and layout hardening (2 Nov 2026)

A focused source audit of `showDefaultLauncherDialog()` found that the custom popup content had an opaque inner panel, but the enclosing `AlertDialog` window and `ScrollView` were explicitly assigned transparent backgrounds. This left an avoidable OEM/theme-dependent risk of the launcher showing through around or behind the popup, matching the previously reported visual concern.

Correction on the E01 branch:
- Use the shared `bg_glass_dialog` as the opaque dialog-window surface; the scroll content remains transparent so it reveals the window surface rather than exposing the launcher.
- Keep child clipping, disable overscroll glow, and preserve fill-viewport behavior.
- Retain the existing bounded dialog dimensions, scroll support, and initial focus on the system HOME picker action.

**Verification boundary:** popup opacity correction is included in the validated source tree (workflow `41377496081`, run #460: PASS). Visual acceptance on the target TV (including D-pad focus, corner clipping and long-content scrolling) still requires Stage F device verification. No launcher default or device setting was changed.

## Follow-up — shared Device Settings popup visual system (2 Nov 2026)

A tab-level popup inventory covered the app-owned windows reachable from Device Settings: HOME launcher status/details and phone-installer QR; restart and power confirmations; cache confirmation and result; language selection; OTA control and schedule; update-available, install-permission and download-progress dialogs.

A shared `MainActivity.styleDeviceSettingsPopup()` contract now applies the same opaque `bg_glass_dialog` surface, centered placement, 0.68 dim amount, bounded screen-relative width, shared typography/list styling and consistent dialog sizing. The HOME and phone-installer custom dialogs use the same shared window surface; the OTA custom layout no longer paints a second nested glass panel. Existing per-dialog content, action labels, callbacks, state changes and focus targets are retained; primary confirmation actions receive focus where appropriate.

The update-available, install-permission and download-progress windows also use the shared contract when launched from `MainActivity`. Android-owned settings pages (HOME picker, accessibility, notification access, Dream/screensaver and general Settings) are intentionally excluded because their visual presentation belongs to the TV firmware/Android OS, not Glass TV Home.

**Verification boundary:** source-level styling consolidation is committed and the Android build, JVM unit tests, documentation checks and APK budget all pass (workflow `41377496081`, run #460). Stage F target-TV visual/remote testing is still required before acceptance. No device settings, HOME resolver preference, or user data are modified by this styling work.

## Finalization — 2 November 2026

**E01 source-audit status: COMPLETE — ready to proceed to E02.** This closes E01 source implementation and repository-level verification only. Target-TV acceptance remains assigned to Project F; this is not final release approval.

### Final source corrections and cross-project alignment

- Device Settings now presents Android screensaver and Android display/screen-off settings as separate actions. The descriptions explain that Android controls when its DreamService starts, while Glass TV's ClockDream reuses the same nightstand view and StandbyPhotoStore photo collection as Glass TV StandBy. The in-app StandBy trigger remains separate from Android's display timeout.
- Source-defined timing observed during audit: the in-app overlay starts after 10 minutes idle and its TV-sleep path is 2 hours 10 minutes from last interaction; ClockDream has a 2-hour Dream-session timeout. F must verify these paths do not conflict on the target OEM firmware.
- Verified the E01 copies of B01's canonical TvDialogStyle.java and TvUiTokens.java are byte-for-byte identical to the current B01 branch blobs (85d67a718523d28a907d6bcd531e205e26507519 and cf91d8da298639fcba1451c777f3ddf603f6703a). E01 adopts the global contract; it does not define a competing popup system.
- Verified the QR destination on main contains release/glass-tv-installer.apk, release/glass-tv-home.apk, and release/version.json (version 4.0.0). The QR opens the release folder; it does not assert that a particular APK has been installed.
- Corrected cache-trim success detection. A normal ADB CLSE now counts only as stream closure; the operation is reported successful only when pm trim-caches exits through the success chain and emits the exact __GLASS_TV_SYSTEM_TRIM_OK__ marker. Added CacheCleanerTest for positive, negative, missing-marker and null output cases.
- The final resource set includes the default values/strings.xml entries as well as Indonesian and English entries. Intermediate CI failures caused by missing default resource entries and an unescaped English apostrophe were corrected before final exact-head verification.

### Exact-head verification

- **Branch head:** 9919dbaa0add6cc7ce2a84332354f46f9cc48d61
- **CI:** run #602 / 41400508774 — PASS at the exact branch head.
- Android release build and JVM unit tests — PASS.
- Lists/tools/docs checks — PASS.
- Diagnostic APK — **1,560,565 bytes**; budget **1,572,864 bytes**; headroom **12,299 bytes**.
- Manual TV-test APK publication — skipped; workflow artifact count — **0**.
- PR #34 remains Draft / open / unmerged, based on D07 exact head 964163c918e281b68af5cf7114ca56ead2797486. No canonical integration merge.

### Remaining acceptance gates — Project F

These are physical/OEM acceptance checks, not blockers to starting E02 source work:

- Reconcile the existing HOME resolver versus preferred-activities discrepancy using read-only device evidence; verify the HOME picker and physical HOME button.
- Verify actual service status after enabling/disabling HomeKeyService, InputKeyService and notification access through Android Settings.
- Verify restart/power menu labels and manual fallback on the target TV.
- Verify cache-trim success and denied/unavailable ADB paths on-device.
- Scan the QR with a phone and complete the installer flow without changing HOME defaults unless explicitly approved.
- Inspect all Device Settings popups and the shared Settings shell on the target TV with D-pad/remote: focus order, scroll, clipping, legibility and dimensions.
- Verify Android DreamService/ClockDream, in-app StandBy overlay and Android display timeout behavior together on the target firmware.

No TV setting, HOME default, package state, user data or installed APK was changed during this source audit. E01 source work is closed for now; F owns the remaining device acceptance.
