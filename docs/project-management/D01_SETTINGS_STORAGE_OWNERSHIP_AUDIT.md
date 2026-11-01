# D01 — Settings / Configuration Storage Ownership Audit

**Date:** 1 November 2026  
**Status:** SOURCE INVENTORY COMPLETE — FINDINGS RECORDED — NO REFACTOR OR DATA MIGRATION IN D01  
**Branch:** `feature/d01-config-storage-audit`  
**Parent:** C07 exact head `1cc6d7033daf6f9371805f667467b8860edcfb8f`  
**Merge policy:** D-013 — isolated Draft PR; no early merge.

## 1. Objective and boundary

Identify where launcher settings/configuration and persistent state are stored, which component reads/writes each store, what is device-wide versus profile-scoped, and where ownership overlaps.

D01 is an audit-only step. It does not rename preference files/keys, migrate user data, change defaults, consolidate stores, alter system settings, or modify UI behavior. D02 and D07 depend on this ownership map.

## 2. Storage mechanisms observed

- Android `SharedPreferences` with `MODE_PRIVATE` is the dominant key/value persistence mechanism.
- Android resource XML under `res/values` supplies build-time defaults and static configuration.
- `Config` optionally reads `config.json` from `getExternalFilesDir(null)`; the file is an external theme/config overlay, not a SharedPreferences store.
- User content is stored in app-private `getFilesDir()` directories (profile avatars, custom wallpapers, StandBy photos, and Azan audio).
- External cache/files are also used for downloaded update/wallpaper assets.
- The manifest sets `android:allowBackup=false`. On the current target TV OS (Android 11 / API 30), this disables Android Auto Backup for the app. On Android 12+ / API 31+, some OEMs may still allow device-to-device transfer despite this flag; therefore the flag alone is not a universal D2D exclusion. Treat app state as local and do not promise cloud/reinstall recovery; if D2D exclusion is required, define explicit `dataExtractionRules` and legacy backup rules in a separately reviewed change. D06 must distinguish process death/reboot persistence from backup/restore behavior. Reference: [Android Auto Backup](https://developer.android.com/identity/data/autobackup).
- No Room, SQLiteOpenHelper, or DataStore owner was identified in the inspected launcher persistence paths.

## 3. Preference-store ownership inventory

| Store | Keys / data observed | Primary owner / consumers | Scope and classification |
|---|---|---|---|
| `language_prefs` | `selected_lang` | `AppLanguage`, locale context/settings | App-wide user preference. On API 33+, Android per-app locale is authoritative when set; SharedPreferences is the compatibility/mirror value. |
| `navigation_state` | Home focus key/position/top-pill; Settings tab, focus area/path/signature, scroll offsets | `NavigationState`, `MainActivity`, `ModernSettingsDialog` | UI restoration state, not user content/configuration. |
| `profile_prefs` | Profile JSON, active profile ID, legacy name/avatar fields | `ProfileManager` | Profile catalogue and active-profile pointer. Custom avatar bytes are separate files under `files/profile_avatars`. |
| `child_mode_prefs` | Profile-prefixed child-mode/locked/hidden-app keys; device-wide PIN; legacy keys; migration marker | `ChildModeManager`, profile deletion in `ProfileManager` | Mixed ownership: most policy is profile-scoped; parent PIN and migration marker are device-wide. |
| `standby_prefs` | Newline-separated photo URI list; display mode | `StandbyPhotoStore`, StandBy upload/browser/settings | Collection metadata and mode. Photo bytes live separately under `files/standby_photos`. |
| `wallpaper_prefs` | Wallpaper interval, shuffle mode/cycle/position/last file, active category, custom-single mode/file, description visibility; icon animation/glass/glow/weather overlay flags | Primarily direct reads/writes in `MainActivity`; upload/gallery paths in `WallpaperUploadServer`; settings UI | Large mixed store: wallpaper selection/cycle state and unrelated visual-effect preferences share one file. |
| `wallpaper` | Current wallpaper index | `MainActivity` | Separate cursor/index store for the wallpaper state in `wallpaper_prefs`. |
| `theme_prefs` | Weather widget enabled; Islamic shelf enabled | `MainActivity` | App-wide home-surface visibility preferences. |
| `location` | Prayer zone, first-open marker, published current location fields | `Location`, `LocationActivity`, prayer/home/weather bridge | Location selection for prayer-time zone plus legacy/published location snapshot. |
| `app_location_prefs` | Region, station ID, location name/place, latitude/longitude | `AppLocationManager`, Weather/AQI and station selector | Weather/AQI station selection and coordinates. This is a second location store alongside `location`; ownership/authority between the two needs explicit treatment. |
| `app_weather_prefs` | `atmosphere_mode` (AUTO or manual weather mood) | `WeatherActivity` | Weather visual override, separate from weather data cache. |
| `weather_aqi_cache` | Weather/AQI JSON, values, source/status/place/time, last update | `WeatherAirQuality` | Derived network cache; not authoritative user settings. |
| `prayer_times_cache` | Annual/monthly prayer-time JSON and cache period | `PrayerTimes` | Derived cache keyed by location/period; not user settings. |
| `azan` | Master enable, per-prayer enable, sound choice, scheduled zone | `Azan` and alarm flow | User preferences plus a duplicated zone snapshot; `Location` also owns the selected zone. |
| `update_prefs` | Auto-update toggle/time, last check, available version/changelog/URL, skipped version | `AppUpdateManager` | Update policy and transient update metadata. |
| `recent_apps_prefs` | Recent app history | `RecentAppsManager` | Derived/user-facing history. |
| `online_wallpapers` | Last sync timestamp | `OnlineWallpapers` | Download cache metadata. |
| `place_wallpapers` | Refresh timestamp and rules version | `PlaceWallpapers` | Download cache metadata. |
| `launch` | Last observed boot-time estimate | `MainActivity` | Session/boot animation bookkeeping. |

## 4. File/resource ownership inventory

| Path / source | Owner | Purpose / boundary |
|---|---|---|
| `res/values/config.xml` and resource arrays/booleans/integers | Build resources consumed by `Config` | Baseline defaults for launcher layout, dock/hidden apps, wallpaper behavior, and feature flags. |
| `getExternalFilesDir(null)/config.json` | `Config` reader; theme tooling writes it | Optional theme overlay. Unknown/badly typed keys fall back to resource defaults; file over 64 KiB or invalid JSON is ignored. `Config.load` reads it once during `MainActivity.onCreate`. |
| `getExternalFilesDir(null)/wallpaper.jpg`, `wallpapers/` | `Config` / theme tooling | Theme-provided wallpaper assets. |
| External files `online-wallpapers/` | `OnlineWallpapers` | Downloaded online wallpaper cache. |
| External files `place-wallpapers/` | `PlaceWallpapers` | Downloaded weekly place-photo cache and metadata sidecars. |
| `getFilesDir()/profile_avatars/` | `ProfileManager` and Avatar upload | Private avatar bytes; profile JSON stores the file path reference. |
| `getFilesDir()/custom_wallpapers/` | `MainActivity` and Wallpaper upload/gallery routes | Private custom wallpaper bytes; selected file/mode is stored in `wallpaper_prefs`. |
| `getFilesDir()/standby_photos/` | `StandbyPhotoStore` and StandBy upload | Private StandBy image bytes; `standby_prefs` stores URI references. |
| `getFilesDir()` Azan audio files | `Azan` | Downloaded sound assets; preference store records the chosen sound. |
| External cache / app cache | `AppUpdateManager` and other cache consumers | Download/staging data, not durable settings. |

## 5. Confirmed ownership and consistency findings

| ID | Severity | Finding | Why it matters / owning follow-up |
|---|---|---|---|
| D01-F01 | HIGH | Wallpaper state is split between `wallpaper_prefs` and `wallpaper`; a large number of reads/writes are performed directly in `MainActivity`, while upload/gallery code also reads the same preference keys. | Multiple keys describe one wallpaper domain and depend on coordinated updates. Map exact mutation flows in D02/D05/D07 before consolidating or changing storage. |
| D01-F02 | HIGH | Two location models persist related concepts in `location` and `app_location_prefs`; `Azan` also stores a zone snapshot in `azan`. | Prayer-zone selection, weather station, coordinates, and scheduled alarm zone can drift. Define one canonical location authority and migration/compatibility behavior before changing data. |
| D01-F03 | HIGH | `child_mode_prefs` mixes profile-scoped policy with device-wide PIN and a one-time legacy migration marker. Migration copies old device-wide values to the then-active profile and leaves legacy keys in place. | Scope must remain explicit; do not accidentally profile-scope the parent PIN or repeat legacy migration. D07 must assess migration durability and concurrent profile changes. |
| D01-F04 | MEDIUM | Profile catalogue, active-profile pointer, legacy profile fields, and avatar file references are stored separately; ordinary profile updates use `apply()`, while avatar replacement/deletion use `commit()`. | Multi-key consistency and write durability differ by operation. Preserve the existing save-before-delete protection; review atomicity in D04/D07. |
| D01-F05 | MEDIUM | StandBy collection metadata is a newline-joined URI string in `standby_prefs`, separate from the actual files. | File deletion and metadata persistence must remain coordinated; D05/D07 should test orphan, missing-file, and interrupted-update recovery. |
| D01-F06 | MEDIUM | `AppLanguage` stores the language in SharedPreferences and mirrors/defers to Android per-app locale on API 33+. | Authority differs by API level; preserve the explicit platform-first rule on API 33+ and compatibility behavior below it. |
| D01-F07 | MEDIUM | `Config` uses resource defaults plus an optional external-files JSON overlay, loads once at Activity creation, and is not wired as a live mutable settings store. | Do not treat this overlay as the source of Settings UI toggles or migrate it into SharedPreferences without a separate compatibility plan. |
| D01-F08 | LOW | UI navigation restoration, network-derived caches, update metadata, recent-app history, and user preferences are all persisted through SharedPreferences but have different lifecycle/retention semantics. | D02/D06 must distinguish durable user choices from ephemeral/restorable state and disposable caches. |
| D01-F09 | HIGH / OUT OF D01 SCOPE | `MainActivity.ensureSystemScreensaverAndSleepSettings()` writes Android System/Secure settings (sleep timeout, screensaver, accessibility/notification listener settings). | These are OS/device settings, not app-owned preferences. D01 does not execute or alter them; carry them to the separately scoped E01/device-settings audit. |
| D01-F10 | MEDIUM | Manifest sets `android:allowBackup=false`; semantics differ by OS generation and OEM. | On the current Android 11/API 30 target, Auto Backup is disabled. On Android 12+/API 31+, some OEMs may still permit device-to-device transfer despite the flag. Do not promise cloud/reinstall recovery or assume universal D2D exclusion; D06 must separately test process death/reboot and document backup/restore limits. If explicit D2D exclusion becomes a requirement, review `dataExtractionRules` plus legacy rules before changing the manifest. |
| D01-F11 | MEDIUM | `StandbyPhotoStore.addUploadedFile()` commits the photo URI collection, then calls `setPhotoMode(context, true)` but ignores that method's boolean commit result and still returns success. | A failed mode write can leave a saved photo collection while StandBy photo mode remains disabled, so the upload path may report success without the expected visible behavior. D05/D07 must make the collection + mode transition failure-aware and add regression coverage; do not refactor storage in D01. |

## 6. D01 disposition and guardrails

- Source inventory identifies the preference stores and file owners above; no persistence refactor or data migration is justified in D01 alone.
- No preference file/key was renamed, deleted, reset, or consolidated.
- No resource default, config overlay, profile, wallpaper, photo collection, language, location, PIN, or device setting was changed.
- No candidate APK was published/installed and no physical device setting was changed.
- Before any later migration, add explicit old-key read/compatibility rules, preserve user data, test interrupted writes and process death, and define rollback.
- D02 should start with the live-propagation contract and owner map from this audit; D03–D06 should reuse the same storage ownership boundaries; D07 should address cross-store atomicity and concurrency.
- D01 is source-audit complete only. Runtime/process-death/reboot validation remains deferred to F-stage. D01-F11 is a concrete source-level persistence consistency defect/risk assigned to D05/D07 for repair and regression coverage; it is not considered resolved by this audit.

## 7. Verification

- Source base: C07 branch `feature/c07-upload-session-resilience` @ `1cc6d7033daf6f9371805f667467b8860edcfb8f`.
- Scope reviewed: launcher Java persistence consumers, resource defaults, manifest backup policy, private/external file paths, and Settings/Activity write sites. Follow-up quality review also traced `StandbyPhotoStore.addUploadedFile()` commit handling and checked Android's documented API/OEM backup semantics.
- No source code was changed in this audit-only step. Exact-head CI will validate the audit documentation and branch integrity.
