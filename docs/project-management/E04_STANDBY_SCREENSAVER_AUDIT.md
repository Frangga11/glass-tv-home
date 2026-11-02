# E04 — StandBy / Screensaver Audit

Date: 2 November 2026  
Feature branch: `feature/e04-standby-screensaver`  
Base: E03 exact head `e085f8aed6fdc85b4793b76bb26ab8543b26b2cf`  
Current source head: `7c5e2677eac1c791675d9f5ef7d736eb47d37aca`  
Draft PR: #37 — https://github.com/Frangga11/glass-tv-home/pull/37

## Scope and architecture
StandBy has two display paths: Android `DreamService` (`ClockDream`) and the in-app overlay in `MainActivity`. Both read the same `StandbyPhotoStore`, use the same 20-second photo rotation interval, and use the shared upload server/session infrastructure. They remain distinct lifecycle entry points; this audit does not remove either path.

## Confirmed finding and repair
- `ClockDream.tick` previously called `HomeKeyService.sleepScreen()` and then unconditionally executed `input keyevent 223`.
- This issued two independent display-off actions, ignored the Accessibility action result, and relied on a shell command that is not a supported/reliable app-level Android TV power API.
- Replaced the duplicate shell action with one supported Accessibility global action. The result is checked and a warning is logged if the service cannot perform the action. DreamService then completes its lifecycle.
- Commit: `7c5e2677eac1c791675d9f5ef7d736eb47d37aca`.

## Preserved safeguards
- StandBy photo storage remains separate from wallpaper storage.
- Upload route remains session/token scoped; file-size, collection-count, free-space, image-signature/MIME and dimension validation remain in place.
- Collection membership and photo mode are committed together; persistence failure remains an HTTP error rather than false success.
- Picker additions merge against the latest collection; clear commits collection/mode state before deleting managed files.
- Existing orphan cleanup and asynchronous downsampled image decode remain unchanged.

## Verification and limitations
- Source review confirms both screensaver paths share the photo store and rotation cadence.
- Exact-head CI for `7c5e2677eac1c791675d9f5ef7d736eb47d37aca` has not yet returned a run; source/CI gate remains PENDING.
- No APK was published or installed. No physical Android TV/OEM behavior is claimed.
- Project F must verify: Android DreamService and internal overlay activation precedence; 10-minute idle entry; 2-hour sleep behavior; wake/remote/back behavior; display-lock result with and without enabled HomeKeyService; process pause/resume/recreation; photo upload/cancel/retry; photo rotation and orientation; offline behavior; and no duplicate screensaver/power transition.
- The system screensaver and in-app overlay have independent triggers. Their coexistence/precedence is an OEM/device acceptance item and must not be declared resolved from source inspection alone.

## Disposition
**NOT FINAL.** One confirmed defect is repaired and isolated in Draft PR #37. Exact-head CI and Project F device acceptance remain outstanding. PR #37 remains OPEN/DRAFT/UNMERGED under D-013.
