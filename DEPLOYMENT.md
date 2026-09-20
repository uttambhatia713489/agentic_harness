# DEPLOYMENT.md

# StoreOps SLA Breach Alerting — Deployment, Rollback & Validation Evidence

**Platform:** Claude Code Enterprise  
**Harness:** Configuration-Driven Governed Agentic Delivery Harness  
**Reference Application:** StoreOps  
**Feature:** SLA Breach Alerting  
**Technology:** Resolved from active harness configuration  
**Version:** 2.0  
**Status:** Evidence template — complete after successful harness execution

---

## 1. Purpose

This document records deployment, rollback, and runtime validation evidence for the StoreOps SLA Breach Alerting feature produced through the governed agentic delivery harness.

It demonstrates the traceability chain:

```text
PROMPT.md
    ↓
Planner
    ↓
Approved Sprint Contracts
    ↓
Generator
    ↓
Evaluator
    ↓
PASS
    ↓
Monitor
    ↓
CI/CD
    ↓
Deployment
    ↓
Runtime Validation
    ├── PASS → Deployment Verified
    └── FAIL → Rollback → Known-Good Version
```

Only actual observed results must be recorded.

Do not fabricate or pre-populate execution evidence.

---

# 2. Deployment Preconditions

Deployment may proceed only when:

- all required sprint contracts are approved;
- Generator implementation is complete;
- mandatory Evaluator hard gates pass;
- required sprint verdicts permit deployment;
- build and automated tests pass;
- required architecture checks pass;
- required security and quality checks pass;
- governance evidence is archived; and
- the previous known-good release is identifiable for rollback.

Record:

| Item | Actual |
|---|---|
| Run ID | `[ACTUAL]` |
| Application | `[ACTUAL]` |
| Feature | `[ACTUAL]` |
| Source Revision | `[ACTUAL]` |
| Final Harness Verdict | `[ACTUAL]` |
| Build Result | `[ACTUAL]` |
| Test Result | `[ACTUAL]` |
| Coverage Result | `[ACTUAL]` |
| Architecture Checks | `[ACTUAL]` |
| Static Analysis | `[ACTUAL]` |
| Security Checks | `[ACTUAL]` |
| Previous Known-Good Version | `[ACTUAL]` |

---

# 3. Resolved Deployment Configuration

Deployment configuration must be derived from the active harness, application, technology, and deployment configuration.

Record the effective values used for this deployment.

| Configuration | Actual |
|---|---|
| Application Profile | `[ACTUAL]` |
| Technology Profile | `[ACTUAL]` |
| Governance Policy | `[ACTUAL]` |
| Build Command | `[ACTUAL]` |
| Package Artifact | `[ACTUAL]` |
| Runtime Environment | `[ACTUAL]` |
| Deployment Target | `[ACTUAL]` |
| Application Endpoint | `[ACTUAL]` |

Do not record secrets, credentials, tokens, or private keys.

---

# 4. Build Verification

Execute the verification command defined by the active technology profile.

For the StoreOps reference profile this may resolve to:

```bash
mvn clean verify
```

Record actual results:

```text
Command:
[ACTUAL]

Result:
[ACTUAL]

Tests Executed:
[ACTUAL]

Tests Passed:
[ACTUAL]

Tests Failed:
[ACTUAL]

Overall Coverage:
[ACTUAL]

Architecture Violations:
[ACTUAL]

Static Analysis Result:
[ACTUAL]

Security Check Result:
[ACTUAL]
```

Expected deployment gate:

```text
Mandatory Checks = PASS
```

---

# 5. Package Verification

Build the deployable artifact using the configured technology profile.

Example:

```bash
mvn clean package
```

Record:

| Item | Actual |
|---|---|
| Package Command | `[ACTUAL]` |
| Package Result | `[ACTUAL]` |
| Artifact Name | `[ACTUAL]` |
| Artifact Location | `[ACTUAL]` |
| Artifact Version | `[ACTUAL]` |

---

# 6. Deployment

Deploy StoreOps using the deployment mechanism selected for the reference implementation.

Record only the actual mechanism used.

```text
Deployment Target:
[ACTUAL]

Deployment Command / Pipeline:
[ACTUAL]

Deployment Result:
[ACTUAL]

Application URL:
[ACTUAL]

Health Status:
[ACTUAL]
```

---

# 7. Rollback Strategy

