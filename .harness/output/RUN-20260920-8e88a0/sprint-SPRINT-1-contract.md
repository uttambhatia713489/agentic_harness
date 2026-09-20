# Sprint Contract: SPRINT-1 — SLA Breach Detection

Runtime instance of: `.harness/templates/sprint-contract.template.md`

Status: PENDING APPROVAL (not yet an approved contract). Contains blocking
open questions (OQ-1, OQ-2) inherited from `spec.md` that must be resolved as
part of approving this contract.

---

## Identifiers

```text
Run ID:            RUN-20260920-8e88a0
Sprint ID:         SPRINT-1
Contract ID:       RUN-20260920-8e88a0-SPRINT-1
Application:       storeops
Technology:        java-spring
Governance Policy: default-governance
Feature Prompt:    PROMPT.md
Specification:     .harness/output/RUN-20260920-8e88a0/spec.md
Approved By:       <pending>
Approved At:       <pending>
Baseline:          v2.0
```

---

## Objective

Given an activity's priority, due date, and status, StoreOps can determine
whether the activity is an SLA breach (`HIGH`/`CRITICAL`, due date passed,
unresolved), and this determination is evaluated automatically without
requiring a user to interact with the activity, so that no eligible breach is
missed purely because nobody queried the activity.

This sprint produces the detection capability only. It does not notify or
escalate anyone (Sprints 2 and 3).

---

## Scope

- Add a due-date attribute to the Activity/Task domain model (per approved
  resolution of Open Question OQ-1).
- Implement SLA-breach eligibility evaluation: priority in {HIGH, CRITICAL}
  AND due date has passed AND activity is unresolved (per approved resolution
  of Open Question OQ-6: unresolved == status != DONE).
- Implement a breach-detection trigger that evaluates eligibility without
  requiring a user request (per approved resolution of Open Question OQ-2).
- Expose the outcome of detection in a form Sprint 2 can consume (e.g., an
  internal service method returning newly-detected breaches, and/or an
  approved DomainEvent — exact mechanism fixed by the approved resolution of
  OQ-2 and recorded here before Generator begins).
- Introduce a `java.time.Clock` bean (default `Clock.systemUTC()`) used only
  by the new SLA evaluation code, to support deterministic tests.

## Out of Scope

- Department Lead notification (Sprint 2).
- Store Manager escalation and grace-period tracking (Sprint 3).
- Any change to `reports`, `staff` authentication, or existing endpoints'
  request/response shapes beyond adding the due-date field.
- Any new external scheduler, broker, or workflow engine.

---

## Dependencies

```text
Dependency:               Human approval of Open Question OQ-1 (due-date
                          attribute representation) from spec.md.
Nature:                   domain
Status:                   pending
Impact if Unavailable:    Eligibility evaluation has no due-date input to
                          evaluate.

Dependency:               Human approval of Open Question OQ-2 (breach-
                          detection trigger mechanism) from spec.md.
Nature:                   architecture
Status:                   pending
Impact if Unavailable:    No mechanism exists to invoke evaluation without a
                          user request.

Dependency:               Human approval of Open Question OQ-6 (unresolved ==
                          status != DONE) from spec.md.
Nature:                   domain
Status:                   pending
Impact if Unavailable:    Eligibility evaluation cannot determine "remains
                          unresolved."
```

---

## Assumptions

```text
Assumption:      dueAt is a nullable java.time.Instant on Task, exposed via
                  ActivityDto/CreateActivityRequest/UpdateActivityRequest; a
                  null dueAt means the activity is never overdue.
Basis:            spec.md Open Question OQ-1 (Proposed Assumption).
Impact if False:  Field name/type/nullability used by Generator would not
                  match the approved decision.
Affected Acceptance Criteria: AC-S1-001 through AC-S1-006

Assumption:      Unresolved == TaskStatus != DONE.
Basis:            spec.md Open Question OQ-6 (Proposed Assumption) / Assumptions.
Impact if False:  AC-S1-003/AC-S1-004 would test the wrong status set.
Affected Acceptance Criteria: AC-S1-003, AC-S1-004

Assumption:      Breach-detection trigger is a new Spring @Scheduled
                  fixed-rate job in the activities module, with the interval
                  itself configurable; the scheduled method delegates
                  immediately to a plain, directly-callable evaluation
                  service method that Generator's tests invoke directly
                  (not by waiting on the scheduler).
Basis:            spec.md Open Question OQ-2 (Proposed Assumption).
Impact if False:  A different trigger mechanism (e.g., synchronous evaluation
                  on read) would change which component owns invocation and
                  how AC-S1-005 is tested.
Affected Acceptance Criteria: AC-S1-005
```

Do not use these assumptions to silently redefine business behavior established
in `spec.md`; they restate the proposed resolutions that must be confirmed as
part of approving this contract.

---

## Applicable Domain and Application Context

