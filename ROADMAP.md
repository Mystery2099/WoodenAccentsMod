# Wooden Accents roadmap

I want to keep the existing content working while moving Wooden Accents through newer Minecraft versions. These plans may change as I work on the mod.

## Minecraft 1.20.1 beta

The current beta ports the existing mod from 1.19.4 to 1.20.1. Existing block and item IDs stay the same so old worlds can be tested without a content migration.

Bamboo, cherry, and narrow bookshelf content no longer requires experimental feature flags. The beta needs testing with existing worlds, inventories, multiplayer, and optional storage-preview integrations before it can be considered stable.

See the [changelog](CHANGELOG.md) for the changes and [release instructions](RELEASING.md) for the remaining checks. Larger performance changes can wait until profiling shows a need for them.

## Minecraft 1.20.6 port

The next planned target after the 1.20.1 beta is Minecraft 1.20.6. It will start with the same content before I add another large set of blocks.

This port will need dependency updates and migration to Minecraft's data component system. It is not part of the 1.20.1 beta.

## Future loader support

I plan to investigate a multiloader setup after the version ports are in a good state. That conversion is future work and is not part of the 1.20.1 beta or the initial 1.20.6 port.

## Outdoor building ideas

A possible 1.2.0.0 update would add bridges and benches.

### Bridges

I'd like players to build bridges from sections that connect automatically, with railings along exposed edges. They should work with existing fences where possible and support waterlogging for docks and low walkways.

The old `BridgeBlock` class is an unfinished experiment. I'll start with an oak prototype before adding the other woods. Rope physics, generated bridges, and a separate railing family aren't planned.

### Benches

Benches would use the chair seating behavior and connect into longer seats with left, middle, and right sections. I'll start with one design before considering more styles.
