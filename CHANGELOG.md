# Changelog

This file tracks player-visible changes and anything maintainers need to know before publishing a release. Release headings must match `mod_version` in `gradle.properties` so the publishing workflow can use the section as its release notes.

## [1.19.4-1.1.4.0] - Unreleased

### Added

- Advancements for desks, desk drawers, kitchen counters, and kitchen cabinets.
- Player documentation for seating, named storage, portable crates, tall coffee tables, and bracket shelf loadouts.

### Changed

- Organized creative tabs into Wooden Accents: Furniture, Storage, and Building. Each family stays together, including tall coffee table variants in vanilla wood order.
- Renamed modern fences to picket fences, plank carpets to plank flooring, and thin bookshelves to narrow bookshelves. Existing block and item IDs are unchanged.
- Updated advancement descriptions to match the new names.

### Fixed

- Chairs lift riders slightly so their legs clear the seat's front edge.
- Chairs reject additional riders and clean up their seat entity when the chair breaks or the rider leaves.
- Kitchen cabinets preserve custom names on dropped items and mirror their facing correctly.
- Picking a crate preserves its custom display name along with its contents.
- Crates and desk drawers emit a container-close event when the last viewer closes them.
- Connecting ladders safely detach when their support is removed and connect immediately on placement.
- Tables initialize their connections on placement.

## [1.19.4-1.1.3.1] - 2026-09-14

### Fixed

- Bracket shelves now map stored items and powered hotbar loadouts from left to right when viewed from the front instead of reversing their order.
- Empty bracket shelves now clear their displayed items immediately after a powered hotbar swap.

[Full changelog](https://github.com/Mystery2099/WoodenAccentsMod/compare/v1.19.4-1.1.3.0...v1.19.4-1.1.3.1)

## [1.19.4-1.1.3.0] - 2026-09-14

### Added

- Bracket shelves for all 12 vanilla plank types. Shelves attach to any full square solid side, connect side-by-side with supports at each end and every three shelves, and can be placed in or out of water like other waterloggable blocks. Crafted with 3 planks over two sticks, yielding two shelves.
- Bracket shelves now store and display up to three full item stacks, one per third of the shelf face. Using a shelf swaps the pointed-at slot with the stack in the interacting hand, like vanilla wall shelves.
- When powered by redstone, interacting with a bracket shelf swaps its three slots (and those of any connected powered shelves facing the same way, up to three shelves in total) with the rightmost three, six, or all nine hotbar slots for instant loadout changes.
- A comparator placed behind a bracket shelf outputs a signal strength based on which slots are filled: 1 for the first slot, 2 for the second, 4 for the third, up to a maximum of 7.
- Hoppers can fill bracket shelves from above and empty them from below. Shelves scatter their stored items when broken or when their support block is removed.

[Full changelog](https://github.com/Mystery2099/WoodenAccentsMod/compare/v1.19.4-1.1.2.0...v1.19.4-1.1.3.0)

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
