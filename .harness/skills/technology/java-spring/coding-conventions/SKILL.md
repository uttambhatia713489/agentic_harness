# Java/Spring Coding Conventions

## Purpose

Provide reusable Java/Spring coding conventions for the harness agents that consume technology-implementation guidance: Generator and, where applicable, Evaluator.

This skill applies when the active technology profile identifies Java/Spring as the technology stack.

It focuses on code-level conventions used to implement approved sprint contracts.

It does not encode:

- application/domain business rules;
- application/domain architecture rules;
- REST/API design specifics;
- test-framework or test-behavior rules;
- governance thresholds;
- scoring or verdict rules;
- iteration limits;
- verification commands;
- coverage or static-analysis thresholds;
- security policy thresholds;
- deployment or rollback controls; or
- harness orchestration.

Those concerns remain owned by the applicable application/domain skills, the active technology profile, the sibling technology skills, the governance policy, the harness orchestration, and the deployment lifecycle.

---

## Scope

This skill applies when the resolved technology profile selects Java/Spring.

Conventions must be interpreted together with:

```text
Active Technology Profile
Approved Sprint Contract
Resolved Configuration
Existing Application Source
Existing Application Conventions
Active Application / Domain Rules
Active Application / Domain Architecture Rules
Active Governance Policy
```

Existing application evidence remains authoritative when it conflicts with a general convention described here.

Approved contracts remain authoritative when they define specific behavior applicable to a feature.

---

## Technology Baseline

The reference Java/Spring baseline for this profile is:

```text
Language: Java
Framework: Spring / Spring Boot
```

Exact versions, module identifiers, plugins, libraries, and toolchain choices are resolved from the active technology profile such as:

```text
.harness/config/technologies/java-spring.yaml
```

Where the active baseline confirms:

```text
Java 21
Spring Boot 3.x
```

those values apply to the profile.

This skill does not enumerate specific Java LTS versions, Spring Boot minor versions, plugin identifiers, or library versions.

Resolved technology-profile values remain authoritative.

---

## Coding-Convention Boundary

This skill defines:

```text
Reusable Java/Spring Coding Conventions
Maintainability Principles
Clean-Code Principles
Consistency-with-Existing-Application Rules
Java-Specific Language-Feature Guidance
Spring-Specific Framework-Usage Guidance (at code level)
```

This skill does not define:

```text
Application Architecture (Layering, Modules, Boundaries)
Application Domain Rules
Application Error Contracts
REST / API Design
Testing Framework Choices
Testing Behavior Requirements
Coverage Thresholds
Static Analysis Thresholds
Security Thresholds
Build / Verification Commands
CI/CD Behavior
Observability Recording
Orchestration Statuses
Evaluation Verdicts
```

Those responsibilities remain with the applicable domain/architecture skills, sibling technology skills, governance policy, active configuration, and harness orchestration.

---

## Existing-Application Preservation

Generator must inspect the existing Java/Spring application before applying general conventions.

Prefer:

```text
Existing Package Structure
Existing Naming Conventions
Existing Class-Level Patterns
Existing Dependency-Injection Style
Existing Configuration Patterns
Existing Error-Handling Patterns
Existing Logging Patterns
Existing Framework-Usage Patterns
Existing Utility and Helper Patterns
```

over introducing new conventions.

A new convention must not be introduced merely because it is technically possible or perceived as better.

Convention changes must be captured through:

```text
PROMPT.md
   ↓
Planner Discovery
   ↓
Approved Sprint Contract
```

Generator must not silently introduce large-scale refactoring outside approved scope.

---

## Language-Level Conventions

Java code should follow standard Java language conventions and any conventions established by the existing application.

Reusable guidance:

- prefer explicit, readable code over unnecessary cleverness;
- prefer immutability where the domain naturally supports it;
- prefer well-scoped access modifiers;
- prefer clear method boundaries and single-purpose methods;
- prefer meaningful naming over abbreviations;
- prefer expressive types over primitive obsession where consistent with the application;
- prefer null-safety through explicit design where consistent with the application;
- prefer clear separation of concerns within classes;
- prefer minimal state within classes; and
- prefer deterministic behavior over side-effect-heavy design.

The existing application's language conventions remain authoritative.

This skill does not require specific Java language features (for example, records, sealed types, pattern matching) beyond what the existing application already uses.

---

## Dependency Injection

Prefer constructor injection.

Rationale:

- constructor injection makes required collaborators explicit;
- constructor injection supports immutable dependencies;
- constructor injection supports predictable testability; and
- constructor injection matches common Spring Boot practice.

Avoid field injection for required collaborators unless the existing application already uses field injection consistently.

If the existing application uses another injection style consistently, follow the existing style.

Do not mix injection styles arbitrarily within the same class.

Do not introduce framework-level annotations beyond what the existing application uses.

---

## Immutability and State

Prefer immutable objects for:

