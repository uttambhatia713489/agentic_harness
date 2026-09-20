# DESIGN_BRIEF.md

# Configuration-Driven Governed Agentic Delivery Harness — Design Brief

**Platform:** Claude Code Enterprise  
**Architecture:** Configuration-Driven Multi-Agent Delivery Harness  
**Reference Application:** StoreOps  
**Reference Technology:** Java 21 + Spring Boot 3.x  
**Demonstration Feature:** SLA Breach Alerting  
**Version:** 2.0  

---

## Section A — Intent Decomposition

### A.1 Purpose

The harness converts feature intent into governed, independently evaluated software changes.

The intended lifecycle is:

```text
Feature Prompt
      ↓
Configuration Resolution
      ↓
Planner
      ↓
Human Approval
      ↓
Generator
      ↓
Evaluator
      ↓
Monitor
      ↓
Harness Completion
      ↓
CI/CD Independent Revalidation
      ↓
Deployment / Runtime Validation
      ↓
Reflection
```

StoreOps and SLA Breach Alerting provide the reference demonstration workload. They are not embedded in reusable harness orchestration.

---

### A.2 Four Bounded Agents

The harness uses four responsibility-focused agents:

| Agent | Primary responsibility | Must not do |
|---|---|---|
| Planner | Interpret feature intent and create `spec.md` plus Planner-derived sprint contracts | Generate implementation, self-approve, or evaluate code |
| Generator | Implement one approved sprint contract and produce iteration-specific implementation evidence | Expand scope, issue the final verdict, deploy, or reflect |
| Evaluator | Independently execute configured checks, verify acceptance criteria, and issue a verdict | Modify implementation, remediate findings, or alter contracts |
| Monitor | Record governance-observability evidence in `run-log.md` | Evaluate implementation, change verdicts, authorize retries, or route execution |

Responsibility separation prevents an agent from approving its own interpretation or implementation.

---

### A.3 Configuration-Driven Separation

The harness separates four configuration concerns:

```text
harness.yaml
    → top-level profile selection, approval, context isolation,
      output/review locations, audit/observability enablement,
      and orchestration boundaries

storeops.yaml
    → application identity, type, source root, module inventory,
      and logical StoreOps skill references

java-spring.yaml
    → language/framework, tools, commands, API style,
      testing frameworks, and technology defaults

default-governance.yaml
    → fail-closed behavior, hard gates, thresholds, scoring,
      verdict policy, maxIterations, retry, and escalation
```

No agent or skill may become a competing source of configurable values.

---

### A.4 StoreOps Context Model

The compact StoreOps application profile references four authoritative skills:

```text
domains/storeops/app-context
domains/storeops/domain-rules
domains/storeops/architecture-rules
domains/storeops/business-events
```

Their responsibilities are:

```text
app-context
    → application/module context and responsibilities

domain-rules
    → permanent StoreOps business concepts and semantics

architecture-rules
    → StoreOps structural and integration constraints

business-events
    → established StoreOps business-event knowledge
```

There is no authoritative monolithic `domain-pack-storeops`.

Repository and resolved application evidence remain authoritative for the actual StoreOps implementation.

---

### A.5 Context Isolation

Each agent receives only the context required for its role.

```text
Planner
    Feature Prompt
    Resolved Application Context
    Core Architecture Principles
    Sprint-Decomposition Guidance
    Active Domain Skills
    Relevant Repository Evidence

Generator
    Approved Sprint Contract
    Resolved Configuration
    Active Domain Skills
    Active Technology Skills
    Relevant Source and Tests
    Prior Evaluator Feedback when retrying

Evaluator
    Approved Sprint Contract
    Current Implementation and Tests
    Generator Iteration Evidence
    Resolved Configuration
    Active Architecture/Domain Rules
    Technology Verification Configuration
    Governance Policy
    Evaluation Guidance

Monitor
    Canonical Durable Evidence
    Harness Execution Metadata
```

