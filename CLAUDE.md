# CLAUDE.md

# Governed Agentic Delivery Harness

**Version:** 2.0  
**Platform:** Claude Code Enterprise  
**Architecture:** Configuration-driven multi-agent delivery harness

---

## 1. Purpose

This repository implements a reusable harness for governed AI-assisted software delivery.

The harness converts feature intent into validated software through:

```text
Feature Request
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
      ├── PASS → Monitor → Archive → Next Sprint
      ├── CONDITIONAL_PASS → Monitor → Archive Recommendations →
      │       Default: Next Sprint
      │       If active governance policy explicitly requires
      │       human disposition for CONDITIONAL_PASS → Human
      │       Disposition before advancing
      └── FAIL → Generator Retry → Max Iterations → Escalation
```

The harness is:

- application-agnostic;
- technology-configurable;
- policy-driven;
- fail-closed;
- auditable; and
- reusable.

StoreOps is the reference application, not part of the reusable orchestration.

---

# 2. Operating Principles

Always follow these principles:

1. **Configuration over hard-coding.**
2. **Contracts over conversational assumptions.**
3. **Feedforward context over corrective prompting.**
4. **Deterministic checks over LLM opinion.**
5. **Evidence over unsupported claims.**
6. **Fail closed when compliance is ambiguous.**
7. **Human approval before implementation.**
8. **Bounded retries over unlimited remediation.**
9. **Preserve every evaluation iteration.**
10. **Application/domain knowledge belongs in the active domain skills.**
11. **Technology knowledge belongs in the active technology skills.**
12. **Agents remain reusable and responsibility-focused.**

---

# 3. Repository Structure

```text
.harness/
├── config/
│   ├── harness.yaml
│   ├── applications/
│   │   └── storeops.yaml
│   ├── technologies/
│   │   └── java-spring.yaml
│   └── policies/
│       └── default-governance.yaml
│
├── agents/
│   ├── planner.agent.md
│   ├── generator.agent.md
│   ├── evaluator.agent.md
│   └── monitor.agent.md
│
├── skills/
│   ├── core/
│   │   ├── architecture-principles/
│   │   ├── sprint-decomposition/
│   │   ├── review-guidelines/
│   │   ├── evaluation-framework/
│   │   └── observability/
│   │
│   ├── technology/
│   │   └── java-spring/
│   │       ├── coding-conventions/
│   │       ├── api-design/
│   │       └── testing-strategy/
│   │
│   └── domains/
│       └── storeops/
│           ├── app-context/
│           ├── domain-rules/
│           ├── architecture-rules/
│           └── business-events/
│
├── templates/
├── checks/
├── output/
└── reviews/
```

---

# 4. Configuration

The authoritative runtime configuration is:

```text
.harness/config/harness.yaml
```

It selects:

```text
Application
Technology
Governance Policy
Feature Prompt
Approval Policy
Output Location
Review Location
Observability
```

Resolve referenced configuration from:

```text
.harness/config/applications/<application>.yaml
.harness/config/technologies/<technology>.yaml
.harness/config/policies/<policy>.yaml
```

Agents must not hard-code application, technology, coverage, build, or governance values that are available from configuration.

Iteration policy (including maximum Generator/Evaluator iterations, retry, and escalation behavior) is owned by the active governance policy, not by `harness.yaml`.

---

# 5. Configuration Precedence

Apply constraints in this order:

```text
1. Mandatory Governance Policy
2. Harness Configuration
3. Application Architecture
4. Technology Configuration
5. Domain Rules
6. Approved Sprint Contract
7. Agent Implementation Choice
```

A lower-precedence source must never weaken a higher-precedence mandatory rule.

---

# 6. Bootstrap

Before invoking Planner:

1. Read `.harness/config/harness.yaml`.
2. Resolve application configuration.
3. Resolve technology configuration.
4. Resolve governance policy.
5. Resolve required skill packs.
6. Validate referenced files and configuration.
7. Generate a unique `RUN_ID`.
8. Create a resolved configuration snapshot.
9. Start Planner.

If mandatory configuration is missing, invalid, or contradictory:

```text
STATUS: CONFIGURATION_ERROR
```

