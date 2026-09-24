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
            addOptionalTag(ConventionalItemTags.SHULKER_BOXES.location)
            add(Items.SHULKER_BOX, Items.WHITE_SHULKER_BOX, Items.ORANGE_SHULKER_BOX,
                Items.MAGENTA_SHULKER_BOX, Items.LIGHT_BLUE_SHULKER_BOX, Items.YELLOW_SHULKER_BOX,
                Items.LIME_SHULKER_BOX, Items.PINK_SHULKER_BOX, Items.GRAY_SHULKER_BOX,
                Items.LIGHT_GRAY_SHULKER_BOX, Items.CYAN_SHULKER_BOX, Items.PURPLE_SHULKER_BOX,
                Items.BLUE_SHULKER_BOX, Items.BROWN_SHULKER_BOX, Items.GREEN_SHULKER_BOX,
                Items.RED_SHULKER_BOX, Items.BLACK_SHULKER_BOX)
            add(Items.BUNDLE)
        }
    }
}
