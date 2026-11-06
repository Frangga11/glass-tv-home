# GTVH-UIR-002 v2.0 — G3B Weather & AQI V4 Audit
Date: 7 November 2026

## Status
**SOURCE/HOST QC PASS.** Physical Android TV, remote, OEM overscan, daylight/glare and large-font visual acceptance remain DEFERRED / NOT RUN.

## 1. Trigger and input verification
- Project continued from G3A; no new project or redesign was started.
- Canonical documentation branch at audit start: docs/project-master-inventory @ 6df9f773bc225c1f64e79cc425c48bf36b3d7ef2.
- G2: feature/g2-shared-ui-foundation-repair @ 40fa0715bdbcd324042927fbc56ec19f91d1947a, PR #65 OPEN/DRAFT/UNMERGED.
- G3A: feature/g3a-home-independent-dock @ 87302f52de844954392bac5438cbe82ec5d14a30, PR #66 OPEN/DRAFT/UNMERGED.
- Integration: integration/b-series @ 562cde0751de93748eb4e0d4cdece4fa1672ac4d; read-only during this work.
- G3B owner branch: feature/g3b-weather-aqi-v4; PR #67 OPEN/DRAFT/UNMERGED, base G3A. No branch merge or rebase was performed.

## 2. Source audit
Reviewed WeatherActivity, WeatherAirQuality, AppLocationManager, activity_weather.xml, Weather V4 custom views, string resources, AndroidManifest network permissions, HomeWeather summary path, G2 shared UI styles and G1 V6 prototype.

### Provider and data contract
- Forecast source: Open-Meteo Forecast API. Requested current/hourly/daily weather fields cover temperature, apparent temperature, humidity, wind speed/direction/gust, pressure, dew point, visibility, weather code/day-night, precipitation, precipitation probability, daily min/max, rainfall total, sunrise/sunset and UV maximum.
- Air-quality source: Open-Meteo Air Quality API with CAMS model values and US AQI. Requested fields: PM2.5, PM10, carbon monoxide, nitrogen dioxide, sulphur dioxide and US AQI.
- US AQI is a model estimate, not an official JAS/APIMS IPU or Indonesian ISPU ground-station measurement. UI/source wording now makes this distinction explicit.
- CO is converted from provider µg/m³ to mg/m³ to match the G1 V6 visual reference. PM2.5, PM10, NO₂ and SO₂ remain µg/m³.
- Missing/invalid provider values remain unavailable (em dash plus note); they are never substituted with zero.
- Location selection is coordinate-based through AppLocationManager. The selector's legacy “Location API Haze” label is not evidence of a ground-station feed.

### Cache, lifecycle and state
- Cache freshness is checked against payload existence, success age and the currently selected coordinates.
- Legacy JAS/APIMS cache is not accepted as a fresh CAMS/US AQI payload and cannot suppress a refresh. Legacy data is ignored until a successful CAMS response replaces it; source metadata is updated only on successful AQI response.
- Weather and AQI retain separate success timestamps and refresh-failure state.
- Dashboard exposes per-source current/stale/offline/unavailable/refresh-failed status, source time/date/timezone and model reference location.
- Activity refreshes on resume; asynchronous callback is guarded against finishing/destroyed Activity.
- D-pad focus is restored to the same hourly/daily item where possible, with first-row fallback if the item disappears.

### UI and visual authority
- G1 V6 remains the visual authority; no direction change was made.
- G2 shared card/material/focus resources are reused; no duplicate shared foundation was introduced.
- Weather V4 retains the complete agreed information scope: current/feels-like/min/max temperature, humidity, wind, pressure, dew point, visibility, sunrise/sunset, moon phase/rise/set, rainfall/probability, UV category/advice, US AQI and pollutant breakdown.
- Pollutants are separate readable metrics, not compressed into a single long line.
- Indonesian and English resource parity is maintained; these are the app's supported locales.

## 3. Findings and disposition
| ID | Severity | Finding | Disposition |
|---|---|---|---|
| G3B-H01 | High | Cache freshness could suppress refresh after location change | FIXED; coordinate-scoped helper and regression test |
| G3B-H02 | High | Legacy JAS/APIMS cache could suppress CAMS refresh | FIXED; source-aware freshness guard and legacy cache rejection |
| G3B-H03 | High | Weather V4 metrics absent from request/model | FIXED; request and FullWeather contract expanded |
| G3B-M01 | Medium | Per-source stale/offline/unavailable/error state missing | FIXED |
| G3B-M02 | Medium | Refresh could lose D-pad focus or update destroyed Activity | FIXED |
| G3B-M03 | Medium | Pollutants compressed and missing values ambiguous | FIXED; separate grid and missing note |
| G3B-M04 | Medium | HomeWeather summary uses a separate data/cache owner from WeatherActivity | OPEN RISK; no proven defect in this audit; integration consistency check retained |
| G3B-M05 | Low | Connectivity changes while dashboard remains foregrounded are not observed by a live NetworkCallback | ACCEPTED LIMITATION; status refreshes on resume/data refresh |
| G3B-L01 | Low | Settings label “Location API Haze” is legacy terminology for coordinate selector | DEFERRED to G3F Global Settings |
| G3B-A01 | Acceptance | Physical TV readability, remote D-pad, OEM overscan, daylight/glare and large-font scaling | DEFERRED / NOT RUN |

**Critical findings: none.** No speculative production changes were made for unproven risks.

## 4. Regression and quality verification
- WeatherAirQualityFreshnessTest: cache age, selected-location mismatch, legacy JAS source guard, invalid/missing cache and pollutant values.
- WeatherV4LayoutContractTest: Weather V4 layout and all five pollutant value IDs.
- WeatherActivityFocusRestoreTest: restore same hourly slot and fallback when saved slot disappears.
- tests/test_weather_cache_location_contract.py: coordinate-scoped freshness, source guard and request field contract.
- XML structural audit: one root, balanced tags, no duplicate IDs, all Activity view IDs and string resources resolved.
- Exact CI run 42274182438 SUCCESS on functional commit 8f2162036991ded803c5ca0981ad1f3e777cfb54; both required jobs SUCCESS. Release build, release unit tests, lint-vital and repository contract tests PASS.
- Current branch HEAD 8cfc5c886950cc9d27d4530ba901df872b8e5c44 differs from the CI-tested commit only by one documentation comment in the Python contract test.
- APK diagnostic: 1,600,893 bytes, +11,068 bytes (approximately 0.7%) versus G3A. Informational only under D-023; no build failure and no feature/assets removed.

## 5. Governance and remaining gates
- PR #65, #66 and #67 remain OPEN/DRAFT/UNMERGED.
- G2, G3A and integration branch heads were not modified.
- No merge, rebase, cherry-pick, force-push, APK publication/installation, ADB/device mutation or physical test occurred.
- G3B disposition: **COMPLETE / SOURCE-HOST QC PASS**. Physical device acceptance remains separately OPEN/BLOCKED.
- Next UI Refresh package: G3C Profile, Avatar & Child Mode.
