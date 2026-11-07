# Patch 4.1 Dock & Liquid Glass Adoption — 7 November 2026

## Device-test finding

The user's TV photo showed ten application tiles in two Grid rows, but no visible Dock tray or Dock focus treatment. The previous V2 source-host checkpoint was therefore not accepted as visually correct.

## Root cause confirmed by source comparison

Patch 4.1 uses a first-row Dock contract: configured favourites lead the ordered app inventory, and remaining Dock slots are filled from that same inventory up to the configured column count. The V2 split model populated the separate Dock adapter only from configured/user-pinned component keys. If that list was empty or shorter than capacity, the Dock could collapse to zero or too few items, leaving the entire app inventory in the Grid. This is consistent with the reported two-row/no-tray device symptom.

## Adopted Patch 4.1 components

- First-row Dock fill semantics restored through DockLayoutModel.fillDockSlots; configured favourites and user pins keep precedence; explicit unpins are respected; remaining slots are filled from the ordered inventory; overflow stays in Grid.
- Exact Patch 4.1 GlassRim adopted, including thin-film cyan/violet/gold gradient and Window Light renderer.
- Exact Patch 4.1 GlassView adopted, including hardware-layer focus rim and Window Light.
- DockBreathingBorderView adopted and inserted into item_app.xml; wired only to Dock holders so Grid tiles do not receive the Dock breathing effect.
- Window Light reflection added to GlassDrawable, DockDecoration and Glider.
- Existing Dock geometry and separate Dock/Grid surfaces remain; 4/5/6 capacity, orientation and G3A architecture are preserved.

## Source and PR

- Owner branch: fix/home-dock-visual-parity-v2.
- Exact HEAD: 239390db27fafcc39b2fefa68ddfa9f0ba30608f.
- PR #74: OPEN / DRAFT / UNMERGED; base feature/g4-parent-pin-hardening @ 7146de58953325fdfc6c98501828676f143e64cf.
- PR title: Home Dock: adopt Patch 4.1 layout, glass and breathing focus.

## Exact-head validation

- CI #1415 / run ID 42379388302, exact HEAD 239390db27fafcc39b2fefa68ddfa9f0ba30608f: both required jobs SUCCESS.
- Launcher app and installer release unit tests and release builds passed; repository lists/tools/docs checks passed.
- Release APK size: 1,624,741 bytes.
- Artifact ID: 13576403262; ZIP SHA-256: 21a13410b1c6b5784540fd0e4f0acacf341ac63995173960e43b2fb53dae36ce.
- Extracted APK SHA-256: 23ffb28d48b05f975be4b5bd13aa1086c08e47eef1feb5b8e13916799f32fafc.
- Artifact download: https://github.com/Frangga11/glass-tv-home/actions/runs/42379388302/artifacts/13576403262
- Artifact retention expires 14 November 2026.

## Acceptance boundary

SOURCE-HOST QC PASS / READY FOR CONTROLLED TV ACCEPTANCE. The previous device test was FAIL and the new build has not yet been tested physically. Do not claim visual PASS until a new TV photo confirms Dock placement, tray, icon rim and remote behavior. No APK install, ADB, device setting mutation, merge, rebase, cherry-pick or force-push was performed.

## Next device checks

1. Confirm five Dock tiles appear even when configured favourites are fewer than five.
2. Confirm the Dock is bottom-anchored and the App Grid occupies only the region above it.
3. Confirm the blurred wallpaper tray, veil, iridescent rim, top Window Light and drop shadow are visible.
4. Focus each Dock icon: breathing cyan rim appears on Dock cards; no breathing rim appears on Grid cards.
5. Verify capacities 4/5/6, horizontal/vertical orientation and DPAD up/down transitions.
6. Confirm cold-start and return-from-app retain the same Dock placement.
