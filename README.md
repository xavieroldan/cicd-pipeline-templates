# CI/CD Pipeline Templates

> Reusable CI/CD pipelines for **.NET** and **Java** services — in **GitHub Actions** and **Azure DevOps** — validated end to end against two sample apps.

![CI](https://github.com/xavieroldan/cicd-pipeline-templates/actions/workflows/ci.yml/badge.svg)
![Status](https://img.shields.io/badge/status-v0.1-orange)
![License](https://img.shields.io/badge/license-MIT-blue)

## Why this project

Most teams copy-paste pipeline YAML between repositories until every project has its own slightly different (and slightly broken) version. This repository provides **one versioned set of templates** that any .NET or Java service can consume with a few lines of configuration, so build, test, packaging and deployment work the same way everywhere.

It is based on the patterns I apply professionally when automating multi-country enterprise projects, rewritten from scratch with no client code.

## Features (v0.1)

**GitHub Actions — reusable workflows (`workflow_call`)**

| Workflow | What it does |
|---|---|
| `dotnet-ci.yml` | Restore, build and test a .NET solution with NuGet caching, code coverage and test result artifacts |
| `java-ci.yml` | `mvn verify` with Maven caching and Surefire report artifacts |
| `docker-build.yml` | Multi-stage Docker build with Buildx layer cache, SHA / semver / `latest` tags and optional push to GHCR |
| `deploy.yml` | Deploy an image to a **GitHub environment** (approval rules per environment), with concurrency control and a job summary |

**Azure DevOps — job templates**: `.NET` build & test, Maven build & test, Docker build / build-and-push, plus an example consumer pipeline.

**Self-tested**: `ci.yml` runs every reusable workflow against the sample apps on each push and pull request, and all workflows are linted with [actionlint](https://github.com/rhysd/actionlint).

**Least privilege**: workflows declare explicit `permissions`, and only the Docker job gets `packages: write`.

## Architecture

```mermaid
flowchart LR
    A[Push / PR] --> B[dotnet-ci / java-ci]
    B --> C[docker-build]
    C -->|push to main| D[deploy: dev]
    D -.-> E[deploy: test]
    E -.->|manual approval| F[deploy: prod]
```

Dashed steps are supported by `deploy.yml` (one call per environment) but not wired in the sample pipeline yet.

## Quick start (GitHub Actions)

```yaml
# .github/workflows/ci.yml in a consumer repository
name: CI
on:
  push:
    branches: [main]
  pull_request:

jobs:
  build:
    uses: xavieroldan/cicd-pipeline-templates/.github/workflows/dotnet-ci.yml@v0.1.0
    with:
      project-path: MyService.sln

  image:
    needs: build
    uses: xavieroldan/cicd-pipeline-templates/.github/workflows/docker-build.yml@v0.1.0
    with:
      image-name: my-service
      push: ${{ github.event_name == 'push' }}
    permissions:
      contents: read
      packages: write

  deploy-dev:
    needs: image
    uses: xavieroldan/cicd-pipeline-templates/.github/workflows/deploy.yml@v0.1.0
    with:
      environment: dev
      image: ${{ needs.image.outputs.image }}
      deploy-command: ./scripts/deploy.sh
```

## Quick start (Azure DevOps)

```yaml
# azure-pipelines.yml in a consumer repository
resources:
  repositories:
    - repository: templates
      type: github
      name: xavieroldan/cicd-pipeline-templates
      endpoint: github-connection   # your GitHub service connection
      ref: refs/tags/v0.1.0

stages:
  - stage: build
    jobs:
      - template: azure-devops/templates/dotnet-build.yml@templates
        parameters:
          projectPath: MyService.sln
```

More examples and all inputs: [docs/usage.md](docs/usage.md).

## Repository structure

```text
.
├── .github/workflows/
│   ├── dotnet-ci.yml        # Reusable: .NET build + test
│   ├── java-ci.yml          # Reusable: Maven build + test
│   ├── docker-build.yml     # Reusable: image build and push
│   ├── deploy.yml           # Reusable: deploy to an environment
│   └── ci.yml               # Self-test against the samples
├── azure-devops/
│   ├── templates/           # dotnet-build, java-build, docker
│   └── azure-pipelines.yml  # Example consumer pipeline
├── samples/
│   ├── dotnet-api/          # .NET 8 Minimal API + xUnit tests + Dockerfile
│   └── springboot-api/      # Spring Boot 3 / Java 21 + JUnit tests + Dockerfile
└── docs/usage.md
```

## Tech stack

GitHub Actions · Azure DevOps Pipelines · Docker Buildx · GitHub Container Registry · .NET 8 · xUnit · Java 21 · Spring Boot 3 · Maven · actionlint

## Roadmap

- [x] Reusable .NET and Java build/test workflows
- [x] Docker build with caching and GHCR push
- [x] Environment deployment workflow
- [x] Azure DevOps job templates
- [x] Sample apps and self-test CI
- [ ] Run the Azure DevOps example pipeline in a public Azure DevOps project
- [ ] Image and dependency vulnerability scanning
- [ ] OIDC federation to Azure / AWS for deployments (no long-lived secrets)
- [ ] DEV → TEST → PROD promotion with manual approval in the sample
- [ ] Semantic versioning and release notes from Conventional Commits

## What this project demonstrates

- Designing CI/CD as a **shared platform** rather than per-project scripts
- Working fluently in **both GitHub Actions and Azure DevOps**
- Pipelines that are **tested like code**: self-test workflow, linting and least-privilege permissions

## Author

**Xavier Roldán** — Senior Developer & Team Lead
[xavierroldan.com](https://xavierroldan.com) · [LinkedIn](https://www.linkedin.com/in/xavierroldan/)

## License

MIT
