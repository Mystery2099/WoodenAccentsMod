package com.github.mystery2099.woodenAccentsMod.data.generation

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.biome.Biome
import java.util.concurrent.CompletableFuture

/**
 * BiomeTagDataGen is a class responsible for generating data for biome tags.
 *
 * @param output The data output where the generated data will be written.
 * @param registriesFuture A CompletableFuture that provides access to the registries.
 */
class BiomeTagDataGen(
    output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricTagProvider<Biome>(output, RegistryKeys.BIOME, registriesFuture) {

    override fun configure(arg: RegistryWrapper.WrapperLookup) {
    }
}