The design does not depend on an undefined persisted Run State artifact. Durable evidence and harness-provided execution metadata form the authoritative handoff.

---

### A.6 Reference Feature Intent

The demonstration feature is StoreOps SLA Breach Alerting.

The feature prompt establishes the expected business outcomes, including:

- eligibility for overdue HIGH and CRITICAL activities;
- exclusion of LOW, MEDIUM, DONE, and non-overdue activities;
- Department Lead notification;
- Store Manager escalation after a configured grace period when unresolved;
- no escalation when resolved before the configured grace period; and
- discovery of duplicate/idempotency, time-handling, state-tracking, and unresolved semantics where not already established.

These are feature requirements, not permanent reusable harness behavior.

---

### A.7 Planner-Derived Sprint Decomposition

Sprint decomposition is determined by Planner using:

```text
Feature Intent
Repository Evidence
Business Dependencies
Architecture Boundaries
Testability
Risk
```

Any decomposition shown in design documentation is illustrative.

Each sprint contract must be:

- bounded;
- independently testable;
- independently evaluable;
- traceable to feature intent;
- architecture-aware; and
- suitable for Generator/Evaluator execution.

---

### A.8 Implementation-Mechanism Neutrality

The SLA Breach Alerting implementation mechanism is discovered by Planner from the active StoreOps context and repository.

Possible mechanisms may include:

```text
Existing Event
New Approved Event
Service-Mediated Interaction
Scheduled Processing
Another Approved StoreOps Pattern
```

An `SLA_BREACH` event or Event Bus flow is illustrative unless required by permanent StoreOps architecture evidence and approved through the sprint contract.

The mechanism is not selected by Generator or Evaluator.

---

## Section B — Governance Framework

### B.1 Human Approval Boundary

Planner produces:

```text
spec.md
sprint-N-contract.md
```

and stops with:

```text
STATUS: AWAITING_APPROVAL
```

Implementation cannot begin until the configured approval mechanism is satisfied.

A material change to an approved contract requires re-planning and re-approval.

---

### B.2 Configuration Resolution

Before Planner starts, the harness resolves and validates:

```text
harness.yaml
+
selected application profile
+
selected technology profile
+
selected governance policy
```

The harness then generates:

```text
resolved-config.yaml
```

This file is:

- runtime-generated;
- immutable for the run;
- free of resolved secret values;
- traceable to its source profiles; and
- non-authoritative outside the run.

Invalid or contradictory mandatory configuration results in:

```text
STATUS: CONFIGURATION_ERROR
```

---

### B.3 Contract Governance

The approved sprint contract is the implementation boundary.

Generator and Evaluator must not:

- expand scope;
- reinterpret acceptance criteria;
- introduce unsupported architecture assumptions; or
- silently change the approved implementation mechanism.

When a material contract change is required:

```text
STATUS: CONTRACT_CHANGE_REQUIRED
```

The harness returns to re-planning and human approval.

---

### B.4 Architecture Governance

StoreOps architecture constraints are supplied through:

```text
domains/storeops/architecture-rules
+
Existing Application Evidence
+
Approved Sprint Contract where feature-specific
```

The reusable harness does not hard-code StoreOps architecture rules.

Planner applies applicable constraints to sprint contracts, Generator preserves them, and Evaluator verifies them independently.

---

### B.5 Technology and Policy Governance

Technology configuration owns:

```text
Tools
Frameworks
Commands
API Style
Technology Defaults
```

Governance policy owns:

```text
Hard Gates
Thresholds
Scoring
Verdict Policy
Fail-Closed Behavior
maxIterations
Retry Policy
Escalation Policy
```

Skills provide reusable guidance but do not duplicate authoritative configuration values.

---

### B.6 Verdict Semantics

Evaluator issues one of:

```text
PASS
CONDITIONAL_PASS
FAIL
```