```text
Value Objects
DTOs
Configuration Objects
Event Payloads (where allowed by the active domain and technology)
```

where the existing application supports immutability.

Prefer immutability for shared collaborators exposed by beans.

Avoid unnecessary shared mutable state in components managed by the framework.

Where mutability is required, document its necessity through the approved contract or clear inline reasoning.

---

## Naming Conventions

Follow the existing application's naming conventions.

General reusable guidance:

- class names should describe the responsibility of the class;
- interface names should describe the capability they define;
- method names should describe observable behavior;
- variable names should describe the value they hold;
- boolean names should read naturally in conditions;
- constants should be named in a way that describes their meaning; and
- test class/method names should describe the scenario under test.

Do not introduce a new naming convention when the existing application already uses a consistent style.

---

## Package Structure

Follow the existing application's package structure.

General reusable guidance:

- align packages with application/domain responsibilities;
- avoid packages that mix unrelated responsibilities;
- avoid moving classes across packages unless required by the approved contract;
- avoid introducing new top-level packages without justification; and
- respect the boundaries defined by the active application/domain architecture rules.

Structural interaction rules such as layering, cross-module access, integration mechanisms, and read-only boundaries are supplied by the active application/domain architecture context, not by this skill.

---

## Null Safety and Validation

Prefer explicit null-safety patterns consistent with the existing application.

Reusable guidance:

- validate inputs at the appropriate boundary as established by the existing application;
- return well-defined values rather than nulls where practical;
- avoid propagating nullable values silently through internal layers;
- prefer clear input contracts over defensive nulls sprinkled through internal methods; and
- prefer meaningful validation errors over generic failures.

Feature-specific validation requirements must come from the approved sprint contract and applicable application/domain rules.

Application-level exception handling patterns must follow the existing application error contract.

---

## Error Handling

Follow the active application/domain error contract.

Reusable guidance:

- do not introduce raw exception hierarchies that bypass the active error contract;
- do not swallow exceptions;
- do not log-and-return in place of proper error handling;
- do not translate errors in ways that lose meaningful context;
- do not create feature-specific exception hierarchies unless required by the approved contract;
- prefer clear, contextual error information over generic messages; and
- prefer error handling that preserves observability without exposing sensitive detail.

This skill does not define the specific error contract used by any particular application.

The active application/domain rules remain authoritative.

For example, when the active application defines a typed application-error hierarchy, code must integrate with that hierarchy.

The specific hierarchy is not enumerated here.

---

## Logging

Follow the existing application's logging strategy and framework conventions.

Reusable guidance:

- use the application's chosen logging framework consistently;
- log at appropriate levels;
- log actionable, context-rich information;
- avoid excessive logging in tight loops;
- avoid logging sensitive information such as credentials, tokens, personal identifiers, or business information whose exposure is restricted;
- prefer structured logging where the application already uses it;
- avoid replacing the existing logging framework without justification; and
- avoid introducing a parallel logging strategy.

The exact logging framework, formatting, correlation strategy, and log level policy remain application-defined.

---

## Configuration and Secrets

Configuration must be externalized in a way that is consistent with the existing application.

Reusable guidance:

- read configuration through the application's established mechanism;
- avoid hard-coding configurable values;
- avoid environment-specific literals in code;
- avoid duplicating configuration authority across code and configuration files;
- avoid introducing new configuration mechanisms when an existing one already applies;
- never inline secrets, credentials, tokens, or private keys;
- consume secrets through the approved secret-management mechanism;
- avoid logging secrets or writing them into generated artifacts; and
- treat configuration semantics as application-defined.

Feature-specific configuration keys, defaults, and semantics are supplied through Planner discovery and the approved sprint contract, not by this skill.

---

## Concurrency

Where concurrency is required, follow the existing application's concurrency patterns.

Reusable guidance:

- prefer well-defined concurrency boundaries;
- prefer immutable data across concurrent boundaries;
- avoid ad hoc thread creation when the framework or application already provides managed alternatives;
- avoid unbounded parallelism;
- avoid unnecessary synchronization; and
- do not introduce concurrency into paths that were previously sequential without approval.

If the approved contract does not require concurrency, do not introduce it.

---

## Spring Framework Usage

Use Spring features consistently with the existing application.

Reusable guidance:

- use component-scanning, dependency injection, configuration, and lifecycle features consistent with the active version of Spring Boot;
- prefer well-scoped beans over broadly shared mutable beans;
- prefer explicit configuration classes for cross-cutting bean setup where the application already uses them;
- avoid introducing new framework abstractions when existing patterns already satisfy the approved contract;
- avoid unnecessary reliance on framework "magic" that reduces readability;
- avoid mixing multiple approaches for the same responsibility within the same module; and
- avoid framework upgrades or feature migrations outside approved scope.

Specific annotation choices, bean lifecycles, and framework-integration patterns follow the existing application and resolved technology profile.

---

## API Boundary

REST/API design guidance is not part of this skill.

