# Run Log Template

Runtime instantiation: `.harness/output/<run-id>/run-log.md`

This template records governance-observability evidence for a harness run.

It preserves execution history derived from canonical durable evidence and harness execution metadata.

It does not:

- issue or reinterpret Evaluator verdicts;
- define governance thresholds;
- define hard gates;
- define scoring weights or verdict thresholds;
- define `maxIterations`;
- authorize retries or escalation;
- determine sprint progression or run completion;
- replace Generator or Evaluator evidence;
- replace CI/CD independent revalidation; or
- define deployment, rollback, or reflection behavior.

The active governance policy, resolved configuration, agent definitions, and `CLAUDE.md` remain authoritative for those responsibilities.

---

## Run Metadata

```text
Run ID:                 <RUN_ID>
Application:            <resolved application id>
Technology:             <resolved technology profile id>
Governance Policy:      <resolved governance policy id>
Feature Prompt:         <path/to/PROMPT.md>
Specification:          .harness/output/<RUN_ID>/spec.md
Resolved Configuration: .harness/output/<RUN_ID>/resolved-config.yaml
Run Started At:         <timestamp or N/A>
Run Completed At:       <timestamp or N/A>
```

Timestamps must be derived from available harness execution metadata.

Do not fabricate unavailable timestamps.

---

## Run Evidence

Reference the canonical run-level evidence available for this execution.

```text
Feature Prompt:          <path/to/PROMPT.md>
Specification:           <path/to/spec.md>
Resolved Configuration:  <path/to/resolved-config.yaml>
Sprint Contracts:        <list of sprint-N-contract.md references>
Escalation Evidence:     <path/to/escalation.md or N/A>
```

This log references authoritative evidence rather than duplicating its complete content.

---

## Sprint History

Record each sprint independently.

Example structure:

```text
Sprint ID:               <SPRINT_ID>
Contract ID:             <CONTRACT_ID>
Sprint Contract:         <path/to/sprint-N-contract.md>
Iterations Used:         <count>
Final Evaluator Verdict: <PASS / CONDITIONAL_PASS / FAIL / N/A>
Escalation Status:       <status determined by harness or N/A>
Sprint Started At:       <timestamp or N/A>
Sprint Concluded At:     <timestamp or N/A>
```

The Monitor records the sprint outcome only after the harness determines that the sprint has concluded.

Monitor does not independently determine sprint completion.

Repeat for each sprint.

---

## Iteration History

Record every Generator/Evaluator iteration independently and preserve iteration ordering.

Example structure:

```text
Sprint ID:               <SPRINT_ID>
Contract ID:             <CONTRACT_ID>
Iteration:               <N>

Generator Evidence:
  <path/to/generator-summary-iteration-N.md>

Evaluator Evidence:
  <path/to/evaluator-feedback-iteration-N.md>

Evaluator Verdict:
  <PASS / CONDITIONAL_PASS / FAIL>

Findings / Violations:
  <summary or evidence reference>

Quality Evidence:
  <summary or evaluator evidence reference>

Escalation Status:
  <status determined by harness or N/A>

Iteration Started At:
  <timestamp or N/A>

Iteration Evaluated At:
  <timestamp or N/A>
```

Detailed deterministic quality evidence remains authoritative in:

```text
evaluator-feedback-iteration-N.md
```

Detailed implementation and Generator verification evidence remains authoritative in:

```text
generator-summary-iteration-N.md
```

The run log should summarize or reference those artifacts rather than duplicate their complete contents.

---

## Deterministic Quality Summary

Where available, record a concise summary or reference for configured deterministic checks.

Example structure:

| Sprint | Iteration | Build | Tests | Coverage | Static Analysis | Architecture | Security | Evaluator Evidence |
|---|---:|---|---|---|---|---|---|---|
| `<SPRINT_ID>` | `<N>` | `<result or N/A>` | `<result or N/A>` | `<result or N/A>` | `<result or N/A>` | `<result or N/A>` | `<result or N/A>` | `<reference>` |

Use:

```text
N/A
```

when a check is not configured, not applicable, or the result is unavailable.

Do not infer or fabricate quality results.

The Evaluator evidence remains authoritative for detailed check results, configured requirements, hard-gate outcomes, and findings.

---

## Acceptance-Criteria Summary

Where useful for traceability, record the Evaluator's acceptance-criteria outcome by reference.

