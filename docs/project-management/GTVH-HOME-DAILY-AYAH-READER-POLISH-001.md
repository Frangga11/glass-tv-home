# GTVH-HOME-DAILY-AYAH-READER-POLISH-001

## Objective
Polish the existing centered complete-verse glass reader using the approved visual review while preserving the existing Ayah data flow, launch entry points, and close/focus restoration behavior.

## Implemented
- Increased panel inner spacing and strengthened the navy glass gradient and edge stroke for readable content over a busy Home wallpaper.
- Increased the Arabic reading size from 28sp to 33sp on 1080p-class displays (38sp on 4K-class displays).
- Increased Indonesian translation to 20sp and expanded separation from the Arabic verse.
- Moved the scroll hint out of the scrolling verse body into a fixed footer position, so long verses do not push the hint away.
- Increased dialog height from 63% to 68% of display height while preserving the centered horizontal form and 72% width.
- Increased background dim amount from 0.30 to 0.48 to reduce competing Home text behind the reader.
- Preserved the existing DPAD scroll model, close controls, back handling, and restoration of focus to the originating Home control.

## Explicitly preserved
- Ayah content source, translation language selection, and reference text.
- Home preview and its capsule activation behavior.
- Prayer timetable, prayer popup, Azan playback, wallpaper, top navigation, Dock, app grid, and global glass implementation.
- Existing reader's two close affordances (top-right X and bottom Close).

## Validation
- Added source-contract tests for glass contrast, reading typography, fixed footer placement, and DPAD scroll/focus routing.
- Android unit tests and release build validation are required on this commit.
- Physical Mi TV Stick visual and remote-control verification remains a separate device QA gate; no emulator/CI result is represented as physical-device verification.

## Status
Implementation committed on `fix/home-ayah-pill-focus-align-001`; PR remains draft/unmerged pending CI and device QA.
