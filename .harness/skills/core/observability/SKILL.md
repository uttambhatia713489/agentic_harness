# Observability

## Purpose

Provide reusable governance-observability guidance for the governed agentic delivery harness.

This skill supports Monitor recording, iteration/verdict history, escalation status, timestamps, evidence references, and available telemetry.

It is intentionally application- and technology-agnostic.

It does not define application business rules, application architecture rules, technology-specific implementation conventions, governance thresholds, orchestration statuses, retry/escalation policy, deployment controls, rollback controls, or reflection.

Those concerns remain owned by the applicable configuration, governance policy, orchestration contract, agent definitions, deployment lifecycle, and reflection artifact.

---

## Scope

This skill applies when the harness records execution history and governance-observability evidence.

Observability information must be derived from authoritative inputs:

```text
Approved Sprint Contract
Current Generator Iteration Evidence
Current Evaluator Iteration Evidence
Prior Iteration Evidence (when applicable)
Escalation Evidence (when applicable)
Resolved Configuration Reference
Harness Execution Metadata
```

Detailed deterministic evaluation evidence remains authoritative in:

```text
evaluator-feedback-iteration-N.md
```

Detailed implementation evidence remains authoritative in:

```text
generator-summary-iteration-N.md
```

Observability may summarize or reference these evidence artifacts without duplicating their complete content.

---

## Observability Principles

Observability must be:

- evidence-based;
- derived from authoritative canonical evidence and harness execution metadata;
- traceable to the approved sprint contract;
- non-invasive;
- immutable with respect to previously recorded facts;
- clearly labeled where a value is unavailable; and
- restricted to Monitor's responsibility boundary.

Observability must not:

- issue evaluation verdicts;
- reinterpret Evaluator verdicts;
- modify Generator, Evaluator, contract, or escalation evidence;
- introduce new verification rules;
- introduce governance thresholds;
- introduce orchestration states;
- authorize retries or escalation;
- fabricate telemetry;
- extrapolate unavailable telemetry;
- promote informational observations into governance decisions; or
- replace CI/CD independent revalidation.

---

## Observability Inputs

Observability information is derived only from authoritative inputs.

### Canonical Evidence

```text
sprint-N-contract.md
generator-summary-iteration-N.md
evaluator-feedback-iteration-N.md
prior iteration evidence
escalation.md (when applicable)
```

### Reference Artifacts

```text
resolved-config.yaml
spec.md (reference for traceability)
```

### Harness Execution Metadata

```text
Run ID
Sprint ID
Contract ID
Current Iteration
Iterations Used
Current Sprint Status
Timestamps
Token Usage (when available)
Estimated Cost (when available)
Retry / Escalation Decisions (as determined by the harness)
```

Observability must not require:

- the complete application source tree;
- unrelated domain packs;
- unrelated technology packs;
- Generator implementation reasoning;
- Evaluator internal reasoning;
- deployment evidence;
- rollback evidence;
- reflection content; or
- an undefined persisted Run State artifact.

---

## Verdict Preservation

Observability preserves Evaluator verdicts as recorded:

```text
PASS
CONDITIONAL_PASS
FAIL
```

Observability must not:

- issue verdicts;
- reinterpret verdicts;
- change a verdict outcome;
- convert `CONDITIONAL_PASS` into a mandatory human-intervention state unless the active governance policy explicitly configures it;
- determine whether another iteration is permitted;
- determine whether escalation is required; or
- treat `PASS` or `CONDITIONAL_PASS` as final for the run when additional approved sprints remain.

Routing after the verdict is controlled by the harness.

---

## Iteration Recording

Observability records information for each Generator/Evaluator iteration independently.

Canonical iteration evidence used as reference:

```text
generator-summary-iteration-N.md
evaluator-feedback-iteration-N.md
```

For each iteration, observability records:

```text
Iteration Number
Evaluator Verdict
Findings Summary or Reference
Available Quality Evidence Summary or Reference
Escalation Status
Timestamp
Evidence References
```

Prior iteration records must not be overwritten.

The observability record must preserve iteration ordering.

---

## Sprint Recording

A sprint outcome is recorded after the harness determines the sprint has concluded.

Observability records:

```text
Run ID
Sprint ID
Contract ID
Iterations Used
Final Evaluator Verdict
Escalation Status
Evidence References
Timestamp
```

Observability must not independently determine sprint completion.

Sprint progression, escalation, and run completion decisions remain with the harness and governance policy.

---

## Run Log

Where the harness records execution history in:

```text
run-log.md
```

observability supports the fields required by the active baseline and resolved configuration.

At minimum, `run-log.md` should record sufficient information to reconstruct the governed execution history, including:

```text
Run ID
Sprint ID
Contract ID
Iteration Number
Iterations Used
Evaluator Verdict
Findings / Violations Summary or Evidence Reference
Escalation Status
Timestamp
Generator Evidence Reference
Evaluator Evidence Reference
Sprint Contract Reference
```

Where available and required by the resolved configuration, `run-log.md` may also record:

```text
Coverage Result
Other Quality Results
Token Usage
Estimated Cost
```

Detailed deterministic quality evidence remains authoritative in `evaluator-feedback-iteration-N.md`.

Observability should summarize or reference that evidence rather than duplicate the complete Evaluator report.

Prior `run-log.md` entries must not be overwritten. Where the harness supports appending or preserving iteration entries in a single `run-log.md` file, existing entries must remain unchanged when new iteration or sprint entries are added.

Runtime evidence filenames and paths remain owned by the harness baseline and resolved configuration. This skill does not redefine them.

---

## Available Telemetry

