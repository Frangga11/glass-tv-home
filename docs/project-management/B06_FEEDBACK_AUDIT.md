# B06 — Notification Feedback Audit

**Roadmap:** GTVH-BRR-001 v1.0  
**Status:** SOURCE + STATIC QC + EXACT-HEAD CI COMPLETE  
**Implementation branch:** `feature/b06-notification-feedback`  
**Draft PR:** [#16 — B06: harden notification feedback and network timeout handling](https://github.com/Frangga11/glass-tv-home/pull/16)  
**Base:** `d7b76c4a22c9eadffb8171a616283f3ad647d488`  
**Verified head:** `bc86a250d73a5cbb83250217d42c3c3993cf29d7`

## 1. Scope and baseline

B06 standardizes transient notifications, success/error feedback, loading indicators, and timeout feedback without changing unrelated confirmation/settings dialog behavior.

The baseline audit covered 63 app-layer Java source files (the QR utility package is separate). It found 108 direct Toast call sites across 10 classes, one OTA ProgressDialog, and independent JavaScript feedback paths for Avatar, Wallpaper, and Standby QR uploads. No shared feedback delivery helper or Snackbar implementation existed.

## 2. Implemented changes

### Shared transient feedback

- Added `TvFeedback` as the single delivery path for non-blocking launcher feedback.
- Posts display work to the main thread.
- Rejects null/empty messages and invalid durations.
- Resolves Activity context wrappers and suppresses feedback for finishing/destroyed Activities.
- Preserves the existing localized message text and short/long durations.
- Migrated all 108 identified Toast call sites across 10 app-layer Java files. Direct Toast construction is now contained in `TvFeedback`.

### OTA update feedback and loading

- Guards callbacks against stale Activity lifecycle state.
- Disconnects metadata HTTP connections on success, HTTP failure, and exceptions; closes the response reader safely.
- Rejects non-200 download responses and empty, truncated, or inconsistent zero-length APK responses.
- Handles servers without Content-Length using indeterminate progress; switches to determinate progress when the size is known.
- Flushes/syncs and closes the downloaded APK before invoking the installer.
- Deletes incomplete/cancelled APK files.
- Adds a visible Cancel action that disconnects the active request and suppresses an error Toast for an intentional cancellation.
- Preserves operation budgets: metadata check 6s connect / 6s read; APK download 10s connect / 15s read.

### QR upload feedback

- Avatar page displays upload percentage and explicit connection/timeout feedback.
- Wallpaper page displays upload progress and surfaces localized server response details, connection failures, and timeout errors.
- Standby page displays aggregate upload progress and retains the number of photos already saved when the connection is lost or times out.
- Timeout messages are present in Indonesian and English.
- Upload size limits, Avatar's 5-minute QR session, and endpoint/session semantics are unchanged.

### Wallpaper category synchronization

- Retains the initial download-start feedback.
- Adds explicit success feedback including category and downloaded-photo count.
- Adds failure feedback and safely handles background synchronization exceptions.

## 3. Timeout policy

Timeouts remain operation-specific and are named/tested; they are not globally equalized.

| Operation | Browser total timeout | Socket idle timeout | Session lifetime |
|---|---:|---:|---:|
| Avatar upload | 240s | 90s | 5 minutes |
| Wallpaper upload | 600s | 30s | Managed by server/dialog lifecycle |
| Standby upload | 600s | 30s | Managed by server/dialog lifecycle |
| OTA metadata check | — | — | 6s connect / 6s read |
| OTA APK download | — | — | 10s connect / 15s read |

## 4. Static quality and tests

- 63 app-layer Java files audited for Toast, dialog, progress/loading, delayed callbacks, and network/socket timeout patterns.
- 108 Toast call sites migrated across 10 classes.
- Three Android string resource sets contain 566 keys each (default, Indonesian, English).
- Static parity result: zero duplicate keys, zero missing/extra keys, zero format-placeholder mismatches.
- Added `FeedbackPolicyTest` (3 test methods).
- Added/expanded `NetworkFeedbackPolicyTest` (4 test methods) for download completeness and operation-specific socket/browser timeout budgets.
- Explicit confirmation/settings AlertDialogs retain their existing TV styling and focus behavior; B06 does not replace unrelated interactive dialogs.

## 5. CI evidence

- Exact head: `bc86a250d73a5cbb83250217d42c3c3993cf29d7`.
- GitHub Actions CI run **#295 / 41280874732: SUCCESS**.
- `Launcher build and unit tests`: SUCCESS.
- `Lists, tools and docs`: SUCCESS.
- Release build completed; APK publication step was skipped.
- No APK was published or installed; no TV/device settings were changed.

## 6. Closure boundary

**B06 source implementation, static quality checks, and exact-head CI are complete.**

PR #16 remains Draft/open/unmerged under the approved D-013 grouped-merge decision. Do not merge B06 into `integration/b-series` until the A–G source audit/fix work is complete and the final grouped integration is authorized.

The following are deferred to final F-stage overall acceptance, not waived:

- Android API 30 and API 33+ Activity/configuration/lifecycle checks.
- D-pad focus, dialog focus, and return-focus behavior on the target TV.
- Avatar, Wallpaper, and Standby QR uploads from a real phone on the same Wi-Fi network.
- Physical TV acceptance and regression.

A source/CI pass is not a physical-device pass.