```text
Application Context (reference):   .harness/skills/domains/storeops/app-context/SKILL.md
Domain Rules (reference):          .harness/skills/domains/storeops/domain-rules/SKILL.md
Business Event Knowledge (ref):    .harness/skills/domains/storeops/business-events/SKILL.md
Feature-Specific Domain Behavior:  SLA-breach eligibility = priority in
                                    {HIGH, CRITICAL} AND dueAt is non-null AND
                                    dueAt is before "now" (per the sprint's
                                    Clock) AND status != DONE. TaskOverdueEvent
                                    (existing, BLOCKED-status-driven) is a
                                    distinct signal and must not be reused or
                                    conflated with this eligibility check.
```

---

## Applicable Architecture Rules

```text
Architecture Rules (reference):    .harness/skills/domains/storeops/architecture-rules/SKILL.md
Approved Interaction Mechanism:    Not cross-module in this sprint (activities
                                    module only). The mechanism by which Sprint
                                    2 will observe Sprint 1's detection output
                                    (direct service method call returning
                                    newly-detected breaches, vs. an internal
                                    DomainEvent) must be fixed here before
                                    Generator implements it — see Open
                                    Question OQ-2 in spec.md.
Feature-Specific Constraints:      Controller -> Service -> Repository layering
                                    must be preserved; the new @Scheduled
                                    component must delegate to a service, not
                                    contain business logic itself; no new
                                    external dependency may be introduced for
                                    scheduling.
```

Implementation-mechanism neutrality applies: the exact detection-output
mechanism (service call vs. DomainEvent) must be confirmed at approval time
and is not left to Generator's discretion.

---

## Acceptance Criteria

```text
AC ID:            AC-S1-001
Description:      Overdue HIGH-priority, unresolved activity is detected as
                   an SLA breach.
GIVEN:            An activity with priority = HIGH, status != DONE, and
                   dueAt in the past relative to the evaluation Clock.
WHEN:             SLA-breach evaluation runs for that activity.
THEN:             The activity is identified as an SLA breach.
Mandatory:        Yes
Positive/Negative: positive
Applicable Domain / Architecture References: domains/storeops/domain-rules/SKILL.md (Priorities, Activity Lifecycle)
Feature Evidence Expectations: Unit test on the new evaluation service/method.

AC ID:            AC-S1-002
Description:      Overdue CRITICAL-priority, unresolved activity is detected
                   as an SLA breach.
GIVEN:            An activity with priority = CRITICAL, status != DONE, and
                   dueAt in the past.
WHEN:             SLA-breach evaluation runs for that activity.
THEN:             The activity is identified as an SLA breach.
Mandatory:        Yes
Positive/Negative: positive
Applicable Domain / Architecture References: domains/storeops/domain-rules/SKILL.md (Priorities)
Feature Evidence Expectations: Unit test on the new evaluation service/method.

AC ID:            AC-S1-003
Description:      LOW-priority activity is never an SLA breach.
GIVEN:            An activity with priority = LOW, status != DONE, and dueAt
                   in the past.
WHEN:             SLA-breach evaluation runs for that activity.
THEN:             The activity is NOT identified as an SLA breach.
Mandatory:        Yes
Positive/Negative: negative
Applicable Domain / Architecture References: domains/storeops/domain-rules/SKILL.md (Priorities)
Feature Evidence Expectations: Unit test on the new evaluation service/method.

AC ID:            AC-S1-004
Description:      MEDIUM-priority activity is never an SLA breach.
GIVEN:            An activity with priority = MEDIUM, status != DONE, and
                   dueAt in the past.
WHEN:             SLA-breach evaluation runs for that activity.
THEN:             The activity is NOT identified as an SLA breach.
Mandatory:        Yes
Positive/Negative: negative
Applicable Domain / Architecture References: domains/storeops/domain-rules/SKILL.md (Priorities)
Feature Evidence Expectations: Unit test on the new evaluation service/method.

AC ID:            AC-S1-005
Description:      DONE activity is never an SLA breach, regardless of
                   priority or due date.
GIVEN:            An activity with priority in {HIGH, CRITICAL}, status =
                   DONE, and dueAt in the past.
WHEN:             SLA-breach evaluation runs for that activity.
THEN:             The activity is NOT identified as an SLA breach.
Mandatory:        Yes
Positive/Negative: negative
Applicable Domain / Architecture References: domains/storeops/domain-rules/SKILL.md (Activity Lifecycle, Completed/Unresolved Semantics)
Feature Evidence Expectations: Unit test on the new evaluation service/method.

AC ID:            AC-S1-006
Description:      Activity that is not yet overdue (dueAt is null, or dueAt is
                   in the future) is never an SLA breach, regardless of
                   priority or status.
GIVEN:            An activity with priority in {HIGH, CRITICAL}, status !=
                   DONE, and dueAt is null OR dueAt is after "now".
WHEN:             SLA-breach evaluation runs for that activity.
THEN:             The activity is NOT identified as an SLA breach.
Mandatory:        Yes
Positive/Negative: negative
Applicable Domain / Architecture References: domains/storeops/domain-rules/SKILL.md (Time Awareness)
Feature Evidence Expectations: Unit test using a fixed/injected Clock for both
                   the "null dueAt" and "future dueAt" cases.

AC ID:            AC-S1-007
Description:      Breach evaluation runs without requiring a user request
                   against the specific activity.
GIVEN:            An eligible breach activity exists and no client issues any
                   request referencing it.
WHEN:             The approved trigger mechanism (per OQ-2) fires.
THEN:             The activity is evaluated and identified as an SLA breach
                   (observable via the mechanism fixed under Applicable
                   Architecture Rules above).
Mandatory:        Yes
Positive/Negative: positive
Applicable Domain / Architecture References: domains/storeops/architecture-rules/SKILL.md §5
Feature Evidence Expectations: Test invokes the evaluation entry point
                   directly (not by waiting on the scheduler) per the
                   deterministic-testing requirement in spec.md.
```

