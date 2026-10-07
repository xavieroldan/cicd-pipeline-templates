# Usage

Reference the reusable workflows with a tag (`@v0.1.0`) rather than `@main`, so pipeline changes reach consumers only when they upgrade.

## dotnet-ci.yml

| Input | Required | Default | Description |
|---|---|---|---|
| `project-path` | yes | — | `.sln` or `.csproj` to build and test |
| `dotnet-version` | no | `8.0.x` | SDK version |
| `configuration` | no | `Release` | Build configuration |
| `working-directory` | no | `.` | Directory where commands run |

Uploads test results (TRX + Cobertura coverage) as the `dotnet-test-results` artifact.

## java-ci.yml

| Input | Required | Default | Description |
|---|---|---|---|
| `working-directory` | no | `.` | Directory containing `pom.xml` |
| `java-version` | no | `21` | Temurin JDK version |
| `maven-args` | no | `""` | Extra arguments for `mvn verify` |

Uploads Surefire reports as the `java-test-reports` artifact.

## docker-build.yml

| Input | Required | Default | Description |
|---|---|---|---|
| `image-name` | yes | — | Image name, pushed as `ghcr.io/<owner>/<image-name>` |
| `context` | no | `.` | Build context |
| `dockerfile` | no | `Dockerfile` | Dockerfile path from the repository root |
| `push` | no | `false` | Push to GHCR (requires `packages: write`) |

Output `image`: full reference with the `sha-<short-sha>` tag, ready for `deploy.yml`.

Tags: `sha-<short-sha>` always, `<version>` for semver git tags (`v1.2.3`), `latest` on the default branch.

## deploy.yml

| Input | Required | Description |
|---|---|---|
| `environment` | yes | GitHub environment (`dev`, `test`, `prod`) |
| `image` | yes | Image reference to deploy |
| `deploy-command` | yes | Command that performs the deployment; receives `IMAGE` and `ENVIRONMENT` |

Configure **required reviewers** on the `prod` environment (*Settings → Environments*) to get a manual approval gate. Environment secrets are available to `deploy-command`.

Example promotion chain:

```yaml
  deploy-test:
    needs: deploy-dev
    uses: xavieroldan/cicd-pipeline-templates/.github/workflows/deploy.yml@v0.1.0
    with:
      environment: test
      image: ${{ needs.image.outputs.image }}
      deploy-command: ./scripts/deploy.sh
    secrets: inherit

  deploy-prod:
    needs: deploy-test
    uses: xavieroldan/cicd-pipeline-templates/.github/workflows/deploy.yml@v0.1.0
    with:
      environment: prod
      image: ${{ needs.image.outputs.image }}
      deploy-command: ./scripts/deploy.sh
    secrets: inherit
```

## Azure DevOps templates

| Template | Parameters |
|---|---|
| `templates/dotnet-build.yml` | `projectPath`, `dotnetVersion` (`8.0.x`), `configuration` (`Release`) |
| `templates/java-build.yml` | `workingDirectory` (`.`), `javaVersion` (`21`) |
| `templates/docker.yml` | `imageName`, `dockerfile`, `buildContext`, `containerRegistry` (service connection, needed when `push: true`), `push` (`false`), `dependsOn` |

The Azure DevOps templates are YAML-validated but, in v0.1, have not yet been run in a public Azure DevOps project (see roadmap).

## Local checks

```bash
# Lint all GitHub workflows
actionlint

# Run the samples
dotnet test samples/dotnet-api/SampleApi.sln
mvn -f samples/springboot-api/pom.xml verify
```
