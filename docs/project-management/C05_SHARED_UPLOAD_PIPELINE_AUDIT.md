# C05 — Shared Upload / Receive-File Pipeline Audit

**Date:** 1 November 2026  
**Status:** IMPLEMENTATION IN PROGRESS — EXACT-HEAD CI PENDING  
**Branch:** `feature/c05-shared-upload-pipeline`  
**Parent:** C04 exact source head `aad9aeb163b46ff4c13a8d93c160a3eec78f9770`  
**Merge policy:** D-013 — isolated Draft PR; no early merge.

## 1. Scope

Audit and harden the common receive-file path used by Avatar, Wallpaper, and StandBy QR uploads: strict Content-Length interpretation, bounded streaming, exact byte accounting, partial-file cleanup, and durability before the uploaded file is handed to the existing application/storage callback.

Preserve existing per-feature limits, image dimension/decode validation, storage directories/naming, route contracts, QR tokens, listener semantics, and operation-specific timeouts. File-type/dimension/size/name/path policy remains C06; disconnect/retry/expiry/cancellation/timeout policy remains C07. C05 does not change those policies unless a direct shared-pipeline defect requires it.

## 2. Baseline findings

| ID | Severity | Finding | Impact |
|---|---|---|---|
| C05-F01 | MEDIUM | Avatar and Wallpaper/StandBy implemented separate Content-Length parsers. Their behavior and error mapping could drift as endpoint code evolves. | One shared HTTP body could be interpreted differently by the three receive paths. |
| C05-F02 | MEDIUM | Three upload handlers duplicated the bounded read/write loop and independently checked remaining bytes and file length. | Fixes for short reads, zero-length reads, exact boundary handling, and partial-file cleanup had to be repeated and could diverge. |
| C05-F03 | LOW | Avatar fsynced the temporary file before committing its path, while Wallpaper and StandBy only flushed. | Durability behavior differed before callbacks/storage accepted the received file. |

These are consistency and reliability findings in shared code paths; they do not imply that a production data-loss incident was observed.

## 3. Remediation in progress

1. Added Android-free `UploadBodyReader` to own strict Content-Length parsing and exact bounded body reception.
2. The parser rejects missing, duplicate, malformed, overflowing, transfer-encoded, zero, and over-limit lengths; it preserves the existing `-1` invalid / `-2` oversized result contract.
3. The receiver reads no more than the declared length, handles zero-length reads without stalling, verifies exact file length, fsyncs before success, and deletes partial files on truncation or I/O failure.
4. Avatar now uses the shared parser and receiver while retaining its 15 MiB limit, image bounds/decode checks, profile callback, and one-successful-upload session behavior.
5. Wallpaper and StandBy now use the same receiver while retaining their existing 60 MiB cap, image validation, storage destinations, and listener/store integration.
6. Wallpaper's existing `parseContentLength` test/API helper delegates to the shared parser to avoid breaking existing regression coverage.

## 4. Regression coverage added

- Valid positive length and exact maximum.
- Missing, duplicate, malformed, overflowing, transfer-encoded, zero, and oversized lengths.
- Exact byte copy without consuming bytes after Content-Length.
- Truncated body cleanup.
- Progress when an InputStream returns a zero-length read.

Existing C01–C04 route, HTTP, network, and upload tests remain required regression coverage.

## 5. Verification

- Source changes and focused JVM tests added on the isolated C05 branch.
- Exact-head GitHub Actions CI: PENDING.
- Release APK size gate: PENDING; avoid nonessential APK growth.
- Real phone/TV upload and physical TV acceptance: deferred to F-stage final acceptance, not waived.
- No APK publication/installation, merge, or device-setting change.

## 6. Disposition

C05 is not complete until source review, focused and inherited regression tests, exact-head CI, APK-size diagnostics, and documentation checks pass. Do not claim parity or completion based only on the implementation diff.