Example structure:

```text
Sprint ID:          <SPRINT_ID>
Iteration:          <N>
ACs Passed:         <count or N/A>
ACs Failed:         <count or N/A>
ACs Ambiguous:      <count or N/A>
Evaluator Evidence: <path/to/evaluator-feedback-iteration-N.md>
```

Do not independently evaluate acceptance criteria in the run log.

---

## Findings Summary

Record only evidence-derived findings information.

Example structure:

```text
Sprint ID:              <SPRINT_ID>
Iteration:              <N>
Mandatory Findings:     <count or N/A>
Non-Blocking Findings:  <count or N/A>
Findings Reference:     <path/to/evaluator-feedback-iteration-N.md>
```

Do not reinterpret finding severity or create new findings.

---

## Verdict History

Preserve Evaluator verdicts exactly as issued.

Example structure:

| Sprint | Contract | Iteration | Evaluator Verdict | Evidence |
|---|---|---:|---|---|
| `<SPRINT_ID>` | `<CONTRACT_ID>` | `<N>` | `<PASS / CONDITIONAL_PASS / FAIL>` | `<evaluator evidence reference>` |

Monitor must not:

- change a verdict;
- reinterpret a verdict;
- convert `CONDITIONAL_PASS` into mandatory human intervention unless the active governance policy explicitly requires it;
- determine whether another iteration is permitted; or
- determine whether the run is complete.

---

## Escalation History

Record escalation only when the harness has determined that escalation applies.

Example structure:

```text
Sprint ID:               <SPRINT_ID>
Contract ID:             <CONTRACT_ID>
Iteration:               <N>
Final Evaluator Verdict: <PASS / CONDITIONAL_PASS / FAIL>
Escalation Status:       <status determined by harness>
Escalation Evidence:     <path/to/escalation.md>
Recorded At:             <timestamp or N/A>
```

If no escalation occurred:

```text
Escalation Status: N/A
```

Monitor must not initiate, authorize, resolve, or reinterpret escalation.

---

## Telemetry

Record only telemetry actually available from harness execution metadata or canonical evidence.

Example structure:

```text
Sprint ID:        <SPRINT_ID>
Iteration:        <N>
Token Usage:      <value or N/A>
Estimated Cost:   <value or N/A>
Timestamp:        <timestamp or N/A>
Telemetry Source: <harness metadata reference or N/A>
```

Do not:

- infer token usage;
- fabricate cost;
- estimate unavailable telemetry;
- fabricate performance metrics;
- fabricate quality trends; or
- convert qualitative observations into unsupported numeric metrics.

If a required telemetry field is unavailable, record:

```text
N/A
```

---

## Evidence-Derived Observations

Record only concise factual observations directly supported by canonical evidence or harness execution metadata.

Example structure:

```text
Observation ID:       <OBS-N>
Sprint ID:            <SPRINT_ID or N/A>
Iteration:            <N or N/A>
Observation:          <concise factual observation>
Evidence Reference:   <canonical evidence or metadata reference>
Recorded At:          <timestamp or N/A>
```

Permitted observations include facts such as:

```text
Evaluator returned FAIL for iteration <N>.
A retry was authorized by the harness.
The configured maximum iteration count was exhausted.
Token usage was unavailable and recorded as N/A.
The sprint concluded with PASS.
```

Monitor must not:

- create new findings;
- infer a cause not stated in the evidence;
- make an independent quality judgment;
- reinterpret an Evaluator result;
- create a trend from a single observation;
- prescribe remediation;
- recommend an architecture or implementation change; or
- convert an observation into a routing decision.

Every observation must include an authoritative evidence reference.

If no evidence-derived observation is required:

```text
Evidence-Derived Observations: N/A
```

---

## Human Intervention History

Record only actual human approval, disposition, or escalation interactions supplied by harness execution metadata or canonical evidence.

Example structure:

```text
Interaction ID:       <HUMAN-N>
Run ID:               <RUN_ID>
Sprint ID:            <SPRINT_ID or N/A>
Contract ID:          <CONTRACT_ID or N/A>
Interaction Type:     <Approval / Conditional-Pass Disposition / Escalation Decision / Other>
Decision:             <recorded decision or N/A>
Recorded By:          <identity/reference or N/A>
Recorded At:          <timestamp or N/A>
Evidence Reference:   <approval/disposition/escalation evidence reference or N/A>
```

