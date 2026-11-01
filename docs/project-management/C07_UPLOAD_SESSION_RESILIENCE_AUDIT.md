# C07 — Upload Session Resilience Audit

**Date:** 1 November 2026  
**Status:** IMPLEMENTATION COMPLETE — CI #366 PASS ON EXACT PR HEAD — PHYSICAL ACCEPTANCE DEFERRED  
**Branch:** `feature/c07-upload-session-resilience`  
**Parent:** C06 exact head `e74b6466c058ec930aa92c8db9603b11cf96a185`  
**Merge policy:** D-013 — isolated Draft PR; no early merge.

## 1. Scope

Harden disconnect, retry, session expiry, cancellation, and timeout behavior for the Avatar, Wallpaper, and StandBy QR upload flows. Preserve C01–C06 route contracts, validation policy, byte limits, storage destinations, and callbacks.

## 2. Baseline findings

| ID | Severity | Finding | Impact |
|---|---|---|---|
| C07-F01 | HIGH | Socket reads used only an idle timeout; a peer could keep a request alive by periodically sending bytes. | Header/body reads had no absolute request deadline. |
| C07-F02 | HIGH | Wallpaper/StandBy sessions had no explicit bounded lifetime, unlike Avatar. | QR endpoints could remain usable indefinitely if the UI did not close them. |
| C07-F03 | MEDIUM | Stop/cancel and upload commit were not consistently serialized for Wallpaper/StandBy. | A late worker could race session shutdown and persist a file after cancellation. |
| C07-F04 | MEDIUM | StandBy response used the temporary filename after the file had been renamed to its final extension. | Client success metadata did not match the stored file. |
| C07-F05 | MEDIUM | StandBy retry after a transport interruption needed to resume the current item rather than restart the completed batch. | Replaying a batch could duplicate already completed photos. |
| C07-F06 | LOW | Browser upload flows lacked consistent explicit cancellation and terminal expired-session feedback. | Users had limited control and could retry a QR session that was no longer valid. |
| C07-F07 | LOW | Avatar active-client membership and count updates were not coordinated across accept, worker start/finally, and stop. | Shutdown and worker completion could double-decrement or leave stale active-client accounting. |

## 3. Implemented controls

- Added `UploadDeadlineInputStream` to apply both an idle-read timeout and an absolute per-request deadline on every socket read.
- Kept existing idle timeouts and browser XHR timeouts; added explicit absolute deadlines:
  - Avatar request: 4 minutes, bounded by its existing 5-minute QR session.
  - Wallpaper/StandBy request: 10 minutes.
  - Wallpaper/StandBy QR session: 30 minutes.
- Wallpaper/StandBy server accept loop polls the session deadline and stops expired sessions; requests after expiry return HTTP 410.
- Upload commit is serialized with server stop using the same monitor. The final commit rechecks session activity; server stop/expiry that wins before commit blocks persistence. Incomplete bodies and timed-out reads are deleted. If a browser abort arrives after the full body has already been received and the commit has entered its critical section, that commit may win; StandBy UI tells users to inspect the collection before retrying the current photo.
- Avatar retains its single-successful-upload claim and resets the claim after failed/cancelled attempts. Stop closes active clients.
- Added explicit browser cancel controls for Avatar, Wallpaper, and StandBy. Client abort closes the request; server-side receive cleanup removes incomplete temporary files.
- StandBy batch state persists the current index and completed counts across transport retries. A retry resumes the current photo; completed items are not replayed. HTTP 410 stops the batch and requires a new QR session.
- Wallpaper/StandBy retryable transport/server failures retain the current item; terminal validation failures advance to the next selected item.
- StandBy success JSON now reports the final stored filename, not the temporary `.upload` filename.
- Serialized Avatar accept, worker-start failure, worker-finally, and stop accounting under one monitor. Stop clears tracked sockets and resets the count atomically; late worker-finally paths decrement only when they still remove a tracked socket.

## 4. Regression coverage

Added:
- `UploadDeadlineInputStreamTest`: read timeout is applied on each read; absolute deadline rejects reads after expiry.
- `UploadDeadlinePolicyTest`: idle and absolute deadline intersection, session activity, and boundary behavior.

Existing C01–C06 endpoint, exact-byte receive, file-policy, Avatar, Wallpaper, and StandBy regression suites remain required.

## 5. Verification

- GitHub Actions CI #366 / run `41292640936` passed on exact PR head `496d49d79f6c9a90ac50011e7a596b5b5a2ba6e3`; both required jobs succeeded.
- `testReleaseUnitTest`, `assembleRelease`, read-only APK diagnostics, and the 1.5 MiB APK size gate passed; manual APK publication was skipped.
- The documentation update itself triggers a new exact-head validation; its result is authoritative for closing the C07 source/CI gate.
- Required gates: `testReleaseUnitTest`, `assembleRelease`, read-only APK diagnostics, 1.5 MiB APK size gate, documentation/tools validation.
- Manual APK publication remains disabled/skipped; no APK installation or device-setting changes.
- Physical phone-to-TV disconnect, cancellation, retry, and expiry acceptance remains deferred to F-stage and is not waived.

## 6. Disposition

C07 implementation and source-head CI are PASS at `496d49d79f6c9a90ac50011e7a596b5b5a2ba6e3`. The final documentation-inclusive PR head must also retain both required CI jobs as PASS before the source/CI gate is closed. PR remains Draft/unmerged under D-013.
