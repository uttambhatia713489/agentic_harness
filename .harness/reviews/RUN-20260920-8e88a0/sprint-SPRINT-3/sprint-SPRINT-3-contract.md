# Sprint Contract: SPRINT-3 — Grace Period and Store Manager Escalation

Runtime instance of: `.harness/templates/sprint-contract.template.md`

Status: APPROVED (Sprint 3 only). Approval resolves non-blocking Open
Questions OQ-4, OQ-5, and OQ-6 exactly as stated in this contract's
Assumptions section (below), scoped to Sprint 3 only. This is the final
Planner-derived sprint in the current decomposition.

---

## Approval Record

```text
Approval Command:   APPROVED
Approved Scope:     Sprint 3 (SPRINT-3) only — scope, acceptance criteria,
                     architecture constraints, required tests, dependencies,
                     assumptions, and completion conditions of this contract
                     as written.
Not Approved:       Any other module, endpoint, event, domain model,
                     infrastructure, refactoring, or contract change beyond
                     this document.
Approved By:        uttambhatia@outlook.com (session user)
Approved At:        2026-09-21
```

---

## Identifiers

```text
Run ID:            RUN-20260920-8e88a0
Sprint ID:         SPRINT-3
Contract ID:       RUN-20260920-8e88a0-SPRINT-3
Application:       storeops
Technology:        java-spring
Governance Policy: default-governance
Feature Prompt:    PROMPT.md
Specification:     .harness/output/RUN-20260920-8e88a0/spec.md
Approved By:       uttambhatia@outlook.com (session user)
Approved At:        2026-09-21
Baseline:          v2.0
```

---

## Objective

If a breached activity remains unresolved after the configured grace period
following Department Lead notification, StoreOps resolves the Store Manager
for the activity's programme and escalates the breach to them, exactly once
per breach. If the activity is resolved before the grace period expires, no
escalation occurs.

---

## Scope

- New configuration property for the grace period (per approved resolution of
  Open Question OQ-5), e.g. `storeops.sla.grace-period` bound via
  `@ConfigurationProperties` as a `java.time.Duration`.
- Grace-period expiry evaluation: for each activity with an existing
  `SLA_BREACH` `Notification` (from Sprint 2, correlated via `referenceId`)
  whose `createdAt` plus the configured grace period has passed, and which is
  still unresolved (per OQ-6: status != DONE) and does not yet have a
  corresponding `ESCALATION` notification, resolve the Store Manager and
  create an `ESCALATION` notification.
- Store Manager resolution as the `ProjectMember`(s) with `role ==
  ProjectRole.STORE_MANAGER` in the activity's programme, reusing Sprint 2's
  `ProgrammeService` read method (per approved resolution of Open Question
  OQ-4).
- Resolution-before-escalation: if the activity's status becomes `DONE`
  before the grace period expires, no `ESCALATION` notification is created
  for that breach.
- Escalation idempotency: at most one `ESCALATION` notification per activity
  per breach.
- Reuse of the `java.time.Clock` bean introduced in Sprint 1 for grace-period
  expiry computation (no second clock abstraction).

## Out of Scope

- Any change to the Department Lead notification logic itself (Sprint 2),
  other than reading its persisted `Notification` as the timing anchor.
- Any new notification channel or delivery mechanism.
- Any change to `reports` or `staff` authentication.

---

## Dependencies

```text
Dependency:               Sprint 1 (SPRINT-1) and Sprint 2 (SPRINT-2) approved
                          and implemented.
Nature:                   technical
Status:                   pending
Impact if Unavailable:    No detection signal and no Department-Lead
                          Notification exists to anchor grace-period timing
                          or to satisfy the "already notified" precondition.

Dependency:               Human approval (or acceptance of proposed default)
                          of Open Question OQ-5 (grace-period value/
                          representation/configuration key) from spec.md.
Nature:                   configuration
Status:                   pending
Impact if Unavailable:    No concrete grace-period value to evaluate against.

Dependency:               Human approval (or acceptance of proposed default)
                          of Open Question OQ-4 (Store Manager resolution
                          source) from spec.md.
Nature:                   domain
Status:                   pending
Impact if Unavailable:    Escalation could be resolved to the wrong
                          recipient(s).

Dependency:               Human approval (or acceptance of proposed default)
                          of Open Question OQ-6 (unresolved == status !=
                          DONE) from spec.md.
Nature:                   domain
Status:                   pending
Impact if Unavailable:    Resolution-before-escalation check would use the
                          wrong status set.
```