Monitor must not infer that:

- approval occurred;
- a participant authorized implementation;
- `CONDITIONAL_PASS` received human disposition;
- escalation was resolved; or
- a contract change was approved.

If the corresponding decision is not present in harness metadata or canonical evidence, record:

```text
N/A
```

Human approval and disposition remain owned by the configured approval/governance workflow.

---

## Final Run Outcome

Record the final run outcome only after the harness has determined that the run has concluded.

```text
Run ID:                         <RUN_ID>
Harness-Determined Run Status:  <recorded status or N/A>
Total Sprints:                  <count or N/A>
Concluded Sprints:              <count or N/A>
Final Sprint ID:                <SPRINT_ID or N/A>
Final Contract ID:              <CONTRACT_ID or N/A>
Final Sprint Evaluator Verdict: <PASS / CONDITIONAL_PASS / FAIL / N/A>
Escalation Status:              <harness-determined status or N/A>
Run Completed At:               <timestamp or N/A>
Outcome Evidence Reference:     <harness/evaluator/escalation reference or N/A>
```

The final sprint Evaluator verdict must not be presented as a separately determined run-level verdict.

Monitor must not determine:

```text
Retry Authorization
Escalation
Human Disposition
Sprint Progression
Run Completion
Run Outcome
```

If the harness has not concluded the run:

```text
Harness-Determined Run Status: N/A
Run Completed At:              N/A
```

---

## CI/CD Handoff

Record CI/CD handoff information only when it is actually available.

```text
Committed Revision:       <commit/tag/pull-request reference or N/A>
CI/CD Pipeline Reference: <pipeline/run reference or N/A>
CI/CD Status:             <recorded pipeline status or N/A>
CI/CD Evidence:           <evidence reference or N/A>
Recorded At:              <timestamp or N/A>
```

Monitor must not:

- infer that a revision was committed;
- determine CI/CD success;
- claim that CI/CD checks executed without evidence;
- reinterpret CI/CD outcomes; or
- substitute harness evidence for pipeline evidence.

The relationship remains:

```text
Harness Governance Evidence
        ↓
Audit / Traceability

Committed Revision
        ↓
CI/CD Independent Revalidation
```

Harness verdicts and `run-log.md` do not replace independent CI/CD validation.

---

## Downstream Deployment and Runtime References

Record downstream deployment, runtime-validation, or rollback references only when those artifacts and statuses are actually available.

```text
Deployment Evidence:        <DEPLOYMENT.md or other reference or N/A>
Deployment Status:          <recorded downstream status or N/A>
Runtime-Validation Evidence:<reference or N/A>
Rollback Evidence:          <reference or N/A>
Rollback Status:            <recorded downstream status or N/A>
Recorded At:                <timestamp or N/A>
```

These values are reference-only.

Monitor does not:

- authorize deployment;
- execute deployment;
- determine deployment success;
- perform runtime validation;
- initiate rollback;
- determine rollback success; or
- modify downstream evidence.

Harness completion is distinct from:

```text
Deployment
Runtime Validation
Rollback
Reflection
```

Deployment or rollback evidence is not mandatory for the run log unless the active downstream lifecycle explicitly requires the log to reference it.

---

## Reflection Reference

Record a reflection reference only after an actual reflection artifact exists.

```text
Reflection Evidence: <REFLECTION.md reference or N/A>
Recorded At:         <timestamp or N/A>
```

Monitor must not:

- perform reflection;
- create reflection findings;
- infer lessons learned;
- recommend architectural improvements; or
- represent expected behavior as observed behavior.

`REFLECTION.md` remains a downstream evidence-based lifecycle artifact.

---

## Evidence Traceability

Maintain traceability among all available canonical artifacts.

```text
Feature Prompt:
  <reference or N/A>

Resolved Configuration:
  <reference or N/A>

Specification:
  <reference or N/A>

Sprint Contract:
  <reference or N/A>

Generator Evidence:
  <generator-summary-iteration-N.md reference or N/A>

Evaluator Evidence:
  <evaluator-feedback-iteration-N.md reference or N/A>

Run Log:
  <run-log.md reference>

Escalation Evidence:
  <escalation.md reference or N/A>

CI/CD Evidence:
  <reference or N/A>

Deployment Evidence:
  <reference or N/A>

Reflection Evidence:
  <reference or N/A>
```