REST endpoint style, versioning, media types, request/response modeling, and API error response structure are defined by:

```text
technology/java-spring/api-design/SKILL.md
```

Where this skill mentions controllers, DTOs, or API-adjacent code, it does so only from a code-quality/maintainability perspective.

---

## Testing Boundary

Test-framework selection, test types, test behavior, coverage, and testing strategy are not part of this skill.

Testing guidance belongs to:

```text
technology/java-spring/testing-strategy/SKILL.md
```

This skill does not require specific test frameworks, mocking approaches, integration-test styles, or coverage numbers.

---

## Build and Verification Boundary

This skill does not define:

```text
Build Tools (e.g., Maven, Gradle)
Build Plugins
Build Commands
Test Commands
Coverage Commands
Coverage Tools (e.g., JaCoCo)
Static Analysis Tools (e.g., Checkstyle, SpotBugs)
Security Verification Tools
CI/CD Behavior
```

Those values are resolved from the active technology profile and governance policy.

Generator executes only configured verification commands; Evaluator applies only configured checks and thresholds.

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

Java/Spring code produced by Generator must preserve whatever architecture is active and approved.

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

Generator must not embed feature-specific business rules into general Java/Spring coding conventions.

---

## Change Minimization

Generator must prefer the smallest change that satisfies the approved sprint contract while respecting these conventions.

Avoid:

- unrelated refactoring;
- unnecessary dependencies;
- framework replacement;
- broad package restructuring;
- speculative abstractions;
- new libraries introduced solely for stylistic reasons;
- naming changes to previously stable code; and
- pattern changes across previously consistent classes.

Existing application conventions take precedence over newly invented Java/Spring patterns unless the approved contract explicitly requires a change.

---

## Planner Usage

Planner may reference this skill when technology conventions materially affect scope, decomposition, or acceptance criteria.

Planner must not:

- own Java/Spring implementation decisions;
- introduce framework choices;
- introduce library choices;
- prescribe specific classes, annotations, or bean patterns; or
- embed technology-specific implementation into the sprint contract beyond what the approved intent requires.

Planner may reference this skill to describe technology-context assumptions used during discovery.

---

## Generator Usage

Generator uses this skill to implement approved Java/Spring code consistently with the existing application and active technology profile.

Generator must:

- follow existing application conventions;
- apply reusable coding principles described here;
- integrate with the active domain/architecture rules;
- integrate with the active error contract;
- integrate with the active logging strategy;
- integrate with the active configuration mechanism;
- integrate with the approved sprint contract; and
- avoid unrelated changes.

Generator must not:

- override active application/domain rules;
- override the active technology profile;
- override the active governance policy;
- introduce structural changes outside approved scope; or
- create parallel patterns that duplicate existing responsibilities.

If implementation requires a material change to conventions, framework usage, error contract, or module structure, Generator must use the applicable contract-change workflow rather than silently modifying the application.

---

## Evaluator Usage

Evaluator uses this skill only for semantic review of Java/Spring coding quality and convention consistency, where applicable and required by the approved sprint contract and active governance policy.

Evaluator may consider:

```text
Consistency With Existing Application Conventions
Adherence to Reusable Coding Principles Described Here
Alignment With Active Application/Domain Rules
Alignment With the Active Technology Profile
Alignment With the Approved Sprint Contract
```

Evaluator must not:

- introduce new conventions during evaluation;
- fail implementation for coding style that the existing application already accepts;
- fail implementation for reasonable code choices when the approved contract does not require otherwise;
- override deterministic tool results with subjective coding-style opinions; or
- interpret this skill as a set of hard gates.

Hard gates, coverage thresholds, and scoring rules remain governance-owned.

---

## Monitor Usage

Monitor does not require this skill for governance-observability recording.

Monitor records canonical execution evidence and preserves Evaluator verdicts without interpreting Java/Spring coding conventions.

---

## Responsibility Boundaries

This skill provides reusable Java/Spring coding conventions.

It does not:

- prescribe application architecture;
- prescribe domain rules;
- prescribe REST/API design;
- prescribe testing strategy;
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
Generator Implementation
   ↓
Evaluator Verification
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
Build Tools and Commands
Test Commands
Coverage Commands
Coverage Thresholds
Static Analysis Commands
Static Analysis Thresholds
Architecture Verification Commands
Security Commands and Thresholds
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
Reusable Java/Spring Coding Conventions
```

Sibling technology skills define:

```text
API Design (technology/java-spring/api-design)
Testing Strategy (technology/java-spring/testing-strategy)
```

Application/domain skills define:

```text
Application Context, Domain Rules, Architecture Rules, Business Events
```

Configuration defines:

```text
Applicable Runtime Values
```

Governance policy defines:

```text
Evaluation and Decision Policy
```

The approved sprint contract defines:

```text
Feature-Specific Approved Scope
+
Applicable Coding Behavior Beyond These General Conventions
```
