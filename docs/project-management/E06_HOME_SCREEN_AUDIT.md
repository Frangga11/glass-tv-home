# E06 — Home Screen Audit & Finalization

**Status:** Source implementation and audit finalized; exact-head CI pending after language cleanup; physical acceptance pending Project F.

## Scope
The active Home Screen Settings destination only. It contains four retained controls: Daily Prayer & Prayer Times Panel, Daily Verses & Prayers, Interface Sound Effects, and Azan Settings. This is one destination with four rows, not a nested subtab navigator.

## Findings and repairs
1. Home Screen labels and descriptions now follow the selected language: Bahasa Indonesia or English. Malay-specific fallback text was removed from all eight Home Screen label/summary methods; Indonesian is the fallback.
2. The language selector in MainActivity already presents only Bahasa Indonesia and English. AppLanguage.normalizeLanguage() maps unsupported/legacy language codes to Indonesian, so a previously saved Malay preference migrates to Indonesian rather than showing a Malay option.
3. Switch-row typography, surface colors, focus border, corner radius, focus scale, and spacing now use the shared TvUiTokens.
4. Existing four controls and their callbacks/preferences remain unchanged.

## Quality check
- Source-level callback and label references checked.
- No feature logic or persisted state changed.
- Exact-head CI at prior source head `a07fe2fc69842bc75efa4bb77c2f3074f2a6c2c8` passed both required jobs; language cleanup added a new commit and therefore needs fresh CI.
- No APK installation or physical TV test was performed. Project F retains device acceptance.

## Git checkpoint
- Branch: `feature/e06-home-screen-subtabs`
- PR: #39 (Draft, open, unmerged)
- Base: `feature/e05-information-center-ayah`
- Latest language cleanup commit: recorded in PR branch; verify exact SHA and CI before closing E06.

## Decision
E06 source work is finalized subject to fresh exact-head CI. Keep PR draft/unmerged under D-013. Physical acceptance remains Project F.