Stop execution.

Do not guess missing mandatory configuration.

---

# 7. Resolved Configuration Snapshot

Create:

```text
.harness/output/<run-id>/resolved-config.yaml
```

Capture the effective:

- application;
- technology;
- governance policy;
- build/verification commands;
- coverage thresholds;
- hard gates;
- maximum iterations;
- selected skills; and
- output/review locations.

Do not include secrets.

---

# 8. Agent Responsibilities

## Planner

```text
Feature Intent → Specification → Sprint Contracts
```

Planner defines intent and does not generate implementation code.

Definition:

```text
.harness/agents/planner.agent.md
```

---

## Generator

```text
Approved Sprint Contract → Code + Tests
```

Generator implements intent and does not determine the final verdict.

Definition:

```text
.harness/agents/generator.agent.md
```

---

## Evaluator

```text
Implementation + Evidence → Independent Verdict
```

Evaluator judges implementation and does not modify application code.

Definition:

```text
.harness/agents/evaluator.agent.md
```

---

## Monitor

```text
Execution Evidence → Governance Record
```

Monitor records execution and does not modify application code.

Definition:

```text
.harness/agents/monitor.agent.md
```

---

# 9. Context Isolation

Load only the context required by the active agent.

Reset context between agents and sprints when configured.

Durable files are the authoritative handoff mechanism.

## Planner

Load:

```text
Feature Prompt
Application Configuration
Core Architecture Principles
Sprint Decomposition
Domain App Context
Domain Rules
Domain Architecture Rules
Business Events
```

## Generator

Load:

```text
Approved Sprint Contract
Resolved Configuration
Core Architecture Principles
Domain Skills
Technology Coding Conventions
API Design
Testing Strategy
Current Evaluator Feedback (when retrying)
```

## Evaluator

Load:

```text
Approved Sprint Contract
Changed Source Files
Changed Tests
generator-summary-iteration-N.md
Resolved Configuration
Architecture Principles
Review Guidelines
Evaluation Framework
Domain Architecture Rules
Governance Policy
Technology Configuration
```

## Monitor

Load:

```text
Approved Sprint Contract
generator-summary-iteration-N.md
evaluator-feedback-iteration-N.md
Resolved Configuration Reference
Harness Execution Metadata (Run ID, Sprint ID, Contract ID, Current Iteration, Iterations Used, Current Sprint Status, Timestamps, Token Usage/Estimated Cost when available)
Observability Configuration
Observability Skill
```
Monitor must not depend on an undefined persisted Run State artifact. Canonical durable evidence and harness-provided execution metadata are authoritative.

Do not automatically load:

```text
DESIGN_BRIEF.md
IMPLEMENTATION_PLAN.md
DEPLOYMENT.md
REFLECTION.md
```

unless the current task explicitly requires them.

---

# 10. Planner Workflow

Planner must:

1. read the configured feature prompt;
2. inspect active application/domain context;
3. identify impacted components;
4. identify assumptions and open questions;
5. identify architecture constraints;
6. decompose work into independently verifiable sprints;
7. create measurable acceptance criteria;
8. identify required tests and hard gates; and
9. stop for human approval.

Create:

```text
.harness/output/<run-id>/spec.md
.harness/output/<run-id>/sprint-N-contract.md
```

Use templates from `.harness/templates/` when available.

Acceptance criteria must have unique IDs and preferably use:

```text
GIVEN
WHEN
THEN
```

Planner completion:

```text
STATUS: AWAITING_APPROVAL
```

No implementation may begin before approval.

---

# 11. Human Approval

The approval command is defined by configuration.

Default:

```text
APPROVED
```

On approval:

1. freeze the approved specification;
2. freeze approved sprint contracts;
3. record approval;
4. start the first approved sprint.

A material contract change requires re-approval.

---

# 12. Generator Workflow

For each approved sprint, Generator must:

1. load the approved contract;
2. inspect existing application patterns;
3. load active domain and technology skills;
4. implement only approved scope;
5. preserve architecture boundaries;
6. generate/update required tests;
7. execute configured verification commands;
8. self-assess every acceptance criterion; and
9. create `generator-summary-iteration-N.md` for the current iteration.

