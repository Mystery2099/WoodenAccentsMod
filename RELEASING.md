# Releasing Wooden Accents Mod

Releases are published manually to GitHub. Nothing is published by a push, merge, tag, or scheduled workflow, and GitHub provides the required token automatically.

## Prepare a release

1. Update `mod_version` in `gradle.properties`.
2. Move the pending notes under `## [Unreleased]` in `CHANGELOG.md` to a new section named `## [<mod_version>] - YYYY-MM-DD`.
3. Open and merge a pull request containing the version and changelog changes.
4. Confirm the build workflow succeeds on `master`.

The changelog heading must match `mod_version` exactly and its section must not be empty.

## Publish

1. Open **Actions → Release → Run workflow**.
2. Select the `master` branch.
3. Select `STABLE`, `BETA`, or `ALPHA`.
4. Leave dry run disabled.
5. Check **Confirm release**.
6. Run the workflow.
7. Verify the new version on GitHub Releases.

The workflow creates a `v<mod_version>` GitHub tag from the exact commit selected by the workflow and attaches the remapped release and sources JARs.

## Dry run

Enable **Dry run** to validate the release metadata and artifacts without publishing. Dry runs may use any branch and do not require confirmation. The generated files are uploaded as a workflow artifact.

## Retry a failed release

If publishing fails while uploading assets, inspect GitHub for a draft release. Remove or finish that draft before rerunning the workflow.
