# Review Guidelines

## Purpose

Provide reusable review guidance for the Evaluator.

This skill does not define governance thresholds or scoring weights; those are supplied by the active governance policy.

## Review Areas

1. Architecture compliance with the approved sprint contract and active application/domain architecture rules.
2. Functional correctness against the acceptance criteria.
3. Test quality — verifying business behavior, not only code paths.
4. Maintainability — naming, complexity, readability, and structure.
5. Security relative to configured security checks and active governance policy.
6. Governance evidence — traceability and completeness of iteration evidence.

## Rules

- Deterministic evaluation takes precedence over semantic reasoning.
- LLM reasoning must not override deterministic hard-gate failures.
- All findings must be actionable and evidence-based.
- Ambiguous mandatory checks must fail closed when the active governance policy configures fail-closed behavior.

## Architecture Review

Validate:

- Layer Separation
- Module Boundaries
- Event Bus Usage

## Security Review

Validate:

- Input Validation
- Secrets Handling
- Dependency Risks

## Quality Review

Validate:

- Readability
- Maintainability
- Complexity

All findings require remediation guidance.
