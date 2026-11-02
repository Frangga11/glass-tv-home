# Cross-Project Audit — Weather, Air Quality & Popup Ownership

**Date:** 2 November 2026  
**Repository:** Frangga11/glass-tv-home  
**Canonical documentation branch:** docs/project-master-inventory  
**Related feature:** E05 PR #38, OPEN / DRAFT / UNMERGED  
**Purpose:** Trace weather/AQI and popup defects to the correct A–G owner. This is an ownership and verification handover, not a claim that source repairs are complete.

## 1. Verified baseline

- E05 PR #38 head: `d1d461c2c08609462354ebebd9dc7ce678e2e049`; base E04 `08076bb945a169de198905152a092f419f48aa82`.
- PR #38 changed files are limited to E05 audit report, `MainActivity.java`, `activity_main.xml`, and default/ID/EN strings. It does **not** change `WeatherAirQuality.java`, `WeatherActivity.java`, or `HomeWeather.java`. Thus the recent E05 ayah reader CI pass is not evidence that weather/AQI repairs have been made.
- B-series PR #10–#12, #16–#17 remain Draft/open/unmerged; C-series #18–#24 and D-series #25–#31 remain Draft/open/unmerged under D-013. D07 final head is `964163c918e281b68af5cf7114ca56ead2797486`; E01–E04 PRs #34–#37 form the downstream dependency chain.
- Canonical master records B01 as owner of global visual/modal tokens, B02/B03 as focus/dialog behavior owners, D07 as stale/concurrent state owner, E feature owners as consumers, F as integrated/device verification and G as cross-project closure.
- D-014 explicitly requires cross-project findings to be traced to root cause and repaired by the owning component; project boundaries are not barriers to audit or coordination.

## 2. Confirmed source-level findings

### WQ-01 — Synthetic pollutant concentrations
`WeatherAirQuality.getFullWeather()` derives `pm25 = aqi * 0.45` and `pm10 = aqi * 0.55`. These are not API observations and must never be presented as measured/modelled concentrations. Parse the actual `pm2_5` and `pm10` values returned by the Air Quality API; if absent, render unavailable, not a calculated substitute.

### WQ-02 — Synthetic weather/current/forecast fallback
`WeatherAirQuality.syncInternal()` initializes current conditions with example values (e.g. 29°C, weather code 2, pressure 1013); `getFullWeather()` supplies additional defaults and hard-coded hourly/daily sample rows when response data are missing. This can turn a failed request into apparently live conditions. Missing variables and missing forecast arrays must be represented as unavailable; cached last-known data must be visibly marked stale and retain its actual source time.

### WQ-03 — Incorrect update time and success state
The displayed update label is formatted from `new Date()` at render time, not the API's `current.time`. The sync path writes `KEY_LAST_UPDATE=now` even if one or both API calls failed, and may persist default AQI=50/status Sedang. Store per-source fetch outcome, source timestamp, successful fetch timestamp, and stale age separately. Never mark failed data as freshly updated.

### WQ-04 — AQI semantic labelling
The endpoint requests aggregate `us_aqi`, `pm2_5`, and `pm10`; the aggregate US AQI is the maximum of pollutant-specific indices, not a PM2.5-only index. The UI must call it **US AQI (estimasi model CAMS)**, distinguish it from Indonesian ISPU, and show PM2.5/PM10 only from their actual API fields with µg/m³ units. AQI category thresholds must match the provider's US EPA breakpoints and handle missing/negative/out-of-range values safely.

### WQ-05 — Weather metric meaning and time basis
The UI must label `uv_index_max` as maximum UV index today, not current UV. `pressure_msl` is mean-sea-level pressure, not local station surface pressure. Rain amount (mm) must be distinguished from probability (%). Weather, hourly and daily timestamps must use the API timezone returned for the selected coordinates; do not assume device clock/timezone matches the grid location. Missing sunrise/sunset must not become hard-coded times. WMO condition codes should be handled completely, including freezing precipitation/snow codes even if uncommon in Indonesia.

### WQ-06 — Separate weather data paths
`HomeWeather` fetches a compact Home line on a 15-minute cache, while `WeatherAirQuality` fetches the detailed dashboard on a 30-minute cache. These are independent paths with separate storage and freshness. They can disagree or update at different times. Define a shared source/data contract and make Home summary and detail view report their own true source timestamps/status; avoid stale old-location data during location/language changes.

### WQ-07 — AQI model limitations and attribution
Open-Meteo documents CAMS model forecasts, with global CAMS grid around 0.4° (~45 km), and European CAMS around 0.1° (~11 km). This is a model/grid estimate, not a local Indonesian regulatory monitoring-station reading. The dashboard must show Open-Meteo and CAMS attribution and explain that US AQI is not official Indonesian ISPU. Location presets must be described as selected reference coordinates/model grid point, not an actual monitoring station unless independently verified.

