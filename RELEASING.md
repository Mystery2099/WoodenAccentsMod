# Releasing Wooden Accents Mod

Run the Release workflow manually to publish. Merging a pull request, pushing a commit, or creating a tag does not start a release.

The release workflow publishes to GitHub Releases only. It does not upload anything to Modrinth, CurseForge, or a Maven repository.

## Workflow requirements

- Dry-run mode is enabled by default.
- A real release must run from `master`.
- A real release requires the **Confirm release** checkbox.
- Existing GitHub releases and tags are rejected instead of overwritten.
- The publishing token is provided by GitHub Actions; there is no personal token to configure.

## Testing for 1.19.4-1.1.4.0

Mathew confirmed single-player testing passed, including the corrected ladder recipes, and chose to proceed without multiplayer testing. Dedicated-server multiplayer, existing-world migration, and optional storage-preview integrations remain unverified. The local publishing dry run passed.

In-game screenshots of mixed structural builds would improve the README's schematic examples, but are optional for this release. A custom seated pose is deferred.

## Prepare a release

1. Update `mod_version` in `gradle.properties`.
2. Move the relevant notes from `## [Unreleased]` in `CHANGELOG.md` into a new `## [<mod_version>] - YYYY-MM-DD` section.
3. Make sure the new changelog section is not empty and matches `mod_version` exactly.
4. Open and merge a pull request containing the version and changelog changes.
5. Confirm the build on `master` succeeds.

The Gradle publishing tasks fail when the current version is missing from the changelog or its section is empty.

## Run a dry run first

1. Open **Actions → Release → Run workflow**.
2. Select `master` or the branch you want to validate.
3. Leave **Dry run** enabled.
4. Leave **Confirm release** disabled.
5. Choose the intended release type and run the workflow.

The workflow builds the same remapped release and sources JARs used by a real release, then uploads them as a temporary workflow artifact. It does not create a tag or GitHub release. Dry-run artifacts are kept for seven days.

For a local dry run, use:

```bash
./gradlew clean publishMods -PdryRun=true -PreleaseType=STABLE
```

## Publish

Once the release commit and dry run are both good:

1. Open **Actions → Release → Run workflow**.
2. Select the `master` branch.
3. Choose `STABLE`, `BETA`, or `ALPHA`.
4. Disable **Dry run**.
5. Enable **Confirm release**.
6. Run the workflow.
7. Verify the tag, release notes, and both attached JARs on GitHub Releases.

The workflow creates `v<mod_version>` from the exact `master` commit used by the run. `BETA` and `ALPHA` releases are marked as prereleases by the publishing plugin.

## If publishing fails

Before retrying, check GitHub Releases and repository tags for a partial result. If the plugin created a draft release but failed while uploading an asset, either finish that draft or remove it before retrying. The workflow will refuse to continue while the target tag or release already exists.

## Keep Modrinth (and CurseForge) in sync

Use `README.md` for the project descriptions. The GitHub Actions release workflow does not update Modrinth or CurseForge descriptions.

When you change player-facing README content:

1. Open the Modrinth project settings for Wooden Accents.
2. Paste the README body into the project description.
3. Remove the **Gallery** section and any embedded image markdown. Modrinth already has its own gallery.
4. Set the short summary to: `Vanilla-scale furniture and structural accents for every wood type.`
5. Repeat on CurseForge if that page is still maintained.

Doc links in the README use absolute GitHub URLs so they keep working when pasted onto Modrinth or CurseForge.

Do this for documentation-only README edits as well, not only full releases.
