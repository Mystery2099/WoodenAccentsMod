package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModItemTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Items
import java.util.concurrent.CompletableFuture

class ItemTagDataGen(
    output: FabricDataOutput?,
    completableFuture: CompletableFuture<HolderLookup.Provider>?
) : FabricTagProvider.ItemTagProvider(output, completableFuture, ModDataGenerator.blockTagGen) {
    override fun addTags(arg: HolderLookup.Provider) {
        ModBlockTags.blockToItemTagMap.forEach(::copy)

        getOrCreateTagBuilder(ModItemTags.unnestable).apply {
            addTag(requireNotNull(ModBlockTags.blockToItemTagMap[ModBlockTags.crates]))
            forceAddTag(ConventionalItemTags.SHULKER_BOXES)
            add(Items.BUNDLE)
        }
    }
}
