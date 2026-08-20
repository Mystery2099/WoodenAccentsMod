# Wooden Accents Mod

Wooden Accents is a Fabric mod for Minecraft 1.19.4 that adds furniture, storage, and structural details for every vanilla wood type.

I like building with vanilla blocks, but sometimes a chair should look like a chair and a support beam should not need three layers of trapdoors. The goal of this mod is simple: give builders more options without deciding how those options have to be used.

[Download from Modrinth](https://modrinth.com/mod/wooden-accents-mod) · [Report a bug](https://github.com/Mystery2099/WoodenAccentsMod/issues) · [Read the changelog](CHANGELOG.md)

## What it adds

### Furniture and storage

- Chairs you can actually sit in
- Tables, desks, and desk drawers
- Coffee tables that stack into a taller version; Silk Touch preserves the taller block when it is picked up
- Kitchen counters and cabinets
- Crates that work like wooden shulker boxes and can be stacked
- Thin bookshelves based on vanilla chiseled bookshelves

### Building details

- Connecting stripped-wood ladders
- Plank ladders
- Plank carpets and walls
- Modern fences and fence gates
- Omnidirectional support beams
- Thin and thick connecting pillars

Every block has variants for the wood types available in Minecraft 1.19.4, including bamboo, cherry, crimson, and warped wood. Experimental wood types and thin bookshelves require the matching experimental features to be enabled in the world.

## Requirements

Wooden Accents currently targets **Minecraft 1.19.4** and **Fabric**. It is not compatible with other Minecraft versions.

You will need:

- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.3 or newer
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) 1.13.13 or newer
- [VoxLib](https://modrinth.com/mod/voxlib) 1.6.1 or newer for Minecraft 1.19.4
- Java 17 or newer

[Shulker Box Tooltip](https://modrinth.com/mod/shulkerboxtooltip) is optional. When installed, it can preview crate contents from the inventory.

## Installation

1. Install Fabric Loader for Minecraft 1.19.4.
2. Install the required dependencies listed above.
3. Download Wooden Accents from Modrinth or [GitHub Releases](https://github.com/Mystery2099/WoodenAccentsMod/releases).
4. Put the mod and its dependencies in your Minecraft `mods` directory.

If Minecraft reports a missing or incompatible dependency, use the version named in that error. Do not mix builds made for different Minecraft versions.

## Building from source

The Gradle wrapper handles the build tooling. Java 21 is recommended for development because that is what CI uses; compiled mod classes still target Java 17.

On Linux or macOS:

```bash
./gradlew build
```

On Windows:

```powershell
gradlew.bat build
```

Finished JARs are written to `build/libs/`. See [CONTRIBUTING.md](CONTRIBUTING.md) for development commands and project conventions.

## License

Wooden Accents is available under the [Minecraft Mod Public License 1.0.1](LICENSE).

## Support (totally optional)

This project is free, and it always will be. Nobody owes me anything for it.

If you somehow still want to tip, you can [buy me a coffee](https://buymeacoffee.com/mystery2099). No pressure at all.
