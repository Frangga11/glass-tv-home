# C04 — HTTP Endpoint, Routing, Method & Response-Code Audit

**Date:** 1 November 2026  
**Status:** SOURCE + EXACT-HEAD CI VERIFIED — PASS; PHYSICAL ACCEPTANCE DEFERRED  
**Branch:** feature/c04-http-endpoint-routing  
**Parent:** C03 exact source head d266287df287167e5a8e73fd675b51759b580ed6  
**Merge policy:** D-013 — isolated Draft PR; no early merge.

## 1. Scope

Audit the HTTP surface exposed by Avatar and Wallpaper/StandBy QR upload services: request-line syntax and HTTP version; token-scoped page/upload/API routes; allowed methods and preflight behavior; query parameters used by thumbnail/select/delete; status codes and response framing; success/failure propagation to browser UI.

C04 does not redesign upload body validation, file-type/dimension/size policy, storage naming/path policy, retries, or disconnect/expiry policy; those remain C05–C07. C01–C03 behavior is retained.

## 2. Findings

| ID | Severity | Finding | Impact |
|---|---|---|---|
| C04-F01 | HIGH | Wallpaper session authorization relied on request-line prefix checks; Avatar split the line and accepted a minimum token count without validating HTTP version or exact request-line shape. | Malformed or extra-token requests could be routed; path/query handling differed between services. |
| C04-F02 | MEDIUM | Unsupported methods often fell through to 404 rather than 405; Wallpaper preflight accepted OPTIONS broadly beneath the session prefix. | Misleading route errors and an unnecessarily broad preflight contract. |
| C04-F03 | MEDIUM | Wallpaper select/delete returned success JSON even when files did not exist; callback failures were swallowed. | Browser UI could report success for an operation that had not happened. |
| C04-F04 | MEDIUM | Gallery browser actions parsed JSON but did not check HTTP status. | JSON error responses could still be treated as successful actions. |
| C04-F05 | LOW | Some thumbnail/list failures closed the connection or only logged errors; incomplete request headers could be routed after EOF. | Clients received empty or misleading responses instead of deterministic HTTP errors. |
| C04-F06 | LOW | Query parsing returned empty strings for missing/malformed input and silently accepted the first duplicate value. | Ambiguous/missing file values were not distinguished from valid values. |
| C04-F07 | MEDIUM | HTTP header lines were not uniformly checked for obsolete folding, invalid field names, or control characters before route dispatch. | Ambiguous or malformed header syntax could be accepted by one layer and interpreted differently by another. |

## 3. Remediation

1. Added Android-free UploadHttpRequest parser. It requires exactly three request-line fields, uppercase method token, origin-form path, and HTTP/1.0 or HTTP/1.1; separates path/query and rejects malformed, extra-token, fragment, and whitespace cases.
2. Both services require complete HTTP headers before route processing. Incomplete headers and invalid request lines return 400.
3. Avatar page/upload routes compare parsed paths (query does not alter route identity), return 404 for unknown paths and 405 with Allow for wrong methods.
4. Wallpaper/StandBy routes use exact endpoint maps and per-endpoint method contracts. Unknown session paths return 404; known paths with wrong methods return 405 and Allow; OPTIONS is answered only for known Wallpaper endpoints and returns endpoint-specific allowed methods.
5. Query parsing rejects missing, malformed, or duplicate parameters rather than silently selecting one.
6. Wallpaper select/delete/slideshow/clear APIs return status codes reflecting validation, missing files, unavailable callbacks, and operation failures. List/thumbnail failures now produce explicit errors with response framing.
7. Browser gallery POST actions check response.ok, refresh only after success, and show localized failure feedback. Added the matching Indonesian/default/English string.
8. Existing upload payload limits, tokens, route destinations, storage behavior, session lifetimes, and C01–C03 network binding are preserved.
9. Added strict shared header validation in both Avatar and Wallpaper/StandBy handlers: reject folded headers, invalid field-name syntax, malformed lines, and prohibited control characters while retaining valid headerless HTTP/1.0 requests.

## 4. Regression coverage

- Strict HTTP/1.0 and HTTP/1.1 request-line parsing.
- Accept valid standard headers and headerless requests; reject folded, malformed, invalid-name, and control-character headers.
- Reject missing fields, extra tokens, repeated separators, absolute-form targets, fragments, and unsupported versions.
- Exact session path segment boundaries.
- Endpoint-to-method matrix for page, upload, thumbnail, list, select, delete, slideshow, and clear.
- Avatar page/upload route matching with query strings and suffix rejection.
- Existing C01 route/content-length tests and C03 network-address tests remain inherited and must continue passing.

## 5. Verification

- CI #342 / `41288521501` passed on code/test head `f09ee1b30b80e45e793c1678935eff8867aedae4`.
- CI #350 / `41288909657` passed on intermediate head `4e10c725818d7f0a97b9a4a46a32bae92c240fcb`.
- Latest exact-head CI #354 / `41289030375` passed on `d147d8daa254b8fa56489ae1ce7bfdd6092695b4`; both jobs SUCCESS. Launcher/installer `testReleaseUnitTest` and `assembleRelease` succeeded.
- Release APK diagnostic: 1,548,097 bytes; 14,767 bytes below the 1,572,864-byte (1.5 MiB) cap. Publication skipped.
- Physical phone/TV browser validation remains deferred to F-stage final acceptance, not waived.

## 6. Disposition

- HTTP request parser and route contracts: IMPLEMENTED.
- Response-code/API failure semantics: IMPLEMENTED.
- Browser action status handling: IMPLEMENTED.
- Regression tests: PASS in exact-head CI #354.
- Release APK size gate: PASS with 14,767 bytes of headroom; avoid nonessential APK growth.
- Physical phone/TV browser acceptance: DEFERRED, NOT WAIVED.
- No merge, APK publication/installation, or device-setting change.