---

## Required Tests

```text
Test Expectation:       Unit tests covering AC-S1-001 through AC-S1-007 using
                         a mocked/fixed Clock (no reliance on wall-clock time
                         or Thread.sleep).
Type:                   unit
Applies to AC:          AC-S1-001, AC-S1-002, AC-S1-003, AC-S1-004, AC-S1-005,
                         AC-S1-006, AC-S1-007
Existing Application Convention Reference: JUnit 5 + Mockito, @ExtendWith
                         (MockitoExtension.class), @Mock/@BeforeEach style seen
                         in ActivityServiceTest.java and AlertServiceTest.java.
Deterministic Time Handling Required: Yes

Test Expectation:       If the due-date field is exposed via
                         CreateActivityRequest/UpdateActivityRequest/
                         ActivityDto, existing ActivityController/
                         ActivityService tests must continue to pass
                         unmodified in their existing assertions (only new
                         assertions/fields added), demonstrating the change is
                         additive and non-breaking.
Type:                   unit / api-controller (regression)
Applies to AC:          AC-S1-001 through AC-S1-006
Existing Application Convention Reference: ActivityServiceTest.java,
                         ActivityControllerTest.java
Deterministic Time Handling Required: No
```

---

## Hard Gates (Reference)

```text
Hard Gate Source:          .harness/config/policies/default-governance.yaml
Applicable Hard Gates:     build (enabled, mandatory), tests (enabled,
                            mandatory), acceptanceCriteria (enabled,
                            mandatory), governanceEvidence (enabled,
                            mandatory). coverage, staticAnalysis,
                            architecture, and security hard gates are
                            disabled/non-mandatory for this run per the
                            resolved governance policy, but see spec.md Risks
                            regarding the pom-level JaCoCo check bound to the
                            `verify` phase.
Sprint-Specific Emphasis:  acceptanceCriteria (all 7 ACs are mandatory,
                            including all negative paths); build/tests must
                            pass via `mvn clean verify`.
```

---

## Verification Expectations

```text
Verification Command Source: .harness/config/technologies/java-spring.yaml
                              (mvn clean verify)
Applicable Configured Checks:
  - Build
  - Tests
  - Coverage        (configured but not a mandatory hard gate this run)
  - Static Analysis (configured but not a mandatory hard gate this run)
  - Architecture    (configured but not a mandatory hard gate this run)
  - Security        (not configured)
Evaluator Order Reference:   .harness/skills/core/evaluation-framework/SKILL.md
```

---

## Completion Conditions

```text
Required Verdict For Completion:  PASS or CONDITIONAL_PASS as permitted by
                                   default-governance.yaml.
Required Acceptance Criteria:     AC-S1-001, AC-S1-002, AC-S1-003, AC-S1-004,
                                   AC-S1-005, AC-S1-006, AC-S1-007 (all
                                   mandatory).
Required Evidence Artifacts:      generator-summary-iteration-N.md
                                   evaluator-feedback-iteration-N.md
                                   run-log.md
Non-Blocking Findings Permitted:  Per default-governance.yaml CONDITIONAL_PASS
                                   routing (requiresHumanIntervention: false).
```

---

## Contract Change Handling

If Generator or Evaluator determines that the approved due-date representation
(OQ-1), trigger mechanism (OQ-2), or detection-output mechanism cannot be
implemented as specified without a material change, `STATUS:
CONTRACT_CHANGE_REQUIRED` applies per `CLAUDE.md` §12/§16. Generator must not
silently substitute a different mechanism.

---

## Traceability

```text
PROMPT.md:                   PROMPT.md §2, §2.1, §2.5, §3, §4.1, §4.2 (dueAt
                              only), §4.3, §4.7, §8
spec.md:                      .harness/output/RUN-20260920-8e88a0/spec.md
Application Configuration:   storeops
Technology Configuration:    java-spring
Governance Policy:           default-governance
Domain Skills Consulted:     domains/storeops/app-context, domain-rules,
                              architecture-rules, business-events
Approved Human Decisions:    Pending (OQ-1, OQ-2, OQ-6)
```
