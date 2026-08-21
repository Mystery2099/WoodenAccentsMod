# Changelog

This file tracks player-visible changes and anything maintainers need to know before publishing a release. Release headings must match `mod_version` in `gradle.properties` so the publishing workflow can use the section as its release notes.

## [Unreleased]

## [1.19.4-1.1.2.0] - 2026-08-21

### Changed

- Reworked the project, contribution, and release documentation.
- Rewrote the player README for clearer features, dependencies, and FAQ, and mirrored Modrinth gallery screenshots under `docs/images/`.
- Precomputed reusable outline shapes for connecting and multipart blocks without changing their dimensions.
- Desks, desk drawers, kitchen counters, and kitchen cabinets now use full-block collision while retaining their detailed selection outlines. Isolated thick pillars use full-block collision and return to their detailed shape when connected.
- Raised the minimum supported versions to Fabric Loader 0.19.3, Fabric Language Kotlin 1.13.13+kotlin.2.4.10, and VoxLib 1.6.1+1.19.4.

### Fixed

- Desk drawers now drop their stored contents when broken and preserve custom names on the dropped block item.

[Full changelog](https://github.com/Mystery2099/WoodenAccentsMod/compare/v1.19.4-1.1.1.1...v1.19.4-1.1.2.0)

## [1.19.4-1.1.1.1] - 2024-05-20

Simplified outline shapes for:

- Plank ladders
- Desk drawers
- Modern fences
- Modern fence gates
- Thin pillars

[Full changelog](https://github.com/Mystery2099/WoodenAccentsMod/compare/v1.19.4-1.1.1.0...v1.19.4-1.1.1.1)

## [1.19.4-1.1.1.0] - 2024-05-20

Implemented an advancement provider and generated new advancements.

[Full changelog](https://github.com/Mystery2099/WoodenAccentsMod/compare/v1.1+1.19.4...v1.19.4-1.1.1.0)

## [1.1+1.19.4] - 2024-03-15

- Updated Fabric Loader from 0.14.23 to 0.15.7.
- Updated Fabric Language Kotlin from 1.10.14+kotlin.1.9.20 to 1.10.19+kotlin.1.9.23.
- Updated Kotlin from 1.9.20 to 1.9.23.
- Updated the mod version from 1.0.0+1.19.4 to 1.1+1.19.4.
- Re-added thin pillar blocks.
- Re-added thick pillar blocks.
- Added data files providing compatibility with Easy Shulker Boxes for all storage blocks in this mod.

## [1.0.0+1.19.4] - 2023-12-03

Initial release.

Wooden Accents Mod adds wooden furniture, walls, pillars, crates, and other decorative blocks for builders.

Added wooden variants of:

- Chairs
- Tables
- Coffee tables
- Stripped wood ladders
- Plank ladders
- Crates
- Plank carpets
- Plank walls
- Desks
- Desk drawers
- Kitchen counters
- Kitchen cabinets
- Modern fences
- Modern fence gates
- Supports
- Thin pillars
- Thick pillars
- Thin bookshelves

Each block is available for every wood type in Minecraft 1.19.4, including wood types behind experimental features.
