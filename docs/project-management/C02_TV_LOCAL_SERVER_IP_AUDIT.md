# C02 — TV Local Server & IP Address Audit

**Date:** 1 November 2026  
**Status:** SOURCE AUDIT — FINDINGS RECORDED; REMEDIATION NOT YET APPLIED  
**Work branch:** `feature/c02-tv-local-server-ip`  
**Parent:** C01 exact source head `bba0801c9eb61ab27ae5046379c7a50aabebf570`  
**Merge policy:** D-013 — isolated branch and Draft PR; no early merge.

## 1. Scope

C02 covers the installer module's phone-to-TV local network utility:
- local phone IPv4 discovery and selection;
- subnet discovery and Android TV ADB scan;
- temporary HTTP APK distribution server;
- server bind address, port selection, request handling, concurrency, timeout and stop/restart lifecycle;
- caller integration in installer `MainActivity`.

Audited files:
- `launcher/installer/src/main/java/com/gathin/tvhome/installer/TvScanner.java`
- `launcher/installer/src/main/java/com/gathin/tvhome/installer/LocalApkServer.java`
- `launcher/installer/src/main/java/com/gathin/tvhome/installer/MainActivity.java`
- `launcher/installer/src/main/AndroidManifest.xml`
- `launcher/installer/build.gradle`

C02 is distinct from C01's launcher avatar/wallpaper/StandBy QR upload servers. The latter remain on their own source branch and are not rewritten by this audit.

## 2. Findings

| ID | Severity | Finding | Impact |
|---|---|---|---|
| C02-F01 | HIGH | `TvScanner.getLocalIpAddress()` returns hard-coded `192.168.1.100` when discovery fails. Its interface fallback does not require an interface to be up, does not prefer the active route, and does not exclude IPv4 link-local addresses. | The displayed phone IP, scan subnet and APK download URL can be wrong or unreachable. |
| C02-F02 | HIGH | `TvScanner.scan()` derives the scan range by taking the first three IPv4 octets, assuming every LAN is /24. | Misses devices on other subnet masks and can scan the wrong address range on routed/multi-interface networks. |
| C02-F03 | HIGH | `LocalApkServer` uses `new ServerSocket(port)`, which binds wildcard/all local interfaces, while its displayed URL is built from a separately selected IP. | The service is reachable through interfaces beyond the intended phone-TV LAN; advertised address and actual listening scope are not tied together. |
| C02-F04 | HIGH | The HTTP server creates an unbounded thread for each accepted client and has no socket read timeout or bounded request-line/header parsing. | Slow or idle clients can retain threads/sockets and grow memory/resource use; concurrent downloads are not capped. |
| C02-F05 | HIGH | Stop/restart lifecycle is not race-safe: server state is a non-volatile static boolean; the accept loop reads the mutable static `serverSocket`; stop does not join the server thread or close tracked active clients. A stop racing with the server thread before bind can leave a newly created socket open. | Old workers/downloads may outlive stop; a rapid restart can make an old worker observe the new socket; cleanup is not deterministic. |
| C02-F06 | MEDIUM | APK route validation uses `line.contains("GET /glass-tv.apk")` rather than parsing and matching the exact HTTP method and path. | Prefix/suffix paths can be treated as the APK download endpoint. |
| C02-F07 | MEDIUM | `TvScanner.scan()` waits at most four seconds but does not cancel unfinished tasks. Remaining tasks may call `onDeviceFound` after `onScanFinished`; there is no scan cancellation or Activity-lifecycle guard. | UI state and results can arrive out of order or after the caller is no longer active. |
| C02-F08 | MEDIUM | Automatic discovery probes only TCP 5555. | It detects legacy ADB-over-TCP only; Android wireless debugging/pairing configurations using dynamic ports are not discoverable by this scan. Manual IP/port entry remains available. |
| C02-F09 | MEDIUM | `LocalApkServer` has no active-client registry and does not guarantee socket and APK InputStream closure through a finally/try-with-resources path on every exception. | A failed download or stop during transfer can leave resources open until runtime cleanup. |
| C02-F10 | LOW | APK server always attempts one configured port and does not report the actual bound port through a bound-socket accessor. | Port collision makes server startup fail even when another safe port is available. |

