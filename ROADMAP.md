# Wooden Accents roadmap

Wooden Accents currently supports Minecraft 1.19.4. I want to finish the existing content before adding another large set of blocks.

This is a rough release order, not a schedule. Plans may change as I work on the mod.

## Next release: polish and cleanup

The next 1.19.4 release will focus on making the current blocks more consistent and easier to use.

### Finish what is already here

- Test every block family and fix placement, connection, collision, and waterlogging problems.
- Make crates, desk drawers, and kitchen cabinets handle names and stored items consistently.
- Check chair seating and dismounting, including what happens when an occupied chair breaks.
- Review recipes, yields, tags, sounds, hardness, and tool requirements.

### Clean up names and creative tabs

- Replace unclear block names with names that better match their appearance and use.
- Rework the creative tabs so blocks are easier to find.
- Keep each block family together and sort wood variants in vanilla order.
- Keep existing block and item IDs so old worlds do not lose their blocks.

The current tab idea is:

- Wooden Accents: Furniture
- Wooden Accents: Storage
- Wooden Accents: Building

Some names still need work, especially the thin bookshelves, modern fences, plank carpets, support beams, and pillars.

### Finish progression and documentation

- Add advancements for desks, desk drawers, kitchen counters, and kitchen cabinets.
- Update advancement text after the naming pass.
- Document useful behavior that is easy to miss, such as portable crates and tall coffee tables.
- Replace screenshots when they no longer match the mod.

### Optimize as I go

Small, safe optimizations may be included when I am already working on the affected code. Larger optimization work will wait until profiling shows where it would matter.

This release will avoid a broad rewrite. The goal is to leave the 1.19.4 version complete and dependable.

## Considering: Minecraft 1.20.1

I am considering a 1.20.1 port after the polish release. This is not promised yet.

The first 1.20.1 build would contain the same blocks and behavior as the finished 1.19.4 version. It would not add a second wave of features during the port.

The port would include:

- Updating VoxLib for 1.20.1.
- Making cherry, bamboo, and chiseled-bookshelf content available without experimental features.
- Preserving block IDs, inventories, custom names, and item data.
- Checking storage preview integrations.
- Testing a copy of an existing 1.19.4 world before release.

If 1.20.1 becomes the main version, 1.19.4 would receive only small, important fixes.

## Possible 1.2.0.0 release: outdoor building

After the polish work and possible port, the next feature idea is a small outdoor building update.

### Bridges

- Modular bridge sections placed by the player.
- Automatic connections between sections.
- Side railings where the bridge edge is exposed.
- Connections to existing fences where they make sense.
- Waterlogging for docks and low walkways.

The old `BridgeBlock` class is only an unfinished experiment. The bridge design will start with a new oak prototype before expanding to every wood type.

### Benches

- Benches that use the existing chair seating behavior.
- Connected left, middle, and right sections.
- One finished design before considering more styles.

Rope physics, generated bridges, and a separate railing family are not planned for this release.

## Still deciding

- A better name for the thin bookshelves.
- The final creative tab names and contents.
- Whether modern fences should be called picket fences.
- Whether plank carpets should be called plank flooring or floorboards.
- Whether support beams and pillars need clearer names.
- Whether to begin the Minecraft 1.20.1 port after the polish release.
