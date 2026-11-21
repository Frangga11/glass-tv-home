# Glass edge reflection checkpoint — 22 November 2026

## Workstream
- Goal: adopt the restrained, thin glass-edge reflection visible on the Universal Home Dock and Settings panel in the owner's 22 November reference video.
- Repository: `Frangga11/glass-tv-home`.
- Base branch: `main`.
- Exact base SHA: `71f3a051a25fd66cabfd1740756cc36442dc92cc`.
- Work branch: `feat/glass-edge-reflection-001`.
- Draft PR: [#161 — Add restrained top-edge glass reflection to Dock and Quick Settings](https://github.com/Frangga11/glass-tv-home/pull/161).
- Current source head at checkpoint preparation: `7339b89c16fe1544a9ad738cd15c75cdc329f9b6`.

## Baseline audit
- Main HEAD `71f3a051a25fd66cabfd1740756cc36442dc92cc` was verified as the current main tip. Compared with code integration commit `d17db23f2fdac3641dee860033e88aad1b8190de`, the two intervening commits only add this main checkpoint and synchronize `HANDOVER.md`; no application source changed.
- Read `GTVH-MAIN-INTEGRATION-CHECKPOINT-2026-11-22.md`, `HANDOVER.md`, and `GTVH-QUICK-SETTINGS-DEVICE-ACCEPTANCE-CHECKPOINT-2026-11-22.md`.
- Existing Universal Glass material from PR #160 is already included in main through merged PR #156.
- Existing shared material: `Backdrop.Fill`, `GLASS_VEIL_COLOR`, and `GlassRim`.
- Existing `GlassRim.drawWindowLight()` paints a rounded perimeter glint; it does not expose a separate, independently controlled straight upper-edge reflection.
- `DockBreathingBorderView` is a separate animated cyan card-rim treatment; it is not the Dock tray reflection and remains unchanged.
- Preserve accepted Quick Settings geometry and behavior, Universal Glass, and Horizontal Carousel / Glider motion. PR #62 remains OPEN / DRAFT / UNMERGED / DEFERRED and untouched.

## Implemented scope
- `GlassRim.java`: added a very thin upper-edge specular line with a softer under-glow and fading horizontal gradient.
- `DockDecoration.java`: enabled the upper-edge reflection on the Dock tray.
- `GlassDrawable.java`: added an explicit opt-in flag; existing constructors default to false.
- `QuickSettingsDialog.java`: enabled the opt-in for Quick Settings only.
- `WifiSettingsDialog.java` and other popup callers keep the default material and do not opt in.
- `tests/test_popup_family_glass_material_contract.py`: added regression coverage for the drawing primitive and its scoped call sites.
- `tests/test_global_frosted_silver_glass_contract.py`: updated the fallback-constructor assertion to include the new explicit default-false argument.
- No geometry, typography, focus, DPAD, Glider, carousel, wallpaper blur, networking, popup actions, navigation, or breathing animation changes.

## Commit sequence
- `72423865482177b17bb969541b1cdd4fb23ca2fc` — add restrained top-edge specular reflection.
- `26fda5ce87e08f8a26e2406075c6c8a6c5f51be0` — apply to Dock tray.
- `0b64a7c6ca064c7d63c3d89073dae0c4478da2c1` — make popup reflection opt-in.
- `50663c61e1d38b48ead24915f844a60015fdf730` — enable for Quick Settings.
- `c485027dabfbf54efd0dce175f697089b2601701` — add regression contract.
- `7339b89c16fe1544a9ad738cd15c75cdc329f9b6` — update existing fallback constructor contract after CI exposed the expected signature change.

## Verification status
- First PR CI run #2511 / `44959150326` on `c485027dabfbf54efd0dce175f697089b2601701`: Lists/tools/docs failed one pre-existing assertion that expected the old constructor call shape; 385 tests ran, one failed and one was skipped. Android build job was still in progress at the last check. This was a test-contract mismatch, not a runtime/build failure.
- Corrected the assertion without weakening the fallback guarantee.
- Second PR CI run #2512 / `44959229044` on exact source head `7339b89c16fe1544a9ad738cd15c75cdc329f9b6`: Lists, tools and docs — SUCCESS; Launcher build and unit tests — IN PROGRESS at checkpoint preparation.
- APK release publisher run #877 was SKIPPED; no release APK is claimed.
- No local Gradle run was possible because the working environment could not resolve `github.com`. Exact-head GitHub Actions is the authoritative build gate.
- Physical Mi TV Stick visual comparison for this new reflection is NOT YET TESTED. Existing earlier Quick Settings acceptance remains preserved but does not count as acceptance of this new visual delta.

## Acceptance and merge boundary
- PR #161 remains OPEN / DRAFT / UNMERGED.
- Do not merge without explicit owner approval.
- Do not install or publish an APK as a release.
- Do not alter PR #62.
- The new reflection is source-implemented but not yet visually accepted on the target Mi TV Stick.

## Next actions
1. Verify completion of CI run #2512 on exact head `7339b89c16fe1544a9ad738cd15c75cdc329f9b6`.
2. If the launcher build/unit tests fail, inspect exact logs, fix on this branch, and rerun exact-head CI.
3. Once automated checks pass, build/use a QA candidate and ask the owner to inspect the Dock and Quick Settings reflection on the target Mi TV Stick at 1920×1080 / 320dpi.
4. Compare against the reference video for thinness, fade, corner continuity, and restraint on both bright and dark wallpaper.
5. Only after visual acceptance, update this checkpoint and `HANDOVER.md` with exact final-head evidence. Keep PR #161 unmerged pending explicit approval.
