# F03 — QR Communication Feasibility Audit

**Checkpoint:** GTVH-F03-CP-2026-11-03  
**Date:** 3 November 2026  
**Disposition:** HOST/SOURCE FEASIBILITY PASS; OVERALL F03 PARTIAL — real-device acceptance remains deferred to Project G.  
**Scope:** QR communication and upload across Avatar, Wallpaper, and StandBy: active LAN address, server binding, HTTP routes, request/body handling, image validation, retry/timeout/expiry/cancellation, persistence boundary, and storage pressure.

## 1. Governance and test boundary

- F03 is a Project F validation workstream, not a new feature implementation branch. No dedicated F03 source branch or PR was found; the validated integrated candidate is integration/project-f02-system-validation, PR #50, exact final checkpoint 2ede452b3137580bd51088661f80b6d0eeac7f4b.
- Project F host/source validation only. Do not install an APK, publish an artifact, alter TV settings, or claim physical phone/TV acceptance. Real Mi TV Stick + phone/LAN tests remain Project G.
- Owner PRs #18–#24 remain Draft/open/unmerged under the staged merge policy. No early merge was performed.

## 2. Owner-module evidence

| Module | F03 coverage | Recorded evidence | Result |
|---|---|---|---|
| C01 / PR #18 | Local QR server, selected-interface binding, bounded concurrency, HTTP framing and upload route query handling | Exact-head CI #549 PASS | HOST CI PASS |
| C02 / PR #19 | Server/IP discovery, bounded LAN scan, port fallback and lifecycle cleanup | Latest recorded head b0e1ff048e36bf21f8da1e165a427075f85a7cb3; CI #333 PASS | HOST CI PASS |
| C03 / PR #20 | Active Wi-Fi/Ethernet IPv4 selection; QR URL uses the actual bound address | Latest recorded head d266287df287167e5a8e73fd675b51759b580ed6; CI #338 and global UI CI #550 PASS | HOST CI PASS |
| C04 / PR #21 | Strict request-line/header parsing, exact token-scoped routes, HTTP method/status contracts | Head aad9aeb163b46ff4c13a8d93c160a3eec78f9770; CI #355 PASS | HOST CI PASS |
| C05 / PR #22 | Shared bounded upload-body reception and Content-Length enforcement | Head 53cf9a883f061632eb5b51d4b50b828100076acb; CI #361 PASS | HOST CI PASS |
| C06 / PR #23 | JPEG/PNG/WebP signature validation, declared MIME consistency, safe names/paths and no overwrite | Head e74b6466c058ec930aa92c8db9603b11cf96a185; CI #363 and global UI CI #551 PASS | HOST CI PASS |
| C07 / PR #24 | Disconnect, retry, expiry, cancellation, monotonic deadlines, shutdown atomicity and storage reservation | Owner head 442b78c68b1540394d543bab59bddf52f865ccf0; CI #41521013896 PASS | OWNER HOST PASS |

## 3. Integrated F02 candidate evidence reused by F03

- Integration PR #50 exact head: 2ede452b3137580bd51088661f80b6d0eeac7f4b.
- GitHub Actions run #41523201383 completed SUCCESS. Both required jobs completed SUCCESS: Lists/tools/docs and Launcher build/unit tests.
- Launcher validation steps succeeded: Android unit tests + release validation build, read-only APK diagnostics, and APK size budget gate.
- Artifact list is empty. No APK was published or installed.
- C07 owner PR #24 exact head 442b78c68b1540394d543bab59bddf52f865ccf0 run #41521013896 also completed SUCCESS; both jobs succeeded and artifact list is empty.
- F02 integrated validation recorded 42 Python tests (one intentional merge-time README count normalization skip), Android testReleaseUnitTest and assembleRelease PASS, APK diagnostic 1,565,713 bytes against 1,572,864-byte cap (7,151 bytes headroom).

## 4. Targeted regression-source review

Inspected the following tests on the F02 integration branch:

- tests/test_upload_storage_reservation.py: guards overflow-safe arithmetic, process-wide reservation lock/counter, reservation/release on Wallpaper and StandBy paths, and HTTP 507 insufficient-storage response.
- tests/test_wallpaper_session_shutdown_atomicity.py: guards stop/mutation serialization, in-lock session rechecks, HTTP 410 expiry response, custom Wallpaper rename/callback atomicity, and StandBy persistence/callback atomicity.
- tests/test_upload_monotonic_deadline_clock.py: guards use of SystemClock.elapsedRealtime() rather than wall-clock time for Avatar/Wallpaper/session/read deadlines.
- tests/test_recent_apps_manifest_security.py was inspected as a neighboring F02 regression, not an F03 QR test; it is outside F03 acceptance scope.

These source guards are included in the integrated host CI. This review does not claim a separate local test execution or a real socket/browser/device simulation.

## 5. Findings and risk assessment

No new source-level blocker was identified in the reviewed F03 scope. The prior C07 defects have recorded owner + integration fixes and passing host CI: monotonic deadline clock, session shutdown atomicity, process-wide upload byte reservation, 64 MiB minimum free-space margin, HTTP 507, and release of reservations in all exit paths. Expired/revoked session behavior returns HTTP 410.

The following are explicitly NOT VERIFIED and must not be marked PASS:

1. Actual QR scan and phone browser access over the target Mi TV Stick Wi-Fi/Ethernet LAN.
2. Real disconnect, retry, cancel, expiry, and reconnect timing under Android/OEM/network behavior.
3. Concurrent uploads and low-storage behavior on the physical device.
4. Avatar/Wallpaper/StandBy persistence and isolation after process death/reboot.
5. Real browser compatibility, network-interface changes, and remote/D-pad behavior around QR dialogs.

These are Project G device acceptance gates, not reasons to repeat already-passing source CI in F03.

## 6. Decision

**F03 host/source feasibility: PASS. Overall F03: PARTIAL / NOT DEVICE-ACCEPTED.** No new code change is justified by the evidence reviewed. Keep PRs staged and unmerged. Carry the five device/LAN acceptance groups above into Project G with named evidence and outcomes. Do not label the complete QR feature device-ready until those tests pass.

## 7. Next action

Continue the remaining Project F validation workstream only after recording this checkpoint. Project G later performs real Mi TV Stick + phone/LAN acceptance, resolves any reproducible device-only defects through their owning branches, then repeats the affected tests.