---
name: java-spring-api-design
description: >
  Reusable Java/Spring API-design guidance for implementing or reviewing
  API changes when the resolved technology profile selects Java/Spring.
  Use for REST resource design, HTTP semantics, DTOs, validation, API
  errors, compatibility, pagination, and API-boundary concerns.
user-invocable: false
---

# Java/Spring API Design

**Version:** 2.0  
**Skill Type:** Reusable Technology Guidance  
**Logical Skill ID:** `technology/java-spring/api-design`

---

## 1. Purpose

Provide reusable Java/Spring API-design guidance for harness agents that consume technology implementation guidance:

```text
Generator
Evaluator, where applicable
```

This skill applies when the active technology profile selects Java/Spring.

It focuses on API design at the code and contract boundary for the API style selected by the active technology profile.

It does not define:

- application or domain business rules;
- application architecture or module boundaries;
- application-specific error semantics;
- feature-specific endpoints or workflows;
- general Java coding conventions;
- testing strategy;
- governance thresholds or hard gates;
- verification commands;
- orchestration;
- deployment, rollback, or reflection behavior.

---

## 2. Authoritative Context

Interpret this skill together with:

```text
Active Technology Profile
Approved Sprint Contract
Resolved Configuration
Existing Application Source
Existing API Contracts
Existing Application API Conventions
Active Application / Domain Rules
Active Architecture Rules
Active Application Error Contract
Active Governance Policy
```

Precedence is:

```text
Existing Approved Application Contract
        +
Approved Sprint Contract
        +
Resolved Configuration
        ↓
Reusable Guidance in This Skill
```

Existing application conventions remain authoritative when they conflict with general guidance here.

Generator must not introduce a new API convention merely because it appears technically preferable.

---

## 3. API Style Selection

The API style is selected by the active technology profile, for example:

```text
.harness/config/technologies/java-spring.yaml
```

If the resolved profile specifies:

```text
API Style: REST
```

the REST-specific guidance below applies.

If another supported API style is selected, REST-specific guidance does not apply.

This skill does not independently select:

```text
API Style
Transport
Protocol
Media Type
Serialization Format
```

Those values remain configuration- and application-owned.

---

## 4. Existing-Application Preservation

Before implementing an API change, Generator must inspect the existing API surface.

Prefer existing:

```text
Endpoint Patterns
Resource Naming
HTTP Method Usage
HTTP Status Semantics
Request / Response DTO Structures
Validation Strategy
Error-Response Structure
Versioning Strategy
Pagination / Filtering / Sorting
Serialization and Media-Type Conventions
```

over introducing new patterns.

A material API convention or contract change requires:

```text
Planner Discovery
      ↓
Approved Sprint Contract
      ↓
Generator Implementation
```

Generator must not silently perform broad API refactoring.

---

## 5. REST Resource Design

When REST is active:

- design endpoints around resources rather than implementation actions;
- use resource-oriented naming consistent with the existing API;
- use HTTP methods to express operations;
- avoid unrelated responsibilities in one endpoint;
- avoid RPC-style endpoints when an established resource-oriented pattern already fits;
- use stable and predictable resource identifiers; and
- align resource relationships with the active application/domain model.

Exact:

```text
Paths
Resource Names
Hierarchy
Identifier Format
Casing
```

remain application-defined.

---

## 6. HTTP Semantics

### Methods

When REST is active:

- reads must not unexpectedly modify state;
- unsafe operations must not be exposed as safe methods;
- operations exposed as idempotent must preserve idempotent behavior;
- creation must follow existing application conventions;
- partial versus full update semantics must remain consistent with existing contracts; and
- deletion semantics must follow existing application behavior.

Do not silently change the HTTP method used by an existing endpoint.

### Statuses

HTTP status behavior must remain consistent with the existing application and standard HTTP semantics.

Guidance:

- represent the actual API outcome;
- distinguish client-caused and server-caused failures;
- preserve existing validation/error distinctions;
- avoid assigning unrelated outcomes the same semantics; and
- avoid introducing status behavior inconsistent with related endpoints.

Exact status codes for feature scenarios are determined by:

```text
Existing API Contract
Active Application Error Contract
Approved Sprint Contract
```

This skill does not prescribe feature-specific status codes.

---

## 7. Request and Response DTOs

Use API DTOs consistently with the existing application.

Guidance:

- separate API DTOs from internal domain/persistence models where the application already does so;
- design request DTOs around API input requirements;
- design response DTOs around caller-facing output requirements;
- avoid exposing persistence structures directly;
- avoid mixing input and output responsibilities where separate DTOs are established;
- use domain-aligned field names; and
- preserve compatibility for optional or evolving fields.

Serialization format, naming strategy, and media types remain application- and profile-defined.

---

## 8. Input Validation

Validate API input consistently with the existing application.

Guidance:

- validate structural and syntactic requirements at the API boundary;
- keep semantic business validation in the appropriate application/domain layer;
- provide clear validation outcomes;
- avoid inconsistent duplicate validation across layers;
- avoid silent normalization that changes input meaning;
- reject structurally invalid requests rather than treating them as successful; and
- avoid exposing internal validation implementation details.

Feature-specific validation rules come from:

```text
Approved Sprint Contract
+
Active Application / Domain Rules
```

---

## 9. Spring MVC Boundary Guidance

Where the existing application uses Spring MVC:

- preserve its `@RestController` and request-mapping conventions;
- preserve established HTTP-method mappings;
- preserve established `consumes` and `produces` behavior;
- apply Bean Validation through the application's existing `@Valid` or `@Validated` pattern;
- keep structural request validation at the controller/API boundary;
- delegate business-rule validation to the appropriate application/domain layer;
- preserve configured serialization and content-negotiation behavior; and
- use the application's established `@RestControllerAdvice`, `@ControllerAdvice`, or `@ExceptionHandler` mechanism for exception-to-response mapping.

Conceptually:

```java
@RestController
@RequestMapping("/resources")
class ResourceController {

    @PostMapping
    ResponseEntity<ResourceResponse> create(
            @Valid @RequestBody ResourceRequest request) {
        ...
    }
}
```

This is illustrative only.

Do not introduce these annotations or mechanisms when the existing application uses another supported API model or convention.

---

## 10. API Error Responses

API errors must integrate with the active application error contract.

Guidance:

- preserve the existing error-response structure;
- preserve application/domain error semantics;
- avoid raw exceptions or ad hoc parallel error structures;
- avoid exposing stack traces, secrets, or internal implementation details;
- provide sufficient caller-facing diagnostic context;
- preserve consistent negative-path behavior; and
- use the application's established exception-to-response mapping mechanism.

The exact:

```text
Error Schema
Error Codes
Exception Hierarchy
HTTP Mapping
```

is supplied by:

```text
Active Application Error Contract
Existing Application
Approved Sprint Contract
```

For StoreOps, application-specific error semantics such as `AppError` remain owned by the StoreOps architecture/application context and are not defined by this technology skill.

---

## 11. Serialization and Content Negotiation

Preserve the application's established serialization and content-negotiation behavior.

Where applicable, respect existing:

```text
HttpMessageConverter Configuration
JSON / Serialization Conventions
Content Negotiation
consumes
produces
Media Types
Field Naming
Date / Time Serialization
```

Do not introduce a new serializer, media type, field-naming strategy, or content-negotiation convention solely for a feature unless approved.

---

## 12. Versioning and Backward Compatibility

Follow the existing application's API versioning strategy.

Generator must not silently:

- introduce a new versioning mechanism;
- remove an existing endpoint;
- remove an existing response field;
- change established field semantics;
- change existing HTTP method semantics;
- change established status semantics; or
- introduce another breaking contract change.

Prefer additive and backward-compatible changes where possible.

A required breaking change must flow through:

```text
Planner Discovery
      ↓
Approved Sprint Contract
      ↓
Generator Implementation
```

---

## 13. Pagination, Filtering, and Sorting

Apply pagination, filtering, and sorting only when required by:

```text
Existing Application
Active Technology Profile
Approved Sprint Contract
```

Guidance:

- preserve existing pagination conventions;
- preserve existing filtering and sorting semantics;
- preserve deterministic ordering where required;
- validate API-level query inputs;
- avoid exposing internal persistence/query implementation details; and
- align behavior with existing response DTO conventions.

Exact parameter names and semantics remain application-defined.

---

## 14. Idempotency, Concurrency, and Consistency

Where these concerns are exposed through the API:

- preserve existing idempotency semantics;
- preserve existing concurrency-control mechanisms;
- preserve established conditional-request behavior;
- avoid inventing a new concurrency mechanism when one already exists; and
- do not change transactional or consistency guarantees without approval.

Feature-specific semantics are determined by:

```text
Approved Sprint Contract
+
Active Application / Domain Rules
```

This skill does not independently define an idempotency strategy.

---

## 15. API Security

This skill does not define authentication or authorization mechanisms.

API implementation must:

- preserve the application's existing authentication mechanism;
- preserve existing authorization behavior;
- avoid weakening API security controls;
- avoid exposing secrets, credentials, tokens, or sensitive internal information;
- avoid introducing public endpoints outside approved scope; and
- preserve application/domain role and permission semantics.

Ownership is:

```text
Authentication / Authorization Semantics
    → active application/security context

Security Tools and Verification Commands
    → active technology profile

Security Thresholds
Mandatory Security-Gate Status
Verdict Impact
    → active governance policy
```

Security-relevant contract changes require approval.

---

## 16. API-Boundary Observability

Preserve the application's existing API observability approach.

