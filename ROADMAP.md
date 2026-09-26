# Wooden Accents roadmap

I want to keep the existing content working while moving Wooden Accents through newer Minecraft versions. These plans may change as I work on the mod.

## Minecraft 1.21.1

`1.21.1-1.2.0.0` is the first stable Fabric and NeoForge release on Minecraft 1.21.1. Existing block and item IDs stay the same so older worlds can keep using their builds without a content migration.

See the [changelog](CHANGELOG.md) for what shipped and [release instructions](RELEASING.md) for publishing. Larger performance changes can wait until profiling shows a need for them.

## Older Minecraft lines

1.20.1 and 1.20.6 builds stay available for older worlds. After this 1.21.1 line is published, permanent maintenance branches should be cut from the final commit of each older line.

## Outdoor building ideas

A later update could add bridges and benches. That content is not part of `1.21.1-1.2.0.0`.

### Bridges

I'd like players to build bridges from sections that connect automatically, with railings along exposed edges. They should work with existing fences where possible and support waterlogging for docks and low walkways.

The old `BridgeBlock` class is an unfinished experiment. I'll start with an oak prototype before adding the other woods. Rope physics, generated bridges, and a separate railing family aren't planned.

### Benches

Benches would use the chair seating behavior and connect into longer seats with left, middle, and right sections. I'll start with one design before considering more styles.