Repeat sprint and iteration references as required.

The run log must contain enough references to reconstruct the governed execution history without duplicating the complete contents of authoritative evidence.

---

## Evidence Preservation and Corrections

Previously recorded evidence and historical run-log entries must remain preserved.

Do not:

- overwrite prior iteration entries;
- alter Generator evidence;
- alter Evaluator evidence;
- alter sprint contracts;
- alter the resolved configuration snapshot;
- alter escalation evidence;
- delete prior evidence references;
- rewrite historical verdicts;
- retroactively modify telemetry; or
- replace historical entries with later outcomes.

Where `run-log.md` is maintained as one run-level artifact, new records must be appended or otherwise added without changing previously recorded facts.

If a run-log recording error must be corrected, append a correction entry:

```text
Correction ID:            <CORRECTION-N>
Original Entry Reference: <reference>
Incorrect Recorded Value: <value>
Corrected Value:          <value>
Correction Reason:        <factual explanation>
Corrected At:             <timestamp or N/A>
Correction Evidence:      <reference or N/A>
```

A correction entry must not modify the underlying authoritative evidence.

---

## Template Completion Rules

When populating this template:

1. Use canonical runtime artifact names:
   ```text
   resolved-config.yaml
   spec.md
   sprint-N-contract.md
   generator-summary-iteration-N.md
   evaluator-feedback-iteration-N.md
   run-log.md
   escalation.md
   ```

2. Preserve:
   ```text
   Run ID
   Sprint ID
   Contract ID
   Iteration Number
   ```

   consistently across all references.

3. Record `N/A` when a required field is unavailable or not applicable.

4. Do not fabricate:
   - timestamps;
   - telemetry;
   - costs;
   - quality results;
   - acceptance-criterion outcomes;
   - findings;
   - verdicts;
   - approvals;
   - escalation decisions;
   - CI/CD results;
   - deployment results; or
   - reflection conclusions.

5. Preserve Evaluator verdicts and check outcomes exactly as issued.

6. Summarize or reference canonical evidence rather than reproducing full Generator or Evaluator reports.

7. Preserve all prior iteration and historical run-log entries.

8. Use the active resolved configuration for runtime locations; this template must not create a competing path convention.

9. Keep Monitor within its evidence-recording boundary.

---

## Responsibility Boundary

Monitor records:

```text
Run Metadata
Sprint History
Iteration History
Evaluator Verdict History
Acceptance-Criteria Summaries or References
Findings Summaries or References
Deterministic Quality Summaries or References
Escalation Status
Available Telemetry
Evidence-Derived Observations
Human Intervention History
Harness-Determined Final Run Outcome
CI/CD and Downstream References
Evidence Traceability
```

Monitor does not:

```text
Plan Work
Generate Implementation
Modify Tests
Execute Deterministic Checks
Evaluate Acceptance Criteria
Create or Reclassify Findings
Issue or Change Verdicts
Modify Contracts
Modify Configuration
Modify Governance Policy
Authorize Retries
Own maxIterations
Initiate or Resolve Escalation
Determine Sprint Progression
Determine Run Completion
Determine CI/CD Success
Deploy
Perform Runtime Validation
Rollback
Reflect
```

The harness and active governance policy control routing and lifecycle progression.

---

## Configuration Ownership

This template must not become an authoritative source for:

```text
Build Commands
Test Commands
Coverage Commands
Coverage Thresholds
Static-Analysis Commands
Architecture-Verification Commands
Security Commands
Hard Gates
Scoring Dimensions
Scoring Weights
Verdict Thresholds
Fail-Closed Policy
maxIterations
Approval Mechanism
Output Paths
Review Paths
Deployment Controls
Rollback Controls
```

Ownership remains:

```text
Technology Profile
    → tools, frameworks, and commands

Governance Policy
    → gates, thresholds, scoring, verdict policy,
      failClosed, maxIterations, retry, escalation

Harness Configuration
    → profile selection, locations, approval,
      context isolation, audit, observability,
      orchestration boundaries

Evaluator Evidence
    → authoritative check results, findings,
      acceptance-criteria results, scores, verdict

Generator Evidence
    → authoritative implementation summary,
      Generator verification, self-assessment,
      limitations, risks, assumptions

Run Log
    → recording, referencing, preserving,
      and tracing authoritative evidence
```
