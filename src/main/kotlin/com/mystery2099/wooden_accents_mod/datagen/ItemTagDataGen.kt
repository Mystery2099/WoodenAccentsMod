package com.mystery2099.wooden_accents_mod.datagen

import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class ItemTagDataGen(
    output: FabricDataOutput?,
    completableFuture: CompletableFuture<RegistryWrapper.WrapperLookup>?
) : FabricTagProvider.ItemTagProvider(output, completableFuture, ModDataGenerator.blockTagGen) {
    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        ModBlockTags.blockTagWithMatchingItemTag.forEach(::copy)
    }
}