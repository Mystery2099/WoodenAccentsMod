# Wooden Accents roadmap

I want to finish the existing 1.19.4 content before adding more blocks. These plans may change as I work on the mod.

## Polish release

The current release work covers block connections, placement, storage, and seating fixes, along with clearer names, creative tabs, and advancements. Existing block and item IDs stay the same so old worlds keep working.

The renamed blocks are Picket Fence, Plank Flooring, and Narrow Bookshelf. Support Beam and the thin/thick pillar names stay as they are. The creative tabs are Furniture, Storage, and Building.

See the [changelog](CHANGELOG.md) for the changes and [release instructions](RELEASING.md) for the remaining checks. Larger performance changes can wait until profiling shows a need for them.

## Possible Minecraft 1.21.1 port

I'm considering a 1.21.1 port after the polish release. It would start with the same content as 1.19.4 before I add another big wave of features.

This needs a VoxLib update and testing of existing worlds, inventories, and storage-preview integrations. Cherry, bamboo, and chiseled-bookshelf content would no longer need experimental features. The newer data component system is one reason I'm leaning toward 1.21.1 instead of 1.20.1.

If 1.21.1 becomes the main version, I'd keep 1.19.4 updates to important fixes.

## Outdoor building ideas

A possible 1.2.0.0 update would add bridges and benches.

### Bridges

I'd like players to build bridges from sections that connect automatically, with railings along exposed edges. They should work with existing fences where possible and support waterlogging for docks and low walkways.

The old `BridgeBlock` class is an unfinished experiment. I'll start with an oak prototype before adding the other woods. Rope physics, generated bridges, and a separate railing family aren't planned.

### Benches

Benches would use the chair seating behavior and connect into longer seats with left, middle, and right sections. I'll start with one design before considering more styles.