Default routing is:

```text
PASS
    ↓
Monitor
    ↓
Evidence Recording
    ↓
Next Sprint / Completion

CONDITIONAL_PASS
    ↓
Monitor
    ↓
Recommendations Recorded
    ↓
Next Sprint / Completion
```

If the active governance policy explicitly requires human disposition for `CONDITIONAL_PASS`, the harness routes to human review before advancing.

For `FAIL`, the harness determines whether another governance-permitted iteration remains or escalation is required.

---

### B.7 Durable Evidence

Canonical runtime evidence includes:

```text
resolved-config.yaml
spec.md
sprint-N-contract.md
generator-summary-iteration-N.md
evaluator-feedback-iteration-N.md
run-log.md
escalation.md (when applicable)
```

Generic filenames such as:

```text
generator-summary.template.md
evaluator-feedback.template.md
```

remain template names only.

Previous iteration evidence must not be overwritten.

---

### B.8 Deterministic Evidence and Empty Check Extension Points

Configured checks may execute through:

- direct technology-profile commands/tools; or
- future adapters under `.harness/checks/*`.

The current directories:

```text
.harness/checks/architecture
.harness/checks/quality
.harness/checks/coverage
.harness/checks/security
```

are empty extension points. Their existence is not evidence that executable checks have been implemented.

---

### B.9 CI/CD Boundary

Successful harness completion precedes CI/CD.

```text
STATUS: COMPLETED
      ↓
Committed Revision
      ↓
CI/CD Independent Revalidation
      ↓
Deployment
```

Harness evidence does not replace independent CI/CD build, test, coverage, static-analysis, architecture, or security validation.

---

### B.10 Deployment and Rollback Boundary

Generator/Evaluator remediation and deployment rollback are separate controls.

```text
Generator / Evaluator Failure
        ↓
Contract-Bounded Remediation
```

```text
Deployment / Runtime Failure
        ↓
Operational Rollback
        ↓
Previous Known-Good Version
```

Rollback does not substitute for failed pre-deployment governance.

---

## Section C — Non-Determinism Strategy

### C.1 Strategy

The harness controls non-determinism by separating:

```text
Deterministic Engineering Checks
        +
Mandatory Acceptance-Criterion Verification
        +
Bounded LLM Semantic Evaluation
        +
Governance-Controlled Verdict and Retry Policy
```

Deterministic checks execute before semantic evaluation. An LLM score, recommendation, or interpretation cannot override:

- a failed mandatory hard gate;
- a failed mandatory acceptance criterion; or
- ambiguous mandatory evidence when `failClosed` is enabled.

The active governance policy defines which gates are mandatory, their thresholds, fail-closed behavior, scoring, retry limits, and escalation policy. The active technology profile defines the tools and commands used to produce deterministic evidence.

If a mandatory gate has no configured executable mechanism, evidence source, or required threshold, the harness must report:

```text
STATUS: CONFIGURATION_ERROR
```

The gate must not be silently downgraded to a soft check.

The `.harness/checks/{architecture,quality,coverage,security}` directories are currently extension points only. No executable adapters are assumed to exist there. Deterministic verification therefore uses configured technology-profile commands/tools and their generated evidence.

---

### C.2 Mandatory Hard-Gate Design

