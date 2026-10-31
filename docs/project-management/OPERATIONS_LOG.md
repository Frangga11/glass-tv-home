# Glass TV Home — Operational Log

**Version:** 1.0  
**Updated:** 31 October 2026  
**Rule:** concise events only. Detailed findings and test matrices remain in the relevant audit.

| Date | Work item | Branch / PR / evidence | Event and result | Next action |
|---|---|---|---|---|
| 2026-10-31 | Documentation foundation | `docs/project-master-inventory` | GitHub selected as canonical record; ChatGPT Library retained as milestone backup; project docs organized modularly | Apply DOCUMENTATION_WORKFLOW.md |
| 2026-10-31 | Documentation standard D-007 | `DOCUMENTATION_WORKFLOW.md` | Approved three connected layers: Master, Audit/SOP, Operational Log; one authoritative home per fact | Use for all future work |
| 2026-10-31 | Avatar audit | [Draft PR #6](https://github.com/Frangga11/glass-tv-home/pull/6) | QR single-photo avatar implementation and CI pass recorded; actual TV/phone acceptance not yet evidenced | User device acceptance |
| 2026-10-31 | Wallpaper QR security W-001 / W-F-01 | [Draft PR #7](https://github.com/Frangga11/glass-tv-home/pull/7), [CI run 41200811301](https://github.com/Frangga11/glass-tv-home/actions/runs/41200811301) | Replaced bare-root wallpaper QR routes with per-server random session-scoped routes; added JVM route tests. CI PASS. PR remains draft. | Verify foreign/legacy route rejection and dialog-close revocation on TV/phone |
| 2026-10-31 | Default wallpaper W-001 / W-F-02 | Artifact 13042880821 from CI run 41200811301 | APK inspection found no `assets/kabut-pagi-dempo.jpg`; intended offline default image is not packaged | User approves/provides final image; add asset and verify APK |