## 7.1 Purpose

Rollback provides a controlled recovery path when the deployed StoreOps release fails deployment validation, runtime health checks, mandatory business scenarios, or introduces a blocking regression.

Rollback must restore the last known-good application version without deleting or modifying harness governance evidence.

Rollback is separate from harness remediation:

```text
Generator / Evaluator Failure
        ↓
Remediation / Retry
```

whereas:

```text
Deployment / Runtime Failure
        ↓
Rollback
```

---

## 7.2 Rollback Triggers

Initiate rollback when a blocking condition occurs after deployment, including:

- application startup failure;
- mandatory health-check failure;
- mandatory SLA Breach Alerting scenario failure;
- critical regression in existing StoreOps functionality;
- blocking architecture or security issue discovered after deployment;
- data-integrity risk;
- invalid deployment configuration;
- required runtime dependency failure; or
- release behavior determined unsafe to continue.

Non-blocking observations do not automatically require rollback unless the applicable deployment or governance policy requires it.

---

## 7.3 Rollback Preconditions

Before deployment, identify the last known-good release.

Record:

| Item | Actual |
|---|---|
| Previous Known-Good Version | `[ACTUAL]` |
| Previous Source Revision | `[ACTUAL]` |
| Previous Deployment Artifact | `[ACTUAL]` |
| Current Release Version | `[ACTUAL]` |
| Current Source Revision | `[ACTUAL]` |
| Rollback Mechanism | `[ACTUAL]` |

The previous known-good artifact or deployment revision must remain available until deployment validation completes.

---

## 7.4 Rollback Decision Flow

```text
Deployment
    ↓
Runtime Validation
    ↓
Blocking Failure?
   /          \
 NO            YES
 │              │
 v              v
Continue     Stop Release
Validation       ↓
             Record Failure
                 ↓
          Assess Data/Config Impact
                 ↓
          Rollback Safe?
             /       \
           YES        NO
            │          │
            v          v
        Rollback    Human Review
            │          │
            v          v
      Known-Good    Recovery Plan
        Version
            │
            v
      Health Validation
            │
            v
     Regression Validation
            │
            v
      Record Evidence
```

---

## 7.5 Rollback Procedure

If rollback is required:

1. stop further release progression;
2. preserve deployment and failure evidence;
3. identify the previous known-good version;
4. assess database, configuration, event, and integration compatibility;
5. execute the configured rollback mechanism;
6. restore the previous application version;
7. verify application health;
8. execute critical regression checks;
9. verify previous known-good behavior;
10. record rollback evidence.

Record:

```text
Rollback Required:
[YES/NO]

Rollback Reason:
[ACTUAL / N/A]

Rollback Command / Pipeline:
[ACTUAL / N/A]

Rollback Target Version:
[ACTUAL / N/A]

Rollback Source Revision:
[ACTUAL / N/A]

Rollback Result:
[ACTUAL / N/A]
```

Do not document hypothetical platform-specific commands as actual execution evidence.

---

## 7.6 Data and Configuration Considerations

Before rollback, determine whether the failed release introduced:

- database schema changes;
- data migrations;
- irreversible data changes;
- configuration changes;
- event/message schema changes;
- external integration changes; or
- other backward-incompatible behavior.

Application rollback must not automatically imply database rollback.

Use:

```text
Application Rollback
        ≠
Automatic Data Rollback
```

If rollback could cause data loss, schema incompatibility, or destructive recovery:

```text
STOP
  ↓
Human Review
  ↓
Approved Recovery Decision
```

Do not perform destructive data recovery automatically.

---

## 7.7 Post-Rollback Validation

After rollback, verify:

| Check | Expected | Actual | Status |
|---|---|---|---|
| Application starts successfully | Yes | `[ACTUAL]` | `[PASS/FAIL]` |
| Health endpoint responds | Healthy | `[ACTUAL]` | `[PASS/FAIL]` |
| Required persistence is available | Available | `[ACTUAL]` | `[PASS/FAIL]` |
| Required event mechanism is available | Available | `[ACTUAL]` | `[PASS/FAIL]` |
| Existing StoreOps functionality operates | Yes | `[ACTUAL]` | `[PASS/FAIL]` |
| Previous known-good behavior is restored | Yes | `[ACTUAL]` | `[PASS/FAIL]` |

---

## 7.8 Rollback Governance Evidence