Generator must not:

- expand scope without approval;
- weaken governance rules;
- disable quality gates;
- introduce unrelated refactoring;
- introduce an unconfigured technology stack; or
- silently resolve material ambiguity.

If a material contract change is required:

```text
STATUS: CONTRACT_CHANGE_REQUIRED
```

Stop for human review.

Generator completion:

```text
STATUS: READY_FOR_EVALUATION
```

---

# 13. Generator Evidence

`generator-summary-iteration-N.md` must include:

```text
Run ID
Sprint ID
Contract ID
Iteration
Acceptance Criteria Self-Assessment
Files Added/Modified
Tests Added/Modified
Commands Executed
Build Result
Test Result
Coverage Result
Static Analysis Result
Known Limitations
Risks
Assumptions Used
```

---

# 14. Evaluator Workflow

Evaluator independently verifies Generator output.

Do not accept Generator self-assessment without evidence.

Execute deterministic checks before semantic review.

Preferred order:

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

Verification commands and thresholds must come from resolved configuration.

---

# 15. Hard Gates

Hard gates are defined by the active governance policy.

Typical gates include:

```text
Build
Tests
Coverage
Architecture
Module Boundaries
Integration Mechanism
Error Contract
Security
Governance Evidence
```

Any enabled hard-gate failure results in:

```text
VERDICT: FAIL
```

A weighted score must never override a failed hard gate.

---

# 16. Application-Specific Rules

Application-specific architecture rules come from:

```text
Active Domain Architecture Skill
        +
Existing Application Evidence
        +
Approved Sprint Contract where feature-specific

```

For the StoreOps reference profile these may resolve to:

```text
Controller → Service → Repository

Cross-Module Repository Access = Prohibited

Cross-Module Reads = Service Only

Cross-Module Side Effects = Established Business Event, Approved Application Interface, Scheduled Processing, or Another Existing StoreOps Pattern (mechanism is not automatically Event Bus; it is determined through Planner discovery and approved via the sprint contract)

Reports = Read Only

Error Contract = AppError
```

These are StoreOps reference rules, not universal harness rules. See domains/storeops/architecture-rules/SKILL.md for the authoritative constraint and its full set of possible cross-module mechanisms.

---

# 17. Evaluation Scoring

Evaluation dimensions and thresholds come from the active governance policy.

Example:

```text
Architecture and Contract Compliance     40%
Functional Correctness and Testing       40%
Maintainability and Governance           20%
```

Weights must total:

```text
100%
```

Invalid weights result in:

```text
STATUS: CONFIGURATION_ERROR
```

---

# 18. Verdict Logic

Apply configured thresholds.

Typical behavior:

```text
Any Hard Gate Failure
        ↓
FAIL

Any Mandatory Acceptance-Criterion Failure
        ↓
FAIL

Ambiguous Mandatory Evidence under fail-closed policy
        ↓
FAIL
```

Otherwise:

```text
Score >= Pass Threshold
        ↓
PASS
```

```text
Conditional Threshold <= Score < Pass Threshold
        ↓
CONDITIONAL_PASS
```

```text
Score < Conditional Threshold
        ↓
FAIL
```

Verdict is owned by Evaluator.

Routing is owned by the harness.

`CONDITIONAL_PASS` routes to Monitor and Archive by default. If the active governance policy explicitly requires human disposition for `CONDITIONAL_PASS`, the harness routes to human review before advancing.

---

# 19. Fail-Closed Evaluation

Evaluator must not guess.

If mandatory compliance cannot be established:

```text
CHECK_STATUS: AMBIGUOUS
```

Record:

```text
RULE
FILES_INSPECTED
MISSING_EVIDENCE
REQUIRED_ACTION
```

When governance policy specifies:

```text
failClosed: true
```

then:

```text
AMBIGUOUS = FAIL
```

---

# 20. Evaluator Findings

Every failed finding must contain:

```text
RULE
CHECK
FILE
LINE
OBSERVED
EXPECTED
REMEDIATION
```

Generic feedback such as:

```text
Fix architecture.
```

is prohibited.

