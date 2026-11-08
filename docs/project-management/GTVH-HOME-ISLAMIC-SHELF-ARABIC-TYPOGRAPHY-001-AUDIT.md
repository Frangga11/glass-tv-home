# GTVH-HOME-ISLAMIC-SHELF-ARABIC-TYPOGRAPHY-001 — Audit & Implementation

**Date:** 9 November 2026  
**Status:** SOURCE CHANGE COMMITTED / CI NOT YET VERIFIED / PHYSICAL VISUAL ACCEPTANCE PENDING / NOT FINAL

## Objective
Refine the Arabic verse text in the Home Islamic Shelf so it reads with a warm yellow-orange tone, tighter two-line rhythm, and subtle dimensional shadow.

## Source baseline and delivery
- Repository: `Frangga11/glass-tv-home`
- PR: [#85](https://github.com/Frangga11/glass-tv-home/pull/85) — OPEN / DRAFT / UNMERGED
- Branch: `fix/home-islamic-shelf-arabic-typography-001`
- Base: PR #84 branch `fix/home-islamic-shelf-single-anchor-004`
- Base HEAD: `d821842c2211cb6e65bbde506253cde7b3929771`
- Current branch HEAD at implementation: `a5eaae843f8998c53238870609102e4c20b05ded`

## Implementation
Only `launcher/app/src/main/res/layout/activity_main.xml`, TextView `ayahArabic` was restyled:
- Text color: `#FFFFC45A` (warm yellow-orange).
- Font: retained `@font/amiri_quran`, 20sp.
- Two-line spacing: `lineSpacingExtra=-1dp`, `lineSpacingMultiplier=0.94`.
- Arabic font padding remains enabled to protect glyph/harakat extents.
- Maximum remains two lines; ellipsize behavior unchanged.
- Soft warm shadow: `#CC3A1800`, dx 0, dy 2, radius 3.
- Translation `ayahText`, labels, prayer table, shelf geometry, focus navigation, and Dock layout were not changed.

## Regression coverage
Added `tests/test_home_islamic_shelf_arabic_typography_contract.py` to verify:
1. Exact warm yellow-orange text color.
2. Tight but non-collapsed line spacing and retained font padding/two-line cap.
3. Soft shadow parameters.
4. Styling isolation from the translation text.

## Quality and acceptance status
- Source diff is intentionally limited to the Arabic TextView and its regression contract.
- Python/Android CI for PR #85: not yet verified at checkpoint time.
- Physical Mi TV Stick screenshot/visual acceptance: pending. Inspect both one-line and two-line Arabic verses, especially diacritics at line boundaries, against bright and dark wallpapers.
- Do not merge PR #85 until exact-head CI passes and physical visual acceptance confirms that glyphs/harakat are not clipped and the shadow remains subtle.
