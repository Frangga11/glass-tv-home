# E06 — Home Screen Audit & Finalization

**Status:** Source implementation, audit, and exact-head CI finalized; physical acceptance pending Project F.

## Scope
The active Home Screen Settings destination only. It contains four retained controls: Daily Prayer & Prayer Times Panel, Daily Verses & Prayers, Interface Sound Effects, and Azan Settings. This is one destination with four rows, not a nested subtab navigator.

## Findings and repairs
1. Home Screen labels and descriptions now follow the selected language: Bahasa Indonesia or English. Malay-specific fallback text was removed from all eight Home Screen label/summary methods; Indonesian is the fallback.
2. The language selector in MainActivity already presents only Bahasa Indonesia and English. AppLanguage.normalizeLanguage() maps unsupported/legacy language codes to Indonesian, so a previously saved Malay preference migrates to Indonesian rather than showing a Malay option.
3. Switch-row typography, surface colors, focus border, corner radius, focus scale, and spacing now use the shared TvUiTokens.
4. Existing four controls and their callbacks/preferences remain unchanged.
5. Additional feature ideas (Home Screen preview, panel ordering, transparency, information density, and restore defaults) are deferred as future candidates; none was added to current E06 scope.

## Quality check
- Source-level callback and label references checked.
- No feature logic or persisted state changed.
- Exact-head CI passed on commit `ecb58cc53d328f0b542055f7941c0fc39ae48579`.
- CI workflow run: `41438751146` (run #667), conclusion: success.
- No APK installation or physical TV test was performed. Project F retains device acceptance.

## Git checkpoint
- Branch: `feature/e06-home-screen-subtabs`
- PR: #39 (Draft, open, unmerged)
- Base: `feature/e05-information-center-ayah`
- Verified source head: `ecb58cc53d328f0b542055f7941c0fc39ae48579`

## Decision
E06 source work is finalized. Keep PR draft/unmerged under D-013. Physical acceptance remains Project F. Future feature candidates remain deferred until a later scope is approved.
