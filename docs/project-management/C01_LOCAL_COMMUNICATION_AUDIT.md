# C01 — Local Communication & Server Foundation Audit

**Date:** 1 November 2026  
**Status:** SOURCE CHANGES IMPLEMENTED — EXACT-HEAD CI PENDING  
**Work branch:** `feature/c01-local-communication-audit`  
**Parent:** B07 source head `3fcbf8d52d0baafe76d8239a5e5dab7634d0850e`  
**Merge policy:** D-013 — isolated branch and Draft PR; no early merge.

## 1. Scope

Audit the Android launcher local-network communication foundation used by QR upload flows:
- profile avatar upload;
- wallpaper upload and gallery management;
- StandBy photo upload;
- their MainActivity lifecycle and QR URL construction;
- request framing, client resource limits, image validation, and local interface binding.

The installer module's `LocalApkServer` is inventoried separately because it is a manually started APK-distribution utility, not part of the launcher QR upload lifecycle. Its open LAN endpoint, thread-per-client model, lack of socket timeout, and fallback IP behavior are recorded for a dedicated installer transport review; it is not modified in C01 to avoid coupling installer behavior into launcher QR changes.

## 2. Existing architecture and preserved behavior

| Flow | Existing contract | C01 treatment |
|---|---|---|
| Avatar QR | UUID-derived 128-bit session token; 5-minute session; one successful upload; 15 MiB limit; max 4 active clients; selected local IPv4 binding | Preserved |
| Wallpaper QR | Per-server random session path; upload + gallery list/select/delete/slideshow/clear APIs; max 60 MiB payload | Preserved; server resource and request validation hardened |
| StandBy QR | Separate `/standby/<token>` and upload route; collection quota enforced by `StandbyPhotoStore`; max 60 MiB per photo | Preserved; request and image validation hardened |
| Lifecycle | MainActivity stops QR servers when dialog is dismissed and on Activity destruction | Preserved |
| Timeout policy | Avatar socket idle 90s; wallpaper/StandBy socket idle 30s; browser upload 240s/600s | Preserved; operation budgets are intentionally distinct |

No QR routes, user-facing screens, file formats, storage directories, upload size limits, callback contracts, or existing avatar single-upload semantics were removed.

## 3. Findings and repairs

| ID | Finding | Risk | Repair |
|---|---|---|---|
| C01-F01 | Wallpaper server bound to wildcard interfaces although the QR advertises one selected LAN address | Unnecessary exposure on other interfaces | Bind only to the selected usable IPv4 address; expose that exact bound address and use it when building wallpaper/StandBy QR URLs |
| C01-F02 | Wallpaper server spawned an unbounded thread for every accepted socket | Resource exhaustion / thread growth | Cap active clients at 4; reject excess connections with HTTP 503; worker threads are daemonized and cleaned up |
| C01-F03 | Server worker loop referenced the mutable serverSocket field | Stop/restart race could let an old worker observe a newly assigned socket | Capture the listening ServerSocket per server-thread instance |
| C01-F04 | Wallpaper/StandBy accepted ambiguous Content-Length and did not reject Transfer-Encoding | Ambiguous HTTP body framing | Require exactly one positive decimal Content-Length; reject duplicate, malformed, missing, oversized lengths and all Transfer-Encoding |
| C01-F05 | Wallpaper/StandBy validated image headers/dimensions but did not confirm a real decode | Truncated/corrupt image could pass initial checks | Validate dimensions and decode a bounded sample before accepting the file |
| C01-F06 | Wallpaper rejected exactly 60 MiB while StandBy accepted it | Inconsistent boundary behavior | Both flows now share the same inclusive 60 MiB maximum |

### Important implementation notes

- The server continues to stream uploads to temporary files; it does not buffer the full upload in memory.
- A failed/incomplete image is deleted and is not passed to the feature listener.
- The image sample decoder is bounded to approximately 512 pixels per dimension and recycles the decoded bitmap.
- Existing QR session tokens and operation-specific timeout values are not changed.
- The local server remains HTTP on the private LAN; the unpredictable session token remains the route capability.

## 4. Regression coverage added

`WallpaperUploadSessionRouteTest` now covers:
- valid single Content-Length;
- exact 60 MiB boundary;
- missing, duplicate, malformed, zero, and oversized Content-Length;
- rejection of Transfer-Encoding;
- existing session route isolation and unsupported method checks.

Existing avatar route, byte-limit, and image-dimension policy tests remain unchanged.

## 5. Remaining audit boundary

1. **Installer transport:** `launcher/installer/.../LocalApkServer.java` is a separate user-started utility. It currently uses a public LAN route, unbounded client threads, no socket timeout, and a hard-coded fallback IP. Keep it visible as a separate follow-up; do not mistake it for the launcher QR server.
2. **Physical network acceptance:** verify phone and TV on the same Wi-Fi, selected interface correctness, port fallback, upload/retry, dialog-close revocation, and network loss on real devices during final system acceptance.
3. **Platform lifecycle:** verify API 30 and API 33+ behavior in the final integrated acceptance matrix.

These are not marked PASS by static source review.

## 6. C01 acceptance gates

| Gate | Status |
|---|---|
| Source audit of launcher QR server and callers | PASS |
| Selected-interface binding and QR URL consistency | IMPLEMENTED |
| Bounded wallpaper/StandBy client concurrency | IMPLEMENTED |
| Strict request body framing | IMPLEMENTED |
| Image decode validation | IMPLEMENTED |
| Regression unit tests added | IMPLEMENTED — CI pending |
| Exact-head GitHub Actions CI | PENDING |
| Real phone / TV same-Wi-Fi test | DEFERRED to final system acceptance |
| API 30 / API 33+ lifecycle test | DEFERRED to final system acceptance |

**C01 is not complete until exact-head CI is green.** Device/runtime checks remain deferred, not waived. No APK publication, installation, or TV settings change is authorized by this work.
