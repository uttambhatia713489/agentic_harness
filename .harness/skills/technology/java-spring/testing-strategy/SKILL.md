# Java/Spring Testing Strategy

## Purpose

Provide reusable Java/Spring testing guidance for the harness agents that consume technology-implementation guidance: Generator and Evaluator.

This skill applies when the active technology profile identifies Java/Spring as the technology stack.

It focuses on how meaningful tests are designed, added, updated, and evaluated for the approved sprint contract.

It does not encode:

- application/domain business rules;
- application/domain architecture rules;
- StoreOps-specific test scenarios;
- feature-specific coverage numbers, hard gates, or scoring rules;
- general Java coding conventions;
- API design;
- exact tool commands, versions, or thresholds;
- security policy thresholds;
- deployment or rollback controls; or
- harness orchestration.

Those concerns remain owned by the applicable application/domain skills, the active technology profile, the sibling technology skills, the governance policy, the harness orchestration, and the deployment lifecycle.

---

## Scope

This skill applies when the resolved technology profile selects Java/Spring.

Guidance must be interpreted together with:

```text
Active Technology Profile
Approved Sprint Contract
Resolved Configuration
Existing Application Source
Existing Application Tests
Existing Application Test Conventions
Active Application / Domain Rules
Active Application / Domain Architecture Rules
Active Application / Domain Error Contract
Active Governance Policy
```

The existing application's test conventions remain authoritative when they conflict with general guidance described here.

The approved sprint contract remains authoritative when it defines feature-specific test behavior.

Every requirement in this skill is conditional on being applicable to the approved sprint contract, the existing application, the resolved technology profile, or the active governance policy.

---

## Technology Baseline

The reference Java/Spring baseline for testing is derived from the resolved technology profile such as:

```text
.harness/config/technologies/java-spring.yaml
```

Where the resolved profile confirms:

```text
JUnit 5
MockMvc
```

or another supported testing stack, guidance for that stack applies.

Exact framework versions, plugins, libraries, coverage tools, architecture-test tools, and security-check tools are resolved from the technology profile and governance policy.

This skill does not enumerate specific versions or command-line invocations.

Resolved technology-profile values remain authoritative.

---

## Testing Boundary

This skill defines:

```text
Reusable Java/Spring Testing Guidance
Test-Design Principles
Business-Behavior Verification Guidance
Positive- and Negative-Path Guidance
Boundary and Error-Condition Guidance
Deterministic Time-Based Test Guidance
Integration and API-Level Test Guidance
Architecture and Security Test Reference (Where Configured)
Regression Test Guidance
Acceptance-Criteria Traceability Guidance
Test Evidence Expectations
```

This skill does not define:

```text
Application Architecture (Layering, Modules, Boundaries)
Application Domain Rules
Application Error Contracts
API Design (Endpoints, DTOs, Status Codes, Versioning)
General Java Coding Conventions
Build Tools and Commands
Test Runner Commands
Coverage Tools, Commands, or Thresholds
Static Analysis Tools, Commands, or Thresholds
Architecture Test Tools or Rulesets
Security Test Tools or Thresholds
Hard Gates
Scoring Weights
Verdict Thresholds
Fail-Closed Policy
maxIterations
Orchestration Statuses
Deployment or Rollback Controls
```

Those responsibilities remain with the applicable domain/architecture skills, sibling technology skills, governance policy, active configuration, and harness orchestration.

---

## Existing-Application Preservation

Generator must inspect the existing Java/Spring test suite before applying general guidance.

Prefer:

```text
Existing Test Placement and Package Structure
Existing Naming Conventions
Existing Test Types and Style
Existing Assertion Style
Existing Mocking Style
Existing Setup / Teardown Patterns
Existing Configuration and Fixture Patterns
Existing Integration-Test Patterns
Existing API/Controller Test Patterns
Existing Architecture / Static Analysis / Security Test Patterns
```

over introducing new conventions.

A new testing convention must not be introduced merely because it is technically possible or perceived as better.

Convention changes must be captured through:

```text
PROMPT.md
   ↓
Planner Discovery
   ↓
Approved Sprint Contract
```

Generator must not silently introduce large-scale test refactoring outside approved scope.

---

## Test-Design Principles

Tests must verify observable business behavior in addition to code execution.

Reusable guidance:

- verify what the approved behavior produces, not only that code ran;
- verify observable side effects where behavior includes side effects;
- verify error behavior consistent with the active error contract;
- verify boundary and edge conditions relevant to the approved behavior;
- verify negative paths that the approved contract identifies or that are implied by the domain;
- prefer deterministic tests over flaky ones;
- prefer clear arrange/act/assert structure or the equivalent style used by the existing application;
- prefer readable test names that describe the scenario under test;
- prefer minimal, purpose-built test fixtures over shared broad fixtures where the application supports both; and
- prefer explicit assertions over implicit ones.