Rollback must not delete, overwrite, or alter existing harness evidence.

Preserve:

```text
Run ID
Source Revision
Deployment Result
Failure Evidence
Rollback Decision
Rollback Reason
Rollback Target
Rollback Result
Post-Rollback Validation
```

Record:

| Evidence | Actual |
|---|---|
| Rollback Required | `[YES/NO]` |
| Rollback Reason | `[ACTUAL / N/A]` |
| Rollback Target | `[ACTUAL / N/A]` |
| Rollback Result | `[ACTUAL / N/A]` |
| Post-Rollback Health | `[ACTUAL / N/A]` |

---

## 7.9 Rollback Outcome

If rollback succeeds and the previous known-good release is restored:

```text
ROLLBACK_VERIFIED
```

If rollback fails:

```text
ROLLBACK_FAILED
```

A failed rollback requires immediate human intervention.

It must not be represented as a successful deployment.

---

# 8. Runtime Health Validation

Verify that the deployed StoreOps application is operational before testing SLA behavior.

Record:

| Check | Expected | Actual | Status |
|---|---|---|---|
| Application starts successfully | Yes | `[ACTUAL]` | `[PASS/FAIL]` |
| Health endpoint responds | Healthy | `[ACTUAL]` | `[PASS/FAIL]` |
| Required persistence is available | Available | `[ACTUAL]` | `[PASS/FAIL]` |
| Required event mechanism is available | Available | `[ACTUAL]` | `[PASS/FAIL]` |
| Existing StoreOps functionality remains operational | Yes | `[ACTUAL]` | `[PASS/FAIL]` |

If a mandatory health check fails, evaluate the rollback criteria before continuing business validation.

---

# 9. SLA Breach Alerting Validation

Validate the business outcomes defined in `PROMPT.md` and the approved sprint contracts.

---

## Scenario 1 — HIGH Priority Overdue Activity

### Given

```text
Priority = HIGH
Due Date/Time = Passed
Status != DONE
```

### Expected

```text
SLA breach raised
Department Lead notified
```

### Actual

```text
[ACTUAL]
```

### Result

```text
[PASS/FAIL]
```

---

## Scenario 2 — CRITICAL Priority Overdue Activity

### Given

```text
Priority = CRITICAL
Due Date/Time = Passed
Status != DONE
```

### Expected

```text
SLA breach raised
Department Lead notified
```

### Actual

```text
[ACTUAL]
```

### Result

```text
[PASS/FAIL]
```

---

## Scenario 3 — LOW Priority Overdue Activity

### Given

```text
Priority = LOW
Due Date/Time = Passed
Status != DONE
```

### Expected

```text
No SLA breach
No SLA notification
```

### Actual

```text
[ACTUAL]
```

### Result

```text
[PASS/FAIL]
```

---

## Scenario 4 — MEDIUM Priority Overdue Activity

### Given

```text
Priority = MEDIUM
Due Date/Time = Passed
Status != DONE
```

### Expected

```text
No SLA breach
No SLA notification
```

### Actual

```text
[ACTUAL]
```

### Result

```text
[PASS/FAIL]
```

---

## Scenario 5 — Completed Activity

### Given

```text
Priority = HIGH or CRITICAL
Due Date/Time = Passed
Status = DONE
```

### Expected

```text
No SLA breach
```

### Actual

```text
[ACTUAL]
```

### Result

```text
[PASS/FAIL]
```

---

## Scenario 6 — Activity Not Yet Overdue

### Given

```text
Priority = HIGH or CRITICAL
Due Date/Time = Future
Status != DONE
```

### Expected

```text
No SLA breach
```

### Actual

```text
[ACTUAL]
```

### Result

```text
[PASS/FAIL]
```

---

## Scenario 7 — Department Lead Notification

### Given

```text
Eligible SLA breach exists
```

### Expected

```text
Responsible Department Lead receives the initial SLA breach notification
```

### Actual

```text
[ACTUAL]
```

### Result

```text
[PASS/FAIL]
```

---

## Scenario 8 — Store Manager Escalation

### Given

```text
Eligible SLA breach exists
Configured Grace Period has expired
Activity remains unresolved
```

### Expected

```text
Appropriate STORE_MANAGER receives escalation notification
```

### Actual

```text
[ACTUAL]
```

### Result

```text
[PASS/FAIL]
```

---

## Scenario 9 — Resolved Before Configured Grace Period