| Hard Gate | Automated Check / Evidence | Failure Mode Prevented | Why It Cannot Be Soft | Pass / Fail Condition |
|---|---|---|---|---|
| **Build Integrity** | Configured Maven verification command, e.g. `mvn clean verify` | Compilation errors, unresolved dependencies, packaging failures, invalid plugin execution | Software that cannot build is not a viable implementation regardless of semantic quality | **PASS:** configured Maven verification exits successfully. **FAIL:** non-zero exit or inability to execute the mandatory build |
| **Automated Tests** | JUnit 5 / MockMvc executed through the configured Maven lifecycle and associated test reports | Functional regression, incorrect business behavior, API/controller failures, broken negative paths | A failing mandatory test is objective evidence of incorrect behavior and cannot be compensated by a weighted score | **PASS:** all applicable mandatory tests pass. **FAIL:** any mandatory test fails or errors |
| **Coverage** | JaCoCo report where configured by the Java/Spring build | Required implementation paths remaining insufficiently tested | Coverage below an approved mandatory threshold is an objective governance breach | **PASS:** measured coverage satisfies the threshold from `default-governance.yaml`. **FAIL:** coverage is below the configured mandatory threshold |
| **Static Code Quality** | Checkstyle and SpotBugs through the configured build/tool lifecycle | Deterministically detectable coding-rule violations, likely defects, and prohibited implementation patterns | Reproducible mandatory violations must not be waived by subjective LLM confidence | **PASS:** configured checks satisfy governance-defined limits. **FAIL:** mandatory violations remain |
| **Architecture Compliance** | Configured ArchUnit tests executed through Maven/JUnit and traced to applicable StoreOps architecture rules | Layer bypass, prohibited dependency direction, cross-module repository access, read-only boundary violations, or other configured structural violations | Architecture violations may remain functionally invisible while creating unacceptable structural coupling | **PASS:** all applicable mandatory ArchUnit rules pass. **FAIL:** any enabled mandatory architecture rule fails |
| **Security** | No security tool is assumed unless explicitly configured in the active technology profile | Configured dependency, secret, vulnerability, or security-policy violations | A mandatory security failure cannot be offset by correctness, coverage, or maintainability scores | If security is mandatory, an executable tool/evidence source must be configured. Otherwise the gate must be explicitly disabled/not applicable. A mandatory gate without a mechanism causes configuration failure |
| **Mandatory Acceptance Criteria** | Approved `sprint-N-contract.md` plus independently verified tests, implementation evidence, and Evaluator assessment by acceptance-criterion ID | Implementation that technically builds but does not satisfy approved business behavior | A failed mandatory requirement means the approved contract has not been fulfilled | **PASS:** every mandatory criterion has sufficient passing evidence. **FAIL:** any mandatory criterion fails |
| **Governance Evidence** | `resolved-config.yaml`, approved sprint contract, `generator-summary-iteration-N.md`, `evaluator-feedback-iteration-N.md`, `run-log.md`, and `escalation.md` where applicable | Missing approval history, untraceable changes, overwritten iterations, or inability to reconstruct the governed run | Missing mandatory evidence makes compliance unauditable regardless of implementation quality | **PASS:** required artifacts and identifiers are present and consistent. **FAIL:** mandatory evidence is missing or contradictory |

A tool being named in `java-spring.yaml` is not by itself proof that a hard gate executed. The corresponding command, test, plugin, report, or other evidence source must actually be available for the active application.

---

### C.3 Fail-Closed Ambiguity

For mandatory checks, Evaluator distinguishes:

```text
COMPLIANCE FAILURE
TOOL EXECUTION ERROR
INSUFFICIENT / AMBIGUOUS EVIDENCE
NOT APPLICABLE
```

When the active governance policy specifies:

```text
failClosed: true
```

then:

```text
Mandatory Check
        +
Insufficient / Ambiguous Evidence
        ↓
CHECK_STATUS: AMBIGUOUS
        ↓
VERDICT: FAIL
```

Evaluator records sufficient diagnostic evidence, including:

```text
RULE
CHECK
FILES / EVIDENCE INSPECTED
MISSING EVIDENCE
OBSERVED CONDITION
EXPECTED CONDITION
REQUIRED REMEDIATION
```

Evaluator must not infer that a mandatory rule passed because a tool was unavailable, a report was missing, or the implementation appeared reasonable.

---

### C.4 Deterministic vs LLM Evaluation Boundary

Deterministic mechanisms decide reproducible engineering facts:

```text
Build outcome
Test outcome
Measured coverage
Static-analysis findings
Architecture-test results
Configured security-check results
Mandatory evidence presence and consistency
```

Bounded LLM evaluation is used only where deterministic tools cannot fully establish semantic adequacy, including:

- whether implementation satisfies approved business intent;
- whether tests meaningfully verify required behavior;
- whether an approved interaction contains sufficient consumer context;
- whether semantic architectural coupling exists beyond structural checks; and
- whether remediation guidance is specific and actionable.

The evaluation sequence is therefore:

```text
Deterministic Checks
        ↓
Mandatory Acceptance Criteria
        ↓
Fail-Closed Decision
        ↓
Bounded Semantic Review
        ↓
Configured Scoring, if enabled
        ↓
PASS / CONDITIONAL_PASS / FAIL
```

Weighted scoring is subordinate to mandatory gates.

A failed mandatory hard gate or mandatory acceptance criterion cannot be converted into `PASS` or `CONDITIONAL_PASS` by a higher aggregate score.

The **Evaluator owns the verdict**.

The **harness owns routing, retry, escalation, human disposition, sprint progression, and run completion**.

---

## Section D — Architectural Decisions

### D.1 Four Bounded Agents

**Decision:** Use four responsibility-focused agents: Planner, Generator, Evaluator, and Monitor.

**Alternatives considered:**

1. One general-purpose agent performing all lifecycle activities.
2. One agent switching roles through conversational prompts.
3. Four agents with explicit responsibilities, context boundaries, and durable handoffs.

**Chosen option:** Four bounded agents.

**Rationale:** Separating planning, implementation, independent evaluation, and evidence recording reduces self-approval risk, limits context pollution, and makes responsibility ownership auditable.

**Governing assumption / dependency:** Role-specific context and canonical handoff artifacts must be maintained between agent stages.

**Trade-offs / consequences:**

- additional agent transitions;
- potentially higher token usage;
- stronger independence, traceability, and failure isolation.

**Trigger to revisit:** Revisit if execution evidence shows that role isolation cannot be maintained reliably or orchestration overhead materially outweighs its governance benefit.

---

### D.2 Configuration-Driven Separation

**Decision:** Separate application, technology, governance, and orchestration configuration.

**Alternatives considered:**

1. Hard-code StoreOps, Java/Spring, thresholds, and retry behavior into agents.
2. Maintain one combined configuration containing all concerns.
3. Resolve separate application, technology, governance, and harness profiles.

**Chosen option:** Separate:

```text
storeops.yaml
java-spring.yaml
default-governance.yaml
harness.yaml
```

**Rationale:** Applications, technologies, and governance policies can evolve independently while reusable agents remain unchanged.

**Governing assumption / dependency:** Configuration resolution must validate references, ownership, mandatory values, and contradictions before Planner execution.

**Trade-offs / consequences:**

- requires explicit configuration resolution;
- invalid configuration must stop execution;
- enables future applications and technology stacks without redesigning the agent workflow.

**Trigger to revisit:** Revisit if profile composition becomes ambiguous or future requirements cannot be represented without duplicating authority.

---

### D.3 Four Authoritative StoreOps Skills

**Decision:** Represent StoreOps knowledge through four authoritative skills:

```text
domains/storeops/app-context
domains/storeops/domain-rules
domains/storeops/architecture-rules
domains/storeops/business-events
```

**Alternatives considered:**

1. One monolithic StoreOps domain pack.
2. Duplicate StoreOps rules across `storeops.yaml`, agents, and prompts.
3. Separate application context, domain rules, architecture rules, and business-event knowledge.

**Chosen option:** Four authoritative StoreOps skills referenced by a compact `storeops.yaml`.

**Rationale:** The separation establishes clear ownership, minimizes duplication, supports role-specific context loading, and prevents feature-specific behavior from becoming permanent application truth.

