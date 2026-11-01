# C03 — Phone-to-TV Communication & Same-Network Audit

**Date:** 1 November 2026  
**Status:** SOURCE FIXES APPLIED — EXACT-HEAD CI PENDING; PHYSICAL ACCEPTANCE DEFERRED  
**Work branch:** `feature/c03-phone-tv-network-verification`  
**Parent:** C02 exact source head `b0e1ff048e36bf21f8da1e165a427075f85a7cb3`  
**Merge policy:** D-013 — isolated Draft PR; no early merge.

## 1. Scope

Verify the launcher QR upload flows establish a reachable phone-to-TV HTTP route over the active local network:
- profile avatar QR;
- wallpaper QR;
- StandBy photo QR;
- active network/interface selection;
- bound IPv4 address, port, and QR URL consistency;
- same-Wi-Fi instructions and server startup failure behavior.

C03 does not expand into HTTP endpoint semantics, upload file validation, persistence, or server resource limits already assigned to C01/C04–C07. Installer APK distribution and ADB scanning remain C02.

## 2. Findings

| ID | Severity | Finding | Impact |
|---|---|---|---|
| C03-F01 | HIGH | `WallpaperUploadServer.getLocalIpAddress()` enumerated all interfaces and selected the first address based on interface-name preference (`wlan*`/ `eth*`), not Android's active default network. | On devices with multiple active interfaces, VPN, Ethernet plus Wi-Fi, or stale interfaces, QR generation could advertise an address that is not the route Android currently uses. |
| C03-F02 | MEDIUM | Avatar and Wallpaper/StandBy callers independently preflighted the no-argument address resolver, while each server resolved/bound again at start. | Network changes between preflight and bind could make the preflight result stale and diagnostics inconsistent. |
| C03-F03 | LOW | Address selection and validation rules were embedded in the upload server, making policy difficult to test independently. | No isolated regression coverage for loopback, link-local, IPv6, multicast, or unusable subnet prefixes. |

## 3. Remediation

1. Added `LocalNetworkAddress` as the single app-module resolver.
2. Resolve only Android's active default network through `ConnectivityManager`, `NetworkCapabilities`, and `LinkProperties`.
3. Accept only active Wi-Fi/Ethernet transports; reject VPN-default, cellular-only, missing, loopback, link-local, unspecified, multicast, IPv6, and unsupported prefix cases. No fabricated fallback address is returned.
4. Avatar, Wallpaper, and StandBy preflight checks and server bind operations now use the same resolver. Server URLs continue to use the exact address and actual bound port from the listening server.
5. Existing separate QR routes, tokens, same-Wi-Fi user instructions, upload limits, timeout budgets, session expiry, storage paths, and UI behavior are preserved.

## 4. Regression coverage

- Accept normal Wi-Fi/Ethernet IPv4 examples across /16, /23, and /24.
- Reject loopback, link-local, unspecified, multicast, and IPv6 addresses.
- Reject unsupported prefix lengths, including /31 and /32.
- Full launcher unit tests and release build are required in exact-head CI.

## 5. Runtime acceptance — deferred to final F-stage

On a real phone and TV:
- connect both devices to the same Wi-Fi/LAN;
- run Avatar, Wallpaper, and StandBy QR sessions independently;
- scan each QR with a phone and load the page;
- upload valid files and verify the correct feature store is updated;
- verify a phone on another network cannot reach the selected LAN-bound service;
- repeat after Wi-Fi reconnect, Ethernet/Wi-Fi switching, and VPN connect/disconnect;
- verify closing the QR dialog revokes the route.

A static source audit or CI run cannot prove access-point client isolation, router firewall policy, phone VPN routing, or actual phone-to-TV reachability. These checks remain DEFERRED, not PASS.

## 6. Disposition

- Active-network resolver: IMPLEMENTED.
- QR caller/server resolver consistency: IMPLEMENTED.
- Regression tests: ADDED; CI PENDING.
- Physical phone/TV same-network acceptance: DEFERRED, NOT WAIVED.
- No APK publication/installation, device setting changes, or PR merge.
