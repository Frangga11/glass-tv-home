# D06 — Process-death and TV-reboot persistence/recovery audit

**Date:** 1 November 2026  
**Status:** CLOSED for source audit and exact-head CI. Physical-device acceptance remains deferred to Stage F, not waived.  
**Branch:** `feature/d06-process-death-recovery`  
**Parent:** D05 exact head `835986c0e28c50311d4474f1060c73dd56104bf8`  
**Merge policy:** D-013 — isolated Draft PR; no early merge.

## 1. Scope and boundary

D06 verifies how app-owned settings, profile data, local content files, caches, navigation state, and scheduled work behave when the app process is recreated or Android TV restarts.

This is not a backup/restore or reinstall guarantee. The manifest retains `android:allowBackup="false"`. Process death, device reboot, uninstall/clear-data, cloud backup, and OEM device-to-device transfer are distinct cases. No backup policy, preference schema, key, or user data was changed.

## 2. Recovery paths verified

- **Home/navigation:** `MainActivity.onCreate()` reloads persisted Home focus state and wallpaper category/selection. Wallpaper cursor and shuffle-cycle state are read from their existing preference stores. Settings navigation remains owned by `NavigationState` / `ModernSettingsDialog`.
- **Profiles/avatars:** `ProfileManager.getAllProfiles()` rebuilds the profile list from its JSON store and seeds a default profile from legacy values if no valid list is available. `getActiveProfile()` falls back to the first valid profile when the saved active ID is stale. Avatar bytes remain in app-private `files/profile_avatars`; startup cleanup removes unreferenced/orphan files. Avatar replacement persists the new reference before removing the superseded file.
- **Child Mode:** profile-scoped settings, device-wide parent PIN, and one-time migration marker remain in `child_mode_prefs`. Legacy values and the migration marker are written through one editor operation. No migration or key change was introduced.
- **StandBy photos:** URI collection and photo mode are now one synchronous commit from D05. Image bytes remain under app-private `files/standby_photos`. `MainActivity.onCreate()` invokes orphan cleanup; `ClockDream` reads the persisted collection/mode and local files when the DreamService starts.
- **Wallpaper:** category, shuffle state, interval, single-photo selection and index are read from the existing preference stores; custom wallpaper bytes remain under `files/custom_wallpapers`. A missing selected custom file falls back to the available collection rather than requiring a migration.
- **Language:** `AppLanguage` restores the saved language below API 33 and follows Android's per-app locale on API 33+; `LocaleContext.wrap()` is applied at Activity base-context attachment.
- **Weather/prayer data:** cached values are derived data, not authoritative user settings. Weather refresh is checked on Home resume; prayer-time cache is keyed by zone/period and can be rebuilt/refetched.
- **Config/device settings:** the optional external-files `config.json` overlay is loaded at Activity creation. Android System/Secure settings remain OS-owned and outside D06; no device settings were touched.
- **Backup boundary:** `allowBackup=false` is preserved. This audit makes no claim that app data is restored after uninstall, clear-data, cloud restore, or across OEM device migration.

## 3. Confirmed recovery gap — D06-F01

Prayer alarms are scheduled through `AlarmManager`. `MainActivity.onResume()` schedules the next prayer and `AzanReceiver` schedules the following one after an alarm fires. Android clears scheduled alarms on full reboot, but the manifest had no `BOOT_COMPLETED` receiver. Recovery therefore depended on the launcher Activity being started after boot.

### Repair

- Added `RECEIVE_BOOT_COMPLETED` permission.
- Added non-exported `AzanBootReceiver` for `android.intent.action.BOOT_COMPLETED`.
- The receiver resolves the same selected prayer zone through `Location.chosen()` and delegates to the existing `Azan.schedule()` path. Existing master/prayer enable settings, exact-alarm behavior, and scheduling calculations remain the single source of truth.
- Added Android-free `AzanBootRecoveryPolicy` so boot-action/zone validation and scheduling dispatch can be covered by JVM tests.
- Repeated scheduling is safe: the existing `Azan.schedule()` cancels the prior pending alarm before calculating the next one.
- No UI launch, network operation, preference migration, or unrelated alarm policy was added to the receiver.

## 4. Regression coverage

`AzanBootRecoveryPolicyTest` covers:
- BOOT_COMPLETED dispatches the persisted zone to the scheduler.
- An unrelated broadcast does not schedule.
- A missing/blank zone does not schedule.
- A missing scheduler does not claim recovery.

These JVM tests verify the pure dispatch policy. They do not simulate Android's real boot broadcast or prove AlarmManager behavior on a TV; those checks remain in Stage F.

## 5. Verification boundary and deferred acceptance

| Gate | Result |
|---|---|
| Source ownership/recovery-path audit | PASS |
| D06-F01 boot alarm recovery | IMPLEMENTED |
| Pure JVM regression tests | PASS in exact-head CI #398 |
| Release build / APK size gate | PASS; APK 1,551,217 bytes, 21,647 bytes below 1,572,864-byte cap |
| Process-death / force-stop behavior on target TV | Deferred to F-stage |
| Full TV power-off/reboot and alarm restoration | Deferred to F-stage |
| Backup/restore, reinstall, OEM D2D behavior | Not promised; outside D06 |
| APK publication/installation or device mutation | Not performed; manual publication skipped |

Force-stop is intentionally distinct from ordinary process death: Android may suppress app broadcasts until the user launches the app again. The receiver is not direct-boot-aware and runs after normal user unlock; it does not read credential-protected app data before unlock.

## 6. Changed files

- `launcher/app/src/main/AndroidManifest.xml`
- `launcher/app/src/main/java/com/gathin/tvhome/AzanBootReceiver.java`
- `launcher/app/src/main/java/com/gathin/tvhome/AzanBootRecoveryPolicy.java`
- `launcher/app/src/test/java/com/gathin/tvhome/AzanBootRecoveryPolicyTest.java`
- `docs/project-management/D06_PROCESS_DEATH_REBOOT_RECOVERY_AUDIT.md`