Feedback must be actionable.

---

# 21. Evaluator Output

Create:

```text
evaluator-feedback-iteration-N.md
```

for the current iteration, containing:

```text
Run ID
Sprint ID
Contract ID
Iteration
Hard Gate Results
Acceptance Criteria Results
Evaluation Scores
Findings
Verdict
Required Remediation
```

Supported verdicts:

```text
PASS
CONDITIONAL_PASS
FAIL
```

---

# 22. Retry and Remediation

On:

```text
VERDICT: FAIL
```

if another iteration is available:

1. preserve current evidence;
2. increment iteration;
3. pass the prior `evaluator-feedback-iteration-(N-1).md` to Generator;
4. correct only failed/impacted areas;
5. rerun verification;
6. invoke Evaluator again.

Never overwrite previous iteration evidence.

Use:

```text
generator-summary-iteration-1.md
evaluator-feedback-iteration-1.md

generator-summary-iteration-2.md
evaluator-feedback-iteration-2.md
```

---

# 23. Maximum Iterations

Maximum iterations are read from the active governance policy, not from `harness.yaml`.

Example (in the active governance policy):

```yaml
iteration:
  generatorEvaluator:
    maxIterations: 3
```

Do not hard-code the value in agent definitions.

Do not duplicate this value in `harness.yaml`.

---

# 24. Escalation

If the sprint still fails after the configured maximum iterations:

```text
STATUS: ESCALATED
```

Create:

```text
.harness/output/<run-id>/escalation.md
```

containing:

```text
Run ID
Sprint ID
Contract ID
Iterations Used
Failed Hard Gates
Failed Acceptance Criteria
Files and Lines
Attempted Remediations
Residual Risk
Open Questions
Decision Required
```

Stop autonomous execution.

Human intervention is required.

---

# 25. Monitor

Monitor executes after every concluded evaluation outcome
(PASS, CONDITIONAL_PASS, or FAIL leading to ESCALATED).

Record:

```text
Run ID
Sprint ID
Contract ID
Iteration Number
Evaluator Verdict
Findings Summary or Reference
Available Quality Evidence (Coverage Result, Architecture Violations, Quality Violations, when available)
Escalation Status
Timestamp
Generator Evidence Reference
Evaluator Evidence Reference
```

When the harness determines that the sprint has concluded, additionally record the final sprint outcome:
```text
Iterations Used
Final Evaluator Verdict
Escalation Status
Evidence References
Timestamp
```

Create:

```text
run-log.md
```

Monitor does not independently determine evaluation outcomes, escalation policy, or sprint completion — those are determined by Evaluator and the harness. See .harness/agents/monitor.agent.md for the full agent definition.

---

# 26. Governance Archive

Archive completed evidence under:

```text
.harness/reviews/<run-id>/
```

Recommended structure:

```text
.harness/reviews/<run-id>/
├── resolved-config.yaml
├── spec.md
│
├── sprint-1/
│   ├── sprint-1-contract.md
│   ├── generator-summary-iteration-1.md
│   ├── evaluator-feedback-iteration-1.md
│   ├── generator-summary-iteration-2.md
│   ├── evaluator-feedback-iteration-2.md
│   └── run-log.md
│
├── sprint-2/
│   ├── sprint-2-contract.md
│   ├── generator-summary-iteration-1.md
│   ├── evaluator-feedback-iteration-1.md
│   └── run-log.md
│
└── escalation.md   (only when escalation occurs)
```

When configured as immutable, previous review evidence must never be rewritten.

---

# 27. Traceability

Maintain end-to-end traceability:

```text
Feature Prompt
      ↓
Resolved Configuration
      ↓
Specification
      ↓
Sprint Contract
      ↓
Acceptance Criteria
      ↓
Source Code + Tests
      ↓
Generator Summary
      ↓
Evaluator Feedback
      ↓
Run Log
      ↓
CI/CD Evidence
      ↓
Deployment Evidence
```

Run, sprint, contract, and acceptance-criterion identifiers must remain consistent.

---

# 28. Templates

Use templates from:

```text
.harness/templates/
```

when available.

Recommended templates:

```text
spec.template.md
sprint-contract.template.md
generator-summary.template.md
evaluator-feedback.template.md
run-log.template.md
escalation.template.md
```

Templates define mandatory structure.

Agents populate content and may add useful sections, but must not remove mandatory fields.

Runtime evidence uses canonical iteration-specific names such as `generator-summary-iteration-N.md` and `evaluator-feedback-iteration-N.md`. Generic filenames apply only to templates.

---

# 29. Executable Checks

Prefer executable checks over LLM judgment where such checks are configured.

Checks are conceptually organized under:

```text
.harness/checks/
├── architecture/
├── quality/
├── coverage/
└── security/
```

These directories currently exist as empty extension points. No executable adapters currently exist under `.harness/checks/*`.

Configured deterministic checks may run as:

- direct technology-profile commands or tools; or
- future adapters placed under the appropriate `.harness/checks/*` subdirectory.

Examples of check categories:

```text
Architecture dependency tests
Layering tests
Coverage verification
Static analysis
Dependency scanning
Secret scanning
```

Evaluator must use configured check results as evidence.

---

# 30. LLM Evaluation Boundary

Use LLM evaluation only where deterministic tools are insufficient.

Examples:

- whether implementation satisfies business intent;
- whether tests meaningfully verify business behavior;
- whether an event contains sufficient consumer context;
- whether remediation guidance is actionable; and
- whether semantic architectural coupling exists.

LLM judgment must never override deterministic hard-gate failures.

---

# 31. Security

Agents must not:

- expose secrets;
- commit credentials;
- log access tokens;
- commit private keys;
- disable security checks without approval; or
- fabricate security evidence.

Represent secrets as:

```text
<SECRET>
```

or reference the approved secret-management mechanism.

---

# 32. Change Minimization

Generator must prefer the smallest change satisfying the approved contract.

Avoid:

- unrelated refactoring;
- unnecessary dependencies;
- framework replacement;
- speculative abstractions;
- broad package restructuring; and
- changes outside approved scope.

Existing application conventions take precedence over newly invented patterns unless the approved contract explicitly requires architectural change.

---

# 33. Configuration vs Skills

## Configuration defines

```text
WHAT is active
WHAT value applies
WHAT policy applies
```

Example:

```yaml
application: storeops
technology: java-spring
governancePolicy: default-governance
```

## Skills define

```text
HOW agents should reason and act
```

Example:

```text
Generate business-rule tests sufficient to satisfy
the configured quality thresholds.
```

Avoid duplicating configurable values inside skills.

---

# 34. Reusability

A new application should normally require only:

```text
.harness/config/applications/<application>.yaml
```

and the active domain skills under:

```text
.harness/skills/domains/<domain>/
```

A new technology stack should normally require only:

```text
.harness/config/technologies/<technology>.yaml
```

and:

```text
.harness/skills/technology/<technology>/
```

The following should remain reusable:

```text
CLAUDE.md
planner.agent.md
generator.agent.md
evaluator.agent.md
monitor.agent.md
core skills
handoff templates
orchestration protocol
```

---

# 35. Workflow States

Supported states:

```text
INITIALIZING
CONFIGURATION_ERROR
PLANNING
AWAITING_APPROVAL
APPROVED
GENERATING
READY_FOR_EVALUATION
EVALUATING
RETRY_REQUIRED
PASS
CONDITIONAL_PASS
ESCALATED
COMPLETED
```

---

# 36. Machine-Readable Markers

Planner:

```text
STATUS: AWAITING_APPROVAL
```

Generator:

```text
STATUS: READY_FOR_EVALUATION
```

Evaluator:

```text
VERDICT: PASS
```

or:

```text
VERDICT: CONDITIONAL_PASS
```

or:

```text
VERDICT: FAIL
```

Retry:

```text
STATUS: RETRY_REQUIRED
```

Escalation:

```text
STATUS: ESCALATED
```

Configuration failure:

```text
STATUS: CONFIGURATION_ERROR
```

Successful completion:

```text
STATUS: COMPLETED
```

---

# 37. Orchestration Algorithm

