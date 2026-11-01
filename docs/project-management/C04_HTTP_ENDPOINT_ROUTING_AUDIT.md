# C04 — Local HTTP Endpoint & Request Semantics Audit

**Date:** 1 November 2026  
**Status:** IMPLEMENTATION + EXACT-HEAD CI PENDING; PHYSICAL ACCEPTANCE DEFERRED  
**Work branch:** `feature/c04-http-endpoint-routing`  
**Parent:** C03 source head `d266287df287167e5a8e73fd675b51759b580ed6`  
**Merge policy:** D-013 — isolated Draft PR; no early merge.

## 1. Scope

Audit HTTP request parsing and endpoint semantics for:
- Avatar QR page and upload endpoint;
- Wallpaper QR page, upload, thumbnail, gallery APIs;
- StandBy QR page and upload endpoint;
- method/path matching, malformed request handling, HTTP status and response framing.

C04 does not change active network selection (C03), transport/server resource limits (C01), or installer APK delivery and ADB scanning (C02).

## 2. Findings

| ID | Severity | Finding | Impact |
|---|---|---|---|
| C04-F01 | HIGH | Request lines were split/checked inconsistently and some routes relied on prefix matching. | Malformed or colliding paths could be interpreted ambiguously. |
| C04-F02 | HIGH | HTTP header validation did not consistently reject obsolete folded or malformed header lines. | Request framing could be interpreted differently by clients and server code. |
| C04-F03 | MEDIUM | Wallpaper endpoints did not have one explicit method contract for each route. | Wrong methods could receive misleading route responses and preflight rules were broad. |
| C04-F04 | MEDIUM | Several wallpaper management APIs returned success even when files/listeners/actions failed. | Phone UI could report success while TV state remained unchanged. |
| C04-F05 | LOW | Thumbnail and gallery error paths were inconsistent and could fail without a complete HTTP response. | Client diagnostics and retry behavior were unreliable. |

## 3. Remediation

1. Added shared strict `UploadHttpRequest` parser: origin-form target only, exact request-line tokenization, HTTP/1.0 or HTTP/1.1, no fragments/control characters.
2. Added strict header syntax validation; reject obsolete folded headers, invalid header names, malformed lines, and control characters.
3. Avatar routes now require exact page GET or upload POST paths; malformed requests return 400, unknown paths 404, wrong methods 405 with Allow.
4. Wallpaper routes now have explicit per-endpoint method contracts; OPTIONS advertises only the endpoint's allowed methods.
5. Query parsing rejects duplicate/ambiguous named parameters. API select/delete validate the requested file and return meaningful 400/404/500/503 responses rather than unconditional success.
6. Thumbnail, list, and upload error paths return complete status, content type, content length, and connection-close framing.
7. Gallery actions now check HTTP success and show localized failure feedback instead of treating any JSON response as success.
8. Existing session tokens, route names, QR URLs, image validation, upload size limits, storage paths, and user-facing flows are preserved.

## 4. Regression coverage

- Valid and malformed HTTP request lines, exact path boundaries, query parsing, and HTTP versions.
- Valid, headerless, malformed, folded, and control-character header cases.
- Avatar page/upload route boundaries.
- Wallpaper endpoint-to-method matrix, unknown paths, wrong methods, and existing content-length rules.
- Full launcher unit tests and release build required in exact-head CI.

## 5. Runtime acceptance — deferred to final F-stage

On a real phone and TV, verify:
- Avatar QR page opens, valid avatar uploads, invalid file/method/path receives a clear failure.
- Wallpaper QR page opens; gallery list, thumbnail, select, delete, slideshow, clear, and upload all work.
- StandBy QR page opens and accepts valid photo uploads.
- Expired/closed QR sessions no longer work; wrong method and unknown route do not mutate data.
- Browser success/failure feedback matches the actual TV-side result.

Automated tests cannot prove real browser compatibility or the end-to-end TV UI state. Physical acceptance remains DEFERRED, not waived.

## 6. Disposition

- Source remediation: implemented; exact-head CI pending.
- Physical phone/TV acceptance: deferred to final integrated A–G acceptance.
- No APK publication/installation, device setting changes, or PR merge.