Tests must not:

- assert only HTTP status codes or generic execution success without validating the underlying business behavior;
- rely on unrelated implementation details that are not part of the approved contract;
- depend on non-deterministic ordering, timing, or environmental state; or
- silently suppress failures.

---

## Acceptance-Criteria Traceability

Tests must be traceable to the approved sprint contract's acceptance criteria.

Reusable guidance:

- structure or name tests so that their relationship to the acceptance-criterion identifier is clear;
- ensure every mandatory acceptance criterion is covered by at least one meaningful test;
- ensure required negative paths are covered by explicit tests;
- ensure regression protection for previously approved behavior affected by the change; and
- ensure that Evaluator can independently confirm which tests address which acceptance criterion.

Generator must include acceptance-criterion self-assessment in its iteration evidence.

Generator self-assessment is informational only.

Evaluator remains independently responsible for verifying acceptance criteria.

---

## Test Types

Applicability of each test type must be determined by:

```text
Approved Sprint Contract
Existing Application Test Conventions
Resolved Technology Profile
Active Governance Policy
```

The categories below are examples of commonly applicable test types when they are supported by the existing application and required by the approved contract.

Not every test type is required for every sprint.

### Unit Tests

Verify isolated component behavior.

Reusable guidance:

- unit tests should exercise the component's approved behavior;
- unit tests should isolate collaborators consistent with the existing application's mocking strategy;
- unit tests should verify business logic, not only method invocation;
- unit tests should avoid duplicating tests provided by frameworks; and
- unit tests should remain fast and deterministic.

### Integration Tests

Verify behavior across collaborating components.

Reusable guidance:

- integration tests should verify collaboration boundaries required by the approved contract;
- integration tests should follow the existing application's integration-test style;
- integration tests should exercise realistic wiring where the application uses realistic wiring; and
- integration tests should remain deterministic.

### API / Controller Tests

Verify HTTP or transport-level behavior where APIs are part of the approved contract.

Reusable guidance:

- controller tests should verify the approved request/response behavior end-to-end at the controller level;
- controller tests should verify HTTP-status semantics only in combination with observable business behavior;
- controller tests should verify request validation as established by the API-design skill and the existing application;
- controller tests should verify API error responses consistent with the active application/domain error contract;
- controller tests should follow existing MockMvc or equivalent conventions where established; and
- controller tests should not replace lower-level tests that verify business logic in isolation.

### Architecture Tests

Verify structural constraints where configured.

Reusable guidance:

- architecture tests should follow the tool selection resolved by the active technology profile;
- architecture tests should target rules established by the active application/domain architecture rules and configured checks;
- architecture tests should not introduce new architecture rules beyond the resolved context; and
- architecture tests should be deterministic and reviewable.

Specific architecture rules and tool selection are supplied by the active application/domain architecture skill and the active technology/governance configuration.

### Security Tests

Verify security-related behavior where configured.

Reusable guidance:

- security tests should follow the security checks defined by the active technology/governance configuration;
- security tests should not introduce ad hoc thresholds or new security policies;
- security tests should verify approved security-related behavior at the appropriate layer; and
- security tests must not include secrets, credentials, tokens, or sensitive data in evidence or logs.

Security tool selection, thresholds, and policies remain governance- and configuration-owned.

### Regression Tests

Verify that previously approved behavior remains intact.

Reusable guidance:

- regression tests should protect established behavior affected by the current change;
- regression tests should be added when the approved contract introduces risk to established behavior;
- regression tests should follow existing patterns; and
- regression tests should not silently redefine established behavior.

### End-to-End Tests

Include end-to-end tests only when the approved contract, existing application, or resolved technology profile requires them and the existing application supports them.

Do not introduce a new end-to-end test framework outside approved scope.

---

## Deterministic Time-Based Testing

Where feature behavior depends on time, tests must be deterministic.

Reusable guidance:

- prefer the existing application's clock or time abstraction where one is established;
- prefer another approved deterministic mechanism when the resolved context provides it;
- avoid relying directly on wall-clock time;
- avoid relying on time zones not established by the resolved context;
- avoid non-deterministic scheduling in tests unless the approved contract requires it;
- verify boundary conditions such as "just before" and "just after" time-based thresholds where applicable; and
- verify that time-dependent negative paths behave correctly.

This skill does not:

- introduce a scheduler;
- select a specific clock implementation;
- specify a time-zone strategy; or
- prescribe a fixed timing strategy for a feature.

Specific time semantics for a feature (for example, grace-period behavior, scheduled evaluation, or timing thresholds) are determined by the approved sprint contract and applicable application/domain context.

---

## Business-Behavior Focus

Tests must verify business behavior, not only technical execution.

Reusable guidance:

- verify observable outputs;
- verify observable side effects;
- verify approved event or notification behavior only when the approved contract defines that behavior;
- verify error and rejection behavior;
- verify negative paths for excluded conditions established by the approved contract;
- verify approved recipient or role-based behavior only where defined by the approved contract; and
- avoid testing internal implementation details when the observable behavior is what the contract requires.

Tests must not:

- rely on assumptions about implementation mechanism (for example, a specific event name or bus) unless the approved contract requires that mechanism;
- assume feature-specific business rules that are not present in the approved contract; or
- promote feature-specific behavior into permanent domain expectations.

Feature-specific business rules remain owned by:

```text
PROMPT.md
   ↓
Planner Discovery
   ↓
Approved Sprint Contract
```

Domain concepts remain owned by the applicable application/domain skills.

---

## Coverage

Coverage measurement, tool selection, thresholds, and enforcement are configuration- and governance-owned.

Reusable guidance:

- where coverage measurement is configured, tests should be structured so that coverage measurement is meaningful;
- coverage numbers alone must not be treated as proof of test quality;
- exercising code paths without asserting observable business behavior does not constitute meaningful coverage;
- coverage must not be optimized by removing or weakening assertions; and
- coverage evidence recorded by Generator must reference the configured coverage tool and results without duplicating tool internals.

This skill does not:

- enumerate specific coverage percentages;
- select a specific coverage tool; or
- enforce hard gates.

Coverage thresholds, hard gates, and enforcement remain governance- and technology-configuration-owned.

---

## Test Data and Fixtures

Prefer test data and fixtures consistent with the existing application.

Reusable guidance:

- follow existing fixture placement and naming;
- prefer purpose-built fixtures for a scenario over unnecessarily broad shared fixtures;
- avoid coupling unrelated tests through shared mutable fixtures;
- avoid fixtures that hide the behavior under test;
- avoid fixtures that depend on external systems unless the existing application already relies on them; and
- avoid persistent state leakage between tests.

Do not introduce a new fixture framework outside approved scope.

---

## Mocks and Test Doubles

Prefer the existing application's mocking strategy and libraries.

Reusable guidance:

- mock only what is necessary to isolate the behavior under test;
- prefer real collaborators when the existing application already uses them at the target level;
- avoid over-mocking that hides real behavior;
- avoid asserting mock interactions when observable outcomes are sufficient;
- avoid mocking classes owned by the framework where the application already exercises them with realistic wiring; and
- avoid inventing new mocking conventions.

Specific mocking libraries and utilities follow the existing application and resolved technology profile.

---

## Assertions

Assertions must be meaningful.

Reusable guidance:

- assert observable outcomes;
- assert semantic correctness, not only structural correctness;
- prefer specific assertions over generic ones;
- prefer positive assertions over broad "not null" or "not empty" checks alone;
- avoid asserting only that a method was called;
- avoid asserting only an HTTP status code;
- verify negative expectations (for example, "no notification sent") where the approved contract requires such behavior; and
- ensure assertion failure messages help diagnose the failure.

---

## Test Isolation and Determinism

Tests must be independent and deterministic.

Reusable guidance:

- test order must not affect outcomes;
- concurrent execution safety must be maintained where applicable;
- do not depend on hidden environment configuration;
- do not depend on host-specific state;
- do not depend on real-time timing where deterministic alternatives exist;
- do not depend on external services unless the existing application already relies on them for tests; and
- do not depend on the presence or absence of unrelated tests.

Flaky tests must be treated as defects.

Generator must not disable, ignore, or skip tests to satisfy verification unless the approved sprint contract explicitly authorizes it.

---

## Logging and Sensitive Data in Tests

Test evidence must not include secrets, credentials, tokens, or sensitive personal information.

Reusable guidance:

- do not print secrets in logs or assertion messages;
- do not persist sensitive information in fixtures;
- do not log full request or response payloads at levels that expose sensitive information; and
- follow the existing application's approach for handling sensitive data in tests.

Configured secret handling remains governance- and configuration-owned.

---

## Test Evidence Expectations

Generator must record test evidence in canonical iteration evidence:

```text
generator-summary-iteration-N.md
```

Test evidence should include, where applicable:

```text
Acceptance-Criterion Self-Assessment (using unique AC IDs)
Tests Added
Tests Modified
Executed Verification Commands (as resolved by configuration)
Build Result
Test Result
Coverage Result (when configured)
Static Analysis Result (when configured)
Architecture Check Result (when configured)
Security Check Result (when configured)
Known Limitations
Risks
Assumptions Used
```

Self-assessment is informational only.

Evaluator independently reruns configured deterministic checks and verifies acceptance criteria without treating Generator self-assessment as authoritative evidence.

Evaluator records its findings in canonical iteration evidence:

```text
evaluator-feedback-iteration-N.md
```

The evaluation framework and governance policy determine verdicts and hard-gate behavior.

Canonical evidence naming and locations are controlled by the harness baseline and resolved configuration and are not redefined by this skill.

---

## Change Minimization

Generator must prefer the smallest set of tests that satisfies the approved sprint contract while respecting these principles.

Avoid:

- unrelated test refactoring;
- unnecessary new test frameworks;
- broad restructuring of existing test packages;
- new mocking libraries introduced solely for stylistic reasons;
- test rewrites for previously stable code; and
- changes to established test conventions.

Existing application test conventions take precedence over newly invented patterns unless the approved contract explicitly requires a change.

---

## Planner Usage

Planner may reference this skill when testing constraints materially affect acceptance criteria or sprint decomposition and the baseline permits it.

Planner must not:

- prescribe specific Java/Spring test frameworks beyond what the resolved technology profile establishes;
- prescribe specific test cases beyond what acceptance criteria naturally imply;
- prescribe coverage numbers or hard gates;
- introduce new test types outside the existing application and approved scope; or
- embed technology-specific test implementation into the sprint contract beyond what the approved intent requires.

Planner may reference this skill to describe technology-context assumptions used during discovery.

---

## Generator Usage

Generator uses this skill to add or update Java/Spring tests required by the approved sprint contract.

Generator must:

- follow existing application test conventions;
- apply reusable test-design principles described here;
- integrate with the active application/domain rules and architecture rules;
- integrate with the active application/domain error contract for error-behavior tests;
- integrate with the active API-design guidance for API-level tests;
- execute configured verification commands;
- record test evidence in canonical iteration evidence;
- self-assess acceptance criteria; and
- avoid unrelated test changes.

Generator must not:

- override active application/domain rules;
- override the resolved technology profile;
- override the active governance policy;
- introduce new test frameworks outside approved scope;
- disable or skip tests to satisfy verification unless explicitly authorized;
- weaken assertions to increase coverage;
- treat its own verification results as a final verdict; or
- assume feature-specific business rules that are not present in the approved contract.

If implementation requires a material change to testing conventions, frameworks, or contracts, Generator must use the applicable contract-change workflow rather than silently modifying the test approach.

---

## Evaluator Usage

Evaluator independently verifies test behavior for the current iteration.

Evaluator must:

- independently execute configured deterministic checks (build, tests, coverage, static analysis, architecture checks, security checks) using the active technology/governance configuration;
- independently verify acceptance criteria against observable evidence;
- assess test meaningfulness where semantic review is required (for example, that tests validate business behavior, negative paths, side effects, and error conditions relevant to the approved contract);
- apply the ordering and precedence defined in the evaluation framework, with deterministic results taking precedence over semantic review;
- apply fail-closed behavior as configured; and
- record findings and the verdict in canonical iteration evidence.

Evaluator must not:

- accept Generator self-assessment as proof;
- invent test requirements absent from the approved contract, active application/domain context, or configured checks;
- introduce new coverage numbers, hard gates, or scoring rules;
- override deterministic hard-gate failures with semantic reasoning;
- fail an implementation for test choices that the existing application already accepts and that the approved contract does not require otherwise;
- introduce new test frameworks or tools; or
- overwrite prior iteration evidence.

Hard gates, coverage thresholds, and scoring rules remain governance-owned.

---

## Monitor Usage

Monitor does not require this skill for governance-observability recording.

Monitor records canonical execution evidence and preserves Evaluator verdicts without interpreting Java/Spring testing semantics.

---

## Architecture Boundary

This skill does not define:

```text
Layering (Controller → Service → Repository)
Module Boundaries
Cross-Module Repository Rules
Cross-Module Read Rules
Cross-Module Side-Effect Mechanisms
Approved Integration Mechanisms
Reports Read-Only Constraints
Error-Contract Semantics
```

Architecture constraints belong to the active application/domain architecture rules.

For the StoreOps reference application, these are supplied by:

```text
domains/storeops/architecture-rules/SKILL.md
```

Tests may verify architecture rules through configured architecture checks. Rule definitions remain architecture-owned.

