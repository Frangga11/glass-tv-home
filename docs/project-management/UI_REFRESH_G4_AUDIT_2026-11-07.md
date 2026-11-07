# UI Refresh G4 — Security & Combined-Tree Audit

**Date:** 7 November 2026  
**Disposition:** PASS — SOURCE/HOST SCOPE  
**Work branch:** `feature/g4-parent-pin-hardening`  
**Owner PR:** #72 — OPEN / DRAFT / UNMERGED

## 1. Objective and baseline

G4 reconciles G3A–G3F as one product tree and closes the parent-PIN plaintext-storage risk without changing the approved PIN experience. G3F baseline is exact HEAD `455b619c3eefe417ac0152a51f3cb12478a8f4a9`. The G3 PR chain #66–#71 remains linear, unchanged, OPEN/DRAFT/UNMERGED.

## 2. Security finding

**G4-SEC-001 — plaintext parent PIN at rest.** The previous `ChildModeManager` stored the parent PIN directly under `security_pin` in private SharedPreferences. This exposed custom PIN values to anyone able to inspect app-private preferences.

## 3. Implemented correction

- New `PinCredentialStore` stores custom PIN credentials as PBKDF2-HMAC-SHA256 with a per-credential 16-byte random salt, 120,000 iterations and 256-bit output.
- Hash comparison uses `MessageDigest.isEqual`.
- Existing legacy custom PINs are migrated only after successful verification; the value is preserved and plaintext is removed when the hashed write succeeds.
- Fresh-install default remains `0000`; no forced first-use change. Legacy default `0000` is preserved.
- Explicitly saving `0000` retains the default-status presentation while persisting the credential hash.
- Missing, partial, malformed, mistyped or previously stored-but-lost credential data fails closed. A corrupted stored credential never silently falls back to `0000`.
- PIN derivation and verification run on a dedicated background executor; UI callbacks return to the main looper.
- Failed attempts remain stateless. No attempt counter, retry limit, cooldown, lockout, forced delay or temporary restriction was introduced. Once an incorrect result is displayed, the user can retry immediately.
- Parent PIN remains device-wide. Child Mode, App Lock and Hidden Apps remain profile-scoped.

## 4. Regression coverage

`PinCredentialStoreTest` contains 11 focused tests covering fresh default, legacy custom/default migration, explicit default save, persisted salted hash, salt rotation, repeated wrong attempts followed by correct PIN, failed legacy verification without migration, malformed hash, invalid PIN values, wrong SharedPreferences value types, and missing hash/salt without default fallback. `ProfileManagerPersistenceTest` verifies that legacy profile policies migrate while the parent PIN remains global.

## 5. Combined-tree and CI evidence

- G3F base: `455b619c3eefe417ac0152a51f3cb12478a8f4a9`.
- G4 branch HEAD: `f46adf27bbc6b2156c7e9bb49d48cf4fd0fcdb5e`.
- PR synthetic merge candidate: `376b5b69e2aed56e1f813169f35dbb9459ada554`; parents are exactly the G3F base and G4 branch HEAD; tree `095e11e6cab067b1a27951382590bcc8008d0897`.
- Exact-head GitHub Actions run #1394 / `42353368760`: both required jobs SUCCESS. Checkout log confirms CI built `refs/remotes/pull/72/merge` at the synthetic merge SHA above.
- Android app and installer release unit-test tasks, release builds and lint-vital passed. Repository Python tests: 134 passed, 1 intentional skip.
- Release APK diagnostic: **1,622,401 bytes (1,584 KB)**, +2,208 bytes against G3F's 1,620,193-byte diagnostic. Informational only under D-023; no artifact was published.

## 6. Final source review

The combined diff against G3F is limited to five files:
1. `ChildModeManager.java`
2. `ModernSettingsDialog.java`
3. `PinCredentialStore.java` (new)
4. `PinCredentialStoreTest.java` (new)
5. `ProfileManagerPersistenceTest.java`

No G3 source branch, integration branch, main branch or documentation file is included in PR #72. PR #66–#72 are all OPEN/DRAFT/UNMERGED; the G3 dependency chain and exact heads remain unchanged. PR #72 is mergeable and remains intentionally draft.

## 7. Residual risks and scope boundaries

A four-digit PIN with unlimited attempts remains guessable; salted hashing improves protection against plaintext exposure but does not make the PIN brute-force-proof. This limitation is explicit and follows the user's no-attempt-limit requirement.

This is a **source-host checkpoint only**. Physical Android TV/Mi TV Stick, remote/D-pad/Back, OEM Settings handoff, overscan, 1080p/4K, daylight/glare and large-text acceptance remain a separate gate and were not run. No APK publication/installation, ADB, device configuration, merge, rebase, cherry-pick or force-push occurred.