---

## Assumptions

```text
Assumption:      Store Manager == ProjectMember(s) with role ==
                  ProjectRole.STORE_MANAGER in the Project referenced by the
                  breached activity's programmeId. Zero matching members:
                  logged, no escalation created, no exception. Multiple
                  matching members: all are escalated to.
Basis:            spec.md Open Question OQ-4 and related Assumptions
                  (symmetry with Sprint 2's Department Lead resolution).
Impact if False:  Wrong recipient(s), or an unhandled exception on an
                  unassigned role.
Affected Acceptance Criteria: AC-S3-002, AC-S3-005

Assumption:      Grace-period expiry anchor = the existing SLA_BREACH
                  Notification's createdAt (from Sprint 2) plus the
                  configured grace-period Duration.
Basis:            spec.md Open Question OQ-3 (Notification correlation) and
                  OQ-5 (grace-period representation).
Impact if False:  Grace-period timing would need a different anchor (e.g., a
                  separate timestamp field), changing Generator's
                  implementation.
Affected Acceptance Criteria: AC-S3-001, AC-S3-002, AC-S3-003

Assumption:      Resolution-before-escalation is evaluated by checking the
                  activity's current status at the moment grace-period expiry
                  is evaluated (status == DONE means resolved); there is no
                  separate "resolution timestamp" compared against the grace
                  period deadline, since Task has no such timestamp today and
                  adding one is not required to satisfy PROMPT.md §2.3's
                  stated outcomes.
Basis:            Task model has updatedAt but no dedicated "resolvedAt";
                  PROMPT.md's stated outcomes only require presence/absence
                  of escalation, not exact resolution timing.
Impact if False:  A different, more precise resolution-timing model may be
                  required to satisfy the business rule.
Affected Acceptance Criteria: AC-S3-004
```

---

## Applicable Domain and Application Context

```text
Application Context (reference):   .harness/skills/domains/storeops/app-context/SKILL.md
Domain Rules (reference):          .harness/skills/domains/storeops/domain-rules/SKILL.md
Business Event Knowledge (ref):    .harness/skills/domains/storeops/business-events/SKILL.md
Feature-Specific Domain Behavior:  AlertType.ESCALATION (existing enum value,
                                    currently unused) is reused for the Store
                                    Manager notification, consistent with
                                    Sprint 2's reuse of AlertType.SLA_BREACH.
                                    The existing StoreOps STORE_MANAGER role
                                    semantics (ProjectRole.STORE_MANAGER) are
                                    reused unchanged, per PROMPT.md §2.3/§4.5.
```

---

## Applicable Architecture Rules

```text
Architecture Rules (reference):    .harness/skills/domains/storeops/architecture-rules/SKILL.md
Approved Interaction Mechanism:    Reuses Sprint 2's ProgrammeService read
                                    method for Store Manager resolution (no
                                    new cross-module interface introduced).
                                    Grace-period expiry evaluation is invoked
                                    by the same trigger mechanism approved in
                                    Sprint 1 (OQ-2) — no second, competing
                                    scheduling mechanism is introduced.
Feature-Specific Constraints:      No direct ProgrammeRepository/
                                    ActivityRepository access from alerts; no
                                    new notification channel; AppError
                                    contract preserved.
```

---

## Acceptance Criteria

