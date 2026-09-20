# Evaluation Framework

## Purpose

Provide reusable evaluation guidance for the governed agentic delivery harness.

This skill defines the evaluation process and evidence expectations used by Evaluator.

It does not define application-specific architecture rules, technology-specific verification commands, governance thresholds, scoring weights, verdict thresholds, coverage thresholds, hard-gate values, or iteration limits.

Those values are resolved from the active application, domain, technology, and governance configuration.

---

## Evaluation Principles

Evaluation must be:

- independent of Generator self-assessment;
- evidence-based;
- deterministic where tooling can decide the outcome;
- semantic only where deterministic checks cannot decide the outcome;
- traceable to the approved sprint contract;
- governed by the active governance policy; and
- fail-closed where configured for mandatory checks.

Generator self-assessment is informational evidence only.

Evaluator independently determines compliance and the final verdict.

---

## Evaluation Inputs

Evaluation is based on:

```text
Approved Sprint Contract
Current Implementation
Current Tests
generator-summary-iteration-N.md
Resolved Harness Configuration
Applicable Architecture Rules
Applicable Domain Rules
Applicable Technology Verification Configuration
Active Governance Policy
```

The evaluation framework must not assume application-specific or technology-specific rules that are not present in the resolved context.

---

## Evaluation Sequence

Execute deterministic checks before semantic review.

Where applicable and configured, use the following canonical sequence:

```text
1. Build
2. Tests
3. Coverage
4. Static Analysis
5. Architecture Checks
6. Security Checks
7. Acceptance Criteria
8. Semantic Review
9. Governance Evidence
```

A check that is not applicable or not configured must be recorded appropriately rather than silently treated as passed.

Deterministic results take precedence over semantic reasoning.

---

## Deterministic Checks

Deterministic checks may include:

```text
Build
Tests
Coverage
Static Analysis
Architecture Checks
Security Checks
Other Configured Verification
```

Commands, tools, thresholds, and mandatory status are resolved from the active technology configuration and governance policy.

This skill does not define fixed commands or numeric thresholds.

Evaluator must not introduce alternative thresholds or weaken configured checks.

---

## Hard Gates

Hard gates are defined by the active governance policy.

A mandatory hard-gate failure results in:

```text
VERDICT: FAIL
```

A mandatory hard-gate failure must not be overridden by:

```text
Weighted Score
Semantic Judgment
Generator Self-Assessment
Non-Blocking Findings
```

For each hard gate, record sufficient evidence to identify:

```text
Rule / Gate
Configured Requirement
Observed Result
Status
Evidence Reference
```

---

## Acceptance-Criteria Evaluation

Evaluator independently verifies every acceptance criterion defined by the approved sprint contract.

Use the unique acceptance-criterion identifiers established by Planner.

For each criterion, record:

```text
Acceptance Criterion ID
Required Behavior
Verification Method
Observed Result
Status
Evidence Reference
```

Acceptance criteria must be evaluated against observable behavior and supporting evidence.

Generator self-assessment must not be treated as proof that an acceptance criterion has passed.

Mandatory acceptance-criterion failures are handled according to the active governance policy and must not be overridden by weighted scoring.

---

## Semantic Review

Semantic review is used only for concerns that deterministic checks cannot fully decide.

Examples may include:

```text
Business Intent Satisfaction
Test Meaningfulness
Semantic Architectural Coupling
Implementation Consistency
Consumer Context Sufficiency
Remediation Quality
```

Semantic review must:

- remain within the approved sprint contract;
- use available evidence;
- avoid unsupported assumptions;
- avoid redefining business intent;
- avoid introducing new architecture requirements; and
- never override deterministic mandatory failures.

---

## Fail-Closed Evaluation

Fail-closed behavior is controlled by the active governance policy.

When:

```text
failClosed: true
```

and a mandatory check cannot be determined with sufficient evidence, record:

```text
CHECK_STATUS: AMBIGUOUS
```

and identify:

```text
Rule
Check
Evidence Inspected
Missing Evidence
Required Action
```

For mandatory checks:

```text
AMBIGUOUS
   ↓
FAIL
```

when required by the active fail-closed policy.

Evaluator must not infer or guess compliance when mandatory evidence is insufficient.

---

## Weighted Evaluation

Weighted scoring is used only when configured by the active governance policy.

The governance policy owns:

```text
Evaluation Dimensions
Scoring Weights
PASS Threshold
CONDITIONAL_PASS Threshold
Other Verdict Thresholds
```

This skill does not define fixed dimensions, weights, or thresholds.

Weighted scoring occurs only after mandatory hard gates and mandatory acceptance criteria permit scoring.

Where weighted scoring is configured, Evaluator must validate the scoring model according to the active governance policy.

If the configured scoring model is invalid:

```text
CONFIGURATION ERROR
```

Evaluator must not silently normalize, correct, or substitute scoring weights.

The active governance policy remains authoritative for scoring-model validity.

Weighted scoring must never override:

```text
Mandatory Hard-Gate Failure
Mandatory Acceptance-Criterion Failure
Fail-Closed Mandatory Failure
```

