# G03 Device Acceptance Preflight — 5 November 2026

**Checkpoint:** GTVH-G03-PREFLIGHT-2026-11-05-01  
**Disposition:** PREFLIGHT PARTIAL / DEVICE EXECUTION BLOCKED — NOT FINAL  
**Canonical source:** `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`  
**Build artifact:** `glass-tv-home-final-562cde0` (GitHub artifact ID `13210715335`, run #1185 / `41590889219`)  
**Target:** Mi TV Stick, Android TV 11 / API 30; physical identity/build/resolution/density/storage must be re-read at test start.

## 1. Trigger and input

The canonical handover records G01 HOST/SOURCE PASS and G02 HOST BUILD + ARTIFACT PASS. G03 is the next approved stage: physical acceptance on the Mi TV Stick. The preflight rechecked live GitHub refs, artifact availability, downloaded artifact bytes, archive integrity and local execution-tool availability.

## 2. Live repository and artifact verification

- Live `integration/b-series` ref resolves to `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- G02 workflow run #1185 / ID `41590889219` is completed with conclusion `success`; both required jobs succeeded:
  - Launcher build and unit tests — job `125767104826`, SUCCESS.
  - Lists, tools and docs — job `125767105170`, SUCCESS.
- GitHub artifact `glass-tv-home-final-562cde0`, ID `13210715335`, is currently available (not expired); retention expiry is 10 November 2026 06:37:50 UTC.
- Downloaded ZIP size: 1,390,588 bytes. SHA-256: `669491bfc7c9f5279ecc6c30915e4a340aa492bee21531db203036caf8651c81`, matching GitHub artifact digest.
- ZIP contains `app-release.apk`, 1,572,293 bytes. Extracted APK SHA-256: `dc85fcd590eb2c52eb367d0bc9d4f67735cb94ef1cbf6f2b80b86f0de36de0dd`, matching the prior G02 record.
- APK ZIP integrity test: PASS; 151 entries; `AndroidManifest.xml`, `classes.dex`, and `resources.arsc` present.
- Artifact integrity is confirmed; this does not independently validate APK signing, installation compatibility, runtime behavior or device acceptance.

## 3. Environment and safety checks

- The current execution container has no `adb`, `apksigner`, `aapt`, or `zipalign` executable available. Therefore no local signature verification, ADB discovery, device shell inspection or installation was performed.
- No live Mi TV Stick connection/session is available in this execution context.
- No APK installation, package replacement, HOME/default-launcher change, ADB setting change, permission change, data wipe or other TV mutation was performed.
- PR #61 is CLOSED / UNMERGED; `main` remains outside this operation. Integration ref is unchanged.
- The artifact expires 10 November 2026; preserve/retrieve the canonical artifact before expiry if physical testing is scheduled later.

## 4. G03 execution matrix — pending physical evidence

| Area | Required evidence | Status |
|---|---|---|
| Device identity | model, Android/build fingerprint, API, resolution, density, free storage | BLOCKED — device not connected |
| Install/upgrade | package identity, signature compatibility, install result, rollback path | BLOCKED — explicit readiness approval required |
| Launcher behavior | cold/warm start, Home return, app switching, repeated Home/Back | NOT RUN |
| D-pad/focus | directional navigation, focus retention, dialogs, settings restoration | NOT RUN |
| Settings/security | permissions, app lock, kids mode, device settings, power/restart | NOT RUN |
| QR/LAN | same-Wi-Fi pairing, upload, retry/cancel/expiry, concurrency, reconnect, low storage, cleanup | NOT RUN |
| Media/persistence | avatar/wallpaper/shuffle, corrupt/large image, 1080p/4K, force-stop/process-death/reboot | NOT RUN |
| Runtime/performance | StandBy/DreamService, weather/reduced motion, crash/ANR/logcat, FPS/frame time, CPU/PSS/RSS/heap, thermal and soak | NOT RUN |
| Recovery/data integrity | verify app data and state after each failure scenario | NOT RUN |

No row above is DEVICE-PASS. Host CI and artifact integrity are not substitutes for physical evidence.

## 5. Decision and blocking condition

The G03 preflight verifies the candidate artifact and confirms it is retrievable, but cannot complete the physical acceptance stage from this environment. Project governance requires explicit user readiness approval before installation or any TV/package/ADB mutation. The user's instruction to continue technical work does not waive this explicit safety gate.

**Required next action:** obtain explicit authorization to install the identified G02 candidate on the Mi TV Stick and perform the documented non-destructive acceptance plan. Until then, keep the device untouched and G03 OPEN/BLOCKED. After approval, capture the device baseline first, preserve rollback path, run tests in risk order, collect logs/screenshots/measurements, repair source defects on owning branches, and rerun exact-head CI before retesting.

## 6. Quality check

- [x] Live integration SHA revalidated.
- [x] G02 workflow conclusion and both job conclusions revalidated.
- [x] Artifact ID, expiry, download digest and extracted APK SHA revalidated.
- [x] APK archive integrity and required structural entries verified.
- [x] No device mutation occurred.
- [ ] APK signing independently verified.
- [ ] Physical device connected and baseline captured.
- [ ] Installation/upgrade and rollback verified.
- [ ] Full G03 matrix executed with evidence.
- [ ] Defects repaired and retested.
- [ ] Final G03 acceptance approved.

**Final status: BELUM FINAL — G03 PREFLIGHT VERIFIED; PHYSICAL DEVICE ACCEPTANCE NOT RUN.**