---

## Domain and Feature Boundary

This skill does not define:

```text
StoreOps Business Rules
SLA Breach Alerting Behavior
Event Names such as SLA_BREACH
Notification Recipients
Escalation Recipients
Configured Grace Periods
Idempotency Semantics
State Tracking Semantics
```

Domain semantics are supplied by:

```text
domains/storeops/domain-rules/SKILL.md
```

Application/module context is supplied by:

```text
domains/storeops/app-context/SKILL.md
```

Business-event knowledge is supplied by:

```text
domains/storeops/business-events/SKILL.md
```

Feature-specific behavior flows through:

```text
PROMPT.md
   ↓
Planner Discovery
   ↓
Approved Sprint Contract
```

Tests must not encode feature-specific rules that are not present in the approved contract or resolved domain context.

---

## Coding-Convention and API-Design Boundary

General Java/Spring coding conventions belong to:

```text
technology/java-spring/coding-conventions/SKILL.md
```

REST/API-design guidance belongs to:

```text
technology/java-spring/api-design/SKILL.md
```

This skill does not redefine coding or API-design rules. It only verifies approved behavior at the appropriate testing layer.

---

## Build, Verification, Coverage, Architecture, and Security Boundary

This skill does not define:

```text
Build Tools and Commands
Test Runner Commands
Coverage Tools, Commands, or Thresholds
Static Analysis Tools, Commands, or Thresholds
Architecture Test Tools or Rulesets
Security Test Tools or Thresholds
CI/CD Behavior
```

Those values are resolved from the active technology profile and governance policy.

Generator executes only configured verification commands. Evaluator independently reruns configured checks and applies configured thresholds and rules.

---

## Governance and Orchestration Boundary

This skill does not define:

```text
Hard Gates
Coverage Thresholds
Scoring Dimensions
Scoring Weights
Verdict Thresholds
Fail-Closed Policy
maxIterations
Retry Policy
Escalation Policy
```

It does not define:

```text
STATUS: AWAITING_APPROVAL
STATUS: READY_FOR_EVALUATION
STATUS: CONTRACT_CHANGE_REQUIRED
STATUS: COMPLETED
PASS
CONDITIONAL_PASS
FAIL
```

Agent definitions, `CLAUDE.md`, and the active governance policy remain authoritative for orchestration and evaluation behavior.

---

## Evidence Boundary

This skill does not define runtime evidence filenames, output paths, or review/archive paths.

Canonical evidence naming and locations remain controlled by the harness baseline and resolved configuration.

This skill does not redefine:

```text
generator-summary-iteration-N.md
evaluator-feedback-iteration-N.md
run-log.md
escalation.md
```

as testing concepts.

---

## Responsibility Boundaries

This skill provides reusable Java/Spring testing guidance.

It does not:

- prescribe application architecture;
- prescribe domain rules;
- prescribe API design;
- prescribe general coding conventions;
- prescribe governance thresholds;
- prescribe evaluation scoring;
- prescribe hard gates;
- prescribe iteration limits;
- prescribe evidence naming;
- prescribe orchestration statuses;
- authorize retries;
- authorize escalation;
- authorize contract changes;
- deploy software;
- perform rollback; or
- perform reflection.

The responsibility flow remains:

```text
PROMPT.md
   ↓
Planner Discovery
   ↓
Approved Sprint Contract
   ↓
Generator Implementation and Tests
   ↓
Evaluator Independent Verification
   ↓
Monitor Evidence Recording
```

---

## Configuration Ownership

This skill must not duplicate authoritative values owned elsewhere.

Configuration- or governance-owned examples include:

```text
Application Paths
Exact Language / Framework / Plugin Versions
Test Frameworks Selection
Build Tools and Commands
Test Runner Commands
Coverage Tools, Commands, and Thresholds
Static Analysis Tools, Commands, and Thresholds
Architecture Test Tools and Rulesets
Security Test Tools and Thresholds
Hard Gates
Scoring Dimensions
Scoring Weights
Verdict Thresholds
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
Reusable Java/Spring Testing Guidance
```

Sibling technology skills define:

```text
Coding Conventions (technology/java-spring/coding-conventions)
API Design (technology/java-spring/api-design)
```

Application/domain skills define:

```text
Application Context, Domain Rules, Architecture Rules, Business Events
```

Configuration defines:

```text
Applicable Runtime Values, Including Selected Test Frameworks and Tools
```

Governance policy defines:

```text
Evaluation and Decision Policy
```

The approved sprint contract defines:

```text
Feature-Specific Approved Scope
+
Applicable Test Behavior Beyond These General Principles
```
