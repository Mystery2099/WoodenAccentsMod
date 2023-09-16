package com.mystery2099.wooden_accents_mod.data.generation

import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModItemTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

/**
 * Item tag data gen
 *
 * @constructor
 *
 * @param output
 * @param completableFuture
 */
class ItemTagDataGen(
    output: FabricDataOutput?,
    completableFuture: CompletableFuture<RegistryWrapper.WrapperLookup>?
) : FabricTagProvider.ItemTagProvider(output, completableFuture, ModDataGenerator.blockTagGen) {
    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        ModBlockTags.blockToItemTagMap.forEach(::copy)

        getOrCreateTagBuilder(ModItemTags.uncrateable).apply {
            addTag(ModBlockTags.blockToItemTagMap[ModBlockTags.crates])
            forceAddTag(ConventionalItemTags.SHULKER_BOXES)
            add(Items.BUNDLE)
        }
    }
}