```text
AC ID:            AC-S3-001
Description:      Breach remains unresolved after the configured grace period
                   results in Store Manager escalation.
GIVEN:            An activity with an existing SLA_BREACH Notification whose
                   createdAt + configured grace period has passed, and the
                   activity's status is still != DONE, and no ESCALATION
                   Notification exists yet for that activity.
WHEN:             Grace-period expiry evaluation runs.
THEN:             An ESCALATION Notification is created for the resolved
                   Store Manager(s), containing sufficient business context
                   (activity id, title, priority, due date, programme id).
Mandatory:        Yes
Positive/Negative: positive
Applicable Domain / Architecture References: PROMPT.md §2.3
Feature Evidence Expectations: Unit test with a fixed Clock placing "now"
                   past the grace-period deadline.

AC ID:            AC-S3-002
Description:      When a programme has multiple Store Managers, all are
                   escalated to.
GIVEN:            Grace period expired per AC-S3-001, and the programme has
                   two or more ProjectMembers with role STORE_MANAGER.
WHEN:             Grace-period expiry evaluation runs.
THEN:             One ESCALATION Notification per matching Store Manager is
                   created.
Mandatory:        Yes
Positive/Negative: positive
Applicable Domain / Architecture References: spec.md Assumptions (multiple
                   matching members)
Feature Evidence Expectations: Unit test with two ProjectMembers holding
                   STORE_MANAGER in the same programme.

AC ID:            AC-S3-003
Description:      Breach resolved before the configured grace period expires
                   results in no Store Manager escalation.
GIVEN:            An activity with an existing SLA_BREACH Notification whose
                   createdAt + configured grace period has NOT yet passed, OR
                   has passed but the activity's status is DONE.
WHEN:             Grace-period expiry evaluation runs.
THEN:             No ESCALATION Notification is created for that activity.
Mandatory:        Yes
Positive/Negative: negative
Applicable Domain / Architecture References: PROMPT.md §2.3 ("If the breached
                   activity is resolved before the configured grace period
                   expires: No Store Manager Escalation")
Feature Evidence Expectations: Two unit tests: (a) grace period not yet
                   expired, still unresolved; (b) grace period expired but
                   status == DONE.

AC ID:            AC-S3-004
Description:      An activity resolved (DONE) strictly before grace-period
                   expiry, then re-evaluated after the grace-period deadline
                   has passed, still does not escalate.
GIVEN:            An activity whose status became DONE before its grace-period
                   deadline, and grace-period expiry evaluation runs again
                   after that deadline has since passed.
WHEN:             Grace-period expiry evaluation runs.
THEN:             No ESCALATION Notification is created for that activity.
Mandatory:        Yes
Positive/Negative: negative
Applicable Domain / Architecture References: PROMPT.md §2.3
Feature Evidence Expectations: Unit test asserting the current-status check
                   (not a point-in-time snapshot) governs the outcome, per
                   the Assumption recorded above.

AC ID:            AC-S3-005
Description:      When a breached activity's programme has zero
                   ProjectMembers with role STORE_MANAGER, escalation
                   processing degrades gracefully.
GIVEN:            Grace period expired per AC-S3-001, and the programme has no
                   ProjectMember with role STORE_MANAGER.
WHEN:             Grace-period expiry evaluation runs.
THEN:             No ESCALATION Notification is created and no exception
                   propagates from the escalation step.
Mandatory:        Yes
Positive/Negative: negative
Applicable Domain / Architecture References: spec.md Assumptions (zero
                   matching members)
Feature Evidence Expectations: Unit test with an empty STORE_MANAGER member
                   set.

AC ID:            AC-S3-006
Description:      Repeated evaluation after an escalation has already been
                   created does not create a duplicate ESCALATION
                   notification.
GIVEN:            An activity already has an existing ESCALATION Notification
                   (referenceId = activity id).
WHEN:             Grace-period expiry evaluation runs again for the same
                   activity.
THEN:             No additional ESCALATION Notification is created.
Mandatory:        Yes
Positive/Negative: negative
Applicable Domain / Architecture References: PROMPT.md §4.6
Feature Evidence Expectations: Unit test seeding an existing ESCALATION
                   Notification and asserting no second save occurs; test
                   uses a fixed/injected Clock, not wall-clock time.
```

