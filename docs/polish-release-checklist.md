# 1.19.4 polish release checklist

Target version: `1.19.4-1.1.4.0`. The release remains unpublished until the in-game checks below pass.

## Implemented

- [x] Furniture, Storage, and Building creative tabs, with families kept together and vanilla wood order within each finish.
- [x] Picket Fence, Plank Flooring, and Narrow Bookshelf display names, including advancement text. Registry IDs stay unchanged.
- [x] Advancements for desks, drawers, counters, and cabinets.
- [x] Cabinet custom-name drops and structure mirroring.
- [x] Crate pick-block names and correct crate/drawer close game events.
- [x] Occupied-chair checks and seat cleanup when its chair disappears.
- [x] Connecting ladder support-removal fix and initial connections for ladders and tables.
- [x] Player instructions for storage, seating, coffee tables, and bracket shelves.

## Automated verification

- Datagen and compilation run against Minecraft 1.19.4 and the pinned dependencies.
- A temporary datagen check exercised all 338 blocks and 18,636 states for outline/collision evaluation and waterlogged fluid output. It also passed 60 container name/inventory NBT round trips, creative-entry coverage, connecting ladder support removal, and cabinet mirroring. This did not simulate a live server or client.
- Resource checks cover every registered block's blockstate, item model, recipe, and loot table, positive recipe yields, and cabinet name-copy loot functions.
- No registry IDs, crafting ingredients, recipe yields, hardness, tool requirements, or block dimensions were intentionally changed.
- Existing screenshots still depict the same geometry. Gallery captions use the new names.

## In-game checks before publishing

Use a disposable world and a copy of an existing world. Repeat experimental-content checks with Update 1.20 enabled. Do not mark a check complete based only on compilation or source inspection.

| Family | Checks |
| --- | --- |
| Chairs | Sit facing each direction; sneak to dismount; block dismount spaces; two players attempt the same chair; break an occupied chair; disconnect/reconnect while seated. |
| Tables and coffee tables | Place rows, corners, and grids in different orders; remove interior blocks; stack matching coffee tables; check normal and Silk Touch drops. |
| Desks and drawers | Connect desks and drawers in rows and corners; try opposing facings; walk across the full collision surface. |
| Counters and cabinets | Place straight runs and corners; rotate/mirror a structure; open from each direction; walk across the countertop. |
| Crates, drawers, and cabinets | Rename, fill first/last slots, save/reload, break in survival and creative, and place again. Crates keep inventory; drawers/cabinets scatter it. Check hoppers, comparators, and two simultaneous viewers. |
| All ladder styles | Place on each wall direction, climb, connect stripped ladders, then remove supports. Repeat underwater. |
| Picket fences, gates, and plank walls | Connect same/mixed woods and vanilla neighbors; operate gates manually and with redstone; check collision and wall connections. |
| Support beams and pillars | Connect on every supported axis; remove neighbors; inspect isolated and connected collision. |
| Plank flooring | Place over supported blocks; remove support; check selection, footsteps, and drops. Flooring follows carpet behavior and is not waterloggable. |
| Narrow bookshelves | Insert/remove books from each slot and orientation; check comparator output, saved inventory, and breaking drops. |
| Bracket shelves | Test support removal, three displayed slots, hopper access, comparator values, and powered swaps across one, two, and three shelves. |

- [ ] Complete the family checks above on a client and a dedicated server.
- [ ] For every waterloggable family, place underwater, fill/drain with a bucket, update connections, break the block, and confirm the water remains correct.
- [ ] Check survival crafting, recipe unlocks, yields, axe mining, sounds, and drops for each family.
- [ ] Check all three creative tabs with experimental features both off and on, including tall coffee tables and bamboo mosaic flooring.
- [ ] Earn the four new advancements and check their descriptions.
- [ ] Confirm old blocks, inventories, custom names, and item data survive loading an existing world copy.
- [ ] Check optional storage previews with the supported integrations.
- [ ] Inspect the gallery against the final client build and replace any inaccurate screenshot.
- [ ] Replace the changelog's Unreleased date with the release date and follow [the release procedure](../RELEASING.md).