### Given

```text
Eligible SLA breach exists
Activity is resolved before the configured grace period expires
```

### Expected

```text
No Store Manager escalation
```

### Actual

```text
[ACTUAL]
```

### Result

```text
[PASS/FAIL]
```

---

# 10. Validation Summary

| Scenario | Expected | Actual Result |
|---|---|---|
| HIGH overdue | SLA breach | `[PASS/FAIL]` |
| CRITICAL overdue | SLA breach | `[PASS/FAIL]` |
| LOW overdue | No breach | `[PASS/FAIL]` |
| MEDIUM overdue | No breach | `[PASS/FAIL]` |
| DONE overdue | No breach | `[PASS/FAIL]` |
| Not overdue | No breach | `[PASS/FAIL]` |
| Department Lead notification | Notification created | `[PASS/FAIL]` |
| Unresolved after configured grace period | Store Manager escalation | `[PASS/FAIL]` |
| Resolved before configured grace period | No escalation | `[PASS/FAIL]` |

Any mandatory business-validation failure must be assessed against the rollback criteria before the release is accepted.

---

# 11. Architecture Validation

Confirm that runtime behavior remains consistent with the active StoreOps architecture rules.

Record actual evidence.

| Rule | Actual Result |
|---|---|
| Controller → Service → Repository | `[PASS/FAIL]` |
| No prohibited cross-module repository access | `[PASS/FAIL]` |
| Cross-module reads follow approved mechanism | `[PASS/FAIL]` |
| Cross-module side effects follow approved event mechanism | `[PASS/FAIL]` |
| Reports remains read-only | `[PASS/FAIL]` |
| Configured error contract preserved | `[PASS/FAIL]` |

Evidence source:

```text
[ACTUAL TEST / CHECK / REPORT]
```

---

## 11.1 SLA Breach Implementation Verification

The implementation mechanism is Planner-discovered and approved through sprint contracts. `DEPLOYMENT.md` records evidence for the actual mechanism selected; it does not prescribe an event-based, service-based, or other implementation pattern.

Record evidence based on the approved implementation mechanism (event-driven, service-mediated, or other approved StoreOps pattern). Use the actual mechanism identified during Planner discovery and approved through sprint contracts.

Record only the fields relevant to the approved mechanism. Do not fabricate mechanism-specific artifacts.

If the approved mechanism is event-driven:

```text
Breach Publisher:
[ACTUAL FILE:LINE]

Breach Event:
[ACTUAL FILE:LINE]

Alerts Event Consumer:
[ACTUAL FILE:LINE]

Department Lead Alert Creation:
[ACTUAL FILE:LINE]

Store Manager Escalation:
[ACTUAL FILE:LINE]
```

If the approved mechanism is service-mediated or another approved StoreOps pattern:

```text
Breach Detection Source:
[ACTUAL FILE:LINE]

Notification Mechanism:
[ACTUAL FILE:LINE]

Department Lead Notification Path:
[ACTUAL FILE:LINE]

Store Manager Escalation Path:
[ACTUAL FILE:LINE]
```

---

# 12. Harness Governance Evidence

Record the final harness execution evidence.

| Evidence | Location |
|---|---|
| Resolved Configuration | `[ACTUAL]` |
| Specification | `[ACTUAL]` |
| Approved Sprint Contracts | `[ACTUAL]` |
| Generator Summaries (per iteration) | `[ACTUAL — one or more generator-summary-iteration-N.md]` |
| Evaluator Feedback (per iteration) | `[ACTUAL — one or more evaluator-feedback-iteration-N.md]` |
| Run Logs | `[ACTUAL]` |
| Escalation Evidence (`escalation.md`), if any | `[ACTUAL / N/A]` |
| Review Archive | `[ACTUAL]` |

Expected review root:

```text
.harness/reviews/<run-id>/
    sprint-N/
        sprint-N-contract.md
        generator-summary-iteration-1.md
        evaluator-feedback-iteration-1.md
        generator-summary-iteration-2.md (if applicable)
        evaluator-feedback-iteration-2.md (if applicable)
        run-log.md
```

---

# 13. Retry / Remediation Evidence

Harness remediation and deployment rollback are separate controls.

```text
Pre-Deployment Failure
        ↓
Generator / Evaluator Remediation
```

```text
Post-Deployment Blocking Failure
        ↓
Rollback
```

