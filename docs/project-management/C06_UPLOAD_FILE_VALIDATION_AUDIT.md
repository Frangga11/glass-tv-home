# C06 — Upload File Validation Audit

**Date:** 1 November 2026  
**Status:** IMPLEMENTATION COMPLETE — EXACT-HEAD CI PENDING — PHYSICAL ACCEPTANCE DEFERRED  
**Branch:** `feature/c06-upload-file-validation`  
**Parent:** C05 exact head `53cf9a883f061632eb5b51d4b50b828100076acb`  
**Merge policy:** D-013 — isolated Draft PR; no early merge.

## 1. Scope

Harden file policy for Avatar, Wallpaper, and StandBy QR uploads: actual file type/signature, declared MIME consistency, image dimensions/decode, per-file and collection size limits, generated filenames/extensions, and managed storage paths.

C06 preserves the existing per-feature size and dimension ceilings. C05 remains responsible for bounded exact body reception and partial-file cleanup. C07 remains responsible for disconnect, retry, expiry, cancellation, and timeout policy.

## 2. Baseline findings

| ID | Severity | Finding | Impact |
|---|---|---|---|
| C06-F01 | MEDIUM | Upload validation relied on BitmapFactory decode/dimensions but did not first enforce a shared image-format signature allowlist or compare a declared Content-Type. | Client MIME and actual file format could disagree; accepted formats were implicit. |
| C06-F02 | MEDIUM | Wallpaper and StandBy uploads were always stored with a `.jpg` suffix regardless of actual image bytes. | Stored extension could misrepresent the real format and downstream format filters could miss files. |
| C06-F03 | MEDIUM | Wallpaper API filename handling removed disallowed characters instead of rejecting unsafe names, and path resolution did not consistently verify canonical containment. | Ambiguous names and path traversal/symlink edge cases were not handled by one strict policy. |
| C06-F04 | LOW | MainActivity's custom-wallpaper filters only enumerated JPG/PNG/JPEG. | New format-preserving storage needed matching consumers to avoid hidden uploads. |

## 3. Implemented policy

- Shared pure-Java `UploadFilePolicy` detects JPEG, PNG, and WebP by file signature; the client filename is not trusted.
- If one Content-Type is supplied, it must match the detected format. Missing or `application/octet-stream` is allowed for browser compatibility; duplicate, empty, or mismatched declarations are rejected. JPEG aliases `image/jpg` and `image/pjpeg` are normalized.
- Existing BitmapFactory decode verification remains mandatory after signature/MIME checks.
- Existing limits are retained:
  - Avatar: 15 MiB; max 12,000 px per side; max 80 million pixels.
  - Wallpaper: 60 MiB; max 16,384 px per side; max 100 million pixels.
  - StandBy: 60 MiB per photo; existing 100-photo / 512 MiB collection limits and free-space guard retained; same image dimension/decode policy as Wallpaper.
- Avatar, Wallpaper, and StandBy now store generated filenames with extensions derived from detected bytes. They do not use the phone-supplied filename as a filesystem path. Rename refuses to overwrite an existing destination.
- Wallpaper thumbnail/list/select/delete/clear paths now accept only strict generated-style image names and resolve existing files as canonical direct children of `custom_wallpapers`. Clear only removes recognized managed image files.
- StandBy managed-file checks now require a safe image filename and canonical direct-child containment.
- MainActivity's eight custom-wallpaper enumerators use the shared allowlist, keeping JPEG/PNG/WebP files visible throughout the existing UI.
- Upload pickers advertise the supported JPEG/PNG/WebP formats, matching the documented format hint.

## 4. Regression coverage

Added `UploadFilePolicyTest` for:
- JPEG/PNG/WebP signatures and unknown/short payload rejection.
- MIME match, aliases, generic/absent MIME, empty/mismatched/duplicate MIME rejection.
- Wallpaper/StandBy dimension boundaries and pixel-count overflow-safe calculation.
- Safe filename allowlist and rejection of traversal, separators, hidden names, unsupported extensions, and spaces.
- Canonical direct-child resolution and rejection of missing/outside paths.

Existing Avatar size/dimension, StandBy store, wallpaper route, and C01–C05 regression suites remain required.

## 5. Verification

- Source implementation and focused JVM tests committed to the isolated C06 branch.
- Exact-head GitHub Actions CI: pending.
- Physical phone-to-TV file-type, MIME, image-decoder, and storage acceptance remains deferred to F-stage and is not waived.
- No APK publication/installation, merge, or device-setting change.

## 6. Disposition

C06 implementation is complete pending exact-head CI. Do not mark source/CI gate PASS until both required CI jobs succeed on the final C06 head. PR remains Draft/unmerged under D-013.