Guidance:

- follow established logging conventions;
- follow existing metrics/tracing conventions where present;
- do not log secrets, credentials, tokens, or sensitive information;
- avoid logging request/response payloads when doing so exposes protected information;
- preserve structured logging where established; and
- do not introduce a new observability framework outside approved scope.

Harness governance-observability remains separate and is owned by the Monitor and applicable core observability guidance.

---

## 17. Change Minimization

Generator must prefer the smallest API change that satisfies the approved sprint contract.

Avoid:

```text
Unrelated Endpoint Refactoring
Unnecessary DTO Restructuring
Unapproved New Endpoints
Unnecessary Query Parameters
Naming Changes That Break Existing Conventions
Silent Versioning Changes
Silent Breaking Changes
Parallel API Patterns
```

Existing application conventions take precedence over newly invented patterns unless the approved contract explicitly requires a change.

---

## 18. Agent Usage

### Planner

Planner may use this skill when API constraints materially affect discovery, decomposition, compatibility, or acceptance criteria.

Planner must not:

- prescribe implementation-level endpoint paths without evidence;
- prescribe DTO fields unnecessarily;
- prescribe feature-specific status codes without evidence;
- select an API style different from the resolved technology profile; or
- introduce breaking API changes outside approved scope.

### Generator

Generator uses this skill to implement approved Java/Spring API changes.

Generator must:

- inspect and preserve existing API conventions;
- follow the selected API style;
- preserve active application/domain and architecture rules;
- preserve the active error contract;
- preserve backward compatibility unless explicitly approved otherwise; and
- minimize API-surface changes.

If implementation requires a material API contract or convention change:

```text
STATUS: CONTRACT_CHANGE_REQUIRED
```

Generator must not silently alter the contract.

### Evaluator

Evaluator may use this skill for semantic API-design review where applicable.

Evaluator may assess:

```text
Existing API Convention Consistency
Selected API-Style Consistency
HTTP Semantic Consistency
DTO Boundary Consistency
Validation Placement
Error-Contract Integration
Backward Compatibility
Approved Sprint-Contract Alignment
```

Evaluator must not:

- invent new API conventions;
- fail implementation merely for stylistic preference;
- treat this skill as an independent hard-gate source;
- override deterministic results with subjective API-design opinions; or
- introduce new requirements not present in authoritative context.

Hard gates and verdict policy remain governance-owned.

### Monitor

Monitor does not require this skill for governance-observability recording and does not interpret API-design semantics.

---

## 19. Responsibility Boundaries

| Concern | Authoritative Source |
|---|---|
| Application/module context | Active application context skill |
| Domain/business behavior | Active domain-rules skill |
| Architecture/module boundaries | Active architecture-rules skill |
| Business-event semantics | Active business-events skill |
| General Java/Spring coding conventions | `technology/java-spring/coding-conventions/SKILL.md` |
| Testing strategy | `technology/java-spring/testing-strategy/SKILL.md` |
| Technology, tools, commands, API style | `java-spring.yaml` |
| Hard gates, thresholds, scoring, verdict and iteration policy | `default-governance.yaml` |
| Routing and orchestration statuses | `CLAUDE.md` and agent definitions |
| Runtime evidence names and locations | Harness baseline and resolved configuration |
| Deployment and rollback | Deployment lifecycle |
| Reflection | `REFLECTION.md` lifecycle |

This skill defines only:

```text
Reusable Java/Spring API-Design Guidance
```

---

## 20. Configuration Ownership

This skill must not duplicate authoritative runtime values such as:

```text
Language / Framework Versions
API Style Selection
Build Commands
Test Commands
Coverage Commands
Static-Analysis Commands
Architecture Commands
Security Commands
Coverage / Security / Quality Thresholds
Hard Gates
Scoring Weights
Verdict Thresholds
Fail-Closed Policy
maxIterations
Retry / Escalation Policy
Approval Mechanism
Output / Review Paths
Deployment / Rollback Controls
```

The ownership model is:

```text
api-design/SKILL.md
    → reusable API-design guidance

coding-conventions/SKILL.md
    → reusable Java/Spring coding guidance

testing-strategy/SKILL.md
    → reusable Java/Spring testing guidance

java-spring.yaml
    → selected technology values, tools, commands, API style

Application / Domain Skills
    → application-specific context, rules, architecture, events

default-governance.yaml
    → evaluation and decision policy

Approved Sprint Contract
    → feature-specific approved API behavior
```

---

## 21. Governing Principle

```text
Existing Application API
        +
Resolved Technology Profile
        +
Active Application / Domain Rules
        +
Approved Sprint Contract
        ↓
Generator Implementation
        ↓
Evaluator Semantic Review
```

> **Preserve the existing API contract and conventions first; introduce material API changes only through Planner discovery and an approved sprint contract.**
