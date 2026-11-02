# E04 — StandBy / Screensaver Audit

Date: 2 November 2026  
Feature branch: `feature/e04-standby-screensaver`  
Base: E03 exact head `e085f8aed6fdc85b4793b76bb26ab8543b26b2cf`  
Current source head: `08076bb945a169de198905152a092f419f48aa82`  
Draft PR: #37 — https://github.com/Frangga11/glass-tv-home/pull/37

## Approved scope
Preserve Android TV's native idle, StandBy/Dream, screensaver, sleep and display-power sequence. E04 owns custom photo upload, collection management, decoding, display and rotation only. Do not introduce app-owned idle timers or power transitions.

## Source audit and repairs
- Audited `ClockDream`, `MainActivity`, `StandbyPhotoStore`, `WallpaperUploadServer`, `UploadFilePolicy`, `UploadBodyReader`, `NetworkFeedbackPolicy`, `AndroidManifest`, photo UI callbacks and `view_nightstand`.
- `ClockDream` had a private two-hour timer and invoked `HomeKeyService.sleepScreen()`, taking power management away from Android. Removed the timer and power action. DreamService remains responsible for its own lifecycle; the clock/photo refresh and lifecycle cleanup remain.
- `MainActivity` had an independent 10-minute auto-overlay trigger and 2h10m display-off trigger. Removed those idle/power overrides. The manually invoked in-launcher Nightstand overlay remains available and retains photo display/rotation. Minute refresh now updates that overlay only when it is already open.
- Commits: `75fa1b11f9886767c95f6093f43e7251f035a95c` (Dream lifecycle); `08076bb945a169de198905152a092f419f48aa82` (launcher idle/power override removal).
- No change was made to the Android system's timeout preferences, screensaver selection, power settings, or OEM behavior.

## Preserved photo pipeline
- StandBy collection remains separate from wallpaper and avatar storage.
- QR endpoint remains a dedicated standby-only session/token route; content length, size, signature/MIME, image dimensions, collection count and storage checks remain.
- Collection membership and photo mode are committed together; persistence failures are not reported as success.
- Picker merges with the latest collection and deduplicates; clear commits state before deleting managed uploads.
- Orphan cleanup, private app storage, async downsampled decode, EXIF orientation and shared 20-second photo index remain.
- DreamService and manual Nightstand overlay continue reading the same store. QR upload callback refreshes the active in-app overlay; DreamService's periodic photo tick observes the updated collection.

## Verification status
- Prior CI #627 for the pre-follow-up source passed; it is not evidence for the latest source head.
- Exact-head CI #629 / run `41418393199` is **IN PROGRESS**: Lists/tools/docs passed; launcher unit tests and Release build are running; APK diagnostics and size gate pending.
- No APK published or installed. No physical device acceptance claimed.
- Project F must verify on the target TV: native idle/StandBy/screensaver/sleep sequence remains unchanged; DreamService selection/activation; wake and remote behavior; manual overlay; QR upload/cancel/retry/session expiry; photo appearance, orientation, rotation, invalid image recovery, app process/lifecycle, offline behavior, and no duplicate overlay/power transition.

## Cross-project synchronization
- B: preserve global UI/dialog/navigation contracts; no scope change.
- E01: do not write or override Android display timeout, screensaver or power preferences.
- E02: profile/avatar storage and QR routes remain isolated from StandBy photos.
- E03: wallpaper metadata/assets and wallpaper QR routes remain isolated.
- E04: own StandBy photo path only.
- F: own physical/OEM acceptance of native transitions and photo behavior.
- D-013 remains in force: PR #37 OPEN/DRAFT/UNMERGED; no early merge.

## Disposition
**SOURCE REPAIR COMMITTED; NOT FINAL YET.** Exact-head CI #629 and Project F hardware acceptance are outstanding. Do not call E04 paripurna until those gates pass.
