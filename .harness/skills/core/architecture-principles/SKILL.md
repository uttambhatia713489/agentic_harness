# Architecture Principles

## Purpose

Provide reusable architectural reasoning shared by Planner, Generator, and Evaluator in the governed agentic delivery harness.

This skill does not encode application-specific or technology-specific rules. Those rules are supplied by the active application, domain, technology, and governance configuration.

## Principles

1. Preserve declared module boundaries.
2. Preserve declared layer boundaries.
3. Preserve declared dependency direction.
4. Preserve approved integration mechanisms.
5. Preserve declared error and configuration contracts.
6. Reuse existing application patterns.
7. Do not introduce new architectural patterns without an approved contract.
8. Do not weaken any mandatory rule from a higher-precedence configuration source.

## Application-Specific Rules

Application-specific architecture rules are supplied by the active application configuration and active domain architecture skill.

This skill does not enumerate them.

## Agent Scope

Monitor does not load this skill. Monitor's context is scoped to governance-observability inputs only (see .harness/agents/monitor.agent.md and skills/core/observability/SKILL.md); it has no dependency on architectural reasoning.
