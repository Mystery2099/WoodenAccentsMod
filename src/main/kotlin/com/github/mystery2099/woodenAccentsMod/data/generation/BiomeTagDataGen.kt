package com.github.mystery2099.woodenAccentsMod.data.generation

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.biome.Biome
import java.util.concurrent.CompletableFuture

class BiomeTagDataGen(
    output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricTagProvider<Biome>(output, RegistryKeys.BIOME, registriesFuture) {

    override fun configure(arg: RegistryWrapper.WrapperLookup) {
    }
}