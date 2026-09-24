# Releasing Wooden Accents Mod

Run the Release workflow manually to publish. Merging a pull request, pushing a commit, or creating a tag does not start a release.

The release workflow publishes to GitHub Releases only. It does not upload anything to Modrinth, CurseForge, or a Maven repository.

## Version scheme

Versions follow `<minecraft_version>-<content_version>` (for example `1.20.1-1.1.4.1`). The Minecraft version technically supports, not targets; the content version follows the feature number.

- Patch content number when porting the same content to a new Minecraft version or fixing a release on that Minecraft line.
- Bump to the next minor content version (for example `1.2.0.0`) for the first fully tested, feature-complete release on the target Minecraft version.
- Increment the last component for each distinct published build; do not republish the same version.
- Do not encode loader names (Fabric, NeoForge) in the version. Different loader builds of the same release share one version and differ by artifact name and loader metadata.
- Optional suffixes such as `-beta.1` are unnecessary: the workflow's release type already marks the build as a prerelease.

## Release types

Every artifact gets a unique version regardless of stability. Prereleases are still real releases; do not treat them as disposable snapshots.

| Type | Use for |
|---|---|
| `STABLE` | Fully validated feature releases. Reserve for the first proper `1.21.1` release. |
| `BETA` | Intended functionality exists, but migration, compatibility, or world testing is incomplete. |
| `ALPHA` | First multiloader builds or ports where loader parity may still be incomplete. |

Promote stability by publishing the same content version with the newer release type when testing confirms it.

## Working versions and branches

The current migration sequence. Each listed build publishes under the shown version with the shown release type:

| Target | Work | Version | Release type |
|---|---|---:|---|
| 1.20.1 | Compatibility port | `1.20.1-1.1.4.1` | Beta (released) |
| 1.20.1 | Split into common and Fabric modules | `1.20.1-1.1.4.2` | Alpha |
| 1.20.6 | Port the common module and Fabric build | `1.20.6-1.1.4.3` | Released |
| 1.20.6 | Add NeoForge loader with Fabric feature parity | `1.20.6-1.1.4.4` | Alpha or Beta |
| 1.21.1 | Fully tested target release | `1.21.1-1.2.0.0` | Stable |

Once the `1.21.1` line exists, create permanent `1.20.1` and `1.20.6` branches from the final commit of each Minecraft line for future maintenance fixes. Apply fixes on the relevant branch and forward-port them while they still apply.

## Workflow requirements

- Dry-run mode is enabled by default.
- A real release must run from `master`.
- A real release requires the **Confirm release** checkbox.
- Existing GitHub releases and tags are rejected instead of overwritten.
- The publishing token is provided by GitHub Actions; there is no personal token to configure.

## Testing for 1.20.1-1.1.4.2 alpha

The Fabric build and local `ALPHA` publishing dry run must pass. The common/Fabric split is the scope of this release; the second loader is deferred to NeoForge 1.20.6. Client gameplay, multiplayer behavior, existing-world migration, and optional storage-preview integrations remain unverified.

In-game screenshots of mixed structural builds would improve the README's schematic examples, but are optional for this release. A custom seated pose is deferred.

## Prepare a release

1. Update `mod_version` in `gradle.properties`.
2. Find `## [<mod_version>] - Unreleased` in `CHANGELOG.md` and replace `Unreleased` with the release date in `YYYY-MM-DD` format.
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
./gradlew clean publishMods -PdryRun=true -PreleaseType=BETA
```

## Publish

Once the release commit and dry run are both good:

1. Open **Actions → Release → Run workflow**.
2. Select the `master` branch.
3. Choose `ALPHA` for `1.20.1-1.1.4.2`.
4. Disable **Dry run**.
5. Enable **Confirm release**.
6. Run the workflow.
7. Verify the tag, release notes, and both attached JARs on GitHub Releases.

The workflow creates `v<mod_version>` from the exact `master` commit used by the run. `BETA` and `ALPHA` releases are marked as prereleases by the publishing plugin.

## If publishing fails

Before retrying, check GitHub Releases and repository tags for a partial result. If the plugin created a draft release but failed while uploading an asset, either finish that draft or remove it before retrying. The workflow will refuse to continue while the target tag or release already exists.

## Keep Modrinth (and CurseForge) in sync

Use `DESCRIPTION.md` for the Modrinth and CurseForge project descriptions. `README.md` remains the GitHub landing page. The GitHub Actions release workflow does not update either project page.

When you change player-facing project information:

1. Open the Modrinth project settings for Wooden Accents.
2. Paste the contents of `DESCRIPTION.md` into the project description.
3. Set the short summary to: `Vanilla-scale furniture and structural accents for every wood type.`
4. Repeat on CurseForge if that page is still maintained.

Links in `DESCRIPTION.md` use absolute URLs so they keep working when pasted onto Modrinth or CurseForge.

Do this for documentation-only description edits as well, not only full releases.