**Governing assumption / dependency:** The active application profile must correctly resolve the required StoreOps skills.

**Trade-offs / consequences:**

- more artifacts must remain consistent;
- agents may load multiple skills for one feature;
- each knowledge category can evolve independently.

**Trigger to revisit:** Revisit if persistent overlap between the four skills creates ambiguity or another decomposition provides clearer ownership without recreating a monolithic source.

---

### D.4 Deterministic Hard Gates with Bounded LLM Evaluation

**Decision:** Use deterministic checks for reproducible engineering rules and bounded LLM reasoning for semantic concerns.

**Alternatives considered:**

1. LLM-only evaluation.
2. Deterministic tools only.
3. Deterministic hard gates followed by bounded semantic evaluation.

**Chosen option:** Deterministic-first hybrid evaluation.

**Rationale:** Build, tests, coverage, static analysis, and configured architecture rules can be evaluated reproducibly. Business-intent satisfaction, test meaningfulness, semantic coupling, and remediation quality may still require bounded semantic reasoning.

**Governing assumption / dependency:** Every enabled mandatory hard gate must resolve to a concrete tool/command, evidence source, and governance-owned pass condition.

**Trade-offs / consequences:**

- requires executable toolchain configuration;
- some semantic evaluation remains probabilistic;
- LLM scoring cannot override mandatory deterministic failures.

**Trigger to revisit:** Revisit when a semantic check can be made deterministic or evidence shows that a gate has been incorrectly classified.

---

### D.5 Durable File-Based Handoffs

**Decision:** Use durable files as the authoritative agent handoff and audit mechanism.

**Alternatives considered:**

1. Conversational context only.
2. Opaque persisted runtime state.
3. Canonical durable artifacts.

**Chosen option:** Use:

```text
resolved-config.yaml
spec.md
sprint-N-contract.md
generator-summary-iteration-N.md
evaluator-feedback-iteration-N.md
run-log.md
escalation.md
```

where applicable.

**Rationale:** Durable artifacts survive context resets, preserve iteration history, support human review and CI/CD handoff, and make the governed run reconstructable.

**Governing assumption / dependency:** Artifact locations must resolve from configuration and previous iteration evidence must not be overwritten.

**Trade-offs / consequences:**

- additional artifact management;
- stable templates must be maintained;
- substantially stronger traceability and auditability.

**Trigger to revisit:** Revisit if file-based evidence cannot support required scale, concurrency, integrity, or queryability while preserving equivalent auditability.

---

### D.6 Human Approval Before Implementation

**Decision:** Require human approval after Planner produces the specification and sprint contracts and before Generator implementation.

**Alternatives considered:**

1. Fully autonomous planning and implementation.
2. Human approval for every generated code change.
3. Approval of Planner contracts before implementation, with re-approval for material contract changes.

**Chosen option:** Planner approval gate before implementation.

**Rationale:** The highest-value human intervention is confirmation of interpreted intent, assumptions, scope, architecture constraints, and acceptance criteria before code is generated.

**Governing assumption / dependency:** Planner must expose material ambiguity and produce independently reviewable contracts.

**Trade-offs / consequences:**

- introduces a deliberate execution pause;
- reduces autonomous implementation of misunderstood requirements;
- avoids requiring approval for every bounded remediation step.

**Trigger to revisit:** Revisit if contract quality is insufficient for meaningful approval or governance requirements demand additional approval points.

---

### D.7 Bounded Generator/Evaluator Remediation

**Decision:** Limit Generator/Evaluator remediation using governance-owned `maxIterations`, retry policy, and escalation behavior.

**Alternatives considered:**

1. Unlimited autonomous retries.
2. No automated remediation after the first failure.
3. Bounded remediation followed by escalation.

**Chosen option:** Governance-controlled bounded remediation.

**Rationale:** Recoverable defects can be corrected autonomously while preventing uncontrolled token consumption, repeated ineffective changes, scope drift, and non-terminating execution.

