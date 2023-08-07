package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.RecipeBlockData
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.TaggedBlock
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.WallBlock
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import java.util.function.Consumer

class CustomWallBlock(val baseBlock: Block) : WallBlock(FabricBlockSettings.copyOf(baseBlock)), GroupedBlock,
    RecipeBlockData, TaggedBlock {
    override val tag: TagKey<Block>
        get() = ModBlockTags.woodenWalls
    override val itemGroup get() = ModItemGroups.outsideBlockItemGroup
    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        FabricRecipeProvider.offerWallRecipe(exporter, RecipeCategory.DECORATIONS, this, baseBlock)
    }
}