---

## Required Tests

```text
Test Expectation:       Unit tests covering AC-S3-001 through AC-S3-006 using
                         a fixed/injected Clock (reusing Sprint 1's Clock
                         bean) and mocked ProgrammeService/AlertRepository.
Type:                   unit
Applies to AC:          AC-S3-001, AC-S3-002, AC-S3-003, AC-S3-004, AC-S3-005,
                         AC-S3-006
Existing Application Convention Reference: JUnit 5 + Mockito style from
                         AlertServiceTest.java / ActivityServiceTest.java.
Deterministic Time Handling Required: Yes

Test Expectation:       Grace-period configuration property is validated
                         (e.g., a test confirming the configured value binds
                         correctly and a documented default applies when
                         unset), per PROMPT.md §2.4's requirement that the
                         feature not depend on a hard-coded value.
Type:                   unit
Applies to AC:          AC-S3-001, AC-S3-003
Existing Application Convention Reference: N/A (first
                         @ConfigurationProperties usage in this codebase; see
                         spec.md Open Question OQ-5)
Deterministic Time Handling Required: No
```

---

## Hard Gates (Reference)

```text
Hard Gate Source:          .harness/config/policies/default-governance.yaml
Applicable Hard Gates:     build (mandatory), tests (mandatory),
                            acceptanceCriteria (mandatory), governanceEvidence
                            (mandatory). coverage/staticAnalysis/architecture/
                            security not mandatory this run (see spec.md
                            Risks re: pom-level JaCoCo check).
Sprint-Specific Emphasis:  Deterministic time-based testing (AC-S3-001,
                            AC-S3-003, AC-S3-004, AC-S3-006 all depend on
                            correct Clock-based, non-flaky tests); escalation
                            idempotency (AC-S3-006) is mandatory.
```

---

## Verification Expectations

```text
Verification Command Source: .harness/config/technologies/java-spring.yaml
                              (mvn clean verify)
Applicable Configured Checks:
  - Build
  - Tests
  - Coverage        (configured, not mandatory this run)
  - Static Analysis (configured, not mandatory this run)
  - Architecture    (configured, not mandatory this run)
  - Security        (not configured)
Evaluator Order Reference:   .harness/skills/core/evaluation-framework/SKILL.md
```

---

## Completion Conditions

```text
Required Verdict For Completion:  PASS or CONDITIONAL_PASS as permitted by
                                   default-governance.yaml.
Required Acceptance Criteria:     AC-S3-001, AC-S3-002, AC-S3-003, AC-S3-004,
                                   AC-S3-005, AC-S3-006 (all mandatory).
Required Evidence Artifacts:      generator-summary-iteration-N.md
                                   evaluator-feedback-iteration-N.md
                                   run-log.md
Non-Blocking Findings Permitted:  Per default-governance.yaml CONDITIONAL_PASS
                                   routing (requiresHumanIntervention: false).
```

Completion of SPRINT-3 also completes the feature-level flow described in
`PROMPT.md` §14, subject to `CLAUDE.md` §41 run-completion criteria being
satisfied across all three sprints.

---

## Contract Change Handling

If the approved grace-period representation (OQ-5), Store Manager resolution
source (OQ-4), or the resolution-before-escalation timing model (see
Assumptions) cannot be implemented as specified, `STATUS:
CONTRACT_CHANGE_REQUIRED` applies per `CLAUDE.md` §12/§16.

---

## Traceability

```text
PROMPT.md:                   PROMPT.md §2.3, §2.4, §2.5, §3, §4.2, §4.5, §4.6,
                              §4.7, §8, §14
spec.md:                      .harness/output/RUN-20260920-8e88a0/spec.md
Application Configuration:   storeops
Technology Configuration:    java-spring
Governance Policy:           default-governance
Domain Skills Consulted:     domains/storeops/app-context, domain-rules,
                              architecture-rules, business-events
Approved Human Decisions:    Pending (OQ-4, OQ-5, OQ-6); depends on SPRINT-1,
                              SPRINT-2
```