Observability records only telemetry that is actually available from harness execution metadata or canonical evidence.

Examples include:

```text
Iteration Number
Iterations Used
Evaluator Verdict
Findings Count
Escalation Status
Token Usage (when available)
Estimated Cost (when available)
Timestamp
```

When a metric is unavailable, record:

```text
N/A
```

Observability must not:

- infer token usage;
- estimate cost without an available source;
- fabricate performance metrics;
- fabricate quality trends;
- convert semantic observations into unsupported quantitative metrics;
- assign numeric values to qualitative outcomes; or
- represent expected behavior as observed behavior.

Quality trends may be recorded only when they can be derived from actual evidence across preserved iterations or runs. Trend statements without supporting evidence must not be recorded.

---

## Timestamps

Observability records timestamps derived from harness execution metadata where available.

Timestamp formatting follows the resolved harness configuration.

This skill does not define:

```text
Time Zone
Date/Time Format
Clock Source
Scheduling Mechanism
```

Feature-specific or application-specific time behavior remains outside this skill.

---

## Escalation Recording

Observability records escalation status when the harness has determined escalation.

Canonical escalation evidence, when produced, is referenced as:

```text
escalation.md
```

For a recorded escalation, observability captures:

```text
Run ID
Sprint ID
Contract ID
Iteration
Final Evaluator Verdict
Escalation Status
Escalation Evidence Reference
Timestamp
```

Observability must not:

- initiate escalation policy;
- change the configured iteration limit;
- authorize another Generator iteration;
- resolve the escalation; or
- modify escalation evidence.

---

## Evidence Preservation

Observability preserves the relationships among canonical execution artifacts.

The expected evidence relationship is:

```text
resolved-config.yaml
spec.md
   ↓
sprint-N-contract.md
   ↓
generator-summary-iteration-1.md
   ↓
evaluator-feedback-iteration-1.md
   ↓
generator-summary-iteration-2.md (if applicable)
   ↓
evaluator-feedback-iteration-2.md (if applicable)
   ↓
run-log.md
   ↓
escalation.md (if applicable)
```

The harness determines output and archive locations.

This skill does not assume ownership of creating or relocating run-level artifacts such as:

```text
resolved-config.yaml
spec.md
```

unless the harness explicitly assigns that responsibility.

Observability records references to these artifacts rather than modifying them.

---

## Immutability

Observability must preserve auditability.

Do not:

- overwrite prior iteration records;
- alter Generator evidence;
- alter Evaluator evidence;
- alter escalation evidence;
- delete prior evidence;
- rewrite historical `run-log.md` entries;
- change previously recorded verdicts; or
- retroactively modify recorded metrics.

Corrections must be recorded as new evidence rather than as modifications to prior evidence.

Where the resolved configuration defines an immutable review archive, existing entries within that archive must remain unchanged after they are recorded.

---

## Downstream Evidence Use

Observability evidence supports governance traceability for downstream lifecycle activities.

The relationship is:

```text
Observability Evidence
   ↓
Audit / Traceability
```

CI/CD remains an independent validation activity:

```text
Committed Revision
   ↓
CI/CD Independent Revalidation
```

Observability evidence does not replace CI/CD verification.

Deployment, runtime validation, rollback, and reflection may reference governance evidence produced during the harness run, but this skill does not participate in those activities.

Deployment and rollback evidence remain owned by:

```text
DEPLOYMENT.md
```

Reflection remains owned by:

```text
REFLECTION.md
```

---

## Context Isolation

Observability requires only the context needed to record and reference governance-observability evidence.

Do not require:

- the complete application source tree;
- unrelated domain packs;
- unrelated technology packs;
- Generator internal reasoning beyond the current iteration evidence;
- Evaluator internal reasoning beyond the current iteration evidence;
- deployment evidence;
- rollback evidence;
- reflection content; or
- an undefined persisted Run State artifact.

Harness execution metadata is authoritative for run, sprint, contract, iteration, verdict, and escalation state.

---

## Responsibility Boundaries

This skill supports Monitor reasoning only.

It does not authorize any agent to:

```text
Plan Work
Generate Implementation
Modify Tests
Perform Remediation
Modify Contracts
Modify Configuration
Modify Governance Policy
Change Verdicts
Change Verdict Thresholds
Change Scoring Weights
Change Hard Gates
Change Coverage Thresholds
Change Iteration Limits
Authorize Retries
Initiate Escalation
Emit Orchestration Statuses
Execute CI/CD
Deploy
Perform Runtime Validation
Rollback
Reflect
```

Monitor records governance-observability evidence.

The harness controls downstream routing.

---

## Configuration Ownership

This skill must not duplicate authoritative values owned elsewhere.

The following remain configuration- or governance-owned:

```text
Application Paths
Technology Stack
Build Commands
Test Commands
Coverage Commands
Coverage Thresholds
Static Analysis Commands
Architecture Verification Commands
Security Commands
Hard Gates
Scoring Dimensions
Scoring Weights
PASS Threshold
CONDITIONAL_PASS Threshold
Fail-Closed Policy
maxIterations
Approval Commands
Output Paths
Review Paths
Deployment Controls
Rollback Controls
```

This skill defines:

```text
Reusable Governance-Observability Guidance
```

Configuration defines:

```text
Applicable Runtime Values
```

Governance policy defines:

```text
Evaluation and Decision Policy
```

Agent definitions and `CLAUDE.md` define:

```text
Orchestration and Routing
```

Evaluator evidence remains authoritative for:

```text
Deterministic Quality Results
```

This skill supports:

```text
Recording
Referencing
Preserving
```

authoritative evidence in support of audit, traceability, and reusability.
