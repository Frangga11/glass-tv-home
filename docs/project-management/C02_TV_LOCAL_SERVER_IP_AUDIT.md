# C02 — TV Local Server & IP Address Audit

**Date:** 1 November 2026  
**Status:** IMPLEMENTATION APPLIED — AUTOMATED CI PENDING; DEVICE ACCEPTANCE DEFERRED  
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

## 4. Remediation applied

1. IP discovery now uses the active default Wi-Fi/Ethernet network and its IPv4 LinkAddress. No fabricated fallback address is returned; loopback, link-local, invalid and VPN-default cases are rejected.
2. Scanner uses the actual subnet prefix, supports bounded ranges up to 512 usable hosts, and clearly directs oversized/unsupported ranges to manual IP/port entry. Probe tasks have a fixed pool, connection timeout, global deadline, cancellation and serialized callback completion.
3. The APK server binds to the selected LAN IPv4 only. The URL uses the bound socket's actual port.
4. The HTTP server caps active clients at four, applies a 10-second request read timeout, bounds request headers to 8 KiB, returns explicit HTTP errors, matches GET routes exactly, and closes APK streams and sockets deterministically.
5. Each server run owns an immutable listening socket and isolated state. Active clients are tracked and closed on stop; accept/stop registration is synchronized; start/stop/restart and callback ownership are guarded.
6. The user-started temporary download workflow and existing /glass-tv.apk browser route are preserved. The service is not started automatically.
7. Port selection tries the requested port, documented fallbacks, then an ephemeral port; the published URL always reflects the actual bound port.
8. Installer Activity cancels scans during teardown and ignores stale UI callbacks. Manual TV IP and ADB port entry remain intact.

**Known limitation retained intentionally:** automatic scan detects legacy ADB TCP port 5555 only. Wireless Debugging pairing/dynamic ports are not discovered; manual IP/port entry remains available. Adding pairing support would be a separate feature, not a safe incidental change to C02.

## 5. Verification plan

### Automated unit tests added
- IPv4 unsigned-byte conversion and string conversion.
- Actual subnet network/broadcast calculation for /23 and /25; invalid prefixes/addresses; oversized /22 rejection.
- Exact GET download and landing-route matching; method and path-prefix/suffix rejection.

### CI coverage
- Repository CI runs ./gradlew --no-daemon testReleaseUnitTest assembleRelease from the launcher root, plus the repository tools/docs checks.
- Exact latest C02 head must pass both jobs before C02 is marked CI-verified.

### Runtime acceptance (final integrated device test)
- Phone and TV on same Wi-Fi; confirm displayed phone IP is the active LAN address.
- Verify automatic scan on /23 and /24 networks; manual IP fallback on oversized or unsupported subnet.
- Open the exact server URL from TV browser/Downloader; download a complete APK and verify file size/hash.
- Confirm a non-selected local interface cannot reach the server.
- Stop the server during idle and active download; confirm URL becomes unreachable and restart works.
- Repeat after Wi-Fi disconnect/reconnect and after Activity destruction.

### Runtime acceptance (final integrated device test)
- Phone and TV on same Wi-Fi; confirm displayed phone IP is the active LAN address.
- Verify automatic scan on the target subnet and manual IP fallback.
- Open the exact server URL from TV browser/Downloader; download a complete APK and verify file size/hash.
- Confirm another network interface cannot reach the server when it is not the selected LAN.
- Stop the server during idle and active download; confirm URL becomes unreachable and restart works.
- Repeat after Wi-Fi disconnect/reconnect and after Activity destruction.

## 6. Current disposition

- C02 source audit: **COMPLETE**.
- Findings F01–F07, F09–F10: **REMEDIATION APPLIED**.
- Finding F08: **KNOWN SCOPE LIMITATION** — legacy ADB TCP/5555 discovery only; manual port entry preserved.
- Regression tests: **ADDED; CI RESULT PENDING**.
- Physical phone/TV acceptance: **DEFERRED to final integrated acceptance; not waived**.
- No APK was published or installed; no device settings were changed.
- C01 remains separate and unmerged under D-013.