---

## Verdicts

Supported evaluation verdicts are:

```text
PASS
CONDITIONAL_PASS
FAIL
```

Verdict thresholds and qualifying conditions are resolved from the active governance policy.

### PASS

`PASS` may be returned only when:

- all mandatory hard gates pass;
- all mandatory acceptance criteria pass;
- no mandatory fail-closed condition remains; and
- configured PASS conditions are satisfied.

### CONDITIONAL_PASS

`CONDITIONAL_PASS` may be returned only when:

- all mandatory hard gates pass;
- all mandatory acceptance criteria pass;
- no mandatory fail-closed condition remains;
- configured CONDITIONAL_PASS conditions are satisfied; and
- only permitted non-blocking findings remain.

`CONDITIONAL_PASS` does not require mandatory human intervention unless the active governance policy explicitly defines one.

Recommendations associated with `CONDITIONAL_PASS` are informational governance evidence and are preserved through the harness evidence flow.

### FAIL

`FAIL` is returned when required by the active governance policy, including mandatory hard-gate, mandatory acceptance-criterion, fail-closed, or configured scoring failures.

Evaluator determines the verdict.

The harness determines retry, escalation, sprint progression, and run completion.

---

## Findings

Evaluation findings must be specific, evidence-based, and actionable.

Every failed finding must include, where applicable:

```text
RULE
CHECK
FILE
LINE
OBSERVED
EXPECTED
REMEDIATION
```

Where a file or line reference is not applicable, record:

```text
N/A
```

and identify the relevant artifact, configuration, command, or evidence source.

Generic findings such as:

```text
Fix architecture
Improve tests
Fix quality
```

are not sufficient.

Remediation guidance must be specific enough for Generator to act without requiring scope expansion or reinterpretation of the approved contract.

---

## Tool Errors vs Compliance Failures

Evaluator must distinguish:

```text
Tool Execution Error
```

from:

```text
Compliance Failure
```

A verification tool that cannot execute must not automatically be represented as proof that the implementation violated the corresponding rule.

Record the actual tool outcome and apply the active governance policy, including fail-closed behavior where applicable.

---

## Implementation-Mechanism Neutrality

Evaluation must verify the implementation mechanism approved through Planner discovery and the approved sprint contract.

This reusable skill must not prescribe:

```text
Event Bus
Specific Domain Event
Scheduler
Direct Service Invocation
Message Broker
Polling
```

or another implementation mechanism.

Application-specific mechanism requirements are supplied by the active application/domain architecture context and approved sprint contract.

Evaluator must not penalize an implementation for failing to use a mechanism that was not required by the approved context.

---

## Evaluation Evidence

Evaluator produces canonical iteration-specific evidence:

```text
evaluator-feedback-iteration-N.md
```

The evidence must correspond to the same iteration as:

```text
generator-summary-iteration-N.md
```

Generic template filenames may be used as templates but must not be treated as runtime evidence.

Evaluation evidence must include:

```text
Run ID
Sprint ID
Contract ID
Iteration
Hard-Gate Results
Acceptance-Criteria Results
Findings
Evaluation Scores (when configured)
Verdict
Required Remediation
Evidence References
Traceability to Approved Sprint Contract
```

Prior iteration evidence must not be overwritten.

---

## Iteration Boundary

Evaluator evaluates the current Generator iteration independently.

When the verdict is:

```text
FAIL
```

Evaluator provides actionable remediation evidence.

Evaluator does not determine:

```text
maxIterations
Retry Authorization
Escalation Policy
```

Those responsibilities belong to the active governance policy and harness orchestration.

When another iteration is authorized, the next evaluation produces:

```text
evaluator-feedback-iteration-(N+1).md
```

without overwriting prior evidence.

---

## Contract Boundary

Evaluator evaluates against the approved sprint contract.

Evaluator must not:

- modify acceptance criteria;
- expand sprint scope;
- redefine business intent;
- introduce new architecture requirements; or
- silently compensate for a materially deficient contract.

If evaluation identifies a material contract deficiency, Evaluator records the deficiency and required contract-change condition as actionable evidence.

Contract changes are handled through the harness re-planning and approval workflow.

---

## Responsibility Boundary

This skill supports Evaluator reasoning only.

It does not authorize Evaluator to:

```text
Plan Work
Generate Implementation
Modify Tests
Perform Remediation
Modify Contracts
Modify Configuration
Modify Governance Policy
Authorize Retries
Own maxIterations
Initiate Escalation
Archive Monitor Evidence
Execute CI/CD
Deploy
Perform Runtime Validation
Rollback
Reflect
```

Evaluator produces evaluation evidence and a verdict.

The harness controls downstream routing.

---

## Configuration Ownership

The following remain configuration-owned and must not be duplicated as authoritative values in this skill:

```text
Build Commands
Test Commands
Coverage Commands
Coverage Thresholds
Static Analysis Commands
Architecture Check Commands
Security Check Commands
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

This skill defines **how evaluation is performed**.

Configuration defines **which rules, commands, thresholds, and policies apply**.
