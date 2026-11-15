# GTVH-HOME-AYAH-PILL-FOCUS-ALIGN-001

## Scope
Only the Home Ayat Pilihan preview interaction, translation alignment, and the gap between the Baca Ayat Lengkap capsule and the Waktu Sholat heading.

## Changes
- Make the Ayat preview container display-only and non-focusable/non-clickable.
- Move DPAD focus and activation to the Baca Ayat Lengkap capsule (ayahOpenHint). OK/Enter opens the existing complete-verse glass reader.
- Keep the navigation path from the microphone/header and from the prayer timetable, but route it to the capsule rather than the preview card.
- Apply the focus indicator to the capsule only; the full Ayat card no longer receives a focus halo.
- Right-align the Indonesian translation preview.
- Reduce the divider top margin from 6dp to 1dp. With the existing divider height, bottom margin, parent bottom padding/margin and prayer heading translation, this changes the visual capsule-to-heading gap from approximately 15dp to 10dp. No other prayer layout dimensions are changed.

## Explicitly untouched
Prayer timetable content and popup, Azan playback, global glass implementation, Home wallpaper, top navigation, Dock, app grid, and other Home layout elements.

## Validation
A source-contract test verifies focus ownership, capsule activation, right-aligned translation, and the 10dp spacing calculation. Build and CI validation are pending.