### WQ-08 — Popup coverage and responsibility
WeatherActivity has location selection, station/region selection and animation picker dialogs; the Info Center weather summary is a generic title/message dialog, not a structured detailed weather/AQI explanation. Source-level records state WeatherActivity adopts B01 TvDialogStyle, but that alone does not prove every popup's content hierarchy, clipping, D-pad focus, scroll, close/back behavior or data explanation is correct. E05 must audit these weather-specific popup contents and routes; B owns any defect in shared modal geometry/tokens/focus primitives.

## 3. Ownership and handover matrix

| Finding | Primary owner | Supporting owner | Required action / completion evidence |
|---|---|---|---|
| WQ-01 synthetic PM2.5/PM10; WQ-02 synthetic weather/forecast; WQ-04 AQI semantics; WQ-05 metric labels/time; WQ-07 attribution | **E05** | — | Correct API parsing and truthful unavailable/stale states; source and unit tests; update E05 report. No invented values. |
| WQ-03 source time, per-source success/failure, stale cache semantics; WQ-06 competing cache/request lifecycle | **D07** for concurrency/freshness contract | **D01/D02** only if preference ownership or immediate location-setting propagation is defective; **E05** consumes contract | Review whether detailed weather/AQI path is protected by location/language/request generation, whether last-known cache survives failure without being misrepresented, and whether refresh results can overwrite a newer location. Implement in owning D branch only if the D07 state contract is deficient; otherwise E05 fixes its adapter. Preserve C07 upload/session resilience. |
| WQ-08 shared modal sizing, dimming, glass opacity, focus ring, focus navigation | **B01/B02/B03** | E05 | Compare WeatherActivity and all weather dialogs against canonical TvUiTokens/TvDialogStyle. Fix shared primitive only in B owner branch; E05 must not fork global styling. Existing E05 dialog content, layout, explanation and action wiring remain E05 responsibility. |
| Weather-specific dialog layout/content, location terminology, summary/detail information, missing-data messages, scroll/close semantics at call site | **E05** | B01/B02/B03 | Audit every popup/action and detailed dashboard. Reuse shared modal helper; add clear source/model/freshness context and focusable close/back behavior. |
| WQ-08 integrated visual/remote acceptance, 1080p/4K, stale/offline/location/language scenarios | **F** | B, D, E | Run on target Mi TV Stick and 4K profile; attach reproducible evidence/results. F does not own code fixes: failures return to B/D/E root-cause owner. |
| Cross-project reconciliation, exact-head freeze, grouped integration and final closure | **G** | All A–F | Keep PRs Draft/unmerged until D-013 gates; reconcile branches and rerun exact-head CI plus cross-group regression. |

## 4. Explicit non-assignment to Project C

The weather/AQI requests are direct Open-Meteo HTTPS requests in `HomeWeather` and `WeatherAirQuality`. They do not use the C-series local QR upload server/body-reception/session pipeline. Therefore no weather/AQI API defect is assigned to C01–C07. C07 upload disconnect/retry/expiry/cancellation protections must remain untouched and be regression-tested only for non-regression.

## 5. Required repair sequence

1. **D07 cross-check first:** compare `HomeWeather` and `WeatherAirQuality` against the D07 generation/location/language freshness contract. Record whether the detailed path is covered. Any state/concurrency defect is repaired by D07 owner; do not duplicate a parallel state system in E05.
2. **B cross-check:** compare all weather and Info Center dialogs with canonical B01 tokens/modal contract and B02/B03 focus/opacity behavior. Shared primitive defect goes back to B; no local style fork in E05.
3. **E05 source repair:** after dependency contracts are clear, fix API parsing, no-fabrication rules, source timestamps, truthful stale/offline states, AQI meaning, metric labels, source attribution, location terminology and weather-specific popup content.
4. **Exact-head CI:** test invalid/missing/partial JSON, independent weather/AQI success/failure, stale cache, source timestamps, location/language switch during in-flight requests, forecast array gaps, all AQI bands, WMO codes, and 1080p/4K layout build checks.
5. **F integrated acceptance:** test offline/online recovery, changed location, language change, all weather popups, remote focus/scroll/back/close, dashboard vs Home consistency, and visual readability on Mi TV Stick 1920×1080 plus 4K.
6. **G closure:** verify all owners' findings are closed with evidence, freeze exact heads, audit combined diff, then grouped merge only under D-013 and explicit release approval.

## 6. Status

- Cross-project audit and ownership assignment: **RECORDED / SOURCE FINDINGS CONFIRMED**.
- Source repairs for WQ-01 through WQ-08: **NOT YET IMPLEMENTED**.
- B/D handover: **REQUIRED**; no claim that those owners have accepted or completed the work until their branch evidence is checked.
- E05 PR #38: keep **OPEN / DRAFT / UNMERGED**. Its ayah-reader CI pass does not close weather/AQI.
- Project F hardware acceptance: **PENDING**.
- No merge, APK installation, or physical test is claimed.

## 7. Reference

- Open-Meteo Air Quality API: https://open-meteo.com/en/docs/air-quality-api
- Open-Meteo model updates: https://open-meteo.com/en/docs/model-updates
- Repository decisions: D-013 and D-014 in `DECISION_LOG.md`.