## 3. Existing behavior to preserve

- Installer UI supports manual TV IP/ADB port entry.
- The last manually entered TV IP is persisted.
- Automatic scan reports discovered TCP/5555 hosts.
- User explicitly starts/stops the temporary APK download server.
- Browser/Downloader receives a URL and downloads the packaged APK at `/glass-tv.apk`.
- ADB install, launch, and set-default-home flows are separate from the HTTP APK-serving transport; C02 should not change their protocol or authorization behavior without a demonstrated dependency.

## 4. Recommended remediation design

1. Replace the hard-coded IP fallback with an explicit “no usable local IPv4” result. Select only an up, non-loopback, non-link-local IPv4 interface and prefer the active Wi-Fi/Ethernet network that will carry the phone-TV traffic. Do not infer a subnet mask from an IP address.
2. Derive scan candidates from the actual active IPv4 LinkAddress prefix, cap the scan size, and keep manual IP/port entry as the reliable fallback. Clearly report unsupported/non-scannable subnet sizes instead of silently assuming /24.
3. Bind the temporary HTTP server to the selected interface address and build the displayed URL from the exact address and actual port returned by the bound socket.
4. Add bounded active-client concurrency, per-client read timeout, bounded request-line/header size, exact method/path matching, deterministic error responses, and guaranteed stream/socket closure.
5. Make server state safely visible across threads; capture the listening socket per server instance; track active client sockets; close all of them on stop; guard callbacks against stop/restart races; ensure a failed or cancelled start cannot leave a bound socket behind.
6. Preserve the user-started, temporary download flow. Do not expose the APK server as an always-on service. Evaluate a short-lived unpredictable download path/token in addition to interface binding, without adding friction to the existing TV browser flow.
7. Try a small, documented port candidate list and then an ephemeral port; always publish the actual bound port.
8. Make scanner completion deterministic: cancel remaining probes at deadline, prevent post-completion device callbacks, and suppress callbacks when the owning Activity is destroyed.

## 5. Verification plan

### Source/unit tests
- IP selection rejects loopback, link-local, down interfaces, IPv6 and hard-coded fallback; chooses among Wi-Fi/Ethernet/VPN/virtual candidates deterministically.
- Subnet calculations for /24, /23, /25 and unsupported/oversized prefixes; host-range boundaries; no scan beyond configured cap.
- Server binds only to selected address; QR/manual URL matches bound address and actual port; port collision fallback works.
- Exact HTTP route/method matching; prefix/suffix and malformed request rejection.
- Client concurrency ceiling; idle timeout; oversized headers; incomplete download; APK stream/socket cleanup.
- Stop during accept, stop during download, immediate stop-before-bind, repeated start/stop, and rapid stop/restart.
- Scanner timeout/cancellation and callback ordering; no callback after completion or Activity destruction.

### Runtime acceptance (final integrated device test)
- Phone and TV on same Wi-Fi; confirm displayed phone IP is the active LAN address.
- Verify automatic scan on the target subnet and manual IP fallback.
- Open the exact server URL from TV browser/Downloader; download a complete APK and verify file size/hash.
- Confirm another network interface cannot reach the server when it is not the selected LAN.
- Stop the server during idle and active download; confirm URL becomes unreachable and restart works.
- Repeat after Wi-Fi disconnect/reconnect and after Activity destruction.

## 6. Audit disposition

- C02 source audit: **FINDINGS RECORDED**.
- Source remediation: **NOT STARTED**.
- Unit/CI verification: **NOT RUN for C02**.
- Physical phone/TV acceptance: **DEFERRED to final integrated acceptance; not waived**.
- No APK was published or installed; no device settings were changed.
- C01 remains separate and unmerged. No source code was changed during this C02 audit.