**Governing assumption / dependency:** Evaluator findings must be actionable, and `default-governance.yaml` must provide valid iteration and escalation policy.

**Trade-offs / consequences:**

- some recoverable failures may escalate;
- each iteration increases cost and evidence volume;
- autonomous execution has a deterministic stopping condition.

**Trigger to revisit:** Revisit if run evidence shows that the configured limit consistently escalates recoverable defects or permits repeated ineffective remediation.

---

### D.8 Runtime-Generated Resolved Configuration

**Decision:** Generate an immutable `resolved-config.yaml` snapshot for each run.

**Alternatives considered:**

1. Manually maintain an effective configuration file.
2. Allow every agent to independently read live source configuration.
3. Resolve and validate source profiles once and preserve the effective configuration for the run.

**Chosen option:** Runtime-generated, immutable, non-authoritative `resolved-config.yaml`.

**Rationale:** The snapshot records exactly which application, technology, governance policy, skills, commands, thresholds, and artifact locations governed a specific run.

**Governing assumption / dependency:** Configuration resolution must be deterministic, secrets must be excluded, and the snapshot must remain unchanged throughout the run.

**Trade-offs / consequences:**

- requires bootstrap resolution and validation;
- creates an additional evidence artifact;
- prevents configuration drift between Planner, Generator, Evaluator, and Monitor.

**Trigger to revisit:** Revisit if configuration must legitimately change during an active run; such a change should require explicit versioned snapshots or a new run rather than silent mutation.

---

## Section E — Reusability

A new application should require:

```text
New Application Profile
+
New Domain Skills
```

A new technology should require:

```text
New Technology Profile
+
New Technology Skills
```

A new governance model should require:

```text
New Governance Policy
```

The following remain reusable:

```text
CLAUDE.md
Planner
Generator
Evaluator
Monitor
Core Skills
Templates
Orchestration Protocol
```

---

## Section F — Security and Responsible-AI Boundaries

The harness must:

- exclude secrets from generated evidence;
- prevent agents from weakening mandatory governance controls;
- stop on contradictory mandatory configuration;
- preserve human approval for interpreted intent;
- limit autonomous remediation;
- preserve evidence for audit;
- prohibit unsupported compliance claims; and
- distinguish tool failure from compliance failure.

Security checks must not be claimed as executed unless a concrete configured mechanism and evidence source exist.

---

## Section G — Evidence and Traceability

The target traceability chain is:

```text
PROMPT.md
    ↓
resolved-config.yaml
    ↓
spec.md
    ↓
sprint-N-contract.md
    ↓
generator-summary-iteration-N.md
    ↓
evaluator-feedback-iteration-N.md
    ↓
run-log.md
    ↓
CI/CD Evidence
    ↓
DEPLOYMENT.md
    ↓
REFLECTION.md
```

Run, sprint, contract, iteration, and acceptance-criterion identifiers must remain consistent.

---

## Section H — Reflection Boundary

`REFLECTION.md` is completed only after actual evidence exists from:

- harness execution;
- Generator/Evaluator iterations;
- Monitor records;
- CI/CD;
- deployment;
- runtime validation; and
- rollback where applicable.

Expected behavior must not be presented as observed behavior.

Reflection must identify what worked, what failed or remained limited, where human intervention occurred, and how the architecture should evolve based on evidence.

---

## Section I — Deployment State Model

Deployment occurs only after successful harness completion and independent CI/CD validation.

Supported downstream outcomes are:

```text
DEPLOYMENT_VERIFIED
DEPLOYMENT_FAILED
ROLLBACK_VERIFIED
ROLLBACK_FAILED
```

These states are separate from:

```text
PASS
CONDITIONAL_PASS
FAIL
```

which are Evaluator verdicts for sprint implementation.

Deployment rollback restores a previous known-good operational version. It is not part of the Generator/Evaluator remediation loop.