```text
START

Load harness.yaml
      ↓
Resolve application
      ↓
Resolve technology
      ↓
Resolve governance policy
      ↓
Validate configuration
      ↓
Create RUN_ID + resolved-config.yaml
      ↓
Invoke Planner
      ↓
Generate spec + sprint contracts
      ↓
STATUS: AWAITING_APPROVAL
      ↓
STOP

On APPROVED:

FOR EACH approved sprint

    iteration = 1

    Generator
        ↓
    Evaluator
        ↓
    ┌─────────────────────────────────────────────┐
    │ PASS                                        │
    │      ↓                                      │
    │ Monitor                                     │
    │      ↓                                      │
    │ Archive                                     │
    │      ↓                                      │
    │ Next Sprint                                 │
    │                                             │
    │ CONDITIONAL_PASS                            │
    │      ↓                                      │
    │ Monitor                                     │
    │      ↓                                      │
    │ Archive Recommendations                     │
    │      ↓                                      │
    │ If active governance policy explicitly      │
    │ requires human disposition for              │
    │ CONDITIONAL_PASS → Human Disposition        │
    │ before advancing                            │
    │      ↓                                      │
    │ Otherwise → Next Sprint                     │
    └─────────────────────────────────────────────┘

    FAIL
      ↓
    Preserve Evidence
      ↓
    iteration < maxIterations?
      │
      ├── YES → Generator Retry
      │
      └── NO → Escalate → STOP

When all approved sprints complete:

STATUS: COMPLETED
```

Maximum iteration count is resolved from the active governance policy.

---

# 38. StoreOps Reference Run

For the capstone demonstration, configuration resolves approximately to:

```text
Application:
StoreOps

Technology:
Java 21 + Spring Boot 3.x

Testing:
JUnit 5 + MockMvc

Build:
Maven

Coverage:
JaCoCo

Static Analysis:
Checkstyle + SpotBugs

Architecture:
Modular Monolith

Cross-Module Repository Access:
Prohibited

Cross-Module Reads:
Service Only

Cross-Module Side Effects:
Event Bus Only

Error Contract:
AppError

Reports:
Read Only
```

The demonstration feature is supplied through:

```text
PROMPT.md
```

and currently represents:

```text
SLA Breach Alerting
```

StoreOps and SLA Breach Alerting are demonstration workloads.

They are not embedded into the reusable harness architecture.

The SLA Breach Alerting implementation mechanism (for example, whether the feature uses `SLA_BREACH`, the StoreOps event bus, or another approved integration mechanism) is Planner-discovered from the active StoreOps context and approved through the sprint contract. It is illustrative, not mandated by this file.

---

# 39. CI/CD Relationship

The harness precedes and complements CI/CD; it does not replace it.

Harness Verdict
    ↓
Git Commit / Pull Request
    ↓
CI Pipeline
    ├── Build
    ├── Tests
    ├── Coverage
    ├── Static Analysis
    ├── Architecture Checks
    └── Security Checks
    ↓
Deployment

CI independently revalidates the committed revision.
Harness evidence does not replace CI/CD enforcement.

---

# 40. Deployment and Rollback Boundary

Generator / Evaluator failure → Remediation / Retry (harness responsibility).
Deployment / Runtime failure → Rollback (deployment responsibility, DEPLOYMENT.md).

Rollback must not substitute for failed pre-deployment governance.

---

# 41. Definition of Success

A run is successful when:

- configuration resolves successfully;
- Planner produces explicit contracts;
- human approval occurs before implementation;
- Generator implements only approved scope;
- Evaluator independently verifies implementation;
- mandatory hard gates pass;
- acceptance criteria pass;
- retries remain within configured limits;
- evidence is preserved;
- Monitor records the outcome; and
- traceability exists from feature prompt through final evidence.

Final successful state:

```text
STATUS: COMPLETED
```

---

# 42. Final Rule

The harness exists to convert probabilistic AI-assisted development into a controlled engineering process.

Always prioritize:

```text
Architecture Integrity
Correctness
Security
Testability
Deterministic Evaluation
Traceability
Auditability
Reusability
```

over implementation speed.

Never bypass a mandatory governance rule merely to satisfy a functional requirement.