If any sprint required remediation, record the actual sequence.

Example:

```text
Iteration 1
    ↓
VERDICT: FAIL
    ↓
Evaluator Finding
    ↓
Generator Remediation
    ↓
Iteration 2
    ↓
VERDICT: PASS
```

Record:

| Sprint | Iterations | Initial Verdict | Final Verdict |
|---|---:|---|---|
| `[ACTUAL]` | `[ACTUAL]` | `[ACTUAL]` | `[ACTUAL]` |

If no retry occurred:

```text
No remediation iteration was required.
```

Do not manufacture a failure solely for demonstration purposes.

---

# 14. CI/CD Validation

Where CI/CD is configured, record independent pipeline validation.

| Check | Actual |
|---|---|
| Pipeline | `[ACTUAL]` |
| Source Revision | `[ACTUAL]` |
| Build | `[ACTUAL]` |
| Tests | `[ACTUAL]` |
| Coverage | `[ACTUAL]` |
| Static Analysis | `[ACTUAL]` |
| Architecture Checks | `[ACTUAL]` |
| Security Checks | `[ACTUAL]` |
| Pipeline Result | `[ACTUAL]` |

The CI/CD result must represent the committed revision being deployed.

Harness evaluation does not replace CI/CD validation.

---

# 15. Deployment Issues

Record only issues actually encountered.

| Issue | Impact | Resolution | Status |
|---|---|---|---|
| `[ACTUAL / None]` | `[ACTUAL]` | `[ACTUAL]` | `[ACTUAL]` |

If an issue triggered rollback, reference the corresponding rollback evidence.

---

# 16. Residual Risks

Record unresolved risks remaining after deployment or rollback.

| Risk | Impact | Mitigation / Follow-Up |
|---|---|---|
| `[ACTUAL / None]` | `[ACTUAL]` | `[ACTUAL]` |

Do not invent residual risks solely to populate this section.

---

# 17. Final Deployment Result

```text
Run ID:
[ACTUAL]

Source Revision:
[ACTUAL]

Harness Verdict:
[ACTUAL]

CI/CD Result:
[ACTUAL / N/A]

Deployment Result:
[ACTUAL]

Business Validation:
[ACTUAL]

Rollback Required:
[YES/NO]

Rollback Reason:
[ACTUAL / N/A]

Rollback Result:
[ACTUAL / N/A]

Final Running Version:
[ACTUAL]

Residual Risks:
[ACTUAL / None]
```

Use one of the following final statuses:

```text
DEPLOYMENT_VERIFIED
```

```text
ROLLBACK_VERIFIED
```

```text
DEPLOYMENT_FAILED
```

```text
ROLLBACK_FAILED
```

### `DEPLOYMENT_VERIFIED`

Use only when:

- deployment succeeds;
- required runtime health checks pass;
- mandatory SLA scenarios pass;
- required governance evidence exists;
- rollback was not required; and
- no unresolved blocking issue remains.

### `ROLLBACK_VERIFIED`

Use when:

- the new deployment failed or was rejected;
- rollback was executed successfully;
- the previous known-good version was restored; and
- post-rollback validation passed.

### `DEPLOYMENT_FAILED`

Use when deployment or validation fails and the release cannot be accepted.

### `ROLLBACK_FAILED`

Use when rollback was required but the previous known-good state could not be successfully restored.

This state requires immediate human intervention.

---

# 18. Evidence Chain

The completed demonstration should provide the following traceability:

```text
PROMPT.md
    ↓
resolved-config.yaml
    ↓
spec.md
    ↓
Approved Sprint Contracts
    ↓
Generated Source + Tests
    ↓
generator-summary-iteration-N.md
    ↓
evaluator-feedback-iteration-N.md
    ↓
run-log.md
    ↓
escalation.md (if applicable)
    ↓
CI/CD Validation
    ↓
Deployment
    ↓
Runtime Validation
    ├── PASS
    │     ↓
    │ DEPLOYMENT_VERIFIED
    │
    └── FAIL
          ↓
       Rollback
          ↓
   Post-Rollback Validation
          ↓
     ROLLBACK_VERIFIED
          ↓
      DEPLOYMENT.md
          ↓
      REFLECTION.md
```

This document must contain **observed deployment and rollback evidence only**.

Design assumptions, planned values, expected results, and hypothetical rollback commands must not be represented as actual execution evidence.