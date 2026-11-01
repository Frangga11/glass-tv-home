# APK Size Audit — Glass TV Home

**Date:** 1 November 2026  
**Audited source:** `feature/b07-menu-focus-restoration`  
**Feature source head at audit:** `d4eabc48e020ab6cbef54e0f8884cf125eff97ed`  
**Build evidence:** CI run #298 / `41281838510`, exact-head SUCCESS

## 1. Correct interpretation of the size gate

The CI log reports **1509 KB**, where the workflow prints integer KiB using `size / 1024`.

- Approximate exact byte size from the reported integer: 1,545,216 bytes (the log is rounded down to whole KiB; exact byte count was not printed in run #298).
- Current CI ceiling: 1,572,864 bytes = 1.5 MiB = 1,536 KiB.
- Therefore the tested APK **did not exceed the current CI gate**. It is approximately 27 KiB below the gate.
- In decimal units, the reported 1509 KiB is approximately 1.545 MB, which is about 45 KB above a strict 1.5 MB decimal ceiling.

Do not describe the CI result as an APK-size failure. The issue is that the build has little headroom and the unit convention needs to remain explicit.

## 2. Build configuration already applied

`launcher/app/build.gradle` release build already enables:
- `minifyEnabled true` (R8)
- `shrinkResources true`
- Android optimized default ProGuard configuration

No evidence supports adding more aggressive code shrinking or removing features as the first response.

The CI gate in `.github/workflows/ci.yml` checks `size < 1572864`, i.e. 1.5 MiB, not 1.5 MB decimal.

## 3. Large source assets identified

Source inventory on the B07 branch:

| File | Source size | Notes |
|---|---:|---|
| `assets/kabut-pagi-dempo.jpg` | 230,883 bytes (~225.5 KiB) | Default wallpaper; CI workflow comment explicitly identifies it as an intentional ~226 KiB contribution. |
| `res/font/inter.ttf` | 417,300 bytes | Inter font; explicit `R.font.inter` references exist in MainActivity and ModernSettingsDialog. |
| `res/font/outfit.ttf` | 417,300 bytes | Git blob SHA is identical to `inter.ttf`; exact duplicate bytes in source tree. |
| `res/font/amiri_quran.ttf` | 136,920 bytes | Present in source; package inclusion/use not yet confirmed. |
| `res/drawable-nodpi/qr_coffee.png` | 78,982 bytes | QR artwork. |
| Avatar PNGs | ~20–28 KiB each | Profile avatar choices; retained functionality/visuals must not be removed casually. |
| Audio assets | Several files, ~1–62 KiB each | Interface sound options; preserve behavior and sound quality. |

Important: source file size is not the same as compressed APK contribution. Resource shrinking may already remove unreferenced font/resources. The two font files having the same Git blob SHA proves byte-for-byte duplication in the repository, but does **not** prove both are packaged in the release APK.

The font references inspected use `R.font.inter`; a full resource-usage/package report is still needed before removing or consolidating any font resource.

## 4. Likely size contributors

1. The default wallpaper is the only large asset explicitly called out in the existing CI comment as contributing approximately 226 KiB to the APK.
2. Fonts and visual assets are the next source-level candidates, but their final compressed/package contribution has not been measured.
3. Java source file byte counts are not a direct measure of APK contribution because release R8 minification and optimization are already enabled.

## 5. Safe next diagnostic

A read-only APK ZIP inventory step has been added to the CI workflow on the B07 PR branch. On the next CI execution it will print:
- exact APK byte size;
- largest packaged entries;
- each entry's uncompressed and compressed sizes;
- total compressed and uncompressed payload.

This diagnostic does not modify APK contents or application behavior. At the time of this audit, no new workflow run had yet been observed for that diagnostics-only commit, so its output is pending.

## 6. No destructive optimization authorized/performed

No fonts, wallpaper, avatars, QR artwork, audio, locales, code, or application functions have been removed or altered for size reduction.

Potential safe optimization order after the ZIP report:
1. Confirm whether duplicate `outfit.ttf` and `amiri_quran.ttf` are actually packaged and referenced.
2. If a duplicate font is packaged and unused, remove only that redundant packaged resource after a reference audit and visual/font regression test.
3. If the default wallpaper is the main payload, test lossless metadata stripping or visually validated JPEG re-encoding at the same dimensions; compare on a TV-sized display before accepting.
4. Rebuild and compare all features, visuals, fonts, sounds, QR/upload flows, and startup behavior.

**Decision:** No feature-reducing optimization. The current CI build passes its actual 1.5 MiB gate. A stricter 1.5 MB decimal target is a separate requirement and should only be adopted with a verified package breakdown and safe savings.
