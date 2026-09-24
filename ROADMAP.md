# Wooden Accents roadmap

I want to keep the existing content working while moving Wooden Accents through newer Minecraft versions. These plans may change as I work on the mod.

## Minecraft 1.21.1 port

The current work ports the existing Fabric and NeoForge builds from 1.20.6 to 1.21.1 with the same content. Existing block and item IDs stay the same so old worlds can be tested without a content migration.

The first 1.21.1 build is `1.21.1-1.1.4.5` (alpha). The fully tested target on this line is `1.21.1-1.2.0.0`.

See the [changelog](CHANGELOG.md) for the changes and [release instructions](RELEASING.md) for the remaining checks. Larger performance changes can wait until profiling shows a need for them.

## Older Minecraft lines

1.20.1 and 1.20.6 builds stay available for older worlds. After the 1.21.1 line is in place, permanent maintenance branches should be cut from the final commit of each older line.

## Outdoor building ideas

A possible 1.2.0.0 update would add bridges and benches.

### Bridges

I'd like players to build bridges from sections that connect automatically, with railings along exposed edges. They should work with existing fences where possible and support waterlogging for docks and low walkways.

The old `BridgeBlock` class is an unfinished experiment. I'll start with an oak prototype before adding the other woods. Rope physics, generated bridges, and a separate railing family aren't planned.

### Benches

Benches would use the chair seating behavior and connect into longer seats with left, middle, and right sections. I'll start with one design before considering more